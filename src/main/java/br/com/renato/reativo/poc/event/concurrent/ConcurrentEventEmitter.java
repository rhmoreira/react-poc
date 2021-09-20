package br.com.renato.reativo.poc.event.concurrent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.function.Consumer;

import br.com.renato.reativo.poc.event.Action;
import br.com.renato.reativo.poc.event.EventEmitter;
import br.com.renato.reativo.poc.event.WrapperEventEmitter;

public class ConcurrentEventEmitter<V> extends WrapperEventEmitter<V> {
	
	private ExecutorService executor;
	private Future<?> execution;
	
	public ConcurrentEventEmitter(EventEmitter<V> wrappedEmitter, ExecutorService executor) {
		super(wrappedEmitter);
		this.executor = executor;
	}

	@Override
	public void subscribe(Consumer<V> success, Consumer<Throwable> error, Action complete) {
		this.execution = executor.submit(() -> super.subscribe(success, error, complete));
	}
	
	@Override
	public void await() throws InterruptedException {
		try { this.execution.get(); }
		catch (Exception e) {}
	}
	
}
