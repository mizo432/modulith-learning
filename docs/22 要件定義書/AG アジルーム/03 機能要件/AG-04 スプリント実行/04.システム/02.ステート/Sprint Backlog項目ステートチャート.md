# Sprint Backlog項目 ステートチャート

Sprint Backlog項目情報の状態遷移を示します。

```plantuml
@startuml
skinparam state {
  BackgroundColor White
  BorderColor Black
}

state "未着手" as notstarted
state "作業中" as working
state "レビュー中" as reviewing
state "完了" as completed

[*] --> notstarted
notstarted --> working
working --> reviewing
reviewing --> completed

note right of notstarted
  作業が開始されていない
end note

note right of working
  実装・設計が進行中
end note

note right of reviewing
  コードレビューやテスト中
end note

note right of completed
  作業が完了し、Doneの基準を満たした
end note
@enduml
```

## 状態一覧

| 状態ID | 状態名 | 説明 |
|--------|--------|------|
| ST-SP-05 | 未着手 | 作業が開始されていない |
| ST-SP-06 | 作業中 | 実装・設計が進行中 |
| ST-SP-07 | レビュー中 | コードレビューやテスト中 |
| ST-SP-08 | 完了 | 作業が完了し、Doneの基準を満たした |
