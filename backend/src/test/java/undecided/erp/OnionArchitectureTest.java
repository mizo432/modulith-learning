package undecided.erp;

import static com.tngtech.archunit.library.Architectures.onionArchitecture;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

@AnalyzeClasses(packages = "undecided.erp")
public class OnionArchitectureTest {

  static final String APPLICATION_ROOT_PACKAGE = "undecided.erp";

  /**
   * addressMgmtRule変数は、ArchRuleオブジェクトを表し、ERPシステムのundecided.erp.addressMgmtパッケージに対するオニオンアーキテクチャの制約を定義します。
   * <p>
   * ArchRuleは、ArchUnitライブラリのonionArchitecture()メソッドを使用して作成されます。以下のレイヤーと対応するパッケージを指定します：
   * <ui>
   * <li>ドメインモデル："undecided.erp.addressMgmt.model.."</li>
   * <li>ドメインサービス："undecided.erp.addressMgmt.services.."</li>
   * <li>アプリケーションサービス："undecided.erp.addressMgmt.application.."</li>
   * <li>アダプター（永続）："undecided.erp.addressMgmt.adapters.persistence.."</li>
   * <li>アダプター（REST）："undecided.erp.addressMgmt.adapters.rest.."</li>
   * </ui>
   * <p>
   * allowEmptyShould(true)メソッドは、空の@ShouldBeUsedByおよび@ShouldNotBeUsedBy注釈を有効な制約と見なすために呼び出されます。
   * <p>
   * この変数は、アーキテクチャテストを実行するために@ArchTestで注釈されています。
   */
  @ArchTest
  public static final ArchRule addressMgmtRule = onionArchitecture()
      .as("住所情報管理")
      .domainModels("undecided.erp.addressMgmt.model..")
      .domainServices("undecided.erp.addressMgmt.services..")
      .applicationServices("undecided.erp.addressMgmt.application..")
      .adapter("persistence", "undecided.erp.addressMgmt.adapters.persistence..")
      .adapter("rest", "undecided.erp.addressMgmt.adapters.rest..")
      .allowEmptyShould(true);

  /**
   * この変数は、ERPシステムのrelMgmtパッケージに対するオニオンアーキテクチャの制約を定義するArchRuleオブジェクトを表します。
   * <p>
   * ArchRuleは、ArchUnitライブラリの onionArchitecture() メソッドを使用して作成されます。以下のレイヤーとそれらに対応するパッケージを指定します：
   * <ui>
   * <li>ドメインモデル："undecided.erp.relMgmt.model.."</li>
   * <li>ドメインサービス："undecided.erp.relMgmt.services.."</li>
   * <li>アプリケーションサービス："undecided.erp.relMgmt.application.."</li>
   * <li>アダプター（永続）："undecided.erp.relMgmt.adapters.persistence.."</li>
   * <li>アダプター（REST）："undecided.erp.relMgmt.adapters.rest.."</li>
   * </ui>
   * <p>
   * allowEmptyShould(true) メソッドを呼び出して、空の@ShouldBeUsedByおよび@ShouldNotBeUsedBy注釈を有効な制約と見なすようにします。
   * <p>
   * この変数は、アーキテクチャテストを実行する目的で @ArchTest と注釈されています。
   * <p>
   * 使用例：
   * <p>
   * ArchTests archTests = ArchTests.in(OnionArchitectureTest.class); archTests.check(relMgmtRule);
   */
  @ArchTest
  public static final ArchRule relMgmtRule = onionArchitecture()
      .domainModels("undecided.erp.relMgmt.model..")
      .domainServices("undecided.erp.relMgmt.services..")
      .applicationServices("undecided.erp.relMgmt.application..")
      .adapter("persistence", "undecided.erp.relMgmt.adapters.persistence..")
      .adapter("rest", "undecided.erp.relMgmt.adapters.rest..")
      .allowEmptyShould(true);

}
