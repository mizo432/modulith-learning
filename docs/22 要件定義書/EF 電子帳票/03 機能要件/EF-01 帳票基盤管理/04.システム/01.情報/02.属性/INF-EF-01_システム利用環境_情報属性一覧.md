# システム利用環境属性一覧

EF-01 帳票基盤管理で扱う「システム利用環境」の属性を定義します。

| 属性名              | 型             | PK | 必須 | 説明                                           |
|---------------------|----------------|----|------|------------------------------------------------|
| environment_id      | SnowflakeID    | *  | Y    | システム利用環境の一意な識別子                 |
| tenant_id           | String         |    | Y    | テナントの一意な識別子                         |
| system_name         | String         |    | Y    | システム名                                     |
| environment_type    | String         |    | Y    | 環境区分（本番、ステージング、開発等）         |
| description         | Text           |    | N    | 環境の説明                                     |
| status              | String         |    | Y    | 状態（有効、無効等）                           |
| created_at          | DateTime       |    | Y    | 作成日時                                       |
| updated_at          | DateTime       |    | Y    | 最終更新日時                                   |
