# TMフェーズ4: 応用例

**目的**: 実際のプロジェクト管理システムのERモデル例を通じて、TMモデルの実践的な適用方法を解説する。

---

**ナビゲーション**: [概要](./rdra-overview.guidelines.md) | [TM概要](./rdra-tm-overview.guidelines.md) | [TMフェーズ1](./rdra-tm-phase1.guidelines.md) | [TMフェーズ2](./rdra-tm-phase2.guidelines.md) | [TMフェーズ3](./rdra-tm-phase3.guidelines.md) | **[TMフェーズ4]** | [TMフェーズ5](./rdra-tm-phase5.guidelines.md)

---

## 応用例

プロジェクト管理システムの例を通じて、ERモデルの実践的な適用方法を解説します。

### プロジェクト管理システムの例

| エンティティ | 分類 | 主キー | 主要属性 |
|--------------|------|--------|----------|
| プロジェクト | R | プロジェクトID | 名称、開始日、終了日 |
| チーム | R | チームID | 名称、責任者 |
| メンバー | R | メンバーID | 氏名、役割、スキル |
| スプリント | E | スプリントID | 開始日、終了日、目標 |
| タスク | E | タスクID | 説明、見積時間、状態 |

### 関係の例

- プロジェクト → チーム（1対多）
- チーム → メンバー（1対多）
- プロジェクト → スプリント（1対多）
- スプリント → タスク（1対多）
- タスク → メンバー（多対1）

### PlantUMLでの表現例

```plantuml
@startuml
entity "プロジェクト" as Project {
  * id : INTEGER <<not null>>
  --
  name : VARCHAR
  startDate : DATE
  endDate : DATE
}

entity "チーム" as Team {
  * id : INTEGER <<not null>>
  --
  name : VARCHAR
  leaderId : INTEGER
}

entity "メンバー" as Member {
  * id : INTEGER <<not null>>
  --
  name : VARCHAR
  role : VARCHAR
  skill : VARCHAR
}

entity "スプリント" as Sprint {
  * id : INTEGER <<not null>>
  --
  startDate : DATE
  endDate : DATE
  goal : VARCHAR
}

entity "タスク" as Task {
  * id : INTEGER <<not null>>
  --
  description : VARCHAR
  estimatedHours : INTEGER
  status : VARCHAR
}

Project o-- * Team
Team o-- * Member
Project o-- * Sprint
Sprint o-- * Task
Task }--o Member
@enduml
```

### モデリングのポイント

1. **エンティティの分類**: リソースとイベントを明確に区別
2. **関係の定義**: 多重度を正しく設定
3. **属性の割り当て**: 主キーと一般属性を区別
4. **視覚化**: PlantUMLでER図を作成

---

## まとめ

実際のプロジェクト例を通じて、ERモデルの適用方法を理解しました。このパターンを参考に、自身のプロジェクトに合わせたERモデルを構築してください。
