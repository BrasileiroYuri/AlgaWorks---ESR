package com.algaworks.algafood.domain.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.algaworks.algafood.domain.model.Restaurante;

public interface RestauranteRepository extends JpaRepository<Restaurante, Long>, RestauranteRepositoryQueries {

	List<Restaurante> findByTaxaFreteBetween(BigDecimal taxaInicial, BigDecimal TaxaFinal);

	List<Restaurante> findByNomeContainingAndCozinhaId(String nome, Long cozinhaId);

	List<Restaurante> consultarPorNome(String nome, Long id);

	Optional<Restaurante> findFirstByNomeContaining(String nome);

	List<Restaurante> findTop2ByNomeContaining(String nome);

}
