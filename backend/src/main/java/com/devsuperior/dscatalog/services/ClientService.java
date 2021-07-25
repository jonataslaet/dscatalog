package com.devsuperior.dscatalog.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.dscatalog.entities.Client;
import com.devsuperior.dscatalog.repositories.ClientRepository;
import com.devsuperior.dscatalog.resources.dtos.ClientDTO;
import com.devsuperior.dscatalog.resources.exceptions.EntityNotFoundException;

@Service
public class ClientService {

	@Autowired
	private ClientRepository clientRepository;

	@Transactional(readOnly=true)
	public Page<ClientDTO> findAllPaged(Pageable pageable) {
		Page<Client> clientsPaged = clientRepository.findAll(pageable);
		return clientsPaged.map(c -> new ClientDTO(c));
	}
	
	@Transactional(readOnly=true)
	public ClientDTO findById(Long id) {
		Optional<Client> clientOptional = clientRepository.findById(id);
		clientOptional.orElseThrow(() -> new EntityNotFoundException("Client not found"));
		return new ClientDTO(clientOptional.get());
	}

	public ClientDTO insert(ClientDTO clientDTO) {
		Client client = new Client(clientDTO);
		client = clientRepository.save(client);
		return new ClientDTO(client);
	}

	public ClientDTO update(Long id, ClientDTO clientDTO) {
		Client client = new Client();
		try {
			findById(id);
			copyDtoToEntity(clientDTO, client);
			client.setId(id);
			clientRepository.save(client);
			return new ClientDTO(client);
		}
		catch (Exception e) {
			throw new EntityNotFoundException("Client not found");
		}
		
	}
	
	public void delete(Long id) {
		try {
			findById(id);
			clientRepository.deleteById(id);
		}
		catch (Exception e) {
			throw new EntityNotFoundException("Client not found");
		}
		
	}
	
	private void copyDtoToEntity(ClientDTO clientDTO, Client client) {
		client.setName(clientDTO.getName());
		client.setCpf(clientDTO.getCpf());
		client.setIncome(clientDTO.getIncome());
		client.setBirthDate(clientDTO.getBirthDate());
		client.setChildren(clientDTO.getChildren());
	}

}
