package com.papelesinteligentes.bbva.notascontables.schedule;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;

public class ScheduleLoadData {
	private final String pathName;
	private final String cron;
	private static final SchedulerFactory sf = new StdSchedulerFactory();
	private static Scheduler sched;

	public ScheduleLoadData(String pathName, String cron) {
		super();
		this.pathName = pathName;
		//System.out.println("Path usado para tareas programadas: " + pathName);
		this.cron = cron;
	}

	public void run() throws Exception {

		sched = sf.getScheduler();
		JobDetail job = newJob(LoadDataJob.class).withIdentity("jobLoadData", "group1").usingJobData("pathName", pathName).build();
		CronTrigger trigger = newTrigger().withIdentity("triggerLoadData", "group1").withSchedule(cronSchedule(cron)).build();
		sched.scheduleJob(job, trigger);

		sched.start();
		// wait long enough so that the scheduler as an opportunity to
		// run the job!
		// System.out.println("------- Waiting 65 seconds... -------------");
		// try {
		// // wait 65 seconds to show job
		// Thread.sleep(65L * 1000L);
		// // executing...
		// } catch (Exception e) {
		// }

		// shut down the scheduler
		// System.out.println("------- Shutting Down ---------------------");
		// sched.shutdown(true);
		// System.out.println("------- Shutdown Complete -----------------");
	}

	public void destroy() {
		try {
			if (sched != null && sched.isStarted()) {
				sched.shutdown(true);
			}
		} catch (Exception e) {
			System.err.println("Error deteniendo hilo ");
			e.printStackTrace();
		}
	}

}
