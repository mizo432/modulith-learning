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
- `name`: 名前（必須、最大200文字。制約値は値オブジェクトの定数で単一情報源とする（ADR-002、決定1））
- `description`: 説明（任意、TEXT型で上限なし（決定1））
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

### 2.4 進捗計算

- 完了したバックログアイテムのストーリーポイント合計を `completedStoryPoints` とする
- 配下全体のストーリーポイント合計を `totalStoryPoints` とする
- 進捗率 = `completedStoryPoints / totalStoryPoints`（totalStoryPoints が 0 の場合は 0%）
- 「完了」の判定基準は `BacklogItemStatus.DONE`（決定2）
- ストーリーポイント未設定（null）のアイテムは進捗計算で 0 として扱う（決定6）
- 同じフィーチャーへの再割り当ては冪等に成功とする（決定4）
- 別のフィーチャーに既に割り当て済みのバックログアイテムを割り当てた場合は 409 エラー（決定4）

### 2.5 ステータス管理

- FeatureStatus は手動で設定する。自動更新はしない（決定8）
- 初期状態は `PLANNED`
- 許可される遷移（決定7）：
  - 順方向: `Planned → InProgress`, `InProgress → Done`
  - 差し戻し: `InProgress → Planned`, `Done → InProgress`
  - 許可しない遷移: `Planned → Done`, `Done → Planned`

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

### 3.2 Flyway マイグレーション

| ファイル | 説明 |
|---------|------|
| `db/migration/ag/V20261119260000__add_features_table.sql` | features テーブル作成 |
| `db/migration/ag/V20261119270000__add_feature_backlog_items_table.sql` | feature_backlog_items 関連テーブル作成 |

> **注**: `storyPoints` は #513 で追加済み。BacklogItem への修正は不要。

## 4. 段階の分割

### 段階1: フィーチャー基本機能（CRUD）

- Feature エンティティ、FeatureStatus 区分値、FeatureBacklogItem 関連エンティティ作成
- FeatureRepository、FeatureBacklogItemRepository 作成
- 作成・更新・削除コマンド・サービス実装
- Flyway マイグレーション（features, feature_backlog_items）
- 削除時は関連のみ削除（#661）
- チームメンバー権限チェック（#662）
- フィーチャー名は最大200文字、説明はTEXT型で上限なし（決定1）

### 段階2: バックログアイテムのフィーチャー割り当て

- 割り当て・外しコマンド・サービス実装
- 遡及割り当て対応（#664）
- 1つのバックログアイテムは最大1つのフィーチャーにのみ割り当て（決定3）
- 同じフィーチャーへの再割り当ては冪等に成功、別のフィーチャーへの割り当ては409エラー（決定4）

### 段階3: フィーチャー進捗計算

- FeatureQueryService に進捗計算ロジック実装
- 完了SP ÷ 総SP（#663）
- 「完了」は BacklogItemStatus.DONE（決定2）
- ストーリーポイント未設定は0として扱う（決定6）
- 進捗情報をレスポンスに含める

### 段階4: プレゼンテーション層

- コントローラー・リクエスト・レスポンス実装
- REST API エンドポイント公開

## 5. テストリスト

### 5.0 段階4（プレゼンテーション層）コントローラーテスト

1. 有効なリクエストでフィーチャーを作成したとき、201が返される
2. 権限がないユーザーがフィーチャーを作成したとき、403が返される
3. 名前が空でフィーチャーを作成したとき、400が返される
4. 有効なリクエストでフィーチャーを更新したとき、200が返される
5. 許可されないステータス遷移で更新したとき、409が返される
6. 存在するフィーチャーを削除したとき、204が返される
7. チームのフィーチャー一覧を取得したとき、200と一覧が返される
8. 進捗を含むフィーチャー詳細を取得したとき、200と進捗情報が返される
9. 存在しないフィーチャーの詳細を取得したとき、400が返される
10. バックログアイテムをフィーチャーに割り当てたとき、204が返される
11. 別のフィーチャーに割り当て済みのバックログアイテムを割り当てたとき、409が返される
12. バックログアイテムをフィーチャーから外したとき、204が返される

### 5.1 Feature 作成

1. 有効な名前でフィーチャーを作成したとき、PLANNED状態で保存される
2. 名前が空でフィーチャーを作成したとき、例外がスローされる
3. 名前が200文字でフィーチャーを作成したとき、正常に保存される（境界値）
4. 名前が201文字でフィーチャーを作成したとき、例外がスローされる（境界値）
5. 存在しないチームでフィーチャーを作成したとき、例外がスローされる
6. チームメンバーがフィーチャーを作成したとき、正常に作成される
7. チーム外のユーザーがフィーチャーを作成したとき、例外がスローされる

### 5.2 Feature 更新

8. フィーチャーの名前を更新したとき、新しい名前で保存される
9. フィーチャーの説明を更新したとき、新しい説明で保存される
10. 存在しないフィーチャーを更新したとき、例外がスローされる
11. チーム外のユーザーがフィーチャーを更新したとき、例外がスローされる

### 5.3 Feature ステータス遷移

12. PLANNEDからIN_PROGRESSへ遷移できること
13. IN_PROGRESSからDONEへ遷移できること
14. IN_PROGRESSからPlannedへ差し戻せること
15. DONEからIN_PROGRESSへ差し戻せること
16. PLANNEDからDONEへの直接遷移は許可しないこと
17. DONEからPLANNEDへの直接遷移は許可しないこと

### 5.4 Feature 削除

18. 紐付けのないフィーチャーを削除したとき、フィーチャーが削除される
19. バックログアイテムが紐付いているフィーチャーを削除したとき、フィーチャーは削除されバックログアイテムは残る
20. 存在しないフィーチャーを削除したとき、例外がスローされる
21. チーム外のユーザーがフィーチャーを削除したとき、例外がスローされる

### 5.5 バックログアイテムのフィーチャー割り当て

22. バックログアイテムをフィーチャーに割り当てたとき、関連が作成される
23. 既存のバックログアイテムをフィーチャーに割り当てたとき、関連が作成される（遡及割り当て）
24. 存在しないフィーチャーに割り当てようとしたとき、例外がスローされる
25. 存在しないバックログアイテムを割り当てようとしたとき、例外がスローされる
26. 同じフィーチャーへの再割り当ては冪等に成功する（決定4）
27. 別のフィーチャーに割り当て済みのバックログアイテムを割り当てたとき、409エラー（決定3,4）
28. バックログアイテムをフィーチャーから外したとき、関連が削除される
29. チーム外のユーザーが割り当てを試みたとき、例外がスローされる

### 5.6 フィーチャー進捗計算

30. 紐付けのバックログアイテムがないフィーチャーの進捗を取得したとき、進捗は0%になる
31. ストーリーポイントが0のバックログアイテムのみが紐付いているフィーチャーの進捗を取得したとき、進捗は0%になる
32. 完了した（DONE）バックログアイテムのSPが総SPの半分であるフィーチャーの進捗を取得したとき、進捗は50%になる
33. 全バックログアイテムが完了（DONE）しているフィーチャーの進捗を取得したとき、進捗は100%になる
34. ストーリーポイントがnullのバックログアイテムが混在するフィーチャーの進捗を取得したとき、nullは0として扱われる（決定6）

### 5.7 フィーチャー一覧・詳細取得

35. チームのフィーチャー一覧を取得したとき、チームに属するフィーチャーが返される
36. フィーチャーの詳細を取得したとき、紐付くバックログアイテム一覧と進捗情報が含まれる
37. 存在しないフィーチャーの詳細を取得したとき、空が返される

## 6. 決定事項

| 番号 | 項目 | 決定内容 | 出典 |
|------|------|---------|------|
| D-01 | フィーチャー名・説明の制約 | 名前:最大200文字、説明:TEXT型で上限なし。制約値は値オブジェクトの定数で単一情報源（ADR-002） | 決定1 |
| D-02 | 「完了」バックログアイテムの判定 | `BacklogItemStatus.DONE` のアイテムを完了とする | 決定2 |
| D-03 | バックログアイテムのフィーチャー割り当て | 1つのバックログアイテムは最大1つのフィーチャーにのみ割り当て | 決定3 |
| D-04 | 再割り当ての動作 | 同じフィーチャーへの再割り当ては冪等に成功。別のフィーチャーへの割り当ては409エラー | 決定4 |
| D-05 | ストーリーポイント未設定の扱い | 進捗計算で0として扱う | 決定6 |
| D-06 | ストーリーポイントの値オブジェクト | #513で追加済みの `StoryPoint`（Fibonacci 0から89）を使用。BacklogItemへの項目追加は不要 | 決定6 |
| D-07 | FeatureStatus の状態遷移 | 順方向: Planned→InProgress→Done、差し戻し: InProgress→Planned, Done→InProgress。Planned→Done, Done→Plannedは許可しない | 決定7 |
| D-08 | ステータスの自動更新 | 自動更新はしない | 決定8 |

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
