package iw.iguana.modelo;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class Empleado {

	@Id
	@GeneratedValue
	private long id;
	private int dni;
	private String password;
	private String nombre;
	private String apellidos;
	private String direccion;
	private int telefono;
	private Date fechaContratacion;
	
	@OneToOne
	private Rol rol;

	public Empleado(int dni, String password, String nombre, String apellidos, String direccion, int telefono,
			Date fechaContratacion, Rol rol) {
		this.dni = dni;
		this.password = password;
		this.nombre = nombre;
		this.apellidos = apellidos;
		this.direccion = direccion;
		this.telefono = telefono;
		this.fechaContratacion = fechaContratacion;
		this.rol = rol;
	}

	public long getId() {
		return id;
	}

	public int getDni() {
		return dni;
	}

	public void setDni(int dni) {
		this.dni = dni;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellidos() {
		return apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
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

	public Date getFechaContratacion() {
		return fechaContratacion;
	}

	public void setFechaContratacion(Date fechaContratacion) {
		this.fechaContratacion = fechaContratacion;
	}

	public Rol getRol() {
		return rol;
	}

	public void setRol(Rol rol) {
		this.rol = rol;
	}

	@Override
	public String toString() {
		return "Empleado [id=" + id + ", dni=" + dni + ", password=" + password + ", nombre=" + nombre + ", apellidos="
				+ apellidos + ", direccion=" + direccion + ", telefono=" + telefono + ", fechaContratacion="
				+ fechaContratacion + ", rol=" + rol + "]";
	}
	
}
