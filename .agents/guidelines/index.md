# modulith-learning プロジェクトガイドライン

## 前提条件

**いかなる場合も以下の条件は必ず把握し、遵守すること**

- 回答や返答、思考内容 (think)は全てユーザーが理解できるように日本語にすること
- ユーザーからの指示がない限り、`rm`/`mv`/`ln`/`git merge`/`terraform apply`
  などの破壊的変更を行うコマンドは使用せず、提案もしないこと
- システムディレクトリ (`/`)以下のディレクトリ・ファイルには書き込み・読み込みを行わず、それに関連した提案もしないこと

## 言語ごとの設定

### Java

実装する言語がJavaでSpring Bootを使用している場合、以下のガイドラインを参照すること。

- [Java 用コーディングルールガイドライン](./java/codeing-standards.guidelines.md)
- [Java 用レビューガイドライン](./java/review.guidelines.md)

### TypeScript

実装する言語がTypeScriptの場合、以下のガイドラインを参照すること。

- [TypeScript 用コーディングルールガイドライン](./typescript/codeing-standards.guidelines.md)
- [TypeScript 用レビューガイドライン](./typescript/review.guidelines.md)

## モノレポディレクトリごとの設定

### backend ディレクトリ

以下のガイドラインをあらかじめ把握しておくこと。

- [プロジェクト概要](./backend/projects.instructions.md): プロジェクト概要を記載。
- [開発者向け内容](./backend/developers.instructions.md): 技術スタックなどを記載。
- [環境構築方法](./backend/environments.instructions.md): ローカル環境構築方法などを記載。
- [使用コマンド使用方法](./backend/commands.instructions.md): 開発で使用するコマンドなどを記載。

### frontend ディレクトリ

以下のガイドラインをあらかじめ把握しておくこと。

- [プロジェクト概要](./frontend/projects.instructions.md): プロジェクト概要を記載。
- [開発者向け内容](./frontend/developers.instructions.md): 技術スタックなどを記載。
- [環境構築方法](./frontend/environments.instructions.md): ローカル環境構築方法などを記載。
- [使用コマンド使用方法](./frontend/commands.instructions.md): 開発で使用するコマンドなどを記載。

### api-gateway ディレクトリ

以下のガイドラインをあらかじめ把握しておくこと。

- [プロジェクト概要](./api-gateway/projects.instructions.md): プロジェクト概要を記載。
- [開発者向け内容](./api-gateway/developers.instructions.md): 技術スタックなどを記載。
- [環境構築方法](./api-gateway/environments.instructions.md): 環境構築方法などを記載。
- [使用コマンド使用方法](./api-gateway/commands.instructions.md): 開発で使用するコマンドなどを記載。

### authorization ディレクトリ

以下のガイドラインをあらかじめ把握しておくこと。

- [プロジェクト概要](./authorization/projects.instructions.md): プロジェクト概要を記載。
- [開発者向け内容](./authorization/developers.instructions.md): 技術スタックなどを記載。
- [環境構築方法](./authorization/environments.instructions.md): ローカル環境構築方法などを記載。
- [使用コマンド使用方法](./authorization/commands.instructions.md): 開発で使用するコマンドなどを記載。

### service-registry ディレクトリ

以下のガイドラインをあらかじめ把握しておくこと。

- [プロジェクト概要](./service-registry/projects.instructions.md): プロジェクト概要を記載。
- [開発者向け内容](./service-registry/developers.instructions.md): 技術スタックなどを記載。
- [環境構築方法](./service-registry/environments.instructions.md): ローカル環境構築方法などを記載。
- [使用コマンド使用方法](./service-registry/commands.instructions.md): 開発で使用するコマンドなどを記載。
