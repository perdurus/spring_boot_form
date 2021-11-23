package com.bolsadeideas.springboot.app.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.bolsadeideas.springboot.app.models.entity.Cliente;

public interface IClienteService {

	public List<Cliente> findAll();
	public void save(Cliente cliente);
	public Cliente findOne(Long id);
	public void delete(Long id);
	
	//Paginación
	public Page<Cliente> findAll(Pageable pageable);
}
