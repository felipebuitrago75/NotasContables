package com.papelesinteligentes.bbva.notascontables.schedule.actpendiente;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.papelesinteligentes.bbva.notascontables.carga.manager.CargaAltamiraScheduled;

public class ActPendienteJob implements Job {

	private final CargaAltamiraScheduled cargaAltamiraCierreScheduled = new CargaAltamiraScheduled();

	/**
	 * <p>
	 * Empty constructor for job initilization
	 * </p>
	 * <p>
	 * Quartz requires a public empty constructor so that the scheduler can instantiate the class whenever it needs.
	 * </p>
	 */
	public ActPendienteJob() {
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

		try {
			JobDataMap dataMap = context.getJobDetail().getJobDataMap();
			String mail = dataMap.getString("mail");
			cargaAltamiraCierreScheduled.loadProcesarActividadesPendientes(mail);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}