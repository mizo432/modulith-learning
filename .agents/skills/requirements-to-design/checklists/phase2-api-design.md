# フェーズ2: API設計 チェックリスト

**目的**: 要件定義のUCと画面からREST API設計を導出する。CQRSに基づきCommand Service（書き込み）とQuery Service（読み込み）を分離する。

**参照スキル**: [api-design](../api-design/SKILL.md)

---

## 出力ディレクトリの準備

- [ ] 出力先ディレクトリを特定する
  - `docs/23 詳細設計書/[事業領域コード]_[事業領域名]/backend/api/`
- [ ] `index.md` を作成する

---

## UCからCommand/Queryを分類

- [ ] ユースケース一覧を読み込む
- [ ] 画面経由のUCを特定する
  - UC一覧の「画面」列が空白でないUCを抽出
- [ ] 各UCをCommand（書き込み）とQuery（読み込み）に分類する
  - **Query**: データの取得・検索・一覧表示（GETメソッド）
  - **Command**: データの作成・更新・削除・状態遷移（POST/PUT/PATCH/DELETEメソッド）

---

## Command Service の設計

**書き込み操作を担当するサービス**

- [ ] Command Service の命名を定義する
  - `[Entity]CommandService`（例: `NotificationCommandService`）
- [ ] Commandオブジェクトを定義する
  - 不変の値オブジェクト（`final` クラス + `final` フィールド）
  - 命名: `[Action][Entity]Command`（例: `CreateNotificationCommand`, `MarkAsReadCommand`）
  - バリデーションアノテーションを付与
- [ ] Command Service のメソッドを定義する
  - 作成: `create[Entity](Command)` → 作成されたエンティティのIDを返す
  - 更新: `update[Entity](Command)` → `void` またはステータスを返す
  - 削除: `delete[Entity](Command)` → `void` またはステータスを返す
  - 状態遷移: `[action][Entity](Command)` → `void` またはステータスを返す
- [ ] Command Controller を定義する
  - 命名: `[Entity]CommandController`（例: `NotificationCommandController`）
  - HTTPメソッド: POST, PUT, PATCH, DELETE

---

## Query Service の設計

**読み込み操作を担当するサービス**

- [ ] Query Service の命名を定義する
  - `[Entity]QueryService`（例: `NotificationQueryService`）
- [ ] Queryオブジェクトを定義する
  - 不変の値オブジェクト（`final` クラス + `final` フィールド）
  - 命名: `[Action][Entity]Query`（例: `GetNotificationQuery`, `ListNotificationsQuery`）
  - 検索条件・ページネーションパラメータを含む
- [ ] Query Service のメソッドを定義する
  - 単一取得: `get[Entity](Query)` → `Optional<[Entity]Dto>` または `[Entity]Dto`
  - 一覧取得: `list[Entity](Query)` → `Page<[Entity]Dto>` または `List<[Entity]Dto>`
  - 検索: `search[Entity](Query)` → `Page<[Entity]Dto>`
  - カウント: `count[Entity](Query)` → `long`
- [ ] Query Controller を定義する
  - 命名: `[Entity]QueryController`（例: `NotificationQueryController`）
  - HTTPメソッド: GET

---

## APIエンドポイントの定義

- [ ] リソース名を定義する
  - 情報名から名詞の複数形に変換 (例: 通知 → notifications)
- [ ] URLパスを定義する
  - `/api/v1/[リソース名]` の形式
- [ ] HTTPメソッドをCommand/Queryに応じて定義する

| 操作 | HTTPメソッド | エンドポイント | Service | Command/Query |
|:---|:---|:---|:---|:---|
| 一覧取得 | GET | `/api/v1/[resources]` | QueryService | Query |
| 単一取得 | GET | `/api/v1/[resources]/{id}` | QueryService | Query |
| 作成 | POST | `/api/v1/[resources]` | CommandService | Command |
| 全体更新 | PUT | `/api/v1/[resources]/{id}` | CommandService | Command |
| 部分更新 | PATCH | `/api/v1/[resources]/{id}` | CommandService | Command |
| 削除 | DELETE | `/api/v1/[resources]/{id}` | CommandService | Command |
| 状態遷移 | PATCH | `/api/v1/[resources]/{id}/[action]` | CommandService | Command |

- [ ] ステータスコードを定義する
  - 200 (成功), 201 (作成), 204 (No Content), 400 (クライアントエラー), 404 (NotFound), 500 (サーバーエラー)

---

## リクエスト・レスポンスDTOの定義

- [ ] 画面属性一覧 (`SCR-[ID]_画面属性一覧.md`) を読み込む
- [ ] CommandのリクエストDTOを定義する
  - 入力項目をフィールドとして定義
  - バリデーションアノテーションを付与 (`@NotNull`, `@Size`, `@Email` など)
- [ ] QueryのレスポンスDTOを定義する
  - 表示項目をフィールドとして定義
  - 機密情報を除外
- [ ] ページネーションメタデータを定義する (一覧系APIの場合)
  - `pageSize`, `pageNumber`, `totalElements`, `totalPages`

---

## バリデーションルールの定義

- [ ] 情報属性の制約からバリデーションルールを導出する
  - 必須項目 → `@NotNull`
  - 最大文字数 → `@Size(max = N)`
  - 列挙型 → カスタムバリデーター
- [ ] 条件一覧からビジネスルールを導出する
- [ ] バリデーションエラーレスポンスを定義する

---

## エラーレスポンスの定義

- [ ] 一貫したエラーレスポンス形式を定義する
- [ ] ビジネスロジックに応じたエラーコードを定義する
- [ ] 発生しうるエラーケースを列挙する

---

## API設計書の出力

- [ ] 各APIの設計書を `backend/api/[API名].md` に出力する
- [ ] 以下の項目を含める:
  - エンドポイントの概要
  - URLパス・HTTPメソッド
  - Command/Queryの分類
  - 対応するService（CommandService / QueryService）
  - リクエストボディ・クエリパラメータ
  - レスポンスボディ
  - ステータスコード
  - エラーレスポンス
  - バリデーションルール

---

## 出力チェック

- [ ] 全画面経由のUCがAPIとして定義されているか確認する
- [ ] Command ServiceとQuery Serviceが適切に分離されているか確認する
- [ ] Command/Queryオブジェクトが不変の値オブジェクトとして定義されているか確認する
- [ ] APIの命名規則が一貫しているか確認する
- [ ] リクエスト・レスポンスDTOが定義されているか確認する
- [ ] バリデーションルールが定義されているか確認する
- [ ] `api-design` スキルのCQRSガイドラインに従っているか確認する
