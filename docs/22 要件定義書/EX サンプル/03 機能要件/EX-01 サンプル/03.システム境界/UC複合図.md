# EX-01 サンプル UC複合図

## ユースケース複合図

@startuml
left to right direction

actor "開発者" as 開発者

usecase "UC-EX-002" as UC_EX_002
usecase "UC-EX-001" as UC_EX_001

boundary "サンプル" as SAMPLE

開発者 --> UC_EX_002
開発者 --> UC_EX_001

UC_EX_002 --> SAMPLE
UC_EX_001 --> SAMPLE
@enduml

## ユースケース一覧

| UC ID | UC名 | 関連アクター | 関連情報 |
|-------|------|-------------|---------|
| UC-EX-002 | UC-EX-002 | 開発者 | サンプル |
| UC-EX-001 | UC-EX-001 | 開発者 | サンプル |
