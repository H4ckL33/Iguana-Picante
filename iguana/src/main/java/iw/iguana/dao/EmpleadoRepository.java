package iw.iguana.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import iw.iguana.modelo.Empleado;

import java.util.Collection;
import java.util.List;

public interface EmpleadoRepository extends JpaRepository<Empleado, Long> {

	List<Empleado> findByNombreStartsWithIgnoreCase(String nombre);
	
	List<Empleado> findByApellidosStartsWithIgnoreCase(String apellidos);
	
	List<Empleado> findByDniStartsWithIgnoreCase(String dni);
	
	Empleado findByNombre(String nombre);

	Empleado findByUsername(String username);

	List<Empleado> findByUsernameStartsWithIgnoreCase(String username);

	List<Empleado> findByRestauranteOrRestauranteIsNull(Object attribute); //Muestra los empleados del local y el que no tenga 
																		   //restaurante asignado
	List<Empleado> findByRestaurante(Object attribute);

	List<Empleado> findByRestauranteIsNull();

	List<Empleado> findByRestauranteAndDniStartsWithIgnoreCase(Object attribute, String filterText);

	List<Empleado> findByRestauranteAndNombreStartsWithIgnoreCase(Object attribute, String filterText);

	List<Empleado> findByRestauranteAndApellidosStartsWithIgnoreCase(Object attribute, String filterText);
}
