package undecided.agiloom.productDiscovery.spi;

import java.util.List;
import undecided.supporting.snowflake.SnowflakeId;

/**
 * スクラムチームの変更を他事業領域へ伝えるイベント（スナップショット形式）。
 *
 * <p>他事業領域がクエリーサービス等でスクラムチームの情報を必要とする場合は、
 * このイベントを受けて自領域内にサブセット（射影）を保持し、それを参照します。
 * 他領域のエンティティやテーブルを直接参照しません。
 *
 * <p>イベントは Spring Modulith のイベント発行ログ（JDBC）に永続化され、
 * ロングタームイベントとして残ります。
 *
 * @param scrumTeamId スクラムチームID
 * @param productId 所属するProductのID
 * @param name チーム名
 * @param businessDomain 事業領域
 * @param poEmployeeId POの従業員ID
 * @param poName POの名前
 * @param smEmployeeId SMの従業員ID
 * @param smName SMの名前
 * @param memberEmployeeIds チームメンバーの従業員IDリスト（PO/SMを含む）
 * @param removed 削除された場合は true
 */
public record ScrumTeamChanged(
    SnowflakeId scrumTeamId,
    SnowflakeId productId,
    String name,
    String businessDomain,
    SnowflakeId poEmployeeId,
    String poName,
    SnowflakeId smEmployeeId,
    String smName,
    List<SnowflakeId> memberEmployeeIds,
    boolean removed) {

  /** 現在の ScrumTeam の状態からイベントを生成します。 */
  public static ScrumTeamChanged of(ScrumTeam scrumTeam) {
    return new ScrumTeamChanged(
        scrumTeam.getId(),
        scrumTeam.getProduct() != null ? scrumTeam.getProduct().getId() : null,
        scrumTeam.getName(),
        scrumTeam.getBusinessDomain(),
        scrumTeam.getPoEmployeeId(),
        scrumTeam.getPoName(),
        scrumTeam.getSmEmployeeId(),
        scrumTeam.getSmName(),
        scrumTeam.getMemberEmployeeIds(),
        false);
  }

  /** 削除を表すイベントを生成します。 */
  public static ScrumTeamChanged removed(ScrumTeam scrumTeam) {
    ScrumTeamChanged snapshot = of(scrumTeam);
    return new ScrumTeamChanged(
        snapshot.scrumTeamId(),
        snapshot.productId(),
        snapshot.name(),
        snapshot.businessDomain(),
        snapshot.poEmployeeId(),
        snapshot.poName(),
        snapshot.smEmployeeId(),
        snapshot.smName(),
        snapshot.memberEmployeeIds(),
        true);
  }
}
