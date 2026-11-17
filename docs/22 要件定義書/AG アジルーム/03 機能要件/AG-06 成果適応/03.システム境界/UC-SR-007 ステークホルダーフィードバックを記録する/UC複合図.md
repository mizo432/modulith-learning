# UC複合図

成果適応業務領域で扱うユースケース、画面、イベント、情報、条件を関連づけた図です。

## 1. 目的

本書は、AG-06 成果適応業務領域のシステム境界レイヤーで扱うユースケース「ステークホルダーフィードバックを記録する」に関するUC複合図です。

## 2. PlantUML UC複合図（英語版）

```plantuml
title [Outcome Adaptation] UC Composite Diagram - RecordStakeholderFeedback
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
actor "Stakeholder\n(ACT-05)" as ACT5

rectangle "Frontend" {
  usecase "SR-03 Sprint Review Execution" as SR03
}

rectangle "Outcome Adaptation Module" {
  usecase "UC-SR-007 RecordStakeholderFeedback" as UC
}

ACT2 -- SR03
ACT3 -- SR03
ACT5 -- SR03
SR03 ..> UC : Call
@enduml
```

## 3. 日本語版 UC複合図

```plantuml
title UC複合図（日本語版）- ステークホルダーフィードバックを記録する
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
actor "ステークホルダー\n(ACT-05)" as ACT5

rectangle "フロントエンド" {
  usecase "SR-03 Sprint Review実施" as SR03
}

rectangle "成果適応モジュール" {
  usecase "UC-SR-007 ステークホルダーフィードバックを記録する" as UC
}

ACT2 -- SR03
ACT3 -- SR03
ACT5 -- SR03
SR03 ..> UC : 呼び出し
@enduml
```

## 4. ユースケース一覧

| ユースケースID | ユースケース名          | 英語名             | 説明                                             | 関連アクター                   |
|----------------|-------------------------|--------------------|--------------------------------------------------|--------------------------------|
| UC-SR-007      | ステークホルダーフィードバックを記録する | RecordStakeholderFeedback | ステークホルダーからのフィードバックを記録し、発言者・日時・関連Storyを紐付ける | ACT-02, ACT-03, ACT-05 |

## 5. 画面一覧

| 画面ID  | 画面名  | 英語名 | 説明       | 関連ユースケース |
|---------|---------|--------|------------|------------------|
| SR-03   | Sprint Review実施 | Sprint Review Execution | Sprint Reviewの実施画面 | UC-SR-007        |

## 6. イベント一覧

| イベントID | イベント名                 | 説明                                                         | 関連情報  |
|------------|----------------------------|--------------------------------------------------------------|-----------|
| EVT-SR-007 | フィードバックが記録された  | ステークホルダーからのフィードバックが記録された               | INF-SR-05 |

## 7. 情報一覧

| 情報ID     | 情報名          | 説明                                   | 操作種類 |
|------------|-----------------|----------------------------------------|----------|
| INF-SR-05 | Reviewフィードバック | フィードバック内容、発言者、分類、関連Story、添付資料 | 作成     |

## 8. 条件一覧

| 条件ID | 条件名 | 説明 |
|--------|--------|------|
| 1 | 関連Story | フィードバックは特定のStoryに紐付かない場合もあるため、関連Storyは任意 |
| 2 | 複数紐付け | フィードバックは複数のStory・Feature・Epicに関連付けられる |
| 3 | 発言者 | 発言者は必須入力 |

## 9. 参照情報

- ユースケース一覧: `01.ユースケース/ユースケース一覧.md`
- 画面一覧: `04.システム/04.画面/画面一覧.md`
- イベント一覧: `04.システム/03.イベント/イベント一覧.md`
- 情報一覧: `04.システム/01.情報/情報一覧.md`
