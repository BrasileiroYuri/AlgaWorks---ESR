package com.algaworks.algafood.domain.repository;

import java.util.List;

import com.algaworks.algafood.domain.model.FormaPagamento;

public interface FormaPagamentoRepository {

	void remover(FormaPagamento formaPagamento);
	FormaPagamento buscar(Long id);
	List<FormaPagamento> listar();
	FormaPagamento salvar(FormaPagamento formaPagamento);
}
