package com.bolsadeideas.springboot.web.app.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/variables")
public class EjemploVariablesRutaController {

	
	@GetMapping("/")
	private String index(Model model) {
		
		model.addAttribute("titulo", "Enviamos parametros por URL");
		
		return "variables/index";
	}
	
	@GetMapping("/string/{texto}")
	public String variables(@PathVariable String  texto, Model model) {
		
		model.addAttribute("titulo", "Recibimos parametros GET en ruta variable");
		model.addAttribute("resultado", "El texto enviado en la ruta: " + texto);
		
		return "variables/ver";
	}
	
	
	@GetMapping("/string/{texto}/{numero}")
	public String variables(@PathVariable String  texto, @PathVariable Integer  numero, Model model) {
		
		model.addAttribute("titulo", "Recibimos parametros GET en ruta variable");
		model.addAttribute("resultado", "El texto enviado en la ruta: " + texto + "El numero enviado en el path: " + numero);
		
		return "variables/ver";
	}
}
