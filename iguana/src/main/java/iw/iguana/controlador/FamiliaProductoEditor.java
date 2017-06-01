package iw.iguana.controlador;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.vaadin.data.Binder;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.event.ShortcutAction;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.AbstractOrderedLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import iw.iguana.dao.FamiliaProductoRepository;
import iw.iguana.modelo.FamiliaProducto;
import iw.iguana.seguridad.SecurityUtils;

@SpringComponent
@UIScope
public class FamiliaProductoEditor extends VerticalLayout {

	private static final long serialVersionUID = 1L;

	private final FamiliaProductoRepository repository;

	private FamiliaProducto familia;

	/* Fields to edit properties in Customer entity */
	TextField nombre = new TextField("Nombre");
	
	List<String> ejemplos = IntStream.range(0, 6).mapToObj(i -> "Ejemplo " + i).collect(Collectors.toList());
	final ComboBox<String> rol = new ComboBox<>("Selecciona una opción", ejemplos);

	/* Action buttons */
	Button save = new Button("Guardar", VaadinIcons.DOWNLOAD);
	Button delete = new Button("Borrar", VaadinIcons.TRASH);
	Button cancel = new Button("Cancelar");
	CssLayout actions = new CssLayout(save, cancel, delete);

	public Binder<FamiliaProducto> binder = new Binder<>(FamiliaProducto.class);

	@Autowired
	public FamiliaProductoEditor(FamiliaProductoRepository repository) {
		this.repository = repository;
		
		Label label = new Label("Formulario de Familia de Producto");
		label.addStyleName(ValoTheme.LABEL_H2);
		label.addStyleName(ValoTheme.LABEL_COLORED);
		
		Label espacio = new Label("<br>",ContentMode.HTML);

		addComponents(label, nombre, actions, espacio);
		
		binder.forField(nombre).asRequired("El campo nombre no puede estar vacio")
			.withValidator(new StringLengthValidator("El nombre debe tener al menos dos caracteres", 2, 20))
			.bind(FamiliaProducto::getNombre, FamiliaProducto::setNombre);

		// bind using naming convention
		//binder.bindInstanceFields(this);

		// Configure and style components
		setSpacing(true);
		actions.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
		save.setStyleName(ValoTheme.BUTTON_PRIMARY);
		save.setClickShortcut(ShortcutAction.KeyCode.ENTER);

		// wire action buttons to save, delete and reset
		save.addClickListener(e -> {
			if(binder.validate().isOk())
				repository.save(familia);
		});
		delete.addClickListener(e -> repository.delete(familia));
		cancel.addClickListener(e -> setVisible(false));
		setVisible(false);
	}

	public interface ChangeHandler {

		void onChange();
	}

	public final void editarFamilia(FamiliaProducto c) {
		if (c == null) {
			setVisible(false);
			return;
		}
		final boolean persisted = c.getId() != null;
		if (persisted) {
			// Find fresh entity for editing
			familia = repository.findOne(c.getId());
		}
		else {
			familia = c;
		}
		cancel.setVisible(persisted);

		// Bind customer properties to similarly named fields
		// Could also use annotation or "manual binding" or programmatically
		// moving values from fields to entities before saving
		binder.setBean(familia);

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
			}
		});
		delete.addClickListener(e -> h.onChange());
	}

	public static void listarFamilia(String filterText, String tipoFiltro, Grid<FamiliaProducto> grid, 
			FamiliaProductoRepository repo) 
		{ 		
	 		if (StringUtils.isEmpty(filterText) && StringUtils.isEmpty(tipoFiltro)) {
				grid.setItems(repo.findAll());
			}
			else switch(tipoFiltro) {
	 		
	 		case "nombre":
	 			grid.setItems(repo.findByNombreStartsWithIgnoreCase(filterText));
	 			break;
	 		}
		}
}
