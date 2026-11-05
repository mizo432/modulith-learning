# Sprint Review ステートチャート

Sprint Review情報の状態遷移を示します。

```plantuml
@startuml
skinparam state {
  BackgroundColor White
  BorderColor Black
}

state "未準備" as unprepared
state "準備中" as preparing
state "準備完了" as prepared
state "開催中" as inprogress
state "完了" as completed

[*] --> unprepared
unprepared --> preparing
preparing --> prepared
prepared --> inprogress
inprogress --> completed

note right of unprepared
  Sprint Reviewが計画されたが、まだ準備されていない
end note

note right of preparing
  Sprint Reviewの準備が進行中
end note

note right of prepared
  Sprint Reviewの準備が完了した
end note

note right of inprogress
  Sprint Reviewが進行中
end note

note right of completed
  Sprint Reviewが完了した
end note
@enduml
```

## 状態一覧

| 状態ID | 状態名 | 説明 |
|--------|--------|------|
| ST-SR-01 | 未準備 | Sprint Reviewが計画されたが、まだ準備されていない |
| ST-SR-02 | 準備中 | Sprint Reviewの準備が進行中 |
| ST-SR-03 | 準備完了 | Sprint Reviewの準備が完了した |
| ST-SR-04 | 開催中 | Sprint Reviewが進行中 |
| ST-SR-05 | 完了 | Sprint Reviewが完了した |
