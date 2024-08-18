package com.rodmibielli.email.api;

import com.rodmibielli.email.api.Email.Builder;

/**
 * Factory of email types.
 */
public interface EmailFactory<T> {
	
	Runnable createEmailRunnable(Email email);
	
	Translator<T> createTranslator();
}
