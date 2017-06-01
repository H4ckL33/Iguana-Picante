package iw.iguana.modelo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class LineaVenta {

	@Id
	@GeneratedValue
	private Long id;
	private int unidades;
	private boolean enCocina;
	@ManyToOne
	private Producto producto;

	public LineaVenta(int unidades, boolean enCocina, Producto producto) {

		this.unidades = unidades;
		this.enCocina = enCocina;
		this.producto = producto;

	}

	public LineaVenta() {

	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getUnidades() {
		return unidades;
	}

	public void setUnidades(int unidades) {
		this.unidades = unidades;
	}
	
	public boolean isEnCocina() {
		return enCocina;
	}

	public void setEnCocina(boolean enCocina) {
		this.enCocina = enCocina;
	}

	public Producto getProducto() {
		return producto;
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
	}
	
	public String getNombreProd() {
		return producto.getNombre();
	}

	public float getPrecio() {
		return producto.getPrecioFin();
	}

	public float getSubtotal() {
		return (unidades * getPrecio());
	}

	/*@Override
	public String toString() {
		return " " + unidades + " " + producto.getNombre();
	}*/

}
