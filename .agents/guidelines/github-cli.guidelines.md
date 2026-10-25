---
agents:
  - primary
  - general-purpose
  - custom
---

# GitHub CLI (gh) 使用ガイドライン

## 概要

このプロジェクトでは、macOSバージョンの制限によりHomebrewでghをインストールできないため、
プロジェクトルートに保存されている `gh_2.103.2_macOS_arm64.zip` を使用してGitHub CLIを実行する。

## 使用方法

### 1. zipファイルの解凍と実行

```bash
# 解凍して実行
unzip -o gh_2.103.2_macOS_arm64.zip -d /tmp/gh-cli
/tmp/gh-cli/gh_2.103.2_macOS_arm64/bin/gh <コマンド>
```

### 2. 簡易的な使用方法

```bash
# 解凍済みのバイナリを直接使用
/tmp/gh-cli/gh_2.103.2_macOS_arm64/bin/gh pr create --base develop --title "タイトル" --body "本文"
/tmp/gh-cli/gh_2.103.2_macOS_arm64/bin/gh pr list
/tmp/gh-cli/gh_2.103.2_macOS_arm64/bin/gh issue list
```

## 重要なポイント

1. **Homebrewでのインストールは行わない**: macOS 27.0.1はHomebrewをサポートしていないため、`brew install gh`は失敗する。
2. **zipファイルの場所**: プロジェクトルートの `gh_2.103.2_macOS_arm64.zip` を使用する。
3. **解凍先**: `/tmp/gh-cli` に解凍する（セッション間でクリアされる可能性があるが、再解凍は簡単）。
4. **バージョン**: gh version 2.103.2 (2026-10-23)

## 使用例

### プルリクエストの作成
```bash
unzip -o gh_2.103.2_macOS_arm64.zip -d /tmp/gh-cli
/tmp/gh-cli/gh_2.103.2_macOS_arm64/bin/gh pr create \
  --base develop \
  --title "PRタイトル" \
  --body "PR本文"
```

### Issueの一覧表示
```bash
/tmp/gh-cli/gh_2.103.2_macOS_arm64/bin/gh issue list
```

### コミット履歴の確認
```bash
/tmp/gh-cli/gh_2.103.2_macOS_arm64/bin/gh pr view <PR番号> --json commits
```