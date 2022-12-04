package com.algaworks.algafood.api.model;

import java.util.List;

import com.algaworks.algafood.domain.model.Restaurante;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import lombok.Data;
import lombok.NonNull;
/*Já que queremos customizar o RootElement List do XML (visto que é um CollectionResource, ou seja, não podemos mudar a partir da entidade), podemos criar uma classe wrapper para nosso tipo, e encapsular essa lista nela.
 A partir do nosso dela classe customizaremos o nome do RootElement*/
@JacksonXmlRootElement(localName="Restaurantes") /*Poderia ser usado aqui o JsonRootName, també funcionaria, ambas são equivalentes a mesma classe: XmlRootElement.*/ 
@Data
public class RestauranteXmlWrapper {

	@JsonProperty("Restaurante") /*Aqui definimos o nome da propriedade.*/
	@NonNull /*Definimos a propriedade obrigátorio para o Lombok criar um construtor. Funciona em conjunto com o @Data.*/
	@JacksonXmlElementWrapper(useWrapping = false) /*Como o nome dado a propriedade se repete, visto que no XML temos o encapsulamento, podemos desátiva-lo por organização.*/
	private List<Restaurante> restaurantes;
}
