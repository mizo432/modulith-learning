package undecided.erp.shared.entity;

import jakarta.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Immutable;

/**
 * ビジネスエンティティの基底クラスを表します。このクラスは特定のエンティティタイプによって拡張されることを前提としています。
 * <p>
 * このクラスは、作成タイムスタンプや作成者の識別子といった共通プロパティを提供します。
 * <p>
 * このクラスは抽象クラスであり、不変(immutable)としてマークされています。つまり、初期化後にその状態を変更することはできません。
 * <p>
 * このクラスを拡張する際は、{@code BusinessEntity}を継承した具体的なエンティティタイプを提供する必要があります。
 * <p>
 * アノテーション: - {@code @MappedSuperclass}: このクラスがJPAにおいてマッピングされたスーパークラスであることを示します。
 * このため、フィールドはエンティティによって継承されます。 - {@code @Getter} および {@code @Setter}:
 * Lombokアノテーションにより、すべてのフィールドに対して 自動的にゲッターおよびセッターが生成されます。 - {@code @Immutable}:
 * このクラスまたはそのサブクラスのオブジェクトが作成後に変更されないことを示します。
 *
 * @param <E> {@code BusinessEntity}を拡張するエンティティのタイプ。
 */
@MappedSuperclass
@Getter
@Setter
@Immutable
public abstract class BusinessEntity<E extends BusinessEntity<E>> {

  /**
   * エンティティが作成された時刻を示すタイムスタンプを表します。
   * <p>
   * このプロパティは不変（immutable）であり、エンティティの作成時に自動的に設定されます。
   * <p>
   * 監査目的で使用され、エンティティがいつ作成されたかを追跡するために利用されます。
   */
  private LocalDateTime createdAt;

  /**
   * エンティティを作成したユーザーまたはプロセスを識別します。
   * <p>
   * このフィールドは、エンティティに関連付けられた作成者の一意の識別子を保持します。
   * <p>
   * 監査目的で使用され、エンティティの起源を追跡できるようにします。
   * <p>
   * この値はエンティティが初期作成された際に設定され、その後変更されることはありません。
   */
  private Long createdBy;

}
