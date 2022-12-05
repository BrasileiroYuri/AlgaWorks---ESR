package com.algaworks.algafood.domain.repository;

import java.util.List;

import com.algaworks.algafood.domain.model.Restaurante;

public interface RestauranteRepository {

	void remover(Long id);
	Restaurante buscar(Long id);
	List<Restaurante> listar();
	Restaurante salvar(Restaurante restaurante);
}
