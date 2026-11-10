# UC複合図

運用・監査業務領域で扱うユースケース、画面、イベント、情報などを関連づけたUC複合図です。

## 1. 目的

本書は、EF-05 運用・監査業務領域のシステム境界レイヤーで扱うユースケース、画面、イベント、情報、条件を関連づけたUC複合図です。

## 2. PlantUML UC複合図（英語版）

```plantuml
@startuml
title Operations & Audit UC Composite Diagram
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

actor "System Admin (ACT-EF-01)" as ACT01
actor "Operations Admin (ACT-EF-04)" as ACT04
actor "Auditor (ACT-EF-08)" as ACT08
actor "Manager (ACT-EF-09)" as ACT09

rectangle "Frontend" {
  usecase "SCR-009 Organization & User Mgmt" as SCR009
  usecase "SCR-010 Archive Dashboard" as SCR010
  usecase "SCR-014 Operations Error List" as SCR014
  usecase "SCR-015 Audit Log List" as SCR015
  usecase "SCR-016 Document Analytics Dashboard" as SCR016
}

rectangle "Operations & Audit Module" {
  usecase "UC-EF-066 Register Notification Settings" as UC066
  usecase "UC-EF-067 Update Notification Settings" as UC067
  usecase "UC-EF-068 View Notification Settings" as UC068
  usecase "UC-EF-069 View Audit Log" as UC069
  usecase "UC-EF-070 Search Audit Log" as UC070
  usecase "UC-EF-071 Export Audit Log" as UC071
  usecase "UC-EF-072 Check Processing Status" as UC072
  usecase "UC-EF-073 Check Operations Error" as UC073
  usecase "UC-EF-074 Retry Error Processing" as UC074
  usecase "UC-EF-075 View Analytics Dashboard" as UC075
  usecase "UC-EF-076 Generate Analytics Report" as UC076
  usecase "UC-EF-077 Check Storage Usage" as UC077
  usecase "UC-EF-078 Monitor Archive Processing" as UC078
  usecase "UC-EF-079 Check Archive Error" as UC079
  usecase "UC-EF-080 Retry Archive Processing" as UC080
}

note "ES-EF-03 Mail Server" as ES03
note "ES-EF-06 Webhook Endpoint" as ES06

rectangle "System Layer" {
  card "INF-EF-23\nNotification Settings" as INF23
  card "INF-EF-24\nAudit Log" as INF24
  card "INF-EF-25\nProcessing Status" as INF25
  card "INF-EF-26\nAnalytics Data" as INF26
  card "INF-EF-27\nArchive Monitoring" as INF27
}

ACT01 -- SCR009
ACT04 -- SCR010
ACT04 -- SCR014
ACT08 -- SCR015
ACT09 -- SCR016

SCR009 ..> UC066 : Call
SCR009 ..> UC067 : Call
SCR009 ..> UC068 : Call
SCR010 ..> UC072 : Call
SCR010 ..> UC078 : Call
SCR014 ..> UC073 : Call
SCR014 ..> UC074 : Call
SCR014 ..> UC079 : Call
SCR014 ..> UC080 : Call
SCR015 ..> UC069 : Call
SCR015 ..> UC070 : Call
SCR015 ..> UC071 : Call
SCR016 ..> UC075 : Call
SCR016 ..> UC076 : Call
SCR016 ..> UC077 : Call

UC066 -- INF23 : Use
UC067 -- INF23 : Use
UC068 -- INF23 : Use
UC069 -- INF24 : Use
UC070 -- INF24 : Use
UC071 -- INF24 : Use
UC072 -- INF25 : Use
UC073 -- INF25 : Use
UC074 -- INF25 : Use
UC075 -- INF26 : Use
UC076 -- INF26 : Use
UC077 -- INF26 : Use
UC078 -- INF27 : Use
UC079 -- INF27 : Use
UC080 -- INF27 : Use

UC066 ..> ES03 : Data Exchange
UC067 ..> ES03 : Data Exchange
UC066 ..> ES06 : Data Exchange
UC067 ..> ES06 : Data Exchange
@enduml
```

## 3. 日本語版 UC複合図

```plantuml
@startuml
title 運用・監査 UC複合図（日本語版）
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

actor "システム管理者 (ACT-EF-01)" as ACT01
actor "運用管理者 (ACT-EF-04)" as ACT04
actor "監査担当者 (ACT-EF-08)" as ACT08
actor "管理者 (ACT-EF-09)" as ACT09

rectangle "フロントエンド" {
  usecase "SCR-009 組織・利用者管理" as SCR009
  usecase "SCR-010 アーカイブ管理ダッシュボード" as SCR010
  usecase "SCR-014 運用エラー一覧" as SCR014
  usecase "SCR-015 監査ログ一覧" as SCR015
  usecase "SCR-016 帳票分析ダッシュボード" as SCR016
}

rectangle "運用・監査モジュール" {
  usecase "UC-EF-066 通知設定を登録する" as UC066
  usecase "UC-EF-067 通知設定を更新する" as UC067
  usecase "UC-EF-068 通知設定を参照する" as UC068
  usecase "UC-EF-069 監査ログを参照する" as UC069
  usecase "UC-EF-070 監査ログを検索する" as UC070
  usecase "UC-EF-071 監査ログをエクスポートする" as UC071
  usecase "UC-EF-072 処理状況を確認する" as UC072
  usecase "UC-EF-073 運用エラーを確認する" as UC073
  usecase "UC-EF-074 エラー処理を再実行する" as UC074
  usecase "UC-EF-075 帳票分析ダッシュボードを表示する" as UC075
  usecase "UC-EF-076 帳票分析レポートを生成する" as UC076
  usecase "UC-EF-077 ストレージ使用量を確認する" as UC077
  usecase "UC-EF-078 アーカイブ処理を監視する" as UC078
  usecase "UC-EF-079 アーカイブエラーを確認する" as UC079
  usecase "UC-EF-080 アーカイブ処理を再実行する" as UC080
}

note "ES-EF-03 メールサーバー" as ES03
note "ES-EF-06 Webhookエンドポイント" as ES06

rectangle "システムレイヤー" {
  card "INF-EF-23\n通知設定" as INF23
  card "INF-EF-24\n監査ログ" as INF24
  card "INF-EF-25\n処理状況" as INF25
  card "INF-EF-26\n分析データ" as INF26
  card "INF-EF-27\nアーカイブ監視" as INF27
}

ACT01 -- SCR009
ACT04 -- SCR010
ACT04 -- SCR014
ACT08 -- SCR015
ACT09 -- SCR016

SCR009 ..> UC066 : 呼び出し
SCR009 ..> UC067 : 呼び出し
SCR009 ..> UC068 : 呼び出し
SCR010 ..> UC072 : 呼び出し
SCR010 ..> UC078 : 呼び出し
SCR014 ..> UC073 : 呼び出し
SCR014 ..> UC074 : 呼び出し
SCR014 ..> UC079 : 呼び出し
SCR014 ..> UC080 : 呼び出し
SCR015 ..> UC069 : 呼び出し
SCR015 ..> UC070 : 呼び出し
SCR015 ..> UC071 : 呼び出し
SCR016 ..> UC075 : 呼び出し
SCR016 ..> UC076 : 呼び出し
SCR016 ..> UC077 : 呼び出し

UC066 -- INF23 : 使用
UC067 -- INF23 : 使用
UC068 -- INF23 : 使用
UC069 -- INF24 : 使用
UC070 -- INF24 : 使用
UC071 -- INF24 : 使用
UC072 -- INF25 : 使用
UC073 -- INF25 : 使用
UC074 -- INF25 : 使用
UC075 -- INF26 : 使用
UC076 -- INF26 : 使用
UC077 -- INF26 : 使用
UC078 -- INF27 : 使用
UC079 -- INF27 : 使用
UC080 -- INF27 : 使用

UC066 ..> ES03 : データ交換
UC067 ..> ES03 : データ交換
UC066 ..> ES06 : データ交換
UC067 ..> ES06 : データ交換
@enduml
```

## 4. ユースケース一覧

| ユースケースID | ユースケース名                   | 英語名                          | 説明                                                       | 関連アクター                       |
|----------------|----------------------------------|---------------------------------|------------------------------------------------------------|------------------------------------|
| UC-EF-066      | 通知設定を登録する               | Register Notification Settings  | メール、システム内通知、Webhook通知の設定を登録する         | ACT-EF-01                          |
| UC-EF-067      | 通知設定を更新する               | Update Notification Settings    | 既存の通知設定を修正する                                   | ACT-EF-01                          |
| UC-EF-068      | 通知設定を参照する               | View Notification Settings      | 登録された通知設定を確認する                               | ACT-EF-01                          |
| UC-EF-069      | 監査ログを参照する               | View Audit Log                  | 操作履歴、閲覧履歴、ダウンロード履歴を確認する             | ACT-EF-08                          |
| UC-EF-070      | 監査ログを検索する               | Search Audit Log                | 利用者、操作種別、日時などの条件で監査ログを検索する       | ACT-EF-08                          |
| UC-EF-071      | 監査ログをエクスポートする       | Export Audit Log                | 監査ログをエクスポートする                                 | ACT-EF-08                          |
| UC-EF-072      | 処理状況を確認する               | Check Processing Status         | バッチ処理、アーカイブ処理などの進捗を確認する             | ACT-EF-04                          |
| UC-EF-073      | 運用エラーを確認する             | Check Operations Error          | 発生したエラーの内容・回数を確認する                       | ACT-EF-04                          |
| UC-EF-074      | エラー処理を再実行する           | Retry Error Processing          | 失敗した処理を再実行する                                   | ACT-EF-04                          |
| UC-EF-075      | 帳票分析ダッシュボードを表示する | View Analytics Dashboard        | 帳票件数、配信状況、利用状況の統計を表示する               | ACT-EF-09                          |
| UC-EF-076      | 帳票分析レポートを生成する       | Generate Analytics Report       | 集計期間を指定して分析レポートを生成する                   | ACT-EF-09                          |
| UC-EF-077      | ストレージ使用量を確認する       | Check Storage Usage             | 通常・アーカイブ・プレビューのストレージ使用量を確認する   | ACT-EF-09                          |
| UC-EF-078      | アーカイブ処理を監視する         | Monitor Archive Processing      | アーカイブ・復元処理の進捗・状態を監視する                 | ACT-EF-04                          |
| UC-EF-079      | アーカイブエラーを確認する       | Check Archive Error             | アーカイブ・復元処理で発生したエラーを確認する             | ACT-EF-04                          |
| UC-EF-080      | アーカイブ処理を再実行する       | Retry Archive Processing        | 失敗したアーカイブ・復元処理を再実行する                   | ACT-EF-04                          |

## 5. 画面一覧

| 画面ID  | 画面名                      | 英語名                     | 主な利用者                       | 関連ユースケース |
|---------|-----------------------------|----------------------------|----------------------------------|------------------|
| SCR-009 | 組織・利用者管理            | Organization & User Mgmt   | システム管理者                   | UC-EF-066〜068   |
| SCR-010 | アーカイブ管理ダッシュボード | Archive Dashboard          | 運用管理者                       | UC-EF-072, 078   |
| SCR-014 | 運用エラー一覧              | Operations Error List      | 運用管理者                       | UC-EF-073〜074, 079〜080 |
| SCR-015 | 監査ログ一覧                | Audit Log List             | 監査担当者                       | UC-EF-069〜071   |
| SCR-016 | 帳票分析ダッシュボード      | Document Analytics Dashboard | 管理者                         | UC-EF-075〜077   |

## 6. イベント一覧

| イベントID | イベント名                           | 英語名                              | 説明                                                         | 関連ユースケース |
|------------|--------------------------------------|-------------------------------------|--------------------------------------------------------------|------------------|
| EVT-EF-043 | 通知設定が登録された                 | Notification Settings Registered    | メール、システム内通知、Webhook通知の設定が登録された         | UC-EF-066        |
| EVT-EF-044 | 通知設定が更新された                 | Notification Settings Updated       | 既存の通知設定が修正された                                   | UC-EF-067        |
| EVT-EF-045 | 監査ログが参照された                 | Audit Log Viewed                    | 操作履歴、閲覧履歴、ダウンロード履歴が確認された             | UC-EF-069        |
| EVT-EF-046 | 監査ログがエクスポートされた         | Audit Log Exported                  | 監査ログがエクスポートされた                                 | UC-EF-071        |
| EVT-EF-047 | エラー処理が再実行された             | Error Processing Retried            | 失敗した処理が再実行された                                   | UC-EF-074        |
| EVT-EF-048 | 帳票分析レポートが生成された         | Analytics Report Generated          | 集計期間を指定して分析レポートが生成された                   | UC-EF-076        |
| EVT-EF-049 | アーカイブ処理が再実行された         | Archive Processing Retried          | 失敗したアーカイブ・復元処理が再実行された                   | UC-EF-080        |

## 7. 情報一覧

| 情報ID    | 情報名             | 英語名               | 情報種類 | 説明                                           | 関連ユースケース |
|-----------|-------------------|----------------------|----------|------------------------------------------------|------------------|
| INF-EF-23 | 通知設定         | Notification Settings | リソース | メール、システム内通知、Webhook通知の設定       | UC-EF-066〜068   |
| INF-EF-24 | 監査ログ         | Audit Log            | リソース | 操作履歴、閲覧履歴、ダウンロード履歴           | UC-EF-069〜071   |
| INF-EF-25 | 処理状況         | Processing Status    | リソース | バッチ処理、アーカイブ処理などの進捗           | UC-EF-072〜074   |
| INF-EF-26 | 分析データ       | Analytics Data       | リソース | 帳票件数、配信状況、利用状況の統計             | UC-EF-075〜077   |
| INF-EF-27 | アーカイブ監視   | Archive Monitoring   | リソース | アーカイブ・復元処理の進捗・状態               | UC-EF-078〜080   |

## 8. 参照情報

- ユースケース一覧: `01.ユースケース/ユースケース一覧.md`
- 画面一覧: `04.システム/04.画面/画面一覧.md`
- イベント一覧: `04.システム/03.イベント/イベント一覧.md`
- 情報一覧: `04.システム/01.情報/情報一覧.md`
- 外部システム一覧: `02.外部環境/01.外部システム定義/外部システム一覧.md`
