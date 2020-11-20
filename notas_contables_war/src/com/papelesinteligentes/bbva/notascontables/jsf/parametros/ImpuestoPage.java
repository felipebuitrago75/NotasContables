package com.papelesinteligentes.bbva.notascontables.jsf.parametros;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;

import com.papelesinteligentes.bbva.notascontables.carga.dto.PUC;
import com.papelesinteligentes.bbva.notascontables.dto.Impuesto;

/**
 * <p>
 * Pagina para manejar la administración de parametros relacionados con la entidad Impuesto
 * </p>
 * 
 */
@ViewScoped
public class ImpuestoPage extends GeneralParametrosPage<Impuesto, Impuesto> {

	private static final long serialVersionUID = 1L;

	private List<SelectItem> cuentas = new ArrayList<SelectItem>();
	private String filtroPUC;

	public ImpuestoPage() {
		super(true);
	}

	/**
	 * retorna una instancia de Impuesto
	 * 
	 * @return
	 */
	@Override
	protected Impuesto _getInstance() {
		return new Impuesto();
	}

	@Override
	protected void _init() {
		super._init();
	}

	public String buscarPartidas() {
		cuentas = new ArrayList<SelectItem>();
		if (filtroPUC.length() < 4) {
			nuevoMensaje(FacesMessage.SEVERITY_WARN, "El filtro debe ser de longitud 4 o superior");
		} else {
			try {
				Map<String, String> mapa = notasContablesManager.getCVBy(com.papelesinteligentes.bbva.notascontables.carga.dto.PUC.class, filtroPUC);
				for (final Object key : new TreeSet<String>(mapa.keySet())) {
					cuentas.add(new SelectItem(key, key + " - " + mapa.get(key)));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		return null;
	}

	/**
	 * Se realiza el proceso de busqueda completo de entidades de tipo Impuesto
	 * 
	 * @return
	 */
	@Override
	public Collection<Impuesto> _buscarTodos() throws Exception {
		return notasContablesManager.getImpuestos();
	}

	/**
	 * Realiza la busqueda de entidades de tipo Impuesto filtrando según lo indicado por el usuario
	 * 
	 * @return
	 */
	@Override
	public Collection<Impuesto> _buscarPorFiltro() throws Exception {
		return notasContablesManager.searchImpuesto(param);
	}

	/**
	 * Funcion llamada cuando se desea inciar la edicion o creacion de registros de tipo Impuesto
	 * 
	 * @return
	 */
	@Override
	protected void _editar() throws Exception {
		objActual = new Impuesto();
		filtroPUC = "";
		cuentas = new ArrayList<SelectItem>();
		if (objActual.getPartidaContable() != null && !objActual.getPartidaContable().isEmpty()) {
			filtroPUC = objActual.getPartidaContable();
		}
	}

	@Override
	protected boolean _guardar() throws Exception {
		String cuenta = objActual.getPartidaContable();
		if (cuenta.indexOf('-') > 0) {
			cuenta = cuenta.substring(0, cuenta.indexOf('-') - 1);
		}
		objActual.setPartidaContable(cuenta);

		Number codInicial = objActual.getCodigo();
		try {
			if (objActual.getCodigo().intValue() <= 0) {
				notasContablesManager.addImpuesto(objActual, getCodUsuarioLogueado());
			} else {
				notasContablesManager.updateImpuesto(objActual, getCodUsuarioLogueado());
			}
		} catch (Exception e) {
			objActual.setCodigo(codInicial);
			lanzarError(e, "Ya existe esa Cuenta registrada");
			return false;
		}
		return true;

	}

	@Override
	protected void _validar() throws Exception {
		if (objActual.getValor() <= 0 || objActual.getValor() > 100) {
			nuevoMensaje(FacesMessage.SEVERITY_WARN, "Porcentaje incorrecto, verifique por favor");
		}

		String cuenta = objActual.getPartidaContable();
		if (cuenta.indexOf('-') > 0) {
			cuenta = cuenta.substring(0, cuenta.indexOf('-') - 1);
		}

		PUC puc = new PUC();
		puc.setNumeroCuenta(cuenta);
		puc = cargaAltamiraManager.getPUC(puc);
		if (puc.getDescripcion().equals("")) {
			nuevoMensaje(FacesMessage.SEVERITY_WARN, "La cuenta contable no es una cuenta válida");
		}
	}

	/**
	 * Cambia el estado de la instancia seleccionada de tipo Impuesto
	 * 
	 * @return
	 */
	@Override
	public boolean _cambiarEstado() throws Exception {
		notasContablesManager.changeEstadoImpuesto(notasContablesManager.getImpuesto(objActual), getCodUsuarioLogueado());
		return true;
	}

	/**
	 * Borra la informacion de la instancia seleccionada de tipo Impuesto
	 * 
	 * @return
	 */
	@Override
	public boolean _borrar() throws Exception {
		notasContablesManager.deleteImpuesto(objActual, getCodUsuarioLogueado());
		return true;
	}

	@Override
	protected String _getPage() {
		return IMPUESTO;
	}

	public List<SelectItem> getCuentas() {
		return cuentas;
	}

	public void setCuentas(List<SelectItem> cuentas) {
		this.cuentas = cuentas;
	}

	public String getFiltroPUC() {
		return filtroPUC;
	}

	public void setFiltroPUC(String filtroPUC) {
		this.filtroPUC = filtroPUC;
	}
}
