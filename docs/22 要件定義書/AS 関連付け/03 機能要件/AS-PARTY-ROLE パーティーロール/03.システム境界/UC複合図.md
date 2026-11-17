# UC複合図

AS-PARTY-ROLE パーティーロール業務領域で扱うユースケース、画面、イベント、情報、条件を関連づけた図です。

## 1. 目的

本書は、AS-PARTY-ROLE パーティーロール業務領域のシステム境界レイヤーで扱うユースケース、画面、イベント、情報、条件を関連づけたUC複合図です。

## 2. PlantUML UC複合図（英語版）

```plantuml
@startuml
title AS-PARTY-ROLE PartyRole UC Composite Diagram
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
  usecase "SCR-AS-PARTY-ROLE-001 RoleDefinitionList" as SCR001
  usecase "SCR-AS-PARTY-ROLE-002 RoleDefinitionRegisterEdit" as SCR002
  usecase "SCR-AS-PARTY-ROLE-003 RoleAssignment" as SCR003
  usecase "SCR-AS-PARTY-ROLE-004 RoleChangeHistory" as SCR004
}

rectangle "AS Association Module" {
  usecase "UC-AS-PARTY-ROLE-001 RegisterRoleDefinition" as UC001
  usecase "UC-AS-PARTY-ROLE-002 UpdateRoleDefinition" as UC002
  usecase "UC-AS-PARTY-ROLE-003 SearchRole" as UC003
  usecase "UC-AS-PARTY-ROLE-004 GetRoleList" as UC004
  usecase "UC-AS-PARTY-ROLE-005 GetRoleDetail" as UC005
  usecase "UC-AS-PARTY-ROLE-006 AssignRoleToParty" as UC006
  usecase "UC-AS-PARTY-ROLE-007 RemoveRoleFromParty" as UC007
  usecase "UC-AS-PARTY-ROLE-008 GetRoleChangeHistory" as UC008
}

rectangle "System Layer" {
  card "INF-AS-PARTY-ROLE-01 PartyRole" as INF01
  card "INF-AS-PARTY-ROLE-02 RoleType" as INF02
  card "INF-AS-PARTY-ROLE-03 RoleValidPeriod" as INF03
  card "INF-AS-PARTY-ROLE-04 RoleAssigned" as INF04
  card "RL-AS-PARTY-ROLE-01 RoleDefinitionIdUniqueness" as RL01
  card "RL-AS-PARTY-ROLE-02 RequiredFieldValidation" as RL02
  card "RL-AS-PARTY-ROLE-03 ValidPeriodConsistency" as RL03
  card "RL-AS-PARTY-ROLE-04 InvalidRoleAssignmentProhibition" as RL04
  card "RL-AS-PARTY-ROLE-05 RoleDuplicateProhibition" as RL05
  card "RL-AS-PARTY-ROLE-06 SearchConditionValidation" as RL06
  card "RL-AS-PARTY-ROLE-07 PaginationLimit" as RL07
}

Admin -- SCR001
Admin -- SCR002
Admin -- SCR003
Admin -- SCR004
Operator -- SCR004
JB -- UC003
JB -- UC004
JB -- UC005
AG -- UC003
AG -- UC004
AG -- UC005

SCR001 ..> UC003 : Call
SCR001 ..> UC004 : Call
SCR002 ..> UC001 : Call
SCR002 ..> UC002 : Call
SCR003 ..> UC006 : Call
SCR003 ..> UC007 : Call
SCR004 ..> UC008 : Call

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
UC006 -- INF03 : Use
UC007 -- INF03 : Use
UC008 -- INF03 : Use

UC001 -- RL01 : Apply
UC001 -- RL02 : Apply
UC001 -- RL03 : Apply
UC002 -- RL03 : Apply
UC003 -- RL06 : Apply
UC004 -- RL07 : Apply
UC006 -- RL04 : Apply
UC006 -- RL05 : Apply
@enduml
```

## 3. 日本語版 UC複合図

```plantuml
@startuml
title AS-PARTY-ROLE パーティーロール UC複合図（日本語版）
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
  usecase "SCR-AS-PARTY-ROLE-001 ロール定義一覧" as SCR001
  usecase "SCR-AS-PARTY-ROLE-002 ロール定義登録・編集" as SCR002
  usecase "SCR-AS-PARTY-ROLE-003 ロール割り当て" as SCR003
  usecase "SCR-AS-PARTY-ROLE-004 ロール変更履歴" as SCR004
}

rectangle "AS 関連付けモジュール" {
  usecase "UC-AS-PARTY-ROLE-001 ロール定義を登録する" as UC001
  usecase "UC-AS-PARTY-ROLE-002 ロール定義を更新する" as UC002
  usecase "UC-AS-PARTY-ROLE-003 ロールを検索する" as UC003
  usecase "UC-AS-PARTY-ROLE-004 ロール一覧を取得する" as UC004
  usecase "UC-AS-PARTY-ROLE-005 ロールの詳細を取得する" as UC005
  usecase "UC-AS-PARTY-ROLE-006 パーティーにロールを付与する" as UC006
  usecase "UC-AS-PARTY-ROLE-007 パーティーからロールを解除する" as UC007
  usecase "UC-AS-PARTY-ROLE-008 ロール変更履歴を取得する" as UC008
}

rectangle "システムレイヤー" {
  card "INF-AS-PARTY-ROLE-01 パーティーロール" as INF01
  card "INF-AS-PARTY-ROLE-02 ロール種類" as INF02
  card "INF-AS-PARTY-ROLE-03 ロール有効期間" as INF03
  card "INF-AS-PARTY-ROLE-04 ロール付与完了" as INF04
  card "RL-AS-PARTY-ROLE-01 ロール定義ID一意性" as RL01
  card "RL-AS-PARTY-ROLE-02 必須項目入力チェック" as RL02
  card "RL-AS-PARTY-ROLE-03 有効期間の整合性" as RL03
  card "RL-AS-PARTY-ROLE-04 無効ロールの付与禁止" as RL04
  card "RL-AS-PARTY-ROLE-05 ロール重複付与禁止" as RL05
  card "RL-AS-PARTY-ROLE-06 検索条件のバリデーション" as RL06
  card "RL-AS-PARTY-ROLE-07 ページネーション制限" as RL07
}

Admin -- SCR001
Admin -- SCR002
Admin -- SCR003
Admin -- SCR004
Operator -- SCR004
JB -- UC003
JB -- UC004
JB -- UC005
AG -- UC003
AG -- UC004
AG -- UC005

SCR001 ..> UC003 : 呼び出し
SCR001 ..> UC004 : 呼び出し
SCR002 ..> UC001 : 呼び出し
SCR002 ..> UC002 : 呼び出し
SCR003 ..> UC006 : 呼び出し
SCR003 ..> UC007 : 呼び出し
SCR004 ..> UC008 : 呼び出し

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
UC001 -- RL02 : 適用
UC001 -- RL03 : 適用
UC002 -- RL03 : 適用
UC003 -- RL06 : 適用
UC004 -- RL07 : 適用
UC006 -- RL04 : 適用
UC006 -- RL05 : 適用
@enduml
```

## 4. ユースケース一覧

| ユースケースID | ユースケース名 | 英語名 | 説明 | 関連アクター |
|----------------|----------------|--------|------|--------------|
| UC-AS-PARTY-ROLE-001 | ロール定義を登録する | RegisterRoleDefinition | 新しいロール定義を登録する | AC-AS-01 |
| UC-AS-PARTY-ROLE-002 | ロール定義を更新する | UpdateRoleDefinition | ロール定義を更新する | AC-AS-01 |
| UC-AS-PARTY-ROLE-003 | ロールを検索する | SearchRole | 条件に合致するロールを検索する | AC-AS-01, AC-AS-03, AC-AS-04 |
| UC-AS-PARTY-ROLE-004 | ロール一覧を取得する | GetRoleList | ロールの一覧をページネーションで取得する | AC-AS-01, AC-AS-03, AC-AS-04 |
| UC-AS-PARTY-ROLE-005 | ロールの詳細を取得する | GetRoleDetail | 指定したロールの詳細情報を取得する | AC-AS-01, AC-AS-03, AC-AS-04 |
| UC-AS-PARTY-ROLE-006 | パーティーにロールを付与する | AssignRoleToParty | パーティーにロールを付与する | AC-AS-01 |
| UC-AS-PARTY-ROLE-007 | パーティーからロールを解除する | RemoveRoleFromParty | パーティーからロールを解除する | AC-AS-01 |
| UC-AS-PARTY-ROLE-008 | ロール変更履歴を取得する | GetRoleChangeHistory | ロールの有効期間変更履歴を取得する | AC-AS-01, AC-AS-02 |

## 5. 画面一覧

| 画面ID | 画面名 | 英語名 | 説明 | 関連ユースケース |
|--------|--------|--------|------|------------------|
| SCR-AS-PARTY-ROLE-001 | ロール定義一覧 | RoleDefinitionList | ロール定義の検索・一覧表示・ページネーション | UC-AS-PARTY-ROLE-003, UC-AS-PARTY-ROLE-004 |
| SCR-AS-PARTY-ROLE-002 | ロール定義登録・編集 | RoleDefinitionRegisterEdit | 新しいロール定義の登録と既存ロール定義の情報編集 | UC-AS-PARTY-ROLE-001, UC-AS-PARTY-ROLE-002 |
| SCR-AS-PARTY-ROLE-003 | ロール割り当て | RoleAssignment | パーティーへのロール付与・解除 | UC-AS-PARTY-ROLE-006, UC-AS-PARTY-ROLE-007 |
| SCR-AS-PARTY-ROLE-004 | ロール変更履歴 | RoleChangeHistory | ロールの変更履歴の確認 | UC-AS-PARTY-ROLE-008 |

## 6. イベント一覧

| イベントID | イベント名 | 英語名 | 説明 | 関連ユースケース |
|------------|------------|--------|------|------------------|
| EVT-AS-PARTY-ROLE-001 | ロール定義登録完了 | RoleDefinitionRegistered | 新しいロール定義が登録されたとき | UC-AS-PARTY-ROLE-001 |
| EVT-AS-PARTY-ROLE-002 | ロール定義更新完了 | RoleDefinitionUpdated | ロール定義が更新されたとき | UC-AS-PARTY-ROLE-002 |
| EVT-AS-PARTY-ROLE-003 | パーティーロール付与完了 | PartyRoleAssigned | パーティーにロールが付与されたとき | UC-AS-PARTY-ROLE-006 |
| EVT-AS-PARTY-ROLE-004 | パーティーロール解除完了 | PartyRoleRemoved | パーティーからロールが解除されたとき | UC-AS-PARTY-ROLE-007 |

## 7. 情報一覧

| 情報ID | 情報名 | 英語名 | 説明 | 関連ユースケース |
|--------|--------|--------|------|------------------|
| INF-AS-PARTY-ROLE-01 | パーティーロール | PartyRole | パーティーに付与されるロールの定義 | UC-AS-PARTY-ROLE-001〜005 |
| INF-AS-PARTY-ROLE-02 | ロール種類 | RoleType | ロールの種類（人ロール、組織ロールなど） | UC-AS-PARTY-ROLE-001 |
| INF-AS-PARTY-ROLE-03 | ロール有効期間 | RoleValidPeriod | ロールの有効期間管理 | UC-AS-PARTY-ROLE-006〜008 |
| INF-AS-PARTY-ROLE-04 | ロール付与完了 | RoleAssigned | ロールが付与されたことを示すイベント | UC-AS-PARTY-ROLE-006 |

## 8. 条件一覧

| 条件ID | 条件名 | 英語名 | 説明 | 関連ユースケース |
|--------|--------|--------|------|------------------|
| RL-AS-PARTY-ROLE-01 | ロール定義ID一意性 | RoleDefinitionIdUniqueness | ロール定義IDはシステム内で一意であること | UC-AS-PARTY-ROLE-001 |
| RL-AS-PARTY-ROLE-02 | 必須項目入力チェック | RequiredFieldValidation | 登録時に必須項目が入力されていること | UC-AS-PARTY-ROLE-001 |
| RL-AS-PARTY-ROLE-03 | 有効期間の整合性 | ValidPeriodConsistency | 有効開始日 ≤ 有効終了日であること | UC-AS-PARTY-ROLE-001, 002 |
| RL-AS-PARTY-ROLE-04 | 無効ロールの付与禁止 | InvalidRoleAssignmentProhibition | 無効化されたロールは付与できないこと | UC-AS-PARTY-ROLE-006 |
| RL-AS-PARTY-ROLE-05 | ロール重複付与禁止 | RoleDuplicateProhibition | 同じロールを重複して付与できないこと | UC-AS-PARTY-ROLE-006 |
| RL-AS-PARTY-ROLE-06 | 検索条件のバリデーション | SearchConditionValidation | 検索条件は有効な値であること | UC-AS-PARTY-ROLE-003 |
| RL-AS-PARTY-ROLE-07 | ページネーション制限 | PaginationLimit | 一覧取得のページサイズは1〜100件の範囲であること | UC-AS-PARTY-ROLE-004 |

## 9. 参照情報

- ユースケース一覧: `03.システム境界/ユースケース一覧.md`
- 画面一覧: `04.システム/04.画面/画面一覧.md`
- イベント一覧: `../05 実装準備/イベント一覧.tsv`
- 情報一覧: `04.システム/01.情報/情報一覧.md`
- 条件一覧: `04.システム/03.条件/条件一覧.tsv`
