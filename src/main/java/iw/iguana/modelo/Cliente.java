package iw.iguana.modelo;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Cliente {

	@Id
	@GeneratedValue
	private long id;
	private String nombre;
	private String direccion;
	private int telefono;
	
	@OneToMany
	private List<Venta> ventas;

	public Cliente(String nombre, String direccion, int telefono, List<Venta> ventas) {
		this.nombre = nombre;
		this.direccion = direccion;
		this.telefono = telefono;
		this.ventas = new LinkedList<Venta>(ventas);
	}

	public long getId() {
		return id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public int getTelefono() {
		return telefono;
	}

	public void setTelefono(int telefono) {
		this.telefono = telefono;
	}

	public List<Venta> getVentas() {
		return ventas;
	}

	public void setVentas(List<Venta> ventas) {
		this.ventas = ventas;
	}

	@Override
	public String toString() {
		return "Cliente [id=" + id + ", ventas=" + ventas + ", nombre=" + nombre + ", direccion=" + direccion
				+ ", telefono=" + telefono + "]";
	}

}
