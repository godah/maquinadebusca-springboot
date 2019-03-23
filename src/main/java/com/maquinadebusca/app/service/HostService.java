package com.maquinadebusca.app.service;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.maquinadebusca.app.model.Host;
import com.maquinadebusca.app.repository.HostRepository;

import javassist.NotFoundException;

@Service
public class HostService {
	private final Log log = LogFactory.getLog(HostService.class);
	
	@Autowired
	private HostRepository hr;
	
	
	public Host obterHostPorUrl(String url) {
		try {
			URL urlNat = new URL(url);
			Host host = hr.obterPorUrl(urlNat.getHost());
			if(host == null)
				throw new NotFoundException("Host nao encontrado");
			return incrementaQtdUlrs(host);
		} catch (MalformedURLException e) {
			log.error(e.getMessage());
			return null;
		} catch (NotFoundException e) {
			log.error(e.getMessage());
			URL urlNat;
			try {
				urlNat = new URL(url);
				Host host = new Host(urlNat.getHost());
				return hr.save(host);
			} catch (MalformedURLException e1) {
				log.error(e1.getMessage());
				return null;
			}
		}
	}
	
	public Host incrementaQtdUlrs(Host host) {
		host.setCount(host.getCount() + 1L);
		return hr.save(host);
	}
}
