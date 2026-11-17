# backend ディレクトリ - 使用コマンド使用方法

## ビルド・起動コマンド

| 操作                         | コマンド                                  |
|:-----------------------------|:------------------------------------------|
| プロジェクト全体のビルド     | `./gradlew build`                         |
| バックエンドの起動           | `./gradlew :backend:bootRun`              |
| バックエンドのクリーンビルド | `./gradlew :backend:clean :backend:build` |

## テストコマンド

| 操作                          | コマンド                                                      |
|:------------------------------|:--------------------------------------------------------------|
| 単体テスト (Small) の実行     | `./gradlew :backend:test`                                     |
| 統合テスト (Medium) の実行    | `./gradlew :backend:mediumTest`                               |
| システムテスト (Large) の実行 | `./gradlew :backend:largeTest`                                |
| 特定のテストクラスの実行      | `./gradlew :backend:test --tests "undecided.*.TestClassName"` |

## リポート生成

| 操作                   | コマンド                  | 出力先                               |
|:-----------------------|:--------------------------|:-------------------------------------|
| モジュリス構造図の生成 | `./gradlew :backend:test` | `backend/build/spring-modulith-docs` |
| カバレッジレポート     | `./gradlew :backend:test` | `backend/build/reports/jacoco`       |

## 依存関係管理

| 操作                     | コマンド                               |
|:-------------------------|:---------------------------------------|
| 依存ライブラリの更新確認 | `./gradlew :backend:dependencyUpdates` |
| 依存関係ツリーの表示     | `./gradlew :backend:dependencies`      |

## Flywayマイグレーション

| 操作                             | コマンド                           |
|:---------------------------------|:-----------------------------------|
| マイグレーションの実行           | `./gradlew :backend:flywayMigrate` |
| マイグレーションのステータス確認 | `./gradlew :backend:flywayInfo`    |
| マイグレーションのロールバック   | `./gradlew :backend:flywayClean`   |

## バッチ処理

| 操作                       | コマンド                                                    |
|:---------------------------|:------------------------------------------------------------|
| 住所インポートバッチの実行 | アプリ起動後、`/api/batch/address/import` にPOSTリクエスト  |
| 祝日インポートバッチの実行 | アプリ起動後、`/api/batch/calendar/import` にPOSTリクエスト |

## デバッグ用コマンド

| 操作                       | コマンド                                 |
|:---------------------------|:-----------------------------------------|
| デバッグモードで起動       | `./gradlew :backend:bootRun --debug-jvm` |
| アプリケーションのログ確認 | `backend/build/logs/` を確認             |

## 注意事項

- 統合テスト実行時にはDockerが起動している必要があります。
- テスト実行前に `./gradlew :backend:cleanTest` で過去のテスト結果をクリアすることを推奨します。
- マイグレーションファイルは変更せず、新しいファイルを追加してください。
