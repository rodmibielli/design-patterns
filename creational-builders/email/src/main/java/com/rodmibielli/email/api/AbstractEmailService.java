package com.rodmibielli.email.api;

public abstract class AbstractEmailService<T> implements Runnable {

	private final Translator<T> translator;
	private final Email email;
	
	protected AbstractEmailService(Email email, EmailFactory<T> factory) {
		this.email = email;
		this.translator = factory.createTranslator();
	}
	
	@Override
	public void run() {
		T emailTranslated = this.translator.translate(this.email);
		doSend(emailTranslated);
	}
	
	protected abstract void doSend(T email);
	
}
