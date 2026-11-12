package com.github.mizo432.shared.sample.jpa.feature.query;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @Query/@Modifyingのサンプルで使用される祝日エンティティです。
 *
 * <p>カスタムJPQLクエリと更新クエリの例として使用します。
 */
@Entity
@Table(name = "sample_holidays")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SampleHoliday {

  @Id private Long id;

  @Column(name = "holiday_date", nullable = false)
  private LocalDate holidayDate;

  @Column(name = "holiday_name", nullable = false, length = 50)
  private String holidayName;

  @Column(name = "is_active", nullable = false)
  private Boolean active;

  public SampleHoliday(Long id, LocalDate holidayDate, String holidayName) {
    this.id = id;
    this.holidayDate = holidayDate;
    this.holidayName = holidayName;
    this.active = true;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    SampleHoliday that = (SampleHoliday) o;
    return Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
