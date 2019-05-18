package com.algaworks.pedidovenda.repository;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.algaworks.pedidovenda.model.Produto;
import com.algaworks.pedidovenda.repository.filter.ProdutoFilter;
import com.algaworks.pedidovenda.service.NegocioException;
import com.algaworks.pedidovenda.util.jpa.Transactional;

public class Produtos implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject // O CDI busca um produtor que tenha um EntityManager
	private EntityManager manager;

	// Salva ou atualiza um produto na tabela de dados
	public Produto guardar(Produto produto) {

		return manager.merge(produto);
	} 
	
	/*
	 * Como não temos regras de negócio, chamaremos esse método direto na bean.
	 * Sem utilizar o Service.
	*/
	@Transactional
	public void remover(Produto produto){
		
		
		try {

			/*
			 * Buscando o objeto no banco de dados temos certeza que ele existe 
			 * e o mesmo fica em um estado de conectado, com isso, podemos manipular
			 *  esse objeto como quisermos que essa mudança será refletida no BD 
			 * */ 
			produto = porId(produto.getId());
			
			
			/*
			 * Chamando o método remove, o objeto não é removido de imediato, mas sim
			 * marcado para exclusão. A exclusão só se da no momento de commit da 
			 * transação ou quando damos um flush() do EntityManager, ou ainda, um 
			 * flush automático pela própria implementação do JPA, que no nosso 
			 * caso é o Hibernate.
			 * */
			manager.remove(produto);
			
			
			/*
			 * Chamamos o flush aqui para que tudo que estiver pendente de execução
			 * no BD, no nosso caso, a remoção do Produto, seja executado.
			 * Fazemos isso, pois se esse produto estiver sendo usado por alguma 
			 * outra tabela no BD, lance a exceção dentro DESTE método, para que
			 * a tratemos aqui.
			 * */
			manager.flush();
			
		} catch (PersistenceException e) {
			throw new NegocioException("Produto não pode ser excluído!");
		}
	}

	public Produto porSku(String sku) {
		try {
			return manager.createQuery("from Produto where upper(sku) = :sku", Produto.class)
					.setParameter("sku", sku.toUpperCase()).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public List<Produto> filtrados(ProdutoFilter filtro) {
		Session session = manager.unwrap(Session.class);
		Criteria criteria = session.createCriteria(Produto.class);

		if (StringUtils.isNotBlank(filtro.getSku())) {
			criteria.add(Restrictions.eq("sku", filtro.getSku()));
		}

		if (StringUtils.isNotBlank(filtro.getNome())) {
			criteria.add(Restrictions.ilike("nome", filtro.getNome(), MatchMode.ANYWHERE));
		}

		return criteria.addOrder(Order.asc("nome")).list();
	}

	public Produto porId(Long id) {
		return manager.find(Produto.class, id);
	}

	public List<Produto> porNome(String nome) {
		return this.manager.createQuery("from Produto where upper(nome) like :nome", Produto.class)
				.setParameter("nome", nome.toUpperCase() + "%").getResultList();
	}

}
