# Issue #490 実装計画

## 概要

バックログアイテムを `ACCEPTED` 状態に変更する際に、`value` が設定されていることを必須とするバリデーションを `BacklogItem` エンティティに実装する。

## 背景

- Issue #493 で `BacklogItem` に `Integer` 型の `value` フィールドと範囲定数（`VALUE_MIN_VALUE=0` / `VALUE_MAX_VALUE=100`）が実装済み
- `changeStatus()` は `ACCEPTED` への遷移時に `validateValue()` で範囲チェック（0〜100）を行っているが、`value` が `null` の場合は通過する
- 現状のテスト（`BacklogItemTest.shouldAcceptValueNullOnAccepted()`）でも `null` 通過が確認されている
- `IllegalArgumentException` は `BacklogItemExceptionHandler` で HTTP 400 に変換される

## 決定済み事項

| 項目 | 決定内容 |
|------|---------|
| Valueの型 | `Integer`（空文字列のケースは存在しない。検証対象は `null` のみ） |
| 作成時のValue | 任意のまま（必須化しない） |
| 更新経路 | `update()` には必須化を広げない |
| 対象タイミング | `ACCEPTED` への状態遷移時のみ |
| REJECTED遷移 | `value=null` でも成功する（変更なし） |
| 既存ACCEPTEDデータ | 移行やデフォルト値設定は行わない |

## バリデーションの配置方針

### 第一候補：エンティティ `BacklogItem.changeStatus()`

**採用する。** 根拠は以下の通り：

1. **Issue #493 の前例**: 範囲チェックが `BacklogItem.validateValue()`（エンティティ内）で実装されている。同じメソッドに `null` チェックを追加することで、Value に関する検証が一つの場所に集約される
2. **ドメインルールの保護**: 「ACCEPTED には Value が必要」という制約はドメインの不変条件であり、エンティティ層で保護するのがオニオンアーキテクチャの原則に合致する
3. **サービス層での追加検証の不要性**: コマンドサービス（`ChangeBacklogItemStatusCommandService`）は権限判定（SM/PO 照合）を担当しており、ドメイン制約の検証をサービス層に置く理由がない
4. **例外ハンドリングの既存経路**: `IllegalArgumentException` → `BacklogItemExceptionHandler` → HTTP 400 の経路が既に確立されている

### サービス層配置の検討結果

サービス層に置くべき理由は見当たらない。以下の理由でエンティティ配置が適切：
- サービス層は権限判定とイベント発行を担当するインフラストラクチャ的な役割であり、ドメイン制約の検証はエンティティに属する
- 他のユースケース（バッチ処理など）から `changeStatus()` を呼び出す場合でも、エンティティ内の検証が適用される

## 変更対象ファイル

### 1. `BacklogItem.java`（spi）

**ファイルパス**: `backend/src/main/java/undecided/agiloom/requirementBacklog/spi/BacklogItem.java`

**変更内容**: `validateValue()` メソッド（179-187行目）の `null` チェック部分を修正

**変更前**:
```java
private void validateValue(Integer value) {
  if (value == null) {
    return;  // ← ここを変更
  }
  if (value < VALUE_MIN_VALUE || value > VALUE_MAX_VALUE) {
    throw new IllegalArgumentException(
        "BacklogItem value must be between " + VALUE_MIN_VALUE + " and " + VALUE_MAX_VALUE);
  }
}
```

**変更方針**: `validateValue()` は現在、`update()`（valueがnullの場合は検証をスキップ）と `changeStatus()`（ACCEPTED時はvalueがnullでも通過）の両方から呼び出されている。`update()` 側では `null` を「変更なし」としてスキップする必要があるため、`validateValue()` 自体を `null` 必須に変更すると `update()` の動作が壊れる。

したがって、以下の2つのアプローチのいずれかを選択する：

**アプローチA（推奨）: `changeStatus()` で直接nullチェックを追加**

`validateValue()` は変更せず、`changeStatus()` の `ACCEPTED` 分岐で `null` チェックを追加：

```java
public void changeStatus(@NonNull BacklogItemStatus target) {
  checkNotNull(target, () -> new IllegalArgumentException("target status must not be null"));
  if (this.status != BacklogItemStatus.SUGGESTED || target == BacklogItemStatus.SUGGESTED) {
    throw new IllegalStateException(
        "Invalid status transition: " + this.status + " -> " + target);
  }
  if (target == BacklogItemStatus.ACCEPTED) {
    if (this.value == null) {
      throw new IllegalArgumentException("BacklogItem value must be set before accepting");
    }
    validateValue(this.value);
  }
  this.status = target;
  this.updatedAt = LocalDateTime.now();
}
```

**アプローチB: `validateValue()` にフラグ引数を追加**

`validateValue(boolean requireNonNull)` のように引数で `null` チェックの有効/無効を切り替える。ただし、メソッドシグネチャの変更が `update()` 側にも影響するため、アプローチAの方が影響範囲が小さい。

**結論: アプローチAを採用する。**

### 2. `BacklogItemTest.java`（spi テスト）

**ファイルパス**: `backend/src/test/java/undecided/agiloom/requirementBacklog/spi/BacklogItemTest.java`

**変更内容**:
- 既存テスト `shouldAcceptValueNullOnAccepted()`（212-217行目）を修正 — `null` で `ACCEPTED` 遷移すると `IllegalArgumentException` がスローされることを検証
- 新規テスト追加 — `value` が設定済みの状態で `ACCEPTED` 遷移が成功すること（既存の `shouldAcceptValueZeroOnAccepted` が該当するが、明示的なテストとして追加検討）

### 3. `ChangeBacklogItemStatusCommandServiceTest.java`（internal テスト）

**ファイルパス**: `backend/src/test/java/undecided/agiloom/requirementBacklog/internal/ChangeBacklogItemStatusCommandServiceTest.java`

**変更内容**:
- 既存テスト `shouldAcceptByPoAndPublishEvent()`（84-105行目）で `suggestedItem()` が `value` を設定していないため、テストが失敗する。`value` を設定したアイテムをモックに置き換える
- 新規テスト追加 — `value=null` のアイテムを `ACCEPTED` に変更しようとすると `IllegalArgumentException` がスローされることを検証

## 例外メッセージ方針

- **例外型**: `IllegalArgumentException`（既存の範囲チェックと統一）
- **メッセージ**: `"BacklogItem value must be set before accepting"`
  - 範囲チェックのメッセージ `"BacklogItem value must be between 0 and 100"` と区別できる表現
  - 英語メッセージは既存コードの `checkNotNull` / `checkNonEmpty` のラムダ内メッセージ（例: `"target status must not be null"`）とスタイルを統一
- **HTTP ステータス**: 400 Bad Request（`BacklogItemExceptionHandler` で変換される既存経路）

## テスト観点

| No. | テストケース | 期待結果 | テスト種別 |
|-----|-------------|---------|-----------|
| 1 | `value=null` で `ACCEPTED` 遷移 | `IllegalArgumentException` スロー | Small（BacklogItemTest） |
| 2 | `value=0` で `ACCEPTED` 遷移（下限境界値） | 成功（既存テスト維持） | Small（BacklogItemTest） |
| 3 | `value=50` で `ACCEPTED` 遷移（中間値） | 成功 | Small（BacklogItemTest） |
| 4 | `value=100` で `ACCEPTED` 遷移（上限境界値） | 成功（既存テスト維持） | Small（BacklogItemTest） |
| 5 | `value=-1` で `ACCEPTED` 遷移（範囲外） | `IllegalArgumentException` スロー（既存テスト維持） | Small（BacklogItemTest） |
| 6 | `value=101` で `ACCEPTED` 遷移（範囲外） | `IllegalArgumentException` スロー（既存テスト維持） | Small（BacklogItemTest） |
| 7 | `value=null` で `REJECTED` 遷移 | 成功（変更なし） | Small（BacklogItemTest） |
| 8 | `value` 設定済みで `REJECTED` 遷移 | 成功（変更なし） | Small（BacklogItemTest） |
| 9 | 作成時 `value=null` | 成功（任意のまま） | Small（BacklogItemTest） |
| 10 | `update()` で `value=null` | 成功（変更なし、必須化対象外） | Small（BacklogItemTest） |
| 11 | コマンドサービス経由で `value=null` のアイテムを `ACCEPTED` に変更 | `IllegalArgumentException` スロー | Small（ChangeBacklogItemStatusCommandServiceTest） |
| 12 | コマンドサービス経由で `value` 設定済みのアイテムを `ACCEPTED` に変更 | 成功、イベント発行 | Small（ChangeBacklogItemCommandServiceTest） |

### 既存テストとの整合性

| 既存テスト | 変更内容 |
|-----------|---------|
| `shouldAcceptValueNullOnAccepted()` | `assertThatThrownBy` に変更して `IllegalArgumentException` を検証 |
| `shouldAcceptFromSuggested()` | `value` を設定したアイテムを使用するように修正 |
| `shouldAcceptByPoAndPublishEvent()` | `value` を設定したアイテムを使用するように修正 |
| `shouldRejectBySm()` | 変更不要（REJECTED遷移はvalue=nullでも成功） |

## 実施手順

1. **`BacklogItem.changeStatus()` の修正**
   - `ACCEPTED` 分岐の `validateValue(this.value)` の直前に `null` チェックを追加
   - `IllegalArgumentException` をスローするメッセージを設定

2. **`BacklogItemTest.java` の修正・追加**
   - `shouldAcceptValueNullOnAccepted()` を `shouldThrowOnValueNullOnAccepted()` にリネームし、`assertThatThrownBy` で `IllegalArgumentException` を検証するように修正
   - `shouldAcceptFromSuggested()` で使用するアイテムに `value` を設定
   - `value=50` の中間値テストを追加（オプション）
   - `value` 設定済みで `REJECTED` 遷移が成功するテストを追加（オプション）

3. **`ChangeBacklogItemStatusCommandServiceTest.java` の修正・追加**
   - `suggestedItem()` メソッドに `value` を設定するか、ACCEPTED 遷移テストで `value` を設定したアイテムを使用するように修正
   - `value=null` のアイテムを `ACCEPTED` に変更しようとすると `IllegalArgumentException` がスローされるテストを追加

4. **ビルドとテスト実行**
   - `./gradlew :backend:test` で全 small テストが通過することを確認

## リスクと対応

| リスク | 影響 | 対応 |
|-------|------|------|
| 既存のACCEPTEDデータに `value=null` のレコードが存在する可能性 | 本バリデーションは状態遷移時のみ対象であり、既存データの読み取り・表示には影響しない | 移行スクリプトは実施しない（決定済み事項） |
| `update()` で `value` を設定後に `changeStatus()` を呼び出すフローがある場合 | エンティティの `value` フィールドが更新されるため、`changeStatus()` 時には `null` でない | 問題なし |
| コマンドサービステストのモック設定漏れ | テストが失敗する | `suggestedItem()` ヘルパーを修正して全テストで一貫させる |

## 範囲外事項

- **`update()` での `value` 必須化**: 対象外。`update()` は `value=null` を「変更なし」として処理し続ける
- **作成時の `value` 必須化**: 対象外。`CreateBacklogItemCommand` および `BacklogItem` のビルダーでは `value` は任意のまま
- **既存ACCEPTEDデータの移行**: 実施しない。既存データへのデフォルト値設定やマイグレーションは行わない
- **API レベルのバリデーション追加**: 対象外。エンティティ層のバリデーションのみで対応
- **フロントエンドの変更**: 対象外。backend のみの変更
- **他の状態遷移（例: ACCEPTED → IN_PROGRESS）**: 現時点で定義されている遷移は `SUGGESTED → ACCEPTED` と `SUGGESTED → REJECTED` のみ。将来の遷移に `value` 制約が必要かどうかは別途検討
