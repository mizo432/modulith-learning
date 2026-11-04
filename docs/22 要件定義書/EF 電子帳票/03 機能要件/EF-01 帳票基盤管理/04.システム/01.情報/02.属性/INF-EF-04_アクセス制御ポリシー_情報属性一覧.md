# アクセス制御ポリシー属性一覧

EF-01 帳票基盤管理で扱う「アクセス制御ポリシー」の属性を定義します。

| 属性名              | 型             | PK | 必須 | 説明                                           |
|---------------------|----------------|----|------|------------------------------------------------|
| policy_id           | SnowflakeID    | *  | Y    | アクセス制御ポリシーの一意な識別子             |
| system_id           | SnowflakeID    |    | Y    | 関連する業務システムの識別子                   |
| policy_name         | String         |    | Y    | ポリシー名                                     |
| policy_type         | String         |    | Y    | ポリシー種類（READ、WRITE、ADMIN等）           |
| access_rule         | Text           |    | Y    | アクセスルール（JSON形式）                     |
| scope               | String         |    | N    | 適用範囲（システム全体、組織単位等）           |
| status              | String         |    | Y    | 状態（有効、無効等）                           |
| description         | Text           |    | N    | ポリシーの説明                                 |
| created_at          | DateTime       |    | Y    | 作成日時                                       |
| updated_at          | DateTime       |    | Y    | 最終更新日時                                   |
