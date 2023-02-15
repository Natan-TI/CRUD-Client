package com.natan.clientcrud.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
	private ClientRepository repository;
	
	@Transactional(readOnly = true)
	public Page<ClientDTO> findAllPaged(PageRequest pageRequest){
		Page<Client> list = repository.findAll(pageRequest);
		return list.map(x -> new ClientDTO(x));
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
