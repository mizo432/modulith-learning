# 帳票生成設定属性一覧

EF-02 帳票管理で扱う「帳票生成設定」の属性を定義します。

| 属性名              | 型             | PK | 必須 | 説明                                           |
|---------------------|----------------|----|------|------------------------------------------------|
| setting_id          | SnowflakeID    | *  | Y    | 帳票生成設定の一意な識別子                     |
| form_def_id         | SnowflakeID    |    | Y    | 関連する帳票定義の識別子                       |
| output_format       | String         |    | Y    | 出力形式（PDF、HTML等）                        |
| destination_type    | String         |    | Y    | 宛先種類（ストレージ、メール、API等）          |
| destination_config  | Text           |    | N    | 宛先設定（JSON形式）                           |
| auto_generate       | Boolean        |    | Y    | 自動生成フラグ                                 |
| status              | String         |    | Y    | 状態（有効、無効等）                           |
| description         | Text           |    | N    | 生成設定の説明                                 |
| created_at          | DateTime       |    | Y    | 作成日時                                       |
| updated_at          | DateTime       |    | Y    | 最終更新日時                                   |
