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

import com.maquinadebusca.app.model.Documento;
import com.maquinadebusca.app.model.Link;
import com.maquinadebusca.app.repository.DocumentoRepository;
import com.maquinadebusca.app.repository.LinkRepository;
import com.panforge.robotstxt.RobotsTxt;

@Service
public class ColetorService {
	private final Log log = LogFactory.getLog(ColetorService.class);

	@Autowired
	private DocumentoRepository dr;
	
	@Autowired
	private LinkRepository lr;
	
	@Autowired
	private StopwordsService stopWordsService;

	public List<Documento> executar() {
		List<Documento> documentos = new LinkedList<>();
		List<String> sementes = new LinkedList<>();
		String urlStringAnterior = null;
		try {
			sementes.add("https://www.youtube.com/");
			sementes.add("https://www.facebook.com/");
			sementes.add("https://www.twitter.com/");
			for (String url : sementes) {
				verificaColetaConsecultiva(urlStringAnterior, new URL(url));
				if(verificaPermissaoRobots(new URL(url))) {
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
		documento = dr.save(documento);
		salvarLinks(urlsColetadas);
		return documento;
	}
	
	private void salvarLinks(List<String> urlsColetadas) {
		List<Link> links = lr.findAll();
		for (String url : urlsColetadas) {
			if(url.length() < 255)
				links.add(new Link(url));
		}
		links = removeElementosRepetidos(links);
		//for (Link link : links) {
			try {
				lr.saveAll(links);
			}catch (Exception e) {
				log.error("Falha ao gravar links");
			}
		//}
	}

	public List<Documento> getDocumentos() {
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
	
	private List<Link> removeElementosRepetidos(List<Link> urls) {
		List<Link> novaLista = new ArrayList<>();
		for(int i = 0;  i < urls.size(); i++){
			if(novaLista.isEmpty()){
				novaLista.add(urls.get(i));
			}else{
				int count = 0;
				for(Link u :novaLista){
					if(urls.get(i).getUrl().equalsIgnoreCase(u.getUrl())){
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
				//urlsSementes.remove(0);
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

	
}