package iw.iguana.modelo;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class FamiliaProducto {

	@Id
	@GeneratedValue
	private long id;
	private String nombre;
	
	@OneToMany
	private List<Producto> productos;

	public FamiliaProducto(String nombre, List<Producto> productos) {
		this.nombre = nombre;
		this.productos = new LinkedList<Producto>(productos);
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public long getId() {
		return id;
	}

	@Override
	public String toString() {
		return "FamiliaProducto [id=" + id + ", nombre=" + nombre + "]";
	}

}
