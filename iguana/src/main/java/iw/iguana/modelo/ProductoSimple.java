package iw.iguana.modelo;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.DiscriminatorType;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue("simple")
public class ProductoSimple extends Producto {
	
	public ProductoSimple(){
		super();
	}
	
	public ProductoSimple(String nombre, String descripcion, float precio, 
			int stock, float iva, FamiliaProducto familia){
		
		super(nombre, descripcion, precio, stock, iva, familia);
	}
}
