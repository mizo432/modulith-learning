# イベントソーシング設計ガイドライン

このドキュメントは、 **modulith-learning**プロジェクトにおけるイベントソーシング（Event
Sourcing）の実践方法を定義するガイドラインです。
ドメインで発生した事象をイベントとして時系列に蓄積し、そのイベントの再生によって現在の状態を再構成する手法を採用します。

---

## 1. イベントソーシングの基本概念

### 1.1 イベントソーシングとは

イベントソーシングは、状態の変更を「状態そのものの更新（UPDATE）」として扱わず、「何が起こったか（イベント）」を不変の記録として蓄積する手法です。

- **状態 = イベントの累積**: エンティティの現在状態は、発生したイベントの時系列を適用（リプレイ）することで再構成可能。
- **不変のイベント**: 一度記録されたイベントは変更・削除せず、追記（INSERT-only）のみで管理。
- **監査証跡の自然な実現**: 過去のすべての変更履歴がイベントとして残るため、監査・デバッグ・分析が容易。

### 1.2 導入の目的とメリット

| メリット             | 説明                                                                     |
|----------------------|--------------------------------------------------------------------------|
| **完全な監査証跡**   | 誰が、いつ、何を、なぜ変えたかがイベントとして完全に記録される           |
| **状態の再現性**     | 任意の時点での状態をイベント再生で再現可能                               |
| **ドメインの表現力** | 「コト（出来事）」を第一級市民として扱え、ドメイン言語と自然にマッピング |
| **CQRSとの親和性**   | コマンド側（Write）とクエリ側（Read）を分離し、それぞれ最適化可能        |
| **時系列分析**       | イベントストリームを分析することで、業務トレンドやパターンを発見可能     |

### 1.3 導入の判断基準

イベントソーシングはすべてのユースケースに適しているわけではありません。以下の条件を考慮して
**部分的な導入**を検討します。

**導入が有効なケース**:

- 完全な監査証跡が必須なドメイン（財務、医療、コンプライアンス関連）
- 状態変更の理由（Why）を記録する必要がある場合
- 過去の状態へのロールバックや時系列分析が必要な場合
- 複数の読み取りモデル（Read Model）を同じイベントストリームから構築する場合

**導入を避けるケース**:

- 単純なCRUDで監査証跡が不要な場合
- 高頻度な微小変更が発生し、イベントストリームが膨大になる場合
- 状態の再構成コストが許容範囲を超える場合

---

## 2. イベントの設計原則

### 2.1 ドメインイベントの定義

ドメインイベントは「 **ドメイン内で発生した重要な出来事**
」を表現します。過去形の動詞句で命名し、不変（Immutable）な値オブジェクトとして実装します。

**命名規則**:

- 過去形の名詞句で命名（例: `OrderPlaced`, `PaymentCompleted`, `TaskAssigned`）
- イベントが発生した時刻（`occurredAt`）を必ず保持
- イベントを一意に識別するID（`eventId`）を保持

### 2.2 イベントの構造

すべてのドメインイベントは以下の共通要素を持ちます。

```java
public interface DomainEvent {
    UUID eventId();           // イベントを一意に識別するID
    LocalDateTime occurredAt(); // イベントが発生した時刻
    String aggregateId();     // 関連する集約ルートのID
    String aggregateType();   // 関連する集約の型名
    int version();            // 集約のバージョン（オプティミスティックロック用）
}
```

### 2.3 イベントの不変性

- 一度作成されたイベントは **決して変更しません**。
- Java `record` または `final` クラスを使用して不変性を保証します。
- 誤ったイベントが記録された場合は、 **訂正イベント**（例: `OrderCancelled`）を新規発行します。

### 2.4 イベントの粒度

- **集約内の状態変更1つにつき、1つのイベント**を発行します。
- 1つのコマンドに対して複数のイベントが発行される場合があります（例: `OrderPlaced` +
  `InventoryReserved`）。
- イベントは集約の境界内で意味が完結するように設計します。

---

## 3. 集約の設計

### 3.1 イベントソーシング集約の基本構造

イベントソーシングを採用する集約は、以下の3つの要素で構成されます。

| 要素                              | 責務                                                         |
|-----------------------------------|--------------------------------------------------------------|
| **イベントストア（Event Store）** | イベントの永続化・取得を担当。インフラ層に配置               |
| **集約（Aggregate）**             | ドメインロジックを実行し、イベントを生成。ドメイン層に配置   |
| **プロジェクター（Projector）**   | イベントストリームから読み取りモデルを構築。インフラ層に配置 |

### 3.2 集約の実装パターン

イベントソーシング集約は、以下の2つのフェーズを持ちます。

#### イベントの適用（Apply）

イベントを適用して内部状態を更新します。このメソッドは **イベントの生成時**と **イベントの再生時**
の両方で呼び出されます。

```java
abstract class EventSourcedAggregate {
    private final List<DomainEvent> uncommittedEvents = new ArrayList<>();
    private int version;

    // イベントを適用して状態を更新
    protected abstract void apply(DomainEvent event);

    // 未コミットのイベントを再生して状態を再構成
    void replay(List<DomainEvent> events) {
        for (DomainEvent event : events) {
            apply(event);
            this.version = event.version();
        }
    }

    // 未コミットのイベントを取得
    List<DomainEvent> getUncommittedEvents() {
        return Collections.unmodifiableList(uncommittedEvents);
    }

    // 未コミットのイベントをクリア
    void markEventsAsCommitted() {
        uncommittedEvents.clear();
    }
}
```

#### イベントの生成（Raise）

ドメインコマンド（状態変更メソッド）内で、ビジネスルールを検証した後、イベントを生成・適用します。

```java
class Order extends EventSourcedAggregate {
    private String status;
    private Money totalAmount;

    // ドメインコマンド
    void place(Money amount) {
        // ビジネスルールの検証
        if (amount.isZero()) {
            throw new BusinessException("注文金額は0より大きく必要があります");
        }

        // イベントの生成と適用
        var event = new OrderPlaced(
            UUID.randomUUID(),
            DateProvider.currentLocalDateTime(),
            this.getId().toString(),
            "Order",
            this.version + 1,
            amount
        );
        raiseEvent(event);
    }

    // イベントの適用
    @Override
    protected void apply(DomainEvent event) {
        switch (event) {
            case OrderPlaced e -> {
                this.status = "PLACED";
                this.totalAmount = e.amount();
            }
            case OrderCancelled e -> {
                this.status = "CANCELLED";
            }
            // ... 他のイベントの適用
        }
    }

    private void raiseEvent(DomainEvent event) {
        apply(event);
        uncommittedEvents.add(event);
    }
}
```

### 3.3 バージョン管理とオプティミスティックロック

- 各イベントは集約のバージョン番号を持ち、イベントストアへの保存時に **オプティミスティックロック**
  として機能します。
- 同時に同じ集約に対する変更が発生した場合、バージョンの不一致を検出して競合を検知します。
- 競合が発生した場合は `ConcurrencyException` をスローし、呼び出し元に再試行を促します。

---

## 4. イベントストアの設計

### 4.1 イベントストアの責務

イベントストアは、イベントの永続化と取得を抽象化するインターフェースです。

```java
public interface EventStore {
    // 集約のイベントストリームを取得
    List<DomainEvent> getEvents(String aggregateId, int fromVersion);

    // 未コミットのイベントを永続化
    void appendEvents(String aggregateId, int expectedVersion, List<DomainEvent> events);

    // すべてのイベントを取得（プロジェクター用）
    Flux<DomainEvent> getAllEvents(Since since);
}
```

### 4.2 インフラ層での実装

- イベントストアの実装は **インフラ層**に配置します。
- データベーステーブル設計は、イミュータブルデータモデルガイドラインに従います。
- イベントテーブルには以下のカラムを必須で保持します。

| カラム名         | 型        | 説明                         |
|------------------|-----------|------------------------------|
| `event_id`       | UUID      | イベントの一意な識別子       |
| `aggregate_id`   | VARCHAR   | 関連する集約のID             |
| `aggregate_type` | VARCHAR   | 集約の型名                   |
| `version`        | INTEGER   | 集約のバージョン             |
| `event_type`     | VARCHAR   | イベントの型名               |
| `payload`        | JSONB     | イベントのデータ（JSON形式） |
| `occurred_at`    | TIMESTAMP | イベント発生時刻             |
| `created_at`     | TIMESTAMP | 記録時刻                     |

### 4.3 イベントの永続化戦略

- **トランザクションの原子性**: 1つのコマンドで生成された複数のイベントは、1つのトランザクションでまとめて保存します。
- **JPAとの統合**: `spring-modulith-starter-jpa` を活用し、JPAトランザクション内でイベントを永続化します。
- **Flywayマイグレーション**: イベントテーブルのスキーマ変更はFlywayで管理します。

---

## 5. プロジェクターと読み取りモデル

### 5.1 プロジェクターの役割

プロジェクターは、イベントストリームを購読し、読み取りモデル（Read Model）を構築・更新します。

```java
@Component
public class OrderReadModelProjector {

    @EventListener
    public void on(OrderPlaced event) {
        // 読み取りモデルの更新
        orderReadModelRepository.save(
            OrderReadModel.fromEvent(event)
        );
    }

    @EventListener
    public void on(OrderCancelled event) {
        // 読み取りモデルの更新
        orderReadModelRepository.updateStatus(
            event.aggregateId(), "CANCELLED"
        );
    }
}
```

### 5.2 読み取りモデルの設計

- 読み取りモデルは、クエリのパフォーマンスを最適化するために設計されます。
- 読み取りモデルは**結果整合性（Eventual Consistency）**で更新されます。
- 読み取りモデルのインターフェースは **アプリケーション層**に配置し、実装は **インフラ層**に配置します。

### 5.3 読み取りモデルの再構築

- スキーマ変更やバグ修正により、読み取りモデルをゼロから再構築できる設計にします。
- イベントストアからすべてのイベントを再生して、読み取りモデルを再構築するバッチ処理を提供します。

---

## 6. Spring Modulith との統合

### 6.1 ドメインイベントの発行

Spring Modulithのドメインイベント機能（`@DomainEvents`）とイベントソーシングを統合します。

```java
class Order {
    @DomainEvents
    List<DomainEvent> domainEvents() {
        return new ArrayList<>(uncommittedEvents);
    }
}
```

### 6.2 モジュール間イベントの伝播

- モジュール間でイベントを伝播する場合は、Spring Modulithのモジュール間イベント機能を活用します。
- イベントの境界を明確にし、モジュール間の結合度を低く保ちます。

### 6.3 モジュール検証

- `ApplicationModules.of(ModulithDemoApplication.class).verify()` でモジュール間の依存関係を検証します。
- イベントソーシングによる非同期通信が、モジュール境界を正しく尊重していることを確認します。

---

## 7. 実装規約

### 7.1 パッケージ構成

イベントソーシング関連のクラスは、プロジェクトの既存モジュール構造（`spi` / `internal` 2層構造）に従って配置します。

#### 共通イベントインターフェース

`DomainEvent` インターフェースおよびイベントソーシング集約の基底クラスは、 **
`com.github.mizo432.shared.event`** パッケージに配置します。これにより、全モジュールから共通のイベント契約を参照できます。

```
com.github.mizo432.shared
├── event/                 # イベントソーシング共通インターフェース
│   ├── DomainEvent.java   # 全ドメインイベントの基底インターフェース
│   └── EventSourcedAggregate.java  # イベントソーシング集約の基底クラス
└── infrastructure/
    └── eventstore/        # イベントストア共通実装（オプション）
        ├── JpaEventStore.java
        └── EventEntry.java    # JPAエンティティ（イベント永続化用）
```

#### モジュール内イベント配置

各モジュール内では、既存の `spi`（公開API）/ `internal`（内部実装）構造に従ってイベントを配置します。

```
undecided.<domain>.<module>   # 例: undecided.association.relationshipMgmt
├── spi/                       # モジュールの公開API（他モジュールから参照可能）
│   ├── <aggregate>/           # 集約ごとにサブパッケージ
│   │   ├── Employee.java      # 集約ルートエンティティ
│   │   ├── EmployeeNo.java    # 値オブジェクト
│   │   └── event/             # モジュール間公開イベント（他モジュールが購読するイベント）
│   │       ├── EmployeeCreated.java
│   │       └── EmployeeTransferred.java
│   └── package-info.java      # @NamedInterface(propagate = true) で外部公開
├── internal/                  # モジュール内部実装（他モジュールから参照不可）
│   ├── <aggregate>/           # 集約内部実装
│   │   ├── EmployeeRepository.java
│   │   ├── EmployeeCommandImpl.java
│   │   └── event/             # モジュール内部用イベント（他モジュールに公開しないイベント）
│   │       └── EmployeeInternalNoteAdded.java
│   ├── projector/             # プロジェクター
│   │   └── EmployeeReadModelProjector.java
│   ├── readmodel/             # 読み取りモデル
│   │   ├── EmployeeReadModel.java
│   │   └── EmployeeReadModelRepository.java
│   └── package-info.java
└── package-info.java          # @ApplicationModule でモジュール定義
```

#### 配置規則のまとめ

| クラス種別                         | パッケージ配置                                           | 可視性                                       |
|------------------------------------|----------------------------------------------------------|----------------------------------------------|
| `DomainEvent` インターフェース     | `com.github.mizo432.shared.event`                                 | public（全モジュール公開）                   |
| `EventSourcedAggregate` 基底クラス | `com.github.mizo432.shared.event`                                 | public（全モジュール公開）                   |
| モジュール間公開イベント           | `undecided.<domain>.<module>.spi.<aggregate>.event`      | public（`@NamedInterface` で外部公開）       |
| モジュール内部イベント             | `undecided.<domain>.<module>.internal.<aggregate>.event` | package-private（モジュール内部限定）        |
| イベントストア実装                 | `com.github.mizo432.shared.infrastructure.eventstore`             | public（共通実装）                           |
| プロジェクター                     | `undecided.<domain>.<module>.internal.projector`         | package-private                              |
| 読み取りモデル                     | `undecided.<domain>.<module>.internal.readmodel`         | package-private                              |
| イベントリスナー                   | `undecided.<domain>.<module>.presentation.event`         | package-private（commandサービスを呼び出す） |

#### イベントリスナーの配置

モジュール間公開イベントに反応して **command サービスを呼び出す**イベントリスナーは、 **
`presentation.event`** パッケージに配置します。これにより、プレゼンテーション層でイベント駆動のワークフローを構築できます。

```
undecided.<domain>.<module>
├── presentation/
│   └── event/               # イベントリスナー（commandサービスを呼び出す）
│       ├── EmployeeCreatedListener.java
│       └── EmployeeTransferredListener.java
└── ...
```

**イベントリスナーの役割:**

- `spi.<aggregate>.event` に配置されたモジュール間公開イベントを購読します。
- 購読したイベントに基づいて、関連する command サービスを呼び出して次のアクションをトリガーします。
- 例: `EmployeeCreated` イベントを受信すると、`OnboardingCommandService` を呼び出してオンボーディングフローを開始します。

**実装例:**

```java
package undecided.association.relationshipMgmt.presentation.event;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import undecided.association.relationshipMgmt.spi.employee.event.EmployeeCreated;
import undecided.association.relationshipMgmt.internal.command.OnboardingCommandService;

@Component
public class EmployeeCreatedListener {

    private final OnboardingCommandService onboardingCommandService;

    public EmployeeCreatedListener(OnboardingCommandService onboardingCommandService) {
        this.onboardingCommandService = onboardingCommandService;
    }

    @EventListener
    public void handle(EmployeeCreated event) {
        onboardingCommandService.startOnboarding(event.getEmployeeNo());
    }
}
```

#### 命名規則とモジュール境界

- **モジュール間公開イベント**は、他のモジュールが購読して反応するイベントです（例:
  `EmployeeCreated`）。`spi.<aggregate>.event` に配置し、`@NamedInterface` により外部公開します。
- **モジュール内部イベント**は、モジュール内の状態変化を表しますが、他モジュールには公開しません（例:
  `EmployeeInternalNoteAdded`）。`internal.<aggregate>.event` に配置します。
- **イベントリスナー**は、モジュール間公開イベントを購読して command サービスを呼び出すクラスです。
  `presentation.event` に配置し、`@EventListener` アノテーションでイベントを購読します。
- イベントクラス名は **過去形の名詞句**で命名します（例: `OrderPlaced`, `PaymentCompleted`,
  `TaskAssigned`）。
- イベントリスナーのクラス名は**`<EventName>Listener`** で命名します（例: `EmployeeCreatedListener`,
  `OrderPlacedListener`）。

### 7.2 命名規則

| 要素             | 命名規則                  | 例                                |
|------------------|---------------------------|-----------------------------------|
| ドメインイベント | 過去形の名詞句            | `OrderPlaced`, `PaymentCompleted` |
| 集約             | 名詞                      | `Order`, `Customer`               |
| イベントストア   | `EventStore` サフィックス | `OrderEventStore`                 |
| プロジェクター   | `Projector` サフィックス  | `OrderProjector`                  |
| 読み取りモデル   | `ReadModel` サフィックス  | `OrderReadModel`                  |

### 7.3 例外処理

| 例外クラス                   | 用途                             |
|------------------------------|----------------------------------|
| `ConcurrencyException`       | オプティミスティックロック競合   |
| `AggregateNotFoundException` | 指定された集約が見つからない場合 |
| `EventStoreException`        | イベントストアの永続化エラー     |
| `BusinessException`          | ビジネスルール違反               |

### 7.4 Null安全性

- イベント内のすべてのプロパティは `@NonNull` とし、Null許容の場合は明示的に `@Nullable` を付与します。
- JSpecifyアノテーションを使用して、コンパイル時のNull安全性を検証します。

---

## 8. テスト戦略

### 8.1 単体テスト（Small）

- **集約のテスト**: イベントの生成・適用ロジックをSpringコンテキストなしでテストします。
- **イベントの再生**: イベントを再生して状態が正しく再構成されることを検証します。
- **ビジネスルール**: ドメインコマンドでのバリデーションが正しく機能することを確認します。

```java
@Nested
class PlaceMethodTest {
    @Test
    @DisplayName("注文金額が正の場合、OrderPlacedイベントを生成するべき")
    void shouldRaiseOrderPlacedEventWhenAmountIsValid() {
        // Arrange
        var order = Order.create(UUID.randomUUID());

        // Act
        order.place(Money.of(1000));

        // Assert
        var events = order.getUncommittedEvents();
        assertThat(events).hasSize(1);
        assertThat(events.get(0)).isInstanceOf(OrderPlaced.class);
    }
}
```

### 8.2 統合テスト（Medium）

- **イベントストアの永続化**: 実際のデータベースに対してイベントの保存・取得を検証します。
- **プロジェクターの動作**: イベント発生時に読み取りモデルが正しく更新されることを確認します。
- **オプティミスティックロック**: 同時更新時の競合検出が正しく機能することを確認します。

### 8.3 テスト実行コマンド

```bash
# Smallテスト（単体テスト）の実行
./gradlew :backend:test

# Mediumテスト（統合テスト）の実行
./gradlew :backend:mediumTest
```

---

## 9. イベントソーシングと既存ガイドラインの関係

### 9.1 イミュータブルデータモデルとの統合

イベントソーシングは、イミュータブルデータモデルガイドラインで定義されている「追記型データ管理（INSERT-only）」の思想と完全に一致します。

- イベントは一度記録すると変更しない（不変性）
- 訂正・キャンセルは新たなイベントとして記録
- 時系列に蓄積されたイベントが完全な監査証跡となる

### 9.2 DDD ガイドラインとの統合

- **ドメインイベント**: DDDガイドラインで定義されている「ドメインイベント」パターンをイベントソーシングで強化。
- **集約**: 集約の境界内でイベントを生成・適用し、強い整合性を確保。
- **複数集約間の整合性**: イベントソーシングにより、結果整合性（Eventual
  Consistency）での複数集約間の連携が自然に実現可能。

### 9.3 CQRS との関係

- イベントソーシングはCQRS（Command Query Responsibility Segregation）と相性が良い。
- コマンド側（Write）はイベントソーシング集約、クエリ側（Read）はプロジェクターで構築された読み取りモデルを使用。
- DDDガイドラインのCQRSセクションで定義されている導入判断基準に従い、必要に応じて部分的に導入。

---

## 10. 実践上の重要な考え方

### 10.1 小さく初めて、小さく失敗する

- いきなりすべての集約にイベントソーシングを導入しない。
- 監査証跡が最も重要な1つの集約から始めて、経験を積む。
- イベントの設計はインクリメンタルに改善していく。

### 10.2 イベントは契約

- イベントはモジュール間の **契約**です。一度公開されたイベントの構造を変更しない。
- 拡張が必要な場合は、新しいイベントを追加するか、既存のイベントに新しいフィールドを追加（後方互換性を維持）。
- イベントのスキーマ変更は、プロジェクターの再構築を伴うことを理解する。

### 10.3 課題ドリブン

- イベントソーシングは手段であり、目的ではありません。
- 「監査証跡が必要」「状態の再現が必要」などの具体的な課題がある場合に導入する。
- 単純なCRUDで済む場合は、イベントソーシングを導入しない。

---

## 11. 設計チェックリスト

イベントソーシングを導入する際に、以下の項目を確認してください。

- [ ] イベントソーシングを導入する具体的な課題（監査証跡・状態再現など）が明確か
- [ ] ドメインイベントが過去形で命名され、不変（Immutable）か
- [ ] イベントに `eventId`, `occurredAt`, `aggregateId`, `aggregateType`, `version` が含まれているか
- [ ] 集約の `apply` メソッドがイベント生成時と再生時の両方で機能するか
- [ ] オプティミスティックロックによる競合検出が実装されているか
- [ ] イベントストアのインターフェースがドメイン層に、実装がインフラ層にあるか
- [ ] プロジェクターがイベントを購読して読み取りモデルを構築しているか
- [ ] 読み取りモデルのゼロからの再構築が可能か
- [ ] モジュール境界が正しく尊重されているか
- [ ] Null安全性がJSpecifyで保証されているか
- [ ] 単体テストでイベントの生成・適用・再生が網羅的にテストされているか
- [ ] 統合テストでイベントストアの永続化が検証されているか

---

## 12. 参考リソース

### 書籍

- **Events First**: イベントソーシングとCQRSの実践ガイド。理論と実践のバランスが良い。
- **Versioning in an Event Sourced System**: Udi Dahanのイベントバージョン管理に関する論文。
- **ドメイン駆動設計入門**: イベントソーシングとの統合パターンが記載されている。

### オンラインリソース

- **Martin Fowler - Event Sourcing**: イベントソーシングの基本概念を解説
  (https://martinfowler.com/eaaDev/EventSourcing.html)
- **Spring Modulith Documentation**: モジュール間イベントの公式ドキュメント
- **jmolecules Documentation**: JavaでのDDD戦術的パターンの実装リファレンス
