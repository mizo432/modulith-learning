# GitHub Issue ベースの開発手順 (GitHub Issue-Driven Development Workflow)

開発作業（機能追加、リファクタリング、バグ修正、ドキュメント更新など）は、原則として **GitHub Issue**
を起点として以下のフローで実施します。

## 1. 開発フロー

### 1.1 Issue の詳細化 (Issue Detailing)

- 対象とする GitHub Issue の本文を詳細化します。
- 要件・受け入れ基準・影響範囲を明確にします。
- 関連するモジュールやコンポーネントを特定します。
- Issue #338「Issueへの対応ルール」に従って、Issue本文に必要な情報を補完します。

### 2. コード修正前の整理（Tidying）× 3回

- 各整理はTidy First?ガイドライン（`.agents/guidelines/java/tidy-first.guidelines.md`）に基づきます。
- 整理の定義：Kent Beck著「Tidy First?」で定義された15種のTidying技術を適用します。
- 各整理後にはコミットを行います（`refactor:` prefixを使用）。
- 各整理後にはorigin/developブランチにプッシュします（PRは作成しない）。
- 3回の整理サイクル：
    1. 整理（1回目）→ origin/developブランチにプッシュ
    2. 整理（2回目）→ origin/developブランチにプッシュ
    3. 整理（3回目）→ origin/developブランチにプッシュ

### 3. Issueに対応する（TDD）

- TDDガイドライン（`.agents/guidelines/tdd/tdd-guidelines.md`）に従って実装します。
- Red-Green-Refactorサイクルを厳守します。
- テストリストを作成し、最も単純なケースから順に実装します。
- コミットメッセージにはIssue番号を明記します（`refs #XX`, `closes #XX`）。

### 4. PR作成

- featureブランチからdevelopブランチに向けてPRを作成します。
- PR本文には変更概要、関連Issue番号、実施したテスト・検証結果を明記します。

### 5. PRレビュー

- PRレビュースキル（`pr-review`）を使用して変更内容を多角的にレビューします。
- モジュール境界（Spring Modulith）、テスト規約、Null安全性（JSpecify）、例外処理、Google Java
  Styleの準拠を検証します。
- レビューで検出された指摘事項を修正します。
