# 複数のプロジェクトのScrum開発を管理するシステム

## プロジェクト概要

このプロジェクトは、Spring Modulithアーキテクチャを学習・実践するためのデモンストレーションプロジェクトです。モジュラーモノリス（Modular
Monolith）アプローチを採用し、マイクロサービスとモノリシックアプリケーションの利点を組み合わせています。

### 主な目的

- Spring Modulithフレームワークの学習と実践
- モジュラーモノリスアーキテクチャの実装
- ドメイン駆動設計（DDD）の原則の適用
- オ尼オンアーキテクチャ_PATTERNの実装

### システム構成

このシステムは以下の主要コンポーネントで構成されています：

1. **バックエンド（Backend）**：
    - Spring Boot 3.4.3ベースのアプリケーション
    - Spring Modulithを使用したモジュール化
    - PostgreSQLデータ баз
    - JPA/Hibernateによるデータアクセス
    - Flywayによるデータ базマイグレ�ーション

2. **APIゲートウェイ（API Gateway）**：
    - クライアントリクエストのルーティングと管理

3. **サービスレジストリ（Service Registry）**：
    - Netflix Eurekaを使用したサービスディスカバリー

4. **フロントエンド（Frontend）**：

- Reactベースのアプリケーション
- SPAとして作成する
- コンポーネントライブラリーとして最新のMUIを使用する

### モジュール構造

バックエンド примененияは以下のモジュールで構成されています：

- **関係管理（Relationship）**：
    - ビジネス関係の管理機能
    - クリーンアーキテクチャに基づく内部構造（ビジネス、ドメイン、インフラ、プレゼンテーション層）

- **共通（Common）**：
    - 共通ユーティリティと設定
    - 例外処理
    - ロギング機能

- **共有（Shared）**：
    - モジュール間で共有されるコンポーネント

### 技術スタック

- **言語**：Java 21
- **フレームワーク**：
    - Spring Boot 3.4.3
    - Spring Modulith
    - Spring Security
    - Spring Data JPA
    - Spring Cloud (Netflix Eureka)
- **データベース**：PostgreSQL
- **マイグレーション**：Flyway
- **テスト**：JUnit 5（小、中、大規模テスト）
- **ドキュメント**：OpenAPI/Swagger
- **その他**：
    - Lombok
    - Guava
    - ICU4J
    - Caffeine（キャッシュ）
    - libphonenumber

### 開発アプローチ

このプロジェクトは、Scrumフレームワークを使用して複数のプロジェクトの開発を管理するシステムとして設計されています。モジュラーモノリスアプローチにより、以下の利点を実現しています：

- モジュール間の明確な境界
- 疸結合と高凝集
- 独立したデプロイの可能性
- マイクロサービスへの段階的な移行パス

### コンテナ化

Dockerfileが提供され、アプリケーションのコンテナ化とデプロイが可能です。

## 新增追加（ Feature Flags, テスト・ strateg, etc.）

### 特徴 flags

- **APIリミット**：API call limitを管理
- **ロールbasedアクセス制御**：RBACを実装
- **バックエンドのリabilit**：Backendのリabilを管理
- **デプロイスcheduling**：デプロイのスケジュール管理

### テスト・strateg

- **单元テスト**：JUnitを基盤とした单元テスト
- ** Integration tests**：APIやサービス之间的 inte gra tion test
- **性能测试**：Loload, JMeter等で性能を測定
- **安全性测试**：Security scanと vulnerability testing

### DevOps プ拉グイン

- **Ansible/Inventory**：CI/CD pipelineを管理
- **Dockerfile**：docker containerの配置とデプロイ
- **Kubernetes pod**：Docker podをkubernetesにデプロイ
- **日記管理**：Git, GitHub Actions, etc.を基盤としてCI/CD Pipelineを構築

### メンバーメネ有幸

- **コミット制限**：_commitsや push restrictionを管理
- **チーム之间的コミット exchange**： team internal code reviewやpull request management
- **リリース管理**：Release schedule and build environment setup
- **CI/CD Pipeline**：CI/CD Pipelineを管理

### プロジェクトの管理者

- **プロジェクト代行者**：project managerに責任割り
- **チームの管理**：team roster management, role assignment
- **プロジェクトの状態管理**：project status update, milestone tracking
- **トラブル対応**：fault management and resolution tracking

### プロジェクトの発展

- **Scrumを拡張**：Scrumに沿うプロジェクトの管理
- ** Sprint interval**：Sprint interval management
- ** retrospekt ion**： retrospective meeting conduct
- **Change request**：Change request system operation

### 定期的_review

- **Mid-term review**：プロジェクトのmid-term status update
- **Post-term review**：project's long-term success evaluation
- **Plan review**：project plan update and revision

## 付録

- [TODO] 未来に追加する項目
