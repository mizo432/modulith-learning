# service-registry ディレクトリ - 環境構築方法

## 前提条件

以下のソフトウェアがインストールされていることを確認してください。

- **Java 25** (OpenJDK推奨)

## 環境変数の設定

`.env`ファイルを作成し、以下の変数を設定します。

```bash
# Eurekaサーバーのポート
SERVER_PORT=8761
```

## アプリケーションの起動

1. プロジェクトルートで以下を実行します。
   ```bash
   ./gradlew :service-registry:bootRun
   ```

2. サービスレジストリは `http://localhost:8761` で起動します。

3. Eurekaダッシュボードは `http://localhost:8761` でアクセスできます。

## テストの実行

- 単体テスト: `./gradlew :service-registry:test`
- 統合テスト: `./gradlew :service-registry:mediumTest`

## トラブルシューティング

- **ポート競合の場合**: `application.yml`でポートを変更してください。
- **サービスが登録されない場合**: クライアント側のEureka設定を確認してください。
- **起動に時間がかかる場合**: 初回起動時はJARのダウンロードに時間がかかることがあります。
