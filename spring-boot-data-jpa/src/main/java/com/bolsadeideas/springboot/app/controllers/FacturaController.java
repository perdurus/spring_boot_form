package com.bolsadeideas.springboot.app.controllers;

import java.util.List;
import java.util.Locale;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bolsadeideas.springboot.app.models.entity.Cliente;
import com.bolsadeideas.springboot.app.models.entity.Factura;
import com.bolsadeideas.springboot.app.models.entity.ItemFactura;
import com.bolsadeideas.springboot.app.models.entity.Producto;
import com.bolsadeideas.springboot.app.services.IClienteService;

@Controller
@RequestMapping("/factura")
@SessionAttributes("factura")
@Secured("ROLE_ADMIN")
public class FacturaController {
	
	@Autowired
	private IClienteService clienteService;
	
	@Autowired
	private MessageSource messageSource;
	
	private final Logger log = LoggerFactory.getLogger(getClass());
	
	
	@GetMapping("/form/{idCliente}")
	public String crear(@PathVariable(value="idCliente") Long id, Model model, RedirectAttributes flash, Locale locale) {

		Cliente cliente = this.clienteService.findOne(id);
		
		if(cliente == null) {
			//flash.addFlashAttribute("error", "El cliente no existe en la BBDD");
			flash.addFlashAttribute("error", messageSource.getMessage("text.cliente.flash.db.error",null, null,  locale));
			
			return "redirect:/listar";
		}
		
		Factura factura = new Factura();
		factura.setCliente(cliente);
		
		model.addAttribute("factura", factura);
		model.addAttribute("titulo", messageSource.getMessage("text.factura.form.titulo",null, null,  locale));
		
		return "factura/form";
	}
	
	@GetMapping(value="/cargar-productos/{term}", produces = {"application/json"})
	public @ResponseBody List<Producto> cargarProductos(@PathVariable(value="term") String term, Model model, RedirectAttributes flash) {
		
		return this.clienteService.findByNombre(term);
		
	}
	
	@PostMapping("/form")
	public String guardar(@Valid Factura factura, BindingResult result, Model model,
			@RequestParam(name="item_id[]", required = false) Long[] itemId, 
			@RequestParam(name="cantidad[]", required = false) Integer[] cantidad,
			RedirectAttributes flash, SessionStatus status, Locale locale) {
		
		if (result.hasErrors()) {
			//model.addAttribute("titulo", "Crear factura");
			model.addAttribute("titulo", messageSource.getMessage("text.factura.form.titulo",null, null,  locale));
			return "factura/form";
		}
		
		if( itemId == null || itemId.length ==0){
			//model.addAttribute("error", "Error: la factura debe tener algún producto");
			model.addAttribute("error", messageSource.getMessage("text.factura.flash.lineas.error",null, null,  locale));
			return "factura/form";
		}
		
		for(int i=0; i<itemId.length; i++) {
			Producto producto = this.clienteService.findProductoById(itemId[i]);
			
			ItemFactura itemFactura = new ItemFactura();
			itemFactura.setCantidad(cantidad[i]);
			itemFactura.setProducto(producto);
			
			factura.addLineaFactura(itemFactura);
			
			this.log.info("ID: " + itemId[i].toString() + " Cantidad: " + cantidad[i].toString());
					
		}
		
		this.clienteService.saveFactura(factura);
		status.setComplete();
		
		//flash.addAttribute("success", "Factura creada con éxito");
		flash.addAttribute("success", messageSource.getMessage("text.factura.flash.crear.success",null, null,  locale));
		
		
		return "redirect:/ver/" + factura.getCliente().getId();
	}
	
	@GetMapping("/ver/{id}")
	public String ver(@PathVariable(value = "id") Long id, Model model, RedirectAttributes flash, Locale locale) {
		
		//Factura factura = this.clienteService.findFacturaById(id);
		Factura factura = this.clienteService.fetchFacturaByIdWithClienteWithItemFacturaWithProducto(id);
		
		if (factura == null ) {
			//flash.addAttribute("error", "La factura no existe en el sistema");
			flash.addAttribute("error", messageSource.getMessage("text.factura.flash.db.error",null, null,  locale));
			return "redirect: /listar";
		}
		
		model.addAttribute("factura", factura);
		//model.addAttribute("titulo", "Factura: " + factura.getDescripcion() );
		String mensaje = String.format(messageSource.getMessage("text.factura.ver.titulo", null, locale), factura.getDescripcion());
		model.addAttribute("titulo", mensaje);
		
		return "factura/ver";
	}
	
	@GetMapping("/eliminar/{id}")
	public String eliminar(@PathVariable(value = "id") Long id, RedirectAttributes flash, Locale locale) {
		
		Factura factura = this.clienteService.findFacturaById(id);
		
		if (factura != null) {
			this.clienteService.deleteFactura(id);
			//flash.addFlashAttribute("success", "factura eliminada con éxito");
			flash.addFlashAttribute("success", messageSource.getMessage("text.factura.flash.eliminar.success", null, locale));
			return "redirect:/ver/" + factura.getCliente().getId();

			//return "redirect:/listar";
		}
		
		//flash.addFlashAttribute("error", "La factura no existe. No se pudo eliminar");
		flash.addFlashAttribute("error", messageSource.getMessage("text.factura.flash.db.error", null, locale));
		
		return "redirect:/listar";
	}

}
