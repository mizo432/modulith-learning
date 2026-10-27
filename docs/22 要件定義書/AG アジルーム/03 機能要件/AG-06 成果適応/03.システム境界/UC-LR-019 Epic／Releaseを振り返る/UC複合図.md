# UC複合図

成果適応業務領域で扱うユースケース、画面、イベント、情報、条件を関連づけた図です。

## 1. 目的

本書は、AG-06 成果適応業務領域のシステム境界レイヤーで扱うユースケース「Epic／Releaseを振り返る」に関するUC複合図です。

## 2. PlantUML UC複合図（英語版）

```plantuml
title [Outcome Adaptation] UC Composite Diagram - RetrospectEpicRelease
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
  usecase "SCR-023 Epic / Release Retrospective" as SCR
}

rectangle "Outcome Adaptation Module" {
  usecase "UC-LR-019 RetrospectEpicRelease" as UC
}

ACT1 -- SCR
ACT2 -- SCR
ACT3 -- SCR
SCR ..> UC : Call
@enduml
```

## 3. 日本語版 UC複合図

```plantuml
title UC複合図（日本語版）- Epic／Releaseを振り返る
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
  usecase "SCR-023 Epic / Release Retrospective" as SCR
}

rectangle "成果適応モジュール" {
  usecase "UC-LR-019 Epic／Releaseを振り返る" as UC
}

ACT1 -- SCR
ACT2 -- SCR
ACT3 -- SCR
SCR ..> UC : 呼び出し
@enduml
```

## 4. ユースケース一覧

| ユースケースID | ユースケース名          | 英語名                | 説明                                         | 関連アクター           |
|----------------|-------------------------|-----------------------|----------------------------------------------|------------------------|
| UC-LR-019      | Epic／Releaseを振り返る | RetrospectEpicRelease | 複数スプリントにまたがる成果と学びを振り返る | ACT-02, ACT-03, ACT-04 |

## 5. 画面一覧

| 画面ID  | 画面名                       | 英語名                       | 説明       | 関連ユースケース |
|---------|------------------------------|------------------------------|------------|------------------|
| SCR-023 | Epic / Release Retrospective | Epic / Release Retrospective | 画面の説明 | UC-LR-019        |

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
