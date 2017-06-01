package iw.iguana.controlador;

import java.lang.Object;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.vaadin.data.Binder;
import com.vaadin.data.converter.*;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.event.ShortcutAction;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinSession;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

import ch.qos.logback.core.joran.util.StringToObjectConverter;
import iw.iguana.dao.EmpleadoRepository;
import iw.iguana.dao.RestauranteRepository;
import iw.iguana.modelo.Empleado;
import iw.iguana.modelo.Restaurante;
import iw.iguana.seguridad.SecurityUtils;

@SpringComponent
@UIScope
public class EmpleadoEditor extends VerticalLayout  {

	private static final long serialVersionUID = 1L;

	private final EmpleadoRepository repository;
	
	private final EmpleadoService service;

	private Empleado empleado;
	
	private RestauranteRepository repo;
	
	public boolean contraseña = false;
	
	/* Fields to edit properties in Customer entity */
	TextField dni = new TextField("DNI");
	TextField nombre = new TextField("Nombre");
	TextField apellidos = new TextField("Apellidos");
	TextField direccion = new TextField("Direccion");
	TextField telefono = new TextField("Telefono");
	PasswordField password = new PasswordField("Contraseña");
	TextField username = new TextField("Username");
	ComboBox<Restaurante> restaurante = new ComboBox<Restaurante>("Restaurante");
	//TextField fechaContratacion = new TextField("fechaContratacion");
	ComboBox<String> rol = new ComboBox<>("Selecciona una opción", Empleado.Roles);

	/* Action buttons */
	Button save = new Button("Guardar", VaadinIcons.DOWNLOAD);
	Button delete = new Button("Borrar", VaadinIcons.TRASH);
	Button cancel = new Button("Cancelar");
	CssLayout actions = new CssLayout(save, cancel, delete);
	
	Button changePass = new Button("Cambiar contraseña...", VaadinIcons.PASSWORD);
	Button changeRol = new Button("Cambiar rol...", VaadinIcons.USER_CHECK);

	Label info = new Label("Si el nuevo Rol es gerente, el empleado no formará parte de ningún restaurante.");
	
	HorizontalLayout passCenter = new HorizontalLayout();
	
	HorizontalLayout cambiarContraseña = new HorizontalLayout();
	
	HorizontalLayout layoutInfo = new HorizontalLayout();

	public Binder<Empleado> binder = new Binder<>(Empleado.class);

	@Autowired
	public EmpleadoEditor(EmpleadoRepository repository, EmpleadoService service, RestauranteRepository repo) {
		this.service = service;
		this.repository = repository;
		this.repo = repo;
		
		Label label = new Label("Formulario de Empleado");
		label.addStyleName(ValoTheme.LABEL_H2);
		label.addStyleName(ValoTheme.LABEL_COLORED);
		HorizontalLayout NombreCompleto = new HorizontalLayout(dni,nombre, apellidos,telefono);
		direccion.setWidth(385, Unit.PIXELS);
		Label espacio = new Label("<br>",ContentMode.HTML);
		
		cambiarContraseña.addComponent(changeRol);
		
		if(VaadinSession.getCurrent().getAttribute("restaurante") == null) {}
		
		passCenter.addComponent(username);
		
		restaurante.setItems(repo.findAll());
		restaurante.setWidth(300, Unit.PIXELS);
		passCenter.addComponent(restaurante); 
		passCenter.addComponent(rol);
		
		info.addStyleName(ValoTheme.LABEL_SMALL);
		info.addStyleName(ValoTheme.LABEL_COLORED);
		layoutInfo.addComponent(info);
		layoutInfo.setVisible(false);
		rol.setVisible(false);
		restaurante.setVisible(false);

		addComponents(label, NombreCompleto, direccion, passCenter, layoutInfo, cambiarContraseña, actions, espacio);

		binder.forField(dni).asRequired("El campo DNI es obligatorio")
		  .withValidator(new StringLengthValidator("El dni debe tener al menos nueve caracteres",9, 20))
		  .bind(Empleado::getDni, Empleado::setDni);
		
		binder.forField(nombre).asRequired("El campo nombre es obligatorio")
		  .bind(Empleado::getNombre, Empleado::setNombre);
		  
		binder.forField(apellidos).bind(Empleado::getApellidos, Empleado::setApellidos);
		  
		binder.forField(direccion).bind(Empleado::getDireccion, Empleado::setDireccion);
		
		binder.forField(username).asRequired("El campo username es obligatorio")
		  .withValidator(new StringLengthValidator("El username debe tener al menos 4 caracteres",4, 20))
		  .bind(Empleado::getUsername, Empleado::setUsername);
		  
		binder.forField(rol).asRequired("El campo rol es obligatorio")
		  .bind(Empleado::getRol, Empleado::setRol);
		  
		binder.forField(telefono).asRequired("El campo telefono es obligatorio")
		  .withValidator(new StringLengthValidator("El numero debe tener al menos 9 caracteres",9, 20))
		  .bind(Empleado::getTelefono, Empleado::setTelefono);

		// Configure and style components
		setSpacing(true);
		actions.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
		save.setStyleName(ValoTheme.BUTTON_PRIMARY);
		save.setClickShortcut(ShortcutAction.KeyCode.ENTER);
		
		//Quito el else ya que lo he puesto en el otro lado, añado otro if else donde si no modifico la contraseña llamo al save normal
		//que no cifra la contraseña y sino llamo al de service que si la cifra. De esta manera la contraseña se queda igual si no la modifico.
		save.addClickListener(e -> {
			if(empleado.getUsername().equals(VaadinSession.getCurrent().getAttribute("username")) && empleado.getRol() != "Encargado") 
			{
				Notification.show("No puedes editar tu propio usuario", "",Notification.Type.ERROR_MESSAGE);
			}
			else
			{
				if(binder.validate().isOk()){
					if(!empleado.getRol().equals("Gerente")) {
						if(VaadinSession.getCurrent().getAttribute("restaurante") != null) {
							if(Contraseña()){
								empleado.setRestaurante((Restaurante) VaadinSession.getCurrent().getAttribute("restaurante"));
								service.save(empleado);
							}else{
								empleado.setRestaurante((Restaurante) VaadinSession.getCurrent().getAttribute("restaurante"));
								repository.save(empleado);
							}
						}else{
							if(Contraseña()){
								service.save(empleado);
								Notification.show("Cierra Sesión para realizar el cambio de rol correctamente", "",
										Notification.Type.HUMANIZED_MESSAGE);
							}else{
								repository.save(empleado);
								Notification.show("Cierra Sesión para realizar el cambio de rol correctamente", "",
										Notification.Type.HUMANIZED_MESSAGE);
							}
						}
					}else{
						empleado.setRestaurante(null);
						if(Contraseña()){
							service.save(empleado);
						}else{
							repository.save(empleado);
						}
					}
						
				}
			}
			binder.forField(restaurante)
			.bind(Empleado::getRestaurante, Empleado::setRestaurante);
			changeRol.setVisible(true);
			layoutInfo.setVisible(false);
			rol.setVisible(false);
			restaurante.setVisible(false);
		});
		delete.addClickListener(e -> service.delete(empleado));
		cancel.addClickListener(e -> {
			setVisible(false);
			binder.forField(restaurante)
			.bind(Empleado::getRestaurante, Empleado::setRestaurante);
			changeRol.setVisible(true);
			layoutInfo.setVisible(false);
			rol.setVisible(false);
			restaurante.setVisible(false);
		});
		//Cuando se pulsa el boton se pone oculto y añade el componente password, ademas de su binder y notifica de que se va a modificar
		changePass.addClickListener(e -> {
			passCenter.addComponent(password); 
			changePass.setVisible(false); 
			binder.forField(password)
				//.withValidator(new StringLengthValidator("El password debe tener al menos 4 caracteres",4, 20))
				.bind(Empleado::getPassword, Empleado::setPassword);
			contraseña = true;
		});
		changeRol.addClickListener(e -> {
			
			binder.forField(restaurante).asRequired("Debes seleccionar un restaurante")
				.bind(Empleado::getRestaurante, Empleado::setRestaurante);
			changeRol.setVisible(false);
			layoutInfo.setVisible(true);
			rol.setVisible(true);
			restaurante.setVisible(true);
		});
		setVisible(false);
		
		delete.setEnabled(SecurityUtils.hasRole("ROLE_ADMIN"));
		changeRol.setEnabled(SecurityUtils.hasRole("ROLE_ADMIN"));
	}

	public interface ChangeHandler {

		void onChange();
	}
	
	public final void editarEmpleado(Empleado c) {
		if (c == null) {
			setVisible(false);
			return;
		}
		final boolean persisted = c.getId() != null;
		if (persisted) {
			// Find fresh entity for editing
			empleado = service.findOne(c.getId());
		}
		else {
			empleado = c;
		}
		delete.setVisible(persisted);

		// Bind user properties to similarly named fields
		// Could also use annotation or "manual binding" or programmatically
		// moving values from fields to entities before saving
		binder.setBean(empleado);

		setVisible(true);

		// A hack to ensure the whole form is visible
		save.focus();
		// Select all text in firstName field automatically
		//nombre.selectAll();
	}

	public void setChangeHandler(ChangeHandler h) {
		// De esta manera si a habido algún error no termina de guardar no esconde el formulario mostrando los errores
		save.addClickListener(e -> {
			if(!binder.validate().isOk()){
				Notification.show("Formulario malformado", "",Notification.Type.ERROR_MESSAGE);
			}else{
				h.onChange();
				Notification.show("Guardado correctamente", "",
 						Notification.Type.TRAY_NOTIFICATION);
			}
		});
		delete.addClickListener(e -> h.onChange());
	}

	public static void listarEmpleados(String filterText, String tipoFiltro, Grid<Empleado> grid, 
			EmpleadoRepository repo) 
		{ 		
	 		if (StringUtils.isEmpty(filterText) && StringUtils.isEmpty(tipoFiltro)) {
	 			if(VaadinSession.getCurrent().getAttribute("restaurante") != null)
	 				grid.setItems(repo.findByRestaurante(VaadinSession.getCurrent().getAttribute("restaurante")));
	 			else {
	 				grid.setItems(repo.findByUsername((String) VaadinSession.getCurrent().getAttribute("username")));
	 				Notification.show("Solo puedes modificar tu usario", "Si deseas realizar otra acción debes seleccionar un restaurante, "
	 						+ "para ello pulsa botón admin en la vista principal",
	 						Notification.Type.WARNING_MESSAGE);
	 			}
	 		}
			else switch(tipoFiltro){
	 		case "dni":
	 			grid.setItems(repo.findByRestauranteAndDniStartsWithIgnoreCase(VaadinSession.getCurrent().getAttribute("restaurante"),
	 					filterText));
	 			break;
	 		case "nombre":
	 			grid.setItems(repo.findByRestauranteAndNombreStartsWithIgnoreCase(VaadinSession.getCurrent().getAttribute("restaurante"), 
	 					filterText));
	 			break;
	 		case "apellidos":
	 			grid.setItems(repo.findByRestauranteAndApellidosStartsWithIgnoreCase(VaadinSession.getCurrent().getAttribute("restaurante"),
	 					filterText));
	 			break;
	 		}
		}
	
	//Si entras en edición pinchando en el grid se quita la contraseña si estuviese y se añade el boton
	public void editando() {
		passCenter.removeComponent(password);
		cambiarContraseña.addComponent(changePass);
		changePass.setVisible(true);
		
		binder.forField(password)
		//.withValidator(new StringLengthValidator("",0, 800))
		.bind(Empleado::getPassword, Empleado::setPassword);
		contraseña = false;
		//Notification.show("Editando");
		//password.setValue("default");
	}
	
	//Si estas creando de cero un empleado se lanza esta funcion que añade el campo password si no estuviera, avisa de que 
	//se va a modificar contraseña y borrar el boton de cambiar contraseña si estuviese, tb añade el binder.
	public void noEditando() {
		passCenter.addComponent(password);
		contraseña = true;
		//Notification.show("No editando");
		cambiarContraseña.removeComponent(changePass);
		
		binder.forField(password)
			//.withValidator(new StringLengthValidator("El password debe tener al menos 4 caracteres",4, 20))
			.bind(Empleado::getPassword, Empleado::setPassword);
	}

	//Este metodo sirve para comprobar si se va a actualizar o crear la contraseña
	public boolean Contraseña() {
		return contraseña;
	}
}
