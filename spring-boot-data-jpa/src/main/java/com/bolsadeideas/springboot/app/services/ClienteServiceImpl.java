package com.bolsadeideas.springboot.app.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bolsadeideas.springboot.app.models.dao.IClienteJPADao;
import com.bolsadeideas.springboot.app.models.dao.IFacturaDao;
import com.bolsadeideas.springboot.app.models.dao.IProductoDao;
import com.bolsadeideas.springboot.app.models.entity.Cliente;
import com.bolsadeideas.springboot.app.models.entity.Factura;
import com.bolsadeideas.springboot.app.models.entity.Producto;

@Service
@Repository("clienteService")
public class ClienteServiceImpl implements IClienteService {

	@Autowired
	private IClienteJPADao clienteDao;
	
	@Autowired
	private IProductoDao productoDao;
	
	@Autowired
	private IFacturaDao facturaDao;
	
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

	@Override
	@Transactional(readOnly=true)
	public Page<Cliente> findAll(Pageable pageable) {
		return this.clienteDao.findAll(pageable);
	}

	@Override
	public List<Producto> findByNombre(String nombre) {
		// TODO Auto-generated method stub
		//return this.productoDao.findByNombre(nombre);
		return this.productoDao.findByNombreLikeIgnoreCase("%" + nombre + "%");
	}

	@Override
	@Transactional
	public void saveFactura(Factura factura) {
		
		this.facturaDao.save(factura);
	}

	@Override
	@Transactional(readOnly = true)
	public Producto findProductoById(Long id) {
		
		return this.productoDao.findById(id).orElse(null);
	}

	@Override
	@Transactional(readOnly = true)
	public Factura findFacturaById(Long id) {
		return this.facturaDao.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public void deleteFactura(Long id) {
		this.facturaDao.deleteById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public Factura fetchFacturaByIdWithClienteWithItemFacturaWithProducto(Long id) {
		return this.facturaDao.fetchByIdWithClienteWithItemFacturaWithProducto(id);
	}

	@Override
	public Cliente fetchClienteByIdWithFacturas(Long id) {
		return this.clienteDao.fetchByIdWithFacturas(id);
	}

	
}
