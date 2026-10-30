# INF-BL-23 Feature品質情報_情報属性一覧

## 概要

| 項目 | 内容 |
|------|------|
| 情報ID | INF-BL-23 |
| 情報名 | Feature品質情報 |
| 情報種類 | リソース |
| 履歴管理要 | Y |
| 証跡要 | N |
| 実現方法 | DB |
| 説明 | Featureのテスト結果、欠陥数、品質指標 |

## 個体指定子（主キー）

| 属性名 | 型 | 必須 | 説明 |
|--------|----|------|------|
| featureId | Long | 必須 | 品質情報の対象FeatureのID（INF-BL-01の外部キー） |

## 属性

| 属性名 | 型 | 必須 | 説明 |
|--------|----|------|------|
| testCaseCount | Integer | 必須 | テストケース総数 |
| passedCaseCount | Integer | 必須 | 合格したテストケース数 |
| failedCaseCount | Integer | 必須 | 不合格したテストケース数 |
| pendingCaseCount | Integer | 必須 | 未実行のテストケース数 |
| defectCount | Integer | 必須 | 欠陥総数 |
| criticalDefectCount | Integer | 必須 | 重大な欠陥数 |
| highDefectCount | Integer | 必須 | 高優先度の欠陥数 |
| mediumDefectCount | Integer | 必須 | 中優先度の欠陥数 |
| lowDefectCount | Integer | 必須 | 低優先度の欠陥数 |
| qualityScore | Double | 任意 | 品質スコア（0.0〜1.0） |
| updatedAt | DateTime | 必須 | 最終更新日時 |

## 関連情報

| 情報ID | 情報名 | 関係 | 説明 |
|--------|--------|------|------|
| INF-BL-01 | バックログ項目 | 1対1 | 品質情報の対象Feature |
| INF-BL-08 | 項目とテスト | 1対多 | Featureに関連するテストケース |

## エンティティの3大要素

1. **単一性 (Oneness)**: 1つのFeatureの品質情報が1つのレコード
2. **同一性 (Sameness)**: featureIdで一意に識別
3. **カテゴリ (Category)**: Featureの品質情報
