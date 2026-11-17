# ユースケース一覧（イテレーションダッシュボード）

イテレーションダッシュボード機能のユースケースを定義します。

| ユースケースID | ユースケース名                 | 説明                                               | アクター                       | 優先度 | ビジネスユースケース | 外部システム | 画面    |
|----------------|--------------------------------|----------------------------------------------------|--------------------------------|--------|----------------------|--------------|---------|
| UC-IDB-001     | ダッシュボードを表示する       | Sprintの状況を一元的に表示する                     | ACT-02, ACT-03, ACT-04, ACT-05 | 高     | BUC-IDB-01           | -            | SCR-029 |
| UC-IDB-002     | Sprintを切り替える             | 対象Sprintを変更する                               | ACT-02, ACT-03, ACT-04, ACT-05 | 高     | BUC-IDB-01           | -            | SCR-029 |
| UC-IDB-003     | Sprint Goalを確認する          | Goalと達成状況を確認する                           | ACT-02, ACT-03, ACT-04, ACT-05 | 高     | BUC-IDB-01           | -            | SCR-029 |
| UC-IDB-004     | Sprintサマリーを確認する       | Story・Taskの進捗を確認する                        | ACT-02, ACT-03, ACT-04, ACT-05 | 高     | BUC-IDB-01           | -            | SCR-029 |
| UC-IDB-005     | Burndownを確認する             | 残作業量の推移を確認する                           | ACT-02, ACT-03, ACT-04, ACT-05 | 高     | BUC-IDB-01           | -            | SCR-029 |
| UC-IDB-006     | Burnupを確認する               | 完了量とスコープの推移を確認する                   | ACT-02, ACT-03, ACT-04, ACT-05 | 高     | BUC-IDB-01           | -            | SCR-029 |
| UC-IDB-007     | User Story進捗を確認する       | Storyごとの状態を確認する                          | ACT-02, ACT-03, ACT-04, ACT-05 | 高     | BUC-IDB-01           | -            | SCR-029 |
| UC-IDB-008     | Task進捗を確認する             | Taskの状態を確認する                               | ACT-02, ACT-03, ACT-04, ACT-05 | 高     | BUC-IDB-01           | -            | SCR-029 |
| UC-IDB-009     | スコープ変更を確認する         | Sprint Backlogの変更を確認する                     | ACT-02, ACT-03, ACT-04, ACT-05 | 中     | BUC-IDB-01           | -            | SCR-029 |
| UC-IDB-010     | ブロッカーを確認する           | 未解決の問題を確認する                             | ACT-02, ACT-03, ACT-04, ACT-05 | 高     | BUC-IDB-01           | -            | SCR-029 |
| UC-IDB-011     | リスクを確認する               | Sprint Goalへの影響を確認する                      | ACT-02, ACT-03, ACT-04, ACT-05 | 中     | BUC-IDB-01           | -            | SCR-029 |
| UC-IDB-012     | メトリクスを比較する           | 過去Sprintと比較する                               | ACT-02, ACT-03, ACT-05         | 低     | BUC-IDB-01           | -            | SCR-029 |
| UC-IDB-013     | Taskボードへ遷移する           | Taskボード画面を表示する                           | ACT-03, ACT-04                 | 中     | BUC-IDB-01           | -            | SCR-015 |
| UC-IDB-014     | Sprint Reviewへ遷移する        | Review画面を表示する                               | ACT-02, ACT-03, ACT-04         | 低     | BUC-IDB-01           | -            | SCR-017 |
| UC-IDB-015     | Retrospectiveへ遷移する        | Retrospective画面を表示する                        | ACT-03, ACT-04                 | 低     | BUC-IDB-01           | -            | SCR-022 |
