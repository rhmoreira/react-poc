package br.com.renato.reativo.poc.examplos.compras.repo;

import br.com.renato.reativo.poc.event.EventEmitter;
import br.com.renato.reativo.poc.event.Events;
import br.com.renato.reativo.poc.examplos.compras.domain.Pessoa;

public class PessoaRepository {

	public EventEmitter<Pessoa> findPessoa(String nome) {
		return Events.newEvent(new Pessoa(nome));
	}
}
