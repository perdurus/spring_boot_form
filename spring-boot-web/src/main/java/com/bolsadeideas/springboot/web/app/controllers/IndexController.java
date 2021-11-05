package com.bolsadeideas.springboot.web.app.controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bolsadeideas.springboot.web.app.models.Usuario;

@Controller
@RequestMapping("/app")
public class IndexController {
	
	@Value("${texto.indexcontroller.index.titulo}") 
	private String textoIndex;
	@Value("${texto.indexcontroller.perfil.titulo}") 
	private String textoPerfil;
	@Value("${texto.indexcontroller.listar.titulo}") 
	private String textoListar;
	
	//Las tres formas son iguales
	//@GetMapping(value = "/index")
	//@RequestMapping(value = "/index")
	//@RequestMapping(value = "/index", method = RequestMethod.GET)
	
	@GetMapping(value = {"/index", "/", "/home", ""})
	public String index(Model model) {
		
		
		model.addAttribute("titulo", this.textoIndex);
		model.addAttribute("titulo2", "Decorando con CSS");
		return "index";
	}
	
	@RequestMapping("/perfil")
	public String perfil(Model model) {
		Usuario usuario = new Usuario();
		usuario.setNombre("Sergio");
		usuario.setApellido("Canal");
		usuario.setEmail("unmail@gmail.com");
		
		model.addAttribute("usuario", usuario);
		model.addAttribute("titulo", this.textoPerfil + usuario.getNombre());
		
		return "perfil";
	}

	
	@RequestMapping("/listar")
	public String listar(Model model) {
		/*
		List<Usuario> usuarios = new ArrayList<Usuario>();
		
		usuarios.add(new Usuario("Sergio", "Canal Rodrigo", "unmail@gmail.com"));
		usuarios.add(new Usuario("Amparo", "de los Dolores Bienvenida", "dosmail@gmail.com"));
		usuarios.add(new Usuario("Chiquito", "de la Calzada", "tresmail@gmail.com"));
		*/
		
		model.addAttribute("titulo", this.textoListar);		
		
		return "listar";
	}
	
	@ModelAttribute("usuarios")
	public List<Usuario> llenarUsuarios(){
		
		List<Usuario> usuarios = Arrays.asList(
				new Usuario("Sergio", "Canal Rodrigo", "unmail@gmail.com"),
				new Usuario("Amparo", "de los Dolores Bienvenida", "dosmail@gmail.com"),
				new Usuario("Chiquito", "de la Calzada", "tresmail@gmail.com"),
				new Usuario("Cuatro", "Cuarto", "cuatromail@gmail.com")
				);
		return usuarios;
	}
}
