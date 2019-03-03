package com.maquinadebusca.app.service;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.SocketUtils;

import com.maquinadebusca.app.model.Documento;
import com.panforge.robotstxt.RobotsTxt;

@Service
public class ColetorService {
	private final Log log = LogFactory.getLog(ColetorService.class);
	
	@Autowired
	private StopwordsService stopWordsService;
	
	List<String> urlsSementes = new ArrayList<>();
	List<String> urlsLidas = new ArrayList<>();

	//Pratica2-4
	public Documento executarColeta() {
		//urlsSementes.add("https://www.github.com");
		urlsSementes.add("https://www.una.br");
		//urlsSementes.add("http://mobister.una.br");
		//urlsSementes.add("http://journals.ecs.soton.ac.uk/java/tutorial/networking/urls/readingWriting.html");
		//urlsSementes.add("https://stormpath.com/blog/spring-boot-dependency-injection");
		//urlsSementes.add("https://dzone.com/articles/spring-boot-restful-web-service-complete-example");

		String urlStringAnterior = null;
		while (!urlsSementes.isEmpty()) {
			log.info("Coletando: "+urlsSementes.get(0));
			
			Documento d = new Documento();
			try {
				URL url = new URL(urlsSementes.get(0));
				
				//Prática2: Regra 8-a,b
				verificaColetaConsecultiva(urlStringAnterior, url);
				
				//Prática2: Regra 8-c
				if(!verificaPermissaoRobots(url)) {
					urlsSementes.remove(0);
					continue;
				}
				
				Document doc = Jsoup.connect(url.toString()).get();
				Elements links = doc.select("a[href]");
				d.setUrl(url);
				d.setTexto(doc.html());
				d.setVisao(trataVisao(doc.text()));
				List<String> urls = new LinkedList<>();
				for (Element link : links)
					if ((!link.attr("abs:href").equals("") && (link.attr("abs:href") != null)))
						urls.add(link.attr("abs:href"));
				d.setUrls(urls);
				urlsSementes.addAll(urls);
				/*
				//imprimeInformacoes(d);
				urls = d.getUrls();
				for (String u : urls)
					log.info(u);
				*/
			} catch (Exception e) {
				log.error("Erro ao coletar a página.", e);
			}
			urlStringAnterior = urlsSementes.get(0);
			urlsLidas.add(urlsSementes.get(0));
			urlsSementes.remove(0);
			if(d.getUrls() != null && !d.getUrls().isEmpty())
				urlsSementes.addAll(d.getUrls());
			
			//Prática2 Regra 9
			urlsSementes = removeElementosRepetidos(urlsSementes);
			log.info("Coleta finalizada. ["+urlsSementes.size()+"] URL's Sementes restantes.");
			//urlsSementes.forEach(p -> System.out.println(p));
		}
		return null;
	}
	
	private List<String> removeElementosRepetidos(List<String> urls) {
		List<String> novaLista = new ArrayList<String>();
		for(int i = 0;  i < urls.size(); i++){
			if(novaLista.isEmpty()){
				novaLista.add(urls.get(i));
			}else{
				int count = 0;
				for(String u :novaLista){
					if(urls.get(i).equalsIgnoreCase(u)){
						count++;
					}
				}
				if(count == 0){
					novaLista.add(urls.get(i));
				}
			}
		}
		return novaLista;
	}
	
	private String trataVisao(String visao1) {
		String visao = visao1;
		visao = visao.toLowerCase();
		visao = removerPontuacao(visao);
		visao = removerStopWords(visao);
		return visao;
	}
	
	private String removerPontuacao(String visao) {
		return visao.replaceAll ( "[^A-Z a-z \u00C0-\u00ff 0-9 ]",""); 
	}
	
	private String removerStopWords(String visao) {
		List<String> words = Arrays.asList(visao.split(" "));
		List<String> stopWords = stopWordsService.getStopWords();
		List<String> novoVisao = new ArrayList<>();

		for (String word : words) {
			if(!stopWords.stream().filter(p -> p.equalsIgnoreCase(word.trim())).findFirst().isPresent()) {
				novoVisao.add(word.trim());
			}
		}
		StringBuilder sb = new StringBuilder();
		for (String w : novoVisao) {
			sb.append(w+" ");
		}
		return sb.toString();
	}

	private boolean verificaPermissaoRobots(URL url) {
		try (InputStream robotsTxtStream = new URL(url.getProtocol()+"://"+url.getHost()+"/robots.txt").openStream()) {
			RobotsTxt robotsTxt = RobotsTxt.read(robotsTxtStream);
			if(!robotsTxt.query(null, url.getPath())) {
				log.info("robots.txt Disalow ==> "+url.getProtocol()+"://"+url.getHost()+url.getPath());
				urlsSementes.remove(0);
				return false;
			} else {
				return true;
			}
		} catch (Exception e) {
			log.error("Falha ao coletar "+url.getProtocol()+"://"+url.getHost()+"/robots.txt");
			return true;
		} 
	}

	private void verificaColetaConsecultiva(String urlStringAnterior, URL url)
			throws MalformedURLException, InterruptedException {
		if(urlStringAnterior != null) {
			URL urlAnterior = new URL(urlStringAnterior);
			if(urlAnterior.getHost().equals(url.getHost())) {
				log.info("Host consecultivo esperando 10 segundos.");
				Thread.sleep(10000);
			}
		}
	}

	@SuppressWarnings("unused")
	private void imprimeInformacoes(Documento d) {
		System.out.println("\n\n\n=================================================");
		System.out.println(">>> URL:");
		System.out.println("=================================================");
		System.out.println(d.getUrl());
		System.out.println("\n\n\n=================================================");
		System.out.println(">>> Página:");
		System.out.println("=================================================");
		System.out.println(d.getTexto());
		System.out.println("\n\n\n=================================================");
		System.out.println(">>> Visão:");
		System.out.println("=================================================");
		System.out.println(d.getVisao());
		System.out.println("\n\n\n=================================================");
		System.out.println(">>> URLs:");
		System.out.println("=================================================");
	}
}