package iw.iguana.modelo;

import java.beans.Transient;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

@Entity
public class Empleado implements UserDetails{
	
	public static final List<String> Roles = Arrays.asList("Gerente", "Encargado", "Camarero");
	
	@Id
	@GeneratedValue
	private Long id;
	private String dni;
	private String password;
	private String nombre;
	private String apellidos;
	private String username;
	private String direccion;
	private String telefono;
	private String rol;
	//private Date fechaContratacion;
	@OneToOne//(fetch=FetchType.LAZY,optional=true)
	private Restaurante restaurante;
	@OneToMany
	private List<Venta> ventas;
	
	
	public Empleado() {}
	
	public Empleado(String dni, String nombre, String apellidos, String direccion, 
			String telefono, String rol, String username) {
		this.dni = dni;
		this.nombre = nombre;
		this.apellidos = apellidos;
		this.direccion = direccion;
		this.telefono = telefono;
		this.rol = rol;
		this.username = username;
	}

	public Long getId() {
		return id;
	}

	public String getDni() {
		return dni;
	}

	public void setDni(String dni) {
		this.dni = dni;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
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

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
	
	public String getRol() {
		return rol;
	}

	public void setRol(String rol) {
		this.rol = rol;
	}
	
	public Restaurante getRestaurante() {
		return restaurante;
	}

	public void setRestaurante(Restaurante restaurante) {
		this.restaurante = restaurante;
	}

	/*public Date getFechaContratacion() {
		return fechaContratacion;
	}

	public void setFechaContratacion(Date fechaContratacion) {
		this.fechaContratacion = fechaContratacion;
	}*/

	@Override
	public String toString() {
		return "Empleado [id=" + id + ", dni=" + dni + ", password=" + password + ", nombre=" + nombre + ", apellidos="
				+ apellidos + ", direccion=" + direccion + ", telefono=" + telefono + ", fechaContratacion="
				/*+ fechaContratacion + ", rol=" + rol*/ + "]";
	}
	
	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<GrantedAuthority> list=new ArrayList<GrantedAuthority>();
		if (this.rol.equals("Gerente")) {
			list.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
		} else if (this.rol.equals("Encargado"))
			list.add(new SimpleGrantedAuthority("ROLE_MANAGER"));
		else
			list.add(new SimpleGrantedAuthority("ROLE_USER"));
		return list;
	}
	
}
