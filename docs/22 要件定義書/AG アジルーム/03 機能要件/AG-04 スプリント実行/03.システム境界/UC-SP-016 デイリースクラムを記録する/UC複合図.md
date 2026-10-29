# UC複合図

スプリント実行業務領域で扱うユースケース、画面、イベント、情報、条件を関連づけた図です。

## 1. 目的

本書は、AG-04 スプリント実行業務領域のシステム境界レイヤーで扱うユースケース「デイリースクラムを記録する」に関するUC複合図です。

## 2. PlantUML UC複合図（英語版）

```plantuml
title [Sprint Execution] UC Composite Diagram - RecordDailyScrum
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

actor "Developer\n(ACT-04)" as ACT1

rectangle "Frontend" {
  usecase "SCR-016 Daily Scrum" as SCR
}

rectangle "Sprint Execution Module" {
  usecase "UC-SP-016 RecordDailyScrum" as UC
}

ACT1 -- SCR
SCR ..> UC : Call
@enduml
```

## 3. 日本語版 UC複合図

```plantuml
title UC複合図（日本語版）- デイリースクラムを記録する
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

actor "開発者\n(ACT-04)" as ACT1

rectangle "フロントエンド" {
  usecase "SCR-016 Daily Scrum記録" as SCR
}

rectangle "スプリント実行モジュール" {
  usecase "UC-SP-016 デイリースクラムを記録する" as UC
}

ACT1 -- SCR
SCR ..> UC : 呼び出し
@enduml
```

## 4. ユースケース一覧

| ユースケースID | ユースケース名             | 英語名           | 説明                                            | 関連アクター |
|----------------|----------------------------|------------------|-------------------------------------------------|--------------|
| UC-SP-016      | デイリースクラムを記録する | RecordDailyScrum | Sprint Goalに向けた状況確認と計画調整を記録する | ACT-04       |

## 5. 画面一覧

| 画面ID  | 画面名          | 英語名      | 説明       | 関連ユースケース |
|---------|-----------------|-------------|------------|------------------|
| SCR-016 | Daily Scrum記録 | Daily Scrum | 画面の説明 | UC-SP-016        |

## 6. イベント一覧

| イベントID | イベント名             | 説明                                     | 関連情報  |
|------------|------------------------|------------------------------------------|-----------|
| EVT-SP-013 | デイリースクラムが記録された | Sprint Goalに向けた状況確認と計画調整が記録された | INF-SP-03 |

## 7. 情報一覧

| 情報ID   | 情報名         | 説明                         | 操作種類 |
|----------|----------------|------------------------------|----------|
| INF-SP-01 | スプリント     | スプリント期間、状態、チーム | 参照     |
| INF-SP-02 | Sprint Goal   | スプリントの目的と関連項目   | 参照     |
| INF-SP-03 | Sprint Backlog項目 | 選択項目、作業、状態、担当 | 参照     |
| INF-SP-07 | アクティビティ | 作業状態変更、コメント追加など | 作成     |

## 8. 条件一覧

条件なし

## 9. 参照情報

- ユースケース一覧: `01.ユースケース/ユースケース一覧.md`
- 画面一覧: `04.システム/04.画面/画面一覧.md`
- イベント一覧: `04.システム/03.イベント/イベント一覧.md`
- 情報一覧: `04.システム/01.情報/情報一覧.md`
