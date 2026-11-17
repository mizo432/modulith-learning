# 課題属性一覧

要求・プロダクトバックログ管理業務領域で扱う「課題」の属性を定義します。

| 属性名              | 型          | PK | 必須 | 説明                                           |
|---------------------|-------------|----|------|------------------------------------------------|
| issue_id            | SnowflakeID | *  | Y    | 課題の一意な識別子                             |
| title               | String      |    | Y    | 課題のタイトル                                 |
| description         | Text        |    | Y    | 課題の詳細説明                                 |
| impact              | Text        |    | N    | 影響範囲                                       |
| urgency             | Enum        |    | N    | 緊急度                                         |
| status              | Enum        |    | Y    | 現在の状態                                     |
| reporter_id         | UUID        |    | Y    | 報告者の識別子                                 |
| created_at          | DateTime    |    | Y    | 作成日時                                       |
| updated_at          | DateTime    |    | Y    | 最終更新日時                                   |
