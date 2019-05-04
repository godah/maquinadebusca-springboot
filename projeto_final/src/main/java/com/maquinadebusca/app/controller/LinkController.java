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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maquinadebusca.app.mensagem.Mensagem;
import com.maquinadebusca.app.model.Link;
import com.maquinadebusca.app.model.service.LinkService;

@SuppressWarnings({ "rawtypes", "unchecked" })
@RestController
@RequestMapping("/link") // URL: http://localhost:8080/link
public class LinkController {

	@Autowired
	LinkService ls;

	// URL: http://localhost:8080/link
	@GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity listarLink() {
		return new ResponseEntity(ls.getLink(), HttpStatus.OK);
	}

	// Request for: http://localhost:8080/link/{id}
	@GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity listarLink(@PathVariable(value = "id") Long id) {
		return new ResponseEntity(ls.getLink(id), HttpStatus.OK);
	}

	// Request for: http://localhost:8080/link
	@PostMapping(value = "/", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity inserirLink(@RequestBody @Valid Link link, BindingResult resultado) {
		ResponseEntity resposta = null;
		if (resultado.hasErrors()) {
			resposta = new ResponseEntity(
					new Mensagem("erro", "os dados sobre o link  não foram informados corretamente"),
					HttpStatus.BAD_REQUEST);
		} else {
			link = ls.salvarLink(link);
			if ((link != null) && (link.getId() > 0)) {
				resposta = new ResponseEntity(link, HttpStatus.OK);
			} else {
				resposta = new ResponseEntity(
						new Mensagem("erro", "não foi possível inserir o link informado no banco de dados"),
						HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		return resposta;
	}

	// Request for: http://localhost:8080/link
	@PutMapping(value = "/", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity atualizarLink(@RequestBody @Valid Link link, BindingResult resultado) {
		ResponseEntity resposta = null;
		if (resultado.hasErrors()) {
			resposta = new ResponseEntity(
					new Mensagem("erro", "os dados sobre o link  não foram informados corretamente"),
					HttpStatus.BAD_REQUEST);
		} else {
			link = ls.atualizarLink(link);
			if ((link != null) && (link.getId() > 0)) {
				resposta = new ResponseEntity(link, HttpStatus.OK);
			} else {
				resposta = new ResponseEntity(
						new Mensagem("erro", "não foi possível atualizar o link informado no banco de dados"),
						HttpStatus.NOT_ACCEPTABLE);
			}
		}
		return resposta;
	}

	// Request for: http://localhost:8080/link
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping(value = "/", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity removerLink(@RequestBody @Valid Link link, BindingResult resultado) {
		ResponseEntity resposta = null;
		if (resultado.hasErrors()) {
			resposta = new ResponseEntity(
					new Mensagem("erro", "os dados sobre o link  não foram informados corretamente"),
					HttpStatus.BAD_REQUEST);
		} else {
			link = ls.removerLink(link);
			if (link != null) {
				resposta = new ResponseEntity(new Mensagem("sucesso", "link removido com suceso"), HttpStatus.OK);
			} else {
				resposta = new ResponseEntity(
						new Mensagem("erro", "não foi possível remover o link informado no banco de dados"),
						HttpStatus.NOT_ACCEPTABLE);
			}
		}
		return resposta;
	}

	// Request for: http://localhost:8080/link
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity removerLink(@PathVariable(value = "id") Long id) {
		ResponseEntity resposta = null;
		if ((id != null) && (id <= 0)) {
			resposta = new ResponseEntity(
					new Mensagem("erro", "os dados sobre o link  não foram informados corretamente"),
					HttpStatus.BAD_REQUEST);
		} else {
			boolean resp = ls.removerLink(id);
			if (resp == true) {
				resposta = new ResponseEntity(new Mensagem("sucesso", "link removido com suceso"), HttpStatus.OK);
			} else {
				resposta = new ResponseEntity(
						new Mensagem("erro", "não foi possível remover o link informado no banco de dados"),
						HttpStatus.NOT_ACCEPTABLE);
			}
		}
		return resposta;
	}

	// Request for: http://localhost:8080/link/encontrar/{url}
	@GetMapping(value = "/encontrar/{url}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity encontrarLink(@PathVariable(value = "url") String url) {
		return new ResponseEntity(ls.encontrarLinkUrl(url), HttpStatus.OK);
	}

	// Request for: http://localhost:8080/link/ordemAlfabetica
	@GetMapping(value = "/ordemAlfabetica", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity listarEmOrdemAlfabetica() {
		return new ResponseEntity(ls.listarEmOrdemAlfabetica(), HttpStatus.OK);
	}

}
