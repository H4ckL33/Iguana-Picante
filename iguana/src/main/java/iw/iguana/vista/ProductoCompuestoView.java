package iw.iguana.vista;

import java.util.ArrayList;
import java.util.Set;
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
import com.vaadin.ui.TwinColSelect;
import com.vaadin.ui.VerticalLayout;

import iw.iguana.controlador.ProductoCompuestoEditor;
import iw.iguana.controlador.ProductoEditor;
import iw.iguana.dao.FamiliaProductoRepository;
import iw.iguana.dao.ProductoCompuestoRepository;
import iw.iguana.dao.ProductoRepository;
import iw.iguana.modelo.Producto;
import iw.iguana.modelo.ProductoCompuesto;
import iw.iguana.modelo.ProductoSimple;
import iw.iguana.modelo.FamiliaProducto;

@SpringView(name = ProductoCompuestoView.VIEW_NAME)
public class ProductoCompuestoView extends VerticalLayout implements View {
    
	private static final long serialVersionUID = 1L;

	public static final String VIEW_NAME = "productoCompuesto";

	private final ProductoCompuestoRepository repo;
	
	private final FamiliaProductoRepository repof;

	private final ProductoCompuestoEditor editor;

	final Grid<ProductoCompuesto> grid;
	
	final TextField filter, filter2, filter3;
	
	private Button addNewBtn;

	@Autowired
	public ProductoCompuestoView(ProductoCompuestoRepository repo, ProductoCompuestoEditor editor, FamiliaProductoRepository repof) {
		this.repo = repo;
		this.repof = repof;
		this.editor = editor;
		this.grid = new Grid<>(ProductoCompuesto.class);
		this.filter = new TextField();
		this.filter2 = new TextField();
		this.filter3 = new TextField();
		this.addNewBtn = new Button("Nuevo Producto personalizado", VaadinIcons.PLUS);
	}
 	
 	@PostConstruct
	void init() {
 		
 		HorizontalLayout actions = new HorizontalLayout(filter3, filter, filter2, addNewBtn);
		actions.setMargin(false);
		
		VerticalLayout mainLayout = new VerticalLayout(editor,actions,grid);
		
		addComponent(mainLayout);
		
		grid.setHeight(500, Unit.PIXELS);
		grid.setWidth(1200, Unit.PIXELS);
		grid.setColumns("id", "nombre", "descripcion", "precio", "stock", "iva", "familia", "productos");

		filter.setPlaceholder("Filtrar por nombre");
		filter2.setPlaceholder("Filtrar por precio");
		filter3.setPlaceholder("Filtrar por stock");

		// Hook logic to components

		// Replace listing with filtered content when user changes filter
		filter.setValueChangeMode(ValueChangeMode.LAZY);
		filter.addValueChangeListener(e -> ProductoCompuestoEditor.listarProductosCompuestos(e.getValue(), "nombre", grid, repo));
		filter2.setValueChangeMode(ValueChangeMode.LAZY);
		filter2.addValueChangeListener(e -> ProductoCompuestoEditor.listarProductosCompuestos(e.getValue(), "precio", grid, repo));
		filter3.setValueChangeMode(ValueChangeMode.LAZY);
		filter3.addValueChangeListener(e -> ProductoCompuestoEditor.listarProductosCompuestos(e.getValue(), "stock", grid, repo));
		 
		// Connect selected Customer to editor or hide if none is selected
		grid.asSingleSelect().addValueChangeListener(e -> {
			editor.editarProductoCompuesto(e.getValue());
		});
		
		// Connect selected Customer to editor or hide if none is selected
		/*select.addValueChangeListener(e -> {
			editor.editarProductoCompuesto((ProductoCompuesto) e.getValue());
			
			this.familia = new ComboBox<>("Selecciona una opción", ejemplos);
		    this.select = new TwinColSelect<Producto>("Select items");
			select.setItems(repo.findAll());
			this.addNewBtn = new Button("Nuevo Producto Compuesto", VaadinIcons.PLUS);
			this.addProdBtn = new Button("Añadir productos", VaadinIcons.PLUS);
		
		});*/

		// Instantiate and edit new Customer the new button is clicked
		addNewBtn.addClickListener(e -> editor.editarProductoCompuesto(new ProductoCompuesto()));
		// Listen changes made by the editor, refresh data from backend
		editor.setChangeHandler(() -> {
			if(editor.binder.validate().isOk()){
				editor.setVisible(false);
			}else{
				Notification.show("Formulario malformado", "",Notification.Type.ERROR_MESSAGE);
			}
			ProductoCompuestoEditor.listarProductosCompuestos(filter.getValue(), "", grid, repo);
		});

		// Initialize listing
		ProductoCompuestoEditor.listarProductosCompuestos(null, null, grid, repo);
	}

 	
 	@Override
 	public void enter(ViewChangeEvent event) {
 		// the view is constructed in the init() method()
 	}
}