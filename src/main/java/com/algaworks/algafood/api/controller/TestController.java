package com.algaworks.algafood.api.controller;

import static com.algaworks.algafood.infrastructure.repository.spec.RestauranteSpecs.comFreteGratis;
import static com.algaworks.algafood.infrastructure.repository.spec.RestauranteSpecs.comNomeSemelhante;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.repository.CozinhaRepository;
import com.algaworks.algafood.domain.repository.RestauranteRepository;

@RestController
@RequestMapping("/teste")
public class TestController {

	@Autowired
	private CozinhaRepository cozinhaRepository;

	@Autowired
	private RestauranteRepository restauranteRepository;

	@RequestMapping(value = "/restaurante/por-nome", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public Optional<Restaurante> cozinhasPorNome(@RequestParam String nome) {
		return restauranteRepository.findFirstByNomeContaining(nome);
	}

	@RequestMapping(value = "/cozinhas/unica-por-nome", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public Optional<Cozinha> cozinhaPorNome(@RequestParam String nome) {
		return cozinhaRepository.findByNome(nome);
	}

	@GetMapping(value = "/restaurantes/por-taxa-frete")
	public List<Restaurante> restaurantesPorTaxaFrete(@RequestParam("taxainicial") BigDecimal taxaInicial,
			@RequestParam("taxafinal") BigDecimal taxaFinal) {
		return restauranteRepository.findByTaxaFreteBetween(taxaInicial, taxaFinal);
	}

	@GetMapping(value = "/restaurantes/por-nome")
	public List<Restaurante> restaurantePrimeiroPorNome(String nome, Long id) {
		return restauranteRepository.consultarPorNome(nome, id);
	}

	@GetMapping(value = "/restaurantes/top2-por-nome")
	public List<Restaurante> restaurantesTop2PorNome(@RequestParam String nome) {
		return restauranteRepository.findTop2ByNomeContaining(nome);
	}

	@GetMapping("/restaurantes/por-nome-e-frete")
	public List<Restaurante> restaurantesPorNome(String nome, BigDecimal taxaFreteInicial, BigDecimal taxaFreteFinal) {
		return restauranteRepository.find(nome, taxaFreteInicial, taxaFreteFinal);
	}

	@GetMapping("/restaurantes/com-frete-gratis")
	public List<Restaurante> restaurantesComFreteGratis(String nome) {
		return restauranteRepository
				.findAll(comFreteGratis().and(comNomeSemelhante(nome)));
	}
}
