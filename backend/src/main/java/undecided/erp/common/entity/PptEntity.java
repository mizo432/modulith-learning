package undecided.erp.common.entity;

import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Immutable;

/**
 * システム内のPPT（パーティ、場所、物）エンティティを表します。
 * <p>
 * この抽象クラスは、PPT構造に準拠するエンティティの基底クラスとして機能します。 PPTエンティティは、パーティ（例: 個人、法人）、物理的または仮想的な場所、
 * 目に見える物や形のない物を表す可能性があります。
 * <p>
 * このクラスは、共通の監査および識別機能を含む、基本的な動作やプロパティのために 汎用の基底クラス {@code BusinessEntity} を活用しています。
 * <p>
 * タイプパラメータ {@code <T>} は {@code PptEntity<T>} のサブタイプである必要があり、 サブクラスが一貫した自己参照型構造を維持することを保証します。
 * これにより、PPTエンティティに関わる操作における流暢なインターフェースと型安全性が確保されます。
 * <p>
 * 設計上、このエンティティを継承するサブクラスのインスタンスは不変（immutable）であることを意図しています。
 * そのため、作成時刻や作成者の識別情報などのプロパティはインスタンス生成時に確立され、 その後変更することはできません。
 * <p>
 * サブクラスはそれぞれのビジネス要件に応じたPPT固有の属性や動作をさらに定義する必要があります。
 *
 * @param <T> {@code PptEntity} を拡張する実際のクラス型。型の一貫性を維持するために使用されます。
 */
@MappedSuperclass
@Getter
@Setter
@Immutable
public abstract class PptEntity<T extends PptEntity<T>> extends BusinessEntity<PptEntity<T>> {
// PPTはParty(パーティ), Place(場所)そして Thing(物)を表します。
}
