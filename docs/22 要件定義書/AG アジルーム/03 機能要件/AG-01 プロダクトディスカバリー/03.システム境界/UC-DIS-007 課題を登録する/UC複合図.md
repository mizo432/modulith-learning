# UC複合図

プロダクトディスカバリー業務領域で扱うユースケース、画面、イベント、情報、条件を関連づけた図です。

## 1. 目的

本書は、AG-01 プロダクトディスカバリー業務領域のシステム境界レイヤーで扱うユースケース「課題を登録する」に関するUC複合図です。

## 2. PlantUML UC複合図（英語版）

```plantuml
title [Product Discovery] UC Composite Diagram - RegisterIssue
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

rectangle "Frontend" {
  usecase "SCR-004 Feedback" as SCR
}

rectangle "Product Discovery Module" {
  usecase "UC-DIS-007 RegisterIssue" as UC
}

ACT1 -- SCR
ACT2 -- SCR
SCR ..> UC : Call
@enduml
```

## 3. 日本語版 UC複合図

```plantuml
title UC複合図（日本語版）- 課題を登録する
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

rectangle "フロントエンド" {
  usecase "SCR-004 フィードバック管理" as SCR
}

rectangle "プロダクトディスカバリーモジュール" {
  usecase "UC-DIS-007 課題を登録する" as UC
}

ACT1 -- SCR
ACT2 -- SCR
SCR ..> UC : 呼び出し
@enduml
```

## 4. ユースケース一覧

| ユースケースID | ユースケース名 | 英語名        | 説明                                                         | 関連アクター   |
|----------------|----------------|---------------|--------------------------------------------------------------|----------------|
| UC-DIS-007     | 課題を登録する | RegisterIssue | 業務上の困りごとや繰り返し発生する問題を背景とともに記録する | ACT-01, ACT-02 |

## 5. 画面一覧

| 画面ID  | 画面名             | 英語名   | 説明       | 関連ユースケース |
|---------|--------------------|----------|------------|------------------|
| SCR-004 | フィードバック管理 | Feedback | 画面の説明 | UC-DIS-007       |

## 6. イベント一覧

| イベントID  | イベント名         | 説明                                                         | 関連情報  |
|-------------|--------------------|--------------------------------------------------------------|-----------|
| EVT-DIS-005 | 課題が登録された   | 業務上の困りごとや繰り返し発生する問題が記録された           | INF-DIS-03 |

## 7. 情報一覧

| 情報ID     | 情報名 | 説明                                   | 操作種類 |
|------------|--------|----------------------------------------|----------|
| INF-DIS-03 | 課題   | 業務上の困りごとや繰り返し発生する問題 | 作成     |

## 8. 条件一覧

条件なし

## 9. 参照情報

- ユースケース一覧: `01.ユースケース/ユースケース一覧.md`
- 画面一覧: `04.システム/04.画面/画面一覧.md`
- イベント一覧: `04.システム/03.イベント/イベント一覧.md`
- 情報一覧: `04.システム/01.情報/情報一覧.md`
