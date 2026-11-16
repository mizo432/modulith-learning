# Issue #491 実装計画

## バックログアイテムをAccepted状態に変更する際にValueが設定されていることを保証するバリデーション（frontend側）

---

## 1. 概要

バックログアイテムを「承認済み（ACCEPTED）」状態に変更する際に、Value（ビジネス価値）が設定されていることを保証するfrontend側のバリデーションを実装する。backend側（Issue #490、PR #581）は既にACCEPTED遷移時にvalueがnullだとHTTP 400を返す実装が完了している。

---

## 2. 調査結果

### 2.1 現在のfrontendでのValue扱い

| 項目 | 現状 |
|------|------|
| **型定義** | `BacklogItem` インターフェース（`backlogItemService.ts`）に `value` フィールド**なし** |
| **APIレスポンス** | backendの`BacklogItemResponse`には`Integer value`フィールドあり（`BacklogItemResponseConverter.java:24`） |
| **一覧ページ** | Valueの表示・編集なし。テーブルにはタイトル・状態・優先度・作成者・作成日時・操作ボタンのみ |
| **詳細ページ** | Valueの表示・編集なし。タイトル・説明・優先度・作成者・作成日時・スプリントIDを表示 |
| **編集機能** | 詳細ページに編集機能なし。一覧ページの追加フォームのみ（title/description/priority/creatorName） |
| **サービスメソッド** | `changeStatus()` は status と actorEmployeeId のみを送信。`updateBacklogItem()` は存在しない |

### 2.2 状態変更UIの実装場所と仕組み

- **実装場所**: `BacklogItemList.tsx`（一覧ページ）
- **確認ダイアログ**: MUIの`<Dialog>`コンポーネントを使用（331-346行目）
- **フロー**:
  1. 「承認」または「却下」ボタンクリック → `setConfirmTarget({item, status})` で状態変更ダイアログを開く
  2. ダイアログで「変更する」ボタンクリック → `handleConfirmStatusChange()` 実行
  3. `backlogItemService.changeStatus()` を呼び出し、成功したら一覧を再読み込み
- **権限判定**: `canChangeStatus` 変数でPO/SMのみボタンを表示（74-77行目）
- **エラー処理**: HTTP 403 の場合は権限エラーメッセージを、それ以外はサーバーメッセージを表示（156-164行目）

### 2.3 テスト基盤

| 項目 | 状況 |
|------|------|
| **テストランナー** | Jest（`react-scripts test`） |
| **テストライブラリ** | React Testing Library + `@testing-library/jest-dom` + `@testing-library/user-event` |
| **既存テスト** | `App.test.tsx`（ダミー）、`sprintService.test.ts`（サービス層のモックテスト） |
| **テスト対象** | サービス層のモックテストパターンが確立されている |

**結論**: テスト先行アプローチが可能。Jest + React Testing Library の環境が整っている。

### 2.4 Backend API の仕様

| API | メソッド | Path | リクエスト | レスポンス |
|-----|---------|------|-----------|-----------|
| Value更新 | PATCH | `/v1/backlog-items/{id}` | `{title?, description?, priority?, sprintId?, value?}` | `BacklogItemResponse`（value含む） |
| 状態変更 | PATCH | `/v1/backlog-items/{id}/status` | `{status, actorEmployeeId}` | `BacklogItemResponse` |
| 単件取得 | GET | `/v1/backlog-items/{id}` | - | `BacklogItemResponse`（value含む） |
| 一覧取得 | GET | `/v1/scrum-teams/{teamId}/backlog-items` | - | `BacklogItem[]`（value含む） |

- **Value範囲**: 0〜100（`BacklogItem.VALUE_MIN_VALUE` / `VALUE_MAX_VALUE`）
- **エラーメッセージ**: `"BacklogItem value must be set before accepting"`（HTTP 400）
- **範囲外エラー**: `"BacklogItem value must be between 0 and 100"`（HTTP 400）

---

## 3. 変更対象ファイルと変更内容

### 3.1 `frontend/src/services/backlogItemService.ts`

**変更内容**:
1. `BacklogItem` インターフェースに `value?: number;` フィールドを追加
2. `UpdateBacklogItemRequest` インターフェースを新規追加
3. `updateBacklogItem(backlogItemId: string, request: UpdateBacklogItemRequest)` メソッドを追加

```typescript
// 追加するインターフェース
export interface UpdateBacklogItemRequest {
  title?: string;
  description?: string;
  priority?: number;
  sprintId?: string;
  value?: number;
}

// 追加するメソッド
async updateBacklogItem(
  backlogItemId: string,
  request: UpdateBacklogItemRequest,
): Promise<BacklogItem> {
  const response = await api.patch(`/v1/backlog-items/${backlogItemId}`, request);
  return response.data;
}
```

### 3.2 `frontend/src/pages/products/BacklogItemDetail.tsx`

**変更内容**:
1. Value入力フィールド（数値型）を詳細表示セクションに追加
2. Valueの表示（読み取り専用モード）と編集モードの切り替え
3. Value入力時のクライアント側バリデーション（0〜100の範囲チェック）
4. 必須マーク（`*`）を表示
5. Value保存ボタンを追加
6. 状態変更ボタン（「承認」）を詳細ページに追加（PO/SMのみ表示）
7. 状態変更確認ダイアログを追加
8. Value未入力時の「承認」ボタン無効化ロジック

**UI構成**:
```
Paper（詳細情報）
├── タイトル + 状態Chip
├── 説明
├── Value入力フィールド（数値、0-100、必須マーク付き）
│   ├── TextField type="number"
│   ├── helperText: "0〜100の範囲で入力してください"
│   └── error: 範囲外時のエラーメッセージ
├── 優先度 / 作成者 / 作成日時
└── [Valueを保存] ボタン

Paper（状態変更）— PO/SMのみ表示
└── [承認] ボタン（Value未入力時はdisabled）
    └── ToolTip: "Valueを設定してから承認してください"（disabled時）

Dialog（状態変更確認）
├── タイトル: "状態変更の確認"
├── 本文: "「{title}」を承認済みに変更します。よろしいですか？"
└── [キャンセル] [変更する]
```

### 3.3 `frontend/src/pages/products/BacklogItemList.tsx`

**変更内容**:
1. テーブルに「Value」列を追加
2. 「承認」ボタンにValueチェックを追加 — Valueがnullの場合はボタンを無効化し、ToolTipで入力を促す
3. 状態変更確認ダイアログにValue入力フィールドを追加（オプションA）または、詳細ページへの遷移を促す（オプションB）

**推奨アプローチ（オプションB）**: 一覧ページではValue表示のみとし、「承認」ボタンクリック時にValueがnullの場合は詳細ページへ遷移してValueを入力させる。これにより、一覧ページの複雑さを抑えられる。

---

## 4. UIの振る舞いの仕様

### 4.1 詳細ページ（BacklogItemDetail.tsx）

#### Value入力フィールド
- **表示**: 常にValue入力フィールドを表示（数値型TextField）
- **必須マーク**: TextFieldのlabelに `*` を付与（例: "Value *"）
- **初期値**: APIレスポンスのvalueを反映（nullの場合は空）
- **範囲バリデーション**: 入力値が0未満または100超過の場合、`error` プロパティをtrueにし、`helperText` で「0〜100の範囲で入力してください」を表示
- **保存**: 「Valueを保存」ボタンで `updateBacklogItem()` を呼び、valueのみを更新
- **作成時**: 新規作成時はValueは任意（SUGGESTED状態なので必須ではない）

#### 承認ボタンの無効化条件
- `item.status === 'SUGGESTED'` かつ `canChangeStatus`（PO/SM）の場合にのみ表示
- `item.value === null || item.value === undefined` の場合、`disabled` 属性を設定
- disabled時のToolTip: "Valueを設定してから承認してください"

#### 状態変更フロー
1. 「承認」ボタンクリック → 確認ダイアログ表示
2. ダイアログで「変更する」クリック → `changeStatus()` API呼び出し
3. 成功 → 一覧ページへ遷移または再読み込み
4. **HTTP 400受信時** → `error` stateにサーバーメッセージを設定し、`<Alert severity="error">` で表示

### 4.2 一覧ページ（BacklogItemList.tsx）

#### Value列
- テーブルに「Value」列を追加
- nullの場合は `-` を表示

#### 承認ボタンの無効化
- `item.value === null` の場合、「承認」ボタンを `disabled` にする
- disabled時のToolTip: "Valueを設定してから承認してください"
- 「却下」ボタンはValueの有無に関係なく有効

### 4.3 エラー表示

#### クライアント側バリデーションエラー
- Value範囲外（0未満または100超過）: TextFieldの`helperText`で即座に表示
- Value未入力時の承認ボタン: ToolTipで入力を促す

#### サーバー側エラー（HTTP 400）
- `<Alert severity="error">` コンポーネントで表示
- サーバーから返されたメッセージ（`err?.response?.data?.message`）を表示
- 例: "BacklogItem value must be set before accepting"
- 閉じるボタン（`onClose`）で非表示可能

---

## 5. テスト観点

### 5.1 テスト先行アプローチ

既存のテスト基盤（Jest + React Testing Library）を活用し、以下のテストを追加する。

#### `backlogItemService.test.ts`（新規作成）
- `updateBacklogItem()` が正しいAPIエンドポイントを呼び出すこと
- `changeStatus()` がHTTP 400エラーを正しく伝播すること

#### `BacklogItemDetail.test.tsx`（新規作成）
- Valueフィールドが表示されること
- Valueに必須マーク（`*`）が含まれること
- Value範囲外入力でエラーメッセージが表示されること
- Value未入力時に「承認」ボタンがdisabledであること
- Value入力後に「承認」ボタンがenabledになること
- HTTP 400受信時にエラーメッセージが表示されること
- Value保存ボタンでAPIが呼び出されること

#### `BacklogItemList.test.tsx`（新規作成）
- Value列が表示されること
- Valueがnullのアイテムで「承認」ボタンがdisabledであること
- Valueがあるアイテムで「承認」ボタンがenabledであること

### 5.2 テストパターン

| テストケース | 入力 | 期待結果 |
|------------|------|---------|
| Value未入力時の承認ボタン | value=null | ボタンdisabled、ToolTip表示 |
| Value入力後の承認ボタン | value=50 | ボタンenabled |
| Value範囲外（-1） | value=-1 | エラーメッセージ表示 |
| Value範囲外（101） | value=101 | エラーメッセージ表示 |
| Value境界値（0） | value=0 | 有効、エラーなし |
| Value境界値（100） | value=100 | 有効、エラーなし |
| HTTP 400エラー受信 | APIが400を返す | Alertにエラーメッセージ表示 |
| Value保存成功 | value=50、保存ボタンクリック | API呼び出し、再読み込み |

---

## 6. 実施手順

### Step 1: 型定義とサービス層の更新
1. `backlogItemService.ts` に `value?: number` を `BacklogItem` に追加
2. `UpdateBacklogItemRequest` インターフェースを追加
3. `updateBacklogItem()` メソッドを追加

### Step 2: テスト作成（テスト先行）
1. `backlogItemService.test.ts` を作成し、`updateBacklogItem()` のテストを追加
2. `BacklogItemDetail.test.tsx` を作成し、Value関連のUIテストを追加
3. `BacklogItemList.test.tsx` を作成し、Value列とボタン無効化のテストを追加

### Step 3: 詳細ページのValue入力フィールド実装
1. `BacklogItemDetail.tsx` にValue入力フィールドを追加
2. 範囲バリデーションロジックを実装
3. Value保存機能を実装
4. 必須マークを表示

### Step 4: 詳細ページの状態変更機能実装
1. PO/SM判定ロジックを追加（一覧ページと同じパターン）
2. 「承認」ボタンを追加（Value未入力時はdisabled）
3. ToolTipを追加
4. 状態変更確認ダイアログを追加
5. HTTP 400エラー処理を実装

### Step 5: 一覧ページの更新
1. テーブルにValue列を追加
2. 「承認」ボタンにValueチェックを追加（null時はdisabled + ToolTip）

### Step 6: 動作確認とテスト実行
1. `cd frontend && npm test` でテストがすべて通ることを確認
2. 手動でブラウザ上で動作確認
3. Edgeケース（境界値、null値、HTTP 400）の確認

---

## 7. リスクと範囲外事項

### 7.1 リスク

| リスク | 影響 | 対応 |
|-------|------|------|
| backendのAPIレスポンスにvalueが含まれていない場合 | 型定義と実際のレスポンスが不一致 | backendのビルド・デプロイが完了していることを確認 |
| Flywayマイグレーションが適用されていない場合 | DBにvalue列が存在せず、APIがエラー | `flywayMigrateAg` が実行済みであることを確認 |
| PO/SM判定ロジックの重複 | 一覧ページと詳細ページで同じロジックを2箇所に書く | 共有フックまたはユーティリティ関数に抽出することを検討 |

### 7.2 範囲外事項

- **backend側の修正**: backendはIssue #490/#493で完了済み。本Issueではfrontendのみ対象
- **Valueの初期値設定**: 新規作成時のValueデフォルト値は設定しない（任意のまま）
- **Valueの履歴管理**: Valueの変更履歴は管理しない（状態遷移の履歴はロングタームイベントパターンで担保）
- **一覧ページでのValue編集**: 一覧ページではValueの表示のみ。編集は詳細ページで行う
- **バッチ処理によるValue一括設定**: 対象外
- **Valueの集計・グラフ表示**: 対象外
- **認可（OAuth2）との連携強化**: 現在のlocalStorageベースの判定を維持

### 7.3 今後の検討事項

- PO/SM判定ロジックを共有フック（`useCanChangeStatus`）に抽出し、一覧ページと詳細ページで共通化
- Value入力フィールドのUX改善（スライダーやプリセット値の提供）
- Valueの必須化をbackendのAPI仕様と同期して、フロントエンドのバリデーションルールを更新

---

## 8. 用語集

| 用語 | 説明 |
|-----|------|
| Value | バックログアイテムのビジネス価値を0〜100で定量的に評価した値 |
| ACCEPTED | バックログアイテムの「承認済み」状態 |
| SUGGESTED | バックログアイテムの「提案」状態（初期状態） |
| PO | Product Owner（プロダクトオーナー） |
| SM | Scrum Master |
| ToolTip | MUIの`<Tooltip>`コンポーネント。マウスホバーでメッセージを表示 |
