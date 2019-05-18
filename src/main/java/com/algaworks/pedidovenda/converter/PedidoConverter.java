package com.algaworks.pedidovenda.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import org.apache.commons.lang3.StringUtils;

import com.algaworks.pedidovenda.model.Pedido;
import com.algaworks.pedidovenda.repository.Pedidos;
import com.algaworks.pedidovenda.util.cdi.CDIServiceLocator;

@FacesConverter(forClass=Pedido.class)
public class PedidoConverter implements Converter{

	// @Inject: Conversores ainda não aceitam injecao de dependencias
		private Pedidos pedidos;

		public PedidoConverter() {
			// para resolver o problema da injecao de dependencias
			pedidos = CDIServiceLocator.getBean(Pedidos.class);
		}

		@Override
		public Object getAsObject(FacesContext context, UIComponent component, String value) {
			Pedido retorno = null;

			if (StringUtils.isNotEmpty(value)) {
				Long id = new Long(value);
				retorno = pedidos.porId(id);
			}

			return retorno;
		}

		@Override
		public String getAsString(FacesContext context, UIComponent component, Object value) {
			if (value != null) {
				Pedido pedido = (Pedido) value;
				return pedido.getId() == null ? null : pedido.getId().toString();
			}

			return "";
		}

}
