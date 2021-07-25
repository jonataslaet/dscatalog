package com.devsuperior.dscatalog.resources;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.devsuperior.dscatalog.resources.dtos.ClientDTO;
import com.devsuperior.dscatalog.services.ClientService;

@RestController
@RequestMapping(value = "/clients")
public class ClientResource {

	@Autowired
	private ClientService clientService;
	
	@GetMapping
	ResponseEntity<Page<ClientDTO>> findAllPaged(Pageable pageable) {
		Page<ClientDTO> clientsDTO = clientService.findAllPaged(pageable);

		return ResponseEntity.ok().body(clientsDTO);
	}
	
	@GetMapping("/{id}")
	ResponseEntity<ClientDTO> findById(@PathVariable("id") Long id) {
		
		ClientDTO clientDTO = clientService.findById(id);

		return ResponseEntity.ok().body(clientDTO);
	}
	
	@PostMapping
	ResponseEntity<ClientDTO> insert(@RequestBody ClientDTO clientDTO) {
		ClientDTO createdClientDTO = clientService.insert(clientDTO);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(createdClientDTO.getId())
				.toUri();
		return ResponseEntity.created(uri).body(createdClientDTO);
	}
	
	@PutMapping("/{id}")
	ResponseEntity<ClientDTO> update(@PathVariable("id") Long id, @RequestBody ClientDTO clientDTO) {
		ClientDTO clientDTO2 = clientService.update(id, clientDTO);
		return ResponseEntity.status(204).body(clientDTO2);
	}
	
	@DeleteMapping("/{id}")
	ResponseEntity<Void> delete(@PathVariable("id") Long id) {
		clientService.delete(id);
		return ResponseEntity.noContent().build();
	}
}
