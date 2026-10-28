package undecided.shared.entity;

/**
 * 物（Thing）の説明エンティティの基底クラスです。
 *
 * <p>この抽象クラスは PPT（Party, Place, Thing）モデルにおける「物」の説明情報を
 * 表すエンティティの基底として機能します。物が持つ説明文や注釈などをモデル化するために
 * 使用されます。
 *
 * <p>{@link DescriptionEntity} を継承するため、作成日時や作成者などの監査情報を保持します。
 *
 * @param <D> {@code ThingDescriptionEntity} を拡張する実際のクラス型
 * @param <T> 説明対象の {@link ThingEntity} の型
 * @see DescriptionEntity
 * @see ThingEntity
 */
public abstract class ThingDescriptionEntity<
        D extends ThingDescriptionEntity<D, T>, T extends ThingEntity<T>>
    extends DescriptionEntity<D, T> {}
