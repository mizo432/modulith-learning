# UC複合図

成果適応業務領域で扱うユースケース、画面、イベント、情報、条件を関連づけた図です。

## 1. 目的

本書は、AG-06 成果適応業務領域のシステム境界レイヤーで扱うユースケース「Sprint Reviewを開始する」に関するUC複合図です。

## 2. PlantUML UC複合図（英語版）

```plantuml
title [Outcome Adaptation] UC Composite Diagram - StartSprintReview
left to right direction

skinparam usecase {
  BackgroundColor #F8FAFC
  BorderColor #475569
  FontColor #0F172A
}
skinparam actor {
  BackgroundColor #F8FAFC
  BorderColor #475569
  FontColor #0F172A
}
skinparam rectangle {
  BackgroundColor #F8FAFC
  BorderColor #475569
  FontColor #0F172A
}
skinparam arrowColor #475569
skinparam shadowing false

actor "Product Owner\n(ACT-02)" as ACT2
actor "Scrum Master\n(ACT-03)" as ACT3

rectangle "Frontend" {
  usecase "SR-01 Sprint Review List" as SR01
}

rectangle "Outcome Adaptation Module" {
  usecase "UC-SR-001 StartSprintReview" as UC
}

ACT2 -- SR01
ACT3 -- SR01
SR01 ..> UC : Call
@enduml
```

## 3. 日本語版 UC複合図

```plantuml
title UC複合図（日本語版）- Sprint Reviewを開始する
left to right direction

skinparam usecase {
  BackgroundColor #F8FAFC
  BorderColor #475569
  FontColor #0F172A
}
skinparam actor {
  BackgroundColor #F8FAFC
  BorderColor #475569
  FontColor #0F172A
}
skinparam rectangle {
  BackgroundColor #F8FAFC
  BorderColor #475569
  FontColor #0F172A
}
skinparam arrowColor #475569
skinparam shadowing false

actor "プロダクトオーナー\n(ACT-02)" as ACT2
actor "スクラムマスター\n(ACT-03)" as ACT3

rectangle "フロントエンド" {
  usecase "SR-01 Sprint Review一覧" as SR01
}

rectangle "成果適応モジュール" {
  usecase "UC-SR-001 Sprint Reviewを開始する" as UC
}

ACT2 -- SR01
ACT3 -- SR01
SR01 ..> UC : 呼び出し
@enduml
```

## 4. ユースケース一覧

| ユースケースID | ユースケース名          | 英語名             | 説明                                             | 関連アクター                   |
|----------------|-------------------------|--------------------|--------------------------------------------------|--------------------------------|
| UC-SR-001      | Sprint Reviewを開始する | StartSprintReview  | 対象Sprintを選択し、Sprint Reviewの準備状態を確認して開始する | ACT-02, ACT-03 |

## 5. 画面一覧

| 画面ID  | 画面名  | 英語名 | 説明       | 関連ユースケース |
|---------|---------|--------|------------|------------------|
| SR-01   | Sprint Review一覧 | Sprint Review List | Sprint Reviewの一覧表示画面 | UC-SR-001        |

## 6. イベント一覧

| イベントID | イベント名                 | 説明                                                         | 関連情報  |
|------------|----------------------------|--------------------------------------------------------------|-----------|
| EVT-SR-001 | Sprint Reviewが開始された  | 対象SprintのSprint Reviewが開始された                        | INF-SR-01 |

## 7. 情報一覧

| 情報ID     | 情報名          | 説明                                   | 操作種類 |
|------------|-----------------|----------------------------------------|----------|
| INF-SR-01 | Sprint Review | Reviewの対象Sprint、開催日時、状態、Sprint Goal達成状況 | 作成     |
| INF-LR-01 | Sprint Review記録 | 成果、フィードバック、決定、未解決事項 | 参照     |

## 8. 条件一覧

| 条件ID | 条件名 | 説明 |
|--------|--------|------|
| 1 | 対象Sprint | 終了したSprintに対してのみSprint Reviewを開始できる |
| 2 | 重複防止 | 既にSprint Reviewが存在するSprintに対しては重複作成できない |
| 3 | 権限 | POまたはSMのみがSprint Reviewを開始できる |

## 9. 参照情報

- ユースケース一覧: `01.ユースケース/ユースケース一覧.md`
- 画面一覧: `04.システム/04.画面/画面一覧.md`
- イベント一覧: `04.システム/03.イベント/イベント一覧.md`
- 情報一覧: `04.システム/01.情報/情報一覧.md`
