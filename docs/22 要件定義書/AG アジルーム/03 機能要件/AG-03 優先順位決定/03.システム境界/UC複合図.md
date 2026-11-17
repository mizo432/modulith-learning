# UC複合図

優先順位決定業務領域で扱うユースケース、画面、イベント、情報などを関連づけたUC複合図です。

## 1. 目的

本書は、AG-03 優先順位決定業務領域のシステム境界レイヤーで扱うユースケース、画面、イベント、情報、条件を関連づけたUC複合図です。

## 2. PlantUML UC複合図（英語版）

```plantuml
@startuml
title Priority Decision UC Composite Diagram
left to right direction

skinparam usecase {
  BackgroundColor #F8FAFC
  BorderColor #475569
  FontColor #0F172A
}
skinparam actor {
  BackgroundColor #F8FAFC
  BorderColor #475569
  FontColor #0F172A
}
skinparam rectangle {
  BackgroundColor #F8FAFC
  BorderColor #475569
  FontColor #0F172A
}
skinparam card {
  BackgroundColor #F8FAFC
  BorderColor #475569
  FontColor #0F172A
}
skinparam arrowColor #475569
skinparam shadowing false

actor "Employee (ACT-01)" as ACT01
actor "Product Owner (ACT-02)" as ACT02
actor "Tracking Officer (ACT-05)" as ACT05

rectangle "Frontend" {
  usecase "SCR-012 Acceptance Criteria & Test" as SCR012
}

rectangle "Priority Decision Module" {
  usecase "UC-PR-001 Set Priority" as UC001
  usecase "UC-PR-002 Update Priority" as UC002
  usecase "UC-PR-003 View Priority" as UC003
  usecase "UC-PR-004 Record Decision Reason" as UC004
  usecase "UC-PR-005 View Decision Reason" as UC005
  usecase "UC-PR-006 Create Kano Questionnaire" as UC006
  usecase "UC-PR-007 Conduct Kano Evaluation" as UC007
  usecase "UC-PR-008 View Kano Result" as UC008
  usecase "UC-PR-009 Compare Value, Risk & Cost" as UC009
  usecase "UC-PR-010 Evaluate Priority with Kano" as UC010
}

rectangle "System Layer" {
  card "INF-PR-01\nPriority" as INF01
  card "INF-PR-02\nKano Questionnaire" as INF02
  card "INF-PR-03\nKano Answer" as INF03
  card "INF-PR-04\nKano Classification Result" as INF04
  card "INF-PR-05\nPriority Decision Reason" as INF05
  card "INF-PR-06\nKano Category" as INF06
  card "INF-PR-07\nAnswer Options" as INF07
}

ACT02 -- SCR012
ACT01 -- SCR012
ACT05 -- SCR012

SCR012 ..> UC001 : Call
SCR012 ..> UC002 : Call
SCR012 ..> UC003 : Call
SCR012 ..> UC004 : Call
SCR012 ..> UC005 : Call
SCR012 ..> UC006 : Call
SCR012 ..> UC007 : Call
SCR012 ..> UC008 : Call
SCR012 ..> UC009 : Call
SCR012 ..> UC010 : Call

UC001 -- INF01 : Use
UC002 -- INF01 : Use
UC003 -- INF01 : Use
UC004 -- INF05 : Use
UC005 -- INF05 : Use
UC006 -- INF02 : Use
UC007 -- INF04 : Use
UC007 -- INF03 : Use
UC008 -- INF04 : Use
UC009 -- INF01 : Use
UC010 -- INF01 : Use
UC010 -- INF04 : Use

UC007 -- INF06 : Use
UC007 -- INF07 : Use
UC008 -- INF06 : Use
@enduml
```

## 3. 日本語版 UC複合図

```plantuml
@startuml
title 優先順位決定 UC複合図（日本語版）
left to right direction

skinparam usecase {
  BackgroundColor #F8FAFC
  BorderColor #475569
  FontColor #0F172A
}
skinparam actor {
  BackgroundColor #F8FAFC
  BorderColor #475569
  FontColor #0F172A
}
skinparam rectangle {
  BackgroundColor #F8FAFC
  BorderColor #475569
  FontColor #0F172A
}
skinparam card {
  BackgroundColor #F8FAFC
  BorderColor #475569
  FontColor #0F172A
}
skinparam arrowColor #475569
skinparam shadowing false

actor "従業員 (ACT-01)" as ACT01
actor "プロダクトオーナー (ACT-02)" as ACT02
actor "トラッキング担当 (ACT-05)" as ACT05

rectangle "フロントエンド" {
  usecase "SCR-012 受入条件・受入テスト" as SCR012
}

rectangle "優先順位決定モジュール" {
  usecase "UC-PR-001 優先順位を設定する" as UC001
  usecase "UC-PR-002 優先順位を更新する" as UC002
  usecase "UC-PR-003 優先順位を参照する" as UC003
  usecase "UC-PR-004 判断理由を記録する" as UC004
  usecase "UC-PR-005 判断理由を参照する" as UC005
  usecase "UC-PR-006 Kano質問票を作成する" as UC006
  usecase "UC-PR-007 Kano評価を実施する" as UC007
  usecase "UC-PR-008 Kano評価結果を参照する" as UC008
  usecase "UC-PR-009 価値・リスク・コストを比較する" as UC009
  usecase "UC-PR-010 Kano分類を用いて優先順位を検討する" as UC010
}

rectangle "システムレイヤー" {
  card "INF-PR-01\n優先順位" as INF01
  card "INF-PR-02\nKano質問票" as INF02
  card "INF-PR-03\nKano回答" as INF03
  card "INF-PR-04\nKano分類結果" as INF04
  card "INF-PR-05\n優先判断理由" as INF05
  card "INF-PR-06\nKanoカテゴリ" as INF06
  card "INF-PR-07\n回答選択肢" as INF07
}

ACT02 -- SCR012
ACT01 -- SCR012
ACT05 -- SCR012

SCR012 ..> UC001 : 呼び出し
SCR012 ..> UC002 : 呼び出し
SCR012 ..> UC003 : 呼び出し
SCR012 ..> UC004 : 呼び出し
SCR012 ..> UC005 : 呼び出し
SCR012 ..> UC006 : 呼び出し
SCR012 ..> UC007 : 呼び出し
SCR012 ..> UC008 : 呼び出し
SCR012 ..> UC009 : 呼び出し
SCR012 ..> UC010 : 呼び出し

UC001 -- INF01 : 使用
UC002 -- INF01 : 使用
UC003 -- INF01 : 使用
UC004 -- INF05 : 使用
UC005 -- INF05 : 使用
UC006 -- INF02 : 使用
UC007 -- INF04 : 使用
UC007 -- INF03 : 使用
UC008 -- INF04 : 使用
UC009 -- INF01 : 使用
UC010 -- INF01 : 使用
UC010 -- INF04 : 使用

UC007 -- INF06 : 使用
UC007 -- INF07 : 使用
UC008 -- INF06 : 使用
@enduml
```

## 4. ユースケース一覧

| ユースケースID | ユースケース名                     | 英語名                              | 説明                                                                   | 関連アクター               |
|----------------|------------------------------------|-------------------------------------|------------------------------------------------------------------------|----------------------------|
| UC-PR-001      | 優先順位を設定する                 | Set Priority                        | 価値・リスク・コストを踏まえてバックログ項目の順序を決める             | ACT-02                     |
| UC-PR-002      | 優先順位を更新する                 | Update Priority                     | 変更された状況に応じて項目の順序を再調整する                           | ACT-02                     |
| UC-PR-003      | 優先順位を参照する                 | View Priority                       | 現在の優先順位と変更履歴を確認する                                     | ACT-02, ACT-05             |
| UC-PR-004      | 判断理由を記録する                 | Record Decision Reason              | 優先順位決定の根拠・決定者・影響範囲を記録する                         | ACT-02                     |
| UC-PR-005      | 判断理由を参照する                 | View Decision Reason                | 優先順位決定の背景を確認する                                           | ACT-02, ACT-05             |
| UC-PR-006      | Kano質問票を作成する               | Create Kano Questionnaire           | 各要望について機能がある場合・ない場合の反応を対で定義する             | ACT-02                     |
| UC-PR-007      | Kano評価を実施する                 | Conduct Kano Evaluation             | Kano質問票に基づいて回答を収集し、品質カテゴリに分類する               | ACT-01, ACT-02             |
| UC-PR-008      | Kano評価結果を参照する             | View Kano Evaluation Result         | 要望のKano分類結果を確認する                                           | ACT-01, ACT-02, ACT-05     |
| UC-PR-009      | 価値・リスク・コストを比較する     | Compare Value, Risk & Cost          | 要求の価値、リスク、コスト、依存関係を比較する                         | ACT-02                     |
| UC-PR-010      | Kano分類を用いて優先順位を検討する | Evaluate Priority with Kano         | Kano分類を他の価値・コスト・リスク情報と合わせて優先順位判断に活用する | ACT-02                     |

## 5. 画面一覧

| 画面ID  | 画面名               | 英語名                     | 主な利用者             | 関連ユースケース |
|---------|----------------------|----------------------------|------------------------|------------------|
| SCR-012 | 受入条件・受入テスト | Acceptance Criteria & Test | PO、テスト担当、開発者 | UC-PR-001〜010   |

## 6. イベント一覧

| イベントID | イベント名                       | 英語名                           | 説明                                                                     | 関連ユースケース |
|------------|----------------------------------|----------------------------------|--------------------------------------------------------------------------|------------------|
| EVT-PR-001 | 優先順位が設定された             | Priority Set                     | 価値・リスク・コストを踏まえてバックログ項目の順序が決まった             | UC-PR-001        |
| EVT-PR-002 | 優先順位が更新された             | Priority Updated                 | 変更された状況に応じて項目の順序が再調整された                           | UC-PR-002        |
| EVT-PR-003 | 判断理由が記録された             | Decision Reason Recorded         | 優先順位決定の根拠・決定者・影響範囲が記録された                         | UC-PR-004        |
| EVT-PR-004 | Kano質問票が作成された           | Kano Questionnaire Created       | 各要望について機能がある場合・ない場合の反応が対で定義された             | UC-PR-006        |
| EVT-PR-005 | Kano評価が完了した               | Kano Evaluation Completed        | Kano質問票に基づいて回答が収集され、品質カテゴリに分類された             | UC-PR-007        |
| EVT-PR-006 | 価値・リスク・コストが比較された | Value, Risk & Cost Compared      | 要求の価値、リスク、コスト、依存関係が比較された                         | UC-PR-009        |
| EVT-PR-007 | Kano分類が優先順位に反映された   | Kano Classification Reflected    | Kano分類を他の価値・コスト・リスク情報と合わせて優先順位判断に活用された | UC-PR-010        |

## 7. 情報一覧

| 情報ID    | 情報名       | 英語名                  | 情報種類 | 説明                                                   | 関連ユースケース |
|-----------|--------------|-------------------------|----------|--------------------------------------------------------|------------------|
| INF-PR-01 | 優先順位     | Priority                | リソース | バックログ項目の順序と優先理由                         | UC-PR-001〜003, 009〜010 |
| INF-PR-02 | Kano質問票   | Kano Questionnaire      | リソース | 充足質問・不充足質問のペア                             | UC-PR-006        |
| INF-PR-03 | Kano回答     | Kano Answer             | リソース | 各質問に対する回答                                     | UC-PR-007        |
| INF-PR-04 | Kano分類結果 | Kano Classification Result | リソース | 回答から導出された品質カテゴリ                         | UC-PR-007〜008, 010 |
| INF-PR-05 | 優先判断理由 | Priority Decision Reason | リソース | 優先順位決定の根拠・決定者・影響範囲                   | UC-PR-004〜005   |
| INF-PR-06 | Kanoカテゴリ | Kano Category           | 対照表   | 当たり前品質、一元品質、魅力的品質、無関心品質、逆品質 | UC-PR-007〜008   |
| INF-PR-07 | 回答選択肢   | Answer Options          | 対照表   | 満足、当たり前、無関心、仕方ない、不満                 | UC-PR-007        |

## 8. 参照情報

- ユースケース一覧: `01.ユースケース/ユースケース一覧.md`
- 画面一覧: `04.システム/04.画面/画面一覧.md`
- イベント一覧: `04.システム/03.イベント/イベント一覧.md`
- 情報一覧: `04.システム/01.情報/情報一覧.md`
