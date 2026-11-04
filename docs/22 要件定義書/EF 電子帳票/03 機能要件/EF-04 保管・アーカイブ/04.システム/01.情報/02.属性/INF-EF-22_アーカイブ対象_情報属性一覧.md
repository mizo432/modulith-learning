# アーカイブ対象属性一覧

EF-04 保管・アーカイブで扱う「アーカイブ対象」の属性を定義します。

| 属性名              | 型             | PK | 必須 | 説明                                           |
|---------------------|----------------|----|------|------------------------------------------------|
| archive_target_id   | SnowflakeID    | *  | Y    | アーカイブ対象の一意な識別子                   |
| form_id             | SnowflakeID    |    | Y    | アーカイブ対象の帳票識別子                     |
| archive_policy_id   | SnowflakeID    |    | Y    | 適用するアーカイブポリシーの識別子             |
| target_status       | String         |    | Y    | 対象状態（保留中、アーカイブ済み等）           |
| original_path       | String         |    | Y    | 元のストレージパス                             |
| archive_path        | String         |    | N    | アーカイブ先パス                               |
| scheduled_at        | DateTime       |    | N    | アーカイブ予定日時                             |
| archived_at         | DateTime       |    | N    | アーカイブ実行日時                             |
| created_at          | DateTime       |    | Y    | 作成日時                                       |
| updated_at          | DateTime       |    | Y    | 最終更新日時                                   |
