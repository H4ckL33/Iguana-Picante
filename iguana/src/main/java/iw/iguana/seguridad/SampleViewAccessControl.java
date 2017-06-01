package iw.iguana.seguridad;

import org.springframework.stereotype.Component;

import com.vaadin.spring.access.ViewAccessControl;
import com.vaadin.ui.UI;

@Component
public class SampleViewAccessControl implements ViewAccessControl {

    @Override
    public boolean isAccessGranted(UI ui, String beanName) {
    	
    	System.out.println("COMPROBANDO " + beanName + " PARA USUARIO CON ROLES: "+ SecurityUtils.roles());

    	if(SecurityUtils.hasRole("ROLE_ADMIN")){
    		return true;
    	} else if (beanName.equals("principalView")) {
            return true;
        } else if (beanName.equals("clienteView")) {
            return SecurityUtils.hasRole("ROLE_USER") || SecurityUtils.hasRole("ROLE_MANAGER");
        } else if (beanName.equals("empleadoView")) {
            return SecurityUtils.hasRole("ROLE_MANAGER");
        } else if (beanName.equals("familiaProductoView")){
            return SecurityUtils.hasRole("ROLE_MANAGER");
        } else if (beanName.equals("mesaView")) {
            return SecurityUtils.hasRole("ROLE_MANAGER");
        } else if (beanName.equals("TPVScreen")) {
        	return SecurityUtils.hasRole("ROLE_USER") || SecurityUtils.hasRole("ROLE_MANAGER");
        } else if (beanName.equals("ventaView")) {
        	return SecurityUtils.hasRole("ROLE_USER") || SecurityUtils.hasRole("ROLE_MANAGER");
        } else if (beanName.equals("productoSimpleView")) {
        	return SecurityUtils.hasRole("ROLE_MANAGER");
        } else if (beanName.equals("productoCompuestoView")) {
        	return SecurityUtils.hasRole("ROLE_USER") || SecurityUtils.hasRole("ROLE_MANAGER");
        } else if (beanName.equals("restauranteView")) {
        	return SecurityUtils.hasRole("ROLE_MANAGER");
        } else if (beanName.equals("cajaView")) {
        	return SecurityUtils.hasRole("ROLE_MANAGER");
        }  else {
        	return false;
        }
    }
}