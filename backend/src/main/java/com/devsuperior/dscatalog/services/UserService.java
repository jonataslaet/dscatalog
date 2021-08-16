package com.devsuperior.dscatalog.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.dscatalog.entities.Role;
import com.devsuperior.dscatalog.entities.User;
import com.devsuperior.dscatalog.repositories.RoleRepository;
import com.devsuperior.dscatalog.repositories.UserRepository;
import com.devsuperior.dscatalog.resources.dtos.RoleDTO;
import com.devsuperior.dscatalog.resources.dtos.UserDTO;
import com.devsuperior.dscatalog.resources.dtos.UserInsertDTO;
import com.devsuperior.dscatalog.resources.dtos.UserUpdateDTO;
import com.devsuperior.dscatalog.resources.exceptions.DatabaseException;
import com.devsuperior.dscatalog.resources.exceptions.EntityNotFoundException;

@Service
public class UserService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private RoleRepository roleRepository;

	@Transactional(readOnly = true)
	public Page<UserDTO> findAllPaged(Pageable pageable) {
		Page<User> usersPaged = userRepository.findAll(pageable);
		return usersPaged.map(c -> new UserDTO(c));
	}

	@Transactional(readOnly = true)
	public UserDTO findById(Long id) {
		Optional<User> userOptional = userRepository.findById(id);
		userOptional.orElseThrow(() -> new EntityNotFoundException("User not found"));
		return new UserDTO(userOptional.get());
	}

	public UserDTO insert(UserInsertDTO userDTO) {
		User user = new User();
		copyDtoToEntity(userDTO, user);
		user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
		user = userRepository.save(user);
		return new UserDTO(user);
	}

	public UserDTO update(Long id, UserUpdateDTO userDTO) {
		User user = new User();
		try {
			findById(id);
			copyDtoToEntity(userDTO, user);
			user.setId(id);
			userRepository.save(user);
			return new UserDTO(user);
		} catch (Exception e) {
			throw new EntityNotFoundException("User not found");
		}

	}

	public void delete(Long id) {
		try {
			userRepository.deleteById(id);
		} catch (EmptyResultDataAccessException e) {
			throw new EntityNotFoundException("User not found");
		} catch (DataIntegrityViolationException e) {
			throw new DatabaseException("User depends on other entity");
		}

	}

	private void copyDtoToEntity(UserDTO userDTO, User user) {
		user.setEmail(userDTO.getEmail());
		user.setFirstName(userDTO.getFirstName());
		user.setLastName(userDTO.getLastName());
		user.getRoles().clear();

		for (RoleDTO roleDTO : userDTO.getRolesDTO()) {
			Role role = roleRepository.findById(roleDTO.getId()).get();
			user.getRoles().add(role);
		}
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Optional<User> userByEmail = userRepository.findByEmail(email);
		if (!userByEmail.isPresent()) {
			throw new EntityNotFoundException("User not found for email: " + email);
		}
		return userByEmail.get();
	}

}
