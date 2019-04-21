package com.maquinadebusca.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maquinadebusca.app.model.service.UserService;

@RestController
@RequestMapping ("/usuario") // URL: http://localhost:8080/coletor
@SuppressWarnings({ "rawtypes", "unchecked" })
public class Usuario {

	@Autowired
	UserService us;
	
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping(value = "/administrador", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity listarAdmin() {
		return new ResponseEntity(us.getAdmin(), HttpStatus.OK );
	}
}
