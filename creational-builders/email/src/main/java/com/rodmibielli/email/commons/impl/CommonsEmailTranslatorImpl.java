package com.rodmibielli.email.commons.impl;

import org.apache.commons.mail.Email;
import org.apache.commons.mail.HtmlEmail;
import org.apache.commons.mail.SimpleEmail;

import com.rodmibielli.email.api.Translator;

public class CommonsEmailTranslatorImpl implements Translator<Email> {
	
	@Override
	public Email translate(com.rodmibielli.email.api.Email email) {
		
		try {
				Email commonsEmail =  email.isHtml() ? new HtmlEmail() : new SimpleEmail();
				
				commonsEmail.setHostName(email.getHostname());
				commonsEmail.setSmtpPort(email.getSmtpPort());
				
				commonsEmail.setFrom(email.getFrom());
				if (email.getTos()!=null) {
					commonsEmail.addTo(email.getTos());
				}
				if (email.getCcs()!=null) {
					commonsEmail.addCc(email.getCcs());
				}
				if (email.getBccs()!=null) {
					commonsEmail.addBcc(email.getBccs());
				}
				commonsEmail.setSubject(email.getSubject());
				commonsEmail.setContent(email.getContent());
				
				return commonsEmail;
				
		} catch (Exception e) {
			
			e.printStackTrace();
			
			throw new RuntimeException(e);
		}
	}

}
