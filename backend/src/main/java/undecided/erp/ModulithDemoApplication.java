package undecided.erp;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.context.event.EventListener;
import undecided.erp.shared.applicatoion.ApplicationInfoInitializer;

@SpringBootApplication
public class ModulithDemoApplication {

  /**
   * Starts the Modulith Demo application.
   *
   * @param args the command line arguments
   */
  public static void main(String[] args) {
    SpringApplication.run(ModulithDemoApplication.class, args);
  }

  @Value("spring.application.name")
  private String applicationNane;
  @Value("server.port")
  private String serverPort;

  /**
   * @param ctxStartEvt このメソッドをトリガーする ContextStartedEvent
   * @see ApplicationInfoInitializer#initialize(String, Long)
   */
  @EventListener
  public void handleContextRefreshEvent(ContextStartedEvent ctxStartEvt) {
    ApplicationInfoInitializer.initialize(applicationNane, Long.valueOf(serverPort));

  }

}