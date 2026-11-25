# Issue #540 実装計画（依存関係設定：#541 backend / #542 frontend）

## 決定前提

- 依存は直接の関係のみ。充足判定は直接の依存先だけを見る（#665）
- 依存に条件種類は持たない。意味は「依存先が完了するまで着手できない」に固定（#666）
- 表示はリスト形式。詳細画面に依存関係セクション、一覧画面は件数バッジのみ（#667）
- 依存未充足でも Ready・見積もり・スプリント割り当ては可能。制限は着手のみ（#668、2026-11-25にユーザー確認）。着手（IN_PROGRESS遷移）は未実装のため、本Issueでは充足判定と警告までを実装し、開始時のブロックは開始操作の実装時に本判定を使う
- 循環（相互依存を含む）は検出して禁止（#670）
- 追加・削除の権限は PO・SM のみ（#541本文）。判定は ScrumTeamSubset の isPo／isSm を使う
- 依存関係の変更通知は送らない。追加時に EVT-BL-014「依存関係が定義された」に対応する spi イベントを発行する（UC-BL-019）
- RDRA の UC-BL-019 を上記決定で更新する

## Backend（#541）

1. マイグレーション `db/migration/ag/V20261125110000__add_backlog_item_dependencies_table.sql`：`undecided_ag.backlog_item_dependencies`（id、backlog_item_id、dependency_item_id、created_by、created_at、unique(backlog_item_id, dependency_item_id)）
2. spi に `BacklogItemDependency` エンティティ（アーキテクチャルールで @Entity は spi に配置。BacklogItemDocument と同じ形）、internal に `BacklogItemDependencyRepository`（findByBacklogItemId、findByDependencyItemId、findByBacklogItemIdAndDependencyItemId）
3. `AddBacklogItemDependencyCommandService`：存在確認、同一チーム確認、自己依存・重複の拒否、循環検出（依存先から依存をたどる DFS で依存元に到達したら拒否。相互依存を含む）、PO/SM 権限、保存後に spi イベント `BacklogItemDependencyDefined` を発行
4. `RemoveBacklogItemDependencyCommandService`：PO/SM 権限、対象が無ければエラー
5. `BacklogItemDependencyQueryService` と GET `/api/v1/backlog-items/{id}/dependencies`：依存先（状態・充足つき）と依存元、全体の充足可否と未充足件数を返す
6. POST `/api/v1/backlog-items/{id}/dependencies`、DELETE `/api/v1/backlog-items/{id}/dependencies/{dependencyItemId}`（actorEmployeeId はリクエストボディ）
7. `BacklogItemResponse` に dependencyCount と dependenciesSatisfied を追加し、一覧・詳細の両方で返す
8. エラーは既存の例外ハンドラの規約に合わせる（権限 403、循環・重複は 409、その他は 400）
9. ScrumTeamSubset に isSm が無ければ追加する

## Frontend（#542）

1. backlogItemService に `BacklogItemDependencyInfo` 型と getDependencies／addDependency／removeDependency を追加。BacklogItem 型に dependencyCount／dependenciesSatisfied を追加
2. BacklogItemDetail に「依存関係」セクション：依存先・依存元を分けてリスト表示（タイトルリンクと状態 Chip）、未充足時は着手できない旨の警告 Alert
3. PO/SM のときだけ追加 UI（同一チームのアイテムから選択）と削除ボタンを表示。追加・削除後は再読み込み
4. BacklogItemList の行に依存件数バッジ（依存がある場合のみ）

## テストリスト

### Backend

1. POが依存関係を追加したとき、依存関係が保存され、定義イベントが発行される
2. SMが依存関係を追加したとき、依存関係が保存される
3. POでもSMでもないメンバーが依存関係を追加したとき、権限エラーとなり保存されない
4. 自分自身への依存を追加したとき、エラーとなり保存されない
5. 同じ依存を重複して追加したとき、エラーとなり保存されない
6. 相互依存になる依存を追加したとき、循環エラーとなり保存されない
7. 連鎖の先で循環する依存を追加したとき、循環エラーとなり保存されない
8. 別のスクラムチームのアイテムへの依存を追加したとき、エラーとなり保存されない
9. POが依存関係を削除したとき、依存関係が削除される
10. POでもSMでもないメンバーが依存関係を削除したとき、権限エラーとなり削除されない
11. 依存先がすべてDONEであるとき、依存は充足していると判定される
12. 依存先にDONEでないものがあるとき、依存は未充足と判定される
13. 依存関係を取得したとき、依存先と依存元が状態と充足可否つきで返る
14. バックログアイテムのレスポンスを取得したとき、依存件数と充足フラグが実際の依存関係を反映している

### Frontend

15. 詳細画面を開いたとき、依存先と依存元が状態バッジつきでリスト表示される
16. 依存先に未完了があるとき、着手できない旨の警告が表示される
17. POまたはSMで詳細画面を開いたとき、依存の追加・削除UIが表示される
18. POでもSMでもないメンバーで詳細画面を開いたとき、追加・削除UIが表示されない
19. 依存を追加したとき、依存関係が再読み込みされ一覧に反映される
20. 依存を削除したとき、依存関係が再読み込みされ一覧から消える
21. 一覧画面で依存関係のあるアイテムには依存件数のバッジが表示される
