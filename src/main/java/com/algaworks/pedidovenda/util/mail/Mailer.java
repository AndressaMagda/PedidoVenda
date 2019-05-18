package com.algaworks.pedidovenda.util.mail;

import java.io.Serializable;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import com.outjected.email.api.MailMessage;
import com.outjected.email.api.SessionConfig;
import com.outjected.email.impl.MailMessageImpl;

@RequestScoped
public class Mailer implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/*
	 * Configuração da sessão de envio de email (sessão configurada de servidor, smtp, usuario, senha...).
	 * 
	 * Contudo, a biblioteca Simple-Email não é uma biblioteca CDI. Com isso, precisamos de um produtor para
	 * o SessionConfig e podermos realmente injeta-lo no bean.
	 * 
	 * */
	
	@Inject
	private SessionConfig config;

	public MailMessage novaMensagem() {
		return new MailMessageImpl(this.config);
	}
}
