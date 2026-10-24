# Sprint Review記録属性一覧

成果計測・フィードバックによる適応業務領域で扱う「Sprint Review記録」の属性を定義します。

| 属性名            | 型          | PK | 必須 | 説明                       |
|-------------------|-------------|----|------|----------------------------|
| review_id         | SnowflakeID | *  | Y    | レビューの一意な識別子     |
| sprint_id         | UUID        |    | Y    | 関連するスプリントの識別子 |
| title             | String      |    | Y    | レビューのタイトル         |
| description       | Text        |    | N    | レビューの詳細説明         |
| outcome           | Text        |    | N    | 成果内容                   |
| feedback          | Text        |    | N    | フィードバック内容         |
| decisions         | Text        |    | N    | 決定事項                   |
| unresolved_issues | Text        |    | N    | 未解決事項                 |
| created_at        | DateTime    |    | Y    | 作成日時                   |
| updated_at        | DateTime    |    | Y    | 最終更新日時               |
