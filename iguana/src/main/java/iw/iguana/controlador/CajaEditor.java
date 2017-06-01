package iw.iguana.controlador;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.vaadin.data.Binder;
import com.vaadin.data.converter.StringToFloatConverter;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.event.ShortcutAction;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.AbstractOrderedLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import iw.iguana.dao.CajaRepository;
import iw.iguana.dao.ClienteRepository;
import iw.iguana.dao.VentaRepository;
import iw.iguana.modelo.Caja;
import iw.iguana.modelo.Cliente;
import iw.iguana.modelo.Restaurante;
import iw.iguana.modelo.Venta;
import iw.iguana.vista.CajaView;

@SpringComponent
@ViewScope
public class CajaEditor extends VerticalLayout {

	private static final long serialVersionUID = 1L;

	private final CajaRepository repository;
	private final VentaRepository repoV;

	private Caja caja;

	/* Fields to edit properties in Customer entity */
	TextField impContabilizado = new TextField("ImporteContabilizado");

	/* Action buttons */
	Button save = new Button("Realizar cierre de caja", VaadinIcons.DOWNLOAD);
	Button cancel = new Button("Cancelar");
	CssLayout actions = new CssLayout(save, cancel);

	public Binder<Caja> binder = new Binder<>(Caja.class);

	@Autowired
	public CajaEditor(CajaRepository repository, VentaRepository repoV) {
		this.repository = repository;
		this.repoV=repoV;
		
		Label label = new Label("Formulario de Caja");
		label.addStyleName(ValoTheme.LABEL_H2);
		label.addStyleName(ValoTheme.LABEL_COLORED);
		
		HorizontalLayout abrir = new HorizontalLayout(impContabilizado);
		HorizontalLayout cerrar = new HorizontalLayout(impContabilizado);
		
		Label espacio = new Label("<br>",ContentMode.HTML);
		
		addComponents(label, abrir, cerrar, actions, espacio);
		
		binder.forField(impContabilizado).asRequired("Importe requerido")
		.withConverter(new StringToFloatConverter("Introduzca importe"))
		.bind(Caja::getImpContabilizado, Caja::setImpContabilizado);
		
		// Configure and style components
		setSpacing(true);
		actions.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
		save.setStyleName(ValoTheme.BUTTON_PRIMARY);
		save.setClickShortcut(ShortcutAction.KeyCode.ENTER);

		// wire action buttons to save, delete and reset
		save.addClickListener(e -> {
			if(binder.validate().isOk()){
				SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
				String fecha = df.format(new Date());
				caja.setFechaCierre(fecha);
				caja.setCajaCerrada(true);
				List<Venta> ventas = repoV.CalcularFechas(caja.getFechaApertura(), caja.getFechaCierre()/*"31-05-2017 02:12:20","31-05-2017 02:16:35"*/);
				float sum=0;
				for (Venta v : ventas) {
					sum+=v.getPrecio();
				}
				caja.setImpRecaudado(sum);
				caja.setVentas(ventas);
				repository.save(caja);
			}
		});
		cancel.addClickListener(e -> setVisible(false));
		setVisible(false);
	}

	public interface ChangeHandler {

		void onChange();
	}

	public final void editarCaja(Caja c) {
		if (c == null) {
			setVisible(false);
			return;
		}
		final boolean persisted = c.getId() != null;
		if (persisted) {
			caja = repository.findOne(c.getId());
		}
		else {
			caja = c;
		}

		binder.setBean(caja);

		setVisible(true);

		// A hack to ensure the whole form is visible
		save.focus();
		// Select all text in firstName field automatically
		//nombre.selectAll();
	}

	public void setChangeHandler(ChangeHandler h) {
		// ChangeHandler is notified when either save or delete
		// is clicked
		save.addClickListener(e -> {
			if(!binder.validate().isOk()){
				Notification.show("Formulario malformado", "",Notification.Type.ERROR_MESSAGE);
			}else{
				h.onChange();
			}
		});
		
		//Apertura.addClickListener(e -> h.onChange());
	}
	
	/*public static void listarCajas( Grid<Caja> grid, CajaRepository repo) {
		grid.setItems(repo.findAll());
	}*/
	public static void listarCajas (String filterText, String tipoFiltro, Grid<Caja> grid, CajaRepository repo) {
			if (StringUtils.isEmpty(filterText) && StringUtils.isEmpty(tipoFiltro)) {
				if(VaadinSession.getCurrent().getAttribute("restaurante") != null)
	 				grid.setItems(repo.findByRestaurante(VaadinSession.getCurrent().getAttribute("restaurante")));
	 			else
	 				Notification.show("Debes seleccionar un restaurante", "Botón admin en la vista principal",Notification.Type.WARNING_MESSAGE);
			}
			else switch(tipoFiltro){
				 case "fecha1":
		 			grid.setItems(repo.findByFechaAperturaContainingAndRestaurante(filterText, 
		 					VaadinSession.getCurrent().getAttribute("restaurante"))); 
		 			break;
				 case "fecha2":
			 			grid.setItems(repo.findByFechaCierreContaining(filterText)); 
			 			break;
			}
	}

	public void abrirCaja(Caja c) {
		
		caja = c;
		
		binder.setBean(caja);
		
		SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		String fecha = df.format(new Date());
		caja.setFechaApertura(fecha);
		caja.setCajaCerrada(false);
		caja.setRestaurante((Restaurante) VaadinSession.getCurrent().getAttribute("restaurante"));
		repository.save(caja);

		//setVisible(true);

		// A hack to ensure the whole form is visible
		//save.focus();
	}
}