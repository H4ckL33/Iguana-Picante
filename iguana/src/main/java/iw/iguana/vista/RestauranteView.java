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
import com.vaadin.server.Sizeable.Unit;
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

import iw.iguana.controlador.ClienteEditor;
import iw.iguana.controlador.EmpleadoEditor;
import iw.iguana.controlador.MesaEditor;
import iw.iguana.controlador.RestauranteEditor;
import iw.iguana.dao.MesaRepository;
import iw.iguana.dao.RestauranteRepository;
import iw.iguana.modelo.Cliente;
import iw.iguana.modelo.Empleado;
import iw.iguana.modelo.Mesa;
import iw.iguana.modelo.Restaurante;

@SpringView(name = RestauranteView.VIEW_NAME)
public class RestauranteView extends VerticalLayout implements View {
    
	private static final long serialVersionUID = 1L;

	public static final String VIEW_NAME = "restaurante";

	private final RestauranteRepository repo;

	private final RestauranteEditor editor;

	final Grid<Restaurante> grid;
	
	final TextField filter, filter2;
	
	private final Button addNewBtn;

	@Autowired
	public RestauranteView(RestauranteRepository repo, RestauranteEditor editor) {
		this.repo = repo;
		this.editor = editor;
		this.grid = new Grid<>(Restaurante.class);
		this.filter = new TextField();
		this.filter2 = new TextField();
		this.addNewBtn = new Button("Nuevo Restaurante", VaadinIcons.PLUS);
	}
 	
 	@PostConstruct
	void init() {
 		HorizontalLayout actions = new HorizontalLayout(filter,filter2, addNewBtn);
		actions.setMargin(false);
		
		VerticalLayout mainLayout = new VerticalLayout(editor,actions,grid);
 			
 		addComponent(mainLayout);
				
 		grid.setHeight(500, Unit.PIXELS);
		grid.setWidth(1070, Unit.PIXELS);
		
		grid.setColumns("ciudad", "direccion");

		filter.setPlaceholder("Filtrar por ciudad");
		filter2.setPlaceholder("Filtrar por dirección");

		// Hook logic to components

		// Replace listing with filtered content when user changes filter
		filter.setValueChangeMode(ValueChangeMode.LAZY);
		filter.addValueChangeListener(e -> RestauranteEditor.listarRestaurante(e.getValue(), "ciudad", grid, repo));
		filter2.setValueChangeMode(ValueChangeMode.LAZY);
		filter2.addValueChangeListener(e -> RestauranteEditor.listarRestaurante(e.getValue(), "direccion", grid, repo));
		
		// Connect selected Customer to editor or hide if none is selected
		grid.asSingleSelect().addValueChangeListener(e -> {
			editor.editarRestaurante(e.getValue());
		});
		
		// Instantiate and edit new Customer the new button is clicked
		addNewBtn.addClickListener(e -> editor.editarRestaurante(new Restaurante()));
		// Listen changes made by the editor, refresh data from backend
		editor.setChangeHandler(() -> {
			if(editor.binder.validate().isOk()){
				editor.setVisible(false);
			}else{
				Notification.show("Formulario malformado", "",Notification.Type.ERROR_MESSAGE);
			}
			RestauranteEditor.listarRestaurante(filter.getValue(), filter2.getValue(), grid, repo);
		});

		// Initialize listing
		RestauranteEditor.listarRestaurante(null, null, grid, repo);
	}
 	
 	@Override
 	public void enter(ViewChangeEvent event) {
 		// the view is constructed in the init() method()
 	}
}