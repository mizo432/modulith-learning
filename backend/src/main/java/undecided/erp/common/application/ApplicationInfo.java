package undecided.erp.common.application;

import java.util.concurrent.atomic.AtomicReference;


/**
 * ApplicationInfoクラスは、アプリケーション名やサーバーポートなどのアプリケーション情報を保存および取得するために使用されます。
 */
public class ApplicationInfo {

  private final static AtomicReference<ApplicationInfo> applicationInfo =
      new AtomicReference<>(new ApplicationInfo());

  private static final long DEFAULT_PORT = 8080;
  private static final String DEFAULT_APPLICATION_NAME = "DEFAULT_APPLICATION_NAME";

  protected ApplicationInfo() {

  }

  public ApplicationInfo(ApplicationInfo applicationInfo) {
    ApplicationInfo.applicationInfo.set(applicationInfo);

  }

  public static String name() {
    return ApplicationInfo.applicationInfo.get().applicationName();
  }

  public static long port() {
    return ApplicationInfo.applicationInfo.get().serverPort();
  }

  public static void clear() {
    ApplicationInfo.applicationInfo.set(new ApplicationInfo());

  }

  protected String applicationName() {
    return DEFAULT_APPLICATION_NAME;
  }


  protected long serverPort() {
    return DEFAULT_PORT;
  }

}
