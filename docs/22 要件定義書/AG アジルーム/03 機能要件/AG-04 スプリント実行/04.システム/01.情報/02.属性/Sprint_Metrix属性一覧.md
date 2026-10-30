# INF-IDB-04 Sprint Metrix 情報属性一覧

## 情報概要

| 項目 | 値 |
|------|-----|
| 情報ID | INF-IDB-04 |
| 情報名 | Sprint Metrix |
| 情報種類 | リソース |
| 説明 | ダッシュボード用集計メトリクスを日次で記録する |
| 履歴管理要 | N |
| 証跡要 | N |
| 実現方法 | DB |

## 個体指定子（主キー）

| 属性名 | 型 | 必須/任意 | 説明 |
|--------|----|-----------|------|
| id | Long | 必須 | メトリクスの一意識別子 |

## 属性

| 属性名 | 型 | 必須/任意 | 説明 |
|--------|----|-----------|------|
| sprintId | Long | 必須 | 関連するSprintのID |
| recordedDate | Date | 必須 | 記録日 |
| completedStoryPoints | Integer | 必須 | 完了したStory Points累計 |
| remainingStoryPoints | Integer | 必須 | 残りのStory Points |
| completedTasks | Integer | 必須 | 完了したTask数 |
| remainingTasks | Integer | 必須 | 残りのTask数 |
| remainingHours | Double | 必須 | 残作業時間合計 |
| totalStoryPoints | Integer | 必須 | Sprint全体のStory Points |
| velocity | Double | 任意 | 日次ベロシティ |
| blockerCount | Integer | 必須 | 未解決ブロッカー数 |
| scopeChangeCount | Integer | 必須 | スコープ変更回数 |

## 関連情報

| 情報ID | 情報名 | 関係 | 説明 |
|--------|--------|------|------|
| INF-SP-01 | スプリント | 1:N | 1つのSprintに複数の日次メトリクス |

## エンティティの3大要素

1. **単一性 (Oneness)**: 1つのSprintの1日分の集計メトリクス
2. **同一性 (Sameness)**: id で一意に識別される (sprintId + recordedDate で一意)
3. **カテゴリ (Category)**: ダッシュボード用集計データ
