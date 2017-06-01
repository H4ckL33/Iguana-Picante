package iw.iguana.vista;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import iw.iguana.controlador.ClienteEditor;
import iw.iguana.dao.ClienteRepository;
import iw.iguana.modelo.Cliente;
import iw.iguana.seguridad.SecurityUtils;

@SpringView(name = ClienteView.VIEW_NAME)
public class ClienteView extends VerticalLayout implements View {
 
	private static final long serialVersionUID = 1L;

	public static final String VIEW_NAME = "cliente";

	private final ClienteRepository repo;

	private final ClienteEditor editor;

	final Grid<Cliente> grid;

	final TextField filter, filter2;

	private final Button addNewBtn;

	@Autowired
	public ClienteView(ClienteRepository repo, ClienteEditor editor) {
		this.repo = repo;
		this.editor = editor;
		this.grid = new Grid<>(Cliente.class);
		this.filter = new TextField();
		this.filter2 = new TextField();
		this.addNewBtn = new Button("Nuevo cliente", VaadinIcons.PLUS);
	}
 	
 	@PostConstruct
	void init() {
		HorizontalLayout actions = new HorizontalLayout(filter, filter2, addNewBtn);
		actions.setMargin(false);
		
		VerticalLayout mainLayout = new VerticalLayout(editor,actions, grid);
		
		addComponent(mainLayout);

		grid.setHeight(500, Unit.PIXELS);
		grid.setWidth(1070, Unit.PIXELS);
		grid.setColumns("id", "nombre", "apellidos", "direccion", "telefono");

		if(VaadinSession.getCurrent().getAttribute("restaurante") == null)
 			addNewBtn.setEnabled(false);
		
		filter.setPlaceholder("Filtrar por nombre");
		filter2.setPlaceholder("Filtrar por apellidos");

		// Hook logic to components

		// Replace listing with filtered content when user changes filter
		filter.setValueChangeMode(ValueChangeMode.LAZY);
		filter.addValueChangeListener(e -> ClienteEditor.listarClientes(e.getValue(), null, grid, repo));
		filter2.setValueChangeMode(ValueChangeMode.LAZY);
		filter2.addValueChangeListener(e -> ClienteEditor.listarClientes(null, e.getValue(), grid, repo));

		// Connect selected Customer to editor or hide if none is selected
		grid.asSingleSelect().addValueChangeListener(e -> {
			editor.editarCliente(e.getValue());
		});

		// Instantiate and edit new Customer the new button is clicked
		addNewBtn.addClickListener(e -> editor.editarCliente(new Cliente()));
		// Listen changes made by the editor, refresh data from backend
		editor.setChangeHandler(() -> {
			if(editor.binder.validate().isOk()){
				editor.setVisible(false);
			}else{
				Notification.show("Formulario malformado", "",Notification.Type.ERROR_MESSAGE);
			}
			ClienteEditor.listarClientes(filter.getValue(), filter2.getValue(), grid, repo);
		});

		// Initialize listing
		ClienteEditor.listarClientes(null, null, grid, repo);
	}

 	
 	@Override
 	public void enter(ViewChangeEvent event) {
 		// the view is constructed in the init() method()
 	}
}
