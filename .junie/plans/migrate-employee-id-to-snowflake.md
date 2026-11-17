---
sessionId: session-261117-172039-6rfa
---

# Requirements

### Overview & Goals

`requirementBacklog` モジュールの `BacklogItem.creatorEmployeeId` と `BacklogItemComment.authorEmployeeId` を `UUID` から `SnowflakeId` へ移行します。プロジェクトのID規約（Snowflake ID を主キーおよび参照IDとして統一）に合わせ、既存の `ChangeBacklogItemStatusCommand.actorEmployeeId` や `ScrumTeamSubset.poEmployeeId/smEmployeeId` と同じパターンに統一します。

### Scope

**In Scope:**
- `spi` の `BacklogItem` と `BacklogItemComment` の型変更（`UUID` → `SnowflakeId`）
- `internal` のコマンド・リクエスト・レスポンスDTOの追随
- コントローラー・コマンドサービス・レスポンスコンバーターの追随
- 新規FlywayマイグレーションでDBカラムを `UUID` → `BIGINT` 化
- 既存テストの追随

**Out of Scope:**
- 既存Flywayマイグレーション（`V20261116150000`）の変更
- 他の事業領域のemployeeId移行
- `ScrumTeamSubset` のemployeeId（既に移行済み）

### User Stories

- As a developer, I want employee IDs in requirementBacklog to use SnowflakeId so that the ID convention is consistent across the project.
- As a developer, I want the API to accept employee IDs as string/long (via Jackson `@JsonCreator`) so that the JSON contract remains compatible.

### Functional Requirements

1. `BacklogItem.creatorEmployeeId` を `UUID` から `SnowflakeId` へ変更
2. `BacklogItemComment.authorEmployeeId` を `UUID` から `SnowflakeId` へ変更
3. 関連するコマンド・リクエスト・レスポンスDTOをすべて追随
4. APIのJSONではIDをstringとして扱う（`SnowflakeId` の `@JsonCreator` が `Long` でデシリアライズ）
5. DBは新規Flywayマイグレーションで `BIGINT` 化（既存マイグレーションは変更しない）

### Non-Functional Requirements

- `./gradlew :backend:test` が通ること
- `./gradlew :backend:mediumTest` が通ること
- モジュール境界（`ApplicationModules.verify()`）を破らないこと

# Delivery Steps

###   Step 1: Stage 1: SPIエンティティとコマンド・リクエスト・レスポンスの型変更
BacklogItem、BacklogItemComment、および関連するコマンド・リクエスト・レスポンスのemployeeIdをUUIDからSnowflakeIdへ変更します。
- `BacklogItem.java` (spi): `creatorEmployeeId`を`UUID`から`SnowflakeId`へ変更、ビルダーの引数も更新、`import java.util.UUID`を削除
- `BacklogItemComment.java` (spi): `authorEmployeeId`を`UUID`から`SnowflakeId`へ変更、`import java.util.UUID`を削除
- `CreateBacklogItemCommand.java` (internal): `creatorEmployeeId`を`UUID`から`SnowflakeId`へ変更
- `CreateBacklogItemRequest.java` (internal): `creatorEmployeeId`を`UUID`から`SnowflakeId`へ変更（APIはstring/longで受信、Jacksonの`@JsonCreator`が変換）
- `AddBacklogItemCommentCommand.java` (internal): `authorEmployeeId`を`UUID`から`SnowflakeId`へ変更
- `AddBacklogItemCommentRequest.java` (internal): `authorEmployeeId`を`UUID`から`SnowflakeId`へ変更
- `BacklogItemResponse.java` (internal): `creatorEmployeeId`を`UUID`から`SnowflakeId`へ変更
- `BacklogItemCommentResponse.java` (internal): `authorEmployeeId`を`UUID`から`SnowflakeId`へ変更

###   Step 2: Stage 2: コントローラー・コマンドサービス・コンバーターの追随とFlywayマイグレーション
コントローラーの変換コードを整理し、新規Flywayマイグレーションを追加します。
- `BacklogItemCommandController.java`: `createBacklogItem`および`addComment`メソッドでUUID→SnowflakeIdの明示的変換が不要になるため、`request.getCreatorEmployeeId()`と`request.getAuthorEmployeeId()`をそのままコマンドに渡す
- `BacklogItemResponseConverter.java`: 変更不要（型がSnowflakeIdになってもgetterの呼び出しは同じ、JSONシリアライズは`SnowflakeId`の`@JsonCreator`が対応）
- `CreateBacklogItemCommandService.java`・`AddBacklogItemCommentCommandService.java`: 変更不要（コマンドの型変更で自動追随）
- 新規Flywayマイグレーション `V20261118100000__alter_backlog_employee_ids_to_bigint.sql` を追加:
  - `backlog_items.creator_employee_id` を `UUID` から `BIGINT` へ変更（`USING NULL`）
  - `backlog_item_comments.author_employee_id` を `UUID` から `BIGINT` へ変更（`USING NULL`）
  - 既存マイグレーション `V20261116150000` は変更しない

###   Step 3: Stage 3: テストの追随と検証
既存テストを型変更に合わせて更新し、ビルドとテストを検証します。
- `BacklogItemTest.java`: ビルダーで `creatorEmployeeId` に `SnowflakeId` を設定するテストケースを追加・更新
- `CreateBacklogItemCommandServiceTest.java`: コマンドの `creatorEmployeeId` が `SnowflakeId` になることを確認
- `BacklogItemMigrationMediumTest.java`: 新規マイグレーションスクリプトの内容を検証するテストケースを追加（`creator_employee_id` と `author_employee_id` が `BIGINT` 化されることを確認）
- `./gradlew :backend:test` を実行して全テストが通ることを確認
- `./gradlew :backend:mediumTest` を実行して統合テストが通ることを確認

# 判定（Muse、2026-11-17）

採用。ただし以下の条件を付す。

1. **Flywayのバージョンは `V20261117180000` とする**（計画の V20261118100000 は使わない）。ag の既存マイグレーションは V20261117170000（#588）まで進んでおり、同日内の次の番号で採番するのがこのプロジェクトの慣例。ファイル名は `V20261117180000__alter_backlog_employee_ids_to_bigint.sql`。
2. **Request／Response の変換方式は既存の移行済みパターンに厳密に合わせる**。特に `ChangeBacklogItemStatusRequest`（#586で移行済み。actorEmployeeId が SnowflakeId で、Jackson 変換に委ねる方式）と `BacklogItemResponse` 内の既存 SnowflakeId フィールド（scrumTeamId 等）の扱いを正とする。独自の変換コードは追加しない。
3. **write_tests の独立ステップは設けない**。型移行タスクでは対象テストのコンパイルエラーで red が自動的に得られるため、テストの追随は implement に含める（#589／#588 と同じ適応）。
4. **検証は `:backend:test` と `:backend:mediumTest` の両方を必須とする**（#609 の CI 失敗の教訓）。BacklogItemMigrationMediumTest へは計画どおり新規マイグレーションのスクリプト検証テストを追加する。なお同テストのカラム名の完全一致検証（containsExactly）はカラム名のみが対象で、今回の型変更では期待値の変更は不要なはずだが、実行して確認すること。
5. frontend は対象外（従業員識別は #573 の範囲）。ただし API の JSON 形式が既存の移行済みエンドポイントと同形式になることを確認する。