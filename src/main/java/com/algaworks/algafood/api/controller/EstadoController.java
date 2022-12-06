package com.algaworks.algafood.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.algaworks.algafood.domain.model.Estado;
import com.algaworks.algafood.domain.repository.EstadoRepository;

/*@RestController -> Essa notação é uma junção das duas abaixo. Melhora a semântica ao vermos que trata-se de um controlador na arquitetura Rest.*/
@Controller /*-> Essa notação define que a classe que é responsável por receber requisições, tratá-las e retornar uma resposta.*/
@ResponseBody /*-> Essa notação define que o retorno dos métodos deve ser a resposta da requisição.*/
@RequestMapping("/estados") /*-> Essa notação define que as resquisições enviadas a URN citada serão tratadas por esse Controller.*/
public class EstadoController {

	@Autowired /*-> Apesar da notação @Component estar apenas na implementação, o Spring considera esse tipo genérico como componente, por seu tipo específico estar notado*/
	private EstadoRepository estadoRepository;
	
	@GetMapping
	public List<Estado> listar() {
		return estadoRepository.listar();
	}
		
	 @GetMapping("/{estadoId}")
	 public ResponseEntity<Estado> buscar(@PathVariable Long estadoId) {
		 Estado estado = estadoRepository.buscar(estadoId);
		 if (estado == null) {
			 return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		 }
		 return ResponseEntity.status(HttpStatus.OK).body(estado);
	 }
}
