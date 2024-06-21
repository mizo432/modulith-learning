package undecided.erp.common.builder;

public class ConcreteClass {

  private final Integer id;
  private final String name;

  public ConcreteClass(Integer id, String name) {
    this.id = id;
    this.name = name;
  }

  public String toString() {
    return "ConcreteClass{" +
        "id=" + id +
        ", name=" + name +
        "}";
  }

  /**
   * ConcreteBuilderの新しいインスタンスを返します。
   *
   * @return ConcreteBuilderの新しいインスタンス
   */
  public static ConcreteClassBuilder builder() {
    return new ConcreteClassBuilder();
  }

  /**
   * ConcreteClassBuilderは、ConcreteClassのインスタンスを作成するためのAbstractBuilderの具体的な実装です。
   * <p>以下のように利用します:
   * <pre>
   * ConcreteClass concreteClass = ConcreteClass.builder()
   *     .withId(1)
   *     .withName("example")
   *     .build();
   * </pre>
   * <p>withId() メソッドと withName() メソッドを使用して、それぞれidとnameの値を設定することが可能です。
   */
  public static class ConcreteClassBuilder extends
      AbstractBuilder<ConcreteClass, ConcreteClassBuilder> {

    private Integer id;
    private String name;

    /**
     * 与えられた {@code vo} からの値を {@code builder} に適用します。
     *
     * @param vo 値を適用するための値オブジェクト
     * @param builder 値を適用するためのビルダー
     */
    @Override
    protected void apply(ConcreteClass vo, ConcreteClassBuilder builder) {
      builder.withId(vo.id);
      builder.withName(vo.name);

    }

    /**
     * ConcreteClassBuilderのid値を設定します。
     *
     * @param id 設定するidの値
     * @return idの値が設定されたConcreteClassBuilder
     */
    public ConcreteClassBuilder withId(Integer id) {
      configurators.add(builder -> builder.id = id);
      return getThis();

    }

    /**
     * ConcreteClassBuilderに名前を設定します。
     *
     * @param name 設定する名前
     * @return 指定された名前が設定されたConcreteClassBuilderを返します。
     */
    public ConcreteClassBuilder withName(String name) {
      configurators.add(builder -> builder.name = name);
      return getThis();
    }

    /**
     * 提供されたidと名前を使用してConcreteClassの新しいインスタンスを作成します。
     *
     * @return ConcreteClassの新しいインスタンス
     */
    @Override
    protected ConcreteClass createValueObject() {
      return new ConcreteClass(id, name);
    }

    /**
     * ConcreteClassBuilderの現在のインスタンスを取得します。
     *
     * @return ConcreteClassBuilderの現在のインスタンス
     */
    @Override
    protected ConcreteClassBuilder getThis() {
      return this;
    }

    /**
     * ConcreteClassBuilderの新しいインスタンスを作成します。
     *
     * @return ConcreteClassBuilderの新しいインスタンス
     */
    @Override
    protected ConcreteClassBuilder newInstance() {
      return new ConcreteClassBuilder();
    }
  }

}
