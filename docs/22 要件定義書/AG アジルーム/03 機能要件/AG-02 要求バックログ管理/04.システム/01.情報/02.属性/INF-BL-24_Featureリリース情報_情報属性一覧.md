# INF-BL-24 Featureリリース情報_情報属性一覧

## 概要

| 項目 | 内容 |
|------|------|
| 情報ID | INF-BL-24 |
| 情報名 | Featureリリース情報 |
| 情報種類 | リソース |
| 履歴管理要 | Y |
| 証跡要 | N |
| 実現方法 | DB |
| 説明 | Featureのリリース予定、準備状況、含まれるStory |

## 個体指定子（主キー）

| 属性名 | 型 | 必須 | 説明 |
|--------|----|------|------|
| featureId | Long | 必須 | リリース情報の対象FeatureのID（INF-BL-01の外部キー） |

## 属性

| 属性名 | 型 | 必須 | 説明 |
|--------|----|------|------|
| plannedReleaseDate | Date | 任意 | 予定リリース日 |
| actualReleaseDate | Date | 任意 | 実際リリース日 |
| releaseName | String | 任意 | リリース名 |
| releaseReadiness | Boolean | 必須 | Releaseに含められる準備が完了したか |
| readinessBlockers | String | 任意 | 準備を妨げている要因 |
| includedStoryCount | Integer | 必須 | リリースに含まれるStory数 |
| updatedAt | DateTime | 必須 | 最終更新日時 |

## 関連情報

| 情報ID | 情報名 | 関係 | 説明 |
|--------|--------|------|------|
| INF-BL-01 | バックログ項目 | 1対1 | リリース情報の対象Feature |
| INF-BL-06 | バックログ階層 | 1対多 | リリースに含まれるStory |

## エンティティの3大要素

1. **単一性 (Oneness)**: 1つのFeatureのリリース情報が1つのレコード
2. **同一性 (Sameness)**: featureIdで一意に識別
3. **カテゴリ (Category)**: Featureのリリース情報
