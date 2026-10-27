# api-gateway ディレクトリ - 使用コマンド使用方法

## ビルド・起動コマンド

| 操作                            | コマンド                                          |
|:--------------------------------|:--------------------------------------------------|
| APIゲートウェイの起動           | `./gradlew :api-gateway:bootRun`                  |
| APIゲートウェイのビルド         | `./gradlew :api-gateway:build`                    |
| APIゲートウェイのクリーンビルド | `./gradlew :api-gateway:clean :api-gateway:build` |

## テストコマンド

| 操作                     | コマンド                                                |
|:-------------------------|:--------------------------------------------------------|
| 単体テストの実行         | `./gradlew :api-gateway:test`                           |
| 統合テストの実行         | `./gradlew :api-gateway:mediumTest`                     |
| 特定のテストクラスの実行 | `./gradlew :api-gateway:test --tests "*.TestClassName"` |

## 依存関係管理

| 操作                     | コマンド                                   |
|:-------------------------|:-------------------------------------------|
| 依存ライブラリの更新確認 | `./gradlew :api-gateway:dependencyUpdates` |
| 依存関係ツリーの表示     | `./gradlew :api-gateway:dependencies`      |

## デバッグ用コマンド

| 操作                       | コマンド                                     |
|:---------------------------|:---------------------------------------------|
| デバッグモードで起動       | `./gradlew :api-gateway:bootRun --debug-jvm` |
| アプリケーションのログ確認 | `api-gateway/build/logs/` を確認             |

## 全サービス起動

すべてのサービスを起動する場合、以下の順序で実行してください。

1. サービスレジストリ: `./gradlew :service-registry:bootRun`
2. バックエンド: `./gradlew :backend:bootRun`
3. APIゲートウェイ: `./gradlew :api-gateway:bootRun`

## 注意事項

- APIゲートウェイはサービスレジストリに依存しているため、事前に起動してください。
- ルーティング設定を変更した場合は、アプリケーションを再起動してください。
- テスト実行時には、必要なサービスが起動していることを確認してください。
