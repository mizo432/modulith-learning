# AG-05 品質検証 UC複合図

## ユースケース複合図

@startuml
left to right direction

actor "テスト担当者" as テスト担当者

usecase "UC-AG-05-003" as UC_AG_05_003
usecase "UC-AG-05-002" as UC_AG_05_002
usecase "UC-AG-05-001" as UC_AG_05_001

boundary "テストケース" as TEST_CASE
boundary "テスト結果" as TEST_RESULT

テスト担当者 --> UC_AG_05_003
テスト担当者 --> UC_AG_05_002
テスト担当者 --> UC_AG_05_001

UC_AG_05_003 --> TEST_CASE
UC_AG_05_003 --> TEST_RESULT
UC_AG_05_002 --> TEST_CASE
UC_AG_05_002 --> TEST_RESULT
UC_AG_05_001 --> TEST_CASE
UC_AG_05_001 --> TEST_RESULT
@enduml

## ユースケース一覧

| UC ID | UC名 | 関連アクター | 関連情報 |
|-------|------|-------------|---------|
| UC-AG-05-003 | UC-AG-05-003 | テスト担当者 | テストケース;テスト結果 |
| UC-AG-05-002 | UC-AG-05-002 | テスト担当者 | テストケース;テスト結果 |
| UC-AG-05-001 | UC-AG-05-001 | テスト担当者 | テストケース;テスト結果 |
