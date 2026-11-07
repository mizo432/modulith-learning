# Sprint Review ステートチャート

Sprint Review情報の状態遷移を示します。

```plantuml
@startuml
skinparam state {
  BackgroundColor White
  BorderColor Black
}

state "準備中" as preparing
state "開催中" as inprogress
state "完了" as completed

[*] --> preparing
preparing --> inprogress
inprogress --> completed

note right of preparing
  Reviewの準備中の状態
end note

note right of inprogress
  Reviewが開催中の状態
end note

note right of completed
  Reviewが完了した状態
end note
@enduml
```

## 状態一覧

| 状態ID | 状態名 | 説明 |
|--------|--------|------|
| ST-MF-005 | 準備中 | Reviewの準備中の状態 |
| ST-MF-006 | 開催中 | Reviewが開催中の状態 |
| ST-MF-007 | 完了 | Reviewが完了した状態 |
