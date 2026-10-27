# UC複合図

品質検証業務領域で扱うユースケース、画面、イベント、情報、条件を関連づけた図です。

## 1. 目的

本書は、AG-05 品質検証業務領域のシステム境界レイヤーで扱うユースケース「性能要件を定義する」に関するUC複合図です。

## 2. PlantUML UC複合図（英語版）

```plantuml
title [Quality Verification] UC Composite Diagram - DefinePerformance
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
  usecase "SCR-021 PerformanceTest" as SCR
}

rectangle "Quality Verification Module" {
  usecase "UC-QL-013 DefinePerformance" as UC
}

ACT1 -- SCR
SCR ..> UC : Call
@enduml
```

## 3. 日本語版 UC複合図

```plantuml
title UC複合図（日本語版）- 性能要件を定義する
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
  usecase "SCR-021 性能テスト管理" as SCR
}

rectangle "品質検証モジュール" {
  usecase "UC-QL-013 性能要件を定義する" as UC
}

ACT1 -- SCR
SCR ..> UC : 呼び出し
@enduml
```

## 4. ユースケース一覧

| ユースケースID | ユースケース名     | 英語名            | 説明                                          | 関連アクター |
|----------------|--------------------|-------------------|-----------------------------------------------|--------------|
| UC-QL-013      | 性能要件を定義する | DefinePerformance | p95等の性能要件、負荷条件、合格基準を設定する | ACT-04       |

## 5. 画面一覧

| 画面ID  | 画面名         | 英語名          | 説明       | 関連ユースケース |
|---------|----------------|-----------------|------------|------------------|
| SCR-021 | 性能テスト管理 | PerformanceTest | 画面の説明 | UC-QL-013        |

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
