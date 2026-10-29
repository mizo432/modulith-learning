# テーブルカラム定義

## 概要

このディレクトリには、テーブルごとのカラム定義を格納します。

## テーブル一覧

| 番号 | テーブル名 | 定義書 |
|------|-----------|--------|
| 01 | party | [party.md](./party.md) |
| 02 | party_type | [party_type.md](./party_type.md) |
| 03 | party_role_mapping | [party_role_mapping.md](./party_role_mapping.md) |
| 04 | person_role | [person_role.md](./person_role.md) |
| 05 | employee | [employee.md](./employee.md) |
| 06 | affiliation | [affiliation.md](./affiliation.md) |
| 07 | organization | [organization.md](./organization.md) |
| 08 | organization_role | [organization_role.md](./organization_role.md) |
| 09 | organization_hierarchy | [organization_hierarchy.md](./organization_hierarchy.md) |
| 10 | company | [company.md](./company.md) |
| 11 | store | [store.md](./store.md) |
| 12 | organization_type | [organization_type.md](./organization_type.md) |
| 13 | party_role | [party_role.md](./party_role.md) |
| 14 | role_type | [role_type.md](./role_type.md) |
| 15 | role_validity_period | [role_validity_period.md](./role_validity_period.md) |

## 命名規則

- テーブル名は複数形で命名（例: `customers`, `order_items`）
- カラム名はスネークケースで命名
- 履歴データを格納するテーブル名はプレフィックス `history_` で始まる

## 参照

- データ設計スキル: `.agents/skills/data-design/SKILL.md`
- [テーブル一覧](../01%20table一覧.md)
