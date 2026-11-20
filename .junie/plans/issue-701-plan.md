---
sessionId: session-261120-134704-1s24
---

# Issue #701 — フロントエンド描画時クラッシュ修正の計画書

## 概要・目標

現行 develop のフロントエンドが `App` コンポーネント描画時に必ず例外をスローしてクラッシュする不具合を修正する。

### 原因

`frontend/src/App.tsx` で、`AppContent` コンポーネント（77行目）が `useAuth()` を呼んでいる（79行目）が、`AuthProvider` は `AppContent` 自身の return 内（96行目）にしか配置されていない。そのため `AppContent` の初回レンダリング時に `useAuth()` が `AuthContext` を見つけられず、例外をスローする。

```
App
 └─ Router
      └─ AppContent          ← ここで useAuth() 呼ぶ（79行目）
           └─ AuthProvider   ← ここにしか Provider ない（96行目）
                └─ ...
```

`useAuth` の実装（`AuthContext.tsx` 84〜90行目）は、context が `undefined` の場合 `throw new Error('useAuth must be used within an AuthProvider')` となる。

### 修正方針

`AuthProvider` を `Router` 内・`AppContent` の外側に移動し、`AppContent` 内の `<AuthProvider>` を除去する。

```
App
 └─ Router
      └─ AuthProvider        ← ここへ移動
           └─ AppContent     ← useAuth() が Provider を見つけられるようになる
                └─ ...
```

### 制約

- `AuthProvider` は `useNavigate()` を使用している（`AuthContext.tsx` 18行目）ため、`<Router>` の配下に配置する必要がある。`<Router>` の外側には配置できない。
- `AppContent` 内の `useNavigate` が Router 配下のままになるよう保つこと（変更不要。`AppContent` は既に `Router` 配下にある）。

### スコープ

**In Scope:**
- `frontend/src/App.tsx` の修正（`AuthProvider` の配置変更）
- `frontend/src/App.test.tsx` の修正（実描画テストの追加）

**Out of Scope:**
- `AuthContext.tsx` の変更
- 他のコンポーネントの変更
- バックエンドの変更

## 技術設計

### 現在の実装

**`frontend/src/App.tsx`**
- `App` (218〜224行目): `<Router>` の中に `<AppContent />` を配置
- `AppContent` (77〜216行目):
  - 79行目で `useAuth()` を呼び出し（→ 例外発生）
  - 96〜214行目で `<AuthProvider>` を return 内に配置（遅すぎる）
  - `employeeId` を `NotificationSnackbar` に渡す（212行目）

**`frontend/src/contexts/AuthContext.tsx`**
- `AuthProvider` (17〜82行目): `useNavigate()` を使用（→ Router 配下必須）
- `useAuth` (84〜90行目): context が undefined の場合例外をスロー

**`frontend/src/App.test.tsx`**
- 8行目のみ。`expect(true).toBe(true)` のダミーテスト。`App` のレンダリングは一切行わない。

### 変更内容

#### 1. `App.tsx` の修正

**変更前:**
```tsx
function App() {
  return (
      <Router>
        <AppContent/>
      </Router>
  );
}

const AppContent = () => {
  const {employeeId} = useAuth();  // ← 例外：AuthProvider がない
  // ...
  return (
      <AuthProvider>  // ← ここにしかないので遅い
        <CssBaseline/>
        {/* ... */}
      </AuthProvider>
  );
};
```

**変更後:**
```tsx
function App() {
  return (
      <Router>
        <AuthProvider>
          <AppContent/>
        </AuthProvider>
      </Router>
  );
}

const AppContent = () => {
  const {employeeId} = useAuth();  // ← OK: 外側に AuthProvider がある
  // ...
  return (
      <>  {/* AuthProvider を除去 */}
        <CssBaseline/>
        {/* ... */}
      </>
  );
};
```

**具体的な変更点:**
1. `App` 関数で `<AuthProvider>` を `<Router>` 内・`<AppContent />` の外側に配置
2. `AppContent` の return から `<AuthProvider>` の開きタグ（96行目）と閉じタグ（214行目）を除去し、`<>` (Fragment) に置き換え
3. これにより `AppContent` 内の `useAuth()` と、`ProtectedRoute`・`FirstLoginRoute`・`AdminRoute`・各ページコンポーネントの `useAuth()` がすべて正常に動作する

#### 2. `App.test.tsx` の修正

従来のテストは `App` のレンダリングを行わず、この不具合を検出できなかった。実描画テストを追加する。

**追加するテスト:**
- `App` を `render()` したとき、例外をスローせずに描画が完了する
- 未認証状態でアクセスしたとき、ログイン画面が表示される（`/login` へのリダイレクト確認）

## テストリスト

### App 描画スモークテスト（新規追加）
1. `App` を `render()` したとき、例外をスローせずに描画が完了する
2. 未認証状態で `App` を描画したとき、ログイン画面のタイトルが表示される
3. `App` を描画したとき、`AuthProvider` が正しく提供され、`useAuth()` が例外をスローしない

### 既存テストの回帰確認
4. 既存のフロントエンドテスト（`NotificationSnackbar.test.tsx` など）を実行したとき、全件パスする
5. `npm run build` を実行したとき、ビルドが成功する

## 変更対象ファイル

| ファイル | 変更種別 | 内容 |
|---------|---------|------|
| `frontend/src/App.tsx` | 変更 | `AuthProvider` の配置変更、`AppContent` 内の Provider 除去 |
| `frontend/src/App.test.tsx` | 変更 | 実描画スモークテストの追加 |

## リスク

1. **`AuthProvider` の `useNavigate()` 依存**: `AuthProvider` を `<Router>` 配下に配置するため問題なし。この点は修正方針で既に考慮済み。
2. **`NotificationSnackbar` の `employeeId` 渡り**: `AppContent` が `useAuth()` から `employeeId` を取得して `NotificationSnackbar` に渡すロジック（212行目）は変更しない。修正後正常に動作する。
3. **テストの `render` 時の警告**: `BrowserRouter` がテスト環境で警告を出す可能性があるが、既存のテストパターン（`react-scripts test` / Jest）では問題なく動作する。
