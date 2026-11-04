# 保存期間属性一覧

EF-04 保管・アーカイブで扱う「保存期間」の属性を定義します。

| 属性名              | 型             | PK | 必須 | 説明                                           |
|---------------------|----------------|----|------|------------------------------------------------|
| retention_id        | SnowflakeID    | *  | Y    | 保存期間の一意な識別子                         |
| form_def_id         | SnowflakeID    |    | Y    | 関連する帳票定義の識別子                       |
| retention_period    | Integer        |    | Y    | 保存期間（月数）                               |
| retention_unit      | String         |    | Y    | 期間単位（月、年等）                           |
| retention_policy    | String         |    | Y    | 保存ポリシー（法的要件、業務要件等）           |
| status              | String         |    | Y    | 状態（有効、無効等）                           |
| description         | Text           |    | N    | 保存期間の説明                                 |
| created_at          | DateTime       |    | Y    | 作成日時                                       |
| updated_at          | DateTime       |    | Y    | 最終更新日時                                   |
