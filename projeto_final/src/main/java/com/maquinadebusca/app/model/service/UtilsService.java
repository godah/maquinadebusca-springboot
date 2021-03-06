package com.maquinadebusca.app.model.service;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import com.maquinadebusca.app.model.Link;

@Service
public class UtilsService {
	private final Log log = LogFactory.getLog(UtilsService.class);
	
	public String removerPontuacao(String visao) {
		return visao.replaceAll("[^A-Z a-z \u00C0-\u00ff 0-9 ]", "");
	}

	public void verificaColetaConsecultiva(String urlStringAnterior, URL url)
			throws MalformedURLException, InterruptedException {
		if (urlStringAnterior != null) {
			URL urlAnterior = new URL(urlStringAnterior);
			if (urlAnterior.getHost().equals(url.getHost())) {
				log.info("Host consecultivo esperando 10 segundos.");
				Thread.sleep(10000);
			}
		}
	}

	public Set<Link> removeElementosRepetidos(Set<Link> urlsSet) {
		List<Link> urls = new ArrayList<>(urlsSet);
		Set<Link> novaLista = new HashSet<>();
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
	
	public List<String> removeElementosRepetidos(List<String> urls) {
		List<String> novaLista = new ArrayList<>();
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
}
