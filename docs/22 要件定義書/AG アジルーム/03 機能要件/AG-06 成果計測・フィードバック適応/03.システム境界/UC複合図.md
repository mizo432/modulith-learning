# AG-06 成果計測・フィードバック適応 UC複合図

## ユースケース複合図

@startuml
left to right direction

actor "プロダクトオーナー" as プロダクトオーナー

usecase "UC-AG-06-002" as UC_AG_06_002
usecase "UC-AG-06-003" as UC_AG_06_003
usecase "UC-AG-06-001" as UC_AG_06_001

boundary "指標" as METRIC
boundary "フィードバック" as FEEDBACK

プロダクトオーナー --> UC_AG_06_002
プロダクトオーナー --> UC_AG_06_003
プロダクトオーナー --> UC_AG_06_001

UC_AG_06_002 --> METRIC
UC_AG_06_002 --> FEEDBACK
UC_AG_06_003 --> METRIC
UC_AG_06_003 --> FEEDBACK
UC_AG_06_001 --> METRIC
UC_AG_06_001 --> FEEDBACK
@enduml

## ユースケース一覧

| UC ID | UC名 | 関連アクター | 関連情報 |
|-------|------|-------------|---------|
| UC-AG-06-002 | UC-AG-06-002 | プロダクトオーナー | 指標;フィードバック |
| UC-AG-06-003 | UC-AG-06-003 | プロダクトオーナー | 指標;フィードバック |
| UC-AG-06-001 | UC-AG-06-001 | プロダクトオーナー | 指標;フィードバック |
