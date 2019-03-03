package com.maquinadebusca.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maquinadebusca.app.model.Documento;
import com.maquinadebusca.app.model.Link;
import com.maquinadebusca.app.service.ColetorService;

@RestController
@RequestMapping("/coletor") // URL: http://localhost:8080/coletor
public class Coletor {
	@Autowired
	ColetorService cs;

	// URL: http://localhost:8080/coletor/iniciar
	@GetMapping(value = "/iniciar", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public List<Documento> iniciar() {
		List<Documento> documentos = cs.executar();
		return documentos;
	}

	// URL: http://localhost:8080/coletor/listar
	@GetMapping(value = "/listar", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public List<Documento> listar() {
		return cs.getDocumentos();
	}

	// Request for: http://localhost:8080/coletor/listar/{id}
	@GetMapping(value = "/listar/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public Documento listar(@PathVariable(value = "id") long id) {
		return cs.getDocumento(id);
	}

	// URL: http://localhost:8080/coletor/link
	@GetMapping(value = "/link", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public List<Link> link() {
		return cs.getLinks();
	}

	// Request for: http://localhost:8080/coletor/link/{id}
	@GetMapping(value = "/link/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public Link link(@PathVariable(value = "id") long id) {
		return cs.getLink(id);
	}
}