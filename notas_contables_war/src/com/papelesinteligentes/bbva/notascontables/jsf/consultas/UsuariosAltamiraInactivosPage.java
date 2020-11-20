package com.papelesinteligentes.bbva.notascontables.jsf.consultas;

import java.util.ArrayList;
import java.util.Collection;

import javax.faces.bean.ViewScoped;

import com.papelesinteligentes.bbva.notascontables.dto.UsuarioModulo;

@ViewScoped
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
