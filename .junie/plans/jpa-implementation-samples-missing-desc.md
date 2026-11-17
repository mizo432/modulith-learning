# JPA 実装サンプル - 不足デスクリプション

> GitHub Issue #266 (Jpa 実装サンプル) のサブイシュー (#267〜#272)には記載されていない、
> プロジェクト内に実装されているJPA関連のパターンを保管します。
> これらはガイドライン作成時に追加でカバーすべき項目です。

---

## 1. @Embeddable / @Embedded パターン（埋め込み可能クラス）

Issue #266 のサブイシューにはカバーされていないパターンです。

### 概要

値オブジェクトをJPAエンティティに埋め込んでマッピングするパターンです。
`@Embeddable` アノテーションを付けたクラスは、エンティティ内で `@Embedded` として使用できます。

### 実装例

#### Embeddable クラス（値オブジェクト）

| ファイル              | 説明                                 |
|-----------------------|--------------------------------------|
| `DepartmentCode.java` | 部署コードの値オブジェクト（record） |
| `EmployeeNo.java`     | 従業員番号の値オブジェクト           |
| `FullName.java`       | 氏名の値オブジェクト                 |
| `SearchName.java`     | 検索用名称の値オブジェクト           |
| `BankCode.java`       | 銀行コードの値オブジェクト（record） |
| `BranchCode.java`     | 支店コードの値オブジェクト（record） |

**例: DepartmentCode.java**

```java
@Embeddable
public record DepartmentCode(String value) {
  public static final DepartmentCode EMPTY = new DepartmentCode("");
}
```

**例: EmployeeNo.java**

```java
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EmployeeNo {
  // ...
}
```

#### Embedded の使用例

**SimpleEmployee.java**

```java
@Entity
@Table(name = "employees", schema = "relationship_mgmt")
public class SimpleEmployee {
  @Id private SnowflakeId employeeId;

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

### 特徴

- 値オブジェクトは個別のテーブルを持たず、エンティティのテーブルにカラムとして展開される
- `@AttributeOverrides` でカラム名をカスタマイズできる
- record 型と class 型の両方が使用可能

---

## 2. @Converter / AttributeConverter パターン（カスタム変換）

Issue #266 のサブイシューにはカバーされていないパターンです。

### 概要

値オブジェクトとデータベースのデータ型間の双方向変換を定義するパターンです。
`@Converter(autoApply = true)` を使用すると、グローバルに自動適用されます。

### 実装例

| ファイル                  | Converter クラス           |
|---------------------------|----------------------------|
| `GovtAssignedId.java`     | GovtAssignedIdConverter    |
| `SearchName.java` (party) | SearchNameConverter        |
| `AccountNumber.java`      | AccountNumberConverter     |
| `HoldersName.java`        | HoldersNameConverter       |
| `OrgName.java`            | OrgNameConverter           |
| `OrgUnitName.java`        | OrgUnitNameConverter       |
| `WhenAassociated.java`    | WhenAassociatedConverter   |
| `WhenDisassociated.java`  | WhenDisassociatedConverter |
| `DateOfBirth.java`        | DateOfBirthConverter       |
| `FirstName.java`          | FirstNameConverter         |
| `LastName.java`           | LastNameConverter          |
| `LegalName.java`          | LegalNameConverter         |
| `ValidInterval.java`      | ValidIntervalConverter     |
| `WhenOccured.java`        | WhenOccuredConverter       |
| `SnowflakeId.java`        | SnowflakeIdConverter       |

**例: GovtAssignedId.java 内の Converter**

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

### 共通パターン

- Converter は値オブジェクトクラス内に static nested class として定義
- `autoApply = true` でグローバルに自動適用
- 変換先は主に `String` 型

### 共通基盤クラス

| ファイル               | 説明                                                 |
|------------------------|------------------------------------------------------|
| `EnumConverter.java`   | PersistableEnum の変換基盤を提供する抽象クラス       |
| `PersistableEnum.java` | データベース値を取得するためのインターフェース       |
| `ValueObject.java`     | 値オブジェクトの共通インターフェースとユーティリティ |

---

## 3. @Query / @Modifying パターン（カスタムJPQLクエリ）

Issue #271 (リポジトリメソッドバリエーション) でカバーされていますが、
プロジェクト内の具体的な使用例が不足しています。

### 実装例

**BankRepository.java**

```java
@Modifying
@Query("DELETE FROM Bank b WHERE b.datasetId <> :datasetId")
int deleteByDatasetIdNot(@Param("datasetId") String datasetId);
```

**BranchRepository.java**

```java
@Modifying
@Query("DELETE FROM Branch b WHERE b.datasetId <> :datasetId")
int deleteByDatasetIdNot(@Param("datasetId") String datasetId);
```

**HolidayRepository.java**

```java
@Query(
    "SELECT h FROM Holiday h WHERE h.holidayDate >= :from AND h.holidayDate <= :to ORDER BY"
        + " h.holidayDate ASC")
List<Holiday> findByHolidayDateBetween(@Param("from") LocalDate from, @Param("to") LocalDate to);
```

---

## 4. @Lock パターン（ _pesimistic_ ロッキング）

Issue #266 のサブイシューにはカバーされていないパターンです。

### 実装例

**SnowflakeNodeJpaRepository.java**

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

### 特徴

- `PESSIMISTIC_WRITE` ロックを使用して、行レベルの排他制御を実現
- Snowflake ID のノード割り当てのような競合が発生するシナリオで使用

---

## 5. @Transactional パターン（トランザクション管理）

Issue #266 のサブイシューにはカバーされていないパターンです。

### 実装例

**UseBankAccountCommandService.java**

```java
@Override
@Transactional
public void use(BankAccountUseDto bankAccountUseDto) {
  // ...
}
```

**SnowflakeNodeAllocator.java**

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

---

## 6. IdValue / UUID ベースの ID パターン

Issue #266 のサブイシューにはカバーされていないパターンです。

### 実装例

| ファイル             | 説明                                        |
|----------------------|---------------------------------------------|
| `IdValue.java`       | ID値オブジェクトの基底インターフェース      |
| `UuidValue.java`     | UUIDベースの値オブジェクト                  |
| `UuidV7IdValue.java` | UUID v7 (タイムスタンプ順) の値オブジェクト |
| `SnowflakeId.java`   | Snowflake ID の値オブジェクト               |

**特徴:**

- プロジェクトでは `@GeneratedValue` を使用せず、アプリケーション側でIDを生成
- Snowflake ID、UUID v7 などの分散ID生成方式を採用
- ID も値オブジェクトとして定義され、`@Converter` で変換

---

## 7. InheritanceType.TABLE_PER_CLASS パターン

Issue #268 で言及されていますが、具体的なサブクラスの実装例が不足しています。

### 実装例

**Party.java (抽象基底クラス)**

```java
@Entity
@Table(name = "party", schema = SchemaRelationshipMgmt.NAME)
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Party {
  @Id private SnowflakeId partyId;
  private GovtAssignedId govtAssignedId;
  private SearchName searchName;
}
```

**Person.java (サブクラス)**

```java
@Entity
@Table(name = "person", schema = SchemaRelationshipMgmt.NAME)
public class Person extends Party {
  // 固有のフィールド
}
```

**Organization.java (サブクラス)**

```java
@Entity
@Table(name = "organizations")
public class Organization {
  // 固有のフィールド
}
```

---

## 8. Schema 分離パターン

Issue #266 のサブイシューにはカバーされていないパターンです。

### 実装例

| スキーマ            | 用途               | 例                                      |
|---------------------|--------------------|-----------------------------------------|
| `undecided_ef`      | 汎用マスターデータ | Prefecture, City, Bank, Branch, Holiday |
| `relationship_mgmt` | 関係管理データ     | Party, Person, Organization, Role       |

**例:**

```java
@Entity
@Table(schema = "undecided_ef", name = "prefectures", comment = "prefecture table")
public class Prefecture { }

@Entity
@Table(name = "party", schema = SchemaRelationshipMgmt.NAME)
public abstract class Party { }
```

---

## 9. 既存ガイドラインの状態

- `.junie/` ディレクトリは存在するが、中身は空
- JPA関連のガイドラインは未作成
- 上記のパターン全てをカバーするガイドラインの新規作成が必要

---

## まとめ：ガイドライン作成でカバーすべき項目

| #  | パターン                             | Issueカバー状況 | 優先度 |
|----|--------------------------------------|-----------------|--------|
| 1  | Entity 階層構造 (@MappedSuperclass)  | #267 ✓         | 高     |
| 2  | 関連付け (@OneToMany, @ManyToOne)    | #268 ✓         | 高     |
| 3  | 継承戦略 (@Inheritance)              | #268 ✓         | 高     |
| 4  | テーブルコメント (@Table.comment)    | #269 ✓         | 中     |
| 5  | カラムコメント (@Column.comment)     | #270 ✓         | 中     |
| 6  | リポジトリバリエーション             | #271 ✓         | 高     |
| 7  | 監査フィールドパターン               | #272 ✓         | 高     |
| 8  | **@Embeddable / @Embedded**          | **未カバー**    | **高** |
| 9  | **@Converter / AttributeConverter**  | **未カバー**    | **高** |
| 10 | **@Query / @Modifying (JPQL)**       | **部分的**      | **中** |
| 11 | **@Lock (_pesimistic_ ロック)**      | **未カバー**    | **中** |
| 12 | **@Transactional**                   | **未カバー**    | **中** |
| 13 | **IdValue / 分散ID生成**             | **未カバー**    | **高** |
| 14 | **Schema 分離**                      | **未カバー**    | **低** |
| 15 | **ValueObject 共通インターフェース** | **未カバー**    | **中** |
