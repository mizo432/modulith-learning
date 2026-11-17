# UC複合図

JB-01 ジョブ実行管理業務領域で扱うユースケース、画面、イベント、情報、条件を関連づけた図です。

## 1. 目的

本書は、JB-01 ジョブ実行管理業務領域のシステム境界レイヤーで扱うユースケース「ジョブ定義を登録する」に関するUC複合図です。

## 2. PlantUML UC複合図（英語版）

```plantuml
@startuml
title [Job Execution Management] UC Composite Diagram - Register Job Definition
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

actor "Operations Admin\n(AC-01)" as Admin

rectangle "Frontend" {
  usecase "SCR-JB-009 Job Definition Register/Edit" as SCR009
}

rectangle "Job Management Module" {
  usecase "UC-JB-001 Register Job Definition" as UC001
}

rectangle "System Layer" {
  card "INF-JB-01 Job Definition" as INF001
}

rectangle "External System" {
  database "ES-02 PostgreSQL" as ES02
}

Admin -- SCR009
SCR009 ..> UC001 : Call
UC001 -- INF001 : Create
UC001 -- ES02 : Persist
@enduml
```

## 3. 日本語版 UC複合図

```plantuml
@startuml
title UC複合図（日本語版）- ジョブ定義を登録する
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

actor "運用管理者\n(AC-01)" as Admin

rectangle "フロントエンド" {
  usecase "SCR-JB-009 ジョブ定義登録・編集" as SCR009
}

rectangle "ジョブ管理モジュール" {
  usecase "UC-JB-001 ジョブ定義を登録する" as UC001
}

rectangle "システムレイヤー" {
  card "INF-JB-01 ジョブ定義" as INF001
}

rectangle "外部システム" {
  database "ES-02 PostgreSQL" as ES02
}

Admin -- SCR009
SCR009 ..> UC001 : 呼び出し
UC001 -- INF001 : 作成
UC001 -- ES02 : 永続化
@enduml
```

## 4. ユースケース一覧

| ユースケースID | ユースケース名       | 英語名               | 説明                                           | 関連アクター        |
|----------------|----------------------|----------------------|------------------------------------------------|---------------------|
| UC-JB-001      | ジョブ定義を登録する | RegisterJobDefinition | ジョブコード、パラメータ仕様、実行制約などを新規登録する | AC-01 運用管理者 |

## 5. 画面一覧

| 画面ID   | 画面名             | 英語名                   | 説明                                     | 関連ユースケース |
|----------|--------------------|--------------------------|------------------------------------------|------------------|
| SCR-JB-009 | ジョブ定義登録・編集 | JobDefinitionRegisterEdit | ジョブ定義の新規登録・編集を行う画面     | UC-JB-001        |

## 6. イベント一覧

| イベントID  | イベント名             | 英語名              | 説明                                     | 関連ユースケース |
|-------------|------------------------|---------------------|------------------------------------------|------------------|
| EVT-JB-001  | ジョブ定義が登録された | JobDefinitionRegistered | ジョブ定義が新規登録された             | UC-JB-001        |

## 7. 情報一覧

| 情報ID   | 情報名     | 英語名        | 説明                                     | 関連ユースケース |
|----------|------------|---------------|------------------------------------------|------------------|
| INF-JB-01 | ジョブ定義 | JobDefinition | ジョブの定義情報（コード、パラメータなど） | UC-JB-001        |

## 8. 条件一覧

条件なし

## 9. 参照情報

- ユースケース一覧: `01.ユースケース/ユースケース一覧.md`
- 画面一覧: `04.システム/04.画面/画面一覧.md`
- イベント一覧: `04.システム/03.イベント/イベント一覧.md`
- 情報一覧: `04.システム/01.情報/情報一覧.md`
