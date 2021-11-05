package com.canal.sergio.springboot.form.app.interceptors;

import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component("tiempoTranscurridoInterceptor")
public class TiempoTranscurridoInterceptor implements HandlerInterceptor{

	private static final Logger logger = LoggerFactory.getLogger(TiempoTranscurridoInterceptor.class);
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		if (request.getMethod().equalsIgnoreCase("post")) {
			return true;
		}
		
		if (handler instanceof HandlerMethod) {
			logger.info("Es un método del controlador: " +  ((HandlerMethod) handler).getMethod().getName());	
		}
		
		logger.info("TiempoTranscurridoInterceptor: preHandle entrando...");
		logger.info("Handler: " +  handler.toString());
		long tiempoIni = System.currentTimeMillis();
		request.setAttribute("tiempoInicio", tiempoIni);
		
		Random random = new Random();
		Integer demora = random.nextInt(500);
		
		Thread.sleep(demora);
		
		//response.sendRedirect(request.getContextPath() + "/login");
		//return false;
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		
		if (request.getMethod().equalsIgnoreCase("post")) {
			return;
		}
		
		long tiempoIni = (Long)request.getAttribute("tiempoInicio");
		long tiempoFin = System.currentTimeMillis();
		long tiempoTranscurrido = tiempoFin - tiempoIni;
		
		if (handler instanceof HandlerMethod && modelAndView!= null) {
			modelAndView.addObject("tiempoTranscurrido", tiempoTranscurrido);
		}
		logger.info("Tiempo Transcurrido en cargar: " + tiempoTranscurrido);
		logger.info("TiempoTranscurridoInterceptor: postHandle saliendo...");
	}

	
}
