# UC複合図

成果適応業務領域で扱うユースケース、画面、イベント、情報、条件を関連づけた図です。

## 1. 目的

本書は、AG-06 成果適応業務領域のシステム境界レイヤーで扱うユースケース「フィードバックからバックログ候補を作成する」に関するUC複合図です。

## 2. PlantUML UC複合図（英語版）

```plantuml
title [Outcome Adaptation] UC Composite Diagram - CreateBacklogCandidateFromFeedback
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

rectangle "Frontend" {
  usecase "SR-04 Feedback Evaluation" as SR04
}

rectangle "Outcome Adaptation Module" {
  usecase "UC-SR-010 CreateBacklogCandidateFromFeedback" as UC
}

ACT2 -- SR04
SR04 ..> UC : Call
@enduml
```

## 3. 日本語版 UC複合図

```plantuml
title UC複合図（日本語版）- フィードバックからバックログ候補を作成する
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

rectangle "フロントエンド" {
  usecase "SR-04 フィードバック評価" as SR04
}

rectangle "成果適応モジュール" {
  usecase "UC-SR-010 フィードバックからバックログ候補を作成する" as UC
}

ACT2 -- SR04
SR04 ..> UC : 呼び出し
@enduml
```

## 4. ユースケース一覧

| ユースケースID | ユースケース名          | 英語名             | 説明                                             | 関連アクター                   |
|----------------|-------------------------|--------------------|--------------------------------------------------|--------------------------------|
| UC-SR-010      | フィードバックからバックログ候補を作成する | CreateBacklogCandidateFromFeedback | フィードバックから新規Story、Feature、Epicのバックログ候補を作成し、元のフィードバックとの関連を保持する | ACT-02 |

## 5. 画面一覧

| 画面ID  | 画面名  | 英語名 | 説明       | 関連ユースケース |
|---------|---------|--------|------------|------------------|
| SR-04   | フィードバック評価 | Feedback Evaluation | フィードバックの評価画面 | UC-SR-010        |

## 6. イベント一覧

| イベントID | イベント名                 | 説明                                                         | 関連情報  |
|------------|----------------------------|--------------------------------------------------------------|-----------|
| EVT-SR-010 | バックログ候補が作成された  | フィードバックからバックログ候補が作成された                   | INF-SR-07 |

## 7. 情報一覧

| 情報ID     | 情報名          | 説明                                   | 操作種類 |
|------------|-----------------|----------------------------------------|----------|
| INF-SR-05 | Reviewフィードバック | フィードバック内容、発言者、分類、関連Story、添付資料 | 参照     |
| INF-SR-07 | バックログ候補 | フィードバックから作成された新規Story・Feature・Epic候補 | 作成     |

## 8. 条件一覧

| 条件ID | 条件名 | 説明 |
|--------|--------|------|
| 1 | トレーサビリティ | フィードバックとバックログ候補の関連は必須で保持する |
| 2 | 優先順位 | バックログ候補の優先順位を設定する |
| 3 | 権限 | POのみがバックログ候補を作成できる |

## 9. 参照情報

- ユースケース一覧: `01.ユースケース/ユースケース一覧.md`
- 画面一覧: `04.システム/04.画面/画面一覧.md`
- イベント一覧: `04.システム/03.イベント/イベント一覧.md`
- 情報一覧: `04.システム/01.情報/情報一覧.md`
