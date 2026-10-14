# authorization ディレクトリ - プロジェクト概要

## 概要

authorizationディレクトリは、modulith-learningプロジェクトの認証・認可サービスです。ユーザーの認証、アクセス制御、セキュリティ管理を担当します。

## アーキテクチャ

- **OAuth 2.0 / OpenID Connect**: 標準的な認証プロトコルの採用
- **JWT (JSON Web Tokens)**: トークンベースの認証
- **Spring Security**: セキュリティフレームワーク

## 主な機能

- ユーザー認証
- トークンの発行・検証
- ロールベースのアクセス制御
- セッション管理

## 技術スタック

- Java 25 (OpenJDK)
- Spring Boot 4.1.x
- Spring Security
- Spring Cloud 2025.x (Eureka Client)
- JWTライブラリ

## テスト戦略

- **単体テスト (Small)**: 認証ロジックの検証
- **統合テスト (Medium)**: 実際の認証フローの検証
