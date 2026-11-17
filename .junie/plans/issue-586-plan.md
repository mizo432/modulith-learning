---
sessionId: session-261117-085542-1e5n
---

# Requirements

## Overview & Goals

productDiscovery 事業領域で UUID として扱われている従業員IDを SnowflakeId へ移行する。対象は `Product.managerId`、`ScrumTeam.poEmployeeId/smEmployeeId`、および `ScrumTeamChanged` イベントの従業員IDフィールド。

## Scope

### In Scope
1. **spi レイヤー**: `Product.managerId`、`ScrumTeam.poEmployeeId/smEmployeeId`、`ScrumTeamChanged.poEmployeeId/smEmployeeId` を `UUID` → `SnowflakeId` へ変更
2. **command レイヤー**: 6つのコマンド/コマンドサービスで `UUID` → `SnowflakeId` へ変更
3. **presentation レイヤー**: リクエスト/レスポンス/コントローラーで `UUID` → `SnowflakeId` へ変更
4. **DB マイグレーション**: `db/migration/ag` に新規マイグレーションを追加し、`products.manager_id` と `scrum_teams.po_employee_id/sm_employee_id` を `UUID` → `BIGINT` へ変更
5. **requirementBacklog 追随**: `ScrumTeamSubset.poEmployeeId/smEmployeeId` と `canChangeStatusBy()` の型追随のみ（`BacklogItem.actorEmployeeId` は #587 に切り分け）
6. **frontend**: `ProductCreate.tsx` / `ProductEdit.tsx` のバリデーションと表記を SnowflakeId 前提へ変更
7. **テスト**: 既存テストの追随

### Out of Scope
- `BacklogItem.actorEmployeeId` の移行（#587）
- `ChangeBacklogItemStatusRequest.actorEmployeeId` の移行（#587）
- `BacklogItemStatusChanged.actorEmployeeId` の移行（#587）
- `CreateScrumTeamCommandService.snowflakeIdToUuid()` の削除（sprintExecution 側のUUID依存は別のIssue）
- 実データ移行（開発データ再投入前提）


# Technical Design

## Current Implementation

### SnowflakeId の既存マッピングパターン
- `SnowflakeId` は `@Converter(autoApply = true)` を持ち、DB では `Long`（BIGINT）として保存
- JSON シリアライズ: `@JsonCreator` が `of(Long value)` に付与されており、JSON デシリアライズ時に `Long` から `SnowflakeId` を再構築
- `toString()` は数値文字列を返す（シリアライズ側）
- 既存の `Product.id` / `ScrumTeam.id` がこのパターンで動作している

### 変更対象のUUID使用箇所
- `Product.managerId`: `UUID`（DB: `products.manager_id UUID`）
- `ScrumTeam.poEmployeeId/smEmployeeId`: `UUID`（DB: `scrum_teams.po_employee_id UUID`, `sm_employee_id UUID`）
- `ScrumTeamChanged.poEmployeeId/smEmployeeId`: `UUID`（イベントペイロード）
- コマンド/リクエスト/レスポンス/コントローラーが `UUID` を使用
- `ScrumTeamSubset.poEmployeeId/smEmployeeId`: `UUID`（requirementBacklog 側）
- フロントエンド: UUID形式チェック `^[0-9a-fA-F-]{36}$` と「マネージャーのUUID」表記

## Key Decisions

1. **型変更は `UUID` → `SnowflakeId`**: 既存の `SnowflakeId` 値オブジェクトをそのまま使用。新規値オブジェクトは作成しない
2. **JSON では `string` として扱う**: `SnowflakeId` の `@JsonCreator` が `Long` を受け取るため、JSON では数値文字列（例: `"1234567890"`）として送受信。フロントエンドの `managerId` は既に `string` 型
3. **Flyway は既存ファイルを変更せず新規マイグレーションを追加**: `ALTER TABLE ... ALTER COLUMN ... TYPE BIGINT USING NULL` で型変更。開発データは再投入前提なので型変換は単純化
4. **ScrumTeamSubset の型追随は本Issueの範囲**: `ScrumTeamChanged` のペイロード型が変わるため、購読側の `ScrumTeamSubset` も追随。ただし `canChangeStatusBy(UUID)` の引数は `BacklogItem.actorEmployeeId` と連動するため、#587 で変更
5. **ScrumTeamSubset テーブルのマイグレーション**: 未作成のテーブルなので、新規マイグレーションで `BIGINT` として定義

## Proposed Changes

### (1) spi レイヤー（3ファイル）
| ファイル | 変更 |
|---------|------|
| `Product.java` | `managerId: UUID` → `SnowflakeId`。`update()` メソッドの引数も変更 |
| `ScrumTeam.java` | `poEmployeeId/smEmployeeId: UUID` → `SnowflakeId`。`update()`/`assignPo()`/`assignSm()` の引数も変更 |
| `ScrumTeamChanged.java` | `poEmployeeId/smEmployeeId: UUID` → `SnowflakeId` |

### (2) command レイヤー（6ファイル）
| ファイル | 変更 |
|---------|------|
| `CreateProductCommand.java` | `managerId: UUID` → `SnowflakeId` |
| `CreateProductCommandService.java` | 型追随 |
| `CreateScrumTeamCommand.java` | `poEmployeeId/smEmployeeId: UUID` → `SnowflakeId` |
| `CreateScrumTeamCommandService.java` | 型追随 |
| `UpdateProductCommand.java` | `managerId: UUID` → `SnowflakeId` |
| `UpdateProductCommandService.java` | 型追随 |
| `UpdateScrumTeamCommand.java` | `poEmployeeId/smEmployeeId: UUID` → `SnowflakeId` |
| `UpdateScrumTeamCommandService.java` | `assignPo()/assignSm()` の引数 `UUID` → `SnowflakeId` |

### (3) presentation レイヤー（10ファイル）
| ファイル | 変更 |
|---------|------|
| `CreateProductRequest.java` | `managerId: UUID` → `SnowflakeId` |
| `UpdateProductRequest.java` | `managerId: UUID` → `SnowflakeId` |
| `CreateScrumTeamRequest.java` | `poEmployeeId/smEmployeeId: UUID` → `SnowflakeId` |
| `UpdateScrumTeamRequest.java` | `poEmployeeId/smEmployeeId: UUID` → `SnowflakeId` |
| `AssignPoRequest.java` | `poEmployeeId: UUID` → `SnowflakeId` |
| `AssignSmRequest.java` | `smEmployeeId: UUID` → `SnowflakeId` |
| `ProductResponse.java` | `managerId: UUID` → `SnowflakeId` |
| `ScrumTeamResponse.java` | `poEmployeeId/smEmployeeId: UUID` → `SnowflakeId` |
| `ProductCommandController.java` | 型追随 |
| `ScrumTeamCommandController.java` | 型追随 |

### (4) requirementBacklog 追随（1ファイル）
| ファイル | 変更 |
|---------|------|
| `ScrumTeamSubset.java` | `poEmployeeId/smEmployeeId: UUID` → `SnowflakeId`。`canChangeStatusBy()` の引数は `UUID` のまま（#587で変更） |

### (5) DB マイグレーション（新規2ファイル）
| ファイル | 内容 |
|---------|------|
| `V20261117100000__alter_employee_ids_to_bigint.sql` | `products.manager_id` と `scrum_teams.po_employee_id/sm_employee_id` を `UUID` → `BIGINT` |
| `V20261117110000__add_scrum_team_subsets_table.sql` | `scrum_team_subsets` テーブル新規作成（`po_employee_id/sm_employee_id` を `BIGINT`） |

### (6) frontend（2ファイル）
| ファイル | 変更 |
|---------|------|
| `ProductCreate.tsx` | UUIDバリデーション `^[0-9a-fA-F-]{36}$` を数値文字列チェック `^[0-9]+$` に変更。「マネージャーのUUID」→「マネージャーのID」 |
| `ProductEdit.tsx` | 同上 |

### (7) テスト（6ファイル）
| ファイル | 変更 |
|---------|------|
| `CreateProductCommandServiceTest.java` | `UUID.randomUUID()` → `SnowflakeId.of(...)` |
| `CreateScrumTeamCommandServiceTest.java` | 同上 |
| `UpdateProductCommandServiceTest.java` | 同上 |
| `UpdateScrumTeamCommandServiceTest.java` | 同上 |
| `ProductCommandControllerTest.java` | 同上 |
| `ScrumTeamMigrationMediumTest.java` | カラム型チェックを `uuid` → `bigint` に変更 |

## Architecture Diagram

```mermaid
graph LR
  A[Product.managerId] -->|SnowflakeId| B[DB: BIGINT]
  C[ScrumTeam.poEmployeeId] -->|SnowflakeId| D[DB: BIGINT]
  E[ScrumTeam.smEmployeeId] -->|SnowflakeId| D
  F[ScrumTeamChanged] -->|SnowflakeId| G[ScrumTeamSubset]
  G -->|SnowflakeId| D
  H[Frontend] -->|string| I[API: SnowflakeId]
  I -->|@JsonCreator| A
```

## Risks

1. **ScrumTeamChanged イベントの永続化データ**: 既存のイベントログに UUID 形式で保存されたデータがある場合、デシリアライズに失敗する可能性。開発環境のみで、イベントログテーブルもクリーン前提とすること
2. **ScrumTeamSubset.canChangeStatusBy() の引数型**: `UUID` のまま残すため、`ScrumTeamSubset.poEmployeeId` が `SnowflakeId` になることで比較に注意。`SnowflakeId.value()` で `Long` を取り出し、`UUID` との比較は行わない（#587 で一貫させる）
3. **sprintExecution 側の UUID 依存**: `CreateScrumTeamCommandService.snowflakeIdToUuid()` は本Issueで変更せず、sprintExecution 側の移行は別のIssueで対応


# Testing

## Validation Approach

既存のテストパターンに沿って、型変更後のコンパイルとテスト実行で検証。

## Key Scenarios

1. **spi レイヤー**: `Product` / `ScrumTeam` のビルダーで `SnowflakeId` を設定できること
2. **command レイヤー**: コマンドサービスが `SnowflakeId` を正しく処理すること
3. **presentation レイヤー**: コントローラーが JSON リクエストを正しく `SnowflakeId` に変換し、レスポンスでシリアライズすること
4. **DB マイグレーション**: `ScrumTeamMigrationMediumTest` のパターンに沿って、カラム型が `bigint` であることを検証
5. **イベント連携**: `ScrumTeamChanged` が `SnowflakeId` を持ち、`ScrumTeamSubset` が正しく適用すること
6. **frontend**: 数値文字列の `managerId` がバリデーションを通過し、UUID形式が拒否されること

## Edge Cases

1. `managerId` / `poEmployeeId` / `smEmployeeId` が `null` のケースは引き続き動作すること
2. `ScrumTeamSubset.canChangeStatusBy()` の引数が `UUID` のままでも、フィールドが `SnowflakeId` になった後の比較が正しく動作すること（`equals` が型不一致で `false` を返すため、#587 まで権限チェックは常に `false` になることを許容）

## Test Changes

- 既存の `UUID.randomUUID()` を `SnowflakeId.of(...)` に変更
- `ScrumTeamMigrationMediumTest.shouldColumnsHaveCorrectTypes()` で `po_employee_id:uuid` → `po_employee_id:bigint` に変更
- `ScrumTeamMigrationMediumTest` に `products.manager_id` の型検証を追加
- 新しいマイグレーションの検証テストを追加


# Delivery Steps

###   Step 1: 1. spi レイヤーの型変更
`Product.managerId`、`ScrumTeam.poEmployeeId/smEmployeeId`、`ScrumTeamChanged` を `UUID` → `SnowflakeId` へ変更。
- `Product.java`: `managerId` の型と `update()` メソッドの引数を `SnowflakeId` に変更
- `ScrumTeam.java`: `poEmployeeId/smEmployeeId` の型と `update()`/`assignPo()`/`assignSm()` の引数を `SnowflakeId` に変更
- `ScrumTeamChanged.java`: `poEmployeeId/smEmployeeId` の型を `SnowflakeId` に変更
- 不要になった `java.util.UUID` の import を削除

###   Step 2: 2. command レイヤーの型変更
6つのコマンド/コマンドサービスで `UUID` → `SnowflakeId` へ変更。
- `CreateProductCommand.java`: `managerId` を `SnowflakeId` に
- `CreateProductCommandService.java`: 型追随
- `CreateScrumTeamCommand.java`: `poEmployeeId/smEmployeeId` を `SnowflakeId` に
- `CreateScrumTeamCommandService.java`: 型追随
- `UpdateProductCommand.java`: `managerId` を `SnowflakeId` に
- `UpdateProductCommandService.java`: 型追随
- `UpdateScrumTeamCommand.java`: `poEmployeeId/smEmployeeId` を `SnowflakeId` に
- `UpdateScrumTeamCommandService.java`: `assignPo()/assignSm()` の引数を `SnowflakeId` に

###   Step 3: 3. presentation レイヤーの型変更
リクエスト/レスポンス/コントローラーで `UUID` → `SnowflakeId` へ変更。
- `CreateProductRequest.java` / `UpdateProductRequest.java`: `managerId` を `SnowflakeId` に
- `CreateScrumTeamRequest.java` / `UpdateScrumTeamRequest.java`: `poEmployeeId/smEmployeeId` を `SnowflakeId` に
- `AssignPoRequest.java` / `AssignSmRequest.java`: 従業員IDを `SnowflakeId` に
- `ProductResponse.java` / `ScrumTeamResponse.java`: 従業員IDを `SnowflakeId` に
- `ProductCommandController.java` / `ScrumTeamCommandController.java`: 型追随
- `ProductResponseConverter.java` / `ScrumTeamResponseConverter.java`: 型追随

###   Step 4: 4. requirementBacklog 追随とDBマイグレーション
`ScrumTeamSubset` の型追随と新規Flywayマイグレーションを追加。
- `ScrumTeamSubset.java`: `poEmployeeId/smEmployeeId` を `SnowflakeId` に。`canChangeStatusBy(UUID)` の引数は変更せず（#587）
- `V20261117100000__alter_employee_ids_to_bigint.sql`: `products.manager_id` と `scrum_teams.po_employee_id/sm_employee_id` を `BIGINT` に変更
- `V20261117110000__add_scrum_team_subsets_table.sql`: `scrum_team_subsets` テーブル新規作成（`po_employee_id/sm_employee_id` を `BIGINT`）

###   Step 5: 5. frontend バリデーション変更とテスト追随
フロントエンドのバリデーションと全テストを型変更へ追随。
- `ProductCreate.tsx`: UUIDバリデーションを数値文字列チェック `^[0-9]+$` に変更。「マネージャーのUUID」→「マネージャーのID」
- `ProductEdit.tsx`: 同上
- `CreateProductCommandServiceTest.java`: `UUID.randomUUID()` → `SnowflakeId.of(...)`
- `CreateScrumTeamCommandServiceTest.java`: 同上
- `UpdateProductCommandServiceTest.java`: 同上
- `UpdateScrumTeamCommandServiceTest.java`: 同上
- `ProductCommandControllerTest.java`: 同上
- `ScrumTeamMigrationMediumTest.java`: カラム型チェックを `uuid` → `bigint` に変更。`products.manager_id` の型検証を追加