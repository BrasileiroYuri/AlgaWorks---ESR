package com.algaworks.algafood.domain.repository;

import java.math.BigDecimal;
import java.util.List;

import com.algaworks.algafood.domain.model.Restaurante;

/* Aqui prevenimos possíveis erros em tempo de execução.
 * Essa interface permite que, se o nome do método na implentação estiver errado, não compilará.
 * Já na interface, evita que precisemos reescrever a assinatura do método.
 * E se mudarmos aqui, dará erro na chamada dos métodos e na implementação.*/
public interface RestauranteRepositoryQueries {

	List<Restaurante> find(String nome, BigDecimal taxaFreteInicial, BigDecimal taxaFreteFinal);

}