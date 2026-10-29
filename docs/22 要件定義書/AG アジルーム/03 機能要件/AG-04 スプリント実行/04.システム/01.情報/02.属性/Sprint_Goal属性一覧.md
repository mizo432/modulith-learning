# Sprint Goal属性一覧

スプリント実行とインクリメント形成業務領域で扱う「Sprint Goal」の属性を定義します。

| 属性名           | 型          | PK | 必須 | 説明                           |
|------------------|-------------|----|------|--------------------------------|
| goal_id          | SnowflakeID | *  | Y    | Sprint Goalの一意な識別子      |
| sprint_id        | SnowflakeID |    | Y    | 関連するスプリントの識別子     |
| goal_description | Text        |    | Y    | Sprint Goalの説明              |
| related_items    | Text        |    | N    | 関連するバックログ項目のリスト |
| created_at       | DateTime    |    | Y    | 作成日時                       |
| updated_at       | DateTime    |    | Y    | 最終更新日時                   |
