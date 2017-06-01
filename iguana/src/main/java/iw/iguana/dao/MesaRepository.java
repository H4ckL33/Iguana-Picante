package iw.iguana.dao;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import iw.iguana.modelo.Mesa;
import iw.iguana.modelo.Restaurante;

public interface MesaRepository extends JpaRepository<Mesa, Long> {
	
	List<Mesa> findByNumeroStartsWithIgnoreCase(String numero);
	
	List<Mesa> findByZonaStartsWithIgnoreCase(String zona);

	List<Mesa> findByRestauranteOrRestauranteIsNull(Object attribute);

	List<Mesa> findByRestaurante(Object attribute);

	boolean findByNumeroAndZona(String numero, String zona);

	List<Mesa> findByRestaurante(Restaurante restaurante);

	Mesa findByNumeroAndZonaAndRestaurante(String numero, String zona, Restaurante restaurante);
	
	@Query("SELECT m FROM Mesa m ORDER BY m.zona")
	List<Mesa> findAllOrderByZona();
}
