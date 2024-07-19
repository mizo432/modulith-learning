package undecided.erp.shared.entity;

public abstract class EnumConverter<E extends PersistableEnum> {

  protected String toDatabaseValue(E value) {
    return value.getDatabaseValue();
  }

  protected abstract E toEntityAttribute(Class<E> enumClass, String value);

}
