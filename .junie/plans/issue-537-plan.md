# Issue #537 実装計画：チームはフィーチャーを追加できる

## 1. 概要

スクラムチームのバックログアイテムを論理的にグループ化する「フィーチャー」機能を実装する。
フィーチャーは複数のバックログアイテムを含め、進捗状況（完了SP÷総SP）を表示する。

## 2. 設計方針

### 2.1 決定事項（Design Issues #660〜#664）

| 項目 | 決定内容 | Issue |
|------|---------|-------|
| サブフィーチャー | **許可しない**。フィーチャーは階層を持たない単層構造 | #660 |
| 削除時の処理 | **フィーチャーとバックログアイテムの関連（紐付け）のみ削除**。バックログアイテム自体は残す | #661 |
| アクセス権限 | **スクラムチームのメンバーなら誰でも追加・編集・削除可能**。ロールによる制限なし | #662 |
| 進捗計算 | **完了したアイテムのストーリーポイント合計 ÷ 配下全体のストーリーポイント合計** | #663 |
| 遡及割り当て | **既存バックログアイテムへのフィーチャーの後付け割り当てを許可** | #664 |

### 2.2 モジュール配置

フィーチャーは `requirementBacklog` モジュール内に実装する。
- `undecided.agiloom.requirementBacklog.spi` — 公開型（Feature エンティティ、FeatureStatus 区分値）
- `undecided.agiloom.requirementBacklog.internal` — 内部実装（コマンド/クエリサービス、リポジトリ、プレゼンテーション）

### 2.3 ドメインモデル

#### Feature（spi）
- `id`: SnowflakeId（主キー）
- `name`: 名前（必須、文字列制約は項目辞書に未定義 → **未決定**）
- `description`: 説明（任意）
- `status`: FeatureStatus（Planned / InProgress / Done）
- `scrumTeamId`: 所属スクラムチームID（必須）
- `createdAt`: 作成日時
- `updatedAt`: 最終更新日時
- `version`: 楽観ロック

#### FeatureBacklogItem（spi）— 関連テーブル
- `featureId`: フィーチャーID（複合主キー）
- `backlogItemId`: バックログアイテムID（複合主キー）
- `createdAt`: 紐付け日時

#### FeatureStatus（spi）— 区分値
- `PLANNED`: 計画済み
- `IN_PROGRESS`: 進行中
- `DONE`: 完了

### 2.4 BacklogItem への追加項目

進捗計算（#663）のため、BacklogItem に `storyPoints` 項目を追加する必要がある。
- `storyPoints`: ストーリーポイント（整数、null可）

### 2.5 進捗計算

- 完了したバックログアイテムのストーリーポイント合計を `completedStoryPoints` とする
- 配下全体のストーリーポイント合計を `totalStoryPoints` とする
- 進捗率 = `completedStoryPoints / totalStoryPoints`（totalStoryPoints が 0 の場合は 0%）
- 「完了」の判定基準は **未決定**（BacklogItemStatus のどの状態を完了とするか）

### 2.6 ステータス管理

- FeatureStatus は手動で設定する（自動遷移はしない）
- 初期状態は `PLANNED`

## 3. 変更対象ファイル

### 3.1 新規作成ファイル

| ファイル | 層 | 説明 |
|---------|-----|------|
| `spi/Feature.java` | Domain | フィーチャーエンティティ |
| `spi/FeatureStatus.java` | Domain | フィーチャー状態の区分値 |
| `spi/FeatureBacklogItem.java` | Domain | フィーチャーとバックログアイテムの関連エンティティ |
| `internal/FeatureRepository.java` | Infrastructure | フィーチャーリポジトリ |
| `internal/FeatureBacklogItemRepository.java` | Infrastructure | 関連リポジトリ |
| `internal/command/CreateFeatureCommand.java` | useCase | フィーチャー作成コマンド |
| `internal/command/CreateFeatureCommandService.java` | useCase | フィーチャー作成サービス |
| `internal/command/UpdateFeatureCommand.java` | useCase | フィーチャー更新コマンド |
| `internal/command/UpdateFeatureCommandService.java` | useCase | フィーチャー更新サービス |
| `internal/command/DeleteFeatureCommand.java` | useCase | フィーチャー削除コマンド |
| `internal/command/DeleteFeatureCommandService.java` | useCase | フィーチャー削除サービス（関連のみ削除） |
| `internal/command/AssignBacklogItemToFeatureCommand.java` | useCase | バックログアイテムをフィーチャーに割り当てるコマンド |
| `internal/command/AssignBacklogItemToFeatureCommandService.java` | useCase | 割り当てサービス |
| `internal/command/RemoveBacklogItemFromFeatureCommand.java` | useCase | バックログアイテムをフィーチャーから外すコマンド |
| `internal/command/RemoveBacklogItemFromFeatureCommandService.java` | useCase | 外しサービス |
| `internal/query/FeatureQueryService.java` | useCase | フィーチャークエリサービス（進捗計算含む） |
| `internal/presentation/FeatureCommandController.java` | Presentation | フィーチャーコマンドAPI |
| `internal/presentation/FeatureQueryController.java` | Presentation | フィーチャークエリAPI |
| `internal/presentation/CreateFeatureRequest.java` | Presentation | 作成リクエストDTO |
| `internal/presentation/UpdateFeatureRequest.java` | Presentation | 更新リクエストDTO |
| `internal/presentation/AssignBacklogItemToFeatureRequest.java` | Presentation | 割り当てリクエストDTO |
| `internal/presentation/FeatureResponse.java` | Presentation | レスポンスDTO |
| `internal/presentation/FeatureResponseConverter.java` | Presentation | レスポンス変換器 |

### 3.2 修正ファイル

| ファイル | 変更内容 |
|---------|---------|
| `spi/BacklogItem.java` | `storyPoints` フィールド追加 |
| `internal/BacklogItemRepository.java` | フィーチャーIDでの検索メソッド追加（必要に応じて） |
| `internal/CreateBacklogItemCommand.java` | `storyPoints` 項目追加 |
| `internal/CreateBacklogItemCommandService.java` | `storyPoints` の設定追加 |
| `internal/UpdateBacklogItemCommand.java` | `storyPoints` 項目追加 |
| `internal/UpdateBacklogItemCommandService.java` | `storyPoints` の更新追加 |
| `internal/BacklogItemResponse.java` | `storyPoints` 項目追加 |
| `internal/BacklogItemResponseConverter.java` | `storyPoints` の変換追加 |

### 3.3 Flyway マイグレーション

| ファイル | 説明 |
|---------|------|
| `db/migration/ag/V20261119250000__add_story_points_to_backlog_items.sql` | backlog_items に story_points 列追加 |
| `db/migration/ag/V20261119260000__add_features_table.sql` | features テーブル作成 |
| `db/migration/ag/V20261119270000__add_feature_backlog_items_table.sql` | feature_backlog_items 関連テーブル作成 |

## 4. 段階の分割

### 段階1: BacklogItem にストーリーポイント追加

- BacklogItem エンティティに `storyPoints` フィールド追加
- 作成・更新コマンド・サービスに `storyPoints` 対応
- レスポンス・コントローラーに `storyPoints` 対応
- Flyway マイグレーション
- 関連テストの更新

### 段階2: フィーチャー基本機能（CRUD）

- Feature エンティティ、FeatureStatus 区分値、FeatureBacklogItem 関連エンティティ作成
- FeatureRepository、FeatureBacklogItemRepository 作成
- 作成・更新・削除コマンド・サービス実装
- Flyway マイグレーション（features, feature_backlog_items）
- 削除時は関連のみ削除（#661）
- チームメンバー権限チェック（#662）

### 段階3: バックログアイテムのフィーチャー割り当て

- 割り当て・外しコマンド・サービス実装
- 遡及割り当て対応（#664）
- 同一バックログアイテムの複数フィーチャー割り当て可否 → **未決定**

### 段階4: フィーチャー進捗計算

- FeatureQueryService に進捗計算ロジック実装
- 完了SP ÷ 総SP（#663）
- 進捗情報をレスポンスに含める

### 段階5: プレゼンテーション層

- コントローラー・リクエスト・レスポンス実装
- REST API エンドポイント公開

## 5. テストリスト

### 5.1 BacklogItem ストーリーポイント関連

1. ストーリーポイントを指定してバックログアイテムを作成したとき、ストーリーポイントが保存される
2. ストーリーポイントをnullでバックログアイテムを作成したとき、ストーリーポイントはnullになる
3. バックログアイテムのストーリーポイントを更新したとき、新しい値が保存される
4. ストーリーポイントをnullに更新したとき、ストーリーポイントはnullになる

### 5.2 Feature 作成

5. 有効な名前でフィーチャーを作成したとき、PLANNED状態で保存される
6. 名前が空でフィーチャーを作成したとき、例外がスローされる
7. 存在しないチームでフィーチャーを作成したとき、例外がスローされる
8. チームメンバーがフィーチャーを作成したとき、正常に作成される
9. チーム外のユーザーがフィーチャーを作成したとき、例外がスローされる

### 5.3 Feature 更新

10. フィーチャーの名前を更新したとき、新しい名前で保存される
11. フィーチャーの説明を更新したとき、新しい説明で保存される
12. フィーチャーのステータスをPLANNEDからIN_PROGRESSに変更したとき、新しいステータスが保存される
13. フィーチャーのステータスをIN_PROGRESSからDONEに変更したとき、新しいステータスが保存される
14. 存在しないフィーチャーを更新したとき、例外がスローされる
15. チーム外のユーザーがフィーチャーを更新したとき、例外がスローされる

### 5.4 Feature 削除

16. 紐付けのないフィーチャーを削除したとき、フィーチャーが削除される
17. バックログアイテムが紐付いているフィーチャーを削除したとき、フィーチャーは削除されバックログアイテムは残る
18. 存在しないフィーチャーを削除したとき、例外がスローされる
19. チーム外のユーザーがフィーチャーを削除したとき、例外がスローされる

### 5.5 バックログアイテムのフィーチャー割り当て

20. バックログアイテムをフィーチャーに割り当てたとき、関連が作成される
21. 既存のバックログアイテムをフィーチャーに割り当てたとき、関連が作成される（遡及割り当て）
22. 存在しないフィーチャーに割り当てようとしたとき、例外がスローされる
23. 存在しないバックログアイテムを割り当てようとしたとき、例外がスローされる
24. 既に割り当てられているバックログアイテムを再度割り当てようとしたとき、例外がスローされる（または無視 → **未決定**）
25. バックログアイテムをフィーチャーから外したとき、関連が削除される
26. チーム外のユーザーが割り当てを試みたとき、例外がスローされる

### 5.6 フィーチャー進捗計算

27. 紐付けのバックログアイテムがないフィーチャーの進捗を取得したとき、進捗は0%になる
28. ストーリーポイントが0のバックログアイテムのみが紐付いているフィーチャーの進捗を取得したとき、進捗は0%になる
29. 完了したバックログアイテムのSPが総SPの半分であるフィーチャーの進捗を取得したとき、進捗は50%になる
30. 全バックログアイテムが完了しているフィーチャーの進捗を取得したとき、進捗は100%になる
31. ストーリーポイントがnullのバックログアイテムが混在するフィーチャーの進捗を取得したとき、nullは0として扱われる（または除外 → **未決定**）

### 5.7 フィーチャー一覧・詳細取得

32. チームのフィーチャー一覧を取得したとき、チームに属するフィーチャーが返される
33. フィーチャーの詳細を取得したとき、紐付くバックログアイテム一覧と進捗情報が含まれる
34. 存在しないフィーチャーの詳細を取得したとき、空が返される

## 6. 未決定事項

| 番号 | 未決定事項 | 影響 | 備考 |
|------|-----------|------|------|
| UD-01 | フィーチャー名・説明の文字列制約（最大長など） | 値オブジェクト定数化（ADR-002）に合わせる必要がある | 項目辞書に未定義。仮置きとして title=200, description=TEXT を検討 |
| UD-02 | 「完了」バックログアイテムの判定基準 | 進捗計算（#663）に直接影響 | BacklogItemStatus のどの状態を完了とするか（例: DONE? 現在の実装には DONE 状態がない） |
| UD-03 | 同一バックログアイテムの複数フィーチャー割り当て可否 | 関連モデルの設計に影響 | 1対1か1対多か |
| UD-04 | 既に割り当て済みのバックログアイテムを再度割り当てたときの動作 | 重複処理の仕様 | 例外スローか無視（idempotent）か |
| UD-05 | ストーリーポイントがnullのバックログアイテムの進捗計算での扱い | 進捗計算ロジックに影響 | 0として扱うか、計算から除外するか |
| UD-06 | ストーリーポイントの値の制約（最小値・最大値・許容値） | バリデーションに影響 | 整数のみか、Fibonacci数列（1, 2, 3, 5, 8, 13, 21...）に制限するか |
| UD-07 | FeatureStatus の状態遷移ルール | ステータス管理の制約 | 自由遷移か、特定の遷移のみ許可するか |
| UD-08 | フィーチャーのステータスを自動更新するかどうか | 進捗100%になったときに自動でDONEにするか | 手動のみか、自動遷移も併用するか |

## 7. 補足

### 7.1 テスト戦略

- サービスレイヤーのテストは Spring Boot に依存しない **small test**（`@Tag("small")`）を基本とする
- リポジトリは Mockito でモック化
- DB 連携が必要なテスト（マイグレーション検証など）のみ medium test（`@Tag("medium")`）とする
- テストメソッド名は `should` で開始、`@DisplayName` は日本語で記述

### 7.2 依存関係

- Feature は BacklogItem を spi で参照する（同一モジュール内）
- Feature は ScrumTeamSubset（internal）を介してチームメンバー権限をチェックする
- 他モジュールのエンティティを直接参照しない

### 7.3 API エンドポイント案

| HTTPメソッド | パス | 説明 |
|-------------|------|------|
| POST | `/api/v1/features` | フィーチャー作成 |
| PUT | `/api/v1/features/{id}` | フィーチャー更新 |
| DELETE | `/api/v1/features/{id}` | フィーチャー削除（関連のみ削除） |
| GET | `/api/v1/features` | チームのフィーチャー一覧 |
| GET | `/api/v1/features/{id}` | フィーチャー詳細（進捗情報含む） |
| POST | `/api/v1/features/{featureId}/backlog-items/{backlogItemId}` | バックログアイテムを割り当て |
| DELETE | `/api/v1/features/{featureId}/backlog-items/{backlogItemId}` | バックログアイテムを外す |
