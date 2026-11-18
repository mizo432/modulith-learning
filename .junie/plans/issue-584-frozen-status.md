---
sessionId: session-261118-093505-1ofo
---

# Delivery Plan

### Stage 1: Backend — BacklogItemStatus の FROZEN 置換と状態遷移ルールの更新

BacklogItemStatus enum と BacklogItem.changeStatus() を FROZEN 対応に更新し、関連テストを更新。

- `BacklogItemStatus.java`: `REJECTED("却下")` → `FROZEN("凍結")` に置換、javadoc 更新
- `BacklogItem.changeStatus()`: 明示的な状態遷移表（switch-case）に書き換え。許可: SUGGESTED→ACCEPTED, SUGGESTED→FROZEN, FROZEN→SUGGESTED
- `BacklogItemTest.java`: REJECTED 参照を FROZEN へ置換、FROZEN→SUGGESTED 新テスト追加
- `ChangeBacklogItemStatusCommandServiceTest.java`: REJECTED 参照を FROZEN へ置換

### Stage 2: Backend — 非対称権限判定とコマンドサービスの更新

ScrumTeamSubset に PO 判定メソッドを追加し、コマンドサービスで遷移先に応じた権限判定を実装。

- `ScrumTeamSubset.java`: `isPo(SnowflakeId)` メソッド追加（パッケージプライベート）
- `ChangeBacklogItemStatusCommandService.java`: 遷移先状態に応じて権限判定を分岐（FROZEN→SUGGESTED は PO のみ、その他は PO/SM）
- `ChangeBacklogItemStatusCommandServiceTest.java`: FROZEN→SUGGESTED の PO 許可テスト、SM 拒否テスト、一般メンバー拒否テストを追加

### Stage 3: Backend — Flyway マイグレーションと DB 制約の更新

CHECK 制約の再定義と既存データ移行のマイグレーションを作成。

- 新規 `V20261117190000__replace_rejected_with_frozen.sql` を作成
- 既存 CHECK 制約 `ck_backlog_items_status` の DROP
- `status = 'REJECTED'` の行を `status = 'FROZEN'` へ UPDATE
- 新規 CHECK 制約の ADD（`'SUGGESTED', 'ACCEPTED', 'FROZEN'`）
- COMMENT の更新

### Stage 4: Frontend — 状態ラベル・ボタン・権限の更新

Frontend の状態表示、操作ボタン、PO 権限判定を更新。

- `backlogItemService.ts`: `BacklogItemStatus` 型の `'REJECTED'` → `'FROZEN'`
- `BacklogItemList.tsx`: STATUS_LABELS/STATUS_COLORS 更新、SUGGESTED 状態に「凍結」ボタン、FROZEN 状態に PO のみ「SUGGESTEDへ戻す」ボタン、`isPo` 判定を追加
- `BacklogItemDetail.tsx`: STATUS_LABELS/STATUS_COLORS 更新
- `BacklogItemList.test.tsx`: REJECTED → FROZEN 置換、凍結ボタンテスト、PO のみ復帰ボタンテストを追加
- `BacklogItemDetail.test.tsx`: REJECTED → FROZEN 置換


# Delivery Steps

###   Step 1: Backend — BacklogItemStatus FROZEN 置換と状態遷移ルール更新
BacklogItemStatus enum と BacklogItem.changeStatus() を FROZEN 対応に更新し、関連テストを更新。
- BacklogItemStatus.java: REJECTED(却下) → FROZEN(凍結) に置換、javadoc 更新
- BacklogItem.changeStatus(): 明示的な状態遷移表（switch-case）に書き換え。許可: SUGGESTED→ACCEPTED, SUGGESTED→FROZEN, FROZEN→SUGGESTED
- BacklogItemTest.java: REJECTED 参照を FROZEN へ置換、FROZEN→SUGGESTED 新テスト追加
- ChangeBacklogItemStatusCommandServiceTest.java: REJECTED 参照を FROZEN へ置換

###   Step 2: Backend — 非対称権限判定とコマンドサービス更新
ScrumTeamSubset に PO 判定メソッドを追加し、コマンドサービスで遷移先に応じた権限判定を実装。
- ScrumTeamSubset.java: isPo(SnowflakeId) メソッド追加（パッケージプライベート）
- ChangeBacklogItemStatusCommandService.java: 遷移先状態に応じて権限判定を分岐（FROZEN→SUGGESTED は PO のみ、その他は PO/SM）
- ChangeBacklogItemStatusCommandServiceTest.java: FROZEN→SUGGESTED の PO 許可テスト、SM 拒否テスト、一般メンバー拒否テストを追加

###   Step 3: Backend — Flyway マイグレーションと DB 制約更新
CHECK 制約の再定義と既存データ移行のマイグレーションを作成。
- 新規 V20261117190000__replace_rejected_with_frozen.sql を作成
- 既存 CHECK 制約 ck_backlog_items_status の DROP
- status = 'REJECTED' の行を status = 'FROZEN' へ UPDATE
- 新規 CHECK 制約の ADD（'SUGGESTED', 'ACCEPTED', 'FROZEN'）
- COMMENT の更新

###   Step 4: Frontend — 状態ラベル・ボタン・権限の更新
Frontend の状態表示、操作ボタン、PO 権限判定を更新。
- backlogItemService.ts: BacklogItemStatus 型の 'REJECTED' → 'FROZEN'
- BacklogItemList.tsx: STATUS_LABELS/STATUS_COLORS 更新、SUGGESTED 状態に「凍結」ボタン、FROZEN 状態に PO のみ「SUGGESTEDへ戻す」ボタン、isPo 判定を追加
- BacklogItemDetail.tsx: STATUS_LABELS/STATUS_COLORS 更新
- BacklogItemList.test.tsx: REJECTED → FROZEN 置換、凍結ボタンテスト、PO のみ復帰ボタンテストを追加
- BacklogItemDetail.test.tsx: REJECTED → FROZEN 置換
---

## 判定（Muse、2026-11-18）

採用。実装は以下の条件を守ること。

1. changeStatus の書き換えでは、#490 で追加した「ACCEPTED への遷移時に Value が必須」のチェックを必ず維持すること。
2. BacklogItemMigrationMediumTest に、新マイグレーション V20261117190000 のスクリプト検証テストを追加すること。既存の CHECK 制約検証（V20261116150000 対象）は変更しない。検証では :backend:test と :backend:mediumTest の両方を実行すること。
3. frontend の状態フィルタの選択肢も FROZEN に追随させること（ラベル定義由来で自動的に追随する場合は、その確認をもって可とする）。
4. 詳細画面（BacklogItemDetail）に状態変更の操作 UI がある場合は、一覧と同じルール（凍結ボタンの表示、復帰は PO のみ表示）を適用すること。無い場合は一覧のみでよい。
5. Flyway のバージョンは V20261117190000 で確定する（ag の既存最大は V20261117180000）。
