# DDDモデル候補

## 概要

本ファイルは、EF-02 帳票管理のDDD（Domain-Driven Design）モデル候補を定義します。

## サブドメイン候補

| 項番 | サブドメイン名 | 種類 | 説明 |
|------|---------------|------|------|
| 01 | 帳票定義管理 | コア | 帳票の種類・形式・レイアウトの定義管理 |
| 02 | 帳票テンプレート管理 | コア | 帳票生成に使用するテンプレートの管理 |
| 03 | 帳票生成設定 | コア | 帳票生成時のパラメータ設定管理 |
| 04 | 帳票情報管理 | コア | 生成された帳票のメタデータ管理 |
| 05 | 帳票バージョン管理 | 支援 | 帳票のバージョン履歴管理 |

## アグリゲート候補

| 項番 | アグリゲート名 | 集約ルート | 説明 |
|------|---------------|-----------|------|
| 01 | FormDefinition | FormDefinition | 帳票定義の集約 |
| 02 | FormTemplate | FormTemplate | 帳票テンプレートの集約 |
| 03 | FormGenerationSetting | FormGenerationSetting | 帳票生成設定の集約 |
| 04 | FormInfo | FormInfo | 帳票情報の集約 |
| 05 | FormVersion | FormVersion | 帳票バージョンの集約 |

## エンティティ候補

| 項番 | エンティティ名 | アグリゲート | 説明 |
|------|---------------|-------------|------|
| 01 | FormDefinition | FormDefinition | 帳票定義 |
| 02 | FormTemplate | FormTemplate | 帳票テンプレート |
| 03 | FormGenerationSetting | FormGenerationSetting | 帳票生成設定 |
| 04 | FormInfo | FormInfo | 帳票情報 |
| 05 | FormVersion | FormVersion | 帳票バージョン |

## 値オブジェクト候補

| 項番 | 値オブジェクト名 | 使用箇所 | 説明 |
|------|-----------------|---------|------|
| 01 | FormDefId | FormDefinition | 帳票定義識別子 |
| 02 | FormType | FormDefinition | 帳票種類 |
| 03 | FormName | FormDefinition | 帳票名 |
| 04 | LayoutConfig | FormDefinition | レイアウト設定 |
| 05 | TemplateId | FormTemplate | テンプレート識別子 |
| 06 | TemplateName | FormTemplate | テンプレート名 |
| 07 | FilePath | FormTemplate | ファイルパス |
| 08 | FileFormat | FormTemplate | ファイル形式 |
| 09 | OutputFormat | FormGenerationSetting | 出力形式 |
| 10 | DestinationType | FormGenerationSetting | 宛先種類 |
| 11 | FormId | FormInfo | 帳票識別子 |
| 12 | FormTitle | FormInfo | 帳票タイトル |
| 13 | StoragePath | FormInfo | ストレージパス |
| 14 | VersionId | FormVersion | バージョン識別子 |
| 15 | VersionNumber | FormVersion | バージョン番号 |
| 16 | ChangeDescription | FormVersion | 変更説明 |

## ドメインサービス候補

| 項番 | サービス名 | 説明 |
|------|-----------|------|
| 01 | FormGenerationService | 帳票生成のロジック |
| 02 | TemplateValidationService | テンプレート検証ロジック |
| 03 | VersionManagementService | バージョン管理ロジック |

## ドメインイベント候補

| 項番 | イベント名 | 発生元 | 説明 |
|------|-----------|--------|------|
| 01 | FormDefinitionRegistered | FormDefinition | 帳票定義が登録された |
| 02 | FormGenerated | FormInfo | 帳票が生成された |
| 03 | FormVersionCreated | FormVersion | 帳票バージョンが作成された |
