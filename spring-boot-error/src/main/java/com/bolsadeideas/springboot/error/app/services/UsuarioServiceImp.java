package com.bolsadeideas.springboot.error.app.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.bolsadeideas.springboot.error.app.models.domain.Usuario;

@Service
public class UsuarioServiceImp implements UsuarioService{

	private List<Usuario> lista;
	
	
	public UsuarioServiceImp() {
		lista = new ArrayList<Usuario>();
		this.lista.add(new Usuario(1, "Sergio", "Canal"));
		this.lista.add(new Usuario(2, "Virginia", "Canal"));
		this.lista.add(new Usuario(3, "Paco", "Pil"));
		this.lista.add(new Usuario(4, "Amparo", "Larrañaga"));
		this.lista.add(new Usuario(5, "Sara", "Varas"));
		this.lista.add(new Usuario(6, "Silvia", "Marso"));
		this.lista.add(new Usuario(7, "Laura", "Perez"));
	}

	@Override
	public List<Usuario> listar() {
		// TODO Auto-generated method stub
		return this.lista;
	}

	@Override
	public Usuario obtenerPorId(Integer id) {
		
		Usuario resultado = null;
		
		for (Usuario u : lista) {
			if(u.getId().equals(id)) {
				resultado = u;
				break;
			}
		}
		
		return resultado;
	}

	@Override
	public Optional<Usuario> obtenerPorIdOptional(Integer id) {
		Usuario usuario = this.obtenerPorId(id);
		 
		return Optional.ofNullable(usuario);
	}
}
