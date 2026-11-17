# バックログ階層属性一覧

要求・プロダクトバックログ管理業務領域で扱う「バックログ階層」の属性を定義します。

| 属性名              | 型          | PK | 必須 | 説明                                           |
|---------------------|-------------|----|------|------------------------------------------------|
| hierarchy_id        | SnowflakeID | *  | Y    | 階層の一意な識別子                             |
| parent_item_id      | SnowflakeID |    | Y    | 親項目の識別子                                 |
| child_item_id       | SnowflakeID |    | Y    | 子項目の識別子                                 |
| level               | Integer     |    | Y    | 階層レベル                                     |
| created_at          | DateTime    |    | Y    | 作成日時                                       |
