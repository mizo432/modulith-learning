アンチパターン
=====

# 1. enumの利用

データベースから返されたままの型を持ち込んでいるため、せっかくドメイン用のEnumを定義していても定数としてしか使えていないことが示唆される。

## enumの定義例

```java
enum CompleteFlg {
  HORYU("0"), SHORICHU("1"), KANRYO("2");
  public String code;
}
```

## enumの利用箇所

```java
    if(!order.completeFlg.equals(CompleteFlg.KANRYO.code)){
    // 未完了時の処理
    }
```

# 2. Listのget(0)や配列[0]

外部APIやデータベースアクセスライブラリの(使い方の)都合で、本来1件しかないものがListや配列で返ってくる。
これをそのままドメインのコードまで持ち込んでしまうと、各所で先頭だけ取るコードが量産される。
(何ならそこで例外発生することも)

```java
Order currentOrder = orders.get(0);
```

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
