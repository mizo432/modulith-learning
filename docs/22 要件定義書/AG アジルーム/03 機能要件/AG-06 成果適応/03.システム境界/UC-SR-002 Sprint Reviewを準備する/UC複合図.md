# UC複合図

成果適応業務領域で扱うユースケース、画面、イベント、情報、条件を関連づけた図です。

## 1. 目的

本書は、AG-06 成果適応業務領域のシステム境界レイヤーで扱うユースケース「Sprint Reviewを準備する」に関するUC複合図です。

## 2. PlantUML UC複合図（英語版）

```plantuml
title [Outcome Adaptation] UC Composite Diagram - PrepareSprintReview
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
actor "Developer\n(ACT-04)" as ACT4

rectangle "Frontend" {
  usecase "SR-02 Sprint Review Preparation" as SR02
}

rectangle "Outcome Adaptation Module" {
  usecase "UC-SR-002 PrepareSprintReview" as UC
}

ACT2 -- SR02
ACT3 -- SR02
ACT4 -- SR02
SR02 ..> UC : Call
@enduml
```

## 3. 日本語版 UC複合図

```plantuml
title UC複合図（日本語版）- Sprint Reviewを準備する
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
actor "開発者\n(ACT-04)" as ACT4

rectangle "フロントエンド" {
  usecase "SR-02 Sprint Review準備" as SR02
}

rectangle "成果適応モジュール" {
  usecase "UC-SR-002 Sprint Reviewを準備する" as UC
}

ACT2 -- SR02
ACT3 -- SR02
ACT4 -- SR02
SR02 ..> UC : 呼び出し
@enduml
```

## 4. ユースケース一覧

| ユースケースID | ユースケース名          | 英語名             | 説明                                             | 関連アクター                   |
|----------------|-------------------------|--------------------|--------------------------------------------------|--------------------------------|
| UC-SR-002      | Sprint Reviewを準備する | PrepareSprintReview | Sprint Goal、Sprint Backlog、完了・未完了Story、デモ対象、参加者、議題を準備する | ACT-02, ACT-03, ACT-04 |

## 5. 画面一覧

| 画面ID  | 画面名  | 英語名 | 説明       | 関連ユースケース |
|---------|---------|--------|------------|------------------|
| SR-02   | Sprint Review準備 | Sprint Review Preparation | Sprint Reviewの準備画面 | UC-SR-002        |

## 6. イベント一覧

| イベントID | イベント名                 | 説明                                                         | 関連情報  |
|------------|----------------------------|--------------------------------------------------------------|-----------|
| EVT-SR-002 | Sprint Reviewが準備された  | Sprint Reviewの準備情報が保存された                           | INF-SR-01 |

## 7. 情報一覧

| 情報ID     | 情報名          | 説明                                   | 操作種類 |
|------------|-----------------|----------------------------------------|----------|
| INF-SR-01 | Sprint Review | Reviewの対象Sprint、開催日時、状態、Sprint Goal達成状況 | 更新     |
| INF-SR-02 | Review参加者 | 参加者、役割、参加状況 | 参照     |
| INF-SR-03 | Review議題 | 議題、順序、説明 | 参照     |
| INF-SR-04 | デモ対象インクリメント | デモ対象となるインクリメント、説明、実施結果 | 参照     |

## 8. 条件一覧

| 条件ID | 条件名 | 説明 |
|--------|--------|------|
| 1 | 計画との比較 | Sprint Planning時点の計画と実際の結果を比較できること |
| 2 | 未完了Story | 未完了Storyは完了扱いにせず、理由を記録すること |
| 3 | Definition of Done | DoDを満たしていない作業を完成したインクリメントとして扱わない |

## 9. 参照情報

- ユースケース一覧: `01.ユースケース/ユースケース一覧.md`
- 画面一覧: `04.システム/04.画面/画面一覧.md`
- イベント一覧: `04.システム/03.イベント/イベント一覧.md`
- 情報一覧: `04.システム/01.情報/情報一覧.md`
