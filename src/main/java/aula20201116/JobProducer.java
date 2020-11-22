package aula20201116;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JobProducer extends Thread {

	private final Logger logger = LoggerFactory.getLogger(JobProducer.class);

	private final JobQueue jobs;

	public JobProducer(JobQueue jobs) {
		this.jobs = jobs;
	}

	@Override
	public void run() {
		final Random r = new Random();
		while (true) {
			try {
				sleep(500);

				Integer newJob = Double.valueOf(60 * r.nextDouble()).intValue();
				newJob = newJob.equals(0) ? 5 : newJob;

				logger.info("Creating a new job: size {}", newJob);
				this.jobs.queueJob(newJob);
			} catch (InterruptedException e) {
				logger.error(e.getMessage());
			}
		}
	}

}
