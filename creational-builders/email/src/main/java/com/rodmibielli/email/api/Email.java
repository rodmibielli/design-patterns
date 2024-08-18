package com.rodmibielli.email.api;

import java.io.File;
import java.util.Arrays;
import java.util.Properties;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Abstract entity for e-mail.
 */
public class Email {

	private final String hostname;
	private final int smtpPort;
	private final boolean html;
	private final String from;
	private final String[] tos;
	private final String[] ccs;
	private final String[] bccs;
	private final String subject;
	private final Object content;
	private final File[] attachments;
	private final Runnable runnable;
	
	public boolean isHtml() {
		return html;
	}

	public String getFrom() {
		return from;
	}
	
	public String[] getTos() {
		return tos;
	}

	public String[] getCcs() {
		return ccs;
	}

	public String[] getBccs() {
		return bccs;
	}

	public String getSubject() {
		return subject;
	}

	public Object getContent() {
		return content;
	}
	
	public File[] getAttachments() {
		return attachments;
	}
	
	public String getHostname() {
		return hostname;
	}

	public int getSmtpPort() {
		return smtpPort;
	}

	public interface Sender {
		Build from(String from);
	}
	
	public interface Build {
		Build to(String to);
		Build cc(String cc);
		Build bcc(String bcc);
		Build subject(String subject);
		Build content(String content);
		Build attachment(File file);
		Build isHTML();
		Email build();
	}
	
	public static class Builder implements Sender, Build {
		
		private final EmailFactory<?> emailFactory; 
		private boolean html;
		private String hostname;
		private int smtpPort;
		private String from;
		private SortedSet<String> tos;
		private SortedSet<String> ccs;
		private SortedSet<String> bccs;
		private String subject;
		private String content;
		private SortedSet<File> attachaments;
		
		private Builder(final EmailFactory<?> emailFactory,final String hostname,final int smtpPort) {
			this.emailFactory = emailFactory;
			this.hostname = hostname;
			this.smtpPort = smtpPort;
		}
		
		public static Build fromSender(String from) {
			try {
					Properties props = new Properties();
					props.load(Builder.class.getResourceAsStream("/mail.properties"));
					EmailFactory<?> emailFactory = (EmailFactory<?>) Class.forName(props.getProperty("factory-class")).newInstance();
					String hostname = props.getProperty("smtp-server-hostname");
					int smtpPort = Integer.valueOf(props.getProperty("smtp-server-port"));
					return of(emailFactory,hostname,smtpPort)
							.from(from);
					
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
			
		}
		
		public static Sender of(final EmailFactory<?> emailFactory,final String hostname,final int smtpPort) {
			return new Builder(emailFactory,hostname,smtpPort);
		}
		
		@Override
		public Builder from(String from) {
			this.from = from;
			return this;
		}
		
		@Override
		public Build to(String to) {
			
			if (to!=null) {
				
				if (this.tos == null) {
					this.tos = new TreeSet<>();
				}
				
				this.tos.addAll( Arrays.asList(to.split("[,;\\s]")));
			}
			
			return this;
		}
		
		@Override
		public Build cc(String cc) {
			
			if (cc!=null) {
				
				if (this.ccs == null) {
					this.ccs = new TreeSet<>();
				}
				
				this.ccs.addAll( Arrays.asList(cc.split("[,;\\s]")));
			}
			
			return this;
		}

		@Override
		public Build bcc(String bcc) {
			
			if (bcc!=null) {
				
				if (this.bccs == null) {
					this.bccs = new TreeSet<>();
				}
				
				this.bccs.addAll( Arrays.asList(bcc.split("[,;\\s]")));
			}
			
			return this;
		}
		
		@Override
		public Build subject(String subject) {
			this.subject = subject;
			return this;
		}

		@Override
		public Build content(String content) {
			this.content = content;
			return this;
		}
		
		@Override
		public Build attachment(File file) {
			if (file!=null) {
				
				if (this.attachaments == null) {
					this.attachaments = new TreeSet<>();
				}
				
				this.attachaments.add(file);
			}
			
			return this;
		}
		
		@Override
		public Build isHTML() {
			this.html = true;
			return this;
		}
		
		@Override
		public Email build() {
			return new Email(
						     this.hostname,
						     this.smtpPort,
					         this.html,
					         this.from, 
							 this.tos!=null  ? this.tos.toArray(new String[this.tos.size()])   : null, 
							 this.ccs!=null  ? this.ccs.toArray(new String[this.ccs.size()])   : null, 
							 this.bccs!=null ? this.bccs.toArray(new String[this.bccs.size()]) : null,
							 this.subject, 
							 this.content,
							 this.attachaments!=null ? this.attachaments.toArray(new File[this.attachaments.size()]) : null,
							 this.emailFactory);
		}
		
	}
	
	private Email(
				  final String hostname,
				  final int smtpPort,
				  final boolean html, 
				  final String from,
				  final String[] tos,
				  final String[] ccs,
				  final String[] bccs,
				  final String subject,
				  final String content,
				  final File[] attachments,
				  final EmailFactory<?> emailFactory) {
		
		this.hostname = hostname;
		
		if (this.hostname==null || this.hostname.trim().length()==0) {
			throw new IllegalArgumentException("Mail hostname is required!");
		}
		
		this.smtpPort = smtpPort;
		
		this.html = html;
		
		this.from = from!=null && from.trim().length()>0 ? from.trim() : null;
		
		if (this.from==null) {
			throw new IllegalArgumentException("From argument is required!");
		}
		
		this.tos = tos!=null && tos.length>0 ? tos : null;
		this.ccs = ccs!=null && ccs.length>0 ? ccs : null;
		this.bccs = bccs!=null && bccs.length>0 ? bccs : null;
		
		if (this.tos==null && this.ccs==null && this.bccs==null) {
			throw new IllegalArgumentException("You must provide at least one recipient!");
		}
		
		this.subject = subject!=null && subject.trim().length()>0 ? subject.trim() : null;
		
		if (subject==null) {
			throw new IllegalArgumentException("Either subject or content are required!");
		}
		
		this.content = content!=null && content.trim().length()>0 ? content.trim() : null;
		
		this.attachments = attachments!=null && attachments.length>0 ? attachments : null;
		
		this.runnable = emailFactory.createEmailRunnable(this);
	}
	
	public void send() {
		runnable.run();
	}
	
}
