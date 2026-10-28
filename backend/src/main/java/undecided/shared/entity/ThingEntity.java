package undecided.shared.entity;

/**
 * 物（Thing）エンティティの基底クラスです。
 *
 * <p>この抽象クラスは PPT（Party, Place, Thing）モデルにおける「物」を表すエンティティの
 * 基底として機能します。物理的な物体や仮想的なオブジェクトなど、業務上で「物」として
 * 扱われるエンティティはこのクラスを継承して実装します。
 *
 * <p>{@link PptEntity} を継承するため、作成日時や作成者などの監査情報を保持します。
 *
 * @param <T> {@code ThingEntity} を拡張する実際のクラス型
 * @see PptEntity
 */
public abstract class ThingEntity<T extends ThingEntity<T>> extends PptEntity<T> {}
