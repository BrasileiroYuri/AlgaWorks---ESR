package com.algaworks.algafood.infrastructure.repository;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.repository.RestauranteRepositoryQueries;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

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
	public List<Restaurante> find(String nome, BigDecimal taxaFreteInicial,
			BigDecimal taxaFreteFinal) { /*
											 * Criamos aqui uma consulta dinâmica, definida pelos parâmetros passados ao
											 * método. Em JPQL será feito com concatenação de Strings.
											 */
		var jpql = new StringBuilder();
		var parametros = new HashMap<String, Object>(); /*
														 * Usamos esse Map para atribuir os parâmetros, visto que eles
														 * também devem ser dinâmicos. Isso pois, caso seja declarado um
														 * parâmetro sem valor na JQPL ocorrerá uma Exception.
														 */
		jpql.append("from Restaurante where 0=0 "); /*
													 * 0=0 sempre será true. Dessa forma, se nenhum valor for dado, será
													 * retornado todos os objetos do tipo pesquisado, ou seja,
													 * instâncias de Restaurante.
													 */
		if (StringUtils.hasLength(nome)) { /* Aqui verifica-se se o nome está ou vazio, sem caracteres("") ou nulo. */
			jpql.append("and nome like :nome ");
			parametros.put("nome", "%" + nome + "%"); /*
														 * Aqui estamos adicionando um parãmetro com chave e valor,
														 * dependendo dos vindos dos parâmetros do próprio método.
														 */
		}
		if (taxaFreteInicial != null) {
			jpql.append("and taxaFrete >= :taxaInicial ");
			parametros.put("taxaInicial", taxaFreteInicial); /*
																 * Aqui não é usado o Between pois os dois parâmetros
																 * de taxa não são obrigátorios e podem vir sem o outro.
																 * Assim, constrói-se a partir de estruturas (and).
																 */
		}
		if (taxaFreteFinal != null) {
			jpql.append("and taxaFrete <= :taxaFinal");
			parametros.put("taxaFinal", taxaFreteFinal);
		}
		TypedQuery<Restaurante> query = manager.createQuery(jpql.toString(), Restaurante.class);
		parametros.forEach((chave, valor) -> query.setParameter(chave,
				valor)); /*
							 * Nesse laço, para cada chave e valor atribudos no Map, é feito um
							 * setParameter() com os mesmos valores.
							 */
		return query.getResultList();
	}

//	public List<Restaurante> find(String nome, BigDecimal taxaFreteInicial, BigDecimal taxaFreteFinal) {
//		var jpql = "from Restaurante where nome like :nome and taxaFrete between :taxaInicial and :taxaFinal";
//		/*Query JPQL.*/
//		return manager.createQuery(jpql, Restaurante.class)
//				.setParameter("nome", "%" + nome + "%") /*NomeContaining*/ 
//				.setParameter("taxaInicial", taxaFreteInicial).setParameter("taxaFinal", taxaFreteFinal) /*ByTaxaFreteBetween*/
//				.getResultList();
//}
}