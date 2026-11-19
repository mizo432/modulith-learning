# Issue #672 実装計画

## 概要

バックログアイテム詳細画面のコメント表示をMarkdown対応にし、リアルタイム更新をポーリング方式で実装する。

- **Markdown対応**: `react-markdown`を導入し、コメント本文をMarkdownとしてレンダリング
- **セキュリティ**: 生HTMLは不可。`rehype-sanitize`でサニタイズ必須（#638の決定）
- **リアルタイム更新**: ポーリング方式で30秒固定の自動リフレッシュ（#636の決定、#574の実績パターン）
- **対象範囲**: frontendのみ

---

## 1. 現状分析

### 1.1 コメント表示の現状

`BacklogItemDetail.tsx`の279-289行目で、コメントはplain textとして直接表示されている。

```tsx
// 現状（279-289行目）
{comments.map((comment) => (
  <React.Fragment key={comment.id}>
    <ListItem alignItems="flex-start">
      <ListItemText
        primary={`${comment.authorName}（${new Date(comment.createdAt).toLocaleString()}）`}
        secondary={comment.body}  // ← plain textで表示
      />
    </ListItem>
    <Divider component="li" />
  </React.Fragment>
))}
```

### 1.2 リアルタイム更新の現状

リアルタイム更新（ポーリング）は**既に実装済み**である。

- `useAutoRefresh`フック（`frontend/src/hooks/useAutoRefresh.ts`）が30秒固定のポーリングを提供
- `useIsVisible`フック（`frontend/src/hooks/useIsVisible.ts`）がPage Visibility APIでタブ非表示を検出
- `BacklogItemDetail.tsx`では既に以下のように使用されている：
  - `REFRESH_INTERVAL = 30000`（30秒固定）
  - `shouldSkipRefresh`でコメント入力中・Value編集中はスキップ
  - `isVisible`でタブ非表示中は停止

**リアルタイム更新に関する追加実装は不要**である。既存の実装が#574の実績パターン（30秒固定、入力中・ダイアログ表示中はスキップ、タブ非表示中は停止）に合致している。

### 1.3 依存関係の現状

`frontend/package.json`には`react-markdown`、`remark-gfm`、`rehype-sanitize`は未導入。

| パッケージ | 現状 | 導入予定バージョン |
|-----------|------|-------------------|
| react-markdown | 未導入 | ^10.1.0 |
| remark-gfm | 未導入 | ^4.0.1 |
| rehype-sanitize | 未導入 | ^6.0.0 |

---

## 2. 変更対象ファイル

| ファイル | 変更内容 |
|---------|---------|
| `frontend/package.json` | 依存パッケージ追加 |
| `frontend/src/pages/products/BacklogItemDetail.tsx` | コメント表示をMarkdownレンダリングに変更 |
| `frontend/src/pages/products/BacklogItemDetail.test.tsx` | Markdownレンダリングのテスト追加 |
| `frontend/src/components/MarkdownComment.tsx`（新規） | 再利用可能なMarkdownコメントコンポーネント |
| `frontend/src/components/MarkdownComment.test.tsx`（新規） | MarkdownCommentコンポーネントのテスト |

---

## 3. 変更内容

### 3.1 依存パッケージ追加

`frontend/package.json`の`dependencies`に以下を追加：

```json
{
  "dependencies": {
    "react-markdown": "^10.1.0",
    "remark-gfm": "^4.0.1",
    "rehype-sanitize": "^6.0.0"
  }
}
```

**選定理由**:
- `react-markdown@10.1.0`: React 18対応の最新メジャーバージョン。`dangerouslySetInnerHTML`を使用せず、React要素として安全にレンダリング
- `remark-gfm@4.0.1`: GFM（GitHub Flavored Markdown）対応。テーブル、取り消し線、タスクリスト、フッターノート、自動リンクをサポート
- `rehype-sanitize@6.0.0`: HTMLサニタイズ。デフォルトスキーマはGitHub.comの許容リストに基づき、`<script>`、`<iframe>`などの危険なタグを除去

### 3.2 MarkdownCommentコンポーネント新規作成

**ファイル**: `frontend/src/components/MarkdownComment.tsx`

コメント本文をMarkdownとしてレンダリングする再利用可能なコンポーネントを作成する。

```tsx
import React from 'react';
import ReactMarkdown from 'react-markdown';
import remarkGfm from 'remark-gfm';
import rehypeSanitize from 'rehype-sanitize';
import { Box, SxProps, Theme } from '@mui/material';

interface MarkdownCommentProps {
  content: string;
  sx?: SxProps<Theme>;
}

export const MarkdownComment: React.FC<MarkdownCommentProps> = ({ content, sx }) => {
  if (!content) {
    return <Box sx={sx}>（本文なし）</Box>;
  }

  return (
    <Box
      sx={{
        '& p': { margin: '0 0 8px 0' },
        '& p:last-child': { margin: 0 },
        '& pre': {
          backgroundColor: '#f5f5f5',
          padding: '8px 12px',
          borderRadius: '4px',
          overflowX: 'auto',
        },
        '& code': {
          backgroundColor: '#f5f5f5',
          padding: '2px 4px',
          borderRadius: '2px',
          fontSize: '0.9em',
        },
        '& pre code': {
          backgroundColor: 'transparent',
          padding: 0,
        },
        '& table': {
          borderCollapse: 'collapse',
          width: '100%',
          marginBottom: '8px',
        },
        '& th, & td': {
          border: '1px solid #ddd',
          padding: '6px 12px',
        },
        '& th': {
          backgroundColor: '#f5f5f5',
        },
        '& ul, & ol': {
          paddingLeft: '24px',
          margin: '0 0 8px 0',
        },
        '& blockquote': {
          borderLeft: '4px solid #ddd',
          paddingLeft: '12px',
          color: '#666',
          margin: '0 0 8px 0',
        },
        ...sx,
      }}
    >
      <ReactMarkdown
        remarkPlugins={[remarkGfm]}
        rehypePlugins={[rehypeSanitize]}
      >
        {content}
      </ReactMarkdown>
    </Box>
  );
};
```

**設計判断**:
- `rehype-raw`は使用しない。ユーザー入力のMarkdownに生HTMLを含める必要がないため、デフォルトの安全な動作で十分
- `rehype-sanitize`は`remarkPlugins`ではなく`rehypePlugins`として適用。Markdown→AST→hASTの変換後にサニタイズする
- MUIの`Box`でスタイリングをカプセル化。既存の`BacklogItemDetail`のデザインと整合
- 空文字列・空白のみの場合は「（本文なし）」を表示

### 3.3 BacklogItemDetail.tsxの変更

**変更箇所1**: インポート追加（1-26行目付近）

```tsx
import { MarkdownComment } from '../../components/MarkdownComment';
```

**変更箇所2**: コメント表示部分の変更（279-289行目）

```tsx
// 変更後
{comments.map((comment) => (
  <React.Fragment key={comment.id}>
    <ListItem alignItems="flex-start">
      <ListItemText
        primary={`${comment.authorName}（${new Date(comment.createdAt).toLocaleString()}）`}
        secondary={
          <MarkdownComment content={comment.body} />
        }
      />
    </ListItem>
    <Divider component="li" />
  </React.Fragment>
))}
```

### 3.4 リアルタイム更新に関する変更

**変更なし**。既存の実装が要件を満たしているため、追加の変更は不要。

確認事項:
- ✅ 30秒固定ポーリング: `REFRESH_INTERVAL = 30000`
- ✅ 入力中はスキップ: `shouldSkipRefresh = commentBody !== '' || (valueInput !== null && item?.value !== valueInput)`
- ✅ タブ非表示中は停止: `useIsVisible()` + `isVisible`パラメータ
- ✅ 既存テストでカバー済み: `BacklogItemDetail.test.tsx`の「自動リフレッシュ」describeブロック

---

## 4. テスト観点

### 4.1 MarkdownCommentコンポーネントテスト（新規）

`frontend/src/components/MarkdownComment.test.tsx`

| テストケース | 検証内容 |
|-------------|---------|
| 太字Markdownのレンダリング | `**bold**`が`<strong>`としてレンダリングされる |
| 斜体Markdownのレンダリング | `*italic*`が`<em>`としてレンダリングされる |
| リンクMarkdownのレンダリング | `[text](url)`が`<a href="url">`としてレンダリングされる |
| コードブロックのレンダリング | ````code````が`<pre><code>`としてレンダリングされる |
| インラインコードのレンダリング | `` `code` ``が`<code>`としてレンダリングされる |
| リストのレンダリング | `- item`が`<ul><li>`としてレンダリングされる |
| GFMテーブルのレンダリング | `| a | b |`が`<table>`としてレンダリングされる |
| 取り消し線のレンダリング | `~~deleted~~`が`<del>`としてレンダリングされる |
| 生HTMLの除去 | `<script>alert(1)</script>`がテキストとしてエスケープされる |
| 危険な属性の除去 | `<a href="javascript:alert(1)">`の`href`が除去される |
| 空文字列の表示 | 空文字列で「（本文なし）」が表示される |
| 空白のみの表示 | 空白のみで「（本文なし）」が表示される |
| null・undefinedの処理 | null/undefinedでエラーにならない |
| 改行のレンダリング | 2行以上の改行が`<p>`タグで区切られる |
| タスクリストのレンダリング | `- [ ] task`が`<input type="checkbox">`としてレンダリングされる |

### 4.2 BacklogItemDetailテストの更新

`frontend/src/pages/products/BacklogItemDetail.test.tsx`

| テストケース | 検証内容 |
|-------------|---------|
| Markdownコメントの表示 | Markdownを含むコメントが正しくレンダリングされる |
| 複数コメントのMarkdown表示 | 複数のコメントがそれぞれ独立してMarkdownレンダリングされる |
| 生HTMLを含むコメントの安全性 | `<script>`タグを含むコメントが安全にレンダリングされる |
| 自動リフレッシュでコメントが更新される | ポーリングで新しいコメントが取得され、Markdownとして表示される |
| コメント追加後のMarkdown表示 | 新規追加されたコメントもMarkdownとして表示される |

**既存テストの維持**:
- 自動リフレッシュのテスト（268-380行目）はそのまま維持
- Value入力・スライダーのテストは影響なし

### 4.3 統合テスト観点

| テストケース | 検証内容 |
|-------------|---------|
| 画面全体のレンダリング | MarkdownCommentがBacklogItemDetailに正しく統合される |
| コメント追加→表示のフロー | コメント追加後、ポーリングで更新されたコメントがMarkdownとして表示される |
| 大量のMarkdownコメント | 多数のコメントがある場合のレンダリングパフォーマンス |

---

## 5. 実装手順

### ステップ1: 依存パッケージの追加

```bash
cd frontend
npm install react-markdown@^10.1.0 remark-gfm@^4.0.1 rehype-sanitize@^6.0.0
```

### ステップ2: MarkdownCommentコンポーネントの作成

`frontend/src/components/MarkdownComment.tsx`を作成。

### ステップ3: MarkdownCommentのテスト作成

`frontend/src/components/MarkdownComment.test.tsx`を作成。

```bash
cd frontend
npm test -- --testPathPattern=MarkdownComment --watchAll=false
```

### ステップ4: BacklogItemDetail.tsxの変更

- `MarkdownComment`のインポート追加
- コメント表示部分の`secondary={comment.body}`を`secondary={<MarkdownComment content={comment.body} />}`に変更

### ステップ5: BacklogItemDetailのテスト更新

- Markdownレンダリングの確認テスト追加
- 既存テストが引き続き通ることを確認

```bash
cd frontend
npm test -- --testPathPattern=BacklogItemDetail --watchAll=false
```

### ステップ6: 全テストの実行

```bash
cd frontend
npm test -- --watchAll=false
```

### ステップ7: ビルド確認

```bash
cd frontend
npm run build
```

---

## 6. リスクと対応

### リスク1: react-markdownのpeer dependency競合

**内容**: react-markdown@10系がReact 19をpeer dependencyとする可能性。
**影響度**: 中
**対応**: react-markdown@10.1.0のpeer dependencyはReact 18以上。現在の`react@^18.2.0`と互換性あり。インストール前に`npm install --dry-run`で確認。

### リスク2: Markdownレンダリングのパフォーマンス

**内容**: 大量のコメント（100件以上）がある場合、各コメントのMarkdownパースがレンダリングを遅くする可能性。
**影響度**: 低
**対応**: 
- 現状のバックログアイテム詳細画面はコメント数が限定的と想定
- 必要に応じて`React.memo`で`MarkdownComment`をメモ化
- 仮想化（react-windowなど）は現時点では不要

### リスク3: MUIのListItemTextとのスタイリング競合

**内容**: `ListItemText`の`secondary`プロパティにコンポーネントを渡す場合、MUIのデフォルトスタイリング（`color: text.secondary`）が適用される。
**影響度**: 低
**対応**: `MarkdownComment`の`Box`で`color: 'inherit'`を指定し、親のスタイルを継承。必要に応じて`sx`プロパティで調整。

### リスク4: サニタイズの抜け漏れ

**内容**: `rehype-sanitize`のデフォルトスキーマが将来のXSS脆弱性に対応していない可能性。
**影響度**: 低
**対応**: 
- `rehype-sanitize`のデフォルトスキーマはGitHub.comの許容リストに基づいており、定期的に更新されている
- `react-markdown`自体が`dangerouslySetInnerHTML`を使用しないため、基本的なXSSは防止される
- 定期的な依存パッケージのセキュリティアップデートで対応

### リスク5: TypeScriptの型定義

**内容**: `rehype-sanitize`の型定義が`react-markdown`の`rehypePlugins`プロパティと完全に互換しない可能性。
**影響度**: 低
**対応**: react-markdown@10系はunified ecosystemの型を適切にエクスポート。必要に応じて`as const`や型アサーションで対応。

---

## 7. 非機能要件の対応

| 要件 | 対応方法 |
|-----|---------|
| XSS防止 | `react-markdown`は`dangerouslySetInnerHTML`不使用。`rehype-sanitize`で追加サニタイズ |
| 生HTML不可 | `rehype-raw`を使用しない。Markdown内のHTMLタグはテキストとしてエスケープ |
| GFM対応 | `remark-gfm`でテーブル、取り消し線、タスクリストなどに対応 |
| リアルタイム更新 | 既存の`useAutoRefresh`フックで30秒ポーリング（変更なし） |
| 入力中のスキップ | 既存の`shouldSkipRefresh`ロジックで対応（変更なし） |
| タブ非表示中の停止 | 既存の`useIsVisible`フックで対応（変更なし） |

---

## 8. 検証チェックリスト

- [ ] `npm install`で依存パッケージが正常にインストールされる
- [ ] `MarkdownComment`コンポーネントの単体テストが全件パスする
- [ ] `BacklogItemDetail`の既存テストが引き続きパスする
- [ ] `BacklogItemDetail`のMarkdown関連テストがパスする
- [ ] 全テスト（`npm test`）がパスする
- [ ] ビルド（`npm run build`）が成功する
- [ ] 手動検証: 太字、斜体、リンク、コードブロック、リスト、テーブルが正しくレンダリングされる
- [ ] 手動検証: `<script>`タグを含むコメントが安全にレンダリングされる
- [ ] 手動検証: 30秒後にコメントが自動更新される
- [ ] 手動検証: コメント入力中は自動更新がスキップされる
- [ ] 手動検証: タブを非表示にすると自動更新が停止する

---

## 9. 参考情報

- react-markdown公式ドキュメント: https://remarkjs.github.io/react-markdown
- rehype-sanitize GitHub: https://github.com/rehypejs/rehype-sanitize
- remark-gfm GitHub: https://github.com/remarkjs/remark-gfm
- 関連Issue: #638（生HTML不可・サニタイズ必須の決定）、#636（リアルタイム更新のポーリング方式決定）、#574（ポーリング実績パターン）
