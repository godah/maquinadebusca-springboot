package com.maquinadebusca.app.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

@Service
public class StopwordsService {
	List<String> stopWords = new ArrayList<>();
	private final Log log = LogFactory.getLog(StopwordsService.class);
	
	private List<String> readStopWords() {
		try {
			InputStream is = getClass().getResourceAsStream("/stopwords.txt");
			InputStreamReader reader = new InputStreamReader(is);
			BufferedReader buffer = new BufferedReader(reader);
			String word;
			while ((word = buffer.readLine()) != null) {
				stopWords.add(word.trim());
			}
			return stopWords;
		} catch (IOException e) {
			log.error("Falha ao abrir [/resources/stopwords.txt]", e);
			return stopWords;
		}
	}
	
	public List<String> getStopWords(){
		if(stopWords.isEmpty()) {
			readStopWords();
		}
		return stopWords;
	}
	
	public boolean isStopWord(String word) {
		if(stopWords.isEmpty()) {
			getStopWords();
		}
		return stopWords.stream().filter(p -> p.equalsIgnoreCase(word)).findFirst().isPresent();
	}
	
	public String removerStopWords(String visao) {
		List<String> words = Arrays.asList(visao.split(" "));
		List<String> stopWords = getStopWords();
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
}
