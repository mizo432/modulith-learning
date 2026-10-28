package undecided.shared.entity;

import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Immutable;

/**
 * ロールエンティティの基底クラスです。
 *
 * <p>この抽象クラスは、エンティティに付与されるロール（役割）を表すエンティティの基底として
 * 機能します。パーティや物が持つ役割をモデル化するために使用されます。
 *
 * <p>{@link BusinessEntity} を継承するため、作成日時や作成者などの監査情報を保持します。
 *
 * @param <R> {@code RoleEntity} を拡張する実際のクラス型
 * @see BusinessEntity
 * @see PartyRole
 * @see ThingRoleEntity
 */
@MappedSuperclass
@Getter
@Setter
@Immutable
public abstract class RoleEntity<R extends RoleEntity<R>> extends BusinessEntity<R> {}
