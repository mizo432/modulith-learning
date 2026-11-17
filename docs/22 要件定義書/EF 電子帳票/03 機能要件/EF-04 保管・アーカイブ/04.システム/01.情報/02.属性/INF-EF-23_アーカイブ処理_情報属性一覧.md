# アーカイブ処理属性一覧

EF-04 保管・アーカイブで扱う「アーカイブ処理」の属性を定義します。

| 属性名              | 型             | PK | 必須 | 説明                                           |
|---------------------|----------------|----|------|------------------------------------------------|
| archive_process_id  | SnowflakeID    | *  | Y    | アーカイブ処理の一意な識別子                   |
| archive_policy_id   | SnowflakeID    |    | Y    | 適用するアーカイブポリシーの識別子             |
| process_type        | String         |    | Y    | 処理種類（自動、手動等）                       |
| process_status      | String         |    | Y    | 処理状態（保留中、処理中、完了、失敗等）       |
| target_count        | Integer        |    | Y    | 対象件数                                       |
| processed_count     | Integer        |    | Y    | 処理済み件数                                   |
| error_message       | Text           |    | N    | エラーメッセージ（失敗時）                     |
| started_at          | DateTime       |    | N    | 処理開始日時                                   |
| completed_at        | DateTime       |    | N    | 処理完了日時                                   |
| created_at          | DateTime       |    | Y    | 作成日時                                       |
| updated_at          | DateTime       |    | Y    | 最終更新日時                                   |
