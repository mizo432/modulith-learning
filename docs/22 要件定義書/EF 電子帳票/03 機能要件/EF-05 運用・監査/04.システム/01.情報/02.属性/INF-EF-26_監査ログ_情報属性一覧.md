# 監査ログ属性一覧

EF-05 運用・監査で扱う「監査ログ」の属性を定義します。

| 属性名              | 型             | PK | 必須 | 説明                                           |
|---------------------|----------------|----|------|------------------------------------------------|
| audit_log_id        | SnowflakeID    | *  | Y    | 監査ログの一意な識別子                         |
| user_id             | SnowflakeID    |    | Y    | 操作者の利用者ID                               |
| action_type         | String         |    | Y    | 操作種類（作成、更新、削除、閲覧等）           |
| target_type         | String         |    | Y    | 操作対象種類（帳票、設定等）                   |
| target_id           | SnowflakeID    |    | Y    | 操作対象の識別子                               |
| action_detail       | Text           |    | N    | 操作の詳細内容                                 |
| ip_address          | String         |    | N    | 操作元のIPアドレス                             |
| user_agent          | String         |    | N    | 操作元のユーザーエージェント                   |
| executed_at         | DateTime       |    | Y    | 操作実行日時                                   |
