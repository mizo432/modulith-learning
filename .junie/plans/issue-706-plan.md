---
sessionId: session-261120-160546-1smx
---

# Requirements

## 概要

PR #696（#679 コメント通知）の CodeRabbit レビュー指摘対応。通知周りの不具合8件を最小修正で修正する。

## 対応項目

### Backend
1. **CommentParticipantSyncListener の重複登録修正** — コメント追加のたびに無条件で参加者を保存し、UNIQUE 制約に違反する不具合を修正
2. **notifications テーブルの複合インデックス追加** — 未読取得クエリ用の Flyway マイグレーション
3. **NotificationModuleBoundaryLargeTest に modules.verify() 追加** — 境界検証が機能していない問題を修正

### Frontend
4. **NotificationSnackbar の close 伝播修正** — 閉じるボタンクリックが詳細遷移に伝播する問題を stopPropagation で修正
5. **NotificationSnackbar のキュー管理簡素化** — queue と currentIndex の二重管理をやめ、先頭通知を表示する方式へ変更
6. **Snackbar の key 設定とタイマーリセット** — 通知IDを key にし、自動消去タイマーを通知ごとにリセット
7. **useNotifications のリクエスト世代管理** — 古い取得結果の上書きと既読済み通知の復活を防止
8. **notificationService の共有 API クライアント利用** — 直接 fetch をやめ、認証ヘッダー・401処理付きの共有クライアントへ変更

## テスト方針
- バックエンド: Spring Boot に依存しない `small` タグを基本、マイグレーション検証は `medium` タグ
- フロントエンド: 既存テスト（NotificationSnackbar.test.tsx、useNotifications.test.ts、notificationService.test.ts）の変更を明記
- 各テスト項目は「〜したとき、〜となる」の一文形式


# Technical Design

## (1) CommentParticipantSyncListener の重複登録修正

### 現状
`CommentParticipantSyncListener.onCommentAdded()` は、イベント受信のたびに `commentParticipantSubsetRepository.save()` を無条件で実行。`comment_participants` テーブルに `UNIQUE (backlog_item_id, employee_id)` 制約があるため、同じ投稿者の2回目以降のコメントで必ず失敗する。

### 変更内容
- **NotificationCommentParticipantSubsetRepository** に `existsByBacklogItemIdAndEmployeeId(SnowflakeId, SnowflakeId)` メソッドを追加
- **CommentParticipantSyncListener.onCommentAdded()** で保存前に存在確認し、登録済みなら保存しない

### 影響ファイル
- `backend/src/main/java/undecided/notification/internal/NotificationCommentParticipantSubsetRepository.java`（修正）
- `backend/src/main/java/undecided/notification/internal/CommentParticipantSyncListener.java`（修正）

### テスト
- **新規: CommentParticipantSyncListenerTest**（small タグ）
  - 参加者が未登録のとき、イベントを受信すると参加者が保存される
  - 参加者が既に登録済みのとき、イベントを受信すると保存されない
  - 異なる投稿者のイベントを受信すると、それぞれ独立して参加者が保存される
- **既存テスト変更: NotificationIntegrationMediumTest** — 投稿者の参加者同期が重複しないことを確認するテストを追加

## (2) notifications テーブルの複合インデックス追加

### 現状
`notifications` テーブルに `recipient_id, is_read, created_at DESC` の複合インデックスがない。`findByRecipientIdAndIsReadFalseOrderByCreatedAtDesc()` のクエリがフルスキャンになる。

### 変更内容
- Flyway マイグレーション `V20261120100002__add_notifications_index.sql` を追加
- `CREATE INDEX idx_notifications_recipient_read_created ON undecided_nt.notifications(recipient_id, is_read, created_at DESC);`

### 影響ファイル
- `backend/src/main/resources/db/migration/nt/V20261120100002__add_notifications_index.sql`（新規）

### テスト
- **既存テスト変更: NotificationIntegrationMediumTest** — インデックス追加後の未読取得クエリが正常に動作することを確認（既存のテストがカバーするため追加テストは不要。medium テストでマイグレーションが適用されることを確認）

## (3) NotificationModuleBoundaryLargeTest に modules.verify() 追加

### 現状
`NotificationModuleBoundaryLargeTest` は `modules.toString()` の文字列チェックのみで、`modules.verify()` を呼び出していない。境界検証になっていない。

### 変更内容
- `modules.verify()` を追加。`ModulithTest.verifyPackageConformity()` のパターンに従う

### 影響ファイル
- `backend/src/test/java/undecided/notification/NotificationModuleBoundaryLargeTest.java`（修正）

### テスト
- 既存テストの修正のみ。`modules.verify()` が例外を投げないことを確認

## (4) NotificationSnackbar の close 伝播修正

### 現状
`NotificationSnackbar.tsx` の外側 `div` に `onClick={handleClick}` が設定されており、Alert の `onClose` ボタンクリックが外側 div の onClick に伝播。閉じるつもりが詳細画面へ遷移する。

### 変更内容
- `handleClose` で `event.stopPropagation()` を呼び出す
- Alert の `onClose` にイベントを受け取るハンドラを設定

### 影響ファイル
- `frontend/src/components/NotificationSnackbar.tsx`（修正）

### テスト
- **既存テスト変更: NotificationSnackbar.test.tsx** — 「スナックバーを閉じたとき、通知が既読になる」のテストで、閉じるボタンクリックが詳細遷移しないことを確認するテストを追加

## (5) NotificationSnackbar のキュー管理簡素化

### 現状
`queue` と `currentIndex` の二重管理で、通知の読み飛ばしや表示停止が起こりうる。

### 変更内容
- `queue` と `currentIndex` を削除
- `notifications` の先頭（`notifications[0]`）を表示する方式に変更
- 通知を閉じたとき、先頭を削除して次の通知を表示
- `MAX_VISIBLE` は不要になる（一度に1件だけ表示）

### 影響ファイル
- `frontend/src/components/NotificationSnackbar.tsx`（修正）

### テスト
- **既存テスト変更: NotificationSnackbar.test.tsx** — 「3件以上の通知があるとき、最大3件まで表示される」のテストを「3件以上の通知があるとき、先頭の1件が表示される」へ更新

## (6) Snackbar の key 設定とタイマーリセット

### 現状
Snackbar に key が設定されておらず、通知が切り替わっても同じ Snackbar コンポーネントが再利用されるため、自動消去タイマーが通知ごとにリセットされない。

### 変更内容
- Snackbar に `key={currentNotification.id}` を設定
- 通知が切り替わるたびに Snackbar が再マウントされ、タイマーがリセットされる

### 影響ファイル
- `frontend/src/components/NotificationSnackbar.tsx`（修正）

### テスト
- **既存テスト変更: NotificationSnackbar.test.tsx** — 通知が切り替わったとき、自動消去タイマーがリセットされることを確認するテストを追加

## (7) useNotifications のリクエスト世代管理

### 現状
- 古い取得結果が新しい状態を上書きする可能性がある
- 既読化に成功後も進行中の取得が完了すると、既読済み通知が復活する

### 変更内容
- `requestGeneration` 状態を追加（`useRef` で管理）
- `fetchNotifications` でリクエスト時に世代をインクリメント
- 取得結果のセット前に世代が最新であることを確認
- `handleMarkAsRead` で既読化に成功したら、進行中の取得を無効化（世代をインクリメント）

### 影響ファイル
- `frontend/src/hooks/useNotifications.ts`（修正）

### テスト
- **既存テスト変更: useNotifications.test.ts** — 以下のテストを追加:
  - 古い取得結果が返ってきたとき、新しい状態が上書きされない
  - 既読化に成功したとき、進行中の取得結果が適用されない

## (8) notificationService の共有 API クライアント利用

### 現状
`notificationService.ts` が直接 `fetch()` を使用。認証ヘッダーの付与や 401 処理がない。

### 変更内容
- `api.ts`（axios ベースの共有クライアント）を利用する
- `getUnreadNotifications` と `markAsRead` を axios 呼び出しへ変更
- API パスの `baseURL` が `/api` を含むため、パスから `/api` を除去

### 影響ファイル
- `frontend/src/services/notificationService.ts`（修正）

### テスト
- **既存テスト変更: notificationService.test.ts** — `global.fetch` のモックを axios モックへ変更。api.ts のモックを使用してテスト


# Testing

## テストリスト

### Backend — (1) CommentParticipantSyncListener 修正

**新規: CommentParticipantSyncListenerTest（small）**
1. 参加者が未登録のとき、コメント追加イベントを受信すると、参加者が保存される
2. 参加者が既に登録済みのとき、コメント追加イベントを受信すると、保存されない
3. 異なる投稿者のコメント追加イベントを受信すると、それぞれ独立して参加者が保存される
4. 投稿者IDがnullのとき、コメント追加イベントを受信すると、保存されない

**既存変更: NotificationIntegrationMediumTest（medium）**
5. 同じ投稿者が同じアイテムに2回目のコメントを追加したとき、参加者レコードが重複しない

### Backend — (2) インデックス追加

**既存変更: NotificationIntegrationMediumTest（medium）**
6. インデックス追加後のマイグレーションが適用されたとき、未読取得クエリが正常に動作する

### Backend — (3) ModuleBoundary 修正

**既存変更: NotificationModuleBoundaryLargeTest（large）**
7. modules.verify() を実行したとき、例外が投げられない

### Frontend — (4) close 伝播修正

**既存変更: NotificationSnackbar.test.tsx**
8. 閉じるボタンをクリックしたとき、通知が既読になり、詳細画面へ遷移しない

### Frontend — (5) キュー管理簡素化

**既存変更: NotificationSnackbar.test.tsx**
9. 3件以上の通知があるとき、先頭の1件が表示される
10. 通知を閉じたとき、次の通知が表示される
11. 通知が1件もないとき、スナックバーが表示されない

### Frontend — (6) key 設定とタイマーリセット

**既存変更: NotificationSnackbar.test.tsx**
12. 通知が切り替わったとき、自動消去タイマーがリセットされる

### Frontend — (7) リクエスト世代管理

**既存変更: useNotifications.test.ts**
13. 古い取得結果が返ってきたとき、新しい状態が上書きされない
14. 既読化に成功したとき、進行中の取得結果が適用されない

### Frontend — (8) 共有 API クライアント利用

**既存変更: notificationService.test.ts**
15. 未読通知を取得したとき、共有 API クライアントが呼ばれる
16. 通知を既読にしたとき、共有 API クライアントが呼ばれる
17. API が401を返したとき、ログイン画面へ遷移する


# Delivery Steps

###   Step 1: Backend (1) CommentParticipantSyncListener の重複登録修正
リポジトリに存在確認メソッドを追加し、登録済みなら保存しないように修正する。
- NotificationCommentParticipantSubsetRepository に `existsByBacklogItemIdAndEmployeeId(SnowflakeId, SnowflakeId)` を追加
- CommentParticipantSyncListener.onCommentAdded() で保存前に存在確認し、登録済みなら保存しない
- CommentParticipantSyncListenerTest（small）を新規作成: 未登録時の保存、登録済み時のスキップ、異なる投稿者の独立動作、null投稿者の処理
- NotificationIntegrationMediumTest に2回目コメントの重複テストを追加

###   Step 2: Backend (2) notifications 複合インデックス + (3) ModuleBoundary 修正
Flyway マイグレーションで複合インデックスを追加し、モジュール境界検証に modules.verify() を追加する。
- V20261120100002__add_notifications_index.sql を新規作成（recipient_id, is_read, created_at DESC の複合インデックス）
- NotificationModuleBoundaryLargeTest に modules.verify() を追加
- NotificationIntegrationMediumTest でインデックス追加後の未読取得が正常動作することを確認

###   Step 3: Frontend (4)(5)(6) NotificationSnackbar の修正
close 伝播修正、キュー管理簡素化、key 設定とタイマーリセットを実装する。
- handleClose で stopPropagation() を呼び出し、Alert の onClose にイベントハンドラを設定
- queue と currentIndex を削除。notifications[0] を表示する方式へ変更
- Snackbar に key={currentNotification.id} を設定
- NotificationSnackbar.test.tsx を更新: close 伝播テスト追加、3件以上テストの更新、次の通知表示テスト、key によるタイマーリセットテスト追加

###   Step 4: Frontend (7) useNotifications のリクエスト世代管理
リクエスト世代管理を追加し、古い取得結果の上書きと既読済み通知の復活を防止する。
- useRef で requestGeneration を追加
- fetchNotifications でリクエスト時に世代インクリメント、結果セット前に世代確認
- handleMarkAsRead で既読化成功後に世代インクリメント
- useNotifications.test.ts にテスト追加: 古い結果の上書き防止、既読化後の取得無効化

###   Step 5: Frontend (8) notificationService の共有 API クライアント利用
直接 fetch をやめ、共有 api クライアント（axios）を利用する。
- notificationService.ts で api.ts（axios）を import
- getUnreadNotifications と markAsRead を axios 呼び出しへ変更
- API パスの /api プレフィックスを baseURL に合わせる
- notificationService.test.ts を更新: fetch モックを axios モックへ変更、401 遷移テスト追加