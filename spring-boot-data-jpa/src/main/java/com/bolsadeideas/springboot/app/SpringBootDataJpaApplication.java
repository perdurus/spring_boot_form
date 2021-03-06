package com.bolsadeideas.springboot.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.bolsadeideas.springboot.app.services.IUploadFileService;

@SpringBootApplication
public class SpringBootDataJpaApplication implements CommandLineRunner{

	@Autowired
	IUploadFileService fileService;
	
	@Autowired
	private BCryptPasswordEncoder passwordEnconder;
	
	public static void main(String[] args) {
		SpringApplication.run(SpringBootDataJpaApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		
		this.fileService.deleteAll();
		this.fileService.init();
		
		String password = "12345";
		
		for (int i=0; i<2 ; i++) {
			String bcryptPassword = passwordEnconder.encode(password);
			System.out.println(bcryptPassword);
		}
		
	}

}
