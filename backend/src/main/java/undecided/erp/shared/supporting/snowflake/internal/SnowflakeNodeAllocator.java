package undecided.erp.shared.supporting.snowflake.internal;

import jakarta.transaction.Transactional;
import java.time.Duration;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import undecided.shared.common.dateProvider.DateProvider;

/**
 * スノーフレークアルゴリズムで使用するノードIDを管理および割り当てるためのクラスです。
 * <p>
 * ノードIDは永続化され、一定期間のリースとして管理されます。
 * <p>
 * このクラスは最大ノードID制限を設けるとともに、リースの有効期間を制御することで、 各スノーフレークノードが一意性を保持したまま運用されることを保証します。
 * <p>
 * 主な機能: - ノードIDの割り当て - ノードリースの更新
 * <p>
 * 使用する主な依存: - SnowflakeNodeJpaRepository: ノード情報を永続化するためのリポジトリ。 - Clock: 時刻管理を行います（テスト可能性向上のため使用）。
 */
@Component
@RequiredArgsConstructor
public class SnowflakeNodeAllocator {

  private static final Duration LEASE_DURATION = Duration.ofSeconds(30);
  private final SnowflakeNodeJpaRepository repository;

  /**
   * 使用可能なスノーフレークノードIDをアプリケーションに割り当てます。 ノードIDは一定のリース期間を持ち、使用状況に応じて再利用または更新されます。
   *
   * @param applicationName ノードIDを取得するアプリケーションの名前。
   * @return 割り当てられた新しいスノーフレークノードオブジェクト。既存ノードが期限切れの場合、そのノードを更新して返します。
   * @throws IllegalStateException 利用可能なノードIDが存在しない場合にスローされます。
   */
  @Transactional
  public SnowflakeNode allocate(String applicationName) {
    LocalDateTime now = DateProvider.currentLocalDateTime();
    LocalDateTime leaseUntil = now.plus(LEASE_DURATION);

    for (int nodeId = 0; nodeId < NodeId.MAX; nodeId++) {
      var existing = repository.findById(nodeId);
      if (existing.isEmpty()) {
        var entity = new SnowflakeNode(nodeId, applicationName, leaseUntil);
        repository.save(entity);

        return entity;
      }
      SnowflakeNode node = existing.get();
      if (node.getLeaseUntil().isBefore(now)) {
        var newNode = node.renew(leaseUntil);
        repository.save(newNode);
        return newNode;
      }
    }
    throw new IllegalStateException("No snowflake node ID available.");
  }

  /**
   * 指定されたスノーフレークノードリースを更新します。 更新は、条件が満たされた場合にのみ行われます。
   * 条件として、指定されたノードIDが既存のノード情報と一致し、リースの有効期限が現在時刻より過去である必要があります。 一致しない場合、またはノードが存在しない場合は更新を行いません。
   *
   * @param lease 更新対象のスノーフレークノードリース。 ノードID、インスタンス名、およびリース有効期限が含まれます。
   * @return 更新が正常に行われた場合はtrue、条件が満たされずに更新されなかった場合はfalse。
   */
  @Transactional
  public boolean renew(SnowflakeNode lease) {
    LocalDateTime now = DateProvider.currentLocalDateTime();
    LocalDateTime newLeaseUntil = now.plus(LEASE_DURATION);
    var entity = repository.findById(lease.getNodeId());
    if (entity.isEmpty()) {
      return false;
    }

    SnowflakeNode node = entity.get();
    if (!node.getInstanceName().equals(lease.getInstanceName())) {
      return false;
    }

    if (!node.getLeaseUntil().isBefore(now)) {
      return false;
    }
    var newNode = node.renew(newLeaseUntil);
    repository.save(newNode);
    return true;


  }

}
