---
name: api-design
description: CQRSに基づくCommand ServiceとQuery Serviceの分離設計、REST API設計、Spring Cloud Gatewayルーティング、APIバージョン管理、エラーハンドリング、セキュリティ、OpenAPIドキュメントの設計ルールを提供。
---

### API Design Guide (`api-design`)

このスキルは、 **modulith-learning** リポジトリにおいて、**CQRS（Command Query Responsibility Segregation）** に基づくCommand ServiceとQuery Serviceの分離設計、REST APIの設計、API
Gatewayのルーティング設定、APIバージョン管理、エラーハンドリング戦略、セキュリティ設計、OpenAPIドキュメントの作成を行う際に適用する設計ガイドラインです。

---

### 1. いつこのスキルを使用するか (When To Use)

- 新規REST APIエンドポイントの設計・実装を行うとき
- API Gatewayのルーティングルールを設定するとき
- APIのバージョン管理戦略を設計するとき
- エラーレスポンスの形式を統一するとき
- APIのセキュリティ設計（認証・認可）を行うとき
- OpenAPI/Swaggerドキュメントを作成・更新するとき
- APIのレートリミッティングやリクエスト制限を設計するとき
- APIの監視・ログ設計を行うとき
- **Command Service（書き込み）とQuery Service（読み込み）を分離して設計するとき**

---

### 2. CQRS設計 (Command Query Responsibility Segregation)

#### 2.1 CQRSの基本概念

CQRS（Command Query Responsibility Segregation）は、**書き込み操作（Command）** と**読み込み操作（Query）** を分離する設計パターンです。

| 区分 | Command Service | Query Service |
|:---|:---|:---|
| 責務 | データの作成・更新・削除 | データの読み込み・検索 |
| HTTPメソッド | POST, PUT, PATCH, DELETE | GET |
| 戻り値 | 処理結果（ステータスコード・ID） | データ（エンティティ・一覧） |
| 最適化対象 | 一貫性・ビジネスルール | パフォーマンス・キャッシュ |
| トランザクション | 必須 | 不要（スナップショット読み込み可能） |
| キャッシュ | 不要 | 積極的に活用 |

#### 2.2 Command Service 設計

**Command Service** は、データの書き込み操作を担当します。

- **パッケージ構成**: `undecided.<domain>.<module>.useCase.command`
- **命名規則**: `[Entity]CommandService`（例: `NotificationCommandService`）
- **メソッド命名**:
  - 作成: `create[Entity](Command)` → `createNotification(CreateNotificationCommand)`
  - 更新: `update[Entity](Command)` → `updateNotification(UpdateNotificationCommand)`
  - 削除: `delete[Entity](Command)` → `deleteNotification(DeleteNotificationCommand)`
  - 状態遷移: `[action][Entity](Command)` → `markAsRead(MarkAsReadCommand)`
- **Commandオブジェクト**: 書き込みリクエストを表現する値オブジェクト
  - 不変（`final` クラス + `final` フィールド）
  - バリデーションアノテーションを付与
  - 例: `CreateNotificationCommand`, `MarkAsReadCommand`
- **戻り値**: 処理結果のIDまたはステータスを返す
  - 作成: 作成されたエンティティのIDを返す
  - 更新・削除: `void` またはステータスを返す

#### 2.3 Query Service 設計

**Query Service** は、データの読み込み操作を担当します。

- **パッケージ構成**: `undecided.<domain>.<module>.useCase.query`
- **命名規則**: `[Entity]QueryService`（例: `NotificationQueryService`）
- **メソッド命名**:
  - 単一取得: `get[Entity](Query)` → `getNotification(GetNotificationQuery)`
  - 一覧取得: `list[Entity](Query)` → `listNotifications(ListNotificationsQuery)`
  - 検索: `search[Entity](Query)` → `searchNotifications(SearchNotificationsQuery)`
  - カウント: `count[Entity](Query)` → `countNotifications(CountNotificationsQuery)`
- **Queryオブジェクト**: 読み込みリクエストを表現する値オブジェクト
  - 不変（`final` クラス + `final` フィールド）
  - 検索条件・ページネーションパラメータを含む
  - 例: `GetNotificationQuery`, `ListNotificationsQuery`
- **戻り値**: 読み込み結果のDTOを返す
  - 単一取得: `Optional<[Entity]Dto>` または `[Entity]Dto`
  - 一覧取得: `Page<[Entity]Dto>` または `List<[Entity]Dto>`

#### 2.4 Controller 層の分離

プレゼンテーション層では、CommandとQueryでコントローラーを分離します。

| コントローラー | 責務 | 例 |
|:---|:---|:---|
| `[Entity]CommandController` | 書き込みAPI | `NotificationCommandController` |
| `[Entity]QueryController` | 読み込みAPI | `NotificationQueryController` |

#### 2.5 APIエンドポイントの設計ルール

CQRS分離に基づくAPIエンドポイントの設計ルール：

| 操作 | HTTPメソッド | エンドポイント | Service | Command/Query |
|:---|:---|:---|:---|:---|
| 一覧取得 | GET | `/api/v1/[resources]` | QueryService | Query |
| 単一取得 | GET | `/api/v1/[resources]/{id}` | QueryService | Query |
| 作成 | POST | `/api/v1/[resources]` | CommandService | Command |
| 全体更新 | PUT | `/api/v1/[resources]/{id}` | CommandService | Command |
| 部分更新 | PATCH | `/api/v1/[resources]/{id}` | CommandService | Command |
| 削除 | DELETE | `/api/v1/[resources]/{id}` | CommandService | Command |
| 状態遷移 | PATCH | `/api/v1/[resources]/{id}/[action]` | CommandService | Command |

#### 2.6 CQRS分離のメリット

- **責務の明確化**: 書き込みと読み込みの分離により、各サービスの責務が明確になる
- **独立した最適化**: 書き込みは一貫性、読み込みはパフォーマンスを最適化できる
- **スケーラビリティ**: 読み込みと書き込みを独立してスケールできる
- **テスト容易性**: CommandとQueryを独立してテストできる

---

### 3. API設計原則 (API Design Principles)

#### 3.1 REST API設計原則

- **リソース指向**: URLはリソースを名詞の複数形で表現し、HTTPメソッドで操作を表現する。
- **HTTPメソッドの適切な使用**: GET（取得）、POST（作成）、PUT（全体更新）、PATCH（部分更新）、DELETE（削除）。
- **ステータスコードの適切な使用**: 200（成功）、201（作成）、204（No
  Content）、400（クライアントエラー）、401（Unauthorized）、403（Forbidden）、404（NotFound）、500（サーバーエラー）。
- **幂等性**: GET、PUT、DELETEは幂等（Idempotent）に設計する。
- **HATEOAS**: 必要に応じてレスポンスに関連リソースへのリンクを含める。

#### 3.2 APIバージョン管理

- **URLベースのバージョン管理**: `/api/v1/`、`/api/v2/` の形式でバージョンを明示。
- **後方互換性の維持**: 既存のAPIを変更する場合は、新しいバージョンを追加し、旧バージョンは廃止スケジュールを明示。
- **デプロケーション通知**: APIの廃止予定は `Deprecation` ヘッダーで通知。

#### 3.3 リソース命名規則

| 要素             | 規則                                 | 例                                   |
|:-----------------|:-------------------------------------|:-------------------------------------|
| リソース名       | 名詞の複数形、小文字、ハイフン区切り | `/api/customers`, `/api/order-items` |
| サブリソース     | スラッシュで階層を表現               | `/api/customers/{id}/orders`         |
| クエリパラメータ | camelCase                            | `?pageSize=10&pageNumber=0`          |
| パスパラメータ   | リソースのID                         | `/api/customers/{customerId}`        |

---

### 4. リクエスト・レスポンス設計 (Request & Response Design)

#### 4.1 リクエスト設計

- **リクエストDTO**: バリデーションアノテーション（`@NotNull`, `@Size`, `@Email` など）を付与。
- **リクエストボディ**: JSON形式で、必要なフィールドのみを含める。
- **クエリパラメータ**: 検索条件、ページネーション、ソート条件をクエリパラメータで表現。
- **ヘッダー**: 認証トークン、Accept-Language、Content-Typeなどを適切に使用。

#### 4.2 レスポンス設計

- **レスポンスDTO**: 機密情報を除外し、API消費者に必要な情報のみを公開。
- **一貫したレスポンス形式**: 成功・エラーともに一貫したJSON構造を返す。
- **ページネーション**: 大量のデータはページネーションで返す。

```json
{
  "data": [...],
  "metadata": {
    "pageSize": 10,
    "pageNumber": 0,
    "totalElements": 100,
    "totalPages": 10
  }
}
```

#### 4.3 エラーレスポンス設計

- **一貫したエラー形式**: すべてのエラーレスポンスは統一された形式で返す。

```json
{
  "error": {
    "status": 400,
    "code": "VALIDATION_ERROR",
    "message": "リクエストの検証に失敗しました",
    "details": [
      {
        "field": "email",
        "message": "有効なメールアドレスを入力してください"
      }
    ],
    "timestamp": "2024-01-01T00:00:00Z"
  }
}
```

- **エラーコード**: ビジネスロジックに応じたエラーコードを定義し、ドキュメント化。

---

### 5. API Gateway設計 (API Gateway Design)

#### 5.1 Spring Cloud Gatewayの役割

- **単一エントリポイント**: すべての外部リクエストはAPI Gatewayを経由。
- **ルーティング**: パスベースのルーティングでバックエンドサービスにリクエストを転送。
- **サービスディスカバリー**: Eurekaによる動的なサービス発見とロードバランシング。
- **クロスカット関心事**: 認証・認可、ログ、レートリミッティングをGatewayで一元管理。

#### 5.2 ルーティング設定

- **YAMLベースのルーティング**: `application.yml` でルーティングルールを宣言的に定義。

```yaml
spring:
  cloud:
    gateway:
      routes:
        - id: backend-service
          uri: lb://backend-service
          predicates:
            - Path=/api/**
          filters:
            - StripPrefix=0
```

- **ルーティング規則**:
    - `id`: ルートの一意の識別子。
    - `uri`: 転送先URI（`lb://` でロードバランシング有効）。
    - `predicates`: ルーティング条件（Path、Method、Headerなど）。
    - `filters`: リクエスト/レスポンスの変換フィルタ。

#### 5.3 フィルタ設計

- **グローバルフィルタ**: 全ルートに適用される共通フィルタ（ログ、セキュリティヘッダー）。
- **ルートフィルタ**: 特定のルートにのみ適用されるフィルタ。
- **フィルタの種類**:
    - `AddRequestHeader`: リクエストヘッダーの追加。
    - `StripPrefix`: パスプレフィックスの削除。
    - `RewritePath`: パスの書き換え。
    - `RequestRateLimiter`: レートリミッティング。

---

### 6. バリデーション設計 (Validation Design)

#### 6.1 リクエストバリデーション

- **DTOレベルのバリデーション**: Jakarta Validationアノテーションを使用。
- **カスタムバリデーター**: ビジネスロジックに依存するバリデーションはカスタムアノテーションで実装。
- **グループバリデーション**: 作成・更新で異なるバリデーションルールを適用。

#### 6.2 バリデーションエラーレスポンス

- **フィールドレベルのエラー**: 不正なフィールド名とエラーメッセージを返す。
- **オブジェクトレベルのエラー**: 全体のバリデーションエラーを返す。
- **エラーメッセージのローカライズ**: 必要に応じてメッセージをローカライズ。

---

### 7. セキュリティ設計 (Security Design)

#### 7.1 認証・認可

- **認証方式**: JWT（JSON Web Token）またはOAuth 2.0を使用。
- **トークン管理**: 認証トークンはHTTPS経由で安全に伝送。
- **認可**: RBAC（Role-Based Access Control）またはABAC（Attribute-Based Access Control）を採用。

#### 7.2 API Gatewayでのセキュリティ

- **CORS設定**: 許可されたオリジンからのリクエストのみを許可。
- **セキュリティヘッダー**: `X-Content-Type-Options`, `X-Frame-Options`, `Strict-Transport-Security`
  などを追加。
- **レートリミッティング**: DDoS対策としてリクエスト制限を適用。
- **IPフィルタリング**: 必要に応じてIPアドレスベースのアクセス制御。

#### 7.3 セキュリティベストプラクティス

- **HTTPSの強制**: すべて通信をHTTPSで暗号化。
- **入力サニタイズ**: SQLインジェクション、XSS対策として入力を検証・サニタイズ。
- **機密情報の保護**: パスワードやトークンはレスポンスに含まれないようにする。
- **ログのセキュリティ**: 機密情報をログに記録しない。

---

### 8. OpenAPIドキュメント設計 (OpenAPI Documentation Design)

#### 8.1 OpenAPI仕様

- **springdoc-openapi**: Spring BootでのOpenAPI 3.0仕様生成ライブラリを使用。
- **自動ドキュメント生成**: コードから自動的にOpenAPI仕様を生成。
- **Swagger UI**: `http://localhost:8080/swagger-ui.html` でAPIドキュメントを閲覧・テスト。

#### 8.2 APIドキュメントの品質

- **エンドポイントの説明**: 各エンドポイントに概要と詳細な説明を記載。
- **リクエスト・レスポンス例**: 各エンドポイントにリクエスト・レスポンスの例を記載。
- **エラーレスポンスのドキュメント化**: 発生しうるエラーレスポンスをドキュメント化。
- **スキーマの説明**: リクエスト・レスポンスの各フィールドに説明を記載。

#### 8.3 OpenAPIアノテーション

- `@Operation`: エンドポイントの概要と説明。
- `@Parameter`: パラメータの説明。
- `@ApiResponse`: レスポンスの説明と例。
- `@Schema`: フィールドの説明と制約。

---

### 9. ページネーション・ソート設計 (Pagination & Sorting Design)

#### 9.1 ページネーション

- **リクエストパラメータ**: `pageSize`, `pageNumber` を使用。
- **レスポンスメタデータ**: 総件数、総ページ数、現在のページ情報を返す。
- **デフォルト値**: `pageSize` のデフォルト値を設定（例: 20）。
- **最大値制限**: `pageSize` の最大値を制限（例: 100）。

#### 9.2 ソート

- **リクエストパラメータ**: `sort` パラメータでソート条件を指定。
- **ソート形式**: `sort=fieldName,ASC` または `sort=fieldName,DESC`。
- **複数ソート**: カンマ区切りで複数ソート条件を指定。

---

### 10. API監視・ログ設計 (API Monitoring & Logging Design)

#### 10.1 リクエストログ

- **ログレベル**: INFOレベルでリクエスト・レスポンスの概要をログ。
- **ログ内容**: メソッド、パス、ステータスコード、処理時間、クライアントIP。
- **機密情報の除外**: トークンやパスワードはログに記録しない。

#### 10.2 パフォーマンス監視

- **処理時間の計測**: 各リクエストの処理時間を計測・ログ。
- **エラー率の監視**: HTTPエラーの発生率を監視。
- **メトリクス**: Micrometerによるメトリクス収集とPrometheus/Grafanaでの可視化。

#### 10.3 ヘルスチェック

- **Liveness Probe**: アプリケーションの生存確認。
- **Readiness Probe**: サービスのリクエスト受信可否確認。
- **カスタムヘルスインジケーター**: データベース接続などの依存サービスのステータスを報告。

---

### 11. 設計チェックリスト (Design Checklist)

設計時・実装時に以下の項目を確認してください。

- [ ] **CQRS分離**: Command ServiceとQuery Serviceが適切に分離されている
- [ ] **Command Service**: 書き込み操作がCommand Serviceに実装されている
- [ ] **Query Service**: 読み込み操作がQuery Serviceに実装されている
- [ ] **Command/Queryオブジェクト**: 不変の値オブジェクトとして定義されている
- [ ] **コントローラー分離**: CommandControllerとQueryControllerが分離されている
- [ ] REST APIの命名規則が一貫している
- [ ] HTTPメソッドが適切に使用されている
- [ ] ステータスコードが適切に返されている
- [ ] エラーレスポンスの形式が一貫している
- [ ] バリデーションが適切に実装されている
- [ ] 認証・認可が適切に設計されている
- [ ] OpenAPIドキュメントが最新である
- [ ] API Gatewayのルーティング設定が正しい
- [ ] ページネーション・ソートが適切に実装されている
- [ ] セキュリティヘッダーが設定されている
- [ ] レートリミッティングが適用されている
- [ ] リクエスト・レスポンスのログが適切に記録されている
- [ ] APIのバージョン管理戦略が明確である
- [ ] CORS設定が適切である
- [ ] ヘルスチェックエンドポイントが実装されている
