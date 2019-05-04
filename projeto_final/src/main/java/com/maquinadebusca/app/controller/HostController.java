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
import com.maquinadebusca.app.model.Host;
import com.maquinadebusca.app.model.service.HostService;

@SuppressWarnings({ "rawtypes", "unchecked" })
@RestController
@RequestMapping("/host") // URL: http://localhost:8080/host
public class HostController {

	@Autowired
	HostService hs;

	// URL: http://localhost:8080/host
	@GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity listarHost() {
		return new ResponseEntity(hs.getHost(), HttpStatus.OK);
	}

	// Request for: http://localhost:8080/host/{id}
	@GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity listarHost(@PathVariable(value = "id") long id) {
		return new ResponseEntity(hs.getHost(id), HttpStatus.OK);
	}

	// Request for: http://localhost:8080/host/encontrar/{url}
	@GetMapping(value = "/encontrar/{url}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity encontrarHost(@PathVariable(value = "url") String url) {
		return new ResponseEntity(hs.encontrarHost(url), HttpStatus.OK);
	}

	// Request for: http://localhost:8080/host/{id}
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity removerHost(@PathVariable(value = "id") Long id) {
		ResponseEntity resposta = null;
		if ((id != null) && (id <= 0)) {
			resposta = new ResponseEntity(
					new Mensagem("erro", "os dados sobre o host  não foram informados corretamente"),
					HttpStatus.BAD_REQUEST);
		} else {
			boolean resp = hs.removerHost(id);
			if (resp == true) {
				resposta = new ResponseEntity(new Mensagem("sucesso", "host removido com suceso"), HttpStatus.OK);
			} else {
				resposta = new ResponseEntity(
						new Mensagem("erro", "não foi possível remover o host informado no banco de dados"),
						HttpStatus.NOT_ACCEPTABLE);
			}
		}
		return resposta;
	}

	// Request for: http://localhost:8080/host
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping(value = "/", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity removerHost(@RequestBody @Valid Host host, BindingResult resultado) {
		ResponseEntity resposta = null;
		if (resultado.hasErrors()) {
			resposta = new ResponseEntity(
					new Mensagem("erro", "os dados sobre o host  não foram informados corretamente"),
					HttpStatus.BAD_REQUEST);
		} else {
			host = hs.removerHost(host);
			if (host != null) {
				resposta = new ResponseEntity(new Mensagem("sucesso", "host removido com suceso"), HttpStatus.OK);
			} else {
				resposta = new ResponseEntity(
						new Mensagem("erro", "não foi possível remover o host informado no banco de dados"),
						HttpStatus.NOT_ACCEPTABLE);
			}
		}
		return resposta;
	}

	// Request for: http://localhost:8080/host/ordemAlfabetica
	@GetMapping(value = "/ordemAlfabetica", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity listarEmOrdemAlfabetica() {
		return new ResponseEntity(hs.listarEmOrdemAlfabetica(), HttpStatus.OK);
	}
}
