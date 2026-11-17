# BUC-IDB-01 イテレーションダッシュボード

## 概要

イテレーションダッシュボードは、スプリント（イテレーション）の進捗・成果・リスクを可視化し、Scrum Teamが適切な判断を行うための業務フローです。タスクボードが開発者による日々の作業管理を目的とするのに対し、イテレーションダッシュボードは、Sprint Goalの達成に向けてチーム全体が順調に進んでいるかを把握することを目的とします。

## ビジネスアクター

| アクター | 役割 |
|----------|------|
| ACT-02 (プロダクトオーナー) | Sprint GoalとProduct Backlogの優先順位を踏まえ、成果を確認 |
| ACT-03 (スクラムマスター) | 進捗の停滞、障害、チームの改善機会を発見 |
| ACT-04 (開発者) | チーム全体の進捗、自分の作業、ブロッカーを確認 |
| ACT-05 (トラッキング担当) | スプリントの進捗や成果を把握 |

## アクティビティ

| 番号 | アクティビティ名 | 説明 |
|------|------------------|------|
| 1 | ダッシュボードを表示する | 対象スプリントのダッシュボード画面を開く |
| 2 | Sprint情報を確認する | 期間、状態、チーム、Sprint Goalを確認 |
| 3 | Sprintサマリーを確認する | Story・Taskの進捗率、完了数、残作業量を確認 |
| 4 | Sprint Goal達成状況を確認する | Goalと関連Storyの完了状況を確認 |
| 5 | Burndown Chartを確認する | 残作業量の推移を確認 |
| 6 | Burnup Chartを確認する | 完了量とスコープの推移を確認 |
| 7 | User Story進捗を確認する | Storyごとの状態・完了状況を確認 |
| 8 | Task進捗を確認する | Taskの状態・残作業時間を確認 |
| 9 | スコープ変更を確認する | Sprint Backlogの変更履歴を確認 |
| 10 | ブロッカーを確認する | 未解決の障害・ブロッカーを確認 |
| 11 | リスクを確認する | Sprint Goalへの影響リスクを確認 |
| 12 | 過去Sprintと比較する | 過去のスプリントとのメトリクス比較 |
| 13 | 関連画面へ遷移する | TaskボードやReview画面へ移動 |

## 関連情報

| 情報ID | 情報名 | 参照目的 |
|--------|--------|----------|
| INF-SP-01 | スプリント | スプリント基本情報 |
| INF-SP-02 | Sprint Goal | 目標と達成状況 |
| INF-SP-03 | Sprint Backlog項目 | 選択されたStory・Task |
| INF-SP-04 | 障害 | ブロッカー情報 |
| INF-SP-10 | タスク | Taskの状態・残作業 |
| INF-IDB-01 | Sprint Planning Snapshot | 計画時点のSnapshot |
| INF-IDB-02 | Sprint Scope Change | スコープ変更履歴 |
| INF-IDB-03 | Task Work Log | 作業実績 |
| INF-IDB-04 | Sprint Metrix | 集計メトリクス |

## 関連ユースケース

UC-IDB-001, UC-IDB-002, UC-IDB-003, UC-IDB-004, UC-IDB-005, UC-IDB-006, UC-IDB-007, UC-IDB-008, UC-IDB-009, UC-IDB-010, UC-IDB-011, UC-IDB-012, UC-IDB-013, UC-IDB-014, UC-IDB-015

## 業務ルール

| ルール名 | 種類 | 説明 |
|----------|------|------|
| Sprint Goal中心の表示 | Policy | ダッシュボードはSprint Goalの達成状況を最優先に表示する |
| Snapshot保存 | Operation | Sprint Planning完了時に計画情報をSnapshotとして保存する |
| 集計単位分離 | Policy | Story PointsとTask残作業時間は別のグラフに表示する |
| 個人監視禁止 | Policy | 個人の生産性を評価するための画面にしない |
