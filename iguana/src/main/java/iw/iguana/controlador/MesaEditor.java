package iw.iguana.controlador;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.vaadin.data.Binder;
import com.vaadin.data.converter.StringToFloatConverter;
import com.vaadin.data.converter.StringToIntegerConverter;
import com.vaadin.data.converter.StringToLongConverter;
import com.vaadin.event.ShortcutAction;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.VaadinSession;
import com.vaadin.server.Page;
import com.vaadin.server.Sizeable.Unit;
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
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import iw.iguana.dao.MesaRepository;
import iw.iguana.modelo.Mesa;
import iw.iguana.modelo.Producto;
import iw.iguana.modelo.Restaurante;
import iw.iguana.seguridad.SecurityUtils;

@SpringComponent
@UIScope
public class MesaEditor extends VerticalLayout {

	private static final long serialVersionUID = 1L;

	private final MesaRepository repository;
	
	private Mesa mesa;
	
	/* Fields to edit properties in Customer entity */
	TextField numero = new TextField("Identificación");
	ComboBox<String> zona = new ComboBox<>("Selecciona una zona", Mesa.Zonas);
	TextField restaurante;

	/* Action buttons */
	Button save = new Button("Guardar", VaadinIcons.DOWNLOAD);
	Button delete = new Button("Borrar", VaadinIcons.TRASH);
	Button cancel = new Button("Cancelar");
	CssLayout actions = new CssLayout(save, cancel, delete);

	public Binder<Mesa> binder = new Binder<>(Mesa.class);

	@Autowired
	public MesaEditor(MesaRepository repository) {
		this.repository = repository;
		
		Label label = new Label("Formulario de Mesa");
		label.addStyleName(ValoTheme.LABEL_H2);
		label.addStyleName(ValoTheme.LABEL_COLORED);
		
		Label espacio = new Label("<br>",ContentMode.HTML);
		
		addComponents(label, numero, zona, actions, espacio);

		binder.forField(numero).asRequired("Numero requerido")
		  .bind(Mesa::getNumero, Mesa::setNumero);
		  
		binder.forField(zona).asRequired("Debes seleccionar una zona")
			.bind(Mesa::getZona, Mesa::setZona);
		
		// Configure and style components
		setSpacing(true);
		actions.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
		save.setStyleName(ValoTheme.BUTTON_PRIMARY);
		save.setClickShortcut(ShortcutAction.KeyCode.ENTER);

		// wire action buttons to save, delete and reset
		save.addClickListener(e -> {
			if(binder.validate().isOk()){
				if(repository.findByNumeroAndZonaAndRestaurante(mesa.getNumero(), mesa.getZona(),
						(Restaurante) VaadinSession.getCurrent().getAttribute("restaurante")) == null) {
					mesa.setRestaurante((Restaurante) VaadinSession.getCurrent().getAttribute("restaurante"));
					repository.save(mesa);
				} else {
					Notification.show("Ya existe una mesa con ese número en esa zona", "",Notification.Type.ERROR_MESSAGE);
				}
			}
		});
		delete.addClickListener(e -> repository.delete(mesa));
		cancel.addClickListener(e -> editarMesa(mesa));
		setVisible(false);
		
		delete.setEnabled(SecurityUtils.hasRole("ROLE_MANAGER") || SecurityUtils.hasRole("ROLE_ADMIN"));
	}

	public interface ChangeHandler {

		void onChange();
	}

	public final void editarMesa(Mesa c) {
		if (c == null) {
			setVisible(false);
			return;
		}
		final boolean persisted = c.getId() != null;
		if (persisted) {
			// Find fresh entity for editing
			mesa = repository.findOne(c.getId());
		}
		else {
			mesa = c;
		}
		delete.setVisible(persisted);

		// Bind customer properties to similarly named fields
		// Could also use annotation or "manual binding" or programmatically
		// moving values from fields to entities before saving
		binder.setBean(mesa);

		setVisible(true);

		// A hack to ensure the whole form is visible
		save.focus();
		// Select all text in firstName field automatically
		//numero.selectAll();
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

	public static void listarMesas(String filterText, String tipoFiltro, Grid<Mesa> grid, 
			MesaRepository repo) 
		{ 		
	 		if (StringUtils.isEmpty(filterText) && StringUtils.isEmpty(tipoFiltro)) {
	 			if(VaadinSession.getCurrent().getAttribute("restaurante") != null)
	 				grid.setItems(repo.findByRestaurante(VaadinSession.getCurrent().getAttribute("restaurante")));
	 			else
	 				Notification.show("Debes seleccionar un restaurante", "Botón admin en la vista principal",Notification.Type.WARNING_MESSAGE);
			}
			else switch(tipoFiltro){
	 		case "zona":
	 			grid.setItems(repo.findByZonaStartsWithIgnoreCase(filterText));
	 			break;
	 		case "numero":
	 			grid.setItems(repo.findByNumeroStartsWithIgnoreCase(filterText));
	 			break;
			}
		}
}
