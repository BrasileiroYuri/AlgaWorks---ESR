package com.algaworks.algafood.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.groups.ConvertGroup;
import javax.validation.groups.Default;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.algaworks.algafood.core.constraints.Multiplo;
import com.algaworks.algafood.core.constraints.Nome;
import com.algaworks.algafood.core.constraints.TaxaFrete;
import com.algaworks.algafood.core.validation.Groups;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.EqualsAndHashCode.Include;

@JsonRootName("Restaurante")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class Restaurante {

	@Include
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank
	@Nome(numero = 5)
	@Column(nullable = false)
	private String nome;

	@TaxaFrete
	@JsonProperty("taxaFrete")
	@Multiplo(numero = 5)
	@Column(name = "taxa_frete", nullable = false)
	private BigDecimal taxaFrete;

	@ConvertGroup(from = Default.class, to = Groups.CozinhaId.class)
	@Valid
	@ManyToOne
	@JoinColumn(name = "cozinha_id", nullable = false)
	private Cozinha cozinha;

	@JsonIgnore
	@CreationTimestamp
	@Column(nullable = false, columnDefinition = "datetime")
	private LocalDateTime dataCadastro;

	@UpdateTimestamp
	@Column(nullable = false, columnDefinition = "datetime")
	private LocalDateTime dataAtualizacao;

	@JsonIgnore
	@Embedded
	private Endereco endereco;

	@JsonIgnore
	@ManyToMany
	@JoinTable(name = "restaurante_forma_pagamento", joinColumns = @JoinColumn(name = "restaurante_id"), inverseJoinColumns = @JoinColumn(name = "forma_pagamento_id"))
	private List<FormaPagamento> formasPagamento = new ArrayList<>();

	@JsonIgnore
	@OneToMany(mappedBy = "restaurante")
	private List<Produto> produtos = new ArrayList<>();

}
