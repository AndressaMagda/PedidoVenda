package com.algaworks.pedidovenda.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import com.algaworks.pedidovenda.model.Cliente;
import com.algaworks.pedidovenda.repository.Clientes;
import com.algaworks.pedidovenda.util.cdi.CDIServiceLocator;

@FacesConverter(forClass = Cliente.class)
public class ClienteConverter implements Converter {

	private Clientes clientes;

	public ClienteConverter() {
		// para resolver o problema da injecao de dependencias
		this.clientes = CDIServiceLocator.getBean(Clientes.class);
	}

	@Override
	public Object getAsObject(FacesContext ctx, UIComponent component, String value) {

		Cliente retorno = null;

		if (value != null) {
			Long id = new Long(value);
			retorno = clientes.porId(id);
		}

		return retorno;
	}

	@Override
	public String getAsString(FacesContext ctx, UIComponent component, Object value) {

		if (value != null) {
			return ((Cliente) value).getId().toString();
		}

		return "";
	}

}
