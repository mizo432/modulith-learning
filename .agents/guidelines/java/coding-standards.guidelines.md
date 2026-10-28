# Java コーディングルールガイドライン

本ガイドラインは、modulith-learning プロジェクトにおける Java コードの記述規則を定義します。

## 1. コードスタイル

- **Google Java Style** を基本とし、インデントや命名規則をプロジェクト既存コードに統一します。
- イミュータブルなデータ構造には Java `record` を積極的に使用します。
- Lombok アノテーション（`@Getter`, `@Setter`, `@RequiredArgsConstructor`, `@UtilityClass`
  等）でボイラープレートを排除します。
- JSpecify アノテーション（`@NonNull`, `@Nullable`）を付与し、Null 安全性を保証します。

## 2. 命名規則

- クラス名: PascalCase（例: `CustomerService`, `OrderRepository`）
- メソッド名・変数名: camelCase（例: `getCustomerById`, `orderList`）
- 定数: UPPER_SNAKE_CASE（例: `MAX_RETRY_COUNT`, `DEFAULT_TIMEOUT`）
- パッケージ名: 小文字のドット区切り（例: `undecided.generic.addressReg`）

## 3. パッケージ構造

- モジュールごとにパッケージを分離し、`undecided` 配下に配置します。
- 公開インターフェース / SPI は `spi` またはモジュールのルート直下に配置します。
- 内部実装は `internal` パッケージに配置し、他モジュールからの直接参照を禁止します。

## 4. レイヤードアーキテクチャ

各モジュール内部はオニオンアーキテクチャ / クリーンアーキテクチャの原則に従って層を分離します。

1. **Domain 層**: 業務ルール、エンティティ、値オブジェクト、ドメインサービス、リポジトリインターフェース
2. **Business / Application 層**: ユースケースの調整、Command/Query ハンドラー、トランザクション境界
3. **Infrastructure 層**: JPA エンティティ、Spring Data JPA リポジトリ実装、外部通信
4. **Presentation 層**: REST コントローラー、リクエスト/レスポンス DTO、バリデーション

## 5. 例外処理

- **業務エラー (想定される例外)**: `BusinessException`, `NotFoundBusinessException` 等を使用し、HTTP
  4xx として返却します。
- **システム障害 (予期せぬ例外)**: `SystemException` を使用し、HTTP 500 として扱います。
- **事前条件検証**: `undecided.supporting.precondition.*` パッケージのクラスを活用します。

## 6. コメントの原則

| 媒体           | 記載内容 | 説明                                                   |
|:---------------|:---------|:-------------------------------------------------------|
| コード         | How      | 実装自体が「どのように」動作するかを明確に表現する     |
| テストコード   | What     | テストが「何を」検証しているかを明確に表現する         |
| コミットログ   | Why      | 変更の背景や意図、つまり「なぜ」変更したかを記載する   |
| コードコメント | Why not  | 実装の選択理由や、代替案を採用しなかった理由を記載する |

### 6.1 Javadoc コメント

- Javadoc コメントは必ず**日本語**で記述します。
- `package-info.java`、クラス、メソッド、フィールドのJavadocは、プロジェクトの共通言語として日本語を使用し、すべての開発者が理解できる様にします。

## 7. バッチ処理 (Spring Batch)

インポート等のバッチ処理はモジュールの `internal/batch` 配下に配置します。

### 7.1 パッケージ構造

| パッケージ  | 役割                                      |
|:------------|:------------------------------------------|
| `config`    | Job / Step の定義 (`*ImportBatchConfig`)  |
| `reader`    | 入力の読み込み（CSV は Super CSV を利用） |
| `processor` | 入力 DTO からドメインモデルへの変換・検証 |
| `writer`    | 永続化                                    |
| `dto`       | 入力レコードの DTO                        |
| `operator`  | Job の起動 (`*ImportJobOperator`)         |

- 取り込み元データは `backend/src/main/resources/data/` に配置します。
- `application-test.yml` では `spring.batch.job.enabled: false` とし、テストから明示的に起動します。

### 7.2 大量レコード処理のベストプラクティス

**チャンクサイズの設定**

- デフォルトのチャンクサイズは `50` とし、メモリ使用量とトランザクションのバランスを取ります。
- 大量レコードの場合は、チャンクサイズを `100` 〜 `500` の範囲で調整します。
- チャンクサイズは `*ImportBatchConfig` で `CHUNK_SIZE` 定数として定義し、一元的に管理します。

**エラー処理とフォールトトレランス**

- Step は必ず `.faultTolerant()` を設定し、個々のレコードエラーが全体処理を停止しないようにします。
- `.skip(Exception.class)` と `.skipLimit(N)` でスキップする例外の種類と上限を定義します。
- `.retry(Exception.class)` と `.retryLimit(N)` で一時的なエラーに対するリトライを設定します。

**メモリ効率の最適化**

- Reader はストリーミング方式で実装し、全データをメモリに読み込まないようにします。
- Writer は `saveAll()` などの一括保存メソッドを使用し、トランザクション内でまとめて永続化します。
- Processor では不要な中間データを保持せず、変換済みのオブジェクトのみを返します。

**パフォーマンス測定とログ出力**

- Job の開始・終了、各 Step の処理件数、スキップ件数、エラー件数をログに出力します。
- 大量レコード処理の場合は、一定間隔（例: 1,000件ごと）で処理進捗をログに出力します。

**データ検証と整合性**

- Processor で入力データの必須項目を検証し、不正なデータはログに記録してスキップします。
- 既存データとの整合性を確認し、重複登録や不一致を防ぎます。

## 8. テスト規約

- テストメソッド名は必ず `should` で開始します。
- テストクラスおよびメソッドに日本語の `@DisplayName` を付与します。
- `@Nested` でテストケースをグループ化します。
- AssertJ を使用し、`.as("...")` でアサーションの意図を明記します。
- テストクラス・メソッドは `package-private` とします。
- 境界値・異常値（`null`, `""`, `" "`, 最大/最小値）を必ずテストします。

## 9. アンチパターン

以下のアンチパターンは避けてください。

### 9.1 enumのコード値を直接比較しない

データベースから返されたコード値をそのまま比較するのではなく、enum自体を比較するようにします。

**❌ 悪い例**:

```java
if(!order.completeFlg.equals(CompleteFlg.KANRYO.code)){
    // 未完了時の処理
    }
```

**✅ 良い例**:

```java
if(order.getCompleteFlg() !=CompleteFlg.KANRYO){
    // 未完了時の処理
    }
```

データベースのコード値からenumへの変換は、enum側で `fromCode()` メソッド等によりカプセル化します。

### 9.2 Listのget (0)や配列[0]で先頭要素を取得しない

外部APIやデータベースアクセスライブラリからListや配列で返された結果を、そのまま `get(0)`
で先頭要素を取得するのは避けてください。

**リスク**:

- 空のリストで `IndexOutOfBoundsException` が発生する
- 暗黙的な最初の要素取得により意図が不明確になる
- 防御的プログラミングの欠如

**❌ 悪い例**:

```java
Order currentOrder = orders.get(0);
```

**✅ 良い例**:

```java
Optional<Order> currentOrderOptional = orders.stream().findFirst();
currentOrderOptional.

ifPresent(order ->{
    // 安全な処理
    });
```

またはドメインロジックでカプセル化する:

```java
public Order getCurrentOrder(List<Order> orders) {
  return Optional.ofNullable(orders)
      .filter(list -> !list.isEmpty())
      .map(list -> list.get(0))
      .orElseThrow(() -> new NoOrderFoundException("注文が見つかりません"));
}
```

### 9.3 パース処理をドメインオブジェクトの外部で行わない

ドメインオブジェクトを使う側が属性の型変換全てを行うのは避けてください。ドメインオブジェクトの内部の型やデータの持ち方に依存することになります。

**❌ 悪い例**:

```java
Order order = new Order(
    UUID.fromString(orderId),
    OrderStatus.getByCode(Integer.parseInt(orderstatus)),
    LocalDate.parse("yyyyMMdd", biddingStartYmd),
    LocalDate.parse("yyyyMMdd", biddingEndYmd)
);
```

**✅ 良い例**:

ドメインオブジェクト側で型変換をカプセル化し、クライアントコードを単純化します。

```java
Order order = Order.fromDto(orderId, orderstatus, biddingStartYmd, biddingEndYmd);
```

型変換の責務をドメインオブジェクト側に移譲することで、コード重複の排除、メンテナンス性の向上、柔軟性の確保が図れます。
