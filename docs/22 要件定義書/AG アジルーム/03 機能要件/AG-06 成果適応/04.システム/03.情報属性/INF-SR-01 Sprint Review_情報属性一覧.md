# INF-SR-01 Sprint Review 情報属性一覧

Sprint Reviewの属性をリストアップします。

## 情報概要

| 項目 | 内容 |
|------|------|
| 情報ID | INF-SR-01 |
| 情報名 | Sprint Review |
| 情報種類 | リソース |
| 説明 | Reviewの対象Sprint、開催日時、状態、Sprint Goal達成状況 |

## 属性一覧

TM（T字形ER）モデルに基づき、個体指定子と属性を定義します。

| 番号 | 属性名 | 個体指定子 | 型 | 必須 | 説明 |
|------|--------|------------|----|------|------|
| 1 | ID | ✓ | Long | 必須 | Sprint Reviewの一意な識別子 |
| 2 | 対象Sprint | | String | 必須 | 対象となるSprintの識別子 |
| 3 | 開催日時 | | DateTime | 必須 | Sprint Reviewの開催日時 |
| 4 | 状態 | | Enum | 必須 | Sprint Reviewの状態（INF-SR-11参照） |
| 5 | Sprint Goal | | String | 任意 | Sprintの目標 |
| 6 | 達成状況 | | String | 任意 | Sprint Goalの達成状況 |
| 7 | 作成日時 | | DateTime | 必須 | 作成日時 |
| 8 | 更新日時 | | DateTime | 任意 | 最終更新日時 |

## 属性の型

属性の型は以下から選択します：

- **String**: 文字列
- **Integer**: 整数
- **Long**: 長整数
- **Double**: 浮動小数点
- **Boolean**: 真偽値
- **Date**: 日付
- **DateTime**: 日時
- **Enum**: 列挙型（対照表を参照）

## 単一性・同一性・カテゴリ

エンティティの3大要素について定義します：

| 要素 | 定義 |
|------|------|
| 単一性 (Oneness) | 1つのSprintに対するSprint Reviewが1つ |
| 同一性 (Sameness) | 対象Sprintが同じ場合、同じSprint Reviewとみなす |
| カテゴリ (Category) | Sprint Reviewを管理するビジネスエンティティ |

## 状態遷移

この情報が持つ状態と遷移を定義します（該当する場合）。

| 状態名 | 説明 | 遷移元 | 遷移先 | トリガーとなるユースケース |
|--------|------|--------|--------|--------------------------|
| 未準備 | Sprint Reviewがまだ準備されていない状態 | - | 準備中 | UC-SR-001 |
| 準備中 | Sprint Reviewの準備中の状態 | 未準備 | 準備完了 | UC-SR-002 |
| 準備完了 | Sprint Reviewの準備が完了した状態 | 準備中 | 開催中 | UC-SR-005 |
| 開催中 | Sprint Reviewが開催中の状態 | 準備完了 | 完了 | UC-SR-013 |
| 完了 | Sprint Reviewが完了した状態 | 開催中 | - | UC-SR-013 |

## 関連情報

この情報と関連する他の情報をリストアップします。

| 関連情報ID | 関連情報名 | 関係の種類 | 説明 |
|------------|------------|------------|------|
| INF-SR-02 | Review参加者 | 1対多 | Sprint Reviewは複数の参加者を持つ |
| INF-SR-03 | Review議題 | 1対多 | Sprint Reviewは複数の議題を持つ |
| INF-SR-04 | デモ対象インクリメント | 1対多 | Sprint Reviewは複数のデモ対象を持つ |
| INF-SR-05 | Reviewフィードバック | 1対多 | Sprint Reviewは複数のフィードバックを持つ |
| INF-SR-08 | Review決定事項 | 1対多 | Sprint Reviewは複数の決定事項を持つ |
| INF-SR-11 | Sprint Review状態 | 多対1 | 状態の対照表を参照 |
