package com.bolsadeideas.springboot.app.controllers;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bolsadeideas.springboot.app.models.entity.Cliente;
import com.bolsadeideas.springboot.app.services.IClienteService;
import com.bolsadeideas.springboot.app.util.paginator.PageRender;

@Controller
@SessionAttributes("cliente")
public class ClienteController {

	@Autowired
	@Qualifier("clienteService")
	private IClienteService clienteService;
	
	@RequestMapping(value="/listar", method=RequestMethod.GET)
	public String listar(@RequestParam(name="page", defaultValue="0") int page,  Model model) {
		
		Pageable pageRequest = PageRequest.of(page, 5);
		
		Page<Cliente> clientes = this.clienteService.findAll(pageRequest);
		
		PageRender<Cliente> pageRender = new PageRender<Cliente>("/listar", clientes);
		
		model.addAttribute("titulo", "Listado de clientes");
		//model.addAttribute("clientes", this.clienteService.findAll());
		model.addAttribute("clientes", clientes);
		model.addAttribute("page", pageRender);
		
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
	public String guardar(@Valid @ModelAttribute("cliente") Cliente cliente, BindingResult result, Model model, 
			RedirectAttributes flash, SessionStatus status) {
		
		if(result.hasErrors()) {
			model.addAttribute("titulo", "Alta de clientes");
			//model.addAttribute("cliente", cliente); Ya lo hemos pasado en la entrada del parametro
			return "form";	
		}
		
		String mensaje = (cliente.getId() != null)?"Cliente editado con éxito" : "Cliente creado con éxito";
		
		this.clienteService.save(cliente);
		status.setComplete();//Eliminamos la sesión
		flash.addFlashAttribute("success", mensaje);
		return "redirect:/listar";
	}
	
	
	@RequestMapping(value="/form/{id}", method=RequestMethod.GET)
	public String modificar(@PathVariable(value="id") Long id,  Map<String, Object> model, RedirectAttributes flash) {
		
		Cliente cliente = null;
		//Long id = Long.parseLong(idString);
		
		if (id > 0) {
			cliente = clienteService.findOne(id);
			if (cliente == null) {
				flash.addFlashAttribute("error", "El ID de cliente no éxiste en BBDD");
				return "redirect:/listar";
			}
		}else {
			flash.addFlashAttribute("error", "El ID no puede ser cero!");
			return "redirect:/listar";
		}
		
		model.put("cliente", cliente);
		model.put("titulo", "Editar Cliente: " + id);
		
		return "form";
	}
	
	@RequestMapping(value="/eliminar/{id}", method=RequestMethod.GET)
	public String eliminar(@PathVariable(value="id") Long id,  Map<String, Object> model, RedirectAttributes flash) {
		
		if (id > 0) {
			clienteService.delete(id);
			flash.addFlashAttribute("success", "Cliente eliminado con éxito");
		}
		
		return "redirect:/listar";
	}
}
