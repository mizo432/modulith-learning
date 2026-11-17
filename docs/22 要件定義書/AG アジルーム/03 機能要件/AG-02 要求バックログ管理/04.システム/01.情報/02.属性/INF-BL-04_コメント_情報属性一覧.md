# コメント属性一覧

要求・プロダクトバックログ管理業務領域で扱う「コメント」の属性を定義します。

| 属性名              | 型          | PK | 必須 | 説明                                           |
|---------------------|-------------|----|------|------------------------------------------------|
| comment_id          | SnowflakeID | *  | Y    | コメントの一意な識別子                         |
| backlog_item_id     | SnowflakeID |    | Y    | 関連するバックログ項目の識別子                 |
| author_id           | UUID        |    | Y    | 投稿者の識別子                                 |
| content             | Text        |    | Y    | コメントの内容                                 |
| mentioned_users     | JSON        |    | N    | メンションされたユーザー一覧                   |
| created_at          | DateTime    |    | Y    | 作成日時                                       |
| updated_at          | DateTime    |    | Y    | 最終更新日時                                   |
