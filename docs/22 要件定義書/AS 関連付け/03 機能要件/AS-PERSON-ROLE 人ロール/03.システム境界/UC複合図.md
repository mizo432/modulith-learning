# UC複合図

AS-PERSON-ROLE 人ロール業務領域で扱うユースケース、画面、イベント、情報、条件を関連づけた図です。

## 1. 目的

本書は、AS-PERSON-ROLE 人ロール業務領域のシステム境界レイヤーで扱うユースケース、画面、イベント、情報、条件を関連づけたUC複合図です。

## 2. PlantUML UC複合図

```plantuml
@startuml
title AS-PERSON-ROLE 人ロール UC複合図
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
  usecase "UC-AS-PERSON-001\n従業員を登録する" as UC001
  usecase "UC-AS-PERSON-002\n従業員を更新する" as UC002
  usecase "UC-AS-PERSON-003\n従業員を無効化する" as UC003
  usecase "UC-AS-PERSON-004\n従業員を検索する" as UC004
  usecase "UC-AS-PERSON-005\n従業員一覧を取得する" as UC005
  usecase "UC-AS-PERSON-006\n従業員の詳細を取得する" as UC006
  usecase "UC-AS-PERSON-007\n所属組織を変更する" as UC007
  usecase "UC-AS-PERSON-008\n部署変更履歴を取得する" as UC008
}

rectangle "システムレイヤー" {
  card "INF-AS-PERSON-01\n従業員" as INF01
  card "INF-AS-PERSON-02\n人ロール" as INF02
  card "INF-AS-PERSON-03\n所属対応表" as INF03
  card "RL-AS-PERSON-01\n従業員ID一意性" as RL01
  card "RL-AS-PERSON-02\nメールアドレス一意性" as RL02
  card "RL-AS-PERSON-06\n所属組織の有効性チェック" as RL06
}

Admin -- UC001
Admin -- UC002
Admin -- UC003
Admin -- UC004
Admin -- UC007
Operator -- UC008
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
UC006 -- INF03 : 使用
UC007 -- INF03 : 使用
UC008 -- INF03 : 使用

UC001 -- RL01 : 適用
UC001 -- RL02 : 適用
UC001 -- RL06 : 適用
UC007 -- RL06 : 適用
@enduml
```

## 3. ユースケース一覧

| ユースケースID | ユースケース名 | 説明 | 関連アクター |
|----------------|----------------|------|--------------|
| UC-AS-PERSON-001 | 従業員を登録する | 新しい従業員を登録する（PartyとPersonRoleを自動作成） | AC-AS-01 |
| UC-AS-PERSON-002 | 従業員を更新する | 従業員の基本情報を更新する | AC-AS-01 |
| UC-AS-PERSON-003 | 従業員を無効化する | 従業員を退社処理する | AC-AS-01 |
| UC-AS-PERSON-004 | 従業員を検索する | 条件に合致する従業員を検索する | AC-AS-01, AC-AS-03, AC-AS-04 |
| UC-AS-PERSON-005 | 従業員一覧を取得する | 従業員の一覧をページネーションで取得する | AC-AS-01, AC-AS-03, AC-AS-04 |
| UC-AS-PERSON-006 | 従業員の詳細を取得する | 指定した従業員の詳細情報を取得する（所属情報を含む） | AC-AS-01, AC-AS-03, AC-AS-04 |
| UC-AS-PERSON-007 | 所属組織を変更する | 従業員の所属組織・部署を変更する | AC-AS-01 |
| UC-AS-PERSON-008 | 部署変更履歴を取得する | 従業員の部署変更履歴を取得する | AC-AS-01, AC-AS-02 |

## 4. 情報一覧

| 情報ID | 情報名 | 説明 | 関連ユースケース |
|--------|--------|------|------------------|
| INF-AS-PERSON-01 | 従業員 | 個人（従業員）の基本情報 | UC-AS-PERSON-001〜006 |
| INF-AS-PERSON-02 | 人ロール | 従業員に付与される人ロール | UC-AS-PERSON-001, 007 |
| INF-AS-PERSON-03 | 所属対応表 | 従業員と組織・部署の所属関係 | UC-AS-PERSON-006〜008 |

## 5. 条件一覧

| 条件ID | 条件名 | 説明 | 関連ユースケース |
|--------|--------|------|------------------|
| RL-AS-PERSON-01 | 従業員ID一意性 | 従業員IDはシステム内で一意であること | UC-AS-PERSON-001 |
| RL-AS-PERSON-02 | メールアドレス一意性 | メールアドレスは重複できないこと | UC-AS-PERSON-001, 002 |
| RL-AS-PERSON-06 | 所属組織の有効性チェック | 所属組織は有効な組織であること | UC-AS-PERSON-001, 007 |

## 6. 参照情報

- ユースケース一覧: `03.システム境界/ユースケース一覧.md`
- 情報一覧: `04.システム/01.情報/情報一覧.md`
- 条件一覧: `04.システム/03.条件/条件一覧.tsv`
