# 帳票配信属性一覧

EF-03 帳票利用で扱う「帳票配信」の属性を定義します。

| 属性名              | 型             | PK | 必須 | 説明                                           |
|---------------------|----------------|----|------|------------------------------------------------|
| delivery_id         | SnowflakeID    | *  | Y    | 帳票配信の一意な識別子                         |
| form_id             | SnowflakeID    |    | Y    | 配信対象の帳票識別子                           |
| recipient_id        | SnowflakeID    |    | Y    | 配信先利用者の識別子                           |
| delivery_type       | String         |    | Y    | 配信種類（メール、システム内通知等）           |
| delivery_status     | String         |    | Y    | 配信状態（送信中、送信済み、失敗等）           |
| delivered_at        | DateTime       |    | N    | 配信完了日時                                   |
| error_message       | Text           |    | N    | エラーメッセージ（失敗時）                     |
| created_at          | DateTime       |    | Y    | 作成日時                                       |
| updated_at          | DateTime       |    | Y    | 最終更新日時                                   |
