package iw.iguana.modelo;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.OneToMany;

public class ProductoCompuesto extends Producto {
	
	@OneToMany
	private List<Producto> productos;

	public ProductoCompuesto(FamiliaProducto familiaProd, String nombre, String desc, double precio, int stock,
			float iva, String rutaImagen, List<Venta> ventas) {
		
		super(familiaProd, nombre, desc, precio, stock, iva, rutaImagen, ventas);
		
		productos = new LinkedList<Producto>();
	}
	
	public void addProducto (Producto producto) {
		productos.add(producto);
	}

}
