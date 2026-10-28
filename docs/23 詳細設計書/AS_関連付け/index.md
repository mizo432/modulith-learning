# AS_関連付け 外部設計

## 概要

関連付け事業領域の外部設計資材を格納します。

## ディレクトリ構成

```
AS_関連付け/
├── index.md                    # このファイル
├── 01 データベース設計/        # データベース設計
│   └── index.md
├── 02 詳細設計書構成案.md      # 詳細設計書構成案
├── Party パーティー/           # Partyパーティー詳細設計
│   ├── backend/
│   │   ├── api/
│   │   │   └── index.md
│   │   └── batch/
│   │       └── index.md
│   └── frontend/
│       └── index.md
├── frontend/                   # フロントエンド外部設計
│   └── index.md
└── backend/
    ├── api/                    # API外部設計
    │   ├── index.md            # APIアーキテクチャ、セキュリティ、ページネーションなど
    │   └── ユースケース記述/     # ユースケースごとのAPI記述
    │       └── index.md
    └── batch/                  # バッチ外部設計
        └── index.md
```

## 参照

- 事業領域一覧: `docs/11 事業領域/事業領域一覧.tsv`
- 業務領域一覧: `docs/12 業務領域/業務領域一覧.tsv`
- 詳細設計書構成案: [02 詳細設計書構成案.md](02%20詳細設計書構成案.md)
- API外部設計: [backend/api/index.md](backend/api/index.md)
