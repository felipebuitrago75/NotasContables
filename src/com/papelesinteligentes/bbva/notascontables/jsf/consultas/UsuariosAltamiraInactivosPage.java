package com.papelesinteligentes.bbva.notascontables.jsf.consultas;

import java.util.ArrayList;
import java.util.Collection;

import org.ajax4jsf.model.KeepAlive;

import com.papelesinteligentes.bbva.notascontables.dto.UsuarioModulo;

@KeepAlive
public class UsuariosAltamiraInactivosPage extends GeneralConsultaPage<UsuarioModulo> {

	private static final long serialVersionUID = -6709113217662690209L;

	public UsuariosAltamiraInactivosPage() {
		super();
	}

	@Override
	protected void _init() {
		super._init();
		try {
			if (getDatos() == null || getDatos().isEmpty()) {
				setDatos(notasContablesManager.getUsuariosAltamiraInactivos());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected Collection<UsuarioModulo> _buscar() throws Exception {
		return new ArrayList<UsuarioModulo>();
	}

	@Override
	protected void _validar() throws Exception {
	}

	@Override
	protected String _getPage() {
		return USUARIOS_INACTIVOS;
	}

}
