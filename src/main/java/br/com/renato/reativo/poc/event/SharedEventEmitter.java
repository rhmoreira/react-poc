package br.com.renato.reativo.poc.event;

import java.util.function.Consumer;	

class SharedEventEmitter<V> extends WrapperEventEmitter<V> {
	
	private V sharedValue;
	private Throwable sharedError;

	private Status status;

	public enum Status {
		CONSUMING,
		CONSUMED
	}

	
	protected SharedEventEmitter(EventEmitter<V> wrappedEmitter) {
		super(wrappedEmitter);
	}


	@Override
	public void subscribe(Consumer<V> success, Consumer<Throwable> error, Action complete) {
		try {
			if (status != Status.CONSUMED) {
				if (status == Status.CONSUMING) {
					super.await();
					this.subscribeShared(success, error, complete);
				} else {
					this.status = Status.CONSUMING;
					super.subscribe(
						value -> {
							this.sharedValue = value;
							success.accept(value);
						},
						exception -> {
							this.sharedError = exception;
							error.accept(exception);
						},
						() -> {
							this.status = Status.CONSUMED;
							complete.execute();
						});
				}
			} else {
				this.subscribeShared(success, error, complete);
			}
		} catch (InterruptedException e) {
			error.accept(e);	
		}
	
		
	}
	
	private void subscribeShared(Consumer<V> success, Consumer<Throwable> error, Action complete) {
		if (sharedError != null)
			error.accept(sharedError);
		else
			success.accept(sharedValue);
		
		complete.execute();
	}
	
}
