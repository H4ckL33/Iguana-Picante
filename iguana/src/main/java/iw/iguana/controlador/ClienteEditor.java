package iw.iguana.controlador;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.vaadin.data.Binder;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.event.ShortcutAction;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.AbstractOrderedLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import iw.iguana.dao.ClienteRepository;
import iw.iguana.dao.EmpleadoRepository;
import iw.iguana.dao.VentaRepository;
import iw.iguana.modelo.Cliente;
import iw.iguana.modelo.Empleado;
import iw.iguana.modelo.Restaurante;
import iw.iguana.seguridad.SecurityUtils;

@SpringComponent
@UIScope
public class ClienteEditor extends VerticalLayout {

	private static final long serialVersionUID = 1L;

	private final ClienteRepository repository;
	
	private final VentaRepository repoVenta;

	private Cliente cliente;

	/* Fields to edit properties in Customer entity */
	TextField nombre = new TextField("Nombre");
	TextField apellidos = new TextField("Apellidos");
	TextField direccion = new TextField("Direccion");
	TextField telefono = new TextField("Telefono");

	/* Action buttons */
	Button save = new Button("Guardar", VaadinIcons.DOWNLOAD);
	Button delete = new Button("Borrar", VaadinIcons.TRASH);
	Button cancel = new Button("Cancelar");
	CssLayout actions = new CssLayout(save, cancel, delete);

	public Binder<Cliente> binder = new Binder<>(Cliente.class);

	@Autowired
	public ClienteEditor(ClienteRepository repository, VentaRepository repoVenta) {
		this.repository = repository;
		this.repoVenta = repoVenta;
		
		Label label = new Label("Formulario de Cliente");
		label.addStyleName(ValoTheme.LABEL_H2);
		label.addStyleName(ValoTheme.LABEL_COLORED);
		
		HorizontalLayout fila1 = new HorizontalLayout(nombre, apellidos, direccion, telefono);
		
		Label espacio = new Label("<br>",ContentMode.HTML);
		
		addComponents(label, fila1, actions, espacio);

		binder.forField(nombre).asRequired("Campo nombre es requerido")
		  .withValidator(new StringLengthValidator("El nombre debe tener al menos dos caracteres",2, 20))
		  .bind(Cliente::getNombre, Cliente::setNombre);
		  
		binder.forField(apellidos)
		  .bind(Cliente::getApellidos, Cliente::setApellidos);
		  
		binder.forField(direccion)
		  .bind(Cliente::getDireccion, Cliente::setDireccion);
		  
		binder.forField(telefono).asRequired("Telefono requerido")
		  .withValidator(new StringLengthValidator("El numero debe tener al menos 9 caracteres",9, 20))
		  .bind(Cliente::getTelefono,Cliente::setTelefono);

		// Configure and style components
		setSpacing(true);
		actions.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
		save.setStyleName(ValoTheme.BUTTON_PRIMARY);
		save.setClickShortcut(ShortcutAction.KeyCode.ENTER);

		// wire action buttons to save, delete and reset
		save.addClickListener(e -> {
			if(binder.validate().isOk()){
				cliente.setRestaurante((Restaurante) VaadinSession.getCurrent().getAttribute("restaurante"));
				repository.save(cliente);
			}
		});
		delete.addClickListener(e -> {
			if(repoVenta.findByCliente(cliente).isEmpty())
				repository.delete(cliente);
			else
				Notification.show("Este producto esta asociado a una venta, no se puede borrar",Notification.Type.ERROR_MESSAGE);
		});
		cancel.addClickListener(e -> setVisible(false));
		setVisible(false);
		
		delete.setEnabled(SecurityUtils.hasRole("ROLE_MANAGER") || SecurityUtils.hasRole("ROLE_ADMIN"));
	}

	public interface ChangeHandler {

		void onChange();
	}

	public final void editarCliente(Cliente c) {
		if (c == null) {
			setVisible(false);
			return;
		}
		final boolean persisted = c.getId() != null;
		if (persisted) {
			cliente = repository.findOne(c.getId());
		}
		else {
			cliente = c;
		}
		delete.setVisible(persisted);

		binder.setBean(cliente);

		setVisible(true);

		// A hack to ensure the whole form is visible
		save.focus();
		// Select all text in firstName field automatically
		//nombre.selectAll();
	}

	public void setChangeHandler(ChangeHandler h) {
		// ChangeHandler is notified when either save or delete
		// is clicked
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
	
	public static void listarClientes(String filterText, String filterText2, Grid<Cliente> grid, 
			ClienteRepository repo) {
		if (StringUtils.isEmpty(filterText) && StringUtils.isEmpty(filterText2)) {
			if(VaadinSession.getCurrent().getAttribute("restaurante") != null)
 				grid.setItems(repo.findByRestaurante(VaadinSession.getCurrent().getAttribute("restaurante")));
 			else
 				Notification.show("Debes seleccionar un restaurante", "Botón admin en la vista principal",Notification.Type.WARNING_MESSAGE);
		}
		else if (StringUtils.isEmpty(filterText2)) {
			grid.setItems(repo.findByNombreStartsWithIgnoreCase(filterText));
		}
		else {
			grid.setItems(repo.findByApellidosStartsWithIgnoreCase(filterText2));
		}
	}
}
