package com.papelesinteligentes.bbva.notascontables.jsf.parametros;

import javax.faces.bean.ViewScoped;

/**
 * <p>
 * Pagina para manejar la administración de parametros relacionados con la entidad PUC
 * </p>
 * 
 */
@ViewScoped
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