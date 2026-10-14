# 案件名・リポジトリ名など

## 前提条件

**いかなる場合も以下の条件は必ず把握し、遵守すること**

- 回答や返答、思考内容 (think)は全てユーザーが理解できるように日本語にすること
- ユーザーからの指示がない限り、`rm`/`mv`/`ln`/`git merge`/`terraform apply`
  などの破壊的変更を行うコマンドは使用せず、提案もしないこと
- システムディレクトリ (`/`)以下のディレクトリ・ファイルには書き込み・読み込みを行わず、それに関連した提案もしないこと

## 言語ごとの設定

### Java

実装する言語がJavaでspring bootを使用している場合以下のguidelinesを参照すること。

- [Java 用コーディングルールguidelines](./guidelines/java/codeing-standards.guidelines.md)
- [Java用レビューguidelines](./guidelines/java/review.guidelines.md)

### TypeScript

実装する言語がTypeScriptの場合以下のguidelinesを参照すること。

- [TypeScript用コーディングルールguidelines](./guidelines/TypeScript/codeing-standards.guidelines.md)
- [TypeScript用レビューguidelines](./guidelines/TypeScript/review.guidelines.md)

## モノレポディレクトリごとの設定

### backendディレクトリ

以下のguidelinesをあらかじめ把握しておくこと。

- [プロジェクト概要](./guidelines/backend/projects.guidelines.md): プロジェクト概要を記載。
- [開発者向け内容](./guidelines/backend/developers.guidelines.md): 技術スタックなどを記載。
- [環境構築方法](./guidelines/backend/environments.guidelines.md): ローカル環境構築方法などを記載。
- [使用コマンド使用方法](./guidelines/backend/commands.guidelines.md): 開発で使用するコマンドなどを記載。

### frontendディレクトリ

以下のguidelinesをあらかじめ把握しておくこと。

- [プロジェクト概要](./guidelines/frontend/projects.guidelines.md): プロジェクト概要を記載。
- [開発者向け内容](./guidelines/frontend/developers.guidelines.md): 技術スタックなどを記載。
- [環境構築方法](./guidelines/frontend/environments.guidelines.md): ローカル環境構築方法などを記載。
- [使用コマンド使用方法](./guidelines/frontend/commands.guidelines.md): 開発で使用するコマンドなどを記載。

### aoi-gatewayディレクトリ

以下のguidelinesをあらかじめ把握しておくこと。

- [プロジェクト概要](./guidelines/api-gateway/projects.guidelines.md): プロジェクト概要を記載。
- [開発者向け内容](./guidelines/api-gateway/developers.guidelines.md): 技術スタックなどを記載。
- [環境構築方法](./guidelines/api-gateway/environments.guidelines.md): 主にterraformによるAWS環境構築方法などを記載。
- [使用コマンド使用方法](./guidelines/api-gateway/commands.guidelines.md): 開発で使用するコマンドなどを記載。

### authorizationディレクトリ

以下のguidelinesをあらかじめ把握しておくこと。

- [プロジェクト概要](./guidelines/authorization/projects.guidelines.md): プロジェクト概要を記載。
- [開発者向け内容](./guidelines/authorization/developers.guidelines.md): 技術スタックなどを記載。
- [環境構築方法](./guidelines/authorization/environments.guidelines.md): ローカル環境構築方法などを記載。
- [使用コマンド使用方法](./guidelines/authorization/commands.guidelines.md): 開発で使用するコマンドなどを記載。

### servie-registryディレクトリ

以下のguidelinesをあらかじめ把握しておくこと。

- [プロジェクト概要](./guidelines/servie-registry/projects.guidelines.md): プロジェクト概要を記載。
- [開発者向け内容](./guidelines/servie-registry/developers.guidelines.md): 技術スタックなどを記載。
- [環境構築方法](./guidelines/servie-registry/environments.guidelines.md): ローカル環境構築方法などを記載。
- [使用コマンド使用方法](./servie-registry/serverless/commands.guidelines.md): 開発で使用するコマンドなどを記載。
