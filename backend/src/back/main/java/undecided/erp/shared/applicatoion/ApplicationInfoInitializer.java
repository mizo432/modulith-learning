package undecided.erp.shared.applicatoion;

import java.util.concurrent.atomic.AtomicReference;
import undecided.erp.common.application.ApplicationInfo;

/**
 * ApplicationInfoInitializerクラスは、アプリケーション名やサーバーポートなどのアプリケーション情報を初期化するために使用されます。
 * <p>
 * このクラスはApplicationInfoクラスを拡張しています。
 */
public class ApplicationInfoInitializer extends ApplicationInfo {

  private static final AtomicReference<String> applicationName = new AtomicReference<>();
  private static final AtomicReference<Long> port = new AtomicReference<>();

  public ApplicationInfoInitializer(String applicationName, Long port) {
    super();
    ApplicationInfoInitializer.applicationName.set(applicationName);
    ApplicationInfoInitializer.port.set(port);

  }

  public static void initialize(String applicationName, Long port) {
    ApplicationInfoInitializer instance = new ApplicationInfoInitializer(applicationName, port);
    new ApplicationInfo(instance);
  }

  public String applicationName() {
    return ApplicationInfoInitializer.applicationName.get();

  }


  public long serverPort() {
    return ApplicationInfoInitializer.port.get();
  }


}
