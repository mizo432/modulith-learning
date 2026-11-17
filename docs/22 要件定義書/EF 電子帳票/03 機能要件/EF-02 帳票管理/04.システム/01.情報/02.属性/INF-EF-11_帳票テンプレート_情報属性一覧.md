# 帳票テンプレート属性一覧

EF-02 帳票管理で扱う「帳票テンプレート」の属性を定義します。

| 属性名              | 型             | PK | 必須 | 説明                                           |
|---------------------|----------------|----|------|------------------------------------------------|
| template_id         | SnowflakeID    | *  | Y    | 帳票テンプレートの一意な識別子                 |
| form_def_id         | SnowflakeID    |    | Y    | 関連する帳票定義の識別子                       |
| template_name       | String         |    | Y    | テンプレート名                                 |
| template_file_path  | String         |    | Y    | テンプレートファイルのパス                     |
| file_format         | String         |    | Y    | ファイル形式（PDF、HTML、DOCX等）              |
| version             | String         |    | Y    | バージョン番号                                 |
| file_size           | Long           |    | N    | ファイルサイズ（バイト）                       |
| status              | String         |    | Y    | 状態（有効、無効等）                           |
| description         | Text           |    | N    | テンプレートの説明                             |
| created_at          | DateTime       |    | Y    | 作成日時                                       |
| updated_at          | DateTime       |    | Y    | 最終更新日時                                   |
