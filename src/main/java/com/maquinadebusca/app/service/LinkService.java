package com.maquinadebusca.app.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.maquinadebusca.app.repository.LinkRepository;

@Service
public class LinkService {
	private final Log log = LogFactory.getLog(LinkService.class);
	
	@Autowired
	private UtilsService utilsService;
	
	@Autowired
	private LinkRepository lr;
	

	
}
