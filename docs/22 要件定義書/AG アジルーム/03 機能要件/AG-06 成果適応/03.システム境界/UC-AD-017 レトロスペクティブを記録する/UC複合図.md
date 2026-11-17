# UC複合図

成果適応業務領域で扱うユースケース、画面、イベント、情報、条件を関連づけた図です。

## 1. 目的

本書は、AG-06 成果適応業務領域のシステム境界レイヤーで扱うユースケース「レトロスペクティブを記録する」に関するUC複合図です。

## 2. PlantUML UC複合図（英語版）

```plantuml
title [Outcome Adaptation] UC Composite Diagram - RecordRetrospective
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

actor "Scrum Master\n(ACT-03)" as ACT1
actor "Developer\n(ACT-04)" as ACT2

rectangle "Frontend" {
  usecase "SCR-026 MeetingDecision" as SCR
}

rectangle "Outcome Adaptation Module" {
  usecase "UC-AD-017 RecordRetrospective" as UC
}

ACT1 -- SCR
ACT2 -- SCR
SCR ..> UC : Call
@enduml
```

## 3. 日本語版 UC複合図

```plantuml
title UC複合図（日本語版）- レトロスペクティブを記録する
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

actor "スクラムマスター\n(ACT-03)" as ACT1
actor "開発者\n(ACT-04)" as ACT2

rectangle "フロントエンド" {
  usecase "SCR-026 会議・意思決定管理" as SCR
}

rectangle "成果適応モジュール" {
  usecase "UC-AD-017 レトロスペクティブを記録する" as UC
}

ACT1 -- SCR
ACT2 -- SCR
SCR ..> UC : 呼び出し
@enduml
```

## 4. ユースケース一覧

| ユースケースID | ユースケース名                 | 英語名                    | 説明                               | 関連アクター   |
|----------------|--------------------------------|---------------------------|------------------------------------|----------------|
| UC-AD-017      | レトロスペクティブを記録する   | RecordRetrospective       | スプリントの振り返り、改善アクションを記録する | ACT-03, ACT-04 |

## 5. 画面一覧

| 画面ID  | 画面名               | 英語名              | 説明       | 関連ユースケース |
|---------|----------------------|---------------------|------------|------------------|
| SCR-026 | 会議・意思決定管理 | MeetingDecision | 画面の説明 | UC-AD-017        |

## 6. イベント一覧

| イベントID | イベント名                       | 説明                                                         | 関連情報  |
|------------|----------------------------------|--------------------------------------------------------------|-----------|
| EVT-AD-011 | レトロスペクティブが記録された   | スプリントの振り返り、改善アクションが記録された             | INF-AD-08 |

## 7. 情報一覧

| 情報ID     | 情報名            | 説明                                   | 操作種類 |
|------------|-------------------|----------------------------------------|----------|
| INF-AD-08 | レトロスペクティブ | スプリントの振り返り、改善アクション   | 作成     |

## 8. 条件一覧

条件なし

## 9. 参照情報

- ユースケース一覧: `01.ユースケース/ユースケース一覧.md`
- 画面一覧: `04.システム/04.画面/画面一覧.md`
- イベント一覧: `04.システム/03.イベント/イベント一覧.md`
- 情報一覧: `04.システム/01.情報/情報一覧.md`
