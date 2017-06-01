package iw.iguana.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import iw.iguana.modelo.Caja;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public interface CajaRepository extends JpaRepository<Caja, Long> {

	List<Caja> findByFechaApertura(String fecha_apertura);
	
	List<Caja> findByImpRecaudado(float importe);

	Caja findByCajaCerrada(boolean b);

	List<Caja> findByFechaAperturaContainingAndRestaurante(String filterText, Object attribute);

	List<Caja> findByFechaCierreContaining(String filterText);

	List<Caja> findByRestaurante(Object attribute);
}
