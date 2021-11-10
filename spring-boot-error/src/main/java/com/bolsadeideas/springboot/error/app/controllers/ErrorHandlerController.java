package com.bolsadeideas.springboot.error.app.controllers;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.bolsadeideas.springboot.error.app.errors.UsuarioNoEncontradoException;

@ControllerAdvice
public class ErrorHandlerController {
	
	@ExceptionHandler(ArithmeticException.class)
	public String aritmeticaError (ArithmeticException e, Model model) {
		
		model.addAttribute("error", "Error de aritmetica");
		model.addAttribute("message", e.getMessage());
		model.addAttribute("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
		model.addAttribute("timestamp", new Date());
		
		return "error/error-generico";
	}

	
	@ExceptionHandler(NumberFormatException.class)
	public String numberFormatError (NumberFormatException e, Model model) {
		
		model.addAttribute("error", "Error: formato numerico incorrecto");
		model.addAttribute("message", e.getMessage());
		model.addAttribute("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
		model.addAttribute("timestamp", new Date());
		
		return "error/error-generico";
	}
	
	
	@ExceptionHandler(UsuarioNoEncontradoException.class)
	public String usuarioNoEncontradoError (UsuarioNoEncontradoException e, Model model) {
		
		model.addAttribute("error", "Error: usuario no encontrado");
		model.addAttribute("message", e.getMessage());
		model.addAttribute("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
		model.addAttribute("timestamp", new Date());
		
		return "error/usuario";
	}
}
