# UC複合図

スプリント実行業務領域で扱うユースケース、画面、イベント、情報、条件を関連づけた図です。

## 1. 目的

本書は、AG-04 スプリント実行業務領域のシステム境界レイヤーで扱うユースケース「スプリントを更新する」に関するUC複合図です。

## 2. PlantUML UC複合図（英語版）

```plantuml
title [Sprint Execution] UC Composite Diagram - UpdateSprint
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

actor "Product Owner\n(ACT-02)" as ACT1
actor "Scrum Master\n(ACT-03)" as ACT2
actor "Developer\n(ACT-04)" as ACT3

rectangle "Frontend" {
  usecase "SCR-013 Sprint" as SCR
}

rectangle "Sprint Execution Module" {
  usecase "UC-SP-002 UpdateSprint" as UC
}

ACT1 -- SCR
ACT2 -- SCR
ACT3 -- SCR
SCR ..> UC : Call
@enduml
```

## 3. 日本語版 UC複合図

```plantuml
title UC複合図（日本語版）- スプリントを更新する
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

actor "プロダクトオーナー\n(ACT-02)" as ACT1
actor "スクラムマスター\n(ACT-03)" as ACT2
actor "開発者\n(ACT-04)" as ACT3

rectangle "フロントエンド" {
  usecase "SCR-013 Sprint一覧・詳細" as SCR
}

rectangle "スプリント実行モジュール" {
  usecase "UC-SP-002 スプリントを更新する" as UC
}

ACT1 -- SCR
ACT2 -- SCR
ACT3 -- SCR
SCR ..> UC : 呼び出し
@enduml
```

## 4. ユースケース一覧

| ユースケースID | ユースケース名       | 英語名       | 説明                                         | 関連アクター           |
|----------------|----------------------|--------------|----------------------------------------------|------------------------|
| UC-SP-002      | スプリントを更新する | UpdateSprint | スプリントの期間・状態・チーム情報を修正する | ACT-02, ACT-03, ACT-04 |

## 5. 画面一覧

| 画面ID  | 画面名           | 英語名 | 説明       | 関連ユースケース |
|---------|------------------|--------|------------|------------------|
| SCR-013 | Sprint一覧・詳細 | Sprint | 画面の説明 | UC-SP-002        |

## 6. イベント一覧

イベントなし

## 7. 情報一覧

情報なし

## 8. 条件一覧

条件なし

## 9. 参照情報

- ユースケース一覧: `01.ユースケース/ユースケース一覧.md`
- 画面一覧: `04.システム/04.画面/画面一覧.md`
- イベント一覧: `04.システム/03.イベント/イベント一覧.md`
- 情報一覧: `04.システム/01.情報/情報一覧.md`
