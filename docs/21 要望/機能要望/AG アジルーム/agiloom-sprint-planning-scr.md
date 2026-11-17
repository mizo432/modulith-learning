# Agiloom：スプリントプランニング画面の要件

Agiloomのスプリントプランニング画面は、プロダクトバックログからSprintで取り組む項目を選び、チームでSprint
Goalを設定し、チームのキャパシティを踏まえて実行計画を作成するための画面です。

単なるタスク割り当て画面ではなく、チームがSprint Goalを達成できる計画を協働で作り、計画の根拠と変更履歴を残す画面として設計します。

## 1. 画面の目的

- Sprintの期間、チームの稼働状況、過去の実績を確認する。
- Product BacklogからSprint Backlogに含めるユーザーストーリーを選択する。
- Sprint Goalをチームで設定する。
- ストーリーの見積もりとキャパシティを比較する。
- 選択したストーリーをタスクに分解する。
- 計画上のリスクや依存関係を確認する。
- 計画内容を確定し、確定時点のスナップショットを保存する。

## 2. 画面レイアウト案

## Sprint Planning

Sprint 12 · 2026/11/02〜11/13

計画中

### ① Sprint Goal

Sprintで達成する価値・成果を記述します。

利用者が申請状況をオンラインで確認できるようにする

チームキャパシティ

# 80 人日

休暇・会議等を考慮した値

選択済みStory

# 5 件

見積もり合計：32ポイント

### ② Product Backlog（選択候補）

優先順位、受入条件、見積もり、依存関係を確認して追加します。

US-101 申請状況を一覧表示する

優先度：高 · 5 pt

追加要件

US-102 申請の詳細を確認する

優先度：高 · 3 pt

追加要件

US-103 申請状況を絞り込む

優先度：中 · 5 pt

追加要件

### ③ Sprint Backlog

選択したStoryと、実行に必要なTaskを管理します。

US-101 申請状況を一覧表示する

API実装

2日

画面実装

3日

テスト

1日

### ④ リスク・確認事項

依存Story、未確定の受入条件、環境準備、チームの稼働不足などを表示します。

US-102はUS-101のAPI仕様に依存しています。

下書き保存の要件計画確定の要件

上記は画面要件を検討するためのワイヤーフレーム例です。数値やストーリーは表示イメージ用の仮データです。

## 3. 画面要件一覧

| ID    | 要件                                                                   | 優先度 |
|-------|------------------------------------------------------------------------|--------|
| SP-01 | 対象Sprintの期間、状態、チームを表示する                               | 必須   |
| SP-02 | Sprint Goalを入力・編集できる                                          | 必須   |
| SP-03 | Product Backlogを検索・絞り込みできる                                  | 必須   |
| SP-04 | Storyの優先順位、見積もり、受入条件、依存関係を確認できる              | 必須   |
| SP-05 | StoryをSprint Backlogに追加・除外できる                                | 必須   |
| SP-06 | 選択したStoryの見積もり合計を表示する                                  | 必須   |
| SP-07 | メンバーの稼働日数、休暇、他業務を踏まえたチームキャパシティを表示する | 必須   |
| SP-08 | 過去のSprintのベロシティや完了実績を参照できる                         | 必須   |
| SP-09 | StoryをTaskに分解し、Taskの作業量を記録できる                          | 必須   |
| SP-10 | Taskの担当者を設定できる                                               | 推奨   |
| SP-11 | 依存関係、未確定事項、リスクを表示できる                               | 必須   |
| SP-12 | 計画を下書き保存・再編集できる                                         | 必須   |
| SP-13 | 計画確定時に計画内容のスナップショットを保存する                       | 必須   |
| SP-14 | 計画確定後の変更履歴を記録する                                         | 必須   |
| SP-15 | 計画内容をチームメンバーが共有・確認できる                             | 必須   |

## 4. 画面項目

| 項目               | 内容                            | 入力・表示             |
|--------------------|---------------------------------|------------------------|
| Sprint             | 対象Sprint                      | 選択・表示             |
| 開始日・終了日     | Sprintの期間                    | 表示                   |
| Sprint Goal        | Sprintで達成する目標            | 入力                   |
| チーム             | 対象開発チーム                  | 表示                   |
| チームキャパシティ | 稼働可能な作業量                | 表示・必要に応じて調整 |
| 過去のベロシティ   | 直近Sprintの完了実績            | 表示                   |
| Backlog候補        | 選択可能なStory                 | 検索・表示             |
| 選択済みStory      | Sprint Backlogに含めるStory     | 追加・除外             |
| Story見積もり      | ストーリーの相対的な規模        | 表示                   |
| 受入条件           | Storyの完了・受入判断に使う条件 | 表示・編集画面へ遷移   |
| 依存関係           | 他Storyや外部作業との依存       | 表示                   |
| Task               | Storyを実現する作業項目         | 追加・編集             |
| Task作業量         | Taskに必要な作業量              | 入力                   |
| Task担当者         | 作業を担当するメンバー          | 選択                   |
| リスク・未決事項   | 計画上の懸念や確認事項          | 表示・記録             |
| 計画状態           | 下書き・計画中・確定など        | 表示・操作             |
| 計画履歴           | 変更日時、変更者、変更理由      | 表示                   |

## 5. 主要ユースケース

### UC-SP-01：Sprint Planningを開始する

アクター： プロダクトオーナー、スクラムマスター、開発者

1. 対象Sprintを選択する。
2. Sprintの期間とチームを確認する。
3. チームのキャパシティと過去の実績を確認する。
4. Product Backlogの候補を確認する。
5. 計画作業を開始する。

事後条件：対象Sprintの計画を編集できる状態になる。

### UC-SP-02：Sprint Goalを設定する

1. チームでSprintの目的と達成したい成果を話し合う。
2. Sprint Goalを入力する。
3. Goalと候補Storyの整合性を確認する。
4. チームで合意したGoalを保存する。

業務ルール： Sprint GoalはPOが一方的に決めるのではなく、Scrum Teamが協働して設定する。

### UC-SP-03：Sprint Backlogを選択する

1. Product Backlogから候補Storyを表示する。
2. 優先順位、受入条件、見積もり、依存関係を確認する。
3. Sprint Goalへの貢献を検討する。
4. 開発者が実施可能性とキャパシティを検討する。
5. StoryをSprint Backlogに追加・除外する。
6. 選択したStoryと見積もり合計を確認する。

業務ルール： POは価値や優先順位の背景を提供し、開発者はSprintで実施する作業量を判断する。ベロシティは計画の参考値であり、達成を保証する値ではない。

### UC-SP-04：StoryをTaskに分解する

1. Sprint BacklogからStoryを選択する。
2. 実現に必要な作業を洗い出す。
3. Task名、内容、作業量を登録する。
4. 必要に応じて担当者やTask間の依存関係を設定する。
5. Story全体の作業を確認する。

業務ルール： Taskへの分解方法と実行計画は開発者が具体化する。Taskの担当者設定はチームの運用に応じて任意とする。

### UC-SP-05：計画を確定する

1. Sprint Goalを確認する。
2. 選択済みStoryとTaskを確認する。
3. キャパシティ、依存関係、リスク、未決事項を確認する。
4. 計画確定の条件を検証する。
5. 計画を確定し、スナップショットを保存する。

事後条件：確定時点のSprint Goal、Sprint Backlog、Task、見積もり、キャパシティ、判断記録が参照できる。

## 6. 業務ルールとバリデーション

| ID       | ルール                                                                              |
|----------|-------------------------------------------------------------------------------------|
| BR-SP-01 | Sprint Goalは計画確定前に設定する。                                                 |
| BR-SP-02 | Sprint Backlogに追加できるのは、対象プロダクトの有効なバックログ項目とする。        |
| BR-SP-03 | 受入条件が未定義のStoryには警告を表示する。必要に応じて確定を制限する。             |
| BR-SP-04 | 見積もり未設定のStoryには警告を表示する。                                           |
| BR-SP-05 | キャパシティ超過時は警告を表示するが、機械的にStoryを削除しない。                   |
| BR-SP-06 | 依存関係が未解決のStoryにはリスクを表示する。                                       |
| BR-SP-07 | Storyの見積もりポイントとTaskの作業時間は別の指標として管理する。単純に合算しない。 |
| BR-SP-08 | 計画確定後の変更は、変更者・変更日時・変更内容・理由を記録する。                    |
| BR-SP-09 | Sprint Goalを損なわずに範囲を調整できるよう、Sprint中の変更も記録できる。           |
| BR-SP-10 | Sprint Backlogの作成・更新は開発者の自己管理を支援する設計とする。                  |

## 7. 画面の状態遷移

\#chatgpt-mermaid-\_r_3km\_{font-family:-apple-system,"system-ui","Segoe UI",sans-serif;font-size:
14px;fill:rgb (26, 28, 31);}@keyframes edge-animation-frame{from{stroke-dashoffset:0;}}@keyframes
dash{to{stroke-dashoffset:0;}}#chatgpt-mermaid-\_r_3km\_ .edge-animation-slow{stroke-dasharray:9,5!
important;stroke-dashoffset:900;animation:dash 50s linear infinite;stroke-linecap:
round;}#chatgpt-mermaid-\_r_3km\_ .edge-animation-fast{stroke-dasharray:9,5!
important;stroke-dashoffset:900;animation:dash 20s linear infinite;stroke-linecap:
round;}#chatgpt-mermaid-\_r_3km\_ .error-icon{fill:rgba (255, 255, 255, 0.96)
;}#chatgpt-mermaid-\_r_3km\_ .error-text{fill:rgb (26, 28, 31);stroke:rgb (26, 28, 31)
;}#chatgpt-mermaid-\_r_3km\_ .edge-thickness-normal{stroke-width:1px;}#chatgpt-mermaid-\_r_3km\_
.edge-thickness-thick{stroke-width:3.5px;}#chatgpt-mermaid-\_r_3km\_
.edge-pattern-solid{stroke-dasharray:0;}#chatgpt-mermaid-\_r_3km\_
.edge-thickness-invisible{stroke-width:0;fill:none;}#chatgpt-mermaid-\_r_3km\_
.edge-pattern-dashed{stroke-dasharray:3;}#chatgpt-mermaid-\_r_3km\_
.edge-pattern-dotted{stroke-dasharray:2;}#chatgpt-mermaid-\_r_3km\_ .marker{fill:rgba (26, 28, 31,
0.494);stroke:rgba (26, 28, 31, 0.494);}#chatgpt-mermaid-\_r_3km\_ .marker.cross{stroke:rgba (26,
28, 31, 0.494);}#chatgpt-mermaid-\_r_3km\_ svg{font-family:-apple-system,"system-ui","Segoe
UI",sans-serif;font-size:14px;}#chatgpt-mermaid-\_r_3km\_ p{margin:0;}#chatgpt-mermaid-\_r_3km\_
defs [id$="-barbEnd"]{fill:rgba (26, 28, 31, 0.494);stroke:rgba (26, 28, 31, 0.494);}#chatgpt-mermaid-\_r_3km\_ g.stateGroup text{fill:rgb (83, 154, 248);stroke:none;font-size:10px;}#chatgpt-mermaid-\_r_3km\_ g.stateGroup text{fill:rgb (26, 28, 31);stroke:none;font-size:10px;}#chatgpt-mermaid-\_r_3km\_ g.stateGroup .state-title{font-weight:bolder;fill:rgb (26, 28, 31);}#chatgpt-mermaid-\_r_3km\_ g.stateGroup rect{fill:rgb (224, 237, 254);stroke:rgb (83, 154, 248);}#chatgpt-mermaid-\_r_3km\_ g.stateGroup line{stroke:rgba (26, 28, 31, 0.494);stroke-width:1;}#chatgpt-mermaid-\_r_3km\_ .transition{stroke:rgba (26, 28, 31, 0.494);stroke-width:1;fill:none;}#chatgpt-mermaid-\_r_3km\_ .stateGroup .composit{fill:transparent;border-bottom:1px;}#chatgpt-mermaid-\_r_3km\_ .stateGroup .alt-composit{fill:#e0e0e0;border-bottom:1px;}#chatgpt-mermaid-\_r_3km\_ .state-note{stroke:rgb (107, 198, 127);fill:rgba (255, 255, 255, 0.96);}#chatgpt-mermaid-\_r_3km\_ .state-note text{fill:rgb (26, 28, 31);stroke:none;font-size:10px;}#chatgpt-mermaid-\_r_3km\_ .stateLabel .box{stroke:none;stroke-width:0;fill:rgb (224, 237, 254);opacity:0.5;}#chatgpt-mermaid-\_r_3km\_ .edgeLabel .label rect{fill:rgb (224, 237, 254);opacity:0.5;}#chatgpt-mermaid-\_r_3km\_ .edgeLabel{background-color:rgb (255, 255, 255);text-align:center;}#chatgpt-mermaid-\_r_3km\_ .edgeLabel p{background-color:rgb (255, 255, 255);}#chatgpt-mermaid-\_r_3km\_ .edgeLabel rect{opacity:0.5;background-color:rgb (255, 255, 255);fill:rgb (255, 255, 255);}#chatgpt-mermaid-\_r_3km\_ .edgeLabel .label text{fill:rgb (26, 28, 31);}#chatgpt-mermaid-\_r_3km\_ .label div .edgeLabel{color:rgb (26, 28, 31);}#chatgpt-mermaid-\_r_3km\_ .stateLabel text{fill:rgb (26, 28, 31);font-size:10px;font-weight:bold;}#chatgpt-mermaid-\_r_3km\_ .node circle.state-start{fill:rgba (26, 28, 31, 0.494);stroke:rgba (26, 28, 31, 0.494);}#chatgpt-mermaid-\_r_3km\_ .node .fork-join{fill:rgba (26, 28, 31, 0.494);stroke:rgba (26, 28, 31, 0.494);}#chatgpt-mermaid-\_r_3km\_ .node circle.state-end{fill:rgb (83, 154, 248);stroke:transparent;stroke-width:1.5;}#chatgpt-mermaid-\_r_3km\_ .end-state-inner{fill:transparent;stroke-width:1.5;}#chatgpt-mermaid-\_r_3km\_ .node rect{fill:rgb (224, 237, 254);stroke:rgb (83, 154, 248);stroke-width:1px;}#chatgpt-mermaid-\_r_3km\_ .node polygon{fill:rgb (224, 237, 254);stroke:rgb (83, 154, 248);stroke-width:1px;}#chatgpt-mermaid-\_r_3km\_ [id$="-barbEnd"]
{fill:rgba (26, 28, 31, 0.494);}#chatgpt-mermaid-\_r_3km\_ .statediagram-cluster rect{fill:rgb (224,
237, 254);stroke:rgb (83, 154, 248);stroke-width:1px;}#chatgpt-mermaid-\_r_3km\_
.cluster-label,#chatgpt-mermaid-\_r_3km\_ .nodeLabel{color:rgb (26, 28, 31)
;}#chatgpt-mermaid-\_r_3km\_ .statediagram-cluster rect.outer{rx:5px;ry:
5px;}#chatgpt-mermaid-\_r_3km\_ .statediagram-state .divider{stroke:rgb (83, 154, 248)
;}#chatgpt-mermaid-\_r_3km\_ .statediagram-state .title-state{rx:5px;ry:
5px;}#chatgpt-mermaid-\_r_3km\_ .statediagram-cluster.statediagram-cluster .inner{fill:
transparent;}#chatgpt-mermaid-\_r_3km\_ .statediagram-cluster.statediagram-cluster-alt .inner{fill:
rgba (255, 255, 255, 0.96);}#chatgpt-mermaid-\_r_3km\_ .statediagram-cluster .inner{rx:0;ry:
0;}#chatgpt-mermaid-\_r_3km\_ .statediagram-state rect.basic{rx:5px;ry:
5px;}#chatgpt-mermaid-\_r_3km\_ .statediagram-state rect.divider{stroke-dasharray:10,10;fill:rgba
(255, 255, 255, 0.96);}#chatgpt-mermaid-\_r_3km\_ .note-edge{stroke-dasharray:
5;}#chatgpt-mermaid-\_r_3km\_ .statediagram-note rect{fill:rgba (255, 255, 255, 0.96);stroke:rgb
(107, 198, 127);stroke-width:1px;rx:0;ry:0;}#chatgpt-mermaid-\_r_3km\_ .statediagram-note rect{fill:
rgba (255, 255, 255, 0.96);stroke:rgb (107, 198, 127);stroke-width:1px;rx:0;ry:
0;}#chatgpt-mermaid-\_r_3km\_ .statediagram-note text{fill:rgb (26, 28, 31)
;}#chatgpt-mermaid-\_r_3km\_ .statediagram-note .nodeLabel{color:rgb (26, 28, 31)
;}#chatgpt-mermaid-\_r_3km\_ .statediagram .edgeLabel{color:
red;}#chatgpt-mermaid-\_r_3km\_ [id$="-dependencyStart"],#chatgpt-mermaid-\_r_3km\_ [id$="-dependencyEnd"]
{fill:rgba (26, 28, 31, 0.494);stroke:rgba (26, 28, 31, 0.494);stroke-width:
1;}#chatgpt-mermaid-\_r_3km\_ .statediagramTitleText{text-anchor:middle;font-size:18px;fill:rgb (26,
28, 31);}#chatgpt-mermaid-\_r_3km\_ [data-look="neo"].statediagram-cluster rect{fill:rgb (224, 237,
254);stroke:url (#chatgpt-mermaid-\_r_3km\_-gradient);stroke-width:
1;}#chatgpt-mermaid-\_r_3km\_ [data-look="neo"].statediagram-cluster rect.outer{rx:5px;ry:
5px;filter:drop-shadow (1px 2px 2px rgba (185,185,185,1));}#chatgpt-mermaid-\_r_3km\_ .node
.neo-node{stroke:rgb (83, 154, 248);}#chatgpt-mermaid-\_r_3km\_ [data-look="neo"].node
rect,#chatgpt-mermaid-\_r_3km\_ [data-look="neo"].cluster
rect,#chatgpt-mermaid-\_r_3km\_ [data-look="neo"].node polygon{stroke:url
(#chatgpt-mermaid-\_r_3km\_-gradient);filter:drop-shadow (1px 2px 2px rgba (185,185,185,1))
;}#chatgpt-mermaid-\_r_3km\_ [data-look="neo"].swimlane.cluster rect{filter:
none;}#chatgpt-mermaid-\_r_3km\_ [data-look="neo"].node path{stroke:url
(#chatgpt-mermaid-\_r_3km\_-gradient);stroke-width:1px;}#chatgpt-mermaid-\_r_3km\_ [data-look="neo"]
.node .outer-path{filter:drop-shadow (1px 2px 2px rgba (185,185,185,1))
;}#chatgpt-mermaid-\_r_3km\_ [data-look="neo"].node .neo-line path{stroke:rgb (83, 154, 248);filter:
none;}#chatgpt-mermaid-\_r_3km\_ [data-look="neo"].node circle{stroke:url
(#chatgpt-mermaid-\_r_3km\_-gradient);filter:drop-shadow (1px 2px 2px rgba (185,185,185,1))
;}#chatgpt-mermaid-\_r_3km\_ [data-look="neo"].node circle .state-start{fill:
#000000;}#chatgpt-mermaid-\_r_3km\_ [data-look="neo"].icon-shape .icon{fill:url
(#chatgpt-mermaid-\_r_3km\_-gradient);filter:drop-shadow (1px 2px 2px rgba (185,185,185,1))
;}#chatgpt-mermaid-\_r_3km\_ [data-look="neo"].icon-shape .icon-neo path{stroke:url
(#chatgpt-mermaid-\_r_3km\_-gradient);filter:drop-shadow (1px 2px 2px rgba (185,185,185,1))
;}#chatgpt-mermaid-\_r_3km\_ :root{--mermaid-font-family:-apple-system,"system-ui","Segoe
UI",sans-serif;}未開始計画中確定変更あり完了Planning開始Goal・Story・Taskを編集計画確定Sprint
Backlogを変更変更内容を更新Sprint終了Sprint終了

「計画確定」は計画時点の記録を保存する意味であり、Sprint中の変更を禁止する意味ではありません。変更後も確定時点のスナップショットと変更履歴を参照できるようにします。

## 8. 画面から導出するユースケース・モデル

既存のAgiloomの設計方針に合わせると、次のApplication Use Caseが候補になります。

| Use Case                   | 責務                                                      |
|----------------------------|-----------------------------------------------------------|
| `PrepareSprintPlanning`    | 計画に必要なSprint情報・候補Story・キャパシティを準備する |
| `SetSprintGoal`            | Sprint Goalを設定・更新する                               |
| `SelectSprintBacklogItems` | Sprint Backlog項目を追加・除外する                        |
| `UpdateTeamCapacity`       | チームのキャパシティを更新する                            |
| `BreakDownStoryIntoTasks`  | StoryをTaskに分解する                                     |
| `RecordPlanningDecision`   | 計画上の判断と理由を記録する                              |
| `CapturePlanningSnapshot`  | 計画時点の状態を保存する                                  |
| `FinalizeSprintPlan`       | 計画確定条件を検証し、確定する                            |

画面表示用のQuery Modelとしては、`SprintPlanningDashboardQuery`、`ProductBacklogCandidatesQuery`、
`TeamCapacityQuery`、`SprintVelocityHistoryQuery`、`SprintPlanningRiskQuery` が考えられます。

これらは画面とドメインモデルを一対一にするものではありません。画面は複数のQuery
Modelから情報を取得し、更新操作は適切なApplication Use Caseに委譲する設計が適しています。

## 9. MVPの受入条件

- 対象Sprintの期間とチームを確認できる。
- Sprint Goalを入力して保存できる。
- Product BacklogからStoryを追加・除外できる。
- 選択済みStoryの見積もり合計を確認できる。
- チームキャパシティと過去のベロシティを確認できる。
- StoryをTaskに分解できる。
- 未設定の受入条件、見積もり、依存関係を確認できる。
- 計画を下書き保存して再編集できる。
- 計画確定時にスナップショットを保存できる。
- 計画確定後の変更履歴を参照できる。
- 権限のない利用者は計画を変更できない。

推奨するMVP範囲は、Sprint
Goal、Story選択、キャパシティ・ベロシティ表示、Task分解、計画保存・確定です。担当者の自動割り当てやAIによるStory選択は、チームでの計画運用が固まった後に追加するのが適切です。

# Sprint Planning 画面（SCR-014）の機能要望

## 1. 概要

AgiloomのSprint Planning画面（SCR-014）は、プロダクトオーナー、スクラムマスター、開発チームが協働してスプリント計画を作成するための画面です。Sprint Goalの設定、バックログからのStory選択、チーム容量・ベロシティの確認、タスク分解等功能を提供します。

## 2. 利用者・役割

| 役割 | 主な操作 |
|------|----------|
| プロダクトオーナー（PO） | Sprint Goalの定義、Storyの優先順位付け、容量の確認 |
| スクラムマスター（SM） | 計画の進行支援、ベロシティの確認、リスクの特定 |
| 開発者 | Storyの見積もり、タスク分解、担当の決定 |

## 3. 機能要望

### 3.1 スプリントの作成
- 新規スプリントの期間（開始日・終了日）を定義する
- 担当チームを指定する
- スプリント状態を「計画済み」に設定する

### 3.2 Sprint Goalの設定・更新
- スプリントの目的（Sprint Goal）を記録する
- Sprint Goalは500文字以内で必須入力
- 計画の合意形成を支援するために共有・編集可能

### 3.3 Sprint Backlogの作成
- Product BacklogからStoryを選択してSprint Backlogに追加する
- 選択したStoryの見積もり合計がチーム容量を超えないように整合性チェック
- Storyをタスクに分解する
- 各タスクに担当者・見積もりを割り当てる

### 3.4 容量・ベロシティの確認
- チームの作業容量（キャパシティ）を表示・入力する
- 過去のベロシティを参照できる
- 選択したStoryの見積もり合計と容量を比較表示する

### 3.5 依存関係・リスクの特定
- Story間の依存関係を定義する
- リスクを特定し、対応策を記録する

## 4. 非機能要望

- リアルタイム性: 複数人での同時編集を支援
- 整合性チェック: 容量超過時に即座に警告
- 履歴管理: 計画の変更履歴を追跡可能

## 5. 制約事項

- Sprint Planningの編集はプロダクトオーナーとスクラムマスターのみ可能
- 選択したStoryの見積もり合計はチームの容量を超えてはいけない
- スプリント期間は2-4週間以内
