---
name: wiki-to-RADRA-model
description: >
  GitHub Wiki に記述された要件を分析し、Frontend（フロントエンド）と Backend（バックエンド）の
  実装作業に分解して関連する GitHub Issues を作成するスキル。
  Wiki 要件を実行可能な GitHub Issues に変換する際に使用します。
---

### Wiki to RADRA Model スキル (`wiki-to-RADRA-model`)

このスキルは、GitHub Wiki（またはプロジェクト内の `src/doc/` 配下のドキュメント）に記述された
システム要件・機能仕様を分析し、Frontend と Backend の実装作業に分解して、
関連する GitHub Issues を作成するためのガイドラインと手順です。

**RADRA** は以下の 5 つのフェーズから成ります：

| フェーズ                      | 略称 | 説明                                         |
|-------------------------------|------|----------------------------------------------|
| **R**equirement (要件分析)    | R    | Wiki から要件を読み取り、ビジネス能力を特定  |
| **A**cceptance (受け入れ基準) | A    | 各要件の受け入れ基準とユーザーストーリを定義 |
| **D**ecomposition (分解)      | D    | Frontend / Backend に実装作業を分解          |
| **R**eview (レビュー・確認)   | R    | API 契約を定義し、既存 Issue と重複チェック  |
| **A**pproval (承認・作成)     | A    | ユーザー承認後に GitHub Issues を作成        |

---

### 1. いつこのスキルを使用するか (When To Use)

- GitHub Wiki や `src/doc/` 配下のドキュメントに記述された要件を GitHub Issues に変換するとき
- 新機能の要件を Frontend と Backend の実装タスクに分解するとき
- 要件定義ドキュメントから実行可能な開発タスクを生成するとき
- API 契約（API Contract）を Frontend / Backend の双方で合意するとき
- 既存の GitHub Issues との重複を確認しながら新しい Issue を作成するとき

---

### 2. RADRA ワークフロー (Overall Workflow)

このワークフローを厳格に従ってください。

```text
GitHub Wiki / src/doc/
    |
    v
[R] Wiki の読み取り
    |
    v
[R] 要件分析
    |
    v
[A] 受け入れ基準 / ユーザーストーリー定義
    |
    v
[D] 実装作業の分解
    |
    +----------------------+
    |                      |
    v                      v
Frontend Issue        Backend Issue
    |                      |
    +----------+-----------+
               |
               v
         [D] API Contract 定義
               |
               v
      [R] 既存 Issue の重複チェック
               |
               v
       [R] Issue 作成計画の提示
               |
               v
       [A] ユーザーの明示的な承認
               |
               v
     [A] GitHub Issues の作成
               |
               v
       [A] 関連 Issue のリンク
               |
               v
       [A] 作成結果の報告
```

---

### 3. 各フェーズの詳細手順 (Detailed Steps)

#### 3.1 Requirement — 要件分析 (R)

1. **Wiki ドキュメントの読み取り**:
    - 対象となる Wiki ページまたは `src/doc/` 配下の Markdown ファイルを読み取る。
    - 要件の階層構造（Epic → Feature → User Story）を把握する。
    - 関連するドキュメント（現行システム分析、要求分析、要件定義、ドメイン駆動設計等）を参照する。

2. **ビジネス能力の特定**:
    - 要件がどのビジネスドメイン（例: 顧客管理、売上管理、部門管理）に属するかを特定する。
    - 関連するモジュール（Spring Modulith モジュール）を特定する。

3. **要件の整理**:
    - 機能要件と非機能要件を分離する。
    - 依存関係がある要件を特定し、実装順序を考慮する。

##### チェックリスト

詳細な確認項目は [checklists/requirement.md](checklists/requirement.md) を参照してください。

---

#### 3.2 Acceptance — 受け入れ基準定義 (A)

1. **ユーザーストーリーの作成**:
    - 「〜したい 〜の役割として、〜のような 機能を実現したい」という形式でユーザーストーリーを記述する。
    - 各ユーザーストーリーに優先度（Must / Should / Could / Won't）を付与する。

2. **受け入れ基準の定義**:
    - 各ユーザーストーリーに対して、明確で検証可能な受け入れ基準を定義する。
    - 正常系と異常系の両方をカバーする。
    - 境界値やエッジケースを含める。

---

#### 3.3 Decomposition — 実装作業の分解 (D)

1. **Frontend 作業の特定**:
    - UI コンポーネントの作成・修正
    - 画面遷移・ルーティング
    - フロントエンドバリデーション
    - API 呼び出しの実装
    - ステート管理

2. **Backend 作業の特定**:
    - ドメインモデルの設計・実装（Entity, Value Object, Repository）
    - ビジネスロジックの実装（Service 層）
    - REST API エンドポイントの実装（Controller 層）
    - データベースマイグレーション
    - テストの実装（Small / Medium / Large）

3. **API Contract の定義**:
    - Frontend と Backend の境界となる API を明確に定義する。
    - リソース URI、HTTP メソッド、リクエスト/レスポンス形式、ステータスコードを指定する。
    - API 契約は [templates/api-contract.md](templates/api-contract.md) のテンプレートを使用する。

##### チェックリスト

詳細な確認項目は [checklists/api.md](checklists/api.md) を参照してください。

---

#### 3.4 Review — レビュー・確認 (R)

1. **既存 Issue の重複チェック**:
    - GitHub 上に同様の Issue が既に存在しないか確認する。
    - 既存 Issue を拡張する場合は、既存 Issue を参照・リンクする。

2. **Issue 作成計画の提示**:
    - 作成予定の Issue 一覧をユーザーに提示する。
    - 各 Issue のタイトル、概要、ラベル、関連 Issue を明示する。
    - Frontend Issue と Backend Issue のペア関係を明確にする。

##### チェックリスト

詳細な確認項目は [checklists/issue-creation.md](checklists/issue-creation.md) を参照してください。

---

#### 3.5 Approval — 承認・作成 (A)

1. **ユーザーの明示的な承認**:
    - Issue 作成計画をユーザーに提示し、明示的な承認を得る。
    - 承認なしに GitHub Issues を作成してはならない。

2. **GitHub Issues の作成**:
    - 承認後、GitHub MCP を使用して Issues を作成する。
    - Issue テンプレート ([templates/frontend-issue.md](templates/frontend-issue.md),
      [templates/backend-issue.md](templates/backend-issue.md)) を使用する。

3. **関連 Issue のリンク**:
    - 作成した Frontend Issue と Backend Issue を相互にリンクする。
    - 元の Wiki ページへの参照リンクを Issue に含める。

4. **作成結果の報告**:
    - 作成した Issue の一覧と URL をユーザーに報告する。
    - 関連関係と実装順序の推奨を記載する。

---

### 4. 重要ルール (Important Rules)

1. **Wiki は変更しない**:
    - Wiki ドキュメントは読み取り専用として扱い、一切変更しない。

2. **明示的な承認が必要**:
    - GitHub Issues の作成前に、必ずユーザーの明示的な承認を得る。

3. **Frontend / Backend の分離**:
    - 各要件を Frontend と Backend の 2 つの Issue に分解する。
    - 両者の関係を API Contract で明確にする。

4. **既存 Issue の尊重**:
    - 既存の Issue を無視せず、重複チェックと適切なリンクを行う。

5. **テンプレートの使用**:
    - Issue 作成時には必ず定義されたテンプレートを使用する。

---

### 5. 付属テンプレートとチェックリスト (Resources)

- **テンプレート**:
    - [api-contract.md](templates/api-contract.md): API 契約の定義テンプレート
    - [frontend-issue.md](templates/frontend-issue.md): Frontend Issue のテンプレート
    - [backend-issue.md](templates/backend-issue.md): Backend Issue のテンプレート
- **チェックリスト**:
    - [requirement.md](checklists/requirement.md): 要件分析時の確認項目
    - [api.md](checklists/api.md): API 契約定義時の確認項目
    - [issue-creation.md](checklists/issue-creation.md): Issue 作成時の確認項目
