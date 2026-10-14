# frontend ディレクトリ - 使用コマンド使用方法

## 開発サーバー

| 操作                          | コマンド              |
|:------------------------------|:----------------------|
| 開発サーバーの起動 (ローカル) | `npm run start:local` |
| 開発サーバーの起動 (本番API)  | `npm run start`       |

## ビルド

| 操作         | コマンド            |
|:-------------|:--------------------|
| 本番用ビルド | `npm run build`     |
| 開発用ビルド | `npm run build:dev` |

## テスト

| 操作              | コマンド                  |
|:------------------|:--------------------------|
| テストの実行      | `npm test`                |
| Storybookの起動   | `npm run storybook`       |
| Storybookのビルド | `npm run build-storybook` |

## コード品質

| 操作                   | コマンド                    |
|:-----------------------|:----------------------------|
| ESLintの実行           | `npm run lint`              |
| Prettierの実行         | `npx prettier --write src/` |
| TypeScriptの型チェック | `npx tsc --noEmit`          |

## 依存関係管理

| 操作                   | コマンド                       |
|:-----------------------|:-------------------------------|
| 依存関係のインストール | `npm install`                  |
| 依存関係の更新確認     | `npm outdated`                 |
| 依存関係の更新         | `npm update`                   |
| パッケージの追加       | `npm install <package-name>`   |
| パッケージの削除       | `npm uninstall <package-name>` |

## クリーンアップ

| 操作               | コマンド                                               |
|:-------------------|:-------------------------------------------------------|
| node_modulesの削除 | `rm -rf node_modules`                                  |
| キャッシュのクリア | `npm cache clean --force`                              |
| 再インストール     | `rm -rf node_modules package-lock.json && npm install` |

## 注意事項

- 開発サーバー起動時には、バックエンドAPIが起動していることを確認してください。
- 本番ビルド前にテストを実行し、問題がないことを確認してください。
- 依存関係を変更後は、必ずテストを実行してください。
