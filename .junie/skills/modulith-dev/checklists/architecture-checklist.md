# アーキテクチャ＆モジュール設計チェックリスト (Architecture Checklist)

Spring Modulith およびオニオンアーキテクチャの規約を確認するためのチェックリストです。

### 1. Spring Modulith モジュール境界

- [ ] モジュール間の直接的な内部参照（`internal` パッケージへのアクセス）を行っていないか？
- [ ] 外部モジュールに公開するインターフェースや DTO は公開パッケージまたは `spi` パッケージに配置されているか？
- [ ] `ModulithTest.verifyPackageConformity()` を実行してモジュール循環や境界違反がないことを確認したか？

### 2. オニオンアーキテクチャ・層分離

- [ ] **Domain 層**:
    - 外部フレームワーク（Spring, JPA, HTTP）への直接依存がなく、ビジネスルールが保護されているか？
    - 値オブジェクトやエンティティの不変条件が `*Precondition` やファクトリメソッド等で保護されているか？
- [ ] **Business / Application 層**:
    - ドメインオブジェクトを協調させてユースケースを実現しているか？
- [ ] **Infrastructure 層**:
    - DBアクセス、外部API呼び出し、メッセージングなどの技術的関心事が実装されているか？
- [ ] **Presentation 層**:
    - REST コントローラーは薄く保たれ、適切なバリデーションと HTTP ステータス変換が行われているか？

### 3. 例外処理・ログ・コーディング規約

- [ ] 業務エラーには `BusinessException` / `NotFoundBusinessException` が適切に使われているか？
- [ ] システム障害には `SystemException` が使用され、内部エラー詳細がクライアントに過剰に漏洩していないか？
- [ ] Lombok（`@Getter`, `@Setter`, `@RequiredArgsConstructor` 等）および JSpecify（`@NonNull`,
  `@Nullable`）が適切に使用されているか？
- [ ] Google Java Style に準拠したフォーマットになっているか？
