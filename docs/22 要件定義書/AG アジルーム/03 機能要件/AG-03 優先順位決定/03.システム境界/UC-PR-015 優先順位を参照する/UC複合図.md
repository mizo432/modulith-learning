# UC複合図

優先順位決定業務領域で扱うユースケース、画面、イベント、情報、条件を関連づけた図です。

## 1. 目的

本書は、AG-03 優先順位決定業務領域のシステム境界レイヤーで扱うユースケース「優先順位を参照する」に関するUC複合図です。

## 2. PlantUML UC複合図（英語版）

```plantuml
title [Priority Decision] UC Composite Diagram - ViewPriority
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
actor "Tracking\n(ACT-05)" as ACT3

rectangle "Frontend" {
  usecase "SCR-010 ProductBacklog" as SCR
}

rectangle "Priority Decision Module" {
  usecase "UC-PR-015 ViewPriority" as UC
}

rectangle "System Layer" {
  card "INF-PR-02 PriorityScore" as INF
}

ACT1 -- SCR
ACT2 -- SCR
ACT3 -- SCR
SCR ..> UC : Call
UC -- INF : Use
@enduml
```

## 3. 日本語版 UC複合図

```plantuml
title UC複合図（日本語版）- 優先順位を参照する
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
actor "トラッキング担当\n(ACT-05)" as ACT3

rectangle "フロントエンド" {
  usecase "SCR-010 Product Backlog" as SCR
}

rectangle "優先順位決定モジュール" {
  usecase "UC-PR-015 優先順位を参照する" as UC
}

rectangle "システムレイヤー" {
  card "INF-PR-02 優先順位スコア" as INF
}

ACT1 -- SCR
ACT2 -- SCR
ACT3 -- SCR
SCR ..> UC : 呼び出し
UC -- INF : 使用
@enduml
```

## 4. ユースケース一覧

| ユースケースID | ユースケース名       | 英語名          | 説明                               | 関連アクター   |
|----------------|----------------------|-----------------|------------------------------------|----------------|
| UC-PR-015      | 優先順位を参照する   | ViewPriority    | 優先順位の結果を参照・並べ替え・フィルタリングする | ACT-01, ACT-02, ACT-05 |

## 5. 画面一覧

| 画面ID  | 画面名          | 英語名          | 説明       | 関連ユースケース |
|---------|-----------------|-----------------|------------|------------------|
| SCR-010 | Product Backlog | ProductBacklog  | 画面の説明 | UC-PR-015        |

## 6. イベント一覧

関連イベントなし

## 7. 情報一覧

| 情報ID     | 情報名       | 説明                                   | 操作種類 |
|------------|--------------|----------------------------------------|----------|
| INF-PR-02 | 優先順位スコア | 評価結果に基づく優先順位スコア         | 参照     |

## 8. 条件一覧

条件なし

## 9. 参照情報

- ユースケース一覧: `01.ユースケース/ユースケース一覧.md`
- 画面一覧: `04.システム/04.画面/画面一覧.md`
- イベント一覧: `04.システム/03.イベント/イベント一覧.md`
- 情報一覧: `04.システム/01.情報/情報一覧.md`
