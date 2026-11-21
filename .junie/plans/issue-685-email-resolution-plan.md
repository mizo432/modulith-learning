---
sessionId: session-261121-103742-1not
---

# Testing

## Validation Approach

- **small テスト**を基本とし、Mockito で依存をモック
- **medium テスト**は永続化が関わる部分のみ（SimpleEmployee の party_id 保存確認）
- 「〜したとき、〜となる」形式でテストケースを記述

## テストリスト

### SimpleEmployee（small）
1. partyId を指定して従業員を作成したとき、partyId が設定される
2. partyId を null で従業員を作成したとき、partyId が null となる
3. partyId を更新したとき、新しい partyId が反映される
4. 氏名などを更新したとき、既存の partyId は維持される

### EmployeeFactoryImpl（small）
5. partyId を持つ EmployeeDto から SimpleEmployee を作成したとき、partyId が設定される
6. partyId が null の EmployeeDto から作成したとき、partyId が null となる

### CreateSimpleEmployeeCommandService（small）
7. 従業員を作成したとき、Factory が EmployeeDto から SimpleEmployee を作成する
8. 従業員を作成したとき、SimpleEmployee に partyId が設定される

### UpdateSimpleEmployeeCommandService（small）
9. 従業員を更新したとき、SimpleEmployee の partyId が更新される
10. 従業員を更新したとき、既存の partyId が新しい値で上書きされる

### EmployeeEmailQueryService（small）
11. partyId と EMAIL 型の ContactUse が存在する従業員でメール解決したとき、メールアドレスが返される
12. 複数の EMAIL 型 ContactUse が存在する従業員でメール解決したとき、whenOccured が最新のものが返される
13. partyId が null の従業員でメール解決したとき、Optional.empty() が返される
14. EMAIL 型の ContactUse が存在しない従業員でメール解決したとき、Optional.empty() が返される
15. 存在しない employeeId でメール解決したとき、Optional.empty() が返される
16. ContactUse の EmailAddress が空のとき、Optional.empty() が返される

### AddBacklogItemCommentCommandService（small）
17. 投稿者IDがありメールが解決できる場合でコメントを投稿したとき、authorEmail が解決された値で保存される
18. 投稿者IDがありメールが解決できない場合でコメントを投稿したとき、authorEmail が空文字で保存される
19. 投稿者IDが null でコメントを投稿したとき、authorEmail が空文字で保存される
20. メール解決で例外が発生したとき、コメントは正常に保存され authorEmail は空文字となる

### CreateSimpleEmployeeCommandService（medium）
21. 従業員を作成したとき、DB の employees テーブルに party_id が保存される
22. 従業員を作成したとき、DB の employees テーブルの party_id は Person の partyId と一致する

### SimpleEmployee（medium）
23. party_id が設定された従業員を保存・読み込みしたとき、partyId が正しく復元される

# Delivery Steps

###   Step 1: Stage 1: FlywayマイグレーションとSimpleEmployeeにpartyIdを追加
employees テーブルに party_id カラムを追加し、SimpleEmployee エンティティに partyId フィールドを追加する。

- `V20261121090000__add_party_id_to_employees.sql` を作成（`ALTER TABLE employees ADD COLUMN party_id BIGINT;`）
- `SimpleEmployee.java` に `@Column(name = "party_id")` の `SnowflakeId partyId` フィールドを追加（nullable）
- `create()` メソッドに `SnowflakeId partyId` パラメータを追加
- `update()` メソッドに `partyId` の更新ロジックを追加
- `SimpleEmployeeTest.java` を更新して partyId 関連のテストケースを追加
- `EmployeeFactoryImpl.java` を更新して `employeeDto.person().getPartyId()` を `SimpleEmployee` に設定
- `UpdateSimpleEmployeeCommandService.java` を更新して `simpleEmployee.update()` に `partyId` を渡す
- `EmployeeFactoryImplTest.java` (small) を新規作成
- 既存の small テストを更新

###   Step 2: Stage 2: EmployeeEmailQuery SPIと実装を追加
relationshipMgmt の SPI に従業員メール解決クエリを追加し、内部実装を提供する。

- `EmployeeEmailQuery.java` インターフェースを `relationshipMgmt.spi.employee` に追加（`Optional<String> resolveEmailByEmployeeId(SnowflakeId)`）
- `EmployeeEmailQueryService.java` を `relationshipMgmt.personRole.internal.query` に実装
  - `EmployeeRepository.findById()` で SimpleEmployee を取得
  - `partyId` が null なら `Optional.empty()` を返す
  - `ContactUseQuery.findByPartyId()` で連絡先一覧を取得
  - `ContactUseType.EMAIL` 型にフィルタ
  - `whenOccured` が最新のものを選択
  - `EmailAddress.value()` を返す（空なら `Optional.empty()`）
- `EmployeeEmailQueryServiceTest.java` (small) を新規作成
  - 正常系：メール解決、複数ContactUseから最新選択
  - 異常系：partyIdがnull、EMAIL型なし、employeeIdなし、EmailAddressが空

###   Step 3: Stage 3: AddBacklogItemCommentCommandServiceでメール解決を統合
コメント投稿時に従業員メールを解決し、authorEmailを自動設定する。

- `AddBacklogItemCommentCommandService.java` に `EmployeeEmailQuery` を依存注入
- `execute()` で `authorEmployeeId` が設定されている場合、`EmployeeEmailQuery.resolveEmailByEmployeeId()` でメール解決を試む
- 解決できた場合は `command.getAuthorEmail()` の代わりに解決した値を使用
- 解決できない場合（Optional.empty()）は null のまま（現行の空文字処理に委ねる）
- 例外発生時もコメント保存は成功し、authorEmail は空文字となる
- `AddBacklogItemCommentCommandServiceTest.java` (small) を更新してメール解決関連のテストケースを追加
- `AddBacklogItemCommentCommandServiceEventTest.java` (small) を更新
- `CreateSimpleEmployeeCommandServiceMediumTest.java` (medium) を更新して party_id の保存を確認

###   Step 4: Stage 4: 全テスト検証と計画ドキュメント保存
すべてのテストが通過することを確認し、計画を .junie/plans/issue-685-plan.md に保存する。

- `./gradlew :backend:test` で small テストが全件通過することを確認
- `./gradlew :backend:mediumTest` で medium テストが全件通過することを確認
- 計画を `.junie/plans/issue-685-plan.md` に保存