# 帳票バージョン属性一覧

EF-02 帳票管理で扱う「帳票バージョン」の属性を定義します。

| 属性名              | 型             | PK | 必須 | 説明                                           |
|---------------------|----------------|----|------|------------------------------------------------|
| version_id          | SnowflakeID    | *  | Y    | 帳票バージョンの一意な識別子                   |
| form_id             | SnowflakeID    |    | Y    | 関連する帳票の識別子                           |
| version_number      | Integer        |    | Y    | バージョン番号                                 |
| change_description  | Text           |    | N    | 変更内容の説明                                 |
| changed_by          | SnowflakeID    |    | Y    | 変更者の利用者ID                               |
| storage_path        | String         |    | Y    | ストレージ上のファイルパス                     |
| file_size           | Long           |    | N    | ファイルサイズ（バイト）                       |
| is_current          | Boolean        |    | Y    | 現在有効なバージョンフラグ                     |
| created_at          | DateTime       |    | Y    | 作成日時                                       |
