package com.bolsadeideas.springboot.app.util.paginator;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;

public class PageRender<T> {
	
	private String url;
	
	private Page<T> page;

	private int totalPaginas;
	
	private int numPaginasVisibles;
	
	private int paginaActual;
	
	private List<PageItem> paginas;
	
	public PageRender(String url, Page<T> page) {
		
		this.url = url;
		this.page = page;
		this.paginas = new ArrayList<PageItem>();
		
		this.numPaginasVisibles = page.getSize();
		this.totalPaginas = page.getTotalPages();
		this.paginaActual = page.getNumber();
		
		int desde, hasta;
		if (this.totalPaginas <= this.numPaginasVisibles ) {
			desde =0;
			hasta = this.totalPaginas-1;
		}else {
			if (this.paginaActual <= this.numPaginasVisibles/2) {
				desde = 0;
				hasta= this.numPaginasVisibles -1;
			
			}else if(this.paginaActual >= this.totalPaginas - this.numPaginasVisibles/2) {
				desde = this.totalPaginas - this.numPaginasVisibles;
				hasta = this.numPaginasVisibles-1;
			
			}else {
				desde = this.paginaActual - this.numPaginasVisibles/2;
				hasta = this.numPaginasVisibles-1;
			}
		}
		
		for (int i=0; i <= hasta ; i++) {
			this.paginas.add(new PageItem(desde + i, this.paginaActual==desde+i));
		}
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @return the totalPaginas
	 */
	public int getTotalPaginas() {
		return totalPaginas;
	}

	/**
	 * @return the paginaActual
	 */
	public int getPaginaActual() {
		return paginaActual;
	}

	/**
	 * @return the paginas
	 */
	public List<PageItem> getPaginas() {
		return paginas;
	}

	
	public boolean isFirst() {
		return this.page.isFirst();
	}
	
	public boolean isLast() {
		return this.page.isLast();
	}
	
	public boolean isHasNext() {
		return this.page.hasNext();
	}
	
	public boolean isHasPrevious() {
		return this.page.hasPrevious();
	}
}
