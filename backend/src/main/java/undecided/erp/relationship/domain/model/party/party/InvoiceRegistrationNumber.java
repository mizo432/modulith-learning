package undecided.erp.relationship.domain.model.party.party;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
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

  public static final InvoiceRegistrationNumber EMPTY = new InvoiceRegistrationNumber();
  @JsonValue
  private String value;

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
   * @param type 請求書登録の対象性を決定するPartyType
   * @return 再構築されたInvoiceRegistrationNumber、値がnullの場合はInvoiceRegistrationNumber.EMPTYを返します
   */
  public static InvoiceRegistrationNumber reconstruct(String value, @NonNull PartyType type) {
    return Objects2.nonNull(value) ?
        new InvoiceRegistrationNumber(value)
        : InvoiceRegistrationNumber.EMPTY;
  }

  @Override
  public String toString() {
    return String.valueOf(value);
  }
}
