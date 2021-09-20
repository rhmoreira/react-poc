package br.com.renato.reativo.poc;

import br.com.renato.reativo.poc.event.EventEmitter;
import br.com.renato.reativo.poc.event.Events;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class Main {

	public static void main(String[] args) throws InterruptedException {
		EventEmitter<String> eventEmitter = 
			Events.newEvent("Renato")
			.doOnNext((v) -> {
				try {
					Thread.sleep(4000);
				} catch (InterruptedException e) {}
			  })
			  .mapValue(nome -> nome + " Moreira")
			  .mapValue(nome -> nome.length())
			  .mapValue(chars -> "Total caracteres: " + chars)
			  .mapValue(chars -> "Papai")
			  .doOnNext(v -> log.info(" Fluxo terminou"))
			  .share();

		eventEmitter.subscribe();
		
	}

}
