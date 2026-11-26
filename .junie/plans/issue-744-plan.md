# Issue #744 計画：APIエラーレスポンスをRFC 9457（Problem Details）に統一する

## 実測した現状（2026-11-26、develop 30d0cd657）

- GlobalExceptionHandler は既に ProblemDetail 形式（業務例外400、バリデーションは fieldErrors 拡張メンバー、冪等性キー競合409、予期しない例外500＋ログ出力）。変更不要
- 独自形式（Map.of("message", ...)）のままなのはモジュール別ハンドラ4つ
  - requirementBacklog/internal/FeatureExceptionHandler（403/409/409/400/409）
  - requirementBacklog/internal/BacklogItemExceptionHandler（403/409/400/409）
  - documentMgmt/internal/DocumentExceptionHandler（400/404/403/409/400）
  - jobmanagement/jobexecution/internal/JobExecutionExceptionHandler（403/404/409/400、#747で追加）
- フロントエンドは err.response.data.message を約24箇所で参照（成功レスポンスの message は対象外）

## 方針

- 4ハンドラの返却を ProblemDetail（ProblemDetail.forStatusAndDetail(status, ex.getMessage())）へ統一する。ステータス対応は現行を維持する
- フロントエンドは互換メンバーを残さず detail へ追随する（ガイドライン「独自形式を作らない」に沿う）
- あわせて agent-guidelines サブモジュールのピンを 4bac647e から e2332b4（main 最新、MEMORY.md記録）へ進める

## テストリスト

1. BacklogItemの権限なし例外が発生したとき、403のProblemDetail（detail=例外メッセージ）が返ること
2. BacklogItemの不正な状態遷移（IllegalStateException）のとき、409のProblemDetailが返ること
3. BacklogItemの入力不正（IllegalArgumentException）のとき、400のProblemDetailが返ること
4. BacklogItemの楽観ロック失敗のとき、409のProblemDetailが返ること（ObjectOptimisticLockingFailureException等のサブクラスも対象）
5. Featureの権限なし／割当済み／不正遷移／入力不正／楽観ロックのとき、それぞれ403／409／409／400／409のProblemDetailが返ること
6. Documentのファイル不正／未検出／権限なし／削除済みの再削除／入力不正のとき、それぞれ400／404／403／409／400のProblemDetailが返ること
7. Jobの権限なし／定義未検出／同時実行競合／パラメータ形式不正のとき、それぞれ403／404／409／400のProblemDetailが返ること
8. バックログ項目分割APIでエラーが発生したとき、レスポンスのdetailにメッセージが入りステータスは従来どおりであること
9. フロントエンドでAPIエラー（ProblemDetail）が返ったとき、detailの文言が画面のエラーメッセージとして表示されること

## 実施手順（TDD）

1. build: サブモジュールのピン進めを単独コミット
2. test: ハンドラ単体テストをProblemDetail検証へ更新・新規追加（BacklogItem更新、Feature／Document／Job新規）、SplitBacklogItemControllerTestの $.message を $.detail へ、フロントエンドテストのエラーモックを detail へ差し替え、Redを確認
3. feat: 4ハンドラをProblemDetail化し、フロントエンドのエラー参照を detail へ追随させ、Greenを確認
4. 検証：backend small 全件、frontend npm test ＋ npmBuild、backend medium（影響モジュール中心）
