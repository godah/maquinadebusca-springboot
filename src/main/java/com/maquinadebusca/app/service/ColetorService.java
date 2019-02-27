package com.maquinadebusca.app.service;

import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import com.maquinadebusca.app.model.Documento;

@Service
public class ColetorService {
	List<String> urlsSementes = new ArrayList<>();
	List<String> urlsLidas = new ArrayList<>();

	public Documento executarColeta() {
		urlsSementes.add("http://journals.ecs.soton.ac.uk/java/tutorial/networking/urls/readingWriting.html");
		urlsSementes.add("https://stormpath.com/blog/spring-boot-dependency-injection");
		urlsSementes.add("https://dzone.com/articles/spring-boot-restful-web-service-complete-example");

		String urlStringAnterior = null;
		while (!urlsSementes.isEmpty()) {
			
			Documento d = new Documento();
			try {
				//Atividade2: Regra 8
				URL url = new URL(urlsSementes.get(0));
				if(urlStringAnterior != null) {
					URL urlAnterior = new URL(urlStringAnterior);
					if(urlAnterior.getHost().equals(url.getHost()))
						Thread.sleep(10000);
				}
				
				Document doc = Jsoup.connect(url.toString()).get();
				Elements links = doc.select("a[href]");
				d.setUrl(url);
				d.setTexto(doc.html());
				d.setVisao(doc.text());
				List<String> urls = new LinkedList<>();
				for (Element link : links)
					if ((!link.attr("abs:href").equals("") && (link.attr("abs:href") != null)))
						urls.add(link.attr("abs:href"));
				d.setUrls(urls);
				urlsSementes.addAll(urls);
				imprimeInformacoes(d);
				urls = d.getUrls();
				for (String u : urls)
					System.out.println(u);
			} catch (Exception e) {
				System.out.println("Erro ao coletar a página.");
				e.printStackTrace();
			}
			urlStringAnterior = urlsSementes.get(0);
			//if(!urlsSementes.stream().filter(p->p.equals(urlsSementes.get(0))).findFirst().isPresent())
			urlsLidas.add(urlsSementes.get(0));
			urlsSementes.remove(0);
			urlsSementes.addAll(d.getUrls());
		}
		
		return null;

	}

	/**
	 * Atividade2 regra9
	 * @param urls
	 */
	private void removerUrlsRepetidas(List<String> urls) {
		//TODO falta testar tudo
		
	}
	
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
