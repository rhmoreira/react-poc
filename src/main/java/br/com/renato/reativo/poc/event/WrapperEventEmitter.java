package br.com.renato.reativo.poc.event;

import java.util.function.Consumer;

public abstract class WrapperEventEmitter<V> extends AbstractEventEmitter<V> {

	private EventEmitter<V> wrappedEmitter;
	
	protected WrapperEventEmitter(EventEmitter<V> wrappedEmitter) {
		this.wrappedEmitter = wrappedEmitter;
	}
	
	@Override
	public void subscribe(Consumer<V> success, Consumer<Throwable> error, Action complete) {
		wrappedEmitter.subscribe(success, error, complete);
	}
	
	@Override
	public void await() throws InterruptedException {
		this.wrappedEmitter.await();		
	}
	
}
