package iw.iguana.controlador;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.vaadin.ui.Grid;
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

import iw.iguana.dao.ClienteRepository;
import iw.iguana.modelo.Cliente;

@SpringComponent
@UIScope
public class ClienteEditor extends VerticalLayout{
	 public static void listarClientes(String filterText, String tipoFiltro, Grid<Cliente> grid, ClienteRepository repo) {   
			   if (StringUtils.isEmpty(filterText) && StringUtils.isEmpty(tipoFiltro)) {
			   grid.setItems(repo.findAll());
			  }
			  else switch(tipoFiltro){
			   case "nombre":
			    grid.setItems(repo.findByNombreStartsWithIgnoreCase(filterText));
			    break;
			   case "telefono":
			    grid.setItems(repo.findByTelefonoStartsWithIgnoreCase(filterText));
			    break;
			   }
			 }
	 
	 private static final long serialVersionUID = 1L;

	 private final ClienteRepository repository;

	 private Cliente cliente;

	 /* Fields to edit properties in Customer entity */
	 TextField nombre = new TextField("Nombre");
	 TextField direccion = new TextField("Direccion");
	 TextField telefono = new TextField("Telefono");

	 /* Action buttons */
	 Button save = new Button("Guardar", VaadinIcons.DOWNLOAD);
	 Button delete = new Button("Borrar", VaadinIcons.TRASH);
	 Button cancel = new Button("Cancelar");
	 CssLayout actions = new CssLayout(save, cancel, delete);

	 Binder<Cliente> binder = new Binder<>(Cliente.class);


	 @Autowired
	 public ClienteEditor(ClienteRepository repository) {
	  this.repository = repository;

	  addComponents(nombre, direccion, telefono);

	  // bind using naming convention
	  binder.bindInstanceFields(this);

	  // Configure and style components
	  setSpacing(true);
	  actions.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
	  save.setStyleName(ValoTheme.BUTTON_PRIMARY);
	  save.setClickShortcut(ShortcutAction.KeyCode.ENTER);

	  // wire action buttons to save, delete and reset
	  save.addClickListener(e -> repository.save(cliente));
	  delete.addClickListener(e -> repository.delete(cliente));
	  cancel.addClickListener(e -> editarCliente(cliente));
	  setVisible(false);
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
	   // Find fresh entity for editing
	   cliente = repository.findOne(c.getId());
	  }
	  else {
	   cliente = c;
	  }
	  cancel.setVisible(persisted);

	  // Bind customer properties to similarly named fields
	  // Could also use annotation or "manual binding" or programmatically
	  // moving values from fields to entities before saving
	  binder.setBean(cliente);

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
