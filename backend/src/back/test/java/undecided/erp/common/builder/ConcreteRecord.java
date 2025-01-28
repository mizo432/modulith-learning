package undecided.erp.common.builder;

public record ConcreteRecord(Integer id,
                             String name) {


  /**
   * ConcreteBuilderの新しいインスタンスを返します。
   *
   * @return ConcreteBuilderの新しいインスタンス
   */
  public static ConcreteRecordBuilder builder() {
    return new ConcreteRecordBuilder();
  }

  public static class ConcreteRecordBuilder extends
      AbstractBuilder<ConcreteRecord, ConcreteRecordBuilder> {

    private Integer id;
    private String name;

    @Override
    protected void apply(ConcreteRecord vo, ConcreteRecordBuilder builder) {
      builder.withId(vo.id());
      builder.withName(vo.name());

    }

    public ConcreteRecordBuilder withId(Integer id) {
      configurators.add(builder -> builder.id = id);
      return getThis();

    }

    public ConcreteRecordBuilder withName(String name) {
      configurators.add(builder -> builder.name = name);
      return getThis();
    }

    @Override
    protected ConcreteRecord createValueObject() {
      return new ConcreteRecord(id, name);
    }

    @Override
    protected ConcreteRecordBuilder getThis() {
      return this;
    }

    @Override
    protected ConcreteRecordBuilder newInstance() {
      return new ConcreteRecordBuilder();
    }
  }

}
