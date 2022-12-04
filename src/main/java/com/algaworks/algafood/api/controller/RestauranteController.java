package com.algaworks.algafood.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.api.model.RestauranteXmlWrapper;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.repository.RestauranteRepository;

@Controller /*Essa notação afirma que essa classe é responsável por receber e processar requisições e enviar uma resposta.*/
@ResponseBody /* Essa notação define que o retorno dos métodos será o retorno da requisição.*/
@RestController /* A junção das duas. Melhora a semântica visto que define como uma API REST. */
@RequestMapping("/restaurantes")
public class RestauranteController {

	@Autowired
	private RestauranteRepository restauranteRepository;

	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Restaurante> listarJson() {
		return restauranteRepository.listar();
	}

	@GetMapping(produces = MediaType.APPLICATION_XML_VALUE)
	public RestauranteXmlWrapper listarXml() {
		return new RestauranteXmlWrapper(restauranteRepository.listar());

	}
	@GetMapping("/{id}")
	public Restaurante buscar(@PathVariable Long id) {
		return restauranteRepository.buscar(id);
	}

}
