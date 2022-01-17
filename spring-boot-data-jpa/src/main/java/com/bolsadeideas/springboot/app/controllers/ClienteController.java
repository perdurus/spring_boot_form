package com.bolsadeideas.springboot.app.controllers;

import java.io.IOException;
import java.util.Collection;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
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
	
	@Autowired
	private MessageSource messageSource;
	
	protected final Log logger = LogFactory.getLog(this.getClass());
	
	@RequestMapping(value={"/listar", "/"}, method=RequestMethod.GET)
	public String listar(@RequestParam(name="page", defaultValue="0") int page,  Model model, Authentication authentication, 
			HttpServletRequest request, Locale locale) {
		
		if (authentication != null) {
			logger.info("1.Hola usuario autenticado, tu username es: " + authentication.getName());
		}
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			logger.info("2.Usando SecurityContextHolder de forma estática: Hola usuario autenticado, tu username es: " + auth.getName());
			
			Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
			for (GrantedAuthority authority : authorities) {
					logger.info("2.*. Hola usuario " + auth.getName() + " tu role es: " + authority.getAuthority());
			}
		}
		
		if (hasRole("ROLE_ADMIN")) {
			logger.info("3.Hola usuario: " + auth.getName() + " tienes acceso");
		}else {
			logger.info("3.Hola usuario: " + auth.getName() + " NO tienes acceso");
		}
		
		SecurityContextHolderAwareRequestWrapper securityContext = new SecurityContextHolderAwareRequestWrapper(request, "ROLE_");
		if (securityContext.isUserInRole("ADMIN")) {
			logger.info("4.Usando SecurityContextHolderAwareRequestWrapper: Hola usuario " + auth.getName() + " tienes acceso");
		}else {
			logger.info("4.Usando SecurityContextHolderAwareRequestWrapper: Hola usuario " + auth.getName() + " NO tienes acceso");
		}
		
		if (request.isUserInRole("ROLE_ADMIN")) {
			logger.info("5.Usando HttpServletRequest: Hola usuario " + auth.getName() + " tienes acceso");
		}else {
			logger.info("5.Usando HttpServletRequest: Hola usuario " + auth.getName() + " NO tienes acceso");
		}
		
		Pageable pageRequest = PageRequest.of(page, 5);
		
		Page<Cliente> clientes = this.clienteService.findAll(pageRequest);
		
		PageRender<Cliente> pageRender = new PageRender<Cliente>("/listar", clientes);
		
		model.addAttribute("titulo", messageSource.getMessage("text.cliente.listar.titulo",null, null,  locale));
		//model.addAttribute("clientes", this.clienteService.findAll());
		model.addAttribute("clientes", clientes);
		model.addAttribute("page", pageRender);
		
		return "listar";
	}
	
	@Secured("ROLE_ADMIN")
	@RequestMapping(value="/form", method=RequestMethod.GET)
	public String crear(Map<String, Object> model, Locale locale) {
		
		Cliente cliente = new Cliente();
		model.put("cliente", cliente);
		model.put("titulo", messageSource.getMessage("text.cliente.crear",null, null,  locale));
		
		return "form";
	}
	
	
	@Secured("ROLE_ADMIN")
	@RequestMapping(value="/form", method=RequestMethod.POST)
	public String guardar(@Valid @ModelAttribute("cliente") Cliente cliente, BindingResult result, Model model, 
			@RequestParam(name="file") MultipartFile foto, RedirectAttributes flash, SessionStatus status, Locale locale) {
		
		if(result.hasErrors()) {
			model.addAttribute("titulo", messageSource.getMessage("text.cliente.crear",null, null,  locale));
			//model.addAttribute("cliente", cliente); Ya lo hemos pasado en la entrada del parametro
			return "form";	
		}
		
		//Tratamiento de la imagen
		if(!foto.isEmpty()) {
			
			if (cliente.getId() != null && cliente.getId() > 0 && 
				cliente.getFoto()!=null && cliente.getFoto().length()>0) {
				try {
					if(this.uploadFileService.delete(cliente.getFoto())) {
						String mensaje = String.format(messageSource.getMessage("text.cliente.flash.foto.eliminar.success", null, locale), cliente.getFoto());
						//flash.addFlashAttribute("info", "Foto " + cliente.getFoto() + " eliminada con éxito");
						flash.addFlashAttribute("info", mensaje);
						
					}
				} catch (IOException e) {
					//e.printStackTrace();
					String mensaje = String.format(messageSource.getMessage("text.cliente.flash.foto.eliminar.deny", null, locale), cliente.getFoto());
					//flash.addFlashAttribute("error", "Foto " + cliente.getFoto() + " NO ha sido eliminada porque no existe.");
					flash.addFlashAttribute("error", mensaje);
				}					
			}


			try {
				cliente.setFoto(this.uploadFileService.copy(foto));
				String mensaje = String.format(messageSource.getMessage("text.cliente.flash.foto.subir.success", null, locale), cliente.getFoto());
				//flash.addFlashAttribute("info", "La imagen se ha subido correctamente '" + cliente.getFoto() + "'");
				flash.addFlashAttribute("info", mensaje);
			} catch (IOException e) {
				//e.printStackTrace();
				String mensaje = String.format(messageSource.getMessage("text.cliente.flash.foto.ver.deny", null, locale), foto.getOriginalFilename());
				//flash.addFlashAttribute("error", "Foto " + foto.getOriginalFilename() + " NO ha subido porque ha habido algún problema.");
				flash.addFlashAttribute("error", mensaje);
			}
		}
		
		String mensaje = 
				(cliente.getId() != null)?
						messageSource.getMessage("text.cliente.flash.editar.success",null, null,  locale) : 
							messageSource.getMessage("text.cliente.flash.crear.success",null, null,  locale);
		
		this.clienteService.save(cliente);
		status.setComplete();//Eliminamos la sesión
		flash.addFlashAttribute("success", mensaje);
		return "redirect:/listar";
	}
	
	//@Secured("ROLE_ADMIN")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value="/form/{id}", method=RequestMethod.GET)
	public String modificar(@PathVariable(value="id") Long id,  Map<String, Object> model, RedirectAttributes flash, Locale locale) {
		
		Cliente cliente = null;
		//Long id = Long.parseLong(idString);
		
		if (id > 0) {
			cliente = clienteService.findOne(id);
			if (cliente == null) {
				//flash.addFlashAttribute("error", "El ID de cliente no éxiste en BBDD");
				flash.addFlashAttribute("error", messageSource.getMessage("text.cliente.flash.db.error", null, locale));
				return "redirect:/listar";
			}
		}else {
			//flash.addFlashAttribute("error", "El ID no puede ser cero!");
			flash.addFlashAttribute("error", messageSource.getMessage("text.cliente.flash.id.error", null, locale));
			return "redirect:/listar";
		}
		
		model.put("cliente", cliente);
		model.put("titulo", messageSource.getMessage("text.cliente.form.titulo.editar",null, null,  locale) + " " + id);
		
		return "form";
	}
	
	
	@Secured("ROLE_ADMIN")
	@RequestMapping(value="/eliminar/{id}", method=RequestMethod.GET)
	public String eliminar(@PathVariable(value="id") Long id,  Map<String, Object> model, RedirectAttributes flash, Locale locale) {
		
		if (id > 0) {
			//Necesitamos el cliente para eliminar la foto
			Cliente cliente = this.clienteService.findOne(id);
			
			clienteService.delete(id);
			flash.addFlashAttribute("success", messageSource.getMessage("text.cliente.flash.eliminar.success",null, null,  locale));
		
			try {
				if(this.uploadFileService.delete(cliente.getFoto())) {
					//flash.addFlashAttribute("info", "Foto " + cliente.getFoto() + " eliminada con éxito");
					String mensaje = String.format(messageSource.getMessage("text.login.success", null, locale), cliente.getFoto());
					flash.addFlashAttribute("info", mensaje);
				}
			} catch (IOException e) {
				//e.printStackTrace();
				//flash.addFlashAttribute("error", "Foto " + cliente.getFoto() + " NO ha sido eliminada porque no existe.");
				String mensaje = String.format(messageSource.getMessage("text.login.success", null, locale), cliente.getFoto());
				flash.addFlashAttribute("error", mensaje);
			}
		}
		
		return "redirect:/listar";
	}
	
	@PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
	@GetMapping(value="/ver/{id}")
	public String ver(@PathVariable(value="id") Long id, Map<String, Object> model, RedirectAttributes flash, Locale locale) {
		
		//Cliente cliente = clienteService.findOne(id);
		Cliente cliente = clienteService.fetchClienteByIdWithFacturas(id);
		
		if(cliente == null) {
			//flash.addFlashAttribute("error", "El cliente no existe en el sistema");
			flash.addFlashAttribute("error", messageSource.getMessage("text.cliente.flash.db.error", null, null,  locale));
			return "redirect:/listar";	
		}
		
		model.put("cliente", cliente);
		model.put("titulo", messageSource.getMessage("text.cliente.detalle.titulo",null, null,  locale) + " " + cliente.getNombre());
		

		return "ver";
	}
	
	@Secured({"ROLE_USER", "ROLE_ADMIN"})
	@GetMapping(value="/uploads/{filename:.+}")
	public ResponseEntity<Resource> verFoto(@PathVariable String filename, RedirectAttributes flash, Locale locale){
 
		try {
			
			return ResponseEntity.ok().
					header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename +  "\"").
					body(this.uploadFileService.load(filename));
		}catch(Exception e) {
			//e.printStackTrace();
			//flash.addFlashAttribute("error", "Foto " + filename + " NO se ha podido cargar.");
			String params[] = {filename};
			flash.addFlashAttribute("error", messageSource.getMessage("text.cliente.flash.foto.ver.deny", params, null,  locale));
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
				logger.info("******Hola usuario " + auth.getName() + " tu role es: " + authority.getAuthority());
				return true;
			}
		}
		
		return false;*/
		
		return authorities.contains(new SimpleGrantedAuthority(role));
	}
}
