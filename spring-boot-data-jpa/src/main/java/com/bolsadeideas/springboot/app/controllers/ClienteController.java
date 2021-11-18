package com.bolsadeideas.springboot.app.controllers;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import com.bolsadeideas.springboot.app.models.entity.Cliente;
import com.bolsadeideas.springboot.app.services.IClienteService;

@Controller
@SessionAttributes("cliente")
public class ClienteController {

	@Autowired
	@Qualifier("clienteService")
	private IClienteService clienteService;
	
	@RequestMapping(value="/listar", method=RequestMethod.GET)
	public String listar(Model model) {
		
		model.addAttribute("titulo", "Listado de clientes");
		
		model.addAttribute("clientes", this.clienteService.findAll());
		
		return "listar";
	}
	
	
	@RequestMapping(value="/form", method=RequestMethod.GET)
	public String crear(Map<String, Object> model) {
		
		Cliente cliente = new Cliente();
		model.put("cliente", cliente);
		model.put("titulo", "Alta de clientes");
		
		return "form";
	}
	
	
	@RequestMapping(value="/form", method=RequestMethod.POST)
	public String guardar(@Valid @ModelAttribute("cliente") Cliente cliente, BindingResult result, Model model, SessionStatus status) {
		
		if(result.hasErrors()) {
			model.addAttribute("titulo", "Alta de clientes");
			//model.addAttribute("cliente", cliente); Ya lo hemos pasado en la entrada del parametro
			return "form";	
		}
		
		this.clienteService.save(cliente);
		status.setComplete();//Eliminamos la sesión
		return "redirect:/listar";
	}
	
	
	@RequestMapping(value="/form/{id}", method=RequestMethod.GET)
	public String modificar(@PathVariable(value="id") Long id,  Map<String, Object> model) {
		
		Cliente cliente = null;
		//Long id = Long.parseLong(idString);
		
		if (id > 0) {
			cliente = clienteService.findOne(id);
		}else {
			return "redirect:/listar";
		}
		
		model.put("cliente", cliente);
		model.put("titulo", "Editar Cliente: " + id);
		
		return "form";
	}
	
	@RequestMapping(value="/eliminar/{id}", method=RequestMethod.GET)
	public String eliminar(@PathVariable(value="id") Long id,  Map<String, Object> model) {
		
		if (id > 0) {
			clienteService.delete(id);
		}
		
		return "redirect:/listar";
	}
}
