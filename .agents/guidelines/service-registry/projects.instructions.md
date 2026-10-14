# service-registry ディレクトリ - プロジェクト概要

## 概要

service-registryディレクトリは、modulith-learningプロジェクトのサービスレジストリです。Netflix
Eurekaを使用したサービスディスカバリー機能を提供し、各サービスの登録・発見を管理します。

## アーキテクチャ

- **サービスディスカバリー**: Netflix Eurekaによるサービスの自動登録と発見
- **高可用性**: ピアツーピアレプリケーションによるフォールトトレランス
- **ヘルスチェック**: 定期 heartbeat によるサービス死活監視

## 主な機能

- サービスの登録・ deregister
- サービスの発見
- 負荷分散との連携
- ヘルスチェックの監視

## 技術スタック

- Java 25 (OpenJDK)
- Spring Boot 4.1.x
- Spring Cloud 2025.x (Eureka Server)

## テスト戦略

- **単体テスト (Small)**: 設定の検証
- **統合テスト (Medium)**: サービス登録・発見のフロー検証
