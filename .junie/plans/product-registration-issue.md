---
sessionId: session-261115-103247-11md
---

# Issue内容

## 概要

Agiloom(AG)領域のプロダクトディスカバリーモジュールにプロダクト基本情報登録機能を追加します。

プロダクト名、説明、オーナー等基本情報のCRUD機能を実装し、Scrumプロジェクト管理の基盤となるプロダクトマスターを管理できるようにします。

既存の`sprintExecution`モジュールの構造を参考に、spi/internal/presentation/command/queryのレイヤー構成に従って実装します。

## 詳細

- `Product`エンティティの作成（プロダクトID、名前、説明、オーナー、作成日時、更新日時、論理削除フラグ等）
- `ProductRepository`の作成（CrudRepository拡張）
- `CreateProductCommand`と`CreateProductCommandService`の作成
- `UpdateProductCommand`と`UpdateProductCommandService`の作成
- `ProductQueryService`の作成（検索・一覧取得）
- `ProductController`の作成（REST API: POST/GET/PATCH/DELETE）
- FlywayマイグレーションSQLの作成（`undecided_ag`スキーマに`products`テーブル）
- 単体テストの追加（エンティティ、サービス、コントローラー）
- APIエンドポイント: `/api/v1/products`

## 環境

- ブランチ: main
- モジュール: `backend/src/main/java/undecided/agiloom/productDiscovery/`
- DBスキーマ: `undecided_ag`
- 参考モジュール: `sprintExecution`（同様の構造パターン）

## 懸念事項

- プロダクトオーナーの管理方法（既存のemployeeモジュールと連携するか、別途管理するか）
- プロダクトIDの生成方法（SnowflakeIdを使用するか、UUIDを使用するか）

## 参考ドキュメント

- [初期要望.txt](初期要望.txt) - Agiloom領域のビジネス概要
- [sprintExecutionモジュール](backend/src/main/java/undecided/agiloom/sprintExecution/) - 参考実装

# Technical Design

### Current Implementation

現在、`productDiscovery`モジュールは`package-info.java`のみで、中身が空です。`sprintExecution`
モジュールは完成しており、以下のような構造を持っています：

- `spi/` - エンティティ（`Sprint.java`等）
- `internal/` - Repository、Command、Query、Presentation層
- `internal/command/` - `CreateSprintCommand`, `CreateSprintCommandService`等
- `internal/query/` - `SprintQueryService`
- `internal/presentation/` - `SprintCommandController`, `SprintQueryController`

### Proposed Changes

`sprintExecution`の構造を参考に、以下を作成します：

1. **spi/Product.java** - プロダクトエンティティ（SnowflakeId、名前、説明、オーナーUUID、ステータス、作成・更新日時、論理削除）
2. **internal/ProductRepository.java** - CrudRepository拡張
3. **internal/command/CreateProductCommand.java** - 作成用コマンド
4. **internal/command/CreateProductCommandService.java** - 作成用サービス
5. **internal/command/UpdateProductCommand.java** - 更新用コマンド
6. **internal/command/UpdateProductCommandService.java** - 更新用サービス
7. **internal/query/ProductQueryService.java** - 検索用サービス
8. **internal/presentation/ProductController.java** - REST APIコントローラー
9. **Flywayマイグレーション** - `products`テーブル作成SQL
10. **テスト** - 単体テストの追加

### Data Model

```
Product
- id: SnowflakeId
- name: String (必須, 最大100文字)
- description: String (最大1000文字)
- ownerId: UUID (オーナー)
- status: ProductStatus (ACTIVE, ARCHIVED)
- createdAt: LocalDateTime
- updatedAt: LocalDateTime
- deletedAt: LocalDateTime (論理削除)
```

# Delivery Steps

### ✓ Step 1: IssueをGitHubに作成

ghコマンドを使用して、プロダクト登録機能のIssueをGitHubリポジトリに作成します。

- タイトル: `feat: プロダクト基本情報登録機能の実装`
- ラベル: `feature`, `backend`
- Assignee: ユーザー(mizo432)
- 本文: 上記のIssue内容テンプレートに沿って作成

### ✓ Step 2: ProductエンティティとProductStatus列挙型の作成

`spi/`パッケージにProductエンティティとProductStatus列挙型を作成します。

- `spi/ProductStatus.java` - ACTIVE, ARCHIVED のステータス
- `spi/Product.java` - SnowflakeId, name, description, ownerId, status, createdAt, updatedAt,
  deletedAt

### ✓ Step 3: ProductRepositoryの作成

`internal/`パッケージにProductRepositoryを作成します。

- `internal/ProductRepository.java` - CrudRepository拡張

### ✓ Step 4: Command層の作成

`internal/command/`パッケージにコマンドとコマンドサービスを作成します。

- `internal/command/CreateProductCommand.java`
- `internal/command/CreateProductCommandService.java`
- `internal/command/UpdateProductCommand.java`
- `internal/command/UpdateProductCommandService.java`

### ✓ Step 5: Query層の作成

`internal/query/`パッケージにクエリサービスを作成します。

- `internal/query/ProductQueryService.java`

### ✓ Step 6: Presentation層の作成

`internal/presentation/`パッケージにコントローラーとDTOを作成します。

- `internal/presentation/CreateProductRequest.java`
- `internal/presentation/UpdateProductRequest.java`
- `internal/presentation/ProductResponse.java`
- `internal/presentation/ProductCommandController.java`
- `internal/presentation/ProductQueryController.java`

### ✓ Step 7: FlywayマイグレーションSQLの作成

`undecided_ag`スキーマに`products`テーブルを作成するSQLを作成します。

- `db/migration/ag/V20261115100000__add_products_table.sql`
- application.ymlに`ag` locationsを追加

### ✓ Step 8: 単体テストの追加

Productエンティティの単体テストを追加します。

- `backend/src/test/java/undecided/agiloom/productDiscovery/spi/ProductTest.java`

### ✓ Step 9: ビルド検証

`./gradlew :backend:compileJava` でビルドが通るか確認します。

- ビルド成功
- ProductTestも正常にパス
