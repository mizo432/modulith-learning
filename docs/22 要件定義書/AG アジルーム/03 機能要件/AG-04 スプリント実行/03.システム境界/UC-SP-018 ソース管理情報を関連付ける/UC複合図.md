# UC複合図

スプリント実行業務領域で扱うユースケース、画面、イベント、情報、条件を関連づけた図です。

## 1. 目的

本書は、AG-04 スプリント実行業務領域のシステム境界レイヤーで扱うユースケース「ソース管理情報を関連付ける」に関するUC複合図です。

## 2. PlantUML UC複合図（英語版）

```plantuml
title [Sprint Execution] UC Composite Diagram - SourceControl
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
  usecase "SCR-015 Sprint Board / Board" as SCR
}

rectangle "Sprint Execution Module" {
  usecase "UC-SP-018 SourceControl" as UC
}

note "ES-03 Source Control System" as ES1

ACT1 -- SCR
SCR ..> UC : Call
UC ..> ES1 : Data Exchange
@enduml
```

## 3. 日本語版 UC複合図

```plantuml
title UC複合図（日本語版）- ソース管理情報を関連付ける
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
  usecase "SCR-015 Sprint Board / タスクボード" as SCR
}

rectangle "スプリント実行モジュール" {
  usecase "UC-SP-018 ソース管理情報を関連付ける" as UC
}

note "ES-03 ソース管理システム" as ES1

ACT1 -- SCR
SCR ..> UC : 呼び出し
UC ..> ES1 : データ交換
@enduml
```

## 4. ユースケース一覧

| ユースケースID | ユースケース名             | 英語名        | 説明                                               | 関連アクター |
|----------------|----------------------------|---------------|----------------------------------------------------|--------------|
| UC-SP-018      | ソース管理情報を関連付ける | SourceControl | コミット・ブランチ・PRをスプリント作業と関連付ける | ACT-04       |

## 5. 画面一覧

| 画面ID  | 画面名                      | 英語名               | 説明       | 関連ユースケース |
|---------|-----------------------------|----------------------|------------|------------------|
| SCR-015 | Sprint Board / タスクボード | Sprint Board / Board | 画面の説明 | UC-SP-018        |

## 6. イベント一覧

| イベントID | イベント名             | 説明                                     | 関連情報  |
|------------|------------------------|------------------------------------------|-----------|
| EVT-SP-014 | ソース管理情報が関連付けられた | コミット・ブランチ・PRがスプリント作業と関連付けられた | INF-SP-03 |

## 7. 情報一覧

| 情報ID   | 情報名         | 説明                         | 操作種類 |
|----------|----------------|------------------------------|----------|
| INF-SP-03 | Sprint Backlog項目 | 選択項目、作業、状態、担当 | 更新     |

## 8. 条件一覧

条件なし

## 9. 参照情報

- ユースケース一覧: `01.ユースケース/ユースケース一覧.md`
- 画面一覧: `04.システム/04.画面/画面一覧.md`
- イベント一覧: `04.システム/03.イベント/イベント一覧.md`
- 情報一覧: `04.システム/01.情報/情報一覧.md`
