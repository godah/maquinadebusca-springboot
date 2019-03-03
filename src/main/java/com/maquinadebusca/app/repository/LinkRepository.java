package com.maquinadebusca.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.maquinadebusca.app.model.Documento;
import com.maquinadebusca.app.model.Link;

public interface LinkRepository extends JpaRepository<Link, Long> {

	@Override
	List<Link> findAll();

	Link findById(long id);

}
