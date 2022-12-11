package com.algaworks.algafood.infrastructure.repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.repository.RestauranteRepositoryQueries;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

/*Repositório customizado. Para repositórios customizados com SDJ usamos o nome do repositório
com o sufixo Impl. Assim ele identifica a implementação.*/
@Repository
public class RestauranteRepositoryImpl implements RestauranteRepositoryQueries {

	@PersistenceContext
	private EntityManager manager;

	/*
	 * As vezes, é necessário aplicar lógica as consultas. Assim, implementamos com
	 * código Java.
	 */
	@Override
	public List<Restaurante> find(String nome, BigDecimal taxaFreteInicial, BigDecimal taxaFreteFinal) {
		
		Integer[] p = new Integer[1];
		
		/*
		 * Aqui vamos usar Criteria API. Criteria é uma interface/API para criar
		 * consultar dinâmicas e programáticas. Por sua burocracia, é preferível usá-la
		 * em consultas complexas.
		 */
		CriteriaBuilder builder = manager
				.getCriteriaBuilder(); /*
										 * CriteriaBuilder é uma interface que serve como Factory pra construir
										 * elementos de consulta, inclusive o CriteriaQuery, a partir do EntityManager.
										 * Também possui diversos métodos para criar expressões para diversos critérios.
										 * Esses elementos são métodos que indicam algum critério de consulta.
										 */
		CriteriaQuery<Restaurante> criteria = builder.createQuery(
				Restaurante.class);/*
									 * Essa interface é responsável por definir a estrutura de uma Query, ou seja, a
									 * composição das clausulas. Isso é feito por meio de seus métodos (Select, from
									 * etc). Seu Generics define o tipo do resultado(consulta).
									 */
		/*
		 * Cláusulas são instruções dadas ao SQL, As mais comuns são SELECT, FROM e
		 * WHERE. Já os elementos de uma Query são relacionando a atributos/colunas. São
		 * predicados.
		 */
		Root<Restaurante> root = criteria.from(Restaurante.class);
		var predicates = new ArrayList<Predicate>();
		/*
		 * Root significa raiz. Refere-se a entidade que estamos realizando a consulta
		 * (ou tabela, no modelo relacional). Podemos usar essa raiz para criar
		 * predicados(filtros) de quais entidades queremos na Query, com qual atributo.
		 */
		if (StringUtils.hasLength(nome)) {
			predicates.add(builder.like(root.get("nome"), "%" + nome + "%"));
		} /*
			 * Um predicado é um critério de pesquisa, um filtro, construido por atributo
			 * que queremos na Query. Nesse predicado, estamos definindo o critério de busca
			 * (ou filtro) de um determinado atributo, no caso nome.
			 */
		if (taxaFreteInicial != null) {
			predicates.add(builder.greaterThanOrEqualTo(root.get("taxaFrete"), taxaFreteInicial));

		} /*
			 * No primeiro parâmetro, definimos qual atributo de qual entidade queremos usar
			 * na pesquisa. Ex: queremos fazer uma consulta de tal entidade (visto que uma
			 * consulta pode ter várias entidades - tabelas, no modelo relacional -)com tal
			 * atributo, criamos um predicado desse atributo, relacionando com outro. Nesse
			 * segundo, definimos o valor que queremos relacionar, a partir da ação do
			 * método. Ex: greaterThanOrEqualTo, like etc.
			 */
		if (taxaFreteFinal != null) {
			predicates.add(builder.lessThanOrEqualTo(root.get("taxaFrete"), taxaFreteFinal));
		}
		/*
		 * A partir do CriteriaBuilder nós criamos o CriteriaQuery. A partir do
		 * CriteriaQuery nós criamos o Root, e a partir do Root, usando o
		 * CriteriaBuilder criamos um Predicade.
		 */

		criteria.where(predicates.toArray(new Predicate[0])); /* O método Where recebe um ou mais predicados. */
		TypedQuery<Restaurante> query = manager.createQuery(criteria);
		/*
		 * Nessa sobrecarga, não é necessário definir o tipo da Query. Ele já está na
		 * variável de CriteriaQuery.
		 */
		return query.getResultList();
	};
	/*
	 * Aqui começamos a usar CRITERIA API. Para usar o CriteriaQuery, precisamos de'
	 * uma instância de CriteriaBuilder, interface fábrica de elementos de consulta.
	 * Podemos instanciá-lo a partir do EntityManager.getCriteriaBuilder(). Depois,
	 * a partir da instância de CriteriaBuilder chamamos o método CreateQuery.
	 */
}