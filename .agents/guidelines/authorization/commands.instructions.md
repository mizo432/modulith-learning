# authorization ディレクトリ - 使用コマンド使用方法

## ビルド・起動コマンド

| 操作                         | コマンド                                              |
|:-----------------------------|:------------------------------------------------------|
| 認証サービスの起動           | `./gradlew :authorization:bootRun`                    |
| 認証サービスのビルド         | `./gradlew :authorization:build`                      |
| 認証サービスのクリーンビルド | `./gradlew :authorization:clean :authorization:build` |

## テストコマンド

| 操作                     | コマンド                                                  |
|:-------------------------|:----------------------------------------------------------|
| 単体テストの実行         | `./gradlew :authorization:test`                           |
| 統合テストの実行         | `./gradlew :authorization:mediumTest`                     |
| 特定のテストクラスの実行 | `./gradlew :authorization:test --tests "*.TestClassName"` |

## リポート生成

| 操作               | コマンド                        | 出力先                               |
|:-------------------|:--------------------------------|:-------------------------------------|
| カバレッジレポート | `./gradlew :authorization:test` | `authorization/build/reports/jacoco` |

## 依存関係管理

| 操作                     | コマンド                                     |
|:-------------------------|:---------------------------------------------|
| 依存ライブラリの更新確認 | `./gradlew :authorization:dependencyUpdates` |
| 依存関係ツリーの表示     | `./gradlew :authorization:dependencies`      |

## Flywayマイグレーション

| 操作                             | コマンド                                 |
|:---------------------------------|:-----------------------------------------|
| マイグレーションの実行           | `./gradlew :authorization:flywayMigrate` |
| マイグレーションのステータス確認 | `./gradlew :authorization:flywayInfo`    |

## デバッグ用コマンド

| 操作                       | コマンド                                       |
|:---------------------------|:-----------------------------------------------|
| デバッグモードで起動       | `./gradlew :authorization:bootRun --debug-jvm` |
| アプリケーションのログ確認 | `authorization/build/logs/` を確認             |

## 認証エンドポイント

| 操作             | エンドポイント           |
|:-----------------|:-------------------------|
| ログイン         | `POST /api/auth/login`   |
| トークンの再発行 | `POST /api/auth/refresh` |
| ログアウト       | `POST /api/auth/logout`  |

## 注意事項

- 統合テスト実行時にはDockerが起動している必要があります。
- JWTシークレットは本番環境では強いランダム文字列を使用してください。
- セキュリティ設定を変更した場合は、必ずテストを実行してください。
