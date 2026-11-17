# AS_関連付け データベース設計

## 概要

関連付け事業領域のデータベース設計資材を格納します。

## ディレクトリ構成

```
01 データベース設計/
├── index.md                # このファイル
├── 01 table一覧.md         # テーブル一覧
├── 01 table一覧.tsv        # テーブル一覧(TSV)
├── 02 テーブルカラム定義/    # テーブルカラム定義
│   ├── index.md
│   ├── party.md            # Partyテーブルカラム定義
│   ├── party_type.md       # PartyTypeマスターカラム定義
│   ├── party_role_mapping.md # Partyとロールの対応カラム定義
│   ├── person_role.md      # PersonRoleカラム定義
│   ├── employee.md         # Employeeカラム定義
│   ├── affiliation.md      # 所属対応表カラム定義
│   ├── organization.md     # Organizationカラム定義
│   ├── organization_role.md # OrganizationRoleカラム定義
│   ├── organization_hierarchy.md # 組織階層カラム定義
│   ├── company.md          # Companyカラム定義
│   ├── store.md            # Storeカラム定義
│   ├── organization_type.md # OrganizationTypeマスターカラム定義
│   ├── party_role.md       # PartyRole定義カラム定義
│   ├── role_type.md        # RoleTypeマスターカラム定義
│   └── role_validity_period.md # ロール有効期間カラム定義
└── 03 viewカラム定義/       # Viewカラム定義
    └── index.md
```

## 参照

- 事業領域一覧: `docs/11 事業領域/事業領域一覧.tsv`
- 業務領域一覧: `docs/12 業務領域/業務領域一覧.tsv`
- データ設計スキル: `.agents/skills/data-design/SKILL.md`
- テーブル一覧: [01 table一覧.md](01%20table一覧.md)
