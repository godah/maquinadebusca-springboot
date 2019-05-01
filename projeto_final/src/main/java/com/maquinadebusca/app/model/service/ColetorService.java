package com.maquinadebusca.app.model.service;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

import javax.validation.Valid;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.maquinadebusca.app.model.Documento;
import com.maquinadebusca.app.model.Link;
import com.maquinadebusca.app.model.repository.DocumentoRepository;
import com.maquinadebusca.app.model.repository.LinkRepository;

@Service
public class ColetorService {

	@Autowired
	private DocumentoRepository dr;

	@Autowired
	private LinkRepository lr;
	
	@Autowired
	private HostService hostService;
	@Autowired
	private UtilsService utilsService;
	@Autowired
	private RobotsService robotsService;
	
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

	public List<Documento> executar() {
		List<Documento> documentos = new LinkedList<>();

		try {
			sementes.add("https://www.youtube.com/");
			sementes.add("https://www.facebook.com/");
			sementes.add("https://www.twitter.com/");

			while (!sementes.isEmpty()) {
				URL url = new URL(sementes.get(0));
				utilsService.verificaColetaConsecultiva(urlStringAnterior, url);
				if(robotsService.verificaPermissaoRobots(url)) {
					documentos.add(this.coletar(sementes.get(0)));
					urlStringAnterior = sementes.remove(0);					
				} else {
					sementes.remove(0);
				}
				
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
			documento.setVisao(utilsService.removerPontuacao(d.text()));

			link.setUrl(urlDocumento);
			link.setUltimaColeta(LocalDateTime.now());
			link.setHost(hostService.obterHostPorUrl(urlDocumento));
			link.addDocumento(documento);
			documento.addLink(link);
			int i = 0;
			for (Element url : urls) {
				i++;
				String u = url.attr("abs:href");
				if ((!u.equals("")) && (u != null)) {
					link = lr.findByUrl(u);
					if (link == null) {
						link = new Link();
						link.setUrl(u);
						link.setHost(hostService.obterHostPorUrl(u));
						link.setUltimaColeta(null);
					}
					link.addDocumento(documento);
					documento.addLink(link);
				}
			}
			System.out.println("Número de links coletados: " + i);
			System.out.println("Tamanho da lista links: " + documento.getLinks().size());
			// 1. Altere o projeto, para que ele colete as novas URLs identificadas em cada página.
			if (sementes.isEmpty()) {
				sementes = lr.obterUrlsNaoColetadas();
				sementes = utilsService.removeElementosRepetidos(sementes);
			}
			// Salvar o documento no banco de dados.
			documento = dr.save(documento);
		} catch (Exception e) {
			System.out.println("\n\n\n Erro ao coletar a página! \n\n\n");
			e.printStackTrace();
		}
		return documento;
	}

	public List<Documento> getDocumento() {
		Iterable<Documento> documentos = dr.findAll();
		List<Documento> resposta = new LinkedList<>();
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
	
	public boolean removerDoc(Long id) {
		boolean resp = false;
		try {
			dr.deleteById(id);
			resp = true;
		} catch (Exception e) {
			System.out.println("\n>>> Não foi possível remover o documento informado no banco de dados.\n");
			e.printStackTrace();
		}
		return resp;
	}

	public @Valid Documento removerDoc(@Valid Documento doc) {
		try {
			dr.delete(doc);
		} catch (Exception e) {
			doc = null;
			System.out.println("\n>>> Não foi possível remover o documento informado no banco de dados.\n");
			e.printStackTrace();
		}
		return doc;
	}

}
