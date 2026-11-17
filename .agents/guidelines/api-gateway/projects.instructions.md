# api-gateway ディレクトリ - プロジェクト概要

## 概要

api-gatewayディレクトリは、modulith-learningプロジェクトのAPIゲートウェイです。クライアントからのリクエストをルーティングし、各サービスに分散する役割を果たします。

## アーキテクチャ

- **APIゲートウェイパターン**: 単一入口点としてのルーティングと集約
- **Spring Cloud Gateway**: リアクティブなゲートウェイフレームワーク
- **サービスディスカバリー**: Eurekaとの連携による動的ルーティング

## 主な機能

- リクエストのルーティング
- 負荷分散
- クロスオリジンリソース共有 (CORS)の設定
- リクエスト/レスポンスの変換

## 技術スタック

- Java 25 (OpenJDK)
- Spring Boot 4.1.x
- Spring Cloud Gateway
- Spring Cloud 2025.x (Eureka Client)

## テスト戦略

- **単体テスト (Small)**: ルーティング設定の検証
- **統合テスト (Medium)**: 実際のリクエストフローの検証
