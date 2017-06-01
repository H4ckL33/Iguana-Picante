package iw.iguana.vista;

import java.util.logging.Level;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;

import com.vaadin.annotations.Theme;
import com.vaadin.server.ClientConnector;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinService;
import com.vaadin.server.VaadinSession;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.ui.ConnectorTracker;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

import iw.iguana.dao.EmpleadoRepository;
import iw.iguana.modelo.Empleado;
import iw.iguana.seguridad.SecurityUtils;

@SpringUI
public class VaadinUI extends UI {

	@Autowired
	SpringViewProvider viewProvider;

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
    MainScreen mainScreen;
	
	@Autowired
	EmpleadoRepository repo;
	
	public VaadinUI(EmpleadoRepository repo) {
		this.repo = repo;
	}
	
	@Override
	protected void init(VaadinRequest request) {

	   	this.getUI().getNavigator().setErrorView(ErrorView.class);
		viewProvider.setAccessDeniedViewClass(AccessDeniedView.class);
		this.getUI().getConnectorTracker();
	
		/*private void updateContent() {
			Empleado user = (Empleado) VaadinSession.getCurrent().getAttribute(
					Empleado.class.getName());
	        if (user != null && "admin".equals(user.getRol())) {
	            // Authenticated user
	            setContent(new MainView());
	            removeStyleName("loginview");
	            getNavigator().navigateTo(getNavigator().getState());
	        } else {
	            setContent(new LoginView());
	            addStyleName("loginview");
	        }
	    }*/
		
		/*final VerticalLayout root = new VerticalLayout();
        root.setSizeFull();
        root.setMargin(true);
        root.setSpacing(true);
        setContent(root);*/
		//setSizeFull();
		
		if (SecurityUtils.isLoggedIn()) {
			showMainScreen();
			//showTPVScreen();
		} else {
			showLoginScreen();
			//showMainScreen();
		}
	}

	private void showLoginScreen() {
		setContent(new LoginScreen(this::login));
	}
	
	/*private void showTPVScreen() {
		setContent(new TPVScreen(this::mostrarMenu));
	}*/

	private void showMainScreen() {
		setContent(mainScreen);
	}
	
	private boolean login(String username, String password) {
		try {
			Authentication token = authenticationManager
					.authenticate(new UsernamePasswordAuthenticationToken(username, password));
			// Reinitialize the session to protect against session fixation
			// attacks. This does not work with websocket communication.
			VaadinService.reinitializeSession(VaadinService.getCurrentRequest());
			//currentUser = username;
			SecurityContextHolder.getContext().setAuthentication(token);
			
			Empleado e = repo.findByUsername(username);
			
			VaadinSession.getCurrent().setAttribute("username", e.getUsername());
			VaadinSession.getCurrent().setAttribute("nombre", e.getNombre());
			VaadinSession.getCurrent().setAttribute("restaurante", e.getRestaurante());
			VaadinSession.getCurrent().setAttribute("rol", e.getRol());
			
            Notification.show("Bienvenido " + VaadinSession.getCurrent().getAttribute("nombre"), "Has accedido como " + 
            						VaadinSession.getCurrent().getAttribute("rol") , Notification.Type.TRAY_NOTIFICATION);
			
			// Show the main UI
            showMainScreen();
			return true;
		} catch (AuthenticationException ex) {
			return false;
		}
	}
	
	public void mostrarMenu() {
		showMainScreen();
	}
}