# UC複合図

カレンダー管理（CA-01）業務領域で扱うユースケース、画面、イベント、情報、条件を関連づけた図です。

## 1. 目的

本書は、CA-01 カレンダー管理業務領域のシステム境界レイヤーで扱うユースケース、画面、イベント、情報、条件を関連づけたUC複合図です。

## 2. PlantUML UC複合図（英語版）

```plantuml
@startuml
title Calendar Management UC Composite Diagram
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

actor "SystemAdmin" as Admin
actor "BusinessSystem" as BizSystem

rectangle "calendarReg" {
  usecase "UC-CA-001 Find Holiday by Date" as UC01
  usecase "UC-CA-002 Find Holidays by Year" as UC02
  usecase "UC-CA-003 Find Holidays by Period" as UC03
  usecase "UC-CA-004 Import Holiday Master" as UC04
  usecase "UC-CA-005 Calculate Business Day" as UC05
  usecase "UC-CA-006 Calculate Business Days Count" as UC06
}

note "ES-CA-01 Digital Agency Holiday Data" as ES01

rectangle "System Layer" {
  card "INF-CA-01 Holiday" as INF01
}

BizSystem -- UC01
BizSystem -- UC02
BizSystem -- UC03
BizSystem -- UC05
BizSystem -- UC06
Admin -- UC04

UC01 -- INF01 : Read
UC02 -- INF01 : Read
UC03 -- INF01 : Read
UC04 -- INF01 : Upsert
UC05 -- INF01 : Read
UC06 -- INF01 : Read
UC04 ..> ES01 : CSV Import

@enduml
```

## 3. 日本語版 UC複合図

```plantuml
@startuml
title カレンダー管理 UC複合図（日本語版）
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

actor "システム管理者" as Admin
actor "業務システム" as BizSystem

rectangle "calendarReg" {
  usecase "UC-CA-001 祝日情報を日付で参照する" as UC01
  usecase "UC-CA-002 祝日情報を年で参照する" as UC02
  usecase "UC-CA-003 祝日情報を期間で参照する" as UC03
  usecase "UC-CA-004 祝日マスターをインポートする" as UC04
  usecase "UC-CA-005 営業日を計算する" as UC05
  usecase "UC-CA-006 営業日数を計算する" as UC06
}

note "ES-CA-01 デジタル庁祝日データ" as ES01

rectangle "システムレイヤー" {
  card "INF-CA-01 祝日" as INF01
}

BizSystem -- UC01
BizSystem -- UC02
BizSystem -- UC03
BizSystem -- UC05
BizSystem -- UC06
Admin -- UC04

UC01 -- INF01 : 参照
UC02 -- INF01 : 参照
UC03 -- INF01 : 参照
UC04 -- INF01 : Upsert
UC05 -- INF01 : 参照
UC06 -- INF01 : 参照
UC04 ..> ES01 : CSVインポート

@enduml
```

## 4. ユースケース一覧

| ユースケースID | ユースケース名 | 英語名      | 説明 | 関連アクター |
|----------------|----------------|-------------|------|--------------|
| UC-CA-001      | 祝日情報を日付で参照する | Find Holiday by Date | 指定された日付の祝日情報を取得する | システム管理者, 業務システム |
| UC-CA-002      | 祝日情報を年で参照する   | Find Holidays by Year | 指定された年の全祝日情報を取得する | 業務システム |
| UC-CA-003      | 祝日情報を期間で参照する | Find Holidays by Period | 指定された期間内の全祝日情報を取得する | 業務システム |
| UC-CA-004      | 祝日マスターをインポートする | Import Holiday Master | デジタル庁/内閣府の祝日CSVをバッチインポートする | システム管理者 |
| UC-CA-005      | 営業日を計算する             | Calculate Business Day | 基準日と営業日数から営業日（日付）を求める | 業務システム |
| UC-CA-006      | 営業日数を計算する           | Calculate Business Days Count | 2つの日付の間の営業日数を求める | 業務システム |

## 5. 画面一覧

カレンダー管理はAPI中心のモジュールであり、GUI画面は定義されていません。

## 6. イベント一覧

| イベントID | イベント名 | 英語名    | 説明 | 関連ユースケース |
|------------|------------|-----------|------|------------------|
| EVT-CA-01  | 祝日CSVインポート | Holiday CSV Import | デジタル庁/内閣府の祝日CSVデータをバッチ読み込み | UC-CA-004 |

## 7. 情報一覧

| 情報ID    | 情報名 | 英語名          | 説明 | 関連ユースケース |
|-----------|--------|-----------------|------|------------------|
| INF-CA-01 | 祝日   | Holiday         | 祝日・休日の日付・名称・備考を管理 | UC-CA-001, UC-CA-002, UC-CA-003, UC-CA-004, UC-CA-005, UC-CA-006 |

## 8. 条件一覧

| 条件ID   | 条件名   | 英語名   | 説明 | 関連ユースケース |
|----------|----------|----------|------|------------------|
| RL-CA-01 | 祝日ID生成ルール | Holiday ID Generation Rule | 日付から祝日IDを生成（yyyyMMdd形式） | UC-CA-004 |
| RL-CA-02 | CSVヘッダーマッピング | CSV Header Mapping | 日本語・英語の複数ヘッダー形式をマッピング | UC-CA-004 |
| RL-CA-03 | 日付形式パースルール | Date Format Parse Rule | 複数日付形式（yyyy/MM/dd, yyyy-MM-dd等）をパース | UC-CA-004 |
| RL-CA-04 | 営業日計算ルール | Business Day Calculation Rule | 基準日から営業日数を加算・減算し、祝日・休業日を除外して営業日を算出 | UC-CA-005, UC-CA-006 |

## 9. 参照情報

- ユースケース一覧: `03.システム境界/01.ユースケース/ユースケース一覧.md`
- 情報一覧: `04.システム/01.情報/情報一覧.md`
- イベント一覧: `04.システム/03.イベント/イベント一覧.md`
