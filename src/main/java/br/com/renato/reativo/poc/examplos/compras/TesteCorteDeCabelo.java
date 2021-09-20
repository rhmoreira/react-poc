package br.com.renato.reativo.poc.examplos.compras;

import br.com.renato.reativo.poc.event.EventEmitter;
import br.com.renato.reativo.poc.event.concurrent.EventExecutor;
import br.com.renato.reativo.poc.examplos.compras.domain.Carrinho;
import br.com.renato.reativo.poc.examplos.compras.domain.CorteDeCabelo;
import br.com.renato.reativo.poc.examplos.compras.domain.Pessoa;
import br.com.renato.reativo.poc.examplos.compras.domain.ResultadoCorte;
import br.com.renato.reativo.poc.examplos.compras.repo.PessoaRepository;
import br.com.renato.reativo.poc.examplos.compras.service.CabelereiroService;
import br.com.renato.reativo.poc.examplos.compras.service.ComprasService;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class TesteCorteDeCabelo {

	private static PessoaRepository pessoaRepo = new PessoaRepository();
	private static CabelereiroService cabeloService = new CabelereiroService();
	private static ComprasService comprasService = new ComprasService();
	
	public static void main(String[] args) throws InterruptedException {
		startEvents();
		
		Runtime.getRuntime().addShutdownHook(new EventExecutor());
		System.exit(0);
	}
	
	private static void startEvents() throws InterruptedException {
		EventEmitter<Pessoa> filhoNoCabelereiro = deixarFilhoNoCabelereiro();
		EventEmitter<Pessoa[]> maeFazendoCompras = irFazerComprasEDepoisBuscarMeuFilho(filhoNoCabelereiro);
		
		filhoNoCabelereiro.subscribe();
		maeFazendoCompras.subscribe();
		
		maeFazendoCompras.subscribe(pessoas -> log.info("Pessoa 1 {}, Pessoa 2{}", pessoas[0], pessoas[1]));
		
	}

	private static EventEmitter<Pessoa> deixarFilhoNoCabelereiro(){
		return pessoaRepo.findPessoa("Filhinho")
				.doOnNext(pessoa -> log.info("Deixando meu filhinho no cabelereiro..."))
				.map(pessoa -> cabeloService.cortarCabelo(pessoa, CorteDeCabelo.TOPETINHO))
				.doOnNext(resultadoCorte -> log.info(resultadoCorte.getPessoa().getNome() + " terminou de cortar o cabelo no estilo " + resultadoCorte.getEstiloCorte()))
				.mapValue(ResultadoCorte::getPessoa)
				.fork()
				.share();
	}
	
	
	private static EventEmitter<Pessoa[]> irFazerComprasEDepoisBuscarMeuFilho(EventEmitter<Pessoa> filhoNoCabelereiro) {
		return pessoaRepo.findPessoa("Mãezinha")
				.doOnNext(pessoa -> log.info("Indo ao mercado fazer compras..."))
				.mapValue(Carrinho::new)
				.map(carrinho -> comprasService.adicionarProduto(carrinho, "Banana"))
				.map(carrinho -> comprasService.adicionarProduto(carrinho, "Salsicha"))
				.map(carrinho -> comprasService.adicionarProduto(carrinho, "Iogurte"))
				.map(carrinho -> comprasService.adicionarProduto(carrinho, "Toddynho"))
				.map(carrinho -> 
					comprasService.pagar(carrinho)
						.doOnNext(produtos -> log.info("Comprei " + produtos))
						.mapValue(produtos -> carrinho.getPessoa())
				).doOnNext(mamae-> log.info("Agora vou catar meu filho no cabelereiro"))
				.join(filhoNoCabelereiro)
				.doOnNext(pessoas -> log.info("Juntos de novo {} e {}", pessoas[0].getNome(), pessoas[1].getNome()))
				.fork()
				.share();
		}

}
