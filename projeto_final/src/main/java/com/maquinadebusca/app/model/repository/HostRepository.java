package com.maquinadebusca.app.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.maquinadebusca.app.model.Host;

public interface HostRepository extends JpaRepository<Host, Long> {

	@Override
	List<Host> findAll();

	Host findById(long id);

	@Query(value = "SELECT h FROM Host h WHERE h.url = ?1 ")
	Host obterPorUrl(String url);

	List<Host> findByUrlIgnoreCaseContaining(String url);

	@Query(value = "SELECT * FROM Host WHERE url = ?1 ORDER BY url", nativeQuery = true)
	List<Host> getInLexicalOrderPorUrl(String url);
	
	@Query(value = "SELECT * FROM Host ORDER BY url", nativeQuery = true)
	List<Host> getInLexicalOrder();
}
