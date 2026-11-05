# 承認・確認属性一覧

EF-03 帳票利用で扱う「承認・確認」の属性を定義します。

| 属性名              | 型             | PK | 必須 | 説明                                           |
|---------------------|----------------|----|------|------------------------------------------------|
| approval_id         | SnowflakeID    | *  | Y    | 承認・確認の一意な識別子                       |
| form_id             | SnowflakeID    |    | Y    | 承認対象の帳票識別子                           |
| approver_id         | SnowflakeID    |    | Y    | 承認者の利用者ID                               |
| approval_type       | String         |    | Y    | 承認種類（承認、確認、拒否等）                 |
| approval_status     | String         |    | Y    | 承認状態（保留中、承認済み、拒否済み等）       |
| approval_comment    | Text           |    | N    | 承認コメント                                   |
| approved_at         | DateTime       |    | N    | 承認日時                                       |
| created_at          | DateTime       |    | Y    | 作成日時                                       |
| updated_at          | DateTime       |    | Y    | 最終更新日時                                   |
