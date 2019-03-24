package com.maquinadebusca.app.model.service;

import java.io.InputStream;
import java.net.URL;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import com.panforge.robotstxt.RobotsTxt;

@Service
public class RobotsService {

	private final Log log = LogFactory.getLog(RobotsService.class);

	public boolean verificaPermissaoRobots(URL url) {
		try (InputStream robotsTxtStream = new URL(url.getProtocol() + "://" + url.getHost() + "/robots.txt")
				.openStream()) {
			RobotsTxt robotsTxt = RobotsTxt.read(robotsTxtStream);
			if (!robotsTxt.query(null, url.getPath())) {
				log.info("robots.txt Disalow ==> " + url.getProtocol() + "://" + url.getHost() + url.getPath());
				// urlsSementes.remove(0);
				return false;
			} else {
				return true;
			}
		} catch (Exception e) {
			log.error("Falha ao coletar " + url.getProtocol() + "://" + url.getHost() + "/robots.txt");
			return true;
		}
	}

}