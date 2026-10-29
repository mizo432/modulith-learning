# UC複合図

要求バックログ管理業務領域で扱うユースケース、画面、イベント、情報、条件を関連づけた図です。

## 1. 目的

本書は、AG-02 要求バックログ管理業務領域のシステム境界レイヤーで扱うユースケース「メンション通知を送信する」に関するUC複合図です。

## 2. PlantUML UC複合図（英語版）

```plantuml
title [Backlog Management] UC Composite Diagram - SendMentionNotification
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
actor "Developer\n(ACT-04)" as ACT3

rectangle "Frontend" {
  usecase "SCR-011 User Story" as SCR
}

rectangle "Backlog Management Module" {
  usecase "UC-BL-017 SendMentionNotification" as UC
}

rectangle "System Layer" {
  card "INF-BL-04\nComment" as INF04
}

ACT1 -- SCR
ACT2 -- SCR
ACT3 -- SCR
SCR ..> UC : Call
UC -- INF04 : Use
@enduml
```

## 3. 日本語版 UC複合図

```plantuml
title UC複合図（日本語版）- メンション通知を送信する
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
actor "開発者\n(ACT-04)" as ACT3

rectangle "フロントエンド" {
  usecase "SCR-011 User Story詳細" as SCR
}

rectangle "要求バックログ管理モジュール" {
  usecase "UC-BL-017 メンション通知を送信する" as UC
}

rectangle "システムレイヤー" {
  card "INF-BL-04\nコメント" as INF04
}

ACT1 -- SCR
ACT2 -- SCR
ACT3 -- SCR
SCR ..> UC : 呼び出し
UC -- INF04 : 使用
@enduml
```

## 4. ユースケース一覧

| ユースケースID | ユースケース名           | 英語名                  | 説明                   | 関連アクター           |
|----------------|--------------------------|-------------------------|------------------------|------------------------|
| UC-BL-017      | メンション通知を送信する | SendMentionNotification | 関係者に通知を送信する | ACT-01, ACT-02, ACT-04 |

## 5. 画面一覧

| 画面ID  | 画面名         | 英語名     | 説明       | 関連ユースケース |
|---------|----------------|------------|------------|------------------|
| SCR-011 | User Story詳細 | User Story | 画面の説明 | UC-BL-017        |

## 6. イベント一覧

| イベントID | イベント名                   | 英語名                      | 説明                                     | 関連ユースケース |
|------------|------------------------------|-----------------------------|------------------------------------------|------------------|
| EVT-BL-013 | メンション通知が送信された   | Mention Notification Sent   | 関係者に通知が送信された                 | UC-BL-017        |

## 7. 情報一覧

| 情報ID  | 情報名     | 英語名     | 情報種類 | 説明                         | 関連ユースケース |
|---------|------------|------------|----------|------------------------------|------------------|
| INF-BL-04 | コメント   | Comment    | リソース | 項目上の質問・回答・議論     | UC-BL-017        |

## 8. 条件一覧

条件なし

## 9. 参照情報

- ユースケース一覧: `01.ユースケース/ユースケース一覧.md`
- 画面一覧: `04.システム/04.画面/画面一覧.md`
- イベント一覧: `04.システム/03.イベント/イベント一覧.md`
- 情報一覧: `04.システム/01.情報/情報一覧.md`
