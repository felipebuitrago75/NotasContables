package com.papelesinteligentes.bbva.notascontables.schedule;

import org.quartz.SchedulerException;

public class ScheduleActualizaUsuarios {
	
	private final String cronUpdateUsuarios;
	
	/*******************************************************************/
	public ScheduleActualizaUsuarios(String cronUpdateUsuarios){
		super();
		//System.out.println("Path usado para tareas programadas: ");
		this.cronUpdateUsuarios = cronUpdateUsuarios;
	}
	public void execute() throws SchedulerException {
		/*SuperDAO obj = new SuperDAO(null);
		Connection con;
		try {
			con= obj.getConexion();
		//	obj.addRegistroAuditoriaLogout(con);
		} catch (Exception e) {
			System.out.println("Error no actualizo " + e);
		}**/
		}
}
