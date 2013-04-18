package cz.cuni.mff.d3s.been.task;

import java.util.concurrent.TimeUnit;

import cz.cuni.mff.d3s.been.taskapi.Requestor;
import cz.cuni.mff.d3s.been.taskapi.Task;

/**
 * 
 * Demonstrates working with checkpoints in different threads.
 * 
 * @author Martin Sixta
 */
public class ExampleSyncTask2 extends Task {

	private static final String SYNC_CHECKPOINT = "been.task.sync2.checkpoint";
	private static final String WAIT_PROPERTY = "been.task.sync2.wait";
	private static final int WAIT_SECONDS;

	static {
		String wait = System.getenv(WAIT_PROPERTY);
		// does not handle NumberFormatException ...
		WAIT_SECONDS = wait == null ? 5 : Integer.valueOf(wait);
	}

	public static void main(String[] args) {
		new ExampleSyncTask2().doMain(args);
	}

	@Override
	public void run() {
		Thread setter = new Thread() {
			@Override
			public void run() {
				Requestor requestor = new Requestor();
				try {
					Thread.sleep(TimeUnit.SECONDS.toMillis(WAIT_SECONDS));
					requestor.checkPointSet(SYNC_CHECKPOINT, "FTW");
				} catch (InterruptedException e) {
					e.printStackTrace();

				} finally {
					requestor.close();
				}
			}
		};

		Thread waiter = new Thread() {
			@Override
			public void run() {
				Requestor requestor = new Requestor();
				try {
					requestor.checkPointWait(SYNC_CHECKPOINT);
				} finally {
					requestor.close();
				}

			}
		};

		waiter.start();
		setter.start();

		try {
			waiter.join();
			setter.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

}
