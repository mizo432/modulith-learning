# UC複合図

保管・アーカイブ業務領域で扱うユースケース、画面、イベント、情報などを関連づけたUC複合図です。

## 1. 目的

本書は、EF-04 保管・アーカイブ業務領域のシステム境界レイヤーで扱うユースケース、画面、イベント、情報、条件を関連づけたUC複合図です。

## 2. PlantUML UC複合図（英語版）

```plantuml
@startuml
title Storage & Archive UC Composite Diagram
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
actor "Document Admin (ACT-EF-05)" as ACT05
actor "Document User (ACT-EF-06)" as ACT06

rectangle "Frontend" {
  usecase "SCR-003 Document Detail & Preview" as SCR003
  usecase "SCR-008 Document Definition Edit" as SCR008
  usecase "SCR-010 Archive Dashboard" as SCR010
  usecase "SCR-011 Archive Policy Mgmt" as SCR011
  usecase "SCR-012 Archive Target List" as SCR012
  usecase "SCR-013 Restore Request List" as SCR013
}

rectangle "Storage & Archive Module" {
  usecase "UC-EF-050 Set Retention Period" as UC050
  usecase "UC-EF-051 Check Disposal Target" as UC051
  usecase "UC-EF-052 Approve Disposal" as UC052
  usecase "UC-EF-053 Execute Disposal" as UC053
  usecase "UC-EF-054 Register Archive Policy" as UC054
  usecase "UC-EF-055 Update Archive Policy" as UC055
  usecase "UC-EF-056 View Archive Policy" as UC056
  usecase "UC-EF-057 Extract Archive Target" as UC057
  usecase "UC-EF-058 Confirm Archive Target" as UC058
  usecase "UC-EF-059 Manage Archive Target" as UC059
  usecase "UC-EF-060 Execute Archive" as UC060
  usecase "UC-EF-061 Check Archive Status" as UC061
  usecase "UC-EF-062 Verify Integrity" as UC062
  usecase "UC-EF-063 Request Restore" as UC063
  usecase "UC-EF-064 Auto Restore" as UC064
  usecase "UC-EF-065 Check Restore Status" as UC065
}

note "ES-EF-01 Amazon S3" as ES01

rectangle "System Layer" {
  card "INF-EF-18\nRetention Period" as INF18
  card "INF-EF-19\nArchive Policy" as INF19
  card "INF-EF-20\nArchive Target" as INF20
  card "INF-EF-21\nArchive Status" as INF21
  card "INF-EF-22\nRestore Request" as INF22
}

ACT01 -- SCR008
ACT01 -- SCR011
ACT04 -- SCR010
ACT04 -- SCR011
ACT04 -- SCR012
ACT04 -- SCR013
ACT05 -- SCR012
ACT06 -- SCR003
ACT06 -- SCR013

SCR003 ..> UC063 : Call
SCR008 ..> UC050 : Call
SCR010 ..> UC060 : Call
SCR010 ..> UC061 : Call
SCR010 ..> UC062 : Call
SCR011 ..> UC054 : Call
SCR011 ..> UC055 : Call
SCR011 ..> UC056 : Call
SCR012 ..> UC051 : Call
SCR012 ..> UC052 : Call
SCR012 ..> UC053 : Call
SCR012 ..> UC057 : Call
SCR012 ..> UC058 : Call
SCR012 ..> UC059 : Call
SCR013 ..> UC064 : Call
SCR013 ..> UC065 : Call

UC050 -- INF18 : Use
UC051 -- INF18 : Use
UC052 -- INF18 : Use
UC053 -- INF18 : Use
UC054 -- INF19 : Use
UC055 -- INF19 : Use
UC056 -- INF19 : Use
UC057 -- INF20 : Use
UC058 -- INF20 : Use
UC059 -- INF20 : Use
UC060 -- INF21 : Use
UC061 -- INF21 : Use
UC062 -- INF21 : Use
UC063 -- INF22 : Use
UC064 -- INF22 : Use
UC065 -- INF22 : Use

UC060 ..> ES01 : Data Exchange
UC062 ..> ES01 : Data Exchange
UC064 ..> ES01 : Data Exchange
@enduml
```

## 3. 日本語版 UC複合図

```plantuml
@startuml
title 保管・アーカイブ UC複合図（日本語版）
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
actor "帳票管理者 (ACT-EF-05)" as ACT05
actor "帳票利用者 (ACT-EF-06)" as ACT06

rectangle "フロントエンド" {
  usecase "SCR-003 帳票詳細・プレビュー" as SCR003
  usecase "SCR-008 帳票定義登録・編集" as SCR008
  usecase "SCR-010 アーカイブ管理ダッシュボード" as SCR010
  usecase "SCR-011 アーカイブポリシー管理" as SCR011
  usecase "SCR-012 アーカイブ対象一覧" as SCR012
  usecase "SCR-013 復元要求一覧" as SCR013
}

rectangle "保管・アーカイブモジュール" {
  usecase "UC-EF-050 保存期間を設定する" as UC050
  usecase "UC-EF-051 廃棄対象帳票を確認する" as UC051
  usecase "UC-EF-052 廃棄を承認する" as UC052
  usecase "UC-EF-053 廃棄処理を実行する" as UC053
  usecase "UC-EF-054 アーカイブポリシーを登録する" as UC054
  usecase "UC-EF-055 アーカイブポリシーを更新する" as UC055
  usecase "UC-EF-056 アーカイブポリシーを参照する" as UC056
  usecase "UC-EF-057 アーカイブ対象を抽出する" as UC057
  usecase "UC-EF-058 アーカイブ対象を確認する" as UC058
  usecase "UC-EF-059 アーカイブ対象を管理する" as UC059
  usecase "UC-EF-060 アーカイブ処理を実行する" as UC060
  usecase "UC-EF-061 アーカイブ処理状況を確認する" as UC061
  usecase "UC-EF-062 完全性検証を行う" as UC062
  usecase "UC-EF-063 帳票復元を要求する" as UC063
  usecase "UC-EF-064 帳票を自動復元する" as UC064
  usecase "UC-EF-065 復元状況を確認する" as UC065
}

note "ES-EF-01 Amazon S3" as ES01

rectangle "システムレイヤー" {
  card "INF-EF-18\n保存期間" as INF18
  card "INF-EF-19\nアーカイブポリシー" as INF19
  card "INF-EF-20\nアーカイブ対象" as INF20
  card "INF-EF-21\nアーカイブ状況" as INF21
  card "INF-EF-22\n復元要求" as INF22
}

ACT01 -- SCR008
ACT01 -- SCR011
ACT04 -- SCR010
ACT04 -- SCR011
ACT04 -- SCR012
ACT04 -- SCR013
ACT05 -- SCR012
ACT06 -- SCR003
ACT06 -- SCR013

SCR003 ..> UC063 : 呼び出し
SCR008 ..> UC050 : 呼び出し
SCR010 ..> UC060 : 呼び出し
SCR010 ..> UC061 : 呼び出し
SCR010 ..> UC062 : 呼び出し
SCR011 ..> UC054 : 呼び出し
SCR011 ..> UC055 : 呼び出し
SCR011 ..> UC056 : 呼び出し
SCR012 ..> UC051 : 呼び出し
SCR012 ..> UC052 : 呼び出し
SCR012 ..> UC053 : 呼び出し
SCR012 ..> UC057 : 呼び出し
SCR012 ..> UC058 : 呼び出し
SCR012 ..> UC059 : 呼び出し
SCR013 ..> UC064 : 呼び出し
SCR013 ..> UC065 : 呼び出し

UC050 -- INF18 : 使用
UC051 -- INF18 : 使用
UC052 -- INF18 : 使用
UC053 -- INF18 : 使用
UC054 -- INF19 : 使用
UC055 -- INF19 : 使用
UC056 -- INF19 : 使用
UC057 -- INF20 : 使用
UC058 -- INF20 : 使用
UC059 -- INF20 : 使用
UC060 -- INF21 : 使用
UC061 -- INF21 : 使用
UC062 -- INF21 : 使用
UC063 -- INF22 : 使用
UC064 -- INF22 : 使用
UC065 -- INF22 : 使用

UC060 ..> ES01 : データ交換
UC062 ..> ES01 : データ交換
UC064 ..> ES01 : データ交換
@enduml
```

## 4. ユースケース一覧

| ユースケースID | ユースケース名                   | 英語名                          | 説明                                                       | 関連アクター                       |
|----------------|----------------------------------|---------------------------------|------------------------------------------------------------|------------------------------------|
| UC-EF-050      | 保存期間を設定する               | Set Retention Period            | 帳票種類ごとの保存期間、起算日を設定する                   | ACT-EF-01                          |
| UC-EF-051      | 廃棄対象帳票を確認する           | Check Disposal Target           | 保存期限到来の帳票を抽出・確認する                         | ACT-EF-05                          |
| UC-EF-052      | 廃棄を承認する                   | Approve Disposal                | 廃棄対象帳票を承認する                                     | ACT-EF-05                          |
| UC-EF-053      | 廃棄処理を実行する               | Execute Disposal                | 承認された帳票を完全に削除する                             | ACT-EF-04                          |
| UC-EF-054      | アーカイブポリシーを登録する     | Register Archive Policy         | 帳票種類ごとのアーカイブ条件を定義する                     | ACT-EF-01                          |
| UC-EF-055      | アーカイブポリシーを更新する     | Update Archive Policy           | 既存のアーカイブポリシーを修正する                         | ACT-EF-01                          |
| UC-EF-056      | アーカイブポリシーを参照する     | View Archive Policy             | 登録されたアーカイブポリシーを確認する                     | ACT-EF-01, ACT-EF-04               |
| UC-EF-057      | アーカイブ対象を抽出する         | Extract Archive Target          | アーカイブ条件に合致する帳票を抽出する                     | ACT-EF-04                          |
| UC-EF-058      | アーカイブ対象を確認する         | Confirm Archive Target          | 抽出されたアーカイブ対象帳票を確認する                     | ACT-EF-04                          |
| UC-EF-059      | アーカイブ対象を管理する         | Manage Archive Target           | アーカイブ対象の除外・追加を管理する                       | ACT-EF-04                          |
| UC-EF-060      | アーカイブ処理を実行する         | Execute Archive                 | S3ストレージクラスを変更してアーカイブ移行する             | ACT-EF-04                          |
| UC-EF-061      | アーカイブ処理状況を確認する     | Check Archive Status            | アーカイブ処理の進捗・結果を確認する                       | ACT-EF-04                          |
| UC-EF-062      | 完全性検証を行う                 | Verify Integrity                | アーカイブ後のデータ完全性をチェックサムで検証する         | ACT-EF-04                          |
| UC-EF-063      | 帳票復元を要求する               | Request Restore                 | アーカイブ済み帳票の復元を要求する                         | ACT-EF-06                          |
| UC-EF-064      | 帳票を自動復元する               | Auto Restore                    | 復元要求に応じ、アーカイブ済み帳票を自動復元する           | ACT-EF-04                          |
| UC-EF-065      | 復元状況を確認する               | Check Restore Status            | 復元処理の進捗・結果を確認する                             | ACT-EF-04, ACT-EF-06               |

## 5. 画面一覧

| 画面ID  | 画面名                      | 英語名                     | 主な利用者                       | 関連ユースケース |
|---------|-----------------------------|----------------------------|----------------------------------|------------------|
| SCR-003 | 帳票詳細・プレビュー        | Document Detail & Preview  | 帳票利用者                       | UC-EF-063        |
| SCR-008 | 帳票定義登録・編集          | Document Definition Edit   | システム管理者                   | UC-EF-050        |
| SCR-010 | アーカイブ管理ダッシュボード | Archive Dashboard          | 運用管理者                       | UC-EF-060〜062   |
| SCR-011 | アーカイブポリシー管理      | Archive Policy Mgmt        | システム管理者、運用管理者       | UC-EF-054〜056   |
| SCR-012 | アーカイブ対象一覧          | Archive Target List        | 運用管理者、帳票管理者           | UC-EF-051〜053, 057〜059 |
| SCR-013 | 復元要求一覧                | Restore Request List       | 運用管理者、帳票利用者           | UC-EF-064〜065   |

## 6. イベント一覧

| イベントID | イベント名                           | 英語名                              | 説明                                                         | 関連ユースケース |
|------------|--------------------------------------|-------------------------------------|--------------------------------------------------------------|------------------|
| EVT-EF-034 | 保存期間が設定された                 | Retention Period Set                | 帳票種類ごとの保存期間、起算日が設定された                   | UC-EF-050        |
| EVT-EF-035 | 廃棄が承認された                     | Disposal Approved                   | 廃棄対象帳票が承認された                                     | UC-EF-052        |
| EVT-EF-036 | 廃棄処理が実行された                 | Disposal Executed                   | 承認された帳票が完全に削除された                             | UC-EF-053        |
| EVT-EF-037 | アーカイブポリシーが登録された       | Archive Policy Registered           | 帳票種類ごとのアーカイブ条件が定義された                     | UC-EF-054        |
| EVT-EF-038 | アーカイブポリシーが更新された       | Archive Policy Updated              | 既存のアーカイブポリシーが修正された                         | UC-EF-055        |
| EVT-EF-039 | アーカイブ処理が実行された           | Archive Executed                    | S3ストレージクラスが変更されてアーカイブ移行された           | UC-EF-060        |
| EVT-EF-040 | 完全性検証が行われた                 | Integrity Verified                  | アーカイブ後のデータ完全性がチェックサムで検証された         | UC-EF-062        |
| EVT-EF-041 | 帳票復元が要求された                 | Restore Requested                   | アーカイブ済み帳票の復元が要求された                         | UC-EF-063        |
| EVT-EF-042 | 帳票が自動復元された                 | Document Auto Restored              | アーカイブ済み帳票が自動復元された                           | UC-EF-064        |

## 7. 情報一覧

| 情報ID    | 情報名             | 英語名               | 情報種類 | 説明                                           | 関連ユースケース |
|-----------|-------------------|----------------------|----------|------------------------------------------------|------------------|
| INF-EF-18 | 保存期間         | Retention Period     | リソース | 帳票種類ごとの保存期間、起算日                 | UC-EF-050〜053   |
| INF-EF-19 | アーカイブポリシー | Archive Policy       | リソース | 帳票種類ごとのアーカイブ条件                   | UC-EF-054〜056   |
| INF-EF-20 | アーカイブ対象   | Archive Target       | リソース | アーカイブ対象帳票のリスト                     | UC-EF-057〜059   |
| INF-EF-21 | アーカイブ状況   | Archive Status       | リソース | アーカイブ処理の進捗・結果                     | UC-EF-060〜062   |
| INF-EF-22 | 復元要求         | Restore Request      | リソース | アーカイブ済み帳票の復元要求                   | UC-EF-063〜065   |

## 8. 参照情報

- ユースケース一覧: `01.ユースケース/ユースケース一覧.md`
- 画面一覧: `04.システム/04.画面/画面一覧.md`
- イベント一覧: `04.システム/03.イベント/イベント一覧.md`
- 情報一覧: `04.システム/01.情報/情報一覧.md`
- 外部システム一覧: `02.外部環境/01.外部システム定義/外部システム一覧.md`
