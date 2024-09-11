package undecided.erp;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import org.jmolecules.archunit.JMoleculesArchitectureRules;

@AnalyzeClasses(packages = "undecided.erp")
public class OnionArchitectureTest {

  /**
   * 変数: onionArchitecture
   * <p>
   * 説明:
   * この変数は、典型的なOnionアーキテクチャを強制するArchRuleを表します。この変数は、指定したパッケージ("undecided.erp")に対してOnionアーキテクチャを遵守しているかどうかをテストするOnionArchitectureTestクラスで使用されます。ArchRuleは、JMoleculesArchitectureRulesメソッドのensureOnionClassical()を使用して作成され、これにより典型的なOnionアーキテクチャのルールが定義されます。
   * <p>
   * ArchRuleはアサーションであり、ソフトウェアプロジェクトのアーキテクチャルルールを定義し強制するために使用することができます。Onionアーキテクチャはソフトウェアアーキテクチャパターンで、アプリケーションの異なる層間の依存関係を制限します。これによりモジュラリティ、関心の分離、そしてテストと保守性の容易さが促進されます。
   *
   * @see OnionArchitectureTest
   * @see JMoleculesArchitectureRules#ensureOnionClassical()
   * @see ArchRule
   */
  @ArchTest
  @SuppressWarnings("unused") // because I don't like IntelliJ warnings
  private final ArchRule onionArchitecture = JMoleculesArchitectureRules.ensureOnionClassical();

}
