---
sessionId: session-261121-190726-1jlp
---

# 現状分析

## 現状の乖離の概要

実装（`BacklogItemStatus.java`）とRDRA成果物の間に以下の乖離があります。

### 実装の正（BacklogItemStatus.java）
- **7状態**: SUGGESTED, ACCEPTED, FROZEN, READY, ESTIMATED, IN_PROGRESS, DONE
- 初期状態: SUGGESTED
- 実装済みの遷移: SUGGESTED→ACCEPTED, SUGGESTED→FROZEN, FROZEN→SUGGESTED, ACCEPTED→READY, READY→SUGGESTED, READY→ESTIMATED, ESTIMATED→READY
- IN_PROGRESS/DONE はenumのみ（未実装）
- イベント: `BacklogItemStatusChanged`

### ステートマシン図_バックログ項目.md（旧5状態）
- 新規→明確化中→準備完了→実装中→完了
- 実装の体系と完全に異なる

### ステートマシン図_ストーリー.md（8状態）
- Frozen, Suggested, Accepted, Ready, Estimated, In Progress, In Review, Done
- `Ready` の意味が実装と異なる（実装では ACCEPTED→READY、ドキュメントでは Accepted→Ready→Estimated）
- `Planned` を含み、実装に存在しない
- `In Review` を含み、実装に存在しない
- この「ストーリー」は実装上の BacklogItem とは別物になっている

### BUC-BL-08（ストーリーのステータス管理）
- 8状態体系を参照
- UC-BL-026/027/028 を関連UCとして参照但这些は実際はFeatureダッシュボードのUC
- 状態遷移の専用UCは存在しない

### ステート一覧.md
- バックログ項目: ST-BL-01〜05（旧5状態）
- ストーリー: ST-BL-12〜20（8状態）
- バックログ項目とストーリーで状態定義が重複

### ビジネスルール.md
- 状態遷移ルールが旧5状態体系のまま

### 条件一覧.tsv
- COND-BL-05, COND-BL-07 が旧体系

### イベント一覧
- 状態変更専用イベント（EVT-BL-xxx バックログ項目の状態が変更された）が存在しない

### BUC一覧.tsv
- BUC-BL-07, BUC-BL-08 がBUC一覧.tsvに登録されていない（BUC記述ファイルのみ存在）


# Technical Design

## 採番方針

### 状態ID（ST-BL）
既存IDの安定性を優先:
- ST-BL-01〜05（バックログ項目旧5状態）: **再定義**。IDは保持し、状態名と説明を実装に合わせて更新
- ST-BL-06〜11（フィードバック、Story Map）: 変更なし
- ST-BL-12〜20（ストーリー8状態）: **削除**。バックログ項目とストーリーの状態モデルを統一
- ST-BL-21〜26（フィーチャー、未決定一覧）: 変更なし

### ユースケース番号
- 既存UC-BL-001〜029: 変更なし
- 新規状態遷移専用UC: **UC-BL-030** から採番開始
- UC-BL-030: バックログ項目の状態を変更する
- UC-BL-031: バックログ項目の状態遷移履歴を参照する

### イベント番号
- 既存EVT-BL-001〜027: 変更なし
- 新規: **EVT-BL-028** バックログ項目の状態が変更された

## バックログ項目とストーリーの統一

**提案: 「ストーリー」状態モデルを削除し、「バックログ項目」に統一**

理由:
1. 実装では `BacklogItemStatus` が単一のenumで、バックログ項目とストーリーを区別していない
2. 「ストーリー」はバックログ項目のタイプ（Epic, Feature, Story, Task）の一つに過ぎない
3. 二つの状態モデルを維持すると、どの項目がどのモデルに従うかの判定ルールが必要になる

## IN_PROGRESS/DONE と Planned/In Review の扱い

| 状態 | 扱い | 理由 |
|------|------|------|
| IN_PROGRESS | enumに存在。ドキュメントに含めるが「未実装」と明記 | Issue #522 で実装予定 |
| DONE | enumに存在。ドキュメントに含めるが「未実装」と明記 | Issue #525 で実装予定 |
| Planned | **含めない**。実装のenumに存在しない | 実装と整合 |
| In Review | **含めない**。実装のenumに存在しない | 実装と整合 |

## 未決定事項

1. **BUC-BL-08 の位置づけ**: 実装では状態遷移が `ChangeBacklogItemStatusCommandService` に統合されており、独立したBUCとして扱うべきか、BUC-BL-02（バックログ項目の作成・明確化）に吸収すべきか
2. **BUC-BL-07（Featureダッシュボード）**: BUC一覧.tsvに登録されていない。今回の範囲外だが、UC-BL-026〜029 の所属BUCが不明確
3. **IN_PROGRESS/DONE への遷移UC**: 実装未完了だが、RDRAでは事前定義すべきか、実装完了後に追記すべきか
4. **分割操作でのDONE遷移**: Issue #519 で計画済み。UC-BL-010（バックログ項目を分割する）で元項目がDONEになるが、これを状態遷移UCとして扱うか
5. **ステート一覧(更新).tsv**: Featureダッシュボード用の別状態体系。今回の対象外だが、整合性確認の対象から明示的に除外する


# Delivery Plan

## 段階1: ステートマシン図とステート一覧の実装整合

**対象ファイル**:
- `ステートマシン図_バックログ項目.md` — 旧5状態から7状態に全面改訂
- `ステートマシン図_ストーリー.md` — 削除（バックログ項目に統合）
- `ステートマシン図.md` — ストーリー参照を削除、バックログ項目のみの参照に更新
- `ステート一覧.md` — ST-BL-01〜05 を実装の7状態に再定義、ST-BL-12〜20 を削除
- `ステート一覧.tsv` — バックログ項目の状態を実装に合わせて更新、ストーリー行を削除

**変更内容**:
- PlantUML図を7状態（SUGGESTED, ACCEPTED, FROZEN, READY, ESTIMATED, IN_PROGRESS, DONE）に更新
- 実装済みの遷移のみを線として描画、IN_PROGRESS/DONE への遷移は「未実装」注記
- 状態説明を `BacklogItemStatus.java` のJavadocと整合

## 段階2: BUC-BL-08 とユースケースの改訂

**対象ファイル**:
- `BUC-BL-08 ストーリーのステータス管理_BUC記述.md` — 名称を「バックログ項目の状態管理」に改訂、7状態体系に更新、関連UC を UC-BL-030/031 に更新
- `BUC一覧.tsv` — BUC-BL-07, BUC-BL-08 を登録
- `ユースケース一覧.tsv`、`ユースケース一覧.md` — UC-BL-030, UC-BL-031 を追加
- 新規: `UC-BL-030 バックログ項目の状態を変更する.md`
- 新規: `UC-BL-031 バックログ項目の状態遷移履歴を参照する.md`

**変更内容**:
- BUC-BL-08 の関連UC を UC-BL-026/027/028（Featureダッシュボード）から UC-BL-030/031 に修正
- UC-BL-008（バックログ項目を更新する）との役割分担を明確化: UC-BL-008 は内容更新、UC-BL-030 は状態変更
- 権限情報を `ChangeBacklogItemStatusCommandService.java` から反映

## 段階3: ビジネスルール・条件・イベントの更新

**対象ファイル**:
- `ビジネスルール.md` — 状態遷移ルールを実装の7状態に更新
- `条件一覧.tsv` — COND-BL-05, COND-BL-07 を実装の遷移ルールに更新
- `イベント一覧.tsv`、`イベント一覧.md` — EVT-BL-028 を追加
- 新規: `EVT-BL-028 バックログ項目の状態が変更された.md`
- 新規: `EVT-BL-028_バックログ項目の状態が変更された_イベント属性一覧.md`

**変更内容**:
- ビジネスルールの状態遷移表を7状態に更新
- 条件一覧の関連状態モデルを更新
- イベント定義を `BacklogItemStatusChanged.java` のrecord定義と整合

## 段階4: 情報属性・トレースアビリティ・整合性確認

**対象ファイル**:
- `情報一覧.tsv`、`情報一覧.md` — INF-BL-11（状態）の説明を更新
- 各UCファイルの関連状態参照を更新（該当する場合）
- `UC複合図.md` — 状態関連の参照を更新

**整合性確認**:
- grep で各ID（ST-BL-xx, UC-BL-030, UC-BL-031, EVT-BL-028, COND-BL-05, COND-BL-07）の参照整合性を検証
- 削除したID（ST-BL-12〜20）への残存参照をチェック
- 状態遷移の双方向整合性確認（TSVの遷移先状態が状態一覧に存在するか）


# Delivery Steps

###   Step 1: ステートマシン図とステート一覧の実装整合
ステートマシン図とステート一覧を実装の7状態体系に整合させる。
- ステートマシン図_バックログ項目.md を旧5状態から7状態（SUGGESTED, ACCEPTED, FROZEN, READY, ESTIMATED, IN_PROGRESS, DONE）に全面改訂。PlantUML図と状態一覧表の両方を更新
- ステートマシン図_ストーリー.md を削除（バックログ項目に統合）。実装では BacklogItemStatus が単一enumであり、ストーリー用の別状態モデルが存在しないため
- ステートマシン図.md（統合版）からストーリー参照を削除し、バックログ項目のみの参照に更新
- ステート一覧.md で ST-BL-01〜05 を実装の7状態に再定義（IDは保持）。ST-BL-12〜20（ストーリー8状態）を削除
- ステート一覧.tsv でバックログ項目の状態行を実装に合わせて更新、ストーリー行を削除
- IN_PROGRESS と DONE はenumに存在するが実装未完了のため、「未実装」注記を付与

###   Step 2: BUC-BL-08 とユースケースの改訂
BUC-BL-08 を実装に合わせて改訂し、状態遷移専用ユースケースを新規作成。
- BUC-BL-08 の名称を「ストーリーのステータス管理」から「バックログ項目の状態管理」に改訂。8状態体系から7状態体系に更新。関連UC を UC-BL-026/027/028（Featureダッシュボード）から UC-BL-030/031 に修正
- BUC一覧.tsv に BUC-BL-07 と BUC-BL-08 を登録（現在未登録）
- UC-BL-030「バックログ項目の状態を変更する」を新規作成。権限情報を ChangeBacklogItemStatusCommandService.java から反映（PO/SM のみ、チームメンバー全員などの遷移ごとの権限）
- UC-BL-031「バックログ項目の状態遷移履歴を参照する」を新規作成。BacklogItemStatusChanged イベントの参照を目的とする
- ユースケース一覧.tsv と .md に UC-BL-030, UC-BL-031 を追加
- UC-BL-008（バックログ項目を更新する）との役割分担を明確化: UC-BL-008 は内容更新、UC-BL-030 は状態変更

###   Step 3: ビジネスルール・条件・イベントの更新
ビジネスルール、条件一覧、イベント定義を実装の7状態体系に更新。
- ビジネスルール.md の状態遷移ルール表を7状態に更新。旧5状態の遷移ID（ST-BL-01〜04）を再定義
- 条件一覧.tsv で COND-BL-05（状態遷移の制約）と COND-BL-07（依存関係未充足時の状態遷移禁止）を実装の遷移ルールに更新
- EVT-BL-028「バックログ項目の状態が変更された」を新規作成。BacklogItemStatusChanged.java のrecord定義（backlogItemId, scrumTeamId, fromStatus, toStatus, actorEmployeeId, occurredAt）と整合
- イベント一覧.tsv と .md に EVT-BL-028 を追加
- EVT-BL-028 のイベント属性一覧ファイルも新規作成

###   Step 4: 情報属性・トレースアビリティ・整合性確認
情報属性を更新し、全RDRA成果物の参照整合性をgrep等で検証。
- 情報一覧.tsv と .md で INF-BL-11（状態）の説明を実装の7状態に更新
- 各UCファイルで状態関連の参照を更新（該当する場合）
- UC複合図.md で状態関連の参照を更新
- grep で各ID（ST-BL-xx, UC-BL-030, UC-BL-031, EVT-BL-028, COND-BL-05, COND-BL-07）の参照整合性を検証
- 削除したID（ST-BL-12〜20）への残存参照をチェックし、発見された場合は修正
- 状態遷移の双方向整合性確認: TSVの遷移先状態が状態一覧に存在するか、遷移UCがユースケース一覧に存在するかを検証