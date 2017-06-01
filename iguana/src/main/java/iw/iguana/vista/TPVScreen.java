package iw.iguana.vista;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Grid;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

import iw.iguana.dao.ClienteRepository;
import iw.iguana.dao.FamiliaProductoRepository;
import iw.iguana.dao.MesaRepository;
import iw.iguana.dao.ProductoRepository;
import iw.iguana.dao.RestauranteRepository;
import iw.iguana.dao.VentaRepository;
import iw.iguana.modelo.Cliente;
import iw.iguana.modelo.FamiliaProducto;
import iw.iguana.modelo.LineaVenta;
import iw.iguana.modelo.Mesa;
import iw.iguana.modelo.Producto;
import iw.iguana.modelo.Restaurante;
import iw.iguana.modelo.Venta;
import iw.iguana.seguridad.SecurityUtils;

@SuppressWarnings("serial")
@SpringView(name = TPVScreen.VIEW_NAME)
public class TPVScreen extends VerticalLayout implements View {
	
	//Button button = new Button();
	
	public static final String VIEW_NAME = "TPV";

	private FamiliaProductoRepository famRepository;
	private ProductoRepository prodRepository;
	private MesaRepository mesaRepository;
	private VentaRepository ventaRepository;
	private ClienteRepository clienteRepository;
	private RestauranteRepository restauranteRepository;

	private List<Producto> productos;
	private List<LineaVenta> lineasVenta;
	private Grid<LineaVenta> grid;
	private HorizontalLayout hlayout4;
	private GridLayout prodLayout;

	// All variables are automatically stored in the session.
	private float current;
	private float stored;
	private char lastOperationRequested;

	// User interface components
	private final TextField display;

	private int numMesa;
	private Long idCliente;
	private float total;
	private final TextField textTotal;
	private final TextField textMesa;
	private final TextField textCliente;
	private final CheckBox llevar;

	@SuppressWarnings("null")
	@Autowired
	public TPVScreen(FamiliaProductoRepository famRepository, ProductoRepository prodRepository,
			MesaRepository mesaRepository, VentaRepository ventaRepository, ClienteRepository clienteRepository,
			RestauranteRepository restauranteRepository) {
		this.famRepository = famRepository;
		this.prodRepository = prodRepository;
		this.mesaRepository = mesaRepository;
		this.ventaRepository = ventaRepository;
		this.clienteRepository = clienteRepository;
		this.restauranteRepository = restauranteRepository;
		this.productos = null;
		this.lineasVenta = new ArrayList<LineaVenta>();
		this.idCliente = null;
		this.current = 0.0f;
		this.stored = 0.0f;
		this.lastOperationRequested = 'C';
		this.display = new TextField(null, "0.0");
		this.total = 0.0f;
		this.textTotal = new TextField(null, "Total: " + String.format("%.2f €", total));
		this.textMesa = new TextField(null, "Mesa: ");
		this.textCliente = new TextField(null, "Cliente: ");
		this.llevar = new CheckBox("Para llevar", false);

		//Panel panel = new Panel("Ventana Principal");
		
		setMargin(false);
		setSpacing(false);
		
		//MainScreen.Menu();//AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
		
		//getParent().setVisible(false);
		
		HorizontalLayout hlayout1 = new HorizontalLayout();
		hlayout1.setSizeFull();

		VerticalLayout vlayout1 = new VerticalLayout();
		// vlayout1.setSizeFull();

		HorizontalLayout hlayout2 = new HorizontalLayout();
		hlayout2.setSizeFull();

		VerticalLayout vlayout2 = new VerticalLayout();

		vlayout2.setMargin(false);

		ticket();

		// label.addStyleName(ValoTheme.LABEL_BOLD);
		// label.addStyleName(ValoTheme.LABEL_H2);
		// label.setSizeFull();

		HorizontalLayout hlayout3 = new HorizontalLayout();

		hlayout3.setSizeFull();

		textMesa.setSizeFull();
		textMesa.setReadOnly(true);
		textMesa.addStyleName(ValoTheme.TEXTFIELD_LARGE);
		
		textCliente.setSizeFull();
		textCliente.setReadOnly(true);
		textCliente.addStyleName(ValoTheme.TEXTFIELD_LARGE);
		
		textTotal.setSizeFull();
		textTotal.setReadOnly(true);
		textTotal.addStyleName(ValoTheme.TEXTFIELD_ALIGN_RIGHT);
		textTotal.addStyleName(ValoTheme.TEXTFIELD_LARGE);
		
		llevar.addStyleName(ValoTheme.TEXTFIELD_LARGE);

		hlayout3.addComponent(textMesa);
		hlayout3.addComponent(textCliente);
		hlayout3.addComponent(textTotal);
		hlayout3.addComponent(llevar);

		hlayout3.setExpandRatio(textMesa, 1.2f);
		hlayout3.setExpandRatio(textCliente, 2.2f);
		hlayout3.setExpandRatio(textTotal, 1.5f);
		hlayout3.setExpandRatio(llevar, 1);
		hlayout3.setComponentAlignment(llevar, Alignment.MIDDLE_CENTER);

		vlayout2.addComponent(grid);
		vlayout2.addComponent(hlayout3);

		hlayout2.addComponent(vlayout2);

		GridLayout calcLayout = calculadora();
		hlayout2.addComponent(calcLayout);

		hlayout2.setExpandRatio(vlayout2, 0.6f);
		hlayout2.setExpandRatio(calcLayout, 0.4f);
		hlayout2.setComponentAlignment(calcLayout, Alignment.TOP_CENTER);

		vlayout1.addComponent(hlayout2);
		// vlayout1.setComponentAlignment(hlayout2, Alignment.TOP_RIGHT);

		hlayout4 = new HorizontalLayout();
		hlayout4.setSizeFull();

		GridLayout famLayout = familia();
		hlayout4.addComponent(famLayout);
		hlayout4.setExpandRatio(famLayout, 0.3f);

		prodLayout = producto();
		hlayout4.addComponent(prodLayout);
		hlayout4.setExpandRatio(prodLayout, 0.7f);
		hlayout4.setMargin(new MarginInfo(true, false, false, false));

		vlayout1.addComponent(hlayout4);
		vlayout1.setComponentAlignment(hlayout4, Alignment.BOTTOM_LEFT);

		hlayout1.addComponent(vlayout1);

		GridLayout grid1 = menu();
		grid1.setMargin(new MarginInfo(true, false, true, false));
		hlayout1.addComponent(grid1);
		hlayout1.setComponentAlignment(grid1, Alignment.TOP_CENTER);

		hlayout1.setExpandRatio(vlayout1, 0.80f);
		hlayout1.setExpandRatio(grid1, 0.2f);

		//panel.setContent(hlayout1);
		//addComponent(panel);
		addComponent(hlayout1);
		
		llevar.addValueChangeListener(event -> {
			if(llevar.getValue() == true) {
				idCliente = null;
				textCliente.setValue("Cliente: ");
				numMesa = 0;
				textMesa.setValue("Mesa: ");
			}
		});
	}

	private void ticket() {
		grid = new Grid<>();
		grid.setSizeFull();

		grid.addColumn(LineaVenta::getUnidades).setCaption("Uds.").setResizable(false).setSortable(false)
				.setExpandRatio(0);
		grid.addColumn(LineaVenta::getNombreProd).setCaption("Descripción").setResizable(false).setSortable(false)
				.setExpandRatio(5);
		grid.addColumn(LineaVenta::getPrecio).setCaption("Precio").setResizable(false).setSortable(false)
				.setExpandRatio(1);
		grid.addColumn(LineaVenta::getSubtotal).setCaption("Total").setResizable(false).setSortable(false)
				.setExpandRatio(1);
	}

	private GridLayout calculadora() {
		GridLayout grid = new GridLayout(4, 5);

		grid.setDefaultComponentAlignment(Alignment.TOP_RIGHT);
		// grid.setSizeFull();
		grid.setSpacing(true);

		display.setSizeFull();
		display.addStyleName(ValoTheme.TEXTFIELD_ALIGN_RIGHT);
		display.setReadOnly(true);

		grid.addComponent(display, 0, 0, 3, 0);

		String[] operations = new String[] { "7", "8", "9", "C", "4", "5", "6", "X", "1", "2", "3", "-", "0", ".", "%",
				"<" };

		for (String caption : operations) {
			Button button = new Button(caption);
			button.setWidth("60pt");
			button.setHeight("60pt");

			button.addClickListener(e -> calculate(button.getCaption().charAt(0)));

			grid.addComponent(button);
		}
		
		if(VaadinSession.getCurrent().getAttribute("restaurante") == null)
 			grid.setEnabled(false);

		return grid;
	}

	private void calculate(char requestedOperation) {
		if ('0' <= requestedOperation && requestedOperation <= '9') {
			current = current * 10 + Float.parseFloat("" + requestedOperation);
			display.setValue(Float.toString(current));
		} else {
			switch (lastOperationRequested) {
			case '+':
				stored += current;
				break;
			case '-':
				stored -= current;
				break;
			case '/':
				stored /= current;
				break;
			case 'X':
				stored *= current;
				break;
			case 'C':
				stored = current;
				break;
			}

			lastOperationRequested = requestedOperation;
			current = 0.0f;

			if (requestedOperation == 'C') {
				stored = 0.0f;
			}

			display.setValue(Float.toString(stored));
		}
	}

	private GridLayout familia() {
		GridLayout grid = new GridLayout(3, 3);

		grid.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
		grid.setSpacing(true);

		List<FamiliaProducto> familias = famRepository.findAll();

		for (FamiliaProducto fam : familias) {
			// Button button = new Button(fam.getNombre());
			Button button = new Button();
			button.setWidth("80pt");
			button.setHeight("80pt");
			button.addClickListener(e -> getProductos(fam));

			VerticalLayout layout = new VerticalLayout();
			layout.addComponent(button);
			Label label = new Label(fam.getNombre());
			label.setStyleName(ValoTheme.LABEL_BOLD);
			layout.addComponent(label);
			layout.setComponentAlignment(button, Alignment.MIDDLE_CENTER);
			layout.setComponentAlignment(label, Alignment.MIDDLE_CENTER);
			layout.setSpacing(false);
			layout.setMargin(false);

			grid.addComponent(layout);
		}

		if(VaadinSession.getCurrent().getAttribute("restaurante") == null) 
 			grid.setEnabled(false);
		
		return grid;
	}

	private void getProductos(FamiliaProducto familia) {
		productos = prodRepository.findByFamilia(familia);
		hlayout4.removeComponent(prodLayout);
		prodLayout = producto();
		hlayout4.addComponent(prodLayout);
		hlayout4.setExpandRatio(prodLayout, 0.6f);
	}

	private GridLayout producto() {
		GridLayout grid = new GridLayout(7, 3);

		grid.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
		grid.setSpacing(true);

		if (productos != null) {
			for (Producto prod : productos) {
				// Button button = new Button(prod.getNombre());
				Button button = new Button();
				button.setWidth("80pt");
				button.setHeight("80pt");
				button.setStyleName(ValoTheme.BUTTON_ICON_ONLY);

				button.addClickListener(e -> addProducto(prod));

				VerticalLayout layout = new VerticalLayout();
				layout.addComponent(button);
				Label label = new Label(prod.getNombre());
				label.setStyleName(ValoTheme.LABEL_BOLD);
				layout.addComponent(label);
				layout.setComponentAlignment(button, Alignment.MIDDLE_CENTER);
				layout.setComponentAlignment(label, Alignment.MIDDLE_CENTER);
				layout.setSpacing(false);
				layout.setMargin(false);

				grid.addComponent(layout);
			}
		}

		return grid;
	}

	private void addProducto(Producto prod) {
		int uds;
		if (current > 0) {
			uds = (int) current;
			current = 0.0f;
			display.setValue("0.0");
		} else
			uds = 1;
		LineaVenta linea = new LineaVenta(uds, false, prod);
		System.out.println("LineaVenta: " + linea);
		lineasVenta.add(linea);
		total += linea.getSubtotal();
		textTotal.setValue("Total: " + String.format("%.2f €", total));
		grid.setItems(lineasVenta);
	}

	private GridLayout menu() {
		GridLayout layout = new GridLayout(2, 6);

		// layout.setSizeFull();
		layout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
		layout.setSpacing(true);

		/** BOTÓN BORRAR **/
		Button borrar = new Button("Borrar");
		borrar.setWidth("80pt");
		borrar.setHeight("80pt");
		borrar.addClickListener(e -> borrarLineaVenta());

		layout.addComponent(borrar);

		/** BOTÓN FINALIZAR **/
		Button finalizar = new Button("Finalizar");
		finalizar.setWidth("80pt");
		finalizar.setHeight("80pt");
		finalizar.addClickListener(e -> finalizarVenta());

		layout.addComponent(finalizar);

		/** BOTÓN MESA **/
		Button mesa = new Button("Mesa");
		mesa.setWidth("80pt");
		mesa.setHeight("80pt");
		mesa.addClickListener(e -> setMesa());

		layout.addComponent(mesa);

		/** BOTÓN CLIENTE **/
		Button cliente = new Button("Cliente");
		cliente.setWidth("80pt");
		cliente.setHeight("80pt");
		cliente.addClickListener(e -> setCliente());

		layout.addComponent(cliente);
		
		/** BOTÓN COCINA **/
		Button cocina = new Button("Cocina");
		cocina.setWidth("80pt");
		cocina.setHeight("80pt");
		cocina.addClickListener(e -> enviarCocina());

		layout.addComponent(cocina);
		
		/** BOTÓN NUEVO PEDIDO **/
		  Button nuevo = new Button("Nuevo");
		  nuevo.setWidth("80pt");
		  nuevo.setHeight("80pt");
		  nuevo.addClickListener(e -> setNuevoPedido());
		  
		  layout.addComponent(nuevo);
		  
		  /** BOTÓN RECUPERAR PEDIDOS **/
		  Button recuperar = new Button("Recuperar");
		  recuperar.setWidth("80pt");
		  recuperar.setHeight("80pt");
		  recuperar.addClickListener(e -> setRecuperar());
		  
		  layout.addComponent(recuperar);
		
		/** BOTÓN ADMIN **/
		Button admin = new Button("Admin");
		admin.setWidth("80pt");
		admin.setHeight("80pt");
		admin.addClickListener(e -> setAdmin());
		
		layout.addComponent(admin);
		
		admin.setEnabled(SecurityUtils.hasRole("ROLE_ADMIN"));
		
		if(VaadinSession.getCurrent().getAttribute("restaurante") == null) {
 			borrar.setEnabled(false);
 			finalizar.setEnabled(false);
 			mesa.setEnabled(false);
 			cliente.setEnabled(false);
 			llevar.setEnabled(false);
 			cocina.setEnabled(false);
 			recuperar.setEnabled(false);
 			nuevo.setEnabled(false);
 			Notification.show("Debes seleccionar un restaurante", "Botón admin",Notification.Type.WARNING_MESSAGE);
		}

		return layout;
	}

	private void borrarLineaVenta() {
		if (grid.getSelectedItems().size() > 0) {
			System.out.println(grid.getSelectedItems().size());
			LineaVenta linea = grid.getSelectedItems().iterator().next();
			lineasVenta.remove(linea);
			total -= linea.getSubtotal();
			textTotal.setValue("Total: " + String.format("%.2f €", total));
			grid.setItems(lineasVenta);
		}
	}
	
	private Window cobrarView() {
	    final Window window = new Window("Pago");
	    // window.setWidth(800, Unit.PIXELS);
	    window.setResizable(false);
	    window.setDraggable(false);
	    window.setModal(true);
	    window.center();

	    Label texto = new Label("El precio total es de " + String.format("%.2f €", total)); 
	    texto.addStyleName(ValoTheme.LABEL_H2);
	    texto.addStyleName(ValoTheme.LABEL_COLORED);
	    Button tarjeta = new Button("Con tarjeta");
	    tarjeta.setWidth("100pt");
	    tarjeta.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
	    Button efectivo = new Button("Con efectivo");
	    efectivo.setWidth("100pt");
	    efectivo.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
	    TextField cantidad = new TextField("Dinero abonado por el cliente");
	    Button finVenta = new Button("Finalizar Venta");
	    finVenta.setWidth("100pt");
	    finVenta.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
	    cantidad.setVisible(false);
	    
	    VerticalLayout efectivoLayout = new VerticalLayout(efectivo);
	    VerticalLayout tarjetaLayout = new VerticalLayout(tarjeta);
	    HorizontalLayout pagos = new HorizontalLayout(tarjetaLayout, efectivoLayout);
	    VerticalLayout main = new VerticalLayout(texto, pagos, cantidad, finVenta);
	    main.setComponentAlignment(texto, Alignment.MIDDLE_CENTER);
	    main.setComponentAlignment(cantidad, Alignment.MIDDLE_CENTER);
	    main.setComponentAlignment(finVenta, Alignment.MIDDLE_CENTER);
	    
	    finVenta.setVisible(false);
	    tarjeta.addClickListener(e -> {
	      efectivo.setEnabled(false);
	      finVenta.setEnabled(true);
	      //Notification.show("Validando...", "",Notification.Type.TRAY_NOTIFICATION);
	      Notification.show("Pago realizado correctamente", "",Notification.Type.TRAY_NOTIFICATION);
	      window.close();
	    });
	    efectivo.addClickListener(e -> {
	      cantidad.setVisible(true);
	      tarjeta.setEnabled(false);  
	      finVenta.setVisible(true);
	    });
	    
	    finVenta.addClickListener(e -> {
	      float vuelta = Integer.parseInt(cantidad.getValue()) - total;
	      Notification.show("La vuelta es de " + String.format("%.2f €", vuelta) + " €." , "", Notification.Type.ERROR_MESSAGE);
	      Notification.show("Pago realizado correctamente", "",Notification.Type.TRAY_NOTIFICATION);
	      window.close();
	    });
	    
	    window.setContent(main);

	    return window;
	  }

	private void finalizarVenta() {
		if((numMesa > 0 || idCliente != null || llevar.getValue() == true) && (!lineasVenta.isEmpty())) {
			
			// Compruebo que no quede ningun producto por procesar
			enviarCocina();
			
			SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
			String fecha = df.format(new Date());
			
			Venta venta = new Venta();
			if (numMesa > 0)
				venta.setMesa(mesaRepository.findOne(Integer.toUnsignedLong(numMesa)));
			if (idCliente != null)
				venta.setCliente(clienteRepository.findOne(idCliente));
				
			venta.setAbierta(false);
			venta.setFecha(fecha);
			venta.setPrecio(total);
			venta.setProductos(lineasVenta);
			venta.setRestaurante((Restaurante) VaadinSession.getCurrent().getAttribute("restaurante"));
			getUI().getUI().addWindow(cobrarView());
			ventaRepository.save(venta);
			
			// Imprimir Ticket
			new TicketPDF(venta);
			UI.getCurrent().getNavigator().navigateTo("TPV");
			//MainScreen.Menu();//AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
		} else if (numMesa == 0 && idCliente == null && llevar.getValue() == false) {
			Notification error = new Notification("¡Mesa o Cliente incorrecto!");
			error.setStyleName(ValoTheme.NOTIFICATION_ERROR);
			error.setDelayMsec(800);
			error.show(Page.getCurrent());
		} else if (lineasVenta.isEmpty()) {
			Notification error = new Notification("¡No hay productos!");
			error.setStyleName(ValoTheme.NOTIFICATION_ERROR);
			error.setDelayMsec(800);
			error.show(Page.getCurrent());
		}
	}

	private void setMesa() {
		int num = 0;
		if (current > 0) {
			num = (int) current;
			current = 0.0f;
			display.setValue("0.0");
			if (mesaRepository.exists(Integer.toUnsignedLong(num))) {
				numMesa = num;
				textMesa.setValue("Mesa: " + String.format("%02d", numMesa));
				idCliente = null;
				textCliente.setValue("Cliente: ");
				llevar.setValue(false);
			} else {
				Notification error = new Notification("Mesa incorrecta!");
				error.setStyleName(ValoTheme.NOTIFICATION_ERROR);
				error.setDelayMsec(800);
				error.show(Page.getCurrent());
			}
		} else {
			getUI().getUI().addWindow(mesasView());
		}
	}

	private Window mesasView() {
		final Window window = new Window("Mesas");

		window.setHeight("60%");
		window.setResizable(false);
		window.setDraggable(false);
		window.setModal(true);
		window.center();
		
		VerticalLayout vlayout = new VerticalLayout();

		GridLayout glayout = new GridLayout();
		glayout.setMargin(true);
		glayout.setSpacing(true);
		glayout.setColumns(3);
		
		String zona = "";
		int cont = 0;
		for (Mesa mesa : mesaRepository.findAllOrderByZona()) {
			cont++;
			if (zona.compareToIgnoreCase(mesa.getZona()) != 0) {
				if (cont != 1)
					vlayout.addComponent(glayout);
				
				glayout = new GridLayout();
				glayout.setMargin(true);
				glayout.setSpacing(true);
				glayout.setColumns(3);
				
				zona = mesa.getZona();
				Label labelZona = new Label(zona);
				
				glayout.addComponent(labelZona, 0, 0, 2, 0);
			}
			Button button = new Button(mesa.getNumero());
			button.setWidth("80pt");
			button.setHeight("80pt");
			button.addClickListener(e -> {
				numMesa = Integer.parseInt(mesa.getNumero());
				textMesa.setValue("Mesa: " + String.format("%02d", numMesa));
				idCliente = null;
				textCliente.setValue("Cliente: ");
				window.close();
			});
			
			glayout.addComponent(button);
		}
		vlayout.addComponent(glayout);
		window.setContent(vlayout);

		return window;
	}

	private void setCliente() {
		getUI().getUI().addWindow(clienteView());
	}

	private Window clienteView() {
		final Window window = new Window("Cliente");
		// window.setWidth(800, Unit.PIXELS);
		window.setResizable(false);
		window.setDraggable(false);
		window.setModal(true);
		window.center();

		Grid<Cliente> gridCli = new Grid<>(Cliente.class);
		
		gridCli.setColumns("id", "nombre", "apellidos", "direccion", "telefono");
		gridCli.setItems(clienteRepository.findByRestaurante(VaadinSession.getCurrent().getAttribute("restaurante")));
		gridCli.asSingleSelect().addValueChangeListener(e -> {
			idCliente = e.getValue().getId();
			textCliente.setValue("Cliente: " + clienteRepository.findOne(idCliente).getNombre());
			numMesa = 0;
			textMesa.setValue("Mesa: ");
			llevar.setValue(false);
			window.close();
		});
		
		window.setContent(gridCli);

		return window;
	}

	private void enviarCocina() {
		List<LineaVenta> lineas = new ArrayList<>();
		for(LineaVenta lv : lineasVenta) {
			if(!lv.isEnCocina()) {
				lv.setEnCocina(true);
				lineas.add(lv);
			}
		}
		if(!lineas.isEmpty())
			new TicketPDF(lineas);
	}
	
	private void setAdmin() {
		getUI().getUI().addWindow(adminView());
	}
	
	private Window adminView() {
		final Window window = new Window("Restaurantes");
		// window.setWidth(800, Unit.PIXELS);
		window.setResizable(false);
		window.setDraggable(false);
		window.setModal(true);
		window.center();

		Grid<Restaurante> gridCli = new Grid<>(Restaurante.class);
		
		gridCli.setColumns("id", "ciudad", "direccion");
		gridCli.setItems(restauranteRepository.findAll());
		gridCli.asSingleSelect().addValueChangeListener(e -> {
			VaadinSession.getCurrent().setAttribute("restaurante", e.getValue());
			window.close();
			UI.getCurrent().getNavigator().navigateTo("TPV");
		});
		
		window.setContent(gridCli);

		return window;
	}
	
	private void setNuevoPedido(){
		SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		String fecha = df.format(new Date());
		Venta venta = new Venta();
		if (numMesa > 0)
			venta.setMesa(mesaRepository.findOne(Integer.toUnsignedLong(numMesa)));
		if (idCliente != null)
			venta.setCliente(clienteRepository.findOne(idCliente));
		venta.setAbierta(true);
		venta.setFecha(fecha);
		venta.setPrecio(total);
		System.out.println("LineasVenta: " + lineasVenta.size());
		venta.setProductos(lineasVenta);
		venta.setRestaurante((Restaurante) VaadinSession.getCurrent().getAttribute("restaurante"));
		ventaRepository.save(venta);
		
		/*if (numMesa > 0){
			venta = new Venta(mesaRepository.findOne(Integer.toUnsignedLong(numMesa)), fecha, total, lineasVenta);
		}else if (idCliente != null){
			venta = new Venta(clienteRepository.findOne(idCliente), fecha, total, lineasVenta);}
		else
			venta = new Venta(fecha, total, lineasVenta);
		venta.setRestaurante((Restaurante) VaadinSession.getCurrent().getAttribute("restaurante"));
		ventaRepository.save(venta);*/
		UI.getCurrent().getNavigator().navigateTo("TPV");
	}
	 
	private void setRecuperar() {
	    getUI().getUI().addWindow(ventasView());
	   }
	   
	private Window ventasView() {
	    final Window window = new Window("Ventas");
	    window.setWidth(500, Unit.PIXELS);
	    window.setResizable(false);
	    window.setDraggable(false);
	    window.setModal(true);
	    window.center();

	    Grid<Venta> gridV = new Grid<>(Venta.class);
	    
	    gridV.setColumns("fecha", "precio", "mesa", "cliente", "abierta");
	    gridV.setItems(ventaRepository.findByAbiertaTrue());
	    gridV.asSingleSelect().addValueChangeListener(e -> {
	    	if(e.getValue().getCliente() != null ){
	    		idCliente = e.getValue().getCliente().getId();
	    		textCliente.setValue("Cliente: " + clienteRepository.findOne(idCliente).getNombre());
	    	}
		    
		    if(e.getValue().getMesa() != null){
		      numMesa = (int)(long)e.getValue().getMesa().getId();
		      textMesa.setValue("Mesa: " + String.format("%02d", numMesa));
		     }
		    List<LineaVenta> aux = e.getValue().getProductos();
			for(LineaVenta v : aux){
				v.setId(null);
			}
		    ventaRepository.delete(e.getValue().getId());
		    textTotal.setValue("Total: " + String.format("%.2f €", e.getValue().getPrecio()));
		    total = e.getValue().getPrecio();
		    lineasVenta = aux;
		    grid.setItems(lineasVenta);
		    //BORRAR DE LA BD
		    
		    window.close();
		    // UI.getCurrent().getNavigator().navigateTo("TPV");
		     
	    });
	    
	    window.setContent(gridV);

	    return window;
	}

	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub
		
	}
}
