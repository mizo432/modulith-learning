# フロントエンドモジュール

これは Modulith Learning プロジェクトのフロントエンドモジュールです。Material-UI (MUI)
をコンポーネントライブラリとして使用する React ベースのシングルページアプリケーション (SPA) です。

## 開発コマンド一覧

### npm コマンド

| コマンド                       | 説明                 |
|----------------------------|--------------------|
| `npm install`              | 依存関係のインストール        |
| `npm start`                | デフォルト環境での開発サーバーの起動 |
| `npm run start:local`      | ローカル環境での開発サーバーの起動  |
| `npm run start:dev`        | 開発環境での開発サーバーの起動    |
| `npm run build`            | デフォルト環境用ビルド        |
| `npm run build:staging`    | ステージング環境用ビルド       |
| `npm run build:production` | 本番環境用ビルド           |
| `npm test`                 | テストの実行             |

### Gradle コマンド

| コマンド                                  | 説明                 |
|---------------------------------------|--------------------|
| `./gradlew :frontend:npmInstall`      | 依存関係のインストール        |
| `./gradlew :frontend:npmStart`        | デフォルト環境での開発サーバーの起動 |
| `./gradlew :frontend:npmStartLocal`   | ローカル環境での開発サーバーの起動  |
| `./gradlew :frontend:npmStartDev`     | 開発環境での開発サーバーの起動    |
| `./gradlew :frontend:npmBuild`        | 本番環境用ビルド           |
| `./gradlew :frontend:npmBuildStaging` | ステージング環境用ビルド       |
| `./gradlew :frontend:npmTest`         | テストの実行             |
| `./gradlew :frontend:clean`           | ビルド成果物のクリーン        |

## 技術スタック

- **React 18**: ユーザーインターフェースを構築するための JavaScript ライブラリ
- **TypeScript**: 型安全性と優れた開発者体験のため
- **Material-UI (MUI)**: Google の Material Design を実装した React コンポーネントライブラリ
- **React Router**: ナビゲーションとルーティング用
- **Axios**: バックエンドとの API 通信用

## プロジェクト構造

```
frontend/
├── public/                 # 静的ファイル
│   ├── index.html          # HTML テンプレート
│   └── manifest.json       # PWA マニフェスト
├── src/                    # ソースコード
│   ├── components/         # 再利用可能な UI コンポーネント
│   │   └── Header.tsx      # アプリケーションヘッダー
│   ├── pages/              # ページコンポーネント
│   │   ├── Dashboard.tsx   # ダッシュボードページ
│   │   └── NotFound.tsx    # 404 ページ
│   ├── services/           # API 通信用サービス
│   │   └── api.ts          # Axios 設定
│   ├── App.tsx             # メインアプリケーションコンポーネント
│   ├── index.tsx           # アプリケーションエントリーポイント
│   ├── index.css           # グローバルスタイル
│   ├── theme.ts            # MUI テーマ設定
│   └── reportWebVitals.ts  # パフォーマンスモニタリング
├── package.json            # NPM 依存関係とスクリプト
├── tsconfig.json           # TypeScript 設定
└── build.gradle.kts        # Gradle ビルド設定
```

## 開発

### 前提条件

- Node.js (v20.12.2 以降)
- npm (v10.5.0 以降)

### 環境設定

フロントエンドプロジェクトは以下の実行環境をサポートしています：

- **ローカル環境**: ローカル開発用
- **開発環境**: 開発サーバー用
- **ステージング環境**: テスト・検証用
- **本番環境**: 実運用用

各環境の設定は対応する `.env` ファイルで管理されています：

- `.env`: デフォルト設定（すべての環境で使用される基本設定）
- `.env.local`: ローカル環境の設定
- `.env.development`: 開発環境の設定
- `.env.staging`: ステージング環境の設定
- `.env.production`: 本番環境の設定

各環境には、以下のカラーテーマ変数が設定されています：

- `REACT_APP_PRIMARY_COLOR`: プライマリーカラー（ヘッダー、ボタンなど）
- `REACT_APP_SECONDARY_COLOR`: セカンダリーカラー（アクセント、強調表示など）
- `REACT_APP_BACKGROUND_COLOR`: 背景色

環境ごとのカラーテーマ：

- **デフォルト**: 青系テーマ
- **ローカル**: 緑系テーマ
- **開発**: 紫系テーマ
- **ステージング**: オレンジ系テーマ
- **本番**: インディゴ系テーマ

現在の環境はアプリケーションのヘッダーに表示されます（本番環境を除く）。これにより、開発者は現在どの環境で作業しているかを簡単に確認できます。環境表示は
`REACT_APP_ENV` 環境変数の値に基づいています。

### 利用可能なスクリプト

- **依存関係のインストール**: `npm install`
- **ローカル環境での開発サーバーの起動**: `npm run start:local`
- **開発環境での開発サーバーの起動**: `npm run start:dev`
- **デフォルト環境での開発サーバーの起動**: `npm start`
- **ステージング環境用ビルド**: `npm run build:staging`
- **本番環境用ビルド**: `npm run build:production`
- **デフォルト環境用ビルド**: `npm run build`
- **テストの実行**: `npm test`

### Gradle 統合

フロントエンドモジュールは node-gradle プラグインを使用して Gradle と統合されています。以下の Gradle
タスクを使用できます：

- **依存関係のインストール**: `./gradlew :frontend:npmInstall`
- **ローカル環境での開発サーバーの起動**: `./gradlew :frontend:npmStartLocal`
- **開発環境での開発サーバーの起動**: `./gradlew :frontend:npmStartDev`
- **デフォルト環境での開発サーバーの起動**: `./gradlew :frontend:npmStart`
- **ステージング環境用ビルド**: `./gradlew :frontend:npmBuildStaging`
- **本番環境用ビルド**: `./gradlew :frontend:npmBuild`
- **テストの実行**: `./gradlew :frontend:npmTest`
- **クリーン**: `./gradlew :frontend:clean`

## バックエンドとの通信

フロントエンドは Axios を使用してバックエンドと通信します。API サービスは `src/services/api.ts`
で設定され、以下を提供します：

- ベース URL 設定
- JWT トークンによる認証
- 一般的な HTTP ステータスコードのエラー処理

## デプロイメント

本番用にフロントエンドをビルドするには、次のコマンドを実行します：

```
./gradlew :frontend:npmBuild
```

これにより、`frontend/build` ディレクトリに本番用ビルドが作成され、任意の静的ファイルサーバーで提供できます。
