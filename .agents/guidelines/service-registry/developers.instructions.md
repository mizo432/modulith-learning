# service-registry ディレクトリ - 開発者向け内容

## 技術スタック

### 言語・ランタイム

- **Java 25** (OpenJDK - Gradle Toolchain)

### コアフレームワーク

- Spring Boot 4.1.x
- Spring Cloud 2025.x (Eureka Server)

### サービスディスカバリー

- Netflix Eureka Server
- ピアツーピアレプリケーション

### テストフレームワーク

- JUnit 6 (JUnit Jupiter)
- AssertJ
- Spring Boot Test

## 開発環境の要件

- Java 25 (OpenJDK)

## IDE設定

- IntelliJ IDEA推奨
- Lombokプラグインのインストールが必要
- Gradleの自動インポートを有効にする

## 設定ファイル

- `application.yml`: メイン設定
- `application-local.yml`: ローカル開発用設定
- `application-test.yml`: テスト用設定

## Eureka設定

Eurekaサーバーの設定は `application.yml` で定義します。

```yaml
server:
  port: 8761

eureka:
  client:
    register-with-eureka: false
    fetch-registry: false
  server:
    enable-self-preservation: false
```

## 注意事項

- サービスレジストリは他のサービスに依存しないため、最初に起動してください。
- 本番環境では高可用性構成を推奨します。
