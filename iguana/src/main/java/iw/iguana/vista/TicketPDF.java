package iw.iguana.vista;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import iw.iguana.modelo.LineaVenta;
import iw.iguana.modelo.Producto;
import iw.iguana.modelo.ProductoCompuesto;
import iw.iguana.modelo.Venta;

public class TicketPDF {
	
	private Document documento = new Document();
	private FileOutputStream ficheroPdf;
	
	public TicketPDF(List<LineaVenta> lineas) {
		try {
			
			this.documento = new Document(PageSize.B7);
			this.ficheroPdf = new FileOutputStream("cocina/" + String.valueOf(new Date().getTime()) + ".pdf");
			
			crearTicketCocina(lineas);
			
		} catch (FileNotFoundException | DocumentException e) {
			e.printStackTrace();
		}
	}
	
	public TicketPDF(Venta venta) {
		try {
			
			this.documento = new Document(PageSize.B6);
			this.ficheroPdf = new FileOutputStream("tickets/" + String.valueOf(new Date().getTime()) + ".pdf");
			
			crearTicket(venta);
			
		} catch (DocumentException | IOException e) {
			e.printStackTrace();
		}
	}
	
	private void crearTicketCocina(List<LineaVenta> lineas) throws DocumentException {
		PdfWriter.getInstance(this.documento, this.ficheroPdf).setInitialLeading(20);
		
		this.documento.open();
		
		PdfPTable tabla = new PdfPTable(2);
		
		tabla.addCell(crearCelda("Unidades", Rectangle.NO_BORDER, PdfPCell.ALIGN_LEFT, 1));
		tabla.addCell(crearCelda("Producto", Rectangle.NO_BORDER, PdfPCell.ALIGN_LEFT, 1));
		
		for(LineaVenta lv : lineas) {
			tabla.addCell(Integer.toString(lv.getUnidades()));
			tabla.addCell(lv.getNombreProd());
			if(lv.getProducto() instanceof ProductoCompuesto) {
				String ingredientes = "";
				ProductoCompuesto prod = (ProductoCompuesto) lv.getProducto();
				Iterator<Producto> it = prod.getProductos().iterator();
				while(it.hasNext())
					ingredientes.concat(it.next().getNombre()+", ");
				tabla.addCell(crearCelda("Ingredientes: " + ingredientes, Rectangle.BOX, PdfPCell.ALIGN_RIGHT, 2));
			}
		}
		
		this.documento.add(tabla);
		
		this.documento.close();
	}
	
	private void crearTicket(Venta venta) throws DocumentException, MalformedURLException, IOException {
		PdfWriter.getInstance(this.documento, this.ficheroPdf).setInitialLeading(20);
		
		this.documento.open();
		
		Image img = Image.getInstance("img/iguana.png");
		img.setAlignment(Image.ALIGN_CENTER);
		img.scalePercent(40);
		this.documento.add(img);
		
		Paragraph lineaVacia = new Paragraph();
		lineaVacia.add("\n");
		
		Paragraph p = new Paragraph();
		p.setAlignment(Paragraph.ALIGN_CENTER);
		p.add("LA IGUANA PICANTE");
		this.documento.add(p);
		
		this.documento.add(lineaVacia);
		
		PdfPTable tabla = new PdfPTable(5);
		tabla.setWidthPercentage(100);
		
		tabla.addCell(crearCelda("FACTURA SIMPLIFICADA", Rectangle.BOTTOM, PdfPCell.ALIGN_CENTER, 5));
		tabla.addCell(crearCelda("Venta: " + venta.getId(), Rectangle.BOTTOM, PdfPCell.ALIGN_CENTER, 5));
		tabla.addCell(crearCelda(venta.getFecha(), Rectangle.NO_BORDER, PdfPCell.ALIGN_RIGHT, 5));
		if(venta.getMesa() != null)
			tabla.addCell(crearCelda(String.format("Mesa: %03d", Integer.valueOf(venta.getMesa().getNumero())), Rectangle.BOTTOM, PdfPCell.ALIGN_LEFT, 5));
		tabla.addCell(crearCelda("Uds.", Rectangle.BOTTOM, PdfPCell.ALIGN_LEFT, 1));
		tabla.addCell(crearCelda("Producto", Rectangle.BOTTOM, PdfPCell.ALIGN_LEFT, 3));
		tabla.addCell(crearCelda("Importe", Rectangle.BOTTOM, PdfPCell.ALIGN_RIGHT, 1));
		
		for(LineaVenta lv : venta.getProductos()) {
			tabla.addCell(crearCelda(Integer.toString(lv.getUnidades()), Rectangle.NO_BORDER, PdfPCell.ALIGN_LEFT, 1));
			tabla.addCell(crearCelda(lv.getNombreProd(), Rectangle.NO_BORDER, PdfPCell.ALIGN_LEFT, 3));
			tabla.addCell(crearCelda(String.format("%.2f €", lv.getSubtotal()), Rectangle.NO_BORDER, PdfPCell.ALIGN_RIGHT, 1));
		}
		
		tabla.addCell(crearCelda(String.format("Total: %.2f €", venta.getPrecio()), Rectangle.TOP, PdfPCell.ALIGN_RIGHT, 5));
		
		this.documento.add(tabla);
		
		this.documento.close();
		
	}
	
	private PdfPCell crearCelda(String texto, int border, int hAlignment, int colspan) {
		PdfPCell celda = new PdfPCell(new Phrase(texto));
		celda.setBorder(border);
		celda.setHorizontalAlignment(hAlignment);
		celda.setColspan(colspan);
		return celda;
	}
	
}
