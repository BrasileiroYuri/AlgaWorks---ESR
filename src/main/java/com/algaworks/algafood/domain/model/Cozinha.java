package com.algaworks.algafood.domain.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.EqualsAndHashCode.Include;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class Cozinha {

	@Include
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String nome;
	/*
	 * Quando a propriedade mappedBy é utilizada, estamos indicando que esse
	 * relacionamento está sendo representado em algum lugar (propriedade) na outra
	 * entidade (Aqui é mapeado o nome do atributo, ou seja, em String). Logo, não é
	 * necessário criar uma tabela de assossiação para representá-lo. Além disso, é
	 * necessário utilizar o @JsonIgnore em alguma propriedade em algumas das
	 * entidades. Isso pois, uma assossiação bidirecional torna-se um referência
	 * circular que retornará uma Exception. No lado do Many, ou seja, aquele que
	 * recebe o One, é possível representar o relacionamento sem a criação de outra
	 * tabela, apenas com uma coluana FK, visto que contém apenas um registro
	 * (ToOne). Se fossémos representar pelo lado do One, teriamos que criar uma
	 * tabela de assossiação, visto que são vários registros para serem
	 * representados em um só.
	 */
	@JsonIgnore
	@OneToMany(mappedBy = "cozinha")
	private List<Restaurante> restaurantes = new ArrayList<>();

}
