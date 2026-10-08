アンチパターン
=====

# 1. enumの利用

データベースから返されたままの型を持ち込んでいるため、せっかくドメイン用のEnumを定義していても定数としてしか使えていないことが示唆される。

## enumの定義例

```java
enum CompleteFlg {
  HORYU("0"),
  SHORICHU("1"),
  KANRYO("2");

  private final String code;

  CompleteFlg(String code) {
    this.code = code;
  }

  // コードから対応する列挙型を取得するメソッド
  public static CompleteFlg fromCode(String code) {
    for (CompleteFlg flg : values()) {
      if (flg.code.equals(code)) {
        return flg;
      }
    }
    throw new IllegalArgumentException("Invalid code: " + code);
  }

  public String getCode() {
    return code;
  }
}

```

## enumの利用箇所

```java
    if(!order.completeFlg.equals(CompleteFlg.KANRYO.code)){
    // 未完了時の処理
    }
```

```java
// 改善されたコード例
if(order.getCompleteFlg() !=CompleteFlg.KANRYO){
    // 未完了時の処理
    }

```

# 2. Listのget(0)や配列[0]

外部APIやデータベースアクセスライブラリの(使い方の)都合で、本来1件しかないものがListや配列で返ってくる。
これをそのままドメインのコードまで持ち込んでしまうと、各所で先頭だけ取るコードが量産される。
(何ならそこで例外発生することも)

```java
// 問題のある記述
Order currentOrder = orders.get(0);
```

#### 1. 安全性の脆弱性

- **リスク**: 空のリストで `IndexOutOfBoundsException` 発生
- **課題**: 暗黙的な最初の要素取得

#### 2. エラーハンドリングの欠陥

- リストの状態に対する防御的プログラミングの欠如
- 明示的な例外処理の不足

### 🛠️ 推奨改善戦略

#### Optional利用アプローチ

``` java
Optional<Order> currentOrderOptional = orders.stream().findFirst();
currentOrderOptional.ifPresent(order -> {
    // 安全な処理
});
```

#### null/空リストチェック

``` java
if (orders != null && !orders.isEmpty()) {
    Order currentOrder = orders.get(0);
    // 条件付き処理
} else {
    // 代替処理
    handleEmptyOrderList();
}
```

#### ドメインロジックによるカプセル化

``` java
public Order getCurrentOrder(List<Order> orders) {
    Optional.ofNullable(orders)
        .filter(list -> !list.isEmpty())
        .map(list -> list.get(0))
        .orElseThrow(() -> new NoOrderFoundException("注文が見つかりません"));
}
```

### ✅ 改善のポイント

1. コード安全性の向上
2. 明示的なエラーハンドリング
3. 意図の明確化
4. 例外リスクの軽減

### 🔍 追加検討事項

- コンテキストに応じた最適解の選択
- ドメイン要件の慎重な評価
- 一貫性のある例外処理戦略

### ⚠️ アンチパターンの危険性

| リスク          | 影響       |
|--------------|----------|
| 暗黙的要素取得      | 予期せぬ例外   |
| 防御的チェックの欠如   | システム不安定性 |
| エラーハンドリング未実装 |

# 3. パースを全部生成する側がやる

ドメインオブジェクトを使う側が属性の型変換全てをやる。ドメインオブジェクトの内部の型やデータの持ち方に依存することになる。

```java
Order order = new Order(
    UUID.fromString(orderId),
    OrderStatus.getByCode(Integer.parseInt(orderstatus)),
    LocalDate.parse("yyyyMMdd", biddingStartYmd),
    LocalDate.parse("yyyyMMdd", biddingEndYmd)
);

```

### ✅ 改善のメリット

1. 型変換ロジックのカプセル化
2. コード重複の排除
3. メンテナンス性の向上
4. 柔軟性の確保

### ⚠️ 追加検討事項

| 観点      | 推奨事項             |
|---------|------------------|
| 型変換     | ドメインオブジェクト内部で管理  |
| 責務分離    | 変換ロジックを専用メソッドに集約 |
| バリデーション | 変換時に入力検証を実施      |

### 🎯 最終目標

- 型変換の責務をドメインオブジェクト側に移譲
- クライアントコードの単純化
- システム全体の柔軟性向上
