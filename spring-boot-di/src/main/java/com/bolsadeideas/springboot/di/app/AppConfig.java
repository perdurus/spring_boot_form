package com.bolsadeideas.springboot.di.app;

import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.bolsadeideas.springboot.di.app.models.domain.ItemFactura;
import com.bolsadeideas.springboot.di.app.models.domain.Producto;
import com.bolsadeideas.springboot.di.app.models.services.IServicio;
import com.bolsadeideas.springboot.di.app.models.services.MiServicio;
import com.bolsadeideas.springboot.di.app.models.services.MiServicioComplejo;

@Configuration
public class AppConfig {

	@Bean("miServicioSimple")
	public IServicio registrarMiServicioSimple() {
		return new MiServicio();
	}
	
	@Bean("miServicioComplejo")
	@Primary
	public IServicio registrarMiServicioComplejo() {
		return new MiServicioComplejo();
	}
	
	
	@Bean("itemsFactura")
	public List<ItemFactura> registrarItems(){
		
		Producto producto1 = new Producto("Cámara Sony", 100);
		Producto producto2 = new Producto("Bicicleta Bianchi", 200);
		Producto producto3 = new Producto("Laptop HP", 300);
		
		ItemFactura item1 = new ItemFactura(producto1, 4);
		ItemFactura item2 = new ItemFactura(producto2, 10);
		ItemFactura item3 = new ItemFactura(producto3, 7);
		
		return Arrays.asList(item1, item2, item3);
	}
	
	
	@Bean("itemsFacturaOficina")
	@Primary
	public List<ItemFactura> registrarItemsOficina(){
		
		Producto producto1 = new Producto("Monitor LG ",250);
		Producto producto2 = new Producto("Impresora HP multifunción", 500);
		Producto producto3 = new Producto("Laptop HP", 1000);
		Producto producto4 = new Producto("Escritorio Oficina", 300);
		
		ItemFactura item1 = new ItemFactura(producto1, 5);
		ItemFactura item2 = new ItemFactura(producto2, 8);
		ItemFactura item3 = new ItemFactura(producto3, 12);
		ItemFactura item4 = new ItemFactura(producto4, 15);
		
		return Arrays.asList(item1, item2, item3, item4);
	}
}
