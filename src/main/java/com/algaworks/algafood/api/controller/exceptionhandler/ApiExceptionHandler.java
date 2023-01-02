package com.algaworks.algafood.api.controller.exceptionhandler;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.hibernate.PropertyValueException;
import org.hibernate.exception.DataException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.exception.NegocioException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException.Reference;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.PropertyBindingException;

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

	private static final String MSG_ERRO_GENERICO_USUARIO_FINAL = "Ocorreu um erro interno inesperado no sistema."
			+ " Tente novamente e se o problema persistir, entre em contato com o administrador do sistema.";

	@ExceptionHandler(EntidadeNaoEncontradaException.class)
	public ResponseEntity<?> handleEntidadeNaoEncontrada(EntidadeNaoEncontradaException ex, WebRequest request) {
		HttpStatus status = HttpStatus.NOT_FOUND;
		ProblemType problemType = ProblemType.RECURSO_NAO_ENCONTRADO;
		String detail = ex.getMessage();
		Problem problem = createProblemBuilder(status, problemType, detail).userMessage(detail).build();
		return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
	}

	@ExceptionHandler(NegocioException.class)
	public ResponseEntity<?> handleNegocio(NegocioException ex, WebRequest request) {
		HttpStatus status = HttpStatus.BAD_REQUEST;
		String detail = ex.getMessage();
		Problem problem = createProblemBuilder(status, ProblemType.ERRO_NEGOCIO, detail).userMessage(detail).build();
		return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
	}

	@ExceptionHandler(EntidadeEmUsoException.class)
	public ResponseEntity<?> handleEntidadeEmUso(EntidadeEmUsoException ex, WebRequest request) {
		HttpStatus status = HttpStatus.CONFLICT;
		String detail = ex.getMessage();
		Problem problem = createProblemBuilder(status, ProblemType.ENTIDADE_EM_USO, detail).userMessage(detail).build();
		return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
	}

	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		Throwable rootCause = ExceptionUtils.getRootCause(ex);
		if (rootCause instanceof InvalidFormatException) {
			return handleInvalidFormat((InvalidFormatException) rootCause, headers, status, request);
		} else if (rootCause instanceof PropertyBindingException) {
			return handlePropertyBinding((PropertyBindingException) rootCause, headers, status, request);
		} else if (rootCause instanceof JsonParseException) {
			return handleJsonParseException((JsonParseException) rootCause, headers, status, request);
		}
		String detail = "O corpo da requisição está inválido. Verifique erro de sintaxe.";
		Problem problem = createProblemBuilder(status, ProblemType.MENSAGEM_INCOMPREENSIVEL, detail)
				.userMessage(MSG_ERRO_GENERICO_USUARIO_FINAL).build();
		return handleExceptionInternal(ex, problem, headers, status, request);
	}

	private ResponseEntity<Object> handleJsonParseException(JsonParseException ex, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		try {
			String detail = String.format("Erro em propriedade '%s'. O valor atribuido (%s) é inválido.",
					ex.getProcessor().getCurrentName(), ex.getLocation().getLineNr());
			Problem problem = createProblemBuilder(status, ProblemType.MENSAGEM_INCOMPREENSIVEL, detail)
					.userMessage(detail).build();
			return handleExceptionInternal(ex, problem, headers, status, request);
		} catch (IOException e) {
			Problem problem = createProblemBuilder(status, ProblemType.ERRO_DE_SISTEMA,
					"Erro na conversão de formatos.").userMessage(MSG_ERRO_GENERICO_USUARIO_FINAL).build();
			return handleExceptionInternal(e, problem, headers, status, request);
		}
	}

	private ResponseEntity<Object> handlePropertyBinding(PropertyBindingException ex, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		String path = joinPath(ex.getPath());
		String detail = String.format(
				"A propriedade '%s' é inválida na entidadade %s. Por favor, envie uma requisição válida e tente novamente.",
				path, ex.getReferringClass().getSimpleName());
		Problem problem = createProblemBuilder(status, ProblemType.MENSAGEM_INCOMPREENSIVEL, detail).userMessage(detail)
				.build();
		return handleExceptionInternal(ex, problem, headers, status, request);
	}

	private ResponseEntity<Object> handleInvalidFormat(InvalidFormatException ex, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		String path = joinPath(ex.getPath());
		ProblemType problemType = ProblemType.MENSAGEM_INCOMPREENSIVEL;
		String detail = String.format(
				"A propriedade '%s' recebeu o valor '%s', "
						+ "que é de um tipo inválido. Corrija e informe um valor com o tipo %s.",
				path, ex.getValue(), ex.getTargetType().getSimpleName());

		createProblemBuilder(status, problemType, detail);
		Problem problem = createProblemBuilder(status, problemType, detail).userMessage(detail).build();

		return handleExceptionInternal(ex, problem, headers, status, request);
	}

	private String joinPath(List<Reference> references) {
		return references.stream().map(ref -> ref.getFieldName()).collect(Collectors.joining("."));
	}

	@Override
	protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		ProblemType problemType = ProblemType.RECURSO_NAO_ENCONTRADO;
		String detail = String.format("O recurso %s, que você tentou acessar é inexistente. Método usado: %s.",
				ex.getRequestURL(), ex.getHttpMethod());
		Problem problem = createProblemBuilder(status, problemType, detail).userMessage(detail).build();
		return handleExceptionInternal(ex, problem, headers, status, request);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Object> handleUncaught(Exception ex, WebRequest request) {
		HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
		String detail = MSG_ERRO_GENERICO_USUARIO_FINAL;
		ex.printStackTrace();
		Problem problem = createProblemBuilder(status, ProblemType.ERRO_DE_SISTEMA, detail)
				.userMessage(MSG_ERRO_GENERICO_USUARIO_FINAL).build();
		return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

//		BindingResult bindingResult = ex.getBindingResult();
		List<Problem.Field> problemFields = ex.getFieldErrors().stream().map(fieldError -> Problem.Field.builder()
				.name(fieldError.getField()).userMessag(fieldError.getDefaultMessage()).build()).toList();

//		String fiel = problemFields.stream().filter(field -> field.getName().length() < 5).map(t -> t.getName())
//				.collect(Collectors.joining());
//		System.out.println(fiel);

		String detail = String.format("O recurso %s não pode ter a propriedade '%s' nula.", ex.getObjectName(),
				ex.getFieldError().getField());
		Problem problem = createProblemBuilder(status, ProblemType.DADOS_INVALIDOS, detail).userMessage(detail)
				.fields(problemFields).build();
		return handleExceptionInternal(ex, problem, headers, status, request);
	}

	/* --------------- DataIntegrity e suas causas. ---------------- */
	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<?> handleDataIntegrityViolation(DataIntegrityViolationException ex, WebRequest request) {
		HttpStatus status = HttpStatus.BAD_REQUEST;
		HttpHeaders headers = new HttpHeaders();
		Throwable root = ExceptionUtils.getRootCause(ex);
		if (root instanceof SQLIntegrityConstraintViolationException) {
			return handleSQLIntegrityConstraintViolation((SQLIntegrityConstraintViolationException) root, headers,
					status, request);
		} else if (root instanceof PropertyValueException) {
			return handlePropertyValue((PropertyValueException) root, headers, status, request);
		} else if (root instanceof DataException) {
			return handleDataException((DataException) root, headers, status, request);
		} else if (root instanceof SQLException) {
			return handleSQLException((SQLException) root, headers, status, request);
		}
		String detail = "Ocorreu um erro de violação de integridade de dados";
		Problem problem = createProblemBuilder(status, ProblemType.INTEGRIDADE_DE_DADOS, detail).userMessage(detail)
				.build();

		return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
	}

	private ResponseEntity<?> handleSQLException(SQLException ex, HttpHeaders headers, HttpStatus status,
			WebRequest request) {
		String detail = String.format("%d", ex.getLocalizedMessage());
		var problem = createProblemBuilder(status, ProblemType.INTEGRIDADE_DE_DADOS, detail).userMessage(detail)
				.build();
		return handleExceptionInternal(ex, problem, headers, status, request);
	}

	private ResponseEntity<?> handleDataException(DataException ex, HttpHeaders headers, HttpStatus status,
			WebRequest request) {
		String detail = String.format("Consulta inválida em '%s'.", ex.getSQLException().getSQLState());
		Problem problem = createProblemBuilder(status, ProblemType.VALOR_INVALIDO, detail).userMessage(detail).build();
		return handleExceptionInternal(ex, problem, headers, status, request);
	}

	private ResponseEntity<?> handlePropertyValue(PropertyValueException ex, HttpHeaders headers, HttpStatus status,
			WebRequest request) {
		String detail = String.format("A propriedade '%s' não pode estar nula.", ex.getPropertyName());
		Problem problem = createProblemBuilder(status, ProblemType.VALOR_INVALIDO, detail).userMessage(detail).build();
		return handleExceptionInternal(ex, problem, headers, status, request);
	}

	private ResponseEntity<?> handleSQLIntegrityConstraintViolation(SQLIntegrityConstraintViolationException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		String detail = String.format("Houve um erro '%s'. Por valor, altere e tente novamente.",
				ex.getLocalizedMessage());
		Problem problem = createProblemBuilder(status, ProblemType.DADOS_INVALIDOS, detail).userMessage(detail).build();
		return handleExceptionInternal(ex, problem, headers, status, request);
	}

	@Override
	protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		if (body == null) {
			body = Problem.builder().title(status.getReasonPhrase()).status(status.value())
					.userMessage(MSG_ERRO_GENERICO_USUARIO_FINAL).timeStamp(LocalDateTime.now()).build();
		} else if (body instanceof String)
			body = Problem.builder().title((String) body).status(status.value()).build();
		return super.handleExceptionInternal(ex, body, headers, status, request);
	}

	private Problem.ProblemBuilder createProblemBuilder(HttpStatus status, ProblemType problemType, String detail) {
		return Problem.builder().status(status.value()).type(problemType.getUri()).title(problemType.getTitle())
				.detail(detail).timeStamp(LocalDateTime.now());
	}

	@Override
	protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		if (ex instanceof MethodArgumentTypeMismatchException) {
			return handleMethodArgumentTypeMismatch((MethodArgumentTypeMismatchException) ex, headers, status, request);
		}
		return super.handleTypeMismatch(ex, headers, status, request);
	}

	private ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		String detail = String.format(
				"O parâmetro de URL '%s' recebeu o valor '%s', que é de um tipo inválido. Corrija e informe um valor compátivel com o tipo %s.",
				ex.getName(), ex.getValue(), ex.getRequiredType().getSimpleName());
		Problem problem = createProblemBuilder(status, ProblemType.VALOR_INVALIDO, detail).userMessage(detail).build();
		return handleExceptionInternal(ex, problem, headers, status, request);
	}

}
