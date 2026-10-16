# ユースケース記述

## 概要

承認申請事業領域のAPIユースケース記述を定義します。
本ドキュメントは、RDRAモデルで定義されたユースケースを外部設計レベルで詳細化したものです。

## 参照

- RDRAユースケース一覧:
  `docs/12 RDRA モデル/06 機能要件/03.開発運用/08.データベース定義/03.システム境界/03.ユースケース/02.ユースケース一覧.md`
- RDRAユースケース記述:
  `docs/12 RDRA モデル/06 機能要件/03.開発運用/08.データベース定義/03.システム境界/03.ユースケース/04.ユースケース記述/`
- API設計ガイドライン: `.agents/skills/api-design/SKILL.md`

---

## UC-01 データベース一覧を取得する

### 概要

接続可能なデータベースの一覧を取得する。

### アクター

- 主要アクター: 開発者

### 目標

開発者が接続可能なデータベースの一覧を確認できること。

### 前提条件

- システムが起動していること
- データベース接続設定が登録されていること

### トリガー

開発者がデータベース一覧の取得をリクエストする

### 基本フロー

1. 開発者がデータベース一覧の取得をリクエストする
2. システムが接続可能なデータベースの一覧を取得する
3. システムがデータベース一覧を表示する
4. ユースケースが完了する

### 代替フロー

A1 接続できないデータベースが存在する場合

- システムがエラーレスポンスを返す
- ユースケースが完了する

### 後件条件

- データベース一覧が取得される

### API仕様

| 項目             | 値                  |
|------------------|---------------------|
| HTTPメソッド     | GET                 |
| エンドポイント   | `/api/v1/databases` |
| 認証             | 必要                |
| リクエストボディ | なし                |
| クエリパラメータ | なし                |

#### リクエスト例

```
GET /api/v1/databases HTTP/1.1
Host: localhost:8080
Authorization: Bearer {token}
Accept: application/json
```

#### レスポンス例 (成功: 200 OK)

```json
{
  "data": [
    {
      "databaseName": "postgres",
      "description": "PostgreSQLデータベース"
    },
    {
      "databaseName": "mysql",
      "description": "MySQLデータベース"
    }
  ]
}
```

#### エラーレスポンス例 (400 Bad Request)

```json
{
  "error": {
    "status": 400,
    "code": "DATABASE_CONNECTION_ERROR",
    "message": "データベースに接続できません",
    "timestamp": "2024-01-01T00:00:00Z"
  }
}
```

### 出力データ

| 項目         | 型     | 説明               |
|--------------|--------|--------------------|
| databaseName | String | データベース名     |
| description  | String | データベースの説明 |

### 制約

- データベースメタデータの取得はキャッシュする（NFR-DB-001）
- キャッシュの有効期間は5分以内（NFR-DB-002）
- 接続できないデータベースが存在する場合、エラーレスポンスを返す（NFR-DB-003）

---

## UC-02 スキーマ一覧を取得する

### 概要

指定されたデータベースのスキーマ一覧を取得する。

### アクター

- 主要アクター: 開発者

### 目標

開発者が指定されたデータベースのスキーマ一覧を確認できること。

### 前提条件

- システムが起動していること
- データベース接続設定が登録されていること
- 対象データベースが指定されていること

### トリガー

開発者がスキーマ一覧の取得をリクエストする

### 基本フロー

1. 開発者がデータベース名を指定してスキーマ一覧の取得をリクエストする
2. システムが指定されたデータベースのスキーマ一覧を取得する
3. システムがスキーマ一覧を表示する
4. ユースケースが完了する

### 代替フロー

A1 接続できないデータベースの場合

- システムがエラーレスポンスを返す
- ユースケースが完了する

### 後件条件

- スキーマ一覧が取得される

### API仕様

| 項目           | 値                                         |
|----------------|--------------------------------------------|
| HTTPメソッド   | GET                                        |
| エンドポイント | `/api/v1/databases/{databaseName}/schemas` |
| 認証           | 必要                                       |
| パスパラメータ | databaseName (String, 必須)                |

#### リクエスト例

```
GET /api/v1/databases/postgres/schemas HTTP/1.1
Host: localhost:8080
Authorization: Bearer {token}
Accept: application/json
```

#### レスポンス例 (成功: 200 OK)

```json
{
  "data": [
    {
      "schemaName": "public",
      "schemaKanaName": "パブリック",
      "description": "デフォルトスキーマ"
    },
    {
      "schemaName": "app",
      "schemaKanaName": "アプリケーション",
      "description": "アプリケーション用スキーマ"
    }
  ]
}
```

#### エラーレスポンス例 (404 Not Found)

```json
{
  "error": {
    "status": 404,
    "code": "DATABASE_NOT_FOUND",
    "message": "指定されたデータベースが見つかりません",
    "timestamp": "2024-01-01T00:00:00Z"
  }
}
```

### 入力パラメータ

| パラメータ   | 型     | 必須 | 説明           |
|--------------|--------|------|----------------|
| databaseName | String | 必須 | データベース名 |

### 出力データ

| 項目           | 型     | 説明                    |
|----------------|--------|-------------------------|
| schemaName     | String | スキーマ名              |
| schemaKanaName | String | スキーマの説明(";"まで) |
| description    | String | スキーマの説明(";"以降) |

### 制約

- データベースメタデータの取得はキャッシュする（NFR-DB-001）
- キャッシュの有効期間は5分以内（NFR-DB-002）

---

## UC-03 テーブル・View一覧を取得する

### 概要

指定されたスキーマのテーブル一覧とView一覧を取得する。

### アクター

- 主要アクター: 開発者

### 目標

開発者が指定されたスキーマのテーブル一覧とView一覧を確認できること。

### 前提条件

- システムが起動していること
- データベース接続設定が登録されていること
- 対象データベースとスキーマが指定されていること

### トリガー

開発者がテーブル・View一覧の取得をリクエストする

### 基本フロー

1. 開発者がデータベース名とスキーマ名を指定してテーブル・View一覧の取得をリクエストする
2. システムが指定されたスキーマのテーブル一覧とView一覧を取得する
3. システムがテーブル一覧とView一覧を表示する
4. ユースケースが完了する

### 代替フロー

A1 接続できないデータベースの場合

- システムがエラーレスポンスを返す
- ユースケースが完了する

### 後件条件

- テーブル一覧とView一覧が取得される

### API仕様

| 項目           | 値                                                             |
|----------------|----------------------------------------------------------------|
| HTTPメソッド   | GET                                                            |
| エンドポイント | `/api/v1/databases/{databaseName}/schemas/{schemaName}/tables` |
| 認証           | 必要                                                           |
| パスパラメータ | databaseName (String, 必須), schemaName (String, 必須)         |

#### リクエスト例

```
GET /api/v1/databases/postgres/schemas/public/tables HTTP/1.1
Host: localhost:8080
Authorization: Bearer {token}
Accept: application/json
```

#### レスポンス例 (成功: 200 OK)

```json
{
  "tables": [
    {
      "tableName": "users",
      "tableKanaName": "ユーザ",
      "tableType": "TABLE",
      "description": "ユーザ情報テーブル"
    }
  ],
  "views": [
    {
      "viewName": "v_user_summary",
      "viewKanaName": "ユーザサマリー",
      "viewType": "VIEW",
      "description": "ユーザサマリーView"
    }
  ]
}
```

### 入力パラメータ

| パラメータ   | 型     | 必須 | 説明           |
|--------------|--------|------|----------------|
| databaseName | String | 必須 | データベース名 |
| schemaName   | String | 必須 | スキーマ名     |

### 出力データ

#### テーブル一覧

| 項目          | 型     | 説明                                           |
|---------------|--------|------------------------------------------------|
| tableName     | String | テーブル名                                     |
| tableKanaName | String | テーブルの説明(最初の";"まで)                  |
| tableType     | String | テーブルの説明(一番目の";"から二番目の";"まで) |
| description   | String | テーブルの説明(二番目の";"から最後まで)        |

#### View一覧

| 項目         | 型     | 説明                                       |
|--------------|--------|--------------------------------------------|
| viewName     | String | View名                                     |
| viewKanaName | String | Viewの説明(最初の";"まで)                  |
| viewType     | String | Viewの説明(一番目の";"から二番目の";"まで) |
| description  | String | Viewの説明(二番目の";"から最後まで)        |

### 制約

- データベースメタデータの取得はキャッシュする（NFR-DB-001）
- キャッシュの有効期間は5分以内（NFR-DB-002）

---

## UC-04 テーブル定義を取得する

### 概要

指定されたテーブルの定義とインデックス定義を取得する。

### アクター

- 主要アクター: 開発者

### 目標

開発者が指定されたテーブルの定義とインデックス定義を確認できること。

### 前提条件

- システムが起動していること
- データベース接続設定が登録されていること
- 対象データベース、スキーマ、テーブルが指定されていること

### トリガー

開発者がテーブル定義の取得をリクエストする

### 基本フロー

1. 開発者がデータベース名、スキーマ名、テーブル名を指定してテーブル定義の取得をリクエストする
2. システムが指定されたテーブルの定義とインデックス定義を取得する
3. システムがテーブル定義とインデックス定義を表示する
4. ユースケースが完了する

### 代替フロー

A1 接続できないデータベースの場合

- システムがエラーレスポンスを返す
- ユースケースが完了する

### 後件条件

- テーブル定義とインデックス定義が取得される

### API仕様

| 項目           | 値                                                                               |
|----------------|----------------------------------------------------------------------------------|
| HTTPメソッド   | GET                                                                              |
| エンドポイント | `/api/v1/databases/{databaseName}/schemas/{schemaName}/tables/{tableName}`       |
| 認証           | 必要                                                                             |
| パスパラメータ | databaseName (String, 必須), schemaName (String, 必須), tableName (String, 必須) |

#### リクエスト例

```
GET /api/v1/databases/postgres/schemas/public/tables/users HTTP/1.1
Host: localhost:8080
Authorization: Bearer {token}
Accept: application/json
```

#### レスポンス例 (成功: 200 OK)

```json
{
  "tableDefinition": {
    "columns": [
      {
        "columnName": "id",
        "dataType": "BIGINT",
        "nullable": false,
        "primaryKey": true,
        "defaultValue": null,
        "comment": "主キー"
      },
      {
        "columnName": "name",
        "dataType": "VARCHAR(100)",
        "nullable": false,
        "primaryKey": false,
        "defaultValue": null,
        "comment": "名前"
      }
    ]
  },
  "indexDefinition": [
    {
      "indexName": "idx_users_name",
      "indexType": "NONUNIQUE",
      "columnName": "name",
      "ordinalPosition": 1
    }
  ]
}
```

### 入力パラメータ

| パラメータ   | 型     | 必須 | 説明           |
|--------------|--------|------|----------------|
| databaseName | String | 必須 | データベース名 |
| schemaName   | String | 必須 | スキーマ名     |
| tableName    | String | 必須 | テーブル名     |

### 出力データ

#### テーブル定義

| 項目         | 型      | 説明           |
|--------------|---------|----------------|
| columnName   | String  | カラム名       |
| dataType     | String  | データ型       |
| nullable     | Boolean | NULL許否       |
| primaryKey   | Boolean | 主キーフラグ   |
| defaultValue | String  | デフォルト値   |
| comment      | String  | カラムコメント |

#### インデックス定義

| 項目            | 型      | 説明                                  |
|-----------------|---------|---------------------------------------|
| indexName       | String  | インデックス名                        |
| indexType       | String  | インデックス種別 (UNIQUE / NONUNIQUE) |
| columnName      | String  | 構成カラム名                          |
| ordinalPosition | Integer | 構成カラムの順序                      |

### 制約

- データベースメタデータの取得はキャッシュする（NFR-DB-001）
- キャッシュの有効期間は5分以内（NFR-DB-002）

---

## UC-05 View定義を取得する

### 概要

指定されたViewの定義とインデックス定義を取得する。マテリアライズドViewの場合はインデックス定義も取得する。

### アクター

- 主要アクター: 開発者

### 目標

開発者が指定されたViewの定義とインデックス定義を確認できること。

### 前提条件

- システムが起動していること
- データベース接続設定が登録されていること
- 対象データベース、スキーマ、Viewが指定されていること

### トリガー

開発者がView定義の取得をリクエストする

### 基本フロー

1. 開発者がデータベース名、スキーマ名、View名を指定してView定義の取得をリクエストする
2. システムが指定されたViewの定義を取得する
3. システムがViewがマテリアライズドViewの場合、インデックス定義も取得する
4. システムがView定義（およびインデックス定義）を表示する
5. ユースケースが完了する

### 代替フロー

A1 接続できないデータベースの場合

- システムがエラーレスポンスを返す
- ユースケースが完了する

### 後件条件

- View定義（およびインデックス定義）が取得される

### API仕様

| 項目           | 値                                                                              |
|----------------|---------------------------------------------------------------------------------|
| HTTPメソッド   | GET                                                                             |
| エンドポイント | `/api/v1/databases/{databaseName}/schemas/{schemaName}/views/{viewName}`        |
| 認証           | 必要                                                                            |
| パスパラメータ | databaseName (String, 必須), schemaName (String, 必須), viewName (String, 必須) |

#### リクエスト例

```
GET /api/v1/databases/postgres/schemas/public/views/v_user_summary HTTP/1.1
Host: localhost:8080
Authorization: Bearer {token}
Accept: application/json
```

#### レスポンス例 (成功: 200 OK)

```json
{
  "viewDefinition": {
    "columns": [
      {
        "columnName": "id",
        "dataType": "BIGINT",
        "ordinalPosition": 1,
        "comment": "主キー"
      },
      {
        "columnName": "name",
        "dataType": "VARCHAR(100)",
        "ordinalPosition": 2,
        "comment": "名前"
      }
    ]
  },
  "indexDefinition": [
    {
      "indexName": "idx_v_user_summary_id",
      "indexType": "UNIQUE",
      "columnName": "id",
      "ordinalPosition": 1
    }
  ]
}
```

> **注**: マテリアライズドViewでない場合は、`indexDefinition` は `null` または空配列を返す。

### 入力パラメータ

| パラメータ   | 型     | 必須 | 説明           |
|--------------|--------|------|----------------|
| databaseName | String | 必須 | データベース名 |
| schemaName   | String | 必須 | スキーマ名     |
| viewName     | String | 必須 | View名         |

### 出力データ

#### View定義

| 項目            | 型      | 説明           |
|-----------------|---------|----------------|
| columnName      | String  | カラム名       |
| dataType        | String  | データ型       |
| ordinalPosition | Integer | カラムの順序   |
| comment         | String  | カラムコメント |

#### インデックス定義

データベースがpostgresでmaterialized viewを使用している場合、インデックス定義を表示する。

| 項目            | 型      | 説明                                  |
|-----------------|---------|---------------------------------------|
| indexName       | String  | インデックス名                        |
| indexType       | String  | インデックス種別 (UNIQUE / NONUNIQUE) |
| columnName      | String  | 構成カラム名                          |
| ordinalPosition | Integer | 構成カラムの順序                      |

### 制約

- データベースメタデータの取得はキャッシュする（NFR-DB-001）
- キャッシュの有効期間は5分以内（NFR-DB-002）
- PostgreSQLの通常ViewおよびマテリアライズドViewの両方を対象とする

---

## エラーレスポンス共通仕様

すべてのAPIエンドポイントで共通して使用するエラーレスポンス形式を定義します。

### エラーレスポンス形式

```json
{
  "error": {
    "status": 400,
    "code": "ERROR_CODE",
    "message": "エラーメッセージ",
    "details": [
      {
        "field": "fieldName",
        "message": "フィールドエラーメッセージ"
      }
    ],
    "timestamp": "2024-01-01T00:00:00Z"
  }
}
```

### エラーコード一覧

| エラーコード              | HTTPステータス | 説明                                 |
|---------------------------|----------------|--------------------------------------|
| DATABASE_CONNECTION_ERROR | 400            | データベース接続に失敗               |
| DATABASE_NOT_FOUND        | 404            | 指定されたデータベースが見つからない |
| SCHEMA_NOT_FOUND          | 404            | 指定されたスキーマが見つからない     |
| TABLE_NOT_FOUND           | 404            | 指定されたテーブルが見つからない     |
| VIEW_NOT_FOUND            | 404            | 指定されたViewが見つからない         |
| INVALID_PARAMETER         | 400            | 無効なパラメータ                     |
| INTERNAL_SERVER_ERROR     | 500            | サーバー内部エラー                   |

---

## 非機能要件

| 要件ID     | 要件名                 | 説明                                                           |
|------------|------------------------|----------------------------------------------------------------|
| NFR-DB-001 | メタデータキャッシュ   | データベースメタデータの取得はキャッシュする                   |
| NFR-DB-002 | キャッシュ有効期間     | キャッシュの有効期間は5分以内                                  |
| NFR-DB-003 | 接続エラーハンドリング | 接続できないデータベースが存在する場合、エラーレスポンスを返す |
