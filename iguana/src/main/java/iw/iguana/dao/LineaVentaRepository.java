package iw.iguana.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import iw.iguana.modelo.FamiliaProducto;
import iw.iguana.modelo.LineaVenta;
import iw.iguana.modelo.ProductoCompuesto;
import iw.iguana.modelo.ProductoSimple;

import java.util.Collection;
import java.util.List;

public interface LineaVentaRepository extends JpaRepository<LineaVenta, Long> {

	List<LineaVenta> findByProducto(Long id);

	List<FamiliaProducto> findByProducto(ProductoSimple producto);

	List<FamiliaProducto> findByProducto(ProductoCompuesto producto);
}