# Issue #494 実装計画書 — バックログアイテムのValue（0〜100）範囲バリデーションのfrontend側実装

> 作成日: 2026-11-16
> 前提: 現在のブランチには #491 の実装が含まれている
> Valueの型: Integer（#577 で決定済み）

---

## 1. 要件概要

Issue #494では、バックログアイテムのValue（0〜100）の範囲バリデーションをfrontend側で実装する。具体的には以下の7つのサブ要件から構成される。

| # | 要件 | 説明 |
|---|------|------|
| R1 | 詳細ページにValue入力（スライダーまたは数値入力） | 範囲を視覚的に表示し範囲外を防止する |
| R2 | 入力フォームに範囲表示（0-100）を出す | ユーザーが有効範囲を一目で理解できる |
| R3 | Valueを色の変化などで視覚的に分かりやすく表示する | 数値の大小を直感的に把握できる |
| R4 | Accepted変更リクエスト送信前にクライアント側で範囲チェックする | サーバーリクエスト前に無効値をブロック |
| R5 | サーバーの400エラーを表示する | バリデーション失敗時に適切なエラーメッセージを表示 |
| R6 | Suggested状態のアイテム作成時もValueの範囲制限を適用する | 作成時にも同様のバリデーションを適用 |
| R7 | レスポンシブ対応 | モバイル含む各種画面サイズで適切に表示・操作できる |

---

## 2. 現行実装の確認結果

### 2.1 BacklogItemDetail.tsx（詳細ページ）

| 項目 | 実装内容 |
|------|---------|
| Value入力フィールド | `TextField`（`type="number"`）を使用。`min=0` / `max=100` のHTML制約付き |
| 範囲外エラー表示 | `handleValueChange()` でリアルタイム検証。範囲外の場合 `error={true}` + `helperText` で「0〜100の範囲で入力してください」を表示 |
| 保存処理 | `handleSaveValue()` で再検証後、`backlogItemService.updateBacklogItem()` を呼び出し |
| 400エラー表示 | `error` state + `<Alert severity="error">` でサーバーエラーメッセージを表示 |
| 初期値 | `item.value` を読み込み時に `valueInput` state に反映 |

### 2.2 BacklogItemList.tsx（一覧ページ）

| 項目 | 実装内容 |
|------|---------|
| Value列 | テーブルに「Value」列あり。`item.value ?? '-'` で表示 |
| 承認ガード | `item.value == null` の場合、承認ボタンを `disabled` に。Tooltip で「Valueを設定してから承認してください」を表示 |
| 却下ボタン | Valueの有無に関係なく常に有効 |
| 400エラー表示 | `<Alert severity="error">` でサーバーエラーメッセージを表示 |
| 作成フォーム | タイトル・説明・優先度・作成者名の4フィールド。**Value入力フィールドはなし** |

### 2.3 backlogItemService.ts

| 項目 | 実装内容 |
|------|---------|
| `BacklogItem` 型 | `value?: number` を含む |
| `CreateBacklogItemRequest` | `value` プロパティ**なし**（backendの作成APIがValueを受け付けない） |
| `UpdateBacklogItemRequest` | `value?: number` を含む |
| `updateBacklogItem()` | PATCH `/v1/backlog-items/{id}` を呼び出し。400エラーのメッセージを正規化して伝播 |
| `changeStatus()` | PATCH `/v1/backlog-items/{id}/status` を呼び出し。400エラーのメッセージを正規化して伝播 |

### 2.4 既存テスト

| テストファイル | カバレッジ |
|---------------|-----------|
| `BacklogItemDetail.test.tsx` | Value入力フィールドの表示、範囲外エラー（-1, 101）、境界値（0, 100）、保存ボタンのAPI呼び出し |
| `BacklogItemList.test.tsx` | Value列の表示、value=null 時の「-」表示、承認ボタンの有効/無効、却下ボタンの無影響 |
| `backlogItemService.test.ts` | `updateBacklogItem()` のPATCH呼び出し、`changeStatus()` の400エラー伝播（value未設定、value範囲外） |

---

## 3. 要件 vs 実装済み 対応表

| 要件# | 要件 | 実装済み | 未実装 | 備考 |
|-------|------|---------|--------|------|
| **R1** | 詳細ページにValue入力（スライダーまたは数値入力）で範囲を視覚的に表示し範囲外を防止する | **数値入力フィールドは実装済み**（min/max制約、範囲外エラー表示） | **スライダーUIは未実装** | 数値入力のみで「範囲を視覚的に表示」しているとは言えない |
| **R2** | 入力フォームに範囲表示（0-100）を出す | **部分的に実装済み**（範囲外エラー時の `helperText` として「0〜100の範囲で入力してください」を表示） | **常時表示は未実装** | エラー時のみ表示。正常時にも範囲が分かるよう常時表示すべき |
| **R3** | Valueを色の変化などで視覚的に分かりやすく表示する | **未実装** | 詳細ページ・一覧ページともに、Valueの数値に応じた色変化なし | 新機能 |
| **R4** | Accepted変更リクエスト送信前にクライアント側で範囲チェックする | **実装済み** | — | `handleSaveValue()` で `num < VALUE_MIN || num > VALUE_MAX` をチェックし、範囲外ならAPI呼び出しをブロック |
| **R5** | サーバーの400エラーを表示する | **実装済み** | — | `<Alert severity="error">` で `err?.response?.data?.message` を表示 |
| **R6** | Suggested状態のアイテム作成時もValueの範囲制限を適用する | **未実装** | 作成フォームにValue入力フィールドなし。backendの作成APIもValueを受け付けない | 後述の判断が必要 |
| **R7** | レスポンシブ対応 | **部分的に実装済み** | — | MUIの `TextField` は `fullWidth` でレスポンシブだが、スライダー追加時は別途確認が必要 |

---

## 4. 未実装分の具体的な実装案

### 4.1 R1 — スライダーUIの要否と方式

**判断: スライダーを追加する**

理由:
- Issue #494の要件には「スライダーまたは数値入力」とあり、数値入力のみでは「範囲を視覚的に表示し範囲外を防止する」という要件を完全に満たしていない
- スライダーは範囲（0〜100）を直感的に示し、範囲外入力を物理的に防止できる
- 数値入力フィールドは既存のものを維持し、スライダーと併用する（デュアル入力方式）

**実装方式:**

MUIの `Slider` コンポーネントを使用し、既存の `TextField` と連動させる。

```
┌─────────────────────────────────────────────┐
│  Value *                                     │
│  ━━━━━━━━━━━━━━━━━━━━━●━━━━ 50             │  ← Slider（0〜100）
│  [  50  ]                                    │  ← TextField（数値直接入力）
│  範囲: 0〜100                                │  ← 常時表示の範囲ラベル
│                     [Valueを保存]            │
└─────────────────────────────────────────────┘
```

**変更箇所:**
- `BacklogItemDetail.tsx`
  - MUI `Slider` のimport追加
  - `valueInput` state を数値型（`number | null`）に変更し、Slider と TextField の両方から更新可能にする
  - Slider の `onChange` で数値を更新、TextField の `onChange` で文字列→数値変換後更新
  - Slider と TextField の双方向連動（片方を変更しても他方が即時反映）

### 4.2 R2 — 範囲表示の常時表示

**判断: Sliderの下に常時表示する**

Slider自体が0と100の目盛りを表示するため、追加の `helperText` として「範囲: 0〜100」を常時表示する。エラー時はSliderの `marks` や TextField の `error` 状態も併用する。

**実装方式:**
- Sliderの `marks` オプションで 0, 25, 50, 75, 100 の目盛りを表示
- Slider下に `<Typography variant="caption" color="text.secondary">範囲: 0〜100</Typography>` を常時表示
- 範囲外エラー時は TextField の `helperText` で既存のエラーメッセージを表示（変更なし）

### 4.3 R3 — Valueの色による視覚的表現

**判断: 色相グラデーションでValueの大小を表示する**

Valueの値に応じて色を変化させ、直感的に「低い＝赤系」「高い＝緑系」を表現する。

**実装方式:**

HSLカラー空間を使用し、Value 0→赤（hue=0°）、Value 100→緑（hue=120°）として線形補間する。

```typescript
const valueToColor = (value: number): string => {
  const hue = (value / 100) * 120; // 0→赤, 100→緑
  return `hsl(${hue}, 70%, 45%)`;
};
```

**適用箇所:**

1. **詳細ページ（BacklogItemDetail.tsx）**: Sliderのトラック色をValueに応じて変化させる。MUI Sliderの `sx` で `& .MuiSlider-track` の `backgroundColor` を動的に設定。
2. **一覧ページ（BacklogItemList.tsx）**: Value列のセルに背景色またはテキスト色を適用。Valueがnullの場合は「-」のまま変更なし。

```typescript
// 一覧ページのValueセル例
<TableCell
  sx={{
    backgroundColor: item.value != null
      ? `hsl(${(item.value / 100) * 120}, 70%, 92%)`
      : 'transparent',
    color: item.value != null
      ? `hsl(${(item.value / 100) * 120}, 70%, 45%)`
      : 'inherit',
    fontWeight: 'bold',
  }}
>
  {item.value ?? '-'}
</TableCell>
```

**共通化:**
- `valueToColor` 関数を `frontend/src/utils/valueColor.ts` などに共通ユーティリティとして抽出し、詳細ページと一覧ページで共有する

### 4.4 R6 — 作成時のValue入力について

**判断: 本Issue（#494）の範囲に含めない**

理由:
1. **backendの作成APIがValueを受け付けない**（#493で対象外と決定済み）。`CreateBacklogItemRequest` に `value` プロパティがない
2. backendのAPI変更は別のIssue（#493など）で扱うべきであり、frontend単独では意味がない
3. 現在のフローでは、アイテム作成後 → 詳細ページでValueを設定 → 承認、という手順で設計されている
4. backendのAPIがValueを受け付けるようになった時点で、作成フォームへのValue追加を別途Issueとして扱う

**ただし、以下の対応は本Issueで行う:**
- 作成フォームの `DialogContentText` に「Valueは作成後、詳細ページで設定してください（範囲: 0〜100）」という案内文を追加する（UX改善）

---

## 5. レスポンシブ対応（R7）

既存の `TextField` は `fullWidth` でレスポンシブ対応済み。スライダー追加時に以下の点を確認・対応する。

- MUI `Slider` はデフォルトでコンテナ幅に追随するが、モバイル画面（<600px）ではマージン調整が必要
- Slider + TextField の縦配置を、モバイルでは縦積み、タブレット以上では横並びにする
- MUIの `useMediaQuery` または `Box` の `display` sx prop でブレークポイント制御

```typescript
<Box sx={{
  display: 'flex',
  flexDirection: { xs: 'column', sm: 'row' },
  alignItems: { xs: 'stretch', sm: 'flex-end' },
  gap: 2,
}}>
  <Box sx={{ flex: 1 }}>
    <Slider ... />
    <TextField ... />
  </Box>
  <Button ...>Valueを保存</Button>
</Box>
```

---

## 6. 変更ファイル一覧

| ファイル | 変更内容 | 対応要件 |
|---------|---------|---------|
| `frontend/src/utils/valueColor.ts`（新規） | Value→色のマッピングユーティリティ関数 | R3 |
| `frontend/src/pages/products/BacklogItemDetail.tsx` | Slider追加、双方向連動、範囲常時表示、Slider色変化 | R1, R2, R3, R7 |
| `frontend/src/pages/products/BacklogItemList.tsx` | Value列の色変化適用、作成フォームの案内文追加 | R3, R6(一部) |

---

## 7. テスト計画

### 7.1 既存テストへの追加観点

| テストファイル | 追加テスト | 対応要件 |
|---------------|-----------|---------|
| `BacklogItemDetail.test.tsx` | Sliderコンポーネントの表示確認 | R1 |
| `BacklogItemDetail.test.tsx` | Slider操作でValueが更新されること | R1 |
| `BacklogItemDetail.test.tsx` | SliderとTextFieldの双方向連動確認 | R1 |
| `BacklogItemDetail.test.tsx` | 範囲表示（0〜100）が常時表示されていること | R2 |
| `BacklogItemDetail.test.tsx` | Sliderのトラック色がValueに応じて変化する | R3 |
| `BacklogItemDetail.test.tsx` | モバイル幅（<600px）での縦積みレイアウト確認 | R7 |
| `BacklogItemList.test.tsx` | Value列のセルに色が適用されている | R3 |
| `BacklogItemList.test.tsx` | Value=nullのセルには色が適用されない | R3 |
| `BacklogItemList.test.tsx` | 作成フォームにValue設定の案内文が表示される | R6(一部) |

### 7.2 新規テストファイル

| ファイル | 内容 |
|---------|------|
| `frontend/src/utils/__tests__/valueColor.test.ts` | `valueToColor()` の境界値テスト（0, 50, 100, 範囲外） |

### 7.3 テスト戦略

- **ユニットテスト**: `valueToColor()` ユーティリティ関数の入出力検証
- **コンポーネントテスト**: Sliderの表示・操作・連動、色の適用、レスポンシブレイアウト
- **既存テストの維持**: 既存の範囲チェックテスト（-1, 101, 0, 100）はそのまま維持。Slider追加後もTextField経由の入力で同等の検証が可能なことを確認

---

## 8. 実装順序（推奨）

1. **ステップ1**: `valueToColor()` ユーティリティ関数の実装 + ユニットテスト
2. **ステップ2**: `BacklogItemDetail.tsx` にSlider追加 + 双方向連動 + 範囲常時表示
3. **ステップ3**: Sliderのトラック色変化（詳細ページ）
4. **ステップ4**: `BacklogItemList.tsx` のValue列に色変化適用
5. **ステップ5**: 作成フォームの案内文追加
6. **ステップ6**: レスポンシブ対応の微調整
7. **ステップ7**: 全テストの実行と確認

---

## 9. リスクと留意点

| リスク | 対応 |
|-------|------|
| SliderとTextFieldのstate同期で無限ループが発生する可能性 | `useEffect` の依存配列を適切に管理し、state更新のトリガーを明確に分離する |
| 色の視認性（色覚多様性への配慮） | 色相変化だけでなく、数値自体も明確に表示。背景色は薄く（lightness=92%）、テキスト色は濃く（lightness=45%）してコントラストを確保 |
| MUI Sliderのアクセシビリティ | MUI SliderはデフォルトでARIA属性をサポート。追加の `aria-label` は不要だが、テストで確認 |
| 既存のValue入力テストとの互換性 | Slider追加後もTextFieldは維持するため、既存テストはそのまま動作するはず。ただし、`valueInput` stateの型変更（string → number \| null）に伴うテスト修正が必要 |

---

## 10. 用語・定数の統一

- `VALUE_MIN = 0` / `VALUE_MAX = 100` は `BacklogItemDetail.tsx` に定義済み。`valueColor.ts` にも同じ定数を定義するか、共通の定数ファイルから参照するようにする
- 範囲表示の文言は「0〜100」に統一（既存の `helperText` と同じ表記）
