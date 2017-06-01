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

import iw.iguana.controlador.ProductoEditor;
import iw.iguana.dao.FamiliaProductoRepository;
import iw.iguana.dao.ProductoRepository;
import iw.iguana.dao.ProductoSimpleRepository;
import iw.iguana.modelo.Producto;
import iw.iguana.modelo.ProductoSimple;
import iw.iguana.seguridad.SecurityUtils;
import iw.iguana.modelo.Cliente;
import iw.iguana.modelo.FamiliaProducto;

@SpringView(name = ProductoSimpleView.VIEW_NAME)
public class ProductoSimpleView extends VerticalLayout implements View {
    
	private static final long serialVersionUID = 1L;

	public static final String VIEW_NAME = "productoSimple";

	private final ProductoSimpleRepository repo;
	
	private final FamiliaProductoRepository repof;

	private final ProductoEditor editor;
	
	private ProductoSimple prod = new ProductoSimple();

	final Grid<ProductoSimple> grid;
	
	final TextField filter, filter2, filter3;
	
	private Button addNewBtn;
	
	ComboBox<FamiliaProducto> familia ;
	
	List<FamiliaProducto> ejemplos;

	@Autowired
	public ProductoSimpleView(ProductoSimpleRepository repo, ProductoEditor editor, FamiliaProductoRepository repof) {
		this.repo = repo;
		this.repof=repof;
		this.editor = editor;
		this.grid = new Grid<>(ProductoSimple.class);
		this.filter = new TextField();
		this.filter2 = new TextField();
		this.filter3 = new TextField();
		ejemplos = repof.findAll();
		this.familia = new ComboBox<>("Selecciona una opción", ejemplos);
		this.addNewBtn = new Button("Nuevo Producto", VaadinIcons.PLUS);
	}
 	
 	@PostConstruct
	void init() {
 		HorizontalLayout actions = new HorizontalLayout(filter,filter2, filter3, addNewBtn);
		actions.setMargin(false);
		
		VerticalLayout mainLayout = new VerticalLayout(editor,actions,grid);
		
		addComponent(mainLayout);
		
		addNewBtn.setEnabled(SecurityUtils.hasRole("ROLE_ADMIN")); //El unico que puede crear, modificar y borrar productos es el gerente.
		
		grid.setHeight(500, Unit.PIXELS);
		grid.setWidth(1070, Unit.PIXELS);
		grid.setColumns("id", "nombre", "descripcion", "precio", "precioFin", "stock", "iva", "familia");

		filter.setPlaceholder("Filtrar por nombre");
		filter2.setPlaceholder("Filtrar por precio");
		filter3.setPlaceholder("Filtrar por stock");

		// Hook logic to components

		// Replace listing with filtered content when user changes filter
		filter.setValueChangeMode(ValueChangeMode.LAZY);
		filter.addValueChangeListener(e -> ProductoEditor.listarProductos(e.getValue(), "nombre", grid, repo));
		filter2.setValueChangeMode(ValueChangeMode.LAZY);
		filter2.addValueChangeListener(e -> ProductoEditor.listarProductos(e.getValue(), "precio", grid, repo));
		filter3.setValueChangeMode(ValueChangeMode.LAZY);
		filter3.addValueChangeListener(e -> ProductoEditor.listarProductos(e.getValue(), "stock", grid, repo));

		// Connect selected Customer to editor or hide if none is selected
		if(SecurityUtils.hasRole("ROLE_ADMIN")) {
			grid.asSingleSelect().addValueChangeListener(e -> {
				editor.editarProducto(e.getValue());
			});
		} 
		else {
			grid.asSingleSelect().addValueChangeListener(e -> {
				Notification.show("No tienes permiso para editar", "", Notification.Type.ERROR_MESSAGE);
			});
		}
		
		// Instantiate and edit new Customer the new button is clicked
		addNewBtn.addClickListener(e -> editor.editarProducto(new ProductoSimple()));
		// Listen changes made by the editor, refresh data from backend
		editor.setChangeHandler(() -> {
			if(editor.binder.validate().isOk()){
				editor.setVisible(false);
			}else{
				Notification.show("Formulario malformado", "",Notification.Type.ERROR_MESSAGE);
			}
			ProductoEditor.listarProductos(filter.getValue(), "", grid, repo);
		});
		

		// Initialize listing
		ProductoEditor.listarProductos(null, null, grid, repo);
	}

 	
 	@Override
 	public void enter(ViewChangeEvent event) {
 		// the view is constructed in the init() method()
 	}
}