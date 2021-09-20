package br.com.renato.reativo.poc.examplos.compras.service;

import java.util.List;

import br.com.renato.reativo.poc.event.EventEmitter;
import br.com.renato.reativo.poc.event.Events;
import br.com.renato.reativo.poc.examplos.compras.domain.Carrinho;

public class ComprasService {
	
	public EventEmitter<Carrinho> adicionarProduto(Carrinho carrinho, String produto) {
		return Events.newEvent(() -> {
			try { Thread.sleep(500); } 
			catch (InterruptedException e) {}
			
			carrinho.getProdutos().add(produto);
			return carrinho;
		});
	}
	
	public EventEmitter<List<String>> pagar(Carrinho carrinho) {
		return Events.newEvent(() -> {
			carrinho.getProdutos().forEach((p) -> {
				try {Thread.sleep(500);}
				catch (InterruptedException e) {}
			});
			return carrinho.getProdutos();
		});
	}
}
