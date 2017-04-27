package iw.iguana.modelo;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Caja {

	@Id
	@GeneratedValue
	private long id;
	private Date fecha;
	private double importeRecaudado;
	private double importeContabilizado;
	
	@OneToMany
	private List<Venta> ventas;
	
	public Caja(Date fecha, double importeRecaudado, double importeContabilizado, List<Venta> ventas) {
		this.fecha = fecha;
		this.importeRecaudado = importeRecaudado;
		this.importeContabilizado = importeContabilizado;
		this.ventas = new LinkedList<Venta>(ventas);
	}

	public long getId() {
		return id;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public double getImporteRecaudado() {
		return importeRecaudado;
	}

	public void setImporteRecaudado(double importeRecaudado) {
		this.importeRecaudado = importeRecaudado;
	}

	public double getImporteContabilizado() {
		return importeContabilizado;
	}

	public void setImporteContabilizado(double importeContabilizado) {
		this.importeContabilizado = importeContabilizado;
	}

	public List<Venta> getVentas() {
		return ventas;
	}

	public void setVentas(List<Venta> ventas) {
		this.ventas = ventas;
	}

	@Override
	public String toString() {
		return "Caja [id=" + id + ", fecha=" + fecha + ", importeRecaudado=" + importeRecaudado
				+ ", importeContabilizado=" + importeContabilizado + ", ventas=" + ventas + "]";
	}
	
}
