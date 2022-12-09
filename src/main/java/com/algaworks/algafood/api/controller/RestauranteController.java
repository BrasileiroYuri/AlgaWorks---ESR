package com.algaworks.algafood.api.controller;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.repository.RestauranteRepository;
import com.algaworks.algafood.domain.service.CadastroRestauranteService;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller /*->Essa notação define a classe responsável por receber, processar requisições eenviar uma resposta.
@ResponseBody/*->Essa notação define que o retorno dos métodos será o retorno da requisição. */
@RestController /*->A junção das duas. Melhora a semântica visto que define como uma API REST. */
@RequestMapping("/restaurantes")
public class RestauranteController {

	@Autowired
	private RestauranteRepository restauranteRepository;

	@Autowired
	private CadastroRestauranteService cadastroRestaurante;

	@GetMapping
	public List<Restaurante> listar() {
		return restauranteRepository.findAll();
	}

	@GetMapping("/{id}")
	public ResponseEntity<Restaurante> buscar(@PathVariable Long id) {
		Optional <Restaurante> restaurante = restauranteRepository.findById(id);
		if (restaurante != null) {
			return ResponseEntity.status(HttpStatus.OK).body(restaurante.get());
		}
		return ResponseEntity.notFound().build();
	}

	@PostMapping
	public ResponseEntity<?> adicionar(@RequestBody Restaurante restaurante) {
		try {
			restaurante = cadastroRestaurante.salvar(restaurante);
			return ResponseEntity.status(HttpStatus.CREATED).body(restaurante);
		} catch (EntidadeNaoEncontradaException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}

	@PutMapping("/{restauranteId}")
	public ResponseEntity<?> atualizar(@PathVariable Long restauranteId, @RequestBody Restaurante restaurante) {
		try {
			Optional <Restaurante> restauranteAtual = restauranteRepository.findById(restauranteId);
			if (restauranteAtual.isPresent()) {
				BeanUtils.copyProperties(restaurante, restauranteAtual.get(), "id");
				Restaurante restauranteSalvar = cadastroRestaurante.salvar(restauranteAtual.get());
				return ResponseEntity.status(HttpStatus.OK).body(restauranteSalvar);
			}
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		} catch (EntidadeNaoEncontradaException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
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

	@PatchMapping("/{restauranteId}") /* Atualização parcial de um recurso utilizando o método HTTP PATCH.*/
	public ResponseEntity<?> atualizarParcial(@PathVariable Long restauranteId, @RequestBody Map<String, Object> campos) {
		Optional <Restaurante> restauranteAtual = restauranteRepository.findById(restauranteId);
		if (restauranteAtual.isPresent()) {
			return ResponseEntity.notFound().build(); /*Caso o recurso pedido pelo cliente (id) no corpo da Request seja inexistente, o método termina aqui com um 404.*/
		}
		merge(campos, restauranteAtual.get());
		return atualizar(restauranteId, restauranteAtual.get()); /* Utilizado visto que esse método possui tratamentos de possiveis exceções e verifica-se em Service os objetos aninhados (Cozinha).*/
		}
	/*Aqui mescla-se usando a API de Reflections do Spring. Essa Reflections se refere a capacidade de inspecionar e alterar objetos Java em tempo de execução, de uma maneira genérica.*/
	private void merge(Map<String, Object> dadosOrigem, Restaurante RestauranteDestino) { /*Esse método mescla as propriedades do Map para dentro do objeto restaurante.*/
		ObjectMapper objectMapper = new ObjectMapper(); /*Como há um problema ao receber dados, devido a castings errados, aqui criamos uma variável do tipo ObjectMapper,
		que serve para serializar e deserializar.*/
		Restaurante restauranteOrigem = objectMapper.convertValue(dadosOrigem, Restaurante.class);/*Aqui realiza-se conversões de tipo, de modo que os valores no Map estarão em coesão com os dessa classe.*/
		dadosOrigem.forEach((nomePropriedade, valorPropriedade) -> { /*Aqui estamos fazendo um Loop com as propriedades que recebemos desse Map.*/
			Field field = ReflectionUtils.findField(Restaurante.class, nomePropriedade); /*Essa variável representa um atributo da classe passada como argumento.
			 Note que está dentro de um forEach, então será aplicado para todas as propriedades. A classe Field é da API de Reflections do Java, contudo, a classe ReflectionUtils é do Spring.*/
			field.setAccessible(true); /*Como os atributos são privados, usamos esse método para torná-los acessíveis a classe Field.*/
			Object novoValor = ReflectionUtils.getField(field, restauranteOrigem); /*Aqui busca-se o valor da campo definido no objeto do tipo Field na instância definida.
			Note que já é a instância com valores convertidos.*/
			ReflectionUtils.setField(field, RestauranteDestino, novoValor);
			/*Assim ocorre o método: no mapa dadosOrigem, instanciado pelo Spring com as propriedades entregues no corpo da requisição pelo cliente, é realizado um forEach, ou seja, sobre cada propriedade 
			 desse Map é feita uma operação. Aqui declara-se uma variável do tipo Field para representar um atributo da classe Restaurante, e depois é usado o metódo setField. Esse método define que um atri
			 buto de uma instância receberá um valor. Assim fica: (atributo, instância que vai receber um valor no atributo e o próprio valor).*/
		}
		);
	}
}
