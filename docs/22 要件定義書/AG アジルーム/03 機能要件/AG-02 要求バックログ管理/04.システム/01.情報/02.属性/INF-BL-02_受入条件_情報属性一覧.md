# 受入条件属性一覧

要求・プロダクトバックログ管理業務領域で扱う「受入条件」の属性を定義します。

| 属性名              | 型          | PK | 必須 | 説明                                           |
|---------------------|-------------|----|------|------------------------------------------------|
| acceptance_id       | SnowflakeID | *  | Y    | 受入条件の一意な識別子                         |
| backlog_item_id     | SnowflakeID |    | Y    | 関連するバックログ項目の識別子                 |
| condition_name      | String      |    | Y    | 条件の名前                                     |
| description         | Text        |    | Y    | 条件の詳細説明                                 |
| scenario            | Text        |    | N    | テストシナリオ                                 |
| expected_result     | Text        |    | N    | 期待される結果                                 |
| created_at          | DateTime    |    | Y    | 作成日時                                       |
| updated_at          | DateTime    |    | Y    | 最終更新日時                                   |
