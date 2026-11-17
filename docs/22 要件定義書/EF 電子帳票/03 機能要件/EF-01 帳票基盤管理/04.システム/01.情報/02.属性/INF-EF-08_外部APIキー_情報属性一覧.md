# 外部APIキー属性一覧

EF-01 帳票基盤管理で扱う「外部APIキー」の属性を定義します。

| 属性名              | 型             | PK | 必須 | 説明                                           |
|---------------------|----------------|----|------|------------------------------------------------|
| api_key_id          | SnowflakeID    | *  | Y    | 外部APIキーの一意な識別子                      |
| system_id           | SnowflakeID    |    | Y    | 関連する業務システムの識別子                   |
| api_key_name        | String         |    | Y    | APIキー名                                     |
| api_key             | String         |    | Y    | APIキー（暗号化保存）                          |
| api_secret          | String         |    | Y    | APIシークレット（暗号化保存）                  |
| api_endpoint        | String         |    | N    | APIエンドポイントURL                           |
| status              | String         |    | Y    | 状態（有効、無効等）                           |
| expires_at          | DateTime       |    | N    | 有効期限                                       |
| last_used_at        | DateTime       |    | N    | 最終利用日時                                   |
| description         | Text           |    | N    | APIキーの説明                                  |
| created_at          | DateTime       |    | Y    | 作成日時                                       |
| updated_at          | DateTime       |    | Y    | 最終更新日時                                   |
