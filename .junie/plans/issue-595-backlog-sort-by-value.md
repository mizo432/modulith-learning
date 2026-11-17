---
sessionId: session-261117-132655-1y0r
---

# Requirements

## Overview & Goals

#595「バックログアイテム一覧の既定のソートをValue（優先度）順にする」の実装計画。
親Issue #571「一覧の既定のソート順序は優先度順とする」の実装タスク。

バックログアイテム一覧を **Value 降順**（Value が高い＝優先度が高い方が上）に既定で並べる。状態フィルタ適用時もフィルタ内でValue降順を維持。#594で導入された30秒自動リフレッシュ後も順序が維持される。

## Scope

### In Scope
- バックログアイテム一覧の既定ソートをValue降順にすること
- Valueが同値の場合の第2ソート（安定した順序）の決定と実装
- Valueがnullのアイテムの位置の決定と実装
- ソートの実施場所の決定と実装
- 状態フィルタ適用時もソートが維持されること
- 自動リフレッシュ後もソートが維持されること
- 関連テストの追加

### Out of Scope
- 並び替え操作（ユーザーが並び順を変更するUI）の追加
- 並び順という独立した属性の追加
- バックログアイテム詳細ページのソート
- スプリントバックログのソート

## User Stories

- As a PO/SM, I want backlog items to be sorted by Value in descending order by default, so that I can see the highest-priority items first.
- As a user, I want the sort order to be preserved when I apply a status filter, so that the list remains meaningful.
- As a user, I want the sort order to be preserved after auto-refresh, so that the view is stable.

## Functional Requirements

1. **既定ソートはValue降順** — Valueが高いアイテムがリストの上部に表示される
2. **Value同値の第2ソート** — Valueが同じ場合は作成日降順（新しい方が上）
3. **Value nullの位置** — Valueがnullのアイテムはリストの末尾に配置
4. **状態フィルタ適用時もソート維持** — フィルタ変更後もValue降順を維持
5. **自動リフレッシュ後もソート維持** — 30秒リフレッシュ後も順序が維持


# Testing

## テスト方針

テスト先行で、各ファイルのテストを先に記述し、その後で実装を行う。

### Backend テストケース

#### `BacklogItemQueryServiceTest.java`（新規）

1. **Value降順でソートされる** — Valueが100, 50, 10の3アイテムが100→50→10の順で返される
2. **Value同値の場合、createdAt降順** — Valueが同じ2アイテムで新しい方が先
3. **Value nullのアイテムは末尾** — Valueありとnullの混在でnullが最後
4. **状態フィルタ適用時もソート維持** — ACCEPTEDフィルタでValue降順が維持される
5. **アイテムがない場合は空リスト** — フィルタに一致するアイテムなしで空リスト

**テスト手法:**
- Mockitoでリポジトリをモック
- 既存の`CreateBacklogItemCommandServiceTest`のパターンに従う（`@ExtendWith(MockitoExtension.class)`、`@Tag("small")`）
- `BacklogItem.builder()`でテストデータを作成

### Frontend テストケース

#### `BacklogItemList.test.tsx`（追加）

1. **Value降順でアイテムが表示される** — APIからValue順のデータが返され、その順でレンダリングされる
2. **Value nullのアイテムが末尾に表示される** — Valueありとnullの混在でnullが最後
3. **状態フィルタ変更後もソート順が維持される** — フィルタ変更後の再読み込みでValue降順
4. **自動リフレッシュ後もソート順が維持される** — 30秒リフレッシュ後も順序が維持

**テスト手法:**
- 既存のテストパターン（serviceモック、localStorageモック）を継承
- `jest.useFakeTimers()`で自動リフレッシュの検証
- モックの`getBacklogItemsByTeam`の戻り値の順序を確認

### エッジケース

1. **全アイテムがValue null** — すべて末尾（createdAt降順のみでソート）
2. **全アイテムがValue同値** — createdAt降順でソート
3. **単一アイテム** — ソート影響なし
4. **空リスト** — 空リストが返される
