package iw.iguana.modelo;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;

@Entity
public class Venta {
	
	@Id
	@GeneratedValue
	private long id;
	private Mesa mesa;
	private Cliente cliente;
	private Date fecha;
	// El atributo hora no hace falta pues ya se guarda en el atributo fecha
	private double precio;
	//FIXME Venta debería tener un atributo Caja??
	
	@OneToOne
	private Empleado empleado;
	
	@ManyToMany
	@JoinTable
	private List<Producto> productos;

	public Venta(Mesa mesa, Cliente cliente, Date fecha, double precio, Empleado empleado, List<Producto> productos) {
		this.mesa = mesa;
		this.cliente = cliente;
		this.fecha = fecha;
		this.precio = precio;
		this.empleado = empleado;
		this.productos = new LinkedList<Producto>(productos);
	}

	public long getId() {
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

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public double getPrecio() {
		return precio;
	}

	public void setPrecio(double precio) {
		this.precio = precio;
	}

	public Empleado getEmpleado() {
		return empleado;
	}

	public void setEmpleado(Empleado empleado) {
		this.empleado = empleado;
	}

	public List<Producto> getProductos() {
		return productos;
	}

	public void setProductos(List<Producto> productos) {
		this.productos = productos;
	}

	@Override
	public String toString() {
		return "Venta [id=" + id + ", mesa=" + mesa + ", cliente=" + cliente + ", fecha=" + fecha + ", precio=" + precio
				+ ", empleado=" + empleado + ", productos=" + productos + "]";
	}
	
}
