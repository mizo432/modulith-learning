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
import org.hibernate.annotations.Comment;

@Getter
@Setter
@ToString
@Entity
@Table(name = "prefectures")
@Comment("prefecture table")
public class Prefecture {

  @Id
  @Comment("Unique identifier for the prefecture")
  @Column(name = "prefecture_id", nullable = false)
  private Long id;

  @Size(max = 2)
  @NotNull
  @Comment("Prefecture code")
  @Column(name = "prefecture_code", nullable = false, length = 2)
  private String prefectureCode;

  @Size(max = 6)
  @NotNull
  @Comment("Legal code")
  @Column(name = "lg_code", nullable = false, length = 6)
  private String lgCode;

  @Size(max = 10)
  @NotNull
  @Comment("Prefecture name")
  @Column(name = "pref_name", nullable = false, length = 10)
  private String prefName;

  @Size(max = 50)
  @NotNull
  @Comment("Prefecture kana")
  @Column(name = "pref_kana", nullable = false, length = 50)
  private String prefKana;

  @Size(max = 50)
  @NotNull
  @Comment("Prefecture romaji")
  @Column(name = "pref_roma", nullable = false, length = 50)
  private String prefRoma;

  @NotNull
  @Comment("Effective date")
  @Column(name = "effective_date", nullable = false)
  private LocalDate effectiveDate;

  @NotNull
  @Comment("Abolition date")
  @Column(name = "abolition_data", nullable = false)
  private LocalDate abolitionData;

  @Size(max = 256)
  @Comment("Remarks")
  @Column(name = "remarks", length = 256)
  private String remarks;


}
