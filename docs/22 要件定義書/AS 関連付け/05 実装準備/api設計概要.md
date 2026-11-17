# API設計概要

AS関連付けのAPI設計概要を整理します。

## APIエンドポイント一覧

| 番号 | エンドポイント | HTTPメソッド | 説明 | ユースケース | 優先度 |
|------|---------------|-------------|------|-------------|--------|
| 01 | /api/parties | GET | パーティー一覧取得 | UC-AS-PARTY-005 | 高 |
| 02 | /api/parties | POST | パーティー登録 | UC-AS-PARTY-001 | 高 |
| 03 | /api/parties/{partyId} | GET | パーティー詳細取得 | UC-AS-PARTY-006 | 高 |
| 04 | /api/parties/{partyId} | PUT | パーティー更新 | UC-AS-PARTY-002 | 中 |
| 05 | /api/parties/{partyId}/invalidate | POST | パーティー無効化 | UC-AS-PARTY-003 | 中 |
| 06 | /api/parties/search | GET | パーティー検索 | UC-AS-PARTY-004 | 高 |
| 07 | /api/parties/{partyId}/roles | POST | ロール付与 | UC-AS-PARTY-007 | 中 |
| 08 | /api/parties/{partyId}/roles/{roleId} | DELETE | ロール解除 | UC-AS-PARTY-008 | 中 |
| 09 | /api/employees | GET | 従業員一覧取得 | UC-AS-PERSON-005 | 高 |
| 10 | /api/employees | POST | 従業員登録 | UC-AS-PERSON-001 | 高 |
| 11 | /api/employees/{employeeId} | GET | 従業員詳細取得 | UC-AS-PERSON-006 | 高 |
| 12 | /api/employees/{employeeId} | PUT | 従業員更新 | UC-AS-PERSON-002 | 中 |
| 13 | /api/employees/{employeeId}/invalidate | POST | 従業員無効化 | UC-AS-PERSON-003 | 中 |
| 14 | /api/employees/search | GET | 従業員検索 | UC-AS-PERSON-004 | 高 |
| 15 | /api/employees/{employeeId}/affiliation | PUT | 所属組織変更 | UC-AS-PERSON-007 | 中 |
| 16 | /api/employees/{employeeId}/transfer-history | GET | 部署変更履歴取得 | UC-AS-PERSON-008 | 低 |
| 17 | /api/organizations | GET | 組織一覧取得 | UC-AS-ORG-007 | 高 |
| 18 | /api/organizations | POST | 組織登録 | UC-AS-ORG-001 | 高 |
| 19 | /api/organizations/{orgId} | GET | 組織詳細取得 | UC-AS-ORG-008 | 高 |
| 20 | /api/organizations/{orgId} | PUT | 組織更新 | UC-AS-ORG-002 | 中 |
| 21 | /api/organizations/{orgId}/invalidate | POST | 組織無効化 | UC-AS-ORG-003 | 中 |
| 22 | /api/organizations/search | GET | 組織検索 | UC-AS-ORG-006 | 高 |
| 23 | /api/companies | GET | 会社一覧取得 | UC-AS-ORG-009 | 高 |
| 24 | /api/companies | POST | 会社登録 | UC-AS-ORG-004 | 高 |
| 25 | /api/stores | GET | 店舗一覧取得 | UC-AS-ORG-010 | 高 |
| 26 | /api/stores | POST | 店舗登録 | UC-AS-ORG-005 | 高 |
| 27 | /api/organizations/{orgId}/hierarchy | PUT | 組織階層変更 | UC-AS-ORG-011 | 中 |
| 28 | /api/organizations/{orgId}/permission-scope | POST | 権限範囲定義 | UC-AS-ORG-012 | 中 |
| 29 | /api/organizations/{orgId}/change-history | GET | 組織変更履歴取得 | UC-AS-ORG-013 | 低 |
| 30 | /api/roles | GET | ロール一覧取得 | UC-AS-PARTY-ROLE-004 | 高 |
| 31 | /api/roles | POST | ロール定義登録 | UC-AS-PARTY-ROLE-001 | 中 |
| 32 | /api/roles/{roleId} | GET | ロール詳細取得 | UC-AS-PARTY-ROLE-005 | 高 |
| 33 | /api/roles/{roleId} | PUT | ロール定義更新 | UC-AS-PARTY-ROLE-002 | 中 |
| 34 | /api/roles/search | GET | ロール検索 | UC-AS-PARTY-ROLE-003 | 高 |
| 35 | /api/parties/{partyId}/assign-role | POST | パーティーにロール付与 | UC-AS-PARTY-ROLE-006 | 中 |
| 36 | /api/parties/{partyId}/revoke-role/{roleId} | DELETE | パーティーからロール解除 | UC-AS-PARTY-ROLE-007 | 中 |
| 37 | /api/roles/{roleId}/change-history | GET | ロール変更履歴取得 | UC-AS-PARTY-ROLE-008 | 低 |

## API設計上の考慮点

1. **ページネーション**: 一覧APIはページネーションをサポート
2. **検索**: 検索APIは条件付き検索をサポート
3. **有効期間管理**: 有効期間に基づく状態管理
4. **履歴管理**: 変更履歴の記録と取得
5. **権限管理**: 組織単位での権限範囲制御

## 参照

- [実装準備概要](./overview.md)
