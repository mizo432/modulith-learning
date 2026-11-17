# 情報ER図

## 概要

本ファイルは、EF-03 帳票利用の情報エンティティ間の関係をER図で定義します。

## ER図（英語版）

```plantuml
@startuml
skinparam linetype ortho

entity "FormDelivery\nINF-EF-15" as FormDelivery {
  * delivery_id : String
  * form_id : String
  * recipient_id : String
  * delivery_type : String
  * delivery_status : String
  * delivered_at : LocalDateTime
  --
  created_at : LocalDateTime
}

entity "FormPreview\nINF-EF-16" as FormPreview {
  * preview_id : String
  * form_id : String
  * cache_path : String
  * expires_at : LocalDateTime
  * view_count : Integer
  --
  created_at : LocalDateTime
}

entity "FormSearch\nINF-EF-17" as FormSearch {
  * search_id : String
  * user_id : String
  * search_condition : String
  * result_count : Integer
  * cache_key : String
  --
  searched_at : LocalDateTime
}

entity "Approval\nINF-EF-18" as Approval {
  * approval_id : String
  * form_id : String
  * approver_id : String
  * approval_status : String
  * approval_comment : String
  * approved_at : LocalDateTime
  * approval_order : Integer
  --
  created_at : LocalDateTime
}

FormDelivery "1" -- "0..*" Approval : requires >
FormPreview "1" -- "0..*" FormSearch : related >

@enduml
```

## ER図（日本語版）

```plantuml
@startuml
skinparam linetype ortho

entity "帳票配信\nINF-EF-15" as FormDelivery {
  * 配信ID : String
  * 帳票ID : String
  * 宛先ID : String
  * 配信種類 : String
  * 配信ステータス : String
  * 配信日時 : LocalDateTime
  --
  作成日時 : LocalDateTime
}

entity "帳票プレビュー\nINF-EF-16" as FormPreview {
  * プレビューID : String
  * 帳票ID : String
  * キャッシュパス : String
  * 有効期限 : LocalDateTime
  * 閲覧回数 : Integer
  --
  作成日時 : LocalDateTime
}

entity "帳票検索\nINF-EF-17" as FormSearch {
  * 検索ID : String
  * 利用者ID : String
  * 検索条件 : String
  * 結果数 : Integer
  * キャッシュキー : String
  --
  検索日時 : LocalDateTime
}

entity "承認・確認\nINF-EF-18" as Approval {
  * 承認ID : String
  * 帳票ID : String
  * 承認者ID : String
  * 承認ステータス : String
  * 承認コメント : String
  * 承認日時 : LocalDateTime
  * 承認順序 : Integer
  --
  作成日時 : LocalDateTime
}

FormDelivery "1" -- "0..*" Approval : 要求 >
FormPreview "1" -- "0..*" FormSearch : 関連 >

@enduml
```

## 情報エンティティ一覧

| 情報ID | 情報名 | 主要属性 | 関連エンティティ |
|--------|--------|----------|-----------------|
| INF-EF-15 | 帳票配信 | delivery_id, form_id, recipient_id, delivery_status | 承認・確認 |
| INF-EF-16 | 帳票プレビュー | preview_id, form_id, cache_path, expires_at | 帳票検索 |
| INF-EF-17 | 帳票検索 | search_id, user_id, search_condition, result_count | 帳票プレビュー |
| INF-EF-18 | 承認・確認 | approval_id, form_id, approver_id, approval_status | 帳票配信 |

## 関係一覧

| 源エンティティ | 関係 | 対象エンティティ | 多重度 | 説明 |
|---------------|------|-----------------|--------|------|
| 帳票配信 | 要求 | 承認・確認 | 1 : 0..* | 配信前に承認が必要な場合がある |
| 帳票プレビュー | 関連 | 帳票検索 | 1 : 0..* | 検索結果からプレビューを表示 |
