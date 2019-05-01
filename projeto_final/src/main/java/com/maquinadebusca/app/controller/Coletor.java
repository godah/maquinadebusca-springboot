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
import com.maquinadebusca.app.model.Documento;
import com.maquinadebusca.app.model.Link;
import com.maquinadebusca.app.model.service.ColetorService;
import com.maquinadebusca.app.model.service.HostService;

@SuppressWarnings({ "rawtypes", "unchecked" })
@RestController
@RequestMapping("/coletor") // URL: http://localhost:8080/coletor
public class Coletor {

	@Autowired
	ColetorService cs;
	
	@Autowired
	HostService hs;

	// URL: http://localhost:8080/coletor/iniciar
	@GetMapping(value = "/iniciar", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity iniciar() {
		return new ResponseEntity(cs.executar(), HttpStatus.OK);
	}

	// URL: http://localhost:8080/coletor/documento
	@GetMapping(value = "/documento", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity listarDocumento() {
		return new ResponseEntity(cs.getDocumento(), HttpStatus.OK);
	}

	// Request for: http://localhost:8080/coletor/documento/{id}
	@GetMapping(value = "/documento/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity listarDocumento(@PathVariable(value = "id") Long id) {
		return new ResponseEntity(cs.getDocumento(id), HttpStatus.OK);
	}
	
	// URL: http://localhost:8080/coletor/host
	@GetMapping(value = "/host", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity listarHost() {
		return new ResponseEntity(hs.getHost(), HttpStatus.OK);
	}

	// Request for: http://localhost:8080/coletor/host/{id}
	@GetMapping(value = "/host/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity listarHost(@PathVariable(value = "id") long id) {
		return new ResponseEntity(hs.getHost(id), HttpStatus.OK);
	}

	// Request for: http://localhost:8080/coletor/documento
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping(value = "/documento", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity removerDoc(@RequestBody @Valid Documento doc, BindingResult resultado) {
		ResponseEntity resposta = null;
		if (resultado.hasErrors()) {
			resposta = new ResponseEntity(
					new Mensagem("erro", "os dados sobre o documento  não foram informados corretamente"),
					HttpStatus.BAD_REQUEST);
		} else {
			doc = cs.removerDoc(doc);
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

	// Request for: http://localhost:8080/coletor/link
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping(value = "/documento/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity removerDoc(@PathVariable(value = "id") Long id) {
		ResponseEntity resposta = null;
		if ((id != null) && (id <= 0)) {
			resposta = new ResponseEntity(
					new Mensagem("erro", "os dados sobre o documento  não foram informados corretamente"),
					HttpStatus.BAD_REQUEST);
		} else {
			boolean resp = cs.removerDoc(id);
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

	// URL: http://localhost:8080/coletor/link
	@GetMapping(value = "/link", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity listarLink() {
		return new ResponseEntity(cs.getLink(), HttpStatus.OK);
	}

	// Request for: http://localhost:8080/coletor/link/{id}
	@GetMapping(value = "/link/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity listarLink(@PathVariable(value = "id") Long id) {
		return new ResponseEntity(cs.getLink(id), HttpStatus.OK);
	}

	// Request for: http://localhost:8080/coletor/link
	@PostMapping(value = "/link", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity inserirLink(@RequestBody @Valid Link link, BindingResult resultado) {
		ResponseEntity resposta = null;
		if (resultado.hasErrors()) {
			resposta = new ResponseEntity(
					new Mensagem("erro", "os dados sobre o link  não foram informados corretamente"),
					HttpStatus.BAD_REQUEST);
		} else {
			link = cs.salvarLink(link);
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

	// Request for: http://localhost:8080/coletor/link
	@PutMapping(value = "/link", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity atualizarLink(@RequestBody @Valid Link link, BindingResult resultado) {
		ResponseEntity resposta = null;
		if (resultado.hasErrors()) {
			resposta = new ResponseEntity(
					new Mensagem("erro", "os dados sobre o link  não foram informados corretamente"),
					HttpStatus.BAD_REQUEST);
		} else {
			link = cs.atualizarLink(link);
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

	// Request for: http://localhost:8080/coletor/link
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping(value = "/link", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity removerLink(@RequestBody @Valid Link link, BindingResult resultado) {
		ResponseEntity resposta = null;
		if (resultado.hasErrors()) {
			resposta = new ResponseEntity(
					new Mensagem("erro", "os dados sobre o link  não foram informados corretamente"),
					HttpStatus.BAD_REQUEST);
		} else {
			link = cs.removerLink(link);
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

	// Request for: http://localhost:8080/coletor/link
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping(value = "/link/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity removerLink(@PathVariable(value = "id") Long id) {
		ResponseEntity resposta = null;
		if ((id != null) && (id <= 0)) {
			resposta = new ResponseEntity(
					new Mensagem("erro", "os dados sobre o link  não foram informados corretamente"),
					HttpStatus.BAD_REQUEST);
		} else {
			boolean resp = cs.removerLink(id);
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
}
