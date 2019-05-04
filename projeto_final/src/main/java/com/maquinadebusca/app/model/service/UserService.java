package com.maquinadebusca.app.model.service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.maquinadebusca.app.model.RoleEnum;
import com.maquinadebusca.app.model.Users;
import com.maquinadebusca.app.model.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	UserRepository ur;

	public List<Users> getAdmin() {
		Iterable<Users> users = ur.findAll();
		List<Users> resposta = new LinkedList<>();
		for (Users user : users) {
			if (RoleEnum.ADMIN.getLabel().equals(user.getAuthorities().getAuthority()))
				resposta.add(user);
		}
		return resposta;
	}

	public List<Users> getUser() {
		Iterable<Users> users = ur.findAll();
		List<Users> resposta = new LinkedList<>();
		for (Users user : users) {
			if (RoleEnum.USER.getLabel().equals(user.getAuthorities().getAuthority()))
				resposta.add(user);
		}
		return resposta;
	}

	public List<Users> getAll() {
		Iterable<Users> users = ur.findAll();
		List<Users> resposta = new LinkedList<>();
		for (Users user : users) {
			resposta.add(user);
		}
		return resposta;
	}

	public Users get(long id) {
		Users user = ur.findById(id);
		return user;
	}

	public Users getAdmin(long id) {
		Users user = ur.findById(id);
		if (RoleEnum.ADMIN.getLabel().equals(user.getAuthorities().getAuthority()))
			return user;
		return null;
	}

	public Users getUser(long id) {
		Users user = ur.findById(id);
		if (RoleEnum.USER.getLabel().equals(user.getAuthorities().getAuthority()))
			return user;
		return null;
	}

	public Users salvarUser(Users user) {
		try {
			return ur.save(user);
		} catch (Exception e) {
			System.out.println("\n>>> Não foi possível salvar o usuario informado no banco de dados.\n");
			e.printStackTrace();
		}
		return null;
	}

	public Users deleteUser(Users user) {
		Users usuario = user;
		try {
			ur.delete(user);
			return usuario;
		} catch (Exception e) {
			System.out.println("\n>>> Não foi possível remover o usuario informado no banco de dados.\n");
			e.printStackTrace();
		}
		return null;
	}

	public boolean removerUser(Long id) {
		boolean resp = false;
		try {
			ur.deleteById(id);
			resp = true;
		} catch (Exception e) {
			System.out.println("\n>>> Não foi possível remover o usuario informado no banco de dados.\n");
			e.printStackTrace();
		}
		return resp;
	}

	public Users removerUser(Users user) {
		try {
			ur.delete(user);
		} catch (Exception e) {
			user = null;
			System.out.println("\n>>> Não foi possível remover o usuario informado no banco de dados.\n");
			e.printStackTrace();
		}
		return user;
	}

	public Boolean loggedUserIsAdmin(SecurityContext securityContext) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		String role = userDetails.getAuthorities().iterator().next().toString();
		return RoleEnum.ADMIN.getLabel().equals(role);
	}

	public Boolean isAdmin(Integer id) {
		Users userDb = ur.findById(id);
		return userDb.getAuthorities().getAuthority().equals(RoleEnum.ADMIN.getLabel());
	}

	public List<Users> encontrarUsuario(String username) {
		List<Users> usuarios = ur.findByUsernameIgnoreCaseContaining(username);
		List<Users> retorno = new ArrayList<>();
		for (Users user : usuarios) {
			if(RoleEnum.USER.getLabel().equals(user.getAuthorities().getAuthority()))
				retorno.add(user);
		}
		return retorno;
	}
	
	public List<Users> encontrarTodos(String username) {
		return ur.findByUsernameIgnoreCaseContaining(username);
	}
}
