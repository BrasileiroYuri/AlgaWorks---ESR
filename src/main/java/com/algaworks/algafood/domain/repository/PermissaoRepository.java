package com.algaworks.algafood.domain.repository;

import java.util.List;

import com.algaworks.algafood.domain.model.Permissao;

public interface PermissaoRepository {

	void remover(Permissao permissao);
	Permissao buscar(Long id);
	List<Permissao> listar();
	Permissao salvar(Permissao permissao);
}
