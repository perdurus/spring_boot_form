package com.bolsadeideas.springboot.app.services;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
@Repository("uploadFileService")
public class UploadFileService implements IUploadFileService {

	private Logger log = LoggerFactory.getLogger(getClass());
	
	private static String UPLOAD_FOLDER = "uploads";
	
	@Override
	public Resource load(String filename) throws MalformedURLException {
		
		Path pathFoto = getPath(filename);
		log.info("pathFoto: " + pathFoto);
		
		Resource recurso = null;
		
		recurso = new UrlResource(pathFoto.toUri());
		
		if(!recurso.exists() && !recurso.isReadable()) {
			throw new RuntimeException("Error: no se puede cargar la imagen: " + pathFoto.toString());
		}	
		
		return recurso;
	}

	@Override
	public String copy(MultipartFile file) throws IOException {
			
		String uniqueFilename = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
		Path absolutePath = getPath(uniqueFilename);
		
		log.info("absolutePath: " + absolutePath);
		
		Files.copy(file.getInputStream(), absolutePath);
			
		return uniqueFilename;
	}

	@Override
	public boolean delete(String filename) throws IOException {
		return Files.deleteIfExists(getPath(filename));
	}

	
	/**/
	public Path getPath(String filename) {
		return Paths.get(UPLOAD_FOLDER).resolve(filename).toAbsolutePath();
	}

	@Override
	public void deleteAll(){
		
		FileSystemUtils.deleteRecursively(Paths.get(UPLOAD_FOLDER).toFile());
	}

	@Override
	public void init() throws IOException {
		Files.createDirectories(Paths.get(UPLOAD_FOLDER));
	}
}
