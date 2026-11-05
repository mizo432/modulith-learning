# 帳票情報属性一覧

EF-02 帳票管理で扱う「帳票情報」の属性を定義します。

| 属性名              | 型             | PK | 必須 | 説明                                           |
|---------------------|----------------|----|------|------------------------------------------------|
| form_id             | SnowflakeID    | *  | Y    | 帳票の一意な識別子                             |
| form_def_id         | SnowflakeID    |    | Y    | 関連する帳票定義の識別子                       |
| system_id           | SnowflakeID    |    | Y    | 関連する業務システムの識別子                   |
| form_title          | String         |    | Y    | 帳票タイトル                                  |
| creator_id          | SnowflakeID    |    | Y    | 作成者の利用者ID                               |
| storage_path        | String         |    | Y    | ストレージ上のファイルパス                     |
| file_size           | Long           |    | N    | ファイルサイズ（バイト）                       |
| file_format         | String         |    | Y    | ファイル形式（PDF、HTML等）                    |
| status              | String         |    | Y    | 状態（生成済み、送信中、送信済み等）           |
| description         | Text           |    | N    | 帳票の説明                                     |
| created_at          | DateTime       |    | Y    | 作成日時                                       |
| updated_at          | DateTime       |    | Y    | 最終更新日時                                   |
