# テーブル一覧

AS関連付け事業領域のテーブル一覧を定義します。

## テーブル一覧

| 番号 | テーブル名 | 説明 | 情報ID | 履歴管理要 | 証跡要 |
|------|-----------|------|--------|-----------|--------|
| 01 | party | パーティー基本情報 | INF-AS-PARTY-01 | Y | Y |
| 02 | party_type | パーティー種類マスター | INF-AS-PARTY-02 | N | N |
| 03 | party_role_mapping | パーティーとロールの対応 | INF-AS-PARTY-03 | Y | N |
| 04 | person_role | 人ロール情報 | INF-AS-PERSON-02 | Y | Y |
| 05 | employee | 従業員基本情報 | INF-AS-PERSON-01 | Y | Y |
| 06 | affiliation | 所属対応表 | INF-AS-PERSON-03 | Y | N |
| 07 | organization | 組織基本情報 | INF-AS-ORG-01 | Y | Y |
| 08 | organization_role | 組織ロール情報 | INF-AS-ORG-02 | Y | Y |
| 09 | organization_hierarchy | 組織階層 | INF-AS-ORG-03 | Y | N |
| 10 | company | 会社基本情報 | INF-AS-ORG-04 | Y | Y |
| 11 | store | 店舗基本情報 | INF-AS-ORG-05 | Y | Y |
| 12 | organization_type | 組織種類マスター | INF-AS-ORG-06 | N | N |
| 13 | party_role | パーティーロール定義 | INF-AS-PARTY-ROLE-01 | Y | Y |
| 14 | role_type | ロール種類マスター | INF-AS-PARTY-ROLE-02 | N | N |
| 15 | role_validity_period | ロール有効期間 | INF-AS-PARTY-ROLE-03 | Y | N |

## テーブル設計上の考慮点

1. **履歴管理**: 履歴管理が必要なテーブルは有効期間カラム(validFrom/validTo)を含む
2. **証跡管理**: 証跡が必要なテーブルは変更者・変更日時カラムを含む
3. **インデックス**: 検索頻度の高いカラムにインデックスを適用
4. **制約**: 一意性制約・外部キー制約を適切に定義

## テーブル間の関係

```
party (パーティ)
    │
    ├─────────────────┐
    │                 │
party_role_mapping  organization_hierarchy
    │                 │
    ▼                 ▼
person_role       organization
    │                 │
    ▼                 ├──────────────┐
employee            company        store
```

## 参照

- [実装準備概要](../../22%20要件定義書/AS%20関連付け/05%20実装準備/overview.md)
- [データベース設計概要](../../22%20要件定義書/AS%20関連付け/05%20実装準備/データベース設計概要.md)
