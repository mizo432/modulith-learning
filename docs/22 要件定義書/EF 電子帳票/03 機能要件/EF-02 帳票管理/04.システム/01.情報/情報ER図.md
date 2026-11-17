# 情報ER図

## 概要

本ファイルは、EF-02 帳票管理の情報エンティティ間の関係をER図で定義します。

## ER図（英語版）

```plantuml
@startuml
skinparam linetype ortho

entity "FormDefinition\nINF-EF-10" as FormDefinition {
  * form_def_id : String
  * form_type : String
  * form_name : String
  * system_id : String
  * layout_config : String
  * status : String
  --
  created_at : LocalDateTime
  updated_at : LocalDateTime
}

entity "FormTemplate\nINF-EF-11" as FormTemplate {
  * template_id : String
  * form_def_id : String
  * template_name : String
  * template_file_path : String
  * file_format : String
  * version : String
  --
  created_at : LocalDateTime
  updated_at : LocalDateTime
}

entity "FormGenerationSetting\nINF-EF-12" as FormGenerationSetting {
  * setting_id : String
  * form_def_id : String
  * output_format : String
  * destination_type : String
  * destination_config : String
  --
  created_at : LocalDateTime
  updated_at : LocalDateTime
}

entity "FormInfo\nINF-EF-13" as FormInfo {
  * form_id : String
  * form_def_id : String
  * system_id : String
  * form_title : String
  * creator_id : String
  * created_at : LocalDateTime
  * storage_path : String
  * status : String
  --
  updated_at : LocalDateTime
}

entity "FormVersion\nINF-EF-14" as FormVersion {
  * version_id : String
  * form_id : String
  * version_number : Integer
  * change_description : String
  * changed_by : String
  * storage_path : String
  --
  created_at : LocalDateTime
}

FormDefinition "1" -- "0..*" FormTemplate : has >
FormDefinition "1" -- "0..*" FormGenerationSetting : has >
FormDefinition "1" -- "0..*" FormInfo : generates >
FormInfo "1" -- "0..*" FormVersion : has >

@enduml
```

## ER図（日本語版）

```plantuml
@startuml
skinparam linetype ortho

entity "帳票定義\nINF-EF-10" as FormDefinition {
  * 帳票定義ID : String
  * 帳票種類 : String
  * 帳票名 : String
  * システムID : String
  * レイアウト設定 : String
  * 状態 : String
  --
  作成日時 : LocalDateTime
  更新日時 : LocalDateTime
}

entity "帳票テンプレート\nINF-EF-11" as FormTemplate {
  * テンプレートID : String
  * 帳票定義ID : String
  * テンプレート名 : String
  * テンプレートファイルパス : String
  * ファイル形式 : String
  * バージョン : String
  --
  作成日時 : LocalDateTime
  更新日時 : LocalDateTime
}

entity "帳票生成設定\nINF-EF-12" as FormGenerationSetting {
  * 設定ID : String
  * 帳票定義ID : String
  * 出力形式 : String
  * 宛先種類 : String
  * 宛先設定 : String
  --
  作成日時 : LocalDateTime
  更新日時 : LocalDateTime
}

entity "帳票情報\nINF-EF-13" as FormInfo {
  * 帳票ID : String
  * 帳票定義ID : String
  * システムID : String
  * 帳票タイトル : String
  * 作成者ID : String
  * 作成日時 : LocalDateTime
  * ストレージパス : String
  * 状態 : String
  --
  更新日時 : LocalDateTime
}

entity "帳票バージョン\nINF-EF-14" as FormVersion {
  * バージョンID : String
  * 帳票ID : String
  * バージョン番号 : Integer
  * 変更説明 : String
  * 変更者 : String
  * ストレージパス : String
  --
  作成日時 : LocalDateTime
}

FormDefinition "1" -- "0..*" FormTemplate : 保有 >
FormDefinition "1" -- "0..*" FormGenerationSetting : 保有 >
FormDefinition "1" -- "0..*" FormInfo : 生成 >
FormInfo "1" -- "0..*" FormVersion : 保有 >

@enduml
```

## 情報エンティティ一覧

| 情報ID | 情報名 | 主要属性 | 関連エンティティ |
|--------|--------|----------|-----------------|
| INF-EF-10 | 帳票定義 | form_def_id, form_type, form_name, system_id | 帳票テンプレート, 帳票生成設定, 帳票情報 |
| INF-EF-11 | 帳票テンプレート | template_id, form_def_id, template_name, file_format | 帳票定義 |
| INF-EF-12 | 帳票生成設定 | setting_id, form_def_id, output_format, destination_type | 帳票定義 |
| INF-EF-13 | 帳票情報 | form_id, form_def_id, system_id, form_title, storage_path | 帳票定義, 帳票バージョン |
| INF-EF-14 | 帳票バージョン | version_id, form_id, version_number, change_description | 帳票情報 |

## 関係一覧

| 源エンティティ | 関係 | 対象エンティティ | 多重度 | 説明 |
|---------------|------|-----------------|--------|------|
| 帳票定義 | 保有 | 帳票テンプレート | 1 : 0..* | 1つの帳票定義に複数のテンプレートが存在 |
| 帳票定義 | 保有 | 帳票生成設定 | 1 : 0..* | 1つの帳票定義に複数の生成設定が存在 |
| 帳票定義 | 生成 | 帳票情報 | 1 : 0..* | 1つの帳票定義から複数の帳票が生成 |
| 帳票情報 | 保有 | 帳票バージョン | 1 : 0..* | 1つの帳票に複数のバージョンが存在 |
