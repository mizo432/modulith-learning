# backend ディレクトリ - 環境構築方法

## 前提条件

以下のソフトウェアがインストールされていることを確認してください。

- **Java 25** (OpenJDK推奨)
- **Docker** (統合テスト用)
- **PostgreSQL** (ローカル開発用、任意。Testcontainersで代替可能)

## 環境変数の設定

`.env`ファイルを作成し、以下の変数を設定します。

```bash
# データベース接続情報
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/modulith_learning
SPRING_DATASOURCE_USERNAME=postgres
SPRING_DATASOURCE_PASSWORD=postgres

# Eurekaサーバーのアドレス
EUREKA_CLIENT_SERVICE_URL_DEFAULTZONE=http://localhost:8761/eureka/
```

## データベースの初期化

1. PostgreSQLにデータベースを作成します。
   ```sql
   CREATE DATABASE modulith_learning;
   ```

2. Flywayが自動的にマイグレーションを適用します。

## アプリケーションの起動

1. プロジェクトルートで以下を実行します。
   ```bash
   ./gradlew :backend:bootRun
   ```

2. アプリケーションは `http://localhost:8080` で起動します。

3. Swagger UIは `http://localhost:8080/swagger-ui.html` でアクセスできます。

## テストの実行

- 単体テスト: `./gradlew :backend:test`
- 統合テスト: `./gradlew :backend:mediumTest` (Dockerが必要)
- システムテスト: `./gradlew :backend:largeTest`

## トラブルシューティング

- **Dockerが起動していない場合**: 統合テストが失敗します。Docker Desktopを起動してください。
- **ポート競合の場合**: `application.yml`でポートを変更してください。
- **データベース接続エラーの場合**: 接続情報とPostgreSQLの起動状態を確認してください。
