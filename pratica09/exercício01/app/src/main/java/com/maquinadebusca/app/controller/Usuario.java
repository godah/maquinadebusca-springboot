package com.maquinadebusca.app.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.maquinadebusca.app.mensagem.Mensagem;
import com.maquinadebusca.app.model.RoleEnum;
import com.maquinadebusca.app.model.Users;
import com.maquinadebusca.app.model.service.UserService;



@RestController
@RequestMapping("/usuario") // URL: http://localhost:8080/usuario
@SuppressWarnings({ "rawtypes", "unchecked" })
public class Usuario {

	@Autowired
	UserService us;

	// URL: http://localhost:8080/usuario/administrador
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping(value = "/administrador", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity listarAdmin() {
		return new ResponseEntity(us.getAdmin(), HttpStatus.OK);
	}

	// URL: http://localhost:8080/usuario/usuario
	@PreAuthorize("hasRole('USER')")
	@GetMapping(value = "/usuario", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity listarUsuario() {
		return new ResponseEntity(us.getUser(), HttpStatus.OK);
	}

	// URL: http://localhost:8080/usuario/administrador/{id}
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping(value = "/administrador/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity listarAdmin(@PathVariable(value = "id") long id) {
		return new ResponseEntity(us.getAdmin(id), HttpStatus.OK);
	}

	// URL: http://localhost:8080/usuario/administrador/{id}
	@PreAuthorize("hasRole('USER')")
	@GetMapping(value = "/usuario/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity listarUsuario(@PathVariable(value = "id") long id) {
		return new ResponseEntity(us.getUser(id), HttpStatus.OK);
	}

	// Request for: http://localhost:8080/usuario/administrador
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping(value = "/administrador", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)	
	public ResponseEntity inserirAdmin(@RequestBody @Valid Users users, BindingResult resultado) {
		ResponseEntity resposta = null;
		if (resultado.hasErrors()) {
			resposta = new ResponseEntity(
					new Mensagem("erro", "os dados sobre o usuario  não foram informados corretamente"),
					HttpStatus.BAD_REQUEST);
		} else {
			users = us.salvarUser(users);
			if ((users != null) && (users.getId() > 0)) {
				resposta = new ResponseEntity(users, HttpStatus.OK);
			} else {
				resposta = new ResponseEntity(
						new Mensagem("erro", "não foi possível inserir o usuario informado no banco de dados"),
						HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		return resposta;
	}

	// Request for: http://localhost:8080/usuario/administrador
	@PreAuthorize("hasRole('USER')")
	@PostMapping(value = "/usuario", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity inserirUser(@RequestBody @Valid Users users, BindingResult resultado) {
		ResponseEntity resposta = null;
		if (resultado.hasErrors() || users.getAuthorities().getAuthority().equals(RoleEnum.ADMIN.getLabel())) {
			resposta = new ResponseEntity(
					new Mensagem("erro", "os dados sobre o usuario  não foram informados corretamente"),
					HttpStatus.BAD_REQUEST);
		} else {
			users = us.salvarUser(users);
			if ((users != null) && (users.getId() > 0)) {
				resposta = new ResponseEntity(users, HttpStatus.OK);
			} else {
				resposta = new ResponseEntity(
						new Mensagem("erro", "não foi possível inserir o usuario informado no banco de dados"),
						HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		return resposta;
	}

	
	// Request for: http://localhost:8080/usuario/usuario
	@DeleteMapping(value = "/usuario", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity removerUser(@RequestBody @Valid Users user, BindingResult resultado) {
		ResponseEntity resposta = null;
		
		if (!us.loggedUserIsAdmin(SecurityContextHolder.getContext()) && us.isAdmin(user.getId().intValue())) {
			resposta = new ResponseEntity(
					new Mensagem("erro", "remoção não autorizada, verifique as permissões de acesso."),
					HttpStatus.UNAUTHORIZED);
		}
		
		if (resultado.hasErrors()) {
			resposta = new ResponseEntity(
					new Mensagem("erro", "os dados sobre o usuario  não foram informados corretamente"),
					HttpStatus.BAD_REQUEST);
		} else {
			user = us.removerUser(user);
			if (user != null) {
				resposta = new ResponseEntity(new Mensagem("sucesso", "usuario removido com suceso"), HttpStatus.OK);
			} else {
				resposta = new ResponseEntity(
						new Mensagem("erro", "não foi possível remover o usuario informado no banco de dados"),
						HttpStatus.NOT_ACCEPTABLE);
			}
		}
		return resposta;
	}

	// Request for: http://localhost:8080/usuario/usuario
	@DeleteMapping(value = "/usuario/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity removerUsuario(@PathVariable(value = "id") Long id) {
		ResponseEntity resposta = null;
		if (!us.loggedUserIsAdmin(SecurityContextHolder.getContext()) && us.isAdmin(id.intValue())) {
			resposta = new ResponseEntity(
					new Mensagem("erro", "remoção não autorizada, verifique as permissões de acesso."),
					HttpStatus.UNAUTHORIZED);
		}
		
		if ((id != null) && (id <= 0)) {
			resposta = new ResponseEntity(
					new Mensagem("erro", "os dados sobre o usuario  não foram informados corretamente"),
					HttpStatus.BAD_REQUEST);
		} else {
			boolean resp = us.removerUser(id);
			if (resp == true) {
				resposta = new ResponseEntity(new Mensagem("sucesso", "usuario removido com suceso"), HttpStatus.OK);
			} else {
				resposta = new ResponseEntity(
						new Mensagem("erro", "não foi possível remover o usuario informado no banco de dados"),
						HttpStatus.NOT_ACCEPTABLE);
			}
		}
		return resposta;
	}
}
