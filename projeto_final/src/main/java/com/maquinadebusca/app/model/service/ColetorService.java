package com.maquinadebusca.app.model.service;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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

	public List<Documento> executar() {
		List<Documento> documentos = new LinkedList<>();

		try {
			sementes.add("https://www.youtube.com/");
			sementes.add("https://www.facebook.com/");
			sementes.add("https://www.twitter.com/");

			while (!sementes.isEmpty()) {
				URL url = new URL(sementes.get(0));
				utilsService.verificaColetaConsecultiva(urlStringAnterior, url);
				if (robotsService.verificaPermissaoRobots(url)) {
					documentos.add(this.coletar(sementes.get(0)));
					// urlStringAnterior = sementes.remove(0);
				} else {
					sementes.remove(0);
				}
				System.out.println(sementes.size() + " Sementes restantes.");
			}
		} catch (Exception e) {
			System.out.println("\n\n\n Erro ao executar o serviço de coleta! \n\n\n");
			e.printStackTrace();
		}
		return documentos;
	}

	public Documento coletar(String urlDocumento) {
		System.out.println("Iniciando coleta url [" + urlDocumento + "]");
		Documento documento = null;

		try {
			
			Document d = Jsoup.connect(urlDocumento).get();
			Elements urls = d.select("a[href]");

			documento = loadOrNewDoc(urlDocumento);
			
			documento.setTexto(d.html());
			documento.setVisao(utilsService.removerPontuacao(d.text()));
			
			trataLinksColetados(urlDocumento, documento, urls);
			documento.setLinks(utilsService.removeElementosRepetidos(documento.getLinks()));
			
			//urlStringAnterior = sementes.remove(0);
			documento = dr.save(documento);
			
		} catch (Exception e) {
			System.out.println("\n\n\n Erro ao coletar a página! \n\n\n");
			//urlStringAnterior = sementes.remove(0);
			e.printStackTrace();
		} finally {
			urlStringAnterior = sementes.remove(0);
			sementes.addAll(lr.obterUrlsNaoColetadas());
			sementes = utilsService.removeElementosRepetidos(sementes);
		}
		return documento;
	}

	private Documento loadOrNewDoc(String urlDocumento) {
		Documento documento;
		Link link;
		Documento docOld = dr.findByUrl(urlDocumento);
		if(docOld != null && docOld.getId() != null) {
			documento = docOld;
		}else {
			documento = new Documento();
			documento.setUrl(urlDocumento);
			
			link = loadOrNewLink(urlDocumento, documento);
			documento.addLink(link);
		}
		return documento;
	}

	private Link loadOrNewLink(String urlDocumento, Documento documento) {
		Link link;
		link = lr.findByUrl(urlDocumento);
		if (link == null) {
			link = new Link();
			link.setUrl(urlDocumento);
			link.setHost(hostService.obterHostPorUrl(urlDocumento));
		}
		link.setUltimaColeta(LocalDateTime.now());
		link.addDocumento(documento);
		return link;
	}

	private void trataLinksColetados(String urlDocumento, Documento documento, Elements urls) {
		List<String> urlsColetadas = new ArrayList<>();
		Link link;
		
		urlsColetadas = converteElementToList(urls, urlsColetadas);
		
		int i = 0;
		for (String url : urlsColetadas) {
			if (url.length() > 253)
				continue;
			i++;
			if ((!url.equals("")) && (url != null)) {
				link = lr.findByUrl(url);
				if (link == null) {
					link = new Link();
					link.setUrl(url);
					link.setHost(hostService.obterHostPorUrl(url));
					link.setUltimaColeta(null);
				}
				link.addDocumento(documento);
				documento.addLink(link);
			}
		}
		System.out.println("Finalizano coleta de [" + urlDocumento + "].");
		System.out.println("Número de links coletados: [" + i + "]");
		System.out.println("Tamanho da lista links: [" + documento.getLinks().size() + "]");
	}

	private List<String> converteElementToList(Elements urls, List<String> urlsColetadas) {
		for (Element url : urls) {
			urlsColetadas.add(url.attr("abs:href"));
		}
		urlsColetadas = utilsService.removeElementosRepetidos(urlsColetadas);
		return urlsColetadas;
	}

}
