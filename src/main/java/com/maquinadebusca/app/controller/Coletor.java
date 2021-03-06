package com.maquinadebusca.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maquinadebusca.app.service.ColetorService;

@RestController
@RequestMapping("/coletor") // URL: http://localhost:8080/coletor
public class Coletor {
	@Autowired
	ColetorService cs;
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
	public ResponseEntity listarDocumento(@PathVariable(value = "id") long id) {
		return new ResponseEntity(cs.getDocumento(id), HttpStatus.OK);
	}

// URL: http://localhost:8080/coletor/link
	@GetMapping(value = "/link", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity listarLink() {
		return new ResponseEntity(cs.getLink(), HttpStatus.OK);
	}

// Request for: http://localhost:8080/coletor/link/{id}
	@GetMapping(value = "/link/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity listarLink(@PathVariable(value = "id") long id) {
		return new ResponseEntity(cs.getLink(id), HttpStatus.OK);
	}
	

	// URL: http://localhost:8080/coletor/host
		@GetMapping(value = "/host", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
		public ResponseEntity listarHost() {
			return new ResponseEntity(cs.getHost(), HttpStatus.OK);
		}

	// Request for: http://localhost:8080/coletor/host/{id}
		@GetMapping(value = "/host/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
		public ResponseEntity listarHost(@PathVariable(value = "id") long id) {
			return new ResponseEntity(cs.getHost(id), HttpStatus.OK);
		}
}