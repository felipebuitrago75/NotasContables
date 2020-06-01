package com.papelesinteligentes.bbva.notascontables.jsf.parametros;

import org.ajax4jsf.model.KeepAlive;

/**
 * <p>
 * Pagina para manejar la administración de parametros relacionados con la entidad PUC
 * </p>
 * 
 */
@KeepAlive
public class CentroDestinoPage extends GeneralCentroPage {

	private static final long serialVersionUID = 1L;

	public CentroDestinoPage() {
		super(false);
	}

	@Override
	protected String _getPage() {
		return CENTRO_DESTINO;
	}
}