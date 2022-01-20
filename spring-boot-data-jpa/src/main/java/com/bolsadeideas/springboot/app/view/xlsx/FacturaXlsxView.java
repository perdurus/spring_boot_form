package com.bolsadeideas.springboot.app.view.xlsx;

import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.view.document.AbstractXlsxView;

import com.bolsadeideas.springboot.app.models.entity.Factura;
import com.bolsadeideas.springboot.app.models.entity.ItemFactura;

@Component("factura/ver.xlsx")
public class FacturaXlsxView extends AbstractXlsxView{

	@Autowired
	private MessageSource messageSource;
	
	@Autowired
    private LocaleResolver localeResolver;
	
	@Override
	protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		Locale locale = localeResolver.resolveLocale(request);
		MessageSourceAccessor message = getMessageSourceAccessor();
		
		Factura factura = (Factura) model.get("factura");
		
		response.setHeader("Content-Disposition", "attachment; filename=\"factura_view.xlsx\"");
		Sheet sheet = workbook.createSheet(message.getMessage("text.factura.form.titulo.excel"));//Factura Spring
		
		Row row = sheet.createRow(0);
		Cell cell = row.createCell(0);
		cell.setCellValue(messageSource.getMessage("text.factura.ver.datos.cliente", null, locale));//Datos del cliente
		
		row = sheet.createRow(1);
		cell = row.createCell(0);
		cell.setCellValue(factura.getCliente().getNombre() + " " + factura.getCliente().getApellido());
		
		row = sheet.createRow(2);
		cell = row.createCell(0);
		cell.setCellValue(factura.getCliente().getEmail());
		
		
		//Encadenado
		sheet.createRow(4).createCell(0).setCellValue(messageSource.getMessage("text.factura.ver.datos.factura", null, locale));//Datos de factura
		sheet.createRow(5).createCell(0).setCellValue(message.getMessage("text.cliente.factura.folio") + ": " + factura.getId());
		sheet.createRow(6).createCell(0).setCellValue(message.getMessage("text.cliente.factura.descripcion") + ": " + factura.getDescripcion());
		sheet.createRow(7).createCell(0).setCellValue(message.getMessage("text.cliente.factura.fecha") + ": " + factura.getCreateAt());
		
		
		//Detalle factura 
		CellStyle headerStyle = workbook.createCellStyle();
		headerStyle.setBorderBottom(BorderStyle.MEDIUM);
		headerStyle.setBorderRight(BorderStyle.MEDIUM);
		headerStyle.setBorderTop(BorderStyle.MEDIUM);
		headerStyle.setBorderLeft(BorderStyle.MEDIUM);
		headerStyle.setFillForegroundColor(IndexedColors.GOLD.index);
		headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		
		CellStyle bodyStyle = workbook.createCellStyle();
		bodyStyle.setBorderBottom(BorderStyle.THIN);
		bodyStyle.setBorderRight(BorderStyle.THIN);
		bodyStyle.setBorderTop(BorderStyle.THIN);
		bodyStyle.setBorderLeft(BorderStyle.THIN);
		
		Row header = sheet.createRow(9);
		header.createCell(0).setCellValue(message.getMessage("text.factura.form.item.nombre"));//Producto
		header.createCell(1).setCellValue(message.getMessage("text.factura.form.item.precio"));//Precio
		header.createCell(2).setCellValue(message.getMessage("text.factura.form.item.cantidad"));//Cantidad
		header.createCell(3).setCellValue(message.getMessage("text.factura.form.item.total"));//Total
		
		header.getCell(0).setCellStyle(headerStyle);
		header.getCell(1).setCellStyle(headerStyle);
		header.getCell(2).setCellStyle(headerStyle);
		header.getCell(3).setCellStyle(headerStyle);
		
		int rownum = 10;
		for(ItemFactura item: factura.getItems()) {
			Row fila = sheet.createRow(rownum++);
			
			cell = fila.createCell(0); 
			cell.setCellValue(item.getProducto().getNombre());
			cell.setCellStyle(bodyStyle);
			
			cell = fila.createCell(1);
			cell.setCellValue(item.getProducto().getPrecio());
			cell.setCellStyle(bodyStyle);
			
			cell = fila.createCell(2);
			cell.setCellValue(item.getCantidad());
			cell.setCellStyle(bodyStyle);
			
			cell = fila.createCell(3);
			cell.setCellValue(item.calcularImporte());
			cell.setCellStyle(bodyStyle);
		}
		
		Row filatotal = sheet.createRow(rownum);
		cell = filatotal.createCell(2);
		cell.setCellValue(message.getMessage("text.factura.form.total"));
		cell.setCellStyle(bodyStyle);
		
		cell = filatotal.createCell(3);
		cell.setCellValue(factura.getTotal());
		cell.setCellStyle(bodyStyle);
		
	}

}
