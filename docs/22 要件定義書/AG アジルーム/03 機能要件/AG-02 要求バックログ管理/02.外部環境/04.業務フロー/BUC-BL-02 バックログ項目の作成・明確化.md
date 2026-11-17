# BUC-BL-02 バックログ項目の作成・明確化

## 概要

課題や仮説をEpic・Feature・Storyに変換し、受入条件を明確にする。

## アクティビティ図

```plantuml
@startuml
skinparam activity {
  BackgroundColor White
  BorderColor Black
}

start

:課題や仮説を確認する;
:バックログ項目の種類を決定する;

if (項目の種類は?) then (Epic)
  :Epicを作成する;
  :大枠の範囲を定義する;
else (Feature)
  :Featureを作成する;
  :機能の範囲を定義する;
else (Story)
  :Storyを作成する;
  :ユーザー価値を明確にする;
  endif

:受入条件を定義する;
:見積もりを行う;
:依存関係を特定する;
:バックログ項目を記録する;
:関係者に共有する;

stop
@enduml
```

## アクティビティ一覧

| 番号   | アクティビティ名 | 説明                       | 担当アクター   |
|--------|------------------|----------------------------|----------------|
| ACT-01 | 課題確認         | 課題や仮説を確認           | ACT-02, ACT-04 |
| ACT-02 | 種類決定         | バックログ項目の種類を決定 | ACT-02, ACT-04 |
| ACT-03 | Epic作成         | Epicを作成                 | ACT-02, ACT-04 |
| ACT-04 | Feature作成      | Featureを作成              | ACT-02, ACT-04 |
| ACT-05 | Story作成        | Storyを作成                | ACT-02, ACT-04 |
| ACT-06 | 受入条件定義     | 受入条件を定義             | ACT-02, ACT-04 |
| ACT-07 | 見積もり         | 見積もりを行う             | ACT-02, ACT-04 |
| ACT-08 | 依存関係特定     | 依存関係を特定             | ACT-02, ACT-04 |
| ACT-09 | 項目記録         | バックログ項目を記録       | ACT-02, ACT-04 |
| ACT-10 | 関係者共有       | 関係者に共有               | ACT-02, ACT-04 |
