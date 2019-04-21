package com.maquinadebusca.app.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.maquinadebusca.app.model.User;
import com.maquinadebusca.app.model.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	UserRepository ur;
	
	

}
