# UC複合図

ジョブ実行管理業務領域で扱うユースケース、画面、イベント、情報、条件を関連づけた図です。

## 1. 目的

本書は、JB-JB-01 ジョブ実行管理業務領域のシステム境界レイヤーで扱うユースケース、画面、イベント、情報、条件を関連づけたUC複合図です。

## 2. PlantUML UC複合図（英語版）

```plantuml
@startuml
title Job Execution Management UC Composite Diagram
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

actor "Operations Admin" as Admin
actor "Business System" as BizSystem

note "Business App Server (ES-01)" as ES01
note "PostgreSQL (ES-02)" as ES02

rectangle "Frontend" {
  usecase "Dashboard" as SCR001
  usecase "Job Execution List" as SCR002
  usecase "Job Execution Detail" as SCR003
  usecase "Job Definition List" as SCR007
  usecase "Job Definition Edit" as SCR009
  usecase "Manual Job Launch" as SCR012
  usecase "Re-execution Confirm" as SCR013
  usecase "Resume Confirm" as SCR014
}

rectangle "Job Execution Management Module" {
  usecase "Register Job Definition" as UC001
  usecase "Modify Job Definition" as UC002
  usecase "Manually Launch Job" as UC008
  usecase "Launch Job via API" as UC009
  usecase "Launch Job by Schedule" as UC010
  usecase "Check Execution Status" as UC013
  usecase "Check Execution History" as UC016
  usecase "Cancel Running Job" as UC018
  usecase "Re-execute Job" as UC019
  usecase "Resume Job from Checkpoint" as UC020
  usecase "Manage User Permissions" as UC007
  usecase "Check Audit Log" as UC024
}

rectangle "System Layer" {
  card "Job Definition (INF-JB-01)" as INF01
  card "Job Deployment (INF-JB-02)" as INF02
  card "App Server (INF-JB-03)" as INF03
  card "Job Execution (INF-JB-04)" as INF04
  card "Execution Event (INF-JB-06)" as INF06
  card "Company Permission (INF-JB-07)" as INF07
  card "Store Permission (INF-JB-08)" as INF08
  card "Notification Setting (INF-JB-09)" as INF09
  card "Audit Log (INF-JB-11)" as INF11
}

Admin -- SCR001
Admin -- SCR002
Admin -- SCR003
Admin -- SCR007
Admin -- SCR009
Admin -- SCR012
Admin -- SCR013
Admin -- SCR014

SCR001 ..> UC013 : Call
SCR002 ..> UC013 : Call
SCR003 ..> UC013 : Call
SCR007 ..> UC001 : Call
SCR009 ..> UC001 : Call
SCR009 ..> UC002 : Call
SCR012 ..> UC008 : Call
SCR013 ..> UC019 : Call
SCR014 ..> UC020 : Call

BizSystem -- UC009
BizSystem -- UC017

UC001 -- INF01 : Use
UC002 -- INF01 : Use
UC008 -- INF01 : Use
UC008 -- INF04 : Use
UC009 -- INF04 : Use
UC010 -- INF04 : Use
UC013 -- INF04 : Use
UC013 -- INF06 : Use
UC016 -- INF04 : Use
UC018 -- INF04 : Use
UC019 -- INF04 : Use
UC020 -- INF04 : Use
UC007 -- INF07 : Use
UC007 -- INF08 : Use
UC024 -- INF11 : Use

UC008 ..> ES01 : Execute Request
UC009 ..> ES01 : Execute Request
UC010 ..> ES01 : Execute Request
UC019 ..> ES01 : Re-execute Request
UC020 ..> ES01 : Resume Request

UC001 ..> ES02 : Persist
UC002 ..> ES02 : Persist
UC008 ..> ES02 : Persist
UC009 ..> ES02 : Persist
UC010 ..> ES02 : Persist
UC013 ..> ES02 : Query
UC016 ..> ES02 : Query
@enduml
```

## 3. 日本語版 UC複合図

```plantuml
@startuml
title ジョブ実行管理 UC複合図（日本語版）
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

actor "運用管理者" as Admin
actor "業務システム" as BizSystem

note "業務アプリケーションサーバー (ES-01)" as ES01
note "PostgreSQL (ES-02)" as ES02

rectangle "フロントエンド" {
  usecase "ダッシュボード" as SCR001
  usecase "ジョブ実行状況一覧" as SCR002
  usecase "ジョブ実行詳細" as SCR003
  usecase "ジョブ定義一覧" as SCR007
  usecase "ジョブ定義登録・編集" as SCR009
  usecase "ジョブ手動起動画面" as SCR012
  usecase "再実行確認画面" as SCR013
  usecase "途中再開確認画面" as SCR014
}

rectangle "ジョブ実行管理モジュール" {
  usecase "ジョブ定義を登録する" as UC001
  usecase "ジョブ定義を変更する" as UC002
  usecase "ジョブを手動起動する" as UC008
  usecase "APIからジョブ起動を要求する" as UC009
  usecase "スケジュールに従ってジョブを起動する" as UC010
  usecase "実行状況を確認する" as UC013
  usecase "実行履歴を検索・参照する" as UC016
  usecase "実行中のジョブをキャンセルする" as UC018
  usecase "ジョブを再実行する" as UC019
  usecase "ジョブを途中再開する" as UC020
  usecase "ユーザー・権限を管理する" as UC007
  usecase "監査ログを確認する" as UC024
}

rectangle "システムレイヤー" {
  card "ジョブ定義 (INF-JB-01)" as INF01
  card "ジョブ配備 (INF-JB-02)" as INF02
  card "アプリケーションサーバー (INF-JB-03)" as INF03
  card "ジョブ実行 (INF-JB-04)" as INF04
  card "実行イベント (INF-JB-06)" as INF06
  card "会社権限 (INF-JB-07)" as INF07
  card "店舗権限 (INF-JB-08)" as INF08
  card "通知設定 (INF-JB-09)" as INF09
  card "監査ログ (INF-JB-11)" as INF11
}

Admin -- SCR001
Admin -- SCR002
Admin -- SCR003
Admin -- SCR007
Admin -- SCR009
Admin -- SCR012
Admin -- SCR013
Admin -- SCR014

SCR001 ..> UC013 : 呼び出し
SCR002 ..> UC013 : 呼び出し
SCR003 ..> UC013 : 呼び出し
SCR007 ..> UC001 : 呼び出し
SCR009 ..> UC001 : 呼び出し
SCR009 ..> UC002 : 呼び出し
SCR012 ..> UC008 : 呼び出し
SCR013 ..> UC019 : 呼び出し
SCR014 ..> UC020 : 呼び出し

BizSystem -- UC009

UC001 -- INF01 : 使用
UC002 -- INF01 : 使用
UC008 -- INF01 : 使用
UC008 -- INF04 : 使用
UC009 -- INF04 : 使用
UC010 -- INF04 : 使用
UC013 -- INF04 : 使用
UC013 -- INF06 : 使用
UC016 -- INF04 : 使用
UC018 -- INF04 : 使用
UC019 -- INF04 : 使用
UC020 -- INF04 : 使用
UC007 -- INF07 : 使用
UC007 -- INF08 : 使用
UC024 -- INF11 : 使用

UC008 ..> ES01 : 実行依頼
UC009 ..> ES01 : 実行依頼
UC010 ..> ES01 : 実行依頼
UC019 ..> ES01 : 再実行依頼
UC020 ..> ES01 : 再開依頼

UC001 ..> ES02 : 永続化
UC002 ..> ES02 : 永続化
UC008 ..> ES02 : 永続化
UC009 ..> ES02 : 永続化
UC010 ..> ES02 : 永続化
UC013 ..> ES02 : 照会
UC016 ..> ES02 : 照会
@enduml
```

## 4. ユースケース一覧

| ユースケースID | ユースケース名                     | 英語名                          | 説明                                                         | 関連アクター |
|----------------|------------------------------------|---------------------------------|--------------------------------------------------------------|--------------|
| UC-JB-001      | ジョブ定義を登録する               | Register Job Definition         | ジョブコード、パラメータ仕様、実行制約などを新規登録する     | AC-01        |
| UC-JB-002      | ジョブ定義を変更する               | Modify Job Definition           | 既存のジョブ定義のパラメータ・実行条件を修正する             | AC-01        |
| UC-JB-003      | ジョブ定義を無効化する             | Disable Job Definition          | ジョブ定義を無効化して新規実行を不可にする                   | AC-01        |
| UC-JB-004      | ジョブ定義を参照する               | View Job Definition             | 登録済みジョブの定義内容・バージョン・配備状況を確認する     | AC-01        |
| UC-JB-005      | サーバー配備情報を登録する         | Register Server Deployment      | 業務アプリケーションサーバーとジョブの配備情報を登録する     | AC-01        |
| UC-JB-006      | サーバー配備情報を更新する         | Update Server Deployment        | サーバー情報やジョブ配備状況・バージョンを修正する           | AC-01        |
| UC-JB-007      | ユーザー・権限を管理する           | Manage User Permissions         | ユーザーのロール、会社・店舗の参照権限を設定する             | AC-01        |
| UC-JB-008      | ジョブを手動起動する               | Manually Launch Job             | 対象ジョブ、パラメータ、会社・店舗を指定して起動する         | AC-01        |
| UC-JB-009      | APIからジョブ起動を要求する        | Launch Job via API              | 個別業務システムからAPI経由でジョブ起動を要求する            | AC-02        |
| UC-JB-010      | スケジュールに従ってジョブを起動する | Launch Job by Schedule          | 設定された実行日時・条件に従ってジョブを起動する             | AC-01        |
| UC-JB-011      | スケジュールを登録する             | Register Schedule               | 実行日時、繰り返し条件、対象ジョブ、有効期間を設定する       | AC-01        |
| UC-JB-012      | スケジュールを変更する             | Modify Schedule                 | 既存のスケジュール設定を修正する                             | AC-01        |
| UC-JB-013      | 実行状況を確認する                 | Check Execution Status          | 実行中・待機中・異常終了などの状態をリアルタイムで確認する   | AC-01        |
| UC-JB-014      | 実行詳細を確認する                 | View Execution Detail           | 実行状態、実行先、パラメータ、処理件数、エラー情報を確認する | AC-01        |
| UC-JB-015      | 実行ログ・エラー情報を確認する     | View Execution Log              | ログ、エラー内容、発生箇所、関連イベントを確認する           | AC-01        |
| UC-JB-016      | 実行履歴を検索・参照する           | Search Execution History        | 期間・ジョブ・会社・店舗・状態などで履歴を検索・参照する     | AC-01        |
| UC-JB-017      | APIから実行状態を照会する          | Query Execution Status via API  | 個別業務システムからAPI経由で実行状態・結果を照会する        | AC-02        |
| UC-JB-018      | 実行中のジョブをキャンセルする     | Cancel Running Job              | 実行中のジョブにキャンセルを要求する                         | AC-01        |
| UC-JB-019      | ジョブを再実行する                 | Re-execute Job                  | 異常終了したジョブを最初から再実行する                       | AC-01        |
| UC-JB-020      | ジョブを途中再開する               | Resume Job from Checkpoint      | チェックポイントからジョブを再開する                         | AC-01        |
| UC-JB-021      | 復旧操作履歴を確認する             | View Recovery History           | 再実行・再開・キャンセルの操作者・理由・日時を確認する       | AC-01        |
| UC-JB-022      | 通知設定を管理する                 | Manage Notification Settings    | 通知条件、通知先、通知対象ジョブを設定する                   | AC-01        |
| UC-JB-023      | 通知を確認する                     | View Notifications              | 成功・異常終了などの通知を確認する                           | AC-01        |
| UC-JB-024      | 監査ログを確認する                 | View Audit Log                  | 起動、設定変更、権限変更、復旧操作などの操作履歴を確認する   | AC-01        |
| UC-JB-025      | 会社別実行状況・履歴を参照する     | View Company Execution Status   | 自社に属するジョブの実行状況・履歴を参照する                 | AC-01        |
| UC-JB-026      | 店舗別実行状況・履歴を参照する     | View Store Execution Status     | 権限を付与された店舗の実行状況・履歴を参照する               | AC-01        |
| UC-JB-027      | 会社・店舗を管理する               | Manage Company and Store        | 会社と店舗の登録、所属関係の管理を行う                       | AC-01        |

## 5. 画面一覧

| 画面ID    | 画面名                       | 英語名                          | 説明                                                   | 関連ユースケース |
|-----------|------------------------------|---------------------------------|--------------------------------------------------------|------------------|
| SCR-JB-001 | ダッシュボード               | Dashboard                       | 実行中・成功・失敗件数、異常発生状況を表示             | UC-JB-013        |
| SCR-JB-002 | ジョブ実行状況一覧           | Job Execution List              | 実行中・待機中・異常終了などを検索・一覧表示           | UC-JB-013        |
| SCR-JB-003 | ジョブ実行詳細               | Job Execution Detail            | 実行状態、実行先、パラメータ、エラー情報を表示         | UC-JB-013, UC-JB-014 |
| SCR-JB-004 | 実行ログ・エラー詳細         | Execution Log Detail            | ログ、エラー内容、発生箇所、関連イベントを確認         | UC-JB-015        |
| SCR-JB-005 | ジョブ実行履歴一覧           | Job Execution History List      | 期間・ジョブ・会社・店舗・状態などで履歴を検索         | UC-JB-016        |
| SCR-JB-006 | ジョブ実行履歴詳細           | Job Execution History Detail    | 実行結果、実行時間、処理件数、再実行履歴を参照         | UC-JB-016        |
| SCR-JB-007 | ジョブ定義一覧               | Job Definition List             | 登録済みジョブの検索、状態、バージョンを確認           | UC-JB-004        |
| SCR-JB-008 | ジョブ定義詳細               | Job Definition Detail           | ジョブ仕様、パラメータ、同時実行数、通知設定を確認     | UC-JB-004        |
| SCR-JB-009 | ジョブ定義登録・編集         | Job Definition Edit             | ジョブコード、パラメータ仕様、実行制約などを登録・変更 | UC-JB-001, UC-JB-002 |
| SCR-JB-010 | スケジュール一覧             | Schedule List                   | スケジュールの検索、有効・無効状態を確認               | UC-JB-010        |
| SCR-JB-011 | スケジュール登録・編集       | Schedule Edit                   | 実行日時、繰り返し条件、対象ジョブ、有効期間を設定     | UC-JB-011, UC-JB-012 |
| SCR-JB-012 | ジョブ手動起動画面           | Manual Job Launch               | 対象ジョブ、実行パラメータを指定して起動               | UC-JB-008        |
| SCR-JB-013 | 再実行確認画面               | Re-execution Confirm            | 元の実行内容、再実行条件、影響、実行理由を確認         | UC-JB-019        |
| SCR-JB-014 | 途中再開確認画面             | Resume Confirm                  | 再開可否、チェックポイント、再開位置を確認             | UC-JB-020        |
| SCR-JB-015 | キャンセル確認画面           | Cancel Confirm                  | 対象実行とキャンセルの影響を確認して要求               | UC-JB-018        |
| SCR-JB-016 | 復旧操作履歴一覧             | Recovery History List           | 再実行・再開・キャンセルの操作者、理由、日時を確認     | UC-JB-021        |
| SCR-JB-017 | アプリケーションサーバー一覧 | App Server List                 | サーバーの稼働状況、接続状態、受付可否を表示           | UC-JB-005        |
| SCR-JB-018 | アプリケーションサーバー詳細 | App Server Detail               | サーバー情報、ヘルスチェック、稼働中ジョブを表示       | UC-JB-005        |
| SCR-JB-019 | ジョブ配備状況一覧           | Job Deployment List             | サーバーごとのジョブ配備状況、バージョンを確認         | UC-JB-005, UC-JB-006 |
| SCR-JB-020 | ジョブ配備詳細               | Job Deployment Detail           | ジョブとサーバーの対応関係、バージョン互換性を確認     | UC-JB-006        |
| SCR-JB-021 | 通知一覧                     | Notification List               | 成功・異常終了などの通知を確認                         | UC-JB-023        |
| SCR-JB-022 | 通知設定                     | Notification Settings           | 通知条件、通知先、通知対象ジョブを設定                 | UC-JB-022        |
| SCR-JB-023 | ユーザー一覧                 | User List                       | ユーザーの検索、利用状態を確認                         | UC-JB-007        |
| SCR-JB-024 | ユーザー・権限編集           | User Permission Edit            | ロール、会社、店舗の参照権限を設定                     | UC-JB-007        |
| SCR-JB-025 | 会社・店舗管理               | Company and Store Management    | 会社と店舗の登録、所属関係の管理                       | UC-JB-027        |
| SCR-JB-026 | 監査ログ一覧                 | Audit Log List                  | 起動、設定変更、権限変更、復旧操作などを検索・参照     | UC-JB-024        |

## 6. イベント一覧

| イベントID | イベント名                         | 英語名                          | 説明                                                         | 関連ユースケース |
|------------|------------------------------------|---------------------------------|--------------------------------------------------------------|------------------|
| EVT-JB-001 | ジョブ定義が登録された             | JobDefinitionRegistered         | ジョブコード、パラメータ仕様、実行制約などが新規登録された   | UC-JB-001        |
| EVT-JB-002 | ジョブ定義が変更された             | JobDefinitionModified           | ジョブ定義のパラメータ・実行条件が修正された                 | UC-JB-002        |
| EVT-JB-003 | ジョブ定義が無効化された           | JobDefinitionDisabled           | ジョブ定義が無効化されて新規実行が不可になった               | UC-JB-003        |
| EVT-JB-004 | サーバー配備情報が登録された       | ServerDeploymentRegistered      | 業務アプリケーションサーバーとジョブの配備情報が登録された   | UC-JB-005        |
| EVT-JB-005 | サーバー配備情報が更新された       | ServerDeploymentUpdated         | サーバー情報やジョブ配備状況・バージョンが修正された         | UC-JB-006        |
| EVT-JB-006 | ユーザー権限が設定された           | UserPermissionSet               | ユーザーのロール、会社・店舗の参照権限が設定された           | UC-JB-007        |
| EVT-JB-007 | ジョブが手動起動された             | JobManuallyLaunched             | 運用管理者が対象ジョブを指定して手動起動した                 | UC-JB-008        |
| EVT-JB-008 | API経由でジョブ起動が要求された    | JobLaunchRequestedViaAPI        | 個別業務システムからAPI経由でジョブ起動が要求された          | UC-JB-009        |
| EVT-JB-009 | スケジュール起動が実行された       | ScheduledJobLaunched            | 設定された実行日時・条件に従ってジョブが起動された           | UC-JB-010        |
| EVT-JB-010 | スケジュールが登録された           | ScheduleRegistered              | 実行日時、繰り返し条件、対象ジョブ、有効期間が設定された     | UC-JB-011        |
| EVT-JB-011 | スケジュールが変更された           | ScheduleModified                | 既存のスケジュール設定が修正された                           | UC-JB-012        |
| EVT-JB-012 | ジョブ実行が開始された             | JobExecutionStarted             | ジョブ実行が待機中から実行中に遷移した                       | UC-JB-008, UC-JB-009, UC-JB-010 |
| EVT-JB-013 | ジョブ実行が成功した               | JobExecutionSucceeded           | ジョブが正常に完了した                                       | -                |
| EVT-JB-014 | ジョブ実行が失敗した               | JobExecutionFailed              | ジョブが異常終了した                                         | -                |
| EVT-JB-015 | ジョブ実行結果が不明になった       | JobExecutionResultUnknown       | 起動APIタイムアウトなどで実行結果が判らなくなった            | -                |
| EVT-JB-016 | ジョブがキャンセルされた           | JobCancelled                    | 実行中のジョブがキャンセルされた                             | UC-JB-018        |
| EVT-JB-017 | ジョブが再実行された               | JobReExecuted                   | 異常終了したジョブが最初から再実行された                     | UC-JB-019        |
| EVT-JB-018 | ジョブが途中再開された             | JobResumedFromCheckpoint        | チェックポイントからジョブが再開された                       | UC-JB-020        |
| EVT-JB-019 | 通知が送信された                   | NotificationSent                | 成功・異常終了などの通知が送信された                         | UC-JB-022        |
| EVT-JB-020 | 会社・店舗が登録された             | CompanyStoreRegistered          | 会社と店舗の登録、所属関係が管理された                       | UC-JB-027        |

## 7. 情報一覧

| 情報ID    | 情報名                       | 英語名                      | 説明                                                   | 関連ユースケース |
|-----------|------------------------------|-----------------------------|--------------------------------------------------------|------------------|
| INF-JB-01 | ジョブ定義                   | Job Definition              | ジョブコード、パラメータ、実行条件、バージョン         | UC-JB-001〜UC-JB-004 |
| INF-JB-02 | ジョブ配備                   | Job Deployment              | サーバーごとの配備状況・バージョン                     | UC-JB-005, UC-JB-006 |
| INF-JB-03 | アプリケーションサーバー     | Application Server          | 業務アプリケーションサーバー情報                       | UC-JB-005, UC-JB-006 |
| INF-JB-04 | ジョブ実行                   | Job Execution               | 実行状態・結果・履歴                                   | UC-JB-008〜UC-JB-020 |
| INF-JB-05 | 実行枠                       | Execution Slot              | 同時実行枠の管理                                       | UC-JB-008〜UC-JB-010 |
| INF-JB-06 | 実行イベント                 | Execution Event             | 状態遷移・実行イベント                                 | UC-JB-008〜UC-JB-020 |
| INF-JB-07 | 会社権限                     | Company Permission          | 会社単位の権限                                         | UC-JB-007, UC-JB-027 |
| INF-JB-08 | 店舗権限                     | Store Permission            | 店舗単位の権限                                         | UC-JB-007, UC-JB-027 |
| INF-JB-09 | 通知設定                     | Notification Setting        | 通知情報                                               | UC-JB-022, UC-JB-023 |
| INF-JB-10 | アップロードファイル         | Uploaded File               | S3/MinIO上のファイル参照情報                           | -                |
| INF-JB-11 | 監査ログ                     | Audit Log                   | 手動起動・再実行・再開などの操作記録                   | UC-JB-024        |
| INF-JB-12 | ジョブ実行状態               | Job Execution Status        | 待機中、実行中、成功、失敗、結果不明                   | UC-JB-013        |

## 8. 条件一覧

| 条件ID   | 条件名                       | 英語名                      | 説明                                                   | 関連ユースケース |
|----------|------------------------------|-----------------------------|--------------------------------------------------------|------------------|
| RL-JB-01 | 重複起動防止                 | Idempotency Check           | 実行IDを冪等性キーとして同じ要求による二重起動を抑止   | UC-JB-008, UC-JB-009 |
| RL-JB-02 | 同時実行枠制御               | Concurrent Execution Limit  | 同時に実行できるジョブの上限数を制御                   | UC-JB-008〜UC-JB-010 |
| RL-JB-03 | 実行権限チェック             | Execution Permission Check  | ジョブ実行の権限を確認                                 | UC-JB-008, UC-JB-009 |
| RL-JB-04 | 再実行可否判定               | Re-execution Eligibility    | 再実行可能な状態か判定                                 | UC-JB-019        |
| RL-JB-05 | 途中再開可否判定             | Resume Eligibility          | チェックポイントから再開可能か判定                     | UC-JB-020        |
| RL-JB-06 | 結果不明時復旧ルール         | Unknown Result Recovery     | 結果不明時に無条件で再実行しない                       | UC-JB-019        |
| RL-JB-07 | 会社・店舗権限フィルタ       | Company Store Filter        | 会社・店舗単位で参照範囲を制限                         | UC-JB-025, UC-JB-026 |
| RL-JB-08 | バージョン互換性チェック     | Version Compatibility       | 実行時のジョブバージョンとサーバーの互換性を確認       | UC-JB-008〜UC-JB-010 |

## 9. 参照情報

- ユースケース一覧: `03.システム境界/01.ユースケース/ユースケース一覧.md`
- 画面一覧: `04.システム/04.画面/画面一覧.md`
- イベント一覧: `04.システム/03.イベント/イベント一覧.md`
- 情報一覧: `04.システム/01.情報/情報一覧.md`
- ステート一覧: `04.システム/02.ステート/ステート一覧.tsv`
- ステートマシン図: `04.システム/02.ステート/ステートマシン図.md`
