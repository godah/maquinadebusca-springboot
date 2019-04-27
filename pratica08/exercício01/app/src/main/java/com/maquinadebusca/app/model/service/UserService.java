package com.maquinadebusca.app.model.service;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.maquinadebusca.app.model.Authorities;
import com.maquinadebusca.app.model.RoleEnum;
import com.maquinadebusca.app.model.Users;
import com.maquinadebusca.app.model.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	UserRepository ur;
	
	public List<Users> getAdmin(){
		Iterable<Users> users = ur.findAll();
		List<Users> resposta = new LinkedList<>();
		for (Users user : users) {
			if(RoleEnum.ADMIN.getLabel().equals(user.getAuthorities().getAuthority()))
				resposta.add(user);
		}
		return resposta;
	}
	
	
	public List<Users> getUser(){
		Iterable<Users> users = ur.findAll();
		List<Users> resposta = new LinkedList<>();
		for (Users user : users) {
			if(RoleEnum.USER.getLabel().equals(user.getAuthorities().getAuthority()))
				resposta.add(user);
		}
		return resposta;
	}
	
	public List<Users> getAll(){
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
		if(RoleEnum.ADMIN.getLabel().equals(user.getAuthorities().getAuthority()))
			return user;
		return null;
	}
	
	public Users getUser(long id) {
		Users user = ur.findById(id);
		if(RoleEnum.USER.getLabel().equals(user.getAuthorities().getAuthority()))
			return user;
		return null;
	}
	
	public Users salvarUser(Users user) {
		try {
		      return ur.save (user);
		    } catch (Exception e) {
		      System.out.println ("\n>>> Não foi possível salvar o usuario informado no banco de dados.\n");
		      e.printStackTrace ();
		    }
		return null;
	}
	
	public Users deleteUser(Users user) {
		Users usuario = user;
		try {
		      ur.delete(user);
		      return usuario;
		    } catch (Exception e) {
		      System.out.println ("\n>>> Não foi possível remover o usuario informado no banco de dados.\n");
		      e.printStackTrace ();
		    }
		return null;
	}

}
