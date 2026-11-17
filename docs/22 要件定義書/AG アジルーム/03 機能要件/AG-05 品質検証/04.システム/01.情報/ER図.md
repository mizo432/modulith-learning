# AG-05 品質検証 ER図

## 英語版

@startuml
left to right direction
skinparam linetype ortho

entity "TEST_CASE" as TEST_CASE {
  * test_case_id : INTEGER <<not null>>
  --
  テスト項目 : VARCHAR
  期待結果 : VARCHAR
  実際結果 : VARCHAR
  状態 : VARCHAR
  作成日 : VARCHAR
  更新日 : VARCHAR
}

entity "TEST_RESULT" as TEST_RESULT {
  * test_result_id : INTEGER <<not null>>
  --
  テストケース : VARCHAR
  実行日 : VARCHAR
  結果 : VARCHAR
  備考 : VARCHAR
  作成日 : VARCHAR
}

TEST_CASE ||--o{ TEST_RESULT
@enduml


## 日本語版

@startuml
left to right direction
skinparam linetype ortho

entity "テストケース" as TEST_CASE {
  * test_case_id : INTEGER <<not null>>
  --
  テスト項目 : VARCHAR
  期待結果 : VARCHAR
  実際結果 : VARCHAR
}

entity "テスト結果" as TEST_RESULT {
  * test_result_id : INTEGER <<not null>>
  --
  テストケース : VARCHAR
  実行日 : VARCHAR
  結果 : VARCHAR
}

TEST_CASE ||--o{ TEST_RESULT
@enduml

## エンティティ一覧

| エンティティ名 | 日本語名 | 分類 |
|---------------|---------|------|
| TEST_CASE | テストケース | R |
| TEST_RESULT | テスト結果 | E |

## 関係一覧

| 親エンティティ | 子エンティティ | 関係 |
|---------------|---------------|------|
| TEST_CASE | TEST_RESULT | 1対多 |
