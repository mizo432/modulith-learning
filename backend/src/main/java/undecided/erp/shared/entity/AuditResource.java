package undecided.erp.shared.entity;

import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import java.time.LocalDateTime;
import lombok.Getter;
import undecided.erp.common.dateProvider.DateProvider;

/**
 * AuditResourceは、エンティティが生成および更新された際の 監査情報を管理するための抽象クラスです。
 *
 * <p>このクラスは、エンティティの生成者(createdBy)と 更新者(updatedBy)、および生成日時(createdAt)と 更新日時(updatedAt)を保持します。
 *
 * <p>クラスにはエンティティが永続化または更新される前に 自動的に呼び出されるコールバックメソッドを提供し、 上記の情報を自動的に設定します。
 *
 * <p>このクラスは、@MappedSuperclassとしてアノテートされており、 基底クラスとして利用できます。また、@Auditedアノテーションを
 * 使用して監査対象としてマークされています。
 */
@MappedSuperclass
@Getter
public abstract class AuditResource {
  /**
   * この変数はエンティティの作成者を表します。
   *
   * <p>基底クラスであるAuditResource内で使用され、エンティティが生成された際の 作成者の情報を格納します。この情報は、永続化の直前に自動的に設定されます。
   *
   * <p>値は通常、システムユーザー名や識別子が格納され、直近の更新処理によって 作成者情報が保持または変更される場合があります。
   */
  protected String createdBy;

  /**
   * エンティティの生成日時を保持するためのフィールドです。
   *
   * <p>このフィールドはエンティティが作成された日時を表し、 永続化処理の直前に自動的に現在の日時が設定されます。
   *
   * <p>LocalDateTime型を使用しており、日付と時刻の両方の情報を含みます。
   *
   * <p>主に監査目的で使用され、エンティティがいつ作成されたかを追跡するために利用されます。
   */
  protected LocalDateTime createdAt;

  /**
   * この変数はエンティティの更新者を表します。
   *
   * <p>エンティティが更新された際に、更新を行ったユーザーやシステムによる識別情報を格納します。
   * 通常、ユーザー名やシステム識別子が設定され、永続化もしくは更新処理の直前に自動的に設定されることが想定されています。
   *
   * <p>主に監査目的で使用され、エンティティの更新履歴を追跡するために利用されます。
   */
  protected String updatedBy;

  /**
   * エンティティの更新日時を保持するフィールドです。
   *
   * <p>このフィールドは、エンティティが最後に更新された日時を記録します。 エンティティが更新されるときに自動的に現在の日時が設定される仕様であり、 主に監査目的で使用されます。
   *
   * <p>LocalDateTime型を使用しており、日付と時刻の両方を格納します。
   */
  protected LocalDateTime updatedAt;

  /**
   * エンティティが永続化される前に呼び出されるコールバックメソッドです。
   *
   * <p>このメソッドは、作成日時(createdAt)と作成者(createdBy)を自動的に設定します。
   * 作成日時は現在の日時(LocalDateTime)が設定され、作成者は「system」という固定値が設定されます。
   *
   * <p>主に監査目的で使用され、エンティティが作成される際の履歴を追跡できるように設計されています。
   *
   * @see PrePersist
   */
  @PrePersist
  public void prePersist() {
    createdAt = DateProvider.currentLocalDateTime();

    createdBy = "system";
  }

  /**
   * エンティティの更新が行われる直前に呼び出されるコールバックメソッドです。
   *
   * <p>このメソッドは、更新日時(updatedAt)および更新者(updatedBy)を自動的に設定します。 更新日時は現在の日時(LocalDateTime)を、更新者は "system"
   * を固定値として設定します。
   *
   * <p>このメソッドは主に監査目的で使用され、エンティティの更新履歴を追跡できるように設計されています。
   *
   * @see PreUpdate
   */
  @PreUpdate
  public void preUpdate() {
    updatedAt = DateProvider.currentLocalDateTime();
    updatedBy = "system";
  }
}
