package br.com.renato.reativo.poc.examplos.compras.domain;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class Carrinho {

	private final Pessoa pessoa;
	private List<String> produtos = new ArrayList<String>();
	
}
