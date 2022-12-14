package com.algaworks.algafood.api.controller;

import java.util.List;

import javax.validation.Valid;

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
import com.algaworks.algafood.domain.exception.EstadoNaoEncontradoException;
import com.algaworks.algafood.domain.model.Estado;
import com.algaworks.algafood.domain.repository.EstadoRepository;
import com.algaworks.algafood.domain.service.CadastroEstadoService;

@Controller
@ResponseBody
@RequestMapping("/estados")
public class EstadoController<T> {

	@Autowired
	private EstadoRepository estadoRepository;

	@Autowired
	private CadastroEstadoService cadastroEstado;

	@GetMapping
	public List<Estado> listar() {
		return estadoRepository.findAll();
	}

	@GetMapping("/{estadoId}")
	public Estado buscar(@PathVariable Long estadoId) {
		return cadastroEstado.buscarOuFalhar(estadoId);
	}

	@PostMapping
	public Estado adicionar(@RequestBody @Valid Estado estado) {
		return cadastroEstado.salvar(estado);
	}

	@PutMapping("/{estadoId}")
	public Estado atualizar(@PathVariable Long estadoId, @RequestBody @Valid Estado estado) {
		Estado estadoAtual = cadastroEstado.buscarOuFalhar(estadoId);
		BeanUtils.copyProperties(estado, estadoAtual, "id");
		return cadastroEstado.salvar(estadoAtual);
	}

	@DeleteMapping("/{estadoId}")
	public ResponseEntity<?> remover(@PathVariable Long estadoId) {
		try {
			cadastroEstado.remover(estadoId);
			return ResponseEntity.noContent().build();
		} catch (EstadoNaoEncontradoException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		} catch (EntidadeEmUsoException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT).build();
		}
	}

}
