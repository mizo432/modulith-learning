package undecided.erp.relMgmt.model.party.organization;

import java.util.ArrayList;
import java.util.List;
import undecided.erp.relMgmt.model.party.organizationUnit.OrganizationUnit;
import undecided.erp.shared.entity.SnowflakeId;

/**
 * Organizationクラスは、組織を表し、Partyクラスを拡張します。
 */
public class Organization {

  private SnowflakeId<Organization> id;
  private OrganizationName name;
  /**
   * 組織の産業部門。
   */
  private IndustrySector industrySector;

  /**
   * taxExemptNumber変数は、組織の免税番号を表します。
   * <p>
   * TaxExemptNumberクラスは、免税識別番号を表します。これは組織の免税番号を保存し、取得するために使用されます。
   * <p>
   * 例:
   * <pre>{@code
   *     TaxExemptNumber taxExemptNumber = new TaxExemptNumber();
   * }</pre>
   */
  private TaxExemptNumber taxExemptNumber;
  /**
   * organizationUnits 変数は OrganizationUnit オブジェクトのリストを示します。
   * <p>
   * OrganizationUnit クラスは、組織内のユニットを示します。組織内の別個の部門や部署を定義するために使用されます。各 OrganizationUnit
   * は、親組織と関連付けることができ、複数の子 OrganizationUnits を持つことが可能です。また、各 OrganizationUnit は、ユニットが使用するアドレスを示す
   * AddressUse オブジェクトのリストを持つことができます。
   * <p>
   * organizationUnits リストは初めは空の ArrayList です。OrganizationUnit オブジェクトは add() メソッドを使用してリストに追加できます。
   * <p>
   * 例：
   * <p>
   * OrganizationUnit unit1 = new OrganizationUnit(); organizationUnits.add(unit1);
   * <p>
   * OrganizationUnit unit2 = new OrganizationUnit(); organizationUnits.add(unit2);
   * <p>
   * // organizationUnits リストへのアクセス for (OrganizationUnit unit : organizationUnits) { // 各
   * OrganizationUnit オブジェクトに何かを行う }
   */
  private final List<OrganizationUnit> organizationUnits = new ArrayList<>();

}
