package iw.iguana.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import iw.iguana.modelo.Restaurante;

import java.util.List;

public interface RestauranteRepository extends JpaRepository<Restaurante, Long> {

	List<Restaurante> findByCiudadStartsWithIgnoreCase(String ciudad);
	
	List<Restaurante> findByDireccionStartsWithIgnoreCase(String direccion);
}
