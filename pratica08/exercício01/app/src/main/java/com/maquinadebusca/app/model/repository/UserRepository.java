package com.maquinadebusca.app.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.maquinadebusca.app.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

  @Override
  List<User> findAll ();

  User findById (long id);

  User findByUsername (String username);
  
  @Override
  User save (User link);

}
