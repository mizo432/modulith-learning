# AS_関連付け API連携設計

## 概要

AS関連付け事業領域のフロントエンドとバックエンドAPIの連携設計を定義します。
APIクライアントの構成、エラーハンドリング、ローディング状態の管理などを整理します。

## 参照

- API外部設計: [backend/api/index.md](../backend/api/index.md)
- ステート管理設計: [ステート管理設計.md](./ステート管理設計.md)
- OpenAPI仕様書: [openapi.yaml](../backend/api/openapi.yaml)

---

## 1. APIクライアント設計

### 1.1 採用技術

| 技術 | 説明 |
|------|------|
| Axios | HTTPクライアント |
| React Query | サーバーステート管理 |
| Axios Interceptors | リクエスト・レスポンスのインターセプト |

### 1.2 クライアント構成

```
src/
├── api/
│   ├── client.ts           # Axiosインスタンス設定
│   ├── interceptors.ts     # インターセプター設定
│   ├── parties.ts          # Party API
│   ├── partyRoles.ts       # Party Role API
│   ├── employees.ts        # Employee API
│   ├── organizations.ts    # Organization API
│   ├── companies.ts        # Company API
│   └── stores.ts           # Store API
```

### 1.3 Axiosインスタンス設定

| 項目 | 設定値 |
|------|--------|
| baseURL | `/api/as/v1` |
| timeout | 10秒 |
| headers | `Content-Type: application/json` |
| withCredentials | `false` |

---

## 2. インターセプター設計

### 2.1 リクエストインターセプター

| 処理 | 説明 |
|------|------|
| 認証トークン付与 | AuthorizationヘッダーにBearerトークンを付与 |
| リクエストログ | 開発環境でリクエスト内容をログ出力 |
| リトライ設定 | 一時的なエラー時に自動リトライ（最大3回） |

### 2.2 レスポンスインターセプター

| 処理 | 説明 |
|------|------|
| 共通エラー処理 | HTTPエラーを統一された形式に変換 |
| トークン更新 | 401エラー時にトークン更新を試行 |
| レスポンスログ | 開発環境でレスポンス内容をログ出力 |

---

## 3. API関数定義

### 3.1 Party API

| 関数名 | HTTPメソッド | エンドポイント | 説明 |
|--------|-------------|----------------|------|
| `getParties` | GET | `/parties` | パーティー一覧取得 |
| `getParty` | GET | `/parties/{id}` | パーティー詳細取得 |
| `createParty` | POST | `/parties` | パーティー作成 |
| `updateParty` | PUT | `/parties/{id}` | パーティー更新 |
| `deleteParty` | DELETE | `/parties/{id}` | パーティー削除 |
| `searchParties` | GET | `/parties/search` | パーティー検索 |

### 3.2 Employee API

| 関数名 | HTTPメソッド | エンドポイント | 説明 |
|--------|-------------|----------------|------|
| `getEmployees` | GET | `/employees` | 従業員一覧取得 |
| `getEmployee` | GET | `/employees/{id}` | 従業員詳細取得 |
| `createEmployee` | POST | `/employees` | 従業員作成 |
| `updateEmployee` | PUT | `/employees/{id}` | 従業員更新 |
| `deleteEmployee` | DELETE | `/employees/{id}` | 従業員削除 |
| `searchEmployees` | GET | `/employees/search` | 従業員検索 |
| `changeAffiliation` | PATCH | `/employees/{id}/affiliation` | 所属変更 |

### 3.3 Organization API

| 関数名 | HTTPメソッド | エンドポイント | 説明 |
|--------|-------------|----------------|------|
| `getOrganizations` | GET | `/organizations` | 組織一覧取得 |
| `getOrganization` | GET | `/organizations/{id}` | 組織詳細取得 |
| `createOrganization` | POST | `/organizations` | 組織作成 |
| `updateOrganization` | PUT | `/organizations/{id}` | 組織更新 |
| `deleteOrganization` | DELETE | `/organizations/{id}` | 組織削除 |
| `searchOrganizations` | GET | `/organizations/search` | 組織検索 |
| `changeHierarchy` | PATCH | `/organizations/hierarchy` | 階層変更 |

### 3.4 Company API

| 関数名 | HTTPメソッド | エンドポイント | 説明 |
|--------|-------------|----------------|------|
| `getCompanies` | GET | `/companies` | 会社一覧取得 |
| `getCompany` | GET | `/companies/{id}` | 会社詳細取得 |
| `createCompany` | POST | `/companies` | 会社作成 |
| `updateCompany` | PUT | `/companies/{id}` | 会社更新 |
| `deleteCompany` | DELETE | `/companies/{id}` | 会社削除 |

### 3.5 Store API

| 関数名 | HTTPメソッド | エンドポイント | 説明 |
|--------|-------------|----------------|------|
| `getStores` | GET | `/stores` | 店舗一覧取得 |
| `getStore` | GET | `/stores/{id}` | 店舗詳細取得 |
| `createStore` | POST | `/stores` | 店舗作成 |
| `updateStore` | PUT | `/stores/{id}` | 店舗更新 |
| `deleteStore` | DELETE | `/stores/{id}` | 店舗削除 |

---

## 4. エラーハンドリング

### 4.1 共通エラー処理

| HTTPステータス | 処理 |
|----------------|------|
| 400 | バリデーションエラーをフォームに反映 |
| 401 | ログイン画面にリダイレクト |
| 403 | 権限エラーのトースト通知を表示 |
| 404 | リソースが見つからないエラーを表示 |
| 409 | 競合エラーのダイアログを表示 |
| 500 | サーバーエラーのトースト通知を表示 |

### 4.2 バリデーションエラー処理

| 項目 | 設計 |
|------|------|
| エラー形式 | バックエンドから返却されたエラーをフィールド単位でマッピング |
| 表示方法 | フォームフィールドの下にエラーメッセージを表示 |
| 色 | 赤色でエラー状態を視覚的に表現 |

### 4.3 ネットワークエラー処理

| エラー種別 | 処理 |
|------------|------|
| タイムアウト | リトライボタンを表示 |
| コネクションエラー | ネットワーク接続確認のメッセージを表示 |
| サーバーダウン | メンテナンス画面に切り替え |

---

## 5. ローディング状態管理

### 5.1 ローディング種別

| 種別 | 表示方法 | 使用場面 |
|------|----------|----------|
| ページローディング | スピナー（画面中央） | 画面遷移時 |
| テーブルローディング | スケルトンUI | 一覧取得時 |
| ボタンローディング | ボタン無効化 + スピナー | 送信時 |
| セクションローディング | ローディングバー | 詳細情報取得時 |

### 5.2 ローディング状態の管理

| 状態 | 説明 |
|------|------|
| `isLoading` | データ取得中 |
| `isError` | エラー発生 |
| `isSuccess` | 取得成功 |
| `isFetching` | バックグラウンドで再取得中 |

---

## 6. ページネーション連携

### 6.1 ページネーションパラメータ

| パラメータ | 型 | 説明 |
|------------|-----|------|
| `pageSize` | number | 1ページあたりの件数 |
| `pageNumber` | number | ページ番号（0から開始） |

### 6.2 ページネーションレスポンス

| フィールド | 型 | 説明 |
|------------|-----|------|
| `data` | array | データ一覧 |
| `metadata.pageSize` | number | 1ページあたりの件数 |
| `metadata.pageNumber` | number | 現在のページ番号 |
| `metadata.totalElements` | number | 総件数 |
| `metadata.totalPages` | number | 総ページ数 |

### 6.3 インフィニットスクロール

| 項目 | 設計 |
|------|------|
| 採用箇所 | 一覧画面（従業員一覧、組織一覧など） |
| トリガー | スクロール位置が画面下部に達した際 |
| バッファ | 100px前で次のページを取得開始 |

---

## 7. ソート連携

### 7.1 ソートパラメータ

| パラメータ | 型 | 説明 |
|------------|-----|------|
| `sort` | string | ソート条件（`fieldName,ASC` または `fieldName,DESC`） |

### 7.2 クライアント側ソート

| 項目 | 設計 |
|------|------|
| 採用箇所 | 小規模なデータ（100件未満） |
| 実装方法 | JavaScriptのArray.sort()を使用 |

### 7.3 サーバー側ソート

| 項目 | 設計 |
|------|------|
| 採用箇所 | 大規模なデータ（100件以上） |
| 実装方法 | APIパラメータでソート条件を指定 |

---

## 8. フィルター連携

### 8.1 フィルターパラメータ

| エンティティ | フィルター項目 | 説明 |
|--------------|---------------|------|
| Party | `type` | パーティー種類でフィルタ |
| Party | `status` | ステータスでフィルタ |
| Employee | `companyId` | 会社コードでフィルタ |
| Employee | `storeId` | 店舗コードでフィルタ |
| Employee | `status` | ステータスでフィルタ |
| Organization | `type` | 組織種類でフィルタ |
| Organization | `status` | ステータスでフィルタ |

### 8.2 フィルター状態の永続化

| 項目 | 設計 |
|------|------|
| 保存先 | sessionStorage |
| 保存タイミング | フィルター変更時 |
| 復元タイミング | 画面表示時 |

---

## 9. 最適化戦略

### 9.1 リクエスト最適化

| 戦略 | 説明 |
|------|------|
| デバウンス | 検索入力で0.5秒のデバウンスを適用 |
| スロットル | スクロールイベントで200msのスロットルを適用 |
| キャッシュ | React Queryのキャッシュを活用 |
| 条件付き取得 | 変更がない場合は再取得しない |

### 9.2 レスポンス最適化

| 戦略 | 説明 |
|------|------|
| フィールド選択 | 必要なフィールドのみをリクエスト |
| ページネーション | 大量データはページネーションで取得 |
| 圧縮 | Gzip圧縮を適用 |

---

## 10. 設計チェックリスト

設計時・実装時に以下の項目を確認してください。

- [ ] APIクライアントが適切に構成されている
- [ ] インターセプターが適切に設定されている
- [ ] エラーハンドリングが適切に実装されている
- [ ] ローディング状態が適切に管理されている
- [ ] ページネーションが適切に実装されている
- [ ] ソート機能が適切に実装されている
- [ ] フィルター機能が適切に実装されている
- [ ] 最適化戦略が適切に適用されている
- [ ] キャッシュ戦略が適切に設定されている
