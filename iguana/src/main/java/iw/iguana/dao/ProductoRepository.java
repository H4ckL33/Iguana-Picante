package iw.iguana.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import iw.iguana.modelo.FamiliaProducto;
import iw.iguana.modelo.Producto;

import java.util.List;

public interface ProductoRepository extends JpaRepository<Producto, Long> {

	List<Producto> findByNombreStartsWithIgnoreCase(String nombre);
	
	List<Producto> findByPrecio(float precio);
	
	List<Producto> findByStock(int stock);

	List<Producto> findByFamilia(FamiliaProducto familia);
}
