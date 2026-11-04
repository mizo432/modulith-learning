# システム設定属性一覧

EF-01 帳票基盤管理で扱う「システム設定」の属性を定義します。

| 属性名              | 型             | PK | 必須 | 説明                                           |
|---------------------|----------------|----|------|------------------------------------------------|
| setting_id          | SnowflakeID    | *  | Y    | システム設定の一意な識別子                     |
| setting_key         | String         |    | Y    | 設定キー（一意）                               |
| setting_value       | Text           |    | Y    | 設定値                                         |
| category            | String         |    | Y    | カテゴリ（保存期間、通知、セキュリティ等）     |
| data_type           | String         |    | Y    | データ型（String、Integer、Boolean等）         |
| scope               | String         |    | N    | 適用範囲（システム全体、組織単位等）           |
| status              | String         |    | Y    | 状態（有効、無効等）                           |
| description         | Text           |    | N    | 設定の説明                                     |
| created_at          | DateTime       |    | Y    | 作成日時                                       |
| updated_at          | DateTime       |    | Y    | 最終更新日時                                   |
