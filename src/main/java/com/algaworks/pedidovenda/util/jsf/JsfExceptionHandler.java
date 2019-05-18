package com.algaworks.pedidovenda.util.jsf;

import java.io.IOException;
import java.util.Iterator;

import javax.faces.FacesException;
import javax.faces.application.ViewExpiredException;
import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerWrapper;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ExceptionQueuedEvent;
import javax.faces.event.ExceptionQueuedEventContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.algaworks.pedidovenda.service.NegocioException;

/*
 * Essa classe empacota o tratador de exceção do JSF. Ou seja, ela adiciona uma camada acima desse tratador de exceções do JSF. 
 * Ele não deixa de existir, só criamos um tratador de exceções uma camada acima da do JSF. Sendo assim, o nosso tem prioridade. 
 * O que não formos tratar aqui, repassamos a responsabilidade para o tratador do JSF. 
 * Só trataremos a exceção de ViewExpiredException.
 * */

public class JsfExceptionHandler extends ExceptionHandlerWrapper {

	private static Log log = LogFactory.getLog(JsfExceptionHandler.class);
	
	private ExceptionHandler wrapped;

	public JsfExceptionHandler(ExceptionHandler wrapped) {
		this.wrapped = wrapped;
	}

	// Retorna o tratador de exceção empacotado
	@Override
	public ExceptionHandler getWrapped() {
		return this.wrapped;
	}

	// Método que é chamado quando ocorrer alguma exceção.
	@Override
	public void handle() throws FacesException {
		// Iterador de ExceptionQueuedEvent. Contém todos os eventos de exceções
		// enfileirados.
		Iterator<ExceptionQueuedEvent> events = getUnhandledExceptionQueuedEvents().iterator();

		while (events.hasNext()) {
			// Pega o evento de exceção
			ExceptionQueuedEvent event = events.next();

			// Pega a origem da exceção. O evento da exceção contextualizado.
			ExceptionQueuedEventContext context = (ExceptionQueuedEventContext) event.getSource();

			// Pega a exceção propriamente dita
			Throwable exception = context.getException();
			NegocioException negocioException = getNegocioException(exception);

			boolean handled = false;

			try {

				// Tratamos a exceção, se a mesma for uma instancia de
				// ViewExpiredException
				if (exception instanceof ViewExpiredException) {
					redirect("/");
					handled = true;
				} else if (negocioException != null) {
					// Para erros de negócio.
					FacesUtil.addErrorMessage(negocioException.getMessage());
					handled = true;
				} else {
					// Para erros desconhecidos
					handled = true;
					
					//Armazena os erros inesperados no arquivo de log
					log.error("Erro de sistema " + exception.getMessage(), exception);
					
					redirect("/Erro.xhtml");
				}

			} finally {
				if (handled) {
					// remove a exceção que foi tratada/relançada da fila de
					// exceções, para não ser tratada 2x.
					events.remove();
				}

			}

			/*
			 * Informa para a classe que estamos encapsulando/empacotando que o
			 * trabalho feito por essa classe está pronto, tratar
			 * ViewExpiredException, o resto é com ela.
			 */
			getWrapped().handle();

		}
	}

	private void redirect(String page) {
		// Trata a exceção relançando outro tipo. Relançando uma FacesException,
		// passando uma IOException como a causa do problema.
		try {
			FacesContext facesContext = FacesContext.getCurrentInstance();
			ExternalContext externalContext = facesContext.getExternalContext();
			String contextPath = externalContext.getRequestContextPath();

			externalContext.redirect(contextPath + page);

			// Indica que o processamento está completo para assim evitar
			// qualquer outro processamento no ciclo de vida do jsf.
			facesContext.responseComplete();

		} catch (IOException e) {
			throw new FacesException(e);
		}
	}

	private NegocioException getNegocioException(Throwable exception) {
		if (exception instanceof NegocioException) {
			return (NegocioException) exception;
		} else if (exception.getCause() != null) {
			return getNegocioException(exception.getCause());
		}

		return null;

	}

}
