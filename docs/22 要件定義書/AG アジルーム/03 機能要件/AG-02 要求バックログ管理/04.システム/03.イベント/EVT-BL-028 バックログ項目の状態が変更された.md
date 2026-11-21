# EVT-BL-028 バックログ項目の状態が変更された

要求・プロダクトバックログ管理業務領域で発生するドメインイベントを定義します。

## 概要

| 項目             | 内容 |
|------------------|------|
| イベントID       | EVT-BL-028 |
| イベント名       | バックログ項目の状態が変更された |
| 説明             | バックログ項目の状態が遷移したことを通知するイベント |
| 関連ユースケース | UC-BL-032 |
| 関連情報         | INF-BL-11 |
| 集約             | バックログ項目 |

## イベント属性

| 属性名 | 型 | 説明 |
|--------|-----|------|
| backlogItemId | SnowflakeId | バックログアイテムID |
| scrumTeamId | SnowflakeId | 所属するスクラムチームID |
| fromStatus | BacklogItemStatus | 遷移前の状態 |
| toStatus | BacklogItemStatus | 遷移後の状態 |
| actorEmployeeId | SnowflakeId | 状態を変更した従業員ID |
| occurredAt | LocalDateTime | 発生日時 |

## 補足

- ロングタームイベントパターンにより、状態遷移の履歴はこのイベントで担保する
- Spring Modulith のイベント発行ログ（JDBC）に永続化されるため、専用の監査ログは持たない
- 他事業領域が状態を必要とする場合は、このイベントを受けて自領域のサブセットを保持する
- 実装は `BacklogItemStatusChanged` record として定義される
