# UC複合図

帳票利用業務領域で扱うユースケース、画面、イベント、情報などを関連づけたUC複合図です。

## 1. 目的

本書は、EF-03 帳票利用業務領域のシステム境界レイヤーで扱うユースケース、画面、イベント、情報、条件を関連づけたUC複合図です。

## 2. PlantUML UC複合図（英語版）

```plantuml
@startuml
title Document Usage UC Composite Diagram
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

actor "Document Admin (ACT-EF-05)" as ACT05
actor "Document User (ACT-EF-06)" as ACT06
actor "Approver (ACT-EF-07)" as ACT07

rectangle "Frontend" {
  usecase "SCR-001 Dashboard" as SCR001
  usecase "SCR-002 Document List" as SCR002
  usecase "SCR-003 Document Detail & Preview" as SCR003
  usecase "SCR-004 Document Search" as SCR004
  usecase "SCR-005 Approval Pending List" as SCR005
  usecase "SCR-006 Approval Detail" as SCR006
}

rectangle "Document Usage Module" {
  usecase "UC-EF-035 Distribute Document" as UC035
  usecase "UC-EF-036 Set Distribution Target" as UC036
  usecase "UC-EF-037 Check Distribution Status" as UC037
  usecase "UC-EF-038 Preview Document" as UC038
  usecase "UC-EF-039 Navigate Pages" as UC039
  usecase "UC-EF-040 Zoom Document" as UC040
  usecase "UC-EF-041 Download Document" as UC041
  usecase "UC-EF-042 Print Document" as UC042
  usecase "UC-EF-043 Keyword Search" as UC043
  usecase "UC-EF-044 Condition Search" as UC044
  usecase "UC-EF-045 View Document List" as UC045
  usecase "UC-EF-046 Check Approval Pending" as UC046
  usecase "UC-EF-047 Approve Document" as UC047
  usecase "UC-EF-048 Return Document" as UC048
  usecase "UC-EF-049 Reject Document" as UC049
}

note "ES-EF-01 Amazon S3" as ES01
note "ES-EF-02 MinIO" as ES02
note "ES-EF-05 LibreOffice" as ES05

rectangle "System Layer" {
  card "INF-EF-14\nDocument Distribution" as INF14
  card "INF-EF-15\nDocument Preview" as INF15
  card "INF-EF-16\nSearch Result" as INF16
  card "INF-EF-17\nApproval Status" as INF17
}

ACT05 -- SCR001
ACT06 -- SCR001
ACT06 -- SCR002
ACT06 -- SCR003
ACT06 -- SCR004
ACT07 -- SCR005
ACT07 -- SCR006

SCR001 ..> UC035 : Call
SCR001 ..> UC036 : Call
SCR001 ..> UC037 : Call
SCR002 ..> UC045 : Call
SCR003 ..> UC038 : Call
SCR003 ..> UC039 : Call
SCR003 ..> UC040 : Call
SCR003 ..> UC041 : Call
SCR003 ..> UC042 : Call
SCR004 ..> UC043 : Call
SCR004 ..> UC044 : Call
SCR005 ..> UC046 : Call
SCR006 ..> UC047 : Call
SCR006 ..> UC048 : Call
SCR006 ..> UC049 : Call

UC035 -- INF14 : Use
UC036 -- INF14 : Use
UC037 -- INF14 : Use
UC038 -- INF15 : Use
UC039 -- INF15 : Use
UC040 -- INF15 : Use
UC041 -- INF15 : Use
UC042 -- INF15 : Use
UC043 -- INF16 : Use
UC044 -- INF16 : Use
UC045 -- INF16 : Use
UC046 -- INF17 : Use
UC047 -- INF17 : Use
UC048 -- INF17 : Use
UC049 -- INF17 : Use

UC038 ..> ES01 : Data Exchange
UC041 ..> ES01 : Data Exchange
UC038 ..> ES05 : Data Exchange
@enduml
```

## 3. 日本語版 UC複合図

```plantuml
@startuml
title 帳票利用 UC複合図（日本語版）
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

actor "帳票管理者 (ACT-EF-05)" as ACT05
actor "帳票利用者 (ACT-EF-06)" as ACT06
actor "承認者 (ACT-EF-07)" as ACT07

rectangle "フロントエンド" {
  usecase "SCR-001 ダッシュボード" as SCR001
  usecase "SCR-002 帳票一覧" as SCR002
  usecase "SCR-003 帳票詳細・プレビュー" as SCR003
  usecase "SCR-004 帳票検索" as SCR004
  usecase "SCR-005 承認待ち一覧" as SCR005
  usecase "SCR-006 承認詳細" as SCR006
}

rectangle "帳票利用モジュール" {
  usecase "UC-EF-035 帳票を配信する" as UC035
  usecase "UC-EF-036 配信先を設定する" as UC036
  usecase "UC-EF-037 配信状況を確認する" as UC037
  usecase "UC-EF-038 帳票をプレビューする" as UC038
  usecase "UC-EF-039 帳票をページ移動する" as UC039
  usecase "UC-EF-040 帳票を拡大・縮小する" as UC040
  usecase "UC-EF-041 帳票をダウンロードする" as UC041
  usecase "UC-EF-042 帳票を印刷する" as UC042
  usecase "UC-EF-043 帳票をキーワード検索する" as UC043
  usecase "UC-EF-044 帳票を条件検索する" as UC044
  usecase "UC-EF-045 帳票一覧を表示する" as UC045
  usecase "UC-EF-046 承認待ち帳票を確認する" as UC046
  usecase "UC-EF-047 帳票を承認する" as UC047
  usecase "UC-EF-048 帳票を差戻しする" as UC048
  usecase "UC-EF-049 帳票を却下する" as UC049
}

note "ES-EF-01 Amazon S3" as ES01
note "ES-EF-02 MinIO" as ES02
note "ES-EF-05 LibreOffice" as ES05

rectangle "システムレイヤー" {
  card "INF-EF-14\n帳票配信" as INF14
  card "INF-EF-15\n帳票プレビュー" as INF15
  card "INF-EF-16\n検索結果" as INF16
  card "INF-EF-17\n承認状況" as INF17
}

ACT05 -- SCR001
ACT06 -- SCR001
ACT06 -- SCR002
ACT06 -- SCR003
ACT06 -- SCR004
ACT07 -- SCR005
ACT07 -- SCR006

SCR001 ..> UC035 : 呼び出し
SCR001 ..> UC036 : 呼び出し
SCR001 ..> UC037 : 呼び出し
SCR002 ..> UC045 : 呼び出し
SCR003 ..> UC038 : 呼び出し
SCR003 ..> UC039 : 呼び出し
SCR003 ..> UC040 : 呼び出し
SCR003 ..> UC041 : 呼び出し
SCR003 ..> UC042 : 呼び出し
SCR004 ..> UC043 : 呼び出し
SCR004 ..> UC044 : 呼び出し
SCR005 ..> UC046 : 呼び出し
SCR006 ..> UC047 : 呼び出し
SCR006 ..> UC048 : 呼び出し
SCR006 ..> UC049 : 呼び出し

UC035 -- INF14 : 使用
UC036 -- INF14 : 使用
UC037 -- INF14 : 使用
UC038 -- INF15 : 使用
UC039 -- INF15 : 使用
UC040 -- INF15 : 使用
UC041 -- INF15 : 使用
UC042 -- INF15 : 使用
UC043 -- INF16 : 使用
UC044 -- INF16 : 使用
UC045 -- INF16 : 使用
UC046 -- INF17 : 使用
UC047 -- INF17 : 使用
UC048 -- INF17 : 使用
UC049 -- INF17 : 使用

UC038 ..> ES01 : データ交換
UC041 ..> ES01 : データ交換
UC038 ..> ES05 : データ交換
@enduml
```

## 4. ユースケース一覧

| ユースケースID | ユースケース名                   | 英語名                          | 説明                                                       | 関連アクター                       |
|----------------|----------------------------------|---------------------------------|------------------------------------------------------------|------------------------------------|
| UC-EF-035      | 帳票を配信する                   | Distribute Document             | 利用者・組織・取引先へ帳票を配信する                       | ACT-EF-05                          |
| UC-EF-036      | 配信先を設定する                 | Set Distribution Target         | 帳票の配信先（利用者・組織）を設定する                     | ACT-EF-05                          |
| UC-EF-037      | 配信状況を確認する               | Check Distribution Status       | 帳票の配信状況を確認する                                   | ACT-EF-05                          |
| UC-EF-038      | 帳票をプレビューする             | Preview Document                | PDF・Office文書をブラウザでプレビュー表示する              | ACT-EF-06                          |
| UC-EF-039      | 帳票をページ移動する             | Navigate Pages                  | プレビューでページを移動する                               | ACT-EF-06                          |
| UC-EF-040      | 帳票を拡大・縮小する             | Zoom Document                   | プレビューで表示倍率を変更する                             | ACT-EF-06                          |
| UC-EF-041      | 帳票をダウンロードする           | Download Document               | 帳票ファイルをダウンロードする                             | ACT-EF-06                          |
| UC-EF-042      | 帳票を印刷する                   | Print Document                  | 帳票を印刷する                                             | ACT-EF-06                          |
| UC-EF-043      | 帳票をキーワード検索する         | Keyword Search                  | キーワードで帳票を検索する                                 | ACT-EF-06                          |
| UC-EF-044      | 帳票を条件検索する               | Condition Search                | 業務システム、帳票種類、発行日などの条件で検索する         | ACT-EF-06                          |
| UC-EF-045      | 帳票一覧を表示する               | View Document List              | 閲覧可能な帳票を一覧表示する                               | ACT-EF-06                          |
| UC-EF-046      | 承認待ち帳票を確認する           | Check Approval Pending          | 承認待ちの帳票一覧を確認する                               | ACT-EF-07                          |
| UC-EF-047      | 帳票を承認する                   | Approve Document                | 帳票の内容を確認して承認する                               | ACT-EF-07                          |
| UC-EF-048      | 帳票を差戻しする                 | Return Document                 | 帳票を申請者に差戻しする                                   | ACT-EF-07                          |
| UC-EF-049      | 帳票を却下する                   | Reject Document                 | 帳票を却下する                                             | ACT-EF-07                          |

## 5. 画面一覧

| 画面ID  | 画面名                      | 英語名                     | 主な利用者                       | 関連ユースケース |
|---------|-----------------------------|----------------------------|----------------------------------|------------------|
| SCR-001 | ダッシュボード              | Dashboard                  | 帳票管理者、帳票利用者           | UC-EF-035〜037   |
| SCR-002 | 帳票一覧                    | Document List              | 帳票利用者                       | UC-EF-045        |
| SCR-003 | 帳票詳細・プレビュー        | Document Detail & Preview  | 帳票利用者                       | UC-EF-038〜042   |
| SCR-004 | 帳票検索                    | Document Search            | 帳票利用者                       | UC-EF-043〜044   |
| SCR-005 | 承認待ち一覧                | Approval Pending List      | 承認者                           | UC-EF-046        |
| SCR-006 | 承認詳細                    | Approval Detail            | 承認者                           | UC-EF-047〜049   |

## 6. イベント一覧

| イベントID | イベント名                           | 英語名                              | 説明                                                         | 関連ユースケース |
|------------|--------------------------------------|-------------------------------------|--------------------------------------------------------------|------------------|
| EVT-EF-025 | 帳票が配信された                     | Document Distributed                | 利用者・組織・取引先へ帳票が配信された                       | UC-EF-035        |
| EVT-EF-026 | 配信先が設定された                   | Distribution Target Set             | 帳票の配信先が設定された                                     | UC-EF-036        |
| EVT-EF-027 | 帳票がプレビューされた               | Document Previewed                  | 帳票がブラウザでプレビュー表示された                         | UC-EF-038        |
| EVT-EF-028 | 帳票がダウンロードされた             | Document Downloaded                 | 帳票ファイルがダウンロードされた                             | UC-EF-041        |
| EVT-EF-029 | 帳票が印刷された                     | Document Printed                    | 帳票が印刷された                                             | UC-EF-042        |
| EVT-EF-030 | 帳票が検索された                     | Document Searched                   | 帳票が検索された                                             | UC-EF-043, 044   |
| EVT-EF-031 | 帳票が承認された                     | Document Approved                   | 帳票が承認された                                             | UC-EF-047        |
| EVT-EF-032 | 帳票が差戻しされた                   | Document Returned                   | 帳票が申請者に差戻しされた                                   | UC-EF-048        |
| EVT-EF-033 | 帳票が却下された                     | Document Rejected                   | 帳票が却下された                                             | UC-EF-049        |

## 7. 情報一覧

| 情報ID    | 情報名             | 英語名               | 情報種類 | 説明                                           | 関連ユースケース |
|-----------|-------------------|----------------------|----------|------------------------------------------------|------------------|
| INF-EF-14 | 帳票配信         | Document Distribution | リソース | 帳票の配信先、配信状況                         | UC-EF-035〜037   |
| INF-EF-15 | 帳票プレビュー   | Document Preview     | リソース | 帳票のプレビュー表示情報                       | UC-EF-038〜042   |
| INF-EF-16 | 検索結果         | Search Result        | リソース | 帳票検索の結果                                 | UC-EF-043〜045   |
| INF-EF-17 | 承認状況         | Approval Status      | リソース | 帳票の承認・差戻し・却下状況                   | UC-EF-046〜049   |

## 8. 参照情報

- ユースケース一覧: `01.ユースケース/ユースケース一覧.md`
- 画面一覧: `04.システム/04.画面/画面一覧.md`
- イベント一覧: `04.システム/03.イベント/イベント一覧.md`
- 情報一覧: `04.システム/01.情報/情報一覧.md`
- 外部システム一覧: `02.外部環境/01.外部システム定義/外部システム一覧.md`
