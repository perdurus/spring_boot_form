package com.bolsadeideas.springboot.web.app.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

	@GetMapping("/")
	public String home() {
		
		//return "redirect:/app/index"; //Cambia la ruta
		return "forward:/app/index"; //No cambia la ruta. Rutas propias del proyecto
	}
}
