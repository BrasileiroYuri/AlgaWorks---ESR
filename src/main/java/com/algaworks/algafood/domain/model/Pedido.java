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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.CreationTimestamp;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class Pedido {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Include
	private Long id;

	@Column(nullable = false)
	private BigDecimal subTotal;

	@Column(nullable = false)
	private BigDecimal taxaFrete;

	@Column(nullable = false)
	private BigDecimal valorTotal;

	@Embedded
	private Endereco enderecoEntrega;

	@CreationTimestamp
	@Column(nullable = false)
	private LocalDateTime dataCriacao;

	private LocalDateTime dataConfirmacao;

	private LocalDateTime dataCancelamento;

	private LocalDateTime dataEntrega;

	@JoinColumn(nullable = false)
	@ManyToOne
	private FormaPagamento formaPagamento;

	@JoinColumn(nullable = false)
	@ManyToOne
	private Restaurante restaurante;

	@ManyToOne
	@JoinColumn(name = "usuario_ciente_id", nullable = false)
	private Usuario cliente;

	@Column(nullable = false)
	private StatusPedido statusPedido;

	@OneToMany(mappedBy = "pedido")
	private List<ItemPedido> itens = new ArrayList<>();

}
