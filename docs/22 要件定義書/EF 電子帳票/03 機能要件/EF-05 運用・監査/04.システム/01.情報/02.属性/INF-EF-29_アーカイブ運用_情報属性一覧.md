# アーカイブ運用属性一覧

EF-05 運用・監査で扱う「アーカイブ運用」の属性を定義します。

| 属性名              | 型             | PK | 必須 | 説明                                           |
|---------------------|----------------|----|------|------------------------------------------------|
| archive_ops_id      | SnowflakeID    | *  | Y    | アーカイブ運用の一意な識別子                   |
| operation_type      | String         |    | Y    | 運用種類（アーカイブ監視、復元監視等）         |
| operation_name      | String         |    | Y    | 運用名                                         |
| status              | String         |    | Y    | 状態（正常、警告、エラー等）                   |
| archive_count       | Integer        |    | N    | アーカイブ件数                                 |
| restore_count       | Integer        |    | N    | 復元件数                                       |
| error_message       | Text           |    | N    | エラーメッセージ（エラー時）                   |
| description         | Text           |    | N    | アーカイブ運用の説明                           |
| created_at          | DateTime       |    | Y    | 作成日時                                       |
| updated_at          | DateTime       |    | Y    | 最終更新日時                                   |
