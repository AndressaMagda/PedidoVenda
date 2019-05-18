package com.algaworks.pedidovenda.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import com.algaworks.pedidovenda.model.Usuario;
import com.algaworks.pedidovenda.repository.Usuarios;
import com.algaworks.pedidovenda.util.cdi.CDIServiceLocator;

@FacesConverter(forClass = Usuario.class)
public class UsuarioConverter implements Converter {

	private Usuarios usuarios;

	public UsuarioConverter() {
		// para resolver o problema da injecao de dependencias
		this.usuarios = CDIServiceLocator.getBean(Usuarios.class);
	}

	@Override
	public Object getAsObject(FacesContext ctx, UIComponent component, String value) {

		Usuario retorno = null;

		if (value != null) {
			Long id = new Long(value);
			retorno = this.usuarios.porId(id);
		}

		return retorno;
	}

	@Override
	public String getAsString(FacesContext ctx, UIComponent component, Object value) {

		if (value != null) {
			return ((Usuario) value).getId().toString();
		}

		return "";
	}

}
