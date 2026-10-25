# Spring Data JPA 実装ガイドライン

本ガイドラインは、modulith-learning プロジェクトにおける Spring Data JPA の実装規則を定義します。


---

### 第1部：JPA Entity 基本

## 1. Entity 階層構造 (@MappedSuperclass)

プロジェクトでは、エンティティの共通フィールドを `@MappedSuperclass` を使用して基底クラスに定義します。

### 1.1 BusinessEntity（ビジネスエンティティ基底クラス）

作成日時と作成者を管理する基底クラスです。`@CreatedDate` アノテーションを使用して、エンティティ作成時に自動的にタイムスタンプを設定します。

```java

@MappedSuperclass
@Getter
@Setter
@Immutable
public abstract class BusinessEntity<E extends BusinessEntity<E>> {

  @CreatedDate
  private LocalDateTime createdAt;
  @Column
  private Long createdBy;
}
```

**特徴:**

- 自己参照型ジェネリクス (`E extends BusinessEntity<E>`) を使用して、サブクラスでの型安全性を確保
- `@Immutable` で不変性を保証
- `@CreatedDate` で作成日時の自動設定

### 1.2 AuditResource（監査リソース基底クラス）

作成・更新の監査情報を管理する基底クラスです。`@PrePersist` / `@PreUpdate`
ライフサイクルコールバックを使用して、手動で監査フィールドを設定します。

```java

@MappedSuperclass
@Getter
public abstract class AuditResource {

  protected String createdBy;
  protected LocalDateTime createdAt;
  protected String updatedBy;
  protected LocalDateTime updatedAt;

  @PrePersist
  public void prePersist() {
    createdAt = DateProvider.currentLocalDateTime();
    createdBy = "system";
  }

  @PreUpdate
  public void preUpdate() {
    updatedAt = DateProvider.currentLocalDateTime();
    updatedBy = "system";
  }
}
```

**特徴:**

- `@PrePersist` / `@PreUpdate` で作成・更新の両方を追跡
- `DateProvider` を使用して、テスト時に日時をモック可能に
- 作成者・更新者は固定値 "system" を使用（将来的に認証情報と連携）

### 1.3 選択基準

| 基準           | BusinessEntity        | AuditResource                    |
|----------------|-----------------------|----------------------------------|
| 監査フィールド | 作成日時・作成者のみ  | 作成・更新の両方                 |
| 設定方法       | `@CreatedDate` (自動) | `@PrePersist/@PreUpdate` (手動)  |
| 不変性         | `@Immutable` 付与     | 更新可能                         |
| 使用シーン     | 不変なマスターデータ  | 更新可能なトランザクションデータ |

### 1.4 分類用抽象クラスパターン

型分類のみを行う空の抽象クラスを使用して、エンティティ階層を整理します。

```java

@MappedSuperclass
public abstract class PptEntity<E extends PptEntity<E>> extends BusinessEntity<E> {

}

@MappedSuperclass
public abstract class ThingEntity<E extends ThingEntity<E>> extends PptEntity<E> {

}

@MappedSuperclass
public abstract class DescriptionEntity<E extends DescriptionEntity<E>> extends ThingEntity<E> {

}
```

**特徴:**

- 具体的なフィールドを持たず、型分類のみを行う
- 継承階層を通じて、共通の基底クラスとの関係を明確化

---

---

## 2. 関連付け (@OneToMany, @ManyToOne)

エンティティ間の関連付けは、JPA の標準アノテーションを使用します。

### 2.1 @OneToMany（一対多）

```java

@Entity
@Table(name = "role")
public class Role {

  @Id
  private SnowflakeId roleId;

  @OneToMany(mappedBy = "role")
  private List<RoleAssignmentForOrg> roleAssignmentsForOrg = new ArrayList<>();

  @OneToMany(mappedBy = "role")
  private List<RoleAssignmentForEmp> roleAssignmentsForEmp = new ArrayList<>();
}
```

**特徴:**

- `mappedBy` で所有側を指定し、逆方向の関連付けを定義
- デフォルトで `ArrayList` を初期化して、null 参照を防ぐ

### 2.2 @ManyToOne（多対一）

```java

@Entity
@Table(name = "role_assignment_for_org")
public class RoleAssignmentForOrg {

  @Id
  private SnowflakeId id;

  @ManyToOne
  @JoinColumn(name = "role_id", nullable = false)
  private Role role;

  @ManyToOne
  @JoinColumn(name = "org_id", nullable = false)
  private Organization organization;
}
```

**特徴:**

- `@JoinColumn` で外部キーカラムを明示的に指定
- `nullable = false` で必須関係を制約

---

---

## 3. 継承戦略 (@Inheritance)

プロジェクトでは、`InheritanceType.TABLE_PER_CLASS` を使用して、各サブクラスを個別のテーブルとしてマッピングします。

### 3.1 抽象基底クラス

```java

@Entity
@Table(name = "party", schema = SchemaRelationshipMgmt.NAME)
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Party {

  @Id
  private SnowflakeId partyId;
  private GovtAssignedId govtAssignedId;
  private SearchName searchName;
}
```

### 3.2 サブクラス

```java

@Entity
@Table(name = "People", schema = SchemaRelationshipMgmt.NAME)
public class Person extends Party {

  private LegalName legalName;
  private DateOfBirth dateOfBirth;
}
```

**特徴:**

- 各サブクラスが独立したテーブルを持ち、基底クラスのフィールドも含まれる
- テーブル間の JOIN が不要で、読み込みパフォーマンスが向上
- 書き込み時は重複カラムへの INSERT が必要になる点に注意

---

---

## 4. @Embeddable / @Embedded（埋め込み可能クラス）

値オブジェクトを JPA エンティティに埋め込んでマッピングするパターンです。

### 4.1 Embeddable クラス（値オブジェクト）

```java

@Embeddable
public record DepartmentCode(String value) {

  public static final DepartmentCode EMPTY = new DepartmentCode("");
}
```

```java

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EmployeeNo {
  // ...
}
```

**特徴:**

- `record` 型と `class` 型の両方が使用可能
- 個別のテーブルを持たず、エンティティのテーブルにカラムとして展開される

### 4.2 Embedded の使用例

```java

@Entity
@Table(name = "employees", schema = "relationship_mgmt")
public class SimpleEmployee {

  @Id
  private SnowflakeId employeeId;

  @Embedded
  @AttributeOverrides(
      @AttributeOverride(name = "value", column = @Column(name = "employee_no")))
  private EmployeeNo employeeNo;

  @Embedded
  @AttributeOverrides(
      @AttributeOverride(name = "value", column = @Column(name = "search_name")))
  private SearchName searchName;
}
```

**特徴:**

- `@AttributeOverrides` でカラム名をカスタマイズできる
- 値オブジェクトの内部構造に依存せずに、カラム名を制御できる

---

---

## 5. @Converter / AttributeConverter（カスタム変換）

値オブジェクトとデータベースのデータ型間の双方向変換を定義するパターンです。

### 5.1 Converter の定義

```java

@Converter(autoApply = true)
public static class GovtAssignedIdConverter implements AttributeConverter<GovtAssignedId, String> {

  @Override
  public String convertToDatabaseColumn(GovtAssignedId attribute) {
    // 値オブジェクト → String 変換
  }

  @Override
  public GovtAssignedId convertToEntityAttribute(String dbData) {
    // String → 値オブジェクト 変換
  }
}
```

**特徴:**

- Converter は値オブジェクトクラス内に `static nested class` として定義
- `autoApply = true` でグローバルに自動適用
- 変換先は主に `String` 型

### 5.2 共通基盤クラス

| クラス                 | 説明                                                 |
|------------------------|------------------------------------------------------|
| `EnumConverter.java`   | `PersistableEnum` の変換基盤を提供する抽象クラス     |
| `PersistableEnum.java` | データベース値を取得するためのインターフェース       |
| `ValueObject.java`     | 値オブジェクトの共通インターフェースとユーティリティ |

### 5.3 主な Converter 一覧

| 値オブジェクト      | Converter                  |
|---------------------|----------------------------|
| `GovtAssignedId`    | GovtAssignedIdConverter    |
| `SearchName`        | SearchNameConverter        |
| `AccountNumber`     | AccountNumberConverter     |
| `HoldersName`       | HoldersNameConverter       |
| `OrgName`           | OrgNameConverter           |
| `OrgUnitName`       | OrgUnitNameConverter       |
| `WhenAassociated`   | WhenAassociatedConverter   |
| `WhenDisassociated` | WhenDisassociatedConverter |
| `DateOfBirth`       | DateOfBirthConverter       |
| `FirstName`         | FirstNameConverter         |
| `LastName`          | LastNameConverter          |
| `LegalName`         | LegalNameConverter         |
| `ValidInterval`     | ValidIntervalConverter     |
| `WhenOccured`       | WhenOccuredConverter       |
| `SnowflakeId`       | SnowflakeIdConverter       |

---

---

## 6. ValueObject 共通インターフェース

値オブジェクトの共通動作を定義するインターフェースです。

```java
public interface ValueObject {

  boolean isEmpty();

  class ValueObjects {

    public static <V extends ValueObject> boolean isAllValuesNotEmpty(V... valueObjects);

    public static <V extends ValueObject> boolean isEmpty(V valueObject);

    public static <E extends RuntimeException, V extends ValueObject> V checkNotEmpty(
        @NonNull V ref, @NonNull Supplier<E> exceptionSupplier);

    public static <V extends ValueObject> V defaultIfNull(V ref, V defaultValue);

    public static <V extends ValueObject> V defaultIfEmpty(V ref, V defaultValue);
  }
}
```

**特徴:**

- `isEmpty()` で値オブジェクトが空かどうかを判定
- 静的ユーティリティメソッドで、null 安全性や空チェックを提供
- `checkNotEmpty()` で空の場合に例外をスロー

---

---

## 7. IdValue / 分散 ID 生成

プロジェクトでは、`@GeneratedValue` を使用せず、アプリケーション側で ID を生成します。
分散環境で一意な ID を生成するために、Snowflake ID や UUID v7 などの分散 ID 生成方式を採用しています。

### 7.1 ID 値オブジェクトのインターフェース階層

プロジェクトでは、ID を値オブジェクトとして定義し、型安全性を確保します。

```
IdValue<A>                  (基底インターフェース)
  │
  ├── SingleValue<T>        (単一値の値オブジェクト)
  │     │
  │     ├── LongValue<VO>   (Long 型をラップ)
  │     │     └── SnowflakeId
  │     │
  │     ├── UuidValue<VO>   (UUID 型をラップ)
  │     │     └── UuidV7IdValue
  │     │
  │     └── IntegerValue<VO> (Integer 型をラップ)
  │
  └── ValueObject           (値オブジェクト共通インターフェース)
```

| インターフェース    | 説明                                                       |
|---------------------|------------------------------------------------------------|
| `IdValue.java`      | ID 値オブジェクトの基底インターフェース                    |
| `ValueObject.java`  | 値オブジェクトの共通インターフェース (`isEmpty()` など)    |
| `SingleValue.java`  | 単一値を保持する値オブジェクト (`value()` メソッド)        |
| `LongValue.java`    | Long 型をラップする値オブジェクト (検証ユーティリティ付き) |
| `UuidValue.java`    | UUID 型をラップする値オブジェクト                          |
| `IntegerValue.java` | Integer 型をラップする値オブジェクト                       |

### 7.2 Snowflake ID

Snowflake アルゴリズムを使用して、分散環境で一意な Long 型 ID を生成します。

#### 7.2.1 SnowflakeId 値オブジェクト

```java

@AllArgsConstructor
@NoArgsConstructor(force = true)
@Convert(converter = SnowflakeId.SnowflakeIdConverter.class)
public class SnowflakeId implements LongValue<SnowflakeId>, Comparable<SnowflakeId>, Serializable {

  public static final SnowflakeId EMPTY = new SnowflakeId(null);

  @JsonCreator
  public static SnowflakeId of(@NonNull Long value) {
    checkNotNull(value, () -> new IllegalArgumentException("value must not be null"));
    checkPositive(value, () -> new IllegalArgumentException("value must be positive"));
    return new SnowflakeId(value);
  }

  public static SnowflakeId reconstruct(Long value) {
    if (isNull(value)) {
      return EMPTY;
    }
    return new SnowflakeId(value);
  }

  @Override
  public Long value() {
    return value;
  }

  @Override
  public boolean isEmpty() {
    return isNull(value);
  }

  // Base62 エンコード/デコード機能
  public String toBase62String() {
    LongValues.checkNotEmpty(this, () -> new IllegalStateException("value is empty"));
    return ENCODE_TO_BASE62.apply(value);
  }

  public SnowflakeId fromBase62String(String base62String) {
    return new SnowflakeId(DECODE_FROM_BASE62.apply(base62String));
  }

  // JPA Converter (autoApply = true でグローバルに自動適用)
  @Converter(autoApply = true)
  public static class SnowflakeIdConverter implements AttributeConverter<SnowflakeId, Long> {

    @Override
    public Long convertToDatabaseColumn(SnowflakeId attribute) {
      return attribute != null ? attribute.value() : null;
    }

    @Override
    public SnowflakeId convertToEntityAttribute(@Nullable Long dbData) {
      return SnowflakeId.reconstruct(dbData);
    }
  }
}
```

**特徴:**

- `@Converter(autoApply = true)` でグローバルに自動適用
- `EMPTY` 定数で空の状態を明示的に表現
- `of()` で正の値を検証してインスタンス生成
- `reconstruct()` で null 許容の再構築 (DB から読み込み時)
- Base62 エンコード/デコード機能付き
- `Comparable` を実装して順序比較可能

#### 7.2.2 Snowflake ID 生成器

```java
public interface SnowflakeIdGenerator {

  long nextId();

  SnowflakeId nextIdValue();
}

public final class SnowflakeIdGeneratorImpl implements SnowflakeIdGenerator {

  private static final long MAX_SEQUENCE = 4095;
  private static final int NODE_ID_SHIFT = 12;
  private static final int TIMESTAMP_SHIFT = 12 + 10;

  private final long epochMillis;
  private final long nodeId;
  private long lastTimestamp = -1;
  private long sequence = 0;

  public synchronized long nextId() {
    long timestamp = DateProvider.currentTimeMillis();
    if (timestamp < lastTimestamp) {
      throw new IllegalStateException("clock moved backwards");
    }
    if (timestamp == lastTimestamp) {
      sequence++;
      if (sequence > MAX_SEQUENCE) {
        timestamp = withNextMillis(timestamp);
        sequence = 0;
      }
    } else {
      sequence = 0;
    }
    lastTimestamp = timestamp;
    return ((timestamp - epochMillis) << TIMESTAMP_SHIFT) | (nodeId << NODE_ID_SHIFT) | sequence;
  }

  @Override
  public synchronized SnowflakeId nextIdValue() {
    return SnowflakeId.of(nextId());
  }
}
```

**Snowflake ID のビット構成:**

```
63          22 21    12 11     0
┌───────────┬─────────┬────────┐
│ タイムスタンプ (42bit) │ ノードID (10bit) │ シーケンス (12bit) │
└───────────┴─────────┴────────┘
```

- **タイムスタンプ (42bit)**: エポックからのミリ秒 (約 136 年間対応)
- **ノード ID (10bit)**: ノード識別子 (最大 1024 ノード)
- **シーケンス (12bit)**: ミリ秒内のシーケンス番号 (最大 4096)

#### 7.2.3 Spring 設定

```java

@Configuration
public class SnowflakeConfiguration {

  // エポック時間: 2025年1月1日 00:00:00 UTC
  private static final long EPOCH_MILLIS = 1735689600000L;

  @Bean
  public SnowflakeIdGenerator snowflakeIdGenerator(SnowflakeNodeLeaseManager leaseManager) {
    return new SnowflakeIdGeneratorImpl(leaseManager.nodeId(), EPOCH_MILLIS);
  }
}
```

**特徴:**

- `EPOCH_MILLIS` を 2025 年 1 月 1 日に設定して、ID の値を小さく抑える
- `SnowflakeNodeLeaseManager` でノード ID を管理 (データベースからのリース)
- `DateProvider` を使用して、テスト時に時刻をモック可能に

#### 7.2.4 エンティティでの使用例

```java

@Entity
@Table(name = "parties", schema = "undecided_as")
public class Party {

  @Id
  private SnowflakeId partyId;
  // ...
}

@Entity
@Table(name = "employees", schema = "relationship_mgmt")
public class SimpleEmployee {

  @Id
  private SnowflakeId employeeId;
  // ...
}
```

**特徴:**

- `@Id` に `SnowflakeId` 値オブジェクトを直接使用
- `@GeneratedValue` を使用しない
- ID はアプリケーション層で生成して設定

#### 7.2.5 ID 生成の使用例

```java

@Service
@RequiredArgsConstructor
public class UseBankAccountFactoryImpl {

  private final SnowflakeIdGenerator snowflakeIdGenerator;

  public BankAccountUse create(@NonNull BankAccountUseDto bankAccountUseDto) {
    return BankAccountUse.create(
        snowflakeIdGenerator.nextIdValue(),  // SnowflakeId を生成
        bankAccountUseDto.getBankAccountId(),
        bankAccountUseDto.getPartyId(),
        // ...
    );
  }
}
```

**テストでのモック例:**

```java

@Test
void shouldCreateBankAccountUse() {
  // Arrange
  SnowflakeId generatedId = SnowflakeId.of(1234567890L);
  when(snowflakeIdGenerator.nextIdValue()).thenReturn(generatedId);

  // Act
  BankAccountUse result = factory.create(dto);

  // Assert
  assertThat(result.getBankAccountUseId()).isEqualTo(generatedId);
}
```

### 7.3 UUID v7

UUID バージョン 7 は、タイムスタンプベースの UUID で、順序付きの ID 生成に適しています。

#### 7.3.1 UuidV7IdValue 値オブジェクト

```java
public class UuidV7IdValue implements UuidValue<UuidV7IdValue>, Serializable {

  private final UUID value;

  public UuidV7IdValue() {
    this(null);
  }

  UuidV7IdValue(UUID value) {
    this.value = value;
  }

  public static UuidValue<UuidV7IdValue> newInstance() {
    return new UuidV7IdValue(UuidV7Provider.newInstance());
  }

  UuidValue<UuidV7IdValue> empty() {
    return new UuidV7IdValue(null);
  }

  @Override
  public UUID value() {
    return value;
  }

  @Override
  public boolean isEmpty() {
    return isNull(value);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    UuidV7IdValue that = (UuidV7IdValue) o;
    return java.util.Objects.equals(value, that.value);
  }

  @Override
  public int hashCode() {
    return java.util.Objects.hash(value);
  }
}
```

**特徴:**

- `newInstance()` で UUID v7 を生成
- `empty()` で空のインスタンスを生成
- `UuidV7Provider` を使用して、テスト時に UUID をモック可能に

#### 7.3.2 エンティティでの使用例

```java

@Entity
@Table(name = "organizations", schema = "undecided_as")
public class Organization {

  @Id
  private UUID organizationId;
  // ...
}
```

**特徴:**

- `@Id` に `UUID` を直接使用
- `UuidV7IdValue.newInstance().value()` で UUID v7 を生成して設定

### 7.4 複合主キー (@IdClass)

複数のカラムを組み合わせた複合主キーを定義するパターンです。

#### 7.4.1 複合主キーの定義

```java

@Entity
@IdClass(Branch.BranchId.class)
@Table(schema = "undecided_ef", name = "branches", comment = "bank branch table")
public class Branch {

  @Id
  @Column(name = "branch_code", comment = "Branch code (3 digits)", nullable = false, length = 3)
  private BranchCode branchCode;

  @Id
  @Column(name = "bank_code", comment = "Bank code (4 digits)", nullable = false, length = 4)
  private String bankCode;

  @Column(name = "branch_name", comment = "Branch name", nullable = false, length = 128)
  private String branchName;

  // 複合主キークラス
  @Getter
  @Setter
  @EqualsAndHashCode
  public static class BranchId implements Serializable {

    @Column(name = "bank_code", nullable = false, length = 4)
    private String bankCode;

    @Column(name = "branch_code", nullable = false, length = 3)
    private String branchCode;
  }
}
```

**特徴:**

- `@IdClass` で複合主キークラスを指定
- 複合主キーを構成する各フィールドに `@Id` を付与
- 複合主キークラスは `Serializable` を実装
- 複合主キークラス内のカラム名はエンティティと一致させる
- `@EqualsAndHashCode` で等価性比較を正しく実装

#### 7.4.2 @Embeddable 値オブジェクトの実装例

複合主キーの一部として `@Embeddable` 値オブジェクトを使用するパターンです。

```java

@Embeddable
public record BranchCode(String value) {

  public static final int LENGTH = 3;

  public static BranchCode of(String code) {
    if (code == null) {
      throw new IllegalArgumentException("branchCode must not be null");
    }
    String trimmed = code.trim();
    if (trimmed.isEmpty()) {
      throw new IllegalArgumentException("branchCode must not be empty");
    }
    if (trimmed.length() != LENGTH) {
      throw new IllegalArgumentException(
          "branchCode must be exactly " + LENGTH + " digits, but was: " + trimmed);
    }
    return new BranchCode(trimmed);
  }

  public String asString() {
    return value;
  }
}
```

**特徴:**

- `@Embeddable` で値オブジェクトとしてデータベースに埋め込み可能
- `record` 型を使用して不変性を保証
- 静的ファクトリメソッド `of()` で検証ロジックを一元化
- `LENGTH` 定数で桁数を明示

#### 7.4.3 Flyway DDL での複合主キー定義

```sql
-- 複合主キーの定義
CREATE TABLE undecided_ef.branches
(
    branch_code      varchar(3)   not null,
    bank_code        varchar(4)   not null,
    branch_name      varchar(128) not null,
    branch_half_kana varchar(256),
    branch_full_kana varchar(256),
    branch_hiragana  varchar(256),
    dataset_id       varchar(128) not null,
    primary key (bank_code, branch_code)
);

-- インデックス定義
CREATE INDEX idx_branches_dataset_id ON undecided_ef.branches (dataset_id);

-- コメント
COMMENT ON TABLE undecided_ef.branches IS '銀行支店マスター';
COMMENT ON COLUMN undecided_ef.branches.branch_code IS '支店コード（3桁）';
COMMENT ON COLUMN undecided_ef.branches.bank_code IS '金融機関コード（4桁）';
COMMENT ON COLUMN undecided_ef.branches.branch_name IS '支店名';
```

**特徴:**

- `PRIMARY KEY (bank_code, branch_code)` で複合主キーを定義
- 主キーの順序は、検索頻度の高いカラムを先に配置
- 複合主キーには暗黙的に一意インデックスが作成される

#### 7.4.4 リポジトリでの使用例

```java

@Repository
public interface BranchRepository extends CrudRepository<Branch, Branch.BranchId> {

  /**
   * 金融機関コードで支店リストを取得（支店コード順）
   */
  List<Branch> findByBankCodeOrderByBranchCodeAsc(@Param("bankCode") String bankCode);

  /**
   * データセットID以外のデータを削除
   */
  @Modifying
  @Query("DELETE FROM Branch b WHERE b.datasetId <> :datasetId")
  int deleteByDatasetIdNot(@Param("datasetId") String datasetId);
}
```

**複合キーでの findById 使用例:**

```java
// 複合キーの作成
Branch.BranchId branchId = new Branch.BranchId();
branchId.

setBankCode(bankCode.asString());
    branchId.

setBranchCode(branchCode.asString());

// 複合キーでの.findById
Branch existing = branchRepository.findById(branchId).orElse(null);

// または Optional で処理
Optional<Branch> branchOpt = branchRepository.findById(branchId);
branchOpt.

ifPresent(branch ->{
    // 存在する場合の処理
    });
```

**特徴:**

- ジェネリクスに複合主キークラス `Branch.BranchId` を指定
- `findById()` で複合キー全体で検索可能
- 複合キーオブジェクトを作成する際、すべての主キーフィールドを設定する必要がある

#### 7.4.5 複合主キーの注意点

| 項目                | 説明                                                                                                                   |
|---------------------|------------------------------------------------------------------------------------------------------------------------|
| **Serializable**    | 複合主キークラスは必ず `Serializable` を実装                                                                           |
| **equals/hashCode** | 正しく実装しないと、JPA がエンティティの等価性を誤判断                                                                 |
| **カラム名の一致**  | 複合主キークラス内の `@Column(name = "...")` はエンティティと一致させる                                                |
| **主キーの順序**    | DDL の `PRIMARY KEY (col1, col2)` の順序と、複合主キークラスのフィールド順序は一致させる必要はないが、一貫性を持たせる |
| **外部キー参照**    | 他のエンティティから複合主キーを参照する場合、`@JoinColumn` で各カラムを明示的にマッピング                             |
| **テスト容易性**    | 複合キーの作成が煩雑になるため、テストではファクトリメソッドを用意する                                                 |

#### 7.4.6 複合主キー vs @EmbeddedId

| 比較項目                 | @IdClass                           | @EmbeddedId                      |
|--------------------------|------------------------------------|----------------------------------|
| **主キークラス**         | 独立したクラス                     | @Embeddable なクラス             |
| **フィールド定義**       | エンティティと重複定義             | 1箇所で定義                      |
| **値オブジェクト統合**   | 別途 @Embeddable を使用            | 主キークラス自体が値オブジェクト |
| **使用シーン**           | 主キーの一部が値オブジェクトの場合 | 主キー全体が値オブジェクトの場合 |
| **プロジェクトでの使用** | Branch エンティティ                | —                                |

**プロジェクトでは `@IdClass` を使用**し、複合主キーの一部として `@Embeddable`
値オブジェクト（BranchCode）を組み合わせています。

### 7.5 ID 値オブジェクトの EMPTY パターン

ID 値オブジェクトに `EMPTY` 定数を定義し、未設定の状態を明示的に表現するパターンです。

```java

@Entity
@Table(name = "address_use", schema = "undecided_as")
public class AddressUse {

  @Id
  private SnowflakeId addressUseId = SnowflakeId.EMPTY;
  private SnowflakeId addressId = SnowflakeId.EMPTY;
  private SnowflakeId partyId = SnowflakeId.EMPTY;
  // ...
}
```

**特徴:**

- フィールド宣言時に `EMPTY` をデフォルト値として設定
- 未初期化の状態を `null` ではなく `EMPTY` で表現
- `isEmpty()` で未設定かどうかを判定可能

### 7.6 業務固有の ID 生成パターン

支社コードや年月などの業務情報を組み合わせて、人間が読みやすい ID を生成するパターンです。

#### 7.6.1 基本パターン：支社コード + 年月 + 通番

```java
public class OrderNumber implements ValueObject {

  private static final Pattern PATTERN = Pattern.compile("^\\d{3}\\d{6}\\d{5}$");

  private final String value;

  private OrderNumber(String value) {
    this.value = Objects.requireNonNull(value, "value must not be null");
    if (!PATTERN.matcher(value).matches()) {
      throw new IllegalArgumentException("Invalid order number format: " + value);
    }
  }

  public static OrderNumber generate(BranchCode branchCode, LocalDate orderDate, int sequence) {
    String branchPart = branchCode.value();                    // 3桁: 支社コード
    String datePart = orderDate.format(DateTimeFormatter.ofPattern("yyMM")); // 6桁: 年月
    String seqPart = String.format("%05d", sequence);          // 5桁: 通番
    return new OrderNumber(branchPart + datePart + seqPart);  // 14桁: 支社コード+年月+通番
  }

  public BranchCode getBranchCode() {
    return BranchCode.of(value.substring(0, 3));
  }

  public LocalDate getYearMonth() {
    return YearMonth.parse(value.substring(3, 9), DateTimeFormatter.ofPattern("yyMM"))
        .atDay(1);
  }

  public int getSequence() {
    return Integer.parseInt(value.substring(9, 14));
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    OrderNumber that = (OrderNumber) o;
    return value.equals(that.value);
  }

  @Override
  public int hashCode() {
    return value.hashCode();
  }
}
```

**ID 構成:**

```
14桁
┌─────┬────────┬──────────┐
│支社コード(3桁)│ 年月(6桁) │ 通番(5桁) │
└─────┴────────┴──────────┘
例: 00125102300001
    │  │    │
    │  │    └─ 通番: 1番目
    │  └────── 年月: 2025年10月
    └───────── 支社コード: 001
```

**特徴:**

- 正規表現でフォーマットを検証
- `generate()` で各要素を組み合わせて ID を生成
- `getBranchCode()`, `getYearMonth()`, `getSequence()` で各要素を抽出
- 値オブジェクトとして `equals()`, `hashCode()` を実装

#### 7.6.2 ID 生成サービス

```java

@Service
@RequiredArgsConstructor
public class OrderNumberGenerator {

  private final OrderNumberRepository orderNumberRepository;

  /**
   * 支社コードと年月に基づいて一意の注文番号を生成
   */
  @Transactional
  public OrderNumber nextOrderNumber(BranchCode branchCode, LocalDate orderDate) {
    // 1. 当該年月の最大通番を取得
    int maxSequence = orderNumberRepository
        .findMaxSequenceByBranchCodeAndYearMonth(
            branchCode,
            YearMonth.from(orderDate))
        .orElse(0);

    // 2. 最大通番 + 1 を使用して新しい注文番号を生成
    int nextSequence = maxSequence + 1;
    validateSequenceLimit(nextSequence);

    return OrderNumber.generate(branchCode, orderDate, nextSequence);
  }

  private void validateSequenceLimit(int sequence) {
    if (sequence > 99999) {
      throw new IllegalStateException(
          "Sequence limit exceeded for branch and year-month combination");
    }
  }
}
```

**特徴:**

- `@Transactional` でトランザクション管理
- 当該年月の最大通番を取得して +1 して新しい通番を生成
- 通番上限（5桁 = 99999）を検証

#### 7.6.3 リポジトリでの最大通番取得

```java
public interface OrderNumberRepository extends JpaRepository<Order, OrderNumber> {

  /**
   * 支社コードと年月の最大通番を取得
   */
  @Query("SELECT MAX(SUBSTRING(o.orderNumber.value, 9, 5)) " +
      "FROM Order o " +
      "WHERE SUBSTRING(o.orderNumber.value, 1, 3) = :branchCode " +
      "AND SUBSTRING(o.orderNumber.value, 4, 6) = :yearMonth")
  Optional<String> findMaxSequenceByBranchCodeAndYearMonth(
      @Param("branchCode") String branchCode,
      @Param("yearMonth") String yearMonth);

  /**
   * ネイティブクエリでの最大通番取得（文字列比較ではなく数値比較）
   */
  @Query(value = "SELECT MAX(CAST(SUBSTRING(order_number, 9, 5) AS INTEGER)) " +
      "FROM undecided_as.orders " +
      "WHERE SUBSTRING(order_number, 1, 3) = :branchCode " +
      "AND SUBSTRING(order_number, 4, 6) = :yearMonth",
      nativeQuery = true)
  Optional<Integer> findMaxSequenceByBranchCodeAndYearMonthNative(
      @Param("branchCode") String branchCode,
      @Param("yearMonth") String yearMonth);
}
```

**特徴:**

- JPQL では文字列操作が限定的なため、ネイティブクエリを使用するケースが多い
- 数値比較を使用して正しい最大値を取得
- 当該年月のデータのみを対象にクエリを実行

#### 7.6.4 エンティティでの使用例

```java

@Entity
@Table(name = "orders", schema = "undecided_as", comment = "注文テーブル")
public class Order {

  @Id
  @Column(name = "order_number", nullable = false, length = 14,
      comment = "注文番号 (支社コード3桁 + 年月6桁 + 通番5桁)")
  private OrderNumber orderNumber;

  @Column(name = "branch_code", nullable = false, length = 3,
      comment = "支社コード")
  private BranchCode branchCode;

  @Column(name = "order_date", nullable = false,
      comment = "注文日")
  private LocalDate orderDate;

  @Column(name = "order_amount", nullable = false,
      comment = "注文金額")
  private BigDecimal orderAmount;

  // ...
}
```

**特徴:**

- `@Id` に `OrderNumber` 値オブジェクトを直接使用
- `@Column.comment` で ID の構成を明確に記録
- `length = 14` で文字列長を制限

#### 7.6.5 Flyway でのテーブル定義

```sql
CREATE TABLE undecided_as.orders
(
    order_number VARCHAR(14)    NOT NULL, -- 注文番号 (支社コード3桁 + 年月6桁 + 通番5桁)
    branch_code  VARCHAR(3)     NOT NULL, -- 支社コード
    order_date   DATE           NOT NULL, -- 注文日
    order_amount NUMERIC(15, 2) NOT NULL, -- 注文金額
    created_at   TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by   VARCHAR(64)    NOT NULL,
    PRIMARY KEY (order_number)
);

COMMENT ON TABLE undecided_as.orders IS '注文テーブル';
COMMENT ON COLUMN undecided_as.orders.order_number IS '注文番号 (支社コード3桁 + 年月6桁 + 通番5桁)';
COMMENT ON COLUMN undecided_as.orders.branch_code IS '支社コード';
COMMENT ON COLUMN undecided_as.orders.order_date IS '注文日';
COMMENT ON COLUMN undecided_as.orders.order_amount IS '注文金額';

-- 支社コードと年月での検索用インデックス
CREATE INDEX idx_orders_branch_year_month
    ON undecided_as.orders (branch_code, order_date);
```

**特徴:**

- `order_number` を主キーとして定義
- 支社コードと年月での検索用インデックスを定義
- コメントで ID の構成を明確に記録

#### 7.6.6 並行アクセス時の競合処理

```java

@Service
@RequiredArgsConstructor
public class OrderNumberGeneratorWithLock {

  private final OrderSequenceRepository orderSequenceRepository;
  private final EntityManager entityManager;

  /**
   * ペシミスティックロックを使用して一意の通番を取得
   */
  @Transactional
  public OrderNumber nextOrderNumber(BranchCode branchCode, LocalDate orderDate) {
    YearMonth yearMonth = YearMonth.from(orderDate);

    // 1. 当該年月のシーケンスレコードを取得（ロック付き）
    OrderSequence sequence = orderSequenceRepository
        .findByBranchCodeAndYearMonthForUpdate(branchCode, yearMonth)
        .orElseGet(() -> createNewSequence(branchCode, yearMonth));

    // 2. 通番をインクリメント
    int nextSeq = sequence.getNextSequence();
    validateSequenceLimit(nextSeq);

    // 3. シーケンスを更新
    sequence.increment();
    orderSequenceRepository.save(sequence);

    // 4. 注文番号を生成
    return OrderNumber.generate(branchCode, orderDate, nextSeq);
  }

  @Query("SELECT s FROM OrderSequence s " +
      "WHERE s.branchCode = :branchCode AND s.yearMonth = :yearMonth")
  @Lock(LockModeType.PESSIMISTIC_WRITE)
  Optional<OrderSequence> findByBranchCodeAndYearMonthForUpdate(
      @Param("branchCode") BranchCode branchCode,
      @Param("yearMonth") YearMonth yearMonth);
}
```

**特徴:**

- `@Lock(LockModeType.PESSIMISTIC_WRITE)` でペシミスティックロックを使用
- シーケンス管理テーブルを使用して通番を管理
- 並行アクセス時の競合を防止

#### 7.6.7 その他の業務固有 ID パターン

| パターン                | 構成            | 例               | 使用シーン                 |
|-------------------------|-----------------|------------------|----------------------------|
| **支社 + 年月 + 通番**  | 3桁 + 6桁 + 5桁 | `00125102300001` | 注文番号、請求書番号       |
| **年月日 + 通番**       | 8桁 + 4桁       | `202510230001`   | 日次処理のバッチ番号       |
| **部門 + 日付 + 通番**  | 2桁 + 6桁 + 4桁 | `012510230001`   | 部門別の伝票番号           |
| **顧客コード + 通番**   | 6桁 + 5桁       | `C0000100001`    | 顧客固有の依頼番号         |
| **プロジェクト + 通番** | 4桁 + 5桁       | `P00100001`      | プロジェクト内のタスク番号 |

#### 7.6.8 注意点

1. **通番上限の管理**: 5桁の場合、99999 が上限。年月で分割しているため、通常問題ないが、大量のデータが発生する場合は桁数を増やす
2. **並行アクセス**: 同一支社・同一年月への同時アクセス時に競合が発生する可能性があるため、ロックまたはシーケンス管理テーブルを使用
3. **ID の解析**: 文字列操作で ID を解析するのではなく、値オブジェクトに解析メソッドを提供
4. **インデックス設計**: 支社コードや年月での検索頻度が高い場合は、適切なインデックスを定義
5. **テスト容易性**: ID 生成ロジックをモック可能にし、テストで固定の ID を使用できるようにする

### 7.7 ID 生成方式の選択基準

| 方式             | 特徴                                 | 使用シーン                         |
|------------------|--------------------------------------|------------------------------------|
| **Snowflake ID** | 数値型、順序付き、分散環境対応       | ビジネスエンティティの主キー       |
| **UUID v7**      | 文字列型、順序付き、グローバルに一意 | 外部システムとの連携、テンポラリID |
| **複合主キー**   | 複数カラムの組み合わせ               | 自然キーが一意なマスターデータ     |
| **業務固有 ID**  | 人間が読みやすい、業務ルールに基づく | 注文番号、請求書番号、伝票番号     |

### 7.8 @GeneratedValue を使用しない理由

プロジェクトでは `@GeneratedValue` を使用せず、アプリケーション側で ID を生成します。

**理由:**

1. **分散環境への対応**: データベースのシーケンスに依存しないため、マルチデータベース環境でも動作可能
2. **テストの容易さ**: ID 生成をモック可能にできるため、テストで ID を固定しやすい
3. **パフォーマンス**: データベースへの round-trip を削減できる
4. **移植性**: データベースベンダーに依存しない ID 生成ロジック
5. **予測可能性**: ID の生成規則がアプリケーション側に明確に定義される

### 7.9 LongValue の検証ユーティリティ

`LongValue` インターフェースは、Long 型値の検証のための豊富なユーティリティを提供します。

| メソッド                 | 説明                                   |
|--------------------------|----------------------------------------|
| `checkPositive()`        | 正の値であることを検証                 |
| `checkNonNegative()`     | 0 以上の値であることを検証             |
| `checkNegative()`        | 負の値であることを検証                 |
| `checkNonPositive()`     | 0 以下の値であることを検証             |
| `checkRangeClosed()`     | 閉区間 [min, max] 内に存在するか検証   |
| `checkRangeOpen()`       | 開区間 (min, max) 内に存在するか検証   |
| `checkRangeClosedOpen()` | 半開区間 [min, max) 内に存在するか検証 |
| `checkRangeOpenClosed()` | 半開区間 (min, max] 内に存在するか検証 |
| `checkAtLeast()`         | 最小値以上であることを検証             |
| `checkAtMost()`          | 最大値以下であることを検証             |
| `checkLessThan()`        | 最大値未満であることを検証             |
| `checkGreaterThan()`     | 最小値超過であることを検証             |
| `checkNotEmpty()`        | 空でないことを検証                     |

---

---


---

### 第2部：JPA 機能

## 8. テーブルコメント (@Table.comment)

テーブルの目的を自己文書化するために、`@Table.comment` を使用します。

```java

@Entity
@Table(schema = "undecided_ef", name = "prefectures", comment = "prefecture table")
public class Prefecture {

}
```

**特徴:**

- DDL 生成時にコメントが反映される（Flyway 等との併用時は注意）
- データベースツールでテーブルの目的を確認できる

---

---

## 9. カラムコメント (@Column.comment)

各カラムの目的を自己文書化するために、`@Column.comment` を使用します。

```java

@Id
@Column(name = "prefecture_id", comment = "Unique identifier for the prefecture", nullable = false)
private Long id;

@Column(name = "prefecture_code", comment = "Prefecture code", nullable = false, length = 2)
private String prefectureCode;
```

**特徴:**

- 各カラムの用途を明確に記録
- データベースツールでカラムの目的を確認できる
- 必須項目 (`nullable = false`) や文字数制限 (`length`) と併用

---

---

## 10. リポジトリバリエーション

プロジェクトでは、`CrudRepository` と `JpaRepository` の両方を使用します。

### 10.1 CrudRepository

基本的な CRUD 操作のみが必要な場合に使用します。

```java
public interface UserRepository extends CrudRepository<User, SnowflakeId> {

  Optional<User> findByEmail(String email);
}
```

### 10.2 JpaRepository

バッチ処理やページネーションが必要な場合に使用します。

```java
public interface EmployeeRepository extends JpaRepository<Employee, SnowflakeId> {

  List<Employee> findBySearchNameContaining(String keyword);
}
```

### 10.3 カスタムクエリメソッド

Spring Data JPA のメソッド名派生クエリを使用して、簡単な検索を実装します。

```java
public interface HolidayRepository extends JpaRepository<Holiday, Long> {

  List<Holiday> findByHolidayDateBetween(LocalDate from, LocalDate to);
}
```

---

---

## 11. 監査フィールドパターン

エンティティの作成・更新履歴を追跡するために、監査フィールドを使用します。

### 11.1 @CreatedDate パターン

`BusinessEntity` で使用されるパターンです。Spring Data JPA の監査機能を使用して、作成日時を自動設定します。

```java

@CreatedDate
private LocalDateTime createdAt;
```

### 11.2 @PrePersist/@PreUpdate パターン

`AuditResource` で使用されるパターンです。ライフサイクルコールバックを使用して、手動で監査フィールドを設定します。

```java

@PrePersist
public void prePersist() {
  createdAt = DateProvider.currentLocalDateTime();
  createdBy = "system";
}

@PreUpdate
public void preUpdate() {
  updatedAt = DateProvider.currentLocalDateTime();
  updatedBy = "system";
}
```

---

---

## 12. @Query / @Modifying（カスタム JPQL クエリ）

標準のリポジトリメソッドでは表現できないクエリは、`@Query` アノテーションを使用して JPQL で定義します。

### 12.1 検索クエリ

```java

@Query(
    "SELECT h FROM Holiday h WHERE h.holidayDate >= :from AND h.holidayDate <= :to ORDER BY"
        + " h.holidayDate ASC")
List<Holiday> findByHolidayDateBetween(@Param("from") LocalDate from, @Param("to") LocalDate to);
```

### 12.2 削除クエリ

```java

@Modifying
@Query("DELETE FROM Bank b WHERE b.datasetId <> :datasetId")
int deleteByDatasetIdNot(@Param("datasetId") String datasetId);
```

**特徴:**

- `@Modifying` を付与すると、UPDATE/DELETE クエリとして実行される
- 戻り値は影響を受けた行数

---

---

## 13. 先頭一致・部分一致検索パターン

文字列フィールドに対する検索は、用途に応じて複数のアプローチを選択できます。

### 13.1 検索アプローチの比較

| アプローチ         | 先頭一致 | 部分一致 | 末尾一致 | 大文字小文字無視 | 複数条件 | インデックス利用 |
|--------------------|----------|----------|----------|------------------|----------|------------------|
| **メソッド名派生** | ✓       | ✓       | ✓       | ✗               | 限定     | ✓               |
| **JPQL LIKE**      | ✓       | ✓       | ✓       | ✓ (LOWER)       | ✓       | ✓               |
| **ExampleMatcher** | ✓       | ✓       | ✓       | ✓               | ✓       | ✓               |
| **Specification**  | ✓       | ✓       | ✓       | ✓               | ✓       | ✓               |

### 13.2 メソッド名派生クエリ

Spring Data JPA がメソッド名から自動的にクエリを生成する最もシンプルな方法です。

#### 13.2.1 基本パターン

```java
public interface EmployeeRepository extends JpaRepository<Employee, SnowflakeId> {

  /**
   * 名前が指定した文字列で始まる従業員を検索（先頭一致）
   */
  List<Employee> findByNameStartingWith(String name);

  /**
   * 名前に指定した文字列を含む従業員を検索（部分一致）
   */
  List<Employee> findByNameContaining(String keyword);

  /**
   * 名前が指定した文字列で終わる従業員を検索（末尾一致）
   */
  List<Employee> findByNameEndingWith(String suffix);

  /**
   * 名前が指定した文字列と完全に一致する従業員を検索
   */
  List<Employee> findByName(String name);
}
```

**特徴:**

- `StartingWith` → `LIKE 'keyword%'`（先頭一致）
- `Containing` → `LIKE '%keyword%'`（部分一致）
- `EndingWith` → `LIKE '%keyword'`（末尾一致）
- コードが簡潔で可読性が高い
- インデックスを活用できる（先頭一致の場合）

#### 13.2.2 複数フィールドの組み合わせ

```java
public interface EmployeeRepository extends JpaRepository<Employee, SnowflakeId> {

  /**
   * 名前と部署の両方で部分一致検索
   */
  List<Employee> findByNameContainingAndDepartmentContaining(
      String nameKeyword, String departmentKeyword);

  /**
   * 名前の先頭一致と部署の完全一致
   */
  List<Employee> findByNameStartingWithAndDepartment(
      String namePrefix, String department);

  /**
   * 名前の部分一致でページネーション
   */
  Page<Employee> findByNameContaining(String keyword, Pageable pageable);
}
```

**特徴:**

- `And` / `Or` で複数条件を組み合わせ可能
- `Pageable` を引数に追加してページネーション対応
- 条件が増えるとメソッド名が長くなる

#### 13.2.3 大文字小文字無視

```java
public interface EmployeeRepository extends JpaRepository<Employee, SnowflakeId> {

  /**
   * 名前の部分一致検索（大文字小文字無視）
   */
  List<Employee> findByNameContainingIgnoreCase(String keyword);

  /**
   * 名前の先頭一致検索（大文字小文字無視）
   */
  List<Employee> findByNameStartingWithIgnoreCase(String prefix);
}
```

**特徴:**

- `IgnoreCase` サフィックスで大文字小文字を無視した検索
- データベースの照合順序に依存しない

#### 13.2.4 制約

- 条件数が増えるとメソッド名が長くなり可読性が低下
- 動的な条件構築には不向き
- `LIKE` のエスケープ処理が限定的

### 13.3 JPQL での LIKE 検索

`@Query` アノテーションを使用して JPQL で LIKE 検索を定義します。

#### 13.3.1 基本パターン

```java
public interface EmployeeRepository extends JpaRepository<Employee, SnowflakeId> {

  /**
   * 名前の先頭一致検索
   */
  @Query("SELECT e FROM Employee e WHERE e.name LIKE :keyword%")
  List<Employee> findByNameStartingWith(@Param("keyword") String keyword);

  /**
   * 名前の部分一致検索
   */
  @Query("SELECT e FROM Employee e WHERE e.name LIKE %:keyword%")
  List<Employee> findByNameContaining(@Param("keyword") String keyword);

  /**
   * 名前の末尾一致検索
   */
  @Query("SELECT e FROM Employee e WHERE e.name LIKE %:keyword")
  List<Employee> findByNameEndingWith(@Param("keyword") String keyword);
}
```

**特徴:**

- パラメータの前後に `%` を追加してワイルドカードを指定
- メソッド名派生よりも柔軟なクエリ構築が可能

#### 13.3.2 大文字小文字無視

```java
public interface EmployeeRepository extends JpaRepository<Employee, SnowflakeId> {

  /**
   * 名前の部分一致検索（大文字小文字無視）
   */
  @Query("SELECT e FROM Employee e WHERE LOWER(e.name) LIKE LOWER(%:keyword%)")
  List<Employee> findByNameContainingIgnoreCase(@Param("keyword") String keyword);

  /**
   * ネイティブクエリでの ILIKE（PostgreSQL）
   */
  @Query(value = "SELECT * FROM undecided_as.employees WHERE name ILIKE %:keyword%",
      nativeQuery = true)
  List<Employee> findByNameContainingIgnoreCaseNative(@Param("keyword") String keyword);
}
```

**特徴:**

- `LOWER()` 関数を使用して大文字小文字を無視
- PostgreSQL の `ILIKE` はネイティブクエリで使用可能
- `LOWER()` 関数を使用するとインデックスが利用されない場合がある

#### 13.3.3 複数条件の組み合わせ

```java
public interface EmployeeRepository extends JpaRepository<Employee, SnowflakeId> {

  /**
   * 名前と部署の両方で部分一致検索
   */
  @Query("SELECT e FROM Employee e " +
      "WHERE e.name LIKE %:nameKeyword% " +
      "AND e.department LIKE %:deptKeyword%")
  List<Employee> findByMultipleKeywords(
      @Param("nameKeyword") String nameKeyword,
      @Param("deptKeyword") String deptKeyword);

  /**
   * 動的な条件（空文字列の場合は条件を適用しない）
   */
  @Query("SELECT e FROM Employee e " +
      "WHERE (:nameKeyword IS NULL OR e.name LIKE %:nameKeyword%) " +
      "AND (:deptKeyword IS NULL OR e.department LIKE %:deptKeyword%)")
  List<Employee> findByOptionalKeywords(
      @Param("nameKeyword") String nameKeyword,
      @Param("deptKeyword") String deptKeyword);
}
```

**特徴:**

- `AND` / `OR` で複数条件を組み合わせ可能
- `IS NULL` チェックでオプション条件を実現

#### 13.3.4 ワイルドカードのエスケープ

```java
public interface EmployeeRepository extends JpaRepository<Employee, SnowflakeId> {

  /**
   * ワイルドカードをエスケープして検索
   */
  @Query("SELECT e FROM Employee e WHERE e.name LIKE :keyword ESCAPE '/'")
  List<Employee> findByNameWithEscape(@Param("keyword") String keyword);
}

// サービス層でのエスケープ処理
public String escapeLikeWildcard(String input) {
  return input.replace("/", "//")
      .replace("%", "/%")
      .replace("_", "/_");
}
```

**特徴:**

- `ESCAPE` 句を使用してワイルドカード文字自体を検索
- `%`、`_`、エスケープ文字をエスケープ処理

### 13.4 ExampleMatcher を使用した検索

Spring Data JPA の Example API を使用して、型安全な動的検索を実現します。

#### 13.4.1 基本パターン

```java
public interface EmployeeRepository extends JpaRepository<Employee, SnowflakeId> {

  /**
   * Example を使用した検索
   */
  List<Employee> findByExample(Example<Employee> example);

  /**
   * Example を使用したページネーション検索
   */
  Page<Employee> findByExample(Example<Employee> example, Pageable pageable);
}

// サービス層での使用例
@Service
@RequiredArgsConstructor
public class EmployeeSearchService {

  private final EmployeeRepository employeeRepository;

  /**
   * 名前の部分一致検索
   */
  public List<Employee> searchByNameContaining(String keyword) {
    Employee probe = new Employee();
    probe.setName(keyword);

    ExampleMatcher matcher = ExampleMatcher.matching()
        .withMatcher("name", ExampleMatcher.GenericPropertyMatchers.contains())
        .withIgnoreCase();

    Example<Employee> example = Example.of(probe, matcher);
    return employeeRepository.findByExample(example);
  }

  /**
   * 名前の先頭一致検索
   */
  public List<Employee> searchByNameStartingWith(String prefix) {
    Employee probe = new Employee();
    probe.setName(prefix);

    ExampleMatcher matcher = ExampleMatcher.matching()
        .withMatcher("name", ExampleMatcher.GenericPropertyMatchers.startsWith());

    Example<Employee> example = Example.of(probe, matcher);
    return employeeRepository.findByExample(example);
  }
}
```

**特徴:**

- `contains()` → 部分一致（`LIKE '%keyword%'`）
- `startsWith()` → 先頭一致（`LIKE 'keyword%'`）
- `endsWith()` → 末尾一致（`LIKE '%keyword'`）
- `withIgnoreCase()` で大文字小文字を無視

#### 13.4.2 複数フィールドの組み合わせ

```java
public List<Employee> searchByMultipleFields(String name, String department) {
  Employee probe = new Employee();
  probe.setName(name);
  probe.setDepartment(department);

  ExampleMatcher matcher = ExampleMatcher.matching()
      .withMatcher("name", ExampleMatcher.GenericPropertyMatchers.contains())
      .withMatcher("department", ExampleMatcher.GenericPropertyMatchers.startsWith())
      .withIgnoreCase()
      .withIgnoreNullValues();  // null のフィールドは条件に含めない

  Example<Employee> example = Example.of(probe, matcher);
  return employeeRepository.findByExample(example);
}
```

**特徴:**

- `withMatcher()` でフィールドごとに異なるマッチングルールを適用
- `withIgnoreNullValues()` で null のフィールドを条件から除外
- デフォルトのマッチングルールを `withMatcher(".*", ...)` で一括設定

#### 13.4.3 正規表現マッチング

```java
public List<Employee> searchByNamePattern(String regex) {
  Employee probe = new Employee();
  probe.setName(regex);

  ExampleMatcher matcher = ExampleMatcher.matching()
      .withMatcher("name", ExampleMatcher.GenericPropertyMatchers.regex());

  Example<Employee> example = Example.of(probe, matcher);
  return employeeRepository.findByExample(example);
}
```

**特徴:**

- `regex()` で正規表現ベースのマッチング
- 複雑なパターンマッチングが可能

#### 13.4.4 制約

- プロブオブジェクトを作成する必要がある
- 関連エンティティの検索が限定的
- `LIKE` と同様にインデックス利用には注意（先頭一致のみ）

### 13.5 Specification を使用した動的検索

JPA Criteria API を使用して、完全に動的な検索条件を構築します。

#### 13.5.1 リポジトリの設定

```java
public interface EmployeeRepository extends JpaRepository<Employee, SnowflakeId>,
    JpaSpecificationExecutor<Employee> {
  // Specification を使用するには JpaSpecificationExecutor を実装
}
```

**特徴:**

- `JpaSpecificationExecutor` を実装して Specification を使用可能に
- `findAll(Specification<T>)`、`count(Specification<T>)` などのメソッドが利用可能

#### 13.5.2 基本パターン

```java
public class EmployeeSpecifications {

  /**
   * 名前の先頭一致検索
   */
  public static Specification<Employee> nameStartingWith(String prefix) {
    return (root, query, cb) -> cb.like(root.get("name"), prefix + "%");
  }

  /**
   * 名前の部分一致検索
   */
  public static Specification<Employee> nameContaining(String keyword) {
    return (root, query, cb) -> cb.like(root.get("name"), "%" + keyword + "%");
  }

  /**
   * 名前の末尾一致検索
   */
  public static Specification<Employee> nameEndingWith(String suffix) {
    return (root, query, cb) -> cb.like(root.get("name"), "%" + suffix);
  }

  /**
   * 名前の部分一致検索（大文字小文字無視）
   */
  public static Specification<Employee> nameContainingIgnoreCase(String keyword) {
    return (root, query, cb) ->
        cb.like(cb.lower(root.get("name")), "%" + keyword.toLowerCase() + "%");
  }
}
```

**特徴:**

- `cb.like()` で LIKE 条件を構築
- `cb.lower()` で大文字小文字を無視
- 静的メソッドとして Specification を定義して再利用

#### 13.5.3 動的な条件構築

```java

@Service
@RequiredArgsConstructor
public class EmployeeSearchService {

  private final EmployeeRepository employeeRepository;

  /**
   * 動的な検索条件を構築して検索
   */
  public Page<Employee> search(EmployeeSearchCondition condition, Pageable pageable) {
    Specification<Employee> spec = Specification.where(null);

    if (StringUtils.hasText(condition.getName())) {
      spec = spec.and(EmployeeSpecifications.nameContaining(condition.getName()));
    }

    if (StringUtils.hasText(condition.getDepartment())) {
      spec = spec.and(EmployeeSpecifications.departmentStartingWith(condition.getDepartment()));
    }

    if (condition.getHireDateFrom() != null) {
      spec = spec.and((root, query, cb) ->
          cb.greaterThanOrEqualTo(root.get("hireDate"), condition.getHireDateFrom()));
    }

    if (condition.getHireDateTo() != null) {
      spec = spec.and((root, query, cb) ->
          cb.lessThanOrEqualTo(root.get("hireDate"), condition.getHireDateTo()));
    }

    return employeeRepository.findAll(spec, pageable);
  }
}

/**
 * 検索条件 DTO
 */
@Value
public class EmployeeSearchCondition {

  String name;
  String department;
  LocalDate hireDateFrom;
  LocalDate hireDateTo;
}
```

**特徴:**

- `Specification.where()` で条件を連結
- 条件が設定されている場合のみ `and()` で追加
- DTO を使用して検索条件をまとめて受け取る

#### 13.5.4 複数条件の OR 結合

```java
public static Specification<Employee> nameOrDepartmentContaining(String keyword) {
  return (root, query, cb) -> {
    Join<Employee, Department> department = root.join("department", JoinType.LEFT);
    return cb.or(
        cb.like(root.get("name"), "%" + keyword + "%"),
        cb.like(department.get("name"), "%" + keyword + "%")
    );
  };
}
```

**特徴:**

- `cb.or()` で OR 条件を構築
- `Join` を使用して関連エンティティのフィールドも検索対象に

#### 13.5.5 制約

- Criteria API の学習コストが高い
- コード量が他のアプローチよりも多くなる
- 複雑なクエリでは可読性が低下する可能性がある

### 13.6 検索パターンの選択基準

| シーン                   | 推奨アプローチ              | 理由                               |
|--------------------------|-----------------------------|------------------------------------|
| 単純な先頭一致・部分一致 | メソッド名派生              | コードが簡潔で可読性が高い         |
| 複数フィールドの固定条件 | JPQL LIKE                   | メソッド名が長くなるのを回避       |
| 動的な条件構築           | Specification               | 条件を柔軟に組み合わせ可能         |
| 型安全な動的検索         | ExampleMatcher              | プロブオブジェクトで型安全性を確保 |
| 大文字小文字無視         | JPQL LOWER / ExampleMatcher | データベースに依存しない           |
| 正規表現マッチング       | ExampleMatcher regex        | 複雑なパターンに対応               |

### 13.7 インデックス設計の考慮点

検索パターンに応じて適切なインデックスを設計します。

#### 13.7.1 先頭一致検索

```sql
-- 先頭一致検索でインデックスが有効に機能
CREATE INDEX idx_employees_name ON undecided_as.employees (name);
```

**特徴:**

- `LIKE 'keyword%'` の形式でインデックスが利用される
- B-tree インデックスで十分

#### 13.7.2 部分一致検索

```sql
-- 部分一致検索ではインデックスが利用されない場合がある
-- trigram インデックスを使用するとインデックスが利用される
CREATE EXTENSION IF NOT EXISTS pg_trgm;
CREATE INDEX idx_employees_name_trgm ON undecided_as.employees
    USING gin (name gin_trgm_ops);
```

**特徴:**

- `LIKE '%keyword%'` の形式では通常の B-tree インデックスが利用されない
- PostgreSQL の `pg_trgm` 拡張を使用して trigram インデックスを定義
- GIN インデックスで部分一致検索のパフォーマンスを向上

#### 13.7.3 複合インデックス

```sql
-- 複数フィールドの検索で複合インデックス
CREATE INDEX idx_employees_name_dept
    ON undecided_as.employees (name, department);
```

**特徴:**

- 先頭一致のフィールドをインデックスの先頭に配置
- 部分一致のフィールドはインデックスの末尾に配置

### 13.8 注意点

1. **SQL インジェクション**: `@Query` で文字列連結を使用して LIKE 条件を構築しない。`@Param`
   を使用してパラメータ化
2. **インデックス利用**: 先頭一致検索ではインデックスが利用されるが、部分一致検索では利用されない場合がある
3. **大文字小文字**: データベースの照合順序に依存しないように、`LOWER()` や `IgnoreCase` を明示的に使用
4. **ワイルドカードエスケープ**: ユーザー入力が `%` や `_` を含む場合、エスケープ処理を行う
5. **パフォーマンス**: 部分一致検索はテーブルスキャンになる可能性があるため、データ量が多い場合は
   trigram インデックスを検討
6. **ExampleMatcher の制限**: 関連エンティティの検索は限定的で、複雑な条件には Specification を使用

---

---

## 14. @Lock（ペシミスティックロック）

競合が発生するシナリオでは、`@Lock` アノテーションを使用して行レベルの排他制御を実現します。

```java

@Lock(LockModeType.PESSIMISTIC_WRITE)
@Query(
    """
        select n
        from SnowflakeNode n
        where n.applicationName = :applicationName
          and n.leasedAt < :before
        """)
Optional<SnowflakeNode> findAvailableNode(
    @Param("applicationName") String applicationName,
    @Param("before") LocalDateTime before);
```

**特徴:**

- `PESSIMISTIC_WRITE` ロックを使用して、行レベルの排他制御を実現
- Snowflake ID のノード割り当てのような競合が発生するシナリオで使用

---

---

## 15. @Transactional（トランザクション管理）

サービス層で `@Transactional` アノテーションを使用して、トランザクション境界を明確に定義します。

```java

@Override
@Transactional
public void use(BankAccountUseDto bankAccountUseDto) {
  // ...
}
```

```java

@Transactional
public SnowflakeNode allocate(String applicationName) {
  // ...
}

@Transactional
public boolean renew(SnowflakeNode lease) {
  // ...
}
```

**特徴:**

- サービス層でトランザクション境界を定義
- デフォルトは READ-WRITE トランザクション
- 読み取り専用の場合は `@Transactional(readOnly = true)` を明示

---

---

## 16. Schema 分離パターン

モジュールごとにスキーマを分離して、データベースの論理的な区切りを明確にします。

### 16.1 スキーマ一覧

| スキーマ            | 用途               | 例                                      |
|---------------------|--------------------|-----------------------------------------|
| `undecided_ef`      | 汎用マスターデータ | Prefecture, City, Bank, Branch, Holiday |
| `relationship_mgmt` | 関係管理データ     | Party, Person, Organization, Role       |

### 16.2 実装例

```java

@Entity
@Table(schema = "undecided_ef", name = "prefectures", comment = "prefecture table")
public class Prefecture {

}

@Entity
@Table(name = "party", schema = SchemaRelationshipMgmt.NAME)
public abstract class Party {

}
```

**特徴:**

- スキーマ名は定数として定義し、一元的に管理（例: `SchemaRelationshipMgmt.NAME`）
- モジュール間のデータ依存性をスキーマ分離で視覚化

---

---


---

### 第3部：Flyway と DDL

## 17. Flyway と JPA の連携

プロジェクトでは、Flyway を使用してデータベーススキーマをバージョン管理し、JPA は DDL
生成を行わない方式を採用しています。

### 17.1 基本設定

`application.yml` で以下の設定を行います。

```yaml
spring:
  jpa:
    hibernate:
      ddl-auto: none  # JPA による DDL 生成を無効化
    properties.hibernate.hbm2ddl.create_namespaces: true  # Schema 自動作成を有効
  flyway:
    enabled: true
    outOfOrder: true
    baseline-on-migrate: true
    locations: >
      classpath:db/migration/as,
      classpath:db/migration/do,
      classpath:db/migration/ef,
      classpath:db/migration/pc,
      classpath:db/migration/ar,
      classpath:db/migration/az,
      classpath:db/migration/nt
```

**特徴:**

- `ddl-auto: none` で JPA による DDL 生成を完全に無効化
- Flyway が DDL の唯一のソースとなる
- `create_namespaces: true` で Schema の自動作成を有効化
- `locations` で事業領域ごとにマイグレーションスクリプトを分離

### 17.2 マイグレーションスクリプトの命名規則

Flyway の標準的な命名規則に従います。

```
V{timestamp}__description.sql
```

**例:**

- `V20240529095514__add_parties_table.sql`
- `V20260813124415__add_prefectures_table.sql`
- `V20241226211235__change_column_name_simple_mame_to_simple_name.sql`

**特徴:**

- `V` プレフィックスでバージョンマイグレーションを示す
- タイムスタンプで順序を管理（`YYYYMMDDHHmmss` 形式）
- ダブルアンダースコア `__` でバージョンと説明を区切る
- 説明は英小文字とアンダースコアで記述

### 17.3 Schema 分離パターン

事業領域ごとに Schema を分離して、論理的な区切りを明確にします。

```sql
-- V20240529095514__add_parties_table.sql
create schema if not exists undecided_as;

create table undecided_as.parties
(
    party_id    BIGINT      not null,
    party_type  varchar(10) not null,
    simple_mame varchar(50) not null,
    primary key (party_id)
);
```

**Schema 一覧:**

| Schema         | 略称 | 用途                            |
|----------------|------|---------------------------------|
| `undecided_as` | as   | 関連付け（Association）         |
| `undecided_do` | do   | ID 管理（Distributed Object）   |
| `undecided_ef` | ef   | 汎用マスターデータ（Effective） |
| `undecided_pc` | pc   | 未定義                          |
| `undecided_ar` | ar   | 未定義                          |
| `undecided_az` | az   | 未定義                          |
| `undecided_nt` | nt   | 未定義                          |

**特徴:**

- `create schema if not exists` で Schema の存在を確保
- テーブル名は `schema.table` 形式で明示的に指定
- JPA エンティティの `@Table(schema = "...")` と対応

### 17.4 テーブル定義パターン

テーブル定義にはコメント、インデックス、制約を含めます。

```sql
-- V20260813124415__add_prefectures_table.sql
create schema if not exists undecided_ef;

create table undecided_ef.prefectures
(
    prefecture_id   BIGINT      not null,
    prefecture_code varchar(2)  not null,
    lg_code         varchar(6)  not null,
    pref_name       varchar(10) not null,
    pref_kana       varchar(50) not null,
    pref_roma       varchar(50) not null,
    effective_date  date        not null,
    abolition_data  date        not null,
    remarks         varchar(256),
    primary key (prefecture_id)
);

create unique index prefecture_code_uindex on undecided_ef.prefectures (prefecture_code);

comment on table undecided_ef.prefectures is 'prefecture table';
comment on column undecided_ef.prefectures.prefecture_id is 'Unique identifier for the prefecture';
comment on column undecided_ef.prefectures.prefecture_code is 'Prefecture code';
comment on column undecided_ef.prefectures.lg_code is 'Legal code';
comment on column undecided_ef.prefectures.pref_name is 'Prefecture name';
comment on column undecided_ef.prefectures.pref_kana is 'Prefecture kana';
comment on column undecided_ef.prefectures.pref_roma is 'Prefecture romaji';
comment on column undecided_ef.prefectures.effective_date is 'Effective date';
comment on column undecided_ef.prefectures.abolition_data is 'Abolition date';
comment on column undecided_ef.prefectures.remarks is 'Remarks';
```

**特徴:**

- `create unique index` で一意制約用のインデックスを定義
- `comment on table` / `comment on column` でテーブル・カラムのコメントを定義
- JPA エンティティの `@Table.comment` / `@Column.comment` と対応

### 17.5 インデックス定義パターン

インデックスは Flyway で明示的に定義します。

```sql
-- 一意インデックス
create unique index holiday_date_uindex on undecided_ef.holiday (holiday_date);

-- 通常インデックス
create index idx_branches_dataset_id on undecided_ef.branches (dataset_id);
create index idx_bank_master_imports_occurred_at on undecided_ef.bank_master_imports (occurred_at);
```

**命名規則:**

- 一意インデックス: `{column_name}_uindex`
- 通常インデックス: `idx_{table_name}_{column_name}`

**特徴:**

- JPA の `@Index` アノテーションは使用せず、Flyway で管理
- インデックス名は用途を明確にする命名規則に従う

### 17.6 複合主キーパターン

複合主キーは複数のカラムを指定して定義します。

```sql
create table undecided_ef.branches
(
    branch_code      varchar(3)   not null,
    bank_code        varchar(4)   not null,
    branch_name      varchar(128) not null,
    branch_half_kana varchar(256),
    branch_full_kana varchar(256),
    branch_hiragana  varchar(256),
    dataset_id       varchar(128) not null,
    primary key (bank_code, branch_code)
);
```

**特徴:**

- 複合主キーは JPA では `@EmbeddedId` または `@IdClass` で対応
- プロジェクトでは単一カラムの Snowflake ID を主キーとするパターンが主流

### 17.7 カラム変更パターン（データ保持を保証）

既存テーブルのカラムを変更する場合は、 **既存のデータ値を失わない**安全な手順でマイグレーションします。Flyway
では DDL が自動生成されないため、すべてのデータ保持ロジックをマイグレーションスクリプトで明示的に記述する必要があります。

#### 17.7.1 カラム名変更

カラム名を変更する場合は、新カラム追加→データコピー→旧カラム削除の3段階で実行します。

```sql
-- V20241226211235__change_column_name_simple_mame_to_simple_name.sql
-- ステップ1: 新しいカラムを追加
alter table undecided_as.parties
    add column simple_name varchar(50);

-- ステップ2: 既存データを新しいカラムにコピー
update undecided_as.parties
set simple_name = simple_mame
where true;

-- ステップ3: 新しいカラムに制約を設定
ALTER TABLE undecided_as.parties
    ALTER COLUMN simple_name SET NOT NULL;

-- ステップ4: 古いカラムを削除
ALTER TABLE undecided_as.parties
    drop column simple_mame;
```

**安全なカラム名変更手順:**

1. 新しいカラムを追加（型・制約は旧カラムと同じ）
2. 既存データを新しいカラムにコピー（`UPDATE ... SET new_col = old_col WHERE TRUE`）
3. 新しいカラムに制約を設定（NOT NULL, UNIQUE など）
4. 古いカラムを削除

**特徴:**

- 既存データを失わないように、段階的に変更
- `NOT NULL` 制約を設定する前にデータをコピー
- 古いカラムの削除は最後に実行
- トランザクション内で実行することで、失敗時にロールバック可能

#### 17.7.2 カラム型変更

カラムのデータ型を変更する場合は、`SET DATA TYPE` と `USING` 句を使用して、既存値を安全に変換します。

```sql
-- varchar(50) → varchar(100) への拡張（後方互換性あり）
ALTER TABLE undecided_as.parties
    ALTER COLUMN simple_name TYPE varchar(100);

-- integer → bigint への拡張（後方互換性あり）
ALTER TABLE undecided_as.parties
    ALTER COLUMN party_id TYPE bigint USING party_id::bigint;

-- varchar → text への変換（後方互換性あり）
ALTER TABLE undecided_as.parties
    ALTER COLUMN description TYPE text USING description::text;

-- 縮小系の変換（データ損失のリスクあり、注意が必要）
ALTER TABLE undecided_as.parties
    ALTER COLUMN simple_name TYPE varchar(30) USING substring(simple_name, 1, 30);
```

**安全なカラム型変更手順:**

1. 型変更が後方互換性があるか確認（拡張系は安全、縮小系は注意）
2. `SET DATA TYPE` で型を変更
3. 型が互換性のない場合は `USING` 句で変換式を指定
4. 縮小系の変換の場合は、事前にデータ検証クエリを実行

**後方互換性のある型変更（USING不要）:**
| 変更前 | 変更後 | 安全性 |
|--------|--------|--------|
| `varchar(50)` | `varchar(100)` | 安全 |
| `integer` | `bigint` | 安全 |
| `smallint` | `integer` | 安全 |
| `varchar` | `text` | 安全 |
| `timestamp` | `timestamptz` | 安全（UTC変換） |

**データ損失のリスクがある型変更（USING必須）:**
| 変更前 | 変更後 | 変換式 |
|--------|--------|--------|
| `varchar(100)` | `varchar(50)` | `USING substring(col, 1, 50)` |
| `bigint` | `integer` | `USING col::integer`（範囲外エラーのリスク） |
| `text` | `varchar(50)` | `USING substring(col, 1, 50)` |
| `numeric(10,2)` | `numeric(5,2)` | `USING col::numeric(5,2)`（桁落ちのリスク） |

**特徴:**

- 拡張系の変換では `USING` 句は不要
- 縮小系の変換では `USING` 句で明示的な変換式を指定
- 縮小系変換前に、影響を受ける行数を検証クエリで確認

#### 17.7.3 NOT NULL 制約追加

既存カラムに NOT NULL 制約を追加する場合は、デフォルト値の設定とデータ補完が必要です。

```sql
-- パターン1: デフォルト値を設定してからNOT NULL制約を追加
ALTER TABLE undecided_as.parties
    ALTER COLUMN simple_name SET DEFAULT '未設定';

-- NULL値をデフォルト値で補完
UPDATE undecided_as.parties
SET simple_name = '未設定'
WHERE simple_name IS NULL;

-- NOT NULL制約を追加
ALTER TABLE undecided_as.parties
    ALTER COLUMN simple_name SET NOT NULL;

-- パターン2: 既存データがNULLでないことを確認してから制約を追加
-- 事前に検証クエリを実行
SELECT count(*)
FROM undecided_as.parties
WHERE simple_name IS NULL;
-- 0件であることを確認してから
ALTER TABLE undecided_as.parties
    ALTER COLUMN simple_name SET NOT NULL;
```

**安全なNOT NULL制約追加手順:**

1. 既存データのNULL値を確認（`SELECT count(*) WHERE col IS NULL`）
2. NULL値が存在する場合は、デフォルト値を設定して補完
3. NOT NULL制約を追加

**特徴:**

- NULL値が存在する状態でNOT NULL制約を追加するとエラーになる
- デフォルト値を設定してから制約を追加すると、既存のNULL値はデフォルト値で補完されないため、明示的にUPDATEが必要
- 制約追加前に検証クエリでNULL値が0件であることを確認

#### 17.7.4 デフォルト値変更

既存カラムのデフォルト値を変更する場合は、既存のデータ値には影響しません。

```sql
-- デフォルト値の変更（既存データには影響しない）
ALTER TABLE undecided_as.parties
    ALTER COLUMN simple_name SET DEFAULT '新しいデフォルト値';

-- デフォルト値の削除
ALTER TABLE undecided_as.parties
    ALTER COLUMN simple_name DROP DEFAULT;
```

**特徴:**

- `SET DEFAULT` は既存のデータ値には影響しない
- 変更後の新規INSERTのみが新しいデフォルト値を使用
- 既存データを新しいデフォルト値に更新したい場合は、別途UPDATE文を実行

#### 17.7.5 カラム追加（NOT NULL + DEFAULT パターン）

既存データを持つテーブルに NOT NULL カラムを追加する場合は、DEFAULT 値を指定する必要があります。

```sql
-- パターン1: DEFAULT値を指定してNOT NULLカラムを追加
ALTER TABLE undecided_as.parties
    ADD COLUMN status_code varchar(20) NOT NULL DEFAULT 'ACTIVE';

-- パターン2: NULL許容で追加→データ補完→NOT NULL制約追加
ALTER TABLE undecided_as.parties
    ADD COLUMN status_code varchar(20);

UPDATE undecided_as.parties
SET status_code = 'ACTIVE'
WHERE status_code IS NULL;

ALTER TABLE undecided_as.parties
    ALTER COLUMN status_code SET NOT NULL;
```

**特徴:**

- `NOT NULL` 制約付きカラムを追加する場合、`DEFAULT` 値の指定が必須
- `DEFAULT` 値を指定すると、既存行にデフォルト値が自動的に設定される
- PostgreSQL 11 以降では、`ADD COLUMN ... NOT NULL DEFAULT ...` はテーブルロックが軽量

#### 17.7.6 テーブル削除前のデータバックアップ

テーブルを削除する前に、データをバックアップしておきます。

```sql
-- V20261001110242__drop_table_public_cites.sql
-- テーブル削除前にデータをバックアップ（必要に応じて）
-- CREATE TABLE undecided_ef.cities_backup AS SELECT * FROM undecided_ef.cities;

-- テーブルを削除
drop table if exists undecided_ef.cities;
```

**安全なテーブル削除手順:**

1. 削除するテーブルが他のテーブルから参照されていないか確認
2. 必要に応じてデータをバックアップ（`CREATE TABLE ... AS SELECT ...`）
3. `DROP TABLE IF EXISTS` で安全に削除

**特徴:**

- `IF EXISTS` を使用すると、テーブルが存在しない場合でもエラーにならない
- 外部キー制約で参照されているテーブルは削除できない（`CASCADE` を使用するか、制約を先に削除）
- 本番環境では、削除前に必ずデータバックアップを取得

#### 17.7.7 制約変更

CHECK制約やUNIQUE制約を変更するパターンです。

```sql
-- CHECK制約の追加
ALTER TABLE undecided_as.parties
    ADD CONSTRAINT chk_parties_status_code
        CHECK (status_code IN ('ACTIVE', 'INACTIVE', 'SUSPENDED'));

-- CHECK制約の削除
ALTER TABLE undecided_as.parties
    DROP CONSTRAINT IF EXISTS chk_parties_status_code;

-- UNIQUE制約の追加
ALTER TABLE undecided_as.parties
    ADD CONSTRAINT uq_parties_email
        UNIQUE (email);

-- UNIQUE制約の削除
ALTER TABLE undecided_as.parties
    DROP CONSTRAINT IF EXISTS uq_parties_email;

-- 既存データが制約に違反していないか確認してから追加
SELECT *
FROM undecided_as.parties
WHERE status_code NOT IN ('ACTIVE', 'INACTIVE', 'SUSPENDED');
-- 0件であることを確認してから制約を追加
```

**特徴:**

- 制約を追加する前に、既存データが制約に違反していないか確認
- `IF EXISTS` を使用すると、制約が存在しない場合でもエラーにならない
- 制約名は `chk_`, `uq_`, `fk_` などのプレフィックスで統一

#### 17.7.8 インデックス変更

インデックスを追加・削除・再構築するパターンです。

```sql
-- インデックスの追加
CREATE INDEX idx_parties_status_code
    ON undecided_as.parties (status_code);

-- 一意インデックスの追加
CREATE UNIQUE INDEX uq_parties_email
    ON undecided_as.parties (email);

-- インデックスの削除
DROP INDEX IF EXISTS idx_parties_status_code;

-- インデックスの再構築（REINDEX）
REINDEX INDEX idx_parties_status_code;
```

**特徴:**

- インデックス追加は既存データに影響しない
- `IF EXISTS` を使用すると、インデックスが存在しない場合でもエラーにならない
- 大規模テーブルではインデックス追加に時間がかかる場合がある

#### 17.7.9 安全なDDL操作のチェックリスト

DDL操作を実行する前に、以下のチェックリストで確認します。

| チェック項目       | 確認内容                                     |
|--------------------|----------------------------------------------|
| データバックアップ | 本番環境では操作前にバックアップを取得       |
| 影響行数確認       | `SELECT count(*)` で影響を受ける行数を確認   |
| 後方互換性         | 型変更が後方互換性があるか確認               |
| NULL値確認         | NOT NULL制約追加前にNULL値が存在しないか確認 |
| 制約違反確認       | 制約追加前に既存データが違反していないか確認 |
| トランザクション   | 可能であればトランザクション内で実行         |
| ロールバック計画   | 失敗した場合の復旧手順を事前に準備           |
| テスト環境での検証 | 本番環境実行前にテスト環境で検証             |

**特徴:**

- 本番環境では、必ずテスト環境で事前に検証
- 大規模テーブルでは、オフピーク時間帯に実行
- 操作ログをマイグレーションスクリプトのコメントに記載

### 17.8 View 定義パターン

データベース View は Flyway で定義し、JPA エンティティでマッピングします。

```sql
-- V1__create_party_with_org_view.sql
CREATE OR REPLACE VIEW undecided_as.v_party_with_org AS
SELECT p.party_id, p.search_name, o.org_name
FROM undecided_as.party p
         JOIN undecided_as.organization o ON p.party_id = o.party_id;
```

**特徴:**

- `CREATE OR REPLACE VIEW` で更新可能
- View の名前は `v_` プレフィックスで統一
- JPA エンティティでは `@Subselect` + `@Immutable` でマッピング（セクション18参照）

### 17.9 JPA エンティティと Flyway DDL の対応ルール

JPA エンティティと Flyway DDL の間で以下の対応関係を維持します。

| JPA アノテーション          | Flyway DDL                         |
|-----------------------------|------------------------------------|
| `@Table(name = "...")`      | `create table ...`                 |
| `@Table(schema = "...")`    | `schema.table`                     |
| `@Table.comment`            | `comment on table ...`             |
| `@Column(name = "...")`     | カラム定義                         |
| `@Column.comment`           | `comment on column ...`            |
| `@Column(nullable = false)` | `not null`                         |
| `@Column(length = N)`       | `varchar(N)`                       |
| `@Id`                       | `primary key`                      |
| `@JoinColumn`               | 外部キーカラム（制約は定義しない） |
| `@Index`                    | `create index ...`                 |

**特徴:**

- Flyway が DDL の唯一のソースであり、JPA は読み取り専用
- 外部キー制約は Flyway で定義せず、アプリケーション層で整合性を管理
- JPA アノテーションはマッピング情報のみとして使用

### 17.10 Flyway の実行タイミング

Flyway はアプリケーション起動時に自動的に実行されます。

**実行フロー:**

1. アプリケーション起動
2. Flyway がマイグレーションスクリプトをスキャン
3. 未実行のマイグレーションを順次適用
4. `schema_version` テーブルに実行履歴を記録
5. JPA が既存のスキーマを使用してエンティティをマッピング

**特徴:**

- `baseline-on-migrate: true` で既存のスキーマをベースライン化
- `outOfOrder: true` で順序外れのマイグレーションを許可
- 開発環境ではマイグレーションの追加・修正が容易

---

---


---

### 第4部：DB オブジェクト

## 18. View を使用するパターン

データベース View を JPA エンティティとしてマッピングするパターンです。読み取り専用の集計データや、複数のテーブルを結合したデータに有効です。

### 18.1 @Subselect（EJB3 準拠の Read-Only View）

`@Subselect` アノテーションを使用して、SQL SELECT 文の結果をエンティティとしてマッピングします。

```java

@Entity
@Subselect("SELECT p.party_id, p.search_name, o.org_name " +
    "FROM party p " +
    "JOIN organization o ON p.party_id = o.party_id")
@Table(name = "v_party_with_org")
@org.hibernate.annotations.Immutable  // 読み取り専用としてマーク
public class PartyWithOrgView {

  @Id
  private SnowflakeId partyId;
  private SearchName searchName;
  private OrgName orgName;
}
```

**特徴:**

- `@org.hibernate.annotations.Immutable` で読み取り専用としてマーク
- INSERT/UPDATE/DELETE が禁止される
- Hibernate 固有のアノテーションだが、View マッピングの標準的な手法
- `@Table(name = "...")` で View 名を明示的に指定

### 18.2 @SqlResultSetMapping（ネイティブクエリ用の結果マッピング）

複雑な View やネイティブ SQL クエリの結果をエンティティにマッピングする場合に使用します。

```java

@Entity
@SqlResultSetMapping(
    name = "PartyWithOrgMapping",
    entities = @EntityResult(
        entityClass = PartyWithOrg.class,
        fields = {
            @FieldResult(name = "partyId", column = "party_id"),
            @FieldResult(name = "searchName", column = "search_name"),
            @FieldResult(name = "orgName", column = "org_name")
        }
    )
)
public class PartyWithOrg {

  @Id
  private SnowflakeId partyId;
  private SearchName searchName;
  private OrgName orgName;
}
```

**特徴:**

- ネイティブクエリの結果をエンティティにマッピング
- カラム名とフィールド名の対応を明示的に定義
- 複雑な JOIN や集計クエリに適している

### 18.3 @NamedNativeQuery（名前付きネイティブクエリ）

View に対するクエリをエンティティに定義して、リポジトリから呼び出します。

```java

@Entity
@Table(name = "v_employee_summary")
@org.hibernate.annotations.Immutable
@NamedNativeQuery(
    name = "EmployeeSummary.findByDepartment",
    query = "SELECT * FROM v_employee_summary WHERE department_code = :deptCode",
    resultClass = EmployeeSummary.class
)
public class EmployeeSummary {

  @Id
  private SnowflakeId employeeId;
  private String departmentCode;
  private Integer employeeCount;
}
```

**特徴:**

- エンティティにクエリを定義して、名前付きで参照可能
- リポジトリでは `@Query(nativeQuery = true, name = "EmployeeSummary.findByDepartment")` で呼び出し
- View の構造が複雑な場合に有効

### 18.4 リポジトリでの View 使用例

```java
public interface PartyWithOrgViewRepository extends JpaRepository<PartyWithOrgView, SnowflakeId> {

  List<PartyWithOrgView> findBySearchNameContaining(String keyword);
}
```

**特徴:**

- 通常のエンティティと同様にリポジトリを定義
- `CrudRepository` または `JpaRepository` を使用
- 読み取り専用なので、save/delete メソッドは使用しない

### 18.5 View の定義方法

データベース側で View を定義するには、Flyway マイグレーションスクリプトを使用します（セクション17.8も参照）。

```sql
-- V1__create_party_with_org_view.sql
CREATE OR REPLACE VIEW v_party_with_org AS
SELECT p.party_id, p.search_name, o.org_name
FROM party p
         JOIN organization o ON p.party_id = o.party_id;
```

**特徴:**

- Flyway で View のバージョン管理を行う
- `CREATE OR REPLACE VIEW` で更新可能
- View の名前は `v_` プレフィックスで統一することを推奨

### 18.6 View へのコメント付与

View にも COMMENT ON 文を使用してコメントを付与できます。テーブルと同様に、View 自体へのコメントと、View
のカラムへのコメントの 2 種類があります。

#### 18.6.1 基本構文

```sql
-- View 自体へのコメント
COMMENT ON VIEW v_party_with_org IS 'Party と Organization を結合した検索用 View';

-- View のカラムへのコメント
COMMENT ON COLUMN v_party_with_org.party_id IS 'Party の一意な識別子';
COMMENT ON COLUMN v_party_with_org.search_name IS '検索用の名称';
COMMENT ON COLUMN v_party_with_org.org_name IS '所属組織名';
```

**特徴:**

- `COMMENT ON VIEW` で View 自体に説明文を付与
- `COMMENT ON COLUMN` で View の各カラムに説明文を付与
- View は仮想テーブルなので、テーブルと同じ構文で使用可能
- コメントは `pg_description` システムカタログに保存される

#### 18.6.2 Schema 分離パターン

プロジェクトでは Schema を分離しているため、コメント付与時もスキーマ名を明示します。

```sql
-- Schema を明示した View コメント
COMMENT ON VIEW undecided_as.v_party_with_org IS 'Party と Organization を結合した検索用 View';

-- Schema を明示したカラムコメント
COMMENT ON COLUMN undecided_as.v_party_with_org.party_id IS 'Party の一意な識別子';
COMMENT ON COLUMN undecided_as.v_party_with_org.search_name IS '検索用の名称';
```

**特徴:**

- スキーマ名を省略すると、`search_path` に依存するため明示することを推奨
- 事業領域ごとに Schema を分離しているため、対象 Schema を指定

#### 18.6.3 Flyway マイグレーションとの連携

View の定義とコメント付与を Flyway マイグレーションスクリプトで一元管理します。

```sql
-- V20261025090000__create_party_with_org_view.sql

-- View の作成
CREATE OR REPLACE VIEW undecided_as.v_party_with_org AS
SELECT p.party_id, p.search_name, o.org_name
FROM undecided_as.parties p
         JOIN undecided_as.organizations o ON p.party_id = o.party_id;

-- View へのコメント付与
COMMENT ON VIEW undecided_as.v_party_with_org IS 'Party と Organization を結合した検索用 View';

-- カラムへのコメント付与
COMMENT ON COLUMN undecided_as.v_party_with_org.party_id IS 'Party の一意な識別子';
COMMENT ON COLUMN undecided_as.v_party_with_org.search_name IS '検索用の名称';
COMMENT ON COLUMN undecided_as.v_party_with_org.org_name IS '所属組織名';
```

**View の更新時のコメント管理:**

```sql
-- V20261026090000__update_party_with_org_view.sql

-- View の更新
CREATE OR REPLACE VIEW undecided_as.v_party_with_org AS
SELECT p.party_id, p.search_name, o.org_name, p.registered_at
FROM undecided_as.parties p
         JOIN undecided_as.organizations o ON p.party_id = o.party_id;

-- View コメントの更新（COMMENT ON VIEW は上書きされる）
COMMENT ON VIEW undecided_as.v_party_with_org IS 'Party と Organization を結合した検索用 View（登録日追加）';

-- 新規カラムへのコメント付与
COMMENT ON COLUMN undecided_as.v_party_with_org.registered_at IS '登録日時';
```

**特徴:**

- `CREATE OR REPLACE VIEW` で View を更新しても、既存のコメントは保持される
- View の定義が変更された場合は、コメントも合わせて更新することを推奨
- 新規カラムが追加された場合は、そのカラムへのコメントも付与

#### 18.6.4 Materialized View へのコメント付与

Materialized View にも同様にコメントを付与できます。

```sql
-- V20261025090001__create_party_summary_mv.sql

-- Materialized View の作成
CREATE MATERIALIZED VIEW undecided_as.mv_party_summary AS
SELECT p.party_type,
       COUNT(*)          AS party_count,
       MAX(p.updated_at) AS last_updated
FROM undecided_as.parties p
GROUP BY p.party_type;

-- Materialized View へのコメント付与
COMMENT ON MATERIALIZED VIEW undecided_as.mv_party_summary IS 'Party 種別ごとの集計結果（1時間ごとにリフレッシュ）';

-- カラムへのコメント付与
COMMENT ON COLUMN undecided_as.mv_party_summary.party_type IS 'Party の種別';
COMMENT ON COLUMN undecided_as.mv_party_summary.party_count IS 'Party の件数';
COMMENT ON COLUMN undecided_as.mv_party_summary.last_updated IS '最終更新日時';
```

**特徴:**

- Materialized View の場合は `COMMENT ON MATERIALIZED VIEW` を使用
- 通常の View とは構文が異なるため注意
- リフレッシュ頻度やデータ鮮度などの情報をコメントに記載することを推奨

#### 18.6.5 コメントの確認方法

付与したコメントは以下の方法で確認できます。

```sql
-- View のコメント確認
SELECT c.relname              AS view_name,
       obj_description(c.oid) AS view_comment
FROM pg_class c
         JOIN pg_namespace n ON c.relnamespace = n.oid
WHERE n.nspname = 'undecided_as'
  AND c.relkind = 'v';
-- 'v' は View, 'm' は Materialized View

-- Materialized View のコメント確認
SELECT c.relname              AS mv_name,
       obj_description(c.oid) AS mv_comment
FROM pg_class c
         JOIN pg_namespace n ON c.relnamespace = n.oid
WHERE n.nspname = 'undecided_as'
  AND c.relkind = 'm';
-- 'm' は Materialized View

-- カラムのコメント確認
SELECT c.relname                        AS view_name,
       a.attname                        AS column_name,
       col_description(c.oid, a.attnum) AS column_comment
FROM pg_class c
         JOIN pg_namespace n ON c.relnamespace = n.oid
         JOIN pg_attribute a ON c.oid = a.attrelid
WHERE n.nspname = 'undecided_as'
  AND c.relname = 'v_party_with_org'
  AND a.attnum > 0
  AND NOT a.attisdropped
ORDER BY a.attnum;
```

**特徴:**

- `obj_description()` で View 自体のコメントを取得
- `col_description()` でカラムのコメントを取得
- `pg_class.relkind` でオブジェクト種別を区別（'v' = View, 'm' = Materialized View）

#### 18.6.6 コメントの削除

```sql
-- View コメントの削除
COMMENT ON VIEW undecided_as.v_party_with_org IS NULL;

-- カラムコメントの削除
COMMENT ON COLUMN undecided_as.v_party_with_org.party_id IS NULL;

-- Materialized View コメントの削除
COMMENT ON MATERIALIZED VIEW undecided_as.mv_party_summary IS NULL;
```

**特徴:**

- `IS NULL` を指定するとコメントが削除される
- View を削除すると、関連するコメントも自動的に削除される

#### 18.6.7 使用シーン

| シーン                               | 推奨                                    |
|--------------------------------------|-----------------------------------------|
| View の目的の説明                    | `COMMENT ON VIEW`                       |
| カラムの意味の説明                   | `COMMENT ON COLUMN`                     |
| Materialized View のリフレッシュ頻度 | `COMMENT ON MATERIALIZED VIEW`          |
| 複雑な View のロジック説明           | `COMMENT ON VIEW`                       |
| 外部システム連携用の View            | `COMMENT ON VIEW` + `COMMENT ON COLUMN` |
| レポート用の View                    | `COMMENT ON VIEW` + `COMMENT ON COLUMN` |

---

### 18.7 使用シーン

| シーン                | パターン                                  |
|-----------------------|-------------------------------------------|
| 単純な View           | `@Subselect` + `@Immutable`               |
| 複雑な JOIN           | `@SqlResultSetMapping` + ネイティブクエリ |
| 名前付きクエリ        | `@NamedNativeQuery`                       |
| 集計データ            | `@Subselect` + `@Immutable`               |
| View のドキュメント化 | `COMMENT ON VIEW` + `COMMENT ON COLUMN`   |

---

---

## 19. Materialized View を使用するパターン

Materialized View は、クエリ結果を物理的にストレージに保存する特殊な View です。通常の View
が都度クエリを実行するのに対し、Materialized View は一度生成した結果をキャッシュのように保持し、高速な読み取りを提供します。

### 19.1 Materialized View の基本

通常の View と Materialized View の違いを理解することが重要です。

| 項目         | View                       | Materialized View                       |
|--------------|----------------------------|-----------------------------------------|
| データ保存   | 保存しない（仮想）         | 物理的に保存                            |
| クエリ速度   | 都度実行するため遅い       | 保存済みデータを読むため高速            |
| データ鮮度   | 常に最新                   | リフレッシュ時まで古いデータ            |
| ストレージ   | 不要                       | 必要                                    |
| インデックス | 定義できない               | 定義可能                                |
| 更新方法     | 基テーブルの変更で自動反映 | `REFRESH MATERIALIZED VIEW` で手動/自動 |

**使用シーン:**

- 集計結果を頻繁に参照するが、リアルタイム性は不要
- 複雑な JOIN や集計クエリを高速化したい
- レポート画面やダッシュボードのデータソース

### 19.2 Flyway での Materialized View 定義

Materialized View は Flyway マイグレーションスクリプトで定義します。

```sql
-- V20261025070000__create_party_summary_materialized_view.sql

-- Materialized View の作成
CREATE MATERIALIZED VIEW undecided_as.mv_party_summary AS
SELECT p.party_type,
       COUNT(*)          AS party_count,
       MAX(p.updated_at) AS last_updated
FROM undecided_as.parties p
GROUP BY p.party_type;

-- インデックスの定義（パフォーマンス向上）
CREATE INDEX idx_mv_party_summary_type ON undecided_as.mv_party_summary (party_type);

-- UNIQUE INDEX（CONCURRENTLY リフレッシュ用）
CREATE UNIQUE INDEX idx_mv_party_summary_type_unique ON undecided_as.mv_party_summary (party_type);
```

**特徴:**

- `CREATE MATERIALIZED VIEW` で定義
- 通常の View と違い、インデックスを定義可能
- `UNIQUE INDEX` を定義すると、`REFRESH ... CONCURRENTLY` が使用可能に
- Materialized View の名前は `mv_` プレフィックスで統一することを推奨

### 19.3 JPA エンティティでのマッピング

Materialized View を JPA エンティティとしてマッピングします。

```java

@Entity
@Table(schema = "undecided_as", name = "mv_party_summary")
@org.hibernate.annotations.Immutable  // 読み取り専用としてマーク
public class PartySummaryMV {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;  // Materialized View には主キーが必要

  @Column(name = "party_type", nullable = false, length = 10)
  private String partyType;

  @Column(name = "party_count", nullable = false)
  private Long partyCount;

  @Column(name = "last_updated")
  private LocalDateTime lastUpdated;
}
```

**特徴:**

- `@org.hibernate.annotations.Immutable` で読み取り専用としてマーク
- Materialized View には主キーが必要ない場合でも、JPA では `@Id` が必須
- PostgreSQL の `oid` カラムや、シーケンスで生成した surrogate key を `@Id` として使用可能

**主キーがない場合のワークアラウンド:**

```java

@Entity
@Table(schema = "undecided_as", name = "mv_party_summary")
@org.hibernate.annotations.Immutable
@SqlResultSetMapping(
    name = "PartySummaryMVMapping",
    entities = @EntityResult(
        entityClass = PartySummaryMV.class,
        fields = {
            @FieldResult(name = "partyType", column = "party_type"),
            @FieldResult(name = "partyCount", column = "party_count"),
            @FieldResult(name = "lastUpdated", column = "last_updated")
        }
    )
)
public class PartySummaryMV {

  // surrogate key として row_number() を使用
  @Id
  @Column(name = "row_num")
  private Long rowNum;

  @Column(name = "party_type", nullable = false, length = 10)
  private String partyType;

  @Column(name = "party_count", nullable = false)
  private Long partyCount;

  @Column(name = "last_updated")
  private LocalDateTime lastUpdated;
}
```

### 19.4 リフレッシュ戦略

Materialized View のデータを最新にするには、リフレッシュが必要です。

**手動リフレッシュ:**

```sql
-- 通常のリフレッシュ（排他ロックを取得）
REFRESH MATERIALIZED VIEW undecided_as.mv_party_summary;

-- CONCURRENTLY オプション（排他ロックを取得しない、UNIQUE INDEX が必要）
REFRESH MATERIALIZED VIEW CONCURRENTLY undecided_as.mv_party_summary;
```

**リフレッシュ比較:**

| オプション     | ロック     | UNIQUE INDEX | 読み取り同時実行 |
|----------------|------------|--------------|------------------|
| 通常           | 排他ロック | 不要         | 不可             |
| `CONCURRENTLY` | 共有ロック | 必須         | 可能             |

**Flyway での自動リフレッシュ設定（pg_cron 使用）:**

```sql
-- V20261025070001__setup_materialized_view_refresh.sql

-- pg_cron 拡張の有効化（スーパーユーザー権限が必要）
CREATE EXTENSION IF NOT EXISTS pg_cron;

-- 毎時 0 分に Materialized View をリフレッシュ
SELECT cron.schedule(
               'refresh_party_summary_mv',
               '0 * * * *',
               $$REFRESH MATERIALIZED VIEW CONCURRENTLY undecided_as.mv_party_summary$$
       );
```

**トリガーによる自動リフレッシュ:**

```sql
-- V20261025070002__create_refresh_trigger.sql

-- リフレッシュ関数の定義
CREATE OR REPLACE FUNCTION undecided_as.refresh_party_summary_mv()
    RETURNS TRIGGER AS
$$
BEGIN
    REFRESH MATERIALIZED VIEW CONCURRENTLY undecided_as.mv_party_summary;
    RETURN NULL;
END;
$$ LANGUAGE plpgsql;

-- 基テーブルに変更があった際にトリガー発火
CREATE TRIGGER trigger_refresh_party_summary_mv
    AFTER INSERT OR UPDATE OR DELETE
    ON undecided_as.parties
    DEFERRABLE INITIALLY DEFERRED
    FOR EACH STATEMENT
EXECUTE FUNCTION undecided_as.refresh_party_summary_mv();
```

**特徴:**

- `CONCURRENTLY` を使用すると、リフレッシュ中に読み取りが可能
- `UNIQUE INDEX` が必須（CONCURRENTLY リフレッシュ用）
- pg_cron による定期リフレッシュが推奨（PostgreSQL 14 以降は組み込み）
- トリガーによる即時リフレッシュは、頻繁な更新がある場合はパフォーマンスに影響

### 19.5 リポジトリでの使用例

```java
public interface PartySummaryMVRepository extends JpaRepository<PartySummaryMV, Long> {

  List<PartySummaryMV> findByPartyType(String partyType);
}

@Service
@Transactional(readOnly = true)
public class PartySummaryService {

  @Autowired
  private PartySummaryMVRepository partySummaryMVRepository;

  @Autowired
  private EntityManager entityManager;

  public List<PartySummaryMV> getAllSummaries() {
    return partySummaryMVRepository.findAll();
  }

  @Transactional  // リフレッシュは書き込み操作なのでトランザクションが必要
  public void refreshSummary() {
    entityManager.createNativeQuery(
        "REFRESH MATERIALIZED VIEW CONCURRENTLY undecided_as.mv_party_summary"
    ).executeUpdate();
  }
}
```

**特徴:**

- 読み取り操作は `@Transactional(readOnly = true)` で最適化
- リフレッシュ操作は `@Transactional` でトランザクション管理
- ネイティブクエリで `REFRESH MATERIALIZED VIEW` を実行

### 20.6 View vs Materialized View の選択基準

| 要件                     | 推奨                                      |
|--------------------------|-------------------------------------------|
| リアルタイム性が必須     | View                                      |
| 集計結果の高速参照       | Materialized View                         |
| ストレージ制約がある     | View                                      |
| 複雑な JOIN の高速化     | Materialized View                         |
| 頻繁な更新がある         | View またはトリガー付き Materialized View |
| レポート・ダッシュボード | Materialized View                         |

---

---


---

### 第5部：DB メンテナンス

## 20. TRUNCATE を使用するパターン

テーブルの全データを高速に削除する際に TRUNCATE を使用します。DELETE と違い、ROW LEVEL
のトリガーが発火せず、ロックの取得が軽量です。

### 20.1 基本構文

PostgreSQL での基本的な TRUNCATE 構文です。

```sql
-- 単一テーブルの TRUNCATE
TRUNCATE TABLE undecided_as.parties;

-- RESTART IDENTITY を付与してシーケンスをリセット
TRUNCATE TABLE undecided_as.parties RESTART IDENTITY;

-- CASCADE を付与して関連テーブルも連動削除
TRUNCATE TABLE undecided_as.parties CASCADE;

-- 複数テーブルを一度に TRUNCATE
TRUNCATE TABLE undecided_as.parties, undecided_as.organizations RESTART IDENTITY CASCADE;
```

**特徴:**

- `RESTART IDENTITY` を付与すると、関連するシーケンスを初期値にリセット
- `CASCADE` を付与すると、外部キーで参照しているテーブルも連動して TRUNCATE
- 複数テーブルをカンマ区切りで指定し、一度のトランザクションで実行可能
- DELETE と違い、WHERE 句は使用できない（全行削除のみ）

### 20.2 CASCADE vs RESTRICT

外部キー制約がある場合の動作を制御します。

```sql
-- RESTRICT（デフォルト）：参照元テーブルにデータがある場合はエラー
TRUNCATE TABLE undecided_as.parties RESTRICT;

-- CASCADE：参照元テーブルも連動して TRUNCATE
TRUNCATE TABLE undecided_as.parties CASCADE;
```

**動作比較:**

| オプション               | 参照元テーブルにデータがある場合 | 参照元テーブルが空の場合 |
|--------------------------|----------------------------------|--------------------------|
| `RESTRICT`（デフォルト） | エラーで中止                     | 正常終了                 |
| `CASCADE`                | 参照元も TRUNCATE して正常終了   | 正常終了                 |

**特徴:**

- デフォルトは `RESTRICT` 動作
- `CASCADE` を使用する場合は、どのテーブルが連動削除されるかを事前に確認
- 意図しないテーブルのデータ削除を防ぐため、本番環境では `RESTRICT` を推奨

### 20.3 Schema 分離パターン

プロジェクトでは Schema を分離しているため、TRUNCATE 時もスキーマ名を明示します。

```sql
-- Schema を明示した TRUNCATE
TRUNCATE TABLE undecided_as.parties RESTART IDENTITY;
TRUNCATE TABLE undecided_ef.prefectures RESTART IDENTITY;

-- 複数 Schema にまたがる TRUNCATE
TRUNCATE TABLE undecided_as.parties, undecided_as.organizations RESTART IDENTITY CASCADE;
```

**特徴:**

- スキーマ名を省略すると、`search_path` に依存するため明示することを推奨
- 複数 Schema にまたがる TRUNCATE も可能

### 20.4 Flyway マイグレーションでの TRUNCATE

データ初期化やテスト環境の準備などで、Flyway マイグレーションスクリプト内で TRUNCATE を使用します。

```sql
-- V20261025060000__truncate_and_seed_test_data.sql

-- 既存データを安全に削除
TRUNCATE TABLE undecided_as.parties RESTART IDENTITY CASCADE;

-- 初期データを投入
INSERT INTO undecided_as.parties (party_id, party_type, simple_name)
VALUES (1, 'ORG', 'テスト組織');
```

**DO ブロックを使用した安全な TRUNCATE:**

```sql
-- V20261025060001__safe_truncate_with_do.sql

DO
$$
    BEGIN
        -- テーブルが存在するか確認してから TRUNCATE
        IF EXISTS (SELECT 1
                   FROM information_schema.tables
                   WHERE table_schema = 'undecided_as'
                     AND table_name = 'parties') THEN
            TRUNCATE TABLE undecided_as.parties RESTART IDENTITY CASCADE;
        END IF;
    END
$$;
```

**特徴:**

- `DO $$ ... $$` ブロックで条件分岐が可能
- テーブル存在確認后进行することで、マイグレーションの堅牢性を向上
- 開発環境・テスト環境でのデータ初期化に有効

### 20.5 JPA からの TRUNCATE 呼び出し

JPA/JPQL からは TRUNCATE を直接使用できないため、ネイティブクエリを使用します。

```java
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class TruncateRepository {

  @PersistenceContext
  private EntityManager entityManager;

  @Transactional
  public void truncateParties() {
    entityManager.createNativeQuery(
        "TRUNCATE TABLE undecided_as.parties RESTART IDENTITY CASCADE"
    ).executeUpdate();
  }

  @Transactional
  public void truncateMultipleTables() {
    entityManager.createNativeQuery(
        "TRUNCATE TABLE undecided_as.parties, undecided_as.organizations RESTART IDENTITY CASCADE"
    ).executeUpdate();
  }
}
```

**特徴:**

- JPQL では TRUNCATE をサポートしないため、ネイティブクエリを使用
- `@Transactional` でトランザクション管理
- `executeUpdate()` で DML を実行

### 20.6 DELETE と TRUNCATE の比較

| 項目             | DELETE                   | TRUNCATE                             |
|------------------|--------------------------|--------------------------------------|
| 速度             | 行単位で削除するため遅い | テーブル全体を一度に削除するため高速 |
| WHERE 句         | 使用可能                 | 使用不可（全行削除のみ）             |
| トリガー         | ROW LEVEL トリガーが発火 | 発火しない                           |
| シーケンス       | リセットされない         | `RESTART IDENTITY` でリセット可能    |
| ロック           | 行ロック                 | テーブルロック（排他ロック）         |
| トランザクション | ロールバック可能         | ロールバック可能（PostgreSQL）       |
| 外部キー         | 参照整合性をチェック     | `CASCADE` / `RESTRICT` で制御        |

**使用シーン:**

| シーン                   | 推奨       |
|--------------------------|------------|
| テスト環境のデータ初期化 | `TRUNCATE` |
| 開発環境のクリーンアップ | `TRUNCATE` |
| 条件付き削除             | `DELETE`   |
| 監査ログが必要な削除     | `DELETE`   |
| 大量データの高速削除     | `TRUNCATE` |

---

---

## 21. REINDEX（再インデックス）を使用するパターン

インデックスの再構築が必要な場合に REINDEX
を使用します。データ的大量更新後のインデックス断片化、インデックス破損時の復旧、テーブル統計情報の更新などに有効です。

### 21.1 基本構文

PostgreSQL での基本的な REINDEX 構文です。

```sql
-- インデックスの再構築
REINDEX INDEX idx_parties_party_id;

-- テーブルの全インデックスを再構築
REINDEX TABLE undecided_as.parties;

-- Schema の全インデックスを再構築
REINDEX SCHEMA undecided_as;

-- データベースの全インデックスを再構築
REINDEX DATABASE undecided;

-- インデックス付きシーケンスの再構築
REINDEX SEQUENCE id_management.id_sequence;
```

**特徴:**

- インデックスの再構築により、断片化されたインデックスを最適化
- テーブルレベルで実行すると、そのテーブルの全インデックスを対象に再構築
- Schema レベルで実行すると、その Schema の全テーブルのインデックスを対象に再構築
- データベースレベルで実行すると、データベース内の全インデックスを対象に再構築

### 21.2 CONCURRENTLY オプション

`CONCURRENTLY` オプションを使用すると、インデックス再構築中に他の操作をブロックしません。

```sql
-- 通常のリインデックス（排他ロックを取得）
REINDEX TABLE undecided_as.parties;

-- CONCURRENTLY オプション（排他ロックを取得しない）
REINDEX CONCURRENTLY TABLE undecided_as.parties;

-- 特定インデックスの CONCURRENTLY リインデックス
REINDEX CONCURRENTLY INDEX idx_parties_party_id;
```

**リインデックス比較:**

| オプション     | ロック     | 読み取り同時実行 | 書き込み同時実行 | 速度 |
|----------------|------------|------------------|------------------|------|
| 通常           | 排他ロック | 不可             | 不可             | 高速 |
| `CONCURRENTLY` | 共有ロック | 可能             | 可能             | 低速 |

**特徴:**

- `CONCURRENTLY` を使用すると、インデックス再構築中に他の読み書きが可能
- 本番環境では `CONCURRENTLY` を推奨
- 通常のリインデックスの方が高速だが、サービス停止が必要
- `CONCURRENTLY` はトランザクションブロック内で実行できない

### 21.3 Schema 分離パターン

プロジェクトでは Schema を分離しているため、REINDEX 時もスキーマ名を明示します。

```sql
-- Schema を明示した REINDEX
REINDEX TABLE undecided_as.parties;
REINDEX TABLE undecided_ef.prefectures;

-- 特定インデックスの再構築
REINDEX INDEX undecided_as.idx_parties_party_id;
REINDEX INDEX undecided_ef.idx_prefectures_code;

-- Schema 全体の再構築
REINDEX SCHEMA undecided_as;
REINDEX SCHEMA undecided_ef;
```

**特徴:**

- スキーマ名を省略すると、`search_path` に依存するため明示することを推奨
- 事業領域ごとに Schema を分離しているため、対象 Schema を指定して実行

### 21.4 Flyway マイグレーションでの REINDEX

インデックスの再構築が必要な場合、Flyway マイグレーションスクリプト内で REINDEX を使用します。

```sql
-- V20261025080000__reindex_parties_table.sql

-- データ的大量更新後のインデックス再構築
REINDEX CONCURRENTLY TABLE undecided_as.parties;
```

**DO ブロックを使用した安全な REINDEX:**

```sql
-- V20261025080001__safe_reindex_with_do.sql

DO
$$
    DECLARE
        idx_record RECORD;
    BEGIN
        -- 指定されたテーブルのインデックスを順に再構築
        FOR idx_record IN
            SELECT indexname
            FROM pg_indexes
            WHERE schemaname = 'undecided_as'
              AND tablename = 'parties'
            LOOP
                EXECUTE format('REINDEX CONCURRENTLY INDEX undecided_as.%I', idx_record.indexname);
            END LOOP;
    END
$$;
```

**特徴:**

- `DO $$ ... $$` ブロックで動的な SQL 実行が可能
- `pg_indexes` システムカタログからインデックス一覧を取得
- `format()` 関数で SQL を動的に生成
- 本番環境では `CONCURRENTLY` を使用してサービス停止を回避

### 21.5 VACUUM との組み合わせ

REINDEX と VACUUM を組み合わせて使用すると、より効果的なメンテナンスが可能です。

```sql
-- VACUUM ANALYZE の後で REINDEX を実行
VACUUM ANALYZE undecided_as.parties;
REINDEX CONCURRENTLY TABLE undecided_as.parties;

-- VACUUM FULL（テーブルの再構築）と REINDEX を組み合わせ
VACUUM FULL undecided_as.parties;
REINDEX TABLE undecided_as.parties;
```

**VACUUM との比較:**

| コマンド               | 効果                            | ロック     | 使用シーン               |
|------------------------|---------------------------------|------------|--------------------------|
| `VACUUM`               | 削除済み行の回収                | 共有ロック | 日常的なメンテナンス     |
| `VACUUM ANALYZE`       | 削除済み行の回収 + 統計情報更新 | 共有ロック | クエリプランナーの最適化 |
| `VACUUM FULL`          | テーブルの再構築                | 排他ロック | 大幅な断片化の解消       |
| `REINDEX`              | インデックスの再構築            | 排他ロック | インデックス断片化の解消 |
| `REINDEX CONCURRENTLY` | インデックスの再構築（非排他）  | 共有ロック | 本番環境でのメンテナンス |

**特徴:**

- `VACUUM ANALYZE` は統計情報を更新し、クエリプランナーの最適化に寄与
- `VACUUM FULL` はテーブルを再構築し、ディスク領域を回収
- `REINDEX` はインデックスのみを再構築
- 本番環境では `VACUUM ANALYZE` + `REINDEX CONCURRENTLY` の組み合わせを推奨

### 21.6 JPA からの REINDEX 呼び出し

JPA/JPQL からは REINDEX を直接使用できないため、ネイティブクエリを使用します。

```java
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class ReindexRepository {

  @PersistenceContext
  private EntityManager entityManager;

  @Transactional
  public void reindexTable(String schema, String table) {
    entityManager.createNativeQuery(
        String.format("REINDEX CONCURRENTLY TABLE %s.%s", schema, table)
    ).executeUpdate();
  }

  @Transactional
  public void reindexIndex(String schema, String indexName) {
    entityManager.createNativeQuery(
        String.format("REINDEX CONCURRENTLY INDEX %s.%s", schema, indexName)
    ).executeUpdate();
  }

  @Transactional
  public void vacuumAnalyzeAndReindex(String schema, String table) {
    entityManager.createNativeQuery(
        String.format("VACUUM ANALYZE %s.%s", schema, table)
    ).executeUpdate();
    entityManager.createNativeQuery(
        String.format("REINDEX CONCURRENTLY TABLE %s.%s", schema, table)
    ).executeUpdate();
  }
}
```

**特徴:**

- JPQL では REINDEX をサポートしないため、ネイティブクエリを使用
- `@Transactional` でトランザクション管理
- `executeUpdate()` で DDL を実行
- SQL インジェクションを防ぐため、スキーマ名とテーブル名はバリデーション后进行

### 21.7 インデックス破損時の復旧

インデックスが破損した場合の復旧手順です。

```sql
-- インデックスの破損確認
SELECT indexrelid, indrelid, indisvalid
FROM pg_index
WHERE NOT indisvalid;

-- 破損したインデックスの再構築
REINDEX INDEX idx_parties_party_id;

-- 破損が解消されない場合、インデックスの再作成
DROP INDEX idx_parties_party_id;
CREATE INDEX idx_parties_party_id ON undecided_as.parties (party_id);
```

**特徴:**

- `pg_index` システムカタログの `indisvalid` カラムでインデックスの有効性を確認
- `REINDEX` で破損したインデックスを再構築
- 破損が解消されない場合は、インデックスを再作成

### 21.8 使用シーン

| シーン                 | 推奨                                      |
|------------------------|-------------------------------------------|
| データ的大量更新後     | `REINDEX CONCURRENTLY`                    |
| インデックス破損時     | `REINDEX`（通常）                         |
| 本番環境のメンテナンス | `REINDEX CONCURRENTLY` + `VACUUM ANALYZE` |
| 開発環境のメンテナンス | `REINDEX`（通常）                         |
| テーブルの大幅な断片化 | `VACUUM FULL` + `REINDEX`                 |
| 定期的なメンテナンス   | `VACUUM ANALYZE` + `REINDEX CONCURRENTLY` |

---

## 22. 履歴テーブルを使用するパターン

履歴テーブルは、データの変更履歴を追跡するために使用されます。プロジェクトでは `address_use_history`
のように、業務ロジックで明示的に履歴を記録するパターンと、トリガーや JPA リスナーによる自動履歴管理のパターンがあります。

### 22.1 履歴テーブルの命名規約

**履歴テーブルのテーブル名は、必ず `_history` サフィックスを付けること。**

| 項目             | 規則                                  |
|------------------|---------------------------------------|
| **サフィックス** | `_history`                            |
| **命名形式**     | `{元のテーブル名}_history`            |
| **例**           | `parties` → `parties_history`         |
| **例**           | `address_use` → `address_use_history` |
| **例**           | `employees` → `employees_history`     |

**命名規約の理由:**

1. **一目で履歴テーブルと識別可能** — テーブル名からその用途が明確になる
2. **スキーマ探索時の見やすさ** — `_history` でフィルタリングして履歴テーブルを一覧可能
3. **バックアップ/アーカイブとの区別** — `_backup` や `_archive` と混同しない
4. **SCD テーブルとの区別** — SCD Type 2 パターンは `_scd` サフィックスを使用

**命名例の比較:**

| テーブル種別      | 命名例               | サフィックス         |
|-------------------|----------------------|----------------------|
| 本体テーブル      | `parties`            | —                    |
| 履歴テーブル      | `parties_history`    | `_history`           |
| SCD テーブル      | `parties_scd`        | `_scd`               |
| View              | `v_parties_summary`  | `v_` プレフィックス  |
| Materialized View | `mv_parties_summary` | `mv_` プレフィックス |

**Flyway DDL での命名例:**

```sql
-- 履歴テーブルの作成（_history サフィックス必須）
CREATE TABLE undecided_as.parties_history
(
    history_id  BIGINT      NOT NULL,
    party_id    BIGINT      NOT NULL,
    changed_at  TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    changed_by  VARCHAR(50),
    change_type VARCHAR(10) NOT NULL,
    PRIMARY KEY (history_id)
);

COMMENT ON TABLE undecided_as.parties_history IS 'Party の変更履歴を記録するテーブル';
```

**プロジェクトの実例:**

```sql
-- V20240629083504__add_adress_use_history_table.sql
-- 住所使用履歴テーブル（_history サフィックス付き）
CREATE TABLE undecided_as.address_use_history
(
    address_use_id      BIGINT      not null,
    party_id            BIGINT      not null,
    address_Id          BIGINT      not null,
    begin_date          date        not null,
    end_date            date        not null,
    type                varchar(20) not null,
    reason_for_transfer varchar(30) not null,
    primary key (address_use_id)
);
```

**特徴:**

- `address_use` テーブルの履歴を `address_use_history` で管理
- テーブル名から履歴テーブルであることが一目で識別可能
- `_history` サフィックス規約に従った命名

### 22.2 基本概念

履歴テーブルには主に2つのアプローチがあります。

| アプローチ     | 説明                                   | 特徴                               |
|----------------|----------------------------------------|------------------------------------|
| **明示的履歴** | アプリケーション層で履歴を明示的に挿入 | 制御が容易、ビジネスロジックと統合 |
| **自動履歴**   | トリガーや JPA リスナーで自動記録      | 漏れがない、実装者は意識不要       |

**プロジェクトの実例:**

```sql
-- V20240629083504__add_adress_use_history_table.sql
-- 住所使用履歴テーブル（明示的履歴パターン）
create table undecided_as.address_use_history
(
    address_use_id      BIGINT      not null,
    party_id            BIGINT      not null,
    address_Id          BIGINT      not null,
    begin_date          date        not null,
    end_date            date        not null,
    type                varchar(20) not null,
    reason_for_transfer varchar(30) not null,
    primary key (address_use_id)
);
```

**特徴:**

- `begin_date` / `end_date` で有効期間を管理
- `type` で履歴の種類を分類
- `reason_for_transfer` で変更理由を記録
- アプリケーション層で明示的に履歴行を挿入

### 22.3 Flyway での履歴テーブル定義

履歴テーブルは Flyway マイグレーションで定義します。

#### 22.3.1 明示的履歴テーブルの定義

```sql
-- V20261025090000__create_party_history_table.sql

-- 履歴テーブルの作成
CREATE TABLE undecided_as.parties_history
(
    history_id  BIGINT      NOT NULL,
    party_id    BIGINT      NOT NULL,
    search_name VARCHAR(100),
    party_type  VARCHAR(20),
    changed_at  TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    changed_by  VARCHAR(50),
    change_type VARCHAR(10) NOT NULL, -- 'INSERT', 'UPDATE', 'DELETE'
    old_data    JSONB,                -- 変更前のデータ（オプション）
    new_data    JSONB,                -- 変更後のデータ（オプション）
    PRIMARY KEY (history_id)
);

-- コメント付与
COMMENT ON TABLE undecided_as.parties_history IS 'Party の変更履歴を記録するテーブル';
COMMENT ON COLUMN undecided_as.parties_history.history_id IS '履歴の一意な識別子';
COMMENT ON COLUMN undecided_as.parties_history.party_id IS '対象 Party の識別子';
COMMENT ON COLUMN undecided_as.parties_history.change_type IS '変更種別（INSERT/UPDATE/DELETE）';
COMMENT ON COLUMN undecided_as.parties_history.old_data IS '変更前のデータ（JSON形式）';
COMMENT ON COLUMN undecided_as.parties_history.new_data IS '変更後のデータ（JSON形式）';

-- インデックス定義
CREATE INDEX idx_parties_history_party_id ON undecided_as.parties_history (party_id);
CREATE INDEX idx_parties_history_changed_at ON undecided_as.parties_history (changed_at DESC);
```

**特徴:**

- `change_type` で変更種別を記録（INSERT/UPDATE/DELETE）
- `old_data` / `new_data` で変更前後のデータを JSONB として保存
- `changed_at` に DESC インデックスで最新履歴を高速に取得
- 主キーは分散 ID（Snowflake ID）を使用

#### 22.3.2 Slowly Changing Dimension (SCD) パターン

Slowly Changing Dimension は、データウェアハウスでよく使用される履歴管理パターンです。

```sql
-- V20261025090001__create_scd_party_table.sql

-- SCD Type 2 パターン（有効期間管理）
CREATE TABLE undecided_as.parties_scd
(
    party_id    BIGINT    NOT NULL,
    version     INTEGER   NOT NULL,
    search_name VARCHAR(100),
    party_type  VARCHAR(20),
    valid_from  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    valid_to    TIMESTAMP, -- NULL は現在有効
    is_current  BOOLEAN   NOT NULL DEFAULT TRUE,
    PRIMARY KEY (party_id, version)
);

-- コメント付与
COMMENT ON TABLE undecided_as.parties_scd IS 'Party の SCD Type 2 履歴テーブル（有効期間管理）';
COMMENT ON COLUMN undecided_as.parties_scd.version IS 'バージョン番号（単調増加）';
COMMENT ON COLUMN undecided_as.parties_scd.valid_from IS '有効開始日時';
COMMENT ON COLUMN undecided_as.parties_scd.valid_to IS '有効終了日時（NULL は現在有効）';
COMMENT ON COLUMN undecided_as.parties_scd.is_current IS '現在有効なレコードかどうか';

-- インデックス定義
CREATE INDEX idx_parties_scd_current ON undecided_as.parties_scd (party_id) WHERE is_current = TRUE;
CREATE INDEX idx_parties_scd_valid_from ON undecided_as.parties_scd (valid_from DESC);
```

**特徴:**

- `version` でバージョン管理
- `valid_from` / `valid_to` で有効期間を管理
- `is_current` で現在有効なレコードを高速に取得
- 部分インデックスで現在有効なレコードのみをインデックス化

### 22.4 JPA エンティティでの履歴マッピング

履歴テーブルを JPA エンティティとしてマッピングします。

#### 22.4.1 明示的履歴エンティティ

```java

@Entity
@Table(
    name = "parties_history",
    schema = "undecided_as",
    comment = "Party の変更履歴を記録するテーブル"
)
@Immutable  // 履歴は読み取り専用
public class PartyHistory extends BusinessEntity {

  @Id
  @Column(name = "history_id", comment = "履歴の一意な識別子")
  private HistoryId historyId;

  @Column(name = "party_id", comment = "対象 Party の識別子")
  private PartyId partyId;

  @Column(name = "search_name", comment = "検索用の名称")
  private String searchName;

  @Column(name = "party_type", comment = "Party の種別")
  private String partyType;

  @Column(name = "changed_at", comment = "変更日時")
  private LocalDateTime changedAt;

  @Column(name = "changed_by", comment = "変更者")
  private String changedBy;

  @Column(name = "change_type", comment = "変更種別")
  private ChangeType changeType;

  @Column(name = "old_data", columnDefinition = "jsonb", comment = "変更前のデータ")
  @Convert(converter = JsonbConverter.class)
  private Map<String, Object> oldData;

  @Column(name = "new_data", columnDefinition = "jsonb", comment = "変更後のデータ")
  @Convert(converter = JsonbConverter.class)
  private Map<String, Object> newData;
}
```

**特徴:**

- `@Immutable` で読み取り専用エンティティとして宣言
- 履歴は更新・削除しないため、読み取り専用に最適化
- `JsonbConverter` で JSONB カラムを Map に変換
- 分散 ID（HistoryId）を主キーとして使用

#### 22.4.2 SCD パターンのエンティティ

```java

@Entity
@Table(
    name = "parties_scd",
    schema = "undecided_as",
    comment = "Party の SCD Type 2 履歴テーブル"
)
public class PartyScd extends BusinessEntity {

  @EmbeddedId
  private PartyScdId id;  // 複合主キー (party_id, version)

  @Column(name = "search_name", comment = "検索用の名称")
  private String searchName;

  @Column(name = "party_type", comment = "Party の種別")
  private String partyType;

  @Column(name = "valid_from", comment = "有効開始日時")
  private LocalDateTime validFrom;

  @Column(name = "valid_to", comment = "有効終了日時")
  private LocalDateTime validTo;

  @Column(name = "is_current", comment = "現在有効なレコードかどうか")
  private Boolean isCurrent;

  public boolean isCurrent() {
    return Boolean.TRUE.equals(isCurrent);
  }
}
```

**複合主キー:**

```java

@Embeddable
public class PartyScdId implements ValueObject<PartyScdId> {

  @Column(name = "party_id")
  private PartyId partyId;

  @Column(name = "version")
  private Integer version;

  // equals, hashCode, コンストラクタなど
}
```

**特徴:**

- `@EmbeddedId` で複合主キーを使用
- `party_id` + `version` で一意な履歴レコードを識別
- `isCurrent` で現在有効なレコードを判別

### 22.5 履歴リポジトリパターン

履歴データにアクセスするためのリポジトリパターンです。

```java
public interface PartyHistoryRepository extends JpaRepository<PartyHistory, HistoryId> {

  /**
   * 指定した Party の履歴を新しい順に取得
   */
  List<PartyHistory> findByPartyIdOrderByChangedAtDesc(PartyId partyId);

  /**
   * 指定した Party の履歴をページネーションで取得
   */
  Page<PartyHistory> findByPartyId(PartyId partyId, Pageable pageable);

  /**
   * 指定した期間の変更履歴を取得
   */
  List<PartyHistory> findByChangedAtBetween(LocalDateTime from, LocalDateTime to);

  /**
   * 指定した変更種別の履歴を取得
   */
  List<PartyHistory> findByChangeType(ChangeType changeType);

  /**
   * 指定した Party の最新履歴を取得
   */
  @Query("SELECT p FROM PartyHistory p WHERE p.partyId = :partyId ORDER BY p.changedAt DESC")
  Optional<PartyHistory> findLatestByPartyId(@Param("partyId") PartyId partyId);
}
```

**SCD リポジトリ:**

```java
public interface PartyScdRepository extends JpaRepository<PartyScd, PartyScdId> {

  /**
   * 現在有効なレコードを取得
   */
  Optional<PartyScd> findByPartyIdAndIsCurrentTrue(PartyId partyId);

  /**
   * 指定した時点でのレコードを取得
   */
  @Query("SELECT p FROM PartyScd p WHERE p.partyId = :partyId " +
      "AND p.validFrom <= :pointInTime AND (p.validTo IS NULL OR p.validTo >= :pointInTime)")
  Optional<PartyScd> findByPartyIdAndPointInTime(
      @Param("partyId") PartyId partyId,
      @Param("pointInTime") LocalDateTime pointInTime);

  /**
   * 指定した Party の全バージョンを取得
   */
  List<PartyScd> findByPartyIdOrderByVersionAsc(PartyId partyId);
}
```

**特徴:**

- 履歴は読み取り専用なので、`save()` は使用しない
- 時間範囲クエリで特定時点のデータを取得
- SCD では `valid_from` / `valid_to` で時点データを復元

### 22.6 指定日の最終エンティティ取得パターン

履歴テーブルから「指定日の時点での最終データ」を取得するパターンです。
受注明細の商品の受注日時時点の単価や、納品予定日時点の在庫評価額など、
業務上で「ある時点でのデータ状態」を正確に復元する必要がある場面で使用します。

#### 22.6.1 業務ユースケース

| ユースケース               | 説明                                   | 必要なデータ                         |
|----------------------------|----------------------------------------|--------------------------------------|
| **受注明細の商品単価**     | 受注日時時点の商品単価を取得           | 商品マスター履歴 + 受注日時          |
| **納品予定日の在庫評価額** | 納品予定日時点の在庫数量 × 単価を取得  | 在庫履歴 + 商品単価履歴 + 納品予定日 |
| **契約締結時の顧客情報**   | 契約締結日時点の顧客住所・連絡先を取得 | 顧客マスター履歴 + 契約締結日        |
| **請求発行時の為替レート** | 請求発行日時点の為替レートを取得       | 為替レート履歴 + 請求発行日          |
| **月次レポートの期末残高** | 月末時点の口座残高を取得               | 取引履歴 + 月末日付                  |

#### 22.6.2 価格履歴テーブルの定義

商品の価格変更履歴を管理するテーブルを定義します。

```sql
-- V20261025100000__create_product_price_history_table.sql

-- 商品価格履歴テーブル（SCD Type 2 パターン）
CREATE TABLE undecided_as.product_price_history
(
    product_id BIGINT         NOT NULL,
    price      NUMERIC(12, 2) NOT NULL,
    valid_from DATE           NOT NULL,
    valid_to   DATE, -- NULL は現在有効
    PRIMARY KEY (product_id, valid_from)
);

COMMENT ON TABLE undecided_as.product_price_history IS '商品の価格変更履歴を記録するテーブル';
COMMENT ON COLUMN undecided_as.product_price_history.product_id IS '商品の識別子';
COMMENT ON COLUMN undecided_as.product_price_history.price IS '価格';
COMMENT ON COLUMN undecided_as.product_price_history.valid_from IS '価格の有効開始日';
COMMENT ON COLUMN undecided_as.product_price_history.valid_to IS '価格の有効終了日（NULL は現在有効）';

-- インデックス定義
CREATE INDEX idx_product_price_product_date
    ON undecided_as.product_price_history (product_id, valid_from DESC);
CREATE INDEX idx_product_price_valid_from
    ON undecided_as.product_price_history (valid_from);
```

**特徴:**

- `product_id` + `valid_from` を複合主キーとして使用
- `valid_to` が NULL の場合は現在有効な価格を示す
- 価格変更のたびに新しい行が追加される

#### 22.6.3 価格履歴エンティティ

```java

@Entity
@Table(
    name = "product_price_history",
    schema = "undecided_as",
    comment = "商品の価格変更履歴を記録するテーブル"
)
@Immutable
public class ProductPriceHistory {

  @EmbeddedId
  private ProductPriceHistoryId id;

  @Column(name = "price", precision = 12, scale = 2, comment = "価格")
  private BigDecimal price;

  @Column(name = "valid_from", comment = "価格の有効開始日")
  private LocalDate validFrom;

  @Column(name = "valid_to", comment = "価格の有効終了日")
  private LocalDate validTo;

  public boolean isValidAt(LocalDate date) {
    return !this.validFrom.isAfter(date)
        && (this.validTo == null || !this.validTo.isBefore(date));
  }
}

@Embeddable
public class ProductPriceHistoryId implements ValueObject<ProductPriceHistoryId> {

  @Column(name = "product_id")
  private ProductId productId;

  @Column(name = "valid_from")
  private LocalDate validFrom;

  // equals, hashCode, コンストラクタなど
}
```

**特徴:**

- `@Immutable` で読み取り専用エンティティとして宣言
- `isValidAt()` メソッドで指定日が有効期間内かどうかを判定
- 複合主キーで `product_id` + `valid_from` を一意に識別

#### 22.6.4 指定日時点の価格取得（JPQL）

```java
public interface ProductPriceHistoryRepository
    extends JpaRepository<ProductPriceHistory, ProductPriceHistoryId> {

  /**
   * 指定した商品・指定日時点の有効な価格を取得
   * 受注日時時点の単価取得に使用
   */
  @Query("SELECT p FROM ProductPriceHistory p " +
      "WHERE p.id.productId = :productId " +
      "AND p.validFrom <= :pointInDate " +
      "AND (p.validTo IS NULL OR p.validTo >= :pointInDate)")
  Optional<ProductPriceHistory> findByProductIdAndPointInDate(
      @Param("productId") ProductId productId,
      @Param("pointInDate") LocalDate pointInDate);

  /**
   * 指定した商品の価格変更履歴を日付順に取得
   */
  @Query("SELECT p FROM ProductPriceHistory p " +
      "WHERE p.id.productId = :productId " +
      "ORDER BY p.validFrom ASC")
  List<ProductPriceHistory> findByProductIdOrderByValidFromAsc(
      @Param("productId") ProductId productId);

  /**
   * 指定した日付範囲で変更された価格履歴を取得
   */
  @Query("SELECT p FROM ProductPriceHistory p " +
      "WHERE p.validFrom <= :toDate " +
      "AND (p.validTo IS NULL OR p.validTo >= :fromDate)")
  List<ProductPriceHistory> findByPriceChangedInPeriod(
      @Param("fromDate") LocalDate fromDate,
      @Param("toDate") LocalDate toDate);
}
```

**特徴:**

- `validFrom <= 指定日 AND (validTo IS NULL OR validTo >= 指定日)` で時点データを取得
- 受注日時を `pointInDate` に渡して、その時点の単価を復元
- `Optional` で価格が存在しない場合を安全に処理

#### 22.6.5 ネイティブクエリでの指定日時点取得

```java
public interface ProductPriceHistoryRepository
    extends JpaRepository<ProductPriceHistory, ProductPriceHistoryId> {

  /**
   * ネイティブクエリで指定日時点の価格を取得
   * 大量データ時のパフォーマンス最適化に有効
   */
  @Query(value = "SELECT product_id, price, valid_from, valid_to " +
      "FROM undecided_as.product_price_history " +
      "WHERE product_id = :productId " +
      "AND valid_from <= :pointInDate " +
      "AND (valid_to IS NULL OR valid_to >= :pointInDate) " +
      "ORDER BY valid_from DESC " +
      "LIMIT 1",
      nativeQuery = true)
  Optional<ProductPriceHistory> findLatestPriceAt(
      @Param("productId") ProductId productId,
      @Param("pointInDate") LocalDate pointInDate);

  /**
   * 受注明細一覧と受注日時時点の単価を結合して取得
   */
  @Query(value = "SELECT o.order_id, o.product_id, o.quantity, " +
      "pph.price AS order_time_price " +
      "FROM undecided_as.order_details o " +
      "LEFT JOIN undecided_as.product_price_history pph " +
      "  ON o.product_id = pph.product_id " +
      " AND pph.valid_from <= o.order_date " +
      " AND (pph.valid_to IS NULL OR pph.valid_to >= o.order_date) " +
      "WHERE o.order_id = :orderId",
      nativeQuery = true)
  List<OrderDetailWithPriceDto> findOrderDetailWithPriceAtOrderTime(
      @Param("orderId") OrderId orderId);
}
```

**特徴:**

- `ORDER BY valid_from DESC LIMIT 1` で最新の有効な価格を取得
- 受注明細と価格履歴を `LEFT JOIN` で結合
- 受注日時 (`o.order_date`) を基準に価格履歴をマッチング

#### 22.6.6 受注明細の商品単価取得（サービス層）

```java

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderDetailPriceService {

  private final ProductPriceHistoryRepository priceHistoryRepository;
  private final OrderDetailRepository orderDetailRepository;

  /**
   * 受注明細の商品の、受注日時時点の単価を取得
   */
  public OrderDetailPriceInfo getOrderDetailPriceAtOrderTime(OrderId orderId, ProductId productId) {
    // 受注明細を取得
    OrderDetail detail = orderDetailRepository.findById(orderId, productId)
        .orElseThrow(() -> new OrderNotFoundException(orderId));

    // 受注日時時点の単価を取得
    ProductPriceHistory priceHistory = priceHistoryRepository
        .findByProductIdAndPointInDate(productId, detail.getOrderDate())
        .orElseThrow(() -> new PriceNotFoundAtDateException(productId, detail.getOrderDate()));

    return new OrderDetailPriceInfo(
        orderId,
        productId,
        detail.getQuantity(),
        priceHistory.getPrice(),
        detail.getOrderDate()
    );
  }

  /**
   * 受注明細一覧と受注日時時点の単価を一括取得
   */
  public List<OrderDetailWithPriceDto> getOrderDetailsWithPriceAtOrderTime(OrderId orderId) {
    return priceHistoryRepository.findOrderDetailWithPriceAtOrderTime(orderId);
  }
}

@Value
public class OrderDetailPriceInfo {

  OrderId orderId;
  ProductId productId;
  int quantity;
  BigDecimal priceAtOrderTime;
  LocalDate orderDate;
}
```

**特徴:**

- 受注日時を基準に価格履歴から単価を復元
- 価格が存在しない場合は例外をスロー
- `@Transactional(readOnly = true)` で読み取り専用トランザクション

#### 22.6.7 納品予定日時点の在庫評価額計算

```java

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class InventoryValuationService {

  private final InventoryHistoryRepository inventoryHistoryRepository;
  private final ProductPriceHistoryRepository priceHistoryRepository;

  /**
   * 納品予定日時点の在庫評価額を計算
   * 在庫数量 × 納品予定日時点の単価
   */
  public InventoryValuationResult calculateValuationAtDeliveryDate(
      ProductId productId, LocalDate deliveryDate) {

    // 納品予定日時点の在庫数量を取得
    InventoryHistory inventory = inventoryHistoryRepository
        .findByProductIdAndPointInDate(productId, deliveryDate)
        .orElse(InventoryHistory.empty(productId, deliveryDate));

    // 納品予定日時点の単価を取得
    ProductPriceHistory price = priceHistoryRepository
        .findByProductIdAndPointInDate(productId, deliveryDate)
        .orElseThrow(() -> new PriceNotFoundAtDateException(productId, deliveryDate));

    // 在庫評価額 = 在庫数量 × 単価
    BigDecimal valuation = inventory.getQuantity().multiply(price.getPrice());

    return new InventoryValuationResult(
        productId,
        deliveryDate,
        inventory.getQuantity(),
        price.getPrice(),
        valuation
    );
  }

  /**
   * 納品予定日一覧の在庫評価額を一括計算
   */
  public List<InventoryValuationResult> calculateValuationForDeliverySchedule(
      List<DeliverySchedule> schedules) {

    return schedules.stream()
        .map(schedule -> calculateValuationAtDeliveryDate(
            schedule.getProductId(), schedule.getDeliveryDate()))
        .collect(Collectors.toList());
  }
}

@Value
public class InventoryValuationResult {

  ProductId productId;
  LocalDate deliveryDate;
  int quantity;
  BigDecimal unitPrice;
  BigDecimal totalValuation;
}
```

**特徴:**

- 在庫履歴と価格履歴の両方から時点データを取得
- 在庫が存在しない場合は空の在庫として処理
- 納品予定日ごとに在庫評価額を計算

#### 22.6.8 在庫履歴テーブルの定義

```sql
-- V20261025100001__create_inventory_history_table.sql

-- 在庫履歴テーブル
CREATE TABLE undecided_as.inventory_history
(
    product_id   BIGINT  NOT NULL,
    warehouse_id BIGINT  NOT NULL,
    quantity     INTEGER NOT NULL,
    valid_from   DATE    NOT NULL,
    valid_to     DATE, -- NULL は現在有効
    PRIMARY KEY (product_id, warehouse_id, valid_from)
);

COMMENT ON TABLE undecided_as.inventory_history IS '在庫数量の変更履歴を記録するテーブル';
COMMENT ON COLUMN undecided_as.inventory_history.product_id IS '商品の識別子';
COMMENT ON COLUMN undecided_as.inventory_history.warehouse_id IS '倉庫の識別子';
COMMENT ON COLUMN undecided_as.inventory_history.quantity IS '在庫数量';
COMMENT ON COLUMN undecided_as.inventory_history.valid_from IS '在庫数量の有効開始日';
COMMENT ON COLUMN undecided_as.inventory_history.valid_to IS '在庫数量の有効終了日（NULL は現在有効）';

-- インデックス定義
CREATE INDEX idx_inventory_product_warehouse_date
    ON undecided_as.inventory_history (product_id, warehouse_id, valid_from DESC);
```

**特徴:**

- `product_id` + `warehouse_id` + `valid_from` を複合主キー
- 倉庫ごとに在庫履歴を管理
- 価格履歴と同様の SCD Type 2 パターン

#### 22.6.9 複数履歴テーブルの結合クエリ

```java
public interface InventoryValuationRepository
    extends JpaRepository<InventoryHistory, InventoryHistoryId> {

  /**
   * 在庫履歴と価格履歴を結合して、指定日時点の在庫評価額を計算
   */
  @Query(value = "SELECT ih.product_id, ih.warehouse_id, ih.quantity, " +
      "pph.price AS unit_price, " +
      "(ih.quantity * pph.price) AS total_valuation " +
      "FROM undecided_as.inventory_history ih " +
      "INNER JOIN undecided_as.product_price_history pph " +
      "  ON ih.product_id = pph.product_id " +
      " AND pph.valid_from <= :pointInDate " +
      " AND (pph.valid_to IS NULL OR pph.valid_to >= :pointInDate) " +
      "WHERE ih.product_id = :productId " +
      "AND ih.warehouse_id = :warehouseId " +
      "AND ih.valid_from <= :pointInDate " +
      "AND (ih.valid_to IS NULL OR ih.valid_to >= :pointInDate) " +
      "ORDER BY ih.valid_from DESC, pph.valid_from DESC " +
      "LIMIT 1",
      nativeQuery = true)
  Optional<InventoryValuationDto> findValuationAtPointInTime(
      @Param("productId") ProductId productId,
      @Param("warehouseId") WarehouseId warehouseId,
      @Param("pointInDate") LocalDate pointInDate);

  /**
   * 倉庫全体の在庫評価額を計算
   */
  @Query(value = "SELECT ih.warehouse_id, " +
      "SUM(ih.quantity * pph.price) AS total_valuation " +
      "FROM undecided_as.inventory_history ih " +
      "INNER JOIN undecided_as.product_price_history pph " +
      "  ON ih.product_id = pph.product_id " +
      " AND pph.valid_from <= :pointInDate " +
      " AND (pph.valid_to IS NULL OR pph.valid_to >= :pointInDate) " +
      "WHERE ih.valid_from <= :pointInDate " +
      "AND (ih.valid_to IS NULL OR ih.valid_to >= :pointInDate) " +
      "GROUP BY ih.warehouse_id",
      nativeQuery = true)
  List<WarehouseValuationDto> findWarehouseValuationAtPointInTime(
      @Param("pointInDate") LocalDate pointInDate);
}
```

**特徴:**

- 在庫履歴と価格履歴を `INNER JOIN` で結合
- 両方の履歴テーブルで時点データをフィルタリング
- `SUM()` で倉庫全体の在庫評価額を集計

#### 22.6.10 指定日時点取得のパフォーマンス最適化

```sql
-- 複合インデックスで時点データ取得を高速化
CREATE INDEX idx_product_price_point_in_time
    ON undecided_as.product_price_history (product_id, valid_from DESC, valid_to)
    WHERE valid_to IS NULL OR valid_to >= CURRENT_DATE;

-- 在庫履歴の複合インデックス
CREATE INDEX idx_inventory_point_in_time
    ON undecided_as.inventory_history (product_id, warehouse_id, valid_from DESC)
    WHERE valid_to IS NULL OR valid_to >= CURRENT_DATE;
```

**特徴:**

- 部分インデックスで現在有効なデータのみをインデックス化
- `valid_from DESC` で最新のレコードを優先して取得
- 結合条件に使用されるカラムをインデックスに含める

#### 22.6.11 使用シーン

| シーン               | 推奨パターン                               |
|----------------------|--------------------------------------------|
| 受注明細の単価復元   | JPQL で `validFrom/validTo` 条件付きクエリ |
| 在庫評価額計算       | ネイティブクエリで在庫・価格履歴を結合     |
| 大量データの時点取得 | ネイティブクエリ + 複合インデックス        |
| 契約時の顧客情報復元 | SCD Type 2 パターン                        |
| 為替レート取得       | 日付キーで直接検索                         |
| 月次レポート         | バッチ処理 + ネイティブクエリ              |
| 倉庫全体の評価額     | 集計クエリ + GROUP BY                      |

---

### 22.7 トリガーによる自動履歴管理

PostgreSQL トリガーを使用して、自動的に履歴を記録するパターンです。

#### 22.7.1 トリガー関数の定義

```sql
-- V20261025090002__create_party_history_trigger.sql

-- 履歴記録用のトリガー関数
CREATE OR REPLACE FUNCTION undecided_as.fn_party_history_trigger()
    RETURNS TRIGGER AS
$$
BEGIN
    IF TG_OP = 'INSERT' THEN
        INSERT INTO undecided_as.parties_history (history_id, party_id, search_name, party_type,
                                                  changed_at, changed_by, change_type, old_data,
                                                  new_data)
        VALUES (nextval('undecided_as.history_id_seq'), -- シーケンスまたはアプリケーション側でID生成
                NEW.party_id, NEW.search_name, NEW.party_type,
                CURRENT_TIMESTAMP, current_setting('app.current_user', true),
                'INSERT', NULL, to_jsonb(NEW));
        RETURN NEW;
    ELSIF TG_OP = 'UPDATE' THEN
        INSERT INTO undecided_as.parties_history (history_id, party_id, search_name, party_type,
                                                  changed_at, changed_by, change_type, old_data,
                                                  new_data)
        VALUES (nextval('undecided_as.history_id_seq'),
                NEW.party_id, NEW.search_name, NEW.party_type,
                CURRENT_TIMESTAMP, current_setting('app.current_user', true),
                'UPDATE', to_jsonb(OLD), to_jsonb(NEW));
        RETURN NEW;
    ELSIF TG_OP = 'DELETE' THEN
        INSERT INTO undecided_as.parties_history (history_id, party_id, search_name, party_type,
                                                  changed_at, changed_by, change_type, old_data,
                                                  new_data)
        VALUES (nextval('undecided_as.history_id_seq'),
                OLD.party_id, OLD.search_name, OLD.party_type,
                CURRENT_TIMESTAMP, current_setting('app.current_user', true),
                'DELETE', to_jsonb(OLD), NULL);
        RETURN OLD;
    END IF;
    RETURN NULL;
END;
$$ LANGUAGE plpgsql;

-- トリガーの登録
CREATE TRIGGER trg_party_history
    AFTER INSERT OR UPDATE OR DELETE
    ON undecided_as.parties
    FOR EACH ROW
EXECUTE FUNCTION undecided_as.fn_party_history_trigger();
```

**特徴:**

- `TG_OP` で操作種別を判別（INSERT/UPDATE/DELETE）
- `to_jsonb()` で変更前後のデータを JSONB として保存
- `current_setting('app.current_user')` で変更者を取得
- `FOR EACH ROW` で行単位でトリガー発火

#### 22.7.2 変更者情報の設定

トリガーから変更者を取得するために、セッション変数を使用します。

```java

@Component
public class AuditSessionManager implements ApplicationListener<RequestHandledEvent> {

  @EventListener
  public void onApplicationEvent(RequestHandledEvent event) {
    // リクエスト終了時にセッション変数をクリア
    try (Connection conn = dataSource.getConnection()) {
      conn.createStatement().execute("SET app.current_user TO ''");
    } catch (SQLException e) {
      // 無視
    }
  }
}

@Aspect
@Component
public class AuditSessionAspect {

  @Before("@annotation(org.springframework.transaction.annotation.Transactional)")
  public void setAuditUser(JoinPoint joinPoint) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth != null && auth.isAuthenticated()) {
      try (Connection conn = dataSource.getConnection()) {
        conn.createStatement()
            .execute("SET app.current_user TO '" + auth.getName() + "'");
      } catch (SQLException e) {
        throw new RuntimeException(e);
      }
    }
  }
}
```

**特徴:**

- トランザクション開始時にセッション変数にユーザー名を設定
- リクエスト終了時にセッション変数をクリア
- トリガー側で `current_setting()` で変更者を取得

### 22.8 JPA リスナーによる履歴管理

JPA リスナーを使用して、アプリケーション層で自動的に履歴を記録するパターンです。

```java

@EntityListeners(PartyHistoryListener.class)
public class Party extends BusinessEntity {
  // ...
}

public class PartyHistoryListener {

  @PersistenceContext
  private EntityManager entityManager;

  @PreUpdate
  public void onPreUpdate(Party party) {
    PartyHistory history = new PartyHistory();
    history.setHistoryId(HistoryId.generate());
    history.setPartyId(party.getPartyId());
    history.setSearchName(party.getSearchName());
    history.setPartyType(party.getPartyType());
    history.setChangedAt(LocalDateTime.now());
    history.setChangedBy(party.getUpdatedBy());
    history.setChangeType(ChangeType.UPDATE);
    // oldData/newData の設定は EntityGraph や他の方法で
    entityManager.persist(history);
  }

  @PreRemove
  public void onPreRemove(Party party) {
    PartyHistory history = new PartyHistory();
    history.setHistoryId(HistoryId.generate());
    history.setPartyId(party.getPartyId());
    history.setSearchName(party.getSearchName());
    history.setPartyType(party.getPartyType());
    history.setChangedAt(LocalDateTime.now());
    history.setChangedBy(party.getUpdatedBy());
    history.setChangeType(ChangeType.DELETE);
    entityManager.persist(history);
  }
}
```

**特徴:**

- `@PreUpdate` / `@PreRemove` で変更前に履歴を記録
- トリガーと異なり、アプリケーション層で完全な制御が可能
- 変更前後のデータを取得するには追加のロジックが必要

### 22.9 履歴テーブルのインデックス設計

履歴テーブルはデータ量が膨大になる可能性があるため、適切なインデックス設計が重要です。

```sql
-- 基本インデックス
CREATE INDEX idx_parties_history_party_id ON undecided_as.parties_history (party_id);
CREATE INDEX idx_parties_history_changed_at ON undecided_as.parties_history (changed_at DESC);

-- 複合インデックス（よくあるクエリパターン用）
CREATE INDEX idx_parties_history_party_changed
    ON undecided_as.parties_history (party_id, changed_at DESC);

-- 部分インデックス（特定の変更種別のみ）
CREATE INDEX idx_parties_history_updates
    ON undecided_as.parties_history (changed_at DESC)
    WHERE change_type = 'UPDATE';

-- JSONB カラムのインデックス（GIN インデックス）
CREATE INDEX idx_parties_history_new_data_gin
    ON undecided_as.parties_history USING GIN (new_data);
```

**特徴:**

- `changed_at DESC` で最新履歴を高速に取得
- 複合インデックスでよくあるクエリパターンを最適化
- 部分インデックスで特定条件のデータを効率的に検索
- JSONB カラムには GIN インデックスを使用

### 22.10 履歴データのクリーンアップ

履歴テーブルは時間とともに膨張するため、定期的なクリーンアップが必要です。

#### 22.10.1 古い履歴の削除

```sql
-- 1年前の履歴を削除
DELETE
FROM undecided_as.parties_history
WHERE changed_at < CURRENT_TIMESTAMP - INTERVAL '1 year';

-- バッチ削除（ロックを最小限に）
DO
$$
    DECLARE
        deleted_count INTEGER;
    BEGIN
        LOOP
            DELETE
            FROM undecided_as.parties_history
            WHERE changed_at < CURRENT_TIMESTAMP - INTERVAL '1 year' LIMIT 1000;

            GET DIAGNOSTICS deleted_count = ROW_COUNT;
            EXIT WHEN deleted_count = 0;

            -- 各バッチ後にコミット
            COMMIT;
        END LOOP;
    END
$$;
```

**特徴:**

- 一度に大量削除するとロックがかかるため、バッチ処理を推奨
- `LIMIT` で削除件数を制限し、定期的にコミット
- `DO` ブロックで匿名ブロックとして実行

#### 22.10.2 履歴のパーティショニング

大規模な履歴テーブルにはパーティショニングを適用します。

```sql
-- V20261025090003__partition_party_history.sql

-- パーティション付き履歴テーブル
CREATE TABLE undecided_as.parties_history_partitioned
(
    history_id  BIGINT      NOT NULL,
    party_id    BIGINT      NOT NULL,
    search_name VARCHAR(100),
    party_type  VARCHAR(20),
    changed_at  TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    changed_by  VARCHAR(50),
    change_type VARCHAR(10) NOT NULL,
    old_data    JSONB,
    new_data    JSONB,
    PRIMARY KEY (history_id, changed_at) -- パーティションキーを含める
) PARTITION BY RANGE (changed_at);

-- 月単位でパーティション分割
CREATE TABLE undecided_as.parties_history_202601
    PARTITION OF undecided_as.parties_history_partitioned
        FOR VALUES FROM ('2026-01-01') TO ('2026-02-01');

CREATE TABLE undecided_as.parties_history_202602
    PARTITION OF undecided_as.parties_history_partitioned
        FOR VALUES FROM ('2026-02-01') TO ('2026-03-01');

-- 古いパーティションの削除（高速）
DROP TABLE undecided_as.parties_history_202501;
```

**特徴:**

- `PARTITION BY RANGE` で日付範囲ごとにパーティション分割
- 古いパーティションを `DROP TABLE` で高速に削除
- パーティションキーは主キーに含まれる必要がある

### 22.11 使用シーン

| シーン                       | 推奨パターン                        |
|------------------------------|-------------------------------------|
| 業務ロジックで履歴を制御     | 明示的履歴 + JPA エンティティ       |
| 変更漏れを防ぐ               | トリガーによる自動履歴              |
| 時点データ復元               | SCD Type 2 パターン                 |
| 監査証跡                     | トリガー + JSONB 保存               |
| 大規模履歴管理               | パーティショニング                  |
| アプリケーション層で完全制御 | JPA リスナー                        |
| 有効期間管理                 | SCD Type 2 + 部分インデックス       |
| 履歴検索パフォーマンス       | 複合インデックス + GIN インデックス |

---

## 23. 仕訳データの変更履歴パターン（逆仕訳とマイナス仕訳）

経理の仕訳データは、一度記録した仕訳を直接修正するのではなく、
変更時に「逆仕訳」または「マイナス仕訳」として新しい仕訳行を記録するパターンがあります。
このアプローチにより、仕訳の履歴が完全に追跡可能になり、監査証跡が保証されます。

### 23.1 基本概念

仕訳の変更履歴管理には、主に2つのアプローチがあります。

| アプローチ       | 説明                                   | 特徴                                      |
|------------------|----------------------------------------|-------------------------------------------|
| **逆仕訳**       | 元仕訳の借方・貸方を反転した仕訳を記録 | 元仕訳と逆仕訳で相殺、残高は0に帰着       |
| **マイナス仕訳** | 元仕訳と同じ科目で金額を負の値で記録   | 元仕訳とマイナス仕訳で相殺、残高は0に帰着 |

**仕訳の基本構造:**

仕訳は借方（Debit）と貸方（Credit）のペアで構成され、借方合計と貸方合計が常に等しくなります。

| 項目           | 説明                                     |
|----------------|------------------------------------------|
| **仕訳ID**     | 仕訳を一意に識別するID                   |
| **仕訳日付**   | 仕訳を記録した日付                       |
| **科目コード** | 会計科目の識別子                         |
| **借方金額**   | Debit 金額（正の値）                     |
| **貸方金額**   | Credit 金額（正の値）                    |
| **摘要**       | 仕訳の説明                               |
| **元仕訳ID**   | 逆仕訳・マイナス仕訳が参照する元仕訳のID |
| **仕訳種別**   | 通常仕訳 / 逆仕訳 / マイナス仕訳         |

### 23.2 逆仕訳パターン

逆仕訳は、元仕訳の借方・貸方を反転して新しい仕訳行を記録するパターンです。

#### 23.2.1 逆仕訳の概念

元仕訳が「現金 10,000（借方） / 売上高 10,000（貸方）」の場合、
逆仕訳は「売上高 10,000（借方） / 現金 10,000（貸方）」となります。

| 仕訳種別   | 借方科目 | 借方金額 | 貸方科目 | 貸方金額 |
|------------|----------|----------|----------|----------|
| **元仕訳** | 現金     | 10,000   | 売上高   | 10,000   |
| **逆仕訳** | 売上高   | 10,000   | 現金     | 10,000   |

**逆仕訳の特徴:**

- 元仕訳と逆仕訳を合計すると、各科目の残高は0になる
- 借方・貸方の構造が保たれるため、会計処理の整合性が明確
- 監査時に「何を変更したか」が一目で理解可能

#### 23.2.2 Flyway DDL（仕訳テーブル）

```sql
-- V20261025110000__create_journal_entries_table.sql

-- 仕訳テーブル
CREATE TABLE undecided_do.journal_entries
(
    entry_id          BIGINT         NOT NULL,
    entry_date        DATE           NOT NULL,
    account_code      VARCHAR(10)    NOT NULL,
    debit_amount      NUMERIC(18, 2) NOT NULL DEFAULT 0,
    credit_amount     NUMERIC(18, 2) NOT NULL DEFAULT 0,
    description       VARCHAR(256),
    original_entry_id BIGINT,
    entry_type        VARCHAR(10)    NOT NULL DEFAULT 'NORMAL',
    dataset_id        VARCHAR(128)   NOT NULL,
    created_at        TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by        VARCHAR(50),
    PRIMARY KEY (entry_id)
);

COMMENT ON TABLE undecided_do.journal_entries IS '仕訳データを記録するテーブル';
COMMENT ON COLUMN undecided_do.journal_entries.entry_id IS '仕訳の識別子';
COMMENT ON COLUMN undecided_do.journal_entries.entry_date IS '仕訳日付';
COMMENT ON COLUMN undecided_do.journal_entries.account_code IS '会計科目コード';
COMMENT ON COLUMN undecided_do.journal_entries.debit_amount IS '借方金額（正の値）';
COMMENT ON COLUMN undecided_do.journal_entries.credit_amount IS '貸方金額（正の値）';
COMMENT ON COLUMN undecided_do.journal_entries.description IS '摘要（仕訳の説明）';
COMMENT ON COLUMN undecided_do.journal_entries.original_entry_id IS '元仕訳のID（逆仕訳・マイナス仕訳の場合）';
COMMENT ON COLUMN undecided_do.journal_entries.entry_type IS '仕訳種別（NORMAL, REVERSAL, NEGATIVE）';

-- インデックス定義
CREATE INDEX idx_journal_entry_date
    ON undecided_do.journal_entries (entry_date);

CREATE INDEX idx_journal_account_date
    ON undecided_do.journal_entries (account_code, entry_date);

CREATE INDEX idx_journal_original_entry
    ON undecided_do.journal_entries (original_entry_id)
    WHERE original_entry_id IS NOT NULL;

CREATE INDEX idx_journal_entry_type
    ON undecided_do.journal_entries (entry_type);
```

**特徴:**

- `debit_amount` と `credit_amount` は常に正の値
- `original_entry_id` で元仕訳を参照（逆仕訳・マイナス仕訳の場合）
- `entry_type` で仕訳種別を識別（NORMAL, REVERSAL, NEGATIVE）
- `original_entry_id` に部分インデックスで逆仕訳・マイナス仕訳のみを高速検索

#### 23.2.3 仕訳エンティティ

```java

@Getter
@Setter
@Entity
@Table(schema = "undecided_do", name = "journal_entries", comment = "仕訳データを記録するテーブル")
public class JournalEntry extends BusinessEntity {

  @Id
  @Column(name = "entry_id", comment = "仕訳の識別子", nullable = false)
  private EntryId entryId;

  @NotNull
  @Column(name = "entry_date", comment = "仕訳日付", nullable = false)
  private LocalDate entryDate;

  @NotNull
  @Size(min = 1, max = 10)
  @Column(name = "account_code", comment = "会計科目コード", nullable = false, length = 10)
  private AccountCode accountCode;

  @NotNull
  @Column(name = "debit_amount", comment = "借方金額（正の値）", nullable = false, precision = 18, scale = 2)
  private BigDecimal debitAmount;

  @NotNull
  @Column(name = "credit_amount", comment = "貸方金額（正の値）", nullable = false, precision = 18, scale = 2)
  private BigDecimal creditAmount;

  @Size(max = 256)
  @Column(name = "description", comment = "摘要（仕訳の説明）", length = 256)
  private String description;

  @Column(name = "original_entry_id", comment = "元仕訳のID（逆仕訳・マイナス仕訳の場合）")
  private EntryId originalEntryId;

  @NotNull
  @Enumerated(EnumType.STRING)
  @Column(name = "entry_type", comment = "仕訳種別", nullable = false, length = 10)
  private EntryType entryType;

  @NotNull
  @Size(max = 128)
  @Column(name = "dataset_id", comment = "データセットID", nullable = false, length = 128)
  private String datasetId;

  /**
   * 逆仕訳を生成
   */
  public JournalEntry createReversal() {
    JournalEntry reversal = new JournalEntry();
    reversal.setEntryId(EntryId.generate());
    reversal.setEntryDate(LocalDate.now());
    reversal.setAccountCode(this.accountCode);
    // 借方・貸方を反転
    reversal.setDebitAmount(this.creditAmount);
    reversal.setCreditAmount(this.debitAmount);
    reversal.setDescription("逆仕訳: " + this.description);
    reversal.setOriginalEntryId(this.entryId);
    reversal.setEntryType(EntryType.REVERSAL);
    reversal.setDatasetId(this.datasetId);
    return reversal;
  }

  /**
   * マイナス仕訳を生成
   */
  public JournalEntry createNegative() {
    JournalEntry negative = new JournalEntry();
    negative.setEntryId(EntryId.generate());
    negative.setEntryDate(LocalDate.now());
    negative.setAccountCode(this.accountCode);
    // 同じ科目で金額を負の値で記録
    if (this.debitAmount.compareTo(BigDecimal.ZERO) > 0) {
      negative.setDebitAmount(this.debitAmount.negate());
      negative.setCreditAmount(BigDecimal.ZERO);
    } else {
      negative.setDebitAmount(BigDecimal.ZERO);
      negative.setCreditAmount(this.creditAmount.negate());
    }
    negative.setDescription("マイナス仕訳: " + this.description);
    negative.setOriginalEntryId(this.entryId);
    negative.setEntryType(EntryType.NEGATIVE);
    negative.setDatasetId(this.datasetId);
    return negative;
  }

  /**
   * 純金額を計算（借方 - 貸方）
   */
  public BigDecimal netAmount() {
    return this.debitAmount.subtract(this.creditAmount);
  }
}

/** 仕訳種別 */
public enum EntryType {
  /** 通常仕訳 */
  NORMAL,
  /** 逆仕訳（借方・貸方を反転） */
  REVERSAL,
  /** マイナス仕訳（金額を負の値で記録） */
  NEGATIVE
}
```

**特徴:**

- `createReversal()` で借方・貸方を反転した逆仕訳を生成
- `createNegative()` で同じ科目に負の金額を記録するマイナス仕訳を生成
- `netAmount()` で借方 - 貸方の純金額を計算
- `originalEntryId` で元仕訳を参照

### 23.3 マイナス仕訳パターン

マイナス仕訳は、元仕訳と同じ科目で金額を負の値として記録するパターンです。

#### 23.3.1 マイナス仕訳の概念

元仕訳が「現金 10,000（借方） / 売上高 10,000（貸方）」の場合、
マイナス仕訳は「現金 -10,000（借方） / 売上高 -10,000（貸方）」となります。

| 仕訳種別         | 借方科目 | 借方金額 | 貸方科目 | 貸方金額 |
|------------------|----------|----------|----------|----------|
| **元仕訳**       | 現金     | 10,000   | 売上高   | 10,000   |
| **マイナス仕訳** | 現金     | -10,000  | 売上高   | -10,000  |

**マイナス仕訳の特徴:**

- 科目は元仕訳と同じまま、金額を負の値で記録
- 借方・貸方の構造は維持されるが、金額の符号が反転
- 元仕訳とマイナス仕訳を合計すると、各科目の残高は0になる
- 逆仕訳と異なり、科目の借方・貸方の意味が保たれる

#### 23.3.2 逆仕訳とマイナス仕訳の比較

| 比較項目             | 逆仕訳                   | マイナス仕訳             |
|----------------------|--------------------------|--------------------------|
| **科目**             | 借方・貸方を反転         | 元仕訳と同じ             |
| **金額**             | 常に正の値               | 負の値                   |
| **会計処理の整合性** | 借方・貸方の構造が反転   | 借方・貸方の構造は維持   |
| **監査のしやすさ**   | 元仕訳との対応が明確     | 金額の符号で判別         |
| **残高計算**         | 借方・貸方を合計して相殺 | 金額の符号を考慮して合計 |
| **使用シーン**       | 正式な取消し処理         | 修正・調整処理           |
| **税法上の扱い**     | 取消しとして明確         | 修正として処理           |

### 23.4 仕訳リポジトリパターン

仕訳データにアクセスするためのリポジトリパターンです。

#### 23.4.1 基本リポジトリ

```java

@Repository
public interface JournalEntryRepository
    extends JpaRepository<JournalEntry, EntryId> {

  /**
   * 仕訳日付範囲で取得
   */
  List<JournalEntry> findByEntryDateBetween(LocalDate startDate, LocalDate endDate);

  /**
   * 科目コードと仕訳日付範囲で取得
   */
  List<JournalEntry> findByAccountCodeAndEntryDateBetween(
      AccountCode accountCode, LocalDate startDate, LocalDate endDate);

  /**
   * 元仕訳IDで逆仕訳・マイナス仕訳を取得
   */
  List<JournalEntry> findByOriginalEntryId(EntryId originalEntryId);

  /**
   * 仕訳種別で取得
   */
  List<JournalEntry> findByEntryType(EntryType entryType);

  /**
   * 科目コードの期間合計を計算（借方合計）
   */
  @Query("SELECT SUM(j.debitAmount) FROM JournalEntry j " +
      "WHERE j.accountCode = :accountCode " +
      "AND j.entryDate BETWEEN :startDate AND :endDate")
  Optional<BigDecimal> sumDebitByAccountAndDateRange(
      @Param("accountCode") AccountCode accountCode,
      @Param("startDate") LocalDate startDate,
      @Param("endDate") LocalDate endDate);

  /**
   * 科目コードの期間合計を計算（貸方合計）
   */
  @Query("SELECT SUM(j.creditAmount) FROM JournalEntry j " +
      "WHERE j.accountCode = :accountCode " +
      "AND j.entryDate BETWEEN :startDate AND :endDate")
  Optional<BigDecimal> sumCreditByAccountAndDateRange(
      @Param("accountCode") AccountCode accountCode,
      @Param("startDate") LocalDate startDate,
      @Param("endDate") LocalDate endDate);
}
```

**特徴:**

- 仕訳日付範囲、科目コード、仕訳種別で検索
- 元仕訳IDで関連する逆仕訳・マイナス仕訳を取得
- 借方合計・貸方合計を JPQL で集計

#### 23.4.2 元帳集計クエリ

```java

@Repository
public interface JournalEntryRepository
    extends JpaRepository<JournalEntry, EntryId> {

  /**
   * 科目別元帳集計（借方・貸方・残高）
   */
  @Query(value = "SELECT account_code, " +
      "SUM(debit_amount) AS total_debit, " +
      "SUM(credit_amount) AS total_credit, " +
      "SUM(debit_amount) - SUM(credit_amount) AS balance " +
      "FROM undecided_do.journal_entries " +
      "WHERE entry_date BETWEEN :startDate AND :endDate " +
      "GROUP BY account_code " +
      "ORDER BY account_code",
      nativeQuery = true)
  List<AccountLedgerDto> findAccountLedger(
      @Param("startDate") LocalDate startDate,
      @Param("endDate") LocalDate endDate);

  /**
   * 逆仕訳・マイナス仕訳を含む科目別残高計算
   * （マイナス仕訳の負の金額を考慮）
   */
  @Query(value = "SELECT account_code, " +
      "SUM(CASE WHEN entry_type = 'NEGATIVE' " +
      "THEN -debit_amount ELSE debit_amount END) AS net_debit, " +
      "SUM(CASE WHEN entry_type = 'NEGATIVE' " +
      "THEN -credit_amount ELSE credit_amount END) AS net_credit " +
      "FROM undecided_do.journal_entries " +
      "WHERE entry_date BETWEEN :startDate AND :endDate " +
      "GROUP BY account_code " +
      "ORDER BY account_code",
      nativeQuery = true)
  List<AccountBalanceDto> findAccountBalanceWithNegatives(
      @Param("startDate") LocalDate startDate,
      @Param("endDate") LocalDate endDate);

  /**
   * 元仕訳と関連する逆仕訳・マイナス仕訳を結合して取得
   */
  @Query(value = "SELECT o.entry_id AS original_id, o.entry_date AS original_date, " +
      "o.account_code, o.debit_amount AS original_debit, o.credit_amount AS original_credit, " +
      "r.entry_id AS reversal_id, r.entry_type, " +
      "r.debit_amount AS reversal_debit, r.credit_amount AS reversal_credit " +
      "FROM undecided_do.journal_entries o " +
      "LEFT JOIN undecided_do.journal_entries r " +
      "  ON r.original_entry_id = o.entry_id " +
      "WHERE o.entry_type = 'NORMAL' " +
      "AND o.entry_date BETWEEN :startDate AND :endDate " +
      "ORDER BY o.entry_date DESC, o.entry_id DESC",
      nativeQuery = true)
  List<JournalReversalDto> findJournalWithReversals(
      @Param("startDate") LocalDate startDate,
      @Param("endDate") LocalDate endDate);
}
```

**特徴:**

- 科目別元帳集計で借方・貸方・残高を計算
- マイナス仕訳の負の金額を `CASE WHEN` で考慮
- 元仕訳と逆仕訳・マイナス仕訳を `LEFT JOIN` で結合

### 23.5 サービス層での仕訳処理

仕訳の記録と取消しをサービス層で管理します。

#### 23.5.1 逆仕訳による取消し

```java

@Service
@Transactional
public class JournalEntryService {

  private final JournalEntryRepository journalEntryRepository;

  /**
   * 逆仕訳で仕訳を取消す
   */
  public JournalEntry reverseEntry(EntryId entryId) {
    // 元仕訳を取得
    JournalEntry original = journalEntryRepository.findById(entryId)
        .orElseThrow(() -> new IllegalArgumentException("仕訳が見つかりません: " + entryId));

    // 既に取消されているか確認
    List<JournalEntry> existingReversals =
        journalEntryRepository.findByOriginalEntryId(entryId);
    if (!existingReversals.isEmpty()) {
      throw new IllegalStateException("既に取消されています: " + entryId);
    }

    // 逆仕訳を生成（借方・貸方を反転）
    JournalEntry reversal = original.createReversal();
    return journalEntryRepository.save(reversal);
  }

  /**
   * マイナス仕訳で仕訳を修正
   */
  public JournalEntry negativeEntry(EntryId entryId) {
    // 元仕訳を取得
    JournalEntry original = journalEntryRepository.findById(entryId)
        .orElseThrow(() -> new IllegalArgumentException("仕訳が見つかりません: " + entryId));

    // マイナス仕訳を生成（金額を負の値で記録）
    JournalEntry negative = original.createNegative();
    return journalEntryRepository.save(negative);
  }

  /**
   * 仕訳を修正（逆仕訳 + 新仕訳）
   */
  public List<JournalEntry> correctEntry(EntryId entryId, JournalEntry newEntry) {
    // 1. 元仕訳を逆仕訳で取消す
    JournalEntry reversal = reverseEntry(entryId);

    // 2. 正しい仕訳を記録
    JournalEntry saved = journalEntryRepository.save(newEntry);

    return List.of(reversal, saved);
  }

  /**
   * 科目別残高を計算
   */
  @Transactional(readOnly = true)
  public Map<AccountCode, BigDecimal> calculateAccountBalances(
      LocalDate startDate, LocalDate endDate) {
    List<AccountLedgerDto> ledger =
        journalEntryRepository.findAccountLedger(startDate, endDate);

    return ledger.stream()
        .collect(Collectors.toMap(
            AccountLedgerDto::getAccountCode,
            dto -> dto.getTotalDebit().subtract(dto.getTotalCredit())
        ));
  }
}
```

**特徴:**

- `reverseEntry()` で逆仕訳を生成して取消し
- `negativeEntry()` でマイナス仕訳を生成して修正
- `correctEntry()` で逆仕訳 + 新仕訳の2ステップ修正
- 既に取消されている仕訳の再取消しを防止

#### 23.5.2 仕訳の整合性検証

```java

@Service
public class JournalEntryValidator {

  /**
   * 仕訳の借方・貸方の整合性を検証
   */
  public void validateJournalEntry(JournalEntry entry) {
    // 借方と貸方のどちらかが0でなければならない
    if (entry.getDebitAmount().compareTo(BigDecimal.ZERO) > 0
        && entry.getCreditAmount().compareTo(BigDecimal.ZERO) > 0) {
      throw new IllegalArgumentException(
          "仕訳は借方または貸方のどちらかのみを設定してください");
    }

    // 逆仕訳の場合は借方・貸方が反転されていることを確認
    if (entry.getEntryType() == EntryType.REVERSAL && entry.getOriginalEntryId() != null) {
      // 元仕訳を取得して検証
      // ...
    }

    // マイナス仕訳の場合は金額が負であることを確認
    if (entry.getEntryType() == EntryType.NEGATIVE) {
      if (entry.getDebitAmount().compareTo(BigDecimal.ZERO) >= 0
          && entry.getCreditAmount().compareTo(BigDecimal.ZERO) >= 0) {
        throw new IllegalArgumentException(
            "マイナス仕訳は負の金額を設定してください");
      }
    }
  }

  /**
   * 仕訳ペアの借方合計・貸方合計が等しいことを検証
   */
  public void validateJournalBalance(List<JournalEntry> entries) {
    BigDecimal totalDebit = entries.stream()
        .map(JournalEntry::getDebitAmount)
        .reduce(BigDecimal.ZERO, BigDecimal::add);

    BigDecimal totalCredit = entries.stream()
        .map(JournalEntry::getCreditAmount)
        .reduce(BigDecimal.ZERO, BigDecimal::add);

    if (totalDebit.compareTo(totalCredit) != 0) {
      throw new IllegalArgumentException(
          "仕訳の借方合計(" + totalDebit + ")と貸方合計(" + totalCredit + ")が一致しません");
    }
  }
}
```

**特徴:**

- 借方・貸方の排他性を検証
- 逆仕訳の借方・貸方反転を確認
- マイナス仕訳の負の金額を検証
- 仕訳ペアの借方合計・貸方合計の整合性を確認

### 23.6 残高計算パターン

仕訳データから科目別残高を計算するパターンです。

#### 23.6.1 時点残高計算

```java

@Service
@Transactional(readOnly = true)
public class AccountBalanceService {

  private final JournalEntryRepository journalEntryRepository;

  /**
   * 指定日時点の科目別残高を計算
   * （その日までの全仕訳を合計）
   */
  public Map<AccountCode, BigDecimal> calculateBalanceAtDate(LocalDate asOfDate) {
    // 指定日までの全仕訳を取得
    List<JournalEntry> entries =
        journalEntryRepository.findByEntryDateBefore(asOfDate.plusDays(1));

    return entries.stream()
        .collect(Collectors.groupingBy(
            JournalEntry::getAccountCode,
            Collectors.reducing(
                BigDecimal.ZERO,
                entry -> {
                  // マイナス仕訳の場合は符号を考慮
                  if (entry.getEntryType() == EntryType.NEGATIVE) {
                    return entry.getDebitAmount()
                        .subtract(entry.getCreditAmount())
                        .negate();
                  }
                  return entry.getDebitAmount()
                      .subtract(entry.getCreditAmount());
                },
                BigDecimal::add
            )
        ));
  }

  /**
   * 期間中の仕訳発生額を計算
   */
  public AccountPeriodSummary calculatePeriodSummary(
      LocalDate startDate, LocalDate endDate) {
    List<AccountLedgerDto> ledger =
        journalEntryRepository.findAccountLedger(startDate, endDate);

    BigDecimal totalDebit = ledger.stream()
        .map(AccountLedgerDto::getTotalDebit)
        .reduce(BigDecimal.ZERO, BigDecimal::add);

    BigDecimal totalCredit = ledger.stream()
        .map(AccountLedgerDto::getTotalCredit)
        .reduce(BigDecimal.ZERO, BigDecimal::add);

    return new AccountPeriodSummary(
        startDate, endDate, totalDebit, totalCredit,
        totalDebit.compareTo(totalCredit) == 0
    );
  }
}
```

**特徴:**

- 指定日までの全仕訳を合計して時点残高を計算
- マイナス仕訳の負の金額を符号反転で考慮
- 期間中の借方合計・貸方合計の整合性を確認

#### 23.6.2 元仕訳と逆仕訳の相殺計算

```java

@Service
@Transactional(readOnly = true)
public class JournalReversalService {

  private final JournalEntryRepository journalEntryRepository;

  /**
   * 元仕訳と逆仕訳・マイナス仕訳を結合して相殺後の残高を計算
   */
  public List<JournalNetBalanceDto> calculateNetBalances(
      LocalDate startDate, LocalDate endDate) {
    List<JournalReversalDto> reversals =
        journalEntryRepository.findJournalWithReversals(startDate, endDate);

    return reversals.stream()
        .map(dto -> {
          BigDecimal netDebit = dto.getOriginalDebit();
          BigDecimal netCredit = dto.getOriginalCredit();

          // 逆仕訳またはマイナス仕訳で相殺
          if (dto.getReversalId() != null) {
            if (dto.getEntryType() == EntryType.REVERSAL) {
              // 逆仕訳: 借方・貸方が反転されている
              netDebit = netDebit.subtract(dto.getReversalCredit());
              netCredit = netCredit.subtract(dto.getReversalDebit());
            } else if (dto.getEntryType() == EntryType.NEGATIVE) {
              // マイナス仕訳: 金額が負
              netDebit = netDebit.add(dto.getReversalDebit());
              netCredit = netCredit.add(dto.getReversalCredit());
            }
          }

          return new JournalNetBalanceDto(
              dto.getOriginalId(),
              dto.getAccountCode(),
              netDebit,
              netCredit
          );
        })
        .filter(dto -> dto.getNetDebit().compareTo(BigDecimal.ZERO) != 0
            || dto.getNetCredit().compareTo(BigDecimal.ZERO) != 0)
        .collect(Collectors.toList());
  }
}
```

**特徴:**

- 元仕訳と逆仕訳・マイナス仕訳を結合して相殺後の残高を計算
- 逆仕訳は借方・貸方を反転して相殺
- マイナス仕訳は負の金額を加算して相殺
- 相殺後、残高が0の仕訳はフィルタリング

### 23.7 仕訳履歴テーブルの命名規約

仕訳の履歴テーブルも `_history` サフィックス規約に従います。

```sql
-- 仕訳履歴テーブル（_history サフィックス付き）
CREATE TABLE undecided_do.journal_entries_history
(
    history_id        BIGINT         NOT NULL,
    entry_id          BIGINT         NOT NULL,
    entry_date        DATE           NOT NULL,
    account_code      VARCHAR(10)    NOT NULL,
    debit_amount      NUMERIC(18, 2) NOT NULL,
    credit_amount     NUMERIC(18, 2) NOT NULL,
    description       VARCHAR(256),
    original_entry_id BIGINT,
    entry_type        VARCHAR(10)    NOT NULL,
    changed_at        TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    changed_by        VARCHAR(50),
    change_type       VARCHAR(10)    NOT NULL,
    PRIMARY KEY (history_id)
);

COMMENT ON TABLE undecided_do.journal_entries_history
    IS '仕訳データの変更履歴を記録するテーブル';
```

**特徴:**

- `journal_entries` の履歴を `journal_entries_history` で管理
- `_history` サフィックス規約に従った命名
- 逆仕訳・マイナス仕訳も含めて全変更を追跡

### 23.8 仕訳データのパフォーマンス最適化

```sql
-- 科目別集計を高速化
CREATE INDEX idx_journal_account_date_type
    ON undecided_do.journal_entries (account_code, entry_date, entry_type);

-- 逆仕訳・マイナス仕訳の検索を高速化
CREATE INDEX idx_journal_original_entry_type
    ON undecided_do.journal_entries (original_entry_id, entry_type)
    WHERE original_entry_id IS NOT NULL;

-- 部分インデックスで通常仕訳のみを高速検索
CREATE INDEX idx_journal_normal_entries
    ON undecided_do.journal_entries (entry_date)
    WHERE entry_type = 'NORMAL';

-- Materialized View で科目別残高をキャッシュ
CREATE MATERIALIZED VIEW undecided_do.mv_account_balance
AS
SELECT account_code,
       SUM(debit_amount) - SUM(credit_amount) AS balance,
       MAX(entry_date)                        AS last_entry_date
FROM undecided_do.journal_entries
GROUP BY account_code;

CREATE UNIQUE INDEX idx_mv_account_balance
    ON undecided_do.mv_account_balance (account_code);
```

**特徴:**

- 科目・日付・仕訳種別の複合インデックスで集計クエリを高速化
- 部分インデックスで通常仕訳のみを高速検索
- Materialized View で科目別残高をキャッシュ

### 23.9 使用シーン比較

| シーン               | 推奨パターン          | 理由                           |
|----------------------|-----------------------|--------------------------------|
| **正式な取消し**     | 逆仕訳                | 借方・貸方の反転で取消しが明確 |
| **仕訳の修正**       | 逆仕訳 + 新仕訳       | 取消し後に正しい仕訳を記録     |
| **金額の微調整**     | マイナス仕訳          | 科目を変更せずに金額のみ調整   |
| **監査証跡**         | 逆仕訳 + 履歴テーブル | 取消しの理由が明確             |
| **税法上の取消し**   | 逆仕訳                | 取消し処理として明確           |
| **内部調整**         | マイナス仕訳          | 修正処理として簡潔             |
| **大量仕訳の取消し** | バッチ処理 + 逆仕訳   | トランザクションごとに逆仕訳   |
| **残高計算**         | Materialized View     | 集計結果をキャッシュ           |

---

## 24. アンチパターン

以下のアンチパターンは避けてください。

### 24.1 @GeneratedValue を使用しない

プロジェクトでは、分散 ID 生成方式を採用しているため、`@GeneratedValue` は使用しません。

**❌ 悪い例:**

```java

@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;
```

**✅ 良い例:**

```java

@Id
private SnowflakeId id;
```

### 24.2 値オブジェクトを直接データベースにマッピングしない

値オブジェクトは `@Embeddable` または `@Converter` を使用してマッピングします。

**❌ 悪い例:**

```java

@Column(name = "employee_no")
private String employeeNo;
```

**✅ 良い例:**

```java

@Embedded
@AttributeOverrides(
    @AttributeOverride(name = "value", column = @Column(name = "employee_no")))
private EmployeeNo employeeNo;
```

---

---

## 25. 参考リンク

| Issue                                                           | 説明                             |
|-----------------------------------------------------------------|----------------------------------|
| [#266](https://github.com/mizo432/modulith-learning/issues/266) | Jpa 実装サンプル (親イシュー)    |
| [#267](https://github.com/mizo432/modulith-learning/issues/267) | Entity 階層構造                  |
| [#268](https://github.com/mizo432/modulith-learning/issues/268) | 関連付けと継承戦略               |
| [#269](https://github.com/mizo432/modulith-learning/issues/269) | テーブルコメント                 |
| [#270](https://github.com/mizo432/modulith-learning/issues/270) | カラムコメント                   |
| [#271](https://github.com/mizo432/modulith-learning/issues/271) | リポジトリメソッドバリエーション |
| [#272](https://github.com/mizo432/modulith-learning/issues/272) | 監査フィールドパターン           |
