# UC複合図

帳票基盤管理業務領域で扱うユースケース、画面、イベント、情報、条件を関連づけた図です。

## 1. 目的

本書は、EF-01 帳票基盤管理業務領域のシステム境界レイヤーで扱うユースケース「組織を登録する」に関するUC複合図です。

## 2. PlantUML UC複合図（英語版）

```plantuml
title [Foundation Management] UC Composite Diagram - RegisterOrganization
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

actor "System Admin\n(ACT-EF-01)" as ACT01

rectangle "Frontend" {
  usecase "SCR-009 Organization & User Mgmt" as SCR
}

rectangle "Foundation Management Module" {
  usecase "UC-EF-013 RegisterOrganization" as UC
}

rectangle "System Layer" {
  card "INF-EF-05\n組織" as INF
}

ACT01 -- SCR
SCR ..> UC : Call
UC -- INF : Use
@enduml
```

## 3. 日本語版 UC複合図

```plantuml
title UC複合図（日本語版）- 組織を登録する
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

actor "システム管理者\n(ACT-EF-01)" as ACT01

rectangle "フロントエンド" {
  usecase "SCR-009 組織・利用者管理" as SCR
}

rectangle "帳票基盤管理モジュール" {
  usecase "UC-EF-013 組織を登録する" as UC
}

rectangle "システムレイヤー" {
  card "INF-EF-05\n組織" as INF
}

ACT01 -- SCR
SCR ..> UC : 呼び出し
UC -- INF : 使用
@enduml
```

## 4. ユースケース一覧

| ユースケースID | ユースケース名 | 英語名 | 説明 | 関連アクター |
|----------------|----------------|--------|------|--------------|
| UC-EF-013 | 組織を登録する | RegisterOrganization | システム管理者が組織を組織を登録する | ACT-EF-02 |

## 5. 画面一覧

| 画面ID | 画面名 | 英語名 | 説明 | 関連ユースケース |
|--------|--------|--------|------|------------------|
| SCR-009 | 組織・利用者管理 | Organization & User Mgmt | 画面の説明 | UC-EF-013 |

## 6. イベント一覧

| イベントID | イベント名 | 英語名 | 説明 | 関連ユースケース |
|------------|------------|--------|------|------------------|
| EVT-EF-009 | 組織が登録された | Organization Registered | 組織が組織を登録する | UC-EF-013 |

## 7. 情報一覧

| 情報ID | 情報名 | 英語名 | 情報種類 | 説明 | 関連ユースケース |
|--------|--------|--------|----------|------|------------------|
| INF-EF-05 | 組織 | 組織 | リソース | 組織の情報 | UC-EF-013 |

## 8. 条件一覧

条件なし

## 9. 参照情報

- ユースケース一覧: `01.ユースケース/ユースケース一覧.md`
- 画面一覧: `04.システム/04.画面/画面一覧.md`
- イベント一覧: `04.システム/03.イベント/イベント一覧.md`
- 情報一覧: `04.システム/01.情報/情報一覧.md`
