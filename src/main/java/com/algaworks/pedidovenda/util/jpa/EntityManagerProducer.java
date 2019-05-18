package com.algaworks.pedidovenda.util.jpa;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.hibernate.Session;

@ApplicationScoped
public class EntityManagerProducer {

	private EntityManagerFactory factory;

	public EntityManagerProducer() {
		this.factory = Persistence.createEntityManagerFactory("PedidoPU");
	} 

	// Produz um EntityManager a cada requisição
	@Produces
	@RequestScoped
	public Session createEntityManager() {
		return (Session) factory.createEntityManager();
	}

	// @Disposes: indica que cada vez que a requisição acabar, o EntityManager
	// deve ser fechado
	public void closeEntityManager(@Disposes Session manager) {
		manager.close();
	}
}
