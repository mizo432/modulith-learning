# frontend ディレクトリ - 開発者向け内容

## 技術スタック

### 言語・フレームワーク

- **TypeScript 4.9**
- **React 18**
- **Create React App** (`react-scripts`)

### UIコンポーネント

- **MUI v5** (`@mui/material`, `@mui/icons-material`)
- **Emotion** (CSS-in-JS)

### ルーティング・通信

- **React Router v6**
- **axios** (HTTPクライアント)

### テーマ管理

- `frontend/src/theme.ts` に集約
- 環境別の色は `.env.*` (`REACT_APP_*`) で上書き

### テスト

- **Storybook**: コンポーネント単位のテストとドキュメント
- **`play`関数**: インタラクションテスト

## 開発環境の要件

- Node.js 18以上
- npm 9以上

## IDE設定

- VS Code推奨
- ESLintプラグインのインストールを推奨
- Prettierプラグインのインストールを推奨

## ディレクトリ構造

```
frontend/
├── src/
│   ├── components/    # 再利用可能なUIコンポーネント
│   ├── pages/         # ページコンポーネント
│   ├── types/         # TypeScript型定義
│   ├── utils/         # ユーティリティ関数
│   ├── theme.ts       # テーマ設定
│   └── App.tsx        # ルートコンポーネント
├── public/            # 静的アセット
└── package.json       # 依存関係定義
```
