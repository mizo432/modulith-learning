# Issue #590 実装計画

## 概要

`shared` パッケージの UUID 値オブジェクト群を撤去する。#583「UUIDになっているIDは全てSnowflakeIdに変更する」の最終整理タスク。

## 調査結果（2026-11-17 実測）

### 撤去対象ファイル一覧

| ファイル | モジュール | 種別 | 備考 |
|---------|-----------|------|------|
| `backend/.../shared/value/UuidV7IdValue.java` | backend | 本体 | 撤去対象（Issue 記載） |
| `backend/.../shared/value/UuidValue.java` | backend | インターフェース | 撤去対象（Issue 記載） |
| `backend/.../shared/value/UuidV7IdValueTest.java` | backend | テスト | 本体撤去に伴い連動削除 |
| `backend/.../shared/sample/jpa/value/package-info.java` | backend | javadoc | 参照リンクの修正が必要 |
| `mizo432-shared-common/.../uuidV7Provider/UuidV7Provider.java` | mizo432-shared-common | 本体 | 参照がゼロになるため撤去 |
| `mizo432-shared-common/.../uuidV7Provider/FixedUuidV7IdProvider.java` | mizo432-shared-common | 本体 | 同上 |
| `mizo432-shared-common/.../uuidV7Provider/RollingUuidV7IdProvider.java` | mizo432-shared-common | 本体 | 同上 |
| `mizo432-shared-common/.../uuidV7Provider/UuidV7ProviderTest.java` | mizo432-shared-common | テスト | 連動削除 |
| `mizo432-shared-common/.../uuidV7Provider/FixedUuidV7IdProviderTest.java` | mizo432-shared-common | テスト | 連動削除 |
| `mizo432-shared-common/.../uuidV7Provider/RollingUuidV7IdProviderTest.java` | mizo432-shared-common | テスト | 連動削除 |

### 参照関係マップ

```
UuidV7IdValue.java
  └─ import UuidV7Provider (mizo432-shared-common)
  └─ implements UuidValue<UuidV7IdValue>

UuidValue.java
  └─ extends SingleValue<UUID>
  └─ @see UuidV7IdValue (javadoc)
  └─ 実装クラス: UuidV7IdValue のみ

UuidV7Provider.java (mizo432-shared-common)
  └─ import 参照元: UuidV7IdValue.java, UuidV7IdValueTest.java のみ
  └─ サブクラス: FixedUuidV7IdProvider, RollingUuidV7IdProvider

SingleValue.java
  └─ 実装: IntegerValue, LongValue, StringValue, UuidValue
  └─ UuidValue 撤去後も IntegerValue / LongValue / StringValue が利用中 → 撤去対象外
```

### UuidV7Provider 撤去の判断根拠

- `import com.github.mizo432.shared.common.uuidV7Provider.*` を持つファイルは、`UuidV7IdValue.java` と `UuidV7IdValueTest.java` の2ファイルのみ
- UuidV7IdValue 撤去後、uuidV7Provider パッケージ全体が未使用になる
- **結論: UuidV7Provider パッケージ全体を撤去対象に含める**

### 影響なし項目

- `SingleValue.java`: IntegerValue / LongValue / StringValue が利用中 → 維持
- `fasterxml-uuid` 依存（build.gradle.kts）: uuidV7Provider が唯一の参照元 → 依存も削除可能

## 実装手順

### ステップ1: 事前確認

1. `grep -r "UuidV7IdValue\|UuidValue\|UuidV7Provider\|uuidV7Provider" backend/ mizo432-shared-common/` を再実行し、参照が計画対象ファイルに限定されていることを確認
2. `grep -r "fasterxml.uuid"` で `fasterxml-uuid` ライブラリの参照先が uuidV7Provider のみであることを確認

### ステップ2: backend モジュールの削除

1. `UuidV7IdValue.java` を削除
2. `UuidValue.java` を削除
3. `UuidV7IdValueTest.java` を削除
4. `package-info.java` の javadoc から `UuidV7IdValue` への `@link` を削除（行12）

### ステップ3: mizo432-shared-common モジュールの削除

1. `uuidV7Provider` パッケージ全体（`com/github/mizo432/shared/common/uuidV7Provider/`）を削除
   - UuidV7Provider.java
   - FixedUuidV7IdProvider.java
   - RollingUuidV7IdProvider.java
   - UuidV7ProviderTest.java
   - FixedUuidV7IdProviderTest.java
   - RollingUuidV7IdProviderTest.java

### ステップ4: ビルド設定の整理

1. `mizo432-shared-common/build.gradle.kts` から `fasterxml-uuid` 依存を削除（uuidV7Provider が唯一の参照元の場合）

### ステップ5: ビルド検証

1. `./gradlew :mizo432-shared-common:build` — コンパイルエラーなしを確認
2. `./gradlew :backend:build` — コンパイルエラーなしを確認
3. `./gradlew :backend:test` — テスト 통과を確認

## リスクと対応

| リスク | 影響 | 対応 |
|-------|------|------|
| 他の #583 分割 Issue が完了していない | 予期せぬ参照が残っている可能性 | ステップ1で grep 再確認 |
| `fasterxml-uuid` の他の参照を見落としている | 依存削除でビルド失敗 | ステップ1で grep 確認し、参照があれば依存は維持 |
| `package-info.java` の修正漏れ | javadoc のリンク切れ | 修正対象を明確にリスト化 |

## 成功基準

- [ ] 対象ファイルがすべて削除されている
- [ ] `grep -r "UuidV7IdValue\|UuidValue\|UuidV7Provider\|uuidV7Provider"` で残参照がない（または正当な残参照のみ）
- [ ] `./gradlew :backend:build` が成功する
- [ ] `./gradlew :backend:test` が成功する
- [ ] Issue #590 に削除結果を記録する
