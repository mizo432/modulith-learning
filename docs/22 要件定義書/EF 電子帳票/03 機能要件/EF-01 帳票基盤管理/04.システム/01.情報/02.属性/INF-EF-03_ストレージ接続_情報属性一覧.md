# ストレージ接続属性一覧

EF-01 帳票基盤管理で扱う「ストレージ接続」の属性を定義します。

| 属性名              | 型             | PK | 必須 | 説明                                           |
|---------------------|----------------|----|------|------------------------------------------------|
| storage_id          | SnowflakeID    | *  | Y    | ストレージ接続の一意な識別子                   |
| storage_name        | String         |    | Y    | ストレージ名                                   |
| storage_type        | String         |    | Y    | ストレージ種類（Amazon S3、MinIO等）           |
| endpoint_url        | String         |    | Y    | エンドポイントURL                              |
| bucket_name         | String         |    | Y    | バケット名                                     |
| access_key          | String         |    | Y    | アクセスキー（暗号化保存）                     |
| secret_key          | String         |    | Y    | シークレットキー（暗号化保存）                 |
| region              | String         |    | N    | リージョン                                     |
| status              | String         |    | Y    | 状態（有効、無効等）                           |
| description         | Text           |    | N    | ストレージの説明                               |
| created_at          | DateTime       |    | Y    | 作成日時                                       |
| updated_at          | DateTime       |    | Y    | 最終更新日時                                   |
