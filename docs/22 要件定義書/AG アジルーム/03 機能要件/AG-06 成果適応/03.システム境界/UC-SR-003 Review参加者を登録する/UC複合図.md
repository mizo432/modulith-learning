# UC複合図

成果適応業務領域で扱うユースケース、画面、イベント、情報、条件を関連づけた図です。

## 1. 目的

本書は、AG-06 成果適応業務領域のシステム境界レイヤーで扱うユースケース「Review参加者を登録する」に関するUC複合図です。

## 2. PlantUML UC複合図（英語版）

```plantuml
title [Outcome Adaptation] UC Composite Diagram - RegisterReviewParticipants
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

actor "Product Owner\n(ACT-02)" as ACT2
actor "Scrum Master\n(ACT-03)" as ACT3

rectangle "Frontend" {
  usecase "SR-02 Sprint Review Preparation" as SR02
}

rectangle "Outcome Adaptation Module" {
  usecase "UC-SR-003 RegisterReviewParticipants" as UC
}

ACT2 -- SR02
ACT3 -- SR02
SR02 ..> UC : Call
@enduml
```

## 3. 日本語版 UC複合図

```plantuml
title UC複合図（日本語版）- Review参加者を登録する
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

actor "プロダクトオーナー\n(ACT-02)" as ACT2
actor "スクラムマスター\n(ACT-03)" as ACT3

rectangle "フロントエンド" {
  usecase "SR-02 Sprint Review準備" as SR02
}

rectangle "成果適応モジュール" {
  usecase "UC-SR-003 Review参加者を登録する" as UC
}

ACT2 -- SR02
ACT3 -- SR02
SR02 ..> UC : 呼び出し
@enduml
```

## 4. ユースケース一覧

| ユースケースID | ユースケース名          | 英語名             | 説明                                             | 関連アクター                   |
|----------------|-------------------------|--------------------|--------------------------------------------------|--------------------------------|
| UC-SR-003      | Review参加者を登録する | RegisterReviewParticipants | Sprint Reviewの参加者（Scrum Team、ステークホルダー）を登録する | ACT-02, ACT-03 |

## 5. 画面一覧

| 画面ID  | 画面名  | 英語名 | 説明       | 関連ユースケース |
|---------|---------|--------|------------|------------------|
| SR-02   | Sprint Review準備 | Sprint Review Preparation | Sprint Reviewの準備画面 | UC-SR-003        |

## 6. イベント一覧

| イベントID | イベント名                 | 説明                                                         | 関連情報  |
|------------|----------------------------|--------------------------------------------------------------|-----------|
| EVT-SR-003 | Review参加者が登録された  | Sprint Reviewの参加者が登録された                             | INF-SR-02 |

## 7. 情報一覧

| 情報ID     | 情報名          | 説明                                   | 操作種類 |
|------------|-----------------|----------------------------------------|----------|
| INF-SR-02 | Review参加者 | 参加者、役割、参加状況 | 作成     |

## 8. 条件一覧

| 条件ID | 条件名 | 説明 |
|--------|--------|------|
| 1 | 必須参加者 | Scrum Teamは必須参加者として登録される |
| 2 | 役割設定 | 各参加者の役割を正しく設定する必要がある |

## 9. 参照情報

- ユースケース一覧: `01.ユースケース/ユースケース一覧.md`
- 画面一覧: `04.システム/04.画面/画面一覧.md`
- イベント一覧: `04.システム/03.イベント/イベント一覧.md`
- 情報一覧: `04.システム/01.情報/情報一覧.md`
