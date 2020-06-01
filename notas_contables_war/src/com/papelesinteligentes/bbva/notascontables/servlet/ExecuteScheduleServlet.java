package com.papelesinteligentes.bbva.notascontables.servlet;

import java.sql.Connection;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.papelesinteligentes.bbva.notascontables.dao.SuperDAO;
import com.papelesinteligentes.bbva.notascontables.schedule.ScheduleLoadData;
import com.papelesinteligentes.bbva.notascontables.schedule.ScheduleActualizaUsuarios;
import com.papelesinteligentes.bbva.notascontables.schedule.actpendiente.ScheduleActPendiente;
import com.papelesinteligentes.bbva.notascontables.schedule.cierre.ScheduleLoadCierreFile;
import com.papelesinteligentes.bbva.notascontables.schedule.historico.ScheduleCierreMensual;

public class ExecuteScheduleServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static boolean executed = false;
	private static ScheduleLoadData st;
	private static ScheduleLoadCierreFile stLC;
	private static ScheduleCierreMensual stCM;
	private static ScheduleActPendiente stAP;
	private static ScheduleActualizaUsuarios stActUsu;

	public ExecuteScheduleServlet() {
		super();
	}

	@Override
	public void init(ServletConfig config) throws ServletException {
		if (executed) {
			return;
		}
		executed = true;
		super.init();
		if (st == null) {
			try {
				st = new ScheduleLoadData(config.getServletContext().getInitParameter("DIR_CARGA"), config.getInitParameter("cron"));
				st.run();
			} catch (Exception e) {
				System.err.println("Error iniciando tarea de carga de archivos");
				e.printStackTrace();
			} 
		}
		/*****************************************/
		if (stActUsu == null) {
			try {
				stActUsu = new ScheduleActualizaUsuarios(config.getInitParameter("cronUpdateUsuarios"));
				stActUsu.execute();
			} catch (Exception e) {
				System.err.println("Error iniciando tarea de carga de archivos");
				e.printStackTrace();
			} 
		}
		
		
		
		if (stLC == null) {
			try {
				stLC = new ScheduleLoadCierreFile(config.getServletContext().getInitParameter("DIR_RECEPCION_ALTAMIRA"), config.getInitParameter("cron"));
				stLC.run();
			} catch (Exception e) {
				System.err.println("Error iniciando tarea de recepción de altamira (precierre-cierre)");
				e.printStackTrace();
			}
		}
		if (stCM == null) {
			try {
				stCM = new ScheduleCierreMensual(config.getInitParameter("cronCierreHist"), config.getInitParameter("cronCierreAnulado"), config.getInitParameter("cronCierreAnular"));
				stCM.run();
			} catch (Exception e) {
				System.err.println("Error iniciando tarea de cierre de notas y copia a tablas de historicos");
				e.printStackTrace();
			}
		}
		if (stAP == null) {
			try {
				stAP = new ScheduleActPendiente(config.getInitParameter("cronActPendiente"), config.getInitParameter("mail"));
				stAP.run();
			} catch (Exception e) {
				System.err.println("Error iniciando tarea de envio de correos para actividades pendientes ");
				e.printStackTrace();
			}
		}
	}

	@Override
	public void destroy() {
		super.destroy();
		if (st != null) {
			st.destroy();
		}
		if (stLC != null) {
			stLC.destroy();
		}
		if (st != null) {
			stCM.destroy();
		}
		if (stAP != null) {
			stAP.destroy();
		}

	}
}