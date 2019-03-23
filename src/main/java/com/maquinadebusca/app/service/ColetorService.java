package com.maquinadebusca.app.service;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.maquinadebusca.app.model.Documento;
import com.maquinadebusca.app.model.Host;
import com.maquinadebusca.app.model.Link;
import com.maquinadebusca.app.repository.DocumentoRepository;
import com.maquinadebusca.app.repository.HostRepository;
import com.maquinadebusca.app.repository.LinkRepository;

@Service
public class ColetorService {
	@Autowired
	private DocumentoRepository dr;
	@Autowired
	private LinkRepository lr;
	@Autowired
	private HostRepository hr;
	@Autowired
	private HostService hostService;
	
	
	List<String> sementes = new LinkedList();

	public List<Documento> executar() {
		List<Documento> documentos = new LinkedList();
		try {
			sementes.add("https://www.youtube.com/");
			sementes.add("https://www.facebook.com/");
			sementes.add("https://www.twitter.com/");
			while (!sementes.isEmpty()) {
				documentos.add(this.coletar(sementes.remove(0)));
			}
		} catch (Exception e) {
			System.out.println("\n\n\n Erro ao executar o serviço de coleta! \n\n\n");
			e.printStackTrace();
		}
		return documentos;
	}

	public Documento coletar(String urlDocumento) {
		Documento documento = new Documento();

		try {
			Link link = new Link();
			Document d = Jsoup.connect(urlDocumento).get();
			Elements urls = d.select("a[href]");
			documento.setUrl(urlDocumento);
			documento.setTexto(d.html());
			documento.setVisao(d.text());
			link.setUrl(urlDocumento);
			link.setUltimaColeta(LocalDateTime.now());
			link.setHost(hostService.obterHostPorUrl(urlDocumento));
			documento.addLink(link);
			int i = 0;
			for (Element url : urls) {
				i++;
				String u = url.attr("abs:href");
				if ((!u.equals("")) && (u != null)) {
					link = new Link();
					link.setUrl(u);
					link.setHost(hostService.obterHostPorUrl(u));
					link.setUltimaColeta(null);
					documento.addLink(link);
				}
			}
			System.out.println("Número de links coletados: " + i);
			System.out.println("Tamanho da lista links: " + documento.getLinks().size());
			//Salvar o documento no banco de dados.
			documento = dr.save(documento);
			//1. Altere o projeto, para que ele colete as novas URLs identificadas em cada página. 
			if(sementes.isEmpty())
				sementes = lr.obterUrlsNaoColetadas();
		} catch (Exception e) {
			System.out.println("\n\n\n Erro ao coletar a página! \n\n\n");
			e.printStackTrace();
		}
		return documento;
	}

	public List<Documento> getDocumento() {
		Iterable<Documento> documentos = dr.findAll();
		List<Documento> resposta = new LinkedList();
		for (Documento documento : documentos) {
			resposta.add(documento);
		}
		return resposta;
	}

	public Documento getDocumento(long id) {
		Documento documento = dr.findById(id);
		return documento;
	}

	public List<Link> getLink() {
		Iterable<Link> links = lr.findAll();
		List<Link> resposta = new LinkedList();
		for (Link link : links) {
			resposta.add(link);
		}
		return resposta;
	}

	public Link getLink(long id) {
		Link link = lr.findById(id);
		return link;
	}
	
	public List<Host> getHost() {
		Iterable<Host> hosts = hr.findAll();
		List<Host> resposta = new LinkedList();
		for (Host host : hosts) {
			resposta.add(host);
		}
		return resposta;
	}

	public Host getHost(long id) {
		Host host = hr.findById(id);
		return host;
	}
	
}