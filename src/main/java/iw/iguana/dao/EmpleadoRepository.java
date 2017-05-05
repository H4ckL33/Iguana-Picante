package iw.iguana.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import iw.iguana.modelo.Empleado;

import java.util.List;

public interface EmpleadoRepository extends JpaRepository<Empleado, Long> {
	List<Empleado> findByNombreStartsWithIgnoreCase(String nombre);
	
	List<Empleado> findByApellidosStartsWithIgnoreCase(String apellidos);
	
	List<Empleado> findByDniStartsWithIgnoreCase(String dni);
}
