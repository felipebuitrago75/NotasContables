package com.papelesinteligentes.bbva.notascontables.jsf.common;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;

import javax.faces.application.FacesMessage;

import org.ajax4jsf.model.KeepAlive;

import com.papelesinteligentes.bbva.notascontables.carga.dto.Festivo;
import com.papelesinteligentes.bbva.notascontables.dto.ActividadRealizada;
import com.papelesinteligentes.bbva.notascontables.jsf.BasePage;
import com.papelesinteligentes.bbva.notascontables.util.DateUtils;

@KeepAlive
public class FlujoSubPage extends BasePage {

	private ActividadRealizada actividadRealizada;

	private ArrayList<ActividadRealizada> actividadesRealizadas;

	public FlujoSubPage() {
		super();
		actividadRealizada = new ActividadRealizada();
		actividadesRealizadas = new ArrayList<ActividadRealizada>();
	}

	/**
	 * 
	 * <b> Modificar metodo para fechas tipo Date a Calendar. </b>
	 * <p>
	 * [Author: Usuario, Date: 11/11/2020]
	 * </p>
	 *
	 * @return
	 */
	public String consultarFlujo() {
		try {
			actividadesRealizadas = new ArrayList<ActividadRealizada>(notasContablesManager.getActividadesRealizadasPorInstancia(actividadRealizada));
			for (int i = 0; i < actividadesRealizadas.size(); i++) {
				ActividadRealizada ac = actividadesRealizadas.get(i);
				long dif = 0L;
				// se obtienen los festivos
				Collection<Festivo> festivos = cargaAltamiraManager.getFestivos();
				int cantFest = 0;
				if (ac.getFechaHoraCierreTs() != null) {
					// se hace el calculo de duracion
					Calendar calendarHoraC = Calendar.getInstance();
					calendarHoraC.setTimeInMillis(ac.getFechaHoraCierreTs().getTime());
					Calendar calendarHotaTS = Calendar.getInstance();
					calendarHotaTS.setTimeInMillis(ac.getFechaHoraTs().getTime());

					cantFest = DateUtils.getFestivosEntre(calendarHotaTS, calendarHoraC, new ArrayList<Festivo>(festivos));

					dif = ac.getFechaHoraCierreTs().getTime() - ac.getFechaHoraTs().getTime();
				} else {
					// se hace el calculo de duracion
					Calendar calendarHoraTS = Calendar.getInstance();
					calendarHoraTS.setTimeInMillis(ac.getFechaHoraTs().getTime());

					Calendar calendar = Calendar.getInstance();
					calendar.setTimeInMillis(Calendar.getInstance().getTimeInMillis());
					cantFest = DateUtils.getFestivosEntre(calendarHoraTS, calendar, new ArrayList<Festivo>(festivos));

					dif = Calendar.getInstance().getTimeInMillis() - ac.getFechaHoraTs().getTime();
				}

				ac.setHorasEtapa(Math.max(1D, dif / 86400000 - cantFest));
			}
		} catch (Exception le_e) {
			actividadesRealizadas = new ArrayList<ActividadRealizada>();
			le_e.printStackTrace();
			nuevoMensaje(FacesMessage.SEVERITY_WARN, "Error en ejecución " + le_e.getMessage());
		}
		return null;
	}

	public ActividadRealizada getActividadRealizada() {
		return actividadRealizada;
	}

	public void setActividadRealizada(ActividadRealizada actividadRealizada) {
		this.actividadRealizada = actividadRealizada;
	}

	public ArrayList<ActividadRealizada> getActividadesRealizadas() {
		return actividadesRealizadas;
	}

	public void setActividadesRealizadas(ArrayList<ActividadRealizada> actividadesRealizadas) {
		this.actividadesRealizadas = actividadesRealizadas;
	}
}
