package com.devsuperior.dscatalog.resources.exceptions;

public class EntityNotFoundException extends RuntimeException{
	private static final long serialVersionUID = 1L;

	public EntityNotFoundException() {
	}
	
	public EntityNotFoundException(String msg) {
		super(msg);
	}
}
