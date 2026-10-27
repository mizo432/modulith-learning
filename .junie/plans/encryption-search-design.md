---
sessionId: session-261027-083406-6nef
---

# Requirements

## Overview & Goals

Issue #320「機微な情報はDBに格納する際は暗号化する」の詳細化において、
**暗号化されたデータはDBでの直接検索ができません**という制約に対する設計方針を明確にします。

## Current Search Usage

プロジェクト内の機微な情報の検索使用情况:

| 情報種類                  | 検索方法                  | 使用箇所                     | 検索頻度               |
|---------------------------|---------------------------|------------------------------|------------------------|
| `searchName` (検索用名称) | 完全一致 / 部分一致(LIKE) | SimpleEmployee.searchName    | 従業員検索で頻繁使用   |
| `email`                   | 完全一致                  | UserRepository.findByEmail() | ログイン・ユーザー検索 |
| `firstName` / `lastName`  | 検索なし                  | 表示用のみ                   | -                      |
| `phoneNumber`             | 検索なし                  | SampleContact（サンプル）    | -                      |

## Key Finding

プロジェクトには既に **`SearchName`（検索用名称）**
という値オブジェクトが存在し、機微な情報（氏名）と検索キーを分離する設計が採用されています:

- `undecided.association.relationshipMgmt.spi.party.SearchName`: 共通の検索用名称
- `undecided.association.relationshipMgmt.personRole.spi.employee.SearchName`: Employee用の検索用名称
- `SimpleEmployee` エンティティは `searchName` と `fullName` を別フィールドで保持

## Design Decision: SearchName Separation Approach

**採用方針**: 既存の `SearchName` 分離パターンを拡張し、暗号化と検索の両立を実現。

### 暗号化対象

- `User.firstName`, `User.lastName` – 表示用のみ、検索不要
- `User.email` – 完全一致検索のみ（暗号化可能、後述）
- `Employee.firstName`, `Employee.lastName` – 表示用のみ、検索不要
- `FirstName.value`, `LastName.value` – 表示用のみ

### 暗号化不要（検索用）

- `SearchName.value` – 検索用名称は平文で保持、DB検索可能
- `User.username` – 認証用ID、検索用

### Email検索の維持

- `findByEmail()` は完全一致検索のみ
- 暗号化後の完全一致検索は困難 → **Emailも検索用として平文保持**、またはハッシュ値で検索
- 推奨: Emailは「表示用（暗号化）」と「検索用（平文）」を分離、またはEmailは暗号化対象外とする

## Scope

**In Scope:**

- 暗号化と検索機能の兼ね合い設計の文書化
- 各機微な情報フィールドの暗号化対象/非対象の明確化
- Issue #320の詳細化更新

**Out of Scope:**

- 実際の暗号化実装（別issueで対応）
- フロントエンドの変更

# Technical Design

## Current Implementation

### 既存のSearchName分離パターン

```
SimpleEmployee (backend)
├── searchName: SearchName  → 検索用名称（平文、DB検索可能）
├── fullName: FullName      → 表示用氏名（暗号化対象）
└── employeeNo: EmployeeNo  → 従業員番号

User (authorization)
├── username: String        → 認証用ID（平文）
├── email: String           → メールアドレス（完全一致検索使用）
├── firstName: String       → ファーストネーム（表示用）
├── lastName: String        → ラストネーム（表示用）
└── password: String        → BCryptハッシュ済み
```

### 検索使用箇所

1. **UserRepository.findByEmail ()**: 完全一致検索のみ
2. **SimpleEmployee.searchName**: 検索用名称での検索（LIKE検索の可能性あり）
3. **SampleProductSearch**: 検索パターンのサンプル（LIKE、Specification）

## Proposed Changes

### Approach: SearchName分離パターンの拡張

既存の `SearchName` 分離パターンを維持・拡張し、以下のルールで暗号化対象を決定:

```
graph LR
    A[機微な情報] --> B{検索が必要か?}
    B -->|はい| C[検索用フィールドを平文で保持]
    B -->|いいえ| D[暗号化して保持]
    C --> E[SearchNameなど]
    D --> F[firstName, lastNameなど]
    C --> G[Email: 完全一致のみ]
    G --> H[検索用として平文保持]
```

### 暗号化対象/非対象の決定

| フィールド           | 暗号化      | 理由                             |
|----------------------|-------------|----------------------------------|
| `User.firstName`     | ✅ 暗号化   | 検索不使用、表示のみ             |
| `User.lastName`      | ✅ 暗号化   | 検索不使用、表示のみ             |
| `User.email`         | ❌ 平文保持 | findByEmail() で完全一致検索使用 |
| `Employee.firstName` | ✅ 暗号化   | 検索不使用                       |
| `Employee.lastName`  | ✅ 暗号化   | 検索不使用                       |
| `FirstName.value`    | ✅ 暗号化   | 表示用値オブジェクト             |
| `LastName.value`     | ✅ 暗号化   | 表示用値オブジェクト             |
| `SearchName.value`   | ❌ 平文保持 | 検索用名称、DB検索必須           |
| `User.username`      | ❌ 平文保持 | 認証用ID                         |

### Email検索の代替案（将来の検討）

1. **Emailハッシュ検索**: EmailのSHA-256ハッシュ値を別カラムに保持し、検索時はハッシュ値で一致判定
2. **Email分離**: Emailを「表示用（暗号化）」と「検索用（平文）」に分離
3. **Email暗号化対象外**: 検索頻度が高い場合、Emailは暗号化対象外とする（推奨）

### 実装方針

- **JPA AttributeConverter** でエンティティレベルの暗号化/復号化
- **Spring Security Crypto** の `AesGcmContentEncryptor` を利用
- **鍵管理**: 環境変数から取得、本番環境では秘密管理サービス連携
- **SearchNameConverter**: 既存の `SearchNameConverter` は変更不要（平文変換のみ）

### Architecture Diagram

``` mermaid
graph LR
    subgraph "DB Layer"
        D1[encrypted_first_name] 
        D2[encrypted_last_name]
        D3[search_name PLAIN]
        D4[email PLAIN]
    end
    subgraph "Entity Layer"
        E1[User.firstName<br/>encrypted]
        E2[User.lastName<br/>encrypted]
        E3[SearchName<br/>plain]
        E4[User.email<br/>plain]
    end
    subgraph "Converter Layer"
        C1[EncryptedStringConverter]
        C2[SearchNameConverter]
    end
    D1 --> C1 --> E1
    D2 --> C1 --> E2
    D3 --> C2 --> E3
    D4 --> E4
```

### Risks

1. **Email検索のパフォーマンス**: 完全一致検索はインデックス使用可能だが、データ量増加時の影響を監視
2. **SearchNameの整合性**: 検索用名称と表示用氏名の整合性を維持するビジネスロジックが必要
3. **既存データのマイグレーション**: 既存データの暗号化移行時のダウンタイム

# Testing

## Validation Approach

Issue #320の詳細化が適切に行われたことを検証。

## Key Scenarios

1. **暗号化対象/非対象の明確化**: 全ての機微な情報フィールドが暗号化対象か非対象かに分類されている
2. **検索機能の維持**: 既存の検索機能が暗号化後も動作することが設計上保証されている
3. **SearchName分離パターンの一貫性**: 既存のSearchNameパターンが維持・拡張されている

## Edge Cases

1. **Email検索**: findByEmail () が暗号化後も動作すること
2. **SearchName整合性**: 検索用名称と表示用氏名の整合性維持
3. **既存データマイグレーション**: 既存データの暗号化移行計画

## Test Changes

- 暗号化/復号化のユニットテスト計画
- 鍵ローテーション時のデータ移行テスト計画
- パフォーマンスベンチマーク計画

# Delivery Steps

### ✓ Step 1: Create GitHub Issue #320 detailed body with encryption-search tradeoff design

Issue #320のBodyを暗号化と検索機能の兼ね合いを含む完全な詳細化内容に更新しました。

- [x] Issue #320のBodyをGitHub API経由で更新
- [x] 暗号化対象情報のマッピング表（エンティティ・フィールド単位）
- [x] 検索機能との兼ね合い設計（SearchName分離アプローチ、email完全一致検索の維持方法）
- [x] 暗号化方式（AES-256-GCM）、鍵管理、AttributeConverter実装方針
- [x] 影響範囲、テスト戦略、リスクと緩和策
- [x] Flywayマイグレーション計画

**完了**: https://github.com/mizo432/modulith-learning/issues/320

### ✓ Step 2: Add Spring Security Crypto dependency to backend and authorization modules

Add `spring-security-crypto` dependency to both `backend/build.gradle.kts` and
`authorization/build.gradle.kts`.

### ✓ Step 3: Create encryption configuration class in backend shared package

Create `EncryptionConfig` in `undecided.shared.config` package with AES-256-GCM encryptor bean,
reading the encryption key from environment variable `ENCRYPTION_KEY`.

### ✓ Step 4: Create EncryptedStringConverter in backend shared package

Create `EncryptedStringConverter` in `undecided.shared.converter` package as a JPA
`AttributeConverter<String, String>` that encrypts/decrypts String values using the AES-256-GCM
encryptor. Mark with `@Converter(autoApply = false)` so it is applied explicitly via `@Convert`.

### ✓ Step 5: Apply encryption to User entity firstName and lastName fields

Update `User.java` in the authorization module to apply `@Convert` annotation on `firstName` and
`lastName` fields. Create a corresponding `EncryptedStringConverter` in the authorization module or
reference the shared one. Add Flyway migration to alter the `users` table columns.

### ✓ Step 6: Apply encryption to FirstName and LastName value object converters

Update `FirstNameConverter` and `LastNameConverter` in the backend module to encrypt/decrypt the
value field.

### ✓ Step 7: Apply encryption to Employee entity firstName and lastName fields

Update `Employee.java` in the backend module to apply encryption on `firstName` and `lastNane`
(lastName typo) fields.

### ✓ Step 8: Create Flyway migrations for encrypted columns

Create Flyway migration scripts to alter the column types for encrypted fields in both authorization
and backend modules.

### ✓ Step 9: Create unit tests for encryption converter

Create unit tests for `EncryptedStringConverter` covering encryption, decryption, null handling, and
empty string handling.

### ✓ Step 10: Verify build and tests pass

Run `./gradlew :backend:test :authorization:test` to verify all tests pass.

**完了**: All tests passed successfully.
