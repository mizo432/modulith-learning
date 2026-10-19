# GitHub Issue ベースの開発手順 (GitHub Issue-Driven Development Workflow)

開発作業（機能追加、リファクタリング、バグ修正、ドキュメント更新など）は、原則として **GitHub Issue**
を起点として以下のフローで実施します。

## 1. 開発フロー

### 1.1 Issue の選定・確認 (Issue Identification & Scope)

- 対象とする GitHub Issue の番号・タイトル・本文を確認し、解決すべき課題・要件・受け入れ基準を明確化します。
- 関連するモジュールや影響範囲を特定します。
- 要件や受け入れ基準が曖昧な場合は、要求分析スキル (`requirement-analysis`) で要件を整理してから着手します。

### 1.2 feature ブランチの作成 (Branching)

- 最新のメインブランチから作業用の feature ブランチを作成して切り替えます。
- ブランチ命名規則の例: `feature/issue-<number>-<short-description>` (例:
  `feature/issue-139-prefecture-import`)

### 1.3 設計・方針検討 (Design & Approach)

- Spring Modulith のモジュール境界（公開 API/SPI と internal の分離）およびオニオンアーキテクチャの層構造に準拠した設計を行います。
- 必要な単体テスト（Small）および統合テスト（Medium）のテストケースを策定します。

### 1.4 実装とテスト作成 (Implementation & Testing)

- プロジェクト規約（Google Java Style、Lombok、JSpecify、`undecided.supporting.precondition.*`
  による事前条件検証、例外処理方針）に従って実装します。
- テスト作成規約（`should` 命名、日本語 `@DisplayName`、`@Nested` 構造、`package-private`
  可視性、境界値/異常系網羅）に従いテストコードを作成します。

### 1.5 検証の実行 (Verification)

- `./gradlew :backend:test`（単体テストおよび Modulith 境界検証）を実行します。
- 必要に応じて `./gradlew :backend:mediumTest`（統合テスト）を実行し、全テストの成功を確認します。

### 1.6 Issue 番号の紐付けとコミット (Linking & Commit)

- コミットメッセージに対応する Issue 番号（例: `refs #XX`, `closes #XX`）を明記してコミットします。

### 1.7 プルリクエストの作成 (Pull Request Creation)

- 作業完了後、作業ブランチからメインブランチ（または指定のベースブランチ）に向けて Pull Request (PR)
  を作成します。
- PR 本文には変更概要、関連 Issue 番号（`closes #XX` / `fixes #XX` ）、実施したテスト・検証結果を明記します。

### 1.8 レビューSKILLによるPRレビュー (PR Review with Review Skill)

- PR 作成後は、PR レビュースキル (`pr-review`) を使用して変更内容を多角的にレビューします。
- モジュール境界（Spring Modulith）、オニオンアーキテクチャ、テスト規約（`should` 命名、日本語
  `@DisplayName`、`@Nested` 構造、境界値検証）、Null 安全性（JSpecify）、例外処理、Google Java Style
  の準拠状況を検証・確認します。

### 1.9 レビュー指摘の修正と3回の反復検証 (Review Feedback Resolution & 3-Iteration Cycle)

- PR レビューで検出された要修正点（モジュール境界違反、テスト規約違反、Null 安全性、例外処理等の指摘事項）を
  feature ブランチ上で速やかに修正します。
- 修正後、再度 `./gradlew :backend:test` および `./gradlew :backend:mediumTest`
  を実行して全テストが成功することを確認し、追加コミット・PR更新を行います。
- **レビューと指摘事項修正のサイクルは3回繰り返して実施**
  し、潜在的な不具合や設計上の課題を段階的に洗練・排除して、開発効率と長期的な品質・保守性の便益を最適化します。
