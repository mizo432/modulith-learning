# UC複合図

要求バックログ管理業務領域で扱うユースケース、画面、イベント、情報、条件を関連づけた図です。

## 1. 目的

本書は、AG-02 要求バックログ管理業務領域のシステム境界レイヤーで扱うユースケース「Story
Mappingを行う」に関するUC複合図です。

## 2. PlantUML UC複合図（英語版）

```plantuml
title [Backlog Management] UC Composite Diagram - PerformStoryMapping
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
actor "Developer\n(ACT-04)" as ACT2

rectangle "Frontend" {
  usecase "SCR-007 ユーザーストーリーマップ" as SCR
}

rectangle "Backlog Management Module" {
  usecase "UC-BL-023 PerformStoryMapping" as UC
}

rectangle "System Layer" {
  card "INF-BL-01\nBacklog Item" as INF01
}

ACT1 -- SCR
ACT2 -- SCR
SCR ..> UC : Call
UC -- INF01 : Use
@enduml
```

## 3. 日本語版 UC複合図

```plantuml
title UC複合図（日本語版）- Story Mappingを行う
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
actor "開発者\n(ACT-04)" as ACT2

rectangle "フロントエンド" {
  usecase "SCR-007 ユーザーストーリーマップ" as SCR
}

rectangle "要求バックログ管理モジュール" {
  usecase "UC-BL-023 Story Mappingを行う" as UC
}

rectangle "システムレイヤー" {
  card "INF-BL-01\nバックログ項目" as INF01
}

ACT1 -- SCR
ACT2 -- SCR
SCR ..> UC : 呼び出し
UC -- INF01 : 使用
@enduml
```

## 4. ユースケース一覧

| ユースケースID | ユースケース名      | 英語名              | 説明                                                   | 関連アクター   |
|----------------|---------------------|---------------------|--------------------------------------------------------|----------------|
| UC-BL-023      | Story Mappingを行う | PerformStoryMapping | ユーザー活動とストーリーを並べ、リリース範囲を検討する | ACT-02, ACT-04 |

## 5. 画面一覧

| 画面ID  | 画面名                   | 英語名                   | 説明       | 関連ユースケース |
|---------|--------------------------|--------------------------|------------|------------------|
| SCR-007 | ユーザーストーリーマップ | ユーザーストーリーマップ | 画面の説明 | UC-BL-023        |

## 6. イベント一覧

| イベントID | イベント名                 | 英語名                    | 説明                                     | 関連ユースケース |
|------------|----------------------------|---------------------------|------------------------------------------|------------------|
| EVT-BL-021 | Story Mappingが完了した    | Story Mapping Completed   | ユーザー活動とストーリーが並べられ、リリース範囲が検討された | UC-BL-023 |

## 7. 情報一覧

| 情報ID  | 情報名       | 英語名        | 情報種類 | 説明                             | 関連ユースケース |
|---------|--------------|---------------|----------|----------------------------------|------------------|
| INF-BL-01 | バックログ項目 | Backlog Item | リソース | Epic、Feature、Story、欠陥などの項目 | UC-BL-023      |

## 8. 条件一覧

条件なし

## 9. 参照情報

- ユースケース一覧: `01.ユースケース/ユースケース一覧.md`
- 画面一覧: `04.システム/04.画面/画面一覧.md`
- イベント一覧: `04.システム/03.イベント/イベント一覧.md`
- 情報一覧: `04.システム/01.情報/情報一覧.md`
