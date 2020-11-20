package com.papelesinteligentes.bbva.notascontables.jsf.carga;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Locale;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;

import com.papelesinteligentes.bbva.notascontables.carga.dto.CierreMensual;
import com.papelesinteligentes.bbva.notascontables.jsf.GeneralPage;
import com.papelesinteligentes.bbva.notascontables.jsf.IPages;

/**
 * <p>
 * Page bean that corresponds to a similarly named JSP page. This class contains component definitions (and initialization code) for all components that you have defined on this page, as well as lifecycle methods and event handlers where you may add
 * behavior to respond to incoming events.
 * </p>
 * 
 */
@ViewScoped
public class CierreMensualPage extends GeneralPage implements IPages, Serializable {

	private String cierreMensual;
	private Integer diasDesdeCierre;
	/**
	 * 
	 */
	private static final long serialVersionUID = -8330009617976284212L;

	/**
	 * <p>
	 * Construct a new Page bean instance.
	 * </p>
	 */
	public CierreMensualPage() {
		super();
	}

	/**
	 * Se realiza el proceso de busqueda completo
	 * 
	 * @return
	 */
	@Override
	public void _init() throws Exception {
		mostrar();
	}

	public String mostrar() {
		try {
			Collection<CierreMensual> datos = cargaAltamiraManager.getCierresMensuales();
			if (datos.isEmpty()) {
				nuevoMensaje(FacesMessage.SEVERITY_INFO, "No se encontró información de cierre mensual ");
			} else {
				for (CierreMensual cm : datos) {
					SimpleDateFormat sdf = new SimpleDateFormat("MMMM-yyyy", new Locale("es", "CO"));
					Calendar c = Calendar.getInstance();
					c.set(Calendar.YEAR, cm.getYear());
					c.set(Calendar.MONTH, cm.getMes() - 1);
					cierreMensual = sdf.format(c.getTime()).replace("-", " de ");
					break;
				}
				diasDesdeCierre = notasContablesManager.getDiasHabilesDesdeUltimoCierre();
			}
		} catch (final Exception e) {
			lanzarError(e, "Ocurrio un error al realizar la consulta de cierres mensuales");
		}
		return CIERRE_MENSUAL;
	}

	public String getCierreMensual() {
		return cierreMensual;
	}

	public void setCierreMensual(String cierreMensual) {
		this.cierreMensual = cierreMensual;
	}

	public Integer getDiasDesdeCierre() {
		return diasDesdeCierre;
	}

	public void setDiasDesdeCierre(Integer diasDesdeCierre) {
		this.diasDesdeCierre = diasDesdeCierre;
	}

}
