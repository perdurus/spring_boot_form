package com.bolsadeideas.springboot.di.app.models.services;

import org.springframework.stereotype.Component;

//@Component("miServicioComplejo")
public class MiServicioComplejo implements IServicio {

	@Override
	public String operacion() {
		return "Hola mundo!! Ejecutando algún proceso pero mas complicado!!!";
	}
}
