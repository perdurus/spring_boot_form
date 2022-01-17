package com.bolsadeideas.springboot.app.controllers;

import java.security.Principal;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class LoginController {

	@Autowired
	private MessageSource messageSource;
	
	@GetMapping("/login")
	public String login(
			@RequestParam(value="error", required=false) String error,
			@RequestParam(value="logout", required=false) String logout,
			Model model, Principal principal, RedirectAttributes flash,
			Locale locale) {
		
		if (principal != null){
			//flash.addFlashAttribute("info", "Ya se ha iniciado sesión con anterioridad");
			flash.addFlashAttribute("info", messageSource.getMessage("text.login.already",null, null,  locale));
			
			return "redirect:/";
		}
		
		if (error != null) {
			//Mensajito
			//model.addAttribute("error", "Error en el login: Nombre de usuario o contraseña incorrectos");
			model.addAttribute("error", messageSource.getMessage("text.login.error",null, null,  locale));
		}
		
		if (logout != null) {
			//Mensajito
			//model.addAttribute("success", "Ha cerrado sesión con éxito");
			model.addAttribute("success", messageSource.getMessage("text.login.logout",null, null,  locale));
		}
		
		return "login";
	}
}
