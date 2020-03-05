package com.papelesinteligentes.bbva.notascontables.schedule.historico;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.papelesinteligentes.bbva.notascontables.carga.manager.CargaAltamiraScheduled;

public class CierreMensualAnularJob implements Job {
	private final CargaAltamiraScheduled cargaAltamiraScheduled = new CargaAltamiraScheduled();

	public CierreMensualAnularJob() {
	}

	public void execute(JobExecutionContext context) throws JobExecutionException {
		cargaAltamiraScheduled.procesarAnular();
	}
}
