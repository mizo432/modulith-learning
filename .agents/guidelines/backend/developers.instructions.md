# backend ディレクトリ - 開発者向け内容

## 技術スタック

### 言語・ランタイム

- **Java 25** (OpenJDK - Gradle Toolchain)

### コアフレームワーク

- Spring Boot 4.1.x
- Spring Modulith 2.1.x
- Spring Data JPA
- Spring Cloud 2025.x (Eureka Client)

### データベース

- **PostgreSQL** (本番・開発)
- **Testcontainers + PostgreSQL 17** (統合テスト用、Dockerが必要)
- **Flyway** (DBマイグレーション、モジュールごとにDBスキーマを分離)

### バッチ処理

- Spring Batch (マスタデータのインポート等)
- Super CSV (CSV読み込み)

### 主要ライブラリ

- **Lombok**: ボイラープレートコード削減
- **JSpecify**: Null安全性アノテーション
- **jMolecules**: オニオンアーキテクチャ・DDD構造定義
- **YAVI**: 型安全なバリデーションライブラリ
- **Google Guava / ICU4J / libphonenumber / Apache POI**: 共通ユーティリティ
- **Caffeine**: インメモリキャッシュ
- **Jilt**: ビルダー生成
- **springdoc-openapi**: OpenAPI / Swagger UI
- **Spring Modulith Actuator / Observability / Insight**: モジュール構造・イベントの可視化

### テストフレームワーク

- JUnit 6 (JUnit Jupiter)
- AssertJ, AssertJ-DB
- Spring Modulith Test
- Spring Batch Test
- Testcontainers (PostgreSQL)

### カバレッジ

- JaCoCo (`./gradlew :backend:test` 実行後に `backend/build/reports/jacoco` に出力)

## 開発環境の要件

- Java 25 (OpenJDK)
- Docker (統合テスト用)
- PostgreSQL (ローカル開発用、任意)

## IDE設定

- IntelliJ IDEA推奨
- Lombokプラグインのインストールが必要
- Gradleの自動インポートを有効にする
