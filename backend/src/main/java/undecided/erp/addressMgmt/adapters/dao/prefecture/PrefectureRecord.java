package undecided.erp.addressMgmt.adapters.dao.prefecture;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import undecided.erp.addressMgmt.model.prefecture.Prefecture;
import undecided.erp.addressMgmt.model.prefecture.PrefectureAttribute;

/**
 * このクラスはデータベースの都道府県のレコードを表現します。
 * <p>
 * それはID、LGコード、名前、カナ、ローマ字、有効日、能力日、備考などの都道府県に関する情報を含んでいます。
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "prefectures", schema = "address_info")
public class PrefectureRecord {

  @Id
  @Column(name = "prefecture_id", nullable = false)
  private Long id;
  @Column(name = "lg_code", nullable = false)
  private String lgCode;
  @Column(name = "pref_name", nullable = false)
  private String prefName;
  @Column(name = "pref_kana", nullable = false)
  private String prefKana;
  @Column(name = "pref_roma", nullable = false)
  private String prefRoma;
  @Column(nullable = false)
  private LocalDate efctDate;
  private LocalDate abltDate;
  private String remarks;

  public static PrefectureRecord from(Prefecture prefecture) {
    return new PrefectureRecord(
        prefecture.getId().getValue(),
        prefecture.getAttribute().getLgCode().getValue(),
        prefecture.getAttribute().getNames().getName().getValue(),
        prefecture.getAttribute().getNames().getKana().getValue(),
        prefecture.getAttribute().getNames().getRoma().getValue(),
        prefecture.getEffectiveDate().getValue().getValue(),
        prefecture.getAbolitionDate().getValue().getValue(),
        prefecture.getRemarks()
    );
  }

  public Prefecture toPrefecture() {
    return Prefecture.reconstruct(id,
        PrefectureAttribute.reconstruct(lgCode, prefName, prefKana, prefRoma), efctDate, abltDate,
        remarks);
  }


}