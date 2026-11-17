# AS-PARTY ユースケース記述

## 概要

AS関連付け事業領域のParty管理APIユースケース記述を定義します。
本ドキュメントは、RDRAモデルで定義されたユースケースを外部設計レベルで詳細化したものです。

## 参照

- RDRAユースケース一覧: [AS-PARTY ユースケース一覧](../../22%20要件定義書/AS%20関連付け/03%20機能要件/AS-PARTY%20パーティー/03.システム境界/ユースケース一覧.tsv)
- API設計ガイドライン: `.agents/skills/api-design/SKILL.md`
- API外部設計: [backend/api/index.md](../index.md)

---

## UC-AS-PARTY-001 パーティーを登録する

### 概要

新しい実体(Party)を登録する。

### アクター

- 主要アクター: 組織管理者 (AC-AS-01)

### 目標

組織管理者が新しいPartyを登録できること。

### 前提条件

- システムが起動していること
- ユーザーが認証されていること
- ユーザーにAS:WRITE権限があること

### トリガー

組織管理者がPartyの登録をリクエストする

### 基本フロー

1. 組織管理者がPartyの登録をリクエストする
2. システムがリクエストデータをバリデーションする
3. システムがPartyを登録する
4. システムが登録結果を返す
5. ユースケースが完了する

### 代替フロー

A1 重複したコードが存在する場合

- システムがエラーレスポンスを返す (409 Conflict)
- ユースケースが完了する

A2 バリデーションエラーの場合

- システムがエラーレスポンスを返す (400 Bad Request)
- ユースケースが完了する

### 後件条件

- Partyが登録される

### API仕様

| 項目             | 値                          |
|------------------|-----------------------------|
| HTTPメソッド     | POST                        |
| エンドポイント   | `/api/as/v1/parties`        |
| 認証             | 必要                        |
| 権限             | AS:WRITE                    |
| リクエストボディ | あり                        |

#### リクエスト例

```json
POST /api/as/v1/parties HTTP/1.1
Host: localhost:8080
Authorization: Bearer {token}
Content-Type: application/json
Accept: application/json

{
  "partyTypeCode": "PERSON",
  "name": "山田 太郎",
  "code": "EMP001"
}
```

#### レスポンス例 (成功: 201 Created)

```json
{
  "data": {
    "partyId": 1,
    "partyTypeCode": "PERSON",
    "name": "山田 太郎",
    "code": "EMP001",
    "validFrom": "2024-01-01T00:00:00Z",
    "validTo": null,
    "createdAt": "2024-01-01T00:00:00Z"
  }
}
```

#### エラーレスポンス例 (409 Conflict)

```json
{
  "error": {
    "status": 409,
    "code": "PARTY_ALREADY_EXISTS",
    "message": "指定されたコードは既に存在します",
    "timestamp": "2024-01-01T00:00:00Z"
  }
}
```

---

## UC-AS-PARTY-002 パーティーを更新する

### 概要

パーティーの基本情報を更新する。

### アクター

- 主要アクター: 組織管理者 (AC-AS-01)

### 目標

組織管理者がPartyの基本情報を更新できること。

### 前提条件

- システムが起動していること
- ユーザーが認証されていること
- ユーザーにAS:WRITE権限があること
- 対象Partyが存在すること

### トリガー

組織管理者がPartyの更新をリクエストする

### 基本フロー

1. 組織管理者がPartyの更新をリクエストする
2. システムがリクエストデータをバリデーションする
3. システムがPartyを更新する
4. システムが更新結果を返す
5. ユースケースが完了する

### 代替フロー

A1 対象Partyが存在しない場合

- システムがエラーレスポンスを返す (404 Not Found)
- ユースケースが完了する

### 後件条件

- Partyが更新される

### API仕様

| 項目             | 値                          |
|------------------|-----------------------------|
| HTTPメソッド     | PUT                         |
| エンドポイント   | `/api/as/v1/parties/{partyId}` |
| 認証             | 必要                        |
| 権限             | AS:WRITE                    |
| リクエストボディ | あり                        |

#### リクエスト例

```json
PUT /api/as/v1/parties/1 HTTP/1.1
Host: localhost:8080
Authorization: Bearer {token}
Content-Type: application/json
Accept: application/json

{
  "name": "山田 次郎",
  "code": "EMP001"
}
```

#### レスポンス例 (成功: 200 OK)

```json
{
  "data": {
    "partyId": 1,
    "partyTypeCode": "PERSON",
    "name": "山田 次郎",
    "code": "EMP001",
    "validFrom": "2024-01-01T00:00:00Z",
    "validTo": null,
    "updatedAt": "2024-01-02T00:00:00Z"
  }
}
```

---

## UC-AS-PARTY-003 パーティーを無効化する

### 概要

パーティーを無効化する。

### アクター

- 主要アクター: 組織管理者 (AC-AS-01)

### 目標

組織管理者がPartyを無効化できること。

### 前提条件

- システムが起動していること
- ユーザーが認証されていること
- ユーザーにAS:DELETE権限があること
- 対象Partyが存在すること

### トリガー

組織管理者がPartyの無効化をリクエストする

### 基本フロー

1. 組織管理者がPartyの無効化をリクエストする
2. システムがPartyを無効化する
3. システムが無効化結果を返す
4. ユースケースが完了する

### 後件条件

- Partyが無効化される

### API仕様

| 項目             | 値                          |
|------------------|-----------------------------|
| HTTPメソッド     | DELETE                      |
| エンドポイント   | `/api/as/v1/parties/{partyId}` |
| 認証             | 必要                        |
| 権限             | AS:DELETE                   |

#### レスポンス例 (成功: 204 No Content)

```
HTTP/1.1 204 No Content
```

---

## UC-AS-PARTY-004 パーティーを検索する

### 概要

条件に合致するパーティーを検索する。

### アクター

- 主要アクター: 組織管理者 (AC-AS-01)、JBジョブ管理 (AC-AS-03)、AGアジルーム (AC-AS-04)

### 目標

アクターが条件に合致するPartyを検索できること。

### 前提条件

- システムが起動していること
- ユーザーが認証されていること
- ユーザーにAS:READ権限があること

### トリガー

アクターがPartyの検索をリクエストする

### 基本フロー

1. アクターがPartyの検索をリクエストする
2. システムが検索条件に合致するPartyを検索する
3. システムが検索結果を返す
4. ユースケースが完了する

### 後件条件

- 検索結果が返される

### API仕様

| 項目             | 値                          |
|------------------|-----------------------------|
| HTTPメソッド     | GET                         |
| エンドポイント   | `/api/as/v1/parties/search` |
| 認証             | 必要                        |
| 権限             | AS:READ                     |
| クエリパラメータ | あり                        |

#### クエリパラメータ

| パラメータ       | 型     | 必須 | 説明             |
|-----------------|--------|------|------------------|
| partyTypeCode   | String | いいえ | パーティー種類コード |
| name            | String | いいえ | 名称（部分一致）   |
| code            | String | いいえ | コード（完全一致） |

#### リクエスト例

```
GET /api/as/v1/parties/search?partyTypeCode=PERSON&name=山田 HTTP/1.1
Host: localhost:8080
Authorization: Bearer {token}
Accept: application/json
```

#### レスポンス例 (成功: 200 OK)

```json
{
  "data": [
    {
      "partyId": 1,
      "partyTypeCode": "PERSON",
      "name": "山田 太郎",
      "code": "EMP001"
    },
    {
      "partyId": 2,
      "partyTypeCode": "PERSON",
      "name": "山田 次郎",
      "code": "EMP002"
    }
  ],
  "metadata": {
    "totalElements": 2
  }
}
```

---

## UC-AS-PARTY-005 パーティー一覧を取得する

### 概要

パーティーの一覧をページネーションで取得する。

### アクター

- 主要アクター: 組織管理者 (AC-AS-01)、JBジョブ管理 (AC-AS-03)、AGアジルーム (AC-AS-04)

### 目標

アクターがPartyの一覧をページネーションで取得できること。

### 前提条件

- システムが起動していること
- ユーザーが認証されていること
- ユーザーにAS:READ権限があること

### トリガー

アクターがParty一覧の取得をリクエストする

### 基本フロー

1. アクターがParty一覧の取得をリクエストする
2. システムがParty一覧をページネーションで取得する
3. システムがParty一覧を返す
4. ユースケースが完了する

### 後件条件

- Party一覧が返される

### API仕様

| 項目             | 値                        |
|------------------|---------------------------|
| HTTPメソッド     | GET                       |
| エンドポイント   | `/api/as/v1/parties`      |
| 認証             | 必要                      |
| 権限             | AS:READ                   |
| クエリパラメータ | あり                      |

#### クエリパラメータ

| パラメータ   | 型     | 必須 | 説明             |
|-------------|--------|------|------------------|
| pageSize    | Integer| いいえ | ページサイズ（デフォルト: 20） |
| pageNumber  | Integer| いいえ | ページ番号（デフォルト: 0）   |
| sort        | String | いいえ | ソート条件       |

#### リクエスト例

```
GET /api/as/v1/parties?pageSize=10&pageNumber=0&sort=name,ASC HTTP/1.1
Host: localhost:8080
Authorization: Bearer {token}
Accept: application/json
```

#### レスポンス例 (成功: 200 OK)

```json
{
  "data": [
    {
      "partyId": 1,
      "partyTypeCode": "PERSON",
      "name": "山田 太郎",
      "code": "EMP001"
    }
  ],
  "metadata": {
    "pageSize": 10,
    "pageNumber": 0,
    "totalElements": 100,
    "totalPages": 10
  }
}
```

---

## UC-AS-PARTY-006 パーティーの詳細を取得する

### 概要

指定したパーティーの詳細情報を取得する。

### アクター

- 主要アクター: 組織管理者 (AC-AS-01)、JBジョブ管理 (AC-AS-03)、AGアジルーム (AC-AS-04)

### 目標

アクターが指定したPartyの詳細情報を取得できること。

### 前提条件

- システムが起動していること
- ユーザーが認証されていること
- ユーザーにAS:READ権限があること
- 対象Partyが存在すること

### トリガー

アクターがParty詳細の取得をリクエストする

### 基本フロー

1. アクターがParty詳細の取得をリクエストする
2. システムがPartyの詳細情報を取得する
3. システムがParty詳細を返す
4. ユースケースが完了する

### 後件条件

- Party詳細が返される

### API仕様

| 項目             | 値                          |
|------------------|-----------------------------|
| HTTPメソッド     | GET                         |
| エンドポイント   | `/api/as/v1/parties/{partyId}` |
| 認証             | 必要                        |
| 権限             | AS:READ                     |

#### リクエスト例

```
GET /api/as/v1/parties/1 HTTP/1.1
Host: localhost:8080
Authorization: Bearer {token}
Accept: application/json
```

#### レスポンス例 (成功: 200 OK)

```json
{
  "data": {
    "partyId": 1,
    "partyTypeCode": "PERSON",
    "name": "山田 太郎",
    "code": "EMP001",
    "validFrom": "2024-01-01T00:00:00Z",
    "validTo": null,
    "createdAt": "2024-01-01T00:00:00Z",
    "updatedAt": "2024-01-01T00:00:00Z",
    "roles": [
      {
        "roleId": 1,
        "roleCode": "EMPLOYEE",
        "roleName": "従業員",
        "validFrom": "2024-01-01T00:00:00Z",
        "validTo": null
      }
    ]
  }
}
```

---

## UC-AS-PARTY-007 ロールを付与する

### 概要

パーティーにロールを付与する。

### アクター

- 主要アクター: 組織管理者 (AC-AS-01)

### 目標

組織管理者がPartyにロールを付与できること。

### 前提条件

- システムが起動していること
- ユーザーが認証されていること
- ユーザーにAS:WRITE権限があること
- 対象Partyが存在すること
- 対象ロールが存在すること

### トリガー

組織管理者がロール付与をリクエストする

### 基本フロー

1. 組織管理者がロール付与をリクエストする
2. システムがロールを付与する
3. システムが付与結果を返す
4. ユースケースが完了する

### 後件条件

- ロールが付与される

### API仕様

| 項目             | 値                                      |
|------------------|-----------------------------------------|
| HTTPメソッド     | POST                                    |
| エンドポイント   | `/api/as/v1/parties/{partyId}/roles`    |
| 認証             | 必要                                    |
| 権限             | AS:WRITE                                |
| リクエストボディ | あり                                    |

#### リクエスト例

```json
POST /api/as/v1/parties/1/roles HTTP/1.1
Host: localhost:8080
Authorization: Bearer {token}
Content-Type: application/json
Accept: application/json

{
  "roleId": 1,
  "validFrom": "2024-01-01T00:00:00Z"
}
```

#### レスポンス例 (成功: 201 Created)

```json
{
  "data": {
    "partyId": 1,
    "roleId": 1,
    "roleCode": "EMPLOYEE",
    "roleName": "従業員",
    "validFrom": "2024-01-01T00:00:00Z",
    "validTo": null
  }
}
```

---

## UC-AS-PARTY-008 ロールを解除する

### 概要

パーティーからロールを解除する。

### アクター

- 主要アクター: 組織管理者 (AC-AS-01)

### 目標

組織管理者がPartyからロールを解除できること。

### 前提条件

- システムが起動していること
- ユーザーが認証されていること
- ユーザーにAS:WRITE権限があること
- 対象Partyが存在すること
- 対象ロールが付与されていること

### トリガー

組織管理者がロール解除をリクエストする

### 基本フロー

1. 組織管理者がロール解除をリクエストする
2. システムがロールを解除する
3. システムが解除結果を返す
4. ユースケースが完了する

### 後件条件

- ロールが解除される

### API仕様

| 項目             | 値                                              |
|------------------|-------------------------------------------------|
| HTTPメソッド     | DELETE                                          |
| エンドポイント   | `/api/as/v1/parties/{partyId}/roles/{roleId}`   |
| 認証             | 必要                                            |
| 権限             | AS:WRITE                                        |

#### レスポンス例 (成功: 204 No Content)

```
HTTP/1.1 204 No Content
```

---

## 参照

- [AS-PARTY ユースケース一覧](../../22%20要件定義書/AS%20関連付け/03%20機能要件/AS-PARTY%20パーティー/03.システム境界/ユースケース一覧.tsv)
- [API外部設計](../index.md)
- [データベース設計](../../01%20データベース設計/index.md)
