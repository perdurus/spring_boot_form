package com.bolsadeideas.springboot.app.controllers;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bolsadeideas.springboot.app.models.entity.Cliente;
import com.bolsadeideas.springboot.app.services.IClienteService;
import com.bolsadeideas.springboot.app.services.IUploadFileService;
import com.bolsadeideas.springboot.app.util.paginator.PageRender;

@Controller
@SessionAttributes("cliente")
public class ClienteController {

	@Autowired
	@Qualifier("clienteService")
	private IClienteService clienteService;
	
	@Autowired
	@Qualifier("uploadFileService")
	private IUploadFileService uploadFileService;
	
	protected final Log logger = LogFactory.getLog(this.getClass());
	
	@RequestMapping(value={"/listar", "/"}, method=RequestMethod.GET)
	public String listar(@RequestParam(name="page", defaultValue="0") int page,  Model model, Authentication authentication, 
			HttpServletRequest request) {
		
		if (authentication != null) {
			logger.info("Hola usuario autenticado, tu username es: " + authentication.getName());
		}
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			logger.info("Usando SecurityContextHolder de forma estática: Hola usuario autenticado, tu username es: " + auth.getName());
		}
		
		if (hasRole("ROLE_ADMIN")) {
			logger.info("Hola usuario: " + auth.getName() + " tienes acceso");
		}else {
			logger.info("Hola usuario: " + auth.getName() + " NO tienes acceso");
		}
		
		SecurityContextHolderAwareRequestWrapper securityContext = new SecurityContextHolderAwareRequestWrapper(request, "ROLE_");
		if (securityContext.isUserInRole("ADMIN")) {
			logger.info("Usando SecurityContextHolderAwareRequestWrapper: Hola usuario " + auth.getName() + " tienes acceso");
		}else {
			logger.info("Usando SecurityContextHolderAwareRequestWrapper: Hola usuario " + auth.getName() + " NO tienes acceso");
		}
		
		if (request.isUserInRole("ROLE_ADMIN")) {
			logger.info("Usando HttpServletRequest: Hola usuario " + auth.getName() + " tienes acceso");
		}else {
			logger.info("Usando HttpServletRequest: Hola usuario " + auth.getName() + " NO tienes acceso");
		}
		
		Pageable pageRequest = PageRequest.of(page, 5);
		
		Page<Cliente> clientes = this.clienteService.findAll(pageRequest);
		
		PageRender<Cliente> pageRender = new PageRender<Cliente>("/listar", clientes);
		
		model.addAttribute("titulo", "Listado de clientes");
		//model.addAttribute("clientes", this.clienteService.findAll());
		model.addAttribute("clientes", clientes);
		model.addAttribute("page", pageRender);
		
		return "listar";
	}
	
	@Secured("ROLE_ADMIN")
	@RequestMapping(value="/form", method=RequestMethod.GET)
	public String crear(Map<String, Object> model) {
		
		Cliente cliente = new Cliente();
		model.put("cliente", cliente);
		model.put("titulo", "Alta de clientes");
		
		return "form";
	}
	
	
	@Secured("ROLE_ADMIN")
	@RequestMapping(value="/form", method=RequestMethod.POST)
	public String guardar(@Valid @ModelAttribute("cliente") Cliente cliente, BindingResult result, Model model, 
			@RequestParam(name="file") MultipartFile foto, RedirectAttributes flash, SessionStatus status) {
		
		if(result.hasErrors()) {
			model.addAttribute("titulo", "Alta de clientes");
			//model.addAttribute("cliente", cliente); Ya lo hemos pasado en la entrada del parametro
			return "form";	
		}
		
		//Tratamiento de la imagen
		if(!foto.isEmpty()) {
			
			if (cliente.getId() != null && cliente.getId() > 0 && 
				cliente.getFoto()!=null && cliente.getFoto().length()>0) {
				try {
					if(this.uploadFileService.delete(cliente.getFoto())) {
						flash.addFlashAttribute("info", "Foto " + cliente.getFoto() + " eliminada con éxito");
					}
				} catch (IOException e) {
					//e.printStackTrace();
					flash.addFlashAttribute("error", "Foto " + cliente.getFoto() + " NO ha sido eliminada porque no existe.");
				}					
			}


			try {
				cliente.setFoto(this.uploadFileService.copy(foto));
				flash.addFlashAttribute("info", "La imagen se ha subido correctamente '" + cliente.getFoto() + "'");
			} catch (IOException e) {
				//e.printStackTrace();
				flash.addFlashAttribute("error", "Foto " + foto.getOriginalFilename() + " NO ha subido porque ha habido algún problema.");
			}
		}
		
		String mensaje = (cliente.getId() != null)?"Cliente editado con éxito" : "Cliente creado con éxito";
		
		this.clienteService.save(cliente);
		status.setComplete();//Eliminamos la sesión
		flash.addFlashAttribute("success", mensaje);
		return "redirect:/listar";
	}
	
	//@Secured("ROLE_ADMIN")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value="/form/{id}", method=RequestMethod.GET)
	public String modificar(@PathVariable(value="id") Long id,  Map<String, Object> model, RedirectAttributes flash) {
		
		Cliente cliente = null;
		//Long id = Long.parseLong(idString);
		
		if (id > 0) {
			cliente = clienteService.findOne(id);
			if (cliente == null) {
				flash.addFlashAttribute("error", "El ID de cliente no éxiste en BBDD");
				return "redirect:/listar";
			}
		}else {
			flash.addFlashAttribute("error", "El ID no puede ser cero!");
			return "redirect:/listar";
		}
		
		model.put("cliente", cliente);
		model.put("titulo", "Editar Cliente: " + id);
		
		return "form";
	}
	
	
	@Secured("ROLE_ADMIN")
	@RequestMapping(value="/eliminar/{id}", method=RequestMethod.GET)
	public String eliminar(@PathVariable(value="id") Long id,  Map<String, Object> model, RedirectAttributes flash) {
		
		if (id > 0) {
			//Necesitamos el cliente para eliminar la foto
			Cliente cliente = this.clienteService.findOne(id);
			
			clienteService.delete(id);
			flash.addFlashAttribute("success", "Cliente eliminado con éxito");
		
			try {
				if(this.uploadFileService.delete(cliente.getFoto())) {
					flash.addFlashAttribute("info", "Foto " + cliente.getFoto() + " eliminada con éxito");
				}
			} catch (IOException e) {
				//e.printStackTrace();
				flash.addFlashAttribute("error", "Foto " + cliente.getFoto() + " NO ha sido eliminada porque no existe.");
			}
		}
		
		return "redirect:/listar";
	}
	
	@PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
	@GetMapping(value="/ver/{id}")
	public String ver(@PathVariable(value="id") Long id, Map<String, Object> model, RedirectAttributes flash) {
		
		//Cliente cliente = clienteService.findOne(id);
		Cliente cliente = clienteService.fetchClienteByIdWithFacturas(id);
		
		if(cliente == null) {
			flash.addFlashAttribute("error", "El cliente no existe en el sistema");
			return "redirect:/listar";	
		}
		
		model.put("cliente", cliente);
		model.put("titulo", "Detalle cliente: " + cliente.getNombre());

		return "ver";
	}
	
	@Secured({"ROLE_USER", "ROLE_ADMIN"})
	@GetMapping(value="/uploads/{filename:.+}")
	public ResponseEntity<Resource> verFoto(@PathVariable String filename, RedirectAttributes flash){
 
		try {
			
			return ResponseEntity.ok().
					header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename +  "\"").
					body(this.uploadFileService.load(filename));
		}catch(Exception e) {
			//e.printStackTrace();
			flash.addFlashAttribute("error", "Foto " + filename + " NO se ha podido cargar.");
			return null;
		}		
	}
	
	
	
	private boolean hasRole(String role) {
		
		SecurityContext context = SecurityContextHolder.getContext();
		if (context == null) {
			return false;
		}
				
		Authentication auth = context.getAuthentication();
		if (auth == null) {
			return false;
		}
		
		Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
		
		/*
		for (GrantedAuthority authority : authorities) {
			
			if(role.equals(authority.getAuthority())) {
				logger.info("Hola usuario " + auth.getName() + " tu role es: " + authority.getAuthority());
				return true;
			}
		}
		
		return false;*/
		
		return authorities.contains(new SimpleGrantedAuthority(role));
	}
}
