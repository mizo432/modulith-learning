---
sessionId: session-261117-153918-1uc9
---

# Requirements

### Overview & Goals

バックログアイテムのValue（0〜100、null可）から狩野式品質カテゴリを自動分類する機能を追加します。Valueの設定・変更時に必ず再計算され、APIレスポンスおよびフロントエンドで色分け表示されます。

### Scope

**In Scope:**
1. `QualityCategory` enumのspiパッケージへの新規定義（4カテゴリ＋null）
2. `BacklogItem`エンティティに`qualityCategory`カラム追加、Value設定時に自動再計算
3. Flywayマイグレーション（カラム追加＋既存行の一括計算）
4. APIレスポンスにqualityCategory（コード値＋日本語ラベル）を含める
5. フロントエンドでバックログ一覧に品質カテゴリを色分け表示
6. 境界値テスト・作成・更新時の再計算テスト・フロントエンドテスト

**Out of Scope:**
- QualityCategoryを独立した値オブジェクトとする（enumで十分）
- 品質カテゴリの変更イベント発行
- バックログ詳細ページ（BacklogItemDetail）への品質カテゴリ表示

### Decision: 境界値

| カテゴリ | コード | 範囲 | 日本語ラベル |
|---|---|---|---|
| 魅力的品質 | ATTRACTIVE | 76〜100 | 魅力的 |
| 当たり前品質 | MUST_BE | 51〜75 | 当たり前 |
| 一元的品質 | ONE_DIMENSIONAL | 26〜50 | 一元的 |
| 無関心品質 | INDIFFERENT | 0〜25 | 無関心 |
| - | null | value=null | - |

### User Stories

- As a スクラムチームメンバー, I want バックログアイテムのValueから品質カテゴリが自動表示されるようにしたい, so that Valueの意味を直感的に理解できる.
- As a PO/SM, I want 品質カテゴリを色分けで確認できるようにしたい, so that バックログの優先度判断を支援できる.


# Technical Design

### Current Implementation

**BacklogItemエンティティ** (`spi/BacklogItem.java`):
- `value`フィールド（Integer、null可）を保持。範囲検証は`validateValue()`で0〜100をチェック
- `update()`メソッドでvalueが変更可能。`@Builder`で初期作成
- `BacklogItemStatus`と同様のパターンで区分値を管理

**Create経路** (`internal/CreateBacklogItemCommandService.java`):
- `BacklogItem.builder()`でエンティティを作成。現在valueはCommand/Requestに含まれておらず、ビルダーでも設定しない
- 作成後のvalueはnullで、詳細ページで更新される

**Update経路** (`internal/UpdateBacklogItemCommandService.java`):
- `backlogItem.update()`を呼び出し、valueを含むフィールドを更新

**APIレスポンス** (`internal/BacklogItemResponse.java` + `BacklogItemResponseConverter.java`):
- `status`と`statusLabel`の両方を保持するパターンが確立済み
- Converterで`backlogItem.getStatus().label()`を`statusLabel`に設定

**フロントエンド** (`frontend/src/pages/products/BacklogItemList.tsx`):
- Value列は既存。`valueColor.ts`の`valueToColor()`/`valueToBackgroundColor()`で色付け済み
- `BacklogItem`型に`qualityCategory`を追加し、テーブルに品質カテゴリ列を挿入

### Key Decisions

1. **QualityCategoryはenumとしてspiパッケージに配置** — `BacklogItemStatus`と同じ形式（日本語ラベル付き）。ADR-003準拠でDBには英語コード値のみ
2. **再計算ロジックはBacklogItemエンティティ内に閉じる** — `calculateQualityCategory()`静的メソッドでValueからカテゴリを決定。`update()`とビルダーの両方で呼び出し
3. **CreateBacklogItemCommandにvalueを追加しない** — 作成時はvalue=nullのまま（既存のUXフローと一致）。valueが設定された時点で（update経由）カテゴリが計算される
4. **FlywayはV20261117160000** — 最新マイグレーション（V20261117100000）より後のバージョン

### Proposed Changes

#### (1) QualityCategory enum（新規）
- `spi/QualityCategory.java`を新規作成
- 4カテゴリ＋`fromValue(Integer)`静的メソッドでValueからカテゴリを決定
- `label()`メソッドで日本語ラベルを返す

#### (2) BacklogItemエンティティ変更
- `qualityCategory`フィールド追加（`@Enumerated(EnumType.STRING)`、`@Column(length=20)`、null可）
- `calculateQualityCategory()`静的メソッド追加
- `update()`メソッドでvalue変更時に`qualityCategory`を再計算
- `@Builder`の`value`フィールド設定時にも再計算（`@Builder`の`build()`前に再計算するため、`@Builder`で`value`を設定する場合は`build()`メソッドで再計算するか、`value`セッターで再計算）

#### (3) Flywayマイグレーション（新規）
- `V20261117160000__add_quality_category_to_backlog_items.sql`
- `quality_category`カラム追加（VARCHAR(20)）
- 既存行のvalueからCASE式で一括計算

#### (4) APIレスポンス変更
- `BacklogItemResponse`に`qualityCategory`（Stringコード）と`qualityCategoryLabel`（Stringラベル）を追加
- `BacklogItemResponseConverter`で変換

#### (5) フロントエンド変更
- `backlogItemService.ts`の`BacklogItem`インターフェースに`qualityCategory`と`qualityCategoryLabel`を追加
- `BacklogItemList.tsx`に品質カテゴリ列を追加（Chipで色分け表示）
- 色: 魅力的=緑、当たり前=青、一元的=黄、無関心=赤

### File Structure

**新規ファイル:**
- `backend/src/main/java/undecided/agiloom/requirementBacklog/spi/QualityCategory.java`
- `backend/src/main/resources/db/migration/ag/V20261117160000__add_quality_category_to_backlog_items.sql`
- `backend/src/test/java/undecided/agiloom/requirementBacklog/spi/QualityCategoryTest.java`

**変更ファイル:**
- `backend/src/main/java/undecided/agiloom/requirementBacklog/spi/BacklogItem.java`
- `backend/src/main/java/undecided/agiloom/requirementBacklog/internal/BacklogItemResponse.java`
- `backend/src/main/java/undecided/agiloom/requirementBacklog/internal/BacklogItemResponseConverter.java`
- `backend/src/test/java/undecided/agiloom/requirementBacklog/spi/BacklogItemTest.java`
- `backend/src/test/java/undecided/agiloom/requirementBacklog/internal/CreateBacklogItemCommandServiceTest.java`
- `frontend/src/services/backlogItemService.ts`
- `frontend/src/pages/products/BacklogItemList.tsx`
- `frontend/src/pages/products/BacklogItemList.test.tsx`

### Architecture Diagram

``` mermaid
graph LR
  A[QualityCategory enum] --> B[BacklogItem エンティティ]
  B --> C[BacklogItemResponse]
  C --> D[BacklogItemResponseConverter]
  D --> E[API レスポンス]
  E --> F[BacklogItemList フロントエンド]
  G[UpdateBacklogItemCommand] --> B
  H[Flyway マイグレーション] --> I[DB: quality_category カラム]
  B --> I
```

### Risks

- **Builderでの再計算タイミング**: `@Builder`は`build()`メソッドを生成するため、`value`が設定された後の再計算を`@Builder`の`build()`メソッドで処理する必要がある。Lombokの`@Builder`ではカスタム`build()`を定義できるが、既存の`@AllArgsConstructor`との競合に注意
- **既存テストの更新**: `BacklogItemTest`の既存テストはvalueの境界値をテストしているが、qualityCategoryの検証は含まない。既存テストは壊れないが、新しい検証を追加


# Testing

### Validation Approach

各層ごとに単体テスト（smallタグ）を作成し、境界値と再計算ロジックを網羅的に検証する。

### Key Scenarios

#### QualityCategory境界値テスト（`QualityCategoryTest.java`）
| Value | 期待カテゴリ | テスト名 |
|---|---|---|
| 0 | INDIFFERENT | shouldReturnIndifferentForValueZero |
| 25 | INDIFFERENT | shouldReturnIndifferentForValue25 |
| 26 | ONE_DIMENSIONAL | shouldReturnOneDimensionalForValue26 |
| 50 | ONE_DIMENSIONAL | shouldReturnOneDimensionalForValue50 |
| 51 | MUST_BE | shouldReturnMustBeForValue51 |
| 75 | MUST_BE | shouldReturnMustBeForValue75 |
| 76 | ATTRACTIVE | shouldReturnAttractiveForValue76 |
| 100 | ATTRACTIVE | shouldReturnAttractiveForValue100 |
| null | null | shouldReturnNullForNullValue |

#### BacklogItemエンティティテスト（`BacklogItemTest.java`に追加）
- `update()`でvalueを設定するとqualityCategoryが再計算される
- `update()`でvalueを変更するとqualityCategoryが更新される
- 作成時（Builder）にvalueが設定されていればqualityCategoryが計算される
- valueがnullの場合、qualityCategoryもnull

#### CreateBacklogItemCommandServiceテスト（既存テストの更新）
- 作成時のvalue=nullでqualityCategoryもnullであることを確認

#### フロントエンドテスト（`BacklogItemList.test.tsx`に追加）
- 品質カテゴリ列が各カテゴリのChipとして表示される
- 色分けが正しく適用される（緑/青/黄/赤）
- qualityCategoryがnullの場合は「-」または空表示

### Edge Cases

- Valueが境界値（25/26/50/51/75/76）で正しく分類される
- Valueがnull→値→nullへの遷移でqualityCategoryが正しく更新される
- 既存行のマイグレーションでvalueがnullの行はquality_categoryもnull

### Test Cases Summary

| テストファイル | テスト数 | 概要 |
|---|---|---|
| `QualityCategoryTest.java` | 9 | 境界値テスト（0, 25, 26, 50, 51, 75, 76, 100, null） |
| `BacklogItemTest.java`（追加） | 6 | 作成時・更新時の再計算テスト |
| `CreateBacklogItemCommandServiceTest.java`（更新） | 1 | 作成時のqualityCategory=null |
| `BacklogItemList.test.tsx`（追加） | 4 | 品質カテゴリ列の表示・色分けテスト |


# Delivery Steps

###   Step 1: 1. QualityCategory enumとFlywayマイグレーションを作成
spiパッケージにQualityCategory enumを新規作成し、Flywayマイグレーションを追加する。
- `spi/QualityCategory.java`を新規作成（4カテゴリ＋fromValue()＋label()）
- 境界値テスト`spi/QualityCategoryTest.java`を新規作成（9ケース）
- `db/migration/ag/V20261117160000__add_quality_category_to_backlog_items.sql`を新規作成（カラム追加＋CASE式による一括計算）
- `./gradlew :backend:test`でQualityCategoryTestが通過することを確認

###   Step 2: 2. BacklogItemエンティティにqualityCategoryを追加し再計算ロジックを実装
BacklogItemエンティティにqualityCategoryフィールドと再計算ロジックを追加する。
- `BacklogItem.java`に`qualityCategory`フィールド追加（@Enumerated(STRING)、length=20、null可）
- `calculateQualityCategory()`静的メソッドを追加（QualityCategory.fromValue()を呼び出し）
- `update()`メソッドでvalue変更時にqualityCategoryを再計算
- カスタム`build()`メソッドでvalue設定時にqualityCategoryを計算
- `BacklogItemTest.java`に再計算テストを追加（6ケース）
- `CreateBacklogItemCommandServiceTest.java`を更新（qualityCategory=null確認）
- `./gradlew :backend:test`で全テストが通過することを確認

###   Step 3: 3. APIレスポンスにqualityCategoryを含める
BacklogItemResponseとConverterにqualityCategoryを追加し、APIレスポンスに含める。
- `BacklogItemResponse.java`に`qualityCategory`（String）と`qualityCategoryLabel`（String）を追加
- `BacklogItemResponseConverter.java`でqualityCategoryとラベルの変換を追加
- `./gradlew :backend:test`で全テストが通過することを確認

###   Step 4: 4. フロントエンドに品質カテゴリ列を色分け表示で追加
フロントエンドのバックログ一覧に品質カテゴリ列を追加し、色分け表示する。
- `backlogItemService.ts`の`BacklogItem`インターフェースに`qualityCategory`と`qualityCategoryLabel`を追加
- `BacklogItemList.tsx`に品質カテゴリ列をValue列の後に追加（Chipで色分け：緑/青/黄/赤）
- `BacklogItemList.test.tsx`に品質カテゴリ表示テストを追加（4ケース）
- フロントエンドテストが通過することを確認

# 判定（Muse、2026-11-17）

採用。ただし次の2点を修正条件とする。

1. 日本語ラベルは正式名称の「魅力的品質」「当たり前品質」「一元的品質」「無関心品質」とする（計画書中の短縮表記は読み替える）。
2. FlywayマイグレーションのCASE式では、value が NULL の既存行は quality_category も NULL のままにする。