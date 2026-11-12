# UC複合図

AS-ORG-ROLE 組織ロール業務領域で扱うユースケース、画面、イベント、情報、条件を関連づけた図です。

## 1. 目的

本書は、AS-ORG-ROLE 組織ロール業務領域のシステム境界レイヤーで扱うユースケース、画面、イベント、情報、条件を関連づけたUC複合図です。

## 2. PlantUML UC複合図（英語版）

```plantuml
@startuml
title AS-ORG-ROLE OrganizationRole UC Composite Diagram
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
  usecase "SCR-AS-ORG-001 OrganizationList" as SCR001
  usecase "SCR-AS-ORG-002 OrganizationRegisterEdit" as SCR002
  usecase "SCR-AS-ORG-003 OrganizationDetail" as SCR003
  usecase "SCR-AS-ORG-004 CompanyList" as SCR004
  usecase "SCR-AS-ORG-005 CompanyRegisterEdit" as SCR005
  usecase "SCR-AS-ORG-006 StoreList" as SCR006
  usecase "SCR-AS-ORG-007 StoreRegisterEdit" as SCR007
  usecase "SCR-AS-ORG-008 OrganizationHierarchyChange" as SCR008
  usecase "SCR-AS-ORG-009 OrganizationChangeHistory" as SCR009
}

rectangle "AS Association Module" {
  usecase "UC-AS-ORG-001 RegisterOrganization" as UC001
  usecase "UC-AS-ORG-002 UpdateOrganization" as UC002
  usecase "UC-AS-ORG-003 InvalidateOrganization" as UC003
  usecase "UC-AS-ORG-004 RegisterCompany" as UC004
  usecase "UC-AS-ORG-005 RegisterStore" as UC005
  usecase "UC-AS-ORG-006 SearchOrganization" as UC006
  usecase "UC-AS-ORG-007 GetOrganizationList" as UC007
  usecase "UC-AS-ORG-008 GetOrganizationDetail" as UC008
  usecase "UC-AS-ORG-009 GetCompanyList" as UC009
  usecase "UC-AS-ORG-010 GetStoreList" as UC010
  usecase "UC-AS-ORG-011 ChangeOrganizationHierarchy" as UC011
  usecase "UC-AS-ORG-012 DefinePermissionScope" as UC012
  usecase "UC-AS-ORG-013 GetOrganizationChangeHistory" as UC013
}

rectangle "System Layer" {
  card "INF-AS-ORG-01 Organization" as INF01
  card "INF-AS-ORG-02 OrganizationRole" as INF02
  card "INF-AS-ORG-03 OrganizationHierarchy" as INF03
  card "INF-AS-ORG-04 Company" as INF04
  card "INF-AS-ORG-05 Store" as INF05
  card "INF-AS-ORG-06 OrganizationType" as INF06
  card "INF-AS-ORG-07 OrganizationRegistered" as INF07
  card "RL-AS-ORG-01 OrganizationIdUniqueness" as RL01
  card "RL-AS-ORG-02 CompanyCodeUniqueness" as RL02
  card "RL-AS-ORG-03 StoreCodeUniqueness" as RL03
  card "RL-AS-ORG-04 RequiredFieldValidation" as RL04
  card "RL-AS-ORG-05 ValidPeriodConsistency" as RL05
  card "RL-AS-ORG-06 HierarchyCircularRefProhibition" as RL06
  card "RL-AS-ORG-07 ParentOrgValidityCheck" as RL07
  card "RL-AS-ORG-08 DeletedOrgUpdateRestriction" as RL08
  card "RL-AS-ORG-09 SearchConditionValidation" as RL09
  card "RL-AS-ORG-10 PaginationLimit" as RL10
}

Admin -- SCR001
Admin -- SCR002
Admin -- SCR003
Admin -- SCR004
Admin -- SCR005
Admin -- SCR006
Admin -- SCR007
Admin -- SCR008
Admin -- SCR009
Operator -- SCR009
JB -- UC006
JB -- UC007
JB -- UC008
JB -- UC009
JB -- UC010
AG -- UC006
AG -- UC007
AG -- UC008
AG -- UC009
AG -- UC010

SCR001 ..> UC006 : Call
SCR001 ..> UC007 : Call
SCR002 ..> UC001 : Call
SCR002 ..> UC002 : Call
SCR003 ..> UC008 : Call
SCR004 ..> UC009 : Call
SCR005 ..> UC004 : Call
SCR006 ..> UC010 : Call
SCR007 ..> UC005 : Call
SCR008 ..> UC011 : Call
SCR009 ..> UC013 : Call

Admin -- UC012
Operator -- UC013

UC001 ..> ES01 : Data Exchange
UC002 ..> ES01 : Data Exchange
UC003 ..> ES01 : Data Exchange
UC004 ..> ES01 : Data Exchange
UC005 ..> ES01 : Data Exchange
UC006 ..> ES01 : Data Exchange
UC007 ..> ES01 : Data Exchange
UC008 ..> ES01 : Data Exchange
UC009 ..> ES01 : Data Exchange
UC010 ..> ES01 : Data Exchange
UC011 ..> ES01 : Data Exchange
UC012 ..> ES01 : Data Exchange
UC013 ..> ES01 : Data Exchange

UC001 -- INF01 : Use
UC002 -- INF01 : Use
UC003 -- INF01 : Use
UC004 -- INF04 : Use
UC005 -- INF05 : Use
UC006 -- INF01 : Use
UC007 -- INF01 : Use
UC008 -- INF01 : Use
UC009 -- INF04 : Use
UC010 -- INF05 : Use
UC011 -- INF03 : Use
UC012 -- INF02 : Use

UC001 -- RL01 : Apply
UC001 -- RL04 : Apply
UC001 -- RL07 : Apply
UC002 -- RL05 : Apply
UC002 -- RL08 : Apply
UC004 -- RL02 : Apply
UC004 -- RL04 : Apply
UC005 -- RL03 : Apply
UC005 -- RL04 : Apply
UC006 -- RL09 : Apply
UC007 -- RL10 : Apply
UC011 -- RL06 : Apply
UC011 -- RL07 : Apply
@enduml
```

## 3. 日本語版 UC複合図

```plantuml
@startuml
title AS-ORG-ROLE 組織ロール UC複合図（日本語版）
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
  usecase "SCR-AS-ORG-001 組織一覧" as SCR001
  usecase "SCR-AS-ORG-002 組織登録・編集" as SCR002
  usecase "SCR-AS-ORG-003 組織詳細" as SCR003
  usecase "SCR-AS-ORG-004 会社一覧" as SCR004
  usecase "SCR-AS-ORG-005 会社登録・編集" as SCR005
  usecase "SCR-AS-ORG-006 店舗一覧" as SCR006
  usecase "SCR-AS-ORG-007 店舗登録・編集" as SCR007
  usecase "SCR-AS-ORG-008 組織階層変更" as SCR008
  usecase "SCR-AS-ORG-009 組織変更履歴" as SCR009
}

rectangle "AS 関連付けモジュール" {
  usecase "UC-AS-ORG-001 組織を登録する" as UC001
  usecase "UC-AS-ORG-002 組織を更新する" as UC002
  usecase "UC-AS-ORG-003 組織を無効化する" as UC003
  usecase "UC-AS-ORG-004 会社を登録する" as UC004
  usecase "UC-AS-ORG-005 店舗を登録する" as UC005
  usecase "UC-AS-ORG-006 組織を検索する" as UC006
  usecase "UC-AS-ORG-007 組織一覧を取得する" as UC007
  usecase "UC-AS-ORG-008 組織の詳細を取得する" as UC008
  usecase "UC-AS-ORG-009 会社一覧を取得する" as UC009
  usecase "UC-AS-ORG-010 店舗一覧を取得する" as UC010
  usecase "UC-AS-ORG-011 組織階層を変更する" as UC011
  usecase "UC-AS-ORG-012 権限範囲を定義する" as UC012
  usecase "UC-AS-ORG-013 組織変更履歴を取得する" as UC013
}

rectangle "システムレイヤー" {
  card "INF-AS-ORG-01 組織" as INF01
  card "INF-AS-ORG-02 組織ロール" as INF02
  card "INF-AS-ORG-03 組織階層" as INF03
  card "INF-AS-ORG-04 会社" as INF04
  card "INF-AS-ORG-05 店舗" as INF05
  card "INF-AS-ORG-06 組織種類" as INF06
  card "INF-AS-ORG-07 組織登録完了" as INF07
  card "RL-AS-ORG-01 組織ID一意性" as RL01
  card "RL-AS-ORG-02 会社コード一意性" as RL02
  card "RL-AS-ORG-03 店舗コード一意性" as RL03
  card "RL-AS-ORG-04 必須項目入力チェック" as RL04
  card "RL-AS-ORG-05 有効期間の整合性" as RL05
  card "RL-AS-ORG-06 階層循環参照禁止" as RL06
  card "RL-AS-ORG-07 親組織の有効性チェック" as RL07
  card "RL-AS-ORG-08 廃止組織の更新制限" as RL08
  card "RL-AS-ORG-09 検索条件のバリデーション" as RL09
  card "RL-AS-ORG-10 ページネーション制限" as RL10
}

Admin -- SCR001
Admin -- SCR002
Admin -- SCR003
Admin -- SCR004
Admin -- SCR005
Admin -- SCR006
Admin -- SCR007
Admin -- SCR008
Admin -- SCR009
Operator -- SCR009
JB -- UC006
JB -- UC007
JB -- UC008
JB -- UC009
JB -- UC010
AG -- UC006
AG -- UC007
AG -- UC008
AG -- UC009
AG -- UC010

SCR001 ..> UC006 : 呼び出し
SCR001 ..> UC007 : 呼び出し
SCR002 ..> UC001 : 呼び出し
SCR002 ..> UC002 : 呼び出し
SCR003 ..> UC008 : 呼び出し
SCR004 ..> UC009 : 呼び出し
SCR005 ..> UC004 : 呼び出し
SCR006 ..> UC010 : 呼び出し
SCR007 ..> UC005 : 呼び出し
SCR008 ..> UC011 : 呼び出し
SCR009 ..> UC013 : 呼び出し

Admin -- UC012
Operator -- UC013

UC001 ..> ES01 : データ交換
UC002 ..> ES01 : データ交換
UC003 ..> ES01 : データ交換
UC004 ..> ES01 : データ交換
UC005 ..> ES01 : データ交換
UC006 ..> ES01 : データ交換
UC007 ..> ES01 : データ交換
UC008 ..> ES01 : データ交換
UC009 ..> ES01 : データ交換
UC010 ..> ES01 : データ交換
UC011 ..> ES01 : データ交換
UC012 ..> ES01 : データ交換
UC013 ..> ES01 : データ交換

UC001 -- INF01 : 使用
UC002 -- INF01 : 使用
UC003 -- INF01 : 使用
UC004 -- INF04 : 使用
UC005 -- INF05 : 使用
UC006 -- INF01 : 使用
UC007 -- INF01 : 使用
UC008 -- INF01 : 使用
UC009 -- INF04 : 使用
UC010 -- INF05 : 使用
UC011 -- INF03 : 使用
UC012 -- INF02 : 使用

UC001 -- RL01 : 適用
UC001 -- RL04 : 適用
UC001 -- RL07 : 適用
UC002 -- RL05 : 適用
UC002 -- RL08 : 適用
UC004 -- RL02 : 適用
UC004 -- RL04 : 適用
UC005 -- RL03 : 適用
UC005 -- RL04 : 適用
UC006 -- RL09 : 適用
UC007 -- RL10 : 適用
UC011 -- RL06 : 適用
UC011 -- RL07 : 適用
@enduml
```

## 4. ユースケース一覧

| ユースケースID | ユースケース名 | 英語名 | 説明 | 関連アクター |
|----------------|----------------|--------|------|--------------|
| UC-AS-ORG-001 | 組織を登録する | RegisterOrganization | 新しい組織・部署を登録する | AC-AS-01 |
| UC-AS-ORG-002 | 組織を更新する | UpdateOrganization | 組織・部署の基本情報を更新する | AC-AS-01 |
| UC-AS-ORG-003 | 組織を無効化する | InvalidateOrganization | 組織・部署を廃止する | AC-AS-01 |
| UC-AS-ORG-004 | 会社を登録する | RegisterCompany | 新しい会社を登録する | AC-AS-01 |
| UC-AS-ORG-005 | 店舗を登録する | RegisterStore | 新しい店舗を登録する | AC-AS-01 |
| UC-AS-ORG-006 | 組織を検索する | SearchOrganization | 条件に合致する組織を検索する | AC-AS-01, AC-AS-03, AC-AS-04 |
| UC-AS-ORG-007 | 組織一覧を取得する | GetOrganizationList | 組織の一覧をページネーションで取得する | AC-AS-01, AC-AS-03, AC-AS-04 |
| UC-AS-ORG-008 | 組織の詳細を取得する | GetOrganizationDetail | 指定した組織の詳細情報を取得する | AC-AS-01, AC-AS-03, AC-AS-04 |
| UC-AS-ORG-009 | 会社一覧を取得する | GetCompanyList | 会社の一覧を取得する | AC-AS-01, AC-AS-03, AC-AS-04 |
| UC-AS-ORG-010 | 店舗一覧を取得する | GetStoreList | 店舗の一覧を取得する（会社コードフィルタ対応） | AC-AS-01, AC-AS-03, AC-AS-04 |
| UC-AS-ORG-011 | 組織階層を変更する | ChangeOrganizationHierarchy | 組織の階層構造を変更する | AC-AS-01 |
| UC-AS-ORG-012 | 権限範囲を定義する | DefinePermissionScope | 組織単位での権限範囲を定義する | AC-AS-01 |
| UC-AS-ORG-013 | 組織変更履歴を取得する | GetOrganizationChangeHistory | 組織の変更履歴を取得する | AC-AS-01, AC-AS-02 |

## 5. 画面一覧

| 画面ID | 画面名 | 英語名 | 説明 | 関連ユースケース |
|--------|--------|--------|------|------------------|
| SCR-AS-ORG-001 | 組織一覧 | OrganizationList | 組織・部署の検索・一覧表示・ページネーション | UC-AS-ORG-006, UC-AS-ORG-007 |
| SCR-AS-ORG-002 | 組織登録・編集 | OrganizationRegisterEdit | 新しい組織・部署の登録と既存組織の情報編集 | UC-AS-ORG-001, UC-AS-ORG-002 |
| SCR-AS-ORG-003 | 組織詳細 | OrganizationDetail | 組織の詳細情報と階層構造の確認 | UC-AS-ORG-008 |
| SCR-AS-ORG-004 | 会社一覧 | CompanyList | 会社の検索・一覧表示 | UC-AS-ORG-009 |
| SCR-AS-ORG-005 | 会社登録・編集 | CompanyRegisterEdit | 新しい会社の登録と既存会社の情報編集 | UC-AS-ORG-004 |
| SCR-AS-ORG-006 | 店舗一覧 | StoreList | 店舗の検索・一覧表示（会社コードフィルタ対応） | UC-AS-ORG-010 |
| SCR-AS-ORG-007 | 店舗登録・編集 | StoreRegisterEdit | 新しい店舗の登録と既存店舗の情報編集 | UC-AS-ORG-005 |
| SCR-AS-ORG-008 | 組織階層変更 | OrganizationHierarchyChange | 組織の階層構造の変更 | UC-AS-ORG-011 |
| SCR-AS-ORG-009 | 組織変更履歴 | OrganizationChangeHistory | 組織の変更履歴の確認 | UC-AS-ORG-013 |

## 6. イベント一覧

| イベントID | イベント名 | 英語名 | 説明 | 関連ユースケース |
|------------|------------|--------|------|------------------|
| EVT-AS-ORG-001 | 組織登録完了 | OrganizationRegistered | 新しい組織が登録されたとき | UC-AS-ORG-001 |
| EVT-AS-ORG-002 | 組織更新完了 | OrganizationUpdated | 組織の基本情報が更新されたとき | UC-AS-ORG-002 |
| EVT-AS-ORG-003 | 組織廃止完了 | OrganizationDeleted | 組織が廃止されたとき | UC-AS-ORG-003 |
| EVT-AS-ORG-004 | 会社登録完了 | CompanyRegistered | 新しい会社が登録されたとき | UC-AS-ORG-004 |
| EVT-AS-ORG-005 | 店舗登録完了 | StoreRegistered | 新しい店舗が登録されたとき | UC-AS-ORG-005 |
| EVT-AS-ORG-006 | 組織階層変更完了 | OrganizationHierarchyChanged | 組織の階層構造が変更されたとき | UC-AS-ORG-011 |
| EVT-AS-ORG-007 | 権限範囲定義完了 | PermissionScopeDefined | 組織の権限範囲が定義されたとき | UC-AS-ORG-012 |

## 7. 情報一覧

| 情報ID | 情報名 | 英語名 | 説明 | 関連ユースケース |
|--------|--------|--------|------|------------------|
| INF-AS-ORG-01 | 組織 | Organization | 組織・部署の基本情報 | UC-AS-ORG-001〜003, 006〜008 |
| INF-AS-ORG-02 | 組織ロール | OrganizationRole | 組織に付与される組織ロール | UC-AS-ORG-001, 011 |
| INF-AS-ORG-03 | 組織階層 | OrganizationHierarchy | 組織の階層構造（親・子関係） | UC-AS-ORG-011 |
| INF-AS-ORG-04 | 会社 | Company | 会社の基本情報 | UC-AS-ORG-004, 009 |
| INF-AS-ORG-05 | 店舗 | Store | 店舗の基本情報 | UC-AS-ORG-005, 010 |
| INF-AS-ORG-06 | 組織種類 | OrganizationType | 組織の種類（会社、部署、店舗など） | UC-AS-ORG-001, 004, 005 |
| INF-AS-ORG-07 | 組織登録完了 | OrganizationRegistered | 組織が登録されたことを示すイベント | UC-AS-ORG-001, 004, 005 |

## 8. 条件一覧

| 条件ID | 条件名 | 英語名 | 説明 | 関連ユースケース |
|--------|--------|--------|------|------------------|
| RL-AS-ORG-01 | 組織ID一意性 | OrganizationIdUniqueness | 組織IDはシステム内で一意であること | UC-AS-ORG-001 |
| RL-AS-ORG-02 | 会社コード一意性 | CompanyCodeUniqueness | 会社コードは重複できないこと | UC-AS-ORG-004 |
| RL-AS-ORG-03 | 店舗コード一意性 | StoreCodeUniqueness | 店舗コードは会社内で一意であること | UC-AS-ORG-005 |
| RL-AS-ORG-04 | 必須項目入力チェック | RequiredFieldValidation | 登録時に必須項目が入力されていること | UC-AS-ORG-001, 004, 005 |
| RL-AS-ORG-05 | 有効期間の整合性 | ValidPeriodConsistency | 有効開始日 ≤ 有効終了日であること | UC-AS-ORG-001, 002 |
| RL-AS-ORG-06 | 階層循環参照禁止 | HierarchyCircularRefProhibition | 組織階層に循環参照がないこと | UC-AS-ORG-011 |
| RL-AS-ORG-07 | 親組織の有効性チェック | ParentOrgValidityCheck | 親組織は有効な組織であること | UC-AS-ORG-001, 011 |
| RL-AS-ORG-08 | 廃止組織の更新制限 | DeletedOrgUpdateRestriction | 廃止された組織は更新できないこと | UC-AS-ORG-002 |
| RL-AS-ORG-09 | 検索条件のバリデーション | SearchConditionValidation | 検索条件は有効な値であること | UC-AS-ORG-006 |
| RL-AS-ORG-10 | ページネーション制限 | PaginationLimit | 一覧取得のページサイズは1〜100件の範囲であること | UC-AS-ORG-007 |

## 9. 参照情報

- ユースケース一覧: `03.システム境界/ユースケース一覧.md`
- 画面一覧: `04.システム/04.画面/画面一覧.md`
- イベント一覧: `../05 実装準備/イベント一覧.tsv`
- 情報一覧: `04.システム/01.情報/情報一覧.md`
- 条件一覧: `04.システム/03.条件/条件一覧.tsv`
