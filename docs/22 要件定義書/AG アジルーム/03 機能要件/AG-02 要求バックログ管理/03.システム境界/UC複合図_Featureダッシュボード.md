# UC複合図

Featureダッシュボードのユースケース、アクター、画面、イベント、情報、条件の関係性をPlantUMLで表現します。

## PlantUML

```plantuml
@startuml
left to right direction
skinparam packageStyle rectangle

actor "プロダクトオーナー\n(ACT-02)" as PO
actor "スクラムマスター\n(ACT-03)" as SM
actor "開発者\n(ACT-04)" as Dev
actor "トラッキング担当\n(ACT-05)" as Track

package "AG-02 要求バックログ管理" {
  usecase "UC-BL-026\nFeatureダッシュボードを\n表示する" as UC026
  usecase "UC-BL-027\nFeatureダッシュボードを\nフィルタリングする" as UC027
  usecase "UC-BL-028\nFeatureの詳細情報を\n確認する" as UC028
  usecase "UC-BL-029\nFeatureの進捗レポートを\n出力する" as UC029

  rectangle "SCR-012\nFeatureダッシュボード画面" as SCR012

  rectangle "INF-BL-01\nバックログ項目" as INF01
  rectangle "INF-BL-06\nバックログ階層" as INF06
  rectangle "INF-BL-07\n項目関連付け" as INF07
  rectangle "INF-BL-22\nFeatureダッシュボード集計" as INF22
  rectangle "INF-BL-23\nFeature品質情報" as INF23
  rectangle "INF-BL-24\nFeatureリリース情報" as INF24
}

PO --> UC026
PO --> UC027
PO --> UC028
PO --> UC029

SM --> UC026
SM --> UC027
SM --> UC028

Dev --> UC026
Dev --> UC027
Dev --> UC028

Track --> UC026
Track --> UC027
Track --> UC028
Track --> UC029

UC026 ..> SCR012 : 使用
UC027 ..> SCR012 : 使用
UC028 ..> SCR012 : 使用
UC029 ..> SCR012 : 使用

UC026 ..> INF01 : 参照
UC026 ..> INF06 : 参照
UC026 ..> INF07 : 参照
UC026 ..> INF22 : 参照
UC027 ..> INF01 : 参照
UC028 ..> INF01 : 参照
UC028 ..> INF06 : 参照
UC028 ..> INF07 : 参照
UC028 ..> INF23 : 参照
UC028 ..> INF24 : 参照
UC029 ..> INF01 : 参照
UC029 ..> INF22 : 参照

UC027 ..> UC026 : <<extend>>
UC028 ..> UC026 : <<extend>>
UC029 ..> UC026 : <<extend>>

note right of INF22
  Feature単位の
  開発進捗・価値実現・
  提供準備の集計情報
end note

note right of INF23
  Featureの
  テスト結果・欠陥数・
  品質指標
end note

note right of INF24
  Featureの
  リリース予定・
  準備状況
end note
@enduml
```

## 説明

このUC複合図は、Featureダッシュボードのユースケース、アクター、画面、情報の関係性を表現しています。

- **アクター**: PO、SM、開発者、トラッキング担当がFeatureダッシュボードを利用する
- **ユースケース**: UC-BL-026が基本ユースケースで、UC-BL-027〜029が拡張ユースケース
- **画面**: SCR-012（Featureダッシュボード画面）がすべてのユースケースで使用される
- **情報**: バックログ項目、バックログ階層、項目関連付け、Featureダッシュボード集計、Feature品質情報、Featureリリース情報を参照する
