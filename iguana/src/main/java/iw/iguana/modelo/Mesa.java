package iw.iguana.modelo;

import java.util.Arrays;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class Mesa {

	public static final List<String> Zonas = Arrays.asList("Salon", "Terraza", "Barra");
	
	@Id
	@GeneratedValue
	private Long id;
	@Column(length=25,nullable=false,unique=false)
	private String numero;
	private String zona;
	@OneToOne//(fetch=FetchType.LAZY,optional=true)
	private Restaurante restaurante;
	
	public Mesa() {}

	public Mesa(String num, String zona) {
		this.zona = zona;
		this.numero = num;
	}

	public Long getId() {
		return id;
	}
	
	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getZona() {
		return zona;
	}

	public void setZona(String zona) {
		this.zona = zona;
	}
	
	public Restaurante getRestaurante() {
		return restaurante;
	}

	public void setRestaurante(Restaurante restaurante) {
		this.restaurante = restaurante;
	}

	@Override
	public String toString() {
		return "Mesa nº " + numero + ", " + zona;
	}

}
