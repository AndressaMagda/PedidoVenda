package com.algaworks.pedidovenda.service;

import java.io.Serializable;

import javax.inject.Inject;

import com.algaworks.pedidovenda.model.Pedido;
import com.algaworks.pedidovenda.model.StatusPedido;
import com.algaworks.pedidovenda.repository.Pedidos;
import com.algaworks.pedidovenda.util.jpa.Transactional;

public class CancelamentoPedidoService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private Pedidos pedidos;

	@Inject
	private EstoqueService estoqueService;

	@Transactional
	public Pedido cancelar(Pedido pedido) {
		// Pega o pedido armazenado no BD e o deixa em um estado de pronto
		pedido = this.pedidos.porId(pedido.getId());

		if (pedido.isNaoCancelavel()) {
			throw new NegocioException(
					"Pedido não pode ser cancelado no status " + pedido.getStatus().getDescricao() + ".");
		}

		if (pedido.isEmitido()) {
			// Só é feito a baixa no estoque após a emissão do pedido
			this.estoqueService.retornarItensEstoque(pedido);
		}

		pedido.setStatus(StatusPedido.CANCELADO);

		pedido = this.pedidos.guardar(pedido);

		return pedido;

	}

}
