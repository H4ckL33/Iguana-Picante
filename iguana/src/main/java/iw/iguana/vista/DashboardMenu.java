package iw.iguana.vista;

import java.util.Collection;

import javax.persistence.Table;

import com.vaadin.event.dd.DragAndDropEvent;
import com.vaadin.event.dd.DropHandler;
import com.vaadin.event.dd.acceptcriteria.AcceptCriterion;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinSession;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import iw.iguana.dao.EmpleadoRepository;
import iw.iguana.modelo.Empleado;
import iw.iguana.modelo.Restaurante;

/**
 * A responsive menu component providing user information and the controls for
 * primary navigation between the views.
 */
@SuppressWarnings({ "serial", "unchecked" })
public final class DashboardMenu extends CustomComponent {

    public static final String ID = "dashboard-menu";
    public static final String REPORTS_BADGE_ID = "dashboard-menu-reports-badge";
    public static final String NOTIFICATIONS_BADGE_ID = "dashboard-menu-notifications-badge";
    private static final String STYLE_VISIBLE = "valo-menu-visible";
    private Label notificationsBadge;
    private Label reportsBadge;
    private MenuItem settingsItem;

    public DashboardMenu() {
        setPrimaryStyleName("valo-menu");
        setId(ID);
        //setSizeUndefined();
        setSizeFull();

        setCompositionRoot(buildContent());
    }

	private Component buildContent() {
        final CssLayout menuContent = new CssLayout();
        //menuContent.addStyleName("sidebar");
        menuContent.addStyleName(ValoTheme.MENUBAR_BORDERLESS);
        //menuContent.setWidth(220.0f, Unit.PIXELS);
        //menuContent.setHeight(670.0f, Unit.PIXELS);
        menuContent.setWidth("100%"); menuContent.setHeight("100%");
        //menuContent.setSizeUndefined();

        menuContent.addComponent(buildTitle());
        menuContent.addComponent(buildMenuItems());

        return menuContent;
    }

    private Component buildTitle() {
    	Label icon = new Label("");
    	icon.setIcon(VaadinIcons.COGS);
        Label logo = new Label("Menu de configuración");
        logo.setSizeUndefined();
        HorizontalLayout logoWrapper = new HorizontalLayout(icon,logo);
        logoWrapper.setComponentAlignment(logo, Alignment.MIDDLE_CENTER);
        logoWrapper.addStyleName("valo-menu-title");
        return logoWrapper;
    }
    
    private Component buildMenuItems() {
        CssLayout menuItemsLayout = new CssLayout();
        menuItemsLayout.addStyleName("valo-menuitems");
        
        Button logoutButton = new Button("Logout", event -> {getUI().getPage().reload(); getSession().close(); });
		logoutButton.setStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
		logoutButton.setIcon(VaadinIcons.SIGN_OUT);
		
		Label section;
		
		section = new Label("&nbsp&nbsp General",ContentMode.HTML);
        section.addStyleName(ValoTheme.LABEL_H2);
        section.addStyleName(ValoTheme.LABEL_COLORED);
        menuItemsLayout.addComponent(section);
        menuItemsLayout.addComponent(new ValoMenuItemButton("Restaurantes", VaadinIcons.ABACUS, RestauranteView.VIEW_NAME));
		section = new Label("&nbsp&nbsp Personal",ContentMode.HTML);
        section.addStyleName(ValoTheme.LABEL_H2);
        section.addStyleName(ValoTheme.LABEL_COLORED);
        menuItemsLayout.addComponent(section);
		menuItemsLayout.addComponent(new ValoMenuItemButton("Empleados", VaadinIcons.USER_STAR, EmpleadoView.VIEW_NAME));
		menuItemsLayout.addComponent(new ValoMenuItemButton("Clientes", VaadinIcons.USER, ClienteView.VIEW_NAME));
		section = new Label("&nbsp&nbsp Producción",ContentMode.HTML);
        section.addStyleName(ValoTheme.LABEL_H2);
        section.addStyleName(ValoTheme.LABEL_COLORED);
        menuItemsLayout.addComponent(section);
        menuItemsLayout.addComponent(new ValoMenuItemButton("Ventas", VaadinIcons.BROWSER, VentaView.VIEW_NAME));
        menuItemsLayout.addComponent(new ValoMenuItemButton("Productos", VaadinIcons.COINS, ProductoSimpleView.VIEW_NAME));
        menuItemsLayout.addComponent(new ValoMenuItemButton("Productos personalizados", VaadinIcons.COINS, ProductoCompuestoView.VIEW_NAME));
        menuItemsLayout.addComponent(new ValoMenuItemButton("Familias de producto", VaadinIcons.COIN_PILES, FamiliaProductoView.VIEW_NAME));
		menuItemsLayout.addComponent(new ValoMenuItemButton("Mesas", VaadinIcons.SQUARE_SHADOW, MesaView.VIEW_NAME));
		menuItemsLayout.addComponent(new ValoMenuItemButton("Caja", VaadinIcons.CALC_BOOK, CajaView.VIEW_NAME));
		section = new Label("&nbsp&nbsp TPV",ContentMode.HTML);
        section.addStyleName(ValoTheme.LABEL_H2);
        section.addStyleName(ValoTheme.LABEL_COLORED);
        menuItemsLayout.addComponent(section);
        menuItemsLayout.addComponent(new ValoMenuItemButton("Vista principal", VaadinIcons.CASH, TPVScreen.VIEW_NAME));
		
		section = new Label("<br>",ContentMode.HTML);
        menuItemsLayout.addComponent(section);
        
		menuItemsLayout.addComponent(logoutButton);
		
		section = new Label("<br>",ContentMode.HTML);
        menuItemsLayout.addComponent(section);
        
		return menuItemsLayout;
    }

    
    public final class ValoMenuItemButton extends Button {

        private static final String STYLE_SELECTED = "selected";

        private final String name, viewName;

		private VaadinIcons icon;

        public ValoMenuItemButton(final String name, VaadinIcons icon, 
        		final String viewName) {
        	this.name = name;
        	this.icon = icon;
            this.viewName = viewName;
            setPrimaryStyleName("valo-menu-item");
            setIcon(icon);
            setCaption(name);
            addClickListener(new ClickListener() {
            	@Override
                public void buttonClick(final ClickEvent event) {
                   	UI.getCurrent().getNavigator().navigateTo(viewName);
                }
            });
        }
    }
}
