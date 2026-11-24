# Issue #525 実装計画: スプリント割り当て

## 前提（決定済み）
- #650: 未来4スプリント = 進行中スプリント + 次の3スプリント（進行中がなければ最も早い計画中から4つ）
- #651: スプリントの期間は必ず設定される前提（未確定スプリントは存在しない）
- #653: 期間変更・キャンセル時は次スプリントへスライド。うちキャンセル分を本Issueで実装し、期間変更分は #736 で範囲を決めてから実装する
- #717: 割り当ては状態を変えない（ESTIMATEDのまま）。開始操作が IN_PROGRESS への遷移を持つ
- #620/#717: 権限はチームメンバー全員（PO/SMを含む）。Issue本文の「開発者、PO、SMは割り当てができない」は後発の決定で上書きされたものとして扱う
- #633/#679: 通知はイベント駆動（アプリ内＋メール、受信者は操作者を除くチームメンバー）
- 監査ログは #572 の決定に倣い専用実装なし。同時割当の競合は #623 の方針（楽観ロック）に倣う

## 設計判断（本計画で確定）
1. 割り当ての実体は `BacklogItem.sprintId` とする。`SprintBacklogItem` はスプリント内のタスク分解用であり、本Issueでは作成しない
2. 割り当て可能スプリントの窓は sprintExecution の spi ファサード `SprintFacade.findAssignableSprints(teamId)` が計算する。スプリント0・完了・キャンセル済みは窓に含めない
3. 割り当て/変更/解除は ESTIMATED のアイテムにのみ可能。変更は同じ POST で行い、同じスプリントへの再割り当ては冪等に成功とする
4. 変更・解除時は `BacklogItemSprintAssignmentChanged` イベントを発行し、notification モジュールが受信する
5. キャンセル時のスライドは sprintExecution が `SprintCancelled` イベントを発行し、requirementBacklog が受けて次スプリント（同チーム・番号が大きい最小のスプリント0でないもの）へ移す。次がなければ割り当て解除。スプリント削除時も同じイベントで扱う

## 段階
- 段階1: sprintExecution ファサード（SprintFacade / SprintFacadeImpl、窓計算・次スプリント取得）
- 段階2: requirementBacklog 割り当てコマンド/クエリとエンドポイント（POST/DELETE /api/v1/backlog-items/{id}/sprint-assignment、GET /{id}/assignable-sprints）
- 段階3: 通知（イベント + notification モジュールのリスナー + NotificationType 追加）
- 段階4: キャンセル時のスライド（SprintCancelled イベント + requirementBacklog リスナー）
- 段階5: フロントエンド（詳細画面のスプリント割り当てUI）
- 段階6: 統合検証

## テストリスト
### 段階1（small: SprintFacadeImplTest）
- 進行中スプリントがあるとき、進行中と次の3つの計画中スプリントが割り当て可能として返る
- 進行中スプリントがないとき、最も早い計画中スプリントから4つが返る
- スプリント0があるとき、割り当て可能一覧に含まれない
- 完了・キャンセル済みスプリントがあるとき、割り当て可能一覧に含まれない
- 割り当て可能なスプリントが4つ未満のとき、存在する分だけ返る
- 次スプリントを取得したとき、指定番号より大きい最小番号のスプリント（スプリント0でない）が返り、ないときは空が返る

### 段階2（small: AssignBacklogItemSprintCommandServiceTest / コントローラテスト）
- チームメンバーがESTIMATEDのアイテムを窓内のスプリントに割り当てたとき、sprintIdが設定されイベントが発行される
- チームメンバーでない者が割り当てたとき、権限例外となりsprintIdは変わらない
- ESTIMATEDでないアイテムを割り当てたとき、状態例外となり割り当てられない
- 窓の外のスプリントに割り当てたとき、引数例外となり割り当てられない
- 他チームのスプリントに割り当てたとき、引数例外となり割り当てられない
- スプリント0に割り当てたとき、引数例外となり割り当てられない
- 割り当て済みアイテムを別のスプリントに変更したとき、sprintIdが新しいスプリントになりイベントに前スプリントが含まれる
- 割り当てを解除したとき、sprintIdがnullになりイベントが発行される
- 割り当てがないアイテムを解除したとき、状態例外となる
- POST sprint-assignment を呼んだとき、200でsprintIdを含む応答が返る
- DELETE sprint-assignment を呼んだとき、200が返る
- GET assignable-sprints を呼んだとき、窓内のスプリント一覧（現在の割り当て先を含む）が返る

### 段階2（medium: BacklogItemSprintAssignmentEndpointMediumTest）
- スプリントを作成してアイテムを割り当てたとき、詳細取得でsprintIdが返る
- 窓の外のスプリントに割り当てたとき、400が返る

### 段階3（small: BacklogItemSprintAssignmentEventListenerTest）
- 割り当て変更イベントを受け取ったとき、操作者を除くチームメンバー全員に通知が作られる
- 操作者のみのとき、通知は作られない
- メールアドレスがある受信者にはメールが送られる

### 段階4（small: SprintCancelledSlideTest / medium）
- スプリントがキャンセルされたとき、そのスプリントのアイテムが次スプリントにスライドする
- 次スプリントがないとき、アイテムの割り当てが解除される
- 他スプリントのアイテムはスライドしない
- ステータス変更APIでスプリントをキャンセルしたとき、割り当て済みアイテムのsprintIdが次スプリントになる（medium）

### 段階5（frontend）
- ESTIMATEDかつチームメンバーのとき、スプリント割り当てUIが表示される
- チームメンバーでないとき、割り当て操作は表示されない
- スプリントを選んで割り当てたとき、assignSprintが呼ばれ再表示される
- 解除したとき、unassignSprintが呼ばれる
- サービスの呼び出し形式（URL・パラメータ）がAPIと一致する
