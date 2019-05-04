package com.maquinadebusca.app.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.maquinadebusca.app.model.Users;

public interface UserRepository extends JpaRepository<Users, Long> {

  Users findByUsername (String username);
  
  Users findById (long id);
  
  List<Users> findByUsernameIgnoreCaseContaining(String username);
  
  @Query (value = "SELECT * FROM users WHERE username = ?1 ORDER BY username", nativeQuery = true)
  List<Users> getInLexicalOrderPorNome (String username);

  @Query (value = "SELECT * FROM users ORDER BY username", nativeQuery = true)
  List<Users> getInLexicalOrder ();
}
