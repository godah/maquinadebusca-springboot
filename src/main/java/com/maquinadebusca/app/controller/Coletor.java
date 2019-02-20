package com.maquinadebusca.app.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import org.jsoup.Jsoup;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping ("/coletor") // URL: http://localhost:8080/coletor
public class Coletor {
	// URL: http://localhost:8080/coletor/iniciar
	@GetMapping (value = "/iniciar", produces = MediaType.TEXT_PLAIN_VALUE)
	public String iniciar() {
		StringBuilder pagina = new StringBuilder ();
		try {
			URL url = new URL ("http://journals.ecs.soton.ac.uk/java/tutorial/networking/urls/readingWriting.html");
			URLConnection url_connection = url.openConnection ();
			InputStream is = url_connection.getInputStream ();
			InputStreamReader reader = new InputStreamReader (is);
			BufferedReader buffer = new BufferedReader (reader);
			String linha;
			
			while ((linha = buffer.readLine ()) != null) {
				linha = Jsoup.parse (linha).text ();
				System.out.println (linha);
				pagina.append (linha);
			}
		} catch (IOException e) {
				pagina.append ("Erro: não foi possível coletar a página.");
		}
		return pagina.toString ();
	}
}