package com.canal.sergio.springboot.form.app.editors;

import java.beans.PropertyEditorSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.canal.sergio.springboot.form.app.services.PaisService;

@Component
public class PaisPropertyEditor extends PropertyEditorSupport{
	
	@Autowired
	private PaisService paisService;

	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		if (text != null && text.length() >0) {
			try {
				Integer id = Integer.parseInt(text);
				this.setValue(paisService.obtenerPorId(id));
				
			}catch(NumberFormatException nfe) {
				setValue(null);
			}
		}else {
			setValue(null);
		}
	}	
	
}
