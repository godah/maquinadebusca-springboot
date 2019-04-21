package com.maquinadebusca.app.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.maquinadebusca.app.model.Users;

public interface UserRepository extends JpaRepository<Users, Long> {

  Users findByUsername (String username);
  
  Users findById (long id);
  
}
