# Issue 作成チェックリスト (Issue Creation Checklist)

## 既存 Issue の重複チェック

- [ ] GitHub 上に同様の Issue が既に存在しないか確認したか
- [ ] 既存 Issue を拡張する場合は、既存 Issue を参照・リンクする計画があるか
- [ ] 関連する Epic / Feature Issue が存在するか確認したか

## Issue 作成計画の提示

- [ ] 作成予定の Issue 一覧をユーザーに提示したか
- [ ] 各 Issue のタイトルが明確で簡潔か
    - 例: `[Frontend] 顧客一覧画面の実装`, `[Backend] 顧客CRUD APIの実装`
- [ ] 各 Issue の概要（説明）が要件を正確に反映しているか
- [ ] ラベル（`frontend`, `backend`, `api-contract`, `feature`, `bug` 等）を適切に設定したか
- [ ] Frontend Issue と Backend Issue のペア関係を明確にしたか

## Issue の内容

- [ ] 要件の参照（Wiki ページへのリンク）を含めたか
- [ ] 受け入れ基準（Acceptance Criteria）を記載したか
- [ ] 関連する API Contract の参照を含めたか
- [ ] 優先度（Priority）を設定したか
- [ ] 関連 Issue のリンク（`relates to`, `blocks`, `is blocked by`）を設定したか

## Backend Issue 固有

- [ ] 関連する Spring Modulith モジュールを特定したか
- [ ] ドメインモデルの変更点を記載したか
- [ ] テストの範囲（Small / Medium / Large）を記載したか
- [ ] データベースマイグレーションが必要な場合、それを記載したか

## Frontend Issue 固有

- [ ] 対象となる画面・コンポーネントを特定したか
- [ ] UI/UX の設計参照（あれば）を含めたか
- [ ] API 呼び出しの詳細を記載したか
- [ ] ステート管理の変更点を記載したか

## 承認

- [ ] Issue 作成計画をユーザーに提示し、明示的な承認を得たか
- [ ] 承認なしに Issue を作成していないか
