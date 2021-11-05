package com.bolsadeideas.springboot.web.app.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/params")
public class EjemploParamsController {

	@GetMapping("/")
	private String index(Model model) {
		
		model.addAttribute("titulo", "Enviamos parametros GET");
		
		return "params/index";
	}
	
	@GetMapping("/string")
	public String param(@RequestParam(name="texto", required = false, defaultValue = "Valor por defecto...") String texto,  Model model) {
		
		model.addAttribute("titulo", "Recibimos parametros GET");
		model.addAttribute("resultado", "El parametro enviado es: " + texto);	
		
		return "params/ver";
	}
	
	@GetMapping("/mix-params")
	public String param(@RequestParam String saludo, @RequestParam Integer numero,  Model model) {
		
		model.addAttribute("titulo", "Recibimos parametros GET String/Integer");
		model.addAttribute("resultado", "El string enviado es: '" + saludo +"'" + " El integer enviado es: '" + numero +"'");	
		
		return "params/ver";
	}
	
	@GetMapping("/mix-params-request")
	public String param(HttpServletRequest request,  Model model) {
		
		String saludo = request.getParameter("saludo");
		Integer numero = 0;
		try {
			numero = Integer.parseInt(request.getParameter("numero"));
		}catch(NumberFormatException e) {
			numero =0;
		}
		
		model.addAttribute("titulo", "Recibimos parametros GET con HttpServletRequest");
		model.addAttribute("resultado", "El string enviado es: '" + saludo +"'" + " El integer enviado es: '" + numero +"'");	
		
		return "params/ver";
	}
}
