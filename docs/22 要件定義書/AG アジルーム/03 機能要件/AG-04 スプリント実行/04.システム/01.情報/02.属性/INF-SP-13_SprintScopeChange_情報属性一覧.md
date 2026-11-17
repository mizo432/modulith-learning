# INF-SP-13 Sprint Scope Change_情報属性一覧

Sprint Scope Changeの属性をリストアップします。

## 情報概要

| 項目 | 内容 |
|------|------|
| 情報ID | INF-SP-13 |
| 情報名 | Sprint Scope Change |
| 情報種類 | イベント |
| 説明 | Sprint範囲の変更履歴（Task単位） |

## 属性一覧

TM（T字形ER）モデルに基づき、個体指定子と属性を定義します。

| 番号 | 属性名 | 個体指定子 | 型 | 必須 | 説明 |
|------|--------|------------|----|------|------|
| 1 | scope_change_id | ✓ | String | 必須 | 変更履歴の一意な識別子（UUID） |
| 2 | sprint_id | | String | 必須 | 対象Sprintの識別子（FK） |
| 3 | task_id | | String | 必須 | 変更されたTaskの識別子（FK） |
| 4 | change_type | | Enum | 必須 | 変更の種類（追加、削除、更新） |
| 5 | changed_by | | String | 必須 | 変更者の識別子（FK） |
| 6 | changed_at | | DateTime | 必須 | 変更日時 |
| 7 | reason | | String | 必須 | 変更理由 |
| 8 | before_state | | String | 任意 | 変更前の状態 |
| 9 | after_state | | String | 任意 | 変更後の状態 |

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
| 単一性 (Oneness) | 1つのSprint Scope Changeは、1回のSprint範囲の変更記録である |
| 同一性 (Sameness) | scope_change_idが同じであれば、同じ変更記録である |
| カテゴリ (Category) | change_typeで分類（追加、削除、更新） |

## 関連情報

この情報と関連する他の情報をリストアップします。

| 関連情報ID | 関連情報名 | 関係の種類 | 説明 |
|------------|------------|------------|------|
| INF-SP-01 | スプリント | 多対1 | 複数のScope Changeは1つのSprintに属する |
| INF-SP-10 | タスク | 多対1 | 複数のScope Changeは1つのTaskを対象とする |
