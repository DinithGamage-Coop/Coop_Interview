package lk.ci.int_cn_system.UI.Auth;
 

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.communication.PushMode;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;

public class EnablePushFromBackgroundThread extends UI {

   private int c = 0;

   private Label l = new Label();

   private final TimerTask task = new TimerTask() {

      @Override
      public void run() {
         access(new Runnable() {
            @Override
            public void run() {
               l.setValue("" + c++);
               if (c == 10) {
                  getPushConfiguration().setPushMode(PushMode.AUTOMATIC);
                  setPollInterval(-1);
                  Notification.show("Push enabled!");
               }
            }
         });
      }
   };

   @Override
   protected void init(VaadinRequest request) {
      setPollInterval(8000);
      new Timer(true).scheduleAtFixedRate(task, new Date(), 1000);
      setContent(l);
   }
}