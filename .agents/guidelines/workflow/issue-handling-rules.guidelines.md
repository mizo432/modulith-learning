# Issueへの対応ルール

本ガイドラインは、コード修正を含むIssue対応における標準ワークフローを定義します。
Tidy First?に基づく整理を3回実施した後、TDDで実装し、PRを作成してレビューするプロセスを定めます。
このルールにより、コード品質を段階的に高め、チーム開発の効率と保守性を最適化します。
既存コードの質が低い場合でも、整理を先に適用することで安全に新機能を実装できる環境を整備します。

## 背景

コード修正を行う際、既存コードの質が低いと新しい機能の実装が困難になる場合がある。
「Tidy First?」で提唱されるTidying技術を先に適用することで、コードの構造を改善し、その後にTDDで実装するワークフローを定めます。

## 目的

- Issue対応における標準的なワークフローを確立する
- コード修正前の整理（Tidying）を習慣化する
- Tidyingの結果を確実に記録し、チームで共有する
- 整理と実装を明確に分離し、変更の意図を明確にする
- PRレビューを通じてコード品質を確保する

## ワークフロー全体

### ステップ1: Issueの詳細化

対応するIssueの本文を詳細化する。

- 要件・受け入れ基準・影響範囲を明確にする
- 関連するモジュールやコンポーネントを特定する
- 実装に必要な技術調査を行う

### ステップ2: コード修正前の整理（Tidying）× 3回

修正対象のコードに対して、Tidying技術を**3回**適用します。

- 各整理はTidy First?ガイドライン（`.agents/guidelines/java/tidy-first.guidelines.md`）に基づく
- 整理の定義：Kent Beck著「Tidy First?」で定義された15種のTidying技術を適用する
- 各整理後にはコミットを行う（`refactor:` prefixを使用）
- 各整理後にはorigin/developブランチにプッシュする（PRは作成しない）

**3回の整理サイクル：**

1. **1回目の整理**: コードの構造を改善するTidyingを適用 → origin/developブランチにプッシュ
2. **2回目の整理**: 1回目の整理の結果を基に、さらに改善できる箇所をTidying → origin/developブランチにプッシュ
3. **3回目の整理**: 2回目の整理の結果を基に、最終的な整理を行う → origin/developブランチにプッシュ

### ステップ3: Issueに対応する（TDD）

整理が完了したコードに対して、TDD（Test-Driven Development）で新しい機能を実装します。

- TDDガイドライン（`.agents/guidelines/tdd/tdd-guidelines.md`）に従って実装する
- Red-Green-Refactorサイクルを厳守する
- テストリストを作成し、最も単純なケースから順に実装する
- コミットメッセージにはIssue番号を明記する（`refs #XX`, `closes #XX`）

### ステップ4: PR作成

Issue-Driven Developmentガイドライン（`.agents/guidelines/workflow/issue-driven-development.guidelines.md`）に従う。

- featureブランチからdevelopブランチに向けてPRを作成する
- PR本文には変更概要、関連Issue番号、実施したテスト・検証結果を明記する

### ステップ5: PRレビュー

PRレビュースキル（`pr-review`）を使用して変更内容を多角的にレビューする。

- モジュール境界（Spring Modulith）、テスト規約、Null安全性（JSpecify）、例外処理、Google Java Styleの準拠を検証する
- レビューで検出された指摘事項を修正する

## 適用対象

- コード修正を伴う全てのIssue対応
- 新規機能追加、バグ修正、リファクタリングなど

## 除外対象

以下の場合は、本ルールを適用しません：

- ドキュメントみの更新
- 設定ファイルのみの変更
- テストコードのみの追加・修正（既存テストの修正を除く）

## 整理コミットのルール

- **整理は毎回コミットする**: 各Tidying適用ごとに独立したコミットを作成する
- **コミットメッセージには「refactor:」プレフィックスを使用**: 整理コミットであることを明確にする
- **整理コミットにはテストが通っていること**: Tidying前後でテストが通ることを確認する
- **各整理後にはorigin/developブランチにプッシュする**: 整理の変更を確実に記録する

## 実装コミットのルール

- **整理後の実装は通常通りPRを作成**: 新機能の実装はPRレビューを経てマージする
- **実装コミットには「feat:」「fix:」などのプレフィックスを使用**: Conventional Commits形式に従う
- **コミットメッセージにはIssue番号を明記する**: 変更とIssueの関連を明確にする

## 例外ケース

以下の場合は、整理をスキップして直接実装を行うことができます：

- **緊急のバグ修正**: 迅速な対応が必要な場合
- **トリビアルな変更**: 1ファイル内の小さな修正で、Tidyingの適用が不要と判断された場合
- **新規ファイルの作成**: 既存コードが存在しない場合

## 環境

- ブランチ: develop
- ガイドライン参照:
  - `.agents/guidelines/tdd/tdd-guidelines.md`（TDDガイドライン）
  - `.agents/guidelines/java/tidy-first.guidelines.md`（Tidy First?ガイドライン）
  - `.agents/guidelines/workflow/issue-driven-development.guidelines.md`（Issue-Driven Developmentガイドライン）

## 懸念事項

- 整理の各フェーズでテストが通ることを確認する必要がある
- 3回の整理サイクルにより、作業時間が長くなる可能性があるが、長期的な品質向上に寄与する

## 参考資料

- 書籍: Kent Beck, 「Tidy First?」
- ガイドライン: `.agents/guidelines/java/tidy-first.guidelines.md`
- ガイドライン: `.agents/guidelines/tdd/tdd-guidelines.md`
- ガイドライン: `.agents/guidelines/workflow/issue-driven-development.guidelines.md`
