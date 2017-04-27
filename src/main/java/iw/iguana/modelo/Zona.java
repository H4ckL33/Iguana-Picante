package iw.iguana.modelo;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Zona {

	@Id
	@GeneratedValue
	private long id;
	private String nombre;
	
	@OneToMany
	private List<Mesa> mesas;

	public Zona(String nombre, List<Mesa> mesas) {
		this.nombre = nombre;
		this.mesas = new LinkedList<Mesa>(mesas);
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

	public List<Mesa> getMesas() {
		return mesas;
	}

	public void setMesas(List<Mesa> mesas) {
		this.mesas = mesas;
	}

	@Override
	public String toString() {
		return "Zona [id=" + id + ", nombre=" + nombre + ", mesas=" + mesas + "]";
	}

}
