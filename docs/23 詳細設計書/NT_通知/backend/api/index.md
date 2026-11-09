# NT_通知 API詳細設計

## 概要

通知事業領域のAPI詳細設計資材を格納します。CQRS（Command Query Responsibility Segregation）に基づき、**Command Service**（書き込み）と**Query Service**（読み込み）を分離して設計します。

## 参照

- 事業領域一覧: `docs/11 事業領域/事業領域一覧.tsv`
- 業務領域一覧: `docs/12 業務領域/業務領域一覧.tsv`
- ユースケース一覧: `docs/22 要件定義書/NT 通知/03 機能要件/NT-01 通知管理/03.システム境界/ユースケース一覧.md`
- 情報一覧: `docs/22 要件定義書/NT 通知/03 機能要件/NT-01 通知管理/04.システム/01.情報/情報一覧.md`
- 画面属性一覧: `docs/22 要件定義書/NT 通知/03 機能要件/NT-01 通知管理/04.システム/04.画面/SCR-NT-001_画面属性一覧.md`

---

## UCからCommand/Queryの分類

| ユースケースID | ユースケース名 | Command/Query | 対応Service | HTTPメソッド |
|----------------|----------------|---------------|-------------|-------------|
| UC-NT-001 | 通知を送信する | Command | NotificationCommandService | POST |
| UC-NT-002 | 通知一覧を取得する | Query | NotificationQueryService | GET |
| UC-NT-003 | 未読通知一覧を取得する | Query | NotificationQueryService | GET |
| UC-NT-004 | 通知を既読にする | Command | NotificationCommandService | PATCH |

---

## APIエンドポイント一覧

### Query Service（読み込み）

| エンドポイント | HTTPメソッド | 説明 | 対応UC |
|---------------|-------------|------|--------|
| `/api/v1/notifications` | GET | 通知一覧を取得する | UC-NT-002 |
| `/api/v1/notifications?status=unread` | GET | 未読通知一覧を取得する | UC-NT-003 |

### Command Service（書き込み）

| エンドポイント | HTTPメソッド | 説明 | 対応UC |
|---------------|-------------|------|--------|
| `/api/v1/notifications` | POST | 通知を送信する | UC-NT-001 |
| `/api/v1/notifications/{id}/read` | PATCH | 通知を既読にする | UC-NT-004 |

---

## Command Service 設計

### NotificationCommandService

**責務**: 通知の作成・状態遷移を担当

#### 通知送信 (`CreateNotificationCommand`)

| 項目 | 値 |
|------|-----|
| エンドポイント | `POST /api/v1/notifications` |
| Controller | `NotificationCommandController` |
| Service | `NotificationCommandService.createNotification(CreateNotificationCommand)` |
| ステータスコード | 201 (Created) |

**Commandオブジェクト**:

```java
public final class CreateNotificationCommand {
    @NotNull private final String title;
    @NotNull @Size(max = 2000) private final String body;
    @NotNull private final NotificationType type;
    @NotNull private final String sourceDomain;
    @NotNull private final Long recipientId;
}
```

**レスポンス**:

```json
{
  "id": 1,
  "message": "通知が送信されました"
}
```

#### 既読マーク (`MarkAsReadCommand`)

| 項目 | 値 |
|------|-----|
| エンドポイント | `PATCH /api/v1/notifications/{id}/read` |
| Controller | `NotificationCommandController` |
| Service | `NotificationCommandService.markAsRead(MarkAsReadCommand)` |
| ステータスコード | 204 (No Content) |

**Commandオブジェクト**:

```java
public final class MarkAsReadCommand {
    @NotNull private final Long notificationId;
    @NotNull private final Long recipientId;
}
```

---

## Query Service 設計

### NotificationQueryService

**責務**: 通知の読み込み・検索を担当

#### 通知一覧取得 (`ListNotificationsQuery`)

| 項目 | 値 |
|------|-----|
| エンドポイント | `GET /api/v1/notifications` |
| Controller | `NotificationQueryController` |
| Service | `NotificationQueryService.listNotifications(ListNotificationsQuery)` |
| ステータスコード | 200 (OK) |

**Queryオブジェクト**:

```java
public final class ListNotificationsQuery {
    @NotNull private final Long recipientId;
    private final NotificationStatus status; // unread / all
    private final int page = 0;
    private final int size = 20;
}
```

**レスポンスDTO**:

```java
public final class NotificationDto {
    private final Long id;
    private final String title;
    private final String body;
    private final NotificationType type;
    private final String sourceDomain;
    private final boolean isRead;
    private final LocalDateTime createdAt;
}
```

**レスポンス**:

```json
{
  "data": [
    {
      "id": 1,
      "title": "メンション通知",
      "body": "あなたはメンションされました",
      "type": "MENTION",
      "sourceDomain": "AG",
      "isRead": false,
      "createdAt": "2024-01-15T10:30:00"
    }
  ],
  "metadata": {
    "pageSize": 20,
    "pageNumber": 0,
    "totalElements": 1,
    "totalPages": 1
  }
}
```

#### 未読通知一覧取得

| 項目 | 値 |
|------|-----|
| エンドポイント | `GET /api/v1/notifications?status=unread` |
| Controller | `NotificationQueryController` |
| Service | `NotificationQueryService.listNotifications(ListNotificationsQuery)` |
| ステータスコード | 200 (OK) |

**Queryオブジェクト**:

```java
public final class ListNotificationsQuery {
    @NotNull private final Long recipientId;
    @NotNull private final NotificationStatus status = NotificationStatus.UNREAD;
    private final int page = 0;
    private final int size = 20;
}
```

---

## エラーレスポンス

### 一貫したエラー形式

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

### エラーコード一覧

| エラーコード | HTTPステータス | 説明 |
|-------------|---------------|------|
| VALIDATION_ERROR | 400 | リクエストの検証エラー |
| NOTIFICATION_NOT_FOUND | 404 | 指定された通知が見つからない |
| NOTIFICATION_ALREADY_READ | 409 | 通知は既に既読です |
| INTERNAL_ERROR | 500 | システム内部エラー |

---

## バリデーションルール

| フィールド | 制約 | バリデーションアノテーション |
|-----------|------|---------------------------|
| title | 必須、最大200文字 | `@NotNull`, `@Size(max = 200)` |
| body | 必須、最大2000文字 | `@NotNull`, `@Size(max = 2000)` |
| type | 必須、列挙型 | `@NotNull` |
| sourceDomain | 必須、最大50文字 | `@NotNull`, `@Size(max = 50)` |
| recipientId | 必須 | `@NotNull` |
