# 帳票定義属性一覧

EF-02 帳票管理で扱う「帳票定義」の属性を定義します。

| 属性名              | 型             | PK | 必須 | 説明                                           |
|---------------------|----------------|----|------|------------------------------------------------|
| form_def_id         | SnowflakeID    | *  | Y    | 帳票定義の一意な識別子                         |
| form_type           | String         |    | Y    | 帳票種類（請求書、納品書、発注書等）           |
| form_name           | String         |    | Y    | 帳票名                                         |
| system_id           | SnowflakeID    |    | Y    | 関連する業務システムの識別子                   |
| layout_config       | Text           |    | N    | レイアウト設定（JSON形式）                     |
| paper_size          | String         |    | N    | 用紙サイズ（A4、A3、B4等）                     |
| orientation         | String         |    | N    | 印刷方向（縦、横）                             |
| status              | String         |    | Y    | 状態（有効、無効等）                           |
| description         | Text           |    | N    | 帳票定義の説明                                 |
| created_at          | DateTime       |    | Y    | 作成日時                                       |
| updated_at          | DateTime       |    | Y    | 最終更新日時                                   |
