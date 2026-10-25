# Spring Modulith 実装ガイドライン

本ガイドラインは、modulith-learning プロジェクトにおける Spring Modulith の実装規則を定義します。

## 1. Spring Modulith とは

Spring Modulith は、Spring Boot アプリケーション内でモジュラーモノリスアーキテクチャを実現するためのフレームワークです。
パッケージベースのモジュール境界を定義し、モジュール間の依存関係を自動的に検証・ドキュメント化します。

### 1.1 主な特徴

- **モジュール境界の自動検出**: `@ApplicationModule` アノテーションでモジュールを定義
- **依存関係の検証**: 循環参照や不正な依存関係を起動時に検出
- **自動ドキュメント生成**: PlantUML によるモジュール構造図の自動生成
- **モジュール単位のテスト**: モジュールとその依存関係のみを起動するテストサポート

### 1.2 技術スタック

| コンポーネント     | バージョン      |
|:-------------------|:---------------|
| Spring Boot        | 4.1.x          |
| Spring Modulith    | 2.1.x          |
| Java               | 25 (OpenJDK)   |

---

## 2. モジュールの定義方法

### 2.1 モジュールの基本的な定義

各モジュールは、ルートパッケージの `package-info.java` に `@ApplicationModule` アノテーションを付与して定義します。

```java
@ApplicationModule(id = "moduleName", displayName = "モジュール名")
package undecided.example.moduleName;

import org.springframework.modulith.ApplicationModule;
```

### 2.2 モジュール ID の命名規則

- **小文字キャメルケース**で記述します。
- サブモジュールの場合は **ドット区切り**で親モジュールの ID を接頭辞として使用します。
  - 例: `relationshipMgmt.orgRole`（親: `relationshipMgmt`、子: `orgRole`）
- ID はプロジェクト内で **一意** でなければなりません。

### 2.3 displayName

- 日本語でモジュールの役割を簡潔に記述します。
- 例: `"関係管理サブドメイン"`, `"汎用サブドメイン"`, `"共通モジュール"`

---

## 3. モジュールのタイプ（Type）

Spring Modulith では、モジュールの公開範囲を `ApplicationModule.Type` で制御します。

### 3.1 モジュールタイプの種類

| タイプ     | 説明                                                                 | 使用シーン                              |
|:-----------|:---------------------------------------------------------------------|:----------------------------------------|
| **OPEN**   | 他モジュールから自由に依存可能被るモジュール                          | 共通モジュール、インフラストラクチャ    |
| **CLOSED** | 他モジュールからの依存を一切許可しないモジュール                      | 独立した業務ドメイン                    |
| **（デフォルト）** | 明示的な Type を指定しない場合。`allowedDependencies` で依存を制御 | 一般的な業務モジュール                  |

### 3.2 各タイプの使用例

#### 3.2.1 OPEN モジュール（共通・支援モジュール）

プロジェクト全体の共通機能や支援機能を提供するモジュールです。他モジュールから自由に参照可能です。

```java
@ApplicationModule(id = "shared", displayName = "共通モジュール", type = ApplicationModule.Type.OPEN)
package undecided.shared;

import org.springframework.modulith.ApplicationModule;
```

```java
@ApplicationModule(
    type = Type.OPEN,
    allowedDependencies = {"generic", "relationshipMgmt", "shared", "shared :: entity"},
    id = "supporting",
    displayName = "支援サブドメイン")
package undecided.supporting;

import org.springframework.modulith.ApplicationModule;
import org.springframework.modulith.ApplicationModule.Type;
```

#### 3.2.2 CLOSED モジュール（独立ドメイン）

外部からの依存を一切許可しない完全に独立したモジュールです。

```java
@ApplicationModule(id = "agiloom", displayName = "アジャイルプロダクト管理", type = Type.CLOSED)
package undecided.agiloom;

import org.springframework.modulith.ApplicationModule;
import org.springframework.modulith.ApplicationModule.Type;
```

#### 3.2.3 デフォルトモジュール（一般的な業務モジュール）

明示的な Type を指定しない一般的な業務モジュールです。

```java
@ApplicationModule(id = "generic", displayName = "汎用サブドメイン")
@NamedInterface(propagate = true)
package undecided.generic;

import org.springframework.modulith.ApplicationModule;
import org.springframework.modulith.NamedInterface;
```

---

## 4. モジュール間の依存関係

### 4.1 allowedDependencies

`allowedDependencies` 属性を使用して、モジュールが依存できる他のモジュールを明示的に宣言します。

```java
@ApplicationModule(
    type = Type.OPEN,
    allowedDependencies = {"generic", "relationshipMgmt", "shared", "shared :: entity"},
    id = "supporting",
    displayName = "支援サブドメイン")
package undecided.supporting;
```

### 4.2 依存関係の指定方法

| 記法                      | 意味                                      |
|:--------------------------|:------------------------------------------|
| `"moduleName"`            | モジュール全体への依存                    |
| `"moduleName :: subPackage"` | モジュール内の特定のパッケージへの依存    |

### 4.3 依存関係のルール

1. **依存方向**: 上位層（Presentation）→ 下位層（Domain）の方向に依存します。
2. **循環参照の禁止**: モジュール間の循環参照は起動時にエラーになります。
3. **internal パッケージの非公開**: `internal` パッケージに配置されたクラスは、他モジュールから直接参照してはなりません。
4. **SPI パッケージの公開**: 他モジュールから利用されるインターフェースは `spi` パッケージに配置します。

---

## 5. @NamedInterface アノテーション

### 5.1 概要

`@NamedInterface` アノテーションは、パッケージ内のインターフェースをモジュール間の公開 API としてマークします。

### 5.2 propagate 属性

| 値       | 説明                                                        |
|:---------|:------------------------------------------------------------|
| `true`   | サブパッケージにも公開 API として伝播する                   |
| `false`  | 当該パッケージのみが公開 API となる                         |

### 5.3 使用例

```java
@ApplicationModule(id = "relationshipMgmt", displayName = "関係管理サブドメイン")
@NamedInterface(propagate = true)
package undecided.association.relationshipMgmt;

import org.springframework.modulith.ApplicationModule;
import org.springframework.modulith.NamedInterface;
```

```java
@NamedInterface(propagate = true)
package undecided.association.relationshipMgmt.spi.party;

import org.springframework.modulith.NamedInterface;
```

```java
@NamedInterface(propagate = false)
package undecided.association.relationshipMgmt.party;

import org.springframework.modulith.NamedInterface;
```

### 5.4 配置ルール

- **SPI パッケージ** (`spi/`): `@NamedInterface(propagate = true)` を付与し、公開 API としてマークします。
- **Internal パッケージ** (`internal/`): `@NamedInterface` を付与せず、内部実装として保護します。

---

## 6. パッケージ構成と層分離

### 6.1 モジュール内の標準的なパッケージ構成

各モジュールは以下のパッケージ構成に従います。

```
undecided.{domain}.{module}/
├── package-info.java           # @ApplicationModule 定義
├── spi/                        # 公開 API（Service Provider Interface）
│   ├── package-info.java       # @NamedInterface(propagate = true)
│   └── {interface}.java
├── internal/                   # 内部実装（他モジュールから非公開）
│   ├── domain/                 # ドメイン層
│   │   ├── entity/             # エンティティ
│   │   ├── valueobject/        # 値オブジェクト
│   │   ├── repository/         # リポジトリインターフェース
│   │   └── service/            # ドメインサービス
│   ├── usecase/                # ユースケース層
│   │   ├── command/            # Command ユースケース
│   │   └── query/              # Query ユースケース
│   ├── infrastructure/         # インフラストラクチャ層
│   │   ├── repository/         # リポジトリ実装
│   │   └── batch/              # バッチ処理
│   └── presentation/           # プレゼンテーション層
│       └── api/                # REST API
└── {sub-module}/               # サブモジュール
    └── package-info.java       # @ApplicationModule 定義
```

### 6.2 internal パッケージの役割

`internal` パッケージはモジュールの内部実装を格納し、他モジュールからの直接参照を防ぎます。

- **公開 API/SPI**: `spi` パッケージまたはルートパッケージに配置
- **内部実装**: `internal` パッケージに配置

### 6.3 サブモジュールの定義

サブモジュールは親モジュール配下に配置し、ドット区切りの ID を使用します。

```java
@ApplicationModule(id = "relationshipMgmt.orgRole", displayName = "組織役割")
package undecided.association.relationshipMgmt.orgRole;

import org.springframework.modulith.ApplicationModule;
```

---

## 7. モジュールの検証とテスト

### 7.1 モジュール検証テスト

`ModulithTest.java` でモジュール構造の整合性を検証します。

```java
@Tag("small")
@DisplayName("Spring Modulithの検証テスト")
class ModulithTest {

  ApplicationModules modules = ApplicationModules.of(ModulithDemoApplication.class);

  @Test
  void verifyPackageConformity() {
    modules.verify();
  }
}
```

### 7.2 モジュール構造図の自動生成

`Documenter` クラスを使用して、PlantUML 形式のモジュール構造図を自動生成します。

```java
@Test
void createModulithsDocumentation() {
  new Documenter(modules)
      .writeModulesAsPlantUml()
      .writeIndividualModulesAsPlantUml();
}
```

生成されるファイル:
- **モジュール全体図**: モジュール間の依存関係を可視化
- **個別モジュール図**: 各モジュール内の Bean 構造を可視化

### 7.3 モジュール単位の統合テスト

`@ContextHierarchy` と `@SpringModulithTest` を使用して、モジュール単位で統合テストを実行します。

```java
@ContextHierarchy({
  @ContextConfiguration(classes = SupportingModule.class),
  @ContextConfiguration(classes = TargetModule.class)
})
@SpringModulithTest
class TargetModuleIntegrationTest {
  // モジュール固有のテスト
}
```

### 7.4 CQRS 分離の検証

RestController は Query（GET）と Command（POST/PUT/DELETE/PATCH）を混在させてはなりません。

```java
@Test
void verifyRestControllerCQRSSeparation() throws ClassNotFoundException {
  // QueryController は @GetMapping のみ
  // CommandController は @PostMapping/@PutMapping/@DeleteMapping/@PatchMapping のみ
  // 混在している場合はテストが失敗する
}
```

---

## 8. ドメインイベント

### 8.1 ドメインイベントの発行

Spring Modulith は、モジュール間でドメインイベントを非同期で伝播する機能を 제공합니다。

```java
@DomainEvent
public record OrderCompletedEvent(OrderId orderId) {}

public class OrderService {
  private final ApplicationEventPublisher eventPublisher;

  public void completeOrder(OrderId orderId) {
    // 業務ロジック
    eventPublisher.publishEvent(new OrderCompletedEvent(orderId));
  }
}
```

### 8.2 ドメインイベントの購読

他モジュールでドメインイベントを購読するには、`@EventListener` を使用します。

```java
@Component
public class OrderCompletionListener {

  @EventListener
  public void handleOrderCompleted(OrderCompletedEvent event) {
    // イベントに応じた処理
  }
}
```

### 8.3 イベントのテスト

`AssertableApplicationEvents` を使用して、テスト内で発行されたイベントを検証します。

```java
@Test
void shouldPublishOrderCompletedEvent(
    @Autowired AssertableApplicationEvents events) {
  // Arrange
  OrderId orderId = new OrderId("ORD-001");

  // Act
  orderService.completeOrder(orderId);

  // Assert
  events.shouldHave(
    event -> event.getOrderCompletedEvent().isPresent()
  );
}
```

---

## 9. ベストプラクティス

### 9.1 モジュール設計の原則

1. **明確な境界**: 各モジュールは明確な責務を持ち、他のモジュールの実装詳細に依存しません。
2. **依存性逆転**: 高レベルモジュールは低レベルモジュールに直接依存せず、インターフェース（SPI）を介して依存します。
3. **internal パッケージの厳格な使用**: 内部実装は必ず `internal` パッケージに配置し、他モジュールから直接参照しないようにします。
4. **サブドメインの分離**: 異なるサブドメイン（汎用・支援・限界コンテキスト）は別々のモジュールに分離します。

### 9.2 モジュール ID の命名規則

| モジュール種別     | ID の例                          | displayName の例              |
|:-------------------|:---------------------------------|:------------------------------|
| サブドメイン       | `generic`, `supporting`          | `汎用サブドメイン`            |
| 業務モジュール     | `relationshipMgmt`               | `関係管理サブドメイン`        |
| サブモジュール     | `relationshipMgmt.orgRole`       | `組織役割`                    |
| 共通モジュール     | `shared`                         | `共通モジュール`              |
| 独立ドメイン       | `agiloom`                        | `アジャイルプロダクト管理`    |

### 9.3 依存関係のベストプラクティス

1. **共通モジュールへの依存**: 共通機能は `shared` モジュールに集約し、必要に応じて `allowedDependencies` で宣言します。
2. **SPI 経由の依存**: 他モジュールとの連携は `spi` パッケージのインターフェースを介して行います。
3. **循環参照の回避**: モジュール間の循環参照が発生しないよう、依存方向を一方向に保ちます。
4. **最小依存の原則**: 必要なモジュールのみを `allowedDependencies` に宣言します。

### 9.4 package-info.java の記述ルール

1. **KDoc コメント**: モジュールの役割を簡潔に記述します。
2. **アノテーションの順序**: `@ApplicationModule` → `@NamedInterface` の順に記述します。
3. **import 文**: Spring Modulith のクラスは明示的に import します。

```java
@ApplicationModule(id = "moduleName", displayName = "モジュール名")
@NamedInterface(propagate = true)
/** モジュールの説明 */
package undecided.example.moduleName;

import org.springframework.modulith.ApplicationModule;
import org.springframework.modulith.NamedInterface;
```

---

## 10. トラブルシューティング

### 10.1 循環参照エラー

**症状**: アプリケーション起動時に `CycleException` が発生する。

**原因**: モジュール間に循環参照が存在する。

**解決方法**:
1. `modules.verify()` の出力を確認し、循環参照の原因を特定します。
2. 依存関係を見直し、インターフェース経由の依存に切り替えます。
3. 必要に応じてモジュールの分割または統合を検討します。

### 10.2 モジュールが検出されない

**症状**: `ApplicationModules.of()` で期待したモジュールが含まれていない。

**原因**:
- `package-info.java` に `@ApplicationModule` が付与されていない。
- クラスパスにモジュールパッケージが含まれていない。

**解決方法**:
1. `package-info.java` が正しく配置されているか確認します。
2. `@ApplicationModule` アノテーションが正しく付与されているか確認します。

### 10.3 Bean のスコープエラー

**症状**: モジュール境界を越えて Bean が参照できない。

**原因**: `internal` パッケージの Bean を他モジュールから直接参照している。

**解決方法**:
1. 公開する Bean は `spi` パッケージにインターフェースを配置します。
2. `@NamedInterface(propagate = true)` を SPI パッケージに付与します。

---

## 11. 関連リソース

| リソース                                              | 説明                                     |
|:------------------------------------------------------|:-----------------------------------------|
| [modulith-dev スキル](../../skills/modulith-dev/SKILL.md) | Spring Modulith 開発の詳細ガイド         |
| [JPA ガイドライン](jpa.guidelines.md)                  | Spring Data JPA 実装規則                 |
| [DDD ガイドライン](../ddd/ddd.guideline.md)            | ドメイン駆動設計の基本原則               |
| [テストガイドライン](testing.guidelines.md)            | テスト戦略とテスト作成規約               |
| [アーキテクチャチェックリスト](../../skills/modulith-dev/checklists/architecture-checklist.md) | モジュール分割・層構成の確認項目     |

---

## 12. Spring Modulith 公式リソース

- [Spring Modulith Reference Documentation](https://docs.spring.io/spring-modulith/reference/)
- [Spring Modulith GitHub Repository](https://github.com/spring-projects/spring-modulith)
