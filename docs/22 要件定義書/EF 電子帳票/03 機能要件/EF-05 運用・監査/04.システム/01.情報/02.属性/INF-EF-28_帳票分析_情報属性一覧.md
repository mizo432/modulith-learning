# 帳票分析属性一覧

EF-05 運用・監査で扱う「帳票分析」の属性を定義します。

| 属性名              | 型             | PK | 必須 | 説明                                           |
|---------------------|----------------|----|------|------------------------------------------------|
| analysis_id         | SnowflakeID    | *  | Y    | 帳票分析の一意な識別子                         |
| analysis_type       | String         |    | Y    | 分析種類（生成件数、閲覧統計等）               |
| analysis_period     | String         |    | Y    | 分析期間（日次、週次、月次等）                 |
| start_date          | Date           |    | Y    | 分析開始日                                     |
| end_date            | Date           |    | Y    | 分析終了日                                     |
| result_data         | Text           |    | Y    | 分析結果データ（JSON形式）                     |
| status              | String         |    | Y    | 状態（処理中、完了、失敗等）                   |
| executed_at         | DateTime       |    | Y    | 分析実行日時                                   |
| created_at          | DateTime       |    | Y    | 作成日時                                       |
| updated_at          | DateTime       |    | Y    | 最終更新日時                                   |
