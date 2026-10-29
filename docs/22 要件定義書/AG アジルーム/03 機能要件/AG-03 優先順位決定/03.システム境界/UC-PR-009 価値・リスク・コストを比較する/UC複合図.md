# UC複合図

優先順位決定業務領域で扱うユースケース、画面、イベント、情報、条件を関連づけた図です。

## 1. 目的

本書は、AG-03 優先順位決定業務領域のシステム境界レイヤーで扱うユースケース「価値・リスク・コストを比較する」に関するUC複合図です。

## 2. PlantUML UC複合図（英語版）

```plantuml
title [Priority Decision] UC Composite Diagram - CompareRisk
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

rectangle "Frontend" {
  usecase "SCR-012 AcceptanceAcceptanceTest" as SCR
}

rectangle "Priority Decision Module" {
  usecase "UC-PR-009 CompareRisk" as UC
}

ACT1 -- SCR
SCR ..> UC : Call
@enduml
```

## 3. 日本語版 UC複合図

```plantuml
title UC複合図（日本語版）- 価値・リスク・コストを比較する
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

rectangle "フロントエンド" {
  usecase "SCR-012 受入条件・受入テスト" as SCR
}

rectangle "優先順位決定モジュール" {
  usecase "UC-PR-009 価値・リスク・コストを比較する" as UC
}

ACT1 -- SCR
SCR ..> UC : 呼び出し
@enduml
```

## 4. ユースケース一覧

| ユースケースID | ユースケース名                 | 英語名      | 説明                                           | 関連アクター |
|----------------|--------------------------------|-------------|------------------------------------------------|--------------|
| UC-PR-009      | 価値・リスク・コストを比較する | CompareRisk | 要求の価値、リスク、コスト、依存関係を比較する | ACT-02       |

## 5. 画面一覧

| 画面ID  | 画面名               | 英語名                   | 説明       | 関連ユースケース |
|---------|----------------------|--------------------------|------------|------------------|
| SCR-012 | 受入条件・受入テスト | AcceptanceAcceptanceTest | 画面の説明 | UC-PR-009        |

## 6. イベント一覧

| イベントID | イベント名                       | 説明                                                         | 関連情報  |
|------------|----------------------------------|--------------------------------------------------------------|-----------|
| EVT-PR-006 | 価値・リスク・コストが比較された | 要求の価値、リスク、コスト、依存関係が比較された             | INF-PR-01 |

## 7. 情報一覧

| 情報ID     | 情報名 | 説明                                   | 操作種類 |
|------------|--------|----------------------------------------|----------|
| INF-PR-01 | 優先順位 | バックログ項目の順序と優先理由       | 参照     |

## 8. 条件一覧

条件なし

## 9. 参照情報

- ユースケース一覧: `01.ユースケース/ユースケース一覧.md`
- 画面一覧: `04.システム/04.画面/画面一覧.md`
- イベント一覧: `04.システム/03.イベント/イベント一覧.md`
- 情報一覧: `04.システム/01.情報/情報一覧.md`
