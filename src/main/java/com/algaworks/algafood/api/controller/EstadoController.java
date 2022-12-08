package com.algaworks.algafood.api.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.model.Estado;
import com.algaworks.algafood.domain.repository.EstadoRepository;
import com.algaworks.algafood.domain.service.CadastroEstadoService;

/*@RestController -> Essa notação é uma junção das duas abaixo. Melhora a semântica ao vermos que trata-se de um controlador na arquitetura Rest.*/
@Controller /*-> Essa notação define que a classe que é responsável por receber requisições, tratá-las e retornar uma resposta.*/
@ResponseBody /*-> Essa notação define que o retorno dos métodos deve ser a resposta da requisição.*/
@RequestMapping("/estados") /*-> Essa notação define que as resquisições enviadas a URN citada serão tratadas por esse Controller.*/
public class EstadoController {

	@Autowired /*-> Apesar da notação @Component estar apenas na implementação, o Spring considera esse tipo genérico como componente, por seu tipo específico estar notado*/
	private EstadoRepository estadoRepository;

	@Autowired
	private CadastroEstadoService cadastroEstado;

	@GetMapping
	public List<Estado> listar() {
		return estadoRepository.findAll();
	}

	@GetMapping("/{estadoId}")
	public ResponseEntity<Estado> buscar(@PathVariable Long estadoId) {
		Optional <Estado> estado = estadoRepository.findById(estadoId);
		if (estado.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		return ResponseEntity.status(HttpStatus.OK).body(estado.get());
	}

	@PostMapping
	public Estado adicionar(@RequestBody Estado estado) {
		return cadastroEstado.salvar(estado);
	}

	@PutMapping("/{estadoId}")
	public ResponseEntity<?> atualizar(@PathVariable Long estadoId, @RequestBody Estado estado) {
		Optional <Estado> estadoAtual = estadoRepository.findById(estadoId);
		if (estadoAtual.isPresent()) {
			BeanUtils.copyProperties(estado, estadoAtual, "id");
			Estado estadoSalvar = cadastroEstado.salvar(estadoAtual.get());
			return ResponseEntity.ok(estadoSalvar);
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}

	@DeleteMapping("/{estadoId}")
	public ResponseEntity<?> remover(@PathVariable Long estadoId) {
		try {
			cadastroEstado.remover(estadoId);
			return ResponseEntity.noContent().build();
		} catch (EntidadeNaoEncontradaException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		} catch (EntidadeEmUsoException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT).build();
		}
	}

}
