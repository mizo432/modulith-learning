# 情報ER図

## 概要

本ファイルは、EF-01 帳票基盤管理の情報エンティティ間の関係をER図で定義します。

## ER図（英語版）

```plantuml
@startuml
skinparam linetype ortho

entity "SystemEnvironment\nINF-EF-01" as SystemEnvironment {
  * environment_id : String
  * tenant_id : String
  * system_name : String
  * environment_type : String
  --
  created_at : LocalDateTime
  updated_at : LocalDateTime
}

entity "BusinessSystem\nINF-EF-02" as BusinessSystem {
  * system_id : String
  * system_name : String
  * connection_config : String
  * status : String
  --
  created_at : LocalDateTime
  updated_at : LocalDateTime
}

entity "StorageConnection\nINF-EF-03" as StorageConnection {
  * storage_id : String
  * storage_type : String
  * endpoint_url : String
  * bucket_name : String
  * access_key : String
  * secret_key : String
  --
  created_at : LocalDateTime
  updated_at : LocalDateTime
}

entity "AccessControlPolicy\nINF-EF-04" as AccessControlPolicy {
  * policy_id : String
  * system_id : String
  * policy_name : String
  * access_rule : String
  --
  created_at : LocalDateTime
  updated_at : LocalDateTime
}

entity "Organization\nINF-EF-05" as Organization {
  * org_id : String
  * org_name : String
  * parent_org_id : String
  * org_type : String
  --
  created_at : LocalDateTime
  updated_at : LocalDateTime
}

entity "User\nINF-EF-06" as User {
  * user_id : String
  * login_id : String
  * user_name : String
  * org_id : String
  * status : String
  --
  created_at : LocalDateTime
  updated_at : LocalDateTime
}

entity "Role\nINF-EF-07" as Role {
  * role_id : String
  * role_name : String
  * role_type : String
  * permission_set : String
  --
  created_at : LocalDateTime
  updated_at : LocalDateTime
}

entity "ExternalApiKey\nINF-EF-08" as ExternalApiKey {
  * api_key_id : String
  * system_id : String
  * api_key_name : String
  * api_key : String
  * api_secret : String
  * status : String
  --
  created_at : LocalDateTime
  updated_at : LocalDateTime
}

entity "SystemSetting\nINF-EF-09" as SystemSetting {
  * setting_id : String
  * setting_key : String
  * setting_value : String
  * category : String
  --
  created_at : LocalDateTime
  updated_at : LocalDateTime
}

entity "UserRole\n" as UserRole {
  * user_id : String
  * role_id : String
  --
  assigned_at : LocalDateTime
}

SystemEnvironment "1" -- "0..*" BusinessSystem : manages >
BusinessSystem "1" -- "0..*" AccessControlPolicy : has >
BusinessSystem "1" -- "0..*" ExternalApiKey : has >
Organization "1" -- "0..*" Organization : contains >
Organization "1" -- "0..*" User : has >
User "0..*" -- "0..*" Role : assigned >
UserRole -- User
UserRole -- Role

@enduml
```

## ER図（日本語版）

```plantuml
@startuml
skinparam linetype ortho

entity "システム利用環境\nINF-EF-01" as SystemEnvironment {
  * 環境ID : String
  * テナントID : String
  * システム名 : String
  * 環境区分 : String
  --
  作成日時 : LocalDateTime
  更新日時 : LocalDateTime
}

entity "業務システム\nINF-EF-02" as BusinessSystem {
  * システムID : String
  * システム名 : String
  * 接続設定 : String
  * 状態 : String
  --
  作成日時 : LocalDateTime
  更新日時 : LocalDateTime
}

entity "ストレージ接続\nINF-EF-03" as StorageConnection {
  * ストレージID : String
  * ストレージ種類 : String
  * エンドポイントURL : String
  * バケット名 : String
  * アクセスキー : String
  * シークレットキー : String
  --
  作成日時 : LocalDateTime
  更新日時 : LocalDateTime
}

entity "アクセス制御ポリシー\nINF-EF-04" as AccessControlPolicy {
  * ポリシーID : String
  * システムID : String
  * ポリシー名 : String
  * アクセスルール : String
  --
  作成日時 : LocalDateTime
  更新日時 : LocalDateTime
}

entity "組織\nINF-EF-05" as Organization {
  * 組織ID : String
  * 組織名 : String
  * 親組織ID : String
  * 組織種類 : String
  --
  作成日時 : LocalDateTime
  更新日時 : LocalDateTime
}

entity "利用者\nINF-EF-06" as User {
  * 利用者ID : String
  * ログインID : String
  * 氏名 : String
  * 組織ID : String
  * 状態 : String
  --
  作成日時 : LocalDateTime
  更新日時 : LocalDateTime
}

entity "ロール\nINF-EF-07" as Role {
  * ロールID : String
  * ロール名 : String
  * ロール種類 : String
  * 権限セット : String
  --
  作成日時 : LocalDateTime
  更新日時 : LocalDateTime
}

entity "外部APIキー\nINF-EF-08" as ExternalApiKey {
  * APIキーID : String
  * システムID : String
  * APIキー名 : String
  * APIキー : String
  * APIシークレット : String
  * 状態 : String
  --
  作成日時 : LocalDateTime
  更新日時 : LocalDateTime
}

entity "システム設定\nINF-EF-09" as SystemSetting {
  * 設定ID : String
  * 設定キー : String
  * 設定値 : String
  * カテゴリ : String
  --
  作成日時 : LocalDateTime
  更新日時 : LocalDateTime
}

entity "利用者ロール\n" as UserRole {
  * 利用者ID : String
  * ロールID : String
  --
  付与日時 : LocalDateTime
}

SystemEnvironment "1" -- "0..*" BusinessSystem : 管理 >
BusinessSystem "1" -- "0..*" AccessControlPolicy : 保有 >
BusinessSystem "1" -- "0..*" ExternalApiKey : 保有 >
Organization "1" -- "0..*" Organization : 包含 >
Organization "1" -- "0..*" User : 所属 >
User "0..*" -- "0..*" Role : 付与 >
UserRole -- User
UserRole -- Role

@enduml
```

## 情報エンティティ一覧

| 情報ID | 情報名 | 主要属性 | 関連エンティティ |
|--------|--------|----------|-----------------|
| INF-EF-01 | システム利用環境 | environment_id, tenant_id, system_name, environment_type | 業務システム |
| INF-EF-02 | 業務システム | system_id, system_name, connection_config, status | アクセス制御ポリシー, 外部APIキー |
| INF-EF-03 | ストレージ接続 | storage_id, storage_type, endpoint_url, bucket_name | - |
| INF-EF-04 | アクセス制御ポリシー | policy_id, system_id, policy_name, access_rule | 業務システム |
| INF-EF-05 | 組織 | org_id, org_name, parent_org_id, org_type | 組織(自己参照), 利用者 |
| INF-EF-06 | 利用者 | user_id, login_id, user_name, org_id, status | 組織, ロール |
| INF-EF-07 | ロール | role_id, role_name, role_type, permission_set | 利用者 |
| INF-EF-08 | 外部APIキー | api_key_id, system_id, api_key_name, api_key, status | 業務システム |
| INF-EF-09 | システム設定 | setting_id, setting_key, setting_value, category | - |

## 関係一覧

| 源エンティティ | 関係 | 対象エンティティ | 多重度 | 説明 |
|---------------|------|-----------------|--------|------|
| システム利用環境 | 管理 | 業務システム | 1 : 0..* | 1つの環境に複数の業務システムが存在 |
| 業務システム | 保有 | アクセス制御ポリシー | 1 : 0..* | 1つの業務システムに複数のアクセス制御ポリシーが存在 |
| 業務システム | 保有 | 外部APIキー | 1 : 0..* | 1つの業務システムに複数の外部APIキーが存在 |
| 組織 | 包含 | 組織 | 1 : 0..* | 階層構造（自己参照） |
| 組織 | 所属 | 利用者 | 1 : 0..* | 1つの組織に複数の利用者が所属 |
| 利用者 | 付与 | ロール | 0..* : 0..* | 複数の利用者に複数のロールを付与 |
