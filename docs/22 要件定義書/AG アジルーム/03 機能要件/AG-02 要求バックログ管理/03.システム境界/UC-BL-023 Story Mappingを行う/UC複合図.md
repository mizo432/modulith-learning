# UC複合図

要求バックログ管理業務領域で扱うユースケース、画面、イベント、情報、条件を関連づけた図です。

## 1. 目的

本書は、AG-02 要求バックログ管理業務領域のシステム境界レイヤーで扱うユースケース「Story
Mappingを行う」に関するUC複合図です。

## 2. PlantUML UC複合図（英語版）

```plantuml
title [Backlog Management] UC Composite Diagram - StoryMapping
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

actor "Product Owner\n(ACT-02)" as ACT1
actor "Developer\n(ACT-04)" as ACT2

rectangle "Frontend" {
  usecase "SCR-007\nUser Story Map" as SCR
}

rectangle "Backlog Management Module" {
  usecase "UC-BL-023\nStoryMapping" as UC
}

rectangle "System Layer" {
  card "INF-BL-13\nStory Map" as INF13
  card "INF-BL-14\nStory Map Activity" as INF14
  card "INF-BL-15\nStory Map Task" as INF15
  card "INF-BL-16\nStory Map Story" as INF16
  card "INF-BL-17\nStory Map Release Slice" as INF17
  card "INF-BL-18\nRelease Slice Story" as INF18
  card "INF-BL-01\nBacklog Item" as INF01
}

ACT1 -- SCR
ACT2 -- SCR
SCR ..> UC : Call
UC -- INF13 : Create/Update
UC -- INF14 : Create/Update
UC -- INF15 : Create/Update
UC -- INF16 : Create/Update
UC -- INF17 : Create/Update
UC -- INF18 : Create/Update
UC ..> INF01 : Reference
@enduml
```

## 3. 日本語版 UC複合図

```plantuml
title UC複合図（日本語版）- Story Mappingを行う
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

actor "プロダクトオーナー\n(ACT-02)" as ACT1
actor "開発者\n(ACT-04)" as ACT2

rectangle "フロントエンド" {
  usecase "SCR-007\nユーザーストーリーマップ" as SCR
}

rectangle "要求バックログ管理モジュール" {
  usecase "UC-BL-023\nStory Mappingを行う" as UC
}

rectangle "システムレイヤー" {
  card "INF-BL-13\nStory Map" as INF13
  card "INF-BL-14\nStory Map Activity" as INF14
  card "INF-BL-15\nStory Map Task" as INF15
  card "INF-BL-16\nStory Map Story" as INF16
  card "INF-BL-17\nStory Map Release Slice" as INF17
  card "INF-BL-18\nRelease Slice Story" as INF18
  card "INF-BL-01\nバックログ項目" as INF01
}

ACT1 -- SCR
ACT2 -- SCR
SCR ..> UC : 呼び出し
UC -- INF13 : 作成・更新
UC -- INF14 : 作成・更新
UC -- INF15 : 作成・更新
UC -- INF16 : 作成・更新
UC -- INF17 : 作成・更新
UC -- INF18 : 作成・更新
UC ..> INF01 : 参照
@enduml
```

## 4. ユースケース一覧

| ユースケースID | ユースケース名      | 英語名              | 説明                                                   | 関連アクター   |
|----------------|---------------------|---------------------|--------------------------------------------------------|----------------|
| UC-BL-023      | Story Mappingを行う | StoryMapping        | Activity → User Task → User Story → Release Slice の4階層でストーリーマップを作成・編集する | ACT-02, ACT-04 |

## 5. 画面一覧

| 画面ID  | 画面名                   | 英語名                   | 説明       | 関連ユースケース |
|---------|--------------------------|--------------------------|------------|------------------|
| SCR-007 | ユーザーストーリーマップ | User Story Map           | ユーザーの活動や業務の流れを起点としてストーリーマップを作成・編集する | UC-BL-023        |

## 6. イベント一覧

| イベントID | イベント名                 | 英語名                    | 説明                                     | 関連ユースケース |
|------------|----------------------------|---------------------------|------------------------------------------|------------------|
| EVT-BL-022 | Story Mapが更新された    | Story Map Updated   | Story Mapの内容が変更・保存された | UC-BL-023 |
| EVT-BL-023 | Story Map Activityが追加された    | Story Map Activity Added   | Story MapにActivityが追加された | UC-BL-023 |
| EVT-BL-024 | Story Map Taskが追加された    | Story Map Task Added   | ActivityにTaskが追加された | UC-BL-023 |
| EVT-BL-025 | Storyがマップに配置された    | Story Placed on Map   | User StoryがStory Mapに配置された | UC-BL-023 |
| EVT-BL-026 | Release Sliceが作成された    | Release Slice Created   | Story MapにRelease Sliceが作成された | UC-BL-023 |
| EVT-BL-027 | Story Mapのバージョンが作成された    | Story Map Version Created   | Story Mapのバージョンが保存された | UC-BL-023 |

## 7. 情報一覧

| 情報ID  | 情報名       | 英語名        | 情報種類 | 説明                             | 関連ユースケース |
|---------|--------------|---------------|----------|----------------------------------|------------------|
| INF-BL-13 | Story Map | Story Map | リソース | ユーザーストーリーマップの基本情報・状態・バージョン | UC-BL-023      |
| INF-BL-14 | Story Map Activity | Story Map Activity | リソース | Story Map上のユーザーの大きな活動 | UC-BL-023      |
| INF-BL-15 | Story Map Task | Story Map Task | リソース | Activityを構成する具体的なユーザー行動 | UC-BL-023      |
| INF-BL-16 | Story Map Story | Story Map Story | 対応表 | Story MapとUser Storyの関連・配置・順序 | UC-BL-023      |
| INF-BL-17 | Story Map Release Slice | Story Map Release Slice | リソース | Story Map上のリリース単位のスライス | UC-BL-023      |
| INF-BL-18 | Release Slice Story | Release Slice Story | 対応表 | Release SliceとUser Storyの関連 | UC-BL-023      |
| INF-BL-01 | バックログ項目 | Backlog Item | リソース | 既存Storyの参照元 | UC-BL-023      |

## 8. 条件一覧

| ID | 業務ルール | 種類 |
|----|----------|------|
| BR-USM-01 | 1つのStory Mapは1つのProductに属する | Policy |
| BR-USM-02 | Story Mapには複数のPersonaを設定できる | Policy |
| BR-USM-08 | User StoryはStory MapとProduct Backlogで同一の識別子を使用する | Policy |
| BR-USM-09 | Story Map上の並び順とProduct Backlogの優先順位は別々に管理する | Policy |
| BR-USM-11 | Story Map上のRelease Sliceは計画候補であり、正式なRelease Scopeとは区別する | Policy |

## 9. 参照情報

- ユースケース一覧: `01.ユースケース/ユースケース一覧.md`
- 画面一覧: `04.システム/04.画面/画面一覧.md`
- イベント一覧: `04.システム/03.イベント/イベント一覧.md`
- 情報一覧: `04.システム/01.情報/情報一覧.md`
