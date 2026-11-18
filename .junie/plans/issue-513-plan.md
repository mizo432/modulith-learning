# Issue #513 実装計画

## 概要

開発者がReady状態のバックログアイテムを見積もり（StoryPoint）を入力し、Estimated状態へ遷移する機能を実装する。

## 設計方針

### 1. 状態モデルの拡張

- `BacklogItemStatus` enum に `ESTIMATED` を追加
- 状態遷移表の拡張:
  - `READY → ESTIMATED`（見積もり入力時。チームメンバーのみ）
  - `ESTIMATED → READY`（見積もり取り消し。#643で決定：見積もり値は保持）
  - `ESTIMATED → IN_PROGRESS`（#522で実装予定。本Issueではenum追加のみ）
  - `DONE` 状態も#525で実装予定。本Issueではenum追加のみ
- 完全なライフサイクル: `SUGGESTED → ACCEPTED → READY → ESTIMATED → IN_PROGRESS → Done`

### 2. 見積もり（StoryPoint）の定義

- `BacklogItem` エンティティに `storyPoint` フィールド（`Integer` 型、nullable）を追加
- 許容値: フィボナッチ数列 `0, 1, 2, 3, 5, 8, 13, 21, 34, 55, 89`（#516、#645で決定）
- StoryPoint 0を許可（#645で決定：見積もり不要なタスク）
- 値オブジェクト `StoryPoint` を `spi` パッケージに作成。許容値の検証・定数化を行う
- 項目制約の値は値オブジェクトの定数として定義（ADR-002準拠）

### 3. 状態遷移の条件

- `READY → ESTIMATED` 遷移時: `storyPoint` が有効な値で設定されていることを必須とする
- `ESTIMATED → READY` ロールバック時: 見積もり値はそのまま保持（#643で決定）
- Estimated状態での見積もり値の再評価（値変更）を許可（#642で決定）

### 4. 権限モデル

- 見積もり入力・状態遷移は**チームメンバーのみ**が実行可能（`ScrumTeamSubset.isTeamMember()` で判定）
- PO/SM 以外も実行可能（見積もりは開発チーム全体の活動）
- 権限判定は `ChangeBacklogItemStatusCommandService`（状態遷移）および新規コマンドサービス（見積もり更新）で行う

### 5. 見積もり更新API

- 状態遷移API（`PATCH /api/v1/backlog-items/{id}/status`）を拡張: `ESTIMATED` 遷移時に `storyPoint` を必須とする
- または専用の見積もりAPI（`PATCH /api/v1/backlog-items/{id}/estimate`）を新規追加
- 見積もり値の設定と状態遷移を一体化するか分離するかは**未決定**（後述）

### 6. 通知

- Issue本文では「見積もり完了時に利害関係者全員に通知」が記載されている
- 通知チャネルは#633で「メールおよびアプリ内スナックバー」と決定済み
- 通知宛先は#633で「コメント参加者、ただし操作した人は除く」と決定済み
- ただし、通知機能自体の実装は#507系で別途扱われる前提
- 本Issueでは `BacklogItemEstimated` イベント（または既存の `BacklogItemStatusChanged` イベントで十分か）の発行のみを行い、通知の実装は行わない

### 7. 監査ログ

- Issue本文では「見積もり値の履歴を追跡する監査ログ機能」が記載されている
- ただし、プロジェクト方針（Issue #572の決定）では「状態遷移の履歴はロングタームイベントパターンで担保し、専用の監査ログは持たない」
- 見積もり値の変更履歴は、専用の監査ログではなく、イベントパターンで担保するか、または別途設計Issueで決定する

### 8. DBマイグレーション

- `backlog_items` テーブルに `story_point` 列（`INTEGER`、nullable）を追加
- CHECK制約で許容値（フィボナッチ数列）のみを許可
- `status` CHECK制約に `ESTIMATED`（および `IN_PROGRESS`, `DONE`）を追加
- 既存データへの影響なし（`story_point = NULL` として処理）

### 9. モジュール境界

- 変更は `undecided.agiloom.requirementBacklog` 領域内に閉じる
- 通知連携が必要な場合は、イベント発行のみで他領域（sprintExecution）が受信する形とする

---

## 変更対象ファイル

### 新規作成

| ファイル | 説明 |
|---------|------|
| `spi/StoryPoint.java` | StoryPoint値オブジェクト（許容値定数・検証） |
| `spi/BacklogItemEstimated.java` | 見積もり完了イベント（または `BacklogItemStatusChanged` で十分か検討） |
| `internal/EstimateBacklogItemCommand.java` | 見積もりコマンドDTO |
| `internal/EstimateBacklogItemCommandService.java` | 見積もりコマンドサービス |
| `internal/EstimateBacklogItemRequest.java` | 見積もりリクエストDTO |
| `db/migration/ag/V*__add_story_point_to_backlog_items.sql` | Flywayマイグレーション |
| `test/.../spi/StoryPointTest.java` | StoryPoint値オブジェクトのテスト |
| `test/.../spi/BacklogItemEstimatedTransitionTest.java` | ESTIMATED遷移のエンティティテスト |
| `test/.../internal/EstimateBacklogItemCommandServiceTest.java` | 見積もりコマンドサービスのテスト |
| `test/.../internal/ChangeBacklogItemStatusCommandServiceEstimatedTest.java` | ESTIMATED関連の状態遷移サービステスト |

### 修正

| ファイル | 変更内容 |
|---------|---------|
| `spi/BacklogItemStatus.java` | `ESTIMATED`, `IN_PROGRESS`, `DONE` を追加 |
| `spi/BacklogItem.java` | `storyPoint` フィールド追加、`changeStatus()` に `READY→ESTIMATED`, `ESTIMATED→READY` を追加、`setStoryPoint()` メソッド追加 |
| `spi/BacklogItemStatusChanged.java` | 既存イベントで十分か検討。不十分な場合は拡張 |
| `internal/ChangeBacklogItemStatusCommandService.java` | `READY→ESTIMATED`, `ESTIMATED→READY` の権限判定を追加 |
| `internal/BacklogItemCommandController.java` | 見積もりAPIエンドポイント追加、状態遷移APIでESTIMATED対応 |
| `internal/BacklogItemResponse.java` | `storyPoint` フィールド追加 |
| `internal/BacklogItemResponseConverter.java` | `storyPoint` の変換追加 |
| `test/.../spi/BacklogItemTest.java` | ESTIMATED関連のテストケース追加 |
| `test/.../spi/BacklogItemStatusReadyTest.java` | ESTIMATED関連のテスト追加（必要に応じて） |

---

## 段階の分割

### 段階1: 値オブジェクトと状態enum

- `StoryPoint` 値オブジェクトを作成（許容値定数・検証ロジック）
- `BacklogItemStatus` enum に `ESTIMATED`, `IN_PROGRESS`, `DONE` を追加

### 段階2: エンティティ拡張

- `BacklogItem` に `storyPoint` フィールドを追加
- `changeStatus()` に `READY → ESTIMATED` と `ESTIMATED → READY` の遷移を追加
- `ESTIMATED` 遷移時に `storyPoint` が設定されていることを検証
- `setStoryPoint()` メソッドを追加（見積もり値の再評価用）

### 段階3: コマンドサービス

- `EstimateBacklogItemCommandService` を新規作成（見積もり値の設定・更新）
- `ChangeBacklogItemStatusCommandService` に `READY→ESTIMATED`, `ESTIMATED→READY` の権限判定を追加

### 段階4: プレゼンテーション層

- `BacklogItemCommandController` に見積もりAPIエンドポイントを新規追加
- `BacklogItemResponse` に `storyPoint` を追加
- `BacklogItemResponseConverter` を更新

### 段階5: DBマイグレーション

- Flywayマイグレーションスクリプトを作成（`story_point` 列追加、CHECK制約、status制約拡張）

### 段階6: テスト

- 各段階に対応するテストを作成（small test 基本）

---

## テストリスト

### StoryPoint 値オブジェクト

1. StoryPoint 0を設定したとき、有効な値として受け入れられること
2. StoryPoint 1を設定したとき、有効な値として受け入れられること
3. StoryPoint 89を設定したとき、有効な値として受け入れられること
4. StoryPoint 4を設定したとき（フィボナッチ数列にない）、IllegalArgumentExceptionがスローされること
5. StoryPoint -1を設定したとき、IllegalArgumentExceptionがスローされること
6. StoryPoint 90を設定したとき、IllegalArgumentExceptionがスローされること
7. StoryPointの許容値一覧を取得したとき、フィボナッチ数列の値のみが返ること

### BacklogItem エンティティ — 状態遷移

8. READY状態のBacklogItemに有効なStoryPointを設定しESTIMATEDへ遷移したとき、状態がESTIMATEDになること
9. READY状態のBacklogItemでStoryPointが未設定のときESTIMATEDへ遷移すると、IllegalArgumentExceptionがスローされること
10. READY状態のBacklogItemに無効なStoryPointを設定しESTIMATEDへ遷移したとき、IllegalArgumentExceptionがスローされること
11. ESTIMATED状態のBacklogItemをREADYへ戻したとき、状態がREADYになりStoryPointが保持されること
12. ESTIMATED状態のBacklogItemのStoryPointを変更したとき、新しい値が設定されること（再評価許可）
13. ESTIMATED状態のBacklogItemをACCEPTEDへ遷移しようとすると、IllegalStateExceptionがスローされること
14. ESTIMATED状態のBacklogItemをSUGGESTEDへ遷移しようとすると、IllegalStateExceptionがスローされること
15. READY状態のBacklogItemをESTIMATED以外へ遷移しようとすると、IllegalStateExceptionがスローされること

### BacklogItem エンティティ — StoryPoint設定

16. BacklogItemにStoryPoint 0を設定したとき、値が保持されること
17. BacklogItemにStoryPoint 89を設定したとき、値が保持されること
18. BacklogItemに無効なStoryPoint値を設定しようとすると、IllegalArgumentExceptionがスローされること
19. BacklogItemのStoryPointをnullから有効な値に変更したとき、値が更新されること

### ChangeBacklogItemStatusCommandService — 権限判定

20. POがREADYをESTIMATEDに変更したとき、状態がESTIMATEDになりイベントが発行されること
21. SMがREADYをESTIMATEDに変更したとき、状態がESTIMATEDになりイベントが発行されること
22. チームメンバー（PO/SM以外）がREADYをESTIMATEDに変更したとき、状態がESTIMATEDになりイベントが発行されること
23. チーム外の従業員がREADYをESTIMATEDに変更しようとすると、BacklogItemPermissionDeniedExceptionがスローされること
24. POがESTIMATEDをREADYに戻したとき、状態がREADYになりStoryPointが保持されること
25. チーム外の従業員がESTIMATEDをREADYに戻そうとすると、BacklogItemPermissionDeniedExceptionがスローされること

### ChangeBacklogItemStatusCommandService — イベント発行

26. READY→ESTIMATED遷移時にBacklogItemStatusChangedイベントが発行されること
27. ESTIMATED→READY遷移時にBacklogItemStatusChangedイベントが発行されること
28. 権限がない場合、保存もイベント発行もされないこと

### EstimateBacklogItemCommandService（新規）

29. READY状態のBacklogItemにStoryPointを設定したとき、値が保存されること
30. ESTIMATED状態のBacklogItemのStoryPointを変更したとき、新しい値が保存されること
31. 無効なStoryPoint値を設定しようとすると、IllegalArgumentExceptionがスローされること
32. 存在しないBacklogItemIdでStoryPointを設定しようとすると、IllegalArgumentExceptionがスローされること

### 既存遷移の回帰テスト

33. 既存のSUGGESTED→ACCEPTED遷移が引き続き機能すること
34. 既存のACCEPTED→READY遷移が引き続き機能すること
35. 既存のREADY→SUGGESTED遷移が引き続き機能すること
36. 既存のFROZEN→SUGGESTED遷移が引き続き機能すること

---

## 未決定事項

1. **見積もり値の設定と状態遷移の統合・分離**: 見積もり値の設定（`storyPoint` 更新）と `READY → ESTIMATED` 状態遷移を1つのAPI呼び出しで完結させるか、それとも見積もりAPIと状態遷移APIを分離するか。Issue本文では「見積り完了時に状態が自動的にEstimatedに更新される」とあるが、API設計の詳細は未決定。

2. **通知の実装範囲**: Issue本文では「見積り完了時に利害関係者全員に通知を送信する」とあるが、通知機能の実装は#507系で別途扱われる前提か。本Issueではイベント発行のみで十分か、それとも通知の実装も含めるか。

3. **監査ログの実装範囲**: Issue本文では「見積り値の履歴を追跡する監査ログ機能」と「見積り値の編集履歴を表示する機能」が記載されているが、プロジェクト方針（#572）では「専用の監査ログは持たない」。見積もり値の変更履歴をどのように担保するか（イベントのみで十分か、それとも別途履歴テーブルが必要か）。

4. **IN_PROGRESS / DONE 状態の実装範囲**: Issue本文のライフサイクルには `IN_PROGRESS` と `Done` が含まれるが、これらは#522/#525で別途実装される予定。本Issueではenumの追加のみで十分か、それとも状態遷移ロジックも一部含めるか。

5. **見積もり値の表示形式**: APIレスポンスでStoryPointを数値として返すか、説明テキスト（例: "小さなタスク"）も併せて返すか。#516では説明テキストの追加が記載されているが、詳細は未決定。

6. **BacklogItemEstimated イベントの必要性**: 既存の `BacklogItemStatusChanged` イベントで十分か、それとも見積もり完了専用のイベントを別途定義する必要があるか。

---

## 参考

- Issue #513（親Issue）
- Issue #516（StoryPoint定義）
- Issue #498（READY状態実装、完了済み）
- Design Issue #642（見積り再評価許可、決定済み）
- Design Issue #643（Estimated→Readyロールバック許可、決定済み）
- Design Issue #645（StoryPoint 0許可、決定済み）
- Design Issue #623（楽観ロック追加、決定済み）
- Design Issue #633（通知チャネル決定済み）
