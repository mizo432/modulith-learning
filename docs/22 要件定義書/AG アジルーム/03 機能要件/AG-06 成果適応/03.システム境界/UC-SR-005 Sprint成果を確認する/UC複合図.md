# UC複合図

成果適応業務領域で扱うユースケース、画面、イベント、情報、条件を関連づけた図です。

## 1. 目的

本書は、AG-06 成果適応業務領域のシステム境界レイヤーで扱うユースケース「Sprint成果を確認する」に関するUC複合図です。

## 2. PlantUML UC複合図（英語版）

```plantuml
title [Outcome Adaptation] UC Composite Diagram - ConfirmSprintOutcomes
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

actor "Employee\n(ACT-01)" as ACT1
actor "Product Owner\n(ACT-02)" as ACT2
actor "Scrum Master\n(ACT-03)" as ACT3
actor "Developer\n(ACT-04)" as ACT4
actor "Stakeholder\n(ACT-05)" as ACT5

rectangle "Frontend" {
  usecase "SR-03 Sprint Review Execution" as SR03
}

rectangle "Outcome Adaptation Module" {
  usecase "UC-SR-005 ConfirmSprintOutcomes" as UC
}

ACT1 -- SR03
ACT2 -- SR03
ACT3 -- SR03
ACT4 -- SR03
ACT5 -- SR03
SR03 ..> UC : Call
@enduml
```

## 3. 日本語版 UC複合図

```plantuml
title UC複合図（日本語版）- Sprint成果を確認する
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

actor "従業員\n(ACT-01)" as ACT1
actor "プロダクトオーナー\n(ACT-02)" as ACT2
actor "スクラムマスター\n(ACT-03)" as ACT3
actor "開発者\n(ACT-04)" as ACT4
actor "ステークホルダー\n(ACT-05)" as ACT5

rectangle "フロントエンド" {
  usecase "SR-03 Sprint Review実施" as SR03
}

rectangle "成果適応モジュール" {
  usecase "UC-SR-005 Sprint成果を確認する" as UC
}

ACT1 -- SR03
ACT2 -- SR03
ACT3 -- SR03
ACT4 -- SR03
ACT5 -- SR03
SR03 ..> UC : 呼び出し
@enduml
```

## 4. ユースケース一覧

| ユースケースID | ユースケース名          | 英語名             | 説明                                             | 関連アクター                   |
|----------------|-------------------------|--------------------|--------------------------------------------------|--------------------------------|
| UC-SR-005      | Sprint成果を確認する | ConfirmSprintOutcomes | Sprint Goalと達成状況、完了・未完了Story、完成したインクリメントを確認する | ACT-01, ACT-02, ACT-03, ACT-04, ACT-05 |

## 5. 画面一覧

| 画面ID  | 画面名  | 英語名 | 説明       | 関連ユースケース |
|---------|---------|--------|------------|------------------|
| SR-03   | Sprint Review実施 | Sprint Review Execution | Sprint Reviewの実施画面 | UC-SR-005        |

## 6. イベント一覧

| イベントID | イベント名                 | 説明                                                         | 関連情報  |
|------------|----------------------------|--------------------------------------------------------------|-----------|
| EVT-SR-005 | Sprint成果が確認された  | Sprint Goalと達成状況、完了・未完了Storyが確認された           | INF-SR-01 |

## 7. 情報一覧

| 情報ID     | 情報名          | 説明                                   | 操作種類 |
|------------|-----------------|----------------------------------------|----------|
| INF-SR-01 | Sprint Review | Reviewの対象Sprint、開催日時、状態、Sprint Goal達成状況 | 参照     |
| INF-SR-04 | デモ対象インクリメント | デモ対象となるインクリメント、説明、実施結果 | 参照     |
| INF-SR-05 | Reviewフィードバック | フィードバック内容、発言者、分類、関連Story、添付資料 | 参照     |

## 8. 条件一覧

| 条件ID | 条件名 | 説明 |
|--------|--------|------|
| 1 | Definition of Done | DoDを満たしていない作業を完成したインクリメントとして扱わない |
| 2 | 未完了Story | 未完了Storyは完了扱いにせず、Product Backlogに戻して再検討する |
| 3 | 計画との比較 | Sprint Planning時点の計画と実際の結果を比較できること |

## 9. 参照情報

- ユースケース一覧: `01.ユースケース/ユースケース一覧.md`
- 画面一覧: `04.システム/04.画面/画面一覧.md`
- イベント一覧: `04.システム/03.イベント/イベント一覧.md`
- 情報一覧: `04.システム/01.情報/情報一覧.md`
