# 組織属性一覧

EF-01 帳票基盤管理で扱う「組織」の属性を定義します。

| 属性名              | 型             | PK | 必須 | 説明                                           |
|---------------------|----------------|----|------|------------------------------------------------|
| org_id              | SnowflakeID    | *  | Y    | 組織の一意な識別子                             |
| org_name            | String         |    | Y    | 組織名                                         |
| parent_org_id       | SnowflakeID    |    | N    | 親組織の識別子（NULLの場合ルート組織）         |
| org_type            | String         |    | Y    | 組織種類（部署、チーム、プロジェクト等）       |
| org_code            | String         |    | N    | 組織コード                                     |
| status              | String         |    | Y    | 状態（有効、無効等）                           |
| description         | Text           |    | N    | 組織の説明                                     |
| created_at          | DateTime       |    | Y    | 作成日時                                       |
| updated_at          | DateTime       |    | Y    | 最終更新日時                                   |
