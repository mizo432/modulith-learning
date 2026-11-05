# フェーズ7: 画面照会を生成する

**目的**: 画面一覧.jsonにデータアクセス情報を追記し、BUC画面.jsonとアクター画面.jsonとまとめて画面照会.jsonを生成する。

---

**ナビゲーション**: [概要](./rdra-overview.guidelines.md) | [ドキュメント配置](./rdra-document-structure.guidelines.md) | [フェーズ1](./rdra-phase1.guidelines.md) | [フェーズ2](./rdra-phase2.guidelines.md) | [フェーズ3](./rdra-phase3.guidelines.md) | [フェーズ4](./rdra-phase4.guidelines.md) | [フェーズ5](./rdra-phase5.guidelines.md) | [フェーズ6](./rdra-phase6.guidelines.md) | **[フェーズ7]** | [要件組み立て](./rdra-requirement-assembly.guidelines.md) | [整合性確認](./rdra-consistency-check.guidelines.md) | [チェックリスト](./rdra-checklist.guidelines.md)

---

### 2.7 フェーズ7: 画面照会を生成する

**目的**: 画面一覧.jsonにデータアクセス情報を追記し、BUC画面.jsonとアクター画面.jsonとまとめて画面照会.jsonを生成する。

**対応レイヤー**: 画面と論理データの関係性を定義し、UI定義を完成させる。

フェーズ7では、フェーズ6で生成した画面一覧.json、BUC画面.json、アクター画面.jsonを統合し、画面が操作する論理データへのアクセス情報を追記して画面照会.jsonを作成します。

#### 画面照会の生成

画面一覧.jsonの「screens」にデータアクセス「data_access」を追記し、BUC画面.jsonの「businesses」とアクター画面.jsonの「actorGroups」とまとめて出力します。

**データアクセス（data_access）の追加**

- 画面の機能に必要なentityを「論理データモデル.md」から洗い出す
- 画面に必要な項目を満たす項目を持つ論理データ(entity)から関係を洗い出す
- 画面とEntityの関係は以下の関係データを頼りに類推する
  - 画面 → UC → 情報
  - 情報と論理データの関係を類推する

**データアクセスの構造**

関係するentityとの関係をC,R,U,Dで示し、複数設定可能です。

| CRUD | 説明                           |
|------|--------------------------------|
| C    | 登録、追加などを表す           |
| R    | 参照、表示などを表す           |
| U    | 更新、変更などを表す           |
| D    | 削除を表す                     |

- isMain: true(主として操作するEntity) / false(主Entityから参照されるEntity)
- 一つの画面に主となるEntityは一つ

**出力フォーマット**

```json
{
  "businesses":[{
    "business_name":"業務名",
    "BUCs":[{
        "buc_name":"BUC名",
        "actors":[{
            "actor_name":"アクター名",
            "screen_names":["画面名"]
        }]
    }]
  }],
  "actorGroups":[{
    "group_name":"アクター群",
    "actors":[{
      "actor_name": "アクター名",
      "screen_names": ["画面名"]
    }]
  }],
  "screens":[{
    "screen_name": "画面名",
    "fields": [{"field_name": "項目名",
        "type": "string|number|バリエーション",
        "tooltip":"項目の説明"
    }],
    "screen_datas": [{
        "display_name":"表示用のデータ",
        "dummy_data":[{"field_name": "項目名","value": "ダミーデータ"}]
    }],
    "operation":"機能名",
    "data_access":[{"entity":entity名,"CRUD":["C","R","U","D"],"isMain":true}]
  }]
}
```

**出力ルール**

- 出力ファイル: 「2_RDRASpec/画面照会.json」
- 関連データ.txtの「画面-アクター」定義を全て網羅する
- 同一画面に複数アクターがいる場合、全てのアクターを出力する
- businesses内のscreensが全ての画面を網羅していることを確認する

