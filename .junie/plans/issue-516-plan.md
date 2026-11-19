---
sessionId: session-261119-021708-nli1
---

# Testing

## Validation Approach

- 各ステージ完了後に `cd frontend && npm test` でテストが通るか確認
- 最終ステージで `npm run build` で本番ビルドが通るか確認

## Key Scenarios

### StoryPoint選択UI（詳細画面）
1. StoryPointラジオボタンを表示したとき、すべての許容値（0〜89）が選択肢として表示される
2. 各StoryPoint値に説明テキストが付いている
3. アイテムの既存StoryPoint値が選択された状態で表示される
4. StoryPoint値を選択して保存したとき、`estimateBacklogItem` APIが呼び出される
5. 保存成功后、アイテムが再取得され、状態変化（READY → ESTIMATED）が画面に反映される
6. APIエラー時、エラーメッセージが表示される
7. 既存のValue入力セクション（スライダー+TextField）は維持されたまま、StoryPoint選択UIは独立した新しいセクションとして追加される

### 一覧画面
7. 一覧画面を表示したとき、StoryPoint列が表示される
8. StoryPoint値があるアイテムでは値が表示される
9. StoryPoint値がないアイテムでは「-」または空表示
10. StoryPoint合計がテーブル上部に表示される
11. 合計は表示中アイテムのStoryPointの合計と一致する
12. 状態フィルタを変更したとき、合計も更新される

### エッジケース
13. StoryPointがnullのアイテムの詳細画面でラジオボタンが未選択状態
14. 複数のアイテムが表示されているがすべてStoryPointがnullのとき、合計は0または「-」
15. 通信エラー時に適切なエラーメッセージが表示される

## Test List

### StoryPoint選択UI（詳細画面）
1. 詳細画面を表示したとき、StoryPointラジオボタングループが表示となる
2. すべての許容値（0, 1, 2, 3, 5, 8, 13, 21, 34, 55, 89）が選択肢として表示となる
3. 各StoryPoint値に説明テキストが付随して表示となる
4. StoryPoint値が設定済みのアイテムを表示したとき、該当値が選択された状態となる
5. StoryPoint値が未設定のアイテムを表示したとき、どのラジオボタンも選択されていない状態となる
6. StoryPoint値を選択して保存ボタンをクリックしたとき、`estimateBacklogItem` APIが呼び出される
7. 保存成功后、成功メッセージが表示となる
8. 保存成功后、アイテムが再取得され、選択されたStoryPoint値が反映される
9. 保存成功后、READY から ESTIMATED への状態変化がステータス表示に反映される
10. APIエラーが返されたとき、エラーメッセージが表示となる
11. StoryPoint 0を選択したとき、「見積もり不要」の説明テキストが表示となる
12. StoryPoint 89を選択したとき、「最大規模（分割を検討）」の説明テキストが表示となる
13. ステータスが READY または ESTIMATED のときのみ、見積もりセクションが表示される
14. ステータスが READY 以外（SUGGESTED、ACCEPTED、FROZENなど）のとき、見積もりセクションは非表示となる
15. ESTIMATED 状態ではStoryPoint値を変更して保存できる（状態遷移なし、値の更新のみ）
16. 既存のValue入力セクション（スライダー+TextField）はStoryPointセクションとは別に維持される
17. 見積もり操作はチームメンバーのみが実行可能（frontendでは`ScrumTeamResponse.memberEmployeeIds`と`auth.employeeId`を比較して判定可能）

### 一覧画面
12. 一覧画面を表示したとき、StoryPoint列のヘッダーが表示となる
13. StoryPoint値があるアイテムでは値がCellに表示される
14. StoryPoint値がnullのアイテムでは「-」が表示される
15. StoryPoint合計がテーブル上部に表示される
16. 表示中アイテムのStoryPoint合計が正しい値となる
17. 状態フィルタを変更したとき、合計がフィルタ後のアイテムで再計算される
18. 表示中アイテムが0件のとき、合計は0または非表示となる
19. すべてのアイテムがStoryPoint未設定のとき、合計は0または「-」となる

### サービス層
20. `estimateBacklogItem` を呼び出したとき、正しいAPIエンドポイントにリクエストが送信される
21. `estimateBacklogItem` のレスポンスが `BacklogItem` 型として返される

### 回帰テスト
22. 既存のコメント機能が引き続き動作すること
23. 既存のValue列（品質評価用）が引き続き表示される
24. 既存の状態フィルタが引き続き動作すること

## Test Changes

| ファイル | 変更 | 内容 |
|---------|------|------|
| `BacklogItemDetail.test.tsx` | 修正 | StoryPoint選択テストを追加、Value入力テストは維持、コメント関連テストは維持 |
| `BacklogItemList.test.tsx` | 修正 | StoryPoint列・合計表示のテスト追加、既存テスト維持 |
| `backlogItemService.test.ts` | 修正 | `estimateBacklogItem` のテスト追加 |

## Build Verification
- 最終ステージで `cd frontend && npm run build` を実行し、本番ビルドが成功することを確認

# Delivery Steps

###   Step 1: StoryPoint定数とサービス層の拡張
StoryPoint定数ファイルを作成し、backlogItemServiceにstoryPointフィールドとestimateBacklogItemメソッドを追加。

- `frontend/src/utils/storyPoints.ts` を新規作成（`STORY_POINT_VALUES`、`STORY_POINT_DESCRIPTIONS`、`getStoryPointDescription`）
- `backlogItemService.ts` の `BacklogItem` インターフェースに `storyPoint?: number` を追加
- `EstimateBacklogItemRequest` インターフェースを追加
- `estimateBacklogItem(backlogItemId, request)` メソッドを追加（`PATCH /v1/backlog-items/{id}/estimate`）
- `backlogItemService.test.ts` に `estimateBacklogItem` のテストを追加
- `cd frontend && npm test` でテストが通るか確認

###   Step 2: 詳細画面のStoryPoint選択UI実装
BacklogItemDetailにStoryPointラジオボタン選択UIを**独立した新しいセクションとして追加**する。既存のValue入力セクション（スライダー+TextField）は維持する。

- `BacklogItemDetail.tsx` にValue入力セクションの後にStoryPointセクションを追加（既存のValue入力セクションはそのまま維持）
- 表示条件：ステータスが `READY` または `ESTIMATED` のときのみ見積もりセクションを表示
- `BacklogItemStatus` 型に `READY` と `ESTIMATED` を追加
- `storyPoints.ts` の定数を参照してラジオボタンを生成
- 各ラジオボタンに説明テキストを併記
- 既存の `item.storyPoint` を初期選択値として使用
- 保存ボタンをクリック時に `estimateBacklogItem` APIを呼び出し（`actorEmployeeId` は `auth.employeeId` から取得）
- 保存成功后、アイテムを再取得（`load()` を再実行）し、READY から ESTIMATED への状態変化が画面に反映されるようにする
- 成功/エラーメッセージの表示
- チームメンバー判定：`ScrumTeamResponse.memberEmployeeIds` と `auth.employeeId` を比較して、チームメンバーのみが操作可能であることをfrontend側でも表示制御する（#573の方針：frontendは表示制御のみ、最終判定はbackendが担保）
- `BacklogItemDetail.test.tsx` にStoryPoint選択テストを追加（Value入力テストは維持、コメント関連テストは維持）
- `cd frontend && npm test` でテストが通るか確認

###   Step 3: 一覧画面のStoryPoint列と合計表示
BacklogItemListにStoryPoint列を追加し、表示中アイテムのStoryPoint合計を表示。

- `BacklogItemList.tsx` のテーブルにStoryPoint列を追加（Value列の隣）
- StoryPoint値のChip表示（または色付き表示）
- テーブル上部に「StoryPoint合計: X」のチップまたはTypographyを表示
- 合計は `items.reduce()` で表示中アイテムの `storyPoint` を集計（nullは0として扱う）
- 状態フィルタ変更時に合計が再計算されることを確認
- `BacklogItemList.test.tsx` にStoryPoint列と合計表示のテストを追加
- `cd frontend && npm test` でテストが通るか確認
- `cd frontend && npm run build` で本番ビルドが成功するか確認