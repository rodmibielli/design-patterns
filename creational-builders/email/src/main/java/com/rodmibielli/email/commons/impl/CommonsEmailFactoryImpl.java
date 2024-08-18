package com.rodmibielli.email.commons.impl;

import com.rodmibielli.email.api.Email;
import com.rodmibielli.email.api.EmailFactory;
import com.rodmibielli.email.api.Translator;

/**
 * 
 */
public class CommonsEmailFactoryImpl implements EmailFactory<org.apache.commons.mail.Email> {

	@Override
	public Runnable createEmailRunnable(Email email) {
		return new CommonsEmailServiceImpl(email,this);
	}

	@Override
	public Translator<org.apache.commons.mail.Email> createTranslator() {
		return new CommonsEmailTranslatorImpl();
	}

}
