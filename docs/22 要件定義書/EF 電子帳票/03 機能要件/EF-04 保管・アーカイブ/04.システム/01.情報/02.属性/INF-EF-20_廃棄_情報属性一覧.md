# 廃棄属性一覧

EF-04 保管・アーカイブで扱う「廃棄」の属性を定義します。

| 属性名              | 型             | PK | 必須 | 説明                                           |
|---------------------|----------------|----|------|------------------------------------------------|
| disposal_id         | SnowflakeID    | *  | Y    | 廃棄の一意な識別子                             |
| form_id             | SnowflakeID    |    | Y    | 廃棄対象の帳票識別子                           |
| disposal_reason     | String         |    | Y    | 廃棄理由（保存期間経過、手動廃棄等）           |
| disposal_status     | String         |    | Y    | 廃棄状態（保留中、処理中、完了等）             |
| disposed_by         | SnowflakeID    |    | N    | 廃棄実行者の利用者ID                           |
| disposed_at         | DateTime       |    | N    | 廃棄実行日時                                   |
| scheduled_at        | DateTime       |    | N    | 廃棄予定日時                                   |
| description         | Text           |    | N    | 廃棄の説明                                     |
| created_at          | DateTime       |    | Y    | 作成日時                                       |
| updated_at          | DateTime       |    | Y    | 最終更新日時                                   |
