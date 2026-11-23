# Issue #519 実装計画：バックログアイテム分割

## 1. 概要

バックログアイテムを最大20件の子アイテムに分割する機能を実装する。分割は PO／SM の権限が必要。子アイテムは SUGGESTED 状態で作成され、各子アイテムのストーリーポイントは利用者が個別に入力する。元のアイテムは分割時に DONE 状態へ遷移する。

---

## 2. 決定事項（Issue 本文より優先）

| 項目 | 決定内容 |
|------|----------|
| 子アイテムの初期状態 | SUGGESTED |
| ストーリーポイントの検証 | 子アイテムの合計が元のポイントと等しいことの検証は**行わない**。利用者が個別に入力 |
| 分割数の上限 | 20件 |
| 添付ファイルの継承 | 対象外（添付機能 #501 が未実装） |
| 元のアイテムの遷移 | 分割時に DONE 状態へ遷移 |
| 権限 | PO と SM のみ |
| 分割対象 | ストーリーポイント 13 以上のアイテムのみ |

---

## 3. 既存コードの調査結果

### 3.1. BacklogItem の状態遷移の仕組み

**ファイル**: `backend/src/main/java/undecided/agiloom/requirementBacklog/spi/BacklogItem.java`

- `changeStatus(BacklogItemStatus target)` メソッドで状態遷移を検証・実行
- 現在の許可遷移:
  - SUGGESTED → ACCEPTED（Value 設定必須）
  - SUGGESTED → FROZEN
  - ACCEPTED → READY
  - READY → SUGGESTED
  - READY → ESTIMATED（StoryPoint 設定必須）
  - ESTIMATED → READY
  - FROZEN → SUGGESTED
- **DONE 状態は enum に定義済み**（`BacklogItemStatus.DONE`）だが、`changeStatus()` にはまだ遷移ルールが実装されていない
- 状態遷移には `BacklogItemStatusChanged` ドメインイベントを発行

**ファイル**: `backend/src/main/java/undecided/agiloom/requirementBacklog/spi/BacklogItemStatus.java`

- enum 値: SUGGESTED, ACCEPTED, FROZEN, READY, ESTIMATED, IN_PROGRESS, DONE
- DONE は既に定義済み（#525 で実装予定の enum）
- DB CHECK 制約にも DONE は含まれている（`V20261120200000__add_story_point_and_estimated_status_to_backlog_items.sql`）

**結論**: `changeStatus()` に `→ DONE` 遷移を追加する必要がある。どの状態から DONE へ遷移可能にするかは未決定（後述）。

### 3.2. StoryPoint 値オブジェクト

**ファイル**: `backend/src/main/java/undecided/agiloom/requirementBacklog/spi/StoryPoint.java`

- 許容値: `0, 1, 2, 3, 5, 8, 13, 21, 34, 55, 89`（フィボナッチ数列）
- `StoryPoint.of(Integer)` で検証。許容値以外の場合 `IllegalArgumentException`
- 定数: `MIN_VALUE = 0`, `MAX_VALUE = 89`
- 分割対象の条件「ストーリーポイント 13 以上」は `StoryPoint.ALLOWED_VALUES` から判定可能

**BacklogItem での使用**:
- `storyPoint` 列は `Integer` 型（nullable）
- `setStoryPoint(Integer)` メソッドで `StoryPoint.of()` による検証後、フィールドに設定
- `changeStatus()` の ESTIMATED 遷移時にも `StoryPoint.of()` で検証

### 3.3. 通知モジュールの SPI が分割通知に使えるか

**ファイル**: `backend/src/main/java/undecided/notification/spi/NotificationEntity.java`

- フィールド: id, recipientId, backlogItemId, commentId, title, content, type, isRead, createdAt, updatedAt, version
- `commentId` はコメント通知専用。分割通知には不適合

**ファイル**: `backend/src/main/java/undecided/notification/spi/NotificationType.java`

- 現在の値: `COMMENT_ADDED`, `COMMENT_DELETED` のみ
- 分割通知用のタイプは存在しない

**ファイル**: `backend/src/main/java/undecided/notification/internal/BacklogItemCommentEventListener.java`

- `@ApplicationModuleListener` でドメインイベントを受信し、通知レコードを作成
- パターン: ドメインイベント → リスナー → NotificationEntity 保存 + メール送信

**結論**: 通知モジュールの既存 SPI はコメント通知専用であり、分割通知に直接使用するには以下の拡張が必要:
1. `NotificationType` enum に分割通知用の値（例: `BACKLOG_ITEM_SPLIT`）を追加
2. `NotificationEntity` の `commentId` は分割通知では null で OK（nullable 列）
3. 分割時にドメインイベント（例: `BacklogItemSplit`）を発行し、通知モジュールで `@ApplicationModuleListener` を追加
4. 通知対象者の特定には `ScrumTeamSubset` のメンバー情報を使用

**ただし**: 通知機能の拡張は本 Issue の必須要件かどうかは未決定。計画では分割自体の実装を優先し、通知連携はオプションとして記載する。

### 3.4. ScrumTeamSubset の PO／SM 判定パターン

**ファイル**: `backend/src/main/java/undecided/agiloom/requirementBacklog/internal/ScrumTeamSubset.java`

- `canChangeStatusBy(SnowflakeId)` → PO または SM なら true
- `isPo(SnowflakeId)` → PO なら true
- `isTeamMember(SnowflakeId)` → チームメンバー（PO/SM 含む）なら true

**ファイル**: `backend/src/main/java/undecided/agiloom/requirementBacklog/internal/ChangeBacklogItemStatusCommandService.java`

- 権限判定パターン: `scrumTeam.canChangeStatusBy(command.getActorEmployeeId())` で PO/SM 判定
- 権限なしの場合: `BacklogItemPermissionDeniedException` をスロー

**結論**: 分割機能の権限判定は既存の `canChangeStatusBy()` パターンを踏襲する。

### 3.5. コマンドサービスのパターン

- **Command DTO**: Lombok `@Data @Builder @NoArgsConstructor @AllArgsConstructor`、パッケージプライベート
- **Request DTO**: Lombok `@Data @Builder` + Jakarta Validation (`@NotBlank` 等)、パッケージプライベート
- **CommandService**: `@Service @Transactional`、パッケージプライベート、`execute(Command)` メソッド
- **Controller**: `@RestController`、パッケージプライベート、`ResponseEntity<BacklogItemResponse>` 返却
- **イベント発行**: `ApplicationEventPublisher.publishEvent()` でドメインイベント発行

### 3.6. DB 制約

**ファイル**: `backend/src/main/resources/db/migration/ag/V20261116150000__add_backlog_items_tables.sql`

- `backlog_items` テーブルに `parent_item_id` 列は存在しない → 追加が必要
- FK 制約はモジュール内に閉じる（`scrum_team_subsets` 参照のみ）

---

## 4. 変更ファイル一覧

### 4.1. 新規作成ファイル

| ファイル | 説明 |
|----------|------|
| `internal/SplitBacklogItemCommand.java` | 分割コマンド DTO |
| `internal/SplitBacklogItemRequest.java` | 分割リクエスト DTO |
| `internal/SplitBacklogItemCommandService.java` | 分割コマンドサービス |
| `spi/BacklogItemSplit.java` | 分割完了ドメインイベント |
| `internal/SplitBacklogItemChildRequest.java` | 子アイテム1件分のリクエスト DTO |
| `db/migration/ag/V20261121100000__add_parent_item_id_to_backlog_items.sql` | parent_item_id 列追加マイグレーション |
| `test/.../internal/SplitBacklogItemCommandServiceTest.java` | 分割コマンドサービス単体テスト |
| `test/.../spi/BacklogItemSplitTransitionTest.java` | DONE 遷移テスト |

### 4.2. 修正ファイル

| ファイル | 変更内容 |
|----------|----------|
| `spi/BacklogItem.java` | `parentItemId` フィールド追加、`→ DONE` 遷移追加、ビルダー修正 |
| `internal/BacklogItemRepository.java` | `findByParentItemId` クエリ追加 |
| `internal/BacklogItemCommandController.java` | 分割エンドポイント追加 |
| `internal/BacklogItemResponse.java` | `parentItemId` フィールド追加 |
| `internal/BacklogItemResponseConverter.java` | `parentItemId` 変換追加 |
| `spi/BacklogItemStatus.java` | Javadoc 更新（DONE 遷移の記載追加） |
| `notification/spi/NotificationType.java` | （オプション）分割通知タイプ追加 |
| `notification/internal/BacklogItemCommentEventListener.java` または新規リスナー | （オプション）分割通知リスナー追加 |

---

## 5. 段階分け

### 段階 1: DB スキーマ拡張

- `parent_item_id BIGINT NULL` 列を `backlog_items` テーブルに追加
- インデックス追加: `idx_backlog_items_parent_item_id`
- FK 制約追加: `fk_backlog_items_parent` → `undecided_ag.backlog_items(id)`（自己参照）

### 段階 2: エンティティ層変更

- `BacklogItem` に `parentItemId` フィールド追加（nullable）
- `BacklogItem.changeStatus()` に `→ DONE` 遷移を追加
  - どの状態から DONE へ遷移可能にするか（未決定事項参照）
- ビルダーに `parentItemId` パラメータ追加
- `BacklogItemRepository` に `findByParentItemId(SnowflakeId)` メソッド追加

### 段階 3: 分割コマンド実装

- `SplitBacklogItemCommand` / `SplitBacklogItemRequest` / `SplitBacklogItemChildRequest` 作成
- `SplitBacklogItemCommandService` 実装:
  1. 対象アイテムの取得
  2. 権限チェック（`ScrumTeamSubset.canChangeStatusBy()`）
  3. 分割対象条件チェック（storyPoint >= 13）
  4. 子アイテム数の上限チェック（<= 20）
  5. 子アイテムの作成（SUGGESTED 状態、parentItemId 設定、storyPoint は null）
  6. 元のアイテムを DONE へ遷移
  7. 保存
  8. `BacklogItemSplit` イベント発行

### 段階 4: API エンドポイント実装

- `BacklogItemCommandController` に分割エンドポイント追加
  - `POST /api/v1/backlog-items/{backlogItemId}/split`
  - リクエストボディ: 子アイテムのリスト（タイトルなど）
  - レスポンス: 分割後の元のアイテム + 作成された子アイテムのリスト

### 段階 5: レスポンス拡張

- `BacklogItemResponse` に `parentItemId` フィールド追加
- `BacklogItemResponseConverter` で変換処理追加

### 段階 6: テスト実装

- 単体テスト（small タグ）: コマンドサービスの各検証パス
- 統合テスト（medium タグ）: DB 連携での分割フロー
- 境界値テスト: 分割数 20 件、storyPoint 13 の境界

### 段階 7: （オプション）通知連携

- `BacklogItemSplit` ドメインイベントの定義
- 通知モジュールにリスナー追加
- `NotificationType` に分割通知タイプ追加

---

## 6. テストリスト

### 6.1. 分割コマンドの権限検証

1. **POが分割したとき、成功すること**
2. **SMが分割したとき、成功すること**
3. **チームメンバー（PO/SM以外）が分割したとき、BacklogItemPermissionDeniedExceptionがスローされること**
4. **チーム外の従業員が分割したとき、BacklogItemPermissionDeniedExceptionがスローされること**

### 6.2. 分割対象条件の検証

5. **ストーリーポイント13のアイテムを分割したとき、成功すること**
6. **ストーリーポイント21のアイテムを分割したとき、成功すること**
7. **ストーリーポイント8のアイテムを分割したとき、IllegalArgumentExceptionがスローされること**
8. **ストーリーポイントが未設定のアイテムを分割したとき、IllegalArgumentExceptionがスローされること**

### 6.3. 分割数の検証

9. **1件の子アイテムで分割したとき、成功すること**
10. **20件の子アイテムで分割したとき、成功すること**
11. **21件の子アイテムで分割したとき、IllegalArgumentExceptionがスローされること**
12. **0件の子アイテムで分割したとき、IllegalArgumentExceptionがスローされること**

### 6.4. 子アイテムの作成

13. **分割したとき、子アイテムはSUGGESTED状態で作成されること**
14. **分割したとき、子アイテムのparentItemIdは元のアイテムのIDに設定されること**
15. **分割したとき、子アイテムのscrumTeamIdは元のアイテムと同じチームに設定されること**
16. **分割したとき、子アイテムのstoryPointはnullであること**
17. **分割したとき、子アイテムのタイトルはリクエストで指定された値に設定されること**

### 6.5. 元のアイテムの遷移

18. **分割したとき、元のアイテムはDONE状態に遷移すること**
19. **分割したとき、BacklogItemStatusChangedイベントが発行されること**

### 6.6. 存在しないアイテム

20. **存在しないアイテムIDで分割したとき、IllegalArgumentExceptionがスローされること**

### 6.7. DONE 状態遷移

21. **DONE状態のアイテムはさらに分割できないこと**（DONE 以降の遷移は許可しない）

### 6.8. 統合テスト（medium タグ）

22. **DB連携で分割したとき、子アイテムがDBに保存されること**
23. **DB連携で分割したとき、元のアイテムのstatusがDONEに更新されること**
24. **parentItemIdで子アイテムを検索したとき、正しい子アイテムが返ること**

---

## 7. 未決定事項

1. **DONE への遷移元状態**: どの状態から DONE へ遷移可能にするか。分割時のみ DONE へ遷移させるか、それとも他のユースケース（例: IN_PROGRESS → DONE）でも遷移可能にするか。
2. **子アイテムの継承項目**: タイトル以外に、説明（description）、優先度（priority）、Value、sprintId などのどの項目を子アイテムに継承するか（または継承しないか）。
3. **分割通知の必須性**: 分割時に通知モジュールを介した通知を発行するかどうか。必須要件かオプションか。
4. **子アイテムのストーリーポイント入力タイミング**: 分割時に同時に子アイテムのポイントを入力させるか、分割後はすべて null で、利用者が後から個別に設定するか（Issue 決定事項では「利用者が個別に入力」とあるが、分割 API での同時入力の可否）。
5. **分割後の子アイテムの操作**: 子アイテム作成後、レスポンスに子アイテムのリストを含めるか、それとも元のアイテムのみを返すか。
6. **DONE 状態からの遷移**: DONE になったアイテムは、再度他の状態へ遷移可能か（例: 再分割や修正）。
7. **分割の取り消し**: 分割後の元アイテムを元の状態に戻す機能（逆操作）が必要か。
