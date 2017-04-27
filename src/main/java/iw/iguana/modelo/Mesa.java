package iw.iguana.modelo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Mesa {

	@Id
	@GeneratedValue
	private long id;
	private Zona zona;

	public Mesa(Zona zona) {
		this.zona = zona;
	}

	public long getId() {
		return id;
	}

	public Zona getZona() {
		return zona;
	}

	public void setZona(Zona zona) {
		this.zona = zona;
	}

	@Override
	public String toString() {
		return "Mesa [id=" + id + ", zona=" + zona + "]";
	}

}
