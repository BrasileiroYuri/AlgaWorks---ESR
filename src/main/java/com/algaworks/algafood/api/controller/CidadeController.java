package com.algaworks.algafood.api.controller;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.model.Cidade;
import com.algaworks.algafood.domain.repository.CidadeRepository;
import com.algaworks.algafood.domain.service.CadastroCidadeService;

@RestController
@RequestMapping("/cidades")
public class CidadeController {

	@Autowired
	private CidadeRepository cidadeRepository;

	@Autowired
	private CadastroCidadeService cadastroCidade;

	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public List<Cidade> listar() {
		return cidadeRepository.listar();
	}

	@GetMapping("/{cidadeId}")
	public ResponseEntity<?> buscar(@PathVariable Long cidadeId) {
		Cidade cidade = cidadeRepository.buscar(cidadeId);
		if (cidade != null) {
			return ResponseEntity.status(HttpStatus.OK).body(cidade);
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
	}

	@PostMapping
	public ResponseEntity<?> adicionar(@RequestBody Cidade cidade) {
		try {
			cidade = cadastroCidade.salvar(cidade);
			return ResponseEntity.status(HttpStatus.CREATED).body(cidade);
		} catch (EntidadeNaoEncontradaException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
	}

	@PutMapping("/{cidadeId}")
	public ResponseEntity<?> atualizar(@PathVariable Long cidadeId, @RequestBody Cidade cidade) {
		try {
			Cidade cidadeAtual = cidadeRepository.buscar(cidadeId); /* Aqui eu verifico se a cidade existe. */
			if (cidadeAtual != null) { /* Se ela existir, entra aqui. */
				BeanUtils.copyProperties(cidade, cidadeAtual, "id"); /* Copio as propriedades */
				cidadeAtual = cadastroCidade.salvar(cidadeAtual); /* Tento salvar. */
				return ResponseEntity.status(HttpStatus.OK).body(cidadeAtual);
			}
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		} catch (EntidadeNaoEncontradaException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}

	@DeleteMapping("/{cidadeId}")
	public ResponseEntity<?> remover(@PathVariable Long cidadeId) {
		try {
			cadastroCidade.excluir(cidadeId);
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		} catch (EntidadeNaoEncontradaException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		} catch (DataIntegrityViolationException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT).build();
		}
	}

}