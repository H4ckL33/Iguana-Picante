package iw.iguana.modelo;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Entity
public class Caja {
	@Id
	@GeneratedValue
	@Column(nullable=false)
	private Long id;
	private String fechaApertura;
	private String fechaCierre;
	private float impRecaudado;
	private float impContabilizado;
	private boolean cajaCerrada;
	@OneToOne//(fetch=FetchType.LAZY,optional=true)
	private Restaurante restaurante;
	@OneToMany(cascade={CascadeType.MERGE, CascadeType.REMOVE})
	@JoinTable
	private List<Venta> ventas;
	
	public Caja(){}
	
	public String getFechaApertura() {
		return fechaApertura;
	}

	public void setFechaApertura(String fechaApertura) {
		this.fechaApertura = fechaApertura;
	}

	public String getFechaCierre() {
		return fechaCierre;
	}

	public void setFechaCierre(String fechaCierre) {
		this.fechaCierre = fechaCierre;
	}

	public float getImpRecaudado() {
		return impRecaudado;
	}

	public void setImpRecaudado(float impRecaudado) {
		this.impRecaudado = impRecaudado;
	}

	public float getImpContabilizado() {
		return impContabilizado;
	}

	public void setImpContabilizado(float impContabilizado) {
		this.impContabilizado = impContabilizado;
	}

	public boolean getCajaCerrada() {
		return cajaCerrada;
	}

	public void setCajaCerrada(boolean cajaCerrada) {
		this.cajaCerrada = cajaCerrada;
	}

	public List<Venta> getVentas() {
		return ventas;
	}

	public void setVentas(List<Venta> ventas) {
		this.ventas = ventas;
	}

	public Long getId() {
		return id;
	}
	
	public Restaurante getRestaurante() {
		return restaurante;
	}

	public void setRestaurante(Restaurante restaurante) {
		this.restaurante = restaurante;
	}
}