package undecided.erp.common.builder;

import static undecided.erp.common.primitive.Lists2.newArrayList;

import java.util.List;

/**
 * AbstractBuilderは、設定可能なアクションを持つ一般的なビルダーを表すクラスです。
 *
 * @param <T> ビルダーが作成できる値オブジェクトのタイプ。
 * @param <B> 具体的なビルダーサブクラスのタイプ。
 */
public abstract class AbstractBuilder<T, B extends AbstractBuilder<T, B>> {

  protected List<BuilderConfigurator<B>> configurators = newArrayList();

  /**
   * ビルダの設定に基づき、引数のValueObjectの内容を変更した新しいインスタンスを生成する。
   *
   * @param vo 状態を引用するValueObject
   * @return vo の内容に対して、このビルダの設定を上書きしたValueObjectの新しいインスタンス
   */
  public T apply(T vo) {
    B builder = newInstance();
    apply(vo, builder);
    for (BuilderConfigurator<B> configurator : configurators) {
      builder.addConfigurator(configurator);
    }
    return builder.build();
  }

  /**
   * ビルダーの設定に基づいて指定されたタイプの新しいインスタンスを構築し、その新しいインスタンスを返します。
   *
   * @return ビルダーからの設定が適用された指定されたタイプの新しいインスタンス。
   */
  public T build() {
    for (BuilderConfigurator<B> configurator : configurators) {
      configurator.configure(getThis());
    }

    return createValueObject();
  }

  /**
   * {@link BuilderConfigurator}を追加する。
   *
   * @param configurator {@link BuilderConfigurator}
   */
  protected void addConfigurator(BuilderConfigurator<B> configurator) {
    configurators.add(configurator);
  }

  /**
   * 引数のビルダに対して、引数のValueObjectの内容を適用する。
   *
   * @param vo 状態を引用するValueObject
   * @param builder ビルダ
   */
  protected abstract void apply(T vo, B builder);

  /**
   * ビルダの設定に基づいてValueObjectの新しいインスタンスを生成する。
   * <p>
   * <p>
   * {@link #build}内でこのビルダに追加された{@link BuilderConfigurator}を全て実行した後に、このメソッドが呼ばれる。<br>
   * その為、このビルダに対する変更を行うロジックはこのメソッド内に記述せず、目的となるValueObjectを生成し返すロジックを記述することが望まれる。
   * </p>
   *
   * @return ValueObjectの新しいインスタンス
   */
  protected abstract T createValueObject();

  /**
   * このビルダークラスのインスタンスを返す。
   *
   * @return このビルダークラスのインスタンス。
   */
  protected abstract B getThis();

  /**
   * このビルダークラスの新しいインスタンスを返す。
   *
   * @return このビルダークラスの新しいインスタンス。
   */
  protected abstract B newInstance();


  /**
   * {@link AbstractBuilder#build()}内で順次実行されるビルダの設定を定義するインタフェース。
   *
   * @param <S> 設定対象ビルダーの型
   */
  public interface BuilderConfigurator<S> {

    /**
     * {@link #build()}内で呼ばれる際に実行するビルドアクションを定義する。
     *
     * @param builder ビルダーインスタンス
     */
    void configure(S builder);

  }
}