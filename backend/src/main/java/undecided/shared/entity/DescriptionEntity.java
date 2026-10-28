package undecided.shared.entity;

/**
 * 説明（Description）エンティティの基底クラスです。
 *
 * <p>この抽象クラスは PPT（Party, Place, Thing）モデルにおけるエンティティの説明情報を
 * 表すエンティティの基底として機能します。エンティティに付随する説明文や注釈などを
 * モデル化するために使用されます。
 *
 * <p>{@link BusinessEntity} を継承するため、作成日時や作成者などの監査情報を保持します。
 *
 * @param <R> {@code DescriptionEntity} を拡張する実際のクラス型
 * @param <P> 説明対象の {@link PptEntity} の型
 * @see BusinessEntity
 * @see ThingDescriptionEntity
 */
public abstract class DescriptionEntity<R extends DescriptionEntity<R, P>, P extends PptEntity<P>>
    extends BusinessEntity<R> {}
