---
name: frontend-design
description: frontend/（React + TypeScript + MUI v5 + CRA）の画面・コンポーネントを新規作成・改修するときの設計ガイド。UI を作る、画面を追加する、見た目を整える、デザインを改善する、コンポーネントを切り出す、といった依頼で使う。テーマトークン、レイアウト、文言、アクセシビリティ、Storybook によるテストまでを扱う。
---

# Frontend Design（modulith-learning）

`frontend/` はアジャイル開発管理（プロダクト / プロジェクト / エピック / スプリント / チーム /
ユーザー）の業務アプリ。
派手さより「毎日使って迷わない・速く読める」ことを優先する。個性は 1 か所に絞り、それ以外は静かに保つ。

## 0. 着手前に確認する

1. 既存の近い画面を読む（例: `src/pages/projects/ProjectList.tsx`, `ProjectDetail.tsx`,
   `ProjectEdit.tsx`）。一覧・詳細・作成・編集のパターンを踏襲する。
2. `src/theme.ts` のトークンを確認する。色・フォント・コンポーネント上書きはここに集約されている。
3. データは `src/services/*Service.ts` 経由で取得する。コンポーネントから `axios` を直接呼ばない。
4. 画面の「主な仕事」を 1 文で言えるようにする（例:「スプリントの残作業を把握して次の一手を決める」）。言えなければユーザーに確認する。

## 1. デザイントークン（theme.ts を唯一の源泉にする）

- 色は `theme.palette.*` を参照する。`sx` に生の hex / rgba を書かない。必要な色はまず `theme.ts`
  に追加する。
    - 環境ごとの上書き（`REACT_APP_PRIMARY_COLOR` など）を壊さないこと。
- 余白は MUI の spacing 単位（`sx={{ p: 2, mb: 3 }}`）で書く。px 直書きは避ける。
- タイポグラフィは `Typography` の `variant` を使う。ページ見出しは `variant="h4" component="h1"`
  、セクションは `h5`/`h6`。1 画面に `h1` は 1 つ。
- 新しいスタイル上書きが 2 画面以上で必要になったら、`theme.components` に寄せる。
- 着手時に小さなトークン計画を書く（使う色 4〜6 色、見出し階層、レイアウトのラフ）。ASCII
  ワイヤーフレームで十分:

```
┌ h1 Projects ───────────────── [+ New project] ┐
│ フィルタ: [Status ▼] [検索____]               │
├───────────────────────────────────────────────┤
│ Table: Name | Status(Chip) | Updated | Actions │
└───────────────────────────────────────────────┘
```

## 2. レイアウトと構造

- ページは `Container maxWidth="lg"` + `Box sx={{ my: 4 }}` を基本にする（既存画面と揃える）。
- 一覧は `TableContainer component={Paper}`、詳細は見出し + 定義リスト的な `Grid`、フォームは縦 1 カラムで
  `Stack spacing={2}`。
- 主要アクションは見出し行の右端に 1 つだけ `variant="contained"`。それ以外は `outlined` / `text` /
  `IconButton`。
- 状態（Active / Archived など）は `Chip` で表す。色は意味で固定する（例: active=success,
  archived=default）。画面ごとに変えない。
- 罫線・番号・区切りは意味があるときだけ使う。番号付きマーカーは本当に順序がある場合（スプリント順など）に限る。
- 同じ角丸・同じ影のカードを並べるだけの「SaaS テンプレ」にしない。情報の重要度で大きさと密度を変える。

## 3. 状態の網羅（必須）

データを扱うコンポーネントは次の 4 状態をすべて実装する:

| 状態    | 表現                                                                                                                       |
|---------|----------------------------------------------------------------------------------------------------------------------------|
| loading | `CircularProgress` または `Skeleton`。レイアウトが跳ねないよう領域を確保する                                               |
| error   | `Alert severity="error"`。何が起きたか + どうすれば直るかを書く（謝罪文は不要）                                            |
| empty   | 次の行動を促す文言 + 主要アクション（例:「まだプロジェクトがありません。最初のプロジェクトを作成しましょう」+ 作成ボタン） |
| success | 通常表示                                                                                                                   |

## 4. 文言（UI コピーもデザインの一部）

- ユーザー視点・能動態で書く。見出しやボタンは sentence case（`Create project`、`Create Project` にしない）。
- フローの中で動詞を揃える（「Archive」→ 確認ダイアログ「Archive this project?」→ 通知「Project
  archived」）。
- 不要なラベル、全部大文字のラベル、見出しの一語だけを強調する装飾は使わない。
- 既存画面の言語（現状は英語）に合わせる。混在させない。

## 5. アクセシビリティと品質の下限

- キーボードだけで全操作できること。フォーカスリングを消さない。
- `IconButton` には必ず `aria-label`。アイコンだけで意味を伝えない（`Tooltip` を併用）。
- 色だけで状態を区別しない（Chip にはテキストを入れる）。コントラスト比 4.5:1 以上。
- モーションは操作への反応に限る。`prefers-reduced-motion` を尊重する（MUI の transition はテーマで抑制できる）。
- `xs` 〜 `lg` でレイアウトが崩れないこと。テーブルは狭い幅で横スクロールさせる。

## 6. コンポーネント設計

- ページ（`src/pages/**`）はデータ取得と状態管理、表示は `src/components/**` の props 駆動コンポーネントに切り出す。
  こうすると Storybook でサービス層をモックせずに各状態を描画できる。
- props は表示に必要な最小限の型で受ける（`Project` 全体ではなく必要なフィールドでもよい）。イベントは
  `onXxx` コールバックで上に返す。
- `React.FC` + 名前付き props 型、既存のコードスタイル（2 スペース、`{a, b}` の詰めた import）に合わせる。

## 7. テストは Storybook で書く

フロントエンドのテストは Storybook を使う（`*.test.tsx` を新規に作る前に Storybook で書けるか検討する）。

- コンポーネントと同じディレクトリに `Xxx.stories.tsx` を置く。
- 第 3 節の 4 状態それぞれをストーリーにする（`Loading` / `Error` / `Empty` / `Default`）。
- 操作の検証は `play` 関数で書く:

```tsx
import type {Meta, StoryObj} from '@storybook/react';
import {expect, fn, userEvent, within} from '@storybook/test';
import ProjectTable from './ProjectTable';

const meta: Meta<typeof ProjectTable> = {
  component: ProjectTable,
  args: {onArchive: fn()},
};
export default meta;
type Story = StoryObj<typeof ProjectTable>;

export const Default: Story = {
  args: {projects: [{id: 'p1', name: 'Billing', status: 'ACTIVE'}]},
  play: async ({canvasElement, args}) => {
    const canvas = within(canvasElement);
    await userEvent.click(canvas.getByRole('button', {name: /archive billing/i}));
    await expect(args.onArchive).toHaveBeenCalledWith('p1');
  },
};

export const Empty: Story = {args: {projects: []}};
```

- MUI テーマとルーターはデコレーター（`.storybook/preview.tsx` で `ThemeProvider` + `CssBaseline` +
  `MemoryRouter`）で包む。
- Storybook がまだ導入されていない場合は、導入してよいかユーザーに確認してから進める。

## 8. 仕上げのセルフレビュー

実装後、`npm start` で実画面（または Storybook）を開いて確認し、次をチェックする:

- [ ] 色・余白・フォントがすべて theme 経由か
- [ ] loading / error / empty / success が揃っているか、ストーリーがあるか
- [ ] 主要アクションが 1 つに絞られているか
- [ ] キーボード操作、`aria-label`、コントラスト
- [ ] 狭い幅で崩れないか
- [ ] 飾りを 1 つ外しても成立するか。成立するなら外す
