package com.papelesinteligentes.bbva.notascontables.schedule.cierre;

import java.io.File;
import java.io.IOException;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.papelesinteligentes.bbva.notascontables.carga.manager.CargaAltamiraCierreScheduled;

public class LoadCierreFileJob implements Job {

	private final CargaAltamiraCierreScheduled cargaAltamiraCierreScheduled = new CargaAltamiraCierreScheduled();

	/**
	 * <p>
	 * Empty constructor for job initilization
	 * </p>
	 * <p>
	 * Quartz requires a public empty constructor so that the scheduler can instantiate the class whenever it needs.
	 * </p>
	 */
	public LoadCierreFileJob() {
	}

	/**
	 * <p>
	 * Called by the <code>{@link org.quartz.Scheduler}</code> when a <code>{@link org.quartz.Trigger}</code> fires that is associated with the <code>Job</code>.
	 * </p>
	 * 
	 * @throws JobExecutionException
	 *             if there is an exception while executing the job.
	 */
	public void execute(JobExecutionContext context) throws JobExecutionException {

		JobDataMap dataMap = context.getJobDetail().getJobDataMap();
		String pathName = dataMap.getString("pathName");

		File file = new File(pathName);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (file.exists() && file.isDirectory()) {
			try {
				cargaAltamiraCierreScheduled.loadDatosAltamiraScheduled(file);
			} catch (Exception e) {
			}
		} else {
			System.err.println("No se ha encontrado el directorio indicado para procesar los archivos de precierre/cierre: " + pathName);
		}

		// Say Hello to the World and display the date/time
	}

}