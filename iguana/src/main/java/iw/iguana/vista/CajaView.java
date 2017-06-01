package iw.iguana.vista;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

import iw.iguana.controlador.CajaEditor;
import iw.iguana.controlador.CajaEditor.ChangeHandler;
import iw.iguana.dao.CajaRepository;
import iw.iguana.dao.VentaRepository;
import iw.iguana.modelo.Caja;
import iw.iguana.modelo.Mesa;
import iw.iguana.modelo.Venta;
import iw.iguana.seguridad.SecurityUtils;

@SpringView(name = CajaView.VIEW_NAME)
public class CajaView extends VerticalLayout implements View {
 
	private static final long serialVersionUID = 1L;

	public static final String VIEW_NAME = "caja";

	private final CajaRepository repo;
	
	private final VentaRepository repoV;

	private final CajaEditor editor;

	final Grid<Caja> grid;

	final TextField filter, filter2;
	
	private final Button Apertura;
	
	private final Button Cierre;

	@Autowired
	public CajaView(CajaRepository repo, CajaEditor editor, VentaRepository repoV) {
		this.repo = repo;
		this.repoV = repoV;
		this.editor = editor;
		this.grid = new Grid<>(Caja.class);
		this.filter = new TextField();
		this.filter2 = new TextField();
		this.Apertura = new Button("Apertura Caja", VaadinIcons.LEVEL_DOWN);
		this.Cierre = new Button("Cierre Caja", VaadinIcons.LEVEL_UP);
	}
 	
 	@PostConstruct
	void init() {
		HorizontalLayout actions = new HorizontalLayout(Apertura, Cierre, filter, filter2);
		actions.setMargin(false);
		
		VerticalLayout mainLayout = new VerticalLayout(editor,actions, grid);
		
		addComponent(mainLayout);

		grid.setHeight(500, Unit.PIXELS);
		grid.setWidth(1070, Unit.PIXELS);
		grid.setColumns("id", "fechaApertura","fechaCierre","impRecaudado","impContabilizado", "cajaCerrada");
		
		if(VaadinSession.getCurrent().getAttribute("restaurante") == null)
 			Apertura.setEnabled(false);

		filter.setPlaceholder("Filtrar por fecha de apertura");
		filter.setPlaceholder("Filtrar por fecha de cierre");

		//CajaEditor.listarCajas(grid, repo);
		// Para activar el boton si aun en la fecha de hoy no se ha hecho el cierre de caja
		if(repo.findByCajaCerrada(false) == null){
					Cierre.setEnabled(false);
					Apertura.addClickListener(e -> {
					//SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
					//String fecha = df.format(new Date());
					editor.abrirCaja(new Caja());
					//editor.editarCaja(repo.findByCajaCerrada(false));
					Apertura.setEnabled(false);
					Cierre.setEnabled(true);
					UI.getCurrent().getNavigator().navigateTo("caja");
				});
				//CajaEditor.listarCajas(grid, repo);
		}
		else{
			Apertura.setEnabled(false);
			Cierre.addClickListener(e -> {
				//SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
				//String fecha = df.format(new Date());
				//editor.cerrarCaja(fecha);
				/* 	ESTO PA CRISTOBAAAAAAAAAAAAAAAAAAAAAAAAAAAAAL*/
				if(repoV.findByAbiertaTrue().isEmpty()){
					editor.editarCaja(repo.findByCajaCerrada(false));
					Cierre.setEnabled(false);
				}
				else
					Notification.show("No se puede cerrar la caja con ventas abiertas o sin ningún pedido",Notification.Type.ERROR_MESSAGE);
				
			});
		}
		// Listen changes made by the editor, refresh data from backend
		editor.setChangeHandler(() -> {
			if(editor.binder.validate().isOk()){
				editor.setVisible(false);
				Apertura.setEnabled(true);
				UI.getCurrent().getNavigator().navigateTo("caja");
			}else{
				Notification.show("Formulario malformado", "",Notification.Type.ERROR_MESSAGE);
			}
			CajaEditor.listarCajas(null, null, grid, repo);
		});
		
		//Filtrado
		filter.setValueChangeMode(ValueChangeMode.LAZY);
		filter.addValueChangeListener(e -> CajaEditor.listarCajas(e.getValue().toString(), "fecha1", grid, repo));
		filter.setValueChangeMode(ValueChangeMode.LAZY);
		filter.addValueChangeListener(e -> CajaEditor.listarCajas(e.getValue().toString(), "fecha2", grid, repo));

		// Initialize listing
		CajaEditor.listarCajas(null, null, grid, repo);
	}

 	
 	@Override
 	public void enter(ViewChangeEvent event) {
 		// the view is constructed in the init() method()
 	}
}