package undecided.erp.addressMgmt.infra.dao.prefecture;

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

/**
 * This class represents a record of a prefecture in a database. It contains information about the
 * prefecture such as its ID, LG code, name, kana, romaji, effective date, ability date, and
 * remarks.
 * <p>
 * The class is annotated with various annotations such as @AllArgsConstructor, @NoArgsConstructor,
 * @Getter, @Setter,
 *
 * @ToString, @Entity, and @Table to provide additional functionality and meta-data for the class.
 * <p>
 * The class has the following properties: - id: The ID of the prefecture (prefecture_id) - lgCode:
 * The LG code of the prefecture (lg_code) - prefName: The name of the prefecture (pref_name) -
 * prefKana: The kana representation of the prefecture's name (pref_kana) - prefRoma: The romaji
 * representation of the prefecture's name (pref_roma) - efctDate: The effective date of the
 * prefecture (nullable) - abltDate: The ability date of the prefecture (nullable) - remarks:
 * Remarks about the prefecture
 * <p>
 * Note: The properties are mapped to the corresponding columns in the "prefectures" table in the
 * "address_info" schema.
 * <p>
 * Example usage:
 * <p>
 * PrefectureRecord prefecture = new PrefectureRecord(); prefecture.setId(1L);
 * prefecture.setLgCode("01"); prefecture.setPrefName("Hokkaido"); prefecture.setPrefKana("ほっかいどう");
 * prefecture.setPrefRoma("Hokkaido"); prefecture.setEfctDate(LocalDate.of(1947, 8, 15));
 * prefecture.setAbltDate(LocalDate.of(9999, 12, 31)); prefecture.setRemarks("The northernmost
 * prefecture of Japan");
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


}