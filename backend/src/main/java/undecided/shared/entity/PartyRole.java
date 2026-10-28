package undecided.shared.entity;

/**
 * パーティ（Party）のロールを表すエンティティクラスです。
 *
 * <p>このクラスは PPT（Party, Place, Thing）モデルにおける「パーティ」に付与される
 * ロール（役割）を表します。個人や法人などのパーティが持つ役割をモデル化するために
 * 使用されます。
 *
 * <p>{@link RoleEntity} を継承するため、作成日時や作成者などの監査情報を保持します。
 *
 * @param <P> {@code PartyRole} を拡張する実際のクラス型
 * @see RoleEntity
 */
public class PartyRole<P extends PartyRole<P>> extends RoleEntity<P> {}
