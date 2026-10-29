# UC複合図

品質検証業務領域で扱うユースケース、画面、イベント、情報、条件を関連づけた図です。

## 1. 目的

本書は、AG-05 品質検証業務領域のシステム境界レイヤーで扱うユースケース「テストケースを更新する」に関するUC複合図です。

## 2. PlantUML UC複合図（英語版）

```plantuml
title [Quality Verification] UC Composite Diagram - UpdateTest
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

actor "Product Owner\n(ACT-02)" as ACT1
actor "Developer\n(ACT-04)" as ACT2

rectangle "Frontend" {
  usecase "SCR-020 Test" as SCR
}

rectangle "Quality Verification Module" {
  usecase "UC-QL-008 UpdateTest" as UC
}

ACT1 -- SCR
ACT2 -- SCR
SCR ..> UC : Call
@enduml
```

## 3. 日本語版 UC複合図

```plantuml
title UC複合図（日本語版）- テストケースを更新する
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

actor "プロダクトオーナー\n(ACT-02)" as ACT1
actor "開発者\n(ACT-04)" as ACT2

rectangle "フロントエンド" {
  usecase "SCR-020 シナリオテスト管理" as SCR
}

rectangle "品質検証モジュール" {
  usecase "UC-QL-008 テストケースを更新する" as UC
}

ACT1 -- SCR
ACT2 -- SCR
SCR ..> UC : 呼び出し
@enduml
```

## 4. ユースケース一覧

| ユースケースID | ユースケース名         | 英語名     | 説明                               | 関連アクター   |
|----------------|------------------------|------------|------------------------------------|----------------|
| UC-QL-008      | テストケースを更新する | UpdateTest | テストケースの詳細・手順を修正する | ACT-02, ACT-04 |

## 5. 画面一覧

| 画面ID  | 画面名             | 英語名 | 説明       | 関連ユースケース |
|---------|--------------------|--------|------------|------------------|
| SCR-020 | シナリオテスト管理 | Test   | 画面の説明 | UC-QL-008        |

## 6. イベント一覧

| イベントID | イベント名               | 説明                                                         | 関連情報  |
|------------|--------------------------|--------------------------------------------------------------|-----------|
| EVT-QL-006 | テストケースが更新された | テストケースの詳細・手順が修正された                          | INF-QL-02 |

## 7. 情報一覧

| 情報ID     | 情報名       | 説明                                   | 操作種類 |
|------------|--------------|----------------------------------------|----------|
| INF-QL-02 | テストケース | 要求や受入条件に関連づけたテストケース | 更新     |

## 8. 条件一覧

条件なし

## 9. 参照情報

- ユースケース一覧: `01.ユースケース/ユースケース一覧.md`
- 画面一覧: `04.システム/04.画面/画面一覧.md`
- イベント一覧: `04.システム/03.イベント/イベント一覧.md`
- 情報一覧: `04.システム/01.情報/情報一覧.md`
