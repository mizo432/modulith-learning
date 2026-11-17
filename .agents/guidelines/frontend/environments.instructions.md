# frontend ディレクトリ - 環境構築方法

## 前提条件

以下のソフトウェアがインストールされていることを確認してください。

- **Node.js 18以上**
- **npm 9以上**

## 依存関係のインストール

1. frontendディレクトリに移動します。
   ```bash
   cd frontend
   ```

2. 依存関係をインストールします。
   ```bash
   npm install
   ```

## 環境変数の設定

`.env.local`ファイルを作成し、以下の変数を設定します。

```bash
# APIサーバーのアドレス
REACT_APP_API_URL=http://localhost:8080

# テーマカラー（任意）
REACT_APP_PRIMARY_COLOR=#1976d2
```

## アプリケーションの起動

1. 開発サーバーを起動します。
   ```bash
   npm run start:local
   ```

2. アプリケーションは `http://localhost:3000` で起動します。

## Storybookの起動

Storybookを導入している場合、以下で起動できます。

```bash
npm run storybook
```

## ビルド

本番用ビルドを実行します。

```bash
npm run build
```

ビルド成果物は `build/` ディレクトリに出力されます。

## テストの実行

```bash
npm test
```

## トラブルシューティング

- **ポート競合の場合**: `.env`ファイルで `PORT` 環境変数を変更してください。
- **依存関係エラーの場合**: `node_modules` と `package-lock.json` を削除し、再度 `npm install`
  してください。
- **ビルドエラーの場合**: TypeScriptの型エラーを確認し、修正してください。
