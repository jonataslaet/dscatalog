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

import com.devsuperior.dscatalog.resources.dtos.UserDTO;
import com.devsuperior.dscatalog.resources.dtos.UserInsertNewDTO;
import com.devsuperior.dscatalog.services.UserService;

@RestController
@RequestMapping(value = "/users")
public class UserResource {

	@Autowired
	private UserService userService;

	@GetMapping
	ResponseEntity<Page<UserDTO>> findAllPaged(Pageable pageable) {
		Page<UserDTO> usersDTO = userService.findAllPaged(pageable);

		return ResponseEntity.ok().body(usersDTO);
	}

	@GetMapping("/{id}")
	ResponseEntity<UserDTO> findById(@PathVariable("id") Long id) {

		UserDTO userDTO = userService.findById(id);

		return ResponseEntity.ok().body(userDTO);
	}

	@PostMapping
	ResponseEntity<UserDTO> insert(@RequestBody UserInsertNewDTO userDTO) {
		UserDTO createdUserDTO = userService.insert(userDTO);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(createdUserDTO.getId())
				.toUri();
		return ResponseEntity.created(uri).body(createdUserDTO);
	}

	@PutMapping("/{id}")
	ResponseEntity<UserDTO> update(@PathVariable("id") Long id, @RequestBody UserDTO userDTO) {
		UserDTO userDTO2 = userService.update(id, userDTO);
		return ResponseEntity.status(204).body(userDTO2);
	}

	@DeleteMapping("/{id}")
	ResponseEntity<Void> delete(@PathVariable("id") Long id) {
		userService.delete(id);
		return ResponseEntity.noContent().build();
	}
}
