# Query Usecase Development Guide (`query-usecase-dev`)

このスキルは、 **modulith-learning**リポジトリにおいて、 **Queryユースケース**
（データ読み取り処理）を開発・修正・テストする際に適用するガイドラインです。

CQRSパターンに従い、Queryは **読み取り操作**（Read）のみを担当し、状態を変更しません。値を返すことが基本です。

---

### 1. いつこのスキルを使用するか (When To Use)

- Queryユースケース（データ読み取り処理）を新規作成または修正するとき
- `*Query`, `*QueryImpl`, `*QueryApi` クラスを開発するとき
- 読み取り操作の単体テスト・統合テストを作成するとき
- リポジトリからのデータ取得と変換ロジックを実装するとき
- 検索条件やフィルタリング処理を組み込むとき

---

### 2. Queryユースケースの基本原則

#### 2.1 CQRSにおけるQueryの役割

- **Query = データ読み取り**: データの検索、取得、一覧表示を担当
- **値の返却**: Queryメソッドはドメインエンティティ、DTO、Optional、Listなどを返す
- **状態不変**: Queryは副作用を持たず、データを変更しない
- **リポジトリ連携**: リポジトリからデータを取得し、必要に応じて変換する

#### 2.2 Queryの層構成

1. **SPI層（公開API）**:
    - 他モジュールから利用されるQueryインターフェースを配置
    - 例: `undecided.association.relationshipMgmt.spi.party.person.PersonQuery`
2. **Queryインターフェース**:
    - 読み取り操作の契約を定義（実装の詳細を隠蔽）
    - 例: `DepartmentQuery` → `findByCode()`, `findAll()`
3. **Query実装（`*QueryImpl`）**:
    - 実際のデータ取得ロジックとリポジトリ連携を実装
    - 例: `DepartmentQueryImpl`
4. **Query API（`*QueryApi`）**:
    - RESTコントローラーとしてGETエンドポイントを公開
    - 例: `DepartmentQueryApi`

#### 2.3 Queryインターフェースの命名規則

- インターフェース: `*Query`（例: `DepartmentQuery`, `UserQuery`）
- 実装クラス: `*QueryImpl`（例: `DepartmentQueryImpl`, `UserQueryImpl`）
- APIコントローラー: `*QueryApi`（例: `DepartmentQueryApi`, `UserQueryApi`）
- SPIパッケージ配下: `*Query`（例: `PersonQuery`）

---

### 3. Query実装の作成手順

#### 3.1 Queryインターフェースの定義

```java
package undecided.association.relationshipMgmt.orgRole.internal.department;

import java.util.List;
import java.util.Optional;
import undecided.association.relationshipMgmt.orgRole.spi.Department;
import undecided.association.relationshipMgmt.orgRole.spi.DepartmentCode;

public interface DepartmentQuery {

  Optional<Department> findByCode(DepartmentCode departmentCode);

  List<Department> findAll();
}
```

**ポイント**:

- メソッドは値を返す（`Optional`, `List`, ドメインエンティティ等）
- 引数には検索条件となる値オブジェクトを使用
- メソッド名は検索の意図を明確に（`findBy*`, `findAll`, `findBetween`等）

#### 3.2 Query実装クラスの作成

```java
package undecided.association.relationshipMgmt.orgRole.internal.department;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import undecided.association.relationshipMgmt.orgRole.spi.Department;
import undecided.association.relationshipMgmt.orgRole.spi.DepartmentCode;

@Service
public class DepartmentQueryImpl implements DepartmentQuery {

  @Override
  public Optional<Department> findByCode(DepartmentCode departmentCode) {
    // リポジトリからデータを取得
    return repository.findByCode(departmentCode);
  }

  @Override
  public List<Department> findAll() {
    // リポジトリから全データを取得
    return repository.findAll();
  }
}
```

**ポイント**:

- `@Service`アノテーションを付与
- 状態を変更しない読み取り処理のみを実装
- リポジトリから取得したデータをそのまま、または変換して返す
- 存在しない場合は`Optional.empty()`を返す

#### 3.3 Query APIコントローラーの作成

```java
package undecided.association.relationshipMgmt.orgRole.internal.department;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import undecided.association.relationshipMgmt.orgRole.spi.Department;
import undecided.association.relationshipMgmt.orgRole.spi.DepartmentCode;

/**
 * 部署情報の取得（Query）を担当するREST APIコントローラーです。
 *
 * <p>CQRSパターンに従い、読み取り操作のみを担当します。
 */
@RestController
@RequestMapping("/api/departments")
@RequiredArgsConstructor
public class DepartmentQueryApi {
  private final DepartmentQuery query;

  @GetMapping
  List<Department> findAll() {
    return query.findAll();
  }

  @GetMapping("/{depertmentCode}")
  Department getById(@PathVariable DepartmentCode departmentCode) {
    return query
        .findByCode(departmentCode)
        .orElseThrow(() -> new EntityNotFoundException("Department not found"));
  }
}
```

**ポイント**:

- HTTPメソッド: GETのみを使用
- 成功時のレスポンス: 200 OK
- 存在しない場合は404 Not Found（`EntityNotFoundException`）
- クエリパラメータによるフィルタリングをサポートする場合、`@RequestParam`を使用

---

### 4. Queryテスト作成規約

#### 4.1 テストの基本原則

- **フレームワーク**: JUnit 6, AssertJ, Mockito
- **テストサイズ**: `@Tag("small")`（単体テスト）、`@Tag("medium")`（統合テスト）
- **可視性**: テストクラスおよびテストメソッドは**`package-private`**とします

#### 4.2 単体テスト（Small）の作成パターン

Queryの単体テストでは、リポジトリをモックして、データ取得ロジックを検証します。

```java
package undecided.generic.calendarReg.internal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import undecided.generic.calendarReg.spi.Holiday;

@Tag("small")
@DisplayName("HolidayQueryImplのテスト")
class HolidayQueryImplTest {

  private final HolidayRepository repository = mock(HolidayRepository.class);
  private final HolidayQueryImpl query = new HolidayQueryImpl(repository);

  @Nested
  @DisplayName("findByHolidayDateメソッドのテスト")
  class FindByHolidayDateTest {

    @Test
    @DisplayName("nullが渡された場合、IllegalArgumentExceptionをスローすること")
    void shouldThrowExceptionWhenDateIsNull() {
      // Act & Assert
      assertThatThrownBy(() -> query.findByHolidayDate(null))
          .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("存在する日付の場合、該当するHolidayを返すこと")
    void shouldReturnHolidayWhenExists() {
      // Arrange
      LocalDate date = LocalDate.of(2024, 1, 1);
      Holiday holiday = new Holiday();
      holiday.setId(20240101L);
      holiday.setHolidayDate(date);
      holiday.setHolidayName("元日");
      when(repository.findByHolidayDate(date)).thenReturn(Optional.of(holiday));

      // Act
      Optional<Holiday> result = query.findByHolidayDate(date);

      // Assert
      assertThat(result).isPresent();
      assertThat(result.get().getHolidayName()).isEqualTo("元日");
    }

    @Test
    @DisplayName("存在しない日付の場合、空のOptionalを返すこと")
    void shouldReturnEmptyWhenNotExists() {
      // Arrange
      LocalDate date = LocalDate.of(2024, 1, 2);
      when(repository.findByHolidayDate(date)).thenReturn(Optional.empty());

      // Act
      Optional<Holiday> result = query.findByHolidayDate(date);

      // Assert
      assertThat(result).isEmpty();
    }
  }

  @Nested
  @DisplayName("findBetweenメソッドのテスト")
  class FindBetweenTest {

    @Test
    @DisplayName("fromがnullの場合、IllegalArgumentExceptionをスローすること")
    void shouldThrowExceptionWhenFromIsNull() {
      assertThatThrownBy(() -> query.findBetween(null, LocalDate.of(2024, 1, 1)))
          .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("toがnullの場合、IllegalArgumentExceptionをスローすること")
    void shouldThrowExceptionWhenToIsNull() {
      assertThatThrownBy(() -> query.findBetween(LocalDate.of(2024, 1, 1), null))
          .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("期間内の祝日リストを取得できること")
    void shouldReturnHolidaysBetweenDates() {
      // Arrange
      LocalDate from = LocalDate.of(2024, 1, 1);
      LocalDate to = LocalDate.of(2024, 1, 31);
      Holiday h1 = new Holiday();
      h1.setId(20240101L);
      h1.setHolidayDate(from);
      h1.setHolidayName("元日");

      when(repository.findBetween(from, to)).thenReturn(List.of(h1));

      // Act
      List<Holiday> result = query.findBetween(from, to);

      // Assert
      assertThat(result).containsExactly(h1);
    }
  }
}
```

**ポイント**:

- `should`で始まるメソッド名
- 日本語の`@DisplayName`を必ず付与
- `@Nested`で対象メソッドごとにテストを構造化
- Arrange / Act / Assert の分離
- 境界値テスト（null, 空文字列, 空コレクション）を網羅
- 存在しないデータの場合の動作（`Optional.empty()`）を検証
- 例外検証には`assertThatThrownBy()`を使用

#### 4.3 Queryテストで検証すべき項目

- **正常系**: 预期的な検索条件で正しいデータが返されること
- **空結果**: 存在しないデータで`Optional.empty()`や空の`List`が返されること
- **境界値**: null, 空文字列, 空コレクション, 最大/最小値
- **例外系**: 不正な入力で適切な例外がスローされること
- **データ変換**: リポジトリから取得したデータが正しく変換されること（DTO変換がある場合）

---

### 5. コーディング規約

#### 5.1 例外処理

- **存在しないエンティティ**: `EntityNotFoundException`
- **事前条件違反**: `IllegalArgumentException`
- **システム障害**: `SystemException`

#### 5.2 Lombok・JSpecifyの使用

- `@RequiredArgsConstructor`: 依存関係の注入（必要に応じて）
- `@NonNull`, `@Nullable`: Null安全性の明示

#### 5.3 パッケージ構成

```
undecided.association.relationshipMgmt.orgRole.internal.department
├── DepartmentQuery.java          # Queryインターフェース
├── DepartmentQueryImpl.java      # Query実装
├── DepartmentQueryApi.java       # REST APIコントローラー
└── DepartmentRepository.java     # リポジトリ
```

---

### 6. チェックリスト

Queryユースケース開発時の確認項目:

- [ ] Queryインターフェースが値を返すメソッドのみを持っている
- [ ] 実装クラスに`@Service`アノテーションが付与されている
- [ ] 状態を変更しない読み取り処理のみを実装している
- [ ] 存在しないデータで`Optional.empty()`や空の`List`を返す
- [ ] APIコントローラーでGETメソッドのみを使用している
- [ ] 単体テストで境界値が網羅されている
- [ ] 存在しないデータの場合の動作がテストされている
- [ ] 例外系のテストが`assertThatThrownBy()`で検証されている
