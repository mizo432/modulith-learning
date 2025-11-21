package undecided.erp.common.entity;

import static undecided.erp.common.primitive.Objects2.isNull;

import java.io.Serializable;
import java.util.UUID;
import undecided.erp.common.uuidV7Provider.UuidV7Provider;

public class UuidV7IdValue implements UuidValue<UuidV7IdValue>, Serializable {

  private final UUID value;

  public UuidV7IdValue() {
    this(null);
  }

  UuidV7IdValue(UUID value) {
    this.value = value;
  }

  public static UuidValue<UuidV7IdValue> newInstance() {
    return new UuidV7IdValue(UuidV7Provider.newInstanse());
  }

  UuidValue<UuidV7IdValue> empty() {
    return new UuidV7IdValue(null);
  }

  @Override
  public UUID value() {
    return value;
  }

  @Override
  public boolean isEmpty() {
    return isNull(value);
  }
}
