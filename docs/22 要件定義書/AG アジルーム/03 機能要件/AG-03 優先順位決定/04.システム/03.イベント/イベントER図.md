# イベントER図

優先順位決定業務領域で発生するドメインイベントの関係を示すER図です。

## 1. 目的

本書は、AG-03 優先順位決定業務領域のシステムレイヤーで発生するドメインイベント間の関係を示すER図です。

## 2. PlantUML ER図

```plantuml
@startuml
title AG-03 優先順位決定 イベントER図
hide circle
skinparam linetype ortho
skinparam shadowing false
skinparam entity {
  BackgroundColor #F8FAFC
  BorderColor #475569
  FontColor #0F172A
}
skinparam packageStyle rectangle

entity "優先順位イベント" as PriorityEvents {
  * event_id : String <<PK>>
  --
  EVT-PR-001 : 優先順位が設定された
  EVT-PR-002 : 優先順位が更新された
  EVT-PR-006 : 価値・リスク・コストが比較された
  EVT-PR-007 : Kano分類が優先順位に反映された
}

entity "優先判断理由イベント" as PriorityReasonEvents {
  * event_id : String <<PK>>
  --
  EVT-PR-003 : 判断理由が記録された
}

entity "Kano質問票イベント" as KanoQuestionEvents {
  * event_id : String <<PK>>
  --
  EVT-PR-004 : Kano質問票が作成された
}

entity "Kano分類結果イベント" as KanoResultEvents {
  * event_id : String <<PK>>
  --
  EVT-PR-005 : Kano評価が完了した
}

' Kano質問票からKano分類結果へ
KanoQuestionEvents ||--o{ KanoResultEvents : 評価

' Kano分類結果から優先順位へ
KanoResultEvents ..> PriorityEvents : 反映

' 優先順位から優先判断理由へ
PriorityEvents ||--o{ PriorityReasonEvents : 記録

@enduml
```

## 3. 日本語版 ER図

```plantuml
@startuml
title AG-03 優先順位決定 イベントER図（日本語版）
hide circle
skinparam linetype ortho
skinparam shadowing false
skinparam entity {
  BackgroundColor #F8FAFC
  BorderColor #475569
  FontColor #0F172A
}
skinparam packageStyle rectangle

entity "優先順位イベント" as PriorityEvents {
  * イベントID : String <<PK>>
  --
  EVT-PR-001 : 優先順位が設定された
  EVT-PR-002 : 優先順位が更新された
  EVT-PR-006 : 価値・リスク・コストが比較された
  EVT-PR-007 : Kano分類が優先順位に反映された
}

entity "優先判断理由イベント" as PriorityReasonEvents {
  * イベントID : String <<PK>>
  --
  EVT-PR-003 : 判断理由が記録された
}

entity "Kano質問票イベント" as KanoQuestionEvents {
  * イベントID : String <<PK>>
  --
  EVT-PR-004 : Kano質問票が作成された
}

entity "Kano分類結果イベント" as KanoResultEvents {
  * イベントID : String <<PK>>
  --
  EVT-PR-005 : Kano評価が完了した
}

' Kano質問票からKano分類結果へ
Kano質問票イベント ||--o{ Kano分類結果イベント : 評価

' Kano分類結果から優先順位へ
Kano分類結果イベント ..> 優先順位イベント : 反映

' 優先順位から優先判断理由へ
優先順位イベント ||--o{ 優先判断理由イベント : 記録

@enduml
```

## 4. イベントエンティティ一覧

| イベントエンティティ     | イベントID  | イベント名                             | 関連情報   | 集約               |
|--------------------------|-------------|----------------------------------------|------------|--------------------|
| PriorityEvents           | EVT-PR-001  | 優先順位が設定された                   | INF-PR-01  | 優先順位           |
| PriorityEvents           | EVT-PR-002  | 優先順位が更新された                   | INF-PR-01  | 優先順位           |
| PriorityEvents           | EVT-PR-006  | 価値・リスク・コストが比較された       | INF-PR-01  | 優先順位           |
| PriorityEvents           | EVT-PR-007  | Kano分類が優先順位に反映された         | INF-PR-04  | 優先順位           |
| PriorityReasonEvents     | EVT-PR-003  | 判断理由が記録された                   | INF-PR-05  | 優先判断理由       |
| KanoQuestionEvents       | EVT-PR-004  | Kano質問票が作成された                 | INF-PR-02  | Kano質問票         |
| KanoResultEvents         | EVT-PR-005  | Kano評価が完了した                     | INF-PR-04  | Kano分類結果       |

## 5. 主要な関係

- **Kano質問票イベント → Kano分類結果イベント**：質問票に基づく評価
- **Kano分類結果イベント → 優先順位イベント**：Kano分類の優先順位への反映
- **優先順位イベント → 優先判断理由イベント**：優先順位決定の理由記録
