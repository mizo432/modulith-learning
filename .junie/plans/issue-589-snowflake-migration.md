# Issue #589 実装計画：relationshipMgmt の UUID を SnowflakeId へ移行

## 概要

Issue #583「UUIDになっているIDは全てSnowflakeIdに変更する」の分割Issue。
`backend` の `undecided.association.relationshipMgmt` 配下で、残存する `java.util.UUID` 参照を `undecided.supporting.snowflake.SnowflakeId` に統一する。

**方針**: Java側は `SnowflakeId` 値オブジェクト、DBカラムは `BIGINT`、画面・APIでは `string` として扱う。

---

## 1. 対象ファイル一覧と変更内容

### 1.1 personRole（従業員）

| # | ファイル | 変更内容 |
|---|---------|---------|
| 1 | `personRole/spi/employee/Employee.java` | **UUID → SnowflakeId の完全移行**。<br>• `employeeId` フィールドを `UUID` から `SnowflakeId` に変更<br>• `create()` メソッドの第1引数を `UUID` から `SnowflakeId` に変更<br>• `@Id` アノテーション維持、`@Column(name = "employee_id")` を追加（BIGINTマッピング用）<br>• `import java.util.UUID` を削除、`SnowflakeId` のimportを追加 |
| 2 | `personRole/internal/employee/EmployeeApi.java` | `UUID.randomUUID()` を `SnowflakeId.of(...)` に変更。ダミーデータ生成用なので `SnowflakeId.of(1L)` 等を使用 |

### 1.2 orgRole（組織）

| # | ファイル | 変更内容 |
|---|---------|---------|
| 3 | `orgRole/spi/OrganizationQuery.java` | `findById(UUID id)` → `findById(SnowflakeId id)` に変更。`import java.util.UUID` を削除 |
| 4 | `orgRole/internal/organization/OrganizationApi.java` | `@PathVariable("id") UUID id` → `@PathVariable("id") String id` に変更し、`SnowflakeId.of(Long.parseLong(id))` で変換。`import java.util.UUID` を削除 |
| 5 | `orgRole/internal/organization/OrganizationQueryImpl.java` | `findById(UUID id)` → `findById(SnowflakeId id)` に変更。`import java.util.UUID` を削除 |
| 6 | `orgRole/internal/organization/OrganizationRepository.java` | `CrudRepository<Organization, UUID>` → `CrudRepository<Organization, SnowflakeId>` に変更。`import java.util.UUID` を削除 |

**補足**: `orgRole/spi/Organization.java` の `organizationId` は既に `SnowflakeId` であるため、変更不要。ただし、`OrganizationRepository` のID型が `UUID` のままのため、JPAが正しく動作しない可能性がある。今回の移行で整合する。

### 1.3 partyRole（ロール割り当て）

| # | ファイル | 変更内容 |
|---|---------|---------|
| 7 | `partyRole/spi/role/RoleAssignmentForEmp.java` | **3つのUUIDフィールドをSnowflakeIdに変更**。<br>• `roleAssignmentForEmpId`: `UUID` → `SnowflakeId`<br>• `roleId`: `UUID` → `SnowflakeId`<br>• `employeeId`: `UUID` → `SnowflakeId`<br>• `create()` メソッドの全引数を `SnowflakeId` に変更<br>• `import java.util.UUID` を削除 |
| 8 | `partyRole/spi/role/RoleAssignmentForOrg.java` | **2つのUUIDフィールドをSnowflakeIdに変更**。<br>• `roleAssignmentForOrgId`: `UUID` → `SnowflakeId`<br>• `roleId`: `UUID` → `SnowflakeId`<br>• `org` フィールド（`Org` 型）は変更不要（`Org` は既に `SnowflakeId` ベース）<br>• `create()` メソッドの第1, 2引数を `SnowflakeId` に変更<br>• `import java.util.UUID` を削除 |
| 9 | `partyRole/internal/role/RoleAssignmentsForEmpApi.java` | `UUID.randomUUID()` を `SnowflakeId.of(...)` に変更（3箇所） |
| 10 | `partyRole/internal/role/RoleAssignmentsForOrgApi.java` | `UUID.randomUUID()` を `SnowflakeId.of(...)` に変更（2箇所） |
| 11 | `partyRole/internal/role/RoleRepository.java` | `CrudRepository<Role, UUID>` → `CrudRepository<Role, SnowflakeId>` に変更。`import java.util.UUID` を削除 |

**補足**: `partyRole/spi/role/Role.java` の `roleId` は既に `SnowflakeId` であるため、変更不要。`RoleRepository` のID型が `UUID` のままのため、今回の移行で整合する。

### 1.4 shared（共通ライブラリ）

| # | ファイル | 変更内容 |
|---|---------|---------|
| 12 | `com/github/mizo432/shared/infra/migration/V20261027130000__encrypt_employee_names.java` | `UUID.fromString(rs.getString("employee_id"))` → `Long.valueOf(rs.getString("employee_id"))` に変更し、`updateStmt.setObject(3, employeeId)` の引数を `Long` に変更。`import java.util.UUID` を削除 |

---

## 2. Flyway 新規マイグレーション

### 2.1 新規マイグレーションファイル

**ファイル名**: `V20261117160000__alter_employees_employee_id_to_bigint.sql`

**配置場所**: `backend/src/main/resources/db/migration/`（ルート）

**内容**:
```sql
-- employeesテーブルのemployee_idをUUIDからBIGINTへ変更
-- Issue #589: relationshipMgmtのUUIDをSnowflakeIdへ移行

-- 既存のemployeesテーブルを再作成（UUID→BIGINT）
-- 既存データは開発データの再投入で対応する前提

drop table if exists employees;

create table employees
(
    employee_id bigint       not null primary key,
    lastName    text         not null,
    firstName   text         not null,
    valid_from  timestamp    not null,
    valid_to    timestamp
);
```

**理由**:
- PostgreSQL で `UUID` 型から `BIGINT` 型への直接変換は不可能（`ALTER TABLE ... ALTER COLUMN ... TYPE` では型互換性がない）
- 既存データは開発データであり、再投入で対応する前提（Issue本文記載）
- 既存のマイグレーションファイル（`V20261027070000` 等）は変更しない

### 2.2 影響を受けるJavaマイグレーション

`V20261027130000__encrypt_employee_names.java` は `employees` テーブルの `employee_id` を `UUID.fromString()` で読み取る。上記の新規マイグレーションが適用された後、このJavaマイグレーションは `employee_id` が `BIGINT` になっているため、1.4の変更（`Long.valueOf()` への変更）が必要。

**マイグレーション実行順序**:
1. `V20261027070000__add_employees_table.sql`（既存、UUIDでテーブル作成）
2. `V20261027080000__alter_employee_name_columns_for_encryption.sql`（既存、カラム型変更）
3. `V20261027123200__rename_employee_lastNane_to_lastName.sql`（既存、リネーム）
4. `V20261027130000__encrypt_employee_names.java`（既存、暗号化）→ **この時点ではemployee_idはUUID**
5. `V20261117160000__alter_employees_employee_id_to_bigint.sql`（**新規**、UUID→BIGINT）

**重要な注意点**: `V20261027130000__encrypt_employee_names.java` は新規マイグレーション**より前**に実行されるため、このJavaマイグレーション内のUUID参照は「暗号化実行時にはまだUUID」であることを意味する。つまり、**Javaマイグレーション内のUUID参照は変更不要**であり、新規マイグレーションでテーブルを再作成する際に、Javaマイグレーションの処理結果（暗号化済みデータ）は失われる。

**解決策**: Javaマイグレーション `V20261027130000__encrypt_employee_names.java` の `UUID.fromString()` を `Long.valueOf()` に変更する。ただし、このJavaマイグレーションは新規マイグレーション**より前**に実行されるため、実行時には `employee_id` はまだ `UUID` 型である。この矛盾を解消するため、以下の2つの選択肢がある：

**選択肢A（推奨）**: Javaマイグレーションのバージョン番号を更新して、新規マイグレーションの後に実行されるようにする。
- ファイル名を `V20261117170000__encrypt_employee_names.java` に変更
- この場合、`employees` テーブルは既に `BIGINT` 型になっているため、`Long.valueOf()` への変更が正しく動作する

**選択肢B**: Javaマイグレーションを2つに分割し、UUID対応版とBIGINT対応版を用意する。

**本計画では選択肢Aを採用する**。

---

## 3. テストの追随範囲

### 3.1 修正が必要なテストファイル

| # | テストファイル | 変更内容 |
|---|--------------|---------|
| T1 | `personRole/spi/employee/EmployeeTest.java` | `UUID.randomUUID()` → `SnowflakeId.of(...)` に変更（全テストケース） |
| T2 | `generic/relationshipMgmt/internal/OrganizationQueryImplTest.java` | `UUID` 参照を `SnowflakeId` に変更。`testUuid` フィールドの型変更、`findById()` の引数変更 |

### 3.2 影響を受けないテストファイル（確認済み）

- `personRole/spi/employee/SimpleEmployeeTest.java` — `SimpleEmployee` は既に `SnowflakeId` ベース
- `personRole/internal/command/*Test.java` — `SimpleEmployee` / `EmployeeDto` を使用。`SnowflakeId` ベースのため変更不要
- `orgRole/spi/DepartmentTest.java` — `Department` は既に `SnowflakeId` ベース
- `partyRole` 関連のテスト — 対象のspiエンティティのテストが存在しないため、新規作成の検討対象

### 3.3 新規テストの検討

- `RoleAssignmentForEmp` / `RoleAssignmentForOrg` の単体テストが存在しない場合、`SnowflakeId` 移行後の整合性を確認するテストを追加検討

---

## 4. 影響を受ける呼び出し側（他モジュールからの参照）

### 4.1 他モジュールからの参照確認

grep検索の結果、`undecided.association.relationshipMgmt.(personRole|orgRole|partyRole)` を `import` しているファイルは、すべて `backend` モジュール内の `undecided.association.relationshipMgmt` 配下およびテスト配下であった。

**他事業領域からの参照**: なし（確認済み）

### 4.2 モジュール内の依存関係

- `personRole/spi/employee/Employee` → `EmployeeApi` から参照（同一モジュール内）
- `orgRole/spi/OrganizationQuery` → `OrganizationApi`, `OrganizationQueryImpl` から参照（同一モジュール内）
- `partyRole/spi/role/RoleAssignmentForEmp` → `RoleAssignmentsForEmpApi`, `Role.java` から参照（同一モジュール内）
- `partyRole/spi/role/RoleAssignmentForOrg` → `RoleAssignmentsForOrgApi`, `Role.java` から参照（同一モジュール内）

上記はすべて同一モジュール（relationshipMgmt）内の参照であり、モジュール境界を跨ぐ影響はない。

---

## 5. 作業手順

### フェーズ1: DBマイグレーション

1. **新規Flywayマイグレーション作成**
   - `backend/src/main/resources/db/migration/V20261117160000__alter_employees_employee_id_to_bigint.sql` を作成
   - `employees` テーブルを `UUID` → `BIGINT` に再作成

2. **Javaマイグレーションのバージョン更新**
   - `V20261027130000__encrypt_employee_names.java` を `V20261117170000__encrypt_employee_names.java` にリネーム
   - `UUID.fromString()` → `Long.valueOf()` に変更
   - `UUID employeeId` → `Long employeeId` に変更
   - `import java.util.UUID` を削除

### フェーズ2: SPIエンティティの変更

3. **personRole/spi/employee/Employee.java** の変更
   - `employeeId` を `SnowflakeId` に変更
   - `create()` メソッドのシグネチャ変更

4. **partyRole/spi/role/RoleAssignmentForEmp.java** の変更
   - 3つのUUIDフィールドを `SnowflakeId` に変更
   - `create()` メソッドのシグネチャ変更

5. **partyRole/spi/role/RoleAssignmentForOrg.java** の変更
   - 2つのUUIDフィールドを `SnowflakeId` に変更
   - `create()` メソッドのシグネチャ変更

### フェーズ3: インターフェースとリポジトリの変更

6. **orgRole/spi/OrganizationQuery.java** の変更
   - `findById(UUID)` → `findById(SnowflakeId)`

7. **orgRole/internal/organization/OrganizationRepository.java** の変更
   - `CrudRepository<Organization, UUID>` → `CrudRepository<Organization, SnowflakeId>`

8. **partyRole/internal/role/RoleRepository.java** の変更
   - `CrudRepository<Role, UUID>` → `CrudRepository<Role, SnowflakeId>`

### フェーズ4: 実装クラスとAPIの変更

9. **orgRole/internal/organization/OrganizationQueryImpl.java** の変更
   - `findById(UUID)` → `findById(SnowflakeId)`

10. **orgRole/internal/organization/OrganizationApi.java** の変更
    - `@PathVariable UUID` → `@PathVariable String` + `SnowflakeId.of(Long.parseLong(id))`

11. **personRole/internal/employee/EmployeeApi.java** の変更
    - `UUID.randomUUID()` → `SnowflakeId.of(...)`

12. **partyRole/internal/role/RoleAssignmentsForEmpApi.java** の変更
    - `UUID.randomUUID()` → `SnowflakeId.of(...)`

13. **partyRole/internal/role/RoleAssignmentsForOrgApi.java** の変更
    - `UUID.randomUUID()` → `SnowflakeId.of(...)`

### フェーズ5: テストの追随

14. **EmployeeTest.java** の修正
    - `UUID.randomUUID()` → `SnowflakeId.of(...)`

15. **OrganizationQueryImplTest.java** の修正
    - `UUID` → `SnowflakeId` の全参照変更

### フェーズ6: 検証

16. **ビルドとテスト実行**
    ```bash
    ./gradlew :backend:test
    ```
17. **Flywayマイグレーション検証**
    ```bash
    ./gradlew :backend:flywayMigrateAs
    ```
18. **ArchUnitテスト確認**（モジュール境界違反がないか）

---

## 6. 検証方法

### 6.1 自動テスト

| 検証項目 | コマンド | 成功基準 |
|---------|---------|---------|
| 全テスト通過 | `./gradlew :backend:test` | 全テスト成功（FAILUREなし） |
| mediumタグテスト | `./gradlew :backend:mediumTest` | 全テスト成功 |
| ビルド | `./gradlew :backend:build` | ビルド成功 |

### 6.2 手動検証

| 検証項目 | 方法 |
|---------|------|
| Flywayマイグレーション | `./gradlew :backend:flywayMigrateAs` 実行後、`employees` テーブルの `employee_id` 型が `BIGINT` であることを確認 |
| APIエンドポイント | `OrganizationApi` の `GET /api/organizations/{id}` で数値IDを指定して正常レスポンスを確認 |
| ArchUnit | モジュール境界テストが失敗しないことを確認 |

---

## 7. リスクと対応

| リスク | 影響 | 対応 |
|-------|------|------|
| 既存データ消失 | `employees` テーブルの再作成でデータが失われる | 開発データのみであり、再投入で対応する前提（Issue本文記載） |
| Javaマイグレーションの順序 | バージョン番号の更新で実行順序が変化する | 新規マイグレーションの後に実行されるようバージョン番号を調整 |
| `RoleAssignmentForOrg` の `org` フィールド | `@ManyToOne` 関連が `Org` 型（`SnowflakeId` ベース）のため、整合性確認が必要 | `Org` は既に `SnowflakeId` ベースのため、追加変更は不要 |
| Testcontainers | テスト用コンテナのスキーマが更新される | Flywayマイグレーションが自動適用されるため、問題なし |

---

## 判定（Muse、2026-11-17）

計画を採用する。ただし次の2点を修正条件とする。

1. **Javaマイグレーション V20261027130000__encrypt_employee_names.java は変更もリネームもしない**。本計画2.2自身の分析どおり、このマイグレーションは新規マイグレーションより前に実行され、実行時点では employee_id はまだ UUID 型であるため、UUID.fromString() の使用はその時点のスキーマに対して正しい。Flyway の適用済みマイグレーションのバージョンを変更すると既存DBで適用済みマイグレーションの解決に失敗し、プロジェクト方針「既存マイグレーションファイルは変更しない」にも反する。#583 の移行対象はアプリケーションコードであり、履歴マイグレーションは当時のスキーマに合わせて凍結する。したがって1.4とフェーズ1の手順2は実施しない。
2. **write_tests の独立ステップは設けない**。型移行により対象テスト2ファイル（EmployeeTest、OrganizationQueryImplTest）はコンパイルエラーとなり red が自動的に得られるため、テストの追随は implement の一部として行う。検証は backend 全テストの成功と、対象ファイル群に UUID 参照が残っていないことの grep 確認で行う。

補足の実測事実: ルートの既存マイグレーションの最大バージョンは V20261028070700 で、V20261117160000 はその後ろに正しく位置する。employee_id カラムを持つルートのテーブルは employees のみで、他テーブルへの波及はない。再作成DDLは現行定義（lastName／firstName は TEXT、valid_from／valid_to）と一致していることを確認済み。

## 8. 完了条件

- [ ] 12ファイルのJavaコード変更が完了し、コンパイルエラーなし
- [ ] 新規Flywayマイグレーションが追加され、既存ファイルは変更されていない
- [ ] Javaマイグレーションのバージョン番号が更新され、`employee_id` の参照が `Long` に変更されている
- [ ] 2つのテストファイルが追随変更されている
- [ ] `./gradlew :backend:test` が全テスト成功
- [ ] `./gradlew :backend:build` が成功
- [ ] モジュール境界違反なし（ArchUnitテスト成功）
