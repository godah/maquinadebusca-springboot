package com.maquinadebusca.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maquinadebusca.app.mensagem.Mensagem;
import com.maquinadebusca.app.model.Link;
import com.maquinadebusca.app.model.service.ColetorService;

@SuppressWarnings({ "unchecked", "rawtypes" })
@RestController
@RequestMapping ("/coletor") // URL: http://localhost:8080/coletor
public class Coletor {

  @Autowired
  ColetorService cs;

  // URL: http://localhost:8080/coletor/iniciar
  
@GetMapping (value = "/iniciar", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity iniciar () {
    return new ResponseEntity (cs.executar (), HttpStatus.OK);
  }

  // URL: http://localhost:8080/coletor/documento
  @GetMapping (value = "/documento", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity listarDocumento () {
    return new ResponseEntity (cs.getDocumento (), HttpStatus.OK);
  }

  // Request for: http://localhost:8080/coletor/documento/{id}
  @GetMapping (value = "/documento/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity listarDocumento (@PathVariable (value = "id") long id) {
    return new ResponseEntity (cs.getDocumento (id), HttpStatus.OK);
  }

  // URL: http://localhost:8080/coletor/link
  @GetMapping (value = "/link", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity listarLink () {
    return new ResponseEntity (cs.getLink (), HttpStatus.OK);
  }

  // Request for: http://localhost:8080/coletor/link/{id}
  @GetMapping (value = "/link/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity listarLink (@PathVariable (value = "id") long id) {
    return new ResponseEntity (cs.getLink (id), HttpStatus.OK);
  }

  // Request for: http://localhost:8080/coletor/link
  //@RequestMapping(value = "/link", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  @PostMapping (value = "/link", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)  
  public ResponseEntity inserirLinks (@RequestBody List<Link> links) {
    links = cs.salvarLinks (links);
    if ((!links.isEmpty()) && (links.iterator().next().getId() > 0)) {
      return new ResponseEntity (links, HttpStatus.OK);
     } else {
      return new ResponseEntity (new Mensagem ("erro", "não foi possível inserir o link informado no banco de dados"), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
