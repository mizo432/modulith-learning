# Sprint Backlog項目属性一覧

スプリント実行とインクリメント形成業務領域で扱う「Sprint Backlog項目」の属性を定義します。

| 属性名                 | 型          | PK | 必須 | 説明                             |
|------------------------|-------------|----|------|----------------------------------|
| sprint_backlog_item_id | SnowflakeID | *  | Y    | Sprint Backlog項目の一意な識別子 |
| sprint_id              | UUID        |    | Y    | 関連するスプリントの識別子       |
| backlog_item_id        | UUID        |    | Y    | 関連するバックログ項目の識別子   |
| work_description       | Text        |    | N    | 作業内容                         |
| status                 | Enum        |    | Y    | 現在の状態                       |
| assignee_id            | UUID        |    | N    | 担当者の識別子                   |
| created_at             | DateTime    |    | Y    | 作成日時                         |
| updated_at             | DateTime    |    | Y    | 最終更新日時                     |
