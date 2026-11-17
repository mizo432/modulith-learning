# アーカイブポリシー属性一覧

EF-04 保管・アーカイブで扱う「アーカイブポリシー」の属性を定義します。

| 属性名              | 型             | PK | 必須 | 説明                                           |
|---------------------|----------------|----|------|------------------------------------------------|
| archive_policy_id   | SnowflakeID    | *  | Y    | アーカイブポリシーの一意な識別子               |
| policy_name         | String         |    | Y    | ポリシー名                                     |
| form_def_id         | SnowflakeID    |    | N    | 関連する帳票定義の識別子（NULLの場合全帳票）   |
| archive_trigger     | String         |    | Y    | アーカイブトリガー（保存期間経過、手動等）     |
| archive_destination | String         |    | Y    | アーカイブ先ストレージID                       |
| compression_type    | String         |    | N    | 圧縮種類（gzip、zip等）                        |
| status              | String         |    | Y    | 状態（有効、無効等）                           |
| description         | Text           |    | N    | ポリシーの説明                                 |
| created_at          | DateTime       |    | Y    | 作成日時                                       |
| updated_at          | DateTime       |    | Y    | 最終更新日時                                   |
