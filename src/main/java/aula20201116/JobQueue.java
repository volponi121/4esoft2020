package aula20201116;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;

public class JobQueue {

    private final Logger logger = LoggerFactory.getLogger(JobQueue.class);

    private LinkedList<Integer> jobs = new LinkedList<>();
    private JobQueueListener listener;

    public JobQueue() {
        super();
    }

    public void addJobQueueListener(JobQueueListener listener) {
        this.listener = listener;
    }

    public static interface JobQueueListener {
        void jobQueueChanged(int newSize) throws InterruptedException;
    }

    public synchronized void queueJob(int job) throws InterruptedException {
        synchronized (this) {
            this.jobs.add(job);
            if (this.listener != null) {
                this.listener.jobQueueChanged(this.jobs.size());
            }
        }
    }

    public synchronized Integer getNextJob() throws InterruptedException {
        synchronized (this) {
            if (this.jobs.isEmpty()) {
                return 0;
            }
            Integer job = this.jobs.removeFirst();
            logger.info("Starting another job, {}", job);
            if (this.listener != null) {
                this.listener.jobQueueChanged(this.jobs.size());
            }
            return job;
        }
    }

}
