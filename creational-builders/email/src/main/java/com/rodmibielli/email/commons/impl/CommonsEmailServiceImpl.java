package com.rodmibielli.email.commons.impl;

import com.rodmibielli.email.api.AbstractEmailService;
import com.rodmibielli.email.api.Email;
import com.rodmibielli.email.api.EmailException;
import com.rodmibielli.email.api.EmailFactory;

/**
 * Strategy that user Commons E-mail for implementation.
 */
public class CommonsEmailServiceImpl extends AbstractEmailService<org.apache.commons.mail.Email> { 
	
	public CommonsEmailServiceImpl(Email email,EmailFactory<org.apache.commons.mail.Email> factory) {
		super(email,factory);
	}
	
	@Override
	protected void doSend(org.apache.commons.mail.Email email) {
		try {
			email.send();
		} catch (org.apache.commons.mail.EmailException e) {
			throw new EmailException(e);
		}
	}

}
