package com.bolsadeideas.springboot.di.app.models.domain;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

@Component
@RequestScope
public class Factura{
	
	

	@Value("${factura.desc}")
	private String descripcion;
	
	@Autowired
	private Cliente cliente;
	
	@Autowired
	private List<ItemFactura> items;
	
	
	//Despues del constructor
	@PostConstruct
	public void inicializar() {
		
		this.cliente.setNombre(this.cliente.getNombre() + " Andrés");
		this.descripcion = this.descripcion + " del cliente: " + this.cliente.getNombre();
		
	}
	
	@PreDestroy
	public void finalizar() {
		System.out.println("Estamos finalizando nuestro objeto: " + Factura.class);
	}
	
	
	/**
	 * @return the descripcion
	 */
	public String getDescripcion() {
		return descripcion;
	}
	/**
	 * @param descripcion the descripcion to set
	 */
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	/**
	 * @return the cliente
	 */
	public Cliente getCliente() {
		return cliente;
	}
	/**
	 * @param cliente the cliente to set
	 */
	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}
	/**
	 * @return the items
	 */
	public List<ItemFactura> getItems() {
		return items;
	}
	/**
	 * @param items the items to set
	 */
	public void setItems(List<ItemFactura> items) {
		this.items = items;
	}

}
