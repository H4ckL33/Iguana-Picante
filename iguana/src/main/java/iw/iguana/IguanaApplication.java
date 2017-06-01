package iw.iguana;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import iw.iguana.seguridad.VaadinSessionSecurityContextHolderStrategy;
import iw.iguana.controlador.EmpleadoService;

import iw.iguana.dao.ClienteRepository;
import iw.iguana.dao.EmpleadoRepository;
import iw.iguana.dao.FamiliaProductoRepository;
import iw.iguana.dao.MesaRepository;
import iw.iguana.dao.ProductoRepository;
import iw.iguana.dao.ProductoSimpleRepository;
import iw.iguana.dao.RestauranteRepository;
import iw.iguana.modelo.Cliente;
import iw.iguana.modelo.Empleado;
import iw.iguana.modelo.FamiliaProducto;
import iw.iguana.modelo.ProductoSimple;
import iw.iguana.modelo.Restaurante;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class IguanaApplication {

	private static final Logger log = LoggerFactory.getLogger(IguanaApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(IguanaApplication.class);
	}

	@Bean
	public CommandLineRunner loadData(RestauranteRepository restaurante, ClienteRepository cliente, 
			ProductoSimpleRepository producto_s, MesaRepository mesa, FamiliaProductoRepository fam, EmpleadoService service) {
		return (args) -> {
			if (service.findAll().size() == 0) {
				//Restaurantes
				Restaurante res = new Restaurante();
				res.setCiudad("Puerto Real");
				res.setDireccion("C/ Real nº 14");
				restaurante.save(res);
				Restaurante res2 = new Restaurante();
				res2.setCiudad("Cádiz");
				res2.setDireccion("C/ Ancha nº 22");
				restaurante.save(res2);
				
				//Empleados
				Empleado root = new Empleado("25602004p", "Alberto", "Cabrera Becerra", 
						"Dirección", "", "Gerente", "root");
				root.setPassword("root");
				root.setTelefono("663704310");
				//root.setRestaurante(res);
				service.save(root);
				
				Empleado root2 = new Empleado("77889944q", "Miguel", "Farrujia Porras", 
						"Dirección", "", "Encargado", "farru");
				root2.setPassword("root");
				root2.setTelefono("666998877");
				root2.setRestaurante(res2);
				service.save(root2);
				
				Empleado camarero = new Empleado("22222222q", "Prueba", "Prueba", 
						"Dirección", "", "Camarero", "prueba");
				camarero.setPassword("p");
				camarero.setTelefono("777777777");
				camarero.setRestaurante(res2);
				service.save(camarero);
				

				// fetch all users
				log.info("Users found with findAll():");
				log.info("-------------------------------");
				for (Empleado user : service.findAll()) {
					log.info(user.toString());
				}
				log.info("");

				// fetch an individual user by ID
				Empleado user = service.findOne(1L);
				log.info("User found with findOne(1L):");
				log.info("--------------------------------");
				log.info(user.toString());
				log.info("");

				// fetch users by last name
				log.info("User found with findByLastNameStartsWithIgnoreCase('Bauer'):");
				log.info("--------------------------------------------");
				for (Empleado bauer : service.findByApellidosStartsWithIgnoreCase("Bauer")) {
					log.info(bauer.toString());
				}
				log.info("");
			}
		};
	}
	
	@Configuration
	@EnableGlobalMethodSecurity(securedEnabled = true)
	public static class SecurityConfiguration extends GlobalMethodSecurityConfiguration {

		@Autowired
		private UserDetailsService userDetailsService;

		@Bean
		public PasswordEncoder encoder() {
			return new BCryptPasswordEncoder(11);
		}

		@Bean
		public DaoAuthenticationProvider authenticationProvider() {
			DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
			authProvider.setUserDetailsService(userDetailsService);
			authProvider.setPasswordEncoder(encoder());
			return authProvider;
		}

		@Override
		protected void configure(AuthenticationManagerBuilder auth) throws Exception {

			auth.authenticationProvider(authenticationProvider());

			 auth
			 .inMemoryAuthentication()
			 .withUser("gerente").password("p").roles("ADMIN")
			 .and()
			 .withUser("encargado").password("p").roles("MANAGER")
			 .and()
			 .withUser("camarero").password("p").roles("USER");
		}

		@Bean
		public AuthenticationManager authenticationManagerBean() throws Exception {
			return authenticationManager();
		}

		static {
			// Use a custom SecurityContextHolderStrategy
			SecurityContextHolder.setStrategyName(VaadinSessionSecurityContextHolderStrategy.class.getName());
		}
	}
}
