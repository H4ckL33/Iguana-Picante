package iw.iguana.controlador;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.vaadin.data.Binder;
import com.vaadin.data.converter.StringToFloatConverter;
import com.vaadin.data.converter.StringToIntegerConverter;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.event.ShortcutAction;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import iw.iguana.dao.FamiliaProductoRepository;
import iw.iguana.dao.LineaVentaRepository;
import iw.iguana.dao.ProductoRepository;
import iw.iguana.dao.ProductoSimpleRepository;
import iw.iguana.dao.VentaRepository;
import iw.iguana.modelo.FamiliaProducto;
import iw.iguana.modelo.ProductoSimple;
import iw.iguana.seguridad.SecurityUtils;

@SpringComponent
@ViewScope
public class ProductoEditor extends VerticalLayout {

	private static final long serialVersionUID = 1L;

	private final ProductoSimpleRepository repository;
	private final FamiliaProductoRepository repof;
	private final LineaVentaRepository repoLineaVenta;

	private ProductoSimple producto;
	
	/* Fields to edit properties in Customer entity */
	TextField nombre = new TextField("Nombre");
	TextArea descripcion = new TextArea("Descripción");
	TextField precio = new TextField("Precio");
	TextField stock = new TextField("Stock disponible");
	TextField iva = new TextField("IVA");
	List<FamiliaProducto> ejemplos;
	final ComboBox<FamiliaProducto> familia;

	/* Action buttons */
	Button save = new Button("Guardar", VaadinIcons.DOWNLOAD);
	Button delete = new Button("Borrar", VaadinIcons.TRASH);
	Button cancel = new Button("Cancelar");
	CssLayout actions = new CssLayout(save, cancel, delete);

	public Binder<ProductoSimple> binder = new Binder<>(ProductoSimple.class);
	
	@Autowired
	public ProductoEditor(ProductoSimpleRepository repository, FamiliaProductoRepository repof, LineaVentaRepository repoLineaVenta) {
		this.repository = repository;
		this.repof=repof;
		this.repoLineaVenta = repoLineaVenta;
		
		Label label = new Label("Formulario de Productos de la carta o Ingredientes");
		label.addStyleName(ValoTheme.LABEL_H2);
		label.addStyleName(ValoTheme.LABEL_COLORED);
		
		ejemplos=repof.findAll();
		familia = new ComboBox<>("Selecciona una opción");
		familia.setItems(ejemplos);
		familia.setItemCaptionGenerator(FamiliaProducto::getNombre);
		
		//Binders
		binder.forField(nombre).asRequired("Campo nombre es requerido")
			.withValidator(new StringLengthValidator("El nombre debe tener al menos dos caracteres",2, 20))
			.bind(ProductoSimple::getNombre, ProductoSimple::setNombre);
				
		binder.forField(descripcion).bind(ProductoSimple::getDescripcion, ProductoSimple::setDescripcion);
				
		binder.forField(precio).asRequired("Precio requerido")
			.withConverter(new StringToFloatConverter("Introduzca precio"))
			.bind(ProductoSimple::getPrecio, ProductoSimple::setPrecio);
		
		binder.forField(iva).asRequired("IVA requerido")
			.withConverter(new StringToFloatConverter("Introduzca IVA"))
			.bind(ProductoSimple::getIva,ProductoSimple::setIva);
				
		binder.forField(stock).asRequired("Stock requerido")
			.withConverter(new StringToIntegerConverter("Introduzca stock"))
			.bind(ProductoSimple::getStock,ProductoSimple::setStock);
				
		binder.forField(familia).asRequired("Familia obligatoria")
			.bind(ProductoSimple::getFamilia,ProductoSimple::setFamilia);
		
		descripcion.setWidth(380, Unit.PIXELS);
		
		HorizontalLayout fila1 = new HorizontalLayout(nombre, precio, iva, stock);
		HorizontalLayout fila2 = new HorizontalLayout(descripcion, familia);
		
		Label espacio = new Label("<br>",ContentMode.HTML);
		
		addComponents(label, fila1, fila2, actions, espacio);

		// bind using naming convention
		//binder.bindInstanceFields(this);

		// Configure and style components
		setSpacing(true);
		actions.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
		save.setStyleName(ValoTheme.BUTTON_PRIMARY);
		save.setClickShortcut(ShortcutAction.KeyCode.ENTER);
		
		// wire action buttons to save, delete and reset
		save.addClickListener(e -> {
			if(binder.validate().isOk()){
				float pr = Float.parseFloat(precio.getValue());
				producto.setPrecio(pr);
			    float iv = Float.parseFloat(iva.getValue());
			    float preciofin = (pr * (iv/100))+pr;
			    producto.setPrecioFin(preciofin);
				repository.save(producto);
			}else{
				Notification.show("Formulario malformado", "",Notification.Type.ERROR_MESSAGE);
			}});
		delete.addClickListener(e -> {
			if(repoLineaVenta.findByProducto(producto).isEmpty())
				repository.delete(producto);
			else
				Notification.show("Este producto esta asociado a una venta, no se puede borrar",Notification.Type.ERROR_MESSAGE);
		});
		cancel.addClickListener(e -> setVisible(false));
		setVisible(false);
	}

	public interface ChangeHandler {

		void onChange();
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
	
	public final void editarProducto(ProductoSimple c) {
		if (c == null) {
			setVisible(false);
			return;
		}
		final boolean persisted = c.getId() != null;
		if (persisted) {
			// Find fresh entity for editing
			producto = repository.findOne(c.getId());
		}
		else {
			producto = c;
		}
		delete.setVisible(persisted);
		
		// Bind customer properties to similarly named fields
		// Could also use annotation or "manual binding" or programmatically
		// moving values from fields to entities before saving
		binder.setBean(producto);
		
		setVisible(true);

		// A hack to ensure the whole form is visible
		save.focus();
		// Select all text in firstName field automatically
		//nombre.selectAll();
	}

	public static void listarProductos(String filterText, String tipoFiltro, Grid<ProductoSimple> grid, 
			ProductoSimpleRepository repo) 
		{ 		
			if (StringUtils.isEmpty(filterText) && StringUtils.isEmpty(tipoFiltro)) {
				grid.setItems(repo.findAll());
			}
			else switch(tipoFiltro){
	 		case "nombre":
	 			grid.setItems(repo.findByNombreStartsWithIgnoreCase(filterText));
	 			break;
	 		case "precio":
	 			grid.setItems( repo.findByPrecio(Float.valueOf(filterText)));
	 			break;
	 		case "stock":
	 			grid.setItems( repo.findByStock(Integer.parseInt(filterText)));
	 			break;
	 		}
		}
}
