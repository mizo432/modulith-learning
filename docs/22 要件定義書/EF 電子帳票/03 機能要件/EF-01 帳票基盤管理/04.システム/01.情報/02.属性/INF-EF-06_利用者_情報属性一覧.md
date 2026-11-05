# 利用者属性一覧

EF-01 帳票基盤管理で扱う「利用者」の属性を定義します。

| 属性名              | 型             | PK | 必須 | 説明                                           |
|---------------------|----------------|----|------|------------------------------------------------|
| user_id             | SnowflakeID    | *  | Y    | 利用者の一意な識別子                           |
| login_id            | String         |    | Y    | ログインID（一意）                             |
| user_name           | String         |    | Y    | 氏名                                           |
| email               | String         |    | N    | メールアドレス                                 |
| org_id              | SnowflakeID    |    | N    | 所属組織の識別子                               |
| password_hash       | String         |    | Y    | パスワードハッシュ                             |
| status              | String         |    | Y    | 状態（有効、無効、ロック等）                   |
| last_login_at       | DateTime       |    | N    | 最終ログイン日時                               |
| created_at          | DateTime       |    | Y    | 作成日時                                       |
| updated_at          | DateTime       |    | Y    | 最終更新日時                                   |
