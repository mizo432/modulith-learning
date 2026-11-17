# Issue #588 実装計画 — sprintExecution の UUID を SnowflakeId へ移行

## 概要

sprintExecution 領域で `UUID` を使用している `productId`・`teamId` を `SnowflakeId` へ移行する。
参照先エンティティ（`Product.id`、`ScrumTeam.id`）は既に `BIGINT`（SnowflakeId）であるため、型を一致させる。

### 移行対象

| エンティティ | 対象フィールド | 移行前 | 移行後 |
|---|---|---|---|
| `Sprint` | `productId` | `UUID` | `SnowflakeId` |
| `Sprint` | `teamId` | `UUID` | `SnowflakeId` |
| `SprintSettings` | `productId` | `UUID` | `SnowflakeId` |
| `Notification` | `productId` | `UUID` | `SnowflakeId` |

### 移行対象外（本Issueの範囲外）

| エンティティ | フィールド | 理由 |
|---|---|---|
| `SprintBacklogItem` | `assigneeId` | ユーザー参照。別の移行Issueで対応 |
| `Activity` | `actorId` | ユーザー参照。別の移行Issueで対応 |
| `Impediment` | `assigneeId` | ユーザー参照。別の移行Issueで対応 |
| `Notification` | `recipientId` | ユーザー参照。別の移行Issueで対応 |

---

## 1. 対象ファイル一覧と変更内容

### 1-1. SPI レイヤー（`undecided.agiloom.sprintExecution.spi`）

#### Sprint.java
- `productId` の型を `UUID` → `SnowflakeId`
- `teamId` の型を `UUID` → `SnowflakeId`
- `import java.util.UUID` を削除（残りのUUID使用箇所がない場合）
- `createSprintZero()` メソッドのシグネチャを `UUID` → `SnowflakeId` に変更
- `@Column(name = "product_id", nullable = false)` は変更不要（JPA が SnowflakeId の Converter を使用する）
- `@Column(name = "team_id", nullable = false)` は変更不要

#### SprintSettings.java
- `productId` の型を `UUID` → `SnowflakeId`
- `import java.util.UUID` を削除

#### Notification.java
- `productId` の型を `UUID` → `SnowflakeId`
- `create()` ファクトリメソッドの `productId` パラメータ型を `UUID` → `SnowflakeId`
- `import java.util.UUID` は `recipientId` で使用するため維持

#### SprintZeroFactory.java（SPI インターフェース）
- `createSprintZero()` のシグネチャを `UUID productId, UUID teamId` → `SnowflakeId productId, SnowflakeId teamId`
- `import java.util.UUID` を削除

#### 変更不要のSPIファイル
- `SprintGoal.java` — `sprintId` は既に `SnowflakeId`
- `SprintBacklogItem.java` — `sprintId`・`backlogItemId` は既に `SnowflakeId`（`assigneeId` はユーザー参照で対象外）
- `Activity.java` — `sprintId` は既に `SnowflakeId`（`actorId` はユーザー参照で対象外）
- `Impediment.java` — `sprintId` は既に `SnowflakeId`（`assigneeId` はユーザー参照で対象外）

### 1-2. Command レイヤー（`undecided.agiloom.sprintExecution.internal.command`）

#### CreateSprintCommand.java
- `productId` の型を `UUID` → `SnowflakeId`
- `teamId` の型を `UUID` → `SnowflakeId`
- `import java.util.UUID` を削除

#### CreateSprintCommandService.java
- `execute()` メソッド内の `UUID productId = command.getProductId()` を `SnowflakeId` に変更
- `sprintSettingsRepository.findByProductId(productId)` の呼び出しはリポジトリ側が型変更に対応するため、そのまま動作
- `Sprint.builder().productId(...).teamId(...)` は型変更により自動対応
- `import java.util.UUID` を削除

#### SprintZeroFactoryImpl.java
- `createSprintZero()` のオーバーライドシグネチャを `UUID` → `SnowflakeId` に変更
- `sprintRepository.findByProductIdAndTeamIdAndIsSprintZero()` の引数型はリポジトリ側が対応
- `Sprint.createSprintZero()` の呼び出しはSPI側が対応
- `import java.util.UUID` を削除

#### UpdateSprintSettingsCommand.java
- `productId` の型を `UUID` → `SnowflakeId`
- `import java.util.UUID` を削除

#### UpdateSprintSettingsCommandService.java
- `execute()` メソッド内の `UUID productId = command.getProductId()` を `SnowflakeId` に変更
- `sprintRepository.findByProductIdAndSprintNumber()` の引数型はリポジトリ側が対応
- `sprintSettingsRepository.findByProductId()` の引数型はリポジトリ側が対応
- `SprintSettings.builder().productId(productId)` は型変更により自動対応
- `import java.util.UUID` を削除

### 1-3. Repository レイヤー（`undecided.agiloom.sprintExecution.internal`）

#### SprintRepository.java
- `findByProductId(UUID productId)` → `findByProductId(SnowflakeId productId)`
- `findByProductIdAndSprintNumber(UUID productId, Integer sprintNumber)` → `findByProductIdAndSprintNumber(SnowflakeId productId, Integer sprintNumber)`
- `findByTeamId(UUID teamId)` → `findByTeamId(SnowflakeId teamId)`
- `findByTeamIdOrderBySprintNumberAsc(UUID teamId)` → `findByTeamIdOrderBySprintNumberAsc(SnowflakeId teamId)`
- `findByTeamIdAndIsSprintZero(UUID teamId, Boolean isSprintZero)` → `findByTeamIdAndIsSprintZero(SnowflakeId teamId, Boolean isSprintZero)`
- `findByProductIdAndTeamIdAndIsSprintZero(UUID productId, UUID teamId, Boolean isSprintZero)` → `findByProductIdAndTeamIdAndIsSprintZero(SnowflakeId productId, SnowflakeId teamId, Boolean isSprintZero)`
- `import java.util.UUID` を削除

#### SprintSettingsRepository.java
- `findByProductId(UUID productId)` → `findByProductId(SnowflakeId productId)`
- `import java.util.UUID` を削除

#### NotificationRepository.java
- `findByProductId(UUID productId)` → `findByProductId(SnowflakeId productId)`
- `findByRecipientIdAndIsRead(UUID recipientId, Boolean isRead)` は変更不要（`recipientId` はユーザー参照で対象外）
- `import java.util.UUID` は `findByRecipientIdAndIsRead` で使用するため維持

### 1-4. Query レイヤー（`undecided.agiloom.sprintExecution.internal.query`）

#### SprintQueryService.java
- `findByTeamIdOrderBySprintNumberAsc(UUID teamId)` → `findByTeamIdOrderBySprintNumberAsc(SnowflakeId teamId)`
- `findSprintZeroByTeamId(UUID teamId)` → `findSprintZeroByTeamId(SnowflakeId teamId)`
- `import java.util.UUID` を削除

#### SprintSettingsQueryService.java
- `findByProductId(UUID productId)` → `findByProductId(SnowflakeId productId)`
- `calculateEndDate(UUID productId, LocalDate startDate)` → `calculateEndDate(SnowflakeId productId, LocalDate startDate)`
- `getSettingsOrDefault(UUID productId)` → `getSettingsOrDefault(SnowflakeId productId)`
- `import java.util.UUID` を削除

### 1-5. Presentation レイヤー（`undecided.agiloom.sprintExecution.internal.presentation`）

#### CreateSprintRequest.java
- `productId` の型を `UUID` → `String`（API JSON では string として扱う）
- `teamId` の型を `UUID` → `String`（API JSON では string として扱う）
- `import java.util.UUID` を削除

#### SprintResponse.java
- `productId` の型を `UUID` → `String`（API JSON では string として扱う）
- `teamId` の型を `UUID` → `String`（API JSON では string として扱う）
- `import java.util.UUID` を削除

#### SprintSettingsResponse.java
- `productId` の型を `UUID` → `String`（API JSON では string として扱う）
- `import java.util.UUID` を削除

#### UpdateSprintSettingsRequest.java
- `productId` の型を `UUID` → `String`（API JSON では string として扱う）
- `import java.util.UUID` を削除

#### SprintQueryController.java
- `getSprintSettings()` の `@RequestParam UUID productId` → `@RequestParam String productId`
- コントローラー内で `String` → `SnowflakeId.of(Long.parseLong(productId))` に変換
- `toResponse()` メソッドで `SprintResponse.builder().productId(...).teamId(...)` は `SnowflakeId.value().toString()` で string 変換
- `toSprintSettingsResponse()` で `SprintSettingsResponse.builder().productId(...)` は `SnowflakeId.value().toString()` で string 変換
- `import java.util.UUID` を削除

#### SprintSettingsCommandController.java
- `updateSprintSettings()` の `@PathVariable UUID productId` → `@PathVariable String productId`
- コントローラー内で `String` → `SnowflakeId.of(Long.parseLong(productId))` に変換
- `toResponse()` で `SprintSettingsResponse.builder().productId(...)` は `SnowflakeId.value().toString()` で string 変換
- `import java.util.UUID` を削除

#### SprintSettingsQueryController.java
- `getSprintSettings()` の `@PathVariable UUID productId` → `@PathVariable String productId`
- `calculateEndDate()` の `@PathVariable UUID productId` → `@PathVariable String productId`
- コントローラー内で `String` → `SnowflakeId.of(Long.parseLong(productId))` に変換
- `toResponse()` で `SprintSettingsResponse.builder().productId(...)` は `SnowflakeId.value().toString()` で string 変換
- `import java.util.UUID` を削除

---

## 2. Flyway 新規マイグレーション

既存ファイルは変更しない。`db/migration/ag/` に新規ファイルを追加する。

### ファイル名: `V20261117110000__alter_sprint_ids_to_bigint.sql`

```sql
-- sprintExecution 領域の UUID カラムを BIGINT へ変更します。
-- sprints テーブルの product_id と team_id、
-- sprint_settings テーブルの product_id を SnowflakeId へ移行します。
-- 開発データは再投入前提のため、型変換は単純化します（USING NULL）。

-- sprints テーブル
ALTER TABLE sprints
    ALTER COLUMN product_id TYPE BIGINT USING NULL;

ALTER TABLE sprints
    ALTER COLUMN team_id TYPE BIGINT USING NULL;

-- sprint_settings テーブル
ALTER TABLE undecided_ag.sprint_settings
    ALTER COLUMN product_id TYPE BIGINT USING NULL;
```

### 命名規則の根拠
- 既存の `V20261117100000__alter_employee_ids_to_bigint.sql` の直後（110000）
- 既存の命名規則（`V<日付><時刻>__<説明>.sql`）に準拠

---

## 3. 他モジュールからの参照と追随変更

### 3-1. productDiscovery 領域（`undecided.agiloom.productDiscovery`）

#### CreateScrumTeamCommandService.java
- `sprintZeroFactory.createSprintZero()` の呼び出しを `UUID` → `SnowflakeId` に変更
- `snowflakeIdToUuid()` 変換メソッドを削除（不再需要）
- 呼び出し箇所:
  ```java
  // 変更前
  sprintZeroFactory.createSprintZero(
      snowflakeIdToUuid(product.getId()),
      snowflakeIdToUuid(saved.getId()),
      saved.getName());

  // 変更後
  sprintZeroFactory.createSprintZero(
      product.getId(),
      saved.getId(),
      saved.getName());
  ```
- `import java.util.UUID` を削除（残りのUUID使用箇所がない場合）

#### CreateScrumTeamCommandServiceTest.java
- `SprintZeroFactory` のモック設定を `any(UUID.class)` → `any(SnowflakeId.class)` に変更
- `verify(sprintZeroFactory).createSprintZero(any(UUID.class), any(UUID.class), any(String.class))` → `any(SnowflakeId.class)`
- テスト内の `UUID` 関連の import を見直し

---

## 4. テストの追随範囲

### 4-1. sprintExecution 領域のテスト

| テストファイル | 変更内容 |
|---|---|
| `SprintTest.java` | `createSprint()` の `productId`・`teamId` を `UUID.randomUUID()` → `SnowflakeId.of(...)` に変更。`createSprintZero()` の引数も同様。`import java.util.UUID` を削除 |
| `SprintSettingsTest.java` | `createSettings()` の `productId` を `UUID.randomUUID()` → `SnowflakeId.of(...)` に変更。`import java.util.UUID` を削除 |
| `NotificationTest.java` | `createNotification()` の `productId` を `UUID.randomUUID()` → `SnowflakeId.of(...)` に変更。`recipientId` は `UUID` のまま（対象外）。`import java.util.UUID` は維持 |
| `SprintQueryControllerTest.java` | `productId` を `UUID` → `SnowflakeId` に変更。コントローラーの呼び出し方を `String` 経由に変更。`import java.util.UUID` を削除 |
| `SprintSettingsQueryControllerTest.java` | `productId` を `UUID` → `SnowflakeId` に変更。コントローラーの呼び出し方を `String` 経由に変更。`import java.util.UUID` を削除 |
| `SprintSettingsQueryServiceTest.java` | `productId` を `UUID` → `SnowflakeId` に変更。モック設定も同様。`import java.util.UUID` を削除 |
| `SprintCalculateEndDateMediumTest.java` | `productId` を `UUID` → `SnowflakeId` に変更。`import java.util.UUID` を削除 |

### 4-2. productDiscovery 領域のテスト

| テストファイル | 変更内容 |
|---|---|
| `CreateScrumTeamCommandServiceTest.java` | `SprintZeroFactory` のモック設定を `any(UUID.class)` → `any(SnowflakeId.class)` に変更 |

---

## 5. 作業手順

### ステップ1: Flyway マイグレーションの作成
1. `V20261117110000__alter_sprint_ids_to_bigint.sql` を `backend/src/main/resources/db/migration/ag/` に作成
2. 既存のマイグレーションファイルは変更しない

### ステップ2: SPI レイヤーの変更
1. `Sprint.java` — `productId`・`teamId` を `SnowflakeId` に変更、`createSprintZero()` のシグネチャ変更
2. `SprintSettings.java` — `productId` を `SnowflakeId` に変更
3. `Notification.java` — `productId` を `SnowflakeId` に変更、`create()` のシグネチャ変更
4. `SprintZeroFactory.java` — `createSprintZero()` のシグネチャ変更

### ステップ3: Repository レイヤーの変更
1. `SprintRepository.java` — 全メソッドの引数型を変更
2. `SprintSettingsRepository.java` — `findByProductId()` の引数型を変更
3. `NotificationRepository.java` — `findByProductId()` の引数型を変更

### ステップ4: Command・Query レイヤーの変更
1. `CreateSprintCommand.java` — `productId`・`teamId` を `SnowflakeId` に変更
2. `CreateSprintCommandService.java` — 変数型と処理を変更
3. `SprintZeroFactoryImpl.java` — オーバーライドシグネチャを変更
4. `UpdateSprintSettingsCommand.java` — `productId` を `SnowflakeId` に変更
5. `UpdateSprintSettingsCommandService.java` — 変数型と処理を変更
6. `SprintQueryService.java` — メソッドの引数型を変更
7. `SprintSettingsQueryService.java` — メソッドの引数型を変更

### ステップ5: Presentation レイヤーの変更
1. `CreateSprintRequest.java` — `productId`・`teamId` を `String` に変更
2. `SprintResponse.java` — `productId`・`teamId` を `String` に変更
3. `SprintSettingsResponse.java` — `productId` を `String` に変更
4. `UpdateSprintSettingsRequest.java` — `productId` を `String` に変更
5. `SprintQueryController.java` — `String` → `SnowflakeId` 変換を追加、レスポンスの `SnowflakeId` → `String` 変換を追加
6. `SprintSettingsCommandController.java` — 同上
7. `SprintSettingsQueryController.java` — 同上

### ステップ6: 他モジュールの追随変更
1. `CreateScrumTeamCommandService.java`（productDiscovery）— `snowflakeIdToUuid()` を削除し、直接 `SnowflakeId` を渡すように変更

### ステップ7: テストの変更
1. sprintExecution 領域の全テストファイルで `UUID` → `SnowflakeId` に変更
2. productDiscovery 領域の `CreateScrumTeamCommandServiceTest.java` でモック設定を変更

### ステップ8: ビルドとテスト検証
1. `./gradlew :backend:compileJava` — コンパイル確認
2. `./gradlew :backend:test` — 単体テスト（small タグ）
3. `./gradlew :backend:mediumTest` — medium タグのテスト
4. `./gradlew :backend:largeTest` — large タグのテスト（必要に応じて）

---

## 6. 検証方法

### 6-1. コンパイル検証
```bash
./gradlew :backend:compileJava
```
- 全ファイルの型整合性が取れていることを確認
- 特に `SprintZeroFactory` の SPI 境界で型が一致していることを確認

### 6-2. 単体テスト
```bash
./gradlew :backend:test
```
- sprintExecution 領域の全テストがパスすること
- productDiscovery 領域の `CreateScrumTeamCommandServiceTest` がパスすること

### 6-3. 統合テスト
```bash
./gradlew :backend:mediumTest
```
- `SprintCalculateEndDateMediumTest` が Testcontainers でパスすること
- DB スキーマ変更後の型整合性が確認できる

### 6-4. Flyway マイグレーション検証
```bash
./gradlew :backend:flywayMigrateAg
```
- 新規マイグレーションが正常に適用されること
- `sprints.product_id`、`sprints.team_id`、`sprint_settings.product_id` が `BIGINT` になっていることを確認

### 6-5. ArchUnit テスト
- モジュール境界違反がないことを確認
- `SprintZeroFactory` の SPI 境界が正しく保たれていることを確認

---

## 7. リスクと注意点

### 7-1. API 互換性
- `productId`・`teamId` の JSON 表現が `UUID` 形式（例: `"550e8400-e29b-41d4-a716-446655440000"`）から `string` 形式の整数（例: `"1234567890123456789"`）に変更される
- フロントエンドの対応が必要（別Issueで管理）

### 7-2. DB データ
- 開発データは再投入前提のため、`USING NULL` で型変換
- 本番データが存在する場合は、適切な変換ロジックが必要

### 7-3. Spring Data JPA のメソッド名導出
- Repository のメソッド名（例: `findByProductId`）は変更しない
- 引数型のみ `UUID` → `SnowflakeId` に変更
- Spring Data JPA は型情報からプロパティを解決するため、動作する

### 7-4. SnowflakeId の JPA Converter
- `SnowflakeId` には既に JPA の `AttributeConverter` が存在するものと仮定
- 存在しない場合は、`Sprint.id` が既に `SnowflakeId` で動作しているため、同じ仕組みが適用される

### 7-5. 順序依存性
- Flyway マイグレーションは `V20261117100000` の後に実行される必要がある
- `V20261117110000` の番号は既存の最新マイグレーションより大きいことを確認

---

## 判定（Muse、2026-11-17）

計画を採用する。ただし次の3点を修正条件とする。

1. **ユーザー参照のIDも移行対象に含める**。SprintBacklogItem.assigneeId、Activity.actorId、Impediment.assigneeId、Notification.recipientId（と NotificationRepository.findByRecipientIdAndIsRead）も SnowflakeId へ移行する。理由: 親決定 #583 は「UUIDになっているIDは全て」が対象であり、従業員IDは relationshipMgmt 側で SnowflakeId に統一済み（#589）。「別の移行Issue」は存在せず、切り出す根拠もない。あわせてテストの SprintBacklogItemTest、ImpedimentTest、SprintSettingsCalculatorTest も追随対象に含める（grep 実測で UUID 参照のあるテストは sprintExecution 領域10ファイル）。なお activities／impediments／notifications／sprint_backlog_items のテーブルが存在し、それらのカラムが UUID 型であれば、Flyway マイグレーションの対象にそれらのカラムも追加すること（存在しない場合は Java 側のみでよい）。
2. **Flyway のファイル名は V20261117170000__alter_sprint_ids_to_bigint.sql とする**。ag には #495 の V20261117160000 が先にマージされる可能性があり、V20261117110000 では out-of-order で Flyway が失敗する恐れがあるため。
3. **write_tests の独立ステップは設けない**（#589 と同様）。型移行で対象テストがコンパイルエラーとなり red が得られるため、テスト追随は implement の一部とする。検証は :backend:test と :backend:mediumTest の成功で確認する。

## 8. 完了条件

- [ ] 全対象ファイルの型変更が完了し、コンパイルエラーがない
- [ ] Flyway 新規マイグレーションが追加され、正常に適用される
- [ ] `./gradlew :backend:test` が全テストパス
- [ ] `./gradlew :backend:mediumTest` がパス
- [ ] 他モジュール（productDiscovery）の参照が正しく追随変更されている
- [ ] API の JSON 表現が `string` として ID を返す
- [ ] ArchUnit テストでモジュール境界違反がない
