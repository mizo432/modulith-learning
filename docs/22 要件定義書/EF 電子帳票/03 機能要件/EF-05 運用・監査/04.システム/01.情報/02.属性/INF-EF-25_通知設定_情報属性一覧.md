# 通知設定属性一覧

EF-05 運用・監査で扱う「通知設定」の属性を定義します。

| 属性名              | 型             | PK | 必須 | 説明                                           |
|---------------------|----------------|----|------|------------------------------------------------|
| notification_id     | SnowflakeID    | *  | Y    | 通知設定の一意な識別子                         |
| user_id             | SnowflakeID    |    | N    | 通知対象利用者の識別子（NULLの場合全利用者）   |
| notification_type   | String         |    | Y    | 通知種類（メール、システム内通知等）           |
| event_type          | String         |    | Y    | 通知イベント種類（帳票生成、承認完了等）       |
| enabled             | Boolean        |    | Y    | 通知有効フラグ                                 |
| status              | String         |    | Y    | 状態（有効、無効等）                           |
| description         | Text           |    | N    | 通知設定の説明                                 |
| created_at          | DateTime       |    | Y    | 作成日時                                       |
| updated_at          | DateTime       |    | Y    | 最終更新日時                                   |
