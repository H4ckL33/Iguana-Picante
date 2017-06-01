package iw.iguana.dao;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import iw.iguana.modelo.Cliente;
import iw.iguana.modelo.Producto;
import iw.iguana.modelo.Venta;

public interface VentaRepository extends JpaRepository<Venta, Long> {

	List<Venta> findByFechaBetween(Date fecha1, Date fecha2);

	List<Venta> findByRestauranteOrRestauranteIsNull(Object attribute);

	List<Venta> findByRestaurante(Object attribute);
	
	@Query(value="SELECT * FROM VENTA WHERE FECHA >=?1"
			+" AND FECHA <= ?2", nativeQuery = true)
	List<Venta> CalcularFechas(String inicial, String fin);
	List<Venta> findByFecha(String fecha);
	List<Venta> findByPrecio(float precio);

	List<Venta> findByFechaContaining(String filterText);

	List<Venta> findByAbiertaTrue();

	List<Venta> findByCliente(Cliente cliente);

	//List<Venta> findByFechaStartsWithIgnoreCase(Date filterText);

	//Collection<Venta> findByFechaStartsWithIgnoreCase(String filterText);

	//Collection<Venta> findByPrecioStartsWithIgnoreCase(float filterText);

	//Collection<Venta> findByNumeroMesaStartsWithIgnoreCase(String filterText);

	//Collection<Venta> findByNombreClienteStartsWithIgnoreCase(String filterText);
}
