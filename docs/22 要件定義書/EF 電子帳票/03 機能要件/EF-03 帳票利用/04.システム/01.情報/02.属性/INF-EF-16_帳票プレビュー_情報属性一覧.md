# 帳票プレビュー属性一覧

EF-03 帳票利用で扱う「帳票プレビュー」の属性を定義します。

| 属性名              | 型             | PK | 必須 | 説明                                           |
|---------------------|----------------|----|------|------------------------------------------------|
| preview_id          | SnowflakeID    | *  | Y    | 帳票プレビューの一意な識別子                   |
| form_id             | SnowflakeID    |    | Y    | プレビュー対象の帳票識別子                     |
| user_id             | SnowflakeID    |    | Y    | プレビューした利用者の識別子                   |
| preview_url         | String         |    | Y    | プレビューURL                                  |
| view_count          | Integer        |    | Y    | 閲覧回数                                       |
| last_viewed_at      | DateTime       |    | N    | 最終閲覧日時                                   |
| created_at          | DateTime       |    | Y    | 作成日時                                       |
| updated_at          | DateTime       |    | Y    | 最終更新日時                                   |
