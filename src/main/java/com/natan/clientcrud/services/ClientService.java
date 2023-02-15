package com.natan.clientcrud.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.natan.clientcrud.dto.ClientDTO;
import com.natan.clientcrud.entities.Client;
import com.natan.clientcrud.repositories.ClientRepository;
import com.natan.clientcrud.services.exceptions.DatabaseException;
import com.natan.clientcrud.services.exceptions.ResourceNotFoundException;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ClientService {
	
	@Autowired
	ClientRepository repository;
	
	@Transactional(readOnly = true)
	public List<ClientDTO> findAll(){
		List<Client> list = repository.findAll();
		return list.stream().map(x -> new ClientDTO(x)).collect(Collectors.toList());
	}
	
	@Transactional(readOnly = true)
	public ClientDTO findById(Long id) {
		Optional<Client> obj = repository.findById(id);
		Client entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entity not found"));
		return new ClientDTO(entity);
	}
	
	@Transactional
	public ClientDTO insert(ClientDTO dto) {
		Client entity = new Client();
		entity.setName(dto.getName());
		entity.setBirthDate(dto.getBirthDate());
		entity.setCpf(dto.getCpf());
		entity.setChildren(dto.getChildren());
		entity.setIncome(dto.getIncome());
		
		entity = repository.save(entity);
		return new ClientDTO(entity);
	}
	
	@Transactional
	public ClientDTO update(Long id, ClientDTO dto) {
		try {
			Client entity = repository.getReferenceById(id);
			entity.setName(dto.getName());
			entity.setBirthDate(dto.getBirthDate());
			entity.setCpf(dto.getCpf());
			entity.setChildren(dto.getChildren());
			entity.setIncome(dto.getIncome());
			
			entity = repository.save(entity);
			return new ClientDTO(entity);
		} catch(EntityNotFoundException e) {
			throw new ResourceNotFoundException("Id not found! Id:" + id);
		}
	}

	public void delete(Long id) {
		try {
			repository.deleteById(id);
		} catch(EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException("Id not found! Id:" + id);
		}catch(DataIntegrityViolationException e) {
			throw new DatabaseException("Integrity violation!");
		}
		
	}
}
