package com.bolsadeideas.springboot.app.view.pdf;

import java.awt.Color;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.view.document.AbstractPdfView;

import com.bolsadeideas.springboot.app.models.entity.Factura;
import com.bolsadeideas.springboot.app.models.entity.ItemFactura;
import com.lowagie.text.Document;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfCell;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

@Component("factura/ver")
public class FacturaPdfView extends AbstractPdfView{
	
	@Autowired
	private MessageSource messageSource;
	
	@Autowired
    private LocaleResolver localeResolver;

	@Override
	protected void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		Locale locale = localeResolver.resolveLocale(request);
		
		Factura factura = (Factura) model.get("factura");
		
		MessageSourceAccessor message = getMessageSourceAccessor();
		
		PdfPCell cell = null;
		String mensaje = null;
		
		PdfPTable tablaClien = new PdfPTable(1);
		tablaClien.setSpacingAfter(20); 
		cell = new PdfPCell(new Phrase(messageSource.getMessage("text.factura.ver.datos.cliente", null, locale)));
		cell.setBackgroundColor(new Color(184,218,255));
		cell.setPadding(8f);
		tablaClien.addCell(cell);
		tablaClien.addCell(factura.getCliente().getNombre() + " " + factura.getCliente().getApellido());
		tablaClien.addCell(factura.getCliente().getEmail());
		
		PdfPTable tablaFac = new PdfPTable(1);
		tablaFac.setSpacingAfter(20);
		cell = new PdfPCell(new Phrase(messageSource.getMessage("text.factura.ver.datos.factura", null, locale)));
		cell.setBackgroundColor(new Color(95,230,255));
		cell.setPadding(8f);
		tablaFac.addCell(cell);
		mensaje = String.format(messageSource.getMessage("text.login.success", null, locale), factura.getId());
		tablaFac.addCell(message.getMessage("text.cliente.factura.folio") + ": " + factura.getId());
		tablaFac.addCell(message.getMessage("text.cliente.factura.descripcion") + ": " + factura.getDescripcion());
		tablaFac.addCell(message.getMessage("text.cliente.factura.fecha") + ": " + factura.getCreateAt());
		
		PdfPTable tablaItems = new PdfPTable(4);
		tablaItems.setWidths(new float[] {2.5f, 1, 1, 1});
		tablaItems.addCell(message.getMessage("text.factura.form.item.nombre"));
		tablaItems.addCell(message.getMessage("text.factura.form.item.precio"));
		tablaItems.addCell(message.getMessage("text.factura.form.item.cantidad"));
		tablaItems.addCell(message.getMessage("text.factura.form.item.total"));
		
		for(ItemFactura item: factura.getItems()) {
			tablaItems.addCell(item.getProducto().getNombre());
			tablaItems.addCell(item.getProducto().getPrecio().toString());
			cell = new PdfPCell(new Phrase(item.getCantidad().toString()));
			cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
			tablaItems.addCell(cell);
			tablaItems.addCell(item.calcularImporte().toString());
		}
		
		cell = new PdfPCell(new Phrase(message.getMessage("text.factura.form.total") + ": "));
		cell.setColspan(3);
		cell.setHorizontalAlignment(PdfCell.ALIGN_RIGHT);
		tablaItems.addCell(cell);
		tablaItems.addCell(factura.getTotal().toString());
		
		document.add(tablaClien);
		document.add(tablaFac);
		document.add(tablaItems);
		
	}

}
