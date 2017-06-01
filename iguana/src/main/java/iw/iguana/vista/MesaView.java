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
import iw.iguana.dao.MesaRepository;
import iw.iguana.modelo.Cliente;
import iw.iguana.modelo.Empleado;
import iw.iguana.modelo.Mesa;
import iw.iguana.seguridad.SecurityUtils;

@SpringView(name = MesaView.VIEW_NAME)
public class MesaView extends VerticalLayout implements View {
    
	private static final long serialVersionUID = 1L;

	public static final String VIEW_NAME = "mesa";

	private final MesaRepository repo;

	private final MesaEditor editor;

	final Grid<Mesa> grid;
	
	final TextField filter, filter2;
	
	private final Button addNewBtn;

	@Autowired
	public MesaView(MesaRepository repo, MesaEditor editor) {
		this.repo = repo;
		this.editor = editor;
		this.grid = new Grid<>(Mesa.class);
		this.filter = new TextField();
		this.filter2 = new TextField();
		this.addNewBtn = new Button("Nueva Mesa", VaadinIcons.PLUS);
	}
 	
 	@PostConstruct
	void init() {
 		HorizontalLayout actions = new HorizontalLayout(filter,filter2, addNewBtn);
		actions.setMargin(false);
		
		VerticalLayout mainLayout = new VerticalLayout(editor,actions,grid);
 			
 		addComponent(mainLayout);
				
 		grid.setHeight(500, Unit.PIXELS);
		grid.setWidth(1070, Unit.PIXELS);
		
		grid.setColumns("numero", "zona");
		
		if(VaadinSession.getCurrent().getAttribute("restaurante") == null)
 			addNewBtn.setEnabled(false);

		filter.setPlaceholder("Filtrar por identificación");
		filter2.setPlaceholder("Filtrar por zona");

		// Hook logic to components

		// Replace listing with filtered content when user changes filter
		//filter.setValueChangeMode(ValueChangeMode.LAZY);
		//filter.addValueChangeListener(e -> MesaEditor.listarMesas(e.getValue(), "id", grid, repo));
		filter2.setValueChangeMode(ValueChangeMode.LAZY);
		filter2.addValueChangeListener(e -> MesaEditor.listarMesas(e.getValue(), "zona", grid, repo));
		
		// Connect selected Customer to editor or hide if none is selected
		grid.asSingleSelect().addValueChangeListener(e -> {
			editor.editarMesa(e.getValue());
		});

		
		// Instantiate and edit new Customer the new button is clicked
		addNewBtn.addClickListener(e -> editor.editarMesa(new Mesa()));
		// Listen changes made by the editor, refresh data from backend
		editor.setChangeHandler(() -> {
			if(editor.binder.validate().isOk()){
				editor.setVisible(false);
			}else{
				Notification.show("Formulario malformado", "",Notification.Type.ERROR_MESSAGE);
			}
			MesaEditor.listarMesas(filter.getValue(), filter2.getValue(), grid, repo);
		});

		// Initialize listing
		MesaEditor.listarMesas(null, null, grid, repo);
	}

 	
 	@Override
 	public void enter(ViewChangeEvent event) {
 		// the view is constructed in the init() method()
 	}
}