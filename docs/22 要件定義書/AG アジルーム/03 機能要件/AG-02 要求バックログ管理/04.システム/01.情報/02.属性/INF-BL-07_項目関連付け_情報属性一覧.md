# 項目関連付け属性一覧

要求・プロダクトバックログ管理業務領域で扱う「項目関連付け」の属性を定義します。

| 属性名              | 型          | PK | 必須 | 説明                                           |
|---------------------|-------------|----|------|------------------------------------------------|
| relation_id         | SnowflakeID | *  | Y    | 関連付けの一意な識別子                         |
| source_item_id      | SnowflakeID |    | Y    | 元項目の識別子                                 |
| target_item_id      | SnowflakeID |    | Y    | 関連項目の識別子                               |
| relation_type       | Enum        |    | Y    | 関連の種類（依存、重複、関連など）             |
| description         | Text        |    | N    | 関連の説明                                     |
| created_at          | DateTime    |    | Y    | 作成日時                                       |
