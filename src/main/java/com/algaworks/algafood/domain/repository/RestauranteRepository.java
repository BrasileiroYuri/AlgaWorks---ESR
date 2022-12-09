package com.algaworks.algafood.domain.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.algaworks.algafood.domain.model.Restaurante;

public interface RestauranteRepository extends JpaRepository<Restaurante, Long>, RestauranteRepositoryQueries {

	List<Restaurante> findByTaxaFreteBetween(BigDecimal taxaInicial, BigDecimal TaxaFinal);

	List<Restaurante> findByNomeContainingAndCozinhaId(String nome, Long cozinhaId);
	/*Mesmos métodos, mas um escrito pot meio de JPQL com o @Query e outro com keywords SDJ de Query Methods.*/
	//@Query("from Restaurante where nome like %:nome% and cozinha.id = :id") /*Consulta personalizada sem implementação, como padrão.*/
	List<Restaurante> consultarPorNome(String nome, Long id);
	
	Optional<Restaurante> findFirstByNomeContaining(String nome);

	List<Restaurante> findTop2ByNomeContaining(String nome);
	
	List<Restaurante> find(String nome, BigDecimal taxaFreteInicial, BigDecimal taxaFreteFinal);

	}
