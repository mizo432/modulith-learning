---
name: data-design
description: データベース設計、データモデリング、JPA/Hibernateパターン、Flywayマイグレーション、データアクセス戦略、パフォーマンス最適化、データセキュリティの設計ルールを提供。
---

### Data Design Guide (`data-design`)

このスキルは、 **modulith-learning**
リポジトリにおいて、データベース設計、データモデリング、JPA/Hibernateを使用した永続化層の設計、Flywayによるスキーママイグレーション、データアクセスパターン、パフォーマンス最適化、データセキュリティを行う際に適用する設計ガイドラインです。

---

### 1. いつこのスキルを使用するか (When To Use)

- 新規テーブル・スキーマの設計を行うとき
- データモデル（エンティティ関係、正規化、非正規化）を設計するとき
- JPAエンティティのマッピング設計を行うとき
- Flywayマイグレーションスクリプトを作成するとき
- データアクセスパターン（リポジトリ、クエリ）を設計するとき
- データベースパフォーマンスを最適化するとき
- データセキュリティ（暗号化、アクセス制御）を設計するとき
- データの整合性制約（ユニーク制約、チェック制約）を設計するとき
- 大量データのインポート・エクスポート処理を設計するとき

---

### 2. データモデリング原則 (Data Modeling Principles)

#### 2.1 ドメイン駆動設計との整合性

- **エンティティ**: ドメインエンティティとデータベースエンティティを明確に分離。ドメイン層にはビジネスロジックを、インフラ層にはJPAマッピングを配置。
- **値オブジェクト**: 不変の値オブジェクトは埋め込み可能型（`@Embeddable`）として設計し、データベースではカラムレベルで表現。
- **アグリゲーション**: アグリゲーションルートを中心にデータアクセスを設計し、アグリゲーション内のエンティティはルート経由でアクセス。

#### 2.2 正規化と非正規化のバランス

- **第3正規形（3NF）**: データモデルの基本は3NFとし、更新異常を防ぐ。
- **戦略的非正規化**: 頻繁に結合される読み取り専用データは、パフォーマンスのために非正規化を検討。
- **CQRSパターン**: 書き込みモデルは正規化し、読み込みモデルはクエリ最適化のために非正規化を検討。

#### 2.3 データ型選択ガイドライン

| データ種別   | 推奨型              | 備考                   |
|:-------------|:--------------------|:-----------------------|
| 主キー       | `BIGINT`            | Snowflake ID対応       |
| 文字列（短） | `VARCHAR(N)`        | 最大長を明示           |
| 文字列（長） | `TEXT`              | 記事本文、備考など     |
| 日付時刻     | `TIMESTAMP`         | タイムゾーン付き       |
| 論理値       | `BOOLEAN`           | true/false             |
| 数値（整数） | `INTEGER`, `BIGINT` | 範囲に応じて選択       |
| 数値（小数） | `DECIMAL(p, s)`     | 金額などは桁数を明示   |
| JSONデータ   | `JSON`              | 柔軟な構造が必要な場合 |

---

### 3. データベース設計 (Database Design)

#### 3.1 スキーマ分離戦略

- **事業領域・業務領域ごとにスキーマ分離**: 各事業領域・業務領域は独立したスキーマを持ち、データ独立性を確保。
- **スキーマ命名規則**: 事業領域区分によってスキーマ名の付け方が異なります。
    - **事業領域区分が「汎用」の場合**: `undecided_<業務領域コード>` の形式で命名（例:
      `undecided_ef-form`, `undecided_ar-approval`）。
    - **事業領域区分が「汎用」以外の場合**: `undecided_<事業領域コード>` の形式で命名（例:
      `undecided_as`,
      `undecided_do`）。
- **事業領域コード一覧**: 事業領域一覧.tsvに記載された事業領域コードと事業領域区分を参照してください。

  > 参照: `docs/11.事業領域/事業領域一覧.tsv`

- **業務領域コード一覧**: 事業領域区分が「汎用」の場合、業務領域一覧.tsvに記載された業務領域コードを参照してください。

  > 参照: `docs/02 業務領域/業務領域一覧.tsv`

- **スキーマ間参照禁止**: 事業領域間の外部キー制約は設定せず、アプリケーションレベルで整合性を管理。

#### 3.2 テーブル命名規則

| 要素       | 規則                   | 例                         |
|:-----------|:-----------------------|:---------------------------|
| テーブル名 | スネークケース、複数形 | `customers`, `order_items` |
| 主キー     | `id`                   | `id BIGINT PRIMARY KEY`    |
| 外部キー   | `<関連名>_id`          | `customer_id`, `order_id`  |
| 日付カラム | `_at` サフィックス     | `created_at`, `updated_at` |
| 論理削除   | `deleted_at`           | nullなら有効、日時なら削除 |

#### 3.3 標準カラム

すべてのテーブルに以下の標準カラムを含める。

```sql
id              BIGINT          PRIMARY KEY,
created_at      TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
updated_at      TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
deleted_at      TIMESTAMP       NULL
```

#### 3.4 制約設計

- **主キー制約**: すべてのテーブルに `id BIGINT PRIMARY KEY` を定義。
- **ユニーク制約**: ビジネス上の一意性を保証するカラムに `UNIQUE` 制約を適用。
- **チェック制約**: データの整合性を保証する条件を `CHECK` 制約で定義。
- **NOT NULL制約**: 必須カラムには `NOT NULL` を明示。

```sql
CREATE TABLE customers
(
    id            BIGINT PRIMARY KEY,
    customer_code VARCHAR(20)  NOT NULL UNIQUE,
    name          VARCHAR(100) NOT NULL,
    email         VARCHAR(255) NOT NULL UNIQUE,
    status        VARCHAR(20)  NOT NULL DEFAULT 'ACTIVE'
        CHECK (status IN ('ACTIVE', 'INACTIVE', 'SUSPENDED')),
    created_at    TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted_at    TIMESTAMP    NULL
);
```

---

### 4. JPA/Hibernate設計 (JPA/Hibernate Design)

#### 4.1 エンティティマッピングの基本パターン

- **@Entity**: ドメインエンティティをJPAエンティティとしてマッピング。
- **@Table**: スキーマとテーブル名を明示的に指定。
- **@Id**: 主キーを指定。Snowflake IDの場合は `@GeneratedValue(strategy = IDENTITY)`
  を使用せず、アプリケーション側で生成。

```java

@Entity
@Table(schema = "undecided_as", name = "customers")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CustomerEntity {

  @Id
  private Long id;

  @Column(name = "customer_code", nullable = false, unique = true, length = 20)
  private String customerCode;

  @Column(name = "name", nullable = false, length = 100)
  private String name;
}
```

#### 4.2 関連マッピング

- **@OneToOne**: 1対1の関係。`mappedBy` で所有側を明確化。
- **@OneToMany**: 1対多の関係。`orphanRemoval = true` で子エンティティの自動削除を有効化。
- **@ManyToOne**: 多対1の関係。外部キーを保持する側。
- **@ManyToMany**: 多対多の関係。中間テーブルを明示的に定義することを推奨。

```java

@OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
private List<OrderEntity> orders = new ArrayList<>();

@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "customer_id", nullable = false)
private CustomerEntity customer;
```

#### 4.3 フェッチ戦略

- **デフォルトはLAZY**: 関連フェッチはデフォルトで `FetchType.LAZY` とし、必要に応じて明示的に読み込み。
- **N+1問題の回避**: `@EntityGraph` または `JOIN FETCH` を使用してN+1問題を回避。
- **DTOプロジェクション**: クエリ結果をDTOに直接マッピングし、不要なデータの読み込みを防ぐ。

```java

@EntityGraph(attributePaths = {"orders"})
@Query("SELECT c FROM CustomerEntity c WHERE c.id = :id")
Optional<CustomerEntity> findByIdWithOrders(@Param("id") Long id);
```

#### 4.4 埋め込み可能型（@Embeddable）

- 値オブジェクトを `@Embeddable` として定義し、エンティティ内で再利用可能にする。
- 複合キーや構造化データをカラムレベルで表現。

```java

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Address {

  @Column(name = "prefecture", nullable = false, length = 20)
  private String prefecture;

  @Column(name = "city", nullable = false, length = 100)
  private String city;

  @Column(name = "street", nullable = false, length = 255)
  private String street;
}
```

#### 4.5 論理削除

- **@WhereClause**: 論理削除されたデータをデフォルトで除外。
- **deleted_atカラム**: nullなら有効、日時が設定されていれば削除済み。

```java

@Entity
@Table(schema = "undecided_as", name = "customers")
@SQLRestriction("deleted_at IS NULL")
@Getter
public class CustomerEntity {

  @Id
  private Long id;

  @Column(name = "deleted_at")
  private LocalDateTime deletedAt;
}
```

---

### 5. Flywayマイグレーション設計 (Flyway Migration Design)

#### 5.1 マイグレーションの基本ルール

- **モジュールごとにマイグレーション分離**: 各モジュールの
  `src/main/resources/db/migration/<module>/` にマイグレーションスクリプトを配置。
- **バージョン命名規則**: `V<バージョン>__<説明>.sql` の形式で命名。
- **マイグレーションの不可変性**: 一度適用したマイグレーションは修正せず、新しいマイグレーションを追加。

```
src/main/resources/db/migration/
├── customer/
│   ├── V1.0.0__create_customer_table.sql
│   └── V1.0.1__add_email_index.sql
└── order/
    ├── V1.0.0__create_order_table.sql
    └── V1.0.1__add_customer_id_fk.sql
```

#### 5.2 マイグレーションスクリプトの設計

- **アップマイグレーション**: テーブル作成、カラム追加、インデックス作成など。
- **ダウンマイグレーション**: 必要に応じて `@sql` でロールバックスクリプトを定義。
- **チェックサム**: Flywayが自動的にマイグレーションのチェックサムを管理。

```sql
-- V1.0.0__create_customer_table.sql
CREATE SCHEMA IF NOT EXISTS undecided_as;

CREATE TABLE undecided_as.customers
(
    id            BIGINT PRIMARY KEY,
    customer_code VARCHAR(20)  NOT NULL UNIQUE,
    name          VARCHAR(100) NOT NULL,
    email         VARCHAR(255) NOT NULL UNIQUE,
    status        VARCHAR(20)  NOT NULL DEFAULT 'ACTIVE',
    created_at    TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted_at    TIMESTAMP    NULL
);

CREATE INDEX idx_customers_email ON undecided_as.customers (email);
```

#### 5.3 環境別のマイグレーション管理

- **開発環境**: 自動マイグレーションを有効化。
- **本番環境**: 手動でマイグレーションを適用し、変更履歴を追跡。
- **テスト環境**: テストごとにクリーンなデータベースを構築。

---

### 6. データアクセスパターン (Data Access Patterns)

#### 6.1 リポジトリパターン

- **インターフェース**: Domain層にリポジトリインターフェースを定義。
- **実装**: Infrastructure層でSpring Data JPAを使用して実装。
- **メソッド命名規則**: `findBy<Property>`, `existsBy<Property>`, `deleteBy<Property>` の形式。

```java
public interface CustomerRepository extends JpaRepository<CustomerEntity, Long> {

  Optional<CustomerEntity> findByCustomerCode(String customerCode);

  boolean existsByEmail(String email);

  List<CustomerEntity> findByStatus(String status);
}
```

#### 6.2 クエリ設計ガイドライン

- **メソッド名クエリ**: 単純な条件検索はメソッド名から自動生成。
- **@Queryアノテーション**: 複雑なクエリはJPQLまたはネイティブクエリで定義。
- **動的クエリ**: 条件が動的に変化する場合は `Specification` または `Querydsl` を使用。

```java

@Query("SELECT c FROM CustomerEntity c WHERE c.status = :status AND c.name LIKE %:keyword%")
List<CustomerEntity> searchCustomers(@Param("status") String status,
    @Param("keyword") String keyword);
```

#### 6.3 トランザクション管理

- **@Transactional**: ユースケース層でトランザクション境界を定義。
- **読み取り専用**: 検索クエリには `readOnly = true` を指定し、パフォーマンスを最適化。
- **伝播動作**: デフォルトは `REQUIRED`、必要に応じて `REQUIRES_NEW` を使用。

```java

@Transactional(readOnly = true)
public CustomerDto getCustomer(Long id) {
  // 検索ロジック
}

@Transactional
public CustomerDto createCustomer(CreateCustomerCommand command) {
  // 作成ロジック
}
```

#### 6.4 ページネーション

- **Pageable**: 大量のデータはページネーションで取得。
- **ソート**: ソート条件を `Sort` オブジェクトで指定。
- **ページメタデータ**: 総件数、総ページ数などのメタデータをレスポンスに含める。

```java
Page<CustomerEntity> findByStatus(String status, Pageable pageable);
```

---

### 7. インデックス設計 (Index Design)

#### 7.1 インデックス適用ガイドライン

- **主キー**: 自動的にインデックスが作成される。
- **外部キー**: 結合条件として使用されるカラムにインデックスを適用。
- **検索条件**: WHERE句で頻繁に使用されるカラムにインデックスを適用。
- **ソート条件**: ORDER BY句で頻繁に使用されるカラムにインデックスを適用。

#### 7.2 インデックスの種類

| 種類                   | 用途                 | 例                                                |
|:-----------------------|:---------------------|:--------------------------------------------------|
| 単一カラムインデックス | 単一条件の検索       | `CREATE INDEX idx ON table (column)`              |
| 複合インデックス       | 複数条件の検索       | `CREATE INDEX idx ON table (col1, col2)`          |
| ユニークインデックス   | 一意性制約           | `CREATE UNIQUE INDEX idx ON table (column)`       |
| 部分インデックス       | 条件付きインデックス | `CREATE INDEX idx ON table (col) WHERE condition` |

#### 7.3 インデックス命名規則

- `idx_<テーブル名>_<カラム名>` の形式で命名。
- 複合インデックスの場合は `idx_<テーブル名>_<カラム1>_<カラム2>`。

```sql
CREATE INDEX idx_customers_status ON undecided_as.customers (status);
CREATE INDEX idx_customers_status_created_at ON undecided_as.customers (status, created_at);
```

---

### 8. データセキュリティ設計 (Data Security Design)

#### 8.1 機密データ暗号化

- **保存時暗号化**: 個人情報、パスワードなどの機密データはデータベース保存時に暗号化。
- **暗号化アルゴリズム**: AES-256などの標準的なアルゴリズムを使用。
- **鍵管理**: 暗号化鍵は環境変数または秘密管理サービスで管理。

```java

@Column(name = "encrypted_email")
private String encryptedEmail;

// 暗号化/復号はドメイン層またはインフラ層の専用サービスで行う
```

#### 8.2 SQLインジェクション対策

- **パラメータ化クエリ**: すべてのクエリはパラメータ化し、文字列連結でSQLを構築しない。
- **JPQL優先**: ネイティブクエリよりJPQLを優先し、データベース依存を減らす。
- **入力検証**: ユーザー入力は必ず検証し、期待される形式であることを確認。

#### 8.3 データアクセス制御

- **スキーマレベル**: モジュールごとにスキーマを分離し、アクセス権限を制限。
- **行レベル**: 必要に応じて行レベルのセキュリティ（RLS）を適用。
- **監査ログ**: 機密データへのアクセスをログに記録。

---

### 9. パフォーマンス最適化 (Performance Optimization)

#### 9.1 クエリ最適化

- **EXPLAIN ANALYZE**: 複雑なクエリのパフォーマンスを分析し、インデックスの効果を検証。
- **カバーインデックス**: 頻繁に実行されるクエリに対してカバーインデックスを検討。
- **クエリキャッシュ**: 読み取り中心のデータはクエリキャッシュを検討。

#### 9.2 バッチ処理最適化

- **一括保存**: `saveAll()` を使用してN+1問題を防ぐ。
- **チャンク処理**: 大量データはチャンク単位で処理し、メモリ使用量を制御。
- **@Modifying**: 一括更新・削除は `@Modifying(clearAutomatically = true)` を使用。

```java

@Modifying(clearAutomatically = true)
@Query("UPDATE CustomerEntity c SET c.status = :status WHERE c.id IN :ids")
int bulkUpdateStatus(@Param("status") String status, @Param("ids") List<Long> ids);
```

#### 9.3 キャッシュ戦略

- **@Cacheable**: 頻繁にアクセスされる読み取りデータにキャッシュを適用。
- **キャッシュ無効化**: データ更新時にキャッシュを適切に無効化。
- **キャッシュ戦略**: 読み込み遅延（read-through）または書き込み通過（write-through）を選択。

```java

@Cacheable(value = "customers", key = "#id")
public CustomerDto getCustomer(Long id) {
  // 検索ロジック
}

@CacheEvict(value = "customers", key = "#id")
public void updateCustomer(Long id, UpdateCustomerCommand command) {
  // 更新ロジック
}
```

---

### 10. データ整合性設計 (Data Integrity Design)

#### 10.1 制約レベルの整合性

- **データベース制約**: 主キー、外部キー、ユニーク制約、チェック制約でデータの整合性を保証。
- **アプリケーション制約**: ビジネスルールはアプリケーションレベルで検証し、データベース制約と補完。

#### 10.2 トランザクション整合性

- **ACID特性**: トランザクションの原子性、一貫性、隔離性、永続性を保証。
- **隔離レベル**: デフォルトは `READ_COMMITTED`、必要に応じて `REPEATABLE_READ` を使用。
- **楽観的ロック**: `@Version` アノテーションで楽観的ロックを適用し、競合を検出。

```java

@Version
private Long version;
```

#### 10.3 最終的整合性

- **ドメインイベント**: モジュール間のデータ整合性はドメインイベントで最終的整合性を実現。
- **Sagaパターン**: 分散トランザクションにはSagaパターンを適用。
- **補正処理**: 整合性違反を検出・補正するバッチ処理を設計。

---

### 11. データマイグレーション戦略 (Data Migration Strategy)

#### 11.1 スキーマ変更パターン

- **後方互換性のある変更**: カラム追加、インデックス追加などは後方互換性がある。
- **非互換な変更**: カラム削除、型変更などは慎重に計画し、段階的に適用。
- **ブルーグリーンデプロイ**: 重大なスキーマ変更にはブルーグリーンデプロイを検討。

#### 11.2 データ変換マイグレーション

- **マイグレーションスクリプト**: 既存データの変換をマイグレーションスクリプトで実行。
- **テストデータ**: マイグレーション前にテストデータで検証。
- **ロールバック計画**: データ変換のロールバック手順を事前に準備。

---

### 12. テスト戦略 (Testing Strategy)

#### 12.1 データベーステスト

- **テストコンテナ**: 実際のデータベースを使用して統合テストを実行。
- **Flywayマイグレーション**: テスト環境でもFlywayでスキーマを構築。
- **テストデータ**: テスト用のフィクスチャデータを `src/test/resources/data/` に配置。

#### 12.2 リポジトリテスト

- **@DataJpaTest**: リポジトリ層のテストは `@DataJpaTest` を使用。
- **テストデータ準備**: `@Sql` アノテーションでテストデータを準備。
- **クエリ検証**: クエリ結果の正確性とパフォーマンスを検証。

#### 12.3 トランザクションテスト

- **@Transactional**: テストメソッドに `@Transactional` を付与し、テスト後にロールバック。
- **隔離テスト**: 各テストが独立して実行されることを保証。

---

### 13. 設計チェックリスト (Design Checklist)

データベース設計時・実装時に確認するチェックリストは [
`checklists/design-checklist.md`](checklists/design-checklist.md) を参照してください。
