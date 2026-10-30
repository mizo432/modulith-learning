# Story Map 情報属性一覧

ユーザーストーリーマップの基本情報を定義します。

## 情報概要

| 項目 | 内容 |
|------|------|
| 情報ID | INF-BL-13 |
| 情報名 | Story Map |
| 情報種類 | リソース |
| 説明 | ユーザーストーリーマップの基本情報・状態・バージョン |
| 履歴管理要 | Y |
| 証跡要 | Y |
| 実現方法 | DB |

## 個体指定子と属性

| 属性名 | 型 | 必須 | 説明 |
|--------|----|------|------|
| * id | Long | はい | Story Mapの一意な識別子 |
| mapName | String | はい | マップ名（最大200文字） |
| description | String | いいえ | マップの目的や対象業務（最大2000文字） |
| productId | Long | はい | 対象プロダクトの識別子 |
| creatorId | Long | はい | 作成者の識別子 |
| status | Enum | はい | 状態（DRAFT, IN_PROGRESS, PUBLISHED） |
| version | String | いいえ | バージョン番号 |
| createdAt | DateTime | はい | 作成日時 |
| updatedAt | DateTime | はい | 最終更新日時 |
| updatedById | Long | いいえ | 最終更新者の識別子 |

## 関連情報

| 情報ID | 情報名 | 関係 | 説明 |
|--------|--------|------|------|
| INF-BL-14 | Story Map Activity | 1:N | マップに属するActivity一覧 |
| INF-BL-17 | Story Map Release Slice | 1:N | マップに属するRelease Slice一覧 |
| INF-BL-19 | Story Map Persona | 1:N | マップに関連するPersona一覧 |
| INF-BL-20 | Story Map Comment | 1:N | マップへのコメント一覧 |
| INF-BL-21 | Story Map 変更履歴 | 1:N | マップの変更履歴 |
| INF-BL-01 | バックログ項目 | 参照 | 対象プロダクト |

## エンティティの3大要素

1. **単一性 (Oneness)**: 1つのユーザーストーリーマップを1つの単位として管理する
2. **同一性 (Sameness)**: idで一意に識別される
3. **カテゴリ (Category)**: ユーザーストーリーマップは、ユーザーの活動・タスク・ストーリーを整理するためのモデル
