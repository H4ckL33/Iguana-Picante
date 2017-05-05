package iw.iguana.controlador;

import org.springframework.util.StringUtils;

import com.vaadin.ui.Grid;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.data.Binder;
import com.vaadin.event.ShortcutAction;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.AbstractOrderedLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import iw.iguana.dao.EmpleadoRepository;
import iw.iguana.modelo.Empleado;

public class EmpleadoEditor extends VerticalLayout {
	public static void listarEmpleados(String filterText, String tipoFiltro, Grid<Empleado> grid, EmpleadoRepository repo){ 		
	 		if (StringUtils.isEmpty(filterText) && StringUtils.isEmpty(tipoFiltro)) {
				grid.setItems(repo.findAll());
			}
			else switch(tipoFiltro){
	 		case "dni":
	 			grid.setItems(repo.findByDniStartsWithIgnoreCase(filterText));
	 			break;
	 		case "nombre":
	 			grid.setItems(repo.findByNombreStartsWithIgnoreCase(filterText));
	 			break;
	 		case "apellidos":
	 			grid.setItems(repo.findByApellidosStartsWithIgnoreCase(filterText));
	 			break;
	 		}
		}
	
	private static final long serialVersionUID = 1L;

	private final EmpleadoRepository repository;

	private Empleado empleado;

	/* Fields to edit properties in Customer entity */
	TextField dni = new TextField("DNI");
	TextField nombre = new TextField("Nombre");
	TextField apellidos = new TextField("Apellidos");
	TextField direccion = new TextField("Direccion");
	TextField telefono = new TextField("Telefono");
	TextField fechaContratacion = new TextField("fechaContratacion");

	/* Action buttons */
	Button save = new Button("Guardar", VaadinIcons.DOWNLOAD);
	Button delete = new Button("Borrar", VaadinIcons.TRASH);
	Button cancel = new Button("Cancelar");
	CssLayout actions = new CssLayout(save, cancel, delete);

	Binder<Empleado> binder = new Binder<>(Empleado.class);

	@Autowired
	public EmpleadoEditor(EmpleadoRepository repository) {
		this.repository = repository;

		addComponents(nombre, apellidos, direccion, telefono, actions);

		// bind using naming convention
		binder.bindInstanceFields(this);

		// Configure and style components
		setSpacing(true);
		actions.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
		save.setStyleName(ValoTheme.BUTTON_PRIMARY);
		save.setClickShortcut(ShortcutAction.KeyCode.ENTER);

		// wire action buttons to save, delete and reset
		save.addClickListener(e -> repository.save(empleado));
		delete.addClickListener(e -> repository.delete(empleado));
		cancel.addClickListener(e -> editarEmpleado(empleado));
		setVisible(false);
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
			empleado = repository.findOne(c.getId());
		}
		else {
			empleado = c;
		}
		cancel.setVisible(persisted);

		// Bind customer properties to similarly named fields
		// Could also use annotation or "manual binding" or programmatically
		// moving values from fields to entities before saving
		binder.setBean(empleado);

		setVisible(true);

		// A hack to ensure the whole form is visible
		save.focus();
		// Select all text in firstName field automatically
		nombre.selectAll();
	}

	public void setChangeHandler(ChangeHandler h) {
		// ChangeHandler is notified when either save or delete
		// is clicked
		save.addClickListener(e -> h.onChange());
		delete.addClickListener(e -> h.onChange());
	}

}
