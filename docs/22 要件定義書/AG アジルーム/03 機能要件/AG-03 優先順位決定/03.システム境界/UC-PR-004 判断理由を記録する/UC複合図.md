# UC複合図

優先順位決定業務領域で扱うユースケース、画面、イベント、情報、条件を関連づけた図です。

## 1. 目的

本書は、AG-03 優先順位決定業務領域のシステム境界レイヤーで扱うユースケース「判断理由を記録する」に関するUC複合図です。

## 2. PlantUML UC複合図（英語版）

```plantuml
title [Priority Decision] UC Composite Diagram - RecordReason
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
  usecase "UC-PR-004 RecordReason" as UC
}

ACT1 -- SCR
SCR ..> UC : Call
@enduml
```

## 3. 日本語版 UC複合図

```plantuml
title UC複合図（日本語版）- 判断理由を記録する
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
  usecase "UC-PR-004 判断理由を記録する" as UC
}

ACT1 -- SCR
SCR ..> UC : 呼び出し
@enduml
```

## 4. ユースケース一覧

| ユースケースID | ユースケース名     | 英語名       | 説明                                           | 関連アクター |
|----------------|--------------------|--------------|------------------------------------------------|--------------|
| UC-PR-004      | 判断理由を記録する | RecordReason | 優先順位決定の根拠・決定者・影響範囲を記録する | ACT-02       |

## 5. 画面一覧

| 画面ID  | 画面名               | 英語名                   | 説明       | 関連ユースケース |
|---------|----------------------|--------------------------|------------|------------------|
| SCR-012 | 受入条件・受入テスト | AcceptanceAcceptanceTest | 画面の説明 | UC-PR-004        |

## 6. イベント一覧

| イベントID | イベント名               | 説明                                                         | 関連情報  |
|------------|--------------------------|--------------------------------------------------------------|-----------|
| EVT-PR-003 | 判断理由が記録された     | 優先順位決定の根拠・決定者・影響範囲が記録された             | INF-PR-05 |

## 7. 情報一覧

| 情報ID     | 情報名       | 説明                                   | 操作種類 |
|------------|--------------|----------------------------------------|----------|
| INF-PR-05 | 優先判断理由 | 優先順位決定の根拠・決定者・影響範囲   | 作成     |

## 8. 条件一覧

条件なし

## 9. 参照情報

- ユースケース一覧: `01.ユースケース/ユースケース一覧.md`
- 画面一覧: `04.システム/04.画面/画面一覧.md`
- イベント一覧: `04.システム/03.イベント/イベント一覧.md`
- 情報一覧: `04.システム/01.情報/情報一覧.md`
