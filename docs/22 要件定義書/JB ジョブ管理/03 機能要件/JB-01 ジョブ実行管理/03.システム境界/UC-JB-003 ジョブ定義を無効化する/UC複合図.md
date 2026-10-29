# UC複合図

JB-01 ジョブ実行管理業務領域で扱うユースケース、画面、イベント、情報、条件を関連づけた図です。

## 1. 目的

本書は、JB-01 ジョブ実行管理業務領域のシステム境界レイヤーで扱うユースケース「ジョブ定義を無効化する」に関するUC複合図です。

## 2. PlantUML UC複合図（英語版）

```plantuml
@startuml
title [Job Execution Management] UC Composite Diagram - Disable Job Definition
left to right direction
skinparam usecase { BackgroundColor #F8FAFC; BorderColor #475569; FontColor #0F172A }
skinparam actor { BackgroundColor #F8FAFC; BorderColor #475569; FontColor #0F172A }
skinparam rectangle { BackgroundColor #F8FAFC; BorderColor #475569; FontColor #0F172A }
skinparam arrowColor #475569
skinparam shadowing false

actor "Operations Admin\n(AC-01)" as Admin
rectangle "Frontend" { usecase "SCR-JB-008 Job Definition Detail" as SCR008 }
rectangle "Job Management Module" { usecase "UC-JB-003 Disable Job Definition" as UC003 }
rectangle "System Layer" { card "INF-JB-01 Job Definition" as INF001 }
rectangle "External System" { database "ES-02 PostgreSQL" as ES02 }

Admin -- SCR008
SCR008 ..> UC003 : Call
UC003 -- INF001 : Disable
UC003 -- ES02 : Persist
@enduml
```

## 3. 日本語版 UC複合図

```plantuml
@startuml
title UC複合図（日本語版）- ジョブ定義を無効化する
left to right direction
skinparam usecase { BackgroundColor #F8FAFC; BorderColor #475569; FontColor #0F172A }
skinparam actor { BackgroundColor #F8FAFC; BorderColor #475569; FontColor #0F172A }
skinparam rectangle { BackgroundColor #F8FAFC; BorderColor #475569; FontColor #0F172A }
skinparam arrowColor #475569
skinparam shadowing false

actor "運用管理者\n(AC-01)" as Admin
rectangle "フロントエンド" { usecase "SCR-JB-008 ジョブ定義詳細" as SCR008 }
rectangle "ジョブ管理モジュール" { usecase "UC-JB-003 ジョブ定義を無効化する" as UC003 }
rectangle "システムレイヤー" { card "INF-JB-01 ジョブ定義" as INF001 }
rectangle "外部システム" { database "ES-02 PostgreSQL" as ES02 }

Admin -- SCR008
SCR008 ..> UC003 : 呼び出し
UC003 -- INF001 : 無効化
UC003 -- ES02 : 永続化
@enduml
```

## 4. ユースケース一覧

| ユースケースID | ユースケース名         | 英語名                | 説明                                     | 関連アクター        |
|----------------|------------------------|-----------------------|------------------------------------------|---------------------|
| UC-JB-003      | ジョブ定義を無効化する | DisableJobDefinition | ジョブ定義を無効化して新規実行を不可にする | AC-01 運用管理者 |

## 5. 画面一覧

| 画面ID   | 画面名         | 英語名               | 説明                                     | 関連ユースケース |
|----------|----------------|----------------------|------------------------------------------|------------------|
| SCR-JB-008 | ジョブ定義詳細 | JobDefinitionDetail | ジョブ定義の詳細情報を表示する画面       | UC-JB-003        |

## 6. イベント一覧

| イベントID  | イベント名               | 英語名                | 説明                                     | 関連ユースケース |
|-------------|--------------------------|-----------------------|------------------------------------------|------------------|
| EVT-JB-003  | ジョブ定義が無効化された | JobDefinitionDisabled | ジョブ定義が無効化された                 | UC-JB-003        |

## 7. 情報一覧

| 情報ID   | 情報名     | 英語名        | 説明                                     | 関連ユースケース |
|----------|------------|---------------|------------------------------------------|------------------|
| INF-JB-01 | ジョブ定義 | JobDefinition | ジョブの定義情報（コード、パラメータなど） | UC-JB-003        |

## 8. 条件一覧

条件なし

## 9. 参照情報

- ユースケース一覧: `01.ユースケース/ユースケース一覧.md`
- 画面一覧: `04.システム/04.画面/画面一覧.md`
- イベント一覧: `04.システム/03.イベント/イベント一覧.md`
- 情報一覧: `04.システム/01.情報/情報一覧.md`
