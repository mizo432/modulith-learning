# AG-04 スプリント実行 API設計

## 1. 概要

本ドキュメントは、AG-04「スプリント実行とインクリメント形成」業務領域のREST API設計を定義します。

スプリント実行は、チームがスプリント目標を共有し、作業計画を日々適応させながら、完成の定義を満たすインクリメントを形成する機能です。

## 2. 技術スタック

| カテゴリ | テクノロジー |
|:---|:---|
| **フレームワーク** | Spring Boot 3.x |
| **API仕様** | OpenAPI 3.0 |
| **ドキュメント** | springdoc-openapi |
| **認証** | JWT (JSON Web Token) |
| **バリデーション** | Jakarta Validation |
| **ページネーション** | Spring Data Pagination |

## 3. 設計ドキュメント一覧

| No. | 設計ドキュメント | 説明 |
|-----|-----------------|------|
| 01 | [エンドポイント一覧](01.エンドポイント/エンドポイント一覧.md) | 全リソースのエンドポイント定義 |
| 02 | [OpenAPI仕様書](01.エンドポイント/OpenAPI仕様書.md) | OpenAPI 3.0形式の完全なAPI仕様 |
| 03 | [エラーハンドリング設計](02.エラーハンドリング/エラーハンドリング設計.md) | 統一されたエラーレスポンス設計 |
| 04 | [セキュリティ設計](03.セキュリティ/セキュリティ設計.md) | 認証・認可設計 |

## 4. APIベースURL

```
/api/v1
```

## 5. リソース一覧

AG-04スプリント実行は以下の7つのリソースを提供します。

| リソース | エンドポイントプレフィックス | 説明 | 情報番号 |
|---------|---------------------------|------|---------|
| Sprints | `/sprints` | スプリントの期間・目標情報 | INF-SP-01 |
| Tasks | `/tasks` | 作業単位の情報 | - |
| Impediments | `/impediments` | スプリント中の障害 | INF-SP-04 |
| Dependencies | `/dependencies` | 項目間の依存関係 | INF-SP-05 |
| Notifications | `/notifications` | スプリント中の通知 | INF-SP-06 |
| Activities | `/activities` | スプリント中のアクティビティ | INF-SP-07 |
| Daily Scrums | `/daily-scrums` | デイリースクラムの記録 | - |

## 6. エンドポイント概要

### 6.1 スプリント (Sprints)

| HTTPメソッド | パス | 説明 | ユースケース |
|-------------|------|------|-------------|
| GET | `/sprints` | スプリント一覧を取得 | UC-SP-003 |
| POST | `/sprints` | スプリントを作成 | UC-SP-001 |
| GET | `/sprints/{id}` | スプリント詳細を取得 | UC-SP-003 |
| PUT | `/sprints/{id}` | スプリントを更新 | UC-SP-002 |
| PATCH | `/sprints/{id}/status` | スプリント状態を変更 | UC-SP-002 |
| GET | `/sprints/{id}/stats` | スプリント統計情報を取得 | UC-SP-014 |

### 6.2 タスク (Tasks)

| HTTPメソッド | パス | 説明 | ユースケース |
|-------------|------|------|-------------|
| GET | `/tasks` | タスク一覧を取得 | UC-SP-014 |
| POST | `/tasks` | タスクを作成 | UC-SP-006 |
| GET | `/tasks/{id}` | タスク詳細を取得 | UC-SP-014 |
| PUT | `/tasks/{id}` | タスクを更新 | UC-SP-007 |
| PATCH | `/tasks/{id}/status` | タスク状態を変更 | UC-SP-008 |
| DELETE | `/tasks/{id}` | タスクを削除 | UC-SP-007 |
| GET | `/sprints/{sprintId}/tasks` | スプリントのタスク一覧を取得 | UC-SP-014 |

### 6.3 障害 (Impediments)

| HTTPメソッド | パス | 説明 | ユースケース |
|-------------|------|------|-------------|
| GET | `/impediments` | 障害一覧を取得 | UC-SP-011 |
| POST | `/impediments` | 障害を登録 | UC-SP-009 |
| GET | `/impediments/{id}` | 障害詳細を取得 | UC-SP-011 |
| PUT | `/impediments/{id}` | 障害を更新 | UC-SP-010 |
| PATCH | `/impediments/{id}/status` | 障害状態を変更 | UC-SP-010 |
| GET | `/sprints/{sprintId}/impediments` | スプリントの障害一覧を取得 | UC-SP-011 |

### 6.4 依存関係 (Dependencies)

| HTTPメソッド | パス | 説明 | ユースケース |
|-------------|------|------|-------------|
| GET | `/dependencies` | 依存関係一覧を取得 | UC-SP-012 |
| POST | `/dependencies` | 依存関係を定義 | UC-SP-012 |
| GET | `/dependencies/{id}` | 依存関係詳細を取得 | UC-SP-012 |
| PUT | `/dependencies/{id}` | 依存関係を更新 | UC-SP-012 |
| DELETE | `/dependencies/{id}` | 依存関係を削除 | UC-SP-012 |

### 6.5 通知 (Notifications)

| HTTPメソッド | パス | 説明 | ユースケース |
|-------------|------|------|-------------|
| GET | `/notifications` | 通知一覧を取得 | UC-SP-013 |
| POST | `/notifications` | 通知を送信 | UC-SP-013 |
| GET | `/notifications/{id}` | 通知詳細を取得 | UC-SP-013 |
| PATCH | `/notifications/{id}/read` | 通知を読み済みにする | UC-SP-013 |
| PATCH | `/notifications/read-all` | 全通知を読み済みにする | UC-SP-013 |

### 6.6 アクティビティ (Activities)

| HTTPメソッド | パス | 説明 | ユースケース |
|-------------|------|------|-------------|
| GET | `/activities` | アクティビティ一覧を取得 | UC-SP-014 |
| GET | `/sprints/{sprintId}/activities` | スプリントのアクティビティ一覧を取得 | UC-SP-014 |

### 6.7 デイリースクラム (Daily Scrums)

| HTTPメソッド | パス | 説明 | ユースケース |
|-------------|------|------|-------------|
| GET | `/daily-scrums` | デイリースクラム一覧を取得 | UC-SP-016 |
| POST | `/daily-scrums` | デイリースクラムを記録 | UC-SP-016 |
| GET | `/daily-scrums/{id}` | デイリースクラム詳細を取得 | UC-SP-016 |
| PUT | `/daily-scrums/{id}` | デイリースクラムを更新 | UC-SP-016 |
| DELETE | `/daily-scrums/{id}` | デイリースクラムを削除 | UC-SP-016 |
| GET | `/sprints/{sprintId}/daily-scrums` | スプリントのスクラム一覧を取得 | UC-SP-016 |

## 7. 共通設計

### 7.1 リクエスト・レスポンス形式

- **リクエストボディ**: JSON形式 (`application/json`)
- **レスポンスボディ**: JSON形式 (`application/json`)
- **文字エンコーディング**: UTF-8

### 7.2 ページネーション

| パラメータ | 型 | 説明 | デフォルト | 最大値 |
|-----------|-----|------|-----------|--------|
| `pageNumber` | integer | ページ番号 (0起点) | 0 | - |
| `pageSize` | integer | ページサイズ | 20 | 100 |
| `sort` | string | ソート条件 | `createdAt,DESC` | - |

### 7.3 レスポンス形式

```json
{
  "data": [...],
  "metadata": {
    "pageNumber": 0,
    "pageSize": 20,
    "totalElements": 100,
    "totalPages": 5
  }
}
```

### 7.4 エラーレスポンス形式

```json
{
  "error": {
    "status": 400,
    "code": "VALIDATION_ERROR",
    "message": "リクエストの検証に失敗しました",
    "details": [
      {
        "field": "title",
        "message": "タイトルは必須です"
      }
    ],
    "timestamp": "2024-01-01T00:00:00Z"
  }
}
```

## 8. 認証・認可

- **認証方式**: JWT (Bearer Token)
- **認可方式**: RBAC (Role-Based Access Control)
- **ヘッダー**: `Authorization: Bearer <token>`

### 8.1 役割一覧

| 役割 | 説明 |
|-----|------|
| PRODUCT_OWNER | プロダクトオーナー |
| SCUM_MASTER | スクラムマスター |
| DEVELOPER | 開発者 |
| VIEWER | 閲覧者 |

## 9. バリデーション

- **Jakarta Validation**: `@NotNull`, `@Size`, `@Email` など
- **カスタムバリデーター**: ビジネスロジックに依存するバリデーション

## 10. 参照

- 要件定義: `docs/22 要件定義書/AG アジルーム/03 機能要件/AG-04 スプリント実行/`
- データベース設計: `docs/23 詳細設計書/AG_アジルーム/01 データベース設計/`
- フロントエンド設計: `docs/23 詳細設計書/AG_アジルーム/02 フロントエンド設計/`
