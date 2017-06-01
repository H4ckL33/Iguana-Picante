package iw.iguana.controlador;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.vaadin.data.Binder;
import com.vaadin.data.converter.StringToFloatConverter;
import com.vaadin.data.converter.StringToIntegerConverter;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.event.ShortcutAction;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.VaadinSession;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.TwinColSelect;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import iw.iguana.dao.FamiliaProductoRepository;
import iw.iguana.dao.ProductoCompuestoRepository;
import iw.iguana.dao.ProductoRepository;
import iw.iguana.dao.ProductoSimpleRepository;
import iw.iguana.dao.RestauranteRepository;
import iw.iguana.modelo.FamiliaProducto;
import iw.iguana.modelo.Producto;
import iw.iguana.modelo.ProductoCompuesto;
import iw.iguana.modelo.ProductoSimple;
import iw.iguana.modelo.Restaurante;
import iw.iguana.seguridad.SecurityUtils;

@SpringComponent
@ViewScope
public class RestauranteEditor extends VerticalLayout {

	private static final long serialVersionUID = 1L;

	private final RestauranteRepository repository;

	private Restaurante restaurante;

	/* Fields to edit properties in Customer entity */
	TextField ciudad = new TextField("Ciudad");
	TextField direccion = new TextField("Dirección");

	/* Action buttons */
	Button save = new Button("Guardar", VaadinIcons.DOWNLOAD);
	Button delete = new Button("Borrar", VaadinIcons.TRASH);
	Button cancel = new Button("Cancelar");
	CssLayout actions = new CssLayout(save, cancel, delete);

	public Binder<Restaurante> binder = new Binder<>(Restaurante.class);
	
	@Autowired
	public RestauranteEditor(RestauranteRepository repository) {
		this.repository = repository;
		
		Label label = new Label("Formulario de Restaurante");
		label.addStyleName(ValoTheme.LABEL_H2);
		label.addStyleName(ValoTheme.LABEL_COLORED);
		
		//Binders
		binder.forField(ciudad).asRequired("Campo ciudad es requerido")
			.withValidator(new StringLengthValidator("El nombre debe tener al menos dos caracteres",2, 20))
			.bind(Restaurante::getCiudad, Restaurante::setCiudad);
				
		binder.forField(direccion).asRequired("Campo direccion es requerido")
		.withValidator(new StringLengthValidator("La direccion debe tener al menos 4 caracteres",4, 30))
		.bind(Restaurante::getDireccion, Restaurante::setDireccion);
		
		direccion.setWidth(300, Unit.PIXELS);
		
		HorizontalLayout fila = new HorizontalLayout(ciudad, direccion);
		
		Label espacio = new Label("<br>",ContentMode.HTML);
		
		addComponents(label, fila, actions, espacio);

		// bind using naming convention
		//binder.bindInstanceFields(this);

		// Configure and style components
		setSpacing(true);
		actions.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
		save.setStyleName(ValoTheme.BUTTON_PRIMARY);
		save.setClickShortcut(ShortcutAction.KeyCode.ENTER);

		// wire action buttons to save, delete and reset
		save.addClickListener(e -> {
			if(binder.validate().isOk()){
				repository.save(restaurante);
			}else{
				Notification.show("Formulario malformado");
			}});
		delete.addClickListener(e -> repository.delete(restaurante));
		cancel.addClickListener(e -> setVisible(false));
		setVisible(false);
		
		delete.setEnabled(SecurityUtils.hasRole("ROLE_ADMIN"));
	}

	public interface ChangeHandler {

		void onChange();
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
		
	public static void listarRestaurante(String filterText, String tipoFiltro, Grid<Restaurante> grid,
			RestauranteRepository repo) 
		{ 		
	 		if (StringUtils.isEmpty(filterText) && StringUtils.isEmpty(tipoFiltro)) {
				grid.setItems(repo.findAll());
			}
			else switch(tipoFiltro){
	 		case "ciudad":
	 			grid.setItems(repo.findByCiudadStartsWithIgnoreCase(filterText));
	 			break;
	 		case "direccion":
	 			grid.setItems(repo.findByDireccionStartsWithIgnoreCase(filterText));
	 			break;
	 		}
		}
	
	public final void editarRestaurante(Restaurante c) {
		if (c == null) {
			setVisible(false);
			return;
		}
		final boolean persisted = c.getId() != 0;
		if (persisted) {
			// Find fresh entity for editing
			restaurante = repository.findOne(c.getId());
		}
		else {
			restaurante = c;
		}
		delete.setVisible(persisted);

		// Bind customer properties to similarly named fields
		// Could also use annotation or "manual binding" or programmatically
		// moving values from fields to entities before saving
		binder.setBean(restaurante);

		setVisible(true);

		// A hack to ensure the whole form is visible
		save.focus();
		// Select all text in firstName field automatically
		//nombre.selectAll();
	}
}

