package com.algaworks.algafood.api.model;

import java.util.List;

import com.algaworks.algafood.domain.model.Cozinha;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import lombok.Data;
import lombok.NonNull;
/*A anotação abaixo define o nome do elemento raiz do XML*/
@JacksonXmlRootElement(localName = "cozinhas")
@Data
public class CozinhasXmlWrapper {

	@JsonProperty("Cozinha")
	@JacksonXmlElementWrapper(useWrapping = false)
	@NonNull /*O Lombok só cria construtores com propriedades obrigatórias, ou seja, não nulas. Para definir como uma, usamos essa anotação*/
	private List<Cozinha> cozinhas;
}
