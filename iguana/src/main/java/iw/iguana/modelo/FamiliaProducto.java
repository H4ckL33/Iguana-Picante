package iw.iguana.modelo;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Column;
@Entity

public class FamiliaProducto implements Serializable{
	
	@Id
	@GeneratedValue
	private Long id;
	//@Column(name="NOMBRE", length = 25, nullable = false)
	private String nombre;
	
	public FamiliaProducto(){}
	public FamiliaProducto(String nombre){this.nombre=nombre;}
	
	public Long getId() {
		return id;
	}
	
	@Override
	public String toString() {
		return String.format("%s", nombre);
	}

	public String getNombre() {
		return nombre;
	}
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
}
