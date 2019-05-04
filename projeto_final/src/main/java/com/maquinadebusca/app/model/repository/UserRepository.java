package com.maquinadebusca.app.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.maquinadebusca.app.model.Users;

public interface UserRepository extends JpaRepository<Users, Long> {

  Users findByUsername (String username);
  
  Users findById (long id);
  
  List<Users> findByUsernameIgnoreCaseContaining(String username);
}
