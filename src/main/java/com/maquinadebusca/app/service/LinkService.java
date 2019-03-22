package com.maquinadebusca.app.service;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.maquinadebusca.app.model.Link;
import com.maquinadebusca.app.repository.LinkRepository;

@Service
public class LinkService {
	private final Log log = LogFactory.getLog(LinkService.class);
	
	@Autowired
	private UtilsService utilsService;
	
	@Autowired
	private LinkRepository lr;
	

	public void salvarLinks(List<String> urlsColetadas) {
		List<Link> links = lr.findAll();
		for (String url : urlsColetadas) {
			if(url.length() < 255)
				links.add(new Link(url));
		}
		links = utilsService.removeElementosRepetidos(links);
		//for (Link link : links) {
			try {
				lr.saveAll(links);
			}catch (Exception e) {
				log.error("Falha ao gravar links");
			}
		//}
	}
	
}
