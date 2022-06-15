package lk.ci.int_cn_system.Utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.vaadin.liveimageeditor.LiveImageEditor;

import com.vaadin.server.StreamResource;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Button;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Upload;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

//@Theme("valo")
//@Title("Live Image Editor add-on for Vaadin")
@SuppressWarnings("serial")
public class DemoUI extends VerticalLayout {

   // @WebServlet(value = "/*", asyncSupported = true)
  //  @VaadinServletConfiguration(productionMode = false, ui = DemoUI.class)
//	@WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
 //   @VaadinServletConfiguration(ui = MyUI.class, productionMode = true)
//    public static class Servlet extends VaadinServlet {
//    }

    private Upload upload = new Upload("Upload an image", this::receiveUpload);
    //private Label instructions = new Label();
    private LiveImageEditor imageEditor = new LiveImageEditor(this::receiveImage);
    //private Button send = new Button("Send", this::sendClicked);
    private Label result = new Label("Transformed image as received on the server:");
    private Image editedImage = new Image();

    private ByteArrayOutputStream outputStream;

   // @Override
    public DemoUI() {
//        instructions = new Label("Live Image Editor add-on for Vaadin");
//        instructions.addStyleName(ValoTheme.LABEL_H2);
//        instructions.addStyleName(ValoTheme.LABEL_COLORED);
//
//        instructions.setContentMode(ContentMode.HTML);
        //instructions.setWidth(600, Unit.PIXELS);

        upload.addSucceededListener(this::uploadSucceeded);

       imageEditor.setWidth("100%");
       imageEditor.setHeight("100%");
      //  imageEditor.setSizeFull();
        imageEditor.setBackgroundColor(120, 120, 120);

//        VerticalLayout layout = new VerticalLayout(title, upload, instructions, imageEditor, send, result, editedImage);
//        layout.setSizeUndefined();
//        layout.setMargin(true);
//        layout.setSpacing(true);
// setContent(layout);
        
    this.addComponents( upload, imageEditor, result, editedImage);
        setupUploadStep();
    }

    public void set4TO(byte[] ba) throws IOException{
    	//instructions.setValue(label);
    	imageEditor.setImage(ba);
        imageEditor.resetTransformations();
        setupEditingStep();
    }
    
    private OutputStream receiveUpload(String filename, String mimeType) {
        return outputStream = new ByteArrayOutputStream();
    }

    private void uploadSucceeded(Upload.SucceededEvent event) {
        imageEditor.setImage(outputStream.toByteArray());
        imageEditor.resetTransformations();
        setupEditingStep();
    }

    private void sendClicked(Button.ClickEvent event) {
        imageEditor.requestEditedImage();
    }

    private void receiveImage(InputStream inputStream) {
        StreamResource resource = new StreamResource(() -> inputStream, "edited-image-" + System.currentTimeMillis());
        editedImage.setSource(resource);
        setupFinalStep();
    }

    private void setupUploadStep() {
        upload.setVisible(true);
        //instructions.setVisible(false);
        imageEditor.setVisible(false);
        //send.setVisible(false);
        result.setVisible(false);
        editedImage.setVisible(false);
    }

    private void setupEditingStep() {
        upload.setVisible(false);
        //instructions.setVisible(true);
        imageEditor.setVisible(true);
     //   send.setVisible(true);
        result.setVisible(false);
        editedImage.setVisible(false);
    }

    private void setupFinalStep() {
        upload.setVisible(true);
        //instructions.setVisible(false);
        imageEditor.setVisible(false);
      //  send.setVisible(false);
        result.setVisible(true);
        editedImage.setVisible(true);
    }

}
