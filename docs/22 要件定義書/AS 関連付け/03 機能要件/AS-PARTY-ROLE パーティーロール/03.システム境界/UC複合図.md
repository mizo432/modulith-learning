# UC複合図

AS-PARTY-ROLE パーティーロール業務領域で扱うユースケース、画面、イベント、情報、条件を関連づけた図です。

## 1. 目的

本書は、AS-PARTY-ROLE パーティーロール業務領域のシステム境界レイヤーで扱うユースケース、画面、イベント、情報、条件を関連づけたUC複合図です。

## 2. PlantUML UC複合図

```plantuml
@startuml
title AS-PARTY-ROLE パーティーロール UC複合図
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
actor "システム運用者" as Operator
actor "JBジョブ管理" as JB
actor "AGアジルーム" as AG

note "ES-AS-01 PostgreSQL" as ES01

rectangle "AS 関連付けモジュール" {
  usecase "UC-AS-PARTY-ROLE-001\nロール定義を登録する" as UC001
  usecase "UC-AS-PARTY-ROLE-002\nロール定義を更新する" as UC002
  usecase "UC-AS-PARTY-ROLE-003\nロールを検索する" as UC003
  usecase "UC-AS-PARTY-ROLE-004\nロール一覧を取得する" as UC004
  usecase "UC-AS-PARTY-ROLE-005\nロールの詳細を取得する" as UC005
  usecase "UC-AS-PARTY-ROLE-006\nパーティーにロールを付与する" as UC006
  usecase "UC-AS-PARTY-ROLE-007\nパーティーからロールを解除する" as UC007
  usecase "UC-AS-PARTY-ROLE-008\nロール変更履歴を取得する" as UC008
}

rectangle "システムレイヤー" {
  card "INF-AS-PARTY-ROLE-01\nパーティーロール" as INF01
  card "INF-AS-PARTY-ROLE-02\nロール種類" as INF02
  card "INF-AS-PARTY-ROLE-03\nロール有効期間" as INF03
  card "RL-AS-PARTY-ROLE-01\nロール定義ID一意性" as RL01
  card "RL-AS-PARTY-ROLE-03\n有効期間の整合性" as RL03
  card "RL-AS-PARTY-ROLE-05\nロール重複付与禁止" as RL05
}

Admin -- UC001
Admin -- UC002
Admin -- UC003
Admin -- UC006
Admin -- UC007
Operator -- UC008
JB -- UC003
JB -- UC004
JB -- UC005
AG -- UC003
AG -- UC004
AG -- UC005

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
UC006 -- INF03 : 使用
UC007 -- INF03 : 使用
UC008 -- INF03 : 使用

UC001 -- RL01 : 適用
UC001 -- RL03 : 適用
UC006 -- RL05 : 適用
@enduml
```

## 3. ユースケース一覧

| ユースケースID | ユースケース名 | 説明 | 関連アクター |
|----------------|----------------|------|--------------|
| UC-AS-PARTY-ROLE-001 | ロール定義を登録する | 新しいロール定義を登録する | AC-AS-01 |
| UC-AS-PARTY-ROLE-002 | ロール定義を更新する | ロール定義を更新する | AC-AS-01 |
| UC-AS-PARTY-ROLE-003 | ロールを検索する | 条件に合致するロールを検索する | AC-AS-01, AC-AS-03, AC-AS-04 |
| UC-AS-PARTY-ROLE-004 | ロール一覧を取得する | ロールの一覧をページネーションで取得する | AC-AS-01, AC-AS-03, AC-AS-04 |
| UC-AS-PARTY-ROLE-005 | ロールの詳細を取得する | 指定したロールの詳細情報を取得する | AC-AS-01, AC-AS-03, AC-AS-04 |
| UC-AS-PARTY-ROLE-006 | パーティーにロールを付与する | パーティーにロールを付与する | AC-AS-01 |
| UC-AS-PARTY-ROLE-007 | パーティーからロールを解除する | パーティーからロールを解除する | AC-AS-01 |
| UC-AS-PARTY-ROLE-008 | ロール変更履歴を取得する | ロールの有効期間変更履歴を取得する | AC-AS-01, AC-AS-02 |

## 4. 情報一覧

| 情報ID | 情報名 | 説明 | 関連ユースケース |
|--------|--------|------|------------------|
| INF-AS-PARTY-ROLE-01 | パーティーロール | パーティーに付与されるロールの定義 | UC-AS-PARTY-ROLE-001〜005 |
| INF-AS-PARTY-ROLE-02 | ロール種類 | ロールの種類（人ロール、組織ロールなど） | UC-AS-PARTY-ROLE-001 |
| INF-AS-PARTY-ROLE-03 | ロール有効期間 | ロールの有効期間管理 | UC-AS-PARTY-ROLE-006〜008 |

## 5. 条件一覧

| 条件ID | 条件名 | 説明 | 関連ユースケース |
|--------|--------|------|------------------|
| RL-AS-PARTY-ROLE-01 | ロール定義ID一意性 | ロール定義IDはシステム内で一意であること | UC-AS-PARTY-ROLE-001 |
| RL-AS-PARTY-ROLE-03 | 有効期間の整合性 | 有効開始日 ≤ 有効終了日であること | UC-AS-PARTY-ROLE-001, 002 |
| RL-AS-PARTY-ROLE-05 | ロール重複付与禁止 | 同じロールを重複して付与できないこと | UC-AS-PARTY-ROLE-006 |

## 6. 参照情報

- ユースケース一覧: `03.システム境界/ユースケース一覧.md`
- 情報一覧: `04.システム/01.情報/情報一覧.md`
- 条件一覧: `04.システム/03.条件/条件一覧.tsv`
