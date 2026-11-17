# API設計

JBジョブ管理のREST APIエンドポイントを定義します。

## 1. API一覧

| カテゴリ | エンドポイント | HTTPメソッド | 説明 | ユースケース |
|---------|---------------|-------------|------|-------------|
| ジョブ定義 | /api/v1/job-definitions | GET | ジョブ定義一覧を取得 | UC-JB-004 |
| ジョブ定義 | /api/v1/job-definitions | POST | ジョブ定義を登録 | UC-JB-001 |
| ジョブ定義 | /api/v1/job-definitions/{jobCode} | GET | ジョブ定義を取得 | UC-JB-004 |
| ジョブ定義 | /api/v1/job-definitions/{jobCode} | PUT | ジョブ定義を変更 | UC-JB-002 |
| ジョブ定義 | /api/v1/job-definitions/{jobCode}/disable | POST | ジョブ定義を無効化 | UC-JB-003 |
| 配備 | /api/v1/deployments | GET | 配備情報一覧を取得 | UC-JB-004 |
| 配備 | /api/v1/deployments | POST | 配備情報を登録 | UC-JB-005 |
| 配備 | /api/v1/deployments/{deploymentId} | PUT | 配備情報を更新 | UC-JB-006 |
| スケジュール | /api/v1/schedules | GET | スケジュール一覧を取得 | UC-JB-010 |
| スケジュール | /api/v1/schedules | POST | スケジュールを登録 | UC-JB-011 |
| スケジュール | /api/v1/schedules/{scheduleId} | PUT | スケジュールを変更 | UC-JB-012 |
| ジョブ実行 | /api/v1/job-executions | GET | ジョブ実行状況一覧を取得 | UC-JB-013 |
| ジョブ実行 | /api/v1/job-executions | POST | ジョブを手動起動 | UC-JB-008 |
| ジョブ実行 | /api/v1/job-executions/{executionId} | GET | ジョブ実行詳細を取得 | UC-JB-014 |
| ジョブ実行 | /api/v1/job-executions/{executionId}/cancel | POST | ジョブをキャンセル | UC-JB-018 |
| ジョブ実行 | /api/v1/job-executions/{executionId}/re-execute | POST | ジョブを再実行 | UC-JB-019 |
| ジョブ実行 | /api/v1/job-executions/{executionId}/resume | POST | ジョブを途中再開 | UC-JB-020 |
| ジョブ実行 | /api/v1/job-executions/{executionId}/logs | GET | 実行ログを取得 | UC-JB-015 |
| ジョブ実行 | /api/v1/job-executions/history | GET | 実行履歴を検索 | UC-JB-016 |
| API起動 | /api/v1/external/job-starts | POST | APIからジョブ起動を要求 | UC-JB-009 |
| API照会 | /api/v1/external/job-statuses/{executionId} | GET | APIから実行状態を照会 | UC-JB-017 |
| 通知 | /api/v1/notification-settings | GET | 通知設定一覧を取得 | UC-JB-023 |
| 通知 | /api/v1/notification-settings | POST | 通知設定を登録 | UC-JB-022 |
| 通知 | /api/v1/notification-settings/{settingId} | PUT | 通知設定を変更 | UC-JB-022 |
| 通知 | /api/v1/notification-settings/{settingId} | DELETE | 通知設定を削除 | UC-JB-022 |
| ユーザー | /api/v1/users | GET | ユーザー一覧を取得 | UC-JB-007 |
| ユーザー | /api/v1/users | POST | ユーザーを登録 | UC-JB-007 |
| ユーザー | /api/v1/users/{userId} | PUT | ユーザー権限を変更 | UC-JB-007 |
| 会社 | /api/v1/companies | GET | 会社一覧を取得 | UC-JB-027 |
| 会社 | /api/v1/companies | POST | 会社を登録 | UC-JB-027 |
| 会社 | /api/v1/companies/{companyCode} | PUT | 会社を変更 | UC-JB-027 |
| 店舗 | /api/v1/stores | GET | 店舗一覧を取得 | UC-JB-027 |
| 店舗 | /api/v1/stores | POST | 店舗を登録 | UC-JB-027 |
| 店舗 | /api/v1/stores/{storeCode} | PUT | 店舗を変更 | UC-JB-027 |
| 監査ログ | /api/v1/audit-logs | GET | 監査ログを検索 | UC-JB-024 |
| 復旧履歴 | /api/v1/recovery-histories | GET | 復旧操作履歴を取得 | UC-JB-021 |
| ダッシュボード | /api/v1/dashboard/summary | GET | ダッシュボード概要を取得 | UC-JB-013 |

## 2. API詳細定義

### 2.1 ジョブ定義関連API

#### ジョブ定義一覧を取得

| 項目 | 値 |
|------|-----|
| エンドポイント | GET /api/v1/job-definitions |
| 説明 | ジョブ定義の一覧を取得 |
| 認証 | 必要 |

**クエリパラメータ**

| パラメータ | 型 | 必須 | 説明 |
|-----------|-----|------|------|
| enabled | Boolean | N | 有効フラグでフィルタ |
| page | Integer | N | ページ番号 (デフォルト: 0) |
| size | Integer | N | ページサイズ (デフォルト: 20) |

**レスポンス (200 OK)**

```json
{
  "content": [
    {
      "jobCode": "JOB001",
      "jobName": "日次集計ジョブ",
      "description": "日次データを集計するジョブ",
      "version": "1.0.0",
      "enabled": true,
      "createdAt": "2025-01-01T00:00:00",
      "updatedAt": "2025-01-01T00:00:00"
    }
  ],
  "page": 0,
  "size": 20,
  "totalElements": 1,
  "totalPages": 1
}
```

---

#### ジョブ定義を登録

| 項目 | 値 |
|------|-----|
| エンドポイント | POST /api/v1/job-definitions |
| 説明 | ジョブ定義を新規登録 |
| 認証 | 必要 |

**リクエストボディ**

```json
{
  "jobCode": "JOB001",
  "jobName": "日次集計ジョブ",
  "description": "日次データを集計するジョブ",
  "parameterSpec": {
    "parameters": [
      {
        "name": "date",
        "type": "DATE",
        "required": true,
        "defaultValue": null,
        "description": "対象日付"
      }
    ]
  },
  "executionConstraint": {
    "maxConcurrentExecutions": 1,
    "timeoutSeconds": 3600,
    "retryCount": 0
  },
  "version": "1.0.0"
}
```

**レスポンス (201 Created)**

```json
{
  "jobCode": "JOB001",
  "jobName": "日次集計ジョブ",
  "description": "日次データを集計するジョブ",
  "version": "1.0.0",
  "enabled": true,
  "createdAt": "2025-01-01T00:00:00",
  "updatedAt": "2025-01-01T00:00:00"
}
```

---

#### ジョブ定義を取得

| 項目 | 値 |
|------|-----|
| エンドポイント | GET /api/v1/job-definitions/{jobCode} |
| 説明 | ジョブ定義の詳細を取得 |
| 認証 | 必要 |

**レスポンス (200 OK)**

```json
{
  "jobCode": "JOB001",
  "jobName": "日次集計ジョブ",
  "description": "日次データを集計するジョブ",
  "parameterSpec": {
    "parameters": [
      {
        "name": "date",
        "type": "DATE",
        "required": true,
        "defaultValue": null,
        "description": "対象日付"
      }
    ]
  },
  "executionConstraint": {
    "maxConcurrentExecutions": 1,
    "timeoutSeconds": 3600,
    "retryCount": 0
  },
  "version": "1.0.0",
  "enabled": true,
  "createdAt": "2025-01-01T00:00:00",
  "updatedAt": "2025-01-01T00:00:00"
}
```

---

#### ジョブ定義を変更

| 項目 | 値 |
|------|-----|
| エンドポイント | PUT /api/v1/job-definitions/{jobCode} |
| 説明 | ジョブ定義を変更 |
| 認証 | 必要 |

**リクエストボディ**

```json
{
  "jobName": "日次集計ジョブ（変更）",
  "description": "日次データを集計するジョブ（変更）",
  "parameterSpec": {
    "parameters": [
      {
        "name": "date",
        "type": "DATE",
        "required": true,
        "defaultValue": null,
        "description": "対象日付"
      }
    ]
  },
  "executionConstraint": {
    "maxConcurrentExecutions": 2,
    "timeoutSeconds": 7200,
    "retryCount": 1
  },
  "version": "1.1.0"
}
```

**レスポンス (200 OK)**

```json
{
  "jobCode": "JOB001",
  "jobName": "日次集計ジョブ（変更）",
  "description": "日次データを集計するジョブ（変更）",
  "version": "1.1.0",
  "enabled": true,
  "createdAt": "2025-01-01T00:00:00",
  "updatedAt": "2025-01-02T00:00:00"
}
```

---

#### ジョブ定義を無効化

| 項目 | 値 |
|------|-----|
| エンドポイント | POST /api/v1/job-definitions/{jobCode}/disable |
| 説明 | ジョブ定義を無効化 |
| 認証 | 必要 |

**レスポンス (200 OK)**

```json
{
  "jobCode": "JOB001",
  "enabled": false,
  "updatedAt": "2025-01-02T00:00:00"
}
```

---

### 2.2 配備関連API

#### 配備情報一覧を取得

| 項目 | 値 |
|------|-----|
| エンドポイント | GET /api/v1/deployments |
| 説明 | 配備情報の一覧を取得 |
| 認証 | 必要 |

**クエリパラメータ**

| パラメータ | 型 | 必須 | 説明 |
|-----------|-----|------|------|
| jobCode | String | N | ジョブコードでフィルタ |

**レスポンス (200 OK)**

```json
[
  {
    "deploymentId": "550e8400-e29b-41d4-a716-446655440000",
    "jobCode": "JOB001",
    "serverAddress": {
      "host": "app-server-a.example.com",
      "port": 8080,
      "protocol": "https"
    },
    "deploymentVersion": "1.0.0",
    "status": "ACTIVE",
    "deployedAt": "2025-01-01T00:00:00"
  }
]
```

---

#### 配備情報を登録

| 項目 | 値 |
|------|-----|
| エンドポイント | POST /api/v1/deployments |
| 説明 | 配備情報を登録 |
| 認証 | 必要 |

**リクエストボディ**

```json
{
  "jobCode": "JOB001",
  "serverAddress": {
    "host": "app-server-a.example.com",
    "port": 8080,
    "protocol": "https"
  },
  "deploymentVersion": "1.0.0"
}
```

**レスポンス (201 Created)**

```json
{
  "deploymentId": "550e8400-e29b-41d4-a716-446655440000",
  "jobCode": "JOB001",
  "serverAddress": {
    "host": "app-server-a.example.com",
    "port": 8080,
    "protocol": "https"
  },
  "deploymentVersion": "1.0.0",
  "status": "ACTIVE",
  "deployedAt": "2025-01-01T00:00:00"
}
```

---

### 2.3 スケジュール関連API

#### スケジュール一覧を取得

| 項目 | 値 |
|------|-----|
| エンドポイント | GET /api/v1/schedules |
| 説明 | スケジュールの一覧を取得 |
| 認証 | 必要 |

**クエリパラメータ**

| パラメータ | 型 | 必須 | 説明 |
|-----------|-----|------|------|
| jobCode | String | N | ジョブコードでフィルタ |
| enabled | Boolean | N | 有効フラグでフィルタ |

**レスポンス (200 OK)**

```json
[
  {
    "scheduleId": "550e8400-e29b-41d4-a716-446655440001",
    "jobCode": "JOB001",
    "repetitionCondition": {
      "cronExpression": "0 0 2 * * ?",
      "timezone": "Asia/Tokyo"
    },
    "validPeriod": {
      "startDate": "2025-01-01",
      "endDate": null
    },
    "enabled": true
  }
]
```

---

#### スケジュールを登録

| 項目 | 値 |
|------|-----|
| エンドポイント | POST /api/v1/schedules |
| 説明 | スケジュールを登録 |
| 認証 | 必要 |

**リクエストボディ**

```json
{
  "jobCode": "JOB001",
  "repetitionCondition": {
    "cronExpression": "0 0 2 * * ?",
    "timezone": "Asia/Tokyo"
  },
  "validPeriod": {
    "startDate": "2025-01-01",
    "endDate": null
  }
}
```

**レスポンス (201 Created)**

```json
{
  "scheduleId": "550e8400-e29b-41d4-a716-446655440001",
  "jobCode": "JOB001",
  "repetitionCondition": {
    "cronExpression": "0 0 2 * * ?",
    "timezone": "Asia/Tokyo"
  },
  "validPeriod": {
    "startDate": "2025-01-01",
    "endDate": null
  },
  "enabled": true
}
```

---

### 2.4 ジョブ実行関連API

#### ジョブ実行状況一覧を取得

| 項目 | 値 |
|------|-----|
| エンドポイント | GET /api/v1/job-executions |
| 説明 | ジョブ実行状況の一覧を取得 |
| 認証 | 必要 |

**クエリパラメータ**

| パラメータ | 型 | 必須 | 説明 |
|-----------|-----|------|------|
| state | String | N | 実行状態でフィルタ |
| jobCode | String | N | ジョブコードでフィルタ |
| page | Integer | N | ページ番号 |
| size | Integer | N | ページサイズ |

**レスポンス (200 OK)**

```json
{
  "content": [
    {
      "executionId": "550e8400-e29b-41d4-a716-446655440002",
      "jobCode": "JOB001",
      "executionState": "RUNNING",
      "startedAt": "2025-01-01T02:00:00",
      "finishedAt": null
    }
  ],
  "page": 0,
  "size": 20,
  "totalElements": 1,
  "totalPages": 1
}
```

---

#### ジョブを手動起動

| 項目 | 値 |
|------|-----|
| エンドポイント | POST /api/v1/job-executions |
| 説明 | ジョブを手動で起動 |
| 認証 | 必要 |

**リクエストボディ**

```json
{
  "jobCode": "JOB001",
  "executionParameter": {
    "parameters": {
      "date": "2025-01-01"
    }
  }
}
```

**レスポンス (201 Created)**

```json
{
  "executionId": "550e8400-e29b-41d4-a716-446655440002",
  "jobCode": "JOB001",
  "executionState": "PENDING",
  "startedAt": null,
  "finishedAt": null
}
```

---

#### ジョブ実行詳細を取得

| 項目 | 値 |
|------|-----|
| エンドポイント | GET /api/v1/job-executions/{executionId} |
| 説明 | ジョブ実行の詳細を取得 |
| 認証 | 必要 |

**レスポンス (200 OK)**

```json
{
  "executionId": "550e8400-e29b-41d4-a716-446655440002",
  "jobCode": "JOB001",
  "jobVersion": "1.0.0",
  "serverAddress": {
    "host": "app-server-a.example.com",
    "port": 8080,
    "protocol": "https"
  },
  "executionState": "SUCCESS",
  "executionParameter": {
    "parameters": {
      "date": "2025-01-01"
    }
  },
  "processedCount": 1000,
  "startedAt": "2025-01-01T02:00:00",
  "finishedAt": "2025-01-01T02:05:00"
}
```

---

#### ジョブをキャンセル

| 項目 | 値 |
|------|-----|
| エンドポイント | POST /api/v1/job-executions/{executionId}/cancel |
| 説明 | 実行中のジョブをキャンセル |
| 認証 | 必要 |

**リクエストボディ**

```json
{
  "reason": "手動キャンセル"
}
```

**レスポンス (200 OK)**

```json
{
  "executionId": "550e8400-e29b-41d4-a716-446655440002",
  "executionState": "CANCELLED"
}
```

---

#### ジョブを再実行

| 項目 | 値 |
|------|-----|
| エンドポイント | POST /api/v1/job-executions/{executionId}/re-execute |
| 説明 | 失敗したジョブを最初から再実行 |
| 認証 | 必要 |

**リクエストボディ**

```json
{
  "reason": "データ再取得のため再実行"
}
```

**レスポンス (201 Created)**

```json
{
  "executionId": "550e8400-e29b-41d4-a716-446655440003",
  "jobCode": "JOB001",
  "executionState": "PENDING"
}
```

---

#### ジョブを途中再開

| 項目 | 値 |
|------|-----|
| エンドポイント | POST /api/v1/job-executions/{executionId}/resume |
| 説明 | チェックポイントからジョブを再開 |
| 認証 | 必要 |

**リクエストボディ**

```json
{
  "reason": "チェックポイントから再開"
}
```

**レスポンス (201 Created)**

```json
{
  "executionId": "550e8400-e29b-41d4-a716-446655440004",
  "jobCode": "JOB001",
  "executionState": "PENDING"
}
```

---

#### 実行ログを取得

| 項目 | 値 |
|------|-----|
| エンドポイント | GET /api/v1/job-executions/{executionId}/logs |
| 説明 | ジョブ実行のログを取得 |
| 認証 | 必要 |

**レスポンス (200 OK)**

```json
{
  "executionId": "550e8400-e29b-41d4-a716-446655440002",
  "logs": [
    {
      "timestamp": "2025-01-01T02:00:00",
      "level": "INFO",
      "message": "ジョブ実行を開始"
    },
    {
      "timestamp": "2025-01-01T02:05:00",
      "level": "INFO",
      "message": "ジョブ実行を完了"
    }
  ]
}
```

---

#### 実行履歴を検索

| 項目 | 値 |
|------|-----|
| エンドポイント | GET /api/v1/job-executions/history |
| 説明 | ジョブ実行の履歴を検索 |
| 認証 | 必要 |

**クエリパラメータ**

| パラメータ | 型 | 必須 | 説明 |
|-----------|-----|------|------|
| jobCode | String | N | ジョブコード |
| state | String | N | 実行状態 |
| companyCode | String | N | 会社コード |
| storeCode | String | N | 店舗コード |
| startDate | String | N | 開始日 |
| endDate | String | N | 終了日 |
| page | Integer | N | ページ番号 |
| size | Integer | N | ページサイズ |

**レスポンス (200 OK)**

```json
{
  "content": [
    {
      "executionId": "550e8400-e29b-41d4-a716-446655440002",
      "jobCode": "JOB001",
      "executionState": "SUCCESS",
      "startedAt": "2025-01-01T02:00:00",
      "finishedAt": "2025-01-01T02:05:00"
    }
  ],
  "page": 0,
  "size": 20,
  "totalElements": 1,
  "totalPages": 1
}
```

---

### 2.5 外部API（業務システム用）

#### APIからジョブ起動を要求

| 項目 | 値 |
|------|-----|
| エンドポイント | POST /api/v1/external/job-starts |
| 説明 | 業務システムからAPI経由でジョブ起動を要求 |
| 認証 | Bearer Token |

**リクエストボディ**

```json
{
  "jobCode": "JOB001",
  "executionId": "550e8400-e29b-41d4-a716-446655440005",
  "parameters": {
    "date": "2025-01-01"
  }
}
```

**レスポンス (201 Created)**

```json
{
  "executionId": "550e8400-e29b-41d4-a716-446655440005",
  "jobCode": "JOB001",
  "executionState": "PENDING"
}
```

---

#### APIから実行状態を照会

| 項目 | 値 |
|------|-----|
| エンドポイント | GET /api/v1/external/job-statuses/{executionId} |
| 説明 | 業務システムからAPI経由で実行状態を照会 |
| 認証 | Bearer Token |

**レスポンス (200 OK)**

```json
{
  "executionId": "550e8400-e29b-41d4-a716-446655440005",
  "jobCode": "JOB001",
  "executionState": "SUCCESS",
  "processedCount": 1000,
  "startedAt": "2025-01-01T02:00:00",
  "finishedAt": "2025-01-01T02:05:00"
}
```

---

### 2.6 通知関連API

#### 通知設定一覧を取得

| 項目 | 値 |
|------|-----|
| エンドポイント | GET /api/v1/notification-settings |
| 説明 | 通知設定の一覧を取得 |
| 認証 | 必要 |

**レスポンス (200 OK)**

```json
[
  {
    "notificationSettingId": "550e8400-e29b-41d4-a716-446655440006",
    "jobCode": "JOB001",
    "notificationCondition": {
      "onSuccess": false,
      "onFailure": true,
      "onUnknown": true
    },
    "notificationDestination": {
      "destination": "admin@example.com"
    },
    "notificationChannel": "EMAIL",
    "enabled": true
  }
]
```

---

#### 通知設定を登録

| 項目 | 値 |
|------|-----|
| エンドポイント | POST /api/v1/notification-settings |
| 説明 | 通知設定を登録 |
| 認証 | 必要 |

**リクエストボディ**

```json
{
  "jobCode": "JOB001",
  "notificationCondition": {
    "onSuccess": false,
    "onFailure": true,
    "onUnknown": true
  },
  "notificationDestination": {
    "destination": "admin@example.com"
  },
  "notificationChannel": "EMAIL"
}
```

**レスポンス (201 Created)**

```json
{
  "notificationSettingId": "550e8400-e29b-41d4-a716-446655440006",
  "jobCode": "JOB001",
  "notificationCondition": {
    "onSuccess": false,
    "onFailure": true,
    "onUnknown": true
  },
  "notificationDestination": {
    "destination": "admin@example.com"
  },
  "notificationChannel": "EMAIL",
  "enabled": true
}
```

---

### 2.7 ユーザー関連API

#### ユーザー一覧を取得

| 項目 | 値 |
|------|-----|
| エンドポイント | GET /api/v1/users |
| 説明 | ユーザーの一覧を取得 |
| 認証 | 必要（システム管理者） |

**レスポンス (200 OK)**

```json
[
  {
    "userId": "550e8400-e29b-41d4-a716-446655440007",
    "username": "admin",
    "role": "SYSTEM_ADMIN",
    "enabled": true
  }
]
```

---

#### ユーザーを登録

| 項目 | 値 |
|------|-----|
| エンドポイント | POST /api/v1/users |
| 説明 | ユーザーを登録 |
| 認証 | 必要（システム管理者） |

**リクエストボディ**

```json
{
  "username": "operator1",
  "role": "OPERATOR"
}
```

**レスポンス (201 Created)**

```json
{
  "userId": "550e8400-e29b-41d4-a716-446655440008",
  "username": "operator1",
  "role": "OPERATOR",
  "enabled": true
}
```

---

#### ユーザー権限を変更

| 項目 | 値 |
|------|-----|
| エンドポイント | PUT /api/v1/users/{userId} |
| 説明 | ユーザーの権限を変更 |
| 認証 | 必要（システム管理者） |

**リクエストボディ**

```json
{
  "role": "OPERATOR",
  "companyPermissions": [
    {
      "companyCode": "C001",
      "permissionLevel": "READ"
    }
  ],
  "storePermissions": [
    {
      "storeCode": "S001",
      "permissionLevel": "WRITE"
    }
  ]
}
```

**レスポンス (200 OK)**

```json
{
  "userId": "550e8400-e29b-41d4-a716-446655440008",
  "username": "operator1",
  "role": "OPERATOR",
  "companyPermissions": [
    {
      "companyCode": "C001",
      "permissionLevel": "READ"
    }
  ],
  "storePermissions": [
    {
      "storeCode": "S001",
      "permissionLevel": "WRITE"
    }
  ]
}
```

---

### 2.8 会社・店舗関連API

#### 会社一覧を取得

| 項目 | 値 |
|------|-----|
| エンドポイント | GET /api/v1/companies |
| 説明 | 会社の一覧を取得 |
| 認証 | 必要 |

**レスポンス (200 OK)**

```json
[
  {
    "companyCode": "C001",
    "companyName": "本社",
    "companyType": "HEADQUARTERS",
    "enabled": true
  }
]
```

---

#### 店舗一覧を取得

| 項目 | 値 |
|------|-----|
| エンドポイント | GET /api/v1/stores |
| 説明 | 店舗の一覧を取得 |
| 認証 | 必要 |

**クエリパラメータ**

| パラメータ | 型 | 必須 | 説明 |
|-----------|-----|------|------|
| companyCode | String | N | 会社コードでフィルタ |

**レスポンス (200 OK)**

```json
[
  {
    "storeCode": "S001",
    "storeName": "東京店",
    "storeType": "DIRECT",
    "companyCode": "C001",
    "enabled": true
  }
]
```

---

### 2.9 監査ログ関連API

#### 監査ログを検索

| 項目 | 値 |
|------|-----|
| エンドポイント | GET /api/v1/audit-logs |
| 説明 | 監査ログを検索 |
| 認証 | 必要（システム管理者） |

**クエリパラメータ**

| パラメータ | 型 | 必須 | 説明 |
|-----------|-----|------|------|
| operationType | String | N | 操作タイプでフィルタ |
| userId | String | N | 操作者でフィルタ |
| startDate | String | N | 開始日 |
| endDate | String | N | 終了日 |
| jobCode | String | N | ジョブコードでフィルタ |
| page | Integer | N | ページ番号 |
| size | Integer | N | ページサイズ |

**レスポンス (200 OK)**

```json
{
  "content": [
    {
      "auditLogId": "550e8400-e29b-41d4-a716-446655440009",
      "operationType": "MANUAL_START",
      "operator": {
        "userId": "550e8400-e29b-41d4-a716-446655440007",
        "username": "admin"
      },
      "operationDateTime": "2025-01-01T02:00:00",
      "operationReason": "手動起動",
      "targetJobCode": "JOB001",
      "targetExecutionId": "550e8400-e29b-41d4-a716-446655440002",
      "result": "SUCCESS"
    }
  ],
  "page": 0,
  "size": 20,
  "totalElements": 1,
  "totalPages": 1
}
```

---

### 2.10 ダッシュボード関連API

#### ダッシュボード概要を取得

| 項目 | 値 |
|------|-----|
| エンドポイント | GET /api/v1/dashboard/summary |
| 説明 | ダッシュボードの概要情報を取得 |
| 認証 | 必要 |

**レスポンス (200 OK)**

```json
{
  "runningCount": 5,
  "pendingCount": 3,
  "successCount": 50,
  "failureCount": 2,
  "unknownCount": 0,
  "recentExecutions": [
    {
      "executionId": "550e8400-e29b-41d4-a716-446655440002",
      "jobCode": "JOB001",
      "executionState": "RUNNING",
      "startedAt": "2025-01-01T02:00:00"
    }
  ]
}
```

---

### 2.11 復旧操作履歴関連API

#### 復旧操作履歴を取得

| 項目 | 値 |
|------|-----|
| エンドポイント | GET /api/v1/recovery-histories |
| 説明 | 復旧操作の履歴を取得 |
| 認証 | 必要 |

**クエリパラメータ**

| パラメータ | 型 | 必須 | 説明 |
|-----------|-----|------|------|
| jobCode | String | N | ジョブコードでフィルタ |
| page | Integer | N | ページ番号 |
| size | Integer | N | ページサイズ |

**レスポンス (200 OK)**

```json
{
  "content": [
    {
      "auditLogId": "550e8400-e29b-41d4-a716-446655440010",
      "operationType": "RE_EXECUTE",
      "operator": {
        "userId": "550e8400-e29b-41d4-a716-446655440007",
        "username": "admin"
      },
      "operationDateTime": "2025-01-01T03:00:00",
      "operationReason": "データ再取得のため再実行",
      "targetJobCode": "JOB001",
      "targetExecutionId": "550e8400-e29b-41d4-a716-446655440003"
    }
  ],
  "page": 0,
  "size": 20,
  "totalElements": 1,
  "totalPages": 1
}
```

## 3. エラーレスポンス

### 3.1 共通エラー形式

```json
{
  "errorCode": "JB001",
  "errorMessage": "ジョブ定義が見つかりません",
  "timestamp": "2025-01-01T02:00:00"
}
```

### 3.2 エラーコード一覧

| エラーコード | HTTPステータス | 説明 |
|-------------|---------------|------|
| JB001 | 404 | ジョブ定義が見つかりません |
| JB002 | 400 | 無効なリクエストパラメータ |
| JB003 | 401 | 認証が必要です |
| JB004 | 403 | 権限がありません |
| JB005 | 409 | ジョブ定義が既に存在します |
| JB006 | 409 | 実行IDが既に存在します |
| JB007 | 429 | 同時実行枠が不足しています |
| JB008 | 500 | 内部サーバーエラー |

## 4. API設計まとめ

| APIカテゴリ | エンドポイント数 |
|-------------|-----------------|
| ジョブ定義 | 5 |
| 配備 | 3 |
| スケジュール | 3 |
| ジョブ実行 | 8 |
| 外部API | 2 |
| 通知 | 4 |
| ユーザー | 3 |
| 会社・店舗 | 4 |
| 監査ログ | 1 |
| ダッシュボード | 1 |
| 復旧履歴 | 1 |
| **合計** | **35** |
