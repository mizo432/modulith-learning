package undecided.erp.shared.entity;

import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;
import undecided.erp.common.snowflake.NodeIdGenerator;
import undecided.erp.common.snowflake.SnowflakeIdGenerator;

@Getter
@Setter
@Embeddable
public class SnowflakeId<A> implements Id<A> {

  @Column(nullable = false, unique = true)
  @JsonValue
  private final Long value;

  public SnowflakeId(Long value) {
    this.value = value;
  }

  public SnowflakeId() {
    this.value = null;

  }


  /**
   * SnowflakeIdクラスの新しいインスタンスを作成します。
   *
   * @param <A> 型パラメータ
   * @return SnowflakeIdクラスの新しいインスタンス
   */
  public static <A> SnowflakeId<A> newInstance() {
    return new SnowflakeId<>(
        new SnowflakeIdGenerator(NodeIdGenerator.generateNodeId()).generateId());
  }

  public static <A> SnowflakeId<A> reconstruct(Long id) {
    return new SnowflakeId<>(id);
  }

  public static <A> SnowflakeId<A> of(long value) {
    return new SnowflakeId<>(value);
  }

  /**
   * SnowflakeIdオブジェクトの文字列表現を返します。
   *
   * @return SnowflakeIdオブジェクトの文字列表現。
   */
  @Override
  public String toString() {
    return String.valueOf(value);
  }
}
