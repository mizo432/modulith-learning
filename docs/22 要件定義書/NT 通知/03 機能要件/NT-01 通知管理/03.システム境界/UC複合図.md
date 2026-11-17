# NT-01 通知管理 UC複合図

## ユースケース複合図

@startuml
left to right direction

actor "システム管理者" as システム管理者
actor "システム" as システム

usecase "UC-NT-001" as UC_NT_001
usecase "UC-NT-004" as UC_NT_004
usecase "UC-NT-006" as UC_NT_006
usecase "UC-NT-005" as UC_NT_005
usecase "UC-NT-003" as UC_NT_003
usecase "UC-NT-002" as UC_NT_002

boundary "通知" as NOTIFICATION
boundary "通知チャネル" as NOTIFICATION_CHANNEL

システム管理者 --> UC_NT_001
システム管理者 --> UC_NT_004
システム管理者 --> UC_NT_006
システム --> UC_NT_001
システム --> UC_NT_004
システム --> UC_NT_006

UC_NT_001 --> NOTIFICATION
UC_NT_001 --> NOTIFICATION_CHANNEL
UC_NT_004 --> NOTIFICATION
UC_NT_004 --> NOTIFICATION_CHANNEL
UC_NT_006 --> NOTIFICATION
UC_NT_006 --> NOTIFICATION_CHANNEL
UC_NT_005 --> NOTIFICATION
UC_NT_005 --> NOTIFICATION_CHANNEL
UC_NT_003 --> NOTIFICATION
UC_NT_003 --> NOTIFICATION_CHANNEL
UC_NT_002 --> NOTIFICATION
UC_NT_002 --> NOTIFICATION_CHANNEL
@enduml

## ユースケース一覧

| UC ID | UC名 | 関連アクター | 関連情報 |
|-------|------|-------------|---------|
| UC-NT-001 | UC-NT-001 | システム管理者;システム | 通知;通知チャネル |
| UC-NT-004 | UC-NT-004 | システム管理者;システム | 通知;通知チャネル |
| UC-NT-006 | UC-NT-006 | システム管理者;システム | 通知;通知チャネル |
| UC-NT-005 | UC-NT-005 | システム管理者;システム | 通知;通知チャネル |
| UC-NT-003 | UC-NT-003 | システム管理者;システム | 通知;通知チャネル |
| UC-NT-002 | UC-NT-002 | システム管理者;システム | 通知;通知チャネル |
