package com.maquinadebusca.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maquinadebusca.app.model.Documento;
import com.maquinadebusca.app.service.ColetorService;



@RestController
@RequestMapping("/coletor") // URL: http://localhost:8080/coletor
public class Coletor {
	
	@Autowired
	private ColetorService coletorService;
	// URL: http://localhost:8080/coletor/iniciar
	@GetMapping (value = "/iniciar", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public Documento iniciar () {
		return coletorService.executarColeta();
	}

}