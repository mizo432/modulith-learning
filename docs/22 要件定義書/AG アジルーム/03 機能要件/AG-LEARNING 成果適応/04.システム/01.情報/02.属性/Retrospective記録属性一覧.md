# Retrospective記録属性一覧

成果計測・フィードバックによる適応業務領域で扱う「Retrospective記録」の属性を定義します。

| 属性名                 | 型          | PK | 必須 | 説明                             |
|------------------------|-------------|----|------|----------------------------------|
| retrospective_id       | SnowflakeID | *  | Y    | レトロスペクティブの一意な識別子 |
| sprint_id              | UUID        |    | Y    | 関連するスプリントの識別子       |
| title                  | String      |    | Y    | 振り返りのタイトル               |
| learnings              | Text        |    | N    | 学び                             |
| improvement_candidates | Text        |    | N    | 改善候補                         |
| decisions              | Text        |    | N    | 決定事項                         |
| created_at             | DateTime    |    | Y    | 作成日時                         |
| updated_at             | DateTime    |    | Y    | 最終更新日時                     |
