package com.canal.sergio.springboot.form.app.models.domain;

public class Pais {
	
	private Integer id;
	
	private String nombre;
	
	private String codigo;


	public Pais() {
	}

	public Pais(Integer id, String codigo, String nombre) {
		this.id = id;
		this.nombre = nombre;
		this.codigo = codigo;
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
	 * @return the codigo
	 */
	public String getCodigo() {
		return codigo;
	}

	/**
	 * @param codigo the codigo to set
	 */
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	@Override
	public String toString() {
		return this.id.toString();
	}
	
}
