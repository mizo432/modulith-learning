# UC複合図

要求バックログ管理業務領域で扱うユースケース、画面、イベント、情報、条件を関連づけた図です。

## 1. 目的

本書は、AG-02 要求バックログ管理業務領域のシステム境界レイヤーで扱うユースケース「バックログ項目を更新する」に関するUC複合図です。

## 2. PlantUML UC複合図（英語版）

```plantuml
title [Backlog Management] UC Composite Diagram - UpdateBacklog
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
  usecase "SCR-011 User Story" as SCR
}

rectangle "Backlog Management Module" {
  usecase "UC-BL-008 UpdateBacklog" as UC
}

rectangle "System Layer" {
  card "INF-BL-01\nBacklog Item" as INF01
  card "INF-BL-11\nStatus" as INF11
}

ACT1 -- SCR
ACT2 -- SCR
SCR ..> UC : Call
UC -- INF01 : Use
UC -- INF11 : Use
@enduml
```

## 3. 日本語版 UC複合図

```plantuml
title UC複合図（日本語版）- バックログ項目を更新する
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
  usecase "SCR-011 User Story詳細" as SCR
}

rectangle "要求バックログ管理モジュール" {
  usecase "UC-BL-008 バックログ項目を更新する" as UC
}

rectangle "システムレイヤー" {
  card "INF-BL-01\nバックログ項目" as INF01
  card "INF-BL-11\n状態" as INF11
}

ACT1 -- SCR
ACT2 -- SCR
SCR ..> UC : 呼び出し
UC -- INF01 : 使用
UC -- INF11 : 使用
@enduml
```

## 4. ユースケース一覧

| ユースケースID | ユースケース名           | 英語名        | 説明                                 | 関連アクター   |
|----------------|--------------------------|---------------|--------------------------------------|----------------|
| UC-BL-008      | バックログ項目を更新する | UpdateBacklog | 項目の説明・受入条件・詳細を修正する | ACT-02, ACT-04 |

## 5. 画面一覧

| 画面ID  | 画面名         | 英語名     | 説明       | 関連ユースケース |
|---------|----------------|------------|------------|------------------|
| SCR-011 | User Story詳細 | User Story | 画面の説明 | UC-BL-008        |

## 6. イベント一覧

| イベントID | イベント名               | 英語名                 | 説明                                     | 関連ユースケース |
|------------|--------------------------|------------------------|------------------------------------------|------------------|
| EVT-BL-006 | バックログ項目が更新された | Backlog Item Updated | 項目の説明・受入条件・詳細が修正された   | UC-BL-008        |

## 7. 情報一覧

| 情報ID  | 情報名       | 英語名        | 情報種類 | 説明                             | 関連ユースケース |
|---------|--------------|---------------|----------|----------------------------------|------------------|
| INF-BL-01 | バックログ項目 | Backlog Item | リソース | Epic、Feature、Story、欠陥などの項目 | UC-BL-008      |
| INF-BL-11 | 状態         | Status        | 対照表   | 項目のワークフロー状態           | UC-BL-008        |

## 8. 条件一覧

| 条件ID    | 条件名     | 英語名     | 説明                         | 関連ユースケース |
|-----------|------------|------------|------------------------------|------------------|
| INF-BL-11 | 状態       | Status     | 項目のワークフロー状態       | UC-BL-008        |

## 9. 参照情報

- ユースケース一覧: `01.ユースケース/ユースケース一覧.md`
- 画面一覧: `04.システム/04.画面/画面一覧.md`
- イベント一覧: `04.システム/03.イベント/イベント一覧.md`
- 情報一覧: `04.システム/01.情報/情報一覧.md`
