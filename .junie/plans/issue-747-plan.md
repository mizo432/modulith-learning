# Issue #747 計画：システム管理者はジョブ定義からバッチ処理を任意のタイミングで起動できる

親 #747／backend #748／frontend #749。決定事項は #747 の決定コメントと RDRA JB-01（UC-JB-008、SCR-JB-012、アクター一覧）参照。

## 設計概要

### backend（undecided.jobmanagement）

- permissionmanagement：AdminEmployeeSubset（job_management スキーマの射影テーブル＋Flyway 追加）、AdminEmployeeSubsetEventListener（@EventListener、requirementBacklog と同方式）、AdminEmployeeSubsetRepository を追加
- jobdefinition：定義一覧の取得を追加（JobDefinitionQueryService 相当＋GET /api/job-definitions）。レスポンスは jobCode、version、parameterSpec、maxConcurrentExecutions、timeoutSeconds、isActive
- jobexecution：
  - JobExecutionDTO に actorEmployeeId、parameters（key=value、1行1件）を追加
  - StartJobCommandService を拡張：ADMIN判定（403）→ 定義取得（なければ404）→ 有効判定（無効は409）→ Job解決（ジョブコード＝Job名、解決できなければ409）→ 同時実行判定（PENDING/RUNNING件数が上限以上で409、上限は定義の maxConcurrentExecutions、未設定時は1）→ 実行ID重複判定（409）→ パラメータ検証（形式不正は400）→ PENDINGで保存し EVT-JB-007 相当のイベント発行 → ランナーへ委譲
  - JobExecutionRunner（@Async）：RUNNING へ更新 → JobOperator.start(job, parameters + timestamp) → 終了状態で SUCCESS／FAILED（失敗時は errorMessage、完了時は completedAt）を更新
  - 実行一覧の取得を追加（GET /api/job-executions、新しい順）
  - 例外は jobmanagement 内の ExceptionHandler で 400／403／404／409 に振り分け（既存の Map message 形式。RFC 9457 化は #744 で別途）
- 時刻取得は LocalDateTime.now() の既存踏襲（Clock 化は #745 の範囲）

### frontend

- services/jobService.ts：定義一覧取得、起動、実行一覧取得
- pages/JobLaunchPage.tsx（SCR-JB-012）：定義の選択（ドロップダウン）、ParameterSpec の表示、パラメータ入力（テキストエリア）、起動ボタン、実行一覧（状態ラベル付き）
- App.tsx にルート追加。メニュー表示とルートは isAdmin のときのみ
- 起動時は AuthContext の employeeId を actorEmployeeId として送る

## テストリスト

### backend

1. ADMIN保持者が起動したとき、実行レコードがPENDINGで保存され、ランナーに実行が委ねられる
2. ADMIN保持者でない者が起動したとき、権限例外となり、レコードは保存されない
3. 存在しないジョブコードで起動したとき、定義なし例外となる
4. 無効化された定義で起動したとき、競合例外となる
5. 対応するバッチJobが存在しない定義で起動したとき、競合例外となる
6. 同一ジョブの待機中・実行中件数が上限に達しているとき、起動は競合例外で拒否される
7. maxConcurrentExecutionsが未設定のとき、上限は1として判定される
8. 同じ実行IDで再度起動したとき、競合例外で拒否される
9. パラメータに key=value 形式でない行があるとき、形式例外となる
10. パラメータが JobParameters に変換されてランナーへ渡される
11. ランナーがバッチを正常終了させたとき、実行レコードはRUNNINGを経てSUCCESSになる
12. ランナーがバッチを異常終了させたとき、実行レコードはFAILEDになりエラー情報が記録される
13. AdminEmployeeChanged（付与）を受信したときサブセットに登録され、（剥離）を受信したとき削除される
14. 定義一覧を取得したとき、登録済みの定義が返る
15. 実行一覧を取得したとき、新しい順に実行が返る

### frontend

16. 管理者でログインしているとき、ジョブ手動起動のメニューが表示される。管理者でないときは表示されない
17. 定義一覧が表示され、ジョブを選択したとき ParameterSpec が表示される
18. 起動ボタンを押したとき、選択したジョブコード・パラメータ・actorEmployeeId で起動APIが呼ばれる
19. 起動に成功したとき、成功メッセージが表示され実行一覧が更新される
20. 起動に失敗したとき、エラーメッセージが表示される
21. 実行一覧に状態がラベル付きで表示される

## 検証

- backend：対象テスト → small 全量（--no-build-cache、XML集計）→ medium 全量
- frontend：npm test 全量 → npmBuild
- RDRA：UC-JB-008、SCR-JB-012、アクター一覧を更新済み（決定の反映）
