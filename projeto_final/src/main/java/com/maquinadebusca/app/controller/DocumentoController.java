package com.maquinadebusca.app.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maquinadebusca.app.mensagem.Mensagem;
import com.maquinadebusca.app.model.Documento;
import com.maquinadebusca.app.model.service.DocumentoService;

@SuppressWarnings({ "rawtypes", "unchecked" })
@RestController
@RequestMapping("/documento") // URL: http://localhost:8080/documento
public class DocumentoController {

	@Autowired
	DocumentoService ds;


	// URL: http://localhost:8080/documento
	@GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity listarDocumento() {
		return new ResponseEntity(ds.getDocumento(), HttpStatus.OK);
	}

	// Request for: http://localhost:8080/documento/{id}
	@GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity listarDocumento(@PathVariable(value = "id") Long id) {
		return new ResponseEntity(ds.getDocumento(id), HttpStatus.OK);
	}

	// Request for: http://localhost:8080/documento
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping(value = "/", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity removerDoc(@RequestBody @Valid Documento doc, BindingResult resultado) {
		ResponseEntity resposta = null;
		if (resultado.hasErrors()) {
			resposta = new ResponseEntity(
					new Mensagem("erro", "os dados sobre o documento  não foram informados corretamente"),
					HttpStatus.BAD_REQUEST);
		} else {
			doc = ds.removerDoc(doc);
			if (doc != null) {
				resposta = new ResponseEntity(new Mensagem("sucesso", "documento removido com suceso"), HttpStatus.OK);
			} else {
				resposta = new ResponseEntity(
						new Mensagem("erro", "não foi possível remover o documento informado no banco de dados"),
						HttpStatus.NOT_ACCEPTABLE);
			}
		}
		return resposta;
	}

	// Request for: http://localhost:8080/documento/{id}
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity removerDoc(@PathVariable(value = "id") Long id) {
		ResponseEntity resposta = null;
		if ((id != null) && (id <= 0)) {
			resposta = new ResponseEntity(
					new Mensagem("erro", "os dados sobre o documento  não foram informados corretamente"),
					HttpStatus.BAD_REQUEST);
		} else {
			boolean resp = ds.removerDoc(id);
			if (resp == true) {
				resposta = new ResponseEntity(new Mensagem("sucesso", "documento removido com suceso"), HttpStatus.OK);
			} else {
				resposta = new ResponseEntity(
						new Mensagem("erro", "não foi possível remover o documento informado no banco de dados"),
						HttpStatus.NOT_ACCEPTABLE);
			}
		}
		return resposta;
	}

	// Request for: http://localhost:8080/documento/encontrar/{url}
	@GetMapping(value = "/encontrar/{url}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity encontrarDocumento(@PathVariable(value = "url") String url) {
		return new ResponseEntity(ds.encontrarDocumento(url), HttpStatus.OK);
	}
	
}
