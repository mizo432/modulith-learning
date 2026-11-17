# INF-BL-22 Featureダッシュボード集計_情報属性一覧

## 概要

| 項目 | 内容 |
|------|------|
| 情報ID | INF-BL-22 |
| 情報名 | Featureダッシュボード集計 |
| 情報種類 | リソース |
| 履歴管理要 | N |
| 証跡要 | N |
| 実現方法 | DB |
| 説明 | Feature単位の開発進捗・価値実現・提供準備の集計情報 |

## 個体指定子（主キー）

| 属性名 | 型 | 必須 | 説明 |
|--------|----|------|------|
| featureId | Long | 必須 | 集計対象のFeatureのID（INF-BL-01の外部キー） |

## 属性

| 属性名 | 型 | 必須 | 説明 |
|--------|----|------|------|
| storyTotalCount | Integer | 必須 | 構成User Storyの総数 |
| storyCompletedCount | Integer | 必須 | 完了したUser Storyの数 |
| storyInProgressCount | Integer | 必須 | 進行中のUser Storyの数 |
| storyPendingCount | Integer | 必須 | 未着手のUser Storyの数 |
| valueRealizationStatus | String | 任意 | 価値の実現状況の概要 |
| valueRealizationScore | Double | 任意 | 価値の実現スコア（0.0〜1.0） |
| releaseReadinessStatus | String | 任意 | 提供準備状況の概要 |
| releaseReadinessScore | Double | 任意 | 提供準備スコア（0.0〜1.0） |
| dependencyCount | Integer | 必須 | 依存関係の数 |
| unresolvedDependencyCount | Integer | 必須 | 未解決の依存関係の数 |
| updatedAt | DateTime | 必須 | 最終更新日時 |

## 関連情報

| 情報ID | 情報名 | 関係 | 説明 |
|--------|--------|------|------|
| INF-BL-01 | バックログ項目 | 1対1 | 集計対象のFeature |
| INF-BL-06 | バックログ階層 | 1対多 | 構成User Storyの階層関係 |
| INF-BL-07 | 項目関連付け | 多対多 | Feature間の依存関係 |

## エンティティの3大要素

1. **単一性 (Oneness)**: 1つのFeatureの集計情報が1つのレコード
2. **同一性 (Sameness)**: featureIdで一意に識別
3. **カテゴリ (Category)**: Feature単位の集計情報
