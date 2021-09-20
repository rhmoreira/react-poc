package br.com.renato.reativo.poc.event;

import java.util.function.Supplier;

public class Events {

	public static <V> EventEmitter<V> newEvent(V value) {
		return newEvent(() -> value);
	}
	
	public static <V> EventEmitter<V> newEvent(Supplier<V> valueSupplier) {
		return new SimpleEventEmitter<V>(valueSupplier);
	}
}
