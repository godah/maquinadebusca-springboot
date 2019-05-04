package com.maquinadebusca.app.model.service;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.maquinadebusca.app.model.Link;
import com.maquinadebusca.app.model.repository.LinkRepository;

@Service
public class LinkService {

	@Autowired
	private LinkRepository lr;

	String urlStringAnterior = null;
	List<String> sementes = new LinkedList<>();

	public Link salvarLink(Link link) {
		Link l = null;
		try {
			l = lr.save(link);
		} catch (Exception e) {
			System.out.println("\n>>> Não foi possível salvar o link informado no banco de dados.\n");
			e.printStackTrace();
		}
		return l;
	}

	public List<Link> salvarLinks(List<Link> links) {
		List<Link> ls = null;
		try {
			ls = lr.saveAll(links);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ls;
	}

	public Link atualizarLink(Link link) {
		Link l = null;
		try {
			l = lr.save(link);
		} catch (Exception e) {
			System.out.println("\n>>> Não foi possível atualizar o link informado no banco de dados.\n");
			e.printStackTrace();
		}
		return l;
	}

	public List<Link> getLink() {
		Iterable<Link> links = lr.findAll();
		List<Link> resposta = new LinkedList<>();
		for (Link link : links) {
			resposta.add(link);
		}
		return resposta;
	}

	public Link getLink(long id) {
		Link link = lr.findById(id);
		return link;
	}

	public boolean removerLink(Long id) {
		boolean resp = false;
		try {
			lr.deleteById(id);
			resp = true;
		} catch (Exception e) {
			System.out.println("\n>>> Não foi possível remover o link informado no banco de dados.\n");
			e.printStackTrace();
		}
		return resp;
	}

	public Link removerLink(Link link) {
		try {
			lr.delete(link);
		} catch (Exception e) {
			link = null;
			System.out.println("\n>>> Não foi possível remover o link informado no banco de dados.\n");
			e.printStackTrace();
		}
		return link;
	}

	public List<Link> encontrarLinkUrl(String url) {
		return lr.findByUrlIgnoreCaseContaining(url);
	}

}
