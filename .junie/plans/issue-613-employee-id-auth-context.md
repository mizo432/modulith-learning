---
sessionId: session-261117-175956-17jv
---

# Requirements

## Overview & Goals

Issue #613「ログインにemployeeIdを結びつけ、バックログ画面の従業員ID参照をAuthContextへ移す」の実装計画。
親決定 #573（ロールは所属関係から導出、従業員IDの出所はAuthContext）と #614（usersテーブルにemployee_id追加）の実装タスク。

ログイン応答にemployeeIdを含め、AuthContextで保持し、バックログ画面の`localStorage.getItem('employeeId')`参照をAuthContext参照へ差し替える。

## Scope

### In Scope
- authorizationのusersテーブルへ`employee_id`（BIGINT、nullable）追加のFlywayマイグレーション
- Userエンティティへ`employeeId`フィールド追加
- AuthResponseへ`employeeId`追加、ログイン応答に含める
- AuthControllerのloginエンドポイントがUserのemployeeIdをAuthResponseに含める
- frontendのAuthContextへ`employeeId`追加（login/login応答/ログアウト時破棄）
- Login.tsxがAPI応答からemployeeIdを受け取りAuthContext.loginに渡す
- BacklogItemListとBacklogItemDetailの`localStorage.getItem('employeeId')`をAuthContext参照へ差し替え
- 関連テストの追随

### Out of Scope
- employeeIdの登録・編集UI（ユーザー管理画面での従業員選択）は別Issue
- backendのaccountMgmt `/users`エンドポイントのUser表現へのemployeeId含否は検討のみ（本Issueでは含める。UserエンティティにemployeeIdが追加されるため、既存の`findAllUsers()`は自動的にemployeeIdを含むJSONを返す。追加のDTO変換は不要と判断）
- frontendのロール導出ロジック（canChangeStatus等）の変更（参照元のみ差し替え）
- localStorageの`employeeId`キーの明示的なクリーンアップ（AuthContextが管理するようになれば不要になる）

## User Stories

- As a logged-in employee, I want my employeeId to be available in AuthContext, so that the backlog screens can determine my role without relying on localStorage.
- As a developer, I want the employeeId to come from the login response, so that there is a single source of truth for the current user's employee identity.

## Functional Requirements

1. **usersテーブルにemployee_id列追加** — BIGINT、nullable。EMPLOYEEタイプのユーザーに設定、それ以外はnull
2. **UserエンティティにemployeeId追加** — `@Column(name = "employee_id")`のLong型フィールド
3. **AuthResponseにemployeeId追加** — Long型（null可）。ログイン応答に含める
4. **AuthContextにemployeeId追加** — login()のシグネチャにemployeeIdパラメータ追加、ログアウト時に破棄
5. **Login.tsxのlogin呼び出し更新** — API応答のemployeeIdをAuthContext.loginに渡す
6. **BacklogItemListの参照差し替え** — `localStorage.getItem('employeeId')`を`useAuth().employeeId`へ
7. **BacklogItemDetailの参照差し替え** — `localStorage.getItem('employeeId')`を`useAuth().employeeId`へ

## Non-Functional Requirements

- employeeIdはSnowflakeId（BIGINT）のID参照のみ。FK制約は張らない（アーキテクチャ規約）
- JSONではIDをstringとして扱う（Login.tsxの既存パターンに従う）
- 既存のテストが通ること


# Technical Design

## Current Implementation

### Backend (authorization)
- **User.java** (`authorization/.../domain/model/user/User.java`): id(Long/IDENTITY), username, password, email, firstName, lastName, initials, enabled, createdAt, updatedAt, lastLoginAt, firstLogin, userType(UserType)。employeeIdはなし。
- **AuthResponse.java** (`authorization/.../presentation/controller/AuthResponse.java`): success, username, message, firstLogin の4フィールドのみ。
- **AuthController.java** (`authorization/.../presentation/controller/AuthController.java`): `POST /api/auth/login` で `authenticationService.authenticate()` → User取得 → AuthResponse(success, username, message, firstLogin) を返す。employeeIdは含めない。
- **Flywayマイグレーション**: V1(初期) と V2(氏名暗号化) がある。次はV3。
- **UserController.java**: `GET /api/users` で `userService.findAllUsers()` → Userエンティティリストを直接返す。DTO変換なし。

### Frontend
- **AuthContext.tsx** (`frontend/src/contexts/AuthContext.tsx`): isAuthenticated, username, isAdmin, isFirstLogin を保持。`login(token, username, isAdmin, isFirstLogin)` / `logout()` / `setFirstLogin(value)`。employeeIdはなし。
- **Login.tsx** (`frontend/src/pages/Login.tsx`): `api.post('/auth/login', {username, password})` → 応答の`{success, username, firstLogin}`を受け取り `login(token, username, isAdmin, firstLogin)` を呼ぶ。`response.data.token`および`response.data.isAdmin`は現在API応答に存在しないが、dummy値でフォールバックしている。
- **BacklogItemList.tsx** (`frontend/src/pages/products/BacklogItemList.tsx`): `localStorage.getItem('employeeId')` でcurrentEmployeeIdを取得。canChangeStatusの判定とアイテム作成・状態変更时使用。
- **BacklogItemDetail.tsx** (`frontend/src/pages/products/BacklogItemDetail.tsx`): `localStorage.getItem('employeeId')` をコメント投稿のauthorEmployeeIdに使用。

### テスト
- **BacklogItemList.test.tsx**: localStorageモックで`employeeId` → `'emp-1'`を返す。
- **BacklogItemDetail.test.tsx**: localStorageモックなし（employeeId参照は実装側でlocalStorageを読む）。
- **AuthenticationServiceImplTest.java**: Userのビルダーでテストデータ作成。employeeIdフィールドはなし。

## Key Decisions

1. **employeeIdの型はLong（nullable）** — SnowflakeIdはBIGINT。JPAではLong型。JSONでは数値としてシリアライズ（Login.tsxでstringフォールバックは既存パターンに従う）。
2. **AuthResponseのemployeeIdはLong型** — null可。EMPLOYEE以外や未設定の場合はnull。
3. **AuthContextのemployeeIdはstring型** — frontend側でIDをstringとして扱う規約（JavaScript Number精度対策）に従う。ログイン応答の数値をstringに変換して保持。
4. **backend `/api/users` のUser表現は変更不要** — UserエンティティにemployeeIdフィールドが追加されるため、Jacksonによる自動シリアライズでemployeeIdがJSONに含まれる。追加のDTO変換は不要。
5. **UserCreateRequest / UserAdminUpdateRequest にemployeeId追加** — 管理者がユーザー作成・編集時にemployeeIdを設定できるようにする。

## Proposed Changes

### Backend (authorization)

#### 1. Flywayマイグレーション `V3__add_employee_id_to_users.sql`（新規）
```
ALTER TABLE users ADD COLUMN employee_id BIGINT;
```

#### 2. User.java 変更
- `private Long employeeId;` フィールド追加（`@Column(name = "employee_id")`）
- `toString()` にemployeeIdを追加

#### 3. AuthResponse.java 変更
- `private Long employeeId;` フィールド追加

#### 4. AuthController.java 変更
- `login()` メソッドでAuthResponse作成時に `user.getEmployeeId()` を含める
- 他のAuthResponseを生成するメソッドもemployeeIdを含める（changePassword等）

#### 5. UserCreateRequest.java 変更
- `private Long employeeId;` フィールド追加

#### 6. UserAdminUpdateRequest.java 変更
- `private Long employeeId;` フィールド追加

#### 7. UserController.java 変更
- `createUser()` でemployeeIdをUserに設定
- `updateUser()` でemployeeIdを更新

### Frontend

#### 8. AuthContext.tsx 変更
- `AuthContextType` に `employeeId: string | null` 追加
- `login()` シグネチャに `employeeId?: string | null` パラメータ追加
- stateに`employeeId`追加、localStorageにも保存・復元
- `logout()` でemployeeIdを破棄

#### 9. Login.tsx 変更
- API応答から`employeeId`を取得し、`login()`に渡す
- `response.data.employeeId?.toString()` でstringに変換

#### 10. BacklogItemList.tsx 変更
- `useAuth()` から`employeeId`を取得
- `localStorage.getItem('employeeId')` を `auth.employeeId` へ差し替え
- `localStorage.getItem('user')` も `auth.username` へ差し替え（creatorNameの初期値）

#### 11. BacklogItemDetail.tsx 変更
- `useAuth()` から`employeeId`を取得
- `localStorage.getItem('employeeId')` を `auth.employeeId` へ差し替え
- `localStorage.getItem('user')` も `auth.username` へ差し替え（commentAuthorNameの初期値）

## Testing Changes

### Backend
- **AuthenticationServiceImplTest.java**: UserビルダーにemployeeIdを追加（既存テストに影響しないが、新規テストでemployeeIdを設定）
- **UserTest.java**（存在する場合）: employeeIdのgetter/setter検証

### Frontend
- **BacklogItemList.test.tsx**: localStorageモックの`employeeId`返却を削除。代わりにAuthContextのモックでemployeeIdを提供（またはAuthContextの値が正しく使われることを確認）
- **BacklogItemDetail.test.tsx**: localStorageモックの`employeeId`参照が不要になることを確認

## Architecture Diagram

```mermaid
graph LR
  A[Login.tsx] -->|POST /api/auth/login| B[AuthController]
  B -->|authenticate| C[AuthenticationService]
  C -->|findByUsername| D[UserRepository]
  D -->|User{employeeId}| C
  C -->|User| B
  B -->|AuthResponse{employeeId}| A
  A -->|login(token, username, isAdmin, firstLogin, employeeId)| E[AuthContext]
  E -->|employeeId| F[BacklogItemList]
  E -->|employeeId| G[BacklogItemDetail]
```


# Testing

## Backend テストケース

### `AuthenticationServiceImplTest.java`（既存の追随）
- 既存のUserビルダーにemployeeIdフィールドを追加（テストデータにemployeeIdを設定）。既存テストの動作に影響しない。

### `UserTest.java`（既存の追随）
- UserエンティティのemployeeIdのgetter/setterが動作することを確認（既存のテストパターンに従う）。

### AuthControllerのテスト（存在する場合）
- login応答にemployeeIdが含まれることを確認。

## Frontend テストケース

### `BacklogItemList.test.tsx`（修正）
- localStorageモックの`employeeId`返却を削除
- `useAuth`のモック（またはAuthContextの値）からemployeeIdが正しく使われることを確認
- `localStorage.getItem('user')` の代わりに `useAuth().username` が使われることを確認
- 既存のテストケース（Value表示、承認ボタン、ソート順、自動リフレッシュ等）が引き続き動作すること

### `BacklogItemDetail.test.tsx`（修正）
- `localStorage.getItem('employeeId')` の参照がAuthContext経由になっていることを確認
- `localStorage.getItem('user')` の代わりに `useAuth().username` が使われることを確認
- 既存のテストケース（Value入力、スライダー、自動リフレッシュ等）が引き続き動作すること

## エッジケース

1. **employeeIdがnullのユーザー（EMPLOYEE以外）** — AuthContextのemployeeIdがnull。バックログ画面でcanChangeStatusがfalseになる（正しく動作）
2. **ログイン応答にemployeeIdがない（後方互換）** — Login.tsxで`response.data.employeeId?.toString()` がundefined/nullになる。AuthContextでnullとして保持
3. **localStorageに旧employeeIdが残っている場合** — AuthContextがemployeeIdを管理するため、localStorageの旧値は無視される


# Delivery Steps

###   Step 1: Backend: FlywayマイグレーションとUser/AuthResponseにemployeeId追加
authorizationモジュールにemployeeIdを追加。DBマイグレーション、エンティティ、レスポンスDTOを更新。

**Flywayマイグレーション**
- `authorization/src/main/resources/db/migration/V3__add_employee_id_to_users.sql` を新規作成。`ALTER TABLE users ADD COLUMN employee_id BIGINT;`

**User.java**
- `private Long employeeId;` フィールドを`@Column(name = "employee_id")`で追加
- `toString()` にemployeeIdを追加

**AuthResponse.java**
- `private Long employeeId;` フィールドを追加

**UserCreateRequest.java**
- `private Long employeeId;` フィールドを追加

**UserAdminUpdateRequest.java**
- `private Long employeeId;` フィールドを追加

**テスト追随**
- `AuthenticationServiceImplTest.java` のUserビルダーにemployeeIdを追加
- `UserTest.java`（存在する場合）にemployeeIdのテストを追加

###   Step 2: Backend: AuthControllerとUserControllerにemployeeId連携
ログイン応答とユーザー管理APIでemployeeIdを扱うように更新。

**AuthController.java**
- `login()` メソッドでAuthResponse作成時に `user.getEmployeeId()` を含める
- `changePassword()` 等他のAuthResponseを生成するメソッドもemployeeIdを含める

**UserController.java**
- `createUser()` でリクエストのemployeeIdをUserに設定
- `updateUser()` でemployeeIdを更新（request.getEmployeeId()がnullの場合の扱い：設定しない、またはnullで上書きするか検討。管理者が明示的にnullを設定できる必要があるため、nullで上書きする）

**テスト**
- AuthControllerのテスト（存在する場合）でlogin応答のemployeeIdを確認

###   Step 3: Frontend: AuthContextにemployeeId追加
AuthContextにemployeeIdを追加し、ログイン時に保持・ログアウト時に破棄する。

**AuthContext.tsx**
- `AuthContextType` インターフェースに `employeeId: string | null` を追加
- `login()` シグネチャを `login(token, username, isAdmin, isFirstLogin?, employeeId?)` に変更
- stateに `employeeId` を追加（初期値null）
- `useEffect` でlocalStorageからemployeeIdを復元
- `login()` でemployeeIdをlocalStorageに保存しstateに設定
- `logout()` でemployeeIdをlocalStorageから削除しstateをnullに
- ProviderのvalueにemployeeIdを含める

###   Step 4: Frontend: Login.tsxとバックログ画面の参照差し替え
ログイン画面がAPI応答からemployeeIdを受け取り、バックログ画面がAuthContextからemployeeIdを取得するよう更新。

**Login.tsx**
- API応答から `response.data.employeeId?.toString()` を取得
- `login()` 呼び出しにemployeeIdを渡す

**BacklogItemList.tsx**
- `useAuth()` をimportし、`employeeId` と `username` を取得
- `localStorage.getItem('employeeId')` を `auth.employeeId` へ差し替え
- `localStorage.getItem('user')` を `auth.username` へ差し替え（formCreatorNameの初期値）

**BacklogItemDetail.tsx**
- `useAuth()` をimportし、`employeeId` と `username` を取得
- `localStorage.getItem('employeeId')` を `auth.employeeId` へ差し替え
- `localStorage.getItem('user')` を `auth.username` へ差し替え（commentAuthorNameの初期値）

**テスト追随**
- `BacklogItemList.test.tsx`: localStorageモックのemployeeId/user返却を削除。useAuthのモックでemployeeId='emp-1'、username='テストユーザー'を提供
- `BacklogItemDetail.test.tsx`: localStorageのemployeeId参照が不要になることを確認。useAuthのモックを追加（必要場合）

## 判定（Muse、2026-11-17）

採用。条件は次の3点。

1. **AuthResponse の employeeId は JSON 文字列として返すこと**。SnowflakeId は JavaScript の安全整数（2^53）を超えるため、数値で返すと frontend で精度が失われ、チームの poEmployeeId 等（文字列）との照合が壊れる。「画面・API では ID は string」という既存規約（#583）に従う。User エンティティ内部の保持は Long でよい。frontend 側は文字列のまま保持し、数値化しない（計画の Key Decisions 1 の「数値としてシリアライズ」はこの条件で置き換える）。
2. **employeeId を確実に設定するのはログイン応答**。AuthResponse の他の生成箇所（パスワード変更系など）は User が手元にある場合のみ設定し、それ以外は null でよい。frontend が使うのはログイン応答のみ。
3. **userService.ts の User 型にも employeeId: string | null を追加すること**（/users API の JSON に含まれるため、型も一致させる）。

write_tests の独立ステップは設けない（テスト追随を implement に含める）。検証は authorization のテスト、backend のテスト、frontend のテスト（CI=true npm test）と frontend ビルドの成功を実測すること。