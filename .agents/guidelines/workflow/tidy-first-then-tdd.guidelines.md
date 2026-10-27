# Tidy First then TDD ワークフロー

本ガイドラインは、コード修正を行う際に「整理（Tidying）を3回行ってからTDDで実装する」ワークフローを定めます。Kent Beck著「Tidy First?」で提唱されるTidying技術を先に適用することで、コードの構造を改善し、その後にTDDで実装するプロセスを標準化します。

## 背景

コード修正を行う際、既存コードの質が低いと新しい機能の実装が困難になる場合がある。「Tidy First?」で提唱されるTidying技術を先に適用することで、コードの構造を改善し、その後にTDDで実装するワークフローを定めます。

## 目的

- コード修正前の整理（Tidying）を習慣化する
- Tidyingの結果を確実に記録し、チームで共有する
- 整理と実装を明確に分離し、変更の意図を明確にする

## 「整理」の定義

本ルールにおける「整理」とは、Kent Beck著「Tidy First?」書籍で定義された**Tidying技術**を指します。プロジェクトでは `.agents/guidelines/java/tidy-first.guidelines.md` に15のTidying技術が定義されています。

### 適用可能なTidying技術（15種）

1. Guard Clauses（ガード節）
2. Dead Code（デッドコード）
3. Normalize Symmetries（対称性の正規化）
4. New Interface, Old Implementation（新インターフェース、旧実装）
5. Reading Order（読み順）
6. Cohesion Order（凝集順）
7. Move Declaration And Initialization Together（宣言と初期化を一緒に移動）
8. Explaining Variables（説明変数）
9. Explaining Constants（説明定数）
10. Explicit Parameters（明示的なパラメータ）
11. Chunk Statements（チャンク文）
12. Extract Helper（ヘルパー抽出）
13. One Pile（一つの山）
14. Explaining Comments（説明コメント）
15. Delete Redundant Comments（冗長なコメントの削除）

## ワークフロー

### ステップ1: 修正対象コードの分析

修正が必要なコードを特定し、どのTidying技術を適用すべきか検討します。

### ステップ2: 整理を3回行う

修正対象のコードに対して、Tidying技術を**3回**適用します。

1. **1回目の整理**: コードの構造を改善するTidyingを適用
2. **2回目の整理**: 1回目の整理の結果を基に、さらに改善できる箇所をTidying
3. **3回目の整理**: 2回目の整理の結果を基に、最終的な整理を行う

各整理は独立したコミットとして記録します。

### ステップ3: 整理コミットの記録

各整理コミットは以下の形式でコミットメッセージを記述します。

```
tidy: <Tidying技術名>を<対象ファイル/クラス>に適用

<Tidying技術の詳細と変更内容>
```

例:
```
tidy: Guard ClausesをOrderServiceに適用

processOrderメソッドのネストを浅くし、メインロジックを明確にした。
```

### ステップ4: 整理ブランチのマージ

整理コミットは**PRを作成せずに**、直接developブランチにマージします。

```bash
git checkout develop
git merge <整理ブランチ名> --no-ff
```

### ステップ5: TDDで実装

整理が完了したコードに対して、TDD（Test-Driven Development）で新しい機能を実装します。

1. **Red**: 失敗するテストを書く
2. **Green**: テストが通る最小の実装を書く
3. **Refactor**: テストが通るままコードを改善

実装コミットは通常通りPRを作成し、レビューを経てマージします。

## ルール

### 整理コミットのルール

* **整理は毎回コミットする**: 各Tidying適用ごとに独立したコミットを作成する
* **コミットメッセージには「tidy:」プレフィックスを使用**: 整理コミットであることを明確にする
* **整理コミットにはテストが通っていること**: Tidying前後でテストが通ることを確認する

### 整理マージのルール

* **整理を行なった後はPRを作成せずにdevelopブランチにマージする**: 整理はコードの動作を変更しないため、PRレビューは不要
* **マージコミットは--no-ffオプションを使用**: 整理の変更が独立したコミットとして履歴に残るようにする

### 実装コミットのルール

* **整理後の実装は通常通りPRを作成**: 新機能の実装はPRレビューを経てマージする
* **実装コミットには「feat:」「fix:」などのプレフィックスを使用**: Conventional Commits形式に従う

## 例外ケース

以下の場合は、整理をスキップして直接実装を行うことができます：

- **緊急のバグ修正**: 迅速な対応が必要な場合
- **トリビアルな変更**: 1ファイル内の小さな修正で、Tidyingの適用が不要と判断された場合
- **新規ファイルの作成**: 既存コードが存在しない場合

## 参考資料

- ガイドライン: `.agents/guidelines/java/tidy-first.guidelines.md`
- 書籍: Kent Beck, 「Tidy First?」
