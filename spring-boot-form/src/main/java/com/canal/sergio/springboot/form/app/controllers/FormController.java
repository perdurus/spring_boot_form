package com.canal.sergio.springboot.form.app.controllers;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import com.canal.sergio.springboot.form.app.editors.NombreMayusculaEditor;
import com.canal.sergio.springboot.form.app.editors.PaisPropertyEditor;
import com.canal.sergio.springboot.form.app.editors.RolesEditor;
import com.canal.sergio.springboot.form.app.models.domain.Pais;
import com.canal.sergio.springboot.form.app.models.domain.Rol;
import com.canal.sergio.springboot.form.app.models.domain.Usuario;
import com.canal.sergio.springboot.form.app.services.PaisService;
import com.canal.sergio.springboot.form.app.services.RoleService;
import com.canal.sergio.springboot.form.app.validation.UsuarioValidador;

@Controller
@SessionAttributes("usuario")
public class FormController {
	
	@Autowired
	private UsuarioValidador validador;
	
	@Autowired
	private PaisService paisService;
	
	@Autowired
	private PaisPropertyEditor paisEditor;
	
	@Autowired
	private RoleService roleService;
	
	@Autowired
	private RolesEditor rolesEditor;
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.addValidators(validador);
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		sdf.setLenient(false);//Indica si es flexible con el formato, False es que no lo es
		//binder.registerCustomEditor(Date.class, new CustomDateEditor(sdf, false));
		binder.registerCustomEditor(Date.class, "fechaNacimiento", new CustomDateEditor(sdf, false));
		binder.registerCustomEditor(String.class, "nombre", new NombreMayusculaEditor());
		binder.registerCustomEditor(String.class, "apellido", new NombreMayusculaEditor());
		binder.registerCustomEditor(Pais.class, "pais", paisEditor);
		binder.registerCustomEditor(Rol.class, "roles", rolesEditor);
	}
	
	
	
	@GetMapping({"/form", "", "/"})
	public String form(Model model) {
		Usuario usuario = new Usuario();
		usuario.setNombre("EjemploNombre");
		usuario.setApellido("EjemploApellido");
		usuario.setIdentificador("12.456.789-L");
		
		usuario.setUsername("scanal");
		usuario.setPassword("123456789");
		usuario.setCuenta(Integer.parseInt("123"));
		usuario.setEmail("perdurus@yahoo.es");
		usuario.setFechaNacimiento(new Date());
		usuario.setPais(new Pais(1, "ES", "España"));
		usuario.setRoles(Arrays.asList(new Rol(2, "Usuario", "ROLE_USER")));
		
		usuario.setHabilitar(true);
		//usuario.setValorSecreto(" Un valor secreto");
		
		model.addAttribute("titulo", "Trabajando con Formularios: Formulario usuario");
		model.addAttribute("usuario", usuario);
		
		//Lista de paises
		
		return "form";
	}
	
	
	// @ModelAttribute("usuario") en este caso no hace falta porque el parametro se llama igual. Pero si 
	// queremos cambiar el nombre dentro del metodo hay que indicarle el nombre dado en el template
	//public String procesarFormulario(@Valid @ModelAttribute("usuario") Usuario usuario, BindingResult result, Model model) {
	@PostMapping("/form")
	public String procesarFormulario(@Valid Usuario usuario, BindingResult result, Model model) {
		
		//validador.validate(usuario, result);
		
		model.addAttribute("titulo", "Trabajando con Formularios: Resultado del formulario");
		
		//Manejamos si es válido el formulario
		if (result.hasErrors()) {
//			Map<String, String> errores = new ConcurrentHashMap<String, String>();
//			
//			result.getFieldErrors().forEach(err->{
//				errores.put(err.getField(), "Error en el campo '".concat(err.getField()).concat("': ").concat(err.getDefaultMessage()));
//			});
//			
//			model.addAttribute("error", errores);
			model.addAttribute("titulo", "Trabajando con Formularios: Resultado del formulario ERROR");
			
			return "form";
		}
		
		//model.addAttribute("usuario", usuario);
		
		return "redirect:/ver";
	}
	
	@GetMapping("/ver")
	public String ver(@SessionAttribute(name="usuario", required=false) Usuario usuario, Model model, SessionStatus status) {
		
		if (usuario == null) {
			return "redirect:/form";
			
		}
		
		model.addAttribute("titulo", "Trabajando con Formularios: Resultado del formulario VER");
		
		status.setComplete();
		return "resultado";
		
	}
	
	//@ModelAttribute("paises")
	public List<String> paises(){	
		return Arrays.asList("España", "Mexico", "Chile", "Colombia", "Venezuela", "Perú", "Panamá", "Argentina");
	}
	
	//@ModelAttribute("paisesMap")
	public Map<String, String> paisesMap(){	
		
		Map<String, String> paises = new HashMap<String, String>();
		
		paises.put("ES", "España");
		paises.put("MX", "Mexico");
		paises.put("CL", "Chile");
		paises.put("CO", "Colombia");
		paises.put("VE", "Venezuela");
		paises.put("PE", "Perú");
		paises.put("PA", "Panamá");
		paises.put("AR", "Argentina");
		
		return paises;
	}
	
	@ModelAttribute("listaPaises")
	public List<Pais> listaPaises(){	
		return paisService.listar();
	}
	
	
	
	//@ModelAttribute("rolesString")
	public List<String> rolesString(){
		List<String> roles = new ArrayList<String>();
		roles.add("ROLE_ADMIN");
		roles.add("ROLE_USER");
		roles.add("ROLE_MODERATOR");

		return roles;
	}
	
	
	@ModelAttribute("rolesMap")
	public Map<String, String> rolesMap(){	
		
		Map<String, String> roles = new HashMap<String, String>();
		
		roles.put("ROLE_ADMIN", "Administrador");
		roles.put("ROLE_USER", "Usuario");
		roles.put("ROLE_MODERATOR", "Moderador");
		
		return roles;
	}
		
	
	@ModelAttribute("roles")
	public List<Rol> roles(){	

		return this.roleService.listar();
	}
	
	@ModelAttribute("generos")
	public List<String> genero(){
		return Arrays.asList("Hombre", "Mujer");
	}
	
	
//	OTRA FORMA MAS MANUAL
//	@PostMapping("/form")
//	public String procesarFormulario(Model model, 
//			@RequestParam String username, 
//			@RequestParam String password,
//			@RequestParam String email) {
//		
//		model.addAttribute("titulo", "Trabajando con Formularios: Resultado del formulario");
//		
//		Usuario usuario = new Usuario();
//		
//		usuario.setUsername(username);
//		usuario.setPassword(password);
//		usuario.setEmail(email);
//		
//		model.addAttribute("usuario", usuario);
//		
//		return "resultado";
//	}


}
