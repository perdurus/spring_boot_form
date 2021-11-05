package com.canal.sergio.springboot.form.app.services;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

import com.canal.sergio.springboot.form.app.models.domain.Pais;

@Service
public class PaisServicesImpl implements PaisService {

	private List<Pais> lista;
	
	public PaisServicesImpl() {
		this.lista = Arrays.asList(
				new Pais(1, "ES", "España"), 
				new Pais(2, "MX", "Mexico"), 
				new Pais(3, "CL", "Chile"),
				new Pais(4, "CO", "Colombia"), 
				new Pais(5, "VE", "Venezuela"), 
				new Pais(6, "PE", "Perú"),
				new Pais(7, "PA", "Panamá"), 
				new Pais(8, "AR", "Argentina"));
	}

	@Override
	public List<Pais> listar() {
		// TODO Auto-generated method stub
		return this.lista;
	}

	@Override
	public Pais obtenerPorId(Integer id) {
		
		Pais resultado = null;
		
		for (Pais pais : this.lista) {
			if (id == pais.getId()) {
				resultado = pais;
				break;
			}
			
		}		
		
		return resultado;
	}

}
