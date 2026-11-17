# authorization ディレクトリ - 環境構築方法

## 前提条件

以下のソフトウェアがインストールされていることを確認してください。

- **Java 25** (OpenJDK推奨)
- **Docker** (統合テスト用)
- **PostgreSQL** (ローカル開発用、任意。Testcontainersで代替可能)

## 環境変数の設定

`.env`ファイルを作成し、以下の変数を設定します。

```bash
# データベース接続情報
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/authorization
SPRING_DATASOURCE_USERNAME=postgres
SPRING_DATASOURCE_PASSWORD=postgres

# Eurekaサーバーのアドレス
EUREKA_CLIENT_SERVICE_URL_DEFAULTZONE=http://localhost:8761/eureka/

# JWTシークレット（本番環境では強いランダム文字列を使用）
JWT_SECRET=your-secret-key-here
JWT_EXPIRATION=3600
```

## データベースの初期化

1. PostgreSQLにデータベースを作成します。
   ```sql
   CREATE DATABASE authorization;
   ```

2. Flywayが自動的にマイグレーションを適用します。

## アプリケーションの起動

1. プロジェクトルートで以下を実行します。
   ```bash
   ./gradlew :authorization:bootRun
   ```

2. アプリケーションは `http://localhost:8081` で起動します。

## テストの実行

- 単体テスト: `./gradlew :authorization:test`
- 統合テスト: `./gradlew :authorization:mediumTest` (Dockerが必要)

## トラブルシューティング

- **Dockerが起動していない場合**: 統合テストが失敗します。Docker Desktopを起動してください。
- **ポート競合の場合**: `application.yml`でポートを変更してください。
- **データベース接続エラーの場合**: 接続情報とPostgreSQLの起動状態を確認してください。
- **JWTエラーの場合**: シークレットキーが設定されていることを確認してください。
