package iw.iguana.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import iw.iguana.modelo.FamiliaProducto;
import iw.iguana.modelo.ProductoCompuesto;
import iw.iguana.modelo.ProductoSimple;

import java.util.List;

public interface ProductoCompuestoRepository extends JpaRepository<ProductoCompuesto, Long> {

	List<ProductoCompuesto> findByNombreStartsWithIgnoreCase(String nombre);
	
	List<ProductoCompuesto> findByPrecio(float precio);
	
	List<ProductoCompuesto> findByStock(int stock);

	List<ProductoCompuesto> findByFamilia(FamiliaProducto familia);
}
