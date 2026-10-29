# AS-PERSON-ROLE ユースケース記述

## 概要

AS関連付け事業領域のPersonRole（従業員）管理APIユースケース記述を定義します。
本ドキュメントは、RDRAモデルで定義されたユースケースを外部設計レベルで詳細化したものです。

## 参照

- RDRAユースケース一覧: [AS-PERSON-ROLE ユースケース一覧](../../22%20要件定義書/AS%20関連付け/03%20機能要件/AS-PERSON-ROLE%20人ロール/03.システム境界/ユースケース一覧.tsv)
- API設計ガイドライン: `.agents/skills/api-design/SKILL.md`
- API外部設計: [backend/api/index.md](../index.md)

---

## UC-AS-PERSON-001 従業員を登録する

### 概要

新しい従業員を登録する（PartyとPersonRoleを自動作成）。

### アクター

- 主要アクター: 組織管理者 (AC-AS-01)

### 目標

組織管理者が新しい従業員を登録できること。登録時にPartyとPersonRoleが自動作成される。

### 前提条件

- システムが起動していること
- ユーザーが認証されていること
- ユーザーにAS:WRITE権限があること

### トリガー

組織管理者が従業員の登録をリクエストする

### 基本フロー

1. 組織管理者が従業員の登録をリクエストする
2. システムがリクエストデータをバリデーションする
3. システムがPartyを自動作成する
4. システムがPersonRoleを自動作成する
5. システムが従業員情報を登録する
6. システムが登録結果を返す
7. ユースケースが完了する

### 代替フロー

A1 重複したコードが存在する場合

- システムがエラーレスポンスを返す (409 Conflict)
- ユースケースが完了する

### 後件条件

- Party、PersonRole、従業員が登録される

### API仕様

| 項目             | 値                           |
|------------------|------------------------------|
| HTTPメソッド     | POST                         |
| エンドポイント   | `/api/as/v1/employees`       |
| 認証             | 必要                         |
| 権限             | AS:WRITE                     |
| リクエストボディ | あり                         |

#### リクエスト例

```json
POST /api/as/v1/employees HTTP/1.1
Host: localhost:8080
Authorization: Bearer {token}
Content-Type: application/json
Accept: application/json

{
  "employeeCode": "EMP001",
  "lastName": "山田",
  "firstName": "太郎",
  "lastNameKana": "ヤマダ",
  "firstNameKana": "タロウ",
  "email": "yamada.taro@example.com",
  "phone": "090-1234-5678",
  "hireDate": "2024-01-01",
  "affiliations": [
    {
      "organizationId": 1,
      "position": "エンジニア",
      "isPrimary": true
    }
  ]
}
```

#### レスポンス例 (成功: 201 Created)

```json
{
  "data": {
    "employeeId": 1,
    "partyId": 1,
    "employeeCode": "EMP001",
    "lastName": "山田",
    "firstName": "太郎",
    "email": "yamada.taro@example.com",
    "hireDate": "2024-01-01",
    "status": "ACTIVE",
    "createdAt": "2024-01-01T00:00:00Z"
  }
}
```

---

## UC-AS-PERSON-002 従業員を更新する

### 概要

従業員の基本情報を更新する。

### アクター

- 主要アクター: 組織管理者 (AC-AS-01)

### 目標

組織管理者が従業員の基本情報を更新できること。

### 前提条件

- システムが起動していること
- ユーザーが認証されていること
- ユーザーにAS:WRITE権限があること
- 対象従業員が存在すること

### トリガー

組織管理者が従業員の更新をリクエストする

### 基本フロー

1. 組織管理者が従業員の更新をリクエストする
2. システムがリクエストデータをバリデーションする
3. システムが従業員情報を更新する
4. システムが更新結果を返す
5. ユースケースが完了する

### 後件条件

- 従業員情報が更新される

### API仕様

| 項目             | 値                              |
|------------------|---------------------------------|
| HTTPメソッド     | PUT                             |
| エンドポイント   | `/api/as/v1/employees/{employeeId}` |
| 認証             | 必要                            |
| 権限             | AS:WRITE                        |
| リクエストボディ | あり                            |

#### リクエスト例

```json
PUT /api/as/v1/employees/1 HTTP/1.1
Host: localhost:8080
Authorization: Bearer {token}
Content-Type: application/json
Accept: application/json

{
  "email": "yamada.taro.new@example.com",
  "phone": "090-9876-5432"
}
```

#### レスポンス例 (成功: 200 OK)

```json
{
  "data": {
    "employeeId": 1,
    "partyId": 1,
    "employeeCode": "EMP001",
    "lastName": "山田",
    "firstName": "太郎",
    "email": "yamada.taro.new@example.com",
    "phone": "090-9876-5432",
    "updatedAt": "2024-01-02T00:00:00Z"
  }
}
```

---

## UC-AS-PERSON-003 従業員を無効化する

### 概要

従業員を退社処理する。

### アクター

- 主要アクター: 組織管理者 (AC-AS-01)

### 目標

組織管理者が従業員を退社処理できること。

### 前提条件

- システムが起動していること
- ユーザーが認証されていること
- ユーザーにAS:DELETE権限があること
- 対象従業員が存在すること

### トリガー

組織管理者が従業員の退社処理をリクエストする

### 基本フロー

1. 組織管理者が従業員の退社処理をリクエストする
2. システムが従業員を退社処理する
3. システムが退社処理結果を返す
4. ユースケースが完了する

### 後件条件

- 従業員が退社処理される

### API仕様

| 項目             | 値                              |
|------------------|---------------------------------|
| HTTPメソッド     | DELETE                          |
| エンドポイント   | `/api/as/v1/employees/{employeeId}` |
| 認証             | 必要                            |
| 権限             | AS:DELETE                       |

#### レスポンス例 (成功: 204 No Content)

```
HTTP/1.1 204 No Content
```

---

## UC-AS-PERSON-004 従業員を検索する

### 概要

条件に合致する従業員を検索する。

### アクター

- 主要アクター: 組織管理者 (AC-AS-01)、JBジョブ管理 (AC-AS-03)、AGアジルーム (AC-AS-04)

### 目標

アクターが条件に合致する従業員を検索できること。

### 前提条件

- システムが起動していること
- ユーザーが認証されていること
- ユーザーにAS:READ権限があること

### トリガー

アクターが従業員の検索をリクエストする

### 基本フロー

1. アクターが従業員の検索をリクエストする
2. システムが検索条件に合致する従業員を検索する
3. システムが検索結果を返す
4. ユースケースが完了する

### 後件条件

- 検索結果が返される

### API仕様

| 項目             | 値                                |
|------------------|-----------------------------------|
| HTTPメソッド     | GET                               |
| エンドポイント   | `/api/as/v1/employees/search`     |
| 認証             | 必要                              |
| 権限             | AS:READ                           |
| クエリパラメータ | あり                              |

#### クエリパラメータ

| パラメータ       | 型     | 必須 | 説明             |
|-----------------|--------|------|------------------|
| lastName        | String | いいえ | 姓（部分一致）     |
| firstName       | String | いいえ | 名（部分一致）     |
| employeeCode    | String | いいえ | 従業員コード（完全一致） |
| organizationId  | Long   | いいえ | 所属組織ID         |

#### リクエスト例

```
GET /api/as/v1/employees/search?lastName=山田&organizationId=1 HTTP/1.1
Host: localhost:8080
Authorization: Bearer {token}
Accept: application/json
```

#### レスポンス例 (成功: 200 OK)

```json
{
  "data": [
    {
      "employeeId": 1,
      "employeeCode": "EMP001",
      "lastName": "山田",
      "firstName": "太郎",
      "organizationName": "開発部"
    }
  ],
  "metadata": {
    "totalElements": 1
  }
}
```

---

## UC-AS-PERSON-005 従業員一覧を取得する

### 概要

従業員の一覧をページネーションで取得する。

### アクター

- 主要アクター: 組織管理者 (AC-AS-01)、JBジョブ管理 (AC-AS-03)、AGアジルーム (AC-AS-04)

### 目標

アクターが従業員の一覧をページネーションで取得できること。

### 前提条件

- システムが起動していること
- ユーザーが認証されていること
- ユーザーにAS:READ権限があること

### トリガー

アクターが従業員一覧の取得をリクエストする

### 基本フロー

1. アクターが従業員一覧の取得をリクエストする
2. システムが従業員一覧をページネーションで取得する
3. システムが従業員一覧を返す
4. ユースケースが完了する

### 後件条件

- 従業員一覧が返される

### API仕様

| 項目             | 値                           |
|------------------|------------------------------|
| HTTPメソッド     | GET                          |
| エンドポイント   | `/api/as/v1/employees`       |
| 認証             | 必要                         |
| 権限             | AS:READ                      |
| クエリパラメータ | あり                         |

#### クエリパラメータ

| パラメータ   | 型     | 必須 | 説明             |
|-------------|--------|------|------------------|
| pageSize    | Integer| いいえ | ページサイズ（デフォルト: 20） |
| pageNumber  | Integer| いいえ | ページ番号（デフォルト: 0）   |
| sort        | String | いいえ | ソート条件       |

#### レスポンス例 (成功: 200 OK)

```json
{
  "data": [
    {
      "employeeId": 1,
      "employeeCode": "EMP001",
      "lastName": "山田",
      "firstName": "太郎",
      "status": "ACTIVE"
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

## UC-AS-PERSON-006 従業員の詳細を取得する

### 概要

指定した従業員の詳細情報を取得する（所属情報を含む）。

### アクター

- 主要アクター: 組織管理者 (AC-AS-01)、JBジョブ管理 (AC-AS-03)、AGアジルーム (AC-AS-04)

### 目標

アクターが指定した従業員の詳細情報を取得できること。

### 前提条件

- システムが起動していること
- ユーザーが認証されていること
- ユーザーにAS:READ権限があること
- 対象従業員が存在すること

### トリガー

アクターが従業員詳細の取得をリクエストする

### 基本フロー

1. アクターが従業員詳細の取得をリクエストする
2. システムが従業員の詳細情報を取得する
3. システムが従業員詳細を返す
4. ユースケースが完了する

### 後件条件

- 従業員詳細が返される

### API仕様

| 項目             | 値                              |
|------------------|---------------------------------|
| HTTPメソッド     | GET                             |
| エンドポイント   | `/api/as/v1/employees/{employeeId}` |
| 認証             | 必要                            |
| 権限             | AS:READ                         |

#### レスポンス例 (成功: 200 OK)

```json
{
  "data": {
    "employeeId": 1,
    "partyId": 1,
    "employeeCode": "EMP001",
    "lastName": "山田",
    "firstName": "太郎",
    "lastNameKana": "ヤマダ",
    "firstNameKana": "タロウ",
    "email": "yamada.taro@example.com",
    "phone": "090-1234-5678",
    "hireDate": "2024-01-01",
    "status": "ACTIVE",
    "affiliations": [
      {
        "organizationId": 1,
        "organizationName": "開発部",
        "position": "エンジニア",
        "isPrimary": true,
        "validFrom": "2024-01-01T00:00:00Z"
      }
    ],
    "createdAt": "2024-01-01T00:00:00Z",
    "updatedAt": "2024-01-01T00:00:00Z"
  }
}
```

---

## UC-AS-PERSON-007 所属組織を変更する

### 概要

従業員の所属組織・部署を変更する。

### アクター

- 主要アクター: 組織管理者 (AC-AS-01)

### 目標

組織管理者が従業員の所属組織を変更できること。

### 前提条件

- システムが起動していること
- ユーザーが認証されていること
- ユーザーにAS:WRITE権限があること
- 対象従業員が存在すること
- 対象組織が存在すること

### トリガー

組織管理者が所属組織変更をリクエストする

### 基本フロー

1. 組織管理者が所属組織変更をリクエストする
2. システムが所属組織を変更する
3. システムが変更結果を返す
4. ユースケースが完了する

### 後件条件

- 所属組織が変更される

### API仕様

| 項目             | 値                                        |
|------------------|-------------------------------------------|
| HTTPメソッド     | PUT                                       |
| エンドポイント   | `/api/as/v1/employees/{employeeId}/affiliation` |
| 認証             | 必要                                      |
| 権限             | AS:WRITE                                  |
| リクエストボディ | あり                                      |

#### リクエスト例

```json
PUT /api/as/v1/employees/1/affiliation HTTP/1.1
Host: localhost:8080
Authorization: Bearer {token}
Content-Type: application/json
Accept: application/json

{
  "organizationId": 2,
  "position": "マネージャー",
  "isPrimary": true
}
```

#### レスポンス例 (成功: 200 OK)

```json
{
  "data": {
    "employeeId": 1,
    "organizationId": 2,
    "organizationName": "マネージメント部",
    "position": "マネージャー",
    "isPrimary": true,
    "validFrom": "2024-01-02T00:00:00Z"
  }
}
```

---

## UC-AS-PERSON-008 部署変更履歴を取得する

### 概要

従業員の部署変更履歴を取得する。

### アクター

- 主要アクター: 組織管理者 (AC-AS-01)、システム運用者 (AC-AS-02)

### 目標

アクターが従業員の部署変更履歴を取得できること。

### 前提条件

- システムが起動していること
- ユーザーが認証されていること
- ユーザーにAS:READ権限があること

### トリガー

アクターが部署変更履歴の取得をリクエストする

### 基本フロー

1. アクターが部署変更履歴の取得をリクエストする
2. システムが部署変更履歴を取得する
3. システムが変更履歴を返す
4. ユースケースが完了する

### 後件条件

- 変更履歴が返される

### API仕様

| 項目             | 値                                        |
|------------------|-------------------------------------------|
| HTTPメソッド     | GET                                       |
| エンドポイント   | `/api/as/v1/employees/{employeeId}/affiliation/history` |
| 認証             | 必要                                      |
| 権限             | AS:READ                                   |
| クエリパラメータ | あり                                      |

#### クエリパラメータ

| パラメータ   | 型     | 必須 | 説明             |
|-------------|--------|------|------------------|
| pageSize    | Integer| いいえ | ページサイズ（デフォルト: 20） |
| pageNumber  | Integer| いいえ | ページ番号（デフォルト: 0）   |

#### レスポンス例 (成功: 200 OK)

```json
{
  "data": [
    {
      "historyId": 1,
      "employeeId": 1,
      "organizationId": 1,
      "organizationName": "開発部",
      "position": "エンジニア",
      "changedAt": "2024-01-01T00:00:00Z",
      "changedBy": "admin"
    },
    {
      "historyId": 2,
      "employeeId": 1,
      "organizationId": 2,
      "organizationName": "マネージメント部",
      "position": "マネージャー",
      "changedAt": "2024-01-02T00:00:00Z",
      "changedBy": "admin"
    }
  ],
  "metadata": {
    "pageSize": 10,
    "pageNumber": 0,
    "totalElements": 2,
    "totalPages": 1
  }
}
```

---

## 参照

- [AS-PERSON-ROLE ユースケース一覧](../../22%20要件定義書/AS%20関連付け/03%20機能要件/AS-PERSON-ROLE%20人ロール/03.システム境界/ユースケース一覧.tsv)
- [API外部設計](../index.md)
- [データベース設計](../../01%20データベース設計/index.md)
