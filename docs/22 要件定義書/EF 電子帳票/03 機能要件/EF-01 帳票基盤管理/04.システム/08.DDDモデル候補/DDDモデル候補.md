# DDDモデル候補

## 概要

本ファイルは、EF-01 帳票基盤管理のDDD（Domain-Driven Design）モデル候補を定義します。

## サブドメイン候補

| 項番 | サブドメイン名 | 種類 | 説明 |
|------|---------------|------|------|
| 01 | システム環境管理 | コア | システム利用環境の管理 |
| 02 | 業務システム管理 | コア | 接続する業務システムの管理 |
| 03 | ストレージ管理 | コア | 外部ストレージ接続の管理 |
| 04 | アクセス制御 | コア | アクセス制御ポリシーの管理 |
| 05 | 組織・利用者管理 | 支援 | 組織・部署・利用者の管理 |
| 06 | ロール管理 | 支援 | 権限ロールの管理 |
| 07 | 外部API管理 | 支援 | 外部システム連携用のAPIキー管理 |
| 08 | システム設定管理 | 支援 | システム全体の設定管理 |

## アグリゲート候補

| 項番 | アグリゲート名 | 集約ルート | 説明 |
|------|---------------|-----------|------|
| 01 | SystemEnvironment | SystemEnvironment | システム利用環境の集約 |
| 02 | BusinessSystem | BusinessSystem | 業務システムの集約 |
| 03 | StorageConnection | StorageConnection | ストレージ接続の集約 |
| 04 | AccessControlPolicy | AccessControlPolicy | アクセス制御ポリシーの集約 |
| 05 | Organization | Organization | 組織の集約 |
| 06 | User | User | 利用者の集約 |
| 07 | Role | Role | ロールの集約 |
| 08 | ExternalApiKey | ExternalApiKey | 外部APIキーの集約 |
| 09 | SystemSetting | SystemSetting | システム設定の集約 |

## エンティティ候補

| 項番 | エンティティ名 | アグリゲート | 説明 |
|------|---------------|-------------|------|
| 01 | SystemEnvironment | SystemEnvironment | システム利用環境 |
| 02 | BusinessSystem | BusinessSystem | 業務システム |
| 03 | StorageConnection | StorageConnection | ストレージ接続 |
| 04 | AccessControlPolicy | AccessControlPolicy | アクセス制御ポリシー |
| 05 | Organization | Organization | 組織 |
| 06 | User | User | 利用者 |
| 07 | UserRole | User | 利用者ロール関連 |
| 08 | Role | Role | ロール |
| 09 | ExternalApiKey | ExternalApiKey | 外部APIキー |
| 10 | SystemSetting | SystemSetting | システム設定 |

## 値オブジェクト候補

| 項番 | 値オブジェクト名 | 使用箇所 | 説明 |
|------|-----------------|---------|------|
| 01 | EnvironmentId | SystemEnvironment | 環境識別子 |
| 02 | TenantId | SystemEnvironment | テナント識別子 |
| 03 | SystemId | BusinessSystem | 業務システム識別子 |
| 04 | SystemName | BusinessSystem | 業務システム名 |
| 05 | StorageId | StorageConnection | ストレージ識別子 |
| 06 | EndpointUrl | StorageConnection | エンドポイントURL |
| 07 | BucketName | StorageConnection | バケット名 |
| 08 | PolicyId | AccessControlPolicy | ポリシー識別子 |
| 09 | AccessRule | AccessControlPolicy | アクセスルール |
| 10 | OrgId | Organization | 組織識別子 |
| 11 | OrgName | Organization | 組織名 |
| 12 | UserId | User | 利用者識別子 |
| 13 | LoginId | User | ログインID |
| 14 | UserName | User | 利用者名 |
| 15 | RoleId | Role | ロール識別子 |
| 16 | RoleName | Role | ロール名 |
| 17 | ApiKeyId | ExternalApiKey | APIキー識別子 |
| 18 | ApiKey | ExternalApiKey | APIキー値 |
| 19 | SettingId | SystemSetting | 設定識別子 |
| 20 | SettingKey | SystemSetting | 設定キー |

## ドメインサービス候補

| 項番 | サービス名 | 説明 |
|------|-----------|------|
| 01 | AccessControlService | アクセス制御の判定ロジック |
| 02 | StorageConnectionService | ストレージ接続のテスト・検証 |
| 03 | ApiKeyGenerationService | APIキーの生成・検証 |

## ドメインイベント候補

| 項番 | イベント名 | 発生元 | 説明 |
|------|-----------|--------|------|
| 01 | SystemEnvironmentRegistered | SystemEnvironment | システム利用環境が登録された |
| 02 | BusinessSystemRegistered | BusinessSystem | 業務システムが登録された |
| 03 | UserRegistered | User | 利用者が登録された |
| 04 | AccessControlPolicyChanged | AccessControlPolicy | アクセス制御ポリシーが変更された |
