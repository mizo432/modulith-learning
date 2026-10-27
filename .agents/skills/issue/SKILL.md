---
name: issue
description: |
  RDRAモデルからの指示内容を元に、GitHubリポジトリにissueを作成する。
  発火条件是、ユーザーが新規 Issue の作成を依頼している場合に限る。
  命令に「issue」または「イシュー」のキーワードが含まれるだけでは発火せず、
  既存 Issue の参照・確認・編集・クローズなどの操作には発火しない。
---

# issue

## When to Use (いつ使うべきか)

- issueを新規作成するとき

## Quick Start (処理フロー)

1. ユーザーにissueの内容を聞く
2. issue内容を把握する
3. issue内容を元にissueタイトルを作成
4. issue内容を元に`Label`を推定 (GitHubで用意されているものを使用)
5. テンプレートmarkdownに沿うようにissueの本文をまとめる
6. ghコマンドを用いてissueをGitHubリポジトリに作成

## Guidelines (ガイドライン)

- issue新規作成時の`Assignees`にはユーザーだけを指定すること
- issueタイトルには内容に応じて以下のprefixをつけること
    - `feat`: 新規機能追加
    - `fix`: 既存機能修正 (バグ修正など)
    - `docs`: ドキュメント追加・編集
    - `style`: 空白・フォーマット・セミコロンなどの追加・修正 (静的解析反映)
    - `refactor`: 仕様に影響がないコード改善 (リファクタ)
    - `perf`: パフォーマンス向上関連
    - `test`: テストコードの追加・修正・デバッグ関連
    - `chore`: ビルド・補助ツール・ライブラリ追加や更新

## Templates (実装テンプレート)

[issueテンプレート](./templates/issue.md)
