# EVT-BL-028 バックログ項目の状態が変更された_イベント属性一覧

要求・プロダクトバックログ管理業務領域のイベント属性を定義します。

| 属性名 | 型 | PK | 必須 | 説明 |
|--------|-----|----|------|------|
| backlogItemId | SnowflakeId | * | Y | バックログアイテムID |
| scrumTeamId | SnowflakeId | | Y | 所属するスクラムチームID |
| fromStatus | BacklogItemStatus | | Y | 遷移前の状態 |
| toStatus | BacklogItemStatus | | Y | 遷移後の状態 |
| actorEmployeeId | SnowflakeId | | Y | 状態を変更した従業員ID |
| occurredAt | LocalDateTime | | Y | 発生日時 |
