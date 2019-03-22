package com.maquinadebusca.app.service;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.maquinadebusca.app.model.Documento;
import com.maquinadebusca.app.repository.DocumentoRepository;

@Service
public class DocumentoService {
	@Autowired
	private DocumentoRepository dr;
	
	public List<Documento> getDocumentos() {
		Iterable<Documento> documentos = dr.findAll();
		List<Documento> resposta = new LinkedList<>();
		for (Documento documento : documentos) {

			resposta.add(documento);
		}
		return resposta;
	}
	
	public Documento save(Documento documento){
		return dr.save(documento);
	}
}
