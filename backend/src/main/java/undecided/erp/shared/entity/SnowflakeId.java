package undecided.erp.shared.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.util.Objects;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import undecided.erp.common.snowflake.SnowflakeIdProvider;

@Getter
@Setter
@Embeddable
@SuppressWarnings("unchecked")
public class SnowflakeId<A> implements Id<A> {

  private static final SnowflakeId<?> EMPTY = new SnowflakeId<>();

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
    return new SnowflakeId<>(SnowflakeIdProvider.generateId());
  }

  public static <A> SnowflakeId<A> reconstruct(Long id) {
    return new SnowflakeId<>(id);
  }

  @JsonCreator
  public static <A> SnowflakeId<A> of(@NonNull Long value) {
    return new SnowflakeId<>(value);
  }

  public static <A> SnowflakeId<A> empty() {
    return (SnowflakeId<A>) EMPTY;
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SnowflakeId<?> that = (SnowflakeId<?>) o;
    return Objects.equals(value, that.value);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(value);
  }

  public boolean isEmpty() {
    return value == null;
  }
}
