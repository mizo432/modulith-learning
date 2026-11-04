# 業務システム属性一覧

EF-01 帳票基盤管理で扱う「業務システム」の属性を定義します。

| 属性名              | 型             | PK | 必須 | 説明                                           |
|---------------------|----------------|----|------|------------------------------------------------|
| system_id           | SnowflakeID    | *  | Y    | 業務システムの一意な識別子                     |
| system_name         | String         |    | Y    | 業務システム名                                 |
| system_type         | String         |    | Y    | システム種類（ERP、CRM、在庫管理等）           |
| connection_config   | Text           |    | N    | 接続設定（JSON形式）                           |
| api_endpoint        | String         |    | N    | APIエンドポイントURL                           |
| status              | String         |    | Y    | 状態（有効、無効等）                           |
| description         | Text           |    | N    | システムの説明                                 |
| created_at          | DateTime       |    | Y    | 作成日時                                       |
| updated_at          | DateTime       |    | Y    | 最終更新日時                                   |
