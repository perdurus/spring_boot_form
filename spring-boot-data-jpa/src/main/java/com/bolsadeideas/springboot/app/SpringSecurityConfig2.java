package com.bolsadeideas.springboot.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/*
 *Clase de configuración para el login
 */
public class SpringSecurityConfig2 extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		//Asignamos las rutas que van a ser publicas y las que van a ser privadas
		http.authorizeRequests()
			.antMatchers("/", "/css/**", "/images/**", "/listar").permitAll() //Páginas publicas
			.antMatchers("/ver/**").hasAnyRole("USER")	//Páginas privadas que dependiendo del rol que tenga el usuario podrá acceder o no
			.antMatchers("/uploads/**").hasAnyRole("USER") //Páginas privadas que dependiendo del rol que tenga el usuario podrá acceder o no
			.antMatchers("/form/**").hasAnyRole("ADMIN") //Páginas privadas que dependiendo del rol que tenga el usuario podrá acceder o no
			.antMatchers("/eliminar/**").hasAnyRole("ADMIN") //Páginas privadas que dependiendo del rol que tenga el usuario podrá acceder o no
			.antMatchers("/factura/**").hasAnyRole("ADMIN") //Páginas privadas que dependiendo del rol que tenga el usuario podrá acceder o no
			.anyRequest().authenticated()
			.and() //Con estos and permitimos que si se pulsa en una página pública un botón que nos envía a una privada, nos envíe a la página de login
				.formLogin().loginPage("/login") //Con el método loginPage especificamos la ruta de nuestra página de login personalizada en vez de la de por defecto
				.permitAll()
			.and()
			.logout().permitAll();
	}

	/*
	 * Bean para poder usar el encriptador de contraseñas
	 */
	@Bean
	public static BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	/*
	 * Método para crear usuarios y asignarles su password y roles
	 */
	@Autowired
	public void configurerGlobal(AuthenticationManagerBuilder builder) throws Exception {
		
		PasswordEncoder encoder = passwordEncoder();
		UserBuilder users = User.builder().passwordEncoder(encoder::encode); //Simplificación de User.builder().passwordEncoder(password -> encoder.encode(password));
		
		builder.inMemoryAuthentication()
			.withUser(users.username("admin").password("12345").roles("ADMIN", "USER"))
			.withUser(users.username("julio").password("12345").roles( "USER"));
	}
}