package com.devsuperior.dscatalog.resources.exceptions;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ResourceExceptionHandler {

	@ExceptionHandler(EntityNotFoundException.class)
	public ResponseEntity<StandardError> entityNotFound(EntityNotFoundException e, HttpServletRequest req){
		StandardError error = new StandardError();
		error.setError("Entity not found");
		error.setMessage(e.getLocalizedMessage());
		error.setPath(req.getServletPath());
		error.setStatus(HttpStatus.NOT_FOUND.value());
		return ResponseEntity.status(HttpStatus.NOT_FOUND.value()).body(error);
	}
	
	@ExceptionHandler(DatabaseException.class)
	public ResponseEntity<StandardError> databaseException(DatabaseException e, HttpServletRequest req){
		StandardError error = new StandardError();
		error.setError("Data restriction is being violated");
		error.setMessage(e.getLocalizedMessage());
		error.setPath(req.getServletPath());
		error.setStatus(HttpStatus.BAD_REQUEST.value());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(error);
	}
}
