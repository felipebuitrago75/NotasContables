package com.papelesinteligentes.bbva.notascontables.schedule.historico;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.papelesinteligentes.bbva.notascontables.carga.manager.CargaAltamiraScheduled;

public class CierreMensualHistJob implements Job {
	private final CargaAltamiraScheduled cargaAltamiraScheduled = new CargaAltamiraScheduled();

	public CierreMensualHistJob() {
	}

	public void execute(JobExecutionContext context) throws JobExecutionException {
		cargaAltamiraScheduled.procesarHistorico();
	}
}
