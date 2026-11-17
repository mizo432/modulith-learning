# Agiloom：スプリントレビュー画面の要件

Agiloomのスプリントレビュー画面は、Sprintで完成した成果をステークホルダーに共有し、Product GoalやSprint
Goalに対する進捗を確認し、フィードバックを収集してProduct Backlogに反映するための画面です。

重要なのは、単に完了したStoryを報告することではありません。

実際に動作する成果を確認し、ステークホルダーからフィードバックを得て、今後のプロダクトの方向性やバックログを見直すことを支援します。

Sprint Reviewは成果物の受入検査だけを行う場ではなく、Scrum
Teamとステークホルダーが協働してプロダクトの状況や今後の対応を検討するイベントとして設計します。

## 1. 画面の目的

1. Sprint Goalと達成状況を確認する。
2. Sprintで完成した成果を一覧表示する。
3. 実際に動作するインクリメントをデモする。
4. 完了・未完了のStoryとその理由を確認する。
5. ステークホルダーからフィードバックを収集する。
6. フィードバックを評価し、対応方針を決定する。
7. 新しいバックログ候補や既存Storyの変更につなげる。
8. Sprint Reviewの議論・決定事項を記録する。

## 2. 画面構成案

Sprint Reviewは、準備・開催・結果整理の3段階で利用する画面として設計します。

## Sprint Review

Sprint 12 · 2026/10/19〜10/30

開催中

### ① Sprint Goal

利用者が申請状況をオンラインで確認できるようにする

Goal達成状況

達成

# 8

計画Story

# 6

完了Story

# 2

未完了Story

### ② Sprint成果

US-101 申請状況一覧

完了

申請状況を一覧表示し、検索できる

デモ情報を確認

US-102 申請詳細表示

完了

申請の詳細情報を確認できる

US-103 申請の絞り込み

未完了

検索条件の実装が未完了

### ③ ステークホルダーフィードバック

フィードバック例

「申請状況を一覧で確認できるのは便利だが、申請日でも検索したい」

発言者：業務担当者

対応方針

バックログ候補

### ④ Reviewの決定事項

- 申請日による検索をバックログ候補に追加する
- 未完了Storyは次Sprint以降で再検討する
- 次回Reviewで検索機能の操作性を確認する

開催記録Reviewを完了する

ワイヤーフレーム例。Story、数値、フィードバックは説明用の仮データです。

## 3. 画面一覧

Sprint Reviewの機能は、次の5画面に分けることを推奨します。

| ID    | 画面名             | 目的                                                 |
|-------|--------------------|------------------------------------------------------|
| SR-01 | Sprint Review一覧  | 過去・現在のSprint Reviewを確認する                  |
| SR-02 | Sprint Review準備  | 成果、Sprint Goal、デモ対象、参加者、議題を準備する  |
| SR-03 | Sprint Review実施  | 成果デモ、フィードバック、議論内容を記録する         |
| SR-04 | フィードバック評価 | フィードバックを分類・評価し、対応方針を決める       |
| SR-05 | Sprint Review結果  | 決定事項、バックログ候補、未完了項目、結果を確認する |

画面を1つにまとめることもできますが、準備と開催中の操作、終了後の評価を分けると、情報の整理がしやすくなります。

## 4. 画面要件一覧

| ID        | 要件                                                     | 優先度 |
|-----------|----------------------------------------------------------|--------|
| SR-REQ-01 | 対象Sprintの期間、Sprint Goalを表示する                  | 必須   |
| SR-REQ-02 | Sprint Planning時点の計画内容を参照できる                | 必須   |
| SR-REQ-03 | Sprint BacklogのStory一覧を表示する                      | 必須   |
| SR-REQ-04 | Storyの完了・未完了を表示する                            | 必須   |
| SR-REQ-05 | Sprint Goalの達成状況と根拠を記録できる                  | 必須   |
| SR-REQ-06 | 完成したインクリメントやデモ対象を登録できる             | 必須   |
| SR-REQ-07 | デモの説明や実施結果を記録できる                         | 推奨   |
| SR-REQ-08 | ステークホルダー・参加者を登録できる                     | 必須   |
| SR-REQ-09 | Reviewの議題を設定できる                                 | 必須   |
| SR-REQ-10 | フィードバックを記録できる                               | 必須   |
| SR-REQ-11 | フィードバックに発言者・日時・関連Storyを紐付けられる    | 必須   |
| SR-REQ-12 | フィードバックを分類できる                               | 必須   |
| SR-REQ-13 | フィードバックの対応方針を決定できる                     | 必須   |
| SR-REQ-14 | フィードバックからバックログ候補を作成できる             | 必須   |
| SR-REQ-15 | 既存のEpic・Feature・Storyにフィードバックを紐付けられる | 必須   |
| SR-REQ-16 | Review中の議論・決定事項を記録できる                     | 必須   |
| SR-REQ-17 | Review終了後に結果を参照できる                           | 必須   |
| SR-REQ-18 | 過去のReview結果やフィードバックを参照できる             | 推奨   |
| SR-REQ-19 | Reviewの変更履歴を記録できる                             | 必須   |
| SR-REQ-20 | Review結果を関係者に共有できる                           | 推奨   |

## 5. 画面ごとの詳細要件

### SR-01：Sprint Review一覧

目的： プロジェクト内のSprint Reviewの開催状況と過去の結果を確認する。

表示項目：

| 項目             | 内容                         |
|------------------|------------------------------|
| Sprint名         | 対象Sprint                   |
| 開催予定日       | Reviewの予定日時             |
| 開催実績日       | 実際の開催日時               |
| Sprint Goal      | 対象Sprintの目標             |
| Review状態       | 未準備・準備中・開催中・完了 |
| 参加者数         | Review参加者                 |
| フィードバック数 | 登録されたフィードバック件数 |
| 決定事項数       | 記録された決定事項件数       |

操作：

- Sprint Reviewを開始する
- Review準備画面を開く
- Review実施画面を開く
- 過去のReview結果を参照する

### SR-02：Sprint Review準備

目的： Review開催前に、成果と議題を準備する。

主な機能：

1. Sprint Goalの確認
2. Sprint Planning時点の計画の確認
3. 完了Story・未完了Storyの確認
4. デモ対象の選択
5. デモ内容・説明の登録
6. 参加者の登録
7. Review議題の登録
8. 前回Reviewのフィードバック確認

特に、Sprint Planning時点の計画と実際の結果を比較できることが重要です。

未完了Storyについては、未完了となった理由や残作業を確認できるようにします。

### SR-03：Sprint Review実施

目的： 成果を共有し、ステークホルダーとの対話やフィードバック収集を支援する。

主な機能：

- Sprint Goalと実績の説明
- 完成したインクリメントのデモ
- Storyごとの成果確認
- ステークホルダーからの質問・意見の記録
- フィードバックの登録
- 議論内容の記録
- 決定事項の記録

フィードバック登録項目：

| 項目               | 内容                   |
|--------------------|------------------------|
| フィードバック内容 | 意見・要望・質問・課題 |
| 発言者             | ステークホルダー       |
| 登録日時           | 発言・記録日時         |
| 関連Story          | 関連するUser Story     |
| 関連Feature        | 関連するFeature        |
| 関連Epic           | 関連するEpic           |
| 添付資料           | 画像、資料、URLなど    |
| 補足               | 背景や具体例           |

フィードバックは、特定のStoryに紐付かない場合もあるため、関連Storyを必須にはしません。

### SR-04：フィードバック評価

目的： 収集したフィードバックを整理し、Product Backlogに反映するかどうかを判断する。

フィードバックの分類例：

| 分類           | 説明                             |
|----------------|----------------------------------|
| 新機能要望     | 新しい機能の追加要望             |
| 既存機能改善   | 既存機能の使いやすさ・動作の改善 |
| 不具合         | 想定と異なる動作                 |
| 業務ルール変更 | 業務上のルールや運用の変更       |
| UI/UX改善      | 操作性や画面表示の改善           |
| 質問・確認     | 仕様や動作に関する質問           |
| その他         | 上記に該当しない意見             |

対応方針：

| 対応方針         | 説明                       |
|------------------|----------------------------|
| バックログに追加 | 新しいStory等を作成する    |
| 既存Storyを変更  | 既存Storyの要件を変更する  |
| 調査する         | 技術・業務面の調査を行う   |
| 保留             | 今後の判断に向けて保留する |
| 対応しない       | 理由を記録して対応しない   |
| 追加情報を確認   | 発言者等に詳細を確認する   |

重要なのは、フィードバックを自動的にすべてバックログに追加しないことです。POが価値、コスト、優先順位などを考慮して判断できるようにします。

### SR-05：Sprint Review結果

目的： Reviewで得られた成果、フィードバック、決定事項を整理して残す。

表示項目：

- Sprint Goalと達成状況
- Sprint Backlogの計画と実績
- 完了Story・未完了Story
- デモ対象と成果
- 参加者
- フィードバック一覧
- フィードバックの対応方針
- 作成されたバックログ候補
- 決定事項
- 継続確認事項

Review結果は、後続のSprint PlanningやProduct Backlog Refinementで参照できるようにします。

## 6. 主要ユースケース

| ID       | ユースケース                               | アクター                     |
|----------|--------------------------------------------|------------------------------|
| UC-SR-01 | Sprint Reviewを開始する                    | PO、SM                       |
| UC-SR-02 | Sprint Reviewを準備する                    | PO、SM、開発者               |
| UC-SR-03 | Review参加者を登録する                     | PO、SM                       |
| UC-SR-04 | Review議題を設定する                       | PO、SM                       |
| UC-SR-05 | Sprint成果を確認する                       | Scrum Team、ステークホルダー |
| UC-SR-06 | インクリメントをデモする                   | 開発者                       |
| UC-SR-07 | ステークホルダーフィードバックを記録する   | PO、SM、ステークホルダー     |
| UC-SR-08 | フィードバックを分類する                   | PO                           |
| UC-SR-09 | フィードバックの対応方針を決定する         | PO                           |
| UC-SR-10 | フィードバックからバックログ候補を作成する | PO                           |
| UC-SR-11 | 既存Storyを更新する                        | PO                           |
| UC-SR-12 | Reviewの決定事項を記録する                 | PO、SM                       |
| UC-SR-13 | Sprint Reviewを終了する                    | PO、SM                       |
| UC-SR-14 | Sprint Review結果を確認する                | Scrum Team、ステークホルダー |

## 7. 主要ユースケースの詳細

### UC-SR-01：Sprint Reviewを準備する

アクター： PO、SM

1. 対象Sprintを選択する。
2. Sprint Goalを確認する。
3. Sprint Backlogと計画時点の情報を取得する。
4. 完了・未完了Storyを確認する。
5. デモ対象となるインクリメントを選択する。
6. 参加者と議題を登録する。
7. Review準備を保存する。

事後条件：Review開催に必要な情報が準備されている。

### UC-SR-02：Sprint成果を確認する

アクター： Scrum Team、ステークホルダー

1. Sprint Goalを確認する。
2. 完了Storyと未完了Storyを確認する。
3. 完成したインクリメントを確認する。
4. 実際の動作をデモする。
5. ステークホルダーと成果について議論する。
6. 質問やフィードバックを記録する。

事後条件：成果確認の内容とフィードバックが記録されている。

### UC-SR-03：フィードバックを記録する

アクター： PO、SM、ステークホルダー

1. フィードバック内容を入力する。
2. 発言者を登録する。
3. 関連するStoryやFeatureを指定する。
4. 必要に応じて資料を添付する。
5. フィードバックを保存する。

事後条件：フィードバックがReviewに紐付いて保存されている。

### UC-SR-04：フィードバックの対応方針を決定する

アクター： PO

1. 未評価のフィードバックを確認する。
2. 内容と背景を確認する。
3. プロダクトの価値やProduct Goalへの影響を検討する。
4. 優先順位や実現コストを検討する。
5. 対応方針を選択する。
6. 判断理由を記録する。

事後条件：フィードバックの対応方針が決定されている。

### UC-SR-05：フィードバックからバックログ候補を作成する

アクター： PO

1. 対象フィードバックを選択する。
2. 新規Story、Feature、Epicのいずれが適切かを判断する。
3. バックログ候補を作成する。
4. 元のフィードバックとの関連を保持する。
5. 優先順位を設定する。

事後条件：フィードバックとバックログ候補のトレーサビリティが確保される。

### UC-SR-06：Sprint Reviewを終了する

アクター： PO、SM

1. フィードバックの記録状況を確認する。
2. 決定事項と継続確認事項を確認する。
3. Review結果を保存する。
4. Reviewを完了状態に変更する。

事後条件：Review結果が確定し、後から参照できる。

## 8. 業務ルール

| ID       | 業務ルール                                                                     |
|----------|--------------------------------------------------------------------------------|
| BR-SR-01 | Sprint Reviewは対象Sprintに対して実施する。                                    |
| BR-SR-02 | Sprint GoalとSprint Planning時点の計画を参照できる。                           |
| BR-SR-03 | Storyの完了状態だけでなく、実際のインクリメントを確認できるようにする。        |
| BR-SR-04 | Definition of Doneを満たしていない作業を完成したインクリメントとして扱わない。 |
| BR-SR-05 | 未完了Storyは完了扱いにせず、Product Backlogに戻して再検討する。               |
| BR-SR-06 | フィードバックは複数のStory・Feature・Epicに関連付けられるようにする。         |
| BR-SR-07 | フィードバックはバックログに自動登録せず、POが対応方針を判断する。             |
| BR-SR-08 | フィードバックの採用・保留・却下には判断理由を記録する。                       |
| BR-SR-09 | Review終了後も過去の結果を参照できる。                                         |
| BR-SR-10 | Review結果をSprint Retrospectiveの入力情報として利用できる。                   |
| BR-SR-11 | Reviewの完了とReleaseの承認・リリース判断は別の業務として扱う。                |

## 9. ドメインモデル候補

既存のAgiloomの設計方針に沿って、次のモデルを推奨します。

| モデル                      | 主な属性・役割                                |
|-----------------------------|-----------------------------------------------|
| `SprintReview`              | Reviewの対象Sprint、開催日時、状態            |
| `ReviewAgendaItem`          | Reviewの議題、順序、説明                      |
| `ReviewParticipant`         | 参加者、役割、参加状況                        |
| `ReviewSnapshot`            | Review時点のSprint Goal、Sprint Backlog、実績 |
| `ReviewIncrement`           | デモ対象となるインクリメント、説明、参照情報  |
| `ReviewFeedback`            | フィードバック内容、発言者、分類、関連情報    |
| `ReviewFeedbackDisposition` | 対応方針、判断理由、判断者                    |
| `ReviewDiscussion`          | 議論内容                                      |
| `ReviewDecision`            | 決定事項、決定理由                            |
| `ReviewAction`              | 継続確認事項、担当者、期限、状態              |

### モデル間の関係

\#chatgpt-mermaid-\_r_3rd\_{font-family:-apple-system,"system-ui","Segoe UI",sans-serif;font-size:
14px;fill:rgb (26, 28, 31);}@keyframes edge-animation-frame{from{stroke-dashoffset:0;}}@keyframes
dash{to{stroke-dashoffset:0;}}#chatgpt-mermaid-\_r_3rd\_ .edge-animation-slow{stroke-dasharray:9,5!
important;stroke-dashoffset:900;animation:dash 50s linear infinite;stroke-linecap:
round;}#chatgpt-mermaid-\_r_3rd\_ .edge-animation-fast{stroke-dasharray:9,5!
important;stroke-dashoffset:900;animation:dash 20s linear infinite;stroke-linecap:
round;}#chatgpt-mermaid-\_r_3rd\_ .error-icon{fill:rgba (255, 255, 255, 0.96)
;}#chatgpt-mermaid-\_r_3rd\_ .error-text{fill:rgb (26, 28, 31);stroke:rgb (26, 28, 31)
;}#chatgpt-mermaid-\_r_3rd\_ .edge-thickness-normal{stroke-width:1px;}#chatgpt-mermaid-\_r_3rd\_
.edge-thickness-thick{stroke-width:3.5px;}#chatgpt-mermaid-\_r_3rd\_
.edge-pattern-solid{stroke-dasharray:0;}#chatgpt-mermaid-\_r_3rd\_
.edge-thickness-invisible{stroke-width:0;fill:none;}#chatgpt-mermaid-\_r_3rd\_
.edge-pattern-dashed{stroke-dasharray:3;}#chatgpt-mermaid-\_r_3rd\_
.edge-pattern-dotted{stroke-dasharray:2;}#chatgpt-mermaid-\_r_3rd\_ .marker{fill:rgba (26, 28, 31,
0.494);stroke:rgba (26, 28, 31, 0.494);}#chatgpt-mermaid-\_r_3rd\_ .marker.cross{stroke:rgba (26,
28, 31, 0.494);}#chatgpt-mermaid-\_r_3rd\_ svg{font-family:-apple-system,"system-ui","Segoe
UI",sans-serif;font-size:14px;}#chatgpt-mermaid-\_r_3rd\_ p{margin:0;}#chatgpt-mermaid-\_r_3rd\_
g.classGroup text{fill:rgb (83, 154, 248);stroke:none;font-family:-apple-system,"system-ui","Segoe
UI",sans-serif;font-size:10px;}#chatgpt-mermaid-\_r_3rd\_ g.classGroup text .title{font-weight:
bolder;}#chatgpt-mermaid-\_r_3rd\_ .cluster-label text{fill:rgb (26, 28, 31)
;}#chatgpt-mermaid-\_r_3rd\_ .cluster-label span{color:rgb (26, 28, 31);}#chatgpt-mermaid-\_r_3rd\_
.cluster-label span p{background-color:transparent;}#chatgpt-mermaid-\_r_3rd\_ .cluster rect{fill:
rgba (255, 255, 255, 0.96);stroke:rgba (26, 28, 31, 0.08);stroke-width:
1px;}#chatgpt-mermaid-\_r_3rd\_ .cluster text{fill:rgb (26, 28, 31);}#chatgpt-mermaid-\_r_3rd\_
.cluster span{color:rgb (26, 28, 31);}#chatgpt-mermaid-\_r_3rd\_
.nodeLabel,#chatgpt-mermaid-\_r_3rd\_ .edgeLabel{color:rgb (26, 28, 31);}#chatgpt-mermaid-\_r_3rd\_
.noteLabel .nodeLabel,#chatgpt-mermaid-\_r_3rd\_ .noteLabel .edgeLabel{color:rgb (26, 28, 31)
;}#chatgpt-mermaid-\_r_3rd\_ .edgeLabel .label rect{fill:rgb (224, 237, 254)
;}#chatgpt-mermaid-\_r_3rd\_ .label text{fill:rgb (26, 28, 31);}#chatgpt-mermaid-\_r_3rd\_
.labelBkg{background:rgb (224, 237, 254);}#chatgpt-mermaid-\_r_3rd\_ .edgeLabel .label
span{background:rgb (224, 237, 254);}#chatgpt-mermaid-\_r_3rd\_ .classTitle{font-weight:
bolder;}#chatgpt-mermaid-\_r_3rd\_ .node rect,#chatgpt-mermaid-\_r_3rd\_ .node
circle,#chatgpt-mermaid-\_r_3rd\_ .node ellipse,#chatgpt-mermaid-\_r_3rd\_ .node
polygon,#chatgpt-mermaid-\_r_3rd\_ .node path{fill:rgb (224, 237, 254);stroke:rgb (83, 154, 248)
;stroke-width:1;}#chatgpt-mermaid-\_r_3rd\_ .divider{stroke:rgb (83, 154, 248);stroke-width:
1;}#chatgpt-mermaid-\_r_3rd\_ g.clickable{cursor:pointer;}#chatgpt-mermaid-\_r_3rd\_ g.classGroup
rect{fill:rgb (224, 237, 254);stroke:rgb (83, 154, 248);}#chatgpt-mermaid-\_r_3rd\_ g.classGroup
line{stroke:rgb (83, 154, 248);stroke-width:1;}#chatgpt-mermaid-\_r_3rd\_ .classLabel .box{stroke:
none;stroke-width:0;fill:rgb (224, 237, 254);opacity:0.5;}#chatgpt-mermaid-\_r_3rd\_ .classLabel
.label{fill:rgb (83, 154, 248);font-size:10px;}#chatgpt-mermaid-\_r_3rd\_ .relation{stroke:rgba (26,
28, 31, 0.494);stroke-width:1;fill:none;}#chatgpt-mermaid-\_r_3rd\_ .dashed-line{stroke-dasharray:
3;}#chatgpt-mermaid-\_r_3rd\_ .dotted-line{stroke-dasharray:1
2;}#chatgpt-mermaid-\_r_3rd\_ [id$="-compositionStart"],#chatgpt-mermaid-\_r_3rd\_ .composition{fill:rgba (26, 28, 31, 0.494)!important;stroke:rgba (26, 28, 31, 0.494)!important;stroke-width:1;}#chatgpt-mermaid-\_r_3rd\_ [id$="-compositionEnd"]
,#chatgpt-mermaid-\_r_3rd\_ .composition{fill:rgba (26, 28, 31, 0.494)!important;stroke:rgba (26,
28, 31, 0.494)!important;stroke-width:
1;}#chatgpt-mermaid-\_r_3rd\_ [id$="-dependencyStart"],#chatgpt-mermaid-\_r_3rd\_ .dependency{fill:rgba (26, 28, 31, 0.494)!important;stroke:rgba (26, 28, 31, 0.494)!important;stroke-width:1;}#chatgpt-mermaid-\_r_3rd\_ [id$="-dependencyEnd"]
,#chatgpt-mermaid-\_r_3rd\_ .dependency{fill:rgba (26, 28, 31, 0.494)!important;stroke:rgba (26, 28,
31, 0.494)!important;stroke-width:
1;}#chatgpt-mermaid-\_r_3rd\_ [id$="-extensionStart"],#chatgpt-mermaid-\_r_3rd\_ .extension{fill:transparent!important;stroke:rgba (26, 28, 31, 0.494)!important;stroke-width:1;}#chatgpt-mermaid-\_r_3rd\_ [id$="-extensionEnd"]
,#chatgpt-mermaid-\_r_3rd\_ .extension{fill:transparent!important;stroke:rgba (26, 28, 31, 0.494)!
important;stroke-width:
1;}#chatgpt-mermaid-\_r_3rd\_ [id$="-aggregationStart"],#chatgpt-mermaid-\_r_3rd\_ .aggregation{fill:transparent!important;stroke:rgba (26, 28, 31, 0.494)!important;stroke-width:1;}#chatgpt-mermaid-\_r_3rd\_ [id$="-aggregationEnd"]
,#chatgpt-mermaid-\_r_3rd\_ .aggregation{fill:transparent!important;stroke:rgba (26, 28, 31, 0.494)!
important;stroke-width:
1;}#chatgpt-mermaid-\_r_3rd\_ [id$="-lollipopStart"],#chatgpt-mermaid-\_r_3rd\_ .lollipop{fill:rgb (224, 237, 254)!important;stroke:rgba (26, 28, 31, 0.494)!important;stroke-width:1;}#chatgpt-mermaid-\_r_3rd\_ [id$="-lollipopEnd"]
,#chatgpt-mermaid-\_r_3rd\_ .lollipop{fill:rgb (224, 237, 254)!important;stroke:rgba (26, 28, 31,
0.494)!important;stroke-width:1;}#chatgpt-mermaid-\_r_3rd\_ .edgeTerminals{font-size:
11px;line-height:initial;}#chatgpt-mermaid-\_r_3rd\_ .classTitleText{text-anchor:middle;font-size:
18px;fill:rgb (26, 28, 31);}#chatgpt-mermaid-\_r_3rd\_ .edgeLabel[data-look="neo"]{background-color:
rgb (255, 255, 255);text-align:center;}#chatgpt-mermaid-\_r_3rd\_ .edgeLabel[data-look="neo"]
p{background-color:rgb (255, 255, 255);}#chatgpt-mermaid-\_r_3rd\_ .edgeLabel[data-look="neo"]
rect{opacity:0.5;background-color:rgb (255, 255, 255);fill:rgb (255, 255, 255)
;}#chatgpt-mermaid-\_r_3rd\_ .label-icon{display:inline-block;height:1em;overflow:
visible;vertical-align:-0.125em;}#chatgpt-mermaid-\_r_3rd\_ .node .label-icon path{fill:
currentColor;stroke:revert;stroke-width:revert;}#chatgpt-mermaid-\_r_3rd\_ .node .neo-node{stroke:
rgb (83, 154, 248);}#chatgpt-mermaid-\_r_3rd\_ [data-look="neo"].node
rect,#chatgpt-mermaid-\_r_3rd\_ [data-look="neo"].cluster
rect,#chatgpt-mermaid-\_r_3rd\_ [data-look="neo"].node polygon{stroke:url
(#chatgpt-mermaid-\_r_3rd\_-gradient);filter:drop-shadow (1px 2px 2px rgba (185,185,185,1))
;}#chatgpt-mermaid-\_r_3rd\_ [data-look="neo"].swimlane.cluster rect{filter:
none;}#chatgpt-mermaid-\_r_3rd\_ [data-look="neo"].node path{stroke:url
(#chatgpt-mermaid-\_r_3rd\_-gradient);stroke-width:1px;}#chatgpt-mermaid-\_r_3rd\_ [data-look="neo"]
.node .outer-path{filter:drop-shadow (1px 2px 2px rgba (185,185,185,1))
;}#chatgpt-mermaid-\_r_3rd\_ [data-look="neo"].node .neo-line path{stroke:rgb (83, 154, 248);filter:
none;}#chatgpt-mermaid-\_r_3rd\_ [data-look="neo"].node circle{stroke:url
(#chatgpt-mermaid-\_r_3rd\_-gradient);filter:drop-shadow (1px 2px 2px rgba (185,185,185,1))
;}#chatgpt-mermaid-\_r_3rd\_ [data-look="neo"].node circle .state-start{fill:
#000000;}#chatgpt-mermaid-\_r_3rd\_ [data-look="neo"].icon-shape .icon{fill:url
(#chatgpt-mermaid-\_r_3rd\_-gradient);filter:drop-shadow (1px 2px 2px rgba (185,185,185,1))
;}#chatgpt-mermaid-\_r_3rd\_ [data-look="neo"].icon-shape .icon-neo path{stroke:url
(#chatgpt-mermaid-\_r_3rd\_-gradient);filter:drop-shadow (1px 2px 2px rgba (185,185,185,1))
;}#chatgpt-mermaid-\_r_3rd\_ :root{--mermaid-font-family:-apple-system,"system-ui","Segoe
UI",sans-serif;}SprintReviewReviewAgendaItemReviewParticipantReviewSnapshotReviewIncrementReviewFeedbackReviewDiscussionReviewDecisionReviewActionReviewFeedbackDispositionUserStoryFeatureEpic111111111\*\*\*1\*\*1\*\*\*\*\*0..1\*\*\*0..\*

※関連するStory等への参照は、単一のフィードバックが複数のバックログ項目に関係する可能性を考慮しています。

## 10. Query Model候補

CQRSを採用するAgiloomでは、画面表示用に以下のQuery Modelを用意するとよいでしょう。

| Query Model                      | 用途                     |
|----------------------------------|--------------------------|
| `SprintReviewListQuery`          | Sprint Review一覧        |
| `SprintReviewPreparationQuery`   | Review準備情報           |
| `SprintReviewDashboardQuery`     | Review全体の状況         |
| `SprintGoalAchievementQuery`     | Sprint Goalと達成状況    |
| `SprintOutcomeSummaryQuery`      | 計画と実績の比較         |
| `SprintIncrementListQuery`       | デモ対象インクリメント   |
| `ReviewParticipantListQuery`     | 参加者一覧               |
| `ReviewFeedbackListQuery`        | フィードバック一覧       |
| `ReviewFeedbackDispositionQuery` | フィードバックの評価状況 |
| `ReviewDecisionListQuery`        | 決定事項一覧             |
| `ReviewBacklogCandidateQuery`    | バックログ候補一覧       |
| `PreviousReviewLearningQuery`    | 過去のReviewからの学び   |

## 11. MVPの実装優先順位

| フェーズ | 対象機能                                                           |
|----------|--------------------------------------------------------------------|
| Phase 1  | Sprint Goal、Sprint成果、完了・未完了Story、Review記録             |
| Phase 2  | 参加者、議題、デモ対象、フィードバック記録                         |
| Phase 3  | フィードバック評価、バックログ候補作成、決定事項                   |
| Phase 4  | 過去Reviewとの比較、メトリクス連携、AIによるフィードバック分類支援 |

### MVPの受入条件

- 対象SprintのGoalと計画を確認できる。
- 完了・未完了Storyを確認できる。
- Reviewの参加者と議題を登録できる。
- デモ対象と説明を記録できる。
- ステークホルダーのフィードバックを登録できる。
- フィードバックに関連Storyを紐付けられる。
- フィードバックの対応方針と判断理由を記録できる。
- フィードバックからバックログ候補を作成できる。
- Review結果と決定事項を保存できる。
- Review終了後も記録を参照できる。

## 12. Sprint Planningとの連携

Sprint PlanningとSprint Reviewは、計画と成果をつなぐ関係になります。

Sprint Planning

Sprint Goal・Sprint Backlog・計画スナップショット

Sprint実行

Sprint Review

成果・インクリメント・フィードバック・決定事項

Product Backlog更新

新規Story・既存Story変更・優先順位見直し

次のSprint Planning

新しい情報を反映した計画

Agiloomの設計上の重要ポイント

Sprint Reviewのフィードバックを単なる議事録にとどめず、次の情報につなげることが重要です。

- Review Feedback → Product Backlog
- Review Feedback → User JourneyのPain Point / Opportunity
- Review Decision → Product Goalや優先順位の見直し
- Review Action → 継続確認事項
- Review Snapshot → Sprint成果の履歴

これにより、AgiloomはSprintイベントの記録ツールではなく、ステークホルダーの意見と実際のプロダクト成果を継続的なプロダクト改善につなげるシステムになります。
