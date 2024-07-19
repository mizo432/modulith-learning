package undecided.erp.addressMgmt.domain.model.prefecture;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import undecided.erp.shared.entity.SnowflakeId;

/**
 * Prefectureクラスは、属性、施行日、廃止日、備考とともに都道府県を表現しています。
 */
@AllArgsConstructor
@Getter
@ToString
public class Prefecture {

  private SnowflakeId<Prefecture> id;
  private PrefectureAttribute attribute;
  private PrefectureEffectiveDate effectiveDate;
  private PrefectureAbolitionDate abolitionDate;
  private String remarks;

  /**
   * 与えられたパラメータで都道府県を再構築します。
   *
   * @param id 都道府県のID
   * @param attribute 都道府県の属性
   * @param effectiveDate 都道府県の有効日
   * @param abolitionDate 都道府県の適用日
   * @param remarks 都道府県の備考
   * @return 再構築された都道府県
   */
  public static Prefecture reconstruct(Long id, PrefectureAttribute attribute,
      LocalDate effectiveDate, LocalDate abolitionDate, String remarks) {
    return new Prefecture(SnowflakeId.of(id), attribute, PrefectureEffectiveDate.of(effectiveDate),
        PrefectureAbolitionDate.of(abolitionDate), remarks);

  }

  public static Prefecture create(SnowflakeId<Prefecture> id, PrefectureAttribute attribute,
      PrefectureEffectiveDate effectiveDate, String remarks) {
    return new Prefecture(id, attribute, effectiveDate, PrefectureAbolitionDate.MAX, remarks);
  }

  public static Prefecture update(SnowflakeId<Prefecture> id, PrefectureAttribute attribute,
      PrefectureEffectiveDate effectiveDate, String remarks) {
    return new Prefecture(id, attribute, effectiveDate, PrefectureAbolitionDate.MAX, remarks);
  }

  public Prefecture deleteAtToday() {
    return new Prefecture(SnowflakeId.newInstance(), attribute, effectiveDate,
        PrefectureAbolitionDate.now(), remarks);
  }

  public Prefecture deleteAtYesterday() {
    return new Prefecture(SnowflakeId.newInstance(), attribute, effectiveDate,
        PrefectureAbolitionDate.yesterday(), remarks);
  }

}
