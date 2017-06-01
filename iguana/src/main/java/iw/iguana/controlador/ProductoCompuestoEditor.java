package iw.iguana.controlador;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.vaadin.data.Binder;
import com.vaadin.data.converter.StringToFloatConverter;
import com.vaadin.data.converter.StringToIntegerConverter;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.event.ShortcutAction;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.Sizeable.Unit;
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
import com.vaadin.ui.TwinColSelect;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import iw.iguana.dao.FamiliaProductoRepository;
import iw.iguana.dao.LineaVentaRepository;
import iw.iguana.dao.ProductoCompuestoRepository;
import iw.iguana.dao.ProductoRepository;
import iw.iguana.dao.ProductoSimpleRepository;
import iw.iguana.dao.VentaRepository;
import iw.iguana.modelo.FamiliaProducto;
import iw.iguana.modelo.Producto;
import iw.iguana.modelo.ProductoCompuesto;
import iw.iguana.modelo.ProductoSimple;
import iw.iguana.seguridad.SecurityUtils;

@SpringComponent
@ViewScope
public class ProductoCompuestoEditor extends VerticalLayout {

	private static final long serialVersionUID = 1L;

	private final ProductoCompuestoRepository repository;
	private final ProductoRepository rep;
	private final FamiliaProductoRepository repof;
	private final LineaVentaRepository repoLineaVenta;

	private ProductoCompuesto producto;

	/* Fields to edit properties in Customer entity */
	TextField nombre = new TextField("Nombre");
	TextArea descripcion = new TextArea("Descripción");
	TextField precio = new TextField("Precio");
	TextField stock = new TextField("Stock disponible");
	TextField iva = new TextField("IVA");
	TwinColSelect<Producto> select = new TwinColSelect<Producto>("");
	List<FamiliaProducto> ejemplos;
	final ComboBox<FamiliaProducto> familia;

	/* Action buttons */
	Button save = new Button("Guardar", VaadinIcons.DOWNLOAD);
	Button delete = new Button("Borrar", VaadinIcons.TRASH);
	Button cancel = new Button("Cancelar");
	CssLayout actions = new CssLayout(save, cancel, delete);

	public Binder<ProductoCompuesto> binder = new Binder<>(ProductoCompuesto.class);
	
	@Autowired
	public ProductoCompuestoEditor(ProductoCompuestoRepository repository, FamiliaProductoRepository repof,
			ProductoRepository rep, LineaVentaRepository repoLineaVenta) {
		this.repository = repository;
		this.repof=repof;
		this.rep = rep;
		this.repoLineaVenta = repoLineaVenta;
		ejemplos=repof.findAll();
		familia = new ComboBox<>("Selecciona una opción");
		familia.setItems(ejemplos);
		familia.setItemCaptionGenerator(FamiliaProducto::getNombre);
		//datos = rep.findAll();
		select.setItems(rep.findAll());
		select.setLeftColumnCaption("Opciones");
	    select.setRightColumnCaption("Seleccionados");
	    select.setWidth(500, Unit.PIXELS);
		
		Label label = new Label("Formulario de Productos personalizados o menus");
		label.addStyleName(ValoTheme.LABEL_H2);
		label.addStyleName(ValoTheme.LABEL_COLORED);
		
		//Binders
		binder.forField(nombre).asRequired("Campo nombre es requerido")
			.withValidator(new StringLengthValidator("El nombre debe tener al menos dos caracteres",2, 20))
			.bind(ProductoCompuesto::getNombre, ProductoCompuesto::setNombre);
				
		binder.forField(descripcion).bind(ProductoCompuesto::getDescripcion, ProductoCompuesto::setDescripcion);
				
		binder.forField(precio).asRequired("Precio requerido")
			.withConverter(new StringToFloatConverter("Introduzca precio"))
			.bind(ProductoCompuesto::getPrecio, ProductoCompuesto::setPrecio);
		
		binder.forField(iva)
			.withConverter(new StringToFloatConverter("Introduzca IVA"))
			.bind(ProductoCompuesto::getIva,ProductoCompuesto::setIva);
				
		binder.forField(stock).asRequired("Stock requerido")
			.withConverter(new StringToIntegerConverter("Introduzca stock"))
			.bind(ProductoCompuesto::getStock,ProductoCompuesto::setStock);
				
		binder.forField(familia).asRequired("Familia obligatoria")
			.bind(ProductoCompuesto::getFamilia,ProductoCompuesto::setFamilia);
		
		binder.forField(select).asRequired("Debe contener al menos 1 producto")
			.bind(ProductoCompuesto::getProductos, ProductoCompuesto::setProductos);
		
		descripcion.setWidth(380, Unit.PIXELS);
		
		HorizontalLayout fila1 = new HorizontalLayout(nombre, precio, iva, stock);
		HorizontalLayout fila2 = new HorizontalLayout(descripcion, familia);
		
		VerticalLayout columna = new VerticalLayout(fila1, fila2);
		columna.setMargin(false);
		
		HorizontalLayout main = new HorizontalLayout(columna,select);
		
		Label espacio = new Label("<br>",ContentMode.HTML);
		
		addComponents(label, main, actions, espacio);

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
				Notification.show("Formulario malformado");
			}});
		delete.addClickListener(e -> {
			if(repoLineaVenta.findByProducto(producto).isEmpty())
				repository.delete(producto);
			else
				Notification.show("Este producto esta asociado a una venta, no se puede borrar",Notification.Type.ERROR_MESSAGE);
		});
		cancel.addClickListener(e -> setVisible(false));
		setVisible(false);
		
		delete.setEnabled(SecurityUtils.hasRole("ROLE_MANAGER") || SecurityUtils.hasRole("ROLE_ADMIN"));
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
		
	public static void listarProductosCompuestos(String filterText, String tipoFiltro, Grid<ProductoCompuesto> grid,
			ProductoCompuestoRepository repo) 
		{ 		
	 		if (StringUtils.isEmpty(filterText) && StringUtils.isEmpty(tipoFiltro)) {
				grid.setItems(repo.findAll());
			}
			else switch(tipoFiltro){
	 		case "nombre":
	 			grid.setItems(repo.findByNombreStartsWithIgnoreCase(filterText));
	 			break;
	 		case "precio":
	 			grid.setItems(repo.findByPrecio(Float.valueOf(filterText)));
	 			break;
	 		case "stock":
	 			grid.setItems(repo.findByStock(Integer.parseInt(filterText)));
	 			break;
	 		}
		}
	
	public final void editarProductoCompuesto(ProductoCompuesto c) {
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
}

