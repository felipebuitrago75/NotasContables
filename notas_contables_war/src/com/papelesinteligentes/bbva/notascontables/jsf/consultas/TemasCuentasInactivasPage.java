package com.papelesinteligentes.bbva.notascontables.jsf.consultas;

import java.util.ArrayList;
import java.util.Collection;

import org.ajax4jsf.model.KeepAlive;

import com.papelesinteligentes.bbva.notascontables.dto.Tema;

@KeepAlive
public class TemasCuentasInactivasPage extends GeneralConsultaPage<Tema> {

	private static final long serialVersionUID = -6709113217662690209L;

	public TemasCuentasInactivasPage() {
		super();
	}

	@Override
	protected void _init() {
		super._init();
		try {
			if (getDatos() == null || getDatos().isEmpty()) {
				setDatos(notasContablesManager.getTemasCuentasInactivas());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected Collection<Tema> _buscar() throws Exception {
		return new ArrayList<Tema>();
	}

	@Override
	protected void _validar() throws Exception {
	}

	@Override
	protected String _getPage() {
		return TEMAS_INACTIVOS;
	}

}
