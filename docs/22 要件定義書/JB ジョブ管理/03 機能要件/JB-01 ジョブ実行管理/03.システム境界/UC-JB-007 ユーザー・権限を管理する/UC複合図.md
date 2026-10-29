# UC複合図

JB-01 ジョブ実行管理業務領域で扱うユースケース、画面、イベント、情報、条件を関連づけた図です。

## 1. 目的

本書は、JB-01 ジョブ実行管理業務領域のシステム境界レイヤーで扱うユースケース「��に関するUC複合図です。

## 2. PlantUML UC複合図（英語版）

```plantuml
@startuml
title [Job Execution Management] UC Composite Diagram - ManageUserPermissions
left to right direction
skinparam usecase { BackgroundColor #F8FAFC; BorderColor #475569; FontColor #0F172A }
skinparam actor { BackgroundColor #F8FAFC; BorderColor #475569; FontColor #0F172A }
skinparam rectangle { BackgroundColor #F8FAFC; BorderColor #475569; FontColor #0F172A }
skinparam arrowColor #475569
skinparam shadowing false

actor "Operations Admin\n(AC-01)" as Admin
rectangle "Job Management Module" { usecase "UC-JB-007 ManageUserPermissions" as UC }
rectangle "System Layer" { card "INF-JB-07,INF-JB-08" as INF }
rectangle "External System" { database "ES-02 PostgreSQL" as ES02 }

Admin -- UC
UC -- INF
UC -- ES02 : Persist
@enduml
```

## 3. 日本語版 UC複合図

```plantuml
@startuml
title UC複合図（日本語版）- ユーザー・権限を管理する
left to right direction
skinparam usecase { BackgroundColor #F8FAFC; BorderColor #475569; FontColor #0F172A }
skinparam actor { BackgroundColor #F8FAFC; BorderColor #475569; FontColor #0F172A }
skinparam rectangle { BackgroundColor #F8FAFC; BorderColor #475569; FontColor #0F172A }
skinparam arrowColor #475569
skinparam shadowing false

actor "運用管理者\n(AC-01)" as Admin
rectangle "ジョブ管理モジュール" { usecase "UC-JB-007 ユーザー・権限を管理する" as UC }
rectangle "システムレイヤー" { card "INF-JB-07,INF-JB-08" as INF }
rectangle "外部システム" { database "ES-02 PostgreSQL" as ES02 }

Admin -- UC
UC -- INF
UC -- ES02 : 永続化
@enduml
```

## 4. ユースケース一覧

| ユースケースID | ユースケース名 | 英語名 | 説明 | 関連アクター |
|----------------|----------------|--------|------|--------------|
| UC-JB-007 | ユーザー・権限を管理する | ManageUserPermissions | ユーザーのロール、会社・店舗の参照権限を設定する | AC-01 |

## 5. 画面一覧

| 画面ID | 画面名 | 英語名 | 説明 | 関連ユースケース |
|--------|--------|--------|------|------------------|

| SCR-JB-023 | 023 | 023 | 画面 | UC-JB-007 |
| SCR-JB-024 | 024 | 024 | 画面 | UC-JB-007 |

## 6. イベント一覧

| イベントID | イベント名 | 英語名 | 説明 | 関連ユースケース |
|------------|------------|--------|------|------------------|

| EVT-JB-006 | 006 | 006 | イベント | UC-JB-007 |

## 7. 情報一覧

| 情報ID | 情報名 | 英語名 | 説明 | 関連ユースケース |
|--------|--------|--------|------|------------------|

| INF-JB-07 | 07 | 07 | 情報 | UC-JB-007 |
| INF-JB-08 | 08 | 08 | 情報 | UC-JB-007 |

## 8. 条件一覧

条件なし

## 9. 参照情報

- ユースケース一覧: `01.ユースケース/ユースケース一覧.md`
- 画面一覧: `04.システム/04.画面/画面一覧.md`
- イベント一覧: `04.システム/03.イベント/イベント一覧.md`
- 情報一覧: `04.システム/01.情報/情報一覧.md`
