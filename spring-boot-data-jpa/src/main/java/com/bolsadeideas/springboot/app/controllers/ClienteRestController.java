package com.bolsadeideas.springboot.app.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.bolsadeideas.springboot.app.models.entity.Cliente;
import com.bolsadeideas.springboot.app.services.IClienteService;
import com.bolsadeideas.springboot.app.view.xml.ClienteList;

@RestController
@RequestMapping("/api/clientes")
public class ClienteRestController {
	
	@Autowired
	@Qualifier("clienteService")
	private IClienteService clienteService;
	
	@GetMapping(value="/listar")
	@ResponseBody
	public List<Cliente> listar() {
		return this.clienteService.findAll();
	}
	
	@GetMapping(value="/listar-xml")
	@ResponseBody
	public ClienteList listarRestXml() {
		return new ClienteList(this.clienteService.findAll());
	}

}
