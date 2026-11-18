# Issue #675 実装計画 — BacklogItemに楽観ロック（@Version）を追加する

## 概要

BacklogItem エンティティに `@Version` フィールドを追加し、Flyway マイグレーションで `version` カラムを新規作成する。同時に、`OptimisticLockingFailureException`（およびサブクラス `ObjectOptimisticLockingFailureException`）を HTTP 409 Conflict に変換する例外ハンドラーを追加する。

## 背景

同時実行時に BacklogItem の状態変更や内容更新が後勝ちで上書きされると、ロングタームイベントパターン（`BacklogItemStatusChanged`）の履歴に不整合（同じ遷移元からの重複遷移）が残る。楽観ロックにより、同時更新を検知して拒否する。

## 調査結果

### 現状

| 項目 | 現状 |
|------|------|
| BacklogItem `@Version` | 未設定 |
| `version` カラム | 未作成 |
| プロジェクト内の `@Version` 使用例 | なし（初の導入） |
| 例外ハンドラー | `BacklogItemExceptionHandler`（BacklogItem 専用）+ `GlobalExceptionHandler`（全体用） |
| `OptimisticLockingFailureException` ハンドラー | 未実装 |
| テスト方針 | small test（Mockito）を基本、DB 統合テストは medium test |

### 例外ハンドラーの追加先

`BacklogItemExceptionHandler` に追加する。理由：
- `@RestControllerAdvice(assignableTypes = {BacklogItemCommandController.class, BacklogItemQueryController.class})` で BacklogItem のコントローラーに限定されている
- `OptimisticLockingFailureException` は JPA エンティティの楽観ロック失敗時に発生するため、BacklogItem に関連する例外として扱うのが適切
- `GlobalExceptionHandler` の `@ExceptionHandler(Exception.class)` は最終手段であり、特定の例外は各領域のハンドラーで処理する設計

## 変更対象ファイル

### 1. エンティティ変更

**`backend/src/main/java/undecided/agiloom/requirementBacklog/spi/BacklogItem.java`**

- `@Version` フィールド `protected Integer version` を追加
- import に `jakarta.persistence.Version` を追加
- `@Getter` が付与されているため、getter は自動生成される
- `version` は `Integer`（null 可）とする。新規作成時は `null`（DB では `0` として初期化）
- ビルダーパターン（`@Builder`）は `build()` メソッド内で `version` を設定しない（JPA が管理）

### 2. Flyway マイグレーション

**新規 `backend/src/main/resources/db/migration/ag/V20261118210000__add_version_to_backlog_items.sql`**

```sql
ALTER TABLE undecided_ag.backlog_items
    ADD COLUMN version INTEGER DEFAULT 0;

COMMENT ON COLUMN undecided_ag.backlog_items.version IS '楽観ロック用のバージョン番号';
```

- バージョン番号：`V20261118210000`（既存最大は `V20261118200000`）
- `DEFAULT 0` を設定し、既存行の version を `0` に初期化
- `INTEGER` 型（Hibernate の `@Version` のデフォルトマッピングと一致）

### 3. 例外ハンドラー変更

**`backend/src/main/java/undecided/agiloom/requirementBacklog/internal/BacklogItemExceptionHandler.java`**

- `OptimisticLockingFailureException` を HTTP 409 Conflict に変換するハンドラーを追加
- import に `org.springframework.orm.ObjectOptimisticLockingFailureException` と `org.springframework.orm.ObjectOptimisticLockException` を追加

```java
@ExceptionHandler(OptimisticLockingFailureException.class)
ResponseEntity<Map<String, String>> handleOptimisticLockingFailure(
    OptimisticLockingFailureException ex) {
  return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message", ex.getMessage()));
}
```

- `OptimisticLockingFailureException` は `ObjectOptimisticLockingFailureException` のスーパークラスなので、サブクラスもキャッチされる
- `OptimisticLockException`（インタフェース）も同様に `OptimisticLockingFailureException` でカバーされる
- javadoc のコメントを更新（楽観ロック失敗: 409 を追加）

### 4. テスト変更

#### 4a. BacklogItem の単体テスト更新

**`backend/src/test/java/undecided/agiloom/requirementBacklog/spi/BacklogItemTest.java`**

- 既存テストのビルダー呼び出しが `version` を設定しないため、既存テストはそのまま動作する
- `version` フィールドの初期値が `null` であることを確認するテストを追加（optional）

#### 4b. 例外ハンドラーのテスト

**新規 `backend/src/test/java/undecided/agiloom/requirementBacklog/internal/BacklogItemExceptionHandlerTest.java`**

- `BacklogItemExceptionHandler` の各ハンドラーの単体テスト（small test）
- `OptimisticLockingFailureException` → HTTP 409 の変換テスト
- `ObjectOptimisticLockingFailureException` → HTTP 409 の変換テスト（サブクラスも対象）
- 既存のハンドラー（PermissionDenied → 403, IllegalStateException → 409, IllegalArgumentException → 400）のテストも追加

#### 4c. マイグレーションテスト更新

**`backend/src/test/java/undecided/agiloom/requirementBacklog/internal/BacklogItemMigrationMediumTest.java`**

- `shouldBacklogItemsHaveExpectedColumns()` の期待カラムリストに `"version"` を追加
- 新マイグレーションスクリプトの内容を検証するテストを追加（既存のパターンに倣う）

#### 4d. 楽観ロックの統合テスト

**新規 `backend/src/test/java/undecided/agiloom/requirementBacklog/internal/BacklogItemOptimisticLockMediumTest.java`**

- Testcontainers を使用した medium test
- 同一 BacklogItem に対する同時更新で `ObjectOptimisticLockingFailureException` が発生することを確認
- 状態変更（`changeStatus`）と内容更新（`update`）の両方で楽観ロックが効くことを確認

## 手順

### Step 1: Flyway マイグレーションの作成

1. `V20261118210000__add_version_to_backlog_items.sql` を作成
2. `ALTER TABLE` で `version` カラムを追加（`INTEGER DEFAULT 0`）
3. `COMMENT ON COLUMN` で説明を追加

### Step 2: BacklogItem エンティティに @Version を追加

1. `jakarta.persistence.Version` の import を追加
2. `protected Integer version` フィールドに `@Version` アノテーションを付与
3. `@Column(name = "version")` も付与（明示的なマッピング）
4. ビルダーの `build()` メソッドは変更しない（`version` は JPA 管理フィールド）

### Step 3: 例外ハンドラーに OptimisticLockingFailureException ハンドラーを追加

1. `BacklogItemExceptionHandler` に `OptimisticLockingFailureException` のハンドラーを追加
2. import を追加
3. クラス javadoc のコメントを更新

### Step 4: テストの作成と更新

1. `BacklogItemMigrationMediumTest` のカラムリストに `"version"` を追加
2. マイグレーションスクリプト検証テストを追加
3. `BacklogItemExceptionHandlerTest` を新規作成（small test）
4. `BacklogItemOptimisticLockMediumTest` を新規作成（medium test）

### Step 5: ビルドとテストの実行

1. `./gradlew :backend:test` — small test がすべて通過すること
2. `./gradlew :backend:mediumTest` — medium test がすべて通過すること
3. マイグレーションが正しく適用されることを確認

## テスト観点

| 観点 | テスト種別 | テストファイル |
|------|-----------|---------------|
| `@Version` フィールドが `null` で初期化される | small | `BacklogItemTest` |
| `OptimisticLockingFailureException` → HTTP 409 | small | `BacklogItemExceptionHandlerTest` |
| `ObjectOptimisticLockingFailureException` → HTTP 409 | small | `BacklogItemExceptionHandlerTest` |
| 既存ハンドラー（403, 409, 400）の動作維持 | small | `BacklogItemExceptionHandlerTest` |
| 状態変更で楽観ロック例外が発生する | medium | `BacklogItemOptimisticLockMediumTest` |
| 内容更新（title/value）で楽観ロック例外が発生する | medium | `BacklogItemOptimisticLockMediumTest` |
| `version` カラムが DB に存在する | medium | `BacklogItemMigrationMediumTest` |
| マイグレーションスクリプトの内容が正しい | medium | `BacklogItemMigrationMediumTest` |

## リスクと対応

| リスク | 影響 | 対応 |
|--------|------|------|
| `@Version` の型を `Integer` にすると、初期値 `null` と DB の `DEFAULT 0` の不一致 | Hibernate は `null` を `0` として扱えるが、明示的に `0` で初期化した方が安全 | DB 側で `DEFAULT 0` を設定。Hibernate は初回読み込みで `0` をセットする |
| 既存のテストで `version` が `null` のままになる | `@Version` は JPA 管理フィールドであり、テストのビルダーでは設定しない | テストでは `version` の値をアサートしない（JPA 管理フィールドのため） |
| 同時更新テストの再現性 | 単体テストではスレッド競合を再現しにくい | medium test で Testcontainers + 実際の DB トランザクションを使用して検証 |
| `OptimisticLockingFailureException` の import パス | Spring Boot 4 / Spring Framework 7 でパッケージが変更されている可能性 | `org.springframework.orm.ObjectOptimisticLockingFailureException` を使用（Spring Framework の標準パス） |

## 非対応事項

- 他のエンティティへの `@Version` 拡張は本 Issue の範囲外
- API レスポンスに `version` フィールドを含めることはしない（クライアントが明示的にバージョン管理する必要はない）
- `If-Match` ヘッダーなどの HTTP レベルの楽観ロックは対象外

## 完了条件

- [ ] `BacklogItem` に `@Version` フィールドが追加されている
- [ ] Flyway マイグレーション `V20261118210000` が作成されている
- [ ] `BacklogItemExceptionHandler` に `OptimisticLockingFailureException` → 409 のハンドラーがある
- [ ] `./gradlew :backend:test` がすべて通過する
- [ ] `./gradlew :backend:mediumTest` がすべて通過する
- [ ] 状態変更と内容更新の両方で楽観ロックが効くことがテストで検証されている
