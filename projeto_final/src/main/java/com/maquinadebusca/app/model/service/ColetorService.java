package com.maquinadebusca.app.model.service;

import java.net.URL;
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

			gravaLinksColetados(urlDocumento, documento, urls);
			// Salvar o documento no banco de dados.
			documento.setLinks(utilsService.removeElementosRepetidos(documento.getLinks()));
			documento = dr.save(documento);
			// 1. Altere o projeto, para que ele colete as novas URLs identificadas em cada
			// página.
			urlStringAnterior = sementes.remove(0);
			sementes.addAll(lr.obterUrlsNaoColetadas());
			sementes = utilsService.removeElementosRepetidos(sementes);
		} catch (Exception e) {
			System.out.println("\n\n\n Erro ao coletar a página! \n\n\n");
			e.printStackTrace();
		}
		return documento;
	}

	private void gravaLinksColetados(String urlDocumento, Documento documento, Elements urls) {
		Link link;
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
		System.out.println("Finalizano coleta de [" + urlDocumento + "].");
		System.out.println("Número de links coletados: [" + i + "]");
		System.out.println("Tamanho da lista links: [" + documento.getLinks().size() + "]");
	}

}
