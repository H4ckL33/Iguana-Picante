package iw.iguana.vista;

import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.Page;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.Position;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class LoginScreen extends VerticalLayout {
  
	public LoginScreen(LoginCallback callback) {
		setSizeFull();

        Component loginForm = buildLoginForm(callback);
        addComponent(loginForm);
        setComponentAlignment(loginForm, Alignment.MIDDLE_CENTER);
    }

    @FunctionalInterface
    public interface LoginCallback {

        boolean login(String username, String password);
    }

	
    /**
  	 * 
  	 */
  	private static final long serialVersionUID = 5304492736395275231L;
  	
  	private Component buildLoginForm(LoginCallback callback) {
        final FormLayout loginPanel = new FormLayout();
        loginPanel.setSizeUndefined();
        loginPanel.setSpacing(true);
        Responsive.makeResponsive(loginPanel);
        loginPanel.addStyleName("login-panel");

        loginPanel.addComponent(buildLabels());
        loginPanel.addComponent(buildFields(callback));
        return loginPanel;
    }
  	
  	 private Component buildFields(LoginCallback callback) {
         HorizontalLayout fields = new HorizontalLayout();
         fields.setSpacing(true);
         fields.addStyleName("fields");

         final TextField username = new TextField("Usuario");

         final PasswordField password = new PasswordField("Contraseña");

         final Button signin = new Button("Entrar");
         signin.addStyleName(ValoTheme.BUTTON_PRIMARY);
         signin.setClickShortcut(KeyCode.ENTER);
         signin.focus();

         fields.addComponents(username, password, signin);
         fields.setComponentAlignment(signin, Alignment.BOTTOM_LEFT);

         signin.addClickListener(new ClickListener() {
             @Override
             public void buttonClick(final ClickEvent event) {
             	String pword = password.getValue();
                 password.setValue("");
                 //Notification.show("This UI is "+ UI.getCurrent().getClass().getSimpleName());
                 if (!callback.login(username.getValue(), pword)) {
                     Notification.show("Login fallido");
                     username.focus();
                 }
             }
         });
         return fields;
     }

  	private Component buildLabels() {
        CssLayout labels = new CssLayout();
        labels.addStyleName("labels");

        Label welcome = new Label("Login");
        welcome.setSizeUndefined();
        welcome.addStyleName(ValoTheme.LABEL_H4);
        welcome.addStyleName(ValoTheme.LABEL_COLORED);
        labels.addComponent(welcome);

        return labels;
    }
}
