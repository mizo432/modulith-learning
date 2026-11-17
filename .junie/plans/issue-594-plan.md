---
sessionId: session-261117-125432-7h0d
---

# Requirements

## テスト方針

テスト先行で、各ファイルのテストを先に記述し、その後で実装を行う。

### テストケース一覧

#### `useIsVisible.test.ts`（新規）
1. **初期値が true である** — マウント直後は `isVisible === true`
2. **hidden に変更すると false になる** — `document.visibilityState` を `'hidden'` に変更すると `isVisible === false`
3. **visible に変更すると true になる** — `'hidden'` から `'visible'` に戻すと `isVisible === true`
4. **アンマウント時にイベントリスナーが削除される** — 警告なしでアンマウント

#### `useAutoRefresh.test.ts`（新規）
1. **指定間隔でコールバックが実行される** — 30秒ごとにコールバックが呼び出される
2. **shouldSkip が true の場合はスキップされる** — スキップ条件がtrueの周期はコールバックが呼ばれない
3. **isVisible が false の場合はスキップされる** — タブが非表示の周期はスキップ
4. **アンマウント時にインターバルがクリアされる** — メモリリークしない
5. **コールバックがエラーを投げても次の周期は実行される** — エラー回復

#### `BacklogItemList.test.tsx`（追加）
1. **自動リフレッシュで getBacklogItemsByTeam が定期的に呼び出される** — タイマー進行で2回目以降の呼び出しを確認
2. **formOpen=true の場合はリフレッシュがスキップされる** — 追加フォーム開いている間は呼ばれない
3. **confirmTarget が設定されている場合はリフレッシュがスキップされる** — 確認ダイアログ中は呼ばれない
4. **状態フィルタがリフレッシュ後も維持される** — フィルタ変更後のリフレッシュで同じフィルタが適用される

#### `BacklogItemDetail.test.tsx`（追加）
1. **自動リフレッシュで getBacklogItem が定期的に呼び出される** — タイマー進行で2回目以降の呼び出しを確認
2. **コメント入力中はリフレッシュがスキップされる** — `commentBody` が空でない場合は呼ばれない
3. **Value入力中はリフレッシュがスキップされる** — `valueInput` が `item.value` と異なる場合は呼ばれない
4. **入力がない場合はリフレッシュが実行される** — 両方とも入力されていない場合は呼ばれる

### テスト手法
- Jest の `useFakeTimers()` でタイマーをモック
- `act(() => { jest.advanceTimersByTime(30000) })` でタイマー進行
- `document.visibilityState` のモックは `Object.defineProperty` で上書き
- 既存のテストパターン（service モック、localStorage モック）を継承


# Delivery Steps

###   Step 1: Create plan document for Issue #594
Create the implementation plan document at `.junie/plans/issue-594-plan.md`.

- Write the complete plan covering all requirements, technical design, and testing strategy
- Include file list and change descriptions for each affected file
- Document design decisions: 30s refresh interval, input suppression, filter preservation, tab visibility
- List all test cases for test-first approach
- Save to `.junie/plans/issue-594-plan.md`