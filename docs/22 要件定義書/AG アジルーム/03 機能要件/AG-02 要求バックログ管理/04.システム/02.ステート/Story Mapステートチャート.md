# Story Map ステートチャート

Story Map情報の状態遷移を示します。

```plantuml
@startuml
skinparam state {
  BackgroundColor White
  BorderColor Black
}

state "下書き" as draft
state "編集中" as editing
state "公開済み" as published

[*] --> draft
draft --> editing
editing --> published

note right of draft
  作成されたが、まだ編集されていない
end note

note right of editing
  現在編集中
end note

note right of published
  公開されて共有中
end note
@enduml
```

## 状態一覧

| 状態ID | 状態名 | 説明 |
|--------|--------|------|
| ST-BL-09 | 下書き | 作成されたが、まだ編集されていない |
| ST-BL-10 | 編集中 | 現在編集中 |
| ST-BL-11 | 公開済み | 公開されて共有中 |
