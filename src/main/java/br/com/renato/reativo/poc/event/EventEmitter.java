package br.com.renato.reativo.poc.event;

import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;
import java.util.function.Function;

public interface EventEmitter<V> {
	
	<NV> EventEmitter<NV> map(Function<V, EventEmitter<NV>> mapper);
	
	<NV> EventEmitter<NV> mapValue(Function<V, NV> mapper);
	
	EventEmitter<V> share();
	
	EventEmitter<V> doOnNext(Consumer<V> next);
	
	EventEmitter<V[]> join(EventEmitter<V> event);
	
	EventEmitter<V> fork();
	
	EventEmitter<V> fork(ExecutorService executor);
	
	void subscribe(Consumer<V> success, Consumer<Throwable> error, Action complete);
	
	void subscribe(Consumer<V> success);
	
	void subscribe();
	
	default void await() throws InterruptedException {}

}
