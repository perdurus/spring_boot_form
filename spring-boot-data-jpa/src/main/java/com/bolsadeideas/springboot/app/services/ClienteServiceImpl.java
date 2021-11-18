package com.bolsadeideas.springboot.app.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bolsadeideas.springboot.app.models.dao.IClienteJPADao;
import com.bolsadeideas.springboot.app.models.entity.Cliente;

@Service
@Repository("clienteService")
public class ClienteServiceImpl implements IClienteService {

	@Autowired
	IClienteJPADao clienteDao;
	
	@Override
	@Transactional(readOnly=true)
	public List<Cliente> findAll() {
		return (List<Cliente>) this.clienteDao.findAll();
	}

	@Override
	@Transactional
	public void save(Cliente cliente) {
		this.clienteDao.save(cliente);
	}

	@Override
	@Transactional(readOnly=true)
	public Cliente findOne(Long id) {
		return this.clienteDao.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public void delete(Long id) {
		this.clienteDao.deleteById(id);
	}

}
