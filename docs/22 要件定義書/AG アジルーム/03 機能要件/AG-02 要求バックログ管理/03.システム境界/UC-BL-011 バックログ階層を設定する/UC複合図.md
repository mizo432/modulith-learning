# UC複合図

要求バックログ管理業務領域で扱うユースケース、画面、イベント、情報、条件を関連づけた図です。

## 1. 目的

本書は、AG-02 要求バックログ管理業務領域のシステム境界レイヤーで扱うユースケース「バックログ階層を設定する」に関するUC複合図です。

## 2. PlantUML UC複合図（英語版）

```plantuml
title [Backlog Management] UC Composite Diagram - SetBacklog
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
  usecase "SCR-008 Epic" as SCR
}

rectangle "Backlog Management Module" {
  usecase "UC-BL-011 SetBacklog" as UC
}

rectangle "System Layer" {
  card "INF-BL-06\nBacklog Hierarchy" as INF06
}

ACT1 -- SCR
ACT2 -- SCR
SCR ..> UC : Call
UC -- INF06 : Use
@enduml
```

## 3. 日本語版 UC複合図

```plantuml
title UC複合図（日本語版）- バックログ階層を設定する
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
  usecase "SCR-008 Epic一覧・詳細" as SCR
}

rectangle "要求バックログ管理モジュール" {
  usecase "UC-BL-011 バックログ階層を設定する" as UC
}

rectangle "システムレイヤー" {
  card "INF-BL-06\nバックログ階層" as INF06
}

ACT1 -- SCR
ACT2 -- SCR
SCR ..> UC : 呼び出し
UC -- INF06 : 使用
@enduml
```

## 4. ユースケース一覧

| ユースケースID | ユースケース名           | 英語名     | 説明                                                       | 関連アクター   |
|----------------|--------------------------|------------|------------------------------------------------------------|----------------|
| UC-BL-011      | バックログ階層を設定する | SetBacklog | Product Goal、Epic、Feature、Story、Task間の関係を設定する | ACT-02, ACT-04 |

## 5. 画面一覧

| 画面ID  | 画面名         | 英語名 | 説明       | 関連ユースケース |
|---------|----------------|--------|------------|------------------|
| SCR-008 | Epic一覧・詳細 | Epic   | 画面の説明 | UC-BL-011        |

## 6. イベント一覧

| イベントID | イベント名               | 英語名                  | 説明                                     | 関連ユースケース |
|------------|--------------------------|-------------------------|------------------------------------------|------------------|
| EVT-BL-008 | バックログ階層が設定された | Backlog Hierarchy Set | Product Goal、Epic、Feature、Story間の関係が設定された | UC-BL-011 |

## 7. 情報一覧

| 情報ID  | 情報名         | 英語名            | 情報種類 | 説明                                     | 関連ユースケース |
|---------|----------------|-------------------|----------|------------------------------------------|------------------|
| INF-BL-06 | バックログ階層 | Backlog Hierarchy | 対応表   | Product Goal、Epic、Feature、Story間の階層関係 | UC-BL-011      |

## 8. 条件一覧

条件なし

## 9. 参照情報

- ユースケース一覧: `01.ユースケース/ユースケース一覧.md`
- 画面一覧: `04.システム/04.画面/画面一覧.md`
- イベント一覧: `04.システム/03.イベント/イベント一覧.md`
- 情報一覧: `04.システム/01.情報/情報一覧.md`
