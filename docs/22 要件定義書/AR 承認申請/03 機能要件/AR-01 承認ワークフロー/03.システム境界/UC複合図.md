# AR-01 承認ワークフロー UC複合図

## ユースケース複合図

@startuml
left to right direction

actor "申請者" as 申請者
actor "承認者" as 承認者

usecase "UC-AR-005" as UC_AR_005
usecase "UC-AR-003" as UC_AR_003
usecase "UC-AR-001" as UC_AR_001
usecase "UC-AR-002" as UC_AR_002
usecase "UC-AR-006" as UC_AR_006
usecase "UC-AR-004" as UC_AR_004

boundary "承認リクエスト" as APPROVAL_REQUEST
boundary "承認フロー" as APPROVAL_FLOW

申請者 --> UC_AR_005
申請者 --> UC_AR_003
申請者 --> UC_AR_001
承認者 --> UC_AR_005
承認者 --> UC_AR_003
承認者 --> UC_AR_001

UC_AR_005 --> APPROVAL_REQUEST
UC_AR_005 --> APPROVAL_FLOW
UC_AR_003 --> APPROVAL_REQUEST
UC_AR_003 --> APPROVAL_FLOW
UC_AR_001 --> APPROVAL_REQUEST
UC_AR_001 --> APPROVAL_FLOW
UC_AR_002 --> APPROVAL_REQUEST
UC_AR_002 --> APPROVAL_FLOW
UC_AR_006 --> APPROVAL_REQUEST
UC_AR_006 --> APPROVAL_FLOW
UC_AR_004 --> APPROVAL_REQUEST
UC_AR_004 --> APPROVAL_FLOW
@enduml

## ユースケース一覧

| UC ID | UC名 | 関連アクター | 関連情報 |
|-------|------|-------------|---------|
| UC-AR-005 | UC-AR-005 | 申請者;承認者 | 承認リクエスト;承認フロー |
| UC-AR-003 | UC-AR-003 | 申請者;承認者 | 承認リクエスト;承認フロー |
| UC-AR-001 | UC-AR-001 | 申請者;承認者 | 承認リクエスト;承認フロー |
| UC-AR-002 | UC-AR-002 | 申請者;承認者 | 承認リクエスト;承認フロー |
| UC-AR-006 | UC-AR-006 | 申請者;承認者 | 承認リクエスト;承認フロー |
| UC-AR-004 | UC-AR-004 | 申請者;承認者 | 承認リクエスト;承認フロー |
