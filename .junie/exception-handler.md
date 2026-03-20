Exception Handling in Spring Boot: A Complete Guide for Production Applications
-----
アプリケーションが午前3時にクラッシュし、アラートで起こされたとき、優れた例外処理と劣悪な例外処理の違いは明白になります。優れた例外処理とは、問題を数分で特定できることを意味します。一方、劣悪な例外処理とは、ログを何時間も調べ、問題を再現しようと試み、顧客を苛立たせることを意味します。

このガイドでは、実際のeコマース注文管理システムを例として、Spring
Bootで本番環境レベルの例外処理を構築する方法を解説します。基本的な概念から、大規模アプリケーションで使用される高度なパターンまで、すべてを網羅します。

例外処理が重要な理由
例外処理の基本原則
例外分類
Spring Bootアプリケーションにおける例外処理の流れ
エラー契約設計（APIエラー応答）
エラーコード設計ガイドライン
HTTPステータスコードマッピング戦略
Spring Bootにおけるグローバル例外処理
検証例外処理
ビジネス例外処理
インフラストラクチャと統合のエラー
例外処理のためのログ記録戦略
可観測性とモニタリング
セキュリティに関する考慮事項
再試行、冪等性、および回復力
例外処理のテスト
ドキュメントとAPI契約
避けるべきよくある間違い
生産・保守の観点から

1. 例外処理が重要な理由
   実際のシナリオから始めましょう。eコマースプラットフォームを構築していると想像してください。顧客が注文しようとしますが、何らかの問題が発生します。例外処理が不適切な場合、次のようなことが起こります。

悪いアプローチ：

@PostMapping("/orders")
public Order createOrder ( @RequestBody OrderRequest request) {
try {
return orderService.createOrder(request);
} catch (Exception e) {
e.printStackTrace(); // 本番環境ではスタックトレースが出力されます！
return null ; // クライアントは何が起こったのか分かりません
}
}
顧客には何も表示されません。ログにはスタックトレースが記録されています。支払いエラー、在庫不足、または無効な住所のいずれが原因だったのか、誰も分かりません。

良いアプローチ：

@PostMapping("/orders")
public ResponseEntity<OrderResponse> createOrder ( @RequestBody OrderRequest request) {
OrderResponse response = orderService.createOrder(request);
return ResponseEntity.status(HttpStatus.CREATED).body(response);
} // 例外は、 @RestControllerAdvice を使用して
、適切なエラーコードとメッセージでグローバルに処理されます。
例外が発生した場合、顧客には「製品XYZは在庫切れです」という明確なメッセージが表示されます。監視システムによって、どのエラーコードが急増したかが正確に把握できるため、迅速に問題を解決できます。

不適切な例外処理がもたらす損失：
顧客の混乱: 「何らかの問題が発生しました」のような一般的なエラーメッセージは、ユーザーを苛立たせます。
セキュリティリスク：スタックトレースによって、コード構造、データベース名、内部パスが漏洩する可能性があります。
デバッグの悪夢：適切なコンテキストがなければ、本番環境のバグを修正するのに何時間もかかります。
監視体制の不備：適切なコードがないと、どのエラーが増加しているかを追跡できません。
API契約違反：クライアントはプログラムでエラーを処理できません。
思考の根本的な変化：
失敗した場合→ null を返し、例外を無視し、ログを記録して先に進みます
適切なエラー処理→ 明確なエラー応答、適切なログ記録、実行可能なフィードバック
例外処理は、単なるエラー処理コードではありません。成功時のレスポンスと同様に、API契約の一部です。

2. 例外処理の基本原則
   コードを書く前に、以下の重要なルールを理解しておきましょう。

原則1：例外はシステム設計の一部である
例外処理を後回しにしてはいけません。機能を設計する際には、次の点を自問自答してください。

何が問題になる可能性があるだろうか？
顧客はどのように回復すべきか？
彼らはどのような情報を必要としているのか？
当社のeコマースシステムについて：

チェックアウト時に在庫がなくなったらどうなりますか？
支払いが失敗した場合はどうなりますか？
配送先住所が無効な場合はどうなりますか？
原則2：業務上のミス ≠ システム障害
ビジネスエラー：顧客が100個の商品を購入しようとしたが、在庫は5個しかなかった。

これは想定される動作です
HTTP 400 または 409
警告レベルでログを記録
顧客がこれを修正できます
システム障害：データベース接続タイムアウト

これは予想外だ
HTTP 500 または 503
エラーレベルでログを記録
お客様はこれを修正できません、あなたは
原則3：クライアントメッセージ≠開発者メッセージ
// 悪い例: 両方に同じメッセージ
throw new RuntimeException ( "FK_CONSTRAINT_VIOLATION: orders_customer_id_fkey" );

// 良い例: 対象者ごとに異なるメッセージ
throw new ResourceNotFoundException (
"顧客が見つかりません" , // クライアント用
"FK violation: customer_id=123"  // 開発者ログ用
);
原則4：内部の詳細を決して公開しない
クライアントに送ってはいけないもの：

データベースの列名
SQLクエリ
内部ファイルパス
スタックトレース
サーバーバージョン
内部サービス名
原則5：エラーは一貫性があり、予測可能で、機械可読でなければならない。
すべてのエラー応答は同じ構造に従う必要があります。クライアントは以下の操作を実行できる必要があります。

解析エラーをプログラムで処理する
適切なメッセージを表示する
再試行ロジックを実装する
エラーパターンを追跡する
原則6：一元的な処理（唯一の信頼できる情報源）
例外処理ロジックはすべて一箇所にまとめてください。try-catchブロックをあちこちに散在させないでください。そのためには、
Springの@ControllerAdviceまたは@RestControllerAdviceを使用します。

3. 例外分類
   当社のeコマースシステムでは、様々な種類の例外に遭遇します。それらを適切に分類していきましょう。

A. 責任によって

1. 検証例外
   クライアントから無効なデータが送信されました。

public class OrderRequest {
@NotNull(message = "顧客IDは必須です")
private Long customerId;

    @NotEmpty(message = "注文には少なくとも1つの商品が必要です") 
    private List<OrderItem> items; 
    
    @Valid 
    private ShippingAddress shippingAddress; 

}

public class ShippingAddress {
@NotBlank(message = "番地は必須です")
private String street;

    @Pattern(regexp = "\\d{6}", message = "PINコードは6桁である必要があります") 
    private String pinCode; 

}

2. ビジネス/ドメイン例外
   業務規則違反。

// 顧客が 100 個の商品を注文しようとしたが、在庫は 5 個しかない
throw new InsufficientInventoryException (
"商品: Laptop の在庫は 5 個のみです"
);

// 顧客が既に発送済みの注文をキャンセルしようとした
throw new InvalidOrderStateException (
"SHIPPED ステータスの注文はキャンセルできません"
);

// 顧客のウォレット残高が不足している
throw new InsufficientBalanceException (
"残高: ₹500、必要額: ₹2000"
);

3. 認可／認証の例外
   ユーザーに権限がありません。

// 顧客が他の顧客の注文を表示しようとした場合
throw new UnauthorizedException (
"この注文を表示する権限がありません"
);

// JWTトークンの有効期限が切れた場合
throw new AuthenticationException (
"セッションの有効期限が切れました。再度ログインしてください。"
);

4. インフラストラクチャの例外
   外部依存関係のエラーが発生しています。

// データベースタイムアウト
throw new DatabaseConnectionException (
"リクエストを処理できません。もう一度お試しください。"
);

// 決済ゲートウェイタイムアウト
throw new PaymentGatewayException (
"決済処理に失敗しました。金額は請求されません。"
);

// サードパーティの配送APIがダウン
throw new ShippingServiceException (
"配送料を計算できません。後でもう一度お試しください。"
);

5. 未知の/予期せぬ例外
   その他すべて。

// NullPointerException、ArrayIndexOutOfBoundsExceptionなど
// コードの品質が良ければ、本番環境ではこれらの例外はまれにしか発生しないはずです
B. 回復可能性による
再試行可能（一時的な障害）
データベースのデッドロック -> トランザクションを再試行
ネットワークタイムアウト -> API呼び出しを再試行
レート制限を超過しました -> 遅延後に再試行
@Retryable(
value = {TransientDataAccessException.class},
maxAttempts = 3,
backoff = @Backoff(delay = 1000)
)
public Order createOrder (OrderRequest request) {
// 注文作成ロジック
}
再試行不可（永続的な障害）
検証エラー -> 自動的に修正されません
在庫不足 -> すぐには解決しません
重複注文 -> 再試行しても絶対に修正されません
C. チェックあり vs チェックなし
最新のSpring Bootアプリケーションでは、チェックされない例外（RuntimeException）を好んで使用します。

なぜ？

// チェック例外はメソッドのシグネチャを汚染します
public Order createOrder (OrderRequest request)  
throws ValidationException,
InsufficientInventoryException,
PaymentException,
DatabaseException ()
// すべてのレイヤーでこれらの例外を宣言または処理する必要があります
}

// チェックされない例外はコードをクリーンに保ちます
public Order createOrder (OrderRequest request) {
// 例外は自然に伝播します
// グローバルハンドラがそれらをキャッチします
}
チェック例外はいつ使うべきか？めったに使わない。以下の場合にのみ使う。

発信者に処理を強制したい
これは、その特定の層における回復可能なエラーです。
レガシーコードとの統合にはそれが必要です

4. Spring Bootアプリケーションにおける例外処理の流れ
   弊社のeコマースシステムにおける例外処理の流れを追ってみましょう。

クライアントリクエスト
↓
[コントローラー層] ← @ControllerAdvice がここで例外をキャッチします
↓
[サービス層] ← ビジネス例外がここでスローされます
↓
[リポジトリ層] ← インフラストラクチャ例外がここでスローされます
↓
データベース
例：注文の作成
コントローラー（薄層、ビジネスロジックなし）：

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private  final OrderService orderService; 
    
    @PostMapping 
    public ResponseEntity<OrderResponse> createOrder ( 
            @Valid  @RequestBody OrderRequest request) { 
        
        OrderResponse  response  = orderService.createOrder(request); 
        return ResponseEntity.status(HttpStatus.CREATED).body(response); 
    } 

}
サービス（ビジネスロジック、ビジネス例外をスローする）：

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private  final InventoryService inventoryService; 
    private  final PaymentService paymentService; 
    private  final OrderRepository orderRepository; 
    
    public OrderResponse createOrder (OrderRequest request) { 
        // 1. 在庫を確認する
        for (OrderItem item : request.getItems()) { 
            if (!inventoryService.isAvailable(item.getProductId(), item.getQuantity())) { 
                throw  new  InsufficientInventoryException ( 
                    "商品 " + item.getProductId() + " の在庫が不足しています"
                 ); 
            } 
        } 
        
        // 2. 合計を計算する
        BigDecimal  total  = calculateTotal(request.getItems()); 
        
        // 3. 支払いを処理する
        if (!paymentService.processPayment(request.getCustomerId(), total)) { 
            throw  new  PaymentFailedException ( 
                "支払いが失敗しました。支払い方法を確認してください。"
             ); 
        } 
        
        // 4. 注文を作成する
        Order  order  = buildOrder(request, total); 
        order = orderRepository.save(order); // DataAccessException が発生する場合があります
        
        // 5. 在庫を予約する
        inventoryService.reserveInventory(request.getItems()); 
        
        return OrderResponse.from(order); 
    } 

}
public class InsufficientInventoryException extends RuntimeException {
public InsufficientInventoryException (String message) {
super (message);
}
}

public class PaymentFailedException extends RuntimeException {
public PaymentFailedException (String message) {
super (message);
}
}
リポジトリ（データアクセス、インフラストラクチャ例外をスロー）：

@Repository
public interface OrderRepository extends JpaRepository <Order, Long> {
// Spring Data JPA は DataAccessException を階層的にスローします
// ここではそれらをキャッチせず、伝播させます
}
グローバル例外ハンドラ

標準誤差応答DTO

@Getter
@AllArgsConstructor
public class ApiErrorResponse {
private String message;
private String errorCode;
private Instant timestamp;
}
@RestControllerAdvice
public class GlobalExceptionHandler {

    /* =======================
       ビジネス例外
       ====================== */ 

    @ExceptionHandler(InsufficientInventoryException.class) 
    public ResponseEntity<ApiErrorResponse> handleInventoryException ( 
            InsufficientInventoryException ex) { 

        return ResponseEntity.status(HttpStatus.CONFLICT) 
                .body( new  ApiErrorResponse ( 
                        ex.getMessage(), 
                        "INSUFFICIENT_INVENTORY" , 
                        Instant.now() 
                )); 
    } 

    @ExceptionHandler(PaymentFailedException.class) 
    public ResponseEntity<ApiErrorResponse> handlePaymentException ( 
            PaymentFailedException ex) { 

        return ResponseEntity.status(HttpStatus.PAYMENT_REQUIRED) 
                .body( new  ApiErrorResponse ( 
                        ex.getMessage(), 
                        "PAYMENT_FAILED" , 
                        Instant.now() 
                )); 
    } 

    /* =======================
       インフラストラクチャ例外
       ====================== */ 

    @ExceptionHandler(DataAccessException.class) 
    public ResponseEntity<ApiErrorResponse> handleDatabaseException ( 
            DataAccessException ex) { 

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR) 
                .body( new  ApiErrorResponse ( 
                        "データベースエラーが発生しました。しばらくしてからもう一度お試しください。 " , 
                        "DATABASE_ERROR" , 
                        Instant.now() 
                )); 
    } 

    /* =======================
       フォールバック (セーフティネット) 
       ======================= */ 

    @ExceptionHandler(Exception.class) 
    public ResponseEntity<ApiErrorResponse> handleGenericException ( 
            Exception ex) { 

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR) 
                .body( new  ApiErrorResponse ( 
                        "予期しないエラーが発生しました" , 
                        "INTERNAL_ERROR" , 
                        Instant.now() 
                )); 
    } 

}
要点：
コントローラーはシンプルさを保つ：try-catchもビジネスロジックもなし
サービス独自のビジネス例外：ドメインルールを理解している
リポジトリでインフラストラクチャ例外が発生する：データベース関連の問題
例外は上位に伝播します。適切に処理できる場合を除き、例外をキャッチしないでください。
グローバルハンドラーの翻訳：例外をエラー応答に変換する場所が1箇所に集約されます

5. エラー契約設計（APIエラー応答）
   これは優れた例外処理の核心です。すべてのエラー応答は標準的な構造に従う必要があります。

標準エラー応答が必要な理由：
悪い（一貫性のない回答）：

// バリデーションエラー
{
"error" :  "無効なリクエスト"
}

// ビジネスエラー
{
"message" :  "在庫切れ" ,
"code" :  409
}

// サーバーエラー
{
"status" :  500 ,
"error" :  "内部サーバーエラー"
}
クライアントはこれらのデータを一貫して解析できません。各エンドポイントは異なる構造を返します。

良い（一貫した構造）：

{
"timestamp" :  "2026-01-30T10:15:30Z" ,
"status" :  400 ,
"error" :  "BAD_REQUEST" ,
"errorCode" :  "VALIDATION_FAILED" ,
"message" :  "無効な入力が指定されました" ,
"developerMessage" :  "注文リクエストの検証に失敗しました" ,
"path" :  "/api/orders" ,
"traceId" :  "abc123xyz" ,
"errors" :  [
{
"field" :  "items[0].quantity" ,
"rejectedValue" :  0 ,
"message" :  "数量は1以上である必要があります"
} ,
{
"field" :  "shippingAddress.pinCode" ,
"rejectedValue" :  "ABC" ,
"message" :  "PINコードは6桁である必要があります"
}
]
}
エラー応答クラスを定義します。
@Data
@Builder
public class ErrorResponse {

    private LocalDateTime timestamp; 
    private  int status; 
    private String error; 
    private String errorCode;         // 機械可読で安定したメッセージ
    private String message;            // ユーザーフレンドリーなメッセージ
    private String developerMessage;   // 開発者向けの技術的な詳細
    private String path; 
    private String traceId;            // ログの相関用
    private List<FieldError> errors;   // 検証エラー用
    
    @Data 
    @AllArgsConstructor 
    public  static  class  FieldError { 
        private String field; 
        private Object rejectedValue; 
        private String message; 
    } 

}
必須項目の説明：

1. タイムスタンプ：エラーが発生した日時（ログの相関関係の把握に役立ちます）

2. ステータス：HTTPステータスコード（400、404、500など）

3. エラー：HTTPステータス理由（「BAD_REQUEST」、「NOT_FOUND」）

4. errorCode：最も重要！機械可読で安定した識別子

クライアントはこれをプログラム処理に使用します
これらのコードは絶対に変更しないでください（互換性を損なう変更です）
例：在庫不足、支払失敗、注文が見つかりません

5. メッセージ：ユーザーフレンドリーなメッセージ

ユーザーに直接表示できます
必要に応じてローカライズ
例：「在庫切れ」、「支払いが拒否されました」

6. 開発者メッセージ：技術的な詳細

デバッグ用
エンドユーザーには表示されません
例：「InventoryService.checkStock は productId=123 に対して false を返しました」

7. パス：エラーの原因となったリクエストURI

8. traceId：分散トレーシングの相関ID

複数のサービスにわたるリンク要求
マイクロサービスに不可欠

9. エラー：フィールドレベルの検証エラー

検証失敗の場合のみ
各フィールドのどこが問題なのかをクライアントに正確に伝える
エラーコードとメッセージ：
// 悪い例: クライアントがメッセージをチェックする (不安定)
if (errorResponse.getMessage().contains( "out of stock" )) {
showRestockNotification();
}

// 良い例: クライアントがエラーコードをチェックする (安定)
if (errorResponse.getErrorCode().equals( "INSUFFICIENT_INVENTORY" )) {
showRestockNotification();
}
メッセージは変更可能（ローカライズ、表現の変更など）。エラーコードは変更してはならない。

6. エラーコード設計ガイドライン
   エラーコードは、APIのエラーに関する契約書です。慎重に設計してください。

命名規則:MODULE_ACTION_REASON
public class ErrorCodes {

    // 注文モジュール
    public  static  final  String  ORDER_NOT_FOUND  =  "ORDER_NOT_FOUND" ; 
    public  static  final  String  ORDER_ALREADY_CANCELLED  =  "ORDER_ALREADY_CANCELLED" ; 
    public  static  final  String  ORDER_CANNOT_CANCEL  =  "ORDER_CANNOT_CANCEL" ; 
    public  static  final  String  ORDER_CREATION_FAILED  =  "ORDER_CREATION_FAILED" ; 
    
    // 在庫モジュール
    public  static  final  String  INSUFFICIENT_INVENTORY  =  "INSUFFICIENT_INVENTORY" ; 
    public  static  final  String  INVENTORY_UPDATE_FAILED  =  "INVENTORY_UPDATE_FAILED" ; 
    
    // 支払いモジュール
    public  static  final  String  PAYMENT_FAILED  =  "PAYMENT_FAILED" ; 
    public  static  final  String  PAYMENT_GATEWAY_TIMEOUT  =  "PAYMENT_GATEWAY_TIMEOUT" ; 
    public  static  final  String  INSUFFICIENT_BALANCE  =  "INSUFFICIENT_BALANCE" ; 
    
    // 顧客モジュール
    public  static  final  String  CUSTOMER_NOT_FOUND  =  "CUSTOMER_NOT_FOUND" ; 
    public  static  final  String  CUSTOMER_INACTIVE  =  "CUSTOMER_INACTIVE" ; 
    
    // 検証
    public  static  final  String  VALIDATION_FAILED  =  "VALIDATION_FAILED" ; 
    public  static  final  String  INVALID_INPUT  =  "INVALID_INPUT" ; 
    
    // 認証/認可
    public  static  final  String  UNAUTHORIZED  =  "UNAUTHORIZED" ; 
    public  static  final  String  ACCESS_DENIED  =  "ACCESS_DENIED" ; 
    public  static  final  String  TOKEN_EXPIRED  =  "TOKEN_EXPIRED" ; 
    
    // テクニカルエラー
    public  static final  String  DATABASE_ERROR  =  "DATABASE_ERROR" ; 
    public  static  final  String  EXTERNAL_SERVICE_ERROR  =  "EXTERNAL_SERVICE_ERROR" ; 
    public  static  final  String  INTERNAL_SERVER_ERROR  =  "INTERNAL_SERVER_ERROR" ; 

}
ビジネスエラーコードとテクニカルエラーコード：
ビジネスエラー（4xx）：

クライアントの行動が原因です
クライアントによる復旧が可能
例：在庫不足、支払失敗、注文が見つかりません
技術的なエラー (5xx):

システムの問題が原因です
クライアントによる復旧は不可能
例：DATABASE_ERROR、PAYMENT_GATEWAY_TIMEOUT、INTERNAL_SERVER_ERROR
クライアントがメッセージではなくコードに頼る理由：
// モバイルアプリまたはウェブアプリでさまざまなエラーシナリオを処理する
switch (response.getErrorCode()) {
case  "INSUFFICIENT_INVENTORY" :
showOutOfStockDialog();
suggestAlternatives();
break ;

    case  "PAYMENT_FAILED" : 
        retryPayment(); 
        break ; 
        
    case  "PAYMENT_GATEWAY_TIMEOUT" : 
        showRetryDialog( "支払い処理中です..." ); 
        checkPaymentStatus(); 
        break ; 
        
    case  "CUSTOMER_INACTIVE" : 
        redirectToActivationPage(); 
        break ; 
        
    default : 
        showGenericError(); 

}
バージョン管理戦略：
エラーコードにはバージョン管理をしないでください。一度公開されると、変更はできません。

// 不良
ORDER_NOT_FOUND_V1
ORDER_NOT_FOUND_V2

// 良好 - 新しいコードを追加し、古いコードは保持
ORDER_NOT_FOUND // オリジナル
ORDER_NOT_FOUND_CANCELLED // より具体的、後から追加
ドキュメント:
エラーコード登録簿を維持する：

Enterキーを押すか、画像をクリックしてフルサイズで表示します。

7. HTTPステータスコードマッピング戦略
   適切なHTTPステータスを選択することは非常に重要です。ここでは、当社のeコマースシステムにおける例外処理のマッピング方法を説明します。

黄金律：
2xx：成功（ここではこれについては議論しません）
4xx：クライアントエラー（クライアント側で修正可能）
5xx：サーバーエラー（クライアント側では修正できません。サーバー側で修正する必要があります）
一般的なステータスコード：
400 Bad Request - 無効な入力 / 検証失敗
// 例:
throw new ValidationException ( "無効な注文数量" );
throw new InvalidInputException ( "商品IDは正の値である必要があります" );

// レスポンス:
{
"status" : 400 ,
"errorCode" : "VALIDATION_FAILED" ,
"message" : "注文数量は1～100の間でなければなりません"
}
使用場面：

入力検証に失敗しました
リクエストボディの形式が正しくありません
必須項目が欠落しています
401 Unauthorized - 認証失敗
// 例:
throw new AuthenticationException ( "無効な認証情報" );
throw new TokenExpiredException ( "JWTトークンの有効期限が切れました" );

// レスポンス:
{
"status" : 401 ,
"errorCode" : "TOKEN_EXPIRED" ,
"message" : "セッションの有効期限が切れました。再度ログインしてください。"
}
使用場面：

認証トークンが提供されていません
認証情報が無効です
トークンの有効期限が切れています
403 Forbidden - 認証失敗
// 例:
throw new AccessDeniedException ( "他の顧客の注文にアクセスできません" );
throw new InsufficientPermissionsException ( "管理者アクセスが必要です" );

// レスポンス:
{
"status" : 403 ,
"errorCode" : "ACCESS_DENIED" ,
"message" : "この操作を実行する権限がありません"
}
使用場面：

ユーザーは認証済みですが、権限がありません。
ユーザーが他人のリソースにアクセスしようとする
404 Not Found - リソースが存在しません
// 例:
throw new OrderNotFoundException ( "注文が見つかりません: " + orderId);
throw new ProductNotFoundException ( "商品が見つかりません: " + productId);

// レスポンス:
{
"status" : 404 ,
"errorCode" : "ORDER_NOT_FOUND" ,
"message" : "注文番号12345が見つかりません"
}
使用場面：

リソースIDがデータベースに存在しません
エンドポイントパスが間違っています（Springがこれを処理します）
409 紛争 - 資源状態紛争
// 例:
throw new DuplicateOrderException ( "注文は既に存在します" );
throw new InvalidStateTransitionException ( "出荷済みの注文はキャンセルできません" );
throw new OptimisticLockException ( "注文は別のユーザーによって変更されました" );

// レスポンス:
{
"status" : 409 ,
"errorCode" : "ORDER_ALREADY_CANCELLED" ,
"message" : "この注文は既にキャンセルされています"
}
使用場面：

重複したリソースの作成
状態遷移は許可されていません
楽観的ロック競合
同時修正
422 処理不能なエンティティ - ビジネスルール違反（オプション）
一部のチームは、ビジネスロジック違反に対して400番ではなく422番を使用しています。

// 例:
throw new InsufficientInventoryException ( "在庫は5個のみです" );
throw new InsufficientBalanceException ( "残高: ₹500、必要額: ₹2000" );

// レスポンス:
{
"status" : 422 ,
"errorCode" : "INSUFFICIENT_INVENTORY" ,
"message" : "注文を履行できません。製品「Laptop」の在庫は5個のみです。"
}
使用場面：

入力は有効ですが、ビジネスルールにより処理が拒否されます。
意味的に誤り（形式は正しいが、意味が間違っている）
注：400対422は議論の余地があります。チーム内で一貫性を保つようにしてください。

429 リクエストが多すぎます - レート制限
// 例:
throw new RateLimitExceededException ( "短時間に注文が多すぎます" );

// レスポンス:
{
"status" : 429 ,
"errorCode" : "RATE_LIMIT_EXCEEDED" ,
"message" : "リクエストが多すぎます。60秒後に再度お試しください。" ,
"retryAfter" : 60
}
使用場面：

クライアントがレート制限を超過
ログイン試行回数が多すぎます
500 内部サーバーエラー - 予期しないサーバーエラー
// 例:
throw new InternalServerException ( "予期しないエラーが発生しました" );
// または予期しない例外をキャッチします

// レスポンス:
{
"status" : 500 ,
"errorCode" : "INTERNAL_SERVER_ERROR" ,
"message" : "予期しないエラーが発生しました。修正作業中です。"
}
使用場面：

NullPointerException、未処理の例外
プログラミングエラー
予期しないデータベースエラー
重要：技術的な詳細は隠してください。スタックトレースは絶対に公開しないでください。

503 サービス利用不可 - 下流依存関係の障害
// 例:
throw new PaymentGatewayException ( "決済サービスが利用できません" );
throw new InventoryServiceException ( "在庫サービスがダウンしています" );

// レスポンス:
{
"status" : 503 ,
"errorCode" : "PAYMENT_GATEWAY_TIMEOUT" ,
"message" : "決済サービスは一時的に利用できません。もう一度お試しください。" ,
"retryAfter" : 300
}
使用場面：

外部APIタイムアウト
データベース接続プールが枯渇しました。
サービスがメンテナンスモードになっています
重要な区別：
// 悪い例: すべて 500 を返す
try {
createOrder (request);
} catch (Exception e) {
return ResponseEntity. status ( 500 ). body ( "エラーが発生しました" );
}

// 良い例: エラーの種類ごとに特定のステータス
を返す- 検証エラー->  400

- 認証エラー->  401
- 権限エラー->  403
- 見つかりません->  404
- ビジネスルール違反->  409または422
- 外部サービスがダウンしています->  503
- 予期しないエラー->  500
  ビジネス上のエラーはサーバー障害ではありません！

在庫がなくなった場合、それは500 （サーバー側の問題）ではなく、400/409 （クライアント側の問題）です。

8. Spring Bootにおけるグローバル例外処理
   それでは、 @RestControllerAdviceを使用して、一元化された例外処理を実装してみましょう。

@RestControllerAdviceが不可欠な理由：
それがないと：

// すべてのコントローラー メソッドで try-catch を実行します
@PostMapping("/orders")
public ResponseEntity<?> createOrder( @RequestBody OrderRequest request) {
try {
Order order = orderService.createOrder(request);
return ResponseEntity.ok(order);
} catch (InsufficientInventoryException e) {
return ResponseEntity.status( 409 ).body( new ErrorResponse (...));
} catch (PaymentFailedException e) {
return ResponseEntity.status( 400 ).body( new ErrorResponse (...));
} catch (Exception e) {
return ResponseEntity.status( 500 ).body( new ErrorResponse (...));
}
}

// すべてのコントローラーでこのロジックが繰り返されます!
@RestControllerAdvice を使用する場合:

// コントローラーをクリーンアップします
@PostMapping("/orders")
public ResponseEntity<OrderResponse> createOrder ( @RequestBody OrderRequest request) {
OrderResponse response = orderService.createOrder(request);
return ResponseEntity.status(HttpStatus.CREATED).body(response);
}

// 例外処理はグローバルに行われます
完全なグローバル例外ハンドラー:
@RestControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private  final HttpServletRequest request; 
    
    // 1. カスタムビジネス例外の処理
    
    @ExceptionHandler(OrderNotFoundException.class) 
    public ResponseEntity<ErrorResponse> handleOrderNotFound (OrderNotFoundException ex) { 
        log.warn( "注文が見つかりません: {}" , ex.getMessage()); 
        
        ErrorResponse  error  = ErrorResponse.builder() 
                .timestamp(LocalDateTime.now()) 
                .status(HttpStatus.NOT_FOUND.value()) 
                .error(HttpStatus.NOT_FOUND.getReasonPhrase()) 
                .errorCode(ErrorCodes.ORDER_NOT_FOUND) 
                .message(ex.getMessage()) 
                .developerMessage( "注文IDがシステムに存在しません" ) 
                .path(request.getRequestURI()) 
                .traceId(getTraceId()) 
                .build(); 
        
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error); 
    } 
    
    @ExceptionHandler(InsufficientInventoryException.class) 
    public ResponseEntity<ErrorResponse> handleInsufficientInventory ( 
            InsufficientInventoryException ex) { 
        
        log.warn( "在庫不足: {}" , ex.getMessage()); 
        
        ErrorResponse  error  = ErrorResponse.builder() 
                .timestamp(LocalDateTime.now()) 
                .status(HttpStatus.CONFLICT.value()) 
                .error(HttpStatus.CONFLICT.getReasonPhrase()) .errorCode(ErrorCodes.INSUFFICIENT_INVENTORY 
                ) 
                .message(ex.getMessage()) 
                .developerMessage( "在庫チェックに失敗しました: " + ex.getDeveloperMessage()) 
                .path(request.getRequestURI()) 
                .traceId(getTraceId()) 
                .build(); 
        
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error); 
    } 
    
    @ExceptionHandler(PaymentFailedException.class) 
    public ResponseEntity<ErrorResponse> handlePaymentFailed (PaymentFailedException ex) { 
        log.error( "支払い失敗: {}" , ex.getMessage()); 
        
        ErrorResponse  error  = ErrorResponse.builder()
                .timestamp(LocalDateTime.now()) 
                .status(HttpStatus.BAD_REQUEST.value()) 
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase()) 
                .errorCode(ErrorCodes.PAYMENT_FAILED) 
                .message(ex.getMessage()) 
                .developerMessage( "Payment gateway returned failure: " + ex.getPaymentGatewayCode()) 
                .path(request.getRequestURI()) 
                .traceId(getTraceId()) 
                .build(); 
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error); 
    } 
    
    // 2. 検証例外の処理
    
    @ExceptionHandler(MethodArgumentNotValidException.class) 
    public ResponseEntity<ErrorResponse> handleValidationException ( 
            MethodArgumentNotValidException ex) { 
        
        log.warn( "Validation failed: {}" , ex.getMessage()); 
        
        List<ErrorResponse.FieldError> fieldErrors = ex.getBindingResult() 
                .getFieldErrors() 
                .stream() 
                .map(error -> new  ErrorResponse .FieldError( 
                        error.getField(), error.getRejectedValue 
                        (), 
                        error.getDefaultMessage() 
                )) 
                .collect(Collectors.toList()); 
        
        ErrorResponse  error  = ErrorResponse.builder() 
                .timestamp(LocalDateTime.now()) 
                .status(HttpStatus.BAD_REQUEST.value()) 
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase()) 
                .errorCode(ErrorCodes.VALIDATION_FAILED) 
                .message( "1 つ以上のフィールドの検証に失敗しました" ) 
                .developerMessage( "リクエストの検証に失敗しました" ) 
                .path(request.getRequestURI()) 
                .traceId(getTraceId()) 
                .errors(fieldErrors) 
                .build(); 
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error); 
    } 
    
    // 3. 認証例外の処理
    
    @ExceptionHandler(AccessDeniedException.class) 
    public ResponseEntity<ErrorResponse> handleAccessDenied (AccessDeniedException ex) { 
        log.warn( "アクセス拒否: {}" , ex.getMessage()); 
        
        ErrorResponse  error  =ErrorResponse.builder() 
                .timestamp(LocalDateTime.now()) 
                .status(HttpStatus.FORBIDDEN.value()) 
                .error(HttpStatus.FORBIDDEN.getReasonPhrase()) 
                .errorCode(ErrorCodes.ACCESS_DENIED) 
                .message( "この操作を実行する権限がありません" ) 
                .developerMessage(ex.getMessage()) 
                .path(request.getRequestURI()) 
                .traceId(getTraceId()) 
                .build(); 
        
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error); 
    } 
    
    // 4. データベース/インフラストラクチャ例外の処理
    
    @ExceptionHandler(DataAccessException.class) 
    public ResponseEntity<ErrorResponse> handleDatabaseException (DataAccessException ex) { 
        log.error( "データベースエラーが発生しました" , ex); 
        
        ErrorResponse  error  = ErrorResponse.builder() 
                .timestamp(LocalDateTime.now()) 
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value()) 
                .error(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase()) 
                .errorCode(ErrorCodes.DATABASE_ERROR) 
                .message( "リクエストを処理できませんでした。後でもう一度お試しください。" ) 
                .developerMessage( "データベース操作に失敗しました: " + ex.getClass().getSimpleName()) 
                .path(request.getRequestURI()) 
                .traceId(getTraceId()) 
                .build(); 
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error); 
    } 
    
    // 5. 外部サービス例外の処理
    
    @ExceptionHandler({ 
        PaymentGatewayException.class, 
        ShippingServiceException.class 
    }) 
    public ResponseEntity<ErrorResponse> handleExternalServiceException (RuntimeException ex) { 
        log.error( "外部サービスエラー: {}" , ex.getMessage(), ex); 
        
        ErrorResponse  error  = ErrorResponse.builder() 
                .timestamp(LocalDateTime.now()) 
                .status(HttpStatus.SERVICE_UNAVAILABLE.value()) 
                .error(HttpStatus.SERVICE_UNAVAILABLE.getReasonPhrase()) 
                .errorCode(ErrorCodes.EXTERNAL_SERVICE_ERROR) 
                .message( "サービスが一時的に利用できません。しばらくしてからもう一度お試しください。" )
                .developerMessage( "外部 API 呼び出しが失敗しました: " + ex.getMessage()) 
                .path(request.getRequestURI()) 
                .traceId(getTraceId()) 
                .build(); 
        
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(error); 
    } 
    
    // 6. その他の予期しない例外をすべて処理します
    
    @ExceptionHandler(Exception.class) 
    public ResponseEntity<ErrorResponse> handleGenericException (Exception ex) { 
        log.error( "予期しないエラーが発生しました" , ex); 
        
        ErrorResponse  error  = ErrorResponse.builder() 
                .timestamp(LocalDateTime.now()) 
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value()) 
                .error(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase()) 
                .errorCode(ErrorCodes.INTERNAL_SERVER_ERROR) 
                .message( "予期しないエラーが発生しました。修正作業中です。" ) 
                .developerMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage()) 
                .path(request.getRequestURI()) 
                .traceId(getTraceId()) 
                .build(); 
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error); 
    } 
    
    // MDC からトレース ID を取得するか、新しいトレース ID を生成するヘルパー メソッド
    private String getTraceId () { 
        return MDC.get( "traceId" ) != null ? MDC.get( "traceId" ) : UUID.randomUUID().toString(); 
    } 

}
要点：
順序が重要です。より具体的なハンドラを先に、汎用ハンドラを最後に記述してください。
例外1つ→ハンドラー1つ：処理ロジックを重複させない
適切なログ記録を行う：クライアントエラーの場合はWARN、サーバーエラーの場合はERROR
詳細を非表示にする：スタックトレースや機密情報をクライアントに公開しないでください
一貫した構造：すべてのレスポンスは同じErrorResponse形式に従います

9. 検証例外処理
   検証は最初の防衛線です。適切に対処しましょう。

Bean検証の基本：
public class OrderRequest {

    @NotNull(message = "顧客IDは必須です") 
    private Long customerId; 
    
    @NotEmpty(message = "注文には少なくとも1つのアイテムが必要です") 
    @Size(max = 50, message = "1注文あたり最大50アイテムまで") 
    private List< @Valid OrderItem> items; 
    
    @NotNull(message = "配送先住所は必須です") 
    @Valid 
    private ShippingAddress shippingAddress; 
    
    @DecimalMin(value = "0.0", inclusive = false, message = "合計は0より大きい必要があります") 
    private BigDecimal estimatedTotal; 

}

public class OrderItem {

    @NotNull(message = "商品IDは必須です") 
    @Positive(message = "商品IDは正である必要があります") 
    private Long productId; 
    
    @NotNull(message = "数量は必須です") 
    @Min(value = 1, message = "数量は少なくとも1である必要があります") 
    @Max(value = 100, message = "数量は100を超えることはできません") 
    private Integer quantity; 
    
    @NotNull(message = "価格は必須です") 
    @DecimalMin(value = "0.0", inclusive = false, message = "価格は0より大きい必要があります") 
    private BigDecimal price; 

}

public class ShippingAddress {

    @NotBlank(message = "番地は必須です") 
    @Size(max = 200, message = "番地が長すぎます") 
    private String street; 
    
    @NotBlank(message = "市区町村は必須です") 
    private String city; 
    
    @NotBlank(message = "都道府県は必須です") 
    private String state; 
    
    @NotBlank(message = "PINコードは必須です") 
    @Pattern(regexp = "\\d{6}", message = "PINコードは6桁である必要があります") 
    private String pinCode; 
    
    @Pattern(regexp = "\\+?[0-9]{10,12}", message = "無効な電話番号です") 
    private String phoneNumber; 

}
@Valid を使用したコントローラー:
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @PostMapping 
    public ResponseEntity<OrderResponse> createOrder ( 
            @Valid  @RequestBody OrderRequest request) { 
        // 検証に失敗した場合、MethodArgumentNotValidException がスローされます
        // グローバルハンドラがそれをキャッチします
        OrderResponse  response  = orderService.createOrder(request); 
        return ResponseEntity.status(HttpStatus.CREATED).body(response); 
    } 
    
    @PutMapping("/{orderId}") 
    public ResponseEntity<OrderResponse> updateOrder ( 
            @PathVariable  @Positive Long orderId, 
            @Valid  @RequestBody OrderRequest request) { 
        
        OrderResponse  response  = orderService.updateOrder(orderId, request); 
        return ResponseEntity.ok(response); 
    } 

}
MethodArgumentNotValidException の処理:
グローバルハンドラーですでに説明しましたが、応答を見てみましょう。

リクエスト：

{
"customerId" :  null ,
"items" :  [
{
"productId" :  -5 ,
"quantity" :  0 ,
"price" :  -100
}
] ,
"shippingAddress" :  {
"street" :  "" ,
"pinCode" :  "ABC123"
}
}
応答：

{
"timestamp" :  "2026-01-30T10:15:30" ,
"status" :  400 ,
"error" :  "BAD_REQUEST" ,
"errorCode" :  "VALIDATION_FAILED" ,
"message" :  "1つ以上のフィールドの検証に失敗しました" ,
"developerMessage" :  "リクエスト検証に失敗しました" ,
"path" :  "/api/orders" ,
"traceId" :  "abc123xyz" ,
"errors" :  [
{
"field" :  "customerId" ,
"rejectedValue" :  null ,
"message" :  "顧客IDが必要です"
} ,
{
"field" :  "items[0].productId" ,
"rejectedValue" :  -5 ,
"message" :  "製品IDは正の数である必要があります"
} ,
{
"field" :  { "items[0].quantity" ,
"rejectedValue" :  0 ,
"message" :  "数量は1以上である必要があります"
} ,
{
"field" :  "items[0].price" ,
"rejectedValue" :  -100 ,
"message" :  "価格は0より大きい必要があります"
} ,
{
"field" :  "shippingAddress.street" ,
"rejectedValue" :  "" ,
"message" :  "番地は必須です"
} ,
{
"field" :  "shippingAddress.pinCode" ,
"rejectedValue" :  "ABC123" ,
"message" :  "PINコードは6桁である必要があります"
}
]
}
カスタムバリデーター:
組み込みのバリデーターだけでは不十分な場合があります。カスタムバリデーターを作成しましょう。

@Documented
@Constraint(validatedBy = FutureDateValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface FutureDate {
String message ()  default  "日付は未来である必要があります" ;
Class<?>[] groups() default {};
Class<? extends Payload >[] payload() default {};
}

public class FutureDateValidator implements ConstraintValidator <FutureDate, LocalDate> {

    @Override 
    public  boolean  isValid (LocalDate value, ConstraintValidatorContext context) { 
        if (value == null ) { 
            return  true ; // null チェックには @NotNull を使用します
        } 
        return value.isAfter(LocalDate.now()); 
    } 

}
// 使用例:
public class OrderRequest {

    @FutureDate(message = "配達日は未来である必要があります") 
    private LocalDate requestedDeliveryDate; 

}
検証エラーが常に400になる理由：
検証エラーはクライアント側のエラーです。クライアントが不正なデータを送信しました。クライアントはそれを修正して再試行する必要があります。

検証エラーに対して500番のエラーを返さないでください。

10. ビジネス例外処理
    ビジネス例外は、ドメインルール違反を表します。これらは想定されるものであり、適切に処理されるべきです。

ビジネス例外をスローするタイミング：
ビジネスロジックには、null、ブール値フラグ、ステータスコードを使用しないでください。例外処理を使用してください。

悪い：

public Order createOrder (OrderRequest request) {
if (!inventoryService.checkInventory(request)) {
return null ; // null とはどういう意味ですか？なぜ失敗したのですか？
}
// ...
}

// 呼び出し元は null をチェックする必要があります
Order order = orderService.createOrder(request);
if (order == null ) {
// 顧客には何と伝えますか？
}
良い：

public Order createOrder (OrderRequest request) {
if (!inventoryService.checkInventory(request)) {
throw new InsufficientInventoryException (
"製品「ラップトップ」の在庫は5個のみです。お客様は10個をリクエストされました。"
);
}
// ...
}

// 例外はグローバルに処理され、適切なエラー応答が送信されます
カスタムビジネス例外を定義する：
// すべてのビジネス例外の基底クラス
public abstract class BusinessException extends RuntimeException {

    private  final String errorCode; 
    private  final String developerMessage; 
    
    public  BusinessException (String message, String errorCode, String developerMessage) { 
        super (message); 
        this .errorCode = errorCode; 
        this .developerMessage = developerMessage; 
    } 
    
    public String getErrorCode () { 
        return errorCode; 
    } 
    
    public String getDeveloperMessage () { 
        return developerMessage; 
    } 

}

// 特定のビジネス例外
public class InsufficientInventoryException extends BusinessException {

    public  InsufficientInventoryException (String message) { 
        super ( 
            message, 
            ErrorCodes.INSUFFICIENT_INVENTORY, 
            "在庫チェックに失敗しました"
         ); 
    } 

}

public class PaymentFailedException extends BusinessException {

    private  final String paymentGatewayCode; 
    
    public  PaymentFailedException (String message, String paymentGatewayCode) { 
        super ( 
            message, 
            ErrorCodes.PAYMENT_FAILED, 
            "支払いゲートウェイエラー: " + paymentGatewayCode 
        ); 
        this .paymentGatewayCode = paymentGatewayCode; 
    } 
    
    public String getPaymentGatewayCode () { 
        return paymentGatewayCode; 
    } 

}

public class InvalidOrderStateException extends BusinessException {

    private  final OrderStatus currentStatus; 
    private  final OrderStatus attemptedStatus; 
    
    public  InvalidOrderStateException (OrderStatus currentStatus, OrderStatus attemptedStatus) { 
        super ( 
            String.format( "注文を %s から %s に移行できません" , currentStatus, attemptedStatus),
            ErrorCodes.ORDER_CANNOT_CANCEL, 
            String.format( "無効な状態遷移: %s -> %s" , currentStatus, attemptedStatus) 
        ); 
        this .currentStatus = currentStatus; 
        this .attemptedStatus = attemptedStatus; 
    } 

}

public class OrderNotFoundException extends BusinessException {

    public  OrderNotFoundException (Long orderId) { 
        super ( 
            "注文番号" + orderId + "が見つかりません" , 
            ErrorCodes.ORDER_NOT_FOUND, 
            "注文ID " + orderId + "がデータベースに存在しません"
         ); 
    } 

}
ビジネスロジックの例：
例１：順序状態遷移

@Service
public class OrderService {

    public  void  cancelOrder (Long orderId) { 
        Order  order  = orderRepository.findById(orderId) 
                .orElseThrow(() -> new  OrderNotFoundException (orderId)); 
        
        // ビジネスルール: PENDING または CONFIRMED の注文のみキャンセル可能
        if (order.getStatus() == OrderStatus.SHIPPED || 
            order.getStatus() == OrderStatus.DELIVERED) { 
            throw  new  InvalidOrderStateException ( 
                order.getStatus(), 
                OrderStatus.CANCELLED 
            ); 
        } 
        
        // ビジネスルール: 返金は最初に処理する必要がある
        if (order.getPaymentStatus() == PaymentStatus.PAID) { 
            boolean  refunded  = paymentService.refund(order.getId()); 
            if (!refunded) { 
                throw  new  PaymentFailedException ( 
                    "返金に失敗しました。注文をキャンセルできません。 " , 
                    "REFUND_FAILED"
                 ); 
            } 
        } 
        
        order.setStatus(OrderStatus.CANCELLED); 
        orderRepository.save(order); 
    } 

}
例２：在庫確認

@Service
public class InventoryService {

    public  void  validateInventory (List<OrderItem> items) { 
        for (OrderItem item : items) { 
            Product  product  = productRepository.findById(item.getProductId()) 
                    .orElseThrow(() -> new  ProductNotFoundException (item.getProductId())); 
            
            if (product.getAvailableQuantity() < item.getQuantity()) { 
                throw  new  InsufficientInventoryException ( 
                    String.format( 
                        "商品 '%s' は %d 個しか在庫がありません。お客様は %d 個を要求されました。" , 
                        product.getName(), 
                        product.getAvailableQuantity(), 
                        item.getQuantity() 
                    ) 
                ); 
            } 
            
            if (!product.isActive()) { 
                throw  new  ProductNotAvailableException ( 
                    "商品 '" + product.getName() + "' は在庫切れです"
                 ); 
            } 
        } 
    } 

}
例3：支払い検証

@Service
public class PaymentService {

    public  void  processPayment (Long customerId, BigDecimal amount) { 
        Customer  customer  = customerRepository.findById(customerId) 
                .orElseThrow(() -> new  CustomerNotFoundException (customerId)); 
        
        // ビジネスルール: 顧客はアクティブである必要があります
        if (!customer.isActive()) { 
            throw  new  CustomerInactiveException ( 
                "アカウントが非アクティブです。サポートにお問い合わせください。"
             ); 
        } 
        
        // ビジネスルール: ウォレット残高を確認します
        if (customer.getWalletBalance().compareTo(amount) < 0 ) { 
            throw  new  InsufficientBalanceException ( 
                String.format( 
                    "残高が不足しています。残高: ₹%s、必要額: ₹%s" , 
                    customer.getWalletBalance(), 
                    amount 
                ) 
            ); 
        } 
        
        // 決済ゲートウェイを呼び出します
        PaymentGatewayResponse  response  = paymentGateway.charge(customerId, amount); 
        
        if (!response.isSuccess()) { 
            throw  new  PaymentFailedException ( 
                "支払い失敗: " + response.getMessage(), 
                response.getErrorCode() 
            ); 
        } 
    } 

}
HTTPステータスコードとエラーコードへのマッピング：
ビジネス例外 HTTP ステータス エラー コードInsufficientInventoryException 409 INSUFFICIENT_INVENTORY
PaymentFailedException 400 PAYMENT_FAILED InvalidOrderStateException 409 ORDER_CANNOT_CANCEL
OrderNotFoundException 404 ORDER_NOT_FOUND InsufficientBalanceException 400 INSUFFICIENT_BALANCE
CustomerInactiveException 403 CUSTOMER_INACTIVE

これらはすべて4xxエラーです。なぜなら、サーバー障害ではなく、クライアント側の問題だからです。

11. インフラストラクチャと統合のエラー
    インフラストラクチャのエラーは、業務上のエラーとは異なります。通常、インフラストラクチャのエラーは一時的なものであり、異なる方法で対処する必要があります。

一般的なインフラストラクチャの例外:

1. データベース例外:

// 接続タイムアウト
org.springframework.dao.DataAccessResourceFailureException

// デッドロック
org.springframework.dao.DeadlockLoserDataAccessException
// 制約違反
org.springframework.dao.DataIntegrityViolationException
// クエリタイムアウト
org.springframework.dao.QueryTimeoutException

2. ネットワーク/HTTP例外:

// 接続タイムアウト
java.net.SocketTimeoutException

// 接続拒否
java.net.ConnectException

3. サードパーティAPIの例外：

// 決済ゲートウェイのタイムアウト
// 配送サービスが利用できません
// 在庫同期サービスが停止しています
境界における例外の翻訳:
インフラの詳細を顧客に漏らさないでください。境界で翻訳してください。

悪い：

@Service
public class PaymentService {

    public  void  processPayment ( Long customerId, BigDecimal amount ) { 
        // インフラストラクチャの詳細が漏洩した RestClientException がスローされます
        paymentGatewayClient. charge (customerId, amount); 
    } 

}
良い：

@Service
public class PaymentService {

    public  void  processPayment (Long customerId, BigDecimal amount) { 
        try { 
            paymentGatewayClient.charge(customerId, amount); 
        } catch (ResourceAccessException e) { 
            // ネットワークエラー
            throw  new  PaymentGatewayException ( 
                "支払いサービスが一時的に利用できません" , 
                e 
            ); 
        } catch (HttpClientErrorException e) { 
            // 支払いゲートウェイからの 4xx 
            throw  new  PaymentFailedException ( 
                "支払いが拒否されました" , 
                e.getResponseBodyAsString() 
            ); 
        } catch (HttpServerErrorException e) { 
            // 支払いゲートウェイからの 5xx 
            throw  new  PaymentGatewayException ( 
                "支払いサービスエラー" , 
                e 
            ); 
        } 
    } 

}
カスタムインフラストラクチャ例外:
// 基本インフラストラクチャ例外
public class InfrastructureException extends RuntimeException {

    public  InfrastructureException (String message) { 
        super (message); 
    } 
    
    public  InfrastructureException (String message, Throwable cause) { 
        super (message, cause); 
    } 

}

// 特定のインフラストラクチャ例外
public class DatabaseException extends InfrastructureException {

    public  DatabaseException (String message, Throwable cause) { 
        super ( "データベース操作が失敗しました: " + message, cause); 
    } 

}
public class PaymentGatewayException extends InfrastructureException {

    public  PaymentGatewayException (String message) { 
        super (message); 
    } 
    
    public  PaymentGatewayException (String message, Throwable cause) { 
        super (message, cause); 
    } 

}
public class ShippingServiceException extends InfrastructureException {

    public  ShippingServiceException (String message, Throwable cause) { 
        super ( "配送サービスが利用できません: " + message, cause); 
    } 

}
外部API呼び出しの処理：
@Service
public class ShippingService {

    @Autowired 
    private RestTemplate restTemplate; 
    
    public ShippingCost calculateShippingCost (ShippingRequest request) { 
        try { 
            ResponseEntity<ShippingCost> response = restTemplate.postForEntity( 
                "https://shipping-api.example.com/calculate" , 
                request, 
                ShippingCost.class 
            ); 
            return response.getBody(); 
            
        } catch (ResourceAccessException e) { 
            // ネットワークタイムアウト、接続拒否
            log.error( "配送サービスネットワークエラー" , e); 
            throw  new  ShippingServiceException ( 
                "配送料を計算できません。もう一度お試しください。" , 
                e 
            ); 
            
        } catch (HttpServerErrorException e) { 
            // 配送サービスからの 5xx
             log.error( "配送サービスエラー: {}" , e.getResponseBodyAsString()); 
            throw  new  ShippingServiceException ( 
                "配送サービスが一時的に利用できません" , 
                e 
            ); 
            
        } catch (HttpClientErrorException e) { 
            // 配送サービスからの 4xx エラー
            log.warn( "無効な配送リクエスト: {}" , e.getResponseBodyAsString()); 
            throw  new  InvalidShippingRequestException ( 
                "無効な配送情報が提供されました"
             ); 
        } 
    } 

}
データベース例外の処理：
@Repository
public class OrderRepositoryImpl {

    @Autowired 
    private JdbcTemplate jdbcTemplate; 
    
    public  void  updateOrderStatus (Long orderId, OrderStatus newStatus) { 
        try { 
            String  sql  =  "UPDATE orders SET status = ?, updated_at = NOW() WHERE id = ?" ; 
            int  updated  = jdbcTemplate.update(sql, newStatus.name(), orderId); 
            
            if (updated == 0 ) { 
                throw  new  OrderNotFoundException (orderId); 
            } 
            
        } catch (DeadlockLoserDataAccessException e) { 
            // デッドロックが検出されました - 再試行できます
            log.warn( "注文 {} の更新中にデッドロックが検出されました" , orderId); 
            throw  new  DatabaseException ( "データベースのデッドロックが発生しました" , e); 
            
        } catch (QueryTimeoutException e) { 
            // クエリに時間がかかりすぎました
            log.error( "注文 {} の更新中にクエリがタイムアウトしました" , orderId); 
            throw  new  DatabaseException ( "データベース操作がタイムアウトしました" , e); 
            
        } catch (DataIntegrityViolationException e) { 
            // 制約違反
            log.error( "注文{}のデータ整合性違反" , orderId, e); 
            throw  new  DatabaseException ( "データ整合性違反" , e); 
        } 
    } 

}
インフラストラクチャ例外のグローバルハンドラー:
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(PaymentGatewayException.class) 
    public ResponseEntity<ErrorResponse> handlePaymentGatewayException ( 
            PaymentGatewayException ex) { 
        
        log.error( "Payment gateway error" , ex); 
        
        ErrorResponse  error  = ErrorResponse.builder() 
                .timestamp(LocalDateTime.now()) 
                .status(HttpStatus.SERVICE_UNAVAILABLE.value()) 
                .error(HttpStatus.SERVICE_UNAVAILABLE.getReasonPhrase()) 
                .errorCode(ErrorCodes.PAYMENT_GATEWAY_TIMEOUT) 
                .message( "Payment service is temporary unavailable. Please try again in a few minutes." ) 
                .developerMessage( "Payment gateway integration failed: " + ex.getMessage()) 
                .path(request.getRequestURI()) 
                .traceId(getTraceId()) 
                .build(); 
        
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(error); 
    } 
    
    @ExceptionHandler(DatabaseException.class) 
    public ResponseEntity<ErrorResponse> handleDatabaseException (DatabaseException ex) { 
        log.error( "データベースエラー" , ex); 
        
        ErrorResponse  error  = ErrorResponse.builder() 
                .timestamp(LocalDateTime.now()) 
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value()) .error(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase( 
                )) 
                .errorCode(ErrorCodes.DATABASE_ERROR) 
                .message( "リクエストを処理できませんでした。後でもう一度お試しください。" ) 
                .developerMessage( "データベース操作に失敗しました" ) 
                .path(request.getRequestURI()) 
                .traceId(getTraceId()) 
                .build(); 
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error); 
    } 

}
主な違い：
側面 ビジネス例外 インフラストラクチャ例外原因クライアントの動作/データ システム/外部の問題HTTPステータス4xx
5xx (通常503)再試行可能通常は不可 多くの場合可ログレベル警告 エラークライアントのアクション入力を修正して再試行する
後で再試行する例検証、ビジネスルール DBタイムアウト、APIダウン

12. 例外処理のためのログ記録戦略
    ログ記録は、本番環境における問題のデバッグに不可欠です。しかし、あらゆるものをログに記録すると、ノイズが発生します。

記録すべき内容：

1. 想定されるクライアントエラー (4xx): WARNレベルでログに記録する

@ExceptionHandler(InsufficientInventoryException.class)
public ResponseEntity<ErrorResponse> handleInsufficientInventory (
InsufficientInventoryException ex) {

    log.warn( "在庫不足: productId={}, requested={}, available={}" , 
            ex.getProductId(), ex.getRequestedQuantity(), ex.getAvailableQuantity()); 
    
    // エラーレスポンスを返す

}

2. サーバーエラー (5xx):スタックトレース付きでERRORレベルでログを記録

@ExceptionHandler(Exception.class)
public ResponseEntity<ErrorResponse> handleGenericException (Exception ex) {
log.error( "予期しないエラーが発生しました " , ex); // スタックトレースを含める

    // エラーレスポンスを返す

}
記録してはいけないもの：
機密情報を決して記録しないでください。

// 不正な
ログ log.error( "カードの支払いに失敗しました: {}" , request.getCardNumber());
log.error( "ユーザー: {} のログインに失敗しました。パスワード: {}" , username, password);
log.error( "認証トークン: {}" , jwtToken);
log.error( "顧客の社会保障番号: {}" , customer.getSsn());

// 正常な
ログ log.error( "顧客の支払いに失敗しました: {}" , request.getCustomerId());
log.error( "ユーザー: {} のログインに失敗しました" , username); // パスワードなし
log.error( "認証トークンの検証に失敗しました" ); // トークンなし
log.error( "ID: {} の顧客確認に失敗しました" , customer.getId()); // 社会保障番号なし
個人情報（PII）の漏洩を避けるには：

パスワード
クレジットカード番号
JWTトークン
社会保障番号
メールアドレス（一部の法域において）
電話番号
完全な住所
構造化ログを使用する：
悪い（解析が難しい）：

log.error ( "注文作成に失敗しました: customerId=" + customerId + ", total=" + total + ", reason=" +
reason);

良い（構造化され、解析可能）：

log.error ( "注文作成に失敗しました: customerId={}, total={}, reason={}, traceId={}" ,
customerId, total, reason, traceId);
相関ID / トレースID:
すべてのリクエストには、すべてのログに反映される一意のトレースIDが付与されるべきである。

1. トレースIDフィルターを追加する：

@Component
@Order(1)
public class TraceIdFilter extends OncePerRequestFilter {

    @Override 
    protected  void  doFilterInternal ( 
            HttpServletRequest request, 
            HttpServletResponse response, 
            FilterChain filterChain)  throws ServletException, IOException { 
        
        String  traceId  = request.getHeader( "X-Trace-Id" ); 
        if (traceId == null || traceId.isEmpty()) { 
            traceId = UUID.randomUUID().toString(); 
        } 
        
        MDC.put( "traceId" , traceId); 
        response.setHeader( "X-Trace-Id" , traceId); 
        
        try { 
            filterChain.doFilter(request, response); 
        } finally { 
            MDC.clear(); 
        } 
    } 

}

2. ログにトレースIDを含める：

<!-- logback.xml --> 
< configuration >
< appender name = "CONSOLE"  class = "ch.qos.logback.core.ConsoleAppender" >
< encoder >
< pattern >
%d{ISO8601} [%thread] %-5level %logger{36} [traceId=%X{traceId}] - %msg%n
</ pattern >
</ encoder >
</ appender >
</ configuration >

3. ログは次のようになります。

2026-01-30 10:15:30 [ http-nio-8080-exec-1 ] WARN o.scOrderService [ traceId=abc-123-xyz ] -
在庫不足:  productId=456、 要求数=10、 在庫数=5
2026-01-30 10:15:30 [ http-nio-8080-exec-1 ] WARN
o.scGlobalExceptionHandler [ traceId=abc-123-xyz ] - クライアントに409 CONFLICTを返します    
注：トレースIDでログを検索し、リクエストフロー全体を確認できるようになりました。

ログレベルのガイドライン：
レベル 使用場面 例 ERROR サーバーエラー、予期しない例外、5xx データベースダウン、NullPointer WARN
想定されるクライアントエラー、4xx 検証失敗、在庫切れ INFO 重要なビジネスイベント 注文作成、支払い処理
DEBUG デバッグのための詳細なフロー メソッドのエントリ/イグジット、パラメータ TRACE 非常に詳細なデバッグ
ループの反復、条件

同じ例外を複数回ログに記録しないでください。
悪い：

@Service
public class OrderService {

    public  void  createOrder (OrderRequest request) { 
        try { 
            // ビジネスロジック
        } catch (InsufficientInventoryException e) { 
            log.error( "在庫チェックに失敗しました" , e); // ここでログに記録
            throw e; // 再スロー
        } 
    } 

}

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InsufficientInventoryException.class) 
    public ResponseEntity<ErrorResponse> handle (InsufficientInventoryException ex) { 
        log.error( "在庫例外を処理しています" , ex); // 再度ログに記録！
        // ...
     } 

}
これにより、同じエラーに対して重複したログが作成されます。

良い：

@Service
public class OrderService {

    public  void  createOrder (OrderRequest request) { 
        // 例外が発生した場合は、ログに記録せず、例外をスローする
        if (!inventoryService.checkInventory(request)) { 
            throw  new  InsufficientInventoryException ( "在庫切れ" ); 
        } 
    } 

}

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InsufficientInventoryException.class) 
    public ResponseEntity<ErrorResponse> handle (InsufficientInventoryException ex) { 
        log.warn( "在庫チェックに失敗しました: {}" , ex.getMessage()); // 境界で一度だけログに記録する
        // ...
     } 

}
経験則として、ログはビジネスロジック内ではなく、境界（グローバルハンドラー）で記録する。

13. 可観測性とモニタリング
    適切な例外処理は、アプリケーションの状態を監視し、問題を早期に発見するのに役立ちます。

追跡すべき指標：

1. ステータスコード別のエラー件数：

@RestControllerAdvice
public class GlobalExceptionHandler {

    @Autowired 
    private MeterRegistry meterRegistry; 
    
    @ExceptionHandler(InsufficientInventoryException.class) 
    public ResponseEntity<ErrorResponse> handleInsufficientInventory ( 
            InsufficientInventoryException ex) { 
        
        // カウンターをインクリメント
        meterRegistry.counter( "api.errors" , 
                "status" , "409" , 
                "errorCode" , ErrorCodes.INSUFFICIENT_INVENTORY, 
                "endpoint" , request.getRequestURI() 
        ).increment(); 
        
        // エラーレスポンスを返す
    } 

}

2. エラー率：

// リクエスト総数を追跡
meterRegistry.counter( "api.requests.total" ,
"endpoint" , "/api/orders" ,
"method" , "POST"
).increment();

// エラーリクエストを追跡
meterRegistry.counter( "api.requests.errors" ,
"endpoint" , "/api/orders" ,
"method" , "POST" ,
"status" , "400"
).increment();
// エラー率 = エラー数 / 総数

3. エラーコードによるエラー処理：

meterRegistry.counter( "api.errors.by.code" ,
"errorCode" , ErrorCodes.INSUFFICIENT_INVENTORY
).increment();
これは以下の質問に答えるのに役立ちます。

どのエラーが増加しているか？
どのエンドポイントが最も多く障害を起こしていますか？
特定のエラーコードですか、それとも一般的な不具合ですか？
設定するアラート：

1. 5xxエラー率が高い：

警告： 5 xxエラー率が5分間1%を超えてい ます。 対応：ページングを開始し、エンジニアに連絡してください。

2. 特定のエラーコードの急増：

警告：PAYMENT_GATEWAY_TIMEOUT エラーが 1 分あたり 10 回以上発生しています。
対処方法：決済ゲートウェイのステータスを確認してください。

3. エラー率の閾値：

警告：10分間の合計エラー率が5%を超えました。 対応：根本原因を調査してください。
作成するダッシュボード:
エラー概要ダッシュボード:

総エラー数（過去1時間、24時間、7日間）
エラー率の傾向
HTTPステータスによるエラー
上位10件のエラーコード
エンドポイントごとのエラー
ビジネスエラーダッシュボード：

在庫不足
支払い失敗件数
注文が見つかりませんでした件数
検証エラーのカウント
インフラストラクチャエラーダッシュボード：

データベースエラー数
支払いゲートウェイタイムアウト回数
EXTERNAL_SERVICE_ERROR カウント
外部からの呼び出しに対する応答時間
ログ、トレース、メトリクスをリンクする：
トレースIDがあれば、以下のことが可能です。

まずは指標から始めましょう。「PAYMENT_FAILEDエラーが300%増加しました」
特定のトレースを検索する： 「 errorCode=PAYMENT_FAILEDを検索します」
ログの表示：「traceId=abc-123-xyzのすべてのログを表示」
フロー全体を見る：
リクエスト受信→在庫チェック合格→支払い呼び出し開始→決済ゲートウェイタイムアウト→エラー返される
そのため、トレースIDはすべてのログとエラー応答において非常に重要です。

Grafanaクエリの例：

# ステータスコード別のエラー率

rate (api_requests_errors_total[5m])

# 特定のエラーコードの傾向

rate (api_errors_by_code{errorCode= "INSUFFICIENT_INVENTORY" }[5m])

# 5xx エラーの割合

(sum(rate(api_requests_errors_total{status=~ "5.." }[5m]) ) /
sum(rate(api_requests_total[5m]))) * 100
14．セキュリティに関する考慮事項
例外処理は、慎重に行わないと機密情報が漏洩する可能性がある。

スタックトレースをクライアントに公開してはならない。
悪い（セキュリティリスク）：

{
"error" :  "Internal Server Error" ,
"message" :  "java.lang.NullPointerException: 'customer' が null のため '
com.example.model.Customer.getWalletBalance()' を呼び出すことができません" ,
"stackTrace" :  [
"com.example.service.PaymentService.processPayment(PaymentService.java:45)" ,
"com.example.controller.OrderController.createOrder(OrderController.java:32)" ,
"org.springframework.web.method.support.InvocableHandlerMethod.doInvoke(InvocableHandlerMethod.java:190)" ,
...
]
}
これにより明らかになるのは：

パッケージ構造
クラス名
メソッド名
行番号
フレームワークの詳細
攻撃者はこれを利用して以下のことを行うことができます。

コード構造を理解する
脆弱な図書館を探す
標的型攻撃を計画する
良好（安全）：

{
"timestamp" :  "2026-01-30T10:15:30Z" ,
"status" :  500 ,
"error" :  "INTERNAL_SERVER_ERROR" ,
"errorCode" :  "INTERNAL_SERVER_ERROR" ,
"message" :  "予期しないエラーが発生しました。現在修正中です。" ,
"traceId" :  "abc-123-xyz"
}
クライアントには安全確認メッセージが送信されます。開発者にはログに完全なスタックトレースが記録されます。

例外メッセージをサニタイズする：
悪い：

throw new RuntimeException (
"FK_CONSTRAINT_VIOLATION: テーブル orders の orders_customer_id_fkey"
);
これにより明らかになるのは：

データベースの列名
テーブル名
外部キー関係
良い：

throw new BusinessException (
"無効な顧客参照 " , // クライアント用
ErrorCodes.INVALID_CUSTOMER,
"FK違反: customer_id が見つかりません"    // 開発者用 (
ログに記録され、クライアントには送信されません)
);
認証および認可エラー:
ユーザーの存在を漏洩させないでください。

悪い：

{
"error" :  "メールアドレス john@example.com のユーザーは存在しません"
}
攻撃者は有効なメールアドレスを列挙することができる。

良い：

{
"error" :  "無効なメールアドレスまたはパスワード"
}
「ユーザーが見つかりません」と「パスワードが間違っています」の両方で同じメッセージが表示されます。

権限の詳細を漏洩しないでください。

悪い：

{
"error" : "ユーザー john@example.com には管理者権限がありません"
}
攻撃者は以下のことを学ぶ：

有効なユーザー名
役割構造
権限システム
良い：

{
"error" :  "この操作を実行する権限がありません" ,
"errorCode" :  "ACCESS_DENIED"
}
セキュリティフィルターにおけるエラー処理：
Spring Securityのフィルターは@RestControllerAdviceよりも先に実行されます。別途処理が必要です。

@Component
public class SecurityExceptionHandler implements AuthenticationEntryPoint , AccessDeniedHandler {

    @Autowired 
    private ObjectMapper objectMapper; 
    
    // 認証エラー (トークンなし、無効なトークン) を処理します
    @Override 
    public  void  commence ( 
            HttpServletRequest request, 
            HttpServletResponse response, 
            AuthenticationException authException)  throws IOException { 
        
        ErrorResponse  error  = ErrorResponse.builder() 
                .timestamp(LocalDateTime.now()) 
                .status(HttpStatus.UNAUTHORIZED.value()) 
                .error(HttpStatus.UNAUTHORIZED.getReasonPhrase()) 
                .errorCode(ErrorCodes.UNAUTHORIZED) 
                .message( "認証が必要です" ) 
                .path(request.getRequestURI()) 
                .build(); 
        
        response.setStatus(HttpStatus.UNAUTHORIZED.value()); 
        response.setContentType(MediaType.APPLICATION_JSON_VALUE); 
        response.getWriter().write(objectMapper.writeValueAsString(error)); 
    } 
    
    // 認証エラー (認証済みだが権限がない) を処理します
    @Override 
    public  void  handle ( 
            HttpServletRequest request, 
            HttpServletResponse response, 
            AccessDeniedException accessDeniedException)  throws IOException { 
        
        ErrorResponse  error  = ErrorResponse.builder() 
                .timestamp(LocalDateTime.now()) 
                .status(HttpStatus.FORBIDDEN.value()) 
                .error(HttpStatus.FORBIDDEN.getReasonPhrase()) 
                .errorCode(ErrorCodes.ACCESS_DENIED) 
                .message( "このリソースにアクセスする権限がありません" ) 
                .path(request.getRequestURI()) 
                .build(); 
        
        response.setStatus(HttpStatus.FORBIDDEN.value()); 
        response.setContentType(MediaType.APPLICATION_JSON_VALUE); 
        response.getWriter().write(objectMapper.writeValueAsString(error)); 
    } 

}
セキュリティ設定で登録する：

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired 
    private SecurityExceptionHandler securityExceptionHandler; 
    
    @Bean 
    public SecurityFilterChain filterChain (HttpSecurity http)  throws Exception { 
        http 
            .exceptionHandling() 
                .authenticationEntryPoint(securityExceptionHandler) 
                .accessDeniedHandler(securityExceptionHandler) 
            .and() 
            // ... その他の設定
        ; 
        return http.build(); 
    } 

}
SQLインジェクション対策：
常にパラメータ化クエリを使用してください。

悪い：

String sql =  "SELECT * FROM orders WHERE customer_id = " + customerId;
jdbcTemplate.query(sql, rowMapper);
例外が発生した場合、エラーメッセージによってSQLクエリが明らかになる可能性がある。

良い：

String sql =  "SELECT * FROM orders WHERE customer_id = ?" ;
jdbcTemplate.query(sql, rowMapper, customerId);
レート制限エラー応答:
レート制限が作動したら、クライアントに再試行のタイミングを通知します。

@ExceptionHandler(RateLimitExceededException.class)
public ResponseEntity<ErrorResponse> handleRateLimitExceeded (RateLimitExceededException ex) {

    ErrorResponse  error  = ErrorResponse.builder() 
            .timestamp(LocalDateTime.now()) 
            .status(HttpStatus.TOO_MANY_REQUESTS.value()) 
            .error(HttpStatus.TOO_MANY_REQUESTS.getReasonPhrase()) 
            .errorCode(ErrorCodes.RATE_LIMIT_EXCEEDED) 
            .message( "リクエストが多すぎます。しばらくしてからもう一度お試しください。" ) 
            .build(); 
    
    return ResponseEntity 
            .status(HttpStatus.TOO_MANY_REQUESTS) 
            .header( "Retry-After" , "60" ) // クライアントは60秒後に再試行できます
            .body(error); 

}

15. 再試行、冪等性、回復力
    すべてのエラーを同じように扱うべきではありません。再試行できるものもあれば、できないものもあります。

再試行可能なエラーと再試行不可能なエラー：
再試行可能（一時的な障害）：

データベースのデッドロック
ネットワークタイムアウト
レート制限を超過しました（バックオフあり）
回路遮断器が開く（冷却後）
再試行不可（永続的な障害）：

検証エラー
ビジネスルール違反
リソースが見つかりません
認証/認可エラー
再試行ロジックの実装：

1. Spring Retry を使用する:

<dependency> <groupId> org.springframework.retry </groupId> <artifactId> spring - retry </artifactId> </dependency>​
    ​​​​​​​​​

@Configuration
@EnableRetry
public class RetryConfig {
}
@Service
public class PaymentService {

    // 一時的な失敗時に再試行
    @Retryable( 
        value = {PaymentGatewayTimeoutException.class, TransientDataAccessException.class}, 
        maxAttempts = 3, 
        backoff = @Backoff(delay = 1000, multiplier = 2) 
    ) 
    public PaymentResponse processPayment (PaymentRequest request) { 
        return paymentGateway.charge(request); 
    } 
    
    // すべての再試行が失敗した場合のフォールバック
    @Recover 
    public PaymentResponse recoverFromPaymentFailure ( 
            PaymentGatewayTimeoutException ex, 
            PaymentRequest request) { 
        
        log.error( "再試行後も支払いが失敗しました: {}" , ex.getMessage()); 
        throw  new  PaymentFailedException ( 
            "支払いサービスで問題が発生しています。注文は保存されています。自動的に再試行します。"
         ); 
    } 

}
再試行動作:

試行1：失敗 -> 1秒待機
試行2：失敗 -> 2秒待機（1 * 2）
試行3：失敗 -> 4秒待機（2 * 2）
試行3：失敗 -> @Recoverメソッドを呼び出す
冪等性:
クライアントが再試行する場合、操作が冪等であることを確認してください。

悪い（冪等性がない）：

@PostMapping("/orders")
public OrderResponse createOrder ( @RequestBody OrderRequest request) {
Order order = orderService.createOrder(request);
return OrderResponse.from(order);
}

// クライアントがネットワークタイムアウトで再試行した場合、
// 複数の注文が作成されます。
良好（冪等鍵付き冪等）：

@PostMapping("/orders")
public OrderResponse createOrder (
@RequestHeader("Idempotency-Key") String idempotencyKey,
@RequestBody OrderRequest request) {

    // この冪等キーを持つ注文が既に存在するかどうかを確認します
    Optional<Order> existing = orderService.findByIdempotencyKey(idempotencyKey); 
    if (existing.isPresent()) { 
        return OrderResponse.from(existing.get()); // 既存の注文を返します
    } 
    
    Order  order  = orderService.createOrder(request, idempotencyKey); 
    return OrderResponse.from(order); 

}
@Entity
public class Order {
@Id
private Long id;

    @Column(unique = true, nullable = false) 
    private String idempotencyKey; 
    
    // その他のフィールド

}
これで、クライアントが再試行しても、作成される注文は1件のみとなります。

Enterキーを押すか、画像をクリックしてフルサイズで表示します。

双子座
回路遮断器のパターン：
外部サービスがダウンしている場合は、しばらくの間、そのサービスへの呼び出しを停止してください。

Resilience4jを使用する:

<dependency> <groupId> io.github.resilience4j </groupId> <artifactId> resilience4j - spring - boot2 </artifactId> </dependency>​
    ​​​​​​​

@Service
public class InventoryService {

    @CircuitBreaker(name = "inventoryService", fallbackMethod = "fallbackCheckInventory") 
    public  boolean  checkInventory (Long productId, int quantity) { 
        // 外部在庫サービスを呼び出す
        return inventoryClient.checkAvailability(productId, quantity); 
    } 
    
    // サーキットブレーカーが開いている場合のフォールバック
    public  boolean  fallbackCheckInventory (Long productId, int quantity, Exception ex) { 
        log.error( "在庫サービスが利用できません。フォールバックを使用します。" , ex); 
        
        // オプション 1: 例外をスローする
        throw  new  InventoryServiceException ( 
            "在庫チェックが利用できません。後でもう一度お試しください。"
         ); 
        
        // オプション 2: キャッシュされたデータを使用する
        // return cachedInventoryService.checkInventory(productId, quantity); 
        
        // オプション 3: 利用可能とみなす (リスクあり) 
        // return true;
     } 

}
巡回裁判所は次のように述べている。

# application.yml

resilience4j:
circuitbreaker:
instances:
inventoryService:
slidingWindowSize:  10
failureRateThreshold:  50
waitDurationInOpenState:  10000
permittedNumberOfCallsInHalfOpenState:  3
再試行が盲目的であってはならない理由：
不良（すべてやり直す）：

@Retryable(value = Exception.class, maxAttempts = 5)
public Order createOrder (OrderRequest request) {
// ...
}
これは検証エラーに対しても再試行しますが、決して成功しません！

良好（一時的な障害のみ再試行）：

@Retryable(
value = {
PaymentGatewayTimeoutException.class,
DatabaseTimeoutException.class
},
maxAttempts = 3
)
public Order createOrder (OrderRequest request) {
// ...
}
再試行回数の枯渇への対処：
@Service
public class OrderService {

    @Retryable( 
        value = PaymentGatewayTimeoutException.class, 
        maxAttempts = 3, 
        backoff = @Backoff(delay = 1000) 
    ) 
    public Order createOrder (OrderRequest request) { 
        // 支払い処理
        paymentService.processPayment(request); 
        
        // 注文の作成
        return orderRepository.save(order); 
    } 
    
    @Recover 
    public Order recoverFromPaymentTimeout ( 
            PaymentGatewayTimeoutException ex, 
            OrderRequest request) { 
        
        // すべての再試行が失敗
        log.error( "支払いが 3 回再試行後にタイムアウトしました" , ex); 
        
        // 注文を PENDING 状態で保存
        Order  order  =  new  Order (); 
        order.setStatus(OrderStatus.PAYMENT_PENDING); 
        order = orderRepository.save(order); 
        
        // 後で再試行するようにバックグラウンド ジョブをスケジュール
        paymentRetryScheduler.scheduleRetry(order.getId()); 
        
        // クライアントに通知する
        throw  new  PaymentPendingException ( 
            "ご注文は保存されましたが、お支払いの処理中です。確認が取れ次第、通知されます。" , 
            order.getId() 
        ); 
    } 

}

16. 例外処理のテスト
    例外処理は、正常動作と同様にテストする必要があります。

例外マッピングの単体テスト：
@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock 
    private InventoryService inventoryService; 
    
    @Mock 
    private OrderRepository orderRepository; 
    
    @InjectMocks 
    private OrderService orderService; 
    
    @Test 
    void  createOrder_whenInsufficientInventory_throwsException () { 
        // Given 
        OrderRequest  request  =  new  OrderRequest ( /* ... */ ); 
        when(inventoryService.checkInventory(any())) 
                .thenReturn( false ); 
        
        // When & Then 
        InsufficientInventoryException  exception  = assertThrows( 
            InsufficientInventoryException.class, 
            () -> orderService.createOrder(request) 
        ); 
        
        assertEquals(ErrorCodes.INSUFFICIENT_INVENTORY, exception.getErrorCode()); 
        assertTrue(exception.getMessage().contains( "insufficient stock" )); 
    } 
    
    @Test 
    void  createOrder_whenPaymentFails_throwsException () { 
        // Given 
        OrderRequest  request  =  new  OrderRequest ( /* ... */ ); 
        when(inventoryService.checkInventory(any())).thenReturn( true ); 
        when(paymentService.processPayment(any(), any())) 
                .thenThrow( new  PaymentFailedException ( "Card declined" , "CARD_DECLINED" )); 
        
        // When & Then 
        PaymentFailedException  exception  = assertThrows( 
            PaymentFailedException.class, 
            () -> orderService.createOrder(request) 
        ); 
        
        assertEquals(ErrorCodes.PAYMENT_FAILED, exception.getErrorCode()); 
        assertEquals( "CARD_DECLINED" , exception.getPaymentGatewayCode()); 
    } 

}
エラー応答に対するコントローラテスト：
@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired 
    private MockMvc mockMvc; 
    
    @MockBean 
    private OrderService orderService; 
    
    @Test 
    void  createOrder_whenValidationFails_returns400 ()  throws Exception { 
        // Given - 無効なリクエスト (必須フィールドがありません) 
        String  invalidRequest  =  """ 
            { 
                "customerId": null, 
                "items": [] 
            } 
            """ ; 
        
        // When & Then
         mockMvc.perform(post( "/api/orders" ) 
                .contentType(MediaType.APPLICATION_JSON) 
                .content(invalidRequest)) 
            .andExpect(status().isBadRequest()) 
            .andExpect(jsonPath( "$.errorCode" ).value( "VALIDATION_FAILED" )) 
            .andExpect(jsonPath( "$.errors" ).isArray()) 
            .andExpect(jsonPath( "$.errors[*].field" ).value(hasItems( "customerId" , "items" ))); 
    } 
    
    @Test 
    void  createOrder_whenInsufficientInventory_returns409 ()  throws Exception { 
        // Given 
        OrderRequest  request  =  new  OrderRequest ( /* 有効なリクエスト */ ); 
        when(orderService.createOrder(any())) 
                .thenThrow( new  InsufficientInventoryException ( "在庫切れ" )); 
        
        // When & Then
         mockMvc.perform(post( "/api/orders" ) 
                .contentType(MediaType.APPLICATION_JSON) 
                .content(objectMapper.writeValueAsString(request))) 
            .andExpect(status().isConflict()) 
            .andExpect(jsonPath( "$.status" ).value( 409 )) 
            .andExpect(jsonPath( "$.errorCode" ).value( "INSUFFICIENT_INVENTORY" )) 
            .andExpect(jsonPath( "$.message" ).value(containsString( "stock" ))); 
    } 
    
    @Test 
    void  createOrder_whenOrderNotFound_returns404 ()  throws Exception { 
        // Given
         when(orderService.getOrder( 123L)) 
                .thenThrow( new  OrderNotFoundException ( 123L )); 
        
        // When & Then
         mockMvc.perform(get( "/api/orders/123" )) 
            .andExpect(status().isNotFound()) 
            .andExpect(jsonPath( "$.status" ).value( 404 )) 
            .andExpect(jsonPath( "$.errorCode" ).value( "ORDER_NOT_FOUND" )) 
            .andExpect(jsonPath( "$.traceId" ).exists()); 
    } 

}
エラーシナリオに対する統合テスト：
@SpringBootTest
@AutoConfigureMockMvc
class OrderIntegrationTest {

    @Autowired 
    private MockMvc mockMvc; 
    
    @Autowired 
    private OrderRepository orderRepository; 
    
    @MockBean 
    private PaymentGatewayClient paymentGatewayClient; 
    
    @Test 
    void  createOrder_whenPaymentGatewayTimeout_returns503 ()  throws Exception { 
        // Given - Payment gateway time out
         when(paymentGatewayClient.charge(any())) 
                .thenThrow( new  ResourceAccessException ( "Connection timeout" )); 
        
        OrderRequest  request  = OrderRequest.builder() 
                .customerId( 1L ) 
                .items(List.of( new  OrderItem ( 1L , 2 , new  BigDecimal ( "1000" )))) 
                .build(); 
        
        // When & Then
         mockMvc.perform(post( "/api/orders" ) 
                .contentType(MediaType.APPLICATION_JSON) 
                .content(objectMapper.writeValueAsString(request))) 
            .andExpect(status().isServiceUnavailable()) 
            .andExpect(jsonPath( "$.status" ).value( 503 ) ) 
            .andExpect(jsonPath( "$.errorCode" ).value( "PAYMENT_GATEWAY_TIMEOUT" )); 
        
        // 注文が作成されなかったことを検証
        assertEquals( 0 , orderRepository.count()); 
    } 
    
    @Test 
    void  createOrder_withDuplicateIdempotencyKey_returnsSameOrder ()  throws Exception { 
        // Given - 最初の注文が正常に作成された
        String  idempotencyKey  = UUID.randomUUID().toString(); 
        OrderRequest  request  =  /* 有効なリクエスト */ ; 
        
        // 最初のリクエスト
        MvcResult  result1  = mockMvc.perform(post( "/api/orders" ) 
                .header( "Idempotency-Key" , idempotencyKey) 
                .contentType(MediaType.APPLICATION_JSON) 
                .content(objectMapper.writeValueAsString(request))) 
            .andExpect(status().isCreated()) 
            .andReturn(); 
        
        OrderResponse  firstResponse  =objectMapper.readValue( 
            result1.getResponse().getContentAsString(), 
            OrderResponse.class 
        ); 
        
        // 同じ冪等性キーで再試行
        MvcResult  result2  = mockMvc.perform(post( "/api/orders" ) 
                .header( "Idempotency-Key" , idempotencyKey) 
                .contentType(MediaType.APPLICATION_JSON) 
                .content(objectMapper.writeValueAsString(request))) 
            .andExpect(status().isCreated()) 
            .andReturn(); 
        
        OrderResponse  secondResponse  = objectMapper.readValue( 
            result2.getResponse().getContentAsString(), 
            OrderResponse.class 
        ); 
        
        // 同じ注文が返される
        assertEquals(firstResponse.getOrderId(), secondResponse.getOrderId()); 
        
        // 1 つの注文のみが作成される
        assertEquals( 1 , orderRepository.count()); 
    } 

}
ネガティブパスのテスト:
ほとんどの開発者は正常系（ハッピーパス）をテストする。優秀な開発者は、異常系（ネガティブパス）もテストする。

@Test
void testAllValidationScenarios () {
// 各検証失敗を個別にテストします

    // 1. null 顧客 ID
     assertValidationFails( 
        request.withCustomerId( null ), 
        "customerId" , "顧客 ID は必須です"
     ); 
    
    // 2. 空のアイテム
    assertValidationFails( 
        request.withItems(List.of()), 
        "items" , "注文には少なくとも 1 つのアイテムが必要です"
     ); 
    
    // 3. 無効な数量
    assertValidationFails( 
        request.withItems(List.of( new  OrderItem ( 1L , 0 , BigDecimal.TEN))), 
        "items[0].quantity" , "数量は少なくとも 1 である必要があります"
     ); 
    
    // 4. 無効な PIN コード
    assertValidationFails( 
        request.withShippingAddress(address.withPinCode( "ABC" )), 
        "shippingAddress.pinCode" , "PIN コードは 6 桁である必要があります"
     ); 

}

@Test
void testAllBusinessRuleViolations () {
// 1. 在庫不足
// 2. 支払い失敗
// 3. 無効な注文状態遷移
// 4. 顧客非アクティブ
// ... 各ビジネスルールをテスト
}
@Test
void testAllInfrastructureFailures () {
// 1. データベースタイムアウト
// 2. 決済ゲートウェイダウン
// 3. 配送サービス利用不可
// ... 各統合ポイントをテスト
}
エラー応答に関する契約テスト：
エラー応答が予期せず変更されないようにしてください。

@Test
void errorResponse_hasRequiredFields ()  throws Exception {
// Given
when(orderService.getOrder( 999L ))
.thenThrow( new OrderNotFoundException ( 999L ));

    // When 
    MvcResult  result  = mockMvc.perform(get( "/api/orders/999" )) 
            .andExpect(status().isNotFound()) 
            .andReturn(); 
    
    String  json  = result.getResponse().getContentAsString(); 
    ErrorResponse  error  = objectMapper.readValue(json, ErrorResponse.class); 
    
    // Then - 契約の検証
    assertNotNull(error.getTimestamp()); 
    assertEquals( 404 , error.getStatus()); 
    assertEquals( "NOT_FOUND" , error.getError()); 
    assertEquals( "ORDER_NOT_FOUND" , error.getErrorCode()); 
    assertNotNull(error.getMessage()); 
    assertNotNull(error.getPath()); 
    assertNotNull(error.getTraceId()); 

}

17. ドキュメントとAPI契約
    クライアントがどのようなエラーが発生する可能性があるかを知らなければ、優れた例外処理は無意味である。

OpenAPI/Swaggerでエラー応答を文書化する：
@RestController
@RequestMapping("/api/orders")
@Tag(name = "Orders", description = "注文管理API")
public class OrderController {

    @Operation(summary = "新規注文の作成") 
    @ApiResponses(value = { 
        @ApiResponse( 
            responseCode = "201", 
            description = "注文が正常に作成されました", 
            content = @Content(schema = @Schema(implementation = OrderResponse.class)) 
        ), 
        @ApiResponse( 
            responseCode = "400", 
            description = "無効な入力またはビジネスルール違反", 
            content = @Content( 
                schema = @Schema(implementation = ErrorResponse.class), 
                examples = { 
                    @ExampleObject( 
                        name = "検証エラー", 
                        value = """ 
                            { 
                              "status": 400, 
                              "errorCode": "VALIDATION_FAILED", 
                              "message": "検証に失敗しました", 
                              "errors": [ 
                                { 
                                  "field": "items[0].quantity", 
                                  "rejectedValue": 0, 
                                  "message": "数量は1以上である必要があります" 
                                } 
                              ] 
                            } 
                            """ 
                    ), 
                    @ExampleObject( 
                        name = "残高不足", 
                        value = """ 
                            { 
                              "status": 400, 
                              "errorCode": "INSUFFICIENT_BALANCE", 
                              "message": "残高: ₹500、必要額: ₹2000" 
                            } 
                            """ 
                    ) 
                } 
            ) 
        ), 
        @ApiResponse( 
            responseCode = "404", 
            description = "顧客または商品が見つかりません",
            content = @Content( 
                schema = @Schema(implementation = ErrorResponse.class), 
                examples = @ExampleObject( 
                    value = """ 
                        { 
                          "status": 404,
                          "errorCode": "CUSTOMER_NOT_FOUND", 
                          "message": "顧客が見つかりません" 
                        } 
                        """ 
                ) 
            ) 
        ), 
        @ApiResponse( 
            responseCode = "409", 
            description = "在庫不足または重複注文", 
            content = @Content( 
                schema = @Schema(implementation = ErrorResponse.class), 
                examples = @ExampleObject( 
                    value = """ 
                        { 
                          "status": 409, 
                          "errorCode": "INSUFFICIENT_INVENTORY", 
                          "message": "製品「Laptop」の在庫は5個のみです" 
                        } 
                        """ 
                ) 
            ) 
        ), 
        @ApiResponse( 
            responseCode = "503", 
            description = "外部サービスが利用できません", 
            content = @Content( 
                schema = @Schema(implementation = ErrorResponse.class), 
                examples = @ExampleObject( 
                    value = """ 
                        { 
                          "status": 503, 
                          "errorCode": "PAYMENT_GATEWAY_TIMEOUT", 
                          "message": "決済サービスが一時的に利用できません" 
                        } 
                        """ 
                ) 
            ) 
        ) 
    }) 
    @PostMapping 
    public ResponseEntity<OrderResponse> createOrder ( 
            @Valid  @RequestBody OrderRequest request) { 
        OrderResponse  response  = orderService.createOrder(request); 
        return ResponseEntity.status(HttpStatus.CREATED).body(response); 
    } 

}
エラー応答の例を以下に示します。
顧客は実際の事例を見る必要がある。

READMEまたはAPIドキュメント内：

## エラー応答

すべてのエンドポイントは、同じエラー応答形式に従います:
{
"timestamp" : "2026-01-30T10:15:30Z" ,
"status" : 400,
"error" : "BAD_REQUEST" ,
"errorCode" : "INSUFFICIENT_INVENTORY" ,
"message" : "Product out of stock" ,
"path" : "/api/orders" ,
"traceId" : "abc-123-xyz"
}

### 例: 在庫不足で注文を作成する

**リクエスト:**
POST /api/orders
{
"customerId" : 123,
"items" : [
{ "productId" : 456, "quantity" : 100 }
]
}
**応答:** 409 Conflict
{
"timestamp" : "2026-01-30T10:15:30Z"、
"status" : 409、
"error" : "CONFLICT"、
"errorCode" : "INSUFFICIENT_INVENTORY"、
"message" : "製品「Laptop」の在庫は5個のみです。お客様は100個をリクエストされました。"、
"path" : "/api/orders"、
"traceId" : "abc-123-xyz"
}
エラーコードに関するドキュメントを公開する:
クライアントが参照できる中央エラーコード登録簿を維持する。

# エラーコード参照

## 注文エラー

### 注文が見つかりません

- **HTTP ステータス:** 404
- **原因:**指定された注文 ID が存在しません
- **回復:**注文 ID を確認して再試行してください
- **再試行可能:**いいえ
  ###注文は既にキャンセルされています-
  **  HTTP ステータス:** 409
- **原因:**注文は既にキャンセルされています
- **回復:**アクションは不要です
- **再試行可能:**いいえ
  ###注文はキャンセルできません-
  **  HTTP ステータス:** 409
- **原因:**注文は SHIPPED または DELIVERED ステータスです
- **回復:**返金についてはサポートにお問い合わせください
- **再試行可能:**いいえ

## 在庫エラー

###在庫不足

- **HTTP ステータス:** 409
- **原因:**要求された数量が利用可能な在庫を超えています
- **回復:**数量を減らすか、再入荷をお待ちください
- **再試行可能:**はい、しばらくしてから
- **例:**
   ```json 
  { 
    "errorCode": "INSUFFICIENT_ INVENTORY", 
    "message": "製品「Laptop」の在庫は5個のみです" 
  }

支払いエラー
支払い失敗
HTTPステータス: 400
原因：決済処理の失敗（カード拒否、残高不足など）
復旧：別の支払い方法を使用する
再試行可能：はい、別の支払い方法で
決済ゲートウェイタイムアウト
HTTPステータス: 503
原因：決済ゲートウェイサービスが利用できません
復旧：数分後に再試行してください
再試行可能:はい
Retry-After:レスポンスヘッダーに含まれています

### エラー ドキュメントをコードと同期させる:

**テストを使用してドキュメント化を強制します:**

```java 
@Test 
void  allErrorCodesAreDocumented () { 
    // コードからすべてのエラー コードを取得します
    Set<String> codeErrorCodes = Arrays.stream(ErrorCodes.class.getDeclaredFields()) 
            .filter(field -> Modifier.isStatic(field.getModifiers())) 
            .map(field -> { 
                try { 
                    return (String) field.get( null ); 
                } catch (IllegalAccessException e) { 
                    return  null ; 
                } 
            }) 
            .filter(Objects::nonNull) 
            .collect(Collectors.toSet()); 
    
    // ドキュメントからすべてのエラー コードを取得します
    Set<String> documentedErrorCodes = parseErrorCodesFromMarkdown( "ERROR_CODES.md" ); 
    
    // すべてのコードがドキュメント化されていることを確認します
    Set<String> undocumented = new  HashSet <>(codeErrorCodes); 
    undocumented.removeAll(documentedErrorCodes); 
    
    assertTrue( 
        undocumented.isEmpty(), 
        "以下のエラーコードはドキュメント化されていません: " + undocumented 
    ); 
}
注釈駆動型ドキュメントを使用する：

@Documented 
@ErrorCode( 
    code = "INSUFFICIENT_INVENTORY", 
    httpStatus = 409, 
    description = "要求された数量が利用可能な在庫を超えています", 
    recoveryAction = "数量を減らすか、在庫補充をお待ちください", 
    retryable = true 
) 
public  class  InsufficientInventoryException  extends  BusinessException { 
    // ...
 }
次に、これらの注釈からドキュメントを生成します。

18．避けるべきよくある間違い
アンチパターンとその修正方法について見ていきましょう。

間違い1：あらゆる場所で例外をキャッチする
悪い：

@Service 
public  class  OrderService { 
    
    public Order createOrder (OrderRequest request) { 
        try { 
            // ビジネスロジック
            return order; 
        } catch (Exception e) { 
            log.error( "注文の作成エラー" , e); 
            return  null ; // すべての例外を捕捉
        } 
    } 
}
問題点：

特定のエラータイプを非表示にします
コントローラーは何が問題だったのか分からない
クライアントは有用な情報を得られない
異なるエラーを異なる方法で処理することはできません
良い：

@Service 
public  class  OrderService { 
    
    public Order createOrder (OrderRequest request) { 
        // 例外を伝播させる
        // グローバルハンドラが例外をキャッチして適切に処理します
        
        if (inventoryCheck fails) { 
            throw  new  InsufficientInventoryException ( "..." ); 
        } 
        
        if (payment fails) { 
            throw  new  PaymentFailedException ( "..." ); 
        } 
        
        return order; 
    } 
}
間違い2：生の例外メッセージを返す
悪い：

@ExceptionHandler(Exception.class) 
public ResponseEntity<String> handleException (Exception ex) { 
    return ResponseEntity 
            .status( 500 ) 
            .body(ex.getMessage()); // 内部の詳細を公開
}
問題点：

スタックトレースを公開する
データベースエラーを明らかにする
内部経路を公開する
機械で読み取れません
良い：

@ExceptionHandler(Exception.class) 
public ResponseEntity<ErrorResponse> handleException (Exception ex) { 
    log.error( "予期しないエラーが発生しました" , ex); // 詳細なログを出力
    
    ErrorResponse  error  = ErrorResponse.builder() 
            .status( 500 ) 
            .errorCode( "INTERNAL_SERVER_ERROR" ) 
            .message( "予期しないエラーが発生しました" ) // 安全なメッセージ
            .traceId(getTraceId()) 
            .build(); 
    
    return ResponseEntity.status( 500 ).body(error); 
}
間違い3：エンドポイントごとに異なるエラー形式を使用する
悪い：

// エンドポイント 1 
{ 
  "error" :  "無効な入力" 
} 

// エンドポイント 2 
{ 
  "message" :  "見つかりません" , 
  "code" :  404 
} 

// エンドポイント 3 
{ 
  "status" :  "error" , 
  "reason" :  "支払いに失敗しました" 
}
問題点：

クライアントは一貫して解析できない
各エンドポイントごとに異なる処理コードが必要
メンテナンスの悪夢
良い：

// すべてのエンドポイントは同じ ErrorResponse 構造を使用します
{ 
  "timestamp" :  "..." , 
  "status" :  404 , 
  "error" :  "NOT_FOUND" , 
  "errorCode" :  "ORDER_NOT_FOUND" , 
  "message" :  "..." , 
  "path" :  "..." , 
  "traceId" :  "..." 
}
間違い4：同じ例外を複数回ログに記録してスローする
悪い：

// サービス層
try { 
    processPayment(); 
} catch (PaymentException e) { 
    log.error( "支払い失敗" , e); // ここでログを出力
    throw e; 
} 

// コントローラー層
try { 
    orderService.createOrder(request); 
} catch (PaymentException e) { 
    log.error( "注文作成失敗" , e); // 再度ログを出力
    throw e; 
} 

// グローバルハンドラー
@ExceptionHandler(PaymentException.class) 
public ResponseEntity<?> handle(PaymentException e) { 
    log.error( "支払い例外の処理中" , e); // 3回目のログ出力！
    return ...; 
}
問題点：

散らかった丸太
同じエラーが3回表示されます
実際の問題点を特定するのは難しい
良い：

// サービス層 - 単に例外をスローする
if (payment fails) { 
    throw  new  PaymentFailedException ( "..." ); 
} 

// コントローラー層 - try-catch は使用せず、例外を伝播させる
public OrderResponse createOrder (OrderRequest request) { 
    return orderService.createOrder(request); 
} 

// グローバルハンドラー - 一度だけログに記録する
@ExceptionHandler(PaymentFailedException.class) 
public ResponseEntity<ErrorResponse> handle (PaymentFailedException ex) { 
    log.error( "Payment failed: {}" , ex.getMessage()); // 一度だけログに記録する
    return ...; 
}
間違い5：例外を黙って受け入れる
悪い：

try { 
    updateInventory(productId, quantity); 
} catch (Exception e) { 
    // サイレントエラー - ログなし、例外スローなし
} 

// 何も起こらなかったかのようにコードが続行されます
// 在庫は更新されませんが、誰もそれを知りません
問題点：

サイレントデータ破損
デバッグ不可能
未定義のシステム状態
良い：

// オプション 1: 伝播させる
updateInventory(productId, quantity); 

// オプション 2: キャッチする必要がある場合は、少なくともログに記録する
try { 
    updateInventory(productId, quantity); 
} catch (Exception e) { 
    log.error( "製品 {} の在庫の更新に失敗しました" , productId, e); 
    throw  new  InventoryUpdateException ( "在庫の更新に失敗しました" , e); 
}
間違い6：制御フローに例外を使用する
悪い：

public Customer getCustomer (Long id) { 
    try { 
        return customerRepository.findById(id).get(); // 見つからない場合は例外をスロー
    } catch (NoSuchElementException e) { 
        return createDefaultCustomer(); // 例外をロジックに使用
    } 
}
問題点：

コストがかかる（スタックトレースの作成）
誤解を招く（例外は例外的なものであるべき）
意図を理解するのは難しい
良い：

public Customer getCustomer (Long id) { 
    return customerRepository.findById(id) 
            .orElseGet( this ::createDefaultCustomer); // インテントをクリア
}
間違い7：トレース/相関IDを含めていない
悪い：

{ 
  "status" :  500 , 
  "message" :  "内部サーバーエラー" 
}
顧客がサポートに問い合わせた場合：

サポート：「どのようなエラーが発生しましたか？」
顧客：「内部サーバーエラー」
支持：「いつ？」
顧客：「午前10時頃だったと思います。」
サポート：「どのページですか？」
顧客：「えっと…チェックアウトページですか？」
サポート担当者は、数百万件のログエントリの中から特定のエラーを見つけることができませんでした。

良い：

{ 
  "status" :  500 , 
  "message" :  "内部サーバーエラー" , 
  "traceId" :  "abc-123-xyz" 
}
顧客：「abc-123-xyzというエラーが発生しました」 サポート：ログをtraceIdで検索→正確なリクエストを見つけ、完全なエラーを確認し、迅速に修正します

19．生産・保守の観点から
適切な例外処理は、本番環境での作業を楽にしてくれます。

アーキテクチャの一部としての例外処理：
機能を設計する際には、その機能の失敗も設計に含めるべきです。

機能: 注文のキャンセル

正常パス: 
1.注文が存在するか確認する2.
注文ステータスを確認する
3.返金を処理する
4.注文ステータスを更新する

失敗シナリオ: 
1.注文が見つからない -> 404 ORDER_NOT_FOUND 2.
注文が既にキャンセルされている -> 409 ORDER_ALREADY_CANCELLED 3.
注文が既に発送されている -> 409 ORDER_CANNOT_CANCEL 4.返金
が失敗した-> 400 PAYMENT_FAILED 5.
データベースエラー -> 500 DATABASE_ERROR各

失敗について: 
-クライアントには何が表示されるべきか? 
-何がログに記録されるべきか? 
-再試行すべきか? 
-どのように復旧するか?
エラー応答の後方互換性：
破壊的変更（避けるべき！）：

// バージョン 1 
{ 
  "errorCode" :  "OUT_OF_STOCK" 
} 

// バージョン 2 - 互換性のない変更！
{ 
  "errorCode" :  "INSUFFICIENT_INVENTORY"  // OUT_OF_STOCK をチェックするクライアントは動作しなくなります
}
互換性を損なわない変更（問題なし）：

// バージョン 1 
{ 
  "errorCode" :  "PAYMENT_FAILED" , 
  "message" :  "支払い失敗" 
} 

// バージョン 2 - 新しいフィールドの追加は問題ありません
{ 
  "errorCode" :  "PAYMENT_FAILED" , 
  "message" :  "支払い失敗" , 
  "retryable" :  true ,  // 新しいフィールドを追加
  "retryAfter" :  60    // 新しいフィールドを追加
}
戦略：

// 古いエラーコードを保持し、新しいエラーコードを追加する
public  class  ErrorCodes { 
    @Deprecated 
    public  static  final  String  OUT_OF_STOCK  =  "OUT_OF_STOCK" ; // 後方互換性のために保持
    
    public  static  final  String  INSUFFICIENT_INVENTORY  =  "INSUFFICIENT_INVENTORY" ; // 新しい推奨コード
} 

// 内部的に古いコードを新しいコードにマッピングする
if (errorCode.equals( "OUT_OF_STOCK" )) { 
    errorCode = "INSUFFICIENT_INVENTORY" ; 
}
エラー契約の移行戦略：
エラー応答を変更する必要がある場合：

フェーズ1：新しいフィールドを追加する

// 古い構造を維持し、新しい構造を追加する
{ 
  "error" : "無効な入力" ,   // 古い形式
  "errorCode" : "VALIDATION_FAILED" ,   // 新しい形式
  "errors" : [...]   // 新しい形式
}
フェーズ2：廃止通知

リリースノート：
- 旧エラー形式は非推奨です-バージョン3.0で
削除されます-新しい形式に移行してください
 
フェーズ3：古いフィールドを削除する（メジャーバージョンアップ）

{ 
  "errorCode" :  "VALIDATION_FAILED" ,   // 新しい形式のみ
  "errors" :  [ ... ] 
}
優れた例外処理がオンコールエンジニアにどのように役立つか：
例外処理が不適切です。

午前3時00分 - アラート：エラー率が高い
午前3時05分 - ログを確認：「エラーが発生しました」
午前3時10分 - どのエラー？どのサービス？
午前3時20分 - 再現を試みる
午前3時40分 - まだ原因がわからない
午前4時00分 - 他のチームメンバーを起こす
午前5時00分 - ついに問題を発見
適切な例外処理：

午前3時00分 - アラート: PAYMENT_GATEWAY_TIMEOUT エラーが急増
午前3時02分 - ダッシュボードを確認: 決済ゲートウェイがダウン
午前3時03分 - 決済ゲートウェイのステータスページを確認: インシデント発生中
午前3時05分 - フォールバック決済方法を有効にする
午前3時10分 - 就寝
エラーコードに基づくランブック：
# オンコールランブック

## 決済ゲートウェイタイムアウト
**重要度:**高
**症状:**ユーザーが購入を完了できない
**原因:**決済ゲートウェイがダウンしているか、動作が遅い
**調査:** 
1.決済ゲートウェイのステータスページを確認する
2.最近のデプロイメントを確認する
3.ネットワーク接続を確認する
4.決済ゲートウェイのログを確認する
**即時対応:** 
1.フォールバック決済方法を有効にする
2.顧客にステータス更新を投稿する
3.決済ゲートウェイのサポートに連絡する
**解決策:** 
-ゲートウェイの問題の場合: ゲートウェイの解決を待つ
-当社の問題の場合: 最近の変更を元に戻す
-ネットワークの問題の場合: ネットワーク構成を修正する

##在庫不足
**重要度:**低
**症状:**顧客は人気商品に「在庫切れ」と表示される
**原因:**需要が高いか、在庫同期の問題
**調査:**
 1. 倉庫システムの実際の在庫を確認する
2. 在庫同期ジョブのステータスを確認する
3. 特定の商品に特有の問題かどうかを確認する
**アクション:**
 1. 手動在庫同期をトリガーする
2. 同期が失敗した場合は、統合を確認する
3. 必要に応じて顧客のETAを更新する
これにより、オンコール担当のエンジニアは、チーム全体を起こさずに、特定のエラーに迅速に対応できます。

取り上げた内容：
なぜ重要なのか：セキュリティ、デバッグ、ユーザーエクスペリエンス
原則：ビジネスエラーと技術エラー、クライアントメッセージと開発者メッセージ
分類：責任、回復可能性、種類別
例外処理の流れ：例外をスローする場所、キャッチする場所、変換する場所
エラー契約：標準応答構造、必須フィールド
エラーコード：命名、安定性、ドキュメント
HTTPステータスマッピング：各エラータイプに対応する適切なステータス
グローバルな処理：@ControllerAdvice を唯一の信頼できる情報源として扱う
検証：Bean検証、フィールドレベルのエラー
ビジネス例外：ドメインルールを例外として扱う
インフラストラクチャエラー：境界での翻訳
ログ記録：ログに記録すべき内容、記録すべきでない内容、トレースID
モニタリング：指標、ダッシュボード、アラート
セキュリティ：内部情報を決して公開しないでください
再試行と回復力：サーキットブレーカー、冪等性
テスト：単体テスト、結合テスト、契約テスト
ドキュメント：OpenAPI、エラーコードリファレンス
間違い：避けるべき一般的なアンチパターン
運用：後方互換性、オンコールランブック
建築：例外を第一級市民として扱う
