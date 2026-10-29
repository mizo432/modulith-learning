# OpenAPI仕様書

AG-04スプリント実行のOpenAPI 3.0仕様書を定義します。

## 1. 概要

```yaml
openapi: 3.0.3
info:
  title: Agiloom Sprint Execution API
  description: AG-04 スプリント実行とインクリメント形成のREST API
  version: 1.0.0
  contact:
    name: Agiloom Team
    email: agiloom@example.com

servers:
  - url: https://api.agiloom.example.com/api/v1
    description: 本番サーバー
  - url: https://staging-api.agiloom.example.com/api/v1
    description: ステージングサーバー
  - url: http://localhost:8080/api/v1
    description: 開発サーバー
```

## 2. 共通スキーマ

### 2.1 ページネーションメタデータ

```yaml
components:
  schemas:
    PaginationMetadata:
      type: object
      properties:
        pageNumber:
          type: integer
          format: int32
          example: 0
        pageSize:
          type: integer
          format: int32
          example: 20
        totalElements:
          type: integer
          format: int64
          example: 100
        totalPages:
          type: integer
          format: int32
          example: 5
```

### 2.2 ページネーションレスポンス

```yaml
    PaginatedResponse:
      type: object
      properties:
        data:
          type: array
          items:
            $ref: '#/components/schemas/Entity'
        metadata:
          $ref: '#/components/schemas/PaginationMetadata'
```

### 2.3 エラーレスポンス

```yaml
    ErrorDetail:
      type: object
      properties:
        field:
          type: string
          example: "title"
        message:
          type: string
          example: "タイトルは必須です"

    ErrorResponse:
      type: object
      properties:
        error:
          type: object
          properties:
            status:
              type: integer
              format: int32
              example: 400
            code:
              type: string
              example: "VALIDATION_ERROR"
            message:
              type: string
              example: "リクエストの検証に失敗しました"
            details:
              type: array
              items:
                $ref: '#/components/schemas/ErrorDetail'
            timestamp:
              type: string
              format: date-time
              example: "2024-01-01T00:00:00Z"
            path:
              type: string
              example: "/api/v1/sprints"
```

## 3. 認証スキーマ

```yaml
    securitySchemes:
      BearerAuth:
        type: http
        scheme: bearer
        bearerFormat: JWT
        description: JWT Bearerトークンによる認証
```

## 4. スプリント (Sprints) スキーマ

### 4.1 スプリントエンティティ

```yaml
    SprintStatus:
      type: string
      enum:
        - PLANNED
        - ACTIVE
        - COMPLETED
        - CANCELLED
      example: ACTIVE

    Sprint:
      type: object
      properties:
        id:
          type: integer
          format: int64
          example: 1
        productId:
          type: integer
          format: int64
          example: 1
        sprintNumber:
          type: integer
          format: int32
          example: 1
        name:
          type: string
          example: "Sprint 1"
        goal:
          type: string
          nullable: true
          example: "ユーザー認証機能を実装"
        startDate:
          type: string
          format: date
          example: "2024-01-01"
        endDate:
          type: string
          format: date
          example: "2024-01-14"
        status:
          $ref: '#/components/schemas/SprintStatus'
        teamId:
          type: integer
          format: int64
          nullable: true
          example: 1
        teamName:
          type: string
          nullable: true
          example: "チームA"
        velocity:
          type: number
          format: float
          nullable: true
          example: 30.0
        notes:
          type: string
          nullable: true
        createdAt:
          type: string
          format: date-time
          example: "2023-12-25T09:00:00Z"
        updatedAt:
          type: string
          format: date-time
          example: "2024-01-01T09:00:00Z"
```

### 4.2 スプリントリクエスト

```yaml
    CreateSprintRequest:
      type: object
      required:
        - productId
        - sprintNumber
        - name
        - startDate
        - endDate
        - teamId
      properties:
        productId:
          type: integer
          format: int64
          example: 1
        sprintNumber:
          type: integer
          format: int32
          minimum: 1
          example: 2
        name:
          type: string
          minLength: 1
          maxLength: 100
          example: "Sprint 2"
        goal:
          type: string
          maxLength: 1000
          example: "ダッシュボード機能を実装"
        startDate:
          type: string
          format: date
          example: "2024-01-15"
        endDate:
          type: string
          format: date
          example: "2024-01-28"
        teamId:
          type: integer
          format: int64
          example: 1

    UpdateSprintRequest:
      type: object
      properties:
        name:
          type: string
          minLength: 1
          maxLength: 100
        goal:
          type: string
          maxLength: 1000
        startDate:
          type: string
          format: date
        endDate:
          type: string
          format: date
        status:
          $ref: '#/components/schemas/SprintStatus'
        teamId:
          type: integer
          format: int64
        notes:
          type: string
          maxLength: 2000

    UpdateSprintStatusRequest:
      type: object
      required:
        - status
      properties:
        status:
          $ref: '#/components/schemas/SprintStatus'
```

### 4.3 スプリント統計

```yaml
    SprintStats:
      type: object
      properties:
        sprintId:
          type: integer
          format: int64
          example: 1
        totalTasks:
          type: integer
          format: int32
          example: 20
        completedTasks:
          type: integer
          format: int32
          example: 12
        inProgressTasks:
          type: integer
          format: int32
          example: 5
        todoTasks:
          type: integer
          format: int32
          example: 3
        completionRate:
          type: number
          format: float
          example: 60.0
        totalEstimatedHours:
          type: number
          format: float
          example: 160.0
        totalActualHours:
          type: number
          format: float
          example: 96.0
        impedimentCount:
          type: integer
          format: int32
          example: 2
        activeImpedimentCount:
          type: integer
          format: int32
          example: 1
        dependencyCount:
          type: integer
          format: int32
          example: 5
        velocity:
          type: number
          format: float
          example: 30.0
        committedPoints:
          type: number
          format: float
          example: 25.0
        capacityPoints:
          type: number
          format: float
          example: 32.0
```

## 5. タスク (Tasks) スキーマ

```yaml
    TaskStatus:
      type: string
      enum:
        - TODO
        - IN_PROGRESS
        - DONE
        - CANCELLED
        - BLOCKED
      example: IN_PROGRESS

    BuildStatus:
      type: string
      enum:
        - SUCCESS
        - FAILURE
        - RUNNING
      example: SUCCESS

    Task:
      type: object
      properties:
        id:
          type: integer
          format: int64
          example: 1
        taskCode:
          type: string
          example: "TASK-001"
        title:
          type: string
          example: "ログイン画面を実装"
        description:
          type: string
          nullable: true
        status:
          $ref: '#/components/schemas/TaskStatus'
        estimatedHours:
          type: number
          format: float
          nullable: true
          example: 8.0
        actualHours:
          type: number
          format: float
          nullable: true
          example: 4.0
        progress:
          type: integer
          format: int32
          minimum: 0
          maximum: 100
          example: 50
        assigneeId:
          type: integer
          format: int64
          nullable: true
          example: 1
        assigneeName:
          type: string
          nullable: true
          example: "田中太郎"
        sprintId:
          type: integer
          format: int64
          nullable: true
          example: 1
        startDate:
          type: string
          format: date
          nullable: true
        dueDate:
          type: string
          format: date
          nullable: true
        completedAt:
          type: string
          format: date-time
          nullable: true
        sourceManagementInfo:
          type: string
          nullable: true
        buildStatus:
          $ref: '#/components/schemas/BuildStatus'
        notes:
          type: string
          nullable: true
        createdAt:
          type: string
          format: date-time
        updatedAt:
          type: string
          format: date-time

    CreateTaskRequest:
      type: object
      required:
        - title
      properties:
        title:
          type: string
          minLength: 1
          maxLength: 200
        description:
          type: string
          maxLength: 2000
        estimatedHours:
          type: number
          format: float
          minimum: 0
          maximum: 1000
        assigneeId:
          type: integer
          format: int64
        sprintId:
          type: integer
          format: int64
        dueDate:
          type: string
          format: date

    UpdateTaskRequest:
      type: object
      properties:
        title:
          type: string
          minLength: 1
          maxLength: 200
        description:
          type: string
          maxLength: 2000
        status:
          $ref: '#/components/schemas/TaskStatus'
        estimatedHours:
          type: number
          format: float
          minimum: 0
          maximum: 1000
        actualHours:
          type: number
          format: float
          minimum: 0
          maximum: 1000
        progress:
          type: integer
          format: int32
          minimum: 0
          maximum: 100
        assigneeId:
          type: integer
          format: int64
          nullable: true
        dueDate:
          type: string
          format: date
        sourceManagementInfo:
          type: string
          maxLength: 500
        notes:
          type: string
          maxLength: 2000

    UpdateTaskStatusRequest:
      type: object
      required:
        - status
      properties:
        status:
          $ref: '#/components/schemas/TaskStatus'
```

## 6. 障害 (Impediments) スキーマ

```yaml
    ImpedimentStatus:
      type: string
      enum:
        - NEW
        - IN_PROGRESS
        - RESOLVED
      example: IN_PROGRESS

    Impediment:
      type: object
      properties:
        id:
          type: integer
          format: int64
          example: 1
        sprintId:
          type: integer
          format: int64
          example: 1
        title:
          type: string
          example: "テスト環境がダウンしている"
        description:
          type: string
          example: "テストサーバーが起動せず、テストが実行できない"
        impact:
          type: string
          nullable: true
          example: "全テストが実行できない"
        assigneeId:
          type: integer
          format: int64
          nullable: true
          example: 1
        assigneeName:
          type: string
          nullable: true
          example: "田中太郎"
        status:
          $ref: '#/components/schemas/ImpedimentStatus'
        resolvedAt:
          type: string
          format: date-time
          nullable: true
        createdAt:
          type: string
          format: date-time
        updatedAt:
          type: string
          format: date-time

    CreateImpedimentRequest:
      type: object
      required:
        - sprintId
        - title
        - description
      properties:
        sprintId:
          type: integer
          format: int64
        title:
          type: string
          minLength: 1
          maxLength: 200
        description:
          type: string
          minLength: 1
          maxLength: 2000
        impact:
          type: string
          maxLength: 1000
        assigneeId:
          type: integer
          format: int64

    UpdateImpedimentRequest:
      type: object
      properties:
        title:
          type: string
          minLength: 1
          maxLength: 200
        description:
          type: string
          minLength: 1
          maxLength: 2000
        impact:
          type: string
          maxLength: 1000
        assigneeId:
          type: integer
          format: int64
          nullable: true
        status:
          $ref: '#/components/schemas/ImpedimentStatus'

    UpdateImpedimentStatusRequest:
      type: object
      required:
        - status
      properties:
        status:
          $ref: '#/components/schemas/ImpedimentStatus'
```

## 7. 依存関係 (Dependencies) スキーマ

```yaml
    DependencyType:
      type: string
      enum:
        - BLOCKS
        - REQUIRES
        - RELATES_TO
      example: BLOCKS

    Dependency:
      type: object
      properties:
        id:
          type: integer
          format: int64
          example: 1
        sourceItemId:
          type: integer
          format: int64
          example: 1
        targetItemId:
          type: integer
          format: int64
          example: 2
        dependencyType:
          $ref: '#/components/schemas/DependencyType'
        description:
          type: string
          nullable: true
        createdAt:
          type: string
          format: date-time
        updatedAt:
          type: string
          format: date-time

    CreateDependencyRequest:
      type: object
      required:
        - sourceItemId
        - targetItemId
        - dependencyType
      properties:
        sourceItemId:
          type: integer
          format: int64
        targetItemId:
          type: integer
          format: int64
        dependencyType:
          $ref: '#/components/schemas/DependencyType'
        description:
          type: string
          maxLength: 1000

    UpdateDependencyRequest:
      type: object
      properties:
        dependencyType:
          $ref: '#/components/schemas/DependencyType'
        description:
          type: string
          maxLength: 1000
```

## 8. 通知 (Notifications) スキーマ

```yaml
    NotificationType:
      type: string
      enum:
        - MENTION
        - ASSIGNEE_CHANGE
        - DUE_DATE
        - AWAITING_DECISION
      example: MENTION

    Notification:
      type: object
      properties:
        id:
          type: integer
          format: int64
          example: 1
        recipientId:
          type: integer
          format: int64
          example: 1
        productId:
          type: integer
          format: int64
          example: 1
        title:
          type: string
          example: "メンションされました"
        content:
          type: string
          example: "田中さんがコメントであなたをメンションしました"
        type:
          $ref: '#/components/schemas/NotificationType'
        isRead:
          type: boolean
          example: false
        createdAt:
          type: string
          format: date-time
        updatedAt:
          type: string
          format: date-time

    CreateNotificationRequest:
      type: object
      required:
        - recipientId
        - productId
        - title
        - content
        - type
      properties:
        recipientId:
          type: integer
          format: int64
        productId:
          type: integer
          format: int64
        title:
          type: string
          minLength: 1
          maxLength: 200
        content:
          type: string
          minLength: 1
          maxLength: 1000
        type:
          $ref: '#/components/schemas/NotificationType'

    ReadAllNotificationsResponse:
      type: object
      properties:
        updatedCount:
          type: integer
          format: int32
          example: 5
```

## 9. アクティビティ (Activities) スキーマ

```yaml
    ActivityType:
      type: string
      enum:
        - STATUS_CHANGE
        - COMMENT_ADDED
        - TASK_ASSIGNED
        - TASK_COMPLETED
        - IMPEDIMENT_ADDED
        - IMPEDIMENT_RESOLVED
        - DEPENDENCY_ADDED
        - DEPENDENCY_RESOLVED
        - SPRINT_GOAL_UPDATED
        - BOARD_UPDATED
      example: TASK_COMPLETED

    Activity:
      type: object
      properties:
        id:
          type: integer
          format: int64
          example: 1
        sprintId:
          type: integer
          format: int64
          example: 1
        actorId:
          type: integer
          format: int64
          example: 1
        actorName:
          type: string
          example: "田中太郎"
        activityType:
          $ref: '#/components/schemas/ActivityType'
        description:
          type: string
          nullable: true
          example: "「ログイン画面を実装」タスクを完了しました"
        createdAt:
          type: string
          format: date-time
```

## 10. デイリースクラム (Daily Scrums) スキーマ

```yaml
    DailyScrum:
      type: object
      properties:
        id:
          type: integer
          format: int64
          example: 1
        sprintId:
          type: integer
          format: int64
          example: 1
        teamMemberId:
          type: integer
          format: int64
          example: 1
        teamMemberName:
          type: string
          example: "田中太郎"
        scrumDate:
          type: string
          format: date
          example: "2024-01-03"
        yesterdayWork:
          type: string
          example: "ログイン画面のUIを実装"
        todayWork:
          type: string
          example: "ログインAPIとの連携を実装"
        blockers:
          type: string
          nullable: true
          example: "テスト環境がダウンしている"
        createdAt:
          type: string
          format: date-time
        updatedAt:
          type: string
          format: date-time

    CreateDailyScrumRequest:
      type: object
      required:
        - sprintId
        - teamMemberId
        - scrumDate
        - yesterdayWork
        - todayWork
      properties:
        sprintId:
          type: integer
          format: int64
        teamMemberId:
          type: integer
          format: int64
        scrumDate:
          type: string
          format: date
        yesterdayWork:
          type: string
          minLength: 1
          maxLength: 1000
        todayWork:
          type: string
          minLength: 1
          maxLength: 1000
        blockers:
          type: string
          maxLength: 1000

    UpdateDailyScrumRequest:
      type: object
      properties:
        yesterdayWork:
          type: string
          minLength: 1
          maxLength: 1000
        todayWork:
          type: string
          minLength: 1
          maxLength: 1000
        blockers:
          type: string
          maxLength: 1000
```

## 11. パスパラメータ

```yaml
    parameters:
      SprintId:
        name: id
        in: path
        required: true
        schema:
          type: integer
          format: int64
        description: スプリントID

      SprintIdForSubResource:
        name: sprintId
        in: path
        required: true
        schema:
          type: integer
          format: int64
        description: スプリントID

      TaskId:
        name: id
        in: path
        required: true
        schema:
          type: integer
          format: int64
        description: タスクID

      ImpedimentId:
        name: id
        in: path
        required: true
        schema:
          type: integer
          format: int64
        description: 障害ID

      DependencyId:
        name: id
        in: path
        required: true
        schema:
          type: integer
          format: int64
        description: 依存関係ID

      NotificationId:
        name: id
        in: path
        required: true
        schema:
          type: integer
          format: int64
        description: 通知ID

      DailyScrumId:
        name: id
        in: path
        required: true
        schema:
          type: integer
          format: int64
        description: デイリースクラムID
```

## 12. クエリパラメータ

```yaml
      PageNumber:
        name: pageNumber
        in: query
        schema:
          type: integer
          format: int32
          default: 0
        description: ページ番号 (0起点)

      PageSize:
        name: pageSize
        in: query
        schema:
          type: integer
          format: int32
          default: 20
          maximum: 100
        description: ページサイズ

      Sort:
        name: sort
        in: query
        schema:
          type: string
          default: "createdAt,DESC"
        description: ソート条件
```

## 13. レスポンス

```yaml
    responses:
      NotFound:
        description: リソースが見つかりません
        content:
          application/json:
            schema:
              $ref: '#/components/schemas/ErrorResponse'

      ValidationError:
        description: バリデーションエラー
        content:
          application/json:
            schema:
              $ref: '#/components/schemas/ErrorResponse'

      Conflict:
        description: 競合エラー
        content:
          application/json:
            schema:
              $ref: '#/components/schemas/ErrorResponse'

      Forbidden:
        description: アクセスが拒否されました
        content:
          application/json:
            schema:
              $ref: '#/components/schemas/ErrorResponse'

      Unauthorized:
        description: 認証が必要です
        content:
          application/json:
            schema:
              $ref: '#/components/schemas/ErrorResponse'

      InternalError:
        description: サーバー内部エラー
        content:
          application/json:
            schema:
              $ref: '#/components/schemas/ErrorResponse'
```

## 14. 参照

- API設計スキル: `.agents/skills/api-design/SKILL.md`
- エンドポイント一覧: `01.エンドポイント/エンドポイント一覧.md`
- エラーハンドリング設計: `02.エラーハンドリング/エラーハンドリング設計.md`
- セキュリティ設計: `03.セキュリティ/セキュリティ設計.md`
