package com.algaworks.algafood.api.controller.exceptionhandler;

import lombok.Getter;

@Getter
public enum ProblemType {

	RECURSO_NAO_ENCONTRADO("/recurso-nao-encontrado", "Recurso não encontrado."),
	ENTIDADE_EM_USO("/entidade-em-uso", "Entidade em uso."),
	ERRO_NEGOCIO("/erro-negocio", "Violação de regra de negócio"),
	MENSAGEM_INCOMPREENSIVEL("/mensagem-incompreensivel", "Mensagem incompreensível."),
	PARAMETRO_INVALIDO("/parametro-invalido", "Parâmetro inválido."),

	INTEGRIDADE_DE_DADOS("/integridade-de-dados", "Integridade de dados."),
	VALOR_INVALIDO("/valor-invalido", "Valor inválido."),
	VIOLACAO_INTEGRIDADE("/violacao-de-integridade", "Violação de integridade."),

	ERRO_DE_SISTEMA("/erro-de-sistema", "Erro de sistema."), DADOS_INVALIDOS("/dados-invalidos", "Dados inválidos.");

	private String uri;
	private String title;

	private ProblemType(String path, String title) {
		this.uri = "https://algafood.com.br" + path;
		this.title = title;
	}

}
