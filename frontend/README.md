# フロントエンドモジュール

これは Modulith Learning プロジェクトのフロントエンドモジュールです。Material-UI (MUI)
をコンポーネントライブラリとして使用する React ベースのシングルページアプリケーション (SPA) です。

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

### 利用可能なスクリプト

- **依存関係のインストール**: `npm install`
- **開発サーバーの起動**: `npm start`
- **本番用ビルド**: `npm run build`
- **テストの実行**: `npm test`

### Gradle 統合

フロントエンドモジュールは node-gradle プラグインを使用して Gradle と統合されています。以下の Gradle
タスクを使用できます：

- **依存関係のインストール**: `./gradlew :frontend:npmInstall`
- **開発サーバーの起動**: `./gradlew :frontend:npmStart`
- **本番用ビルド**: `./gradlew :frontend:npmBuild`
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
