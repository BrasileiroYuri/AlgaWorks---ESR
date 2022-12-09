package com.algaworks.algafood.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.algaworks.algafood.domain.model.Cozinha;

@Repository
public interface CozinhaRepository extends JpaRepository<Cozinha, Long> {

	List<Cozinha> findAllByNomeContaining(String nome);
	/*
	 * Pelo fato de ser atribuida a JpaRepository o tipo Cozinha, ao declararmos o
	 * método como "nome", ele identifica esse método como propriedade da classe
	 * Cozinha, buscando por essa propriedade. Ou seja, quando temos um método de
	 * pesquisa em uma interface que extende o JpaRepository, o tipo que atribuimos
	 * a essa interface (JPA) o nome desse método é considerado como atributo,
	 * buscando por esse atributo.
	 */
	/* Query methos funcionam também por meio de prefixos e flags. Ex: findBy...*/
	Optional<Cozinha> findByNome(String nome);
	
}
