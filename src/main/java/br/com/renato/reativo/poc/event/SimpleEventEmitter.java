package br.com.renato.reativo.poc.event;

import java.util.function.Supplier;

class SimpleEventEmitter<V> extends AbstractEventEmitter<V> {

	protected SimpleEventEmitter(Supplier<V> valueSupplier) {
		super(valueSupplier);
		// TODO Auto-generated constructor stub
	}

}
