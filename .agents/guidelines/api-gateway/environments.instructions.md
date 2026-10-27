# api-gateway ディレクトリ - 環境構築方法

## 前提条件

以下のソフトウェアがインストールされていることを確認してください。

- **Java 25** (OpenJDK推奨)

## 環境変数の設定

`.env`ファイルを作成し、以下の変数を設定します。

```bash
# Eurekaサーバーのアドレス
EUREKA_CLIENT_SERVICE_URL_DEFAULTZONE=http://localhost:8761/eureka/

# ゲートウェイのポート
SERVER_PORT=8080
```

## アプリケーションの起動

1. プロジェクトルートで以下を実行します。
   ```bash
   ./gradlew :api-gateway:bootRun
   ```

2. APIゲートウェイは `http://localhost:8080` で起動します。

## サービスレジストリの起動

APIゲートウェイはサービスレジストリ (Eureka)に依存しています。事前に起動してください。

```bash
./gradlew :service-registry:bootRun
```

## テストの実行

- 単体テスト: `./gradlew :api-gateway:test`
- 統合テスト: `./gradlew :api-gateway:mediumTest`

## トラブルシューティング

- **サービスレジストリ接続エラーの場合**: Eurekaサーバーが起動していることを確認してください。
- **ポート競合の場合**: `application.yml`でポートを変更してください。
- **ルーティングエラーの場合**: ルーティング設定とサービス名を確認してください。
