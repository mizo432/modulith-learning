package undecided.erp.shared.address.spi;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
@Table(name = "prefectures", comment = "prefecture table")
public class Prefecture {

  @Id
  @Column(
      name = "prefecture_id",
      comment = "Unique identifier for the prefecture",
      nullable = false)
  private Long id;

  @Size(max = 2)
  @NotNull
  @Column(name = "prefecture_code", comment = "Prefecture code", nullable = false, length = 2)
  private String prefectureCode;

  @Size(max = 6)
  @NotNull
  @Column(name = "lg_code", comment = "Legal code", nullable = false, length = 6)
  private String lgCode;

  @Size(max = 10)
  @NotNull
  @Column(name = "pref_name", comment = "Prefecture name", nullable = false, length = 10)
  private String prefName;

  @Size(max = 50)
  @NotNull
  @Column(name = "pref_kana", comment = "Prefecture kana", nullable = false, length = 50)
  private String prefKana;

  @Size(max = 50)
  @NotNull
  @Column(name = "pref_roma", comment = "Prefecture romaji", nullable = false, length = 50)
  private String prefRoma;

  @NotNull
  @Column(name = "effective_date", comment = "Effective date", nullable = false)
  private LocalDate effectiveDate;

  @NotNull
  @Column(name = "abolition_data", comment = "Abolition date", nullable = false)
  private LocalDate abolitionData;

  @Size(max = 256)
  @Column(name = "remarks", comment = "Remarks", length = 256)
  private String remarks;
}
