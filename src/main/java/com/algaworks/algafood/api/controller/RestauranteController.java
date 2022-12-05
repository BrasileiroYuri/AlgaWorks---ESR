package com.algaworks.algafood.api.controller;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.repository.RestauranteRepository;
import com.algaworks.algafood.domain.service.CadastroRestauranteService;

@Controller /*
			 * Essa notação define a classe responsável por receber, processar requisições e
			 * enviar uma resposta.
			 */
@ResponseBody /*
				 * Essa notação define que o retorno dos métodos será o retorno da requisição.
				 */
@RestController /* A junção das duas. Melhora a semântica visto que define como uma API REST. */
@RequestMapping("/restaurantes")
public class RestauranteController {

	@Autowired
	private RestauranteRepository restauranteRepository;

	@Autowired
	private CadastroRestauranteService cadastroRestaurante;

	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Restaurante> listarJson() {
		return restauranteRepository.listar();
	}

	@GetMapping("/{id}")
	public Restaurante buscar(@PathVariable Long id) {
		return restauranteRepository.buscar(id);
	}

	@PutMapping("/{restauranteId}")
	public ResponseEntity<Restaurante> atualizar(@PathVariable Long restauranteId,
			@RequestBody Restaurante restaurante) {

		Restaurante restauranteAtual = restauranteRepository.buscar(restauranteId);
		if (restauranteAtual != null) {
			BeanUtils.copyProperties(restaurante, restauranteAtual, "id");
			restauranteAtual = cadastroRestaurante.salvar(restauranteAtual);
			return ResponseEntity.status(HttpStatus.OK).body(restauranteAtual);
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}

	@DeleteMapping("/{restauranteId}")
	public ResponseEntity<Restaurante> remover(@PathVariable Long restauranteId) {
		try {
			cadastroRestaurante.excluir(restauranteId);
			return ResponseEntity.status(HttpStatus.OK).build();
		} catch (EntidadeNaoEncontradaException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
	}
}
