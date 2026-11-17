# ロール属性一覧

EF-01 帳票基盤管理で扱う「ロール」の属性を定義します。

| 属性名              | 型             | PK | 必須 | 説明                                           |
|---------------------|----------------|----|------|------------------------------------------------|
| role_id             | SnowflakeID    | *  | Y    | ロールの一意な識別子                           |
| role_name           | String         |    | Y    | ロール名                                       |
| role_type           | String         |    | Y    | ロール種類（管理者、利用者、閲覧者等）         |
| permission_set      | Text           |    | Y    | 権限セット（JSON形式）                         |
| status              | String         |    | Y    | 状態（有効、無効等）                           |
| description         | Text           |    | N    | ロールの説明                                   |
| created_at          | DateTime       |    | Y    | 作成日時                                       |
| updated_at          | DateTime       |    | Y    | 最終更新日時                                   |
