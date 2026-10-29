# UC複合図

プロダクトディスカバリー業務領域で扱うユースケース、画面、イベント、情報、条件を関連づけた図です。

## 1. 目的

本書は、AG-01 プロダクトディスカバリー業務領域のシステム境界レイヤーで扱うユースケース「仮説を参照する」に関するUC複合図です。

## 2. PlantUML UC複合図（英語版）

```plantuml
title [Product Discovery] UC Composite Diagram - ViewHypothesis
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
actor "Tracker\n(ACT-05)" as ACT3

rectangle "Frontend" {
  usecase "SCR-002 プロダクトビジョン・目標" as SCR
}

rectangle "Product Discovery Module" {
  usecase "UC-DIS-012 ViewHypothesis" as UC
}

ACT1 -- SCR
ACT2 -- SCR
ACT3 -- SCR
SCR ..> UC : Call
@enduml
```

## 3. 日本語版 UC複合図

```plantuml
title UC複合図（日本語版）- 仮説を参照する
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
  usecase "SCR-002 プロダクトビジョン・目標" as SCR
}

rectangle "プロダクトディスカバリーモジュール" {
  usecase "UC-DIS-012 仮説を参照する" as UC
}

ACT1 -- SCR
ACT2 -- SCR
ACT3 -- SCR
SCR ..> UC : 呼び出し
@enduml
```

## 4. ユースケース一覧

| ユースケースID | ユースケース名 | 英語名         | 説明                                 | 関連アクター           |
|----------------|----------------|----------------|--------------------------------------|------------------------|
| UC-DIS-012     | 仮説を参照する | ViewHypothesis | 登録された仮説の一覧・詳細を確認する | ACT-01, ACT-02, ACT-05 |

## 5. 画面一覧

| 画面ID  | 画面名                   | 英語名                   | 説明       | 関連ユースケース |
|---------|--------------------------|--------------------------|------------|------------------|
| SCR-002 | プロダクトビジョン・目標 | プロダクトビジョン・目標 | 画面の説明 | UC-DIS-012       |

## 6. イベント一覧

イベントなし

## 7. 情報一覧

| 情報ID     | 情報名 | 説明                                   | 操作種類 |
|------------|--------|----------------------------------------|----------|
| INF-DIS-05 | 仮説   | 課題に対する検証可能な仮説             | 参照     |

## 8. 条件一覧

条件なし

## 9. 参照情報

- ユースケース一覧: `01.ユースケース/ユースケース一覧.md`
- 画面一覧: `04.システム/04.画面/画面一覧.md`
- イベント一覧: `04.システム/03.イベント/イベント一覧.md`
- 情報一覧: `04.システム/01.情報/情報一覧.md`
