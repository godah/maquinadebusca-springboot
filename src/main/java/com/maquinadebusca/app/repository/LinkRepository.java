package com.maquinadebusca.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.maquinadebusca.app.model.Link;

public interface LinkRepository extends JpaRepository<Link, Long> {

	@Override
	List<Link> findAll();

	Link findById(long id);
	
	@Query(value = "SELECT l.url FROM Link l WHERE l.ultimaColeta IS NULL ")
	List<String> obterUrlsNaoColetadas();

}
