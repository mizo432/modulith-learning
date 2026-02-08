# 複数のプロジェクトのScrum開発を管理するシステム

## プロジェクト概要

このプロジェクトは、Spring Modulithアーキテクチャを学習・実践するためのデモンストレーションプロジェクトです。モジュラーモノリス（Modular
Monolith）アプローチを採用し、マイクロサービスとモノリシックアプリケーションの利点を組み合わせています。

### 主な目的

- Spring Modulithフレームワークの学習と実践
- モジュラーモノリスアーキテクチャの実装
- ドメイン駆動設計（DDD）の原則の適用
- オニオンアーキテクチャパターンの実装

### システム構成

このシステムは以下の主要コンポーネントで構成されています：

1. **バックエンド（Backend）**：
    - Spring Boot 4.0.2ベースのアプリケーション
    - Spring Modulithを使用したモジュール化
    - PostgreSQLデータベース
    - JPA/Hibernateによるデータアクセス
    - Flywayによるデータベースマイグレーション

2. **APIゲートウェイ（API Gateway）**：
    - クライアントリクエストのルーティングと管理

3. **サービスレジストリ（Service Registry）**：
    - Netflix Eurekaを使用したサービスディスカバリー

4. **フロントエンド（Frontend）**：
    - Reactベースのアプリケーション
    - SPAとして作成する
    - コンポーネントライブラリーとして最新のMUIを使用する

### モジュール構造

バックエンドアプリケーションは以下のモジュールで構成されています：

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

- **言語**：Java 25 (OpenJDK)
- **フレームワーク**：
    - Spring Boot 4.0.2
    - Spring Modulith 2.0.1
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
- 疎結合と高凝集
- 独立したデプロイの可能性
- マイクロサービスへの段階的な移行パス

### コンテナ化

Dockerfileが提供され、アプリケーションのコンテナ化とデプロイが可能です。

## 機能追加（Feature Flags, テスト戦略, etc.）

### フィーチャーフラグ

- **APIリミット**：API呼び出し制限を管理
- **ロールベースアクセス制御**：RBACを実装
- **バックエンドの信頼性**：バックエンドの信頼性を管理
- **デプロイスケジューリング**：デプロイのスケジュール管理

### テスト戦略

- **単体テスト**：JUnitを基盤とした単体テスト
- **統合テスト**：APIやサービス間の統合テスト
- **性能テスト**：LoadUI、JMeter等で性能を測定
- **セキュリティテスト**：セキュリティスキャンと脆弱性テスト

単体テストはTagアノテーションを利用しテストサイズを指定する

フィーチャーブランチではsmallサイズのテストを行う
プルリクエストではmediumサイズのテストまで行う

テストの作成では下記のようなテストを作成する

テストメソッド名はshouldで始めて。
テストメソッド名に_は使用しない。
assertJを使用して。
テストメソッドとテストクラスにはDisplayNameアノテーションを日本語で付与てして。
テスト対象のメソッドは対象メソッド毎にclassでネストして。
引数がnullの場合のテストも含める。
Junit5で実装して。
classとメソッドはpackage-privateで。

### DevOpsプラグイン

- **Ansible/Inventory**：CI/CDパイプラインを管理
- **Dockerfile**：Dockerコンテナの配置とデプロイ
- **Kubernetes pod**：Docker podをKubernetesにデプロイ
- **履歴管理**：Git、GitHub Actions等を基盤としてCI/CDパイプラインを構築

### メンバー管理

- **コミット制限**：コミットやプッシュ制限を管理
- **チーム間コード交換**：チーム内コードレビューやプルリクエスト管理
- **リリース管理**：リリーススケジュールとビルド環境設定
- **CI/CDパイプライン**：CI/CDパイプラインを管理

### プロジェクト管理者

- **プロジェクト代表者**：プロジェクトマネージャーの責任割り当て
- **チームの管理**：チームロスター管理、役割割り当て
- **プロジェクトの状態管理**：プロジェクト状況更新、マイルストーン追跡
- **問題対応**：障害管理と解決追跡

### プロジェクトの発展

- **Scrumを拡張**：Scrumに沿ったプロジェクト管理
- **スプリント間隔**：スプリント間隔管理
- **レトロスペクティブ**：振り返りミーティングの実施
- **変更要求**：変更要求システムの運用

### 定期的レビュー

- **中間レビュー**：プロジェクトの中間状況更新
- **最終レビュー**：プロジェクトの長期的成功評価
- **計画レビュー**：プロジェクト計画の更新と改訂

## 開発ガイドライン

### ビルドと設定

このプロジェクトは Gradle をビルドシステムとして使用しています。

- **Javaバージョン**: Java 25 が必要です。Gradle Toolchain により自動的に設定されます。
- **ビルドコマンド**:
  ```bash
  ./gradlew build
  ```
- **バックエンドの起動**:
  ```bash
  ./gradlew :backend:bootRun
  ```

### テスト実行ガイドライン

テストは JUnit 5 を使用し、サイズ（`small`, `medium`, `large`）ごとにタグ付けされています。

#### テストの実行

- **小規模テスト (Unit Tests)**: デフォルトで実行されます。
  ```bash
  ./gradlew :backend:test
  ```
- **中規模テスト (Integration Tests)**:
  ```bash
  ./gradlew :backend:mediumTest
  ```
- **大規模テスト (System/Load Tests)**:
  ```bash
  ./gradlew :backend:largeTest
  ```

#### テスト作成のルール

新しいテストを作成する際は、以下のルールに従ってください：

1. **命名規則**:
    - メソッド名は `should` で始める。
    - スネークケース（`_`）は使用しない。
    - 例: `shouldReturnCorrectValue()`
2. **アノテーション**:
    - `@DisplayName` を使用して、クラスとメソッドに日本語で説明を付与する。
    - `Tag` アノテーションでテストサイズを指定する（例: `@Tag("small")`）。
3. **構造**:
    - テスト対象のメソッドごとに `@Nested` クラスでネストする。
    - `assertJ` を使用してアサーションを記述する。
4. **品質**:
    - 引数が `null` の場合の境界値テストを含める。
    - クラスとメソッドは原則として `package-private` とする。

#### テストの例

```java

@Tag("small")
@DisplayName("計算機能のテスト")
class CalculatorTest {

  @Nested
  @DisplayName("addメソッドのテスト")
  class AddTest {

    @Test
    @DisplayName("正の数同士の加算が正しく行われること")
    void shouldAddPositiveNumbers() {
      int result = calculator.add(1, 1);
      assertThat(result).isEqualTo(2);
    }

    @Test
    @DisplayName("引数がnullの場合は例外が発生すること")
    void shouldThrowExceptionWhenArgumentIsNull() {
      assertThatThrownBy(() -> calculator.add(null, 1))
          .isInstanceOf(IllegalArgumentException.class);
    }
  }
}
```

### 追加の開発情報

- **Spring Modulith**: モジュール間の依存関係を確認するために、`./gradlew :backend:test` を実行すると
  `build/spring-modulith-docs` にドキュメント（PlantUML等）が生成されます。
- **Lombok**: `Getter`, `Setter`, `AllArgsConstructor` などを積極的に活用し、ボイラープレートコードを削減しています。
- **コードスタイル**: 既存のコードは Google Java Style
  に近い形式を採用しています。自動整形ツール（Checkstyle等）の導入も検討されています。

## 付録

- [TODO] 未来に追加する項目
