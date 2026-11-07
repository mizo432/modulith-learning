# フェーズ5: システム概要を定義する

**目的**: 初期要望からシステム名とシステム概要を抽出し、システム全体の概要を定義する。

---

**ナビゲーション**: [概要](./rdra-overview.guidelines.md) | [ドキュメント配置](./rdra-document-structure.guidelines.md) | [フェーズ1](./rdra-phase1.guidelines.md) | [フェーズ2](./rdra-phase2.guidelines.md) | [フェーズ3](./rdra-phase3.guidelines.md) | [フェーズ4](./rdra-phase4.guidelines.md) | **[フェーズ5]** | [フェーズ6](./rdra-phase6.guidelines.md) | [フェーズ7](./rdra-phase7.guidelines.md) | [要件組み立て](./rdra-requirement-assembly.guidelines.md) | [整合性確認](./rdra-consistency-check.guidelines.md) | [チェックリスト](./rdra-checklist.guidelines.md)

---

### 2.5 フェーズ5: システム概要を定義する

**目的**: 初期要望からシステム名とシステム概要を抽出し、システム全体の概要を定義する。

**対応レイヤー**: システム価値レイヤーの最上位定義に相当します。

フェーズ5では、初期要望からシステムの名前と概要を抽出し、JSON形式で出力します。これはRDRAの他のフェーズが要素中心の整理であるのに対し、システム全体の概要を定義するステップです。

#### システム名の抽出

- 「初期要望.txt」の内容から自然な日本語のシステム名を1つ抽出・決定する
- システムの目的や対象業務を反映した適切な名前とする

#### システム概要の生成

- 「初期要望.txt」の内容を要約し、システム概要を生成する
- 具体性を保ちつつ簡潔にまとめる（長すぎない文章量）
- システムが解決する課題や提供する価値を明確にする

#### 出力形式

- 出力先: `1_RDRA/システム概要.json`
- 形式: `{"system_name":"システム名","system_overview":"システム概要"}`
- JSONのみを出力し、既存ファイルがある場合は上書きする

