package br.com.renato.reativo.poc.examplos.compras.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResultadoCorte {

	private Pessoa pessoa;
	private CorteDeCabelo estiloCorte;
	
}
