# AG-04 スプリント実行 フロントエンド設計

## 1. 概要

本ドキュメントは、AG-04「スプリント実行とインクリメント形成」業務領域のフロントエンド設計を定義します。

スプリント実行は、チームがスプリント目標を共有し、作業計画を日々適応させながら、完成の定義を満たすインクリメントを形成する機能です。

## 2. 技術スタック

| カテゴリ               | テクノロジー                                          |
|:-----------------------|:------------------------------------------------------|
| **フレームワーク**     | React 18                                              |
| **言語**               | TypeScript 4.9（strictモード有効）                    |
| **UIコンポーネント**   | MUI v5（@mui/material, @mui/icons-material）+ Emotion |
| **ルーティング**       | React Router v6                                       |
| **HTTPクライアント**   | axios                                                 |
| **ビルドツール**       | Create React App (react-scripts 5.0.1)                |
| **テスト**             | Jest / React Testing Library                          |
| **ドラッグ＆ドロップ** | @hello-pangea/dnd (react-beautiful-dndの後継)         |

## 3. 設計ドキュメント一覧

| No. | 設計ドキュメント | 説明 |
|-----|-----------------|------|
| 01 | [型定義設計](01.型定義/型定義.md) | TypeScriptインターフェース・型エイリアスの設計 |
| 02 | [コンポーネント設計](02.コンポーネント/コンポーネント一覧.md) | 4画面分のコンポーネント構成 |
| 03 | [共有コンポーネント設計](07.共有コンポーネント/共有コンポーネント一覧.md) | 複数画面で再利用されるUI部品 |
| 04 | [API連携設計](03.API連携/API連携設計.md) | APIサービス層とエンドポイント設計 |
| 05 | [ステート管理設計](04.ステート管理/ステート管理設計.md) | ローカル・グローバルステートの設計 |
| 06 | [ルーティング設計](05.ルーティング/ルーティング設計.md) | React Router v6によるルート設計 |
| 07 | [カスタムフック設計](06.カスタムフック/カスタムフック一覧.md) | 再利用可能なロジックフック |

## 4. 画面構成

AG-04スプリント実行は以下の4つの画面で構成されます。

| 画面ID | 画面名 | ページコンポーネント | 主な利用者 | 目的 |
|--------|--------|---------------------|------------|------|
| SCR-013 | Sprint一覧・詳細 | `SprintListPage` | 全チーム | Sprint期間、目標、選択Story、状態を確認 |
| SCR-014 | Sprint Planning | `SprintPlanningPage` | PO、SM、開発者 | Sprint Goal、バックログ選択、容量・ベロシティ、タスク分解を協働で決める |
| SCR-015 | Sprint Board / タスクボード | `SprintBoardPage` | 開発者、SM、PO | Story・Taskの状態、作業中項目、ブロッカーを更新・確認 |
| SCR-016 | Daily Scrum記録 | `DailyScrumPage` | 開発者、SM | Sprint Goalに向けた状況、障害、計画調整を記録 |

## 5. アーキテクチャ

### 5.1 ディレクトリ構成

```
frontend/src/
├── pages/
│   └── sprint/
│       ├── SprintListPage.tsx           # SCR-013 Sprint一覧・詳細
│       ├── SprintPlanningPage.tsx       # SCR-014 Sprint Planning
│       ├── SprintBoardPage.tsx          # SCR-015 Sprint Board
│       ├── DailyScrumPage.tsx           # SCR-016 Daily Scrum記録
│       └── components/                  # sprintドメイン固有のコンポーネント
│           ├── SprintList/
│           │   ├── SprintList.tsx
│           │   ├── SprintListItem.tsx
│           │   ├── SprintDetail.tsx
│           │   ├── SprintFilter.tsx
│           │   └── SprintStats.tsx
│           ├── SprintPlanning/
│           │   ├── SprintPlanningForm.tsx
│           │   ├── SprintGoalEditor.tsx
│           │   ├── BacklogSelector.tsx
│           │   ├── TaskBreakdown.tsx
│           │   └── VelocityPanel.tsx
│           ├── SprintBoard/
│           │   ├── KanbanBoard.tsx
│           │   ├── KanbanColumn.tsx
│           │   ├── TaskCard.tsx
│           │   ├── TaskDetailDrawer.tsx
│           │   ├── ImpedimentDialog.tsx
│           │   ├── DependencyPanel.tsx
│           │   └── BuildStatusPanel.tsx
│           └── DailyScrum/
│               ├── DailyScrumForm.tsx
│               ├── DailyScrumList.tsx
│               ├── DailyScrumItem.tsx
│               └── ScrumGoalSidebar.tsx
├── components/                          # 共有コンポーネント
│   ├── StatusBadge.tsx
│   ├── AssigneeChip.tsx
│   ├── ProgressBar.tsx
│   ├── ImpedimentBadge.tsx
│   └── NotificationBadge.tsx
├── services/
│   ├── sprintApi.ts                     # スプリント関連API
│   ├── taskApi.ts                       # タスク関連API
│   ├── impedimentApi.ts                 # 障害関連API
│   ├── dependencyApi.ts                 # 依存関係関連API
│   ├── notificationApi.ts               # 通知関連API
│   ├── activityApi.ts                   # アクティビティ関連API
│   └── dailyScrumApi.ts                 # デイリースクラム関連API
├── hooks/
│   ├── useSprint.ts
│   ├── useSprintList.ts
│   ├── useTasks.ts
│   ├── useImpediments.ts
│   ├── useDependencies.ts
│   ├── useNotifications.ts
│   ├── useActivities.ts
│   ├── useDailyScrum.ts
│   └── useDragAndDrop.ts
├── contexts/
│   └── SprintContext.tsx                # 現在選択中のスプリント情報
├── types/
│   ├── sprint.ts
│   ├── task.ts
│   ├── impediment.ts
│   ├── dependency.ts
│   ├── notification.ts
│   ├── activity.ts
│   └── dailyScrum.ts
└── utils/
    └── sprintUtils.ts                   # スプリント関連のユーティリティ
```

### 5.2 コンポーネント階層

```
App
└── Routes
    └── SprintRoutes
        ├── SprintListPage (SCR-013)
        │   ├── SprintFilter
        │   ├── SprintList
        │   │   └── SprintListItem × N
        │   ├── SprintDetail (Drawer)
        │   └── SprintStats
        ├── SprintPlanningPage (SCR-014)
        │   ├── SprintPlanningForm
        │   ├── TabNavigation
        │   ├── SprintGoalEditor
        │   ├── BacklogSelector
        │   ├── TaskBreakdown
        │   └── VelocityPanel
        ├── SprintBoardPage (SCR-015)
        │   ├── KanbanBoard
        │   │   └── KanbanColumn × 4
        │   │       └── TaskCard × N
        │   ├── TaskDetailDrawer
        │   ├── ImpedimentDialog
        │   ├── DependencyPanel
        │   └── BuildStatusPanel
        └── DailyScrumPage (SCR-016)
            ├── DailyScrumForm
            ├── DailyScrumList
            │   └── DailyScrumItem × N
            └── ScrumGoalSidebar
```

## 6. ステート管理戦略

| スコープ | 手法 | 管理対象 |
|----------|------|----------|
| **グローバル** | Context API | 現在選択中のスプリント、ユーザー情報 |
| **サーバー状態** | axios + カスタムフック | スプリント一覧、タスク一覧、障害一覧など |
| **ローカル** | useState | フォーム入力値、フィルター条件、UI状態 |
| **複雑なローカル** | useReducer | カンバンボードのドラッグ＆ドロップ状態 |

## 7. 権限設計

| 権限 | 役割 | 説明 |
|------|------|------|
| Sprint参照 | 全チームメンバー | Sprint一覧・詳細を参照 |
| Sprint編集 | PO、SM | Sprint情報の編集 |
| Sprint Board編集 | 開発者、SM | タスクの状態変更・担当変更 |
| 障害管理 | 開発者、SM | 障害の登録・更新 |
| 通知送信 | 開発者、SM、PO | メンションや担当変更の通知 |
| Daily Scrum編集 | 開発チーム | デイリースクラムの記録作成・編集 |

## 8. パフォーマンス考慮事項

- **コードスプリット**: 4つのページコンポーネントを `React.lazy` で動的インポート
- **リスト仮想化**: スプリント一覧・タスクカードリストで `react-window` を検討
- **メモ化**: `useMemo` / `useCallback` / `React.memo` を適切に適用
- **ドラッグ＆ドロップ**: @hello-pangea/dnd を使用し、最適化されたレンダリング

## 9. アクセシビリティ

- キーボード操作によるカンバンボードの操作サポート
- ARIA属性によるスクリーンリーダー対応
- フォーカス管理（Drawer・Dialogの開閉時）
- カラーコントラストのWCAG準拠

## 参照

- 要件定義: `docs/22 要件定義書/AG アジルーム/03 機能要件/AG-04 スプリント実行/`
- データベース設計: `docs/23 詳細設計書/AG_アジルーム/01 データベース設計/`
