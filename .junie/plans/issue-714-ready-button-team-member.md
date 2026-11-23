---
sessionId: session-261121-182942-1owy
---

# Requirements

## Overview & Goals

バックログアイテム詳細画面（BacklogItemDetail）に「Ready」ボタンを追加し、ACCEPTED（承認済み）状態のアイテムをREADY（準備完了）へ遷移できるようにする。このボタンはログインユーザーが対象Scrumチームのメンバーのときのみ表示される。

## Scope

### In Scope
- BacklogItemDetail.tsx にACCEPTED状態のアイテム向けReadyボタンを追加
- ボタンの表示制御：`isTeamMember`（既存の`memberEmployeeIds`と`auth.employeeId`の突合）で判定
- Readyボタンクリック時に`changeStatus` APIを呼び出し、ACCEPTED → READYへ遷移
- 遷移成功后の再取得と画面反映
- 関連テストの追加
- 本番ビルド（CI=true）とスモークテスト

### Out of Scope
- バックエンドの変更なし
- BacklogItemList（一覧画面）の変更なし
- 他の状態遷移（SUGGESTED→ACCEPTEDなど）の変更なし


# Technical Design

## Current Implementation

- **BacklogItemDetail.tsx**（858行）：詳細画面。`isTeamMember`変数（147-150行目）が既にIssue #516で実装済み。StoryPoint保存ボタン（461-471行目）が`isTeamMember`で表示制御されるパターンがある。
- **backlogItemService.ts**：`changeStatus(backlogItemId, status, actorEmployeeId)` が既に存在（106-125行目）。`PATCH /v1/backlog-items/{id}/status` を呼び出す。
- **BacklogItemDetail.test.tsx**（1874行）：`changeStatus` はまだmockされていない。`estimateBacklogItem` のmock（54行目）を参考にする。
- **AuthContext**：`employeeId`を提供（useAuth経由）。
- **ScrumTeamService**：`memberEmployeeIds?: string[]`を含む`ScrumTeam`を返す。

## Key Decisions

1. **表示制御パターンをIssue #516に流用**：既存の`isTeamMember`変数（147-150行目）をそのまま使い、Readyボタンを`{isTeamMember && (...)}`でラップ。
2. **changeStatus APIを使用**：既存の`backlogItemService.changeStatus()`を呼び出し、`status: 'READY'`、`actorEmployeeId: auth.employeeId`を渡す。
3. **確認ダイアログなし**：StoryPoint保存ボタンと同様に即座に実行（確認ダイアログはBacklogItemListにあるが、詳細画面のStoryPointボタンにはないパターンに従う）。
4. **ACCEPTED状態のときのみ表示**：`item?.status === 'ACCEPTED'`の条件でReadyボタンを表示。

## Proposed Changes

### BacklogItemDetail.tsx
- `handleReady`関数を追加：`changeStatus(backlogItemId, 'READY', auth.employeeId)`を呼び出し、成功后に`load()`で再取得
- ACCEPTED状態のアイテムで、`isTeamMember`がtrueのときのみReadyボタンを表示
- ボタンはValue入力セクションの近くに配置（StoryPointセクションの手前、またはPaper内の適切な位置）
- 成功/エラーメッセージの表示を既存パターンに従う

### BacklogItemDetail.test.tsx
- `changeStatus`をmockに追加
- `mockBacklogItemAccepted`を作成（status: 'ACCEPTED'）
- Readyボタンの表示/非表示テストを追加
- Readyボタンクリック時のAPI呼び出しテストを追加


# Testing

## Validation Approach

- 各ステージ完了後に `cd frontend && npm test -- --watchAll=false` でテストが通るか確認
- 最終ステージで `CI=true cd frontend && npm run build` で本番ビルドが通るか確認
- Appの実描画スモークテスト（`npm start`で起動し、詳細画面でReadyボタンが正しく表示されるか確認）

## Test List

### Readyボタン表示制御
1. ステータスがACCEPTEDでチームメンバーの場合、Readyボタンが表示される
2. ステータスがACCEPTEDでチームメンバーではない場合、Readyボタンが表示されない
3. ステータスがACCEPTED以外（SUGGESTED）でチームメンバーの場合、Readyボタンが表示されない
4. ステータスがACCEPTED以外（FROZEN）でチームメンバーの場合、Readyボタンが表示されない
5. ステータスがACCEPTED以外（READY）でチームメンバーの場合、Readyボタンが表示されない
6. ステータスがACCEPTED以外（ESTIMATED）でチームメンバーの場合、Readyボタンが表示されない
7. employeeIdが設定されていない場合、Readyボタンが表示されない

### Readyボタン操作
8. Readyボタンをクリックしたとき、`changeStatus` APIが`READY`と`actorEmployeeId`で呼び出される
9. 状態遷移成功后、成功メッセージが表示される
10. 状態遷移成功后、アイテムが再取得され、ステータスがREADYに更新される
11. APIエラーが返されたとき、エラーメッセージが表示される

### 回帰テスト
12. 既存のValue入力機能が引き続き動作すること
13. 既存のStoryPoint選択UIが引き続き動作すること
14. 既存のコメント機能が引き続き動作すること

## Test Changes

| ファイル | 変更 | 内容 |
|---------|------|------|
| `BacklogItemDetail.test.tsx` | 修正 | `changeStatus`をmock追加、`mockBacklogItemAccepted`を追加、Readyボタン関連テストを追加 |

## Build Verification
- 最終ステージで `cd frontend && CI=true npm run build` を実行し、本番ビルドが成功することを確認


# Delivery Steps

###   Step 1: BacklogItemDetailにReadyボタンを追加
BacklogItemDetail.tsxにACCEPTED→READY遷移のReadyボタンを追加し、isTeamMemberで表示制御する。

- `handleReady`関数を追加：`backlogItemService.changeStatus(backlogItemId, 'READY', auth.employeeId ?? '')`を呼び出し、成功后に`load()`で再取得し成功メッセージを表示
- ACCEPTED状態かつ`isTeamMember`がtrueのときのみReadyボタンを表示（`{isTeamMember && item?.status === 'ACCEPTED' && (...)}`）
- ボタンをValue入力セクションの「Valueを保存」ボタンの隣、またはStoryPointセクションの手前に配置
- APIエラー時にエラーメッセージを表示（既存の`setError`パターンに従う）
- `BacklogItemDetail.test.tsx`のmockに`changeStatus`を追加
- `mockBacklogItemAccepted`（status: 'ACCEPTED'）を作成
- Readyボタン表示/非表示のテストケースを追加（チームメンバー/非メンバー、各ステータス）
- Readyボタンクリック時のAPI呼び出しと成功/エラーメッセージのテストを追加
- `cd frontend && npm test -- --watchAll=false` でテストが通るか確認

###   Step 2: 本番ビルドとスモークテスト
本番ビルドとAppの実描画スモークテストを実行し、変更が正しく動作することを確認。

- `cd frontend && CI=true npm run build` で本番ビルドが成功することを確認
- `cd frontend && npm start` でAppを起動し、詳細画面でReadyボタンが正しく表示されるかスモークテスト
- 既存のValue入力・StoryPoint選択・コメント機能が回帰していないことを確認
