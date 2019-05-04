package com.maquinadebusca.app.model.service;

import java.util.LinkedList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.maquinadebusca.app.model.Documento;
import com.maquinadebusca.app.model.repository.DocumentoRepository;

@Service
public class DocumentoService {

	@Autowired
	private DocumentoRepository dr;

	String urlStringAnterior = null;
	List<String> sementes = new LinkedList<>();

	public List<Documento> getDocumento() {
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

	public boolean removerDoc(Long id) {
		boolean resp = false;
		try {
			dr.deleteById(id);
			resp = true;
		} catch (Exception e) {
			System.out.println("\n>>> Não foi possível remover o documento informado no banco de dados.\n");
			e.printStackTrace();
		}
		return resp;
	}

	public @Valid Documento removerDoc(@Valid Documento doc) {
		try {
			dr.delete(doc);
		} catch (Exception e) {
			doc = null;
			System.out.println("\n>>> Não foi possível remover o documento informado no banco de dados.\n");
			e.printStackTrace();
		}
		return doc;
	}

	public List<Documento> encontrarDocumento(String url) {
		return dr.findByUrlIgnoreCaseContaining(url);
	}
}
