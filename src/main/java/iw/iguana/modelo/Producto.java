package iw.iguana.modelo;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

@Entity
public abstract class Producto {

	@Id
	@GeneratedValue
	private long id;
	private FamiliaProducto familiaProd;
	private String nombre;
	private String desc;
	private double precio;
	private int stock;
	private float iva;
	private String rutaImagen;
	
	@ManyToMany
	@JoinTable
	private List<Venta> ventas;

	public Producto(FamiliaProducto familiaProd, String nombre, String desc, double precio, int stock, float iva,
			String rutaImagen, List<Venta> ventas) {

		this.familiaProd = familiaProd;
		this.nombre = nombre;
		this.desc = desc;
		this.precio = precio;
		this.stock = stock;
		this.iva = iva;
		this.rutaImagen = rutaImagen;
		this.ventas = new LinkedList<Venta>(ventas);

	}

	public long getId() {
		return id;
	}

	public FamiliaProducto getFamiliaProd() {
		return familiaProd;
	}

	public void setFamiliaProd(FamiliaProducto familiaProd) {
		this.familiaProd = familiaProd;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public double getPrecio() {
		return precio;
	}

	public void setPrecio(double precio) {
		this.precio = precio;
	}

	public int getStock() {
		return stock;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}

	public float getIva() {
		return iva;
	}

	public void setIva(float iva) {
		this.iva = iva;
	}

	public String getRutaImagen() {
		return rutaImagen;
	}

	public void setRutaImagen(String rutaImagen) {
		this.rutaImagen = rutaImagen;
	}

	public List<Venta> getVentas() {
		return ventas;
	}

	public void setVentas(List<Venta> ventas) {
		this.ventas = ventas;
	}

	@Override
	public String toString() {
		return "Producto [id=" + id + ", familiaProd=" + familiaProd + ", nombre=" + nombre + ", desc=" + desc
				+ ", precio=" + precio + ", stock=" + stock + ", iva=" + iva + ", rutaImagen=" + rutaImagen + "]";
	}

}
