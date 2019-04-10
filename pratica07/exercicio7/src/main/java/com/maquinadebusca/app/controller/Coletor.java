package com.maquinadebusca.app.controller;

import com.maquinadebusca.app.mensagem.Mensagem;
import com.maquinadebusca.app.model.Documento;
import com.maquinadebusca.app.model.Link;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import com.maquinadebusca.app.model.service.ColetorService;

import java.util.List;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@SuppressWarnings({"rawtypes", "unchecked"})
@RestController
@RequestMapping ("/coletor") // URL: http://localhost:8080/coletor
public class Coletor {

  @Autowired
  ColetorService cs;

  // URL: http://localhost:8080/coletor/iniciar
@GetMapping (value = "/iniciar", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity iniciar () {
	List<Documento> docs = cs.executar();
	if (docs == null || docs.isEmpty())
		return new ResponseEntity (new Mensagem("outros erros", "Erro interno do servidor (Internal Server Error)"), HttpStatus.INTERNAL_SERVER_ERROR);
    return new ResponseEntity (cs.executar (), HttpStatus.OK);
  }

  // URL: http://localhost:8080/coletor/documento
  @GetMapping (value = "/documento", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity listarDocumento () {
	 List<Documento> docs = cs.getDocumento();
	 if(docs == null || docs.isEmpty())
		 return new ResponseEntity<>(new Mensagem("sucesso","Nenhum conteúdo"), HttpStatus.NO_CONTENT);
    return new ResponseEntity (docs, HttpStatus.OK);
  }

  // Request for: http://localhost:8080/coletor/documento/{id}
  @GetMapping (value = "/documento/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity listarDocumento (@PathVariable (value = "id") long id) {
	  Documento doc = cs.getDocumento(id);
	  if(doc == null || doc.getId() == null)
		  return new ResponseEntity<>(new Mensagem("erro","Não encontrado"), HttpStatus.NOT_FOUND);
    return new ResponseEntity (cs.getDocumento (id), HttpStatus.OK);
  }

  // URL: http://localhost:8080/coletor/link
  @GetMapping (value = "/link", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity listarLink () {
	  List<Link> lks = cs.getLink();
	  if(lks == null || lks.isEmpty())
		  return new ResponseEntity<>(new Mensagem("sucesso","Nenhum conteúdo"), HttpStatus.NO_CONTENT);
    return new ResponseEntity (cs.getLink (), HttpStatus.OK);
  }

  // Request for: http://localhost:8080/coletor/link/{id}
  @GetMapping (value = "/link/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity listarLink (@PathVariable (value = "id") long id) {
	  Link lk =cs.getLink(id);
	  if (lk == null || lk.getId() == null)
		  return new ResponseEntity (new Mensagem("erro", "Não encontrado"), HttpStatus.NOT_FOUND);
    return new ResponseEntity (cs.getLink (id), HttpStatus.OK);
  }

  // Request for: http://localhost:8080/coletor/link
  @PostMapping (value = "/link", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity inserirLink (@RequestBody @Valid Link link, BindingResult resultado) {
    ResponseEntity resposta = null;
    if (resultado.hasErrors ()) {
      resposta = new ResponseEntity (new Mensagem ("erro", "os dados sobre o link  não foram informados corretamente"), HttpStatus.BAD_REQUEST);
    } else {
      link = cs.salvarLink (link);
      if ((link != null) && (link.getId () > 0)) {
        resposta = new ResponseEntity (link, HttpStatus.OK);
      } else {
        resposta = new ResponseEntity (new Mensagem ("erro", "não foi possível inserir o link informado no banco de dados"), HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }
    return resposta;
  }
}
