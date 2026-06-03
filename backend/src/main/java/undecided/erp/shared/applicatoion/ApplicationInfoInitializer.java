package undecided.erp.shared.applicatoion;

import undecided.erp.common.application.ApplicationInfo;

import java.util.concurrent.atomic.AtomicReference;

/**
 * ApplicationInfoInitializerクラスは、アプリケーション名やサーバーポートなどのアプリケーション情報を初期化するために使用されます。
 * <p>
 * このクラスはApplicationInfoクラスを拡張しています。
 */
public class ApplicationInfoInitializer extends ApplicationInfo {

    private static final AtomicReference<String> applicationName = new AtomicReference<>();
    private static final AtomicReference<Long> port = new AtomicReference<>();

    /**
     * ApplicationInfoInitializerクラスのコンストラクタ。 アプリケーション名とポート番号を初期化します。
     *
     * @param applicationName アプリケーションの名前を設定します。
     * @param port            サーバーが使用するポート番号を設定します。
     */
    public ApplicationInfoInitializer(String applicationName, Long port) {
        super();
        ApplicationInfoInitializer.applicationName.set(applicationName);
        ApplicationInfoInitializer.port.set(port);

    }

    /**
     * アプリケーション名とサーバーポートを使用して、アプリケーション情報を初期化します。 このメソッドを呼び出すことで、アプリケーションのグローバル情報が設定されます。
     *
     * @param applicationName アプリケーションの名前。初期化に必要です。
     * @param port            サーバーが使用するポート番号。初期化に必要です。
     */
    public static void initialize(String applicationName, Long port) {
        ApplicationInfoInitializer instance = new ApplicationInfoInitializer(applicationName, port);
        new ApplicationInfo(instance);
    }

    /**
     * アプリケーションの名前を返します。
     *
     * @return アプリケーションの名前
     */
    @Override
    public String applicationName() {
        return ApplicationInfoInitializer.applicationName.get();

    }


    /**
     * サーバーが使用するポートを返します。
     *
     * @return サーバーが使用しているポート番号
     */
    public long serverPort() {
        return ApplicationInfoInitializer.port.get();
    }


}
