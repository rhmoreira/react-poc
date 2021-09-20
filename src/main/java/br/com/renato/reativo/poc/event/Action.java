package br.com.renato.reativo.poc.event;

@FunctionalInterface
public interface Action {

	void execute();
	
	static Action empty() {
		return () -> {};
	}
	
}
