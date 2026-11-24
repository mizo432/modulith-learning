# Issue #522 計画：ReadyのバックログアイテムをAcceptedに戻す

対象：#522（親）／#523（backend）／#524（frontend）。1つのPRで3件を閉じる。
ブランチ：modulith-learning-522（origin/develop = b6451f3f4、PR #742 マージ時点）

## 決定事項（2026-11-25、ユーザー決定・Issue記録済み）

- 権限：Ready → Accepted の逆遷移は **チームメンバー全員（PO・SMを含む）** が実行できる（#717「遷移はすべてチームメンバー全員にする」に従う。#522/#523/#524 本文の「開発者のみ・PO/SM不可」は置き換え）
- ストーリーポイントはクリアして **null** にする（未見積もりに戻り、再度見積もり可能になる）
- 遷移は既存の `PATCH /api/v1/backlog-items/{id}/status` を使う。新規エンドポイントは作らない
- 履歴は `BacklogItemStatusChanged` イベントで担保する（#572）。状態遷移の通知は行わない（#633 の対象はコメント）
- 同時操作の競合は楽観的ロック（#675 で導入済み）で処理する（#623 の方針）
- Value（優先度）は戻りに伴い変更しない

## 設計

### Backend（#523）

- `BacklogItem.changeStatus`（spi）：READY の分岐に ACCEPTED を追加する。遷移時に status=ACCEPTED、storyPoint=null、updatedAt=現在時刻。クラスJavadocの遷移一覧にも追加する
- `ChangeBacklogItemStatusCommandService`（internal）：権限分岐に `READY → ACCEPTED` を追加し、`ScrumTeamSubset.isTeamMember` で判定する（既存の READY→SUGGESTED 等と同じ形）。拒否時は `BacklogItemPermissionDeniedException`
- イベントは既存どおり `BacklogItemStatusChanged`（from=READY, to=ACCEPTED）を発行する

### Frontend（#524）

- `BacklogItemDetail.tsx`：`isTeamMember && item.status === 'READY'` のとき「Acceptedに戻す」ボタンを表示する（ACCEPTED→READY ボタンと同じ並び）
- ボタン押下で確認ダイアログを表示する。文言：ストーリーポイントがクリアされることを警告する
- 確定で `backlogItemService.changeStatus(id, 'ACCEPTED', actorEmployeeId)` を呼び、成功時は詳細を再読み込みし成功メッセージを表示する。失敗時はエラーメッセージを表示する（いずれも Ready 遷移と同じ successMessage／error の仕組みを使う）
- サービス層の変更は不要（changeStatus は既存）

### RDRA成果物の更新

- `ステートマシン図_バックログ項目.md`：遷移 `READY --> ACCEPTED`（チームメンバー全員が実行／StoryPointはクリアされる）を追加し、状態一覧の「準備完了」の遷移先と「受入済み」の遷移元を更新する
- `UC-BL-032 バックログ項目の状態を変更する.md`：状態遷移ルール表に行を追加し、権限エラーの代替フロー列挙に READY→ACCEPTED を含める

## テストリスト

### Backend エンティティ（BacklogItemStatusReadyTest に追加）

1. Readyでストーリーポイントが設定されているアイテムをAcceptedへ遷移させたとき、状態がAcceptedとなりストーリーポイントがnullとなる
2. Readyでストーリーポイントが未設定のアイテムをAcceptedへ遷移させたとき、状態がAcceptedとなりストーリーポイントはnullのままとなる
3. ReadyのアイテムをAcceptedへ遷移させたとき、Valueは変更されない
4. ESTIMATEDのアイテムをAcceptedへ遷移させたとき、IllegalStateExceptionとなる

### Backend サービス（ChangeBacklogItemStatusCommandServiceAcceptedTest を新規作成）

5. 一般メンバーがReadyのアイテムをAcceptedへ戻したとき、状態がAcceptedになりストーリーポイントがクリアされ、BacklogItemStatusChangedイベント（READY→ACCEPTED）が発行される
6. POがReadyのアイテムをAcceptedへ戻したとき、遷移が成功する
7. SMがReadyのアイテムをAcceptedへ戻したとき、遷移が成功する
8. チームメンバーでない従業員がReadyのアイテムをAcceptedへ戻そうとしたとき、BacklogItemPermissionDeniedExceptionとなり、状態とストーリーポイントは変わらない

### Frontend（BacklogItemDetail.test.tsx に追加）

9. Ready状態のアイテムをチームメンバーが表示したとき、「Acceptedに戻す」ボタンが表示される
10. Ready状態のアイテムをチームメンバーでないユーザーが表示したとき、「Acceptedに戻す」ボタンが表示されない
11. 「Acceptedに戻す」ボタンを押したとき、ストーリーポイントがクリアされる旨の確認ダイアログが表示される
12. 確認ダイアログで確定したとき、状態変更APIがACCEPTEDで呼ばれ、成功後に詳細が再読み込みされる
13. 確認ダイアログでキャンセルしたとき、状態変更APIは呼ばれない
14. 状態変更APIが失敗したとき、エラーメッセージが表示される

## 段階

1. Backend：テスト作成（Red確認）→ 実装（Green確認）
2. Frontend：テスト作成（Red確認）→ 実装（Green確認）
3. RDRA更新、全量検証（backend small／frontend test＋npmBuild）、レビュー、コミット（docs／test／feat分割）、PR作成（Closes #522, #523, #524）、CI確認
