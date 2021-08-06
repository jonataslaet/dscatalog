package com.devsuperior.dscatalog.resources.dtos;

import java.io.Serializable;

import com.devsuperior.dscatalog.entities.Role;

public class RoleDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;
	private String authority;

	public RoleDTO() {

	}

	public RoleDTO(Role r) {
		id = r.getId();
		authority = r.getAuthority();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAuthority() {
		return authority;
	}

	public void setAuthority(String authority) {
		this.authority = authority;
	}

}
