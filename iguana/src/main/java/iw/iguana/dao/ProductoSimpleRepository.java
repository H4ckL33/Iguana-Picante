package iw.iguana.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import iw.iguana.modelo.FamiliaProducto;
import iw.iguana.modelo.ProductoSimple;

import java.util.List;

public interface ProductoSimpleRepository extends JpaRepository<ProductoSimple, Long> {

	List<ProductoSimple> findByNombreStartsWithIgnoreCase(String nombre);
	
	List<ProductoSimple> findByPrecio(float precio);
	
	List<ProductoSimple> findByStock(int stock);

	List<ProductoSimple> findByFamilia(FamiliaProducto familia);
}
