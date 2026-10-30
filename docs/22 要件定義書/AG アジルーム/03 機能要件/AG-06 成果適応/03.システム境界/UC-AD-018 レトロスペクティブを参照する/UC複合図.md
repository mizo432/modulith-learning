# UC複合図

成果適応業務領域で扱うユースケース、画面、イベント、情報、条件を関連づけた図です。

## 1. 目的

本書は、AG-06 成果適応業務領域のシステム境界レイヤーで扱うユースケース「レトロスペクティブを参照する」に関するUC複合図です。

## 2. PlantUML UC複合図（英語版）

```plantuml
title [Outcome Adaptation] UC Composite Diagram - ViewRetrospective
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
actor "Tracking\n(ACT-05)" as ACT5

rectangle "Frontend" {
  usecase "SCR-026 MeetingDecision" as SCR
}

rectangle "Outcome Adaptation Module" {
  usecase "UC-AD-018 ViewRetrospective" as UC
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
title UC複合図（日本語版）- レトロスペクティブを参照する
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
  usecase "SCR-026 会議・意思決定管理" as SCR
}

rectangle "成果適応モジュール" {
  usecase "UC-AD-018 レトロスペクティブを参照する" as UC
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

| ユースケースID | ユースケース名                 | 英語名                    | 説明                               | 関連アクター   |
|----------------|--------------------------------|---------------------------|------------------------------------|----------------|
| UC-AD-018      | レトロスペクティブを参照する   | ViewRetrospective         | 過去の振り返り・改善アクションを確認する | ACT-01, ACT-02, ACT-03, ACT-04, ACT-05 |

## 5. 画面一覧

| 画面ID  | 画面名               | 英語名              | 説明       | 関連ユースケース |
|---------|----------------------|---------------------|------------|------------------|
| SCR-026 | 会議・意思決定管理 | MeetingDecision | 画面の説明 | UC-AD-018        |

## 6. イベント一覧

| イベントID | イベント名                       | 説明                                                         | 関連情報  |
|------------|----------------------------------|--------------------------------------------------------------|-----------|
| - | - | - | - |

## 7. 情報一覧

| 情報ID     | 情報名            | 説明                                   | 操作種類 |
|------------|-------------------|----------------------------------------|----------|
| INF-AD-08 | レトロスペクティブ | スプリントの振り返り、改善アクション   | 参照     |

## 8. 条件一覧

条件なし

## 9. 参照情報

- ユースケース一覧: `01.ユースケース/ユースケース一覧.md`
- 画面一覧: `04.システム/04.画面/画面一覧.md`
- イベント一覧: `04.システム/03.イベント/イベント一覧.md`
- 情報一覧: `04.システム/01.情報/情報一覧.md`
