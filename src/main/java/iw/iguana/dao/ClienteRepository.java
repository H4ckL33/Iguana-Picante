package iw.iguana.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import iw.iguana.modelo.Cliente;

import java.util.List;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
	
	List<Cliente> findByNombreStartsWithIgnoreCase(String nombre);
	
	List<Cliente> findByTelefonoStartsWithIgnoreCase(String telefono);
}
