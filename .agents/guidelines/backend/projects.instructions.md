# backend ディレクトリ - プロジェクト概要

## 概要

backendディレクトリは、modulith-learningプロジェクトのコア業務ロジック、データ永続化、REST
APIを提供するSpring Modulithアプリケーションです。

## アーキテクチャ

- **モジュラーモノリスアーキテクチャ**: Spring Modulithを活用したモジュール化されたモノリス設計
- **ドメイン駆動設計 (DDD)**: ビジネスロジックを外部フレームワークから隔離
- **オニオンアーキテクチャ**: 層ごとの責務分離と依存性逆転の原則

## 主なモジュール

- **汎用・業務モジュール**:
    - `addressReg`: 住所管理（都道府県・市区町村・町字）
    - `calendarReg`: カレンダー管理（祝日）
    - `bankReg`: 銀行・支店管理
    - `relationshipMgmt`: 顧客・組織・従業員関係管理
- **業務ドメインモジュール**:
    - `cashSaleMgmt`: 現金販売管理
    - `customerAccountMgmt`: 顧客口座管理
    - `shared`: 共通モジュール
- **ERPモジュール**:
    - 業務処理・メッセージング機能
- **基盤・共通モジュール**:
    - `snowflake`: 分散ID生成

## 技術スタック

- Java 25 (OpenJDK)
- Spring Boot 4.1.x
- Spring Modulith 2.1.x
- Spring Data JPA
- PostgreSQL
- Flyway (DBマイグレーション)
- Spring Batch (バッチ処理)

## テスト戦略

- **単体テスト (Small)**: Springコンテキスト起動なし、高速実行
- **統合テスト (Medium)**: Testcontainers + PostgreSQL 17
- **システムテスト (Large)**: E2E、パフォーマンステスト
