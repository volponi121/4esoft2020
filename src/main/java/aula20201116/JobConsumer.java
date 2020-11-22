package aula20201116;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class JobConsumer extends Thread {

	private final Logger logger = LoggerFactory.getLogger(JobConsumer.class);

	private final JobQueue jobs;
	private Integer assignedJob = 0;

	public JobConsumer(JobQueue jobs) {
		this.jobs = jobs;
	}

	@Override
	public void run() {
		while (true) {
			if (assignedJob == 0) {
				try {
					if (jobs.getNextJob() == 0) {
						logger.info("Nothing to do..., Time: {} ", System.currentTimeMillis());
						sleep(3000);
					}
				} catch (InterruptedException e) {
					logger.error(e.getMessage());
				}
			} else {
				for (int i = assignedJob; i >= 0; i--) {
					try {
						sleep(1000);
					} catch (InterruptedException e) {
						logger.error(e.getMessage());
					}
				}
				assignedJob = 0;
			}
		}
	}
}
