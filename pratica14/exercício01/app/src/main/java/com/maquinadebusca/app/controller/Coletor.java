package com.maquinadebusca.app.controller;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.maquinadebusca.app.mensagem.Mensagem;
import com.maquinadebusca.app.model.Link;
import com.maquinadebusca.app.model.UrlsSementes;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import com.maquinadebusca.app.model.service.ColetorService;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

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
  public ResponseEntity listarDocumento (@PathVariable (value = "id") Long id) {
    return new ResponseEntity (cs.getDocumento (id), HttpStatus.OK);
  }

  // URL: http://localhost:8080/coletor/link
  @GetMapping (value = "/link", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity listarLink () {
    return new ResponseEntity (cs.getLink (), HttpStatus.OK);
  }

  // Request for: http://localhost:8080/coletor/link/{id}
  @GetMapping (value = "/link/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity listarLink (@PathVariable (value = "id") Long id) {
    return new ResponseEntity (cs.getLink (id), HttpStatus.OK);
  }

  // Request for: http://localhost:8080/coletor/link
  @PostMapping (value = "/link", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  @JsonDeserialize (using = LocalDateTimeDeserializer.class)
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

  // Request for: http://localhost:8080/coletor/urlsSementes
  @PostMapping (value = "/urlsSementes", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity inserirUrlsSementes (@RequestBody UrlsSementes urlsSementes) {
    boolean erro = false;
    ResponseEntity resposta = null;

    for (String url : urlsSementes.getUrls ()) {
      Link link = new Link ();
      link.setUrl (url);
      link = cs.salvarLink (link);
      if ((link == null) || (link.getId () <= 0)) {
        erro = true;
        break;
      }
    }
    if (erro == false) {
      resposta = new ResponseEntity (new Mensagem ("sucesso", "as urls sementes informadas foram inseridas no banco de dados"), HttpStatus.OK);
    } else {
      resposta = new ResponseEntity (new Mensagem ("erro", "não foi possível inserir as urls sementes informadas no banco de dados"), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    return resposta;
  }

  // Request for: http://localhost:8080/coletor/link
  @PutMapping (value = "/link", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity atualizarLink (@RequestBody @Valid Link link, BindingResult resultado) {
    ResponseEntity resposta = null;
    if (resultado.hasErrors ()) {
      resposta = new ResponseEntity (new Mensagem ("erro", "os dados sobre o link  não foram informados corretamente"), HttpStatus.BAD_REQUEST);
    } else {
      link = cs.atualizarLink (link);
      if ((link != null) && (link.getId () > 0)) {
        resposta = new ResponseEntity (link, HttpStatus.OK);
      } else {
        resposta = new ResponseEntity (new Mensagem ("erro", "não foi possível atualizar o link informado no banco de dados"), HttpStatus.NOT_ACCEPTABLE);
      }
    }
    return resposta;
  }

  // Request for: http://localhost:8080/coletor/link
  @DeleteMapping (value = "/link", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity removerLink (@RequestBody @Valid Link link, BindingResult resultado) {
    ResponseEntity resposta = null;
    if (resultado.hasErrors ()) {
      resposta = new ResponseEntity (new Mensagem ("erro", "os dados sobre o link  não foram informados corretamente"), HttpStatus.BAD_REQUEST);
    } else {
      link = cs.removerLink (link);
      if (link != null) {
        resposta = new ResponseEntity (new Mensagem ("sucesso", "link removido com suceso"), HttpStatus.OK);
      } else {
        resposta = new ResponseEntity (new Mensagem ("erro", "não foi possível remover o link informado no banco de dados"), HttpStatus.NOT_ACCEPTABLE);
      }
    }
    return resposta;
  }

  // Request for: http://localhost:8080/coletor/link
  @DeleteMapping (value = "/link/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity removerLink (@PathVariable (value = "id") Long id) {
    ResponseEntity resposta = null;
    if ((id != null) && (id <= 0)) {
      resposta = new ResponseEntity (new Mensagem ("erro", "os dados sobre o link  não foram informados corretamente"), HttpStatus.BAD_REQUEST);
    } else {
      boolean resp = cs.removerLink (id);
      if (resp == true) {
        resposta = new ResponseEntity (new Mensagem ("sucesso", "link removido com suceso"), HttpStatus.OK);
      } else {
        resposta = new ResponseEntity (new Mensagem ("erro", "não foi possível remover o link informado no banco de dados"), HttpStatus.NOT_ACCEPTABLE);
      }
    }
    return resposta;
  }

  // Request for: http://localhost:8080/coletor/encontrar/{url}
  @GetMapping (value = "/encontrar/{url}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity encontrarLink (@PathVariable (value = "url") String url) {
    return new ResponseEntity (cs.encontrarLinkUrl (url), HttpStatus.OK);
  }

  // Request for: http://localhost:8080/coletor/link/ordemAlfabetica
  @GetMapping (value = "/link/ordemAlfabetica", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity listarEmOrdemAlfabetica () {
    return new ResponseEntity (cs.listarEmOrdemAlfabetica (), HttpStatus.OK);
  }

  // Request for: http://localhost:8080/coletor/link/pagina
  @GetMapping (value = "/link/pagina", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity listarPagina () {
    return new ResponseEntity (cs.buscarPagina (), HttpStatus.OK);
  }
  
  // Request for: http://localhost:8080/coletor/link/intervalo/{id1}/{id2}
  @GetMapping (value = "/link/intervalo/{id1}/{id2}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity encontrarLinkPorIntervaloDeId (@PathVariable (value = "id1") Long id1, @PathVariable (value = "id2") Long id2) {
    return new ResponseEntity (cs.pesquisarLinkPorIntervaloDeIdentificacao (id1, id2), HttpStatus.OK);
  }

  // Request for: http://localhost:8080/coletor/link/intervalo/contar/{id1}/{id2}
  @GetMapping (value = "/link/intervalo/contar/{id1}/{id2}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity contarLinkPorIntervaloDeId (@PathVariable (value = "id1") Long id1, @PathVariable (value = "id2") Long id2) {
    return new ResponseEntity (cs.contarLinkPorIntervaloDeIdentificacao (id1, id2), HttpStatus.OK);
  }
}
