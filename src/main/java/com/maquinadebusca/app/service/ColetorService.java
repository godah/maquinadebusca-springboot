package com.maquinadebusca.app.service;

import java.net.URL;
import java.util.ArrayList;
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
import com.maquinadebusca.app.repository.DocumentoRepository;
import com.maquinadebusca.app.repository.LinkRepository;

@Service
public class ColetorService {

	@Autowired
	private StopwordsService stopWordsService;
	
	@Autowired
	private RobotsService robotsService;
	
	@Autowired
	private UtilsService utilsService;
	
	@Autowired
	private DocumentoService documentoService;
	
	@Autowired
	private LinkService linkService;
	
	@Autowired
	private LinkRepository lr;
	
	@Autowired
	private DocumentoRepository dr;

	public List<Documento> executar() {
		List<Documento> documentos = new LinkedList<>();
		List<String> sementes = new LinkedList<>();
		String urlStringAnterior = null;
		try {
			sementes.add("https://www.youtube.com/");
			sementes.add("https://www.facebook.com/");
			sementes.add("https://www.twitter.com/");
			for (String url : sementes) {
				utilsService.verificaColetaConsecultiva(urlStringAnterior, new URL(url));
				if(robotsService.verificaPermissaoRobots(new URL(url))) {
					documentos.add(this.coletar(url));
				}
			}
		} catch (Exception e) {
			System.out.println("Erro ao executar o serviço de coleta!");
			e.printStackTrace();
		}
		return documentos;
	}

	public Documento coletar(String urlDocumento) {
		List<String> urlsColetadas = new ArrayList<>();
		Documento documento = new Documento();
		try {
			Document d = Jsoup.connect(urlDocumento).get();
			Elements urls = d.select("a[href]");
			documento.setUrl(urlDocumento);
			documento.setTexto(d.html());
			documento.setVisao(trataVisao(d.text()));
			for (Element url : urls) {
				String u = url.attr("abs:href");
				if ((!u.equals("")) && (u != null)) {
					System.out.println(u);
					urlsColetadas.add(u);
				}
			}
		} catch (Exception e) {
			System.out.println("Erro ao coletar a página.");
			e.printStackTrace();
		}
		documento = documentoService.save(documento);
		linkService.salvarLinks(urlsColetadas);
		return documento;
	}
	
	
	public Documento getDocumento(long id) {
		Documento documento = dr.findById(id);
		return documento;
	}
	
	public List<Link> getLinks() {
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
	
	private String trataVisao(String visao1) {
		String visao = visao1;
		visao = visao.toLowerCase();
		visao = utilsService.removerPontuacao(visao);
		visao = stopWordsService.removerStopWords(visao);
		return visao;
	}
	
}