---
sessionId: session-261109-153121-bgpc
---

# Requirements

### Overview & Goals

AD 住所レジストリの要件定義書（`docs/22 要件定義書/AD 住所レジストリ/`）を基に、詳細設計書（`docs/23 詳細設計書/AD_住所レジストリ/`）を作成します。住所情報のマスター管理（都道府県・市区町村・町字）と住所登録・更新・削除・検索機能を網羅します。

### Scope

**In Scope:**
- データベース設計: prefecture, city, word, address の4テーブル
- API設計: CQRSベースのREST API（Command/Query Service分離）
- フロントエンド設計: SCR-01001 住所管理画面
- バッチ設計: なし（外部システム経由のUCなし）

**Out of Scope:**
- バッチ処理（外部システム連携のUCが存在しないため）
- 非機能要件の詳細設計（要件定義に未定義）

### User Stories

- As a システム管理者, I want 住所を登録・更新・削除できる画面 so that 住所マスターを管理できる
- As a 従業員, I want 郵便番号から住所を検索できる so that 住所入力を効率化できる

### Functional Requirements

| ID | 要求名 | 概要 | 優先度 |
|----|--------|------|--------|
| FR-AD-01-01 | 住所管理 | 住所を管理（CRUD） | MVP |
| FR-AD-01-02 | 郵便番号検索 | 郵便番号から住所を検索 | MVP |

### Non-Functional Requirements

| ID | 要求名 | 概要 |
|----|--------|------|
| NFR-AD-01-01 | 検索性能 | 住所を迅速に検索 |

### Business Rules

- 住所登録には国・都道府県・市区町村が必須
- 郵便番号は正しい形式であること
- カスタマバーコード値は23桁（スタートコード1 + 郵便番号7 + 住所表示番号13 + チェックデジット1 + ストップコード1）

### State Model

| 状態 | 遷移先 | 遷移UC | 説明 |
|------|--------|--------|------|
| 新規 | 登録済み | UC-AD-001 | 住所を登録する |
| 登録済み | 更新済み | UC-AD-002 | 住所を更新する |
| 更新済み | end | - | - |

### Variations

| バリエーション | 値 | 説明 |
|---------------|-----|------|
| 住所状態 | 新規 | 住所が新規に登録された状態 |
| 住所状態 | 登録済み | 住所が登録済みの状態 |
| 住所状態 | 更新済み | 住所が更新済みの状態 |


# Technical Design

### Current Implementation

AD 住所レジストリの要件定義書は `docs/22 要件定義書/AD 住所レジストリ/03 機能要件/AD-01 住所管理/` に配置されています。既存の詳細設計書は存在せず、新規作成が必要です。

参照する既存の詳細設計書パターン:
- `docs/23 詳細設計書/AS_関連付け/` — 関連付け事業領域の詳細設計（データベース・API・フロントエンド）
- `docs/23 詳細設計書/NT_通知/` — 通知事業領域の詳細設計

### Key Decisions

1. **スキーマ名**: `undecided_ad` — ADは「汎用」区分のため、事業領域コードを使用
2. **CQRSアーキテクチャ**: Command Service（書き込み）とQuery Service（読み込み）を分離
3. **テーブル構成**: 4テーブル（prefecture, city, word, address）— 親子関係で外部キー制約
4. **状態管理**: VARCHARカラムで住所状態（新規, 登録済み, 更新済み）を管理
5. **フロントエンド**: MUI v5 + React Router v6 + TypeScript strictモード
6. **バッチ処理**: なし — 外部システム経由のUCが存在しないため

### Proposed Changes

#### データベース設計

4つのテーブルを定義:

1. **prefecture** — 都道府県マスター
   - PK: prefecture_id (BIGINT)
   - カラム: name (VARCHAR(100)), code (VARCHAR(20)), country (VARCHAR(50))
   - 標準カラム: created_at, updated_at, deleted_at

2. **city** — 市区町村マスター
   - PK: city_id (BIGINT)
   - カラム: name (VARCHAR(100)), code (VARCHAR(20)), prefecture_id (BIGINT FK)
   - 標準カラム: created_at, updated_at, deleted_at

3. **word** — 町字マスター
   - PK: word_id (BIGINT)
   - カラム: name (VARCHAR(100)), code (VARCHAR(20)), city_id (BIGINT FK)
   - 標準カラム: created_at, updated_at, deleted_at

4. **address** — 住所
   - PK: address_id (BIGINT)
   - カラム: country (VARCHAR(50)), prefecture_id (BIGINT FK), city_id (BIGINT FK), word_id (BIGINT FK), postal_code (VARCHAR(10)), customer_barcode (VARCHAR(23)), state (VARCHAR(20))
   - 標準カラム: created_at, updated_at, deleted_at
   - 制約: 国・都道府県・市区町村必須, 郵便番号形式, カスタマバーコード23桁

#### API設計

**Command Service（書き込み）:**
- `POST /api/v1/addresses` — 住所登録 (UC-AD-001)
- `PUT /api/v1/addresses/{id}` — 住所更新 (UC-AD-002)
- `DELETE /api/v1/addresses/{id}` — 住所削除 (UC-AD-003)

**Query Service（読み込み）:**
- `GET /api/v1/addresses` — 住所一覧取得
- `GET /api/v1/addresses/{id}` — 住所詳細取得
- `GET /api/v1/addresses/search` — 郵便番号検索 (FR-AD-01-02)

**Master系API:**
- `GET /api/v1/prefectures` — 都道府県一覧
- `GET /api/v1/cities?prefectureId={id}` — 市区町村一覧
- `GET /api/v1/words?cityId={id}` — 町字一覧

#### フロントエンド設計

- SCR-01001 住所管理画面
- ルーティング: `/addresses` (一覧), `/addresses/:id` (詳細/編集)
- MUI v5 コンポーネント: TextField, Select, Button, Table, Dialog
- TypeScript 型定義: Address, Prefecture, City, Word
- ステート管理: useState (ローカル), カスタムフック (API通信)

### Architecture Diagram

``` mermaid
graph LR
    A[SCR-01001 住所管理画面] --> B[AddressCommandController]
    A --> C[AddressQueryController]
    A --> D[PrefectureQueryController]
    A --> E[CityQueryController]
    A --> F[WordQueryController]
    B --> G[AddressCommandService]
    C --> H[AddressQueryService]
    D --> I[PrefectureQueryService]
    E --> J[CityQueryService]
    F --> K[WordQueryService]
    G --> L[(address)]
    H --> L
    I --> M[(prefecture)]
    J --> N[(city)]
    K --> O[(word)]
    L --> M
    L --> N
    L --> O
    N --> M
    O --> N
```

### File Structure

```
docs/23 詳細設計書/AD_住所レジストリ/
├── index.md                            # 目次
├── 01 データベース設計/
│   ├── index.md                        # データベース設計概要
│   ├── 01 table一覧.md                 # テーブル一覧
│   ├── 01 table一覧.tsv                # テーブル一覧(TSV)
│   ├── 02 テーブルカラム定義/
│   │   ├── index.md                    # カラム定義概要
│   │   ├── prefecture.md               # 都道府県カラム定義
│   │   ├── city.md                     # 市区町村カラム定義
│   │   ├── word.md                     # 町字カラム定義
│   │   └── address.md                  # 住所カラム定義
│   └── 03 viewカラム定義/
│       └── index.md                    # View定義
├── backend/
│   ├── api/
│   │   ├── index.md                    # API設計概要
│   │   ├── address-command-api.md      # 住所Command API
│   │   ├── address-query-api.md        # 住所Query API
│   │   └── master-query-api.md         # マスターQuery API
│   └── batch/
│       └── index.md                    # バッチ設計（なし）
└── frontend/
    ├── index.md                        # フロントエンド設計概要
    └── SCR-01001_画面設計.md           # 住所管理画面設計
```

### Risks

- **郵便番号検索の精度**: 郵便番号と住所の対応が1対多の場合、検索結果の表示方法を検討
- **カスタマバーコードの生成**: 23桁のフォーマットが正確に生成されるようバリデーションを厳格に
- **マスターデータの初期投入**: 都道府県・市区町村・町字の初期データ投入方法を検討


# Testing

### Validation Approach

詳細設計書が要件定義書を忠実に反映していることを確認します。各フェーズのチェックリストに従って整合性を検証します。

### Key Scenarios

1. **データベース設計の整合性**:
   - 全情報（住所, 都道府県, 市, 字）がテーブルとして定義されている
   - 全属性がカラムとして定義されている
   - 状態・バリエーションが列挙型として定義されている
   - 標準カラムが全テーブルに含まれている
   - 外部キー関係が正しい（prefecture → city → word → address）

2. **API設計の整合性**:
   - 全UCがAPIとして定義されている（UC-AD-001〜004）
   - Command ServiceとQuery Serviceが適切に分離されている
   - 全画面経由の操作がAPIとしてカバーされている
   - バリデーションルールが定義されている
   - エラーレスポンスが定義されている

3. **フロントエンド設計の整合性**:
   - 全画面（SCR-01001）が設計書として定義されている
   - UIコンポーネントが適切に定義されている
   - 型定義が明確で `any`型を使用していない
   - API連携ポイントが定義されている
   - 画面遷移が定義されている
   - アクセシビリティ基準を満たしている

### Edge Cases

- 郵便番号が複数住所に対応する場合の検索結果
- 都道府県・市区町村・町字の階層関係が壊れた場合の整合性
- カスタマバーコードの23桁フォーマット違反
- 必須項目（国・都道府県・市区町村）が未入力の場合のバリデーション

### Test Changes

- 詳細設計書の新規作成のため、既存テストの変更はなし
- 各設計書の整合性チェックリストに従って検証

# Delivery Steps

###   Step 1: AD_住所レジストリ 詳細設計書の基本構造とindex.mdを作成
詳細設計書の基本ディレクトリ構造と目次を作成します。

- `docs/23 詳細設計書/AD_住所レジストリ/` ディレクトリを作成
- `index.md` を作成（目次・ディレクトリ構成・参照リンク）
- 各サブディレクトリ（01 データベース設計, backend/api, backend/batch, frontend）を作成

###   Step 2: データベース設計（4テーブル: prefecture, city, word, address）
要件定義のRDRAモデルからデータベース設計を導出します。

- テーブル一覧（01 table一覧.md/tsv）を作成：prefecture, city, word, address の4テーブル
- 各テーブルのカラム定義（02 テーブルカラム定義/）を作成
  - prefecture.md: 都道府県マスター（prefecture_id PK, name, code, country）
  - city.md: 市区町村マスター（city_id PK, name, code, prefecture_id FK）
  - word.md: 町字マスター（word_id PK, name, code, city_id FK）
  - address.md: 住所（address_id PK, country, prefecture_id FK, city_id FK, word_id FK, postal_code, customer_barcode, state）
- 標準カラム（id, created_at, updated_at, deleted_at）を全テーブルに追加
- 外部キー・インデックス・制約を定義
- 状態・バリエーションをVARCHARカラムとして定義（住所状態: 新規, 登録済み, 更新済み）
- Flywayマイグレーションスクリプトを作成
- viewカラム定義（03 viewカラム定義/）のindex.mdを作成

###   Step 3: API設計（CQRS: Command/Query Service分離）
UCと画面からCQRSベースのREST API設計を導出します。

- API設計概要（backend/api/index.md）を作成
  - APIアーキテクチャ・セキュリティ・ページネーション・バリデーション・エラーハンドリング
- Command Service（書き込み）のAPI設計:
  - POST /api/v1/addresses — 住所登録（UC-AD-001）
  - PUT /api/v1/addresses/{id} — 住所更新（UC-AD-002）
  - DELETE /api/v1/addresses/{id} — 住所削除（UC-AD-003）
- Query Service（読み込み）のAPI設計:
  - GET /api/v1/addresses — 住所一覧取得
  - GET /api/v1/addresses/{id} — 住所詳細取得
  - GET /api/v1/addresses/search — 郵便番号検索（FR-AD-01-02）
- Master系API:
  - GET /api/v1/prefectures — 都道府県一覧
  - GET /api/v1/cities — 市区町村一覧（都道府県IDでフィルタ）
  - GET /api/v1/words — 町字一覧（市IDでフィルタ）
- リクエスト・レスポンスDTOを定義
- バリデーションルールを定義（国・都道府県・市区町村必須、郵便番号形式、カスタマバーコード23桁）
- エラーレスポンス・エラーコードを定義

###   Step 4: バッチ設計（なし）
外部システム経由のUCがないためバッチ設計はスキップします。

- backend/batch/index.md を作成（バッチ処理なしの旨を記載）

###   Step 5: フロントエンド設計（SCR-01001 住所管理画面）
画面属性からフロントエンド詳細設計を導出します。

- フロントエンド設計概要（frontend/index.md）を作成
- SCR-01001 住所管理画面設計（frontend/SCR-01001_画面設計.md）を作成
  - ルーティング設計: /addresses, /addresses/:id
  - 画面イメージ（ASCII ワイヤーフレーム）
  - UIコンポーネント一覧（MUI v5 コンポーネントマッピング）
  - 画面項目一覧（国, 都道府県, 市, 字, 郵便番号, カスタマバーコード値, state）
  - 画面操作一覧（登録, 更新, 削除, 検索）
  - TypeScript 型定義
  - ステート管理設計（useState, useReducer, カスタムフック）
  - API連携ポイント
  - 画面遷移図（Mermaid stateDiagram-v2）
  - アクセシビリティ対応（デジタル庁デザインシステム基準）