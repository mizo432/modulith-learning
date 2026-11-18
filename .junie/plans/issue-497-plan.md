---
sessionId: session-261119-010014-1cya
---

# Requirements

### Overview & Goals

Issue #497の残作業（バックログ詳細画面での品質カテゴリ表示、Value入力横のリアルタイム表示、ツールチップなど）を計画する。一覧ページのChip表示は#495/PR#609で完了済みであり、本計画は**残範囲**に焦点を当てる。

### Scope

**In Scope:**
1. バックログ詳細ページ（BacklogItemDetail）に品質カテゴリバッジを表示
2. Valueスライダー横に品質カテゴリをリアルタイムで表示（入力中にカテゴリが切り替わる）
3. 品質カテゴリバッジにツールチップを追加（境界値を表示）
4. 一覧ページ（BacklogItemList）の品質カテゴリChipにツールチップを追加
5. アクセシビリティ対応（色＋ラベル併用、コントラスト比4.5:1以上、黄バッジは濃色文字、フォーカスでもツールチップ表示）
6. 対応テストの追加

**Out of Scope:**
- 品質カテゴリごとのフィルタリング機能（#617で未実装と決定）
- 日本語／英語表記の切り替え（#618で不要と決定）
- backend側の品質カテゴリ計算ロジック（#495で完了済み）
- 一覧ページの品質カテゴリ列自体（#495/PR#609で完了済み）

### 決定済み仕様（#495, #617, #618, #619に基づく）

| カテゴリ | コード | 範囲 | 日本語ラベル | バッジ色 |
|---|---|---|---|---|
| 魅力的品質 | ATTRACTIVE | 76〜100 | 魅力的品質 | 緑 |
| 当たり前品質 | MUST_BE | 51〜75 | 当たり前品質 | 黄 |
| 一元的品質 | ONE_DIMENSIONAL | 26〜50 | 一元的品質 | 青 |
| 無関心品質 | INDIFFERENT | 0〜25 | 無関心品質 | グレー |
| - | null | value=null | ― | なし |

- バッジ形式で日本語ラベルを常時表示（色だけに頼らない）
- ツールチップで境界値を表示（例：Value 76〜100）
- コントラスト比4.5:1以上。黄バッジは濃い文字
- ツールチップはホバーだけでなくフォーカスでも表示
- ツールチップには必須情報を置かず、ラベルだけで意味が分かる状態

### User Stories

- As a スクラムチームメンバー, I want 詳細ページで品質カテゴリをバッジで確認できるようにしたい, so that アイテムの価値を直感的に理解できる.
- As a PO/SM, I want Valueを入力しているときに品質カテゴリがリアルタイムで表示されるようにしたい, so that 設定するValueの意味を即座に把握できる.
- As a ユーザー, I want ツールチップで境界値を確認できるようにしたい, so that カテゴリの分類基準を理解できる.
- As a 色覚多様性のあるユーザー, I want 色だけでなくラベルでもカテゴリを識別できるようにしたい, so that 表示を正しく理解できる.

# Technical Design

### Validation Approach

フロントエンドのテスト（Jest + React Testing Library）で各機能を検証する。既存のテストパターン（`BacklogItemList.test.tsx`、`BacklogItemDetail.test.tsx`）に従う。

### Key Scenarios

#### valueColor.ts の品質カテゴリ関数
- Value 100 を渡したとき、ATTRACTIVE を返す
- Value 76 を渡したとき、ATTRACTIVE を返す
- Value 75 を渡したとき、MUST_BE を返す
- Value 51 を渡したとき、MUST_BE を返す
- Value 50 を渡したとき、ONE_DIMENSIONAL を返す
- Value 26 を渡したとき、ONE_DIMENSIONAL を返す
- Value 25 を渡したとき、INDIFFERENT を返す
- Value 0 を渡したとき、INDIFFERENT を返す
- Value null を渡したとき、null を返す
- ATTRACTIVE のラベルが「魅力的品質」である
- MUST_BE のラベルが「当たり前品質」である
- ONE_DIMENSIONAL のラベルが「一元的品質」である
- INDIFFERENT のラベルが「無関心品質」である
- ATTRACTIVE のツールチップに「76〜100」が含まれる
- MUST_BE のツールチップに「51〜75」が含まれる
- ONE_DIMENSIONAL のツールチップに「26〜50」が含まれる
- INDIFFERENT のツールチップに「0〜25」が含まれる

#### BacklogItemList の修正
- 魅力的品質のChipが緑色である
- 一元的品質のChipが青色である
- 当たり前品質のChipが黄色である
- 無関心品質のChipがグレーである
- 品質カテゴリChipにツールチップが表示される
- Valueがnullのアイテムでは「―」が表示される

#### BacklogItemDetail の品質カテゴリ表示
- 詳細ページのタイトル横に品質カテゴリバッジが表示される
- 品質カテゴリバッジにツールチップが表示される
- Valueスライダーの横にリアルタイム品質カテゴリが表示される
- Valueを76に変更したとき、リアルタイム表示が「魅力的品質」に切り替わる
- Valueを50に変更したとき、リアルタイム表示が「一元的品質」に切り替わる
- Valueがnullのとき、リアルタイム表示が「―」である
- Valueをスライダーで操作したとき、リアルタイム表示が追従する

### Edge Cases

- 境界値（25/26/50/51/75/76）で正しくカテゴリが切り替わる
- Valueがnull→値→nullへの遷移で表示が正しく更新される
- リアルタイム表示で入力中の値（未保存）が反映される
- ツールチップがフォーカスでも表示される

### Test Cases Summary

| テストファイル | 追加テスト数 | 概要 |
|---|---|---|
| `valueColor.test.ts` | 約15 | 品質カテゴリ分類・ラベル・ツールチップの境界値テスト |
| `BacklogItemList.test.tsx` | 約6 | 色の修正・ツールチップ・「―」表示 |
| `BacklogItemDetail.test.tsx` | 約8 | 品質カテゴリバッジ・リアルタイム表示・ツールチップ |

# Delivery Steps

###   Step 1: 1. valueColor.tsに品質カテゴリユーティリティを追加しテストを書く
`valueColor.ts`に品質カテゴリ関連の関数と色定数を追加し、テストを網羅する。
- `valueToQualityCategory(value: number | null)` を追加（backendのfromValueと同等：76以上=ATTRACTIVE、51以上=MUST_BE、26以上=ONE_DIMENSIONAL、0以上=INDIFFERENT、null=null）
- カテゴリごとの色定数を定義（背景色・文字色）。コントラスト比4.5:1以上を確保。黄バッジは濃色文字
- `qualityCategoryToLabel(category)` と `qualityCategoryToTooltip(category)` を追加
- `valueColor.test.ts` に境界値テスト（0, 25, 26, 50, 51, 75, 76, 100, null）とラベル・ツールチップテストを追加
- フロントエンドテストが通過することを確認

###   Step 2: 2. BacklogItemList.tsxの品質カテゴリ表示を修正しツールチップを追加
一覧ページの品質カテゴリChipを#617/#619の決定に合わせて修正。
- `QUALITY_CATEGORY_COLORS` を修正（ATTRACTIVE=緑、ONE_DIMENSIONAL=青、MUST_BE=黄、INDIFFERENT=グレー）。MUST_BEとONE_DIMENSIONALが逆、INDIFFERENTが赤→グレー
- 黄バッジ（MUST_BE）の文字色を濃色に（コントラスト比4.5:1以上）
- 品質カテゴリChipに`Tooltip`を追加（境界値表示、ホバーとフォーカスの両方で表示）
- Valueがnull（カテゴリなし）の場合は「―」を表示
- `BacklogItemList.test.tsx` にツールチップ・色の修正テストを追加
- テストが通過することを確認

###   Step 3: 3. BacklogItemDetail.tsxに品質カテゴリ表示とリアルタイム表示を追加
詳細ページに品質カテゴリバッジとValue入力横のリアルタイム表示を追加。
- タイトル横に品質カテゴリバッジを表示（`item.qualityCategory`から）
- ValueスライダーとTextFieldの間にリアルタイム品質カテゴリバッジを配置（`valueInput`から`valueToQualityCategory()`で計算）
- バッジにツールチップを追加（境界値、ホバーとフォーカスで表示）
- Valueがnullのときは「―」を表示
- `BacklogItemDetail.test.tsx` に品質カテゴリ表示・リアルタイム表示・ツールチップのテストを追加
- テストが通過することを確認