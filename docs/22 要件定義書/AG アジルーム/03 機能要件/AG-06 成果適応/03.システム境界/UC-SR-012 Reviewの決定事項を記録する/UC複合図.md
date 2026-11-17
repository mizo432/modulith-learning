# UC複合図

成果適応業務領域で扱うユースケース、画面、イベント、情報、条件を関連づけた図です。

## 1. 目的

本書は、AG-06 成果適応業務領域のシステム境界レイヤーで扱うユースケース「Reviewの決定事項を記録する」に関するUC複合図です。

## 2. PlantUML UC複合図（英語版）

```plantuml
title [Outcome Adaptation] UC Composite Diagram - RecordReviewDecisions
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
  usecase "SR-03 Sprint Review Execution" as SR03
}

rectangle "Outcome Adaptation Module" {
  usecase "UC-SR-012 RecordReviewDecisions" as UC
}

ACT2 -- SR03
ACT3 -- SR03
SR03 ..> UC : Call
@enduml
```

## 3. 日本語版 UC複合図

```plantuml
title UC複合図（日本語版）- Reviewの決定事項を記録する
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
  usecase "SR-03 Sprint Review実施" as SR03
}

rectangle "成果適応モジュール" {
  usecase "UC-SR-012 Reviewの決定事項を記録する" as UC
}

ACT2 -- SR03
ACT3 -- SR03
SR03 ..> UC : 呼び出し
@enduml
```

## 4. ユースケース一覧

| ユースケースID | ユースケース名          | 英語名             | 説明                                             | 関連アクター                   |
|----------------|-------------------------|--------------------|--------------------------------------------------|--------------------------------|
| UC-SR-012      | Reviewの決定事項を記録する | RecordReviewDecisions | Review中の議論・決定事項を記録し、継続確認事項を整理する | ACT-02, ACT-03 |

## 5. 画面一覧

| 画面ID  | 画面名  | 英語名 | 説明       | 関連ユースケース |
|---------|---------|--------|------------|------------------|
| SR-03   | Sprint Review実施 | Sprint Review Execution | Sprint Reviewの実施画面 | UC-SR-012        |

## 6. イベント一覧

| イベントID | イベント名                 | 説明                                                         | 関連情報  |
|------------|----------------------------|--------------------------------------------------------------|-----------|
| EVT-SR-012 | Reviewの決定事項が記録された  | Review中の議論・決定事項が記録された                           | INF-SR-08 |

## 7. 情報一覧

| 情報ID     | 情報名          | 説明                                   | 操作種類 |
|------------|-----------------|----------------------------------------|----------|
| INF-SR-08 | Review決定事項 | 決定事項、決定理由 | 作成     |
| INF-SR-09 | Reviewアクション | 継続確認事項、担当者、期限、状態 | 作成     |

## 8. 条件一覧

| 条件ID | 条件名 | 説明 |
|--------|--------|------|
| 1 | 決定理由 | 決定事項には決定理由を記録する |
| 2 | 継続確認事項 | 継続確認事項は次のSprint PlanningやRetrospectiveで参照できる |

## 9. 参照情報

- ユースケース一覧: `01.ユースケース/ユースケース一覧.md`
- 画面一覧: `04.システム/04.画面/画面一覧.md`
- イベント一覧: `04.システム/03.イベント/イベント一覧.md`
- 情報一覧: `04.システム/01.情報/情報一覧.md`
