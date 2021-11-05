package com.canal.sergio.springboot.form.app.models.domain;

public class Rol {
	
	private Integer id;
	
	private String nombre;
	
	private String role;

	
	
	public Rol() {
	}

	public Rol(Integer id, String nombre, String role) {
		this.id = id;
		this.nombre = nombre;
		this.role = role;
	}

	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return the nombre
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * @param nombre the nombre to set
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	/**
	 * @return the role
	 */
	public String getRole() {
		return role;
	}

	/**
	 * @param role the role to set
	 */
	public void setRole(String role) {
		this.role = role;
	}
	

	@Override
	public boolean equals(Object obj) {
		
		if (this == obj){
			return true;
		}
		
		if ((obj instanceof Rol)) {
			Rol role = (Rol) obj;
			return this.id!= null && this.id.equals(role.getId());
		}
		
		return false;
	}
}
