package iw.iguana.vista;

import java.sql.Date;
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
import com.vaadin.ui.AbstractTextField;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

import iw.iguana.controlador.ProductoEditor;
import iw.iguana.dao.FamiliaProductoRepository;
import iw.iguana.dao.LineaVentaRepository;
import iw.iguana.dao.ProductoRepository;
import iw.iguana.dao.VentaRepository;
import iw.iguana.modelo.Producto;
import iw.iguana.modelo.Venta;
import iw.iguana.modelo.Cliente;
import iw.iguana.modelo.FamiliaProducto;
import iw.iguana.modelo.LineaVenta;

@SpringView(name = VentaView.VIEW_NAME)
public class VentaView extends VerticalLayout implements View {
    
	private static final long serialVersionUID = 1L;

	public static final String VIEW_NAME = "venta";

	private final VentaRepository repo;
	
	//private final LineaVentaRepository lvrepo;

	final Grid<Venta> grid;
	
	final TextField filter, filter2;

	@Autowired
	public VentaView(VentaRepository repo, LineaVentaRepository lvrepo) {
		this.repo = repo;
		//this.lvrepo = lvrepo;
		this.grid = new Grid<>(Venta.class);
		this.filter = new TextField();
		this.filter2 = new TextField();
	}
 	
 	@PostConstruct
	void init() {
 		HorizontalLayout actions = new HorizontalLayout(filter,filter2);
		actions.setMargin(false);
		
		VerticalLayout mainLayout = new VerticalLayout(grid);
		
		addComponent(mainLayout);
		
		grid.setHeight(500, Unit.PIXELS);
		grid.setWidth(1070, Unit.PIXELS);
		grid.setColumns("id", "fecha", "precio", "mesa", "cliente");
		//grid.addColumn(Venta::getProductos).setCaption("Productos");


		// Hook logic to components

		// Replace listing with filtered content when user changes filter
		filter.setValueChangeMode(ValueChangeMode.LAZY);
		filter.addValueChangeListener(e -> listarVentas(e.getValue().toString(), "fecha"));
		filter2.setValueChangeMode(ValueChangeMode.LAZY);
		filter2.addValueChangeListener(e -> listarVentas(e.getValue(), "precio"));
		/*filter3.setValueChangeMode(ValueChangeMode.LAZY);
		filter3.addValueChangeListener(e -> listarVentas(e.getValue(), "mesa"));
		filter4.setValueChangeMode(ValueChangeMode.LAZY);
		filter4.addValueChangeListener(e -> listarVentas(e.getValue(), "cliente"));*/
		
		grid.asSingleSelect().addValueChangeListener(e -> {
			getUI().getUI().addWindow(Productos(e.getValue()));
		});

		// Initialize listing
		listarVentas(null, null);
	}

 	
 	@Override
 	public void enter(ViewChangeEvent event) {
 		// the view is constructed in the init() method()
 	}
 	
 	public void listarVentas(String filterText, String tipoFiltro) 
	{ 		
		if (StringUtils.isEmpty(filterText) && StringUtils.isEmpty(tipoFiltro)) {
			if(VaadinSession.getCurrent().getAttribute("restaurante") != null)
 				grid.setItems(repo.findByRestaurante(VaadinSession.getCurrent().getAttribute("restaurante")));
 			else
 				Notification.show("Debes seleccionar un restaurante", "Botón admin en la vista principal",Notification.Type.WARNING_MESSAGE);
		}
		else switch(tipoFiltro){
		 case "fecha":
			grid.setItems(repo.findByFechaContaining(filterText)); 
			break;
		 case "precio":
			if (!StringUtils.isEmpty(filterText))
				grid.setItems(repo.findByPrecio(Float.parseFloat(filterText)));
			else
				grid.setItems(repo.findAll());
			break;
	}
	}
 	
 	@SuppressWarnings("null")
	private Window Productos(Venta venta) {
		final Window window = new Window("Productos de la venta");
		//window.setWidth(800, Unit.PIXELS);
		window.setResizable(false);
		window.setDraggable(false);
		window.setModal(true);
		window.center();

		Grid<LineaVenta> gridProd = new Grid<>();
		
		gridProd.addColumn(LineaVenta::getNombreProd).setCaption("Producto");
		gridProd.addColumn(LineaVenta::getUnidades).setCaption("Unidades");
		gridProd.setItems(venta.getProductos());
		gridProd.asSingleSelect().addValueChangeListener(e -> {
			window.close();
		});
		
		/*VerticalLayout main = new VerticalLayout();
		
		TextField total = new TextField(null, "Total");
		total.setReadOnly(true);
		total.addStyleName(ValoTheme.TEXTFIELD_LARGE);
		total.setValue("Coste total: " + String.format("%02d", venta.getPrecio()));
		
		main.addComponent(gridProd);
		main.addComponent(total);
		
		window.setContent(main);*/

		window.setContent(gridProd);
		
		return window;
	}
}