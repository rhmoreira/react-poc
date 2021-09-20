package br.com.renato.reativo.poc.event.concurrent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.logging.log4j.LogManager;

public class EventExecutor extends Thread implements EventShutdownHook{

	private static ExecutorService executor = Executors.newFixedThreadPool(10, new EventThreadFactory());
	
	
	private static class EventThreadFactory implements ThreadFactory {

		private static AtomicInteger threadCount = new AtomicInteger(0);
		private ThreadFactory defaultFactory = Executors.defaultThreadFactory();
		
		@Override
		public Thread newThread(Runnable r) {
			Thread newThread = defaultFactory.newThread(r);
			newThread.setName("fork-event-emitter-" + threadCount.getAndIncrement());
			return newThread;
		}
		
	}
	
	public static ExecutorService defaultExecutor() {
		return executor;
	}
	
	@Override
	public void run() {
		this.shutdown();
	}

	@Override
	public void shutdown() {
		try {
			executor.shutdown();
			executor.awaitTermination(Integer.MAX_VALUE, TimeUnit.SECONDS);
			LogManager.shutdown();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
