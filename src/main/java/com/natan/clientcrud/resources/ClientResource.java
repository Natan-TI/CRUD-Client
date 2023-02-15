package com.natan.clientcrud.resources;

import java.time.Instant;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.natan.clientcrud.entities.Client;
import com.natan.clientcrud.services.ClientService;

@RestController
@RequestMapping(value = "/clients")
public class ClientResource {
	
	@Autowired
	ClientService service;
	
	@GetMapping
	public ResponseEntity<List<Client>> findAll(){
		List<Client> list = service.findAll();
		list.add(new Client(1L, "Natan Eguchi", "45949756827", 15000.0, Instant.now(), 0));
		list.add(new Client(2L, "Julia Dias", "32498757628", 8000.0, Instant.now(), 0));
		return ResponseEntity.ok().body(list);
	}
}
