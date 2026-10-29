# UC複合図

プロダクトディスカバリー業務領域で扱うユースケース、画面、イベント、情報、条件を関連づけた図です。

## 1. 目的

本書は、AG-01 プロダクトディスカバリー業務領域のシステム境界レイヤーで扱うユースケース「ステークホルダーを登録する」に関するUC複合図です。

## 2. PlantUML UC複合図（英語版）

```plantuml
title [Product Discovery] UC Composite Diagram - RegisterStakeholder
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

rectangle "Frontend" {
  usecase "SCR-003 Stakeholder" as SCR
}

rectangle "Product Discovery Module" {
  usecase "UC-DIS-014 RegisterStakeholder" as UC
}

ACT1 -- SCR
SCR ..> UC : Call
@enduml
```

## 3. 日本語版 UC複合図

```plantuml
title UC複合図（日本語版）- ステークホルダーを登録する
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

rectangle "フロントエンド" {
  usecase "SCR-003 ステークホルダー管理" as SCR
}

rectangle "プロダクトディスカバリーモジュール" {
  usecase "UC-DIS-014 ステークホルダーを登録する" as UC
}

ACT1 -- SCR
SCR ..> UC : 呼び出し
@enduml
```

## 4. ユースケース一覧

| ユースケースID | ユースケース名             | 英語名              | 説明                                                 | 関連アクター |
|----------------|----------------------------|---------------------|------------------------------------------------------|--------------|
| UC-DIS-014     | ステークホルダーを登録する | RegisterStakeholder | 関係者、関心事、影響範囲、連絡方法を整理して登録する | ACT-02       |

## 5. 画面一覧

| 画面ID  | 画面名               | 英語名      | 説明       | 関連ユースケース |
|---------|----------------------|-------------|------------|------------------|
| SCR-003 | ステークホルダー管理 | Stakeholder | 画面の説明 | UC-DIS-014       |

## 6. イベント一覧

| イベントID  | イベント名                 | 説明                                                         | 関連情報  |
|-------------|----------------------------|--------------------------------------------------------------|-----------|
| EVT-DIS-010 | ステークホルダーが登録された | 関係者、関心事、影響範囲、連絡方法が整理された               | INF-DIS-06 |

## 7. 情報一覧

| 情報ID     | 情報名       | 説明                                   | 操作種類 |
|------------|--------------|----------------------------------------|----------|
| INF-DIS-06 | ステークホルダー | 関係者、関心事、影響範囲             | 作成     |

## 8. 条件一覧

条件なし

## 9. 参照情報

- ユースケース一覧: `01.ユースケース/ユースケース一覧.md`
- 画面一覧: `04.システム/04.画面/画面一覧.md`
- イベント一覧: `04.システム/03.イベント/イベント一覧.md`
- 情報一覧: `04.システム/01.情報/情報一覧.md`
