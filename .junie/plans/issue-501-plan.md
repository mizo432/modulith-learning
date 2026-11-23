# Issue #501 / #504 実装計画

## 概要

バックログアイテムへのファイル添付機能を実装する。ファイル実体は MinIO（S3互換）に保存し、メタデータとバージョン管理は新設する帳票管理モジュールが DB で持つ。

## 設計決定事項（Issue コメントより優先）

| 項目 | 決定内容 | 出典 |
|------|----------|------|
| ストレージ | MinIO（S3互換） | #625 |
| 帳票管理 | 新設モジュール経由で一元管理 | #631 |
| ファイルサイズ | 1ファイル10MB、アイテム合計50MB | #626 |
| 許可ファイル種別 | 画像(png/jpg/gif/webp)、PDF、テキスト(txt/md/csv)、文書(docx/xlsx/pptx) | #626 |
| 検証方法 | 拡張子と Content-Type の両方 | #626 |
| 削除 | 論理削除、1年保持後バッチで物理削除 | #627 |
| アクセス制御 | スクラムチームメンバーとステークホルダーのみ | #628 |
| プレビュー | 実装しない（ダウンロードのみ） | #629 |
| バージョン管理 | 必須 | #630 |
| 障害時 | 自動リトライのみ、フォールバックなし | #632 |
| 削除権限 | アップロード者のみ | #501 |

---

## 既存コード調査結果

### 1. 帳票管理モジュールの有無

**結論：存在しない。新設が必要。**

- `undecided` 配下のパッケージを走査したが、「帳票」「documentMgmt」「reportMgmt」に相当するモジュールは存在しない
- Flyway の `ef`（電子帳票）スキーマはサンプルテーブルのみ（`flyway_sample_table`、`large_record_table`）
- 帳票管理モジュールを新規作成する

### 2. BacklogItem の詳細 API と画面構造

**Backend API:**

- `GET /api/v1/backlog-items/{backlogItemId}` — `BacklogItemQueryController` で単一アイテム取得
- `BacklogItemResponse` は id, title, description, status, priority, creator, sprintId, scrumTeamId, value, qualityCategory, storyPoint, createdAt, updatedAt を含む
- 添付ファイル関連のフィールドは未定義

**Frontend:**

- `BacklogItemDetail.tsx` — 詳細ページ（858行）。コメント、Value入力、StoryPoint見積もりを表示
- `backlogItemService.ts` — API クライアント。添付ファイル関連のメソッドは未定義
- 添付ファイル一覧表示用のセクションは未実装

### 3. MinIO クライアントの依存

**結論：未導入。新規追加が必要。**

- `build.gradle.kts` に AWS SDK / MinIO 関連の依存はない
- `application.yml` に MinIO 接続設定はない
- `compose.yml` に MinIO サービスは定義されていない

**追加予定:**

- `software.amazon.awssdk:s3`（AWS SDK v2 S3 クライアント。MinIO は S3 互換 API を提供するため、AWS SDK で対応可能）
- または `io.minio:minio`（MinIO 公式 Java SDK）

### 4. Testcontainers での検証方法

**既存パターン:**

- `TestcontainersConfiguration.java` — PostgreSQL と Redis のコンテナを起動
- `@Import(TestcontainersConfiguration.class)` + `@Tag("medium")` で統合テスト
- `GenericContainer` を使用して Redis を起動しているパターンがある

**MinIO テスト用:**

- `GenericContainer<>("minio/minio:latest")` で MinIO コンテナを起動
- AWS SDK v2 の S3 クライアントを MinIO エンドポイントに接続
- テスト用バケットを自動作成・削除

### 5. アクセス制御の既存パターン

- `ScrumTeamSubset.isTeamMember()` — チームメンバー判定
- `ScrumTeamSubset.canChangeStatusBy()` — SM/PO 判定
- 帳票管理モジュールでも同様のサブセットパターンを採用し、スクラムチームメンバーとステークホルダーを判定する

### 6. バッチ処理の既存パターン

- `undecided.generic.addressReg.internal.batch` — Spring Batch の標準構成（config / reader / processor / writer / operator / dto）
- 物理削除バッチも同様の構成で実装

---

## 変更ファイル一覧

### A. 帳票管理モジュール新設（`undecided.generic.documentMgmt`）

#### Domain 層（SPI）

| ファイル | 説明 |
|----------|------|
| `undecided/generic/documentMgmt/package-info.java` | モジュール定義（`@ApplicationModule(Type.OPEN)`） |
| `undecided/generic/documentMgmt/spi/package-info.java` | SPI パッケージ定義 |
| `undecided/generic/documentMgmt/spi/Document.java` | 帳票エンティティ（JPA Entity）。ファイルメタデータ、バージョン、論理削除フラグ |
| `undecided/generic/documentMgmt/spi/DocumentFileType.java` | ファイル種別区分値（IMAGE/PDF/TEXT/DOCUMENT） |
| `undecided/generic/documentMgmt/spi/DocumentVersion.java` | バージョン値オブジェクト |
| `undecided/generic/documentMgmt/spi/DocumentUploaded.java` | ドメインイベント：帳票アップロード済み |
| `undecided/generic/documentMgmt/spi/DocumentDeleted.java` | ドメインイベント：帳票論理削除済み |
| `undecided/generic/documentMgmt/spi/DocumentStorageService.java` | SPI インターフェース：ストレージ操作（アップロード/ダウンロード/物理削除） |

#### Internal パッケージ

| ファイル | 説明 |
|----------|------|
| `undecided/generic/documentMgmt/internal/package-info.java` | `@ApplicationModule(Type.CLOSED)` |
| `undecided/generic/documentMgmt/internal/DocumentRepository.java` | JPA リポジトリ |
| `undecided/generic/documentMgmt/internal/UploadDocumentCommand.java` | アップロードコマンド |
| `undecided/generic/documentMgmt/internal/UploadDocumentCommandService.java` | アップロード処理（MinIO 保存 + DB メタデータ + バージョン管理） |
| `undecided/generic/documentMgmt/internal/DeleteDocumentCommand.java` | 論理削除コマンド |
| `undecided/generic/documentMgmt/internal/DeleteDocumentCommandService.java` | 論理削除処理 |
| `undecided/generic/documentMgmt/internal/DocumentQueryService.java` | 帳票メタデータクエリ |
| `undecided/generic/documentMgmt/internal/DocumentDownloadService.java` | ダウンロード処理（MinIO からストリーミング） |
| `undecided/generic/documentMgmt/internal/DocumentFileValidator.java` | ファイル種別・サイズ検証（拡張子 + Content-Type） |
| `undecided/generic/documentMgmt/internal/DocumentStorageServiceImpl.java` | `DocumentStorageService` の MinIO 実装 |
| `undecided/generic/documentMgmt/internal/DocumentCommandController.java` | REST コントローラー（アップロード/削除） |
| `undecided/generic/documentMgmt/internal/DocumentQueryController.java` | REST コントローラー（一覧/ダウンロード） |
| `undecided/generic/documentMgmt/internal/DocumentResponse.java` | レスポンス DTO |
| `undecided/generic/documentMgmt/internal/DocumentExceptionHandler.java` | 例外ハンドラー |
| `undecided/generic/documentMgmt/internal/DocumentStorageProperties.java` | MinIO 接続設定プロパティ |
| `undecided/generic/documentMgmt/internal/DocumentStorageConfig.java` | MinIO/S3 クライアント Bean 設定 |
| `undecided/generic/documentMgmt/internal/batch/config/DocumentPhysicalDeleteBatchConfig.java` | 物理削除バッチ設定 |
| `undecided/generic/documentMgmt/internal/batch/reader/DocumentPhysicalDeleteReader.java` | 論理削除済みかつ1年経過した帳票を読み込む |
| `undecided/generic/documentMgmt/internal/batch/processor/DocumentPhysicalDeleteProcessor.java` | MinIO から物理削除 + DB から削除 |
| `undecided/generic/documentMgmt/internal/batch/writer/DocumentPhysicalDeleteWriter.java` | DB からの削除 |
| `undecided/generic/documentMgmt/internal/batch/operator/DocumentPhysicalDeleteJobOperator.java` | バッチ起動オペレーター |

### B. 帳票管理モジュールとバックログの連携

#### requirementBacklog モジュール変更

| ファイル | 説明 |
|----------|------|
| `undecided/agiloom/requirementBacklog/internal/BacklogItemDocumentRepository.java` | バックログ-帳票関連付けリポジトリ（新規） |
| `undecided/agiloom/requirementBacklog/internal/BacklogItemDocument.java` | 関連付けエンティティ（新規） |
| `undecided/agiloom/requirementBacklog/internal/AttachDocumentCommand.java` | 帳票をバックログに紐付けるコマンド（新規） |
| `undecided/agiloom/requirementBacklog/internal/AttachDocumentCommandService.java` | 紐付け処理（新規） |
| `undecided/agiloom/requirementBacklog/internal/DetachDocumentCommand.java` | 紐付け解除コマンド（新規） |
| `undecided/agiloom/requirementBacklog/internal/DetachDocumentCommandService.java` | 紐付け解除処理（新規） |
| `undecided/agiloom/requirementBacklog/internal/BacklogItemQueryService.java` | 添付ファイル一覧取得メソッド追加 |
| `undecided/agiloom/requirementBacklog/internal/BacklogItemQueryController.java` | 添付ファイル一覧/アップロード/削除/ダウンロードの API エンドポイント追加 |
| `undecided/agiloom/requirementBacklog/internal/BacklogItemResponse.java` | 添付ファイル一覧フィールド追加 |
| `undecided/agiloom/requirementBacklog/internal/BacklogItemDocumentResponse.java` | 添付ファイルレスポンス DTO（新規） |
| `undecided/agiloom/requirementBacklog/internal/ScrumTeamSubset.java` | ステークホルダー判定メソッド追加（閲覧権限用） |

### C. 設定・インフラ変更

| ファイル | 説明 |
|----------|------|
| `backend/build.gradle.kts` | AWS SDK v2 S3 依存追加、Testcontainers MinIO 依存追加 |
| `backend/src/main/resources/application.yml` | MinIO 接続設定追加、Flyway locations に `ef`（帳票管理用）を追加 |
| `backend/compose.yml` | MinIO サービス追加 |
| `backend/src/test/resources/application-test.yml` | テスト用 MinIO 設定追加 |
| `backend/src/test/java/undecided/TestcontainersConfiguration.java` | MinIO コンテナ追加 |
| `backend/src/main/resources/db/migration/ef/V*__create_documents_table.sql` | 帳票テーブル作成マイグレーション |
| `backend/src/main/resources/db/migration/ag/V*__add_backlog_item_documents_table.sql` | バックログ-帳票関連付けテーブル追加 |

### D. フロントエンド変更

| ファイル | 説明 |
|----------|------|
| `frontend/src/services/backlogItemService.ts` | 添付ファイル関連の API メソッド追加 |
| `frontend/src/services/documentService.ts` | 帳票管理 API クライアント（新規） |
| `frontend/src/pages/products/BacklogItemDetail.tsx` | 添付ファイル一覧セクション追加 |
| `frontend/src/pages/products/BacklogItemDetail.test.tsx` | テスト更新 |

---

## 段階分け

### 段階1: 帳票管理モジュールの基盤

**目標:** MinIO 連携と帳票メタデータ管理の基盤を構築

1. `build.gradle.kts` に AWS SDK v2 S3 依存を追加
2. `compose.yml` に MinIO サービスを追加
3. `application.yml` に MinIO 接続設定を追加
4. 帳票管理モジュールのパッケージ構造と `package-info.java` を作成
5. ドメインモデル（`Document`、`DocumentFileType`、`DocumentVersion`）を実装
6. MinIO/S3 クライアント設定（`DocumentStorageConfig`）を実装
7. `DocumentStorageService` SPI と MinIO 実装（`DocumentStorageServiceImpl`）を作成
8. Flyway マイグレーション（`undecided_ef` スキーマの `documents` テーブル）を作成
9. `DocumentRepository` を実装
10. ファイル検証（`DocumentFileValidator`）を実装 — 拡張子 + Content-Type 両方チェック

**テスト:**
- `DocumentFileTypeTest`（small）— 区分値の検証
- `DocumentVersionTest`（small）— バージョン値オブジェクトの検証
- `DocumentFileValidatorTest`（small）— 許可/不許可ファイル種別、サイズ制限の境界値
- `DocumentStorageServiceImplMediumTest`（medium）— MinIO Testcontainers でのアップロード/ダウンロード/削除

### 段階2: 帳票管理のユースケース

**目標:** アップロード、ダウンロード、論理削除のコマンド/クエリを実装

1. `UploadDocumentCommandService` — ファイル検証 → MinIO アップロード → DB 保存 → バージョン管理
2. `DeleteDocumentCommandService` — 論理削除（`deletedAt` をセット）
3. `DocumentDownloadService` — MinIO からストリーミングダウンロード
4. `DocumentQueryService` — 帳票メタデータの一覧/単体取得
5. REST コントローラー（`DocumentCommandController`、`DocumentQueryController`）
6. 例外ハンドラー（`DocumentExceptionHandler`）
7. リトライロジック実装（Spring Retry または手動実装）

**テスト:**
- `UploadDocumentCommandServiceTest`（small）— ドメインロジックの単体テスト
- `UploadDocumentCommandServiceMediumTest`（medium）— MinIO + DB 連携
- `DeleteDocumentCommandServiceTest`（small）— 論理削除ロジック
- `DocumentQueryServiceMediumTest`（medium）— クエリ連携
- `DocumentCommandControllerTest`（small）— コントローラーの単体テスト
- `DocumentEndpointMediumTest`（medium）— REST API 統合テスト

### 段階3: バックログとの連携

**目標:** バックログアイテムと帳票を関連付け、API を経由して添付操作を提供

1. `BacklogItemDocument` 関連付けエンティティとリポジトリを作成
2. Flyway マイグレーション（`undecided_ag` スキーマの `backlog_item_documents` テーブル）
3. `AttachDocumentCommandService` — 帳票管理モジュールの SPI を呼び出し、紐付けを作成
4. `DetachDocumentCommandService` — 紐付け解除
5. `BacklogItemQueryService` に添付ファイル一覧取得メソッドを追加
6. `BacklogItemQueryController` に添付ファイル関連の API エンドポイントを추가:
   - `GET /api/v1/backlog-items/{id}/attachments` — 一覧取得
   - `POST /api/v1/backlog-items/{id}/attachments` — アップロード（Multipart）
   - `DELETE /api/v1/backlog-items/{id}/attachments/{documentId}` — 削除
   - `GET /api/v1/backlog-items/{id}/attachments/{documentId}/download` — ダウンロード
7. `BacklogItemResponse` に添付ファイル一覧フィールドを追加
8. アクセス制御：アップロード者は削除可能、閲覧はチームメンバーとステークホルダーのみ

**テスト:**
- `AttachDocumentCommandServiceTest`（small）
- `AttachDocumentCommandServiceMediumTest`（medium）
- `DetachDocumentCommandServiceTest`（small）
- `BacklogItemAttachmentsEndpointMediumTest`（medium）— モジュール間連携

### 段階4: 物理削除バッチ

**目標:** 論理削除から1年経過した帳票を物理削除するバッチを実装

1. `DocumentPhysicalDeleteBatchConfig` — Job / Step 定義
2. `DocumentPhysicalDeleteReader` — `deletedAt < 1年前` のレコードを読み込み
3. `DocumentPhysicalDeleteProcessor` — MinIO からオブジェクト削除
4. `DocumentPhysicalDeleteWriter` — DB からレコード削除
5. `DocumentPhysicalDeleteJobOperator` — バッチ起動

**テスト:**
- `DocumentPhysicalDeleteProcessorTest`（small）
- `DocumentPhysicalDeleteBatchConfigTest`（medium）— 実際のバッチ実行

### 段階5: フロントエンド実装

**目標:** 詳細ページに添付ファイル一覧を表示し、アップロード/削除/ダウンロードを操作可能にする

1. `documentService.ts` — 帳票管理 API クライアント
2. `backlogItemService.ts` — 添付ファイル関連メソッド追加
3. `BacklogItemDetail.tsx` — 添付ファイル一覧セクション追加（ファイル名、サイズ、日時、アップロード者）
4. アップロードダイアログ（Multipart 送信）
5. 削除ボタン（アップロード者のみ表示）
6. ダウンロードリンク
7. テスト更新

### 段階6: 統合検証

**目標:** モジュール境界と全機能の統合テスト

1. `ApplicationModules.verify()` が通るか確認
2. ArchUnit テストでモジュール境界を検証
3. 全テスト（small + medium + large）を実行

---

## テストリスト

### 帳票管理モジュール — 値オブジェクト・区分値

| # | テストケース | サイズ |
|---|-------------|--------|
| 1 | 許可された拡張子（png）で `DocumentFileType` を作成したとき、IMAGE が返される | small |
| 2 | 許可された拡張子（pdf）で `DocumentFileType` を作成したとき、PDF が返される | small |
| 3 | 許可された拡張子（txt）で `DocumentFileType` を作成したとき、TEXT が返される | small |
| 4 | 許可された拡張子（docx）で `DocumentFileType` を作成したとき、DOCUMENT が返される | small |
| 5 | 許可されていない拡張子（exe）で `DocumentFileType` を作成したとき、例外がスローされる | small |
| 6 | 大文字の拡張子（PNG）で `DocumentFileType` を作成したとき、IMAGE が返される（大文字小文字を区別しない） | small |
| 7 | 空文字の拡張子で `DocumentFileType` を作成したとき、例外がスローされる | small |
| 8 | `DocumentVersion` の初期値が 1 であるとき、true となる | small |
| 9 | `DocumentVersion` をインクリメントしたとき、次の値が返される | small |

### ファイル検証

| # | テストケース | サイズ |
|---|-------------|--------|
| 10 | 許可された拡張子と Content-Type の組み合わせで検証したとき、合格する | small |
| 11 | 拡張子は許可されているが Content-Type が不一致の場合、不合格となる | small |
| 12 | Content-Type は許可されているが拡張子が不一致の場合、不合格となる | small |
| 13 | ファイルサイズが10MBちょうどの場合、合格する | small |
| 14 | ファイルサイズが10MBを1バイト超えた場合、不合格となる | small |
| 15 | ファイルサイズが0バイトの場合、不合格となる | small |
| 16 | ファイルサイズが負の場合、不合格となる | small |
| 17 | 許可されていない拡張子で検証したとき、不合格となる | small |
| 18 | 許可されていない Content-Type で検証したとき、不合格となる | small |

### アップロード

| # | テストケース | サイズ |
|---|-------------|--------|
| 19 | 有効なファイルをアップロードしたとき、MinIO に保存され DB にメタデータが登録される | medium |
| 20 | アップロード成功時にバージョンが 1 で初期化される | medium |
| 21 | 同じ名前のファイルを再度アップロードしたとき、バージョンが 2 になる | medium |
| 22 | ファイルサイズ制限を超えたファイルをアップロードしたとき、エラーとなる | medium |
| 23 | 許可されていないファイル種別をアップロードしたとき、エラーとなる | medium |
| 24 | MinIO 接続が失敗したとき、リトライ後に成功する | medium |
| 25 | MinIO 接続がリトライ後も失敗したとき、エラーとなる（フォールバックしない） | medium |
| 26 | バックログアイテムあたりの合計サイズが50MBを超えた場合、アップロードが拒否される | medium |
| 27 | 存在しないバックログアイテムにファイルを添付したとき、エラーとなる | medium |

### ダウンロード

| # | テストケース | サイズ |
|---|-------------|--------|
| 28 | 有効な帳票IDでダウンロードしたとき、ファイルコンテンツが返される | medium |
| 29 | 存在しない帳票IDでダウンロードしたとき、エラーとなる | medium |
| 30 | 論理削除済みの帳票をダウンロードしたとき、エラーとなる | medium |
| 31 | ダウンロード時に正しい Content-Type ヘッダーが返される | medium |

### 論理削除

| # | テストケース | サイズ |
|---|-------------|--------|
| 32 | アップロード者がファイルを削除したとき、論理削除フラグがセットされる | medium |
| 33 | アップロード者以外がファイルを削除したとき、エラーとなる | medium |
| 34 | 既に論理削除済みのファイルを再度削除したとき、エラーとなる | medium |
| 35 | 削除時に MinIO のオブジェクトは残ったままとなる（物理削除されない） | medium |

### アクセス制御

| # | テストケース | サイズ |
|---|-------------|--------|
| 36 | スクラムチームメンバーが添付ファイル一覧を取得したとき、一覧が返される | medium |
| 37 | ステークホルダーが添付ファイル一覧を取得したとき、一覧が返される | medium |
| 38 | チーム外の従業員が添付ファイル一覧を取得したとき、エラーとなる | medium |
| 39 | スクラムチームメンバーがファイルをダウンロードしたとき、ファイルが返される | medium |
| 40 | チーム外の従業員がファイルをダウンロードしたとき、エラーとなる | medium |

### バージョン管理

| # | テストケース | サイズ |
|---|-------------|--------|
| 41 | 同じファイル名で2回目のアップロードをしたとき、バージョン2として保存される | medium |
| 42 | バージョン一覧を取得したとき、すべてのバージョンが返される | medium |
| 43 | 特定のバージョンをダウンロードしたとき、そのバージョンのファイルが返される | medium |

### 物理削除バッチ

| # | テストケース | サイズ |
|---|-------------|--------|
| 44 | 論理削除から1年経過した帳票がバッチ対象として読み込まれる | medium |
| 45 | 論理削除から1年未満の帳票はバッチ対象とならない | medium |
| 46 | バッチ実行後、MinIO からオブジェクトが物理削除される | medium |
| 47 | バッチ実行後、DB からレコードが削除される | medium |
| 48 | MinIO からの削除が失敗したとき、DB 削除も行われない（トランザクション） | medium |

### モジュール間連携

| # | テストケース | サイズ |
|---|-------------|--------|
| 49 | バックログアイテムへのファイル添付 API を呼び出したとき、帳票管理モジュール経由で保存される | medium |
| 50 | バックログアイテム詳細 API を呼び出したとき、添付ファイル一覧が含まれる | medium |
| 51 | `ApplicationModules.verify()` が成功する | large |

---

## DB テーブル設計（概要）

### `undecided_ef.documents`（帳票管理）

| 列名 | 型 | 説明 |
|------|-----|------|
| `id` | BIGINT (PK) | Snowflake ID |
| `file_name` | VARCHAR | ファイル名 |
| `original_file_name` | VARCHAR | 元のファイル名 |
| `content_type` | VARCHAR | MIME タイプ |
| `file_size` | BIGINT | ファイルサイズ（バイト） |
| `file_type` | VARCHAR | 種別区分値（IMAGE/PDF/TEXT/DOCUMENT） |
| `version` | INTEGER | バージョン番号 |
| `s3_key` | VARCHAR | MinIO 上のキー |
| `backlog_item_id` | BIGINT | 関連バックログアイテムID |
| `uploader_employee_id` | BIGINT | アップローダーの従業員ID |
| `uploader_name` | VARCHAR | アップローダー名 |
| `deleted` | BOOLEAN | 論理削除フラグ |
| `deleted_at` | TIMESTAMP | 論理削除日時 |
| `version_col` | INTEGER | 楽観ロック |
| `created_at` | TIMESTAMP | 作成日時 |
| `updated_at` | TIMESTAMP | 更新日時 |

### `undecided_ag.backlog_item_documents`（バックログ-帳票関連付け）

| 列名 | 型 | 説明 |
|------|-----|------|
| `id` | BIGINT (PK) | Snowflake ID |
| `backlog_item_id` | BIGINT | バックログアイテムID |
| `document_id` | BIGINT | 帳票ID（最新バージョン） |
| `created_at` | TIMESTAMP | 紐付け作成日時 |
| `version_col` | INTEGER | 楽観ロック |

---

## API エンドポイント（新規）

| メソッド | パス | 説明 |
|----------|------|------|
| POST | `/api/v1/backlog-items/{id}/attachments` | ファイル添付（Multipart） |
| GET | `/api/v1/backlog-items/{id}/attachments` | 添付ファイル一覧取得 |
| GET | `/api/v1/backlog-items/{id}/attachments/{docId}/download` | ファイルダウンロード |
| DELETE | `/api/v1/backlog-items/{id}/attachments/{docId}` | ファイル削除（論理削除） |
| GET | `/api/v1/backlog-items/{id}/attachments/{docId}/versions` | バージョン一覧 |

---

## 未決定事項

1. **MinIO バケット名**: 本番/開発環境でのバケット命名規則を未決定。実装時に環境変数で設定可能にする
2. **S3 Key の命名規則**: ファイル保存時のオブジェクトキーの生成ルール（例: `{backlogItemId}/{timestamp}-{originalFileName}`）を未決定。衝突回避とバージョン管理との整合性を考慮する
3. **リトライの具体値**: リトライ回数・間隔の具体値は実装時に決定（#632 の決定通り）
4. **ステークホルダーの判定方法**: 「ステークホルダー」の定義と判定ロジックの詳細が未決定。現行の `ScrumTeamSubset` にはステークホルダー情報がない。productDiscovery 側の ScrumTeam にステークホルダー情報を追加するか、別のサブセットで管理するかを決定する必要がある
5. **MinIO の本番環境構成**: 本番環境での MinIO デプロイ方法（単一ノード vs ディストリビューテッドモード）を未決定
6. **ファイル名衝突時の処理**: 同じバックログアイテムに同じファイル名をアップロードした場合、上書き（バージョンアップ）とするか、エラーとするか、ファイル名にサフィックスを付けて共存させるかを未決定
7. **フロントエンドのアップロードUI**: プログレスバー、ドラッグ＆ドロップ、複数ファイル同時アップロードなどのUI詳細を未決定
8. **MinIO SDK の選択**: AWS SDK v2 S3 クライアント vs MinIO 公式 Java SDK のどちらを採用するかを未決定

### 段階3 テストリスト（2026-11-24 追記）

前提 #734（ステークホルダー対応）:

- ScrumTeamChanged に stakeholderEmployeeIds を含めたとき、イベントに保持される（small）
- of()／removed() で ScrumTeam のステークホルダーが反映されるとき、その一覧がイベントに入る（small）
- ScrumTeamSubset の isStakeholder が、リストに含まれる従業員IDのとき true となる（small）
- apply()／from() でイベントのステークホルダーが射影されるとき、サブセットに反映される（small）
- stakeholderEmployeeIds 付きサブセットを保存したとき、BIGINT[] で読み戻せる（medium）
- PATCH members に stakeholderEmployeeIds を送ったとき、200 でレスポンスと DB に反映される（medium）
- stakeholderEmployeeIds を送らないとき、既存のステークホルダーは変更されない（medium）

段階3本体:

- 存在しないバックログアイテムに添付したとき、エラーとなる（small: IllegalArgumentException／medium 項目27: 400）
- チームメンバーでない従業員が添付したとき、権限エラーとなる（small）。ステークホルダーも添付は不可（閲覧のみ）
- 合計50MBを超えるとき、エラーとなる（small／medium 項目26: 400）
- チームメンバーが添付したとき、facade 経由で保存され関連付けが作られる（small／medium 項目49）
- 系列を指定して再アップロードしたとき、関連付けの帳票IDが更新される（small）
- 関連付けがない帳票を削除したとき、エラーとなる（small）
- アップロード者以外が削除したとき、権限エラーとなり関連付けが残る（small／medium: 403）
- アップロード者が削除したとき、帳票が論理削除され関連付けが消える（small／medium: 204）
- 項目36〜40・50（medium、BacklogItemAttachmentsEndpointMediumTest）

設計判断（2026-11-24）:

- アップロード権限はチームメンバーのみ。ステークホルダーは一覧・ダウンロードのみ（#501 本文「チームメンバーは…添付できる」と段階3の閲覧範囲の記述に基づく）
- 合計50MBは「関連付け中の各系列の最新バージョンの fileSize 合計＋新規ファイル」で判定する。再アップロード時は当該系列分を新しいサイズで置き換える
- 添付エンドポイントは既存の分割に従い、GET系は BacklogItemQueryController、POST/DELETE は BacklogItemCommandController に置く（計画書の Query 一括記載から変更）
- #734 はエンティティ・DTO・配線の変更が本体のため、write_tests 時点で配線まで実装し、medium で永続化とエンドポイントを検証する（段階2で Document.softDelete を API 表面に含めた前例と同様）


---

## 段階4 追記（2026-11-24）: テストリストと設計判断

### テストリスト（段階4）

small（DocumentPhysicalDeleteProcessorTest）:
- S4-1: 帳票を処理したとき、ストレージからその帳票のS3キーで物理削除される
- S4-2: 帳票を処理したとき、同じ帳票が返される
- S4-3: ストレージの物理削除が失敗したとき、例外が伝播する

medium（計画書テストリスト項目44〜48）:
- 項目44: 論理削除から1年経過した帳票がバッチ対象として読み込まれる
- 項目45: 論理削除から1年未満の帳票はバッチ対象とならない
- 項目46: バッチ実行後、MinIO からオブジェクトが物理削除される
- 項目47: バッチ実行後、DB からレコードが削除される
- 項目48: MinIO からの削除が失敗したとき、DB 削除も行われない（トランザクション）

### 設計判断（段階4）
1. 構成は既存バッチ（bankReg 等）の先例に従い `internal/batch/{config,reader,processor,writer,operator}` とする。単一チャンクステップ（チャンクサイズ50）。
2. 対象抽出はリーダーがステップ開始時に `findByDeletedTrueAndDeletedAtBefore(now - 1年)` で一括取得する（@StepScope でステップごとに新しいインスタンス）。
3. プロセッサーはストレージ削除のみを行い、成功した帳票をライターへ渡す。ライターは DB レコードを削除する。ストレージ削除の失敗時は例外を伝播させ、チャンクのロールバックで DB 削除を行わない（項目48）。スキップ・リトライは付けない。
4. 起動は JobOperator（手動実行メソッド＋ `@Scheduled`、デフォルト毎日3時、`batch.document-physical-delete.cron` で変更可）。bankReg の4時実行と重ならない時刻にした。
