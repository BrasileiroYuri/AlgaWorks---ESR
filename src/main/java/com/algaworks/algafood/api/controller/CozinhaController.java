package com.algaworks.algafood.api.controller;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.api.model.CozinhasXmlWrapper;
import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.repository.CozinhaRepository;

@RestController
@RequestMapping(value = "/cozinhas")
public class CozinhaController {

	@Autowired
	private CozinhaRepository cozinhaRepository;

	@GetMapping
	public List<Cozinha> listar() {
		return cozinhaRepository.listar();

	}

	@GetMapping(produces = MediaType.APPLICATION_XML_VALUE)
	public CozinhasXmlWrapper listarXml() {
		return new CozinhasXmlWrapper(cozinhaRepository.listar());
	}

	/*@ResponseStatus(HttpStatus.CREATED) -> Define o status http que vai estar presente na resposta.*/
	@GetMapping("/{cozinhaId}")
	public ResponseEntity<Cozinha> buscar(@PathVariable Long cozinhaId) { /*@ResponseEntity -> Serve para customizar a resposta HTTP em seus elementos: HttpStatus, Headers e Body .*/
		Cozinha cozinha = cozinhaRepository.buscar(cozinhaId);
		/* return ResponseEntity.status(HttpStatus.OK).body(cozinha);
			return ResponseEntity.ok(cozinha);
		
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.LOCATION, "http://localhost:8080/cozinhas");
		
		return ResponseEntity
				.status(HttpStatus.FOUND)
				.headers(headers).build();
				
		return ResponseEntity.status(HttpStatus.FOUND).header(HttpHeaders.LOCATION, "http://localhost:8080/cozinhas" ).build();
		
		return ResponseEntity.status(HttpStatus.OK).build();*/
		
		if (cozinha != null) {
			return ResponseEntity.ok(cozinha);
		} 
		/*return ResponseEntity.status(HttpStatus.NOT_FOUND).build();*/
		return ResponseEntity.notFound().build();
	}
	/*@ResponseStatus(HttpStatus.CREATED)*/
	@PostMapping
	public void adicionar(@RequestBody Cozinha cozinha) { /*-> Essa anotação define que o corpo da requisição será vinculado a esse parâmetro. */
		cozinha = cozinhaRepository.salvar(cozinha);
	}
}
