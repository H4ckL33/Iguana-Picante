package iw.iguana.vista;

import java.io.File;

import javax.annotation.PostConstruct;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.navigator.ViewDisplay;
import com.vaadin.server.ClassResource;
import com.vaadin.server.FileResource;
import com.vaadin.server.Page;
import com.vaadin.server.Resource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinService;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.spring.annotation.SpringViewDisplay;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import iw.iguana.dao.EmpleadoRepository;
import iw.iguana.vista.DashboardMenu.ValoMenuItemButton;
import iw.iguana.vista.LoginScreen.LoginCallback;

@SpringViewDisplay
public class MainScreen extends VerticalLayout implements ViewDisplay {

	private Panel springViewDisplay;
	
	final VerticalLayout menu = new VerticalLayout();
	final VerticalLayout rest = new VerticalLayout();
	final VerticalLayout menuCompleto = new VerticalLayout(menu,rest);
	final VerticalLayout vista = new VerticalLayout();
	final HorizontalLayout vistacompleta = new HorizontalLayout(vista,menuCompleto);
	
	@Override
    public void attach() {
        super.attach();
        this.getUI().getNavigator().navigateTo("TPV");
    }
	
	@PostConstruct
	void init() {
		
		//final VerticalLayout root = new VerticalLayout();
		setSizeFull();
		//setWidth("100%"); 
		//setHeight(null);
		setMargin(false);
		setSpacing(false);

			menu.setWidth("100%"); 
			//menu.setHeight(null);
			menu.setMargin(false);
			menu.setSpacing(false);
			menu.addStyleName("mainview");
			menu.addComponent(new DashboardMenu());
			
			//menu.addComponent(new Image(null, imagen));
			
			/*Button verMenu = new Button("", event -> MainScreen.Menu());
			verMenu.setIcon(VaadinIcons.MENU);
			verMenu.addStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);*/
			
			//final VerticalLayout vista = new VerticalLayout();
			vista.setSizeFull();
			//vista.setWidth("100%"); 
			//vista.setHeight(null);
			vista.setMargin(false);
			vista.setSpacing(false);
	        springViewDisplay = new Panel();
			springViewDisplay.setSizeFull();
			vista.addComponent(springViewDisplay);
			//vista.setExpandRatio(springViewDisplay, 2.0f);
			
			menuCompleto.setSizeFull();
			menuCompleto.setMargin(false);
			menuCompleto.setSpacing(false);

			vistacompleta.setSizeFull();
			vistacompleta.setExpandRatio(vista, 1.0f);
			vistacompleta.setExpandRatio(menuCompleto, 0.2f);
			
			addComponent(vistacompleta);
			
	}

	private Button createNavigationButton(String caption, final String viewName) {
		Button button = new Button(caption);
		button.addStyleName(ValoTheme.BUTTON_SMALL);
		// If you didn't choose Java 8 when creating the project, convert this
		// to an anonymous listener class
		button.addClickListener(event -> getUI().getNavigator().navigateTo(viewName));
		return button;
	}
	
	
	@Override
	public void showView(View view) {
		springViewDisplay.setContent((Component) view);
	}

	private void logout() {
		getUI().getPage().reload();
		getSession().close();
	}

	public void Menu() {
		if(menu.isVisible()) {
			menu.setVisible(false);
			menuCompleto.setVisible(false);
		} else {
			menu.setVisible(true);
			menuCompleto.setVisible(true);
		}
	}
}
