---
sessionId: session-261119-002149-11ex
---

# Technical Design

## Current Implementation

### 関連ファイル
- `frontend/src/pages/products/BacklogItemDetail.tsx` — 詳細画面（697行）
- `frontend/src/services/backlogItemService.ts` — APIサービス（176行）
- `frontend/src/contexts/AuthContext.tsx` — 認証コンテキスト（91行）
- `frontend/src/pages/products/BacklogItemDetail.test.tsx` — テスト（622行）

### 現在の削除ボタンの表示条件
`CommentItem`（450-553行）および `RepliesList`（571-694行）で:
```
{!hasReplies && !isEditing && (
  <IconButton onClick={onDelete}><DeleteIcon /></IconButton>
)}
```
→ **すべてのユーザー**に削除ボタンを表示（子返信がない場合）

### AuthContext の利用可能情報
- `auth.employeeId` — 現在ログインしている従業員のID
- `auth.isAdmin` — ADMINロール保持フラグ

### BacklogItemComment の情報
- `authorEmployeeId?: string` — コメント作成者のemployeeId

### Backend のエラーレスポンス
- `BacklogItemPermissionDeniedException` → HTTP 403 + `{"message": "..."}`
- 既存の `handleDeleteComment` は `err?.response?.data?.message` を取得して表示するので、403エラーのハンドリングは既に動作する

## Proposed Changes

### 1. 削除ボタンの表示制御（BacklogItemDetail.tsx）

`CommentItem` と `RepliesList` の両方で、削除ボタンの表示条件を以下に変更:

```
const canDelete = (auth.employeeId === comment.authorEmployeeId || auth.isAdmin) && !hasReplies;
```

### 2. props の追加

`CommentItemProps` に `canDelete: boolean` を追加。`RepliesListProps` にも同様に追加。
親コンポーネント（`BacklogItemDetail`）で権限判定の結果を渡す。

### 3. テスト追加（BacklogItemDetail.test.tsx）

AuthContext のモックをパラメータ化し、以下のテストケースを追加:
- 本人のコメント → 削除ボタン表示
- 他人のコメント（非ADMIN） → 削除ボタン非表示
- 他人のコメント（ADMIN） → 削除ボタン表示
- 403エラー → エラーメッセージ表示
- 返信コメントの削除権限制御

## Architecture

変更は `BacklogItemDetail.tsx` とテストファイルに閉じる。

``` mermaid
graph LR
  AuthContext -->|employeeId, isAdmin| BacklogItemDetail
  BacklogItemDetail -->|canDelete prop| CommentItem
  BacklogItemDetail -->|canDelete prop| RepliesList
  CommentItem -->|onDelete| BacklogItemDetail
  RepliesList -->|onDelete| BacklogItemDetail
  BacklogItemDetail -->|deleteComment API| Backend
```


# テストリスト

「〜したとき、〜となる」形式で、単純なケースから順に列挙する。

## 削除ボタンの表示制御

1. 投稿者本人（非ADMIN）が自分のコメントを表示したとき、削除ボタンが表示される
2. 投稿者本人（非ADMIN）が他人のコメントを表示したとき、削除ボタンが表示されない
3. ADMINユーザーが他人のコメントを表示したとき、削除ボタンが表示される
4. 投稿者本人（非ADMIN）が自分の返信コメントを表示したとき、削除ボタンが表示される
5. 他人（非ADMIN）が他人の返信コメントを表示したとき、削除ボタンが表示されない
6. 子返信があるコメントを表示したとき、投稿者本人であっても削除ボタンが表示されない（既存の振る舞いの回帰）

## 403エラー表示

7. 権限がないユーザーが削除ボタン（例：バグで表示された場合など）をクリックして403エラーが返されたとき、エラーメッセージがAlertとして表示される

## 既存削除フローの回帰

8. 投稿者本人が削除ボタンをクリックしたとき、コメントが正常に削除され、リストから消える（既存の削除成功フローが壊れていないことを確認）

## 編集ボタンの回帰

9. 子返信がないコメントを表示したとき、編集ボタンが削除ボタンと同様の権限条件で表示・非表示となる（削除ボタンと編集ボタンは同じ条件で表示されるため）

---

# 実装ステップの全体像

## ステップ1: テスト作成

**対象ファイル:** `frontend/src/pages/products/BacklogItemDetail.test.tsx`

1. AuthContext のモックをパラメータ化
   - 既存の `jest.mock` は固定値（`isAdmin: false`, `employeeId: 'emp-1'`）を返している
   - テストごとに `isAdmin` と `employeeId` を動的に切り替えられるよう、モックを `jest.fn()` ベースに書き換える
   - 各テストの `beforeEach` またはテスト内で `mockUseAuth.mockReturnValue(...)` で値を設定する
2. テストリストのケース1〜9に対応するテストケースを追加
3. 403エラーテストでは `backlogItemService.deleteComment` のモックを `reject` し、レスポンスに `{ status: 403, data: { message: "..." } }` を設定
4. 既存の削除成功テスト（571行目）と子返信あり非表示テスト（597行目）が引き続き通ることを確認

## ステップ2: 本実装

**対象ファイル:** `frontend/src/pages/products/BacklogItemDetail.tsx`

1. `CommentItemProps` に `canDelete: boolean` を追加
2. `RepliesListProps` に `canDelete: boolean` を追加
3. `CommentItem` コンポーネントで削除ボタン（および編集ボタン）の表示条件を `canDelete` prop に変更
   - 変更前: `{!hasReplies && !isEditing && (...)}`
   - 変更後: `{canDelete && !isEditing && (...)}`
4. `RepliesList` コンポーネントで同様に削除ボタン（および編集ボタン）の表示条件を `canDelete` prop に変更
   - 変更前: `{!(reply.hasReplies ?? false) && editingCommentId !== reply.id && (...)}`
   - 変更後: `{canDelete && editingCommentId !== reply.id && (...)}`
5. 親コンポーネント `BacklogItemDetail` で `canDelete` を計算して各サブコンポーネントに渡す
   - 計算式: `const canDelete = auth.employeeId === comment.authorEmployeeId || auth.isAdmin;`
   - `CommentItem` への渡しかた: `canDelete={auth.employeeId === rootComment.authorEmployeeId || auth.isAdmin}`
   - `RepliesList` への渡しかた: `canDelete={auth.employeeId === reply.authorEmployeeId || auth.isAdmin}`

**変更範囲:** `BacklogItemDetail.tsx` のみ。他ファイルは変更しない。

## ステップ3: 検証

1. **ユニットテスト実行:** `./gradlew :frontend:test` を実行し、全テストが通ることを確認
2. **本番ビルド実行:** `./gradlew :frontend:npmBuild` を実行し、ビルドが成功することを確認
3. 既存の削除成功フロー（テストケース8）が壊れていないことを再確認

---

# 未決定事項

1. **編集ボタンの表示条件:** 削除ボタンと同じ権限条件（投稿者本人またはADMIN）で表示・非表示とする予定だが、編集は削除より影響が小さいため、条件を緩和する可能性もある。現状は削除ボタンと同じ条件で統一する方針。
2. **`authorEmployeeId` のnull許容:** `comment.authorEmployeeId` が `undefined` の場合（例：レガシーデータ）の扱い。現状の比較では `auth.employeeId === undefined` は false となるため、削除ボタンは表示されない。この振る舞いで問題ないか確認が必要。
3. **403エラーのテスト方法:** フロントエンドのテストでは、削除ボタンが非表示になるため、403エラーを発生させるにはボタンを強制的に表示するテスト用propsまたは内部状態の操作が必要。テスト用ヘルパー関数で `canDelete` を true に固定してテストする方針でよいか。