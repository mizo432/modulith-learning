# 帳票検索属性一覧

EF-03 帳票利用で扱う「帳票検索」の属性を定義します。

| 属性名              | 型             | PK | 必須 | 説明                                           |
|---------------------|----------------|----|------|------------------------------------------------|
| search_id           | SnowflakeID    | *  | Y    | 帳票検索の一意な識別子                         |
| user_id             | SnowflakeID    |    | Y    | 検索を実行した利用者の識別子                   |
| search_keywords     | Text           |    | Y    | 検索キーワード                                 |
| search_filters      | Text           |    | N    | 検索フィルター（JSON形式）                     |
| result_count        | Integer        |    | Y    | 検索結果件数                                   |
| executed_at         | DateTime       |    | Y    | 検索実行日時                                   |
