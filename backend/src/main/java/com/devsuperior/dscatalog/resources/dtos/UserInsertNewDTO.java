package com.devsuperior.dscatalog.resources.dtos;

public class UserInsertNewDTO extends UserDTO {
	private static final long serialVersionUID = 1L;

	private String password;

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
