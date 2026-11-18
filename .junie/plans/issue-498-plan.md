# Issue #498 実装計画 — ACCEPTED→READY 状態遷移と READY→SUGGESTED 戻し

## 1. 概要

バックログアイテムに新状態「READY（準備完了）」を追加し、以下の状態遷移を新規導入する。

- **ACCEPTED → READY**（#620 の決定：スクラムチームメンバー全員が実行可能）
- **READY → SUGGESTED**（#621 の決定：READY から SUGGESTED への戻しを許可）

> **注記（#621 差異）**: Issue 本文には「Accepted からの戻りのみ」とあったが、後の決定 #621 により「READY から SUGGESTED への戻しを許可」が正である。この差異を本計画書に明記する。

> **注記（未決定事項）**: READY のタイムアウト処理は未決定のため、本実装では扱わない。

## 2. 決定事項のまとめ

| 項目 | 決定内容 | 出典 |
|------|---------|------|
| READY 遷移権限 | PO/SM を含むスクラムチームメンバー全員（所属で判定） | #620 |
| READY→SUGGESTED 戻し | 許可 | #621 |
| 楽観ロック | @Version + 409 応答で対応済み | #675 |
| 状態遷移方式 | 現行 changeStatus の明示的遷移表を拡張 | #584 |
| READY の削除 | 不可 | Issue 本文 |
| READY タイムアウト | 未決定（本実装では扱わない） | — |

## 3. 影響範囲

### 3.1 Backend

| モジュール/パッケージ | 変更種別 | 内容 |
|----------------------|---------|------|
| `requirementBacklog.spi.BacklogItemStatus` | 変更 | `READY` 定数を追加 |
| `requirementBacklog.spi.BacklogItem` | 変更 | `changeStatus()` に ACCEPTED→READY, READY→SUGGESTED を追加 |
| `requirementBacklog.internal.ScrumTeamSubset` | 変更 | メンバー従業員IDリスト（`List<SnowflakeId>`）を追加 |
| `requirementBacklog.internal.ScrumTeamSubsetRepository` | 変更 | 変更なし（エンティティ変更で自動対応） |
| `requirementBacklog.internal.ScrumTeamSubsetEventListener` | 変更 | `ScrumTeamChanged` イベントのメンバーIDリストをサブセットに反映 |
| `requirementBacklog.internal.ChangeBacklogItemStatusCommandService` | 変更 | READY 遷移の権限判定をチームメンバーに拡張 |
| `productDiscovery.spi.ScrumTeamChanged` | 変更 | メンバー従業員IDリスト（`List<SnowflakeId>`）を追加 |
| `productDiscovery.spi.ScrumTeam` | 変更 | メンバー従業員IDリストの保持（必要に応じて） |
| `productDiscovery.internal.*` | 変更 | ScrumTeam のメンバー管理とイベント発行時のメンバーIDリスト含め |
| Flyway マイグレーション | 新規 | READY 状態の CHECK 制約追加 + scrum_team_subsets にメンバーID列追加 |

### 3.2 Frontend

| ファイル | 変更種別 | 内容 |
|---------|---------|------|
| `BacklogItemList.tsx` | 変更 | READY 状態の表示・遷移ボタン追加 |
| `BacklogItemDetail.tsx` | 変更 | READY 状態の表示・遷移ボタン追加 |
| `backlogItemService.ts` | 変更 | READY 状態の型定義追加 |
| `scrumTeamService.ts` | 変更 | メンバーIDリストの型定義追加（必要に応じて） |

## 4. 実装ステップ

### ステップ 1: ScrumTeamChanged イベントにメンバーIDリストを追加

**目的**: productDiscovery 領域から requirementBacklog 領域へ、スクラムチームのメンバー従業員IDをイベント連携で伝える。

**変更ファイル**:
- `productDiscovery/spi/ScrumTeamChanged.java`
  - `List<SnowflakeId> memberEmployeeIds` フィールドを追加
  - `of(ScrumTeam)` メソッドでメンバーIDリストをセット
  - `removed(ScrumTeam)` メソッドでもメンバーIDリストをセット

- `productDiscovery/spi/ScrumTeam.java`（必要に応じて）
  - メンバー従業員IDリストの保持フィールドを追加（`List<SnowflakeId> memberEmployeeIds`）
  - ゲッターを追加

- `productDiscovery/internal/presentation/ScrumTeamCommandController.java`
  - メンバー追加・削除のAPIエンドポイント追加（必要に応じて）
  - `ScrumTeamChanged` 発行時にメンバーIDリストを含める

**Flyway マイグレーション**:
- `V20261119220000__add_member_employee_ids_to_scrum_teams.sql`
  - `undecided_ag.scrum_teams` にメンバーID関連テーブルまたはJSONB列を追加
  - 既存データへの影響なし（NULL 許容）

### ステップ 2: ScrumTeamSubset にメンバー従業員IDリストを追加

**目的**: requirementBacklog 領域内で、チームメンバー判定に使用する。

**変更ファイル**:
- `requirementBacklog/internal/ScrumTeamSubset.java`
  - `List<SnowflakeId> memberEmployeeIds` フィールドを追加（PO/SM を含むメンバー全員のID）
  - `apply(ScrumTeamChanged)` メソッドでメンバーIDリストを更新
  - `from(ScrumTeamChanged)` メソッドでメンバーIDリストをセット
  - `isTeamMember(SnowflakeId actorEmployeeId)` メソッドを追加（メンバー判定用）

**Flyway マイグレーション**:
- `V20261119230000__add_member_employee_ids_to_scrum_team_subsets.sql`
  - `undecided_ag.scrum_team_subsets` に `member_employee_ids` 列（JSONB または配列表）を追加
  - 既存データへの影響なし（NULL 許容）

### ステップ 3: BacklogItemStatus に READY を追加

**変更ファイル**:
- `requirementBacklog/spi/BacklogItemStatus.java`
  - `READY("準備完了")` 定数を追加
  - KDoc の遷移ルール説明を更新

### ステップ 4: BacklogItem.changeStatus() に新遷移を追加

**変更ファイル**:
- `requirementBacklog/spi/BacklogItem.java`
  - `changeStatus()` メソッドの switch 文に以下のケースを追加:
    - `ACCEPTED` 状態から:
      - `READY` へ遷移可能
    - `READY` 状態から:
      - `SUGGESTED` へ遷移可能（#621 の決定）
  - KDoc の遷移ルール説明を更新

**遷移表（拡張後）**:

| 現在状態 | 遷移先 | 権限 | 条件 |
|---------|--------|------|------|
| SUGGESTED | ACCEPTED | PO / SM | Value が設定されていること |
| SUGGESTED | FROZEN | PO / SM | — |
| ACCEPTED | READY | チームメンバー全員 | — |
| READY | SUGGESTED | チームメンバー全員 | — |
| FROZEN | SUGGESTED | PO のみ | — |

### ステップ 5: ChangeBacklogItemStatusCommandService の権限判定を拡張

**変更ファイル**:
- `requirementBacklog/internal/ChangeBacklogItemStatusCommandService.java`
  - ACCEPTED→READY 遷移の場合: `scrumTeam.isTeamMember(actorEmployeeId)` で判定
  - READY→SUGGESTED 遷移の場合: `scrumTeam.isTeamMember(actorEmployeeId)` で判定
  - 既存の SUGGESTED→ACCEPTED, SUGGESTED→FROZEN, FROZEN→SUGGESTED の権限判定は変更なし

### ステップ 6: Flyway で READY 状態の CHECK 制約を追加

**新規ファイル**:
- `V20261119240000__add_ready_status_to_backlog_items.sql`

```sql
-- READY 状態を CHECK 制約に追加します。
-- 既存データへの影響はありません（既存の SUGGESTED/ACCEPTED/FROZEN はそのまま有効）。

ALTER TABLE undecided_ag.backlog_items DROP CONSTRAINT ck_backlog_items_status;
ALTER TABLE undecided_ag.backlog_items
    ADD CONSTRAINT ck_backlog_items_status CHECK (status IN ('SUGGESTED', 'ACCEPTED', 'FROZEN', 'READY'));

COMMENT ON COLUMN undecided_ag.backlog_items.status IS '状態（SUGGESTED／ACCEPTED／FROZEN／READY）';
```

**既存データ影響確認手順**:
1. マイグレーション適用前に `SELECT status, COUNT(*) FROM undecided_ag.backlog_items GROUP BY status;` で現状を確認
2. マイグレーション適用後に同クエリでデータが変化していないことを確認
3. CHECK 制約が正しく適用されていることを確認: `SELECT constraint_name, constraint_type, search_condition FROM information_schema.check_constraints WHERE table_name = 'backlog_items';`

### ステップ 7: Frontend の表示制御

**変更ファイル**:
- `frontend/src/services/backlogItemService.ts`
  - `BacklogItemStatus` 型に `'READY'` を追加

- `frontend/src/pages/products/BacklogItemList.tsx`
  - `STATUS_LABELS` に `READY: '準備完了'` を追加
  - `STATUS_COLORS` に `READY: 'primary'`（適宜）を追加
  - 状態フィルタに `READY` を追加
  - **READY 遷移ボタン（ACCEPTED→READY）**: スクラムチームメンバーである場合にのみ表示
    - 判定: `team.memberEmployeeIds` に `currentEmployeeId` が含まれるか
    - 最終判定は backend で行う（frontend はUX最適化のみ）
  - **SUGGESTED 戻しボタン（READY→SUGGESTED）**: スクラムチームメンバーである場合にのみ表示
  - `canChangeStatus` の判定ロジックを `isTeamMember` に拡張

- `frontend/src/pages/products/BacklogItemDetail.tsx`
  - 同様に READY 状態の表示と遷移ボタンを追加

- `frontend/src/services/scrumTeamService.ts`
  - `ScrumTeam` インターフェースに `memberEmployeeIds?: string[]` を追加

## 5. テストリスト

### 5.1 BacklogItemStatus（値オブジェクト）

| No. | テストケース | 種類 |
|-----|-------------|------|
| 1 | READY 定数が存在し、ラベルが「準備完了」であること | small |
| 2 | READY の label() が「準備完了」を返すこと | small |

### 5.2 BacklogItem.changeStatus()（エンティティ）

| No. | テストケース | 種類 |
|-----|-------------|------|
| 3 | ACCEPTED 状態から READY へ遷移できること | small |
| 4 | READY 状態から SUGGESTED へ遷移できること | small |
| 5 | SUGGESTED 状態から READY へ直接遷移できないこと（IllegalStateException） | small |
| 6 | READY 状態から ACCEPTED へ遷移できないこと（IllegalStateException） | small |
| 7 | READY 状態から FROZEN へ遷移できないこと（IllegalStateException） | small |
| 8 | FROZEN 状態から READY へ遷移できないこと（IllegalStateException） | small |
| 9 | ACCEPTED 状態から SUGGESTED へ遷移できないこと（IllegalStateException） | small |
| 10 | READY 状態から READY へ遷移できないこと（IllegalStateException） | small |
| 11 | 既存遷移 SUGGESTED→ACCEPTED（Value あり）が引き続き機能すること | small |
| 12 | 既存遷移 SUGGESTED→FROZEN が引き続き機能すること | small |
| 13 | 既存遷移 FROZEN→SUGGESTED が引き続き機能すること | small |
| 14 | changeStatus() 後に updatedAt が更新されること（READY 遷移時） | small |

### 5.3 ScrumTeamSubset（サブセット）

| No. | テストケース | 種類 |
|-----|-------------|------|
| 15 | isTeamMember() が PO の従業員IDに対して true を返すこと | small |
| 16 | isTeamMember() が SM の従業員IDに対して true を返すこと | small |
| 17 | isTeamMember() がメンバーリストに含まれる従業員IDに対して true を返すこと | small |
| 18 | isTeamMember() がメンバーリストに含まれない従業員IDに対して false を返すこと | small |
| 19 | isTeamMember() が null の従業員IDに対して false を返すこと | small |
| 20 | memberEmployeeIds が空リストの場合、PO/SM のみが isTeamMember で true となること | small |
| 21 | apply() で ScrumTeamChanged イベントのメンバーIDリストが反映されること | small |
| 22 | from() で ScrumTeamChanged イベントのメンバーIDリストがセットされること | small |

### 5.4 ChangeBacklogItemStatusCommandService（コマンドサービス）

| No. | テストケース | 種類 |
|-----|-------------|------|
| 23 | PO が ACCEPTED を READY に変更できること | small |
| 24 | SM が ACCEPTED を READY に変更できること | small |
| 25 | チームメンバー（PO/SM 以外）が ACCEPTED を READY に変更できること | small |
| 26 | チーム外の従業員が ACCEPTED を READY に変更しようとすると権限例外になること | small |
| 27 | PO が READY を SUGGESTED に戻せること | small |
| 28 | SM が READY を SUGGESTED に戻せること | small |
| 29 | チームメンバー（PO/SM 以外）が READY を SUGGESTED に戻せること | small |
| 30 | チーム外の従業員が READY を SUGGESTED に戻そうとすると権限例外になること | small |
| 31 | ACCEPTED→READY 遷移時に BacklogItemStatusChanged イベントが発行されること | small |
| 32 | READY→SUGGESTED 遷移時に BacklogItemStatusChanged イベントが発行されること | small |
| 33 | 権限がない場合、保存もイベント発行もされないこと（ACCEPTED→READY 試行時） | small |
| 34 | 既存の SUGGESTED→ACCEPTED 遷移が引き続き機能すること | small |
| 35 | 既存の FROZEN→SUGGESTED（PO のみ）が引き続き機能すること | small |

### 5.5 ScrumTeamChanged イベント

| No. | テストケース | 種類 |
|-----|-------------|------|
| 36 | ScrumTeamChanged に memberEmployeeIds が含まれること | small |
| 37 | of(ScrumTeam) で ScrumTeam のメンバーIDリストが正しくセットされること | small |
| 38 | removed(ScrumTeam) でもメンバーIDリストが含まれること | small |

### 5.6 ScrumTeamSubsetEventListener

| No. | テストケース | 種類 |
|-----|-------------|------|
| 39 | ScrumTeamChanged イベント受信時に memberEmployeeIds がサブセットに反映されること | small |
| 40 | 新規チームのイベント受信時に from() で memberEmployeeIds がセットされること | small |

### 5.7 統合テスト（medium）

| No. | テストケース | 種類 |
|-----|-------------|------|
| 41 | DB に対して ACCEPTED→READY 遷移が正しく永続化されること | medium |
| 42 | DB に対して READY→SUGGESTED 遷移が正しく永続化されること | medium |
| 43 | CHECK 制約が READY を許可すること | medium |
| 44 | 楽観ロック（@Version）により同時更新で 409 が返ること（既存機能の確認） | medium |

### 5.8 Frontend テスト

| No. | テストケース | 種類 |
|-----|-------------|------|
| 45 | READY 状態のラベルが「準備完了」として表示されること | small |
| 46 | ACCEPTED のアイテムに READY 遷移ボタンが表示されること（チームメンバーの場合） | small |
| 47 | READY のアイテムに SUGGESTED 戻しボタンが表示されること（チームメンバーの場合） | small |
| 48 | チームメンバーでない場合は READY 遷移ボタンが表示されないこと | small |
| 49 | 状態フィルタで READY が選択できること | small |

## 6. モジュール境界の確認

| 境界 | 確認事項 |
|------|---------|
| productDiscovery → requirementBacklog | `ScrumTeamChanged` イベントのみで連携。メンバーIDリストの追加は SPI の拡張であり、モジュール境界を破らない |
| requirementBacklog 内部 | `ScrumTeamSubset` は `internal` パッケージにあり、パッケージプライベート。`BacklogItem` は `spi` にあり public |
| Spring Modulith 検証 | `ApplicationModules.verify()` が引き続き通ること |

## 7. リスクと対応

| リスク | 影響 | 対応 |
|-------|------|------|
| ScrumTeam にメンバー管理機能が未実装 | メンバーIDリストのデータ源が必要 | productDiscovery 領域で ScrumTeam のメンバー管理（追加・削除）を併せて実装するか、既存の従業員所属情報を活用する |
| 既存の ScrumTeamChanged イベントリスナーへの影響 | 他のリスナーがメンバーIDリストを処理できない | record の新しいフィールドは既存のコードに影響しない（後方互換） |
| Flyway マイグレーションの順序 | CHECK 制約の再定義が既存データを壊す | DROP → ADD の順で実行し、既存データはそのまま有効 |

## 8. 未決定事項

| 項目 | 状態 | 備考 |
|------|------|------|
| **READY のタイムアウト処理** | **未決定** | 本実装では扱わない。将来的に READY 状態のタイムアウト（例：スプリント開始時に自動遷移）を追加する可能性あり |
| READY 状態での削除可否 | 決定済み（不可） | 削除は SUGGESTED 状態のみ許可（既存ルールと同等） |
| READY→ACCEPTED 戻し | 未検討 | 本 Issue の範囲外。必要であれば別 Issue で対応 |

## 9. 作業順序（推奨）

1. **ステップ 1**: ScrumTeamChanged イベント拡張（productDiscovery SPI）
2. **ステップ 2**: ScrumTeamSubset 拡張（requirementBacklog internal）
3. **ステップ 3**: BacklogItemStatus に READY 追加
4. **ステップ 4**: BacklogItem.changeStatus() 拡張
5. **ステップ 5**: ChangeBacklogItemStatusCommandService 権限判定拡張
6. **ステップ 6**: Flyway マイグレーション（CHECK 制約 + メンバーID列）
7. **ステップ 7**: Frontend 表示制御
8. **テスト**: 上記テストリストに従い、small → medium の順で検証
9. **ArchUnit**: モジュール境界テストが通るか確認
10. **全テスト**: `./gradlew :backend:test` が通るか確認

## 10. 完了基準

- [ ] `./gradlew :backend:test` が全件パス
- [ ] `./gradlew :backend:mediumTest` が全件パス
- [ ] ArchUnit テストがパス（モジュール境界違反なし）
- [ ] Flyway マイグレーションが正常に適用され、既存データに影響なし
- [ ] Frontend で READY 状態の表示と遷移ボタンが正しく動作
- [ ] チームメンバー判定が PO/SM/一般メンバーのすべてで正しく機能
- [ ] 既存の遷移（SUGGESTED→ACCEPTED, SUGGESTED→FROZEN, FROZEN→SUGGESTED）が引き続き機能
