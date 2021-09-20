package br.com.renato.reativo.poc.examplos.compras.service;

import br.com.renato.reativo.poc.event.EventEmitter;
import br.com.renato.reativo.poc.event.Events;
import br.com.renato.reativo.poc.examplos.compras.domain.CorteDeCabelo;
import br.com.renato.reativo.poc.examplos.compras.domain.Pessoa;
import br.com.renato.reativo.poc.examplos.compras.domain.ResultadoCorte;

public class CabelereiroService {
	
	public EventEmitter<ResultadoCorte> cortarCabelo(Pessoa pessoa, CorteDeCabelo corte) {
		return Events.newEvent(() -> {
			try { Thread.sleep(6000); } 
			catch (InterruptedException e) {}
			return new ResultadoCorte(pessoa, corte);	
		});
	}

}
