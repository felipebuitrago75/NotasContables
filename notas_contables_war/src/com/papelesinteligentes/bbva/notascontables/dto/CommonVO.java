package com.papelesinteligentes.bbva.notascontables.dto;

import java.io.Serializable;

public abstract class CommonVO<T> implements Serializable {

	private static final long serialVersionUID = 7042737325200423514L;

	public abstract Object getPK();

	/**
	 * @param pk
	 */
	public void restartPK(Object pk) {

	}

	public String getEstado() throws Exception {
		throw new Exception("M�todo no implementado");
	}

	public void setEstado(@SuppressWarnings("unused") String estado) throws Exception {
		throw new Exception("M�todo no implementado");
	}
}
