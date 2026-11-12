# UC複合図

AS-PERSON-ROLE 人ロール業務領域で扱うユースケース、画面、イベント、情報、条件を関連づけた図です。

## 1. 目的

本書は、AS-PERSON-ROLE 人ロール業務領域のシステム境界レイヤーで扱うユースケース、画面、イベント、情報、条件を関連づけたUC複合図です。

## 2. PlantUML UC複合図（英語版）

```plantuml
@startuml
title AS-PERSON-ROLE PersonRole UC Composite Diagram
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

actor "OrganizationAdmin" as Admin
actor "SystemOperator" as Operator
actor "JBJobManagement" as JB
actor "AGWarRoom" as AG

note "ES-AS-01 PostgreSQL" as ES01

rectangle "Frontend" {
  usecase "SCR-AS-PERSON-001 EmployeeList" as SCR001
  usecase "SCR-AS-PERSON-002 EmployeeRegisterEdit" as SCR002
  usecase "SCR-AS-PERSON-003 EmployeeDetail" as SCR003
  usecase "SCR-AS-PERSON-004 AffiliationChange" as SCR004
  usecase "SCR-AS-PERSON-005 DepartmentChangeHistory" as SCR005
}

rectangle "AS Association Module" {
  usecase "UC-AS-PERSON-001 RegisterEmployee" as UC001
  usecase "UC-AS-PERSON-002 UpdateEmployee" as UC002
  usecase "UC-AS-PERSON-003 InvalidateEmployee" as UC003
  usecase "UC-AS-PERSON-004 SearchEmployee" as UC004
  usecase "UC-AS-PERSON-005 GetEmployeeList" as UC005
  usecase "UC-AS-PERSON-006 GetEmployeeDetail" as UC006
  usecase "UC-AS-PERSON-007 ChangeAffiliation" as UC007
  usecase "UC-AS-PERSON-008 GetDepartmentChangeHistory" as UC008
}

rectangle "System Layer" {
  card "INF-AS-PERSON-01 Employee" as INF01
  card "INF-AS-PERSON-02 PersonRole" as INF02
  card "INF-AS-PERSON-03 AffiliationMapping" as INF03
  card "INF-AS-PERSON-04 EmployeeRegistered" as INF04
  card "RL-AS-PERSON-01 EmployeeIdUniqueness" as RL01
  card "RL-AS-PERSON-02 EmailUniqueness" as RL02
  card "RL-AS-PERSON-03 RequiredFieldValidation" as RL03
  card "RL-AS-PERSON-04 ValidPeriodConsistency" as RL04
  card "RL-AS-PERSON-05 ResignedEmployeeUpdateRestriction" as RL05
  card "RL-AS-PERSON-06 AffiliationOrgValidityCheck" as RL06
  card "RL-AS-PERSON-07 SearchConditionValidation" as RL07
  card "RL-AS-PERSON-08 PaginationLimit" as RL08
}

Admin -- SCR001
Admin -- SCR002
Admin -- SCR003
Admin -- SCR004
Admin -- SCR005
Operator -- SCR005
JB -- UC004
JB -- UC005
JB -- UC006
AG -- UC004
AG -- UC005
AG -- UC006

SCR001 ..> UC004 : Call
SCR001 ..> UC005 : Call
SCR002 ..> UC001 : Call
SCR002 ..> UC002 : Call
SCR003 ..> UC006 : Call
SCR004 ..> UC007 : Call
SCR005 ..> UC008 : Call

Admin -- UC003

UC001 ..> ES01 : Data Exchange
UC002 ..> ES01 : Data Exchange
UC003 ..> ES01 : Data Exchange
UC004 ..> ES01 : Data Exchange
UC005 ..> ES01 : Data Exchange
UC006 ..> ES01 : Data Exchange
UC007 ..> ES01 : Data Exchange
UC008 ..> ES01 : Data Exchange

UC001 -- INF01 : Use
UC002 -- INF01 : Use
UC003 -- INF01 : Use
UC004 -- INF01 : Use
UC005 -- INF01 : Use
UC006 -- INF01 : Use
UC006 -- INF03 : Use
UC007 -- INF03 : Use
UC008 -- INF03 : Use

UC001 -- RL01 : Apply
UC001 -- RL02 : Apply
UC001 -- RL03 : Apply
UC001 -- RL04 : Apply
UC001 -- RL06 : Apply
UC002 -- RL02 : Apply
UC002 -- RL04 : Apply
UC002 -- RL05 : Apply
UC004 -- RL07 : Apply
UC005 -- RL08 : Apply
UC007 -- RL06 : Apply
@enduml
```

## 3. 日本語版 UC複合図

```plantuml
@startuml
title AS-PERSON-ROLE 人ロール UC複合図（日本語版）
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

rectangle "フロントエンド" {
  usecase "SCR-AS-PERSON-001 従業員一覧" as SCR001
  usecase "SCR-AS-PERSON-002 従業員登録・編集" as SCR002
  usecase "SCR-AS-PERSON-003 従業員詳細" as SCR003
  usecase "SCR-AS-PERSON-004 所属変更" as SCR004
  usecase "SCR-AS-PERSON-005 部署変更履歴" as SCR005
}

rectangle "AS 関連付けモジュール" {
  usecase "UC-AS-PERSON-001 従業員を登録する" as UC001
  usecase "UC-AS-PERSON-002 従業員を更新する" as UC002
  usecase "UC-AS-PERSON-003 従業員を無効化する" as UC003
  usecase "UC-AS-PERSON-004 従業員を検索する" as UC004
  usecase "UC-AS-PERSON-005 従業員一覧を取得する" as UC005
  usecase "UC-AS-PERSON-006 従業員の詳細を取得する" as UC006
  usecase "UC-AS-PERSON-007 所属組織を変更する" as UC007
  usecase "UC-AS-PERSON-008 部署変更履歴を取得する" as UC008
}

rectangle "システムレイヤー" {
  card "INF-AS-PERSON-01 従業員" as INF01
  card "INF-AS-PERSON-02 人ロール" as INF02
  card "INF-AS-PERSON-03 所属対応表" as INF03
  card "INF-AS-PERSON-04 従業員登録完了" as INF04
  card "RL-AS-PERSON-01 従業員ID一意性" as RL01
  card "RL-AS-PERSON-02 メールアドレス一意性" as RL02
  card "RL-AS-PERSON-03 必須項目入力チェック" as RL03
  card "RL-AS-PERSON-04 有効期間の整合性" as RL04
  card "RL-AS-PERSON-05 退社後の更新制限" as RL05
  card "RL-AS-PERSON-06 所属組織の有効性チェック" as RL06
  card "RL-AS-PERSON-07 検索条件のバリデーション" as RL07
  card "RL-AS-PERSON-08 ページネーション制限" as RL08
}

Admin -- SCR001
Admin -- SCR002
Admin -- SCR003
Admin -- SCR004
Admin -- SCR005
Operator -- SCR005
JB -- UC004
JB -- UC005
JB -- UC006
AG -- UC004
AG -- UC005
AG -- UC006

SCR001 ..> UC004 : 呼び出し
SCR001 ..> UC005 : 呼び出し
SCR002 ..> UC001 : 呼び出し
SCR002 ..> UC002 : 呼び出し
SCR003 ..> UC006 : 呼び出し
SCR004 ..> UC007 : 呼び出し
SCR005 ..> UC008 : 呼び出し

Admin -- UC003

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
UC001 -- RL03 : 適用
UC001 -- RL04 : 適用
UC001 -- RL06 : 適用
UC002 -- RL02 : 適用
UC002 -- RL04 : 適用
UC002 -- RL05 : 適用
UC004 -- RL07 : 適用
UC005 -- RL08 : 適用
UC007 -- RL06 : 適用
@enduml
```

## 4. ユースケース一覧

| ユースケースID | ユースケース名 | 英語名 | 説明 | 関連アクター |
|----------------|----------------|--------|------|--------------|
| UC-AS-PERSON-001 | 従業員を登録する | RegisterEmployee | 新しい従業員を登録する（PartyとPersonRoleを自動作成） | AC-AS-01 |
| UC-AS-PERSON-002 | 従業員を更新する | UpdateEmployee | 従業員の基本情報を更新する | AC-AS-01 |
| UC-AS-PERSON-003 | 従業員を無効化する | InvalidateEmployee | 従業員を退社処理する | AC-AS-01 |
| UC-AS-PERSON-004 | 従業員を検索する | SearchEmployee | 条件に合致する従業員を検索する | AC-AS-01, AC-AS-03, AC-AS-04 |
| UC-AS-PERSON-005 | 従業員一覧を取得する | GetEmployeeList | 従業員の一覧をページネーションで取得する | AC-AS-01, AC-AS-03, AC-AS-04 |
| UC-AS-PERSON-006 | 従業員の詳細を取得する | GetEmployeeDetail | 指定した従業員の詳細情報を取得する（所属情報を含む） | AC-AS-01, AC-AS-03, AC-AS-04 |
| UC-AS-PERSON-007 | 所属組織を変更する | ChangeAffiliation | 従業員の所属組織・部署を変更する | AC-AS-01 |
| UC-AS-PERSON-008 | 部署変更履歴を取得する | GetDepartmentChangeHistory | 従業員の部署変更履歴を取得する | AC-AS-01, AC-AS-02 |

## 5. 画面一覧

| 画面ID | 画面名 | 英語名 | 説明 | 関連ユースケース |
|--------|--------|--------|------|------------------|
| SCR-AS-PERSON-001 | 従業員一覧 | EmployeeList | 従業員の検索・一覧表示・ページネーション | UC-AS-PERSON-004, UC-AS-PERSON-005 |
| SCR-AS-PERSON-002 | 従業員登録・編集 | EmployeeRegisterEdit | 新しい従業員の登録と既存従業員の情報編集 | UC-AS-PERSON-001, UC-AS-PERSON-002 |
| SCR-AS-PERSON-003 | 従業員詳細 | EmployeeDetail | 従業員の詳細情報と所属組織の確認 | UC-AS-PERSON-006 |
| SCR-AS-PERSON-004 | 所属変更 | AffiliationChange | 従業員の所属組織・部署の変更 | UC-AS-PERSON-007 |
| SCR-AS-PERSON-005 | 部署変更履歴 | DepartmentChangeHistory | 従業員の部署変更履歴の確認 | UC-AS-PERSON-008 |

## 6. イベント一覧

| イベントID | イベント名 | 英語名 | 説明 | 関連ユースケース |
|------------|------------|--------|------|------------------|
| EVT-AS-PERSON-001 | 従業員登録完了 | EmployeeRegistered | 新しい従業員が登録されたとき | UC-AS-PERSON-001 |
| EVT-AS-PERSON-002 | 従業員更新完了 | EmployeeUpdated | 従業員の基本情報が更新されたとき | UC-AS-PERSON-002 |
| EVT-AS-PERSON-003 | 従業員退社完了 | EmployeeResigned | 従業員が退社処理されたとき | UC-AS-PERSON-003 |
| EVT-AS-PERSON-004 | 所属組織変更完了 | AffiliationChanged | 従業員の所属組織が変更されたとき | UC-AS-PERSON-007 |

## 7. 情報一覧

| 情報ID | 情報名 | 英語名 | 説明 | 関連ユースケース |
|--------|--------|--------|------|------------------|
| INF-AS-PERSON-01 | 従業員 | Employee | 個人（従業員）の基本情報 | UC-AS-PERSON-001〜006 |
| INF-AS-PERSON-02 | 人ロール | PersonRole | 従業員に付与される人ロール | UC-AS-PERSON-001, 007 |
| INF-AS-PERSON-03 | 所属対応表 | AffiliationMapping | 従業員と組織・部署の所属関係 | UC-AS-PERSON-006〜008 |
| INF-AS-PERSON-04 | 従業員登録完了 | EmployeeRegistered | 従業員が登録されたことを示すイベント | UC-AS-PERSON-001 |

## 8. 条件一覧

| 条件ID | 条件名 | 英語名 | 説明 | 関連ユースケース |
|--------|--------|--------|------|------------------|
| RL-AS-PERSON-01 | 従業員ID一意性 | EmployeeIdUniqueness | 従業員IDはシステム内で一意であること | UC-AS-PERSON-001 |
| RL-AS-PERSON-02 | メールアドレス一意性 | EmailUniqueness | メールアドレスは重複できないこと | UC-AS-PERSON-001, 002 |
| RL-AS-PERSON-03 | 必須項目入力チェック | RequiredFieldValidation | 登録時に必須項目が入力されていること | UC-AS-PERSON-001 |
| RL-AS-PERSON-04 | 有効期間の整合性 | ValidPeriodConsistency | 有効開始日 ≤ 有効終了日であること | UC-AS-PERSON-001, 002 |
| RL-AS-PERSON-05 | 退社後の更新制限 | ResignedEmployeeUpdateRestriction | 退社処理された従業員は基本情報を変更できないこと | UC-AS-PERSON-002 |
| RL-AS-PERSON-06 | 所属組織の有効性チェック | AffiliationOrgValidityCheck | 所属組織は有効な組織であること | UC-AS-PERSON-001, 007 |
| RL-AS-PERSON-07 | 検索条件のバリデーション | SearchConditionValidation | 検索条件は有効な値であること | UC-AS-PERSON-004 |
| RL-AS-PERSON-08 | ページネーション制限 | PaginationLimit | 一覧取得のページサイズは1〜100件の範囲であること | UC-AS-PERSON-005 |

## 9. 参照情報

- ユースケース一覧: `03.システム境界/ユースケース一覧.md`
- 画面一覧: `04.システム/04.画面/画面一覧.md`
- イベント一覧: `../05 実装準備/イベント一覧.tsv`
- 情報一覧: `04.システム/01.情報/情報一覧.md`
- 条件一覧: `04.システム/03.条件/条件一覧.tsv`
