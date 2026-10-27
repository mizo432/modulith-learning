# Tidy First? ガイドライン

本ガイドラインは、Kent Beck 著の「Tidy First?」書籍に基づき、コード整理（Tidying）技術をプロジェクトの共通言語として定めます。Tidying はリファクタリングのサブセットであり、コードの構造を改善する小さな変更を体系的に適用することで、読みやすさと保守性を向上させます。

## 目的

- チーム内でコード整理の共通言語を確立する
- コードレビュー時のチェックポイントを明確化する
- 新規メンバーのオンボーディングを支援する
- コードの読みやすさと保守性を継続的に向上させる

## Tidying の原則

- **小さな変更から始める**: 一度に大きな変更を行うのではなく、小さな Tidying を積み重ねる
- **テストを基盤とする**: Tidying 前にテストが存在し、変更後もテストが通ることを確認する
- **文脈に合わせる**: 全ての Tidying が常に適用されるわけではなく、文脈に応じて適切なものを選ぶ
- **経済的に考える**: Tidying のコストとベネフィットを考慮し、投資価値のあるものから優先する

## Part I. Tidyings（コード整理技術 15 種）

### 1. Guard Clauses（ガード節）

メソッドの先頭で事前条件をチェックし、条件が満たされない場合は早期リターンする技術です。ネストを浅くして、メインロジックが読みやすくなるようにします。

#### 適用タイミング

- メソッドの先頭に複数の条件分岐がある場合
- ネストが深くなり、メインロジックが見えにくくなっている場合
- 事前条件の検証がメソッドの本体と混在している場合

#### ❌ 悪い例

```java
public void processOrder(Order order) {
    if (order != null) {
        if (order.isValid()) {
            if (order.getItems().isEmpty()) {
                // メインロジック
                calculateTotal(order);
                saveOrder(order);
            }
        }
    }
}
```

#### ✅ 良い例

```java
public void processOrder(Order order) {
    // Guard clauses: 事前条件をチェックし、早期リターン
    if (order == null) {
        return;
    }
    if (!order.isValid()) {
        return;
    }
    if (order.getItems().isEmpty()) {
        return;
    }

    // メインロジック: ネストが浅く、意図が明確
    calculateTotal(order);
    saveOrder(order);
}
```

#### 注意点

- 7〜8 個以上のガード節は避ける。多すぎる場合はメソッドの分割を検討する
- ガード節は「例外ケース」をチェックし、メインロジックは「通常ケース」を処理する
- 早期リターンにより、メソッドの出口が複数になる可能性があるが、読みやすさが優先される

---

### 2. Dead Code（デッドコード）

実行されないコード、使用されていない変数、呼ばれないメソッドなどを削除する技術です。不要なコードは読み手の注意を散漫にし、保守性を低下させます。

#### 適用タイミング

- 使用されていない変数や定数が見つかった場合
- コメントアウトされたコードが残留している場合
- 呼ばれないプライベートメソッドが存在する場合
- 未使用のインポート文が残っている場合

#### ❌ 悪い例

```java
public class OrderService {

    private static final String UNUSED_CONSTANT = "legacy_value";

    public void processOrder(Order order) {
        String unusedVariable = order.getId().toString();
        // 古い実装（コメントアウト）
        // saveToLegacyDatabase(order);

        saveOrder(order);
    }

    // 呼ばれていないメソッド
    private void deprecatedMethod() {
        // ...
    }
}
```

#### ✅ 良い例

```java
public class OrderService {

    public void processOrder(Order order) {
        saveOrder(order);
    }
}
```

#### 注意点

- 削除前に本当に使用されていないことを確認する（IDE の「Find Usages」機能を活用）
- 使用されていないか疑わしい場合は、ログを追加して確認してから削除する
- Git 履歴に残るので、必要に応じて復元できる

---

### 3. Normalize Symmetries（対称性の正規化）

同様の処理が異なる形で記述されている場合、統一する技術です。パターンが似ている箇所を見つけ、共通の形にすることで、保守性を向上させます。

#### 適用タイミング

- 同様のロジックが異なる方法で実装されている場合
- 複数のメソッドで同じような処理が繰り返されている場合
- 条件分岐の構造が非対称で、理解しにくい場合

#### ❌ 悪い例

```java
public class PriceCalculator {

    public BigDecimal calculateDiscount(Member member, BigDecimal price) {
        if (member.getTier() == Tier.GOLD) {
            return price.multiply(BigDecimal.valueOf(0.9));
        } else if (member.getTier() == Tier.SILVER) {
            return price.multiply(BigDecimal.valueOf(0.95));
        } else {
            return price;
        }
    }

    public BigDecimal calculateTax(Customer customer, BigDecimal price) {
        if (customer.getRegion().equals("JP")) {
            return price.multiply(BigDecimal.valueOf(1.1));
        } else if (customer.getRegion().equals("US")) {
            return price.multiply(BigDecimal.valueOf(1.08));
        } else {
            return price;
        }
    }
}
```

#### ✅ 良い例

```java
public class PriceCalculator {

    private static final Map<Tier, BigDecimal> DISCOUNT_RATES = Map.of(
        Tier.GOLD, BigDecimal.valueOf(0.9),
        Tier.SILVER, BigDecimal.valueOf(0.95)
    );

    private static final Map<String, BigDecimal> TAX_RATES = Map.of(
        "JP", BigDecimal.valueOf(1.1),
        "US", BigDecimal.valueOf(1.08)
    );

    public BigDecimal calculateDiscount(Member member, BigDecimal price) {
        BigDecimal rate = DISCOUNT_RATES.getOrDefault(member.getTier(), BigDecimal.ONE);
        return price.multiply(rate);
    }

    public BigDecimal calculateTax(Customer customer, BigDecimal price) {
        BigDecimal rate = TAX_RATES.getOrDefault(customer.getRegion(), BigDecimal.ONE);
        return price.multiply(rate);
    }
}
```

#### 注意点

- 違いがある場合は、意図的な違いであることを明確にする
- 完全に同じ処理の場合は、共通メソッドに抽出する
- 部分的に異なる場合は、異なる部分だけをパラメータ化して統一する

---

### 4. New Interface, Old Implementation（新インターフェース、旧実装）

呼び出したいインターフェースを定義し、既存の実装をラップする技術です。将来の変更を容易にし、ソフトウェア設計の微視的な本質です。

#### 適用タイミング

- 既存のメソッドのインターフェースを変更したいが、既存の呼び出し元を変更できない場合
- 新しいインターフェースを定義して、段階的に移行したい場合
- コードの意図を明確にするために、ラッパーメソッドを作成したい場合

#### ❌ 悪い例

```java
public class OrderService {

    // 引数が多く、意図が不明確
    public void createOrder(String customerId, String productId, int quantity,
                            String address, String paymentMethod) {
        // 実装
    }
}
```

#### ✅ 良い例

```java
public class OrderService {

    // 新しいインターフェース: 意図が明確
    public void createOrder(OrderRequest request) {
        createOrderLegacy(
            request.getCustomerId(),
            request.getProductId(),
            request.getQuantity(),
            request.getAddress(),
            request.getPaymentMethod()
        );
    }

    // 既存の実装をラップ
    private void createOrderLegacy(String customerId, String productId, int quantity,
                                    String address, String paymentMethod) {
        // 既存の実装
    }
}
```

#### 注意点

- パススルーインターフェースの作成は、ソフトウェア設計の微視的な本質
- 段階的に実装を新しいインターフェースに移行できる
- 既存のコードを変更せずに、新しいインターフェースを導入できる

---

### 5. Reading Order（読み順）

コードの要素を、読む順番に並べる技術です。単一の順序が完璧であることはありませんが、文脈に合った順序を選びます。

#### 適用タイミング

- クラス内のメソッド配置が論理的な順序でない場合
- 変数の宣言と使用が離れている場合
- コードの流れを追うのが難しい場合

#### ❌ 悪い例

```java
public class OrderService {

    private OrderRepository orderRepository;
    private PaymentService paymentService;
    private NotificationService notificationService;

    // メソッドの順序が論理的でない
    public void cancelOrder(UUID orderId) { /* ... */ }
    public void createOrder(OrderRequest request) { /* ... */ }
    private void sendNotification(Order order) { /* ... */ }
    private void processPayment(Order order) { /* ... */ }
    public void updateOrder(UUID orderId, OrderUpdateRequest request) { /* ... */ }
}
```

#### ✅ 良い例

```java
public class OrderService {

    private OrderRepository orderRepository;
    private PaymentService paymentService;
    private NotificationService notificationService;

    // 公開メソッドを論理的な順序で配置
    public void createOrder(OrderRequest request) { /* ... */ }
    public void updateOrder(UUID orderId, OrderUpdateRequest request) { /* ... */ }
    public void cancelOrder(UUID orderId) { /* ... */ }

    // プライベートメソッドを公開メソッドの近くに配置
    private void processPayment(Order order) { /* ... */ }
    private void sendNotification(Order order) { /* ... */ }
}
```

#### 注意点

- 単一の順序が完璧であることはないが、文脈に合った順序を選ぶ
- 公開メソッドとプライベートメソッドの境界を明確にする
- 一緒に変更されるメソッドは近くに配置する（Cohesion Order と組み合わせる）

---

### 6. Cohesion Order（凝集順）

一緒に変更される要素を隣接して配置する技術です。凝集度を高めることで、結合度を許容しやすくなります。

#### 適用タイミング

- 関連するコードがファイル内で離れて配置されている場合
- 一緒に変更される変数やメソッドが分散している場合
- コードの保守時に、複数の箇所を変更する必要がある場合

#### ❌ 悪い例

```java
public class Order {

    private UUID id;
    private String customerName;
    private List<OrderItem> items;
    private BigDecimal totalAmount;
    private OrderStatus status;
    private LocalDate createdAt;
    private String shippingAddress;
    private BigDecimal discount;

    // 関連するメソッドが離れている
    public UUID getId() { return id; }
    public String getCustomerName() { return customerName; }
    public List<OrderItem> getItems() { return items; }
    public OrderStatus getStatus() { return status; }

    // ... 他のメソッド ...

    public BigDecimal getTotalAmount() { return totalAmount; }
    public void calculateTotal() { /* ... */ }
    public BigDecimal getDiscount() { return discount; }
    public void applyDiscount(BigDecimal discount) { /* ... */ }
}
```

#### ✅ 良い例

```java
public class Order {

    // 基本情報
    private UUID id;
    private String customerName;
    private OrderStatus status;
    private LocalDate createdAt;

    // 商品情報
    private List<OrderItem> items;

    // 金額情報
    private BigDecimal totalAmount;
    private BigDecimal discount;

    // 配送情報
    private String shippingAddress;

    // 基本情報のアクセッサ
    public UUID getId() { return id; }
    public String getCustomerName() { return customerName; }
    public OrderStatus getStatus() { return status; }

    // 商品情報のアクセッサ
    public List<OrderItem> getItems() { return items; }

    // 金額情報のアクセッサとメソッド
    public BigDecimal getTotalAmount() { return totalAmount; }
    public void calculateTotal() { /* ... */ }
    public BigDecimal getDiscount() { return discount; }
    public void applyDiscount(BigDecimal discount) { /* ... */ }

    // 配送情報のアクセッサ
    public String getShippingAddress() { return shippingAddress; }
}
```

#### 注意点

- 凝集度を高めることで、結合度を許容しやすくなる
- 一緒に変更される要素を隣接して配置する
- Reading Order と組み合わせることで、より効果的

---

### 7. Move Declaration And Initialization Together（宣言と初期化を一緒に移動）

変数の宣言と初期化を近くに配置する技術です。使用箇所から遠く離れた宣言は避けます。

#### 適用タイミング

- 変数の宣言と初期化が離れている場合
- 変数の使用箇所から遠く離れた場所で宣言されている場合
- 変数のスコープが不必要に広い場合

#### ❌ 悪い例

```java
public class OrderService {

    public void processOrder(OrderRequest request) {
        BigDecimal totalAmount;
        String customerName;
        List<OrderItem> items;

        // ... 他の処理 ...

        customerName = request.getCustomerName();
        items = request.getItems();

        // ... 他の処理 ...

        totalAmount = calculateTotal(items);

        // ... 他の処理 ...

        saveOrder(customerName, items, totalAmount);
    }
}
```

#### ✅ 良い例

```java
public class OrderService {

    public void processOrder(OrderRequest request) {

        // ... 他の処理 ...

        String customerName = request.getCustomerName();
        List<OrderItem> items = request.getItems();

        // ... 他の処理 ...

        BigDecimal totalAmount = calculateTotal(items);

        // ... 他の処理 ...

        saveOrder(customerName, items, totalAmount);
    }
}
```

#### 注意点

- 変数の宣言と初期化を近くに配置する
- 使用箇所から遠く離れた宣言は避ける
- 変数のスコープを最小限に抑える

---

### 8. Explaining Variables（説明変数）

複雑な式の結果を、意味のある名前の変数に代入する技術です。コードの意図を明確にします。

#### 適用タイミング

- 複雑な式が直接使用されている場合
- 式の意味が一目で理解できない場合
- 同じ式が複数箇所で繰り返されている場合

#### ❌ 悪い例

```java
public class OrderService {

    public boolean isEligibleForDiscount(Order order) {
        return order.getCreatedAt().isBefore(LocalDate.now().minusMonths(6))
            && order.getTotalAmount().compareTo(BigDecimal.valueOf(10000)) >= 0
            && !order.getCustomer().getTier().equals(Tier.BASIC);
    }
}
```

#### ✅ 良い例

```java
public class OrderService {

    public boolean isEligibleForDiscount(Order order) {
        boolean isOldCustomer = order.getCreatedAt().isBefore(LocalDate.now().minusMonths(6));
        boolean meetsMinimumAmount = order.getTotalAmount().compareTo(BigDecimal.valueOf(10000)) >= 0;
        boolean isPremiumTier = !order.getCustomer().getTier().equals(Tier.BASIC);

        return isOldCustomer && meetsMinimumAmount && isPremiumTier;
    }
}
```

#### 注意点

- 複雑な式の結果を、意味のある名前の変数に代入する
- コードの意図を明確にする
- 変数名自体がドキュメントとなるようにする

---

### 9. Explaining Constants（説明定数）

マジックナンバーやマジック文字列を、意味のある定数で置き換える技術です。コードの意図を明確にし、保守性を向上させます。

#### 適用タイミング

- コード中にマジックナンバーやマジック文字列が直接使用されている場合
- 同じ値が複数箇所で繰り返されている場合
- 値の意味が一目で理解できない場合

#### ❌ 悪い例

```java
public class OrderService {

    public BigDecimal calculateTax(BigDecimal amount) {
        if (region.equals("JP")) {
            return amount.multiply(BigDecimal.valueOf(1.1));
        } else if (region.equals("US")) {
            return amount.multiply(BigDecimal.valueOf(1.08));
        }
        return amount;
    }

    public boolean isExpired(Order order) {
        return order.getCreatedAt().isBefore(LocalDate.now().minusDays(90));
    }
}
```

#### ✅ 良い例

```java
public class OrderService {

    private static final BigDecimal JAPAN_TAX_RATE = BigDecimal.valueOf(1.1);
    private static final BigDecimal US_TAX_RATE = BigDecimal.valueOf(1.08);
    private static final long ORDER_EXPIRY_DAYS = 90;

    public BigDecimal calculateTax(BigDecimal amount) {
        if (region.equals("JP")) {
            return amount.multiply(JAPAN_TAX_RATE);
        } else if (region.equals("US")) {
            return amount.multiply(US_TAX_RATE);
        }
        return amount;
    }

    public boolean isExpired(Order order) {
        return order.getCreatedAt().isBefore(LocalDate.now().minusDays(ORDER_EXPIRY_DAYS));
    }
}
```

#### 注意点

- マジックナンバーやマジック文字列を、意味のある定数で置き換える
- 定数はクラスレベルで定義し、一元的に管理する
- 定数名は UPPER_SNAKE_CASE で命名する

---

### 10. Explicit Parameters（明示的なパラメータ）

暗黙的な依存関係を明示的なパラメータにする技術です。メソッドの契約を明確にします。

#### 適用タイミング

- メソッドが暗黙的にクラスフィールドに依存している場合
- メソッドの引数が汎用的なオブジェクト（Map, Context など）である場合
- メソッドの依存関係が不明確な場合

#### ❌ 悪い例

```java
public class OrderService {

    private Context context;

    public void processOrder(Order order) {
        // 暗黙的に context に依存
        String currency = context.getCurrency();
        String locale = context.getLocale();
        BigDecimal taxRate = context.getTaxRate();

        order.calculateTotal(currency, locale, taxRate);
    }
}
```

#### ✅ 良い例

```java
public class OrderService {

    public void processOrder(Order order, String currency, String locale, BigDecimal taxRate) {
        // 明示的なパラメータで依存関係が明確
        order.calculateTotal(currency, locale, taxRate);
    }
}
```

#### 注意点

- 暗黙的な依存関係を明示的なパラメータにする
- メソッドの契約を明確にする
- パラメータが多すぎる場合は、パラメータオブジェクトを検討する

---

### 11. Chunk Statements（チャンク文）

関連する文をグループ化し、空白行で区切る技術です。コードの論理的な構造を視覚的に表現します。

#### 適用タイミング

- コードが一塊になっていて、論理的な区切りが不明確な場合
- 関連する処理が空白行で区切られていない場合
- コードの構造が視覚的に把握しにくい場合

#### ❌ 悪い例

```java
public class OrderService {

    public void processOrder(OrderRequest request) {
        Order order = createOrder(request);
        validateOrder(order);
        calculateTotal(order);
        applyDiscount(order);
        calculateTax(order);
        saveOrder(order);
        sendNotification(order);
        updateInventory(order);
        logOrderProcessing(order);
    }
}
```

#### ✅ 良い例

```java
public class OrderService {

    public void processOrder(OrderRequest request) {
        // 注文の作成
        Order order = createOrder(request);
        validateOrder(order);

        // 金額の計算
        calculateTotal(order);
        applyDiscount(order);
        calculateTax(order);

        // 永続化
        saveOrder(order);

        // 副作用
        sendNotification(order);
        updateInventory(order);
        logOrderProcessing(order);
    }
}
```

#### 注意点

- 関連する文をグループ化し、空白行で区切る
- コードの論理的な構造を視覚的に表現する
- コメントと組み合わせることで、意図をより明確にできる

---

### 12. Extract Helper（ヘルパー抽出）

複雑な処理を独立したメソッドに抽出する技術です。メソッドの責務を单一化し、名前によって意図を明確にします。

#### 適用タイミング

- メソッドが複数の責務を持っている場合
- コードのブロックが明確な目的を持っている場合
- メソッドの長さが長くなり、理解しにくくなっている場合

#### ❌ 悪い例

```java
public class OrderService {

    public void processOrder(OrderRequest request) {
        Order order = new Order();
        order.setCustomerId(request.getCustomerId());
        order.setProductId(request.getProductId());
        order.setQuantity(request.getQuantity());

        Product product = productRepository.findById(request.getProductId());
        order.setUnitPrice(product.getPrice());
        order.setTotalAmount(product.getPrice().multiply(BigDecimal.valueOf(request.getQuantity())));

        Member member = memberRepository.findById(request.getCustomerId());
        if (member.getTier() == Tier.GOLD) {
            order.applyDiscount(BigDecimal.valueOf(0.9));
        } else if (member.getTier() == Tier.SILVER) {
            order.applyDiscount(BigDecimal.valueOf(0.95));
        }

        orderRepository.save(order);

        notificationService.sendOrderConfirmation(order);
    }
}
```

#### ✅ 良い例

```java
public class OrderService {

    public void processOrder(OrderRequest request) {
        Order order = createOrderFromRequest(request);
        order = applyMemberDiscount(order, request.getCustomerId());
        orderRepository.save(order);
        notificationService.sendOrderConfirmation(order);
    }

    private Order createOrderFromRequest(OrderRequest request) {
        Order order = new Order();
        order.setCustomerId(request.getCustomerId());
        order.setProductId(request.getProductId());
        order.setQuantity(request.getQuantity());

        Product product = productRepository.findById(request.getProductId());
        order.setUnitPrice(product.getPrice());
        order.setTotalAmount(product.getPrice().multiply(BigDecimal.valueOf(request.getQuantity())));

        return order;
    }

    private Order applyMemberDiscount(Order order, String customerId) {
        Member member = memberRepository.findById(customerId);
        if (member.getTier() == Tier.GOLD) {
            order.applyDiscount(BigDecimal.valueOf(0.9));
        } else if (member.getTier() == Tier.SILVER) {
            order.applyDiscount(BigDecimal.valueOf(0.95));
        }
        return order;
    }
}
```

#### 注意点

- 複雑な処理を独立したメソッドに抽出する
- メソッドの責務を单一化し、名前によって意図を明確にする
- 抽出したメソッドはプライベートメソッドとして定義する

---

### 13. One Pile（一つの山）

同種の要素を一つのコレクションにまとめる技術です。分散した管理を一元化して保守性を向上させます。

#### 適用タイミング

- 同種のデータが複数の変数やコレクションに分散している場合
- 条件分岐で異なるコレクションを処理している場合
- 分散した管理が保守性を低下させている場合

#### ❌ 悪い例

```java
public class OrderService {

    private List<Order> pendingOrders;
    private List<Order> processingOrders;
    private List<Order> completedOrders;
    private List<Order> cancelledOrders;

    public List<Order> getAllOrders() {
        List<Order> all = new ArrayList<>();
        all.addAll(pendingOrders);
        all.addAll(processingOrders);
        all.addAll(completedOrders);
        all.addAll(cancelledOrders);
        return all;
    }
}
```

#### ✅ 良い例

```java
public class OrderService {

    private List<Order> orders;

    public List<Order> getPendingOrders() {
        return orders.stream()
            .filter(order -> order.getStatus() == OrderStatus.PENDING)
            .collect(Collectors.toList());
    }

    public List<Order> getProcessingOrders() {
        return orders.stream()
            .filter(order -> order.getStatus() == OrderStatus.PROCESSING)
            .collect(Collectors.toList());
    }

    public List<Order> getCompletedOrders() {
        return orders.stream()
            .filter(order -> order.getStatus() == OrderStatus.COMPLETED)
            .collect(Collectors.toList());
    }

    public List<Order> getCancelledOrders() {
        return orders.stream()
            .filter(order -> order.getStatus() == OrderStatus.CANCELLED)
            .collect(Collectors.toList());
    }

    public List<Order> getAllOrders() {
        return orders;
    }
}
```

#### 注意点

- 同種の要素を一つのコレクションにまとめる
- 分散した管理を一元化して保守性を向上させる
- フィルタリングや検索はストリーム API を活用する

---

### 14. Explaining Comments（説明コメント）

「なぜ」その実装を選んだかをコメントで説明する技術です。「How」はコード自体が表現します。

#### 適用タイミング

- 実装の選択理由がコードからは不明確な場合
- 代替案を採用しなかった理由を説明したい場合
- 複雑なロジックの背景にある意図を伝えたい場合

#### ❌ 悪い例

```java
public class OrderService {

    // 注文を保存する
    public void saveOrder(Order order) {
        // データベースに保存
        orderRepository.save(order);

        // 通知を送信
        notificationService.sendOrderConfirmation(order);
    }
}
```

#### ✅ 良い例

```java
public class OrderService {

    /**
     * 注文を保存し、確認通知を送信します。
     *
     * 通知はトランザクション外で送信するため、
     * データベース保存が失敗しても通知は送信されません。
     * これは、注文が確定してから通知を送信するビジネスルールに基づいています。
     */
    public void saveOrder(Order order) {
        orderRepository.save(order);
        notificationService.sendOrderConfirmation(order);
    }
}
```

#### 注意点

- 「なぜ」その実装を選んだかをコメントで説明する
- 「How」はコード自体が表現する
- コメントはコードの意図を補完し、コード自体では表現できない背景を説明する

---

### 15. Delete Redundant Comments（冗長なコメントの削除）

コードで明確なコメントは削除する技術です。outdated なコメントは誤解を招きます。

#### 適用タイミング

- コードの内容を単に繰り返すコメントがある場合
- 実装が変わってもコメントが更新されていない場合
- コメントがコードの理解を妨げている場合

#### ❌ 悪い例

```java
public class OrderService {

    // getOrderById メソッド
    public Order getOrderById(UUID id) {
        // id で注文を検索
        return orderRepository.findById(id)
            // 見つからない場合は例外をスロー
            .orElseThrow(() -> new OrderNotFoundException(id));
    }

    // 注文をキャンセルするメソッド（2024/01/15 追加）
    public void cancelOrder(UUID orderId) {
        Order order = getOrderById(orderId);
        order.cancel();
        orderRepository.save(order);
    }
}
```

#### ✅ 良い例

```java
public class OrderService {

    public Order getOrderById(UUID id) {
        return orderRepository.findById(id)
            .orElseThrow(() -> new OrderNotFoundException(id));
    }

    public void cancelOrder(UUID orderId) {
        Order order = getOrderById(orderId);
        order.cancel();
        orderRepository.save(order);
    }
}
```

#### 注意点

- コードで明確なコメントは削除する
- outdated なコメントは誤解を招く
- コード自体が自明である場合は、コメントは不要

## Part II. Managing（チーム管理）と Part III. Theory（理論）

本ガイドラインでは Part I の 15 の Tidying 技術に焦点を当てています。Part II（チームでの Tidying の管理方法）と Part III（経済的視点やソフトウェア設計の理論）については、書籍を参照してください。

## 参考資料

- Kent Beck, "Tidy First?: A Personal Exercise in Empirical Software Design", O'Reilly Media, 2023
- [Book Summary](https://www.workingsoftware.dev/summary-of-tidy-first-book)
- [SE Radio 615: Kent Beck on Tidy First?](https://se-radio.net/2024/05/se-radio-615-kent-beck-on-tidy-first/)
