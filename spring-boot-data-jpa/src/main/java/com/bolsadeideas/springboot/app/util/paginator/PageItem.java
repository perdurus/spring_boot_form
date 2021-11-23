package com.bolsadeideas.springboot.app.util.paginator;

public class PageItem {
	
	private int numero;
	
	private boolean actual;

	public PageItem(int numero, boolean actual) {
		super();
		this.numero = numero;
		this.actual = actual;
	}

	/**
	 * @return the numero
	 */
	public int getNumero() {
		return numero;
	}

	/**
	 * @param numero the numero to set
	 */
	public void setNumero(int numero) {
		this.numero = numero;
	}

	/**
	 * @return the actual
	 */
	public boolean isActual() {
		return actual;
	}

	/**
	 * @param actual the actual to set
	 */
	public void setActual(boolean actual) {
		this.actual = actual;
	}

	
	
}
