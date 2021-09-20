package br.com.renato.reativo.poc.event;

import java.util.function.Consumer;
import java.util.function.Function;

class MapperEventEmmiter<NV, V> extends WrapperEventEmitter<NV> {
	
	private Function<V, EventEmitter<NV>> mapper;
	
	protected MapperEventEmmiter(EventEmitter<V> wrappedEmitter, Function<V, EventEmitter<NV>> mapper) {
		super((EventEmitter<NV>) wrappedEmitter);
		this.mapper = mapper;
	}
	
	@Override
	public void subscribe(Consumer<NV> success, Consumer<Throwable> error, Action complete) {
		super.subscribe(value -> { 
					mapper.apply((V) value).subscribe(success, error, () ->{});
				}, 
				error, 
				complete);
	}
	
}
