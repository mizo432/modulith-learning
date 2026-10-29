# UC複合図

品質検証業務領域で扱うユースケース、画面、イベント、情報、条件を関連づけた図です。

## 1. 目的

本書は、AG-05 品質検証業務領域のシステム境界レイヤーで扱うユースケース「Definition of
Doneを参照する」に関するUC複合図です。

## 2. PlantUML UC複合図（英語版）

```plantuml
title [Quality Verification] UC Composite Diagram - ViewDefinitionOfDone
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
actor "Tracker\n(ACT-05)" as ACT5

rectangle "Frontend" {
  usecase "SCR-019 Release Readiness" as SCR
}

rectangle "Quality Verification Module" {
  usecase "UC-QL-003 ViewDefinitionOfDone" as UC
}

ACT1 -- SCR
ACT2 -- SCR
ACT3 -- SCR
ACT4 -- SCR
ACT5 -- SCR
SCR ..> UC : Call
@enduml
```

## 3. 日本語版 UC複合図

```plantuml
title UC複合図（日本語版）- Definition of Doneを参照する
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
actor "トラッキング担当\n(ACT-05)" as ACT5

rectangle "フロントエンド" {
  usecase "SCR-019 Release Readiness" as SCR
}

rectangle "品質検証モジュール" {
  usecase "UC-QL-003 Definition of Doneを参照する" as UC
}

ACT1 -- SCR
ACT2 -- SCR
ACT3 -- SCR
ACT4 -- SCR
ACT5 -- SCR
SCR ..> UC : 呼び出し
@enduml
```

## 4. ユースケース一覧

| ユースケースID | ユースケース名               | 英語名               | 説明                     | 関連アクター                           |
|----------------|------------------------------|----------------------|--------------------------|----------------------------------------|
| UC-QL-003      | Definition of Doneを参照する | ViewDefinitionOfDone | 現在の完成基準を確認する | ACT-01, ACT-02, ACT-03, ACT-04, ACT-05 |

## 5. 画面一覧

| 画面ID  | 画面名            | 英語名            | 説明       | 関連ユースケース |
|---------|-------------------|-------------------|------------|------------------|
| SCR-019 | Release Readiness | Release Readiness | 画面の説明 | UC-QL-003        |

## 6. イベント一覧

イベントなし

## 7. 情報一覧

| 情報ID     | 情報名             | 説明                                   | 操作種類 |
|------------|--------------------|----------------------------------------|----------|
| INF-QL-01 | Definition of Done | チームの完成基準                       | 参照     |

## 8. 条件一覧

条件なし

## 9. 参照情報

- ユースケース一覧: `01.ユースケース/ユースケース一覧.md`
- 画面一覧: `04.システム/04.画面/画面一覧.md`
- イベント一覧: `04.システム/03.イベント/イベント一覧.md`
- 情報一覧: `04.システム/01.情報/情報一覧.md`
