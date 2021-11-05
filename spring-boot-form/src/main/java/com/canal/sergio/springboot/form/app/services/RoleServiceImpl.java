package com.canal.sergio.springboot.form.app.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.canal.sergio.springboot.form.app.models.domain.Rol;

@Service
public class RoleServiceImpl implements RoleService {

	private List<Rol> roles;
	
	public RoleServiceImpl() {
		this.roles = new ArrayList<Rol>();
		
		this.roles.add(new Rol(1, "Administrador", "ROLE_ADMIN"));
		this.roles.add(new Rol(2, "Usuario", "ROLE_USER"));
		this.roles.add(new Rol(3, "Moderator", "ROLE_MODERATOR"));
	}

	@Override
	public List<Rol> listar() {
		// TODO Auto-generated method stub
		return this.roles;
	}

	@Override
	public Rol obtenerPorId(Integer id) {
		
		Rol resultado = null;
		
		for (Rol role: this.roles) {
			if (id == role.getId()) {
				resultado = role;
				break;
			}
		}
		
		return resultado;
	}

}
