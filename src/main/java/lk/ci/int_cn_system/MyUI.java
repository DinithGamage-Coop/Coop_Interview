package lk.ci.int_cn_system;

import javax.servlet.annotation.WebServlet;

import lk.ci.int_cn_system.UI.Auth.Login_SUB;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 * This UI is the application entry point. A UI may either represent a browser window 
 * (or tab) or some part of a html page where a Vaadin application is embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is intended to be 
 * overridden to add component to the user interface and initialize non-component functionality.
 */
@Theme("mytheme")
public class MyUI extends UI {

    @Override
    protected void init(VaadinRequest vaadinRequest) {
//        final VerticalLayout layout = new VerticalLayout();
//        
//        final TextField name = new TextField();
//        name.setCaption("Type your name here:");
//
//        Button button = new Button("Click Me");
//        button.addClickListener( e -> {
//            layout.addComponent(new Label("Thanks " + name.getValue() 
//                    + ", it works!"));
//        });
//        
//        layout.addComponents(name, button);
//        layout.setMargin(true);
//        layout.setSpacing(true);
//        
//        setContent(layout);
    	
       	Page.getCurrent().setTitle("Co-operative Insurance");		
		 
    		Login_SUB lsb = new Login_SUB();
    		Window ww = new Window();

    		ww.center();
    		ww.setClosable(false);
    		ww.setModal(true);
    		ww.setResponsive(true);

    		ww.setResizable(false);
    		ww.setWidth("400px");
    		ww.setHeight("360px");

    		lsb.getWindow(ww);
    		ww.setContent(lsb);
    		UI.getCurrent().addWindow(ww);
    }

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }
}
