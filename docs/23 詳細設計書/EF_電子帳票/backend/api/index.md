# EF_電子帳票 API詳細設計

## 1. 概要

本ドキュメントは、EF_電子帳票事業領域のAPI詳細設計を定義します。**CQRS（Command Query Responsibility Segregation）**に基づき、**Command Service**（書き込み）と**Query Service**（読み込み）を分離して設計します。

電子帳票システムは、帳票の生成・管理・配信・保管・アーカイブ・監査を一元的に管理する事業領域です。

## 2. 参照

- 事業領域一覧: `docs/11 事業領域/事業領域一覧.tsv`
- 業務領域一覧: `docs/12 業務領域/業務領域一覧.tsv`
- ユースケース一覧: `docs/22 要件定義書/EF 電子帳票/03 機能要件/*/03.システム境界/01.ユースケース/ユースケース一覧.md`
- 情報一覧: `docs/22 要件定義書/EF 電子帳票/03 機能要件/*/04.システム/01.情報/情報一覧.md`
- 画面属性一覧: `docs/22 要件定義書/EF 電子帳票/03 機能要件/*/04.システム/04.画面/SCR-*_画面属性一覧.md`

---

## 3. CQRS設計

### 3.1 CQRSの基本概念

CQRS（Command Query Responsibility Segregation）は、**書き込み操作（Command）**と**読み込み操作（Query）**を分離する設計パターンです。

| 区分 | Command Service | Query Service |
|:---|:---|:---|
| 責務 | データの作成・更新・削除 | データの読み込み・検索 |
| HTTPメソッド | POST, PUT, PATCH, DELETE | GET |
| 戻り値 | 処理結果（ステータスコード・ID） | データ（エンティティ・一覧） |
| 最適化対象 | 一貫性・ビジネスルール | パフォーマンス・キャッシュ |
| トランザクション | 必須 | 不要（スナップショット読み込み可能） |
| キャッシュ | 不要 | 積極的に活用 |

### 3.2 Command Service 設計

**書き込み操作を担当するサービス**

- **パッケージ構成**: `undecided.ef.<module>.useCase.command`
- **命名規則**: `[Entity]CommandService`（例: `FormDefinitionCommandService`）
- **メソッド命名**:
  - 作成: `create[Entity](Command)` → `createFormDefinition(CreateFormDefinitionCommand)`
  - 更新: `update[Entity](Command)` → `updateFormDefinition(UpdateFormDefinitionCommand)`
  - 削除: `delete[Entity](Command)` → `deleteFormDefinition(DeleteFormDefinitionCommand)`
  - 状態遷移: `[action][Entity](Command)` → `approveForm(ApproveFormCommand)`
- **Commandオブジェクト**: 書き込みリクエストを表現する値オブジェクト
  - 不変（`final` クラス + `final` フィールド）
  - バリデーションアノテーションを付与
  - 例: `CreateFormDefinitionCommand`, `ApproveFormCommand`
- **戻り値**: 処理結果のIDまたはステータスを返す

### 3.3 Query Service 設計

**読み込み操作を担当するサービス**

- **パッケージ構成**: `undecided.ef.<module>.useCase.query`
- **命名規則**: `[Entity]QueryService`（例: `FormDefinitionQueryService`）
- **メソッド命名**:
  - 単一取得: `get[Entity](Query)` → `getFormDefinition(GetFormDefinitionQuery)`
  - 一覧取得: `list[Entity](Query)` → `listFormDefinitions(ListFormDefinitionsQuery)`
  - 検索: `search[Entity](Query)` → `searchFormDocuments(SearchFormDocumentsQuery)`
  - カウント: `count[Entity](Query)` → `countFormDocuments(CountFormDocumentsQuery)`
- **Queryオブジェクト**: 読み込みリクエストを表現する値オブジェクト
  - 不変（`final` クラス + `final` フィールド）
  - 検索条件・ページネーションパラメータを含む
  - 例: `GetFormDefinitionQuery`, `ListFormDefinitionsQuery`
- **戻り値**: 読み込み結果のDTOを返す
  - 単一取得: `Optional<[Entity]Dto>` または `[Entity]Dto`
  - 一覧取得: `Page<[Entity]Dto>` または `List<[Entity]Dto>`

### 3.4 Controller 層の分離

プレゼンテーション層では、CommandとQueryでコントローラーを分離します。

| コントローラー | 責務 | 例 |
|:---|:---|:---|
| `[Entity]CommandController` | 書き込みAPI | `FormDefinitionCommandController` |
| `[Entity]QueryController` | 読み込みAPI | `FormDefinitionQueryController` |

### 3.5 APIエンドポイントの設計ルール

CQRS分離に基づくAPIエンドポイントの設計ルール：

| 操作 | HTTPメソッド | エンドポイント | Service | Command/Query |
|:---|:---|:---|:---|:---|
| 一覧取得 | GET | `/api/v1/[resources]` | QueryService | Query |
| 単一取得 | GET | `/api/v1/[resources]/{id}` | QueryService | Query |
| 作成 | POST | `/api/v1/[resources]` | CommandService | Command |
| 全体更新 | PUT | `/api/v1/[resources]/{id}` | CommandService | Command |
| 部分更新 | PATCH | `/api/v1/[resources]/{id}` | CommandService | Command |
| 削除 | DELETE | `/api/v1/[resources]/{id}` | CommandService | Command |
| 状態遷移 | PATCH | `/api/v1/[resources]/{id}/[action]` | CommandService | Command |

---

## 4. UCからCommand/Queryの分類

EF_電子帳票の80ユースケースをCommand/Queryに分類します。

### 4.1 EF-01 帳票基盤管理 (23UC)

| ユースケースID | ユースケース名 | Command/Query | 対応Service | HTTPメソッド |
|----------------|----------------|---------------|-------------|-------------|
| UC-EF-001 | システム利用環境を登録する | Command | EnvironmentCommandService | POST |
| UC-EF-002 | システム利用環境を更新する | Command | EnvironmentCommandService | PUT |
| UC-EF-003 | システム利用環境を参照する | Query | EnvironmentQueryService | GET |
| UC-EF-004 | 業務システムを登録する | Command | BusinessSystemCommandService | POST |
| UC-EF-005 | 業務システムを更新する | Command | BusinessSystemCommandService | PUT |
| UC-EF-006 | 業務システムを参照する | Query | BusinessSystemQueryService | GET |
| UC-EF-007 | ストレージ接続を設定する | Command | StorageConnectionCommandService | POST |
| UC-EF-008 | ストレージ接続を更新する | Command | StorageConnectionCommandService | PUT |
| UC-EF-009 | ストレージ接続を参照する | Query | StorageConnectionQueryService | GET |
| UC-EF-010 | アクセス制御ポリシーを設定する | Command | AccessControlPolicyCommandService | POST |
| UC-EF-011 | アクセス制御ポリシーを更新する | Command | AccessControlPolicyCommandService | PUT |
| UC-EF-012 | アクセス制御ポリシーを参照する | Query | AccessControlPolicyQueryService | GET |
| UC-EF-013 | 組織を登録する | Command | OrganizationCommandService | POST |
| UC-EF-014 | 組織を更新する | Command | OrganizationCommandService | PUT |
| UC-EF-015 | 組織を参照する | Query | OrganizationQueryService | GET |
| UC-EF-016 | 利用者を管理する | Command | UserAccountCommandService | POST/PUT |
| UC-EF-017 | ロールを管理する | Command | RoleCommandService | POST/PUT |
| UC-EF-018 | 外部APIキーを発行する | Command | ExternalApiKeyCommandService | POST |
| UC-EF-019 | 外部APIキーを無効化する | Command | ExternalApiKeyCommandService | PATCH |
| UC-EF-020 | 外部APIキーを参照する | Query | ExternalApiKeyQueryService | GET |
| UC-EF-021 | システム設定を登録する | Command | SystemSettingCommandService | POST |
| UC-EF-022 | システム設定を更新する | Command | SystemSettingCommandService | PUT |
| UC-EF-023 | システム設定を参照する | Query | SystemSettingQueryService | GET |

### 4.2 EF-02 帳票管理 (11UC)

| ユースケースID | ユースケース名 | Command/Query | 対応Service | HTTPメソッド |
|----------------|----------------|---------------|-------------|-------------|
| UC-EF-024 | 帳票定義を登録する | Command | FormDefinitionCommandService | POST |
| UC-EF-025 | 帳票定義を更新する | Command | FormDefinitionCommandService | PUT |
| UC-EF-026 | 帳票定義を参照する | Query | FormDefinitionQueryService | GET |
| UC-EF-027 | 帳票を生成する | Command | FormDocumentCommandService | POST |
| UC-EF-028 | 帳票生成結果を確認する | Query | FormDocumentQueryService | GET |
| UC-EF-029 | 帳票をAPIで登録する | Command | FormDocumentCommandService | POST |
| UC-EF-030 | 帳票をファイルで一括取込する | Command | FormDocumentCommandService | POST |
| UC-EF-031 | 取込結果を確認する | Query | FormDocumentQueryService | GET |
| UC-EF-032 | 帳票情報を更新する | Command | FormDocumentCommandService | PUT |
| UC-EF-033 | 帳票情報を参照する | Query | FormDocumentQueryService | GET |
| UC-EF-034 | 帳票バージョンを管理する | Query | FormVersionQueryService | GET |

### 4.3 EF-03 帳票利用 (15UC)

| ユースケースID | ユースケース名 | Command/Query | 対応Service | HTTPメソッド |
|----------------|----------------|---------------|-------------|-------------|
| UC-EF-035 | 帳票を配信する | Command | FormDeliveryCommandService | POST |
| UC-EF-036 | 配信先を設定する | Command | FormDeliveryCommandService | POST |
| UC-EF-037 | 配信状況を確認する | Query | FormDeliveryQueryService | GET |
| UC-EF-038 | 帳票をプレビューする | Query | FormPreviewQueryService | GET |
| UC-EF-039 | 帳票をページ移動する | Query | FormPreviewQueryService | GET |
| UC-EF-040 | 帳票を拡大・縮小する | Query | FormPreviewQueryService | GET |
| UC-EF-041 | 帳票をダウンロードする | Query | FormDocumentQueryService | GET |
| UC-EF-042 | 帳票を印刷する | Query | FormDocumentQueryService | GET |
| UC-EF-043 | 帳票をキーワード検索する | Query | FormSearchQueryService | GET |
| UC-EF-044 | 帳票を条件検索する | Query | FormSearchQueryService | GET |
| UC-EF-045 | 帳票一覧を表示する | Query | FormDocumentQueryService | GET |
| UC-EF-046 | 承認待ち帳票を確認する | Query | FormApprovalQueryService | GET |
| UC-EF-047 | 帳票を承認する | Command | FormApprovalCommandService | PATCH |
| UC-EF-048 | 帳票を差戻しする | Command | FormApprovalCommandService | PATCH |
| UC-EF-049 | 帳票を却下する | Command | FormApprovalCommandService | PATCH |

### 4.4 EF-04 保管・アーカイブ (16UC)

| ユースケースID | ユースケース名 | Command/Query | 対応Service | HTTPメソッド |
|----------------|----------------|---------------|-------------|-------------|
| UC-EF-050 | 保存期間を設定する | Command | RetentionPeriodCommandService | POST |
| UC-EF-051 | 廃棄対象帳票を確認する | Query | DisposalQueryService | GET |
| UC-EF-052 | 廃棄を承認する | Command | DisposalCommandService | PATCH |
| UC-EF-053 | 廃棄処理を実行する | Command | DisposalCommandService | POST |
| UC-EF-054 | アーカイブポリシーを登録する | Command | ArchivePolicyCommandService | POST |
| UC-EF-055 | アーカイブポリシーを更新する | Command | ArchivePolicyCommandService | PUT |
| UC-EF-056 | アーカイブポリシーを参照する | Query | ArchivePolicyQueryService | GET |
| UC-EF-057 | アーカイブ対象を抽出する | Command | ArchiveTargetCommandService | POST |
| UC-EF-058 | アーカイブ対象を確認する | Query | ArchiveTargetQueryService | GET |
| UC-EF-059 | アーカイブ対象を管理する | Command | ArchiveTargetCommandService | PUT |
| UC-EF-060 | アーカイブ処理を実行する | Command | ArchiveProcessCommandService | POST |
| UC-EF-061 | アーカイブ処理状況を確認する | Query | ArchiveProcessQueryService | GET |
| UC-EF-062 | 完全性検証を行う | Command | ArchiveProcessCommandService | POST |
| UC-EF-063 | 帳票復元を要求する | Command | FormRestoreCommandService | POST |
| UC-EF-064 | 帳票を自動復元する | Command | FormRestoreCommandService | POST |
| UC-EF-065 | 復元状況を確認する | Query | FormRestoreQueryService | GET |

### 4.5 EF-05 運用・監査 (15UC)

| ユースケースID | ユースケース名 | Command/Query | 対応Service | HTTPメソッド |
|----------------|----------------|---------------|-------------|-------------|
| UC-EF-066 | 通知設定を登録する | Command | NotificationSettingCommandService | POST |
| UC-EF-067 | 通知設定を更新する | Command | NotificationSettingCommandService | PUT |
| UC-EF-068 | 通知設定を参照する | Query | NotificationSettingQueryService | GET |
| UC-EF-069 | 監査ログを参照する | Query | AuditLogQueryService | GET |
| UC-EF-070 | 監査ログを検索する | Query | AuditLogQueryService | GET |
| UC-EF-071 | 監査ログをエクスポートする | Query | AuditLogQueryService | GET |
| UC-EF-072 | 処理状況を確認する | Query | OperationManagementQueryService | GET |
| UC-EF-073 | 運用エラーを確認する | Query | OperationManagementQueryService | GET |
| UC-EF-074 | エラー処理を再実行する | Command | OperationManagementCommandService | POST |
| UC-EF-075 | 帳票分析ダッシュボードを表示する | Query | FormAnalysisQueryService | GET |
| UC-EF-076 | 帳票分析レポートを生成する | Command | FormAnalysisCommandService | POST |
| UC-EF-077 | ストレージ使用量を確認する | Query | FormAnalysisQueryService | GET |
| UC-EF-078 | アーカイブ処理を監視する | Query | ArchiveOperationQueryService | GET |
| UC-EF-079 | アーカイブエラーを確認する | Query | ArchiveOperationQueryService | GET |
| UC-EF-080 | アーカイブ処理を再実行する | Command | ArchiveOperationCommandService | POST |

---

## 5. APIエンドポイント一覧

### 5.1 帳票基盤管理系

#### システム利用環境

| エンドポイント | HTTPメソッド | 説明 | 対応UC | Service |
|---------------|-------------|------|--------|---------|
| `/api/v1/environments` | POST | システム利用環境を登録する | UC-EF-001 | EnvironmentCommandService |
| `/api/v1/environments/{id}` | PUT | システム利用環境を更新する | UC-EF-002 | EnvironmentCommandService |
| `/api/v1/environments` | GET | システム利用環境一覧を取得 | UC-EF-003 | EnvironmentQueryService |
| `/api/v1/environments/{id}` | GET | システム利用環境詳細を取得 | UC-EF-003 | EnvironmentQueryService |

#### 業務システム

| エンドポイント | HTTPメソッド | 説明 | 対応UC | Service |
|---------------|-------------|------|--------|---------|
| `/api/v1/business-systems` | POST | 業務システムを登録する | UC-EF-004 | BusinessSystemCommandService |
| `/api/v1/business-systems/{id}` | PUT | 業務システムを更新する | UC-EF-005 | BusinessSystemCommandService |
| `/api/v1/business-systems` | GET | 業務システム一覧を取得 | UC-EF-006 | BusinessSystemQueryService |
| `/api/v1/business-systems/{id}` | GET | 業務システム詳細を取得 | UC-EF-006 | BusinessSystemQueryService |

#### ストレージ接続

| エンドポイント | HTTPメソッド | 説明 | 対応UC | Service |
|---------------|-------------|------|--------|---------|
| `/api/v1/storage-connections` | POST | ストレージ接続を設定する | UC-EF-007 | StorageConnectionCommandService |
| `/api/v1/storage-connections/{id}` | PUT | ストレージ接続を更新する | UC-EF-008 | StorageConnectionCommandService |
| `/api/v1/storage-connections` | GET | ストレージ接続一覧を取得 | UC-EF-009 | StorageConnectionQueryService |
| `/api/v1/storage-connections/{id}` | GET | ストレージ接続詳細を取得 | UC-EF-009 | StorageConnectionQueryService |

#### アクセス制御ポリシー

| エンドポイント | HTTPメソッド | 説明 | 対応UC | Service |
|---------------|-------------|------|--------|---------|
| `/api/v1/access-control-policies` | POST | アクセス制御ポリシーを設定する | UC-EF-010 | AccessControlPolicyCommandService |
| `/api/v1/access-control-policies/{id}` | PUT | アクセス制御ポリシーを更新する | UC-EF-011 | AccessControlPolicyCommandService |
| `/api/v1/access-control-policies` | GET | アクセス制御ポリシー一覧を取得 | UC-EF-012 | AccessControlPolicyQueryService |
| `/api/v1/access-control-policies/{id}` | GET | アクセス制御ポリシー詳細を取得 | UC-EF-012 | AccessControlPolicyQueryService |

#### 組織

| エンドポイント | HTTPメソッド | 説明 | 対応UC | Service |
|---------------|-------------|------|--------|---------|
| `/api/v1/organizations` | POST | 組織を登録する | UC-EF-013 | OrganizationCommandService |
| `/api/v1/organizations/{id}` | PUT | 組織を更新する | UC-EF-014 | OrganizationCommandService |
| `/api/v1/organizations` | GET | 組織一覧を取得 | UC-EF-015 | OrganizationQueryService |
| `/api/v1/organizations/{id}` | GET | 組織詳細を取得 | UC-EF-015 | OrganizationQueryService |

#### 利用者

| エンドポイント | HTTPメソッド | 説明 | 対応UC | Service |
|---------------|-------------|------|--------|---------|
| `/api/v1/users` | POST | 利用者を登録する | UC-EF-016 | UserAccountCommandService |
| `/api/v1/users/{id}` | PUT | 利用者を更新する | UC-EF-016 | UserAccountCommandService |
| `/api/v1/users` | GET | 利用者一覧を取得 | UC-EF-016 | UserAccountQueryService |
| `/api/v1/users/{id}` | GET | 利用者詳細を取得 | UC-EF-016 | UserAccountQueryService |

#### ロール

| エンドポイント | HTTPメソッド | 説明 | 対応UC | Service |
|---------------|-------------|------|--------|---------|
| `/api/v1/roles` | POST | ロールを登録する | UC-EF-017 | RoleCommandService |
| `/api/v1/roles/{id}` | PUT | ロールを更新する | UC-EF-017 | RoleCommandService |
| `/api/v1/roles` | GET | ロール一覧を取得 | UC-EF-017 | RoleQueryService |
| `/api/v1/roles/{id}` | GET | ロール詳細を取得 | UC-EF-017 | RoleQueryService |

#### 外部APIキー

| エンドポイント | HTTPメソッド | 説明 | 対応UC | Service |
|---------------|-------------|------|--------|---------|
| `/api/v1/external-api-keys` | POST | 外部APIキーを発行する | UC-EF-018 | ExternalApiKeyCommandService |
| `/api/v1/external-api-keys/{id}/invalidate` | PATCH | 外部APIキーを無効化する | UC-EF-019 | ExternalApiKeyCommandService |
| `/api/v1/external-api-keys` | GET | 外部APIキー一覧を取得 | UC-EF-020 | ExternalApiKeyQueryService |
| `/api/v1/external-api-keys/{id}` | GET | 外部APIキー詳細を取得 | UC-EF-020 | ExternalApiKeyQueryService |

#### システム設定

| エンドポイント | HTTPメソッド | 説明 | 対応UC | Service |
|---------------|-------------|------|--------|---------|
| `/api/v1/system-settings` | POST | システム設定を登録する | UC-EF-021 | SystemSettingCommandService |
| `/api/v1/system-settings/{id}` | PUT | システム設定を更新する | UC-EF-022 | SystemSettingCommandService |
| `/api/v1/system-settings` | GET | システム設定一覧を取得 | UC-EF-023 | SystemSettingQueryService |
| `/api/v1/system-settings/{id}` | GET | システム設定詳細を取得 | UC-EF-023 | SystemSettingQueryService |

### 5.2 帳票管理系

#### 帳票定義

| エンドポイント | HTTPメソッド | 説明 | 対応UC | Service |
|---------------|-------------|------|--------|---------|
| `/api/v1/form-definitions` | POST | 帳票定義を登録する | UC-EF-024 | FormDefinitionCommandService |
| `/api/v1/form-definitions/{id}` | PUT | 帳票定義を更新する | UC-EF-025 | FormDefinitionCommandService |
| `/api/v1/form-definitions` | GET | 帳票定義一覧を取得 | UC-EF-026 | FormDefinitionQueryService |
| `/api/v1/form-definitions/{id}` | GET | 帳票定義詳細を取得 | UC-EF-026 | FormDefinitionQueryService |

#### 帳票テンプレート

| エンドポイント | HTTPメソッド | 説明 | 対応UC | Service |
|---------------|-------------|------|--------|---------|
| `/api/v1/form-templates` | POST | 帳票テンプレートを登録する | UC-EF-024 | FormTemplateCommandService |
| `/api/v1/form-templates/{id}` | PUT | 帳票テンプレートを更新する | UC-EF-025 | FormTemplateCommandService |
| `/api/v1/form-templates` | GET | 帳票テンプレート一覧を取得 | UC-EF-026 | FormTemplateQueryService |
| `/api/v1/form-templates/{id}` | GET | 帳票テンプレート詳細を取得 | UC-EF-026 | FormTemplateQueryService |

#### 帳票ドキュメント

| エンドポイント | HTTPメソッド | 説明 | 対応UC | Service |
|---------------|-------------|------|--------|---------|
| `/api/v1/form-documents` | POST | 帳票を生成する | UC-EF-027 | FormDocumentCommandService |
| `/api/v1/form-documents/api-register` | POST | 帳票をAPIで登録する | UC-EF-029 | FormDocumentCommandService |
| `/api/v1/form-documents/bulk-import` | POST | 帳票を一括取込する | UC-EF-030 | FormDocumentCommandService |
| `/api/v1/form-documents/{id}` | PUT | 帳票情報を更新する | UC-EF-032 | FormDocumentCommandService |
| `/api/v1/form-documents` | GET | 帳票一覧を取得 | UC-EF-033 | FormDocumentQueryService |
| `/api/v1/form-documents/{id}` | GET | 帳票詳細を取得 | UC-EF-033 | FormDocumentQueryService |
| `/api/v1/form-documents/{id}/download` | GET | 帳票をダウンロードする | UC-EF-041 | FormDocumentQueryService |
| `/api/v1/form-documents/{id}/print` | GET | 帳票を印刷する | UC-EF-042 | FormDocumentQueryService |

#### 帳票バージョン

| エンドポイント | HTTPメソッド | 説明 | 対応UC | Service |
|---------------|-------------|------|--------|---------|
| `/api/v1/form-documents/{id}/versions` | GET | 帳票バージョン履歴を取得 | UC-EF-034 | FormVersionQueryService |

### 5.3 帳票利用系

#### 帳票配信

| エンドポイント | HTTPメソッド | 説明 | 対応UC | Service |
|---------------|-------------|------|--------|---------|
| `/api/v1/form-deliveries` | POST | 帳票を配信する | UC-EF-035 | FormDeliveryCommandService |
| `/api/v1/form-deliveries/recipients` | POST | 配信先を設定する | UC-EF-036 | FormDeliveryCommandService |
| `/api/v1/form-deliveries` | GET | 配信状況を確認する | UC-EF-037 | FormDeliveryQueryService |

#### 帳票プレビュー

| エンドポイント | HTTPメソッド | 説明 | 対応UC | Service |
|---------------|-------------|------|--------|---------|
| `/api/v1/form-documents/{id}/preview` | GET | 帳票をプレビューする | UC-EF-038 | FormPreviewQueryService |
| `/api/v1/form-previews/{id}/page` | GET | ページ移動する | UC-EF-039 | FormPreviewQueryService |
| `/api/v1/form-previews/{id}/zoom` | GET | 拡大・縮小する | UC-EF-040 | FormPreviewQueryService |

#### 帳票検索

| エンドポイント | HTTPメソッド | 説明 | 対応UC | Service |
|---------------|-------------|------|--------|---------|
| `/api/v1/form-documents/search/keyword` | GET | キーワード検索 | UC-EF-043 | FormSearchQueryService |
| `/api/v1/form-documents/search/filter` | GET | 条件検索 | UC-EF-044 | FormSearchQueryService |

#### 帳票承認

| エンドポイント | HTTPメソッド | 説明 | 対応UC | Service |
|---------------|-------------|------|--------|---------|
| `/api/v1/form-approvals/pending` | GET | 承認待ち帳票を確認 | UC-EF-046 | FormApprovalQueryService |
| `/api/v1/form-approvals/{id}/approve` | PATCH | 帳票を承認する | UC-EF-047 | FormApprovalCommandService |
| `/api/v1/form-approvals/{id}/return` | PATCH | 帳票を差戻しする | UC-EF-048 | FormApprovalCommandService |
| `/api/v1/form-approvals/{id}/reject` | PATCH | 帳票を却下する | UC-EF-049 | FormApprovalCommandService |

### 5.4 保管・アーカイブ系

#### 保存期間

| エンドポイント | HTTPメソッド | 説明 | 対応UC | Service |
|---------------|-------------|------|--------|---------|
| `/api/v1/retention-periods` | POST | 保存期間を設定する | UC-EF-050 | RetentionPeriodCommandService |

#### 廃棄

| エンドポイント | HTTPメソッド | 説明 | 対応UC | Service |
|---------------|-------------|------|--------|---------|
| `/api/v1/disposals/targets` | GET | 廃棄対象帳票を確認 | UC-EF-051 | DisposalQueryService |
| `/api/v1/disposals/{id}/approve` | PATCH | 廃棄を承認する | UC-EF-052 | DisposalCommandService |
| `/api/v1/disposals/execute` | POST | 廃棄処理を実行する | UC-EF-053 | DisposalCommandService |

#### アーカイブポリシー

| エンドポイント | HTTPメソッド | 説明 | 対応UC | Service |
|---------------|-------------|------|--------|---------|
| `/api/v1/archive-policies` | POST | アーカイブポリシーを登録 | UC-EF-054 | ArchivePolicyCommandService |
| `/api/v1/archive-policies/{id}` | PUT | アーカイブポリシーを更新 | UC-EF-055 | ArchivePolicyCommandService |
| `/api/v1/archive-policies` | GET | アーカイブポリシー一覧を取得 | UC-EF-056 | ArchivePolicyQueryService |
| `/api/v1/archive-policies/{id}` | GET | アーカイブポリシー詳細を取得 | UC-EF-056 | ArchivePolicyQueryService |

#### アーカイブ対象

| エンドポイント | HTTPメソッド | 説明 | 対応UC | Service |
|---------------|-------------|------|--------|---------|
| `/api/v1/archive-targets/extract` | POST | アーカイブ対象を抽出 | UC-EF-057 | ArchiveTargetCommandService |
| `/api/v1/archive-targets` | GET | アーカイブ対象を確認 | UC-EF-058 | ArchiveTargetQueryService |
| `/api/v1/archive-targets/{id}` | PUT | アーカイブ対象を管理 | UC-EF-059 | ArchiveTargetCommandService |

#### アーカイブ処理

| エンドポイント | HTTPメソッド | 説明 | 対応UC | Service |
|---------------|-------------|------|--------|---------|
| `/api/v1/archive-processes` | POST | アーカイブ処理を実行 | UC-EF-060 | ArchiveProcessCommandService |
| `/api/v1/archive-processes` | GET | アーカイブ処理状況を確認 | UC-EF-061 | ArchiveProcessQueryService |
| `/api/v1/archive-processes/{id}/verify` | POST | 完全性検証を行う | UC-EF-062 | ArchiveProcessCommandService |

#### 帳票復元

| エンドポイント | HTTPメソッド | 説明 | 対応UC | Service |
|---------------|-------------|------|--------|---------|
| `/api/v1/form-restores` | POST | 帳票復元を要求 | UC-EF-063 | FormRestoreCommandService |
| `/api/v1/form-restores/execute` | POST | 帳票を自動復元 | UC-EF-064 | FormRestoreCommandService |
| `/api/v1/form-restores` | GET | 復元状況を確認 | UC-EF-065 | FormRestoreQueryService |

### 5.5 運用・監査系

#### 通知設定

| エンドポイント | HTTPメソッド | 説明 | 対応UC | Service |
|---------------|-------------|------|--------|---------|
| `/api/v1/notification-settings` | POST | 通知設定を登録 | UC-EF-066 | NotificationSettingCommandService |
| `/api/v1/notification-settings/{id}` | PUT | 通知設定を更新 | UC-EF-067 | NotificationSettingCommandService |
| `/api/v1/notification-settings` | GET | 通知設定一覧を取得 | UC-EF-068 | NotificationSettingQueryService |
| `/api/v1/notification-settings/{id}` | GET | 通知設定詳細を取得 | UC-EF-068 | NotificationSettingQueryService |

#### 監査ログ

| エンドポイント | HTTPメソッド | 説明 | 対応UC | Service |
|---------------|-------------|------|--------|---------|
| `/api/v1/audit-logs` | GET | 監査ログを参照 | UC-EF-069 | AuditLogQueryService |
| `/api/v1/audit-logs/search` | GET | 監査ログを検索 | UC-EF-070 | AuditLogQueryService |
| `/api/v1/audit-logs/export` | GET | 監査ログをエクスポート | UC-EF-071 | AuditLogQueryService |

#### 運用管理

| エンドポイント | HTTPメソッド | 説明 | 対応UC | Service |
|---------------|-------------|------|--------|---------|
| `/api/v1/operations/status` | GET | 処理状況を確認 | UC-EF-072 | OperationManagementQueryService |
| `/api/v1/operations/errors` | GET | 運用エラーを確認 | UC-EF-073 | OperationManagementQueryService |
| `/api/v1/operations/retry` | POST | エラー処理を再実行 | UC-EF-074 | OperationManagementCommandService |

#### 帳票分析

| エンドポイント | HTTPメソッド | 説明 | 対応UC | Service |
|---------------|-------------|------|--------|---------|
| `/api/v1/form-analysis/dashboard` | GET | 分析ダッシュボードを表示 | UC-EF-075 | FormAnalysisQueryService |
| `/api/v1/form-analysis/reports` | POST | 分析レポートを生成 | UC-EF-076 | FormAnalysisCommandService |
| `/api/v1/form-analysis/storage-usage` | GET | ストレージ使用量を確認 | UC-EF-077 | FormAnalysisQueryService |

#### アーカイブ運用

| エンドポイント | HTTPメソッド | 説明 | 対応UC | Service |
|---------------|-------------|------|--------|---------|
| `/api/v1/archive-operations/monitor` | GET | アーカイブ処理を監視 | UC-EF-078 | ArchiveOperationQueryService |
| `/api/v1/archive-operations/errors` | GET | アーカイブエラーを確認 | UC-EF-079 | ArchiveOperationQueryService |
| `/api/v1/archive-operations/retry` | POST | アーカイブ処理を再実行 | UC-EF-080 | ArchiveOperationCommandService |

---

## 6. Command/Queryオブジェクト

### 6.1 Commandオブジェクト例

```kotlin
// 帳票定義作成Command
data class CreateFormDefinitionCommand(
    @field:NotBlank val formType: String,
    @field:NotBlank val formName: String,
    @field:NotNull val systemId: String,
    val layoutConfig: String?,
    val paperSize: String?,
    val orientation: String?,
    val description: String?
)

// 帳票承認Command
data class ApproveFormCommand(
    @field:NotNull val approvalId: String,
    @field:NotNull val approverId: String,
    val approvalComment: String?
)
```

### 6.2 Queryオブジェクト例

```kotlin
// 帳票一覧Query
data class ListFormDocumentsQuery(
    val formDefId: String?,
    val systemId: String?,
    val status: String?,
    val page: Int = 0,
    val size: Int = 20
)

// 帳票検索Query
data class SearchFormDocumentsQuery(
    val keywords: String,
    val filters: Map<String, String>?,
    val page: Int = 0,
    val size: Int = 20
)
```

---

## 7. エラーレスポンス

| HTTPステータス | エラーコード | 説明 |
|---------------|-------------|------|
| 400 | VALIDATION_ERROR | リクエストパラメータのバリデーションエラー |
| 401 | UNAUTHORIZED | 認証失敗 |
| 403 | FORBIDDEN | 権限不足 |
| 404 | NOT_FOUND | 対象リソースが見つからない |
| 409 | CONFLICT | 状態遷移エラー |
| 500 | INTERNAL_ERROR | システム内部エラー |
