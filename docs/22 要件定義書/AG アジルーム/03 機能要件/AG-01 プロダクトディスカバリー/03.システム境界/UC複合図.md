# UC複合図

プロダクトディスカバリー業務領域で扱うユースケース、画面、イベント、情報、条件を関連づけた図です。

## 1. 目的

本書は、AG-01 プロダクトディスカバリー
業務領域のシステム境界レイヤーで扱うユースケース、画面、イベント、情報、条件を関連づけたUC複合図です。

## 2. PlantUML UC複合図（英語版）

```plantuml
@startuml
title Product Discovery UC Composite Diagram
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
skinparam arrowColor #475569
skinparam shadowing false

actor "Product Owner" as PO
actor "Employee" as Emp
actor "System Admin" as Admin

rectangle "Frontend" {
  usecase "SCR-002 Product Vision Screen" as SCR002
  usecase "SCR-003 Stakeholder Screen" as SCR003
  usecase "SCR-004 Issue/Request Screen" as SCR004
  usecase "SCR-005 Persona Screen" as SCR005
  usecase "SCR-006 User Journey Screen" as SCR006
}

rectangle "Product Discovery Module" {
  usecase "UC-DIS-001 Create Vision" as UC001
  usecase "UC-DIS-002 Update Vision" as UC002
  usecase "UC-DIS-003 View Vision" as UC003
  usecase "UC-DIS-004 Create Goal" as UC004
  usecase "UC-DIS-005 Update Goal" as UC005
  usecase "UC-DIS-006 View Goal" as UC006
  usecase "UC-DIS-007 Register Issue" as UC007
  usecase "UC-DIS-008 Update Issue" as UC008
  usecase "UC-DIS-009 View Issue" as UC009
  usecase "UC-DIS-010 Create Hypothesis" as UC010
  usecase "UC-DIS-011 Update Hypothesis" as UC011
  usecase "UC-DIS-012 View Hypothesis" as UC012
  usecase "UC-DIS-013 Register Request" as UC013
  usecase "UC-DIS-014 Register Stakeholder" as UC014
  usecase "UC-DIS-015 Update Stakeholder" as UC015
  usecase "UC-DIS-016 View Stakeholder" as UC016
  usecase "UC-DIS-017 Create Persona" as UC017
  usecase "UC-DIS-018 Update Persona" as UC018
  usecase "UC-DIS-019 Create Journey" as UC019
  usecase "UC-DIS-020 Register Metric" as UC020
  usecase "UC-DIS-021 Update Metric" as UC021
  usecase "UC-DIS-022 Create Kano Question" as UC022
  usecase "UC-DIS-023 Evaluate Kano" as UC023
  usecase "UC-DIS-024 View Kano Result" as UC024
  usecase "UC-DIS-025 Create Experiment" as UC025
  usecase "UC-DIS-026 Record Result" as UC026
}

rectangle "System Layer" {
  card "INF-DIS-01 Product Vision" as INF01
  card "INF-DIS-02 Product Goal" as INF02
  card "INF-DIS-03 Success Metric" as INF03
  card "INF-DIS-04 Issue" as INF04
  card "INF-DIS-05 Hypothesis" as INF05
  card "INF-DIS-06 Experiment Plan" as INF06
  card "INF-DIS-07 Request" as INF07
  card "INF-DIS-08 Kano Evaluation" as INF08
  card "INF-DIS-09 Stakeholder" as INF09
  card "INF-DIS-10 Persona" as INF10
  card "INF-DIS-11 User Journey" as INF11
}

PO -- SCR002
PO -- SCR003
PO -- SCR004
PO -- SCR005
PO -- SCR006
Emp -- SCR002
Emp -- SCR003
Emp -- SCR004
Emp -- SCR005
Emp -- SCR006
Admin -- SCR002
Admin -- SCR003
Admin -- SCR004
Admin -- SCR005
Admin -- SCR006

SCR002 ..> UC001 : Call
SCR002 ..> UC002 : Call
SCR002 ..> UC003 : Call
SCR002 ..> UC004 : Call
SCR002 ..> UC005 : Call
SCR002 ..> UC006 : Call
SCR002 ..> UC010 : Call
SCR002 ..> UC011 : Call
SCR002 ..> UC012 : Call
SCR002 ..> UC020 : Call
SCR002 ..> UC021 : Call
SCR002 ..> UC025 : Call
SCR002 ..> UC026 : Call

SCR003 ..> UC014 : Call
SCR003 ..> UC015 : Call
SCR003 ..> UC016 : Call

SCR004 ..> UC007 : Call
SCR004 ..> UC008 : Call
SCR004 ..> UC009 : Call
SCR004 ..> UC013 : Call
SCR004 ..> UC022 : Call
SCR004 ..> UC023 : Call
SCR004 ..> UC024 : Call

SCR005 ..> UC017 : Call
SCR005 ..> UC018 : Call

SCR006 ..> UC019 : Call

UC001 -- INF01 : Use
UC002 -- INF01 : Use
UC003 -- INF01 : Use
UC004 -- INF02 : Use
UC005 -- INF02 : Use
UC006 -- INF02 : Use
UC007 -- INF04 : Use
UC008 -- INF04 : Use
UC009 -- INF04 : Use
UC010 -- INF05 : Use
UC011 -- INF05 : Use
UC012 -- INF05 : Use
UC013 -- INF07 : Use
UC014 -- INF09 : Use
UC015 -- INF09 : Use
UC016 -- INF09 : Use
UC017 -- INF10 : Use
UC018 -- INF10 : Use
UC019 -- INF11 : Use
UC020 -- INF03 : Use
UC021 -- INF03 : Use
UC022 -- INF08 : Use
UC023 -- INF08 : Use
UC024 -- INF08 : Use
UC025 -- INF06 : Use
UC026 -- INF06 : Use

@enduml
```

## 3. 日本語版 UC複合図

```plantuml
@startuml
title プロダクトディスカバリー UC複合図（日本語版）
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
skinparam arrowColor #475569
skinparam shadowing false

actor "プロダクトオーナー" as PO
actor "従業員" as Emp
actor "システム管理者" as Admin

rectangle "フロントエンド" {
  usecase "SCR-002 プロダクトビジョン画面" as SCR002
  usecase "SCR-003 ステークホルダー画面" as SCR003
  usecase "SCR-004 課題・要望画面" as SCR004
  usecase "SCR-005 ペルソナ画面" as SCR005
  usecase "SCR-006 ユーザージャーニー画面" as SCR006
}

rectangle "プロダクトディスカバリーモジュール" {
  usecase "UC-DIS-001 ビジョン作成" as UC001
  usecase "UC-DIS-002 ビジョン更新" as UC002
  usecase "UC-DIS-003 ビジョン参照" as UC003
  usecase "UC-DIS-004 ゴール作成" as UC004
  usecase "UC-DIS-005 ゴール更新" as UC005
  usecase "UC-DIS-006 ゴール参照" as UC006
  usecase "UC-DIS-007 課題登録" as UC007
  usecase "UC-DIS-008 課題更新" as UC008
  usecase "UC-DIS-009 課題参照" as UC009
  usecase "UC-DIS-010 仮説作成" as UC010
  usecase "UC-DIS-011 仮説更新" as UC011
  usecase "UC-DIS-012 仮説参照" as UC012
  usecase "UC-DIS-013 要望登録" as UC013
  usecase "UC-DIS-014 ステークホルダー登録" as UC014
  usecase "UC-DIS-015 ステークホルダー更新" as UC015
  usecase "UC-DIS-016 ステークホルダー参照" as UC016
  usecase "UC-DIS-017 ペルソナ作成" as UC017
  usecase "UC-DIS-018 ペルソナ更新" as UC018
  usecase "UC-DIS-019 ジャーニー作成" as UC019
  usecase "UC-DIS-020 指標登録" as UC020
  usecase "UC-DIS-021 指標更新" as UC021
  usecase "UC-DIS-022 Kano質問票作成" as UC022
  usecase "UC-DIS-023 Kano評価実施" as UC023
  usecase "UC-DIS-024 Kano結果参照" as UC024
  usecase "UC-DIS-025 実験計画作成" as UC025
  usecase "UC-DIS-026 実験結果記録" as UC026
}

rectangle "システムレイヤー" {
  card "INF-DIS-01 プロダクトビジョン" as INF01
  card "INF-DIS-02 プロダクトゴール" as INF02
  card "INF-DIS-03 成功指標" as INF03
  card "INF-DIS-04 課題" as INF04
  card "INF-DIS-05 仮説" as INF05
  card "INF-DIS-06 実験計画" as INF06
  card "INF-DIS-07 要望" as INF07
  card "INF-DIS-08 Kano評価" as INF08
  card "INF-DIS-09 ステークホルダー" as INF09
  card "INF-DIS-10 ペルソナ" as INF10
  card "INF-DIS-11 ユーザージャーニー" as INF11
}

PO -- SCR002
PO -- SCR003
PO -- SCR004
PO -- SCR005
PO -- SCR006
Emp -- SCR002
Emp -- SCR003
Emp -- SCR004
Emp -- SCR005
Emp -- SCR006
Admin -- SCR002
Admin -- SCR003
Admin -- SCR004
Admin -- SCR005
Admin -- SCR006

SCR002 ..> UC001 : 呼び出し
SCR002 ..> UC002 : 呼び出し
SCR002 ..> UC003 : 呼び出し
SCR002 ..> UC004 : 呼び出し
SCR002 ..> UC005 : 呼び出し
SCR002 ..> UC006 : 呼び出し
SCR002 ..> UC010 : 呼び出し
SCR002 ..> UC011 : 呼び出し
SCR002 ..> UC012 : 呼び出し
SCR002 ..> UC020 : 呼び出し
SCR002 ..> UC021 : 呼び出し
SCR002 ..> UC025 : 呼び出し
SCR002 ..> UC026 : 呼び出し

SCR003 ..> UC014 : 呼び出し
SCR003 ..> UC015 : 呼び出し
SCR003 ..> UC016 : 呼び出し

SCR004 ..> UC007 : 呼び出し
SCR004 ..> UC008 : 呼び出し
SCR004 ..> UC009 : 呼び出し
SCR004 ..> UC013 : 呼び出し
SCR004 ..> UC022 : 呼び出し
SCR004 ..> UC023 : 呼び出し
SCR004 ..> UC024 : 呼び出し

SCR005 ..> UC017 : 呼び出し
SCR005 ..> UC018 : 呼び出し

SCR006 ..> UC019 : 呼び出し

UC001 -- INF01 : 使用
UC002 -- INF01 : 使用
UC003 -- INF01 : 使用
UC004 -- INF02 : 使用
UC005 -- INF02 : 使用
UC006 -- INF02 : 使用
UC007 -- INF04 : 使用
UC008 -- INF04 : 使用
UC009 -- INF04 : 使用
UC010 -- INF05 : 使用
UC011 -- INF05 : 使用
UC012 -- INF05 : 使用
UC013 -- INF07 : 使用
UC014 -- INF09 : 使用
UC015 -- INF09 : 使用
UC016 -- INF09 : 使用
UC017 -- INF10 : 使用
UC018 -- INF10 : 使用
UC019 -- INF11 : 使用
UC020 -- INF03 : 使用
UC021 -- INF03 : 使用
UC022 -- INF08 : 使用
UC023 -- INF08 : 使用
UC024 -- INF08 : 使用
UC025 -- INF06 : 使用
UC026 -- INF06 : 使用

@enduml
```

## 4. ユースケース一覧

| ユースケースID | ユースケース名 | 英語名      | 説明 | 関連アクター |
|----------------|----------------|-------------|------|--------------|
| UC-DIS-001 | Product Visionを作成する | CreateVision | プロダクトビジョンを定義して登録する | 従業員 |
| UC-DIS-002 | Product Visionを更新する | UpdateVision | 既存のビジョンを修正・更新する | 従業員 |
| UC-DIS-003 | Product Visionを参照する | ViewVision | 現在のビジョンの内容を確認する | プロダクトオーナー、従業員、システム管理者 |
| UC-DIS-004 | Product Goalを作成する | CreateGoal | プロダクト目標を定義して登録する | 従業員 |
| UC-DIS-005 | Product Goalを更新する | UpdateGoal | 既存のゴールを修正・更新する | 従業員 |
| UC-DIS-006 | Product Goalを参照する | ViewGoal | 現在のゴールの内容を確認する | プロダクトオーナー、従業員、システム管理者 |
| UC-DIS-007 | 課題を登録する | RegisterIssue | 業務上の課題を記録する | プロダクトオーナー、従業員 |
| UC-DIS-008 | 課題を更新する | UpdateIssue | 既存の課題を修正する | プロダクトオーナー、従業員 |
| UC-DIS-009 | 課題を参照する | ViewIssue | 登録された課題を確認する | プロダクトオーナー、従業員、システム管理者 |
| UC-DIS-010 | 仮説を作成する | CreateHypothesis | 仮説と検証方法を定義する | 従業員 |
| UC-DIS-011 | 仮説を更新する | UpdateHypothesis | 仮説を修正・更新する | 従業員 |
| UC-DIS-012 | 仮説を参照する | ViewHypothesis | 登録された仮説を確認する | プロダクトオーナー、従業員、システム管理者 |
| UC-DIS-013 | 要望を登録する | RegisterRequest | 顧客・利用者からの要望を記録する | プロダクトオーナー、従業員 |
| UC-DIS-014 | ステークホルダーを登録する | RegisterStakeholder | 関係者情報を整理して登録する | 従業員 |
| UC-DIS-015 | ステークホルダーを更新する | UpdateStakeholder | 既存のステークホルダー情報を修正する | 従業員 |
| UC-DIS-016 | ステークホルダーを参照する | ViewStakeholder | 登録されたステークホルダーを確認する | プロダクトオーナー、従業員、システム管理者 |
| UC-DIS-017 | ペルソナを作成する | CreatePersona | ユーザー像を定義して登録する | 従業員 |
| UC-DIS-018 | ペルソナを更新する | UpdatePersona | 既存のペルソナ情報を修正する | 従業員 |
| UC-DIS-019 | ユーザージャーニーを作成する | CreateJourney | 利用者の行動を可視化する | 従業員 |
| UC-DIS-020 | 成功指標を登録する | RegisterMetric | 指標の定義を設定する | 従業員 |
| UC-DIS-021 | 成功指標を更新する | UpdateMetric | 既存の指標を修正する | 従業員 |
| UC-DIS-022 | Kano質問票を作成する | CreateKanoQuestion | Kano質問票を定義する | 従業員 |
| UC-DIS-023 | Kano評価を実施する | EvaluateKano | Kano評価を実施して分類する | プロダクトオーナー、従業員 |
| UC-DIS-024 | Kano評価結果を参照する | ViewKanoResult | Kano分類結果を確認する | プロダクトオーナー、従業員、システム管理者 |
| UC-DIS-025 | 実験計画を作成する | CreateExperiment | 実験計画を登録する | 従業員 |
| UC-DIS-026 | 実験結果を記録する | RecordResult | 実験結果を記録する | 従業員 |

## 5. 画面一覧

| 画面ID    | 画面名 | 英語名     | 説明 | 関連ユースケース |
|-----------|--------|------------|------|------------------|
| SCR-002 | プロダクトビジョン管理画面 | ProductVisionScreen | ビジョン・ゴール・仮説の管理 | UC-DIS-001〜006, 010〜012, 020〜021, 025〜026 |
| SCR-003 | ステークホルダー照会画面 | StakeholderScreen | ステークホルダーの参照 | UC-DIS-014〜016 |
| SCR-004 | 課題・要望一覧画面 | IssueRequestScreen | 課題・要望・Kano評価の参照 | UC-DIS-007〜009, 013, 022〜024 |
| SCR-005 | ペルソナ照会画面 | PersonaScreen | ペルソナの参照 | UC-DIS-017〜018 |
| SCR-006 | ユーザージャーニー照会画面 | UserJourneyScreen | ユーザージャーニーの参照 | UC-DIS-019 |

## 6. イベント一覧

外部システムとの連携イベントはありません。

| イベントID | イベント名 | 英語名    | 説明 | 関連ユースケース |
|------------|------------|-----------|------|------------------|
| - | - | - | - | - |

## 7. 情報一覧

| 情報ID    | 情報名 | 英語名          | 説明 | 関連ユースケース |
|-----------|--------|-----------------|------|------------------|
| INF-DIS-01 | プロダクトビジョン | ProductVision | プロダクトの対象者、課題、将来像 | UC-DIS-001〜003 |
| INF-DIS-02 | プロダクトゴール | ProductGoal | プロダクト目標、期間、状態 | UC-DIS-004〜006 |
| INF-DIS-03 | 成功指標 | SuccessMetric | 指標の定義、基準値、目標値 | UC-DIS-020〜021 |
| INF-DIS-04 | 課題 | Issue | 業務上の困りごと | UC-DIS-007〜009 |
| INF-DIS-05 | 仮説 | Hypothesis | 検証可能な仮説 | UC-DIS-010〜012 |
| INF-DIS-06 | 実験計画 | ExperimentPlan | 実験計画、結果、学び | UC-DIS-025〜026 |
| INF-DIS-07 | 要望 | Request | 顧客・利用者からの要望 | UC-DIS-013 |
| INF-DIS-08 | Kano評価 | KanoEvaluation | Kano質問票、回答、分類 | UC-DIS-022〜024 |
| INF-DIS-09 | ステークホルダー | Stakeholder | 関係者、関心事、影響範囲 | UC-DIS-014〜016 |
| INF-DIS-10 | ペルソナ | Persona | ユーザー像、ニーズ | UC-DIS-017〜018 |
| INF-DIS-11 | ユーザージャーニー | UserJourney | 行動、課題、感情、機会 | UC-DIS-019 |

## 8. 条件一覧

| 条件ID   | 条件名   | 英語名   | 説明 | 関連ユースケース |
|----------|----------|----------|------|------------------|
| RL-DIS-01 | 課題の粒度 | IssueGranularity | 課題は具体的で検証可能な粒度で定義する | UC-DIS-007 |
| RL-DIS-02 | ビジョンとゴールの整合性 | VisionGoalConsistency | ゴールはビジョンに整合している必要がある | UC-DIS-004 |
| RL-DIS-03 | 成功指標の測定可能性 | MetricMeasurability | 成功指標は測定可能な値で定義する | UC-DIS-020 |
| RL-DIS-04 | ゴール期間の明示 | GoalPeriodExplicit | ゴールには開始日と終了日を必ず設定する | UC-DIS-004 |
| RL-DIS-05 | 仮説の検証可能性 | HypothesisVerifiability | 仮説は検証可能な形で定義する | UC-DIS-010 |
| RL-DIS-06 | Kano回答の完全性 | KanoAnswerCompleteness | 充足質問と不充足質問の両方に回答する | UC-DIS-023 |
| RL-DIS-07 | Kano分類の解釈 | KanoClassificationInterpretation | 分類結果はKanoモデルの基準に従って解釈する | UC-DIS-023 |

## 9. 参照情報

- ユースケース一覧: `03.システム境界/01.ユースケース/ユースケース一覧.md`
- 画面一覧: `04.システム/04.画面/画面一覧.md`
- イベント一覧: `04.システム/03.イベント/イベント一覧.md`
- 情報一覧: `04.システム/01.情報/情報一覧.md`
