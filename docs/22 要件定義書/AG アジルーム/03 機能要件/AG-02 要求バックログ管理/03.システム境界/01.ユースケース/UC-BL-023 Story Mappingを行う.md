# UC-BL-023 Story Mappingを行う

ユーザーストーリーマップを作成・編集し、ユーザーの活動・タスク・ストーリーを整理してリリース範囲を検討する。

## 概要

| 項目 | 内容 |
|------|------|
| ユースケースID | UC-BL-023 |
| ユースケース名 | Story Mappingを行う |
| 英語名 | Story Mapping |
| 説明 | ユーザーの活動や業務の流れを起点として、Activity → User Task → User Story → Release Slice の4階層でストーリーマップを作成・編集する |
| 関連アクター | ACT-02（プロダクトオーナー）、ACT-04（開発チーム） |
| 優先度 | 高 |
| ビジネスユースケース | BUC-BL-02 |
| 外部システム | - |
| 関連画面 | SCR-007（ユーザーストーリーマップ） |

## 前置条件

- ユーザーがシステムにログインしていること
- 対象Productが存在すること
- バックログ項目（INF-BL-01）が既に存在すること（既存Storyを配置する場合）

## 基本フロー

1. ユーザーが「ユーザーストーリーマップ」画面（SCR-007）にアクセスする
2. 新規Story Mapを作成するか、既存のマップを選択する
3. マップの基本情報（マップ名、説明、対象Product、対象Persona）を設定する
4. Activity Backboneにユーザーの大きな活動を横方向に追加・並べ替える
5. 各ActivityにUser Taskを追加し、具体的なユーザー行動を分解する
6. 各User TaskにUser Storyを配置する（新規作成または既存Product Backlogから選択）
7. User Storyをドラッグ＆ドロップで移動・並べ替える
8. Release Sliceを作成し、Storyをリリース単位に割り当てる
9. 変更を保存する
10. システムがStory Mapの変更履歴を記録する
11. 完了メッセージを表示する

## 代替フロー

- **3a. Personaを設定する場合**
  - ユーザーは対象となるPersonaを1つ以上選択する
  - ActivityごとにPersonaを設定することも可能

- **6a. 既存Product BacklogからStoryを配置する場合**
  - ユーザーはProduct BacklogからStoryを検索・選択する
  - 選択したStoryは参照としてマップに配置される（複製ではない）
  - Story Map上の並び順とProduct Backlogの優先順位は別々に管理される

- **6b. 新規Storyを作成する場合**
  - ユーザーはマップ上で直接Storyを作成する
  - 作成したStoryはProduct Backlogにも登録される

- **7a. Storyを複数のTaskに関連付ける場合**
  - 1つのUser Storyを複数のUser Taskに関連付けられる

- **8a. Release Sliceを見直す場合**
  - Storyを別のRelease Sliceに移動できる
  - ReleaseごとのStory数・見積もり合計を確認できる

- **9a. バージョンを作成する場合**
  - 公開済みマップを再編集する場合は、新しいバージョンとして管理する
  - 過去バージョンを参照できる

## 後置条件

- Story Map（INF-BL-13）がシステムに登録・更新される
- Activity（INF-BL-14）、User Task（INF-BL-15）、Story配置（INF-BL-16）、Release Slice（INF-BL-17）が登録・更新される
- 変更履歴（INF-BL-21）が記録される
- イベント「EVT-BL-022 Story Mapが更新された」が発行される

## 関連情報

| 情報ID | 情報名 | 操作種類 | 使用目的 |
|--------|--------|----------|----------|
| INF-BL-13 | Story Map | 作成・更新 | マップの基本情報・状態・バージョン管理 |
| INF-BL-14 | Story Map Activity | 作成・更新 | ユーザーの大きな活動 |
| INF-BL-15 | Story Map Task | 作成・更新 | Activityを構成するユーザー行動 |
| INF-BL-16 | Story Map Story | 作成・更新 | Story Map上のStory配置・順序 |
| INF-BL-17 | Story Map Release Slice | 作成・更新 | リリース単位のスライス |
| INF-BL-18 | Release Slice Story | 作成・更新 | Release SliceとStoryの関連 |
| INF-BL-01 | バックログ項目 | 参照 | 既存Storyの参照元 |
| INF-BL-21 | Story Map 変更履歴 | 作成 | 変更の追跡 |

## 関連イベント

| イベントID | イベント名 | 発生タイミング |
|------------|------------|----------------|
| EVT-BL-022 | Story Mapが更新された | 基本フローステップ9完了後 |

## 業務ルール

| ID | 業務ルール | 種類 |
|----|----------|------|
| BR-USM-01 | 1つのStory Mapは1つのProductに属する | Policy |
| BR-USM-02 | Story Mapには複数のPersonaを設定できる | Policy |
| BR-USM-03 | Activityはユーザーの大きな活動を表す | Policy |
| BR-USM-04 | User TaskはActivityを構成する具体的なユーザー行動を表す | Policy |
| BR-USM-05 | User StoryはUser Taskを実現する価値・機能を表す | Policy |
| BR-USM-06 | 1つのUser Storyを複数のUser Taskに関連付けられる | Policy |
| BR-USM-07 | 1つのUser Taskに複数のUser Storyを関連付けられる | Policy |
| BR-USM-08 | User StoryはStory MapとProduct Backlogで同一の識別子を使用する | Policy |
| BR-USM-09 | Story Map上の並び順とProduct Backlogの優先順位は別々に管理する | Policy |
| BR-USM-10 | Release Sliceは複数のUser Storyで構成される | Policy |
| BR-USM-11 | Story Map上のRelease Sliceは計画候補であり、正式なRelease Scopeとは区別する | Policy |
| BR-USM-12 | マップの変更履歴を保存し、過去の状態を確認できるようにする | Commitment |
| BR-USM-13 | Storyの削除は関連情報への影響を確認し、安易に物理削除しない | Policy |
| BR-USM-14 | Story Mapの要素とEpic・Feature・Storyの関連を追跡できるようにする | Commitment |

## 制約事項

| 番号 | 制約事項 | 説明 |
|------|----------|------|
| 1 | ドラッグ＆ドロップ | Activity、Task、Storyの並べ替えはドラッグ＆ドロップで操作 |
| 2 | 編集権限 | Story Mapの編集はプロダクトオーナーと開発者のみ可能 |
| 3 | 参照権限 | Story Mapの参照は全チームメンバー |
| 4 | Storyの参照 | Story Map専用のUser Storyを別途作成せず、Product BacklogのUser Storyを参照する |
| 5 | Release Sliceの分離 | Story MapのRelease Sliceは計画候補であり、正式なRelease Scopeとは区別する |
