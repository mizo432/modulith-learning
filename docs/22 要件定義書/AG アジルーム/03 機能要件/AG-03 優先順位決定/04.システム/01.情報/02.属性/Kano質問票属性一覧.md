# Kano質問票属性一覧

価値・要求の優先順位決定業務領域で扱う「Kano質問票」の属性を定義します。

| 属性名                | 型          | PK | 必須 | 説明                           |
|-----------------------|-------------|----|------|--------------------------------|
| kano_survey_id        | SnowflakeID | *  | Y    | Kano質問票の一意な識別子       |
| backlog_item_id       | SnowflakeID |    | Y    | 関連するバックログ項目の識別子 |
| created_by            | UUID        |    | Y    | 作成者の識別子                 |
| title                 | String      |    | Y    | 質問票のタイトル               |
| sufficient_question   | Text        |    | Y    | 充足質問                       |
| insufficient_question | Text        |    | Y    | 不充足質問                     |
| status                | Enum        |    | Y    | 現在の状態                     |
| created_at            | DateTime    |    | Y    | 作成日時                       |
| updated_at            | DateTime    |    | Y    | 最終更新日時                   |
