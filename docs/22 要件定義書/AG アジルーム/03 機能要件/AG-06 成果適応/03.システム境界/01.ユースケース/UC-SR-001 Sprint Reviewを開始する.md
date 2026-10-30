# UC-SR-01 Sprint Reviewを開始する

ユースケースの詳細を定義します。

## 概要

| 項目 | 内容 |
|------|------|
| ユースケースID | UC-SR-01 |
| ユースケース名 | Sprint Reviewを開始する |
| 英語名 | Start Sprint Review |
| 説明 | 対象Sprintを選択し、Sprint Reviewの準備状態を確認して開始する |
| 関連アクター | ACT-02（プロダクトオーナー）、ACT-03（スクラムマスター） |
| 優先度 | 高 |
| ビジネスユースケース | BUC-LR-01 |
| 外部システム | - |
| 関連画面 | SR-01（Sprint Review一覧） |

## 前置条件

- ユーザーがシステムにログインしていること
- 対象Sprintが存在すること
- Sprintが終了していること

## 基本フロー

1. ユーザーが「Sprint Review一覧」画面（SR-01）にアクセスする
2. 対象Sprintを選択する
3. Sprint GoalとSprint Backlogの計画内容を確認する
4. 完了・未完了Storyを確認する
5. 「Sprint Reviewを開始」ボタンをクリックする
6. システムがSprint Review（INF-SR-01）を作成し、状態を「準備中」に変更する
7. Sprint Review準備画面（SR-02）に遷移する

## 代替フロー

- **2a. 既にSprint Reviewが開始されている場合**
  - システムが現在のSprint Reviewの状態を表示する
  - ユーザーは既存のSprint Reviewを継続するか確認する

- **4a. 未完了Storyが存在する場合**
  - システムが未完了Storyとその理由を表示する
  - ユーザーは残作業を確認する

## 後置条件

- Sprint Review（INF-SR-01）が作成される
- Sprint Reviewの状態が「準備中」になる
- イベント「EVT-SR-001 Sprint Reviewが開始された」が発行される

## 関連情報

| 情報ID | 情報名 | 使用目的 |
|--------|--------|----------|
| INF-SR-01 | Sprint Review | 作成対象の情報 |
| INF-LR-01 | Sprint Review記録 | 関連する記録 |

## 関連イベント

| イベントID | イベント名 | 発生タイミング |
|------------|------------|----------------|
| EVT-SR-001 | Sprint Reviewが開始された | 基本フローステップ6完了後 |

## 制約事項

| 番号 | 制約事項 | 説明 |
|------|----------|------|
| 1 | 対象Sprint | 終了したSprintに対してのみSprint Reviewを開始できる |
| 2 | 重複防止 | 既にSprint Reviewが存在するSprintに対しては重複作成できない |
| 3 | 権限 | POまたはSMのみがSprint Reviewを開始できる |
