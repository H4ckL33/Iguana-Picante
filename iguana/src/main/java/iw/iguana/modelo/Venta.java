package iw.iguana.modelo;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Entity
public class Venta {
	
	@Id
	@GeneratedValue
	private Long id;
	private String fecha;
	private float precio;
	private boolean abierta;
	@OneToOne
	private Mesa mesa;
	@ManyToOne
	private Cliente cliente;
	/*@OneToOne
	private Empleado empleado;*/
	@OneToMany(cascade={CascadeType.PERSIST, CascadeType.REMOVE})
	@JoinTable
	private List<LineaVenta> lineasVenta;
	@OneToOne
	private Restaurante restaurante;

	public Venta(Mesa mesa, String fecha, float precio, List<LineaVenta> lineasVenta) {
		this.abierta = true;
		this.mesa = mesa;
		this.fecha = fecha;
		this.precio = precio;
		this.lineasVenta = new LinkedList<LineaVenta>(lineasVenta);
	}
	
	public Venta(Cliente cliente, String fecha, float precio, List<LineaVenta> lineasVenta) {
		this.abierta = true;
		this.cliente = cliente;
		this.fecha = fecha;
		this.precio = precio;
		this.lineasVenta = new LinkedList<LineaVenta>(lineasVenta);
	}

	public Venta(String fecha, float precio, List<LineaVenta> lineasVenta) {
		this.abierta = true;
		this.fecha = fecha;
		this.precio = precio;
		this.lineasVenta = new LinkedList<LineaVenta>(lineasVenta);
	}

	public Venta() {}
	
	public Long getId() {
		return id;
	}

	public Mesa getMesa() {
		return mesa;
	}

	public void setMesa(Mesa mesa) {
		this.mesa = mesa;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public float getPrecio() {
		return precio;
	}

	public void setPrecio(float precio) {
		this.precio = precio;
	}

	/*public Empleado getEmpleado() {
		return empleado;
	}

	public void setEmpleado(Empleado empleado) {
		this.empleado = empleado;
	}*/

	public List<LineaVenta> getProductos() {
		System.out.println(lineasVenta);
		return lineasVenta;
	}

	public void setProductos(List<LineaVenta> lineasVenta) {
		System.out.println(lineasVenta);
		this.lineasVenta = lineasVenta;
	}
	
	public Restaurante getRestaurante() {
		return restaurante;
	}

	public void setRestaurante(Restaurante restaurante) {
		this.restaurante = restaurante;
	}

	public boolean isAbierta() {
		return abierta;
	}

	public void setAbierta(boolean abierta) {
		this.abierta = abierta;
	}

	@Override
	public String toString() {
		return "Venta [id=" + id + ", mesa=" + mesa + ", fecha=" + fecha + ", precio=" + precio
				+ ", lineasVenta=" + lineasVenta + "]";
	}
}
