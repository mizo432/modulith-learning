# Command Usecase Development Guide (`command-usecase-dev`)

このスキルは、 **modulith-learning**リポジトリにおいて、 **Commandユースケース**
（状態変更処理）を開発・修正・テストする際に適用するガイドラインです。

CQRSパターンに従い、Commandは **書き込み操作**（Create/Update/Delete）のみを担当し、値を返さない（void）ことが基本です。

---

### 1. いつこのスキルを使用するか (When To Use)

- Commandユースケース（状態変更処理）を新規作成または修正するとき
- `*Command`, `*CommandImpl`, `*CommandApi` クラスを開発するとき
- 書き込み操作の単体テスト・統合テストを作成するとき
- 業務ルールに基づくバリデーションや事前条件チェックを組み込むとき
- 例外処理（`BusinessException`, `SystemException`）をCommand側に実装するとき

---

### 2. Commandユースケースの基本原則

#### 2.1 CQRSにおけるCommandの役割

- **Command = 状態変更**: データの作成、更新、削除を担当
- **void返却**: Commandメソッドは通常voidを返します（副作用のみ）
- **業務ルールの実装**: バリデーション、事前条件チェック、ドメインルールの適用をCommand側で行う
- **リポジトリ連携**: ドメインエンティティを永続化するためにリポジトリを使用

#### 2.2 Commandの層構成

1. **SPI層（公開API）**:
    - 他モジュールから利用されるCommandインターフェースを配置
    - 例: `undecided.association.relationshipMgmt.spi.party.CreatePersonCommand`
2. **Commandインターフェース**:
    - 操作の契約を定義（実装の詳細を隠蔽）
    - 例: `DepartmentCommand` → `insert()`, `update()`, `delete()`
3. **Command実装（`*CommandImpl`）**:
    - 実際のビジネスロジックとリポジトリ連携を実装
    - 例: `DepartmentCommandImpl`
4. **Command API（`*CommandApi`）**:
    - RESTコントローラーとしてPOST/PUT/PATCH/DELETEエンドポイントを公開
    - 例: `DepartmentCommandApi`

#### 2.3 Commandインターフェースの命名規則

- インターフェース: `*Command`（例: `DepartmentCommand`, `UserCommand`）
- 実装クラス: `*CommandImpl`（例: `DepartmentCommandImpl`, `UserCommandImpl`）
- APIコントローラー: `*CommandApi`（例: `DepartmentCommandApi`, `UserCommandApi`）
- SPIパッケージ配下: `Create*Command`（例: `CreatePersonCommand`）

---

### 3. Command実装の作成手順

#### 3.1 Commandインターフェースの定義

```java
package undecided.association.relationshipMgmt.orgRole.internal.department;

import undecided.association.relationshipMgmt.orgRole.spi.Department;
import undecided.association.relationshipMgmt.orgRole.spi.DepartmentCode;

public interface DepartmentCommand {
  void insert(Department department);
  void update(DepartmentCode departmentCode, Department department);
  void delete(DepartmentCode departmentCode);
}
```

**ポイント**:

- メソッドはvoidを返す
- 引数にはドメインエンティティや値オブジェクトを使用
- メソッド名は操作の意図を明確に（`insert`, `update`, `delete`）

#### 3.2 Command実装クラスの作成

```java
package undecided.association.relationshipMgmt.orgRole.internal.department;

import jakarta.persistence.EntityNotFoundException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import undecided.association.relationshipMgmt.orgRole.spi.Department;
import undecided.association.relationshipMgmt.orgRole.spi.DepartmentCode;

@RequiredArgsConstructor
@Service
public class DepartmentCommandImpl implements DepartmentCommand {

  private final DepartmentRepository repository;

  @Override
  public void insert(Department department) {
    repository.save(department);
  }

  @Override
  public void update(DepartmentCode departmentCode, Department department) {
    Optional<Department> oldOption = repository.findByCode(departmentCode);
    Department oldDepartment =
        oldOption.orElseThrow(
            () -> new EntityNotFoundException("Department not found: " + departmentCode));
    repository.save(oldDepartment.update(department));
  }

  @Override
  public void delete(DepartmentCode departmentCode) {
    Optional<Department> oldOption = repository.findByCode(departmentCode);
    Department oldDepartment =
        oldOption.orElseThrow(
            () -> new EntityNotFoundException("Department not found: " + departmentCode));
    repository.deleteById(oldDepartment.getId());
  }
}
```

**ポイント**:

- `@Service`アノテーションを付与
- `@RequiredArgsConstructor`で依存関係を注入
- 存在しないエンティティへの操作時は`EntityNotFoundException`をスロー
- ドメインエンティティのメソッドを通じて状態変更を行う

#### 3.3 Command APIコントローラーの作成

```java
package undecided.association.relationshipMgmt.orgRole.internal.department;

import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import undecided.association.relationshipMgmt.orgRole.spi.Department;
import undecided.association.relationshipMgmt.orgRole.spi.DepartmentCode;

/**
 * 部署情報の変更（Command）を担当するREST APIコントローラーです。
 *
 * <p>CQRSパターンに従い、書き込み操作のみを担当します。
 */
@RestController
@RequestMapping("/api/departments")
@RequiredArgsConstructor
public class DepartmentCommandApi {
  private final DepartmentCommand command;

  @PostMapping
  ResponseEntity<Void> post(
      @RequestBody Department department, UriComponentsBuilder uriComponentsBuilder) {
    command.insert(department);
    URI uri =
        uriComponentsBuilder
            .path("api/departments/{departmentCode}")
            .build(department.getCode().value());
    return ResponseEntity.created(uri).build();
  }

  @DeleteMapping("/{departmentCode}")
  ResponseEntity<Void> delete(@PathVariable DepartmentCode departmentCode) {
    command.delete(departmentCode);
    return ResponseEntity.noContent().build();
  }
}
```

**ポイント**:

- HTTPメソッドと操作の対応: POST=Create, PUT/Patch=Update, DELETE=Delete
- 成功時のレスポンス: 201 Created（新規作成）, 204 No Content（更新/削除）
- Locationヘッダーに新規リソースのURIを含める

---

### 4. Commandテスト作成規約

#### 4.1 テストの基本原則

- **フレームワーク**: JUnit 5, AssertJ, Mockito
- **テストサイズ**: `@Tag("small")`（単体テスト）、`@Tag("medium")`（統合テスト）
- **可視性**: テストクラスおよびテストメソッドは**`package-private`**とします

#### 4.2 単体テスト（Small）の作成パターン

Commandの単体テストでは、リポジトリや外部サービスをモックして、ビジネスロジックを検証します。

```java
package undecided.generic.accountMgmt.internal.user;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import undecided.shared.exception.BusinessException;

@Tag("small")
@DisplayName("UserCommandImplのテスト")
@ExtendWith(MockitoExtension.class)
class UserCommandImplTest {

  @Mock private RestTemplate restTemplate;

  private UserCommandImpl userCommandImpl;

  @BeforeEach
  void setUp() {
    userCommandImpl = new UserCommandImpl(restTemplate);
  }

  @Nested
  @DisplayName("changePasswordメソッドのテスト")
  class ChangePasswordTest {

    @Test
    @DisplayName("パスワードを変更できる")
    void shouldChangePassword() {
      // Arrange
      ChangePasswordRequest request =
          new ChangePasswordRequest("currentPass", "newPassword123", "newPassword123");
      when(restTemplate.getForEntity(anyString(), eq(UserStatusDto.class)))
          .thenReturn(new ResponseEntity<>(new UserStatusDto("testuser", false), HttpStatus.OK));
      when(restTemplate.postForEntity(anyString(), any(PasswordChangeDto.class), eq(Void.class)))
          .thenReturn(new ResponseEntity<>(HttpStatus.NO_CONTENT));

      // Act
      userCommandImpl.changePassword("testuser", request);

      // Assert
      verify(restTemplate).postForEntity(contains("/password"), any(), eq(Void.class));
    }

    @Test
    @DisplayName("新しいパスワードが短い場合は例外がスローされる")
    void shouldThrowExceptionWhenNewPasswordIsTooShort() {
      // Arrange
      ChangePasswordRequest request = new ChangePasswordRequest(null, "short", "short");

      // Act & Assert
      assertThatThrownBy(() -> userCommandImpl.changePassword("testuser", request))
          .isInstanceOf(BusinessException.class)
          .hasMessageContaining("8文字以上");
    }
  }
}
```

**ポイント**:

- `should`で始まるメソッド名
- 日本語の`@DisplayName`を必ず付与
- `@Nested`で対象メソッドごとにテストを構造化
- Arrange / Act / Assert の分離
- 境界値テスト（null, 空文字列, 短すぎるパスワード等）を網羅
- 例外検証には`assertThatThrownBy()`を使用

#### 4.3 Commandテストで検証すべき項目

- **正常系**: 预期的な入力で状態が正しく変更されること
- **異常系**: 不正な入力で適切な例外がスローされること
- **境界値**: null, 空文字列, 空コレクション, 最大/最小値
- **副作用**: リポジトリのsave/deleteが正しく呼び出されること（Mockitoの`verify`）
- **業務ルール**: バリデーションロジックが正しく動作すること

---

### 5. コーディング規約

#### 5.1 例外処理

- **業務エラー**: `BusinessException`, `NotFoundBusinessException`
- **システム障害**: `SystemException`
- **事前条件チェック**: `undecided.supporting.precondition.*` を活用

#### 5.2 Lombok・JSpecifyの使用

- `@RequiredArgsConstructor`: 依存関係の注入
- `@NonNull`, `@Nullable`: Null安全性の明示

#### 5.3 パッケージ構成

```
undecided.association.relationshipMgmt.orgRole.internal.department
├── DepartmentCommand.java          # Commandインターフェース
├── DepartmentCommandImpl.java      # Command実装
├── DepartmentCommandApi.java       # REST APIコントローラー
└── DepartmentRepository.java       # リポジトリ
```

---

### 6. チェックリスト

Commandユースケース開発時の確認項目:

- [ ] Commandインターフェースがvoidを返すメソッドのみを持っている
- [ ] 実装クラスに`@Service`アノテーションが付与されている
- [ ] 依存関係が`@RequiredArgsConstructor`で注入されている
- [ ] 存在しないエンティティへの操作で適切な例外がスローされる
- [ ] APIコントローラーで適切なHTTPステータスコードが返される
- [ ] 単体テストで境界値が網羅されている
- [ ] 例外系のテストが`assertThatThrownBy()`で検証されている
- [ ] モックの呼び出しが`verify()`で検証されている（統合テスト除く）
