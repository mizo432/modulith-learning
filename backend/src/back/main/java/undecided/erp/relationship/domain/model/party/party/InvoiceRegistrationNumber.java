package undecided.erp.relationship.domain.model.party.party;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import undecided.erp.common.primitive.Objects2;

/**
 * 適格請求書発行事業者登録番号
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@Getter
@Setter
public class InvoiceRegistrationNumber {

  /**
   * 空のInvoiceRegistrationNumberオブジェクトを表します。
   * <p>
   * InvoiceRegistrationNumberオブジェクトは、資格を持つ請求書発行オペレータの登録番号を表します。
   * その値がnullの場合、InvoiceRegistrationNumberオブジェクトは空とみなされます。
   * <p>
   * このstatic final変数にアクセスすることで、空のInvoiceRegistrationNumberオブジェクトを取得できます。
   *
   * @see InvoiceRegistrationNumber
   */
  public static final InvoiceRegistrationNumber EMPTY = new InvoiceRegistrationNumber();

  /**
   * InvoiceRegistrationNumberオブジェクトのシリアライズされた値。
   */
  @JsonValue
  private String value;

  /**
   * 提供された値に基づいてInvoiceRegistrationNumberオブジェクトを作成します。
   * <p>
   * もし値がnullであれば、InvoiceRegistrationNumber.EMPTYを返します。
   *
   * @param value InvoiceRegistrationNumberを作成するために使用される値
   * @return 作成されたInvoiceRegistrationNumber、または値がnullの場合はInvoiceRegistrationNumber.EMPTY
   */
  @JsonCreator
  public static InvoiceRegistrationNumber of(String value) {
    return Objects2.nonNull(value) ?
        new InvoiceRegistrationNumber(value)
        : InvoiceRegistrationNumber.EMPTY;
  }

  /**
   * 提供された値とタイプに基づいてInvoiceRegistrationNumberを再構築します。
   *
   * @param value InvoiceRegistrationNumberを再構築するために使用される値
   * @return 再構築されたInvoiceRegistrationNumber、値がnullの場合はInvoiceRegistrationNumber.EMPTYを返します
   */
  public static InvoiceRegistrationNumber reconstruct(String value) {
    return Objects2.nonNull(value) ?
        new InvoiceRegistrationNumber(value)
        : InvoiceRegistrationNumber.EMPTY;
  }

  /**
   * オブジェクトの文字列表現を返します。
   *
   * @return オブジェクトの文字列表現。
   */
  @Override
  public String toString() {
    return String.valueOf(value);
  }
}
