package com.canal.sergio.springboot.form.app.services;

import java.util.List;

import com.canal.sergio.springboot.form.app.models.domain.Rol;

public interface RoleService {
	
	public List<Rol> listar();
	public Rol obtenerPorId(Integer id);

}
