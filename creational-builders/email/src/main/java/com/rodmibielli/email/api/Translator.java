package com.rodmibielli.email.api;

public interface Translator<T> {
	
	T translate(Email email);
}
