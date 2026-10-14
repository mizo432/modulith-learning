# api-gateway ディレクトリ - 開発者向け内容

## 技術スタック

### 言語・ランタイム

- **Java 25** (OpenJDK - Gradle Toolchain)

### コアフレームワーク

- Spring Boot 4.1.x
- Spring Cloud Gateway
- Spring Cloud 2025.x (Eureka Client)

### ルーティング

- Spring Cloud Gatewayのリアクティブルーティング
- サービスディスカバリーによる動的ルーティング

### テストフレームワーク

- JUnit 5 (JUnit Jupiter)
- AssertJ
- WebTestClient (リアクティブWebテスト)

## 開発環境の要件

- Java 25 (OpenJDK)
- Docker (統合テスト用、任意)

## IDE設定

- IntelliJ IDEA推奨
- Lombokプラグインのインストールが必要
- Gradleの自動インポートを有効にする

## 設定ファイル

- `application.yml`: メイン設定
- `application-local.yml`: ローカル開発用設定
- `application-test.yml`: テスト用設定

## ルーティング設定

ルーティングルールは `application.yml` で定義します。

```yaml
spring:
  cloud:
    gateway:
      routes:
        - id: backend-service
          uri: lb://backend-service
          predicates:
            - Path=/api/**
```
