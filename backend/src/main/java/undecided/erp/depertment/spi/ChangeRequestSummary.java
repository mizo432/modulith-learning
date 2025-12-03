package undecided.erp.depertment.spi;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jspecify.annotations.NonNull;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ChangeRequestSummary {
  /**
   * 組織IDを表すフィールドです。
   *
   * <p>このフィールドは、組織を一意に識別するために使用される文字列を保持します。 主に変更リクエストや階層構造内での組織間の関連付けやトラッキングに利用されます。
   *
   * <p>組織IDは以下のような要件を満たす必要があります: - 必ず10桁の半角数字 - null値を許容しない
   */
  private @NonNull String organizationId;

  /**
   * 変更リクエスト番号を表すフィールドです。
   *
   * <p>このフィールドは、特定の変更リクエストを一意に識別するための番号を保持します。 主にリクエストのトラッキングや参照を目的として使用されます。
   *
   * <p>必須項目であり、nullを許容しません。また、数値型(Integer)で入力される必要があります。
   */
  private @NonNull Integer chengeRequestNumber;

  /**
   * 親組織のIDを表すフィールドです。
   *
   * <p>このフィールドは、現在の組織が属している親組織の識別子を保持します。 階層構造を持つ組織の親子関係を管理するために使用されます。
   *
   * <p>主な特徴: - 親組織の一意のIDが格納されます。 - null値は許容されません。
   */
  private @NonNull String parentOrganizationId;

  /**
   * フルネーム（完全名称）を表すフィールドです。
   *
   * <p>このフィールドは、組織またはエンティティの完全な名称を保持します。 主に識別や表示目的で使用されます。
   *
   * <p>主な特徴: - 必須項目であり、null値を許容しません。 - 一般的に、組織や部署の正式名称が格納されます。
   */
  private @NonNull String fullName;

  public static ChangeRequestSummary create(@NonNull ChangeRequest changeRequest) {
    return new ChangeRequestSummary(
        changeRequest.getOrganizationId(),
        changeRequest.getChengeRequestNumber(),
        changeRequest.getParentOrganizationId(),
        changeRequest.getFullName());
  }
}
