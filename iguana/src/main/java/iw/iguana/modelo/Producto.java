package iw.iguana.modelo;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.InheritanceType;
import javax.persistence.Inheritance;
import javax.persistence.DiscriminatorType;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_producto", discriminatorType = DiscriminatorType.STRING)
public abstract class Producto {
	
	@Id
	@GeneratedValue
	private Long id;
	@Column(length=25,nullable=false)
	@NotNull(message="El campo no puede estar vacio")
	private String nombre;
	@Column(length=300,nullable=true)
	private String descripcion;
	@Column(length=10,nullable=false)
	@NotNull(message="El campo no puede estar vacio")
	private float precio ;
	@Column(length=10,nullable=true)
	private float precioFin;
	@Column(length=100,nullable=false)
	@NotNull(message="El campo no puede estar vacio")
	private int stock;
	@Column(length=25,nullable=true)
	private float iva;
	//private Imagen;
	
	 @ManyToOne(fetch=FetchType.LAZY,optional=false)
	 private FamiliaProducto familia;
	
	public Producto() {}

	public Producto(String nombre, String descripcion, float precio, 
			int stock, float iva, FamiliaProducto familia) {
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.precio = precio;
		this.stock = stock;
		this.iva = iva; 
		this.familia = familia;
	}
	
	public Long getId() {
		return id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	public float getPrecio(){
		return precio;
	}

	public void setPrecio(float precio) {
		this.precio = precio;
	}

	public int getStock() {
		return stock;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}

	public float getIva() {
		return iva;
	}

	public void setIva(float iva) {
		this.iva = iva;
	}
	
	public FamiliaProducto getFamilia() {
		return familia;
	}

	public void setFamilia(FamiliaProducto familia) {
		this.familia = familia;
	}
	
	public float getPrecioFin() {
		return precioFin;
	}

	public void setPrecioFin(float precioFin) {
		this.precioFin = precioFin;
	}

	public String toString() {
		return nombre ;
	}
}