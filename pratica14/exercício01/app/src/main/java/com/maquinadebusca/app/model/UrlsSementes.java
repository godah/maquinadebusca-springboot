package com.maquinadebusca.app.model;

import java.util.List;
import java.util.LinkedList;

public class UrlsSementes {

  private List<String> urls = new LinkedList ();

  public UrlsSementes () {
  }

  public List<String> getUrls () {
    return urls;
  }

  public void setUrls (List<String> urls) {
    this.urls = urls;
  }

}
