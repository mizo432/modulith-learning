# Definition of Done属性一覧

プロダクト品質・受入・性能検証業務領域で扱う「Definition of Done」の属性を定義します。

| 属性名        | 型          | PK | 必須 | 説明                   |
|---------------|-------------|----|------|------------------------|
| definition_id | SnowflakeID | *  | Y    | 完成基準の一意な識別子 |
| team_id       | UUID        |    | Y    | 所属チームの識別子     |
| title         | String      |    | Y    | 完成基準のタイトル     |
| criteria      | Text        |    | Y    | 完成基準の内容         |
| version       | Integer     |    | Y    | バージョン番号         |
| created_at    | DateTime    |    | Y    | 作成日時               |
| updated_at    | DateTime    |    | Y    | 最終更新日時           |
