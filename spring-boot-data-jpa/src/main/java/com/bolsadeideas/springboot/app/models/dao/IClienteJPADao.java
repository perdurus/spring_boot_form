package com.bolsadeideas.springboot.app.models.dao;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.bolsadeideas.springboot.app.models.entity.Cliente;

//public interface IClienteJPADao extends CrudRepository<Cliente, Long>{ //Sin paginación
public interface IClienteJPADao extends PagingAndSortingRepository<Cliente, Long>{
	
	

}
