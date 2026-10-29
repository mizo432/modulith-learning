# API連携設計

AG-04スプリント実行のAPIサービス層とエンドポイントを設計します。

## 1. APIクライアント設計

### 1.1 axiosインスタンス

`src/services/api.ts` でaxiosインスタンスを一元管理します。

```typescript
import axios from 'axios';

const api = axios.create({
  baseURL: process.env.REACT_APP_API_URL || '/api',
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json',
  },
});

// リクエストインターセプター
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('authToken');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => Promise.reject(error)
);

// レスポンスインターセプター
api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      // トークン期限切れで再認証フロー
      window.location.href = '/login';
    }
    return Promise.reject(error);
  }
);

export default api;
```

### 1.2 エラーハンドリング

```typescript
export class ApiError extends Error {
  constructor(
    public status: number,
    public message: string,
    public details?: string
  ) {
    super(message);
    this.name = 'ApiError';
  }
}

export function handleApiError(error: unknown): ApiError {
  if (error instanceof ApiError) return error;
  if (axios.isAxiosError(error)) {
    const status = error.response?.status || 500;
    const message = error.response?.data?.message || 'サーバーエラーが発生しました';
    return new ApiError(status, message);
  }
  return new ApiError(500, 'ネットワークエラーが発生しました');
}
```

---

## 2. スプリントAPI (`services/sprintApi.ts`)

```typescript
import api from './api';
import {
  Sprint,
  CreateSprintRequest,
  UpdateSprintRequest,
  SprintBacklog,
  CreateSprintBacklogRequest,
  SprintListQuery,
  SprintStats,
  PaginatedResponse,
} from '../types/sprint';

const SPRINTS_PATH = '/sprints';

// スプリント一覧取得
export async function getSprints(
  query: SprintListQuery
): Promise<PaginatedResponse<Sprint>> {
  const { data } = await api.get(SPRINTS_PATH, { params: query });
  return data;
}

// スプリント詳細取得
export async function getSprint(id: number): Promise<Sprint> {
  const { data } = await api.get(`${SPRINTS_PATH}/${id}`);
  return data;
}

// スプリント作成
export async function createSprint(
  request: CreateSprintRequest
): Promise<Sprint> {
  const { data } = await api.post(SPRINTS_PATH, request);
  return data;
}

// スプリント更新
export async function updateSprint(
  id: number,
  request: UpdateSprintRequest
): Promise<Sprint> {
  const { data } = await api.put(`${SPRINTS_PATH}/${id}`, request);
  return data;
}

// スプリントバックログ作成
export async function createSprintBacklog(
  request: CreateSprintBacklogRequest
): Promise<SprintBacklog> {
  const { data } = await api.post('/sprint-backlogs', request);
  return data;
}

// スプリント統計取得
export async function getSprintStats(productId: number): Promise<SprintStats> {
  const { data } = await api.get(`${SPRINTS_PATH}/stats`, {
    params: { productId },
  });
  return data;
}
```

---

## 3. タスクAPI (`services/taskApi.ts`)

```typescript
import api from './api';
import {
  Task,
  CreateTaskRequest,
  UpdateTaskRequest,
  TaskListQuery,
  PaginatedResponse,
} from '../types/task';

const TASKS_PATH = '/tasks';

// タスク一覧取得
export async function getTasks(
  query: TaskListQuery
): Promise<PaginatedResponse<Task>> {
  const { data } = await api.get(TASKS_PATH, { params: query });
  return data;
}

// タスク詳細取得
export async function getTask(id: number): Promise<Task> {
  const { data } = await api.get(`${TASKS_PATH}/${id}`);
  return data;
}

// タスク作成
export async function createTask(request: CreateTaskRequest): Promise<Task> {
  const { data } = await api.post(TASKS_PATH, request);
  return data;
}

// タスク更新
export async function updateTask(
  id: number,
  request: UpdateTaskRequest
): Promise<Task> {
  const { data } = await api.put(`${TASKS_PATH}/${id}`, request);
  return data;
}

// タスク削除
export async function deleteTask(id: number): Promise<void> {
  await api.delete(`${TASKS_PATH}/${id}`);
}

// タスク状態変更（ボード用）
export async function updateTaskStatus(
  id: number,
  status: string
): Promise<Task> {
  const { data } = await api.patch(`${TASKS_PATH}/${id}/status`, { status });
  return data;
}
```

---

## 4. 障害API (`services/impedimentApi.ts`)

```typescript
import api from './api';
import {
  Impediment,
  CreateImpedimentRequest,
  UpdateImpedimentRequest,
} from '../types/impediment';

const IMPEDIMENTS_PATH = '/impediments';

// 障害一覧取得
export async function getImpediments(sprintId: number): Promise<Impediment[]> {
  const { data } = await api.get(IMPEDIMENTS_PATH, {
    params: { sprintId },
  });
  return data;
}

// 障害詳細取得
export async function getImpediment(id: number): Promise<Impediment> {
  const { data } = await api.get(`${IMPEDIMENTS_PATH}/${id}`);
  return data;
}

// 障害作成
export async function createImpediment(
  request: CreateImpedimentRequest
): Promise<Impediment> {
  const { data } = await api.post(IMPEDIMENTS_PATH, request);
  return data;
}

// 障害更新
export async function updateImpediment(
  id: number,
  request: UpdateImpedimentRequest
): Promise<Impediment> {
  const { data } = await api.put(`${IMPEDIMENTS_PATH}/${id}`, request);
  return data;
}

// 障害削除
export async function deleteImpediment(id: number): Promise<void> {
  await api.delete(`${IMPEDIMENTS_PATH}/${id}`);
}
```

---

## 5. 依存関係API (`services/dependencyApi.ts`)

```typescript
import api from './api';
import {
  Dependency,
  CreateDependencyRequest,
  UpdateDependencyRequest,
} from '../types/dependency';

const DEPENDENCIES_PATH = '/dependencies';

// 依存関係一覧取得
export async function getDependencies(sprintId: number): Promise<Dependency[]> {
  const { data } = await api.get(DEPENDENCIES_PATH, {
    params: { sprintId },
  });
  return data;
}

// 依存関係作成
export async function createDependency(
  request: CreateDependencyRequest
): Promise<Dependency> {
  const { data } = await api.post(DEPENDENCIES_PATH, request);
  return data;
}

// 依存関係更新
export async function updateDependency(
  id: number,
  request: UpdateDependencyRequest
): Promise<Dependency> {
  const { data } = await api.put(`${DEPENDENCIES_PATH}/${id}`, request);
  return data;
}

// 依存関係削除
export async function deleteDependency(id: number): Promise<void> {
  await api.delete(`${DEPENDENCIES_PATH}/${id}`);
}
```

---

## 6. 通知API (`services/notificationApi.ts`)

```typescript
import api from './api';
import {
  Notification,
  NotificationListQuery,
  UpdateNotificationRequest,
  PaginatedResponse,
} from '../types/notification';

const NOTIFICATIONS_PATH = '/notifications';

// 通知一覧取得
export async function getNotifications(
  query: NotificationListQuery
): Promise<PaginatedResponse<Notification>> {
  const { data } = await api.get(NOTIFICATIONS_PATH, { params: query });
  return data;
}

// 未読通知数取得
export async function getUnreadCount(): Promise<number> {
  const { data } = await api.get(`${NOTIFICATIONS_PATH}/unread-count`);
  return data;
}

// 通知を既読にする
export async function markAsRead(
  id: number
): Promise<Notification> {
  const { data } = await api.put(`${NOTIFICATIONS_PATH}/${id}/read`, {
    isRead: true,
  });
  return data;
}

// 全通知を既読にする
export async function markAllAsRead(): Promise<void> {
  await api.put(`${NOTIFICATIONS_PATH}/read-all`);
}
```

---

## 7. アクティビティAPI (`services/activityApi.ts`)

```typescript
import api from './api';
import {
  Activity,
  ActivityListQuery,
  PaginatedResponse,
} from '../types/activity';

const ACTIVITIES_PATH = '/activities';

// アクティビティ一覧取得
export async function getActivities(
  query: ActivityListQuery
): Promise<PaginatedResponse<Activity>> {
  const { data } = await api.get(ACTIVITIES_PATH, { params: query });
  return data;
}
```

---

## 8. デイリースクラムAPI (`services/dailyScrumApi.ts`)

```typescript
import api from './api';
import {
  DailyScrum,
  CreateDailyScrumRequest,
  UpdateDailyScrumRequest,
  DailyScrumListQuery,
  PaginatedResponse,
} from '../types/dailyScrum';

const DAILY_SCRUMS_PATH = '/daily-scrums';

// デイリースクラム一覧取得
export async function getDailyScrums(
  query: DailyScrumListQuery
): Promise<PaginatedResponse<DailyScrum>> {
  const { data } = await api.get(DAILY_SCRUMS_PATH, { params: query });
  return data;
}

// デイリースクラム詳細取得
export async function getDailyScrum(id: number): Promise<DailyScrum> {
  const { data } = await api.get(`${DAILY_SCRUMS_PATH}/${id}`);
  return data;
}

// デイリースクラム作成
export async function createDailyScrum(
  request: CreateDailyScrumRequest
): Promise<DailyScrum> {
  const { data } = await api.post(DAILY_SCRUMS_PATH, request);
  return data;
}

// デイリースクラム更新
export async function updateDailyScrum(
  id: number,
  request: UpdateDailyScrumRequest
): Promise<DailyScrum> {
  const { data } = await api.put(`${DAILY_SCRUMS_PATH}/${id}`, request);
  return data;
}

// デイリースクラム削除
export async function deleteDailyScrum(id: number): Promise<void> {
  await api.delete(`${DAILY_SCRUMS_PATH}/${id}`);
}
```

---

## 9. エンドポイント一覧

| メソッド | パス | 説明 | サービス |
|---------|------|------|----------|
| GET | `/sprints` | スプリント一覧取得 | sprintApi |
| GET | `/sprints/:id` | スプリント詳細取得 | sprintApi |
| POST | `/sprints` | スプリント作成 | sprintApi |
| PUT | `/sprints/:id` | スプリント更新 | sprintApi |
| GET | `/sprints/stats` | スプリント統計取得 | sprintApi |
| POST | `/sprint-backlogs` | Sprint Backlog作成 | sprintApi |
| GET | `/tasks` | タスク一覧取得 | taskApi |
| GET | `/tasks/:id` | タスク詳細取得 | taskApi |
| POST | `/tasks` | タスク作成 | taskApi |
| PUT | `/tasks/:id` | タスク更新 | taskApi |
| DELETE | `/tasks/:id` | タスク削除 | taskApi |
| PATCH | `/tasks/:id/status` | タスク状態変更 | taskApi |
| GET | `/impediments` | 障害一覧取得 | impedimentApi |
| GET | `/impediments/:id` | 障害詳細取得 | impedimentApi |
| POST | `/impediments` | 障害作成 | impedimentApi |
| PUT | `/impediments/:id` | 障害更新 | impedimentApi |
| DELETE | `/impediments/:id` | 障害削除 | impedimentApi |
| GET | `/dependencies` | 依存関係一覧取得 | dependencyApi |
| POST | `/dependencies` | 依存関係作成 | dependencyApi |
| PUT | `/dependencies/:id` | 依存関係更新 | dependencyApi |
| DELETE | `/dependencies/:id` | 依存関係削除 | dependencyApi |
| GET | `/notifications` | 通知一覧取得 | notificationApi |
| GET | `/notifications/unread-count` | 未読通知数取得 | notificationApi |
| PUT | `/notifications/:id/read` | 通知を既読にする | notificationApi |
| PUT | `/notifications/read-all` | 全通知を既読にする | notificationApi |
| GET | `/activities` | アクティビティ一覧取得 | activityApi |
| GET | `/daily-scrums` | デイリースクラム一覧取得 | dailyScrumApi |
| GET | `/daily-scrums/:id` | デイリースクラム詳細取得 | dailyScrumApi |
| POST | `/daily-scrums` | デイリースクラム作成 | dailyScrumApi |
| PUT | `/daily-scrums/:id` | デイリースクラム更新 | dailyScrumApi |
| DELETE | `/daily-scrums/:id` | デイリースクラム削除 | dailyScrumApi |

---

## 10. ローディング・エラー状態の管理

各API呼び出しでは以下の状態を管理します：

```typescript
interface AsyncState<T> {
  data: T | null;
  loading: boolean;
  error: ApiError | null;
}
```

- **ローディング表示**: データ取得中は `CircularProgress` または `Skeleton` を表示
- **エラー表示**: エラー発生時は `Alert` コンポーネントでエラーメッセージを表示
- **リトライ機能**: エラー時にリトライボタンを表示

## 参照

- データベース設計: `docs/23 詳細設計書/AG_アジルーム/01 データベース設計/`
- 型定義設計: `docs/23 詳細設計書/AG_アジルーム/02 フロントエンド設計/01.型定義/型定義.md`
