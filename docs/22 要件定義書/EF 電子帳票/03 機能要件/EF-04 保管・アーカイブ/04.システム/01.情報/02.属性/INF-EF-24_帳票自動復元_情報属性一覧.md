# 帳票自動復元属性一覧

EF-04 保管・アーカイブで扱う「帳票自動復元」の属性を定義します。

| 属性名              | 型             | PK | 必須 | 説明                                           |
|---------------------|----------------|----|------|------------------------------------------------|
| restore_id          | SnowflakeID    | *  | Y    | 帳票自動復元の一意な識別子                     |
| form_id             | SnowflakeID    |    | Y    | 復元対象の帳票識別子                           |
| user_id             | SnowflakeID    |    | Y    | 復元要求した利用者の識別子                     |
| restore_status      | String         |    | Y    | 復元状態（保留中、処理中、完了、失敗等）       |
| archive_path        | String         |    | Y    | アーカイブ元パス                               |
| restore_path        | String         |    | N    | 復元先パス                                     |
| error_message       | Text           |    | N    | エラーメッセージ（失敗時）                     |
| requested_at        | DateTime       |    | Y    | 復元要求日時                                   |
| completed_at        | DateTime       |    | N    | 復元完了日時                                   |
| created_at          | DateTime       |    | Y    | 作成日時                                       |
| updated_at          | DateTime       |    | Y    | 最終更新日時                                   |
