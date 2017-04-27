package iw.iguana.modelo;

import java.util.List;

public class ProductoSimple extends Producto {

	public ProductoSimple(FamiliaProducto familiaProd, String nombre, String desc, double precio, int stock, float iva,
			String rutaImagen, List<Venta> ventas) {
		
		super(familiaProd, nombre, desc, precio, stock, iva, rutaImagen, ventas);
		
	}

}
