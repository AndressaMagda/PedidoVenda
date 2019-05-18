package com.algaworks.pedidovenda.util;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;



@WebListener
public class AppContextListener implements ServletContextListener {

	@Override
	public void contextDestroyed(ServletContextEvent event) {
	}
	
	/*
	 * Quando o deploy da aplicação for feita pelo servidor, Tomcat, esse método será chamado.
	 * */
	@Override
	public void contextInitialized(ServletContextEvent event) {
		// Essa propriedade impede que tipos inteiros  não inicializados sejam setados com zero
		System.setProperty("org.apache.el.parser.COERCE_TO_ZERO", "false");
	}

}
