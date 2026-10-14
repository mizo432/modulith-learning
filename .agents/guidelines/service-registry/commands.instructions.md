# service-registry ディレクトリ - 使用コマンド使用方法

## ビルド・起動コマンド

| 操作                               | コマンド                                                    |
|:-----------------------------------|:------------------------------------------------------------|
| サービスレジストリの起動           | `./gradlew :service-registry:bootRun`                       |
| サービスレジストリのビルド         | `./gradlew :service-registry:build`                         |
| サービスレジストリのクリーンビルド | `./gradlew :service-registry:clean :service-registry:build` |

## テストコマンド

| 操作                     | コマンド                                                     |
|:-------------------------|:-------------------------------------------------------------|
| 単体テストの実行         | `./gradlew :service-registry:test`                           |
| 統合テストの実行         | `./gradlew :service-registry:mediumTest`                     |
| 特定のテストクラスの実行 | `./gradlew :service-registry:test --tests "*.TestClassName"` |

## 依存関係管理

| 操作                     | コマンド                                        |
|:-------------------------|:------------------------------------------------|
| 依存ライブラリの更新確認 | `./gradlew :service-registry:dependencyUpdates` |
| 依存関係ツリーの表示     | `./gradlew :service-registry:dependencies`      |

## デバッグ用コマンド

| 操作                       | コマンド                                          |
|:---------------------------|:--------------------------------------------------|
| デバッグモードで起動       | `./gradlew :service-registry:bootRun --debug-jvm` |
| アプリケーションのログ確認 | `service-registry/build/logs/` を確認             |

## Eurekaダッシュボード

| 操作                     | URL                      |
|:-------------------------|:-------------------------|
| ダッシュボードのアクセス | `http://localhost:8761`  |
| サービス一覧の確認       | ダッシュボード上から確認 |

## 全サービス起動

すべてのサービスを起動する場合、以下の順序で実行してください。

1. サービスレジストリ: `./gradlew :service-registry:bootRun`
2. バックエンド: `./gradlew :backend:bootRun`
3. 認証サービス: `./gradlew :authorization:bootRun`
4. APIゲートウェイ: `./gradlew :api-gateway:bootRun`

## 注意事項

- サービスレジストリは最初に起動してください。他のサービスはこれに依存しています。
- 設定を変更した場合は、アプリケーションを再起動してください。
- テスト実行時には、特別な依存サービスは不要です。
