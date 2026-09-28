# テスト品質チェックリスト (Test Quality Checklist)

テストコードを新規作成または改修する際は、以下の項目をすべて満たしているか確認してください。

### 1. アノテーション・設定

- [ ] `@Tag("small")` / `@Tag("medium")` / `@Tag("large")` が正しく付与されているか？
    - 単体テスト（Spring起動なし）: `@Tag("small")`
    - 統合テスト（DB/Spring起動あり）: `@Tag("medium")`
    - システム/E2E/負荷テスト: `@Tag("large")`
- [ ] テストクラスに日本語の `@DisplayName` が付与されているか？

### 2. クラス・メソッド構造

- [ ] テストクラスの可視性が `package-private`（修飾子なし）になっているか？
- [ ] テスト対象メソッドごとに `@Nested` クラスでグループ化されているか？
- [ ] 各 `@Nested` クラスに日本語の `@DisplayName` が付与されているか？
- [ ] 各テストメソッドに日本語の `@DisplayName` が付与されているか？
- [ ] テストメソッドの可視性が `package-private`（修飾子なし）になっているか？

### 3. メソッド命名規則

- [ ] テストメソッド名は必ず `should` で始まっているか？
- [ ] キャメルケースが使用され、アンダースコア（`_`）が含まれていないか？
    - 良い例: `shouldReturnTrueWhenInputIsNull()`, `shouldThrowExceptionWhenArgumentIsEmpty()`
    - 悪い例: `test_null()`, `should_return_true()`

### 4. アサーションとテスト網羅性

- [ ] `AssertJ` (`assertThat`, `assertThatThrownBy`) を使用しているか？
- [ ] `null` 引数や境界値（空文字、最大値、最小値、空コレクションなど）のテストが含まれているか？
- [ ] 期待される例外の型やメッセージが適切に検証されているか？
- [ ] アサーションの理由や説明が `.as("...")` で付与されているか？

### 5. 実行確認

- [ ] `./gradlew :backend:test` でテストが正常にパスすることを確認したか？
