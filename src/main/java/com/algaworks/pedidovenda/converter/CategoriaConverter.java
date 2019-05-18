package com.algaworks.pedidovenda.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import com.algaworks.pedidovenda.model.Categoria;
import com.algaworks.pedidovenda.repository.Categorias;
import com.algaworks.pedidovenda.util.cdi.CDIServiceLocator;

@FacesConverter(forClass = Categoria.class)
public class CategoriaConverter implements Converter {

	// @Inject: Conversores ainda não aceitam injecao de dependencias
	private Categorias categorias;

	public CategoriaConverter() {
		// para resolver o problema da injecao de dependencias
		categorias = CDIServiceLocator.getBean(Categorias.class);
	}

	@Override
	public Object getAsObject(FacesContext ctx, UIComponent component, String value) {

		Categoria retorno = null;

		if (value != null) {
			Long id = new Long(value);
			retorno = categorias.porId(id);
		}

		return retorno;
	}

	@Override
	public String getAsString(FacesContext ctx, UIComponent component, Object value) {

		if (value != null) {
			return ((Categoria) value).getId().toString();
		}

		return "";
	}

}
