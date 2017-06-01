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
public class Restaurante {
	
	@Id
	@GeneratedValue
	private long id;
	private String ciudad;
	private String direccion;
	/*@OneToMany//(cascade={CascadeType.PERSIST, CascadeType.REMOVE})
	private List<Mesa> mesas;
	@OneToMany( targetEntity=Empleado.class)
	private List<Empleado> empleados;
	@OneToMany//(cascade={CascadeType.PERSIST, CascadeType.REMOVE})
	private List<Venta> ventas;
	@OneToMany//(cascade={CascadeType.PERSIST, CascadeType.REMOVE})
	private List<Cliente> clientes;*/
	
	public Restaurante() {}

	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}

	public String getCiudad() {
		return ciudad;
	}

	public void setCiudad(String ciudad) {
		this.ciudad = ciudad;
	}
	
	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	/*public List<Mesa> getMesas() {
		return mesas;
	}

	public void setMesas(List<Mesa> mesas) {
		this.mesas = mesas;
	}

	public List<Empleado> getEmpleados() {
		return empleados;
	}
	
	public void addEmpleado(Empleado empleado) {
		this.empleados.add(empleado);
	}

	public void setEmpleados(List<Empleado> empleados) {
		this.empleados = empleados;
	}
	
	public List<Venta> getVentas() {
		return ventas;
	}

	public void setVentas(List<Venta> ventas) {
		this.ventas = ventas;
	}*/
	
	@Override
	public String toString() {
		return ciudad + " en " + direccion;
	}
}
