package com.bolsadeideas.springboot.di.app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.bolsadeideas.springboot.di.app.models.services.IServicio;

@Controller
public class indexController {

	//Inyecta la primera clase que encuentra o la que encaja con el nombre del atributo
	@Autowired
	//@Qualifier("miServicioSimple")
	@Qualifier("miServicioComplejo")
	private IServicio miServicio; 

//	//En el constructor no necesitamos Autowired. De forma implicita ya lo lleva siempre que los atributos sean de una clase/interface
//	//anotada como @Component o alguna de sus variantes
//	@Autowired
//	public indexController(IServicio miServicio) {
//		super();
//		this.miServicio = miServicio;
//	}
	
	@GetMapping({"/index", "/", ""})
	public String index(Model model) {
		
		model.addAttribute("titulo", "Inyección de Dependencia");
		model.addAttribute("objeto", miServicio.operacion());
		
		return "index";
	}

//	public IServicio getMiServicio() {
//		return miServicio;
//	}
//
////	@Autowired
////	public void setMiServicio(IServicio miServicio) {
////		this.miServicio = miServicio;
////	}
	
	
	
}
