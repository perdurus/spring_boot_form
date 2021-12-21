package com.bolsadeideas.springboot.app.models.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.bolsadeideas.springboot.app.models.entity.Cliente;

//public interface IClienteJPADao extends CrudRepository<Cliente, Long>{ //Sin paginación
public interface IClienteJPADao extends PagingAndSortingRepository<Cliente, Long>{
	
	@Query("select c from Cliente c left join fetch c.facturas f where c.id=?1")
	public Cliente fetchByIdWithFacturas(Long id);

}
