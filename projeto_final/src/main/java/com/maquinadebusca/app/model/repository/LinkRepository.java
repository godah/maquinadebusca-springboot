package com.maquinadebusca.app.model.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.maquinadebusca.app.model.Link;

public interface LinkRepository extends JpaRepository<Link, Long> {

	@Override
	List<Link> findAll();

	Link findById(long id);

	Link findByUrl(String url);

	@Override
	Link save(Link link);

	@Query(value = "SELECT l.url FROM Link l WHERE l.ultimaColeta IS NULL ")
	List<String> obterUrlsNaoColetadas();

	List<Link> findByUrlIgnoreCaseContaining(String url);
}
