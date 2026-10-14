# frontend ディレクトリ - プロジェクト概要

## 概要

frontendディレクトリは、modulith-learningプロジェクトのフロントエンドアプリケーションです。ReactとMaterial-UIを使用したSPA（Single
Page Application）として構築されています。

## アーキテクチャ

- **SPA (Single Page Application)**: React Routerによるクライアントサイドルーティング
- **コンポーネントベース設計**: 再利用可能なUIコンポーネントの構築
- **Material-UI**: 一貫したUIデザインの実現

## 主な機能

- バックエンドAPIとの通信
- 状態管理
- ルーティング
- レスポンシブデザイン

## 技術スタック

- React 18
- TypeScript 4.9
- Create React App (`react-scripts`)
- MUI v5 (`@mui/material`, `@mui/icons-material`) + Emotion
- React Router v6
- axios

## テスト戦略

- **Storybook**: コンポーネント単位のテストとドキュメント
- **インタラクションテスト**: `play`関数によるユーザー操作の検証
