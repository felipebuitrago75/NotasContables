package com.papelesinteligentes.bbva.notascontables.jsf.consultas;

import java.util.ArrayList;

import javax.faces.bean.ViewScoped;

import com.papelesinteligentes.bbva.notascontables.dto.Instancia;
import com.papelesinteligentes.bbva.notascontables.dto.NotaContable;
import com.papelesinteligentes.bbva.notascontables.dto.NotaContableCrucePartidaPendiente;
import com.papelesinteligentes.bbva.notascontables.dto.NotaContableRegistroLibre;
import com.papelesinteligentes.bbva.notascontables.dto.NotaContableTema;

@ViewScoped
public class ConsultaMovimientosContablesPage extends ReporteGeneralPage {

	private static final long serialVersionUID = 5090751661160357021L;

	public ConsultaMovimientosContablesPage() {
		super();
	}

	@Override
	protected String _getPage() {
		return CONSULTA_MOVIMIENTOS_CONTABLES;
	}

	@Override
	public String buscar() {
		try {
			tipoNota = "";
			estado = "6";
			descripcion = "";
			super.buscar();
			for (Instancia ins : getDatos()) {
				NotaContable nc = ins.getNC();
				if (nc.getTipoNota().equals(NotaContable.NORMAL)) {
					nc.setTemas(new ArrayList<NotaContableTema>(notasContablesManager.getTemasPorNotaContable(nc.getCodigo().intValue())));
				} else if (nc.getTipoNota().equals(NotaContable.LIBRE)) {
					nc.setTemasLibre(new ArrayList<NotaContableRegistroLibre>(notasContablesManager.getRegistrosNotaContableLibre(nc.getCodigo().intValue())));
				} else {
					nc.setTemasCruce(new ArrayList<NotaContableCrucePartidaPendiente>(notasContablesManager.getCrucesPartidasPendientesPorNotaContable(nc.getCodigo().intValue())));
				}
				ins.setNC(nc);
			}
		} catch (Exception e) {
			lanzarError(e, "Error realizando la busqueda");
		}
		return null;
	}

}
