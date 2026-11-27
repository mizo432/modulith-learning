# Issue #757 計画：エンティティ内部の現在時刻取得を Clock 経由に統一する

## 実測した現状（2026-11-28、origin/develop da78a99c3 時点）
- backend main の `now()` は全113か所。うち bare な `.now()`（引数なし）は85か所：
  - spi のエンティティ18ファイル・78か所
  - internal のサブセット4ファイル・6か所
  - shared の ValidInterval.isActive()・1か所（呼び出し側は main／test ともに無し）
- 残りは #745 で Clock 化済み（`now(clock)`）。

## 方式（Issue 記載の候補に沿う）
時刻の確定はサービス／リスナー側へ寄せ、エンティティは受け取った時刻を保持するだけにする。

1. **変更メソッド**：`this.updatedAt = LocalDateTime.now()` 等があるドメインメソッドに `LocalDateTime now` 引数を追加する。呼び出し側（コマンドサービス等）は保持する Clock から `LocalDateTime.now(clock)` を渡す。エンティティ同士の呼び出しでは同じ now を伝播させる。
2. **生成（builder／factory）**：`@Builder.Default` の `= LocalDateTime.now()` を外し、生成側が `.createdAt(now).updatedAt(now)` を明示的に渡す。BacklogItem.build／Feature.build の「未設定なら現在時刻」フォールバックは廃止し、createdAt／updatedAt を必須（null は IllegalArgumentException）とする。Sprint.createSprintZero は now 引数を追加し、SprintZeroFactory（spi）と実装・呼び出し側へ波及させる。
3. **サブセット（射影）**：apply(event)／from(event)／of(id) に now 引数を追加し、イベントリスナーが Clock を注入して渡す。
4. **ValidInterval**：isActive() を isActive(LocalDate date) へ変更する（呼び出し側なし）。

## 対象ファイル（23ファイル）
- productDiscovery/spi：Product（6）、ScrumTeam（9）
- requirementBacklog/spi：BacklogItem（16）、Feature（4）、FeatureBacklogItem（1）、BacklogItemComment（1）、BacklogItemDocument（1）、BacklogItemDependency（1）
- sprintExecution/spi：Sprint（11）、SprintBacklogItem（5）、SprintSettings（5）、Impediment（6）、SprintGoal（3）、Notification（3）、Dependency（3）、Activity（1）
- notification/spi：NotificationEntity（3）
- jobmanagement/permissionmanagement/spi：JobAdminEmployeeSubset（1）
- requirementBacklog/internal：ScrumTeamSubset（2）、AdminEmployeeSubset（2）、CommentParticipantSubset（1）
- notification/internal：NotificationCommentParticipantSubset（1）
- shared：ValidInterval（1）
- 波及：上記を生成・変更するコマンドサービス、イベントリスナー、SprintZeroFactory 実装、およびテストの呼び出し側

## テストリスト（「〜したとき、〜となる」形式）
1. 固定 Clock を持つ CreateProductCommandService で Product を作成したとき、createdAt と updatedAt が固定時刻となる
2. Product の update／archive／delete に時刻を渡したとき、updatedAt（delete 時は deletedAt も）が渡した時刻となる
3. BacklogItem の状態遷移メソッドに時刻を渡したとき、updatedAt が渡した時刻となる
4. BacklogItem の builder で createdAt／updatedAt を渡さなかったとき、例外となる
5. Sprint.createSprintZero に時刻を渡したとき、createdAt と updatedAt がその時刻となる
6. ScrumTeamSubset の from／apply に時刻を渡したとき、updatedAt が渡した時刻となる
7. JobAdminEmployeeSubset.of に時刻を渡したとき、updatedAt が渡した時刻となる
8. ValidInterval.isActive に日付を渡したとき、その日付で有効判定となる
9. （回帰）backend small 全体と medium 全体（シャード0／1）が成功する

## 進め方
- write_tests：新シグネチャに対するテストを作成し、最小の API 面（引数を受けるが本体は従来どおり now() を使う）で Red を項目ごとに確認する
- implement：ファイル群ごとに本体を引数の時刻へ切り替え、対象テストの Green を確認しながら進める
- 検証：backend small 全体、medium シャード0／1 を自分で実測する。完了条件は対象ファイル群に bare な `.now()` が残っていないこと
