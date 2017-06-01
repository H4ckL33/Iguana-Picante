package iw.iguana.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import iw.iguana.modelo.FamiliaProducto;

import java.util.List;

public interface FamiliaProductoRepository extends JpaRepository<FamiliaProducto, Long> {

	List<FamiliaProducto> findByNombreStartsWithIgnoreCase(String nombre);
	
}