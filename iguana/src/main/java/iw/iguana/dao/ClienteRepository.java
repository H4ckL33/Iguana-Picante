package iw.iguana.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import iw.iguana.modelo.Cliente;

import java.util.Collection;
import java.util.List;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

	List<Cliente> findByNombreStartsWithIgnoreCase(String nombre);
	
	List<Cliente> findByApellidosStartsWithIgnoreCase(String apellidos);

	List<Cliente> findByRestauranteOrRestauranteIsNull(Object attribute);

	List<Cliente> findByRestaurante(Object attribute);
}
