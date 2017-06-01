package iw.iguana.vista;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import iw.iguana.controlador.EmpleadoEditor;
import iw.iguana.dao.EmpleadoRepository;
import iw.iguana.modelo.Empleado;
import iw.iguana.seguridad.SecurityUtils;

@SpringView(name = EmpleadoView.VIEW_NAME)
public class EmpleadoView extends VerticalLayout implements View {
    
	private static final long serialVersionUID = 1L;

	public static final String VIEW_NAME = "empleado";

	private final EmpleadoRepository repo;

	private final EmpleadoEditor editor;

	final Grid<Empleado> grid;
	
	final TextField filter, filter2, filter3;
	
	private final Button addNewBtn;

	@Autowired
	public EmpleadoView(EmpleadoRepository repo, EmpleadoEditor editor) {
		this.repo = repo;
		this.editor = editor;
		this.grid = new Grid<>(Empleado.class);
		this.filter = new TextField();
		this.filter2 = new TextField();
		this.filter3 = new TextField();
		this.addNewBtn = new Button("Nuevo empleado", VaadinIcons.PLUS);
	}
 	
 	@PostConstruct
	void init() {
 		HorizontalLayout actions = new HorizontalLayout(filter3, filter, filter2, addNewBtn);
 		actions.setMargin(false);

 		VerticalLayout mainLayout = new VerticalLayout(editor,actions,grid);
 		  
 		addComponent(mainLayout);
 		
 		if(VaadinSession.getCurrent().getAttribute("restaurante") != null)
 			addNewBtn.setEnabled(SecurityUtils.hasRole("ROLE_ADMIN"));
		else
			addNewBtn.setEnabled(false);
		
		grid.setHeight(500, Unit.PIXELS);
		grid.setWidth(1070, Unit.PIXELS);
		grid.setColumns("dni", "nombre", "apellidos", "direccion", "telefono", "rol");

		filter.setPlaceholder("Filtrar por nombre");
		filter2.setPlaceholder("Filtrar por apellidos");
		filter3.setPlaceholder("Filtrar por dni");

		// Hook logic to components

		// Replace listing with filtered content when user changes filter
		filter.setValueChangeMode(ValueChangeMode.LAZY);
		filter.addValueChangeListener(e -> EmpleadoEditor.listarEmpleados(e.getValue(), "nombre", grid, repo));
		filter2.setValueChangeMode(ValueChangeMode.LAZY);
		filter2.addValueChangeListener(e -> EmpleadoEditor.listarEmpleados(e.getValue(), "apellidos", grid, repo));
		filter3.setValueChangeMode(ValueChangeMode.LAZY);
		filter3.addValueChangeListener(e -> EmpleadoEditor.listarEmpleados(e.getValue(), "dni", grid, repo));
		
		// Connect selected Customer to editor or hide if none is selected
		grid.asSingleSelect().addValueChangeListener(e -> {
			editor.editando();
			editor.editarEmpleado(e.getValue());
		});
		
		// Instantiate and edit new Customer the new button is clicked
		addNewBtn.addClickListener(e -> {editor.noEditando(); editor.editarEmpleado(new Empleado());});
		// Añadiendo aqui las validaciones, cuando edito un empleado, el editor no se quita mientras no este todo correcto.
		editor.setChangeHandler(() -> {
			if(editor.binder.validate().isOk()){
				editor.setVisible(false);
			}else{
				Notification.show("Formulario malformado", "",Notification.Type.ERROR_MESSAGE);
			}
			//editor.setVisible(false);
			EmpleadoEditor.listarEmpleados(filter.getValue(), "", grid, repo);
		});
		

		// Initialize listing
		EmpleadoEditor.listarEmpleados(null, null, grid, repo);
	}

 	
 	@Override
 	public void enter(ViewChangeEvent event) {
 		// the view is constructed in the init() method()
 	}
}