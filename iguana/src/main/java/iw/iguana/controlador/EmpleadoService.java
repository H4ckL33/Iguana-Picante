package iw.iguana.controlador;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import iw.iguana.dao.EmpleadoRepository;
import iw.iguana.modelo.Empleado;

@Service
public class EmpleadoService implements UserDetailsService {

	@Autowired
	private EmpleadoRepository repo;

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	public Empleado loadUserByUsername(String username) throws UsernameNotFoundException {

		Empleado user = repo.findByUsername(username);
		if (user == null) {
			throw new UsernameNotFoundException(username);
		}
		return user;
	}

	public Empleado save(Empleado user) {
		user.setPassword(passwordEncoder.encode(user.getPassword() != null ? user.getPassword() : "default"));
		user.getAuthorities();
		return repo.save(user);
	}

	public List<Empleado> findByUsernameStartsWithIgnoreCase(String username) {
		return repo.findByUsernameStartsWithIgnoreCase(username);
	}

	public Empleado findOne(Long arg0) {
		return repo.findOne(arg0);
	}

	public void delete(Empleado arg0) {
		repo.delete(arg0);
	}

	public List<Empleado> findAll() {
		return repo.findAll();
	}

	public List<Empleado> findByApellidosStartsWithIgnoreCase(String apellidos) {
		return repo.findByApellidosStartsWithIgnoreCase(apellidos);
	}
}
