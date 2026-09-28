---
name: pr-review
description: Pull Request (PR) の変更内容を、Spring Modulith境界、オニオンアーキテクチャ、テスト規約、Null安全性、例外処理、コードスタイルに基づいて多角的にレビュー・検証するスキル。
---

### PR レビュースキル (`pr-review`)

このスキルは、GitHub Issue および feature ブランチで実装された Pull Request (PR)
に対し、設計思想・アーキテクチャ・品質規約・テスト網羅性を厳格にレビューするためのガイドラインと手順です。

---

### 1. いつこのスキルを使用するか (When To Use)

- feature ブランチでの実装完了後に Pull Request (PR) を作成・レビューするとき
- 既存コードに対するリファクタリングや機能追加の差分（diff）を検証・査定するとき
- コードの品質、モジュール境界違反、テスト規約違反、Null 安全性の不備を検出・修正するとき

---

### 2. PR レビューの実行手順 (Review Workflow)

1. **変更差分の把握 (Diff Inspection)**:
    - `git diff main...<feature-branch>` または PR 差分を確認し、変更されたモジュール、ファイル、クラスを特定。
    - 関連する GitHub Issue の要件および受け入れ基準を満たしているか確認。

2. **アーキテクチャ & モジュール境界の検証 (Architecture & Modulith Check)**:
    - 他モジュールの `internal` パッケージを直接参照していないか。
    - 公開 API/SPI を介した連携になっているか。
    - オニオンアーキテクチャの層の依存方向（Domain ← Business ← Infrastructure /
      Presentation）が守られているか。

3. **テスト規約と網羅性の検証 (Testing Standards Check)**:
    - テストメソッド名は必ず `should` で開始され、アンダースコア（`_`）が含まれていないか。
    - テストクラスおよびテストメソッドの両方に日本語の `@DisplayName` が付与されているか。
    - テスト対象メソッドごとに `@Nested` クラスで構造化されているか。
    - テストクラス・ネストクラス・テストメソッドが `package-private`（修飾子なし）になっているか。
    - `null` 引数や空文字、境界値、異常系（例外スロー）のテストケースが網羅されているか。
    - テストタグ（`@Tag("small")`, `@Tag("medium")`, `@Tag("large")`）が適切に指定されているか。

4. **コーディング規約 & 安全性の検証 (Code Quality Check)**:
    - JSpecify アノテーション（`@NonNull`, `@Nullable`）による Null 安全性が明示されているか。
    - 引数・状態検証に `undecided.supporting.precondition.*`（`*Precondition`）が適切に使用されているか。
    - 業務エラー（`BusinessException`, `NotFoundBusinessException`）とシステム障害（`SystemException`
      ）が正しく分離されているか。
    - 不変データモデルに Java `record` が活用され、ボイラープレートに Lombok が適切に使われているか。
    - Google Java Style に準拠しているか。

5. **ビルドおよび自動テストの実行検証 (Build & Verification)**:
    - 単体テストおよび Spring Modulith 境界検証: `./gradlew :backend:test`
    - 統合テスト: `./gradlew :backend:mediumTest`
    - 全体ビルド: `./gradlew build`

6. **レビュー結果の出力 (Review Output)**:
    - 良い点（Good points）、要修正点（Required changes）、改善提案（Suggestions）を整理して提示。

7. **レビュー指摘事項の修正と再検証 (Fix & Re-verify)**:
    - レビューで指摘された要修正点を feature ブランチ上で修正。
    - 単体テスト・統合テスト（`./gradlew :backend:test`, `./gradlew :backend:mediumTest`
      ）を再実行してパスすることを確認。
    - 修正コミットを作成し、PR を更新。

---

### 3. レビュー観点チェックリスト (Review Checklist)

| カテゴリ                   | レビュー観点                            | 判定基準                                                                |
|:---------------------------|:----------------------------------------|:------------------------------------------------------------------------|
| **Spring Modulith**        | 他モジュールの `internal` 参照禁止      | 公開パッケージ / SPI のみ参照しているか                                 |
| **オニオンアーキテクチャ** | 層依存の方向性                          | Domain層が外部フレームワークやInfrastructureに依存していないか          |
| **テスト命名**             | `should` プレフィックスとキャメルケース | アンダースコアを使用せず `shouldReturn...` 形式になっているか           |
| **テスト DisplayName**     | 日本語 `@DisplayName` の付与            | クラスとメソッドの両方に具体的かつ簡潔な日本語説明があるか              |
| **テスト構造**             | `@Nested` による構造化                  | 対象メソッドごとにネストクラスが作成されているか                        |
| **テスト可視性**           | `package-private` 修飾子                | `public` / `protected` / `private` を付けずに定義されているか           |
| **テスト網羅性**           | 境界値・Null・異常系の検証              | `null`、空文字、境界値、例外発生時の AssertJ 検証が含まれているか       |
| **Null 安全性**            | JSpecify アノテーション                 | `@NonNull` / `@Nullable` が引数・戻り値に明示されているか               |
| **事前条件検証**           | `*Precondition` の利用                  | メソッド先頭で引数や状態の不変条件が保護されているか                    |
| **例外処理**               | 業務例外とシステム例外の分離            | 適切な例外クラスとステータスコードが対応づけられているか                |
| **ビルド検証**             | テスト・ビルドの全件通過                | `./gradlew :backend:test`, `./gradlew :backend:mediumTest` がパスするか |
