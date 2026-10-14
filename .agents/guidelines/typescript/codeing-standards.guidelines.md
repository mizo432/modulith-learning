# TypeScript コーディングルールガイドライン

本ガイドラインは、modulith-learning プロジェクトにおける TypeScript コードの記述規則を定義します。

## 1. 技術スタック

- React 18 / TypeScript 4.9 / Create React App (`react-scripts`)
- MUI v5 (`@mui/material`, `@mui/icons-material`) + Emotion
- React Router v6 / axios
- テーマは `frontend/src/theme.ts` に集約し、環境別の色は `.env.*` (`REACT_APP_*`) で上書きする

## 2. コードスタイル

- TypeScriptの厳格モードを有効にし、型安全性を最大化します。
- `any`型の使用を避け、適切な型定義を使用します。
- インターフェースと型エイリアスを適切に使い分けます。
- 関数型コンポーネントとフックを積極的に使用します。

## 3. 命名規則

- コンポーネント名: PascalCase（例: `CustomerList`, `OrderForm`）
- 関数名・変数名: camelCase（例: `fetchCustomers`, `orderList`）
- 定数: UPPER_SNAKE_CASE（例: `MAX_RETRY_COUNT`, `API_BASE_URL`）
- ファイル名: コンポーネントファイルは PascalCase、その他は camelCase

## 4. ディレクトリ構造

- 機能ごとにディレクトリを分離し、関連ファイルをまとめて配置します。
- コンポーネントは `src/components` 配下に配置します。
- ページコンポーネントは `src/pages` 配下に配置します。
- 型定義は `src/types` 配下に集約します。
- ユーティリティ関数は `src/utils` 配下に配置します。

## 5. コンポーネント設計

- 単一責任の原則に従い、小さなコンポーネントに分割します。
- Propsの型定義を明確にし、デフォルト値を設定します。
- 副作用は `useEffect` で管理し、クリーンアップ関数を適切に実装します。
- カスタムフックを抽出して、ロジックの再利用性を高めます。

## 6. テスト規約

- フロントエンドのテストは **Storybook** で記述します。
- コンポーネントと同じディレクトリに `*.stories.tsx` を置き、loading / error / empty / success
  の各状態をストーリーにします。
- 操作の検証は `play` 関数で行います。
- Jest単体の `*.test.tsx` を新規に作る前に、Storybookで書けるか検討してください。

## 7. ステート管理

- ローカルステートは `useState` で管理します。
- 複雑なステートロジックは `useReducer` で管理します。
- グローバルステートが必要な場合は、Context APIまたは状態管理ライブラリを使用します。

## 8. API通信

- axiosをラップしたカスタムフックを使用して、API通信を一元管理します。
- エラー処理は一貫した方法で行い、ユーザーに適切なフィードバックを提供します。
- ローディング状態を適切に管理し、ユーザー体験を向上させます。
