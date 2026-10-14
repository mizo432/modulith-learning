# authorization ディレクトリ - 開発者向け内容

## 技術スタック

### 言語・ランタイム

- **Java 25** (OpenJDK - Gradle Toolchain)

### コアフレームワーク

- Spring Boot 4.1.x
- Spring Security
- Spring Cloud 2025.x (Eureka Client)

### 認証・認可

- OAuth 2.0 / OpenID Connect
- JWT (JSON Web Tokens)
- Spring Security OAuth2 Resource Server

### データベース

- PostgreSQL (ユーザー情報、ロール情報の保存)
- Flyway (DBマイグレーション)

### テストフレームワーク

- JUnit 5 (JUnit Jupiter)
- AssertJ
- Spring Security Test
- Testcontainers (PostgreSQL)

## 開発環境の要件

- Java 25 (OpenJDK)
- Docker (統合テスト用)
- PostgreSQL (ローカル開発用、任意)

## IDE設定

- IntelliJ IDEA推奨
- Lombokプラグインのインストールが必要
- Gradleの自動インポートを有効にする

## 設定ファイル

- `application.yml`: メイン設定
- `application-local.yml`: ローカル開発用設定
- `application-test.yml`: テスト用設定

## セキュリティ設定

Spring Securityの設定は `SecurityConfig` クラスで定義します。

- 認証エンドポイントの許可
- JWTフィルターの設定
- ロールベースのアクセス制御
