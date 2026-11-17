# Product Goal属性一覧

プロダクトディスカバリー業務領域で扱う「Product Goal」の属性を定義します。

| 属性名          | 型          | PK | 必須 | 説明                         |
|-----------------|-------------|----|------|------------------------------|
| product_goal_id | SnowflakeID | *  | Y    | プロダクト目標の一意な識別子 |
| project_id      | UUID        |    | Y    | 所属プロジェクトの識別子     |
| title           | String      |    | Y    | 目標のタイトル               |
| description     | Text        |    | Y    | 目標の詳細説明               |
| status          | Enum        |    | Y    | 現在の状態                   |
| start_date      | Date        |    | N    | 開始予定日                   |
| end_date        | Date        |    | N    | 終了予定日                   |
| created_at      | DateTime    |    | Y    | 作成日時                     |
| updated_at      | DateTime    |    | Y    | 最終更新日時                 |
