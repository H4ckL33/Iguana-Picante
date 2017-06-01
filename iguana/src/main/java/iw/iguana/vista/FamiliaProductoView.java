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

import iw.iguana.controlador.FamiliaProductoEditor;
import iw.iguana.dao.FamiliaProductoRepository;
import iw.iguana.modelo.FamiliaProducto;
import iw.iguana.seguridad.SecurityUtils;

@SpringView(name = FamiliaProductoView.VIEW_NAME)
public class FamiliaProductoView extends VerticalLayout implements View {
    
	private static final long serialVersionUID = 1L;

	public static final String VIEW_NAME = "familia producto";

	private final FamiliaProductoRepository repo;

	private final FamiliaProductoEditor editor;

	final Grid<FamiliaProducto> grid;
	
	final TextField filter;
	
	private final Button addNewBtn;

	@Autowired
	public FamiliaProductoView(FamiliaProductoRepository repo, FamiliaProductoEditor editor) {
		this.repo = repo;
		this.editor = editor;
		this.grid = new Grid<>(FamiliaProducto.class);
		this.filter = new TextField();
		this.addNewBtn = new Button("Nueva Familia Producto", VaadinIcons.PLUS);
	}
 	
 	@PostConstruct
	void init() {
 		HorizontalLayout actions = new HorizontalLayout(filter, addNewBtn);
		actions.setMargin(false);
		
		VerticalLayout mainLayout = new VerticalLayout(editor,actions,grid);
		
		addComponent(mainLayout);
		
		addNewBtn.setEnabled(SecurityUtils.hasRole("ROLE_ADMIN"));

		grid.setHeight(500, Unit.PIXELS);
		grid.setWidth(1070, Unit.PIXELS);
		grid.setColumns("id", "nombre");

		filter.setPlaceholder("Filtrar por nombre");

		// Hook logic to components

		// Replace listing with filtered content when user changes filter
		filter.setValueChangeMode(ValueChangeMode.LAZY);
		filter.addValueChangeListener(e -> FamiliaProductoEditor.listarFamilia(e.getValue(), "nombre", grid, repo));
		
		
		// Connect selected Customer to editor or hide if none is selected
		if(SecurityUtils.hasRole("ROLE_ADMIN")) {
			grid.asSingleSelect().addValueChangeListener(e -> {
				editor.editarFamilia(e.getValue());
			});
		} 
		else {
			grid.asSingleSelect().addValueChangeListener(e -> {
				Notification.show("No tienes permiso para editar", "", Notification.Type.ERROR_MESSAGE);
			});
		}

		
		// Instantiate and edit new Customer the new button is clicked
		addNewBtn.addClickListener(e -> editor.editarFamilia(new FamiliaProducto("")));
		// Listen changes made by the editor, refresh data from backend
		editor.setChangeHandler(() -> {
			if(editor.binder.validate().isOk()){
				editor.setVisible(false);
			}else{
				Notification.show("Formulario malformado", "",Notification.Type.ERROR_MESSAGE);
			}
			FamiliaProductoEditor.listarFamilia(filter.getValue(), "", grid, repo);
		});
		

		// Initialize listing
		FamiliaProductoEditor.listarFamilia(null, null, grid, repo);
	}

 	
 	@Override
 	public void enter(ViewChangeEvent event) {
 		// the view is constructed in the init() method()
 	}
}