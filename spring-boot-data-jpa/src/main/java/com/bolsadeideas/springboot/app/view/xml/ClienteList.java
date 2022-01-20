package com.bolsadeideas.springboot.app.view.xml;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.bolsadeideas.springboot.app.models.entity.Cliente;

@XmlRootElement(name = "clientes")
public class ClienteList {
	
	@XmlElement(name="cliente")
	public List<Cliente> listaClientes;

	public ClienteList(List<Cliente> clientes) {
		this.listaClientes = clientes;
	}

	public ClienteList() {
	}

	/**
	 * @return the clientes
	 */
	public List<Cliente> getListaClientes() {
		return this.listaClientes;
	}	

}
