# UC複合図

運用・監査業務領域で扱うユースケース、画面、イベント、情報、条件を関連づけた図です。

## 1. 目的

本書は、EF-05 運用・監査業務領域のシステム境界レイヤーで扱うユースケース「アーカイブ運用レポートを生成する」に関するUC複合図です。

## 2. PlantUML UC複合図（英語版）

```plantuml
title [Operations & Audit] UC Composite Diagram - GenerateArchiveOperationsReport
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

actor "Operations Admin\n(ACT-EF-04)" as ACT04

rectangle "Frontend" {
  usecase "SCR-015 Audit Log List" as SCR
}

rectangle "Operations & Audit Module" {
  usecase "UC-EF-079 GenerateArchiveOperationsReport" as UC
}

rectangle "System Layer" {
  card "INF-EF-26\nアーカイブ運用" as INF
}

ACT04 -- SCR
SCR ..> UC : Call
UC -- INF : Use
@enduml
```

## 3. 日本語版 UC複合図

```plantuml
title UC複合図（日本語版）- アーカイブ運用レポートを生成する
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

actor "運用管理者\n(ACT-EF-04)" as ACT04

rectangle "フロントエンド" {
  usecase "SCR-015 監査ログ一覧" as SCR
}

rectangle "運用・監査モジュール" {
  usecase "UC-EF-079 アーカイブ運用レポートを生成する" as UC
}

rectangle "システムレイヤー" {
  card "INF-EF-26\nアーカイブ運用" as INF
}

ACT04 -- SCR
SCR ..> UC : 呼び出し
UC -- INF : 使用
@enduml
```

## 4. ユースケース一覧

| ユースケースID | ユースケース名 | 英語名 | 説明 | 関連アクター |
|----------------|----------------|--------|------|--------------|
| UC-EF-079 | アーカイブ運用レポートを生成する | GenerateArchiveOperationsReport | 運用管理者がアーカイブ運用をアーカイブ運用レポートを生成する | ACT-EF-04 |

## 5. 画面一覧

| 画面ID | 画面名 | 英語名 | 説明 | 関連ユースケース |
|--------|--------|--------|------|------------------|
| SCR-015 | 監査ログ一覧 | Audit Log List | 画面の説明 | UC-EF-079 |

## 6. イベント一覧

| イベントID | イベント名 | 英語名 | 説明 | 関連ユースケース |
|------------|------------|--------|------|------------------|
| EVT-EF-079 | アーカイブ運用レポートが生成された | Archive Operations Report Generated | アーカイブ運用がアーカイブ運用レポートを生成する | UC-EF-079 |

## 7. 情報一覧

| 情報ID | 情報名 | 英語名 | 情報種類 | 説明 | 関連ユースケース |
|--------|--------|--------|----------|------|------------------|
| INF-EF-26 | アーカイブ運用 | アーカイブ運用 | リソース | アーカイブ運用の情報 | UC-EF-079 |

## 8. 条件一覧

条件なし

## 9. 参照情報

- ユースケース一覧: `01.ユースケース/ユースケース一覧.md`
- 画面一覧: `04.システム/04.画面/画面一覧.md`
- イベント一覧: `04.システム/03.イベント/イベント一覧.md`
- 情報一覧: `04.システム/01.情報/情報一覧.md`
