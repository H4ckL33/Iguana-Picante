package iw.iguana.modelo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue("compuesto")
public class ProductoCompuesto extends Producto {
	@ManyToMany
	@JoinTable
	private Set<Producto> productos;
	
	public ProductoCompuesto(){
		super();
		//productos = new ArrayList<>();
	}
	
	public ProductoCompuesto(String nombre, String descripcion, float precio, 
			int stock, float iva, FamiliaProducto familia, Set<Producto> productos) {
		super(nombre, descripcion, precio,stock,iva,familia);
		this.productos=productos;
	}
	
	public Set<Producto> getProductos() {
		return productos;
	}

	public void setProductos(Set<Producto> productos) {
		this.productos = new HashSet<Producto>();
		this.productos.addAll(productos);
	}
	/*
	@Override
	public float getPrecio(){
		float precio = 0;
		for(Producto hijo:productos){
			precio += hijo.getPrecio();
		}
		return precio;
	}
	
	@Override
	public void setPrecio(float precio){
		throw new UnsupportedOperationException();
	}*/
	
	public void annadirProducto(Producto producto){
		productos.add(producto);
	}
	
	public void borrarProducto(Producto producto){
		productos.remove(producto);
	}
	
	public String toString() {
		return getNombre();
	}
}
