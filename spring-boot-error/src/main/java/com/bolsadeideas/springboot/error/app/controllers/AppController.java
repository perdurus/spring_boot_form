package com.bolsadeideas.springboot.error.app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.bolsadeideas.springboot.error.app.errors.UsuarioNoEncontradoException;
import com.bolsadeideas.springboot.error.app.models.domain.Usuario;
import com.bolsadeideas.springboot.error.app.services.UsuarioService;

@Controller
public class AppController {
	
	@Autowired
	private UsuarioService service;
	
	
	@GetMapping({"/", "/index"})
	public String index(Model model) {
		
		
		@SuppressWarnings("unused")
		Integer valor = 100/0;
		//Integer valor = Integer.parseInt("hola");
		return "index";
	}
	
	//@GetMapping("/ver/{id}")
	public String ver(@PathVariable Integer id, Model model) {
		Usuario usuario = this.service.obtenerPorId(id);
	
		if (usuario == null) {
			throw new UsuarioNoEncontradoException(id.toString());
		}
		model.addAttribute("titulo", "Detalle del usuario: " + usuario.getNombre());
		model.addAttribute("usuario", usuario);
		
		return "ver";
	}
	
	/**Con el objeto Optional*/
	@GetMapping("/ver/{id}")
	public String verOptional(@PathVariable Integer id, Model model) {
		
		Usuario usuario = this.service.obtenerPorIdOptional(id).orElseThrow( ()-> new UsuarioNoEncontradoException(id.toString()));
	
		model.addAttribute("titulo", "Detalle del usuario: " + usuario.getNombre());
		model.addAttribute("usuario", usuario);
		
		return "ver";
	}
}
