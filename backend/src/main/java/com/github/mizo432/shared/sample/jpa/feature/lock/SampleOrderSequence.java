package com.github.mizo432.shared.sample.jpa.feature.lock;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @Lockのサンプルで使用される順序エンティティです。
 *
 * <p>ペシミスティックロックを使用して通番を取得するパターンを示します。
 */
@Entity
@Table(name = "sample_order_sequence")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SampleOrderSequence {

  @Id private Long id;

  @Column(name = "sequence_name", nullable = false, unique = true, length = 50)
  private String sequenceName;

  @Column(name = "current_value", nullable = false)
  private Long currentValue;

  public SampleOrderSequence(Long id, String sequenceName, Long currentValue) {
    this.id = id;
    this.sequenceName = sequenceName;
    this.currentValue = currentValue;
  }
}
