# 実装準備

AS関連付け（Association）事業領域の実装準備ドキュメントです。

## 概要

本ドキュメントは、AS関連付けのRDRA要件定義書（フェーズ1〜3）とDDDモデル候補に基づき、実装に必要な情報を整理したものです。

## 目次

- [API設計概要](#api設計概要)
- [データベース設計概要](#データベース設計概要)
- [実装順序推奨](#実装順序推奨)
- [モジュール構成提案](#モジュール構成提案)

## API設計概要

AS関連付けのAPIは、RESTful APIとして設計します。

### APIエンドポイント一覧

| 番号 | エンドポイント | HTTPメソッド | 説明 | ユースケース |
|------|---------------|-------------|------|-------------|
| 01 | /api/parties | GET | パーティー一覧取得 | UC-AS-PARTY-005 |
| 02 | /api/parties | POST | パーティー登録 | UC-AS-PARTY-001 |
| 03 | /api/parties/{partyId} | GET | パーティー詳細取得 | UC-AS-PARTY-006 |
| 04 | /api/parties/{partyId} | PUT | パーティー更新 | UC-AS-PARTY-002 |
| 05 | /api/parties/{partyId}/invalidate | POST | パーティー無効化 | UC-AS-PARTY-003 |
| 06 | /api/parties/search | GET | パーティー検索 | UC-AS-PARTY-004 |
| 07 | /api/parties/{partyId}/roles | POST | ロール付与 | UC-AS-PARTY-007 |
| 08 | /api/parties/{partyId}/roles/{roleId} | DELETE | ロール解除 | UC-AS-PARTY-008 |
| 09 | /api/employees | GET | 従業員一覧取得 | UC-AS-PERSON-005 |
| 10 | /api/employees | POST | 従業員登録 | UC-AS-PERSON-001 |
| 11 | /api/employees/{employeeId} | GET | 従業員詳細取得 | UC-AS-PERSON-006 |
| 12 | /api/employees/{employeeId} | PUT | 従業員更新 | UC-AS-PERSON-002 |
| 13 | /api/employees/{employeeId}/invalidate | POST | 従業員無効化 | UC-AS-PERSON-003 |
| 14 | /api/employees/search | GET | 従業員検索 | UC-AS-PERSON-004 |
| 15 | /api/employees/{employeeId}/affiliation | PUT | 所属組織変更 | UC-AS-PERSON-007 |
| 16 | /api/employees/{employeeId}/transfer-history | GET | 部署変更履歴取得 | UC-AS-PERSON-008 |
| 17 | /api/organizations | GET | 組織一覧取得 | UC-AS-ORG-007 |
| 18 | /api/organizations | POST | 組織登録 | UC-AS-ORG-001 |
| 19 | /api/organizations/{orgId} | GET | 組織詳細取得 | UC-AS-ORG-008 |
| 20 | /api/organizations/{orgId} | PUT | 組織更新 | UC-AS-ORG-002 |
| 21 | /api/organizations/{orgId}/invalidate | POST | 組織無効化 | UC-AS-ORG-003 |
| 22 | /api/organizations/search | GET | 組織検索 | UC-AS-ORG-006 |
| 23 | /api/companies | GET | 会社一覧取得 | UC-AS-ORG-009 |
| 24 | /api/companies | POST | 会社登録 | UC-AS-ORG-004 |
| 25 | /api/stores | GET | 店舗一覧取得 | UC-AS-ORG-010 |
| 26 | /api/stores | POST | 店舗登録 | UC-AS-ORG-005 |
| 27 | /api/organizations/{orgId}/hierarchy | PUT | 組織階層変更 | UC-AS-ORG-011 |
| 28 | /api/organizations/{orgId}/permission-scope | POST | 権限範囲定義 | UC-AS-ORG-012 |
| 29 | /api/organizations/{orgId}/change-history | GET | 組織変更履歴取得 | UC-AS-ORG-013 |
| 30 | /api/roles | GET | ロール一覧取得 | UC-AS-PARTY-ROLE-004 |
| 31 | /api/roles | POST | ロール定義登録 | UC-AS-PARTY-ROLE-001 |
| 32 | /api/roles/{roleId} | GET | ロール詳細取得 | UC-AS-PARTY-ROLE-005 |
| 33 | /api/roles/{roleId} | PUT | ロール定義更新 | UC-AS-PARTY-ROLE-002 |
| 34 | /api/roles/search | GET | ロール検索 | UC-AS-PARTY-ROLE-003 |
| 35 | /api/parties/{partyId}/assign-role | POST | パーティーにロール付与 | UC-AS-PARTY-ROLE-006 |
| 36 | /api/parties/{partyId}/revoke-role/{roleId} | DELETE | パーティーからロール解除 | UC-AS-PARTY-ROLE-007 |
| 37 | /api/roles/{roleId}/change-history | GET | ロール変更履歴取得 | UC-AS-PARTY-ROLE-008 |

### API設計上の考慮点

1. **ページネーション**: 一覧APIはページネーションをサポート
2. **検索**: 検索APIは条件付き検索をサポート
3. **有効期間管理**: 有効期間に基づく状態管理
4. **履歴管理**: 変更履歴の記録と取得
5. **権限管理**: 組織単位での権限範囲制御

## データベース設計概要

AS関連付けのデータベースは、Party Roleパターンに基づいて設計します。

### テーブル一覧

| 番号 | テーブル名 | 説明 | 情報ID |
|------|-----------|------|--------|
| 01 | party | パーティー基本情報 | INF-AS-PARTY-01 |
| 02 | party_type | パーティー種類マスター | INF-AS-PARTY-02 |
| 03 | party_role_mapping | パーティーとロールの対応 | INF-AS-PARTY-03 |
| 04 | person_role | 人ロール情報 | INF-AS-PERSON-02 |
| 05 | employee | 従業員基本情報 | INF-AS-PERSON-01 |
| 06 | affiliation | 所属対応表 | INF-AS-PERSON-03 |
| 07 | organization | 組織基本情報 | INF-AS-ORG-01 |
| 08 | organization_role | 組織ロール情報 | INF-AS-ORG-02 |
| 09 | organization_hierarchy | 組織階層 | INF-AS-ORG-03 |
| 10 | company | 会社基本情報 | INF-AS-ORG-04 |
| 11 | store | 店舗基本情報 | INF-AS-ORG-05 |
| 12 | organization_type | 組織種類マスター | INF-AS-ORG-06 |
| 13 | party_role | パーティーロール定義 | INF-AS-PARTY-ROLE-01 |
| 14 | role_type | ロール種類マスター | INF-AS-PARTY-ROLE-02 |
| 15 | role_validity_period | ロール有効期間 | INF-AS-PARTY-ROLE-03 |

### テーブル設計上の考慮点

1. **履歴管理**: 履歴管理が必要なテーブルは有効期間カラム(validFrom/validTo)を含む
2. **証跡管理**: 証跡が必要なテーブルは変更者・変更日時カラムを含む
3. **インデックス**: 検索頻度の高いカラムにインデックスを適用
4. **制約**: 一意性制約・外部キー制約を適切に定義

## 実装順序推奨

AS関連付けの実装は、以下の順序で進めることを推奨します。

### フェーズ1: コアドメイン実装

| 優先度 | サブドメイン | 説明 | 推定工数 |
|--------|-------------|------|----------|
| 1 | AS-PARTY | パーティー管理の基本機能 | 中 |
| 2 | AS-PARTY-ROLE | ロール定義管理の基本機能 | 中 |

**理由**: Party Roleパターンの基盤となる機能であり、他のサブドメインが依存する部分

**実装内容**:
- PartyエンティティのCRUD
- PartyRoleエンティティのCRUD
- PartyとPartyRoleの関連付け
- 有効期間管理
- 履歴管理

### フェーズ2: サポートドメイン実装

| 優先度 | サブドメイン | 説明 | 推定工数 |
|--------|-------------|------|----------|
| 3 | AS-PERSON | 従業員管理の基本機能 | 中 |
| 4 | AS-ORG | 組織管理の基本機能 | 大 |

**理由**: コアドomainを支える補助的な機能

**実装内容**:
- 従業員のCRUD
- 所属管理
- 組織のCRUD
- 階層構造管理
- 会社・店舗管理

### フェーズ3: 高度な機能実装

| 優先度 | 機能 | 説明 | 推定工数 |
|--------|------|------|----------|
| 5 | 検索機能 | 条件付き検索 | 小 |
| 6 | 履歴機能 | 変更履歴の取得 | 小 |
| 7 | 権限管理 | 組織単位での権限制御 | 中 |

**理由**: 基本機能の上に構築される高度な機能

## モジュール構成提案

DDDモデル候補に基づき、以下のモジュール構成を提案します。

### モジュール構成

```
as-association/
├── as-party/              # パーティーコンテキスト
│   ├── as-party-core/     # Party管理
│   └── as-party-role/     # ロール定義管理
├── as-person/             # 従業員コンテキスト
│   └── as-person-core/    # 従業員管理
└── as-organization/       # 組織コンテキスト
    └── as-org-core/       # 組織管理
```

### モジュール間の依存関係

```
as-party (コアドメイン)
    ↑
    │ 依存
    ├─────────────────┐
    │                 │
as-person         as-organization
(サポートドメイン)   (サポートドメイン)
```

### モジュール構成上の考慮点

1. **独立性**: 各モジュールは自律的に動作可能
2. **依存関係**: 下流モジュールが上流モジュールに依存
3. **共通言語**: 各コンテキストで共通言語を定義
4. **API境界**: モジュール境界にAPIを配置

## 参照

- [AS-PARTY パーティー](../03%20機能要件/AS-PARTY%20パーティー/index.md)
- [AS-PARTY-ROLE パーティーロール](../03%20機能要件/AS-PARTY-ROLE%20パーティーロール/index.md)
- [AS-PERSON-ROLE 人ロール](../03%20機能要件/AS-PERSON-ROLE%20人ロール/index.md)
- [AS-ORG-ROLE 組織ロール](../03%20機能要件/AS-ORG-ROLE%20組織ロール/index.md)
- [DDDモデル候補](../04%20DDDモデル候補/overview.md)
