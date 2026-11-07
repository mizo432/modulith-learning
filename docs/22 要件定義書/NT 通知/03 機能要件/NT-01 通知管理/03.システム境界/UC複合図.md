# NT-01 通知管理 UC複合図

## ユースケース複合図

```plantuml
@startuml
left to right direction

actor "従業員" as 従業員
note "業務システム" as 業務システム

usecase "UC-NT-001 通知を送信する" as UC_NT_001
usecase "UC-NT-002 通知一覧を取得する" as UC_NT_002
usecase "UC-NT-003 未読通知一覧を取得する" as UC_NT_003
usecase "UC-NT-004 通知を既読にする" as UC_NT_004

boundary "通知" as 通知

業務システム --> UC_NT_001

従業員 --> UC_NT_002
従業員 --> UC_NT_003
従業員 --> UC_NT_004

UC_NT_001 --> 通知
UC_NT_002 --> 通知
UC_NT_003 --> 通知
UC_NT_004 --> 通知
@enduml

```

## ユースケース一覧

| UC ID     | UC名                   | 関連アクター | 関連情報 |
|-----------|------------------------|--------------|----------|
| UC-NT-001 | 通知を送信する         | 業務システム | 通知     |
| UC-NT-002 | 通知一覧を取得する     | 従業員       | 通知     |
| UC-NT-003 | 未読通知一覧を取得する | 従業員       | 通知     |
| UC-NT-004 | 通知を既読にする       | 従業員       | 通知     |
