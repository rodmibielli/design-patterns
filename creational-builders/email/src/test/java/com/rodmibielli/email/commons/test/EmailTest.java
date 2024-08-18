package com.rodmibielli.email.commons.test;

import java.util.Hashtable;

import javax.naming.Context;

import org.junit.jupiter.api.Test;

import com.rodmibielli.email.api.Email;
import com.rodmibielli.email.api.EmailFactory;
import com.rodmibielli.email.commons.impl.CommonsEmailFactoryImpl;

public class EmailTest {

	@Test
	void testeEnvioEmail() {
		
		Email email = Email.Builder.fromSender("rodrigo.mibielli@gmail.com")
					  			   .bcc("rodrigo.mibielli@gmail.com")
					  			   .subject("Teste")
					  			   .content("Ola!")
					  			   .build();
		
		email.send();
	}

}
