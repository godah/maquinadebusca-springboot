package com.maquinadebusca.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maquinadebusca.app.model.service.ColetorService;

@SuppressWarnings({ "rawtypes", "unchecked" })
@RestController
@RequestMapping("/coletor") // URL: http://localhost:8080/coletor
public class ColetorController {

	@Autowired
	ColetorService cs;

	// URL: http://localhost:8080/coletor/iniciar
	@GetMapping(value = "/iniciar", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity iniciar() {
		return new ResponseEntity(cs.executar(), HttpStatus.OK);
	}

}
