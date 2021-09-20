package br.com.renato.reativo.poc.event;

import java.lang.reflect.Array;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import br.com.renato.reativo.poc.event.concurrent.ConcurrentEventEmitter;
import br.com.renato.reativo.poc.event.concurrent.EventExecutor;

public abstract class AbstractEventEmitter<V> implements EventEmitter<V>{
	
	private Supplier<V> valueSupplier;
	
	public AbstractEventEmitter() {}
	
	protected AbstractEventEmitter(Supplier<V> valueSupplier) {
		super();
		this.valueSupplier = valueSupplier;
	}


	@Override
	public <NV> EventEmitter<NV> map(Function<V, EventEmitter<NV>> mapper) {
		return new MapperEventEmmiter<NV, V>(this, mapper);
	}
	
	@Override
	public <NV> EventEmitter<NV> mapValue(Function<V, NV> mapper) {
		return map(v -> Events.newEvent(mapper.apply(v)));
	}
	
	@Override
	public EventEmitter<V> doOnNext(Consumer<V> next) {
		return this.map(v -> {
			next.accept(v);
			return Events.newEvent(v);
		});
	}
	
	public EventEmitter<V> share() {
		return new SharedEventEmitter<V>(this);
	}
	
	@Override
	public EventEmitter<V[]> join(EventEmitter<V> event) {
		return this.map(v1 ->
					Events.newEvent(() -> { 
						V[] joined = (V[]) Array.newInstance(v1.getClass(), 2);
						joined[0] = v1;
						event.subscribe(v2 -> joined[1] = v2);
						
						try { 
							event.await();
							return joined;
						} catch (InterruptedException e) {
							throw new RuntimeException(e);
						}
					})
				);
	}
	
	@Override
	public EventEmitter<V> fork() {
		return fork(EventExecutor.defaultExecutor());
	}
	
	@Override
	public EventEmitter<V> fork(ExecutorService executor) {
		return new ConcurrentEventEmitter<V>(this, executor);
	}
	
	
	@Override
	public void subscribe(Consumer<V> success, Consumer<Throwable> error, Action complete) {
		try {
			success.accept(valueSupplier.get());
		} catch (Exception e) {
			error.accept(e);
		} finally {
			complete.execute();
		}
	}
	
	@Override
	public void subscribe(Consumer<V> success) {
		this.subscribe(success, Throwable::printStackTrace, Action::empty);
	}
	
	@Override
	public void subscribe() {
		this.subscribe(v -> {});
	}
	
}
