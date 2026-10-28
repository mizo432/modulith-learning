# UC複合図

AS-PARTY パーティー業務領域で扱うユースケース、画面、イベント、情報、条件を関連づけた図です。

## 1. 目的

本書は、AS-PARTY パーティー業務領域のシステム境界レイヤーで扱うユースケース、画面、イベント、情報、条件を関連づけたUC複合図です。

## 2. PlantUML UC複合図

```plantuml
@startuml
title AS-PARTY パーティー UC複合図
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

actor "組織管理者" as Admin
actor "JBジョブ管理" as JB
actor "AGアジルーム" as AG

note "ES-AS-01 PostgreSQL" as ES01

rectangle "AS 関連付けモジュール" {
  usecase "UC-AS-PARTY-001\nパーティーを登録する" as UC001
  usecase "UC-AS-PARTY-002\nパーティーを更新する" as UC002
  usecase "UC-AS-PARTY-003\nパーティーを無効化する" as UC003
  usecase "UC-AS-PARTY-004\nパーティーを検索する" as UC004
  usecase "UC-AS-PARTY-005\nパーティー一覧を取得する" as UC005
  usecase "UC-AS-PARTY-006\nパーティーの詳細を取得する" as UC006
  usecase "UC-AS-PARTY-007\nロールを付与する" as UC007
  usecase "UC-AS-PARTY-008\nロールを解除する" as UC008
}

rectangle "システムレイヤー" {
  card "INF-AS-PARTY-01\nパーティー" as INF01
  card "INF-AS-PARTY-02\nパーティー種類" as INF02
  card "INF-AS-PARTY-03\nパーティーロール対応表" as INF03
  card "RL-AS-PARTY-01\nパーティーID一意性" as RL01
  card "RL-AS-PARTY-03\n有効期間の整合性" as RL03
  card "RL-AS-PARTY-05\nロール重複付与禁止" as RL05
}

Admin -- UC001
Admin -- UC002
Admin -- UC003
Admin -- UC004
Admin -- UC007
Admin -- UC008
JB -- UC004
JB -- UC005
JB -- UC006
AG -- UC004
AG -- UC005
AG -- UC006

UC001 ..> ES01 : データ交換
UC002 ..> ES01 : データ交換
UC003 ..> ES01 : データ交換
UC004 ..> ES01 : データ交換
UC005 ..> ES01 : データ交換
UC006 ..> ES01 : データ交換
UC007 ..> ES01 : データ交換
UC008 ..> ES01 : データ交換

UC001 -- INF01 : 使用
UC002 -- INF01 : 使用
UC003 -- INF01 : 使用
UC004 -- INF01 : 使用
UC005 -- INF01 : 使用
UC006 -- INF01 : 使用
UC007 -- INF03 : 使用
UC008 -- INF03 : 使用

UC001 -- RL01 : 適用
UC001 -- RL03 : 適用
UC007 -- RL05 : 適用
@enduml
```

## 3. ユースケース一覧

| ユースケースID | ユースケース名 | 説明 | 関連アクター |
|----------------|----------------|------|--------------|
| UC-AS-PARTY-001 | パーティーを登録する | 新しい実体(Party)を登録する | AC-AS-01 |
| UC-AS-PARTY-002 | パーティーを更新する | パーティーの基本情報を更新する | AC-AS-01 |
| UC-AS-PARTY-003 | パーティーを無効化する | パーティーを無効化する | AC-AS-01 |
| UC-AS-PARTY-004 | パーティーを検索する | 条件に合致するパーティーを検索する | AC-AS-01, AC-AS-03, AC-AS-04 |
| UC-AS-PARTY-005 | パーティー一覧を取得する | パーティーの一覧をページネーションで取得する | AC-AS-01, AC-AS-03, AC-AS-04 |
| UC-AS-PARTY-006 | パーティーの詳細を取得する | 指定したパーティーの詳細情報を取得する | AC-AS-01, AC-AS-03, AC-AS-04 |
| UC-AS-PARTY-007 | ロールを付与する | パーティーにロールを付与する | AC-AS-01 |
| UC-AS-PARTY-008 | ロールを解除する | パーティーからロールを解除する | AC-AS-01 |

## 4. 情報一覧

| 情報ID | 情報名 | 説明 | 関連ユースケース |
|--------|--------|------|------------------|
| INF-AS-PARTY-01 | パーティー | 組織・人・会社・店舗などの実体を抽象化した基本情報 | UC-AS-PARTY-001〜006 |
| INF-AS-PARTY-02 | パーティー種類 | パーティーの種類（個人、法人、組織など） | UC-AS-PARTY-001 |
| INF-AS-PARTY-03 | パーティーロール対応表 | パーティーとロールの対応関係 | UC-AS-PARTY-007, 008 |

## 5. 条件一覧

| 条件ID | 条件名 | 説明 | 関連ユースケース |
|--------|--------|------|------------------|
| RL-AS-PARTY-01 | パーティーID一意性 | パーティーIDはシステム内で一意であること | UC-AS-PARTY-001 |
| RL-AS-PARTY-03 | 有効期間の整合性 | 有効開始日 ≤ 有効終了日であること | UC-AS-PARTY-001, 002 |
| RL-AS-PARTY-05 | ロール重複付与禁止 | 同じロールを重複して付与できないこと | UC-AS-PARTY-007 |

## 6. 参照情報

- ユースケース一覧: `03.システム境界/ユースケース一覧.md`
- 情報一覧: `04.システム/01.情報/情報一覧.md`
- 条件一覧: `04.システム/03.条件/条件一覧.tsv`
