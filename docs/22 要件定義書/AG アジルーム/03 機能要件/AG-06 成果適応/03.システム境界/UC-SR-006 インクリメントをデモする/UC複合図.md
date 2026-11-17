# UC複合図

成果適応業務領域で扱うユースケース、画面、イベント、情報、条件を関連づけた図です。

## 1. 目的

本書は、AG-06 成果適応業務領域のシステム境界レイヤーで扱うユースケース「インクリメントをデモする」に関するUC複合図です。

## 2. PlantUML UC複合図（英語版）

```plantuml
title [Outcome Adaptation] UC Composite Diagram - DemonstrateIncrement
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

actor "Developer\n(ACT-04)" as ACT4

rectangle "Frontend" {
  usecase "SR-03 Sprint Review Execution" as SR03
}

rectangle "Outcome Adaptation Module" {
  usecase "UC-SR-006 DemonstrateIncrement" as UC
}

ACT4 -- SR03
SR03 ..> UC : Call
@enduml
```

## 3. 日本語版 UC複合図

```plantuml
title UC複合図（日本語版）- インクリメントをデモする
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

actor "開発者\n(ACT-04)" as ACT4

rectangle "フロントエンド" {
  usecase "SR-03 Sprint Review実施" as SR03
}

rectangle "成果適応モジュール" {
  usecase "UC-SR-006 インクリメントをデモする" as UC
}

ACT4 -- SR03
SR03 ..> UC : 呼び出し
@enduml
```

## 4. ユースケース一覧

| ユースケースID | ユースケース名          | 英語名             | 説明                                             | 関連アクター                   |
|----------------|-------------------------|--------------------|--------------------------------------------------|--------------------------------|
| UC-SR-006      | インクリメントをデモする | DemonstrateIncrement | 完成したインクリメントやデモ対象をデモし、説明や実施結果を記録する | ACT-04 |

## 5. 画面一覧

| 画面ID  | 画面名  | 英語名 | 説明       | 関連ユースケース |
|---------|---------|--------|------------|------------------|
| SR-03   | Sprint Review実施 | Sprint Review Execution | Sprint Reviewの実施画面 | UC-SR-006        |

## 6. イベント一覧

| イベントID | イベント名                 | 説明                                                         | 関連情報  |
|------------|----------------------------|--------------------------------------------------------------|-----------|
| EVT-SR-006 | インクリメントがデモされた  | 完成したインクリメントがデモされ、実施結果が記録された         | INF-SR-04 |

## 7. 情報一覧

| 情報ID     | 情報名          | 説明                                   | 操作種類 |
|------------|-----------------|----------------------------------------|----------|
| INF-SR-04 | デモ対象インクリメント | デモ対象となるインクリメント、説明、実施結果 | 更新     |

## 8. 条件一覧

| 条件ID | 条件名 | 説明 |
|--------|--------|------|
| 1 | Definition of Done | DoDを満たしていない作業をデモ対象として登録できない |
| 2 | デモ記録 | デモの説明や実施結果を記録できること |

## 9. 参照情報

- ユースケース一覧: `01.ユースケース/ユースケース一覧.md`
- 画面一覧: `04.システム/04.画面/画面一覧.md`
- イベント一覧: `04.システム/03.イベント/イベント一覧.md`
- 情報一覧: `04.システム/01.情報/情報一覧.md`
