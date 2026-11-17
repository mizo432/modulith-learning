# 運用管理属性一覧

EF-05 運用・監査で扱う「運用管理」の属性を定義します。

| 属性名              | 型             | PK | 必須 | 説明                                           |
|---------------------|----------------|----|------|------------------------------------------------|
| operation_id        | SnowflakeID    | *  | Y    | 運用管理の一意な識別子                         |
| operation_type      | String         |    | Y    | 運用種類（システム監視、エラー処理等）         |
| operation_name      | String         |    | Y    | 運用名                                         |
| status              | String         |    | Y    | 状態（正常、警告、エラー等）                   |
| error_message       | Text           |    | N    | エラーメッセージ（エラー時）                   |
| resolved_by         | SnowflakeID    |    | N    | 解決者の利用者ID                               |
| resolved_at         | DateTime       |    | N    | 解決日時                                       |
| description         | Text           |    | N    | 運用管理の説明                                 |
| created_at          | DateTime       |    | Y    | 作成日時                                       |
| updated_at          | DateTime       |    | Y    | 最終更新日時                                   |
