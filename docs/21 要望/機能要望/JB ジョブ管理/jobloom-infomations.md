# Jobloom：管理情報とデータ項目一覧

Jobloom の画面・ユースケース・システム構成をもとに、管理すべき情報と各情報の項目を整理します。

設計方針は、ジョブ定義、実行履歴、実行先サーバー、会社・店舗、通知、監査情報を分離し、実行 ID
を中心に関連付けることです。実際の業務データやファイル本体は Jobloom では管理せず、業務システムや S3 /
MinIO 側に保持します。

## 1. 管理情報の全体分類

| 分類               | 管理情報                                 | 主な目的                       |
|--------------------|------------------------------------------|--------------------------------|
| ① ジョブ管理       | ジョブ定義、パラメータ仕様、バージョン   | 実行するジョブの定義           |
| ② 実行管理         | 起動要求、実行状態、実行結果、実行履歴   | ジョブのライフサイクル管理     |
| ③ 実行制御         | 同時実行枠、実行先選択、実行リース       | 複数サーバーでの安全な実行     |
| ④ 配備管理         | アプリケーションサーバー、ジョブ配備情報 | 実行可能なサーバーの判定       |
| ⑤ スケジュール管理 | 実行日時、繰り返し条件、タイムゾーン     | 定期的な起動                   |
| ⑥ ファイル参照管理 | 入力ファイル、S3 / MinIO の参照情報      | ジョブへの入力データ連携       |
| ⑦ 組織・権限管理   | 会社、店舗、ユーザー、ロール             | データ参照範囲と操作権限の制御 |
| ⑧ 通知管理         | 通知条件、宛先、配信結果                 | 実行結果の通知                 |
| ⑨ 監査管理         | 操作履歴、状態遷移、障害情報             | 操作追跡と障害調査             |

## 2. ジョブ定義情報

### 2.1 ジョブ定義（`job_definition`）

ジョブの種類や実行条件など、実行の基準となる情報を管理します。

| 項目             | カラム案                    | 説明                         |
|------------------|-----------------------------|------------------------------|
| ジョブ ID        | `job_definition_id`         | 主キー                       |
| ジョブコード     | `job_code`                  | システム内で一意な識別子     |
| ジョブ名         | `job_name`                  | 画面表示名                   |
| 説明             | `description`               | ジョブの目的                 |
| 所有システム     | `source_system_id`          | ジョブを提供する業務システム |
| 有効状態         | `enabled`                   | 起動可能かどうか             |
| 最大同時実行数   | `max_concurrent_executions` | 同一ジョブの同時実行上限     |
| タイムアウト秒数 | `timeout_seconds`           | 実行時間の監視基準           |
| パラメータ仕様   | `parameter_schema`          | 必須項目、型、制約など       |
| 通知設定 ID      | `notification_setting_id`   | 通知設定への参照             |
| 作成日時         | `created_at`                | 登録日時                     |
| 作成者           | `created_by`                | 登録者                       |
| 更新日時         | `updated_at`                | 最終更新日時                 |
| 更新者           | `updated_by`                | 最終更新者                   |

設計上のポイント：

- `job_code` は変更しにくい業務上の識別子とします。
- パラメータ仕様は JSON Schema 相当の形式で保持する方法が考えられます。
- ジョブの実装バージョンは、定義本体に固定値として埋め込むより、配備情報と実行履歴で管理する方法を推奨します。

### 2.2 ジョブのパラメータ仕様

ジョブごとに、起動時に渡せるパラメータの仕様を管理します。

| 項目               | 説明                                         |
|--------------------|----------------------------------------------|
| パラメータ名       | API で指定する名前                           |
| データ型           | 文字列、数値、真偽値、配列、オブジェクトなど |
| 必須フラグ         | 指定必須かどうか                             |
| デフォルト値       | 省略時に使用する値                           |
| 入力制約           | 桁数、範囲、形式など                         |
| 機密情報フラグ     | ログや画面でマスキングするか                 |
| 説明               | パラメータの用途                             |
| ファイル参照フラグ | S3 / MinIO 上のファイル参照を受け取るか      |

パラメータの実値は実行要求に紐付けて保存し、パスワードや認証情報などの機密値は平文で保存・表示しない方針とします。

## 3. 実行管理情報

### 3.1 ジョブ実行情報（`job_execution`）

Jobloom の中心となる情報です。1回の実行要求につき1つの実行 ID を発行し、状態・実行先・結果・復旧元を管理します。

| 項目                 | カラム案                   | 説明                                     |
|----------------------|----------------------------|------------------------------------------|
| 実行 ID              | `execution_id`             | 実行ごとの一意な ID                      |
| ジョブ ID            | `job_definition_id`        | 対象ジョブ                               |
| ジョブコード         | `job_code`                 | 実行時点の識別子                         |
| 実行状態             | `status`                   | 待機中、実行中、成功、失敗など           |
| 起動元               | `request_source`           | `CONSOLE`、`BUSINESS_SYSTEM`、`SCHEDULE` |
| 要求元システム       | `source_system_id`         | API 起動要求を送ったシステム             |
| 要求者               | `requested_by`             | 手動起動した利用者など                   |
| 会社 ID              | `company_id`               | 会社単位の実行の場合                     |
| 店舗 ID              | `store_id`                 | 店舗単位の実行の場合                     |
| 実行パラメータ       | `parameters`               | 起動時の入力値                           |
| 実行モード           | `execution_mode`           | `NORMAL`、`RERUN`、`RESUME` など         |
| 親実行 ID            | `parent_execution_id`      | 再実行・再開元の実行 ID                  |
| 実行先サーバー ID    | `server_id`                | 実際に実行を担当するサーバー             |
| ジョブバージョン     | `job_version`              | 実行に使用した実装バージョン             |
| 業務側実行 ID        | `application_execution_id` | 業務アプリケーション側の実行識別子       |
| 要求日時             | `requested_at`             | 起動要求を受け付けた日時                 |
| 開始日時             | `started_at`               | 実際の処理開始日時                       |
| 終了日時             | `finished_at`              | 処理終了日時                             |
| 最終状態確認日時     | `last_status_checked_at`   | 業務側の状態を最後に確認した日時         |
| 処理件数             | `processed_count`          | 処理済み件数                             |
| 成功件数             | `success_count`            | 成功件数                                 |
| 失敗件数             | `failure_count`            | 失敗件数                                 |
| エラーコード         | `error_code`               | エラー分類                               |
| エラー概要           | `error_summary`            | 画面表示用の概要                         |
| 結果情報             | `result_summary`           | 業務側から返された結果の要約             |
| バージョン管理用番号 | `version`                  | 楽観ロックなどに利用する更新番号         |

`parameters` と `result_summary` は JSONB
などで保持できます。ただし、大容量データや大量のログは格納せず、必要に応じて別テーブルやオブジェクトストレージに分離します。

### 3.2 実行状態

| 状態                | 意味                                 |
|---------------------|--------------------------------------|
| `QUEUED`            | 実行待ち                             |
| `DISPATCHING`       | 実行先への起動要求を処理中           |
| `RUNNING`           | 業務処理を実行中                     |
| `SUCCEEDED`         | 正常終了                             |
| `FAILED`            | 異常終了が確定                       |
| `CANCEL_REQUESTED`  | キャンセル要求中                     |
| `CANCELED`          | キャンセル完了                       |
| `RECOVERY_REQUIRED` | 実行結果が不明、または復旧判断が必要 |

`RECOVERY_REQUIRED` は、起動 API のタイムアウトや実行サーバー停止などで、業務処理の実行結果を確定できない場合にも利用します。調査前に
`FAILED` と決めつけないことが重要です。

### 3.3 実行イベント・状態遷移（`job_execution_event`）

実行状態の変化や重要なイベントを、実行履歴とは別に時系列で管理します。

| 項目         | カラム案          | 説明                             |
|--------------|-------------------|----------------------------------|
| イベント ID  | `event_id`        | 主キー                           |
| 実行 ID      | `execution_id`    | 対象実行                         |
| イベント種別 | `event_type`      | 受付、開始、進捗、成功、失敗など |
| 変更前状態   | `previous_status` | 変更前の状態                     |
| 変更後状態   | `new_status`      | 変更後の状態                     |
| イベント日時 | `event_at`        | 発生日時                         |
| 発生元       | `event_source`    | Jobloom、業務サーバーなど        |
| メッセージ   | `message`         | 状態変化の説明                   |
| 詳細情報     | `details`         | エラー情報、処理件数など         |

現在の状態は `job_execution` に保持し、詳細な経緯は `job_execution_event` に蓄積します。

## 4. 実行制御・サーバー・配備情報

### 4.1 アプリケーションサーバー（`application_server`）

| 項目                   | カラム案               | 説明                                 |
|------------------------|------------------------|--------------------------------------|
| サーバー ID            | `server_id`            | 主キー                               |
| サーバーコード         | `server_code`          | 一意な識別子                         |
| サーバー名             | `server_name`          | 表示名                               |
| API 接続先             | `base_url`             | 内部 API の接続先                    |
| 環境                   | `environment`          | 開発、テスト、本番など               |
| 有効状態               | `enabled`              | 管理対象かどうか                     |
| 運用状態               | `operational_status`   | 稼働許可、停止中、メンテナンス中など |
| 最終ヘルスチェック日時 | `last_health_check_at` | 最後に確認した日時                   |
| ヘルス状態             | `health_status`        | 正常、異常、不明など                 |
| 登録日時               | `created_at`           | 登録日時                             |
| 更新日時               | `updated_at`           | 更新日時                             |

API の認証情報は通常のカラムに平文で保存せず、Secrets Manager などの秘密情報管理機構を利用する方針です。

### 4.2 ジョブ配備情報（`job_deployment`）

どのサーバーに、どのジョブのどのバージョンが配備されているかを管理します。

| 項目             | カラム案            | 説明                     |
|------------------|---------------------|--------------------------|
| 配備 ID          | `deployment_id`     | 主キー                   |
| サーバー ID      | `server_id`         | 配備先                   |
| ジョブ ID        | `job_definition_id` | 対象ジョブ               |
| ジョブコード     | `job_code`          | 実装上の識別子           |
| ジョブバージョン | `job_version`       | 配備された実装バージョン |
| 配備状態         | `deployment_status` | 配備済み、停止、失敗など |
| 実行可否         | `executable`        | 起動対象にできるか       |
| 配備日時         | `deployed_at`       | 配備日時                 |
| 配備元リリース   | `release_id`        | リリース識別子           |
| 更新日時         | `updated_at`        | 更新日時                 |

ローリングデプロイ時には複数バージョンが同時に存在する可能性があります。実行時は互換性を確認し、実際に使用したバージョンを
`job_execution` に記録します。

### 4.3 同時実行枠（`job_execution_slot`）

複数の Jobloom インスタンスや実行要求が同時に動作しても、設定した同時実行数を超えないように制御します。

| 項目             | カラム案            | 説明                                   |
|------------------|---------------------|----------------------------------------|
| 枠 ID            | `slot_id`           | 主キー                                 |
| ジョブ ID        | `job_definition_id` | 対象ジョブ                             |
| 実行 ID          | `execution_id`      | 枠を使用する実行                       |
| 枠状態           | `slot_status`       | 使用中、解放済みなど                   |
| 確保日時         | `acquired_at`       | 枠を取得した日時                       |
| リース期限       | `lease_expires_at`  | 有効期限                               |
| 解放日時         | `released_at`       | 解放日時                               |
| フェンシング番号 | `fencing_token`     | 古い実行主体による更新を防ぐための番号 |

枠の確保・解放は PostgreSQL のトランザクションとロックで制御します。リース切れだけを根拠に業務処理を再実行せず、古い実行主体が書き込みを継続できない仕組みも検討します。

## 5. スケジュール情報

### 5.1 スケジュール定義（`job_schedule`）

| 項目             | カラム案              | 説明               |
|------------------|-----------------------|--------------------|
| スケジュール ID  | `schedule_id`         | 主キー             |
| スケジュール名   | `schedule_name`       | 表示名             |
| ジョブ ID        | `job_definition_id`   | 起動対象           |
| 有効状態         | `enabled`             | 有効・無効         |
| スケジュール種別 | `schedule_type`       | 一回、繰り返しなど |
| 実行式           | `schedule_expression` | cron 式など        |
| タイムゾーン     | `timezone`            | 実行時刻の基準     |
| 開始日時         | `valid_from`          | 有効開始日時       |
| 終了日時         | `valid_until`         | 有効終了日時       |
| 次回実行予定     | `next_fire_at`        | 次回予定日時       |
| 前回実行日時     | `last_fire_at`        | 前回の起動要求日時 |
| 作成者           | `created_by`          | 登録者             |
| 更新日時         | `updated_at`          | 更新日時           |

### 5.2 スケジュール実行記録（`schedule_fire`）

スケジュールが予定時刻に発火した事実と、その結果を記録します。

- `schedule_fire_id`：発火 ID
- `schedule_id`：対象スケジュール
- `scheduled_at`：予定日時
- `fired_at`：実際の発火日時
- `execution_id`：生成した実行 ID
- `fire_status`：生成済み、処理中、失敗など
- `error_summary`：発火失敗の概要

スケジュールの発火記録と実行要求の生成を一意に関連付け、複数インスタンスによる二重起動を防止します。

## 6. ファイル参照情報

### 6.1 アップロードファイル情報（`uploaded_file`）

Jobloom はファイル本体を保管せず、業務システムが S3 / MinIO に保存したファイルの参照情報を管理します。

| 項目             | カラム案           | 説明                           |
|------------------|--------------------|--------------------------------|
| ファイル ID      | `upload_id`        | Jobloom 内での識別子           |
| ファイル名       | `file_name`        | 元のファイル名                 |
| バケット名       | `bucket_name`      | S3 / MinIO のバケット          |
| オブジェクトキー | `object_key`       | オブジェクトの保存先キー       |
| ファイルサイズ   | `file_size`        | バイト数                       |
| チェックサム     | `checksum`         | 内容の整合性確認               |
| コンテンツ種別   | `content_type`     | MIME タイプ                    |
| アップロード元   | `source_system_id` | ファイルを登録した業務システム |
| 登録者           | `uploaded_by`      | 登録者の識別子                 |
| 登録日時         | `uploaded_at`      | 登録日時                       |
| 利用状態         | `file_status`      | 利用可能、無効、削除済みなど   |

### 6.2 実行とファイルの関連（`job_execution_file`）

1つの実行に複数のファイルを渡せるよう、実行とファイル参照を分離します。

| 項目        | カラム案         | 説明                       |
|-------------|------------------|----------------------------|
| 実行 ID     | `execution_id`   | 対象の実行                 |
| ファイル ID | `upload_id`      | 対象ファイル               |
| 用途        | `file_role`      | 入力、出力、エラー一覧など |
| 論理名      | `parameter_name` | パラメータとの対応         |
| 登録日時    | `created_at`     | 関連付け日時               |

重要な設計ルール

- ファイル実体を PostgreSQL に格納しません。
- 署名付き URL は期限が切れるため、永続的な参照情報として保存しません。
- 実行開始時に、対象ファイルの存在、アクセス権、サイズ、チェックサムなどを検証します。
- ファイルの削除・保管期間・アクセス権は、PDF などの別業務機能と共用するストレージ基盤上で、用途別のルールを適用します。

## 7. 会社・店舗・ユーザー・権限情報

### 7.1 会社・店舗

| 管理情報 | テーブル案 | 主な項目                                                                                    |
|----------|------------|---------------------------------------------------------------------------------------------|
| 会社     | `company`  | `company_id`, `company_code`, `company_name`, `enabled`, `created_at`, `updated_at`         |
| 店舗     | `store`    | `store_id`, `company_id`, `store_code`, `store_name`, `enabled`, `created_at`, `updated_at` |

`store.company_id` により、店舗がどの会社に所属するかを管理します。

### 7.2 ユーザー・所属・ロール

| 管理情報   | テーブル案              | 主な項目                                                          |
|------------|-------------------------|-------------------------------------------------------------------|
| ユーザー   | `app_user`              | `user_id`, `external_subject_id`, `user_name`, `email`, `enabled` |
| 会社所属   | `user_company`          | `user_id`, `company_id`, `role_code`, `created_at`                |
| 店舗所属   | `user_store`            | `user_id`, `store_id`, `role_code`, `created_at`                  |
| ロール定義 | `role_definition`       | `role_code`, `role_name`, `description`                           |
| 権限定義   | `permission_definition` | `permission_code`, `permission_name`, `description`               |
| ロール権限 | `role_permission`       | `role_code`, `permission_code`                                    |

想定するロールの例：

- `SYSTEM_ADMIN`：システム設定・権限管理
- `JOB_OPERATOR`：起動、監視、キャンセル、再実行・再開
- `COMPANY_VIEWER`：自社の実行情報の参照
- `STORE_VIEWER`：許可された店舗の実行情報の参照

会社・店舗 ID を API リクエストから受け取った場合も、サーバー側でログインユーザーの権限を必ず検証します。参照権限と運用操作権限は分離します。

## 8. 通知情報

### 8.1 通知設定（`job_notification_setting`）

| 項目         | カラム案                  | 説明                                 |
|--------------|---------------------------|--------------------------------------|
| 通知設定 ID  | `notification_setting_id` | 主キー                               |
| ジョブ ID    | `job_definition_id`       | 対象ジョブ。全体設定なら NULL も検討 |
| 通知イベント | `event_type`              | 成功、異常終了、キャンセルなど       |
| 通知チャネル | `channel`                 | メール、Webhook など                 |
| 通知先       | `recipient_config`        | 宛先・送信先の設定                   |
| 有効状態     | `enabled`                 | 通知を行うか                         |
| 作成日時     | `created_at`              | 登録日時                             |
| 更新日時     | `updated_at`              | 更新日時                             |

### 8.2 通知履歴（`user_notification`）

| 項目              | カラム案            | 説明                       |
|-------------------|---------------------|----------------------------|
| 通知 ID           | `notification_id`   | 主キー                     |
| 実行 ID           | `execution_id`      | 対象実行                   |
| 通知先ユーザー ID | `user_id`           | 受信者                     |
| 通知種別          | `notification_type` | 成功、異常終了など         |
| 件名              | `subject`           | 通知のタイトル             |
| 内容              | `message`           | 通知本文                   |
| 配信状態          | `delivery_status`   | 未送信、送信済み、失敗など |
| 既読日時          | `read_at`           | 画面通知の既読日時         |
| 送信日時          | `sent_at`           | 配信日時                   |
| エラー概要        | `error_summary`     | 配信失敗の理由             |

通知の送信要求は Outbox パターンなどで実行状態の更新と整合性を取ります。送信処理が失敗した場合の再送は通知処理の再試行であり、業務ジョブ自体の自動リトライとは別の仕組みです。

## 9. 監査・エラー情報

### 9.1 監査ログ（`audit_log`）

設定変更や運用操作の証跡を保存します。

| 項目          | カラム案        | 説明                               |
|---------------|-----------------|------------------------------------|
| 監査 ID       | `audit_id`      | 主キー                             |
| 操作者 ID     | `actor_user_id` | 操作したユーザー                   |
| 操作種別      | `action_type`   | 登録、変更、起動、再実行、再開など |
| 対象種別      | `target_type`   | ジョブ、実行、ユーザーなど         |
| 対象 ID       | `target_id`     | 操作対象の ID                      |
| 操作理由      | `reason`        | 復旧操作などの理由                 |
| 操作日時      | `occurred_at`   | 操作日時                           |
| 結果          | `action_result` | 成功・失敗                         |
| リクエスト ID | `request_id`    | API リクエストの追跡 ID            |
| 変更前情報    | `before_data`   | 変更前の値。必要な項目のみ         |
| 変更後情報    | `after_data`    | 変更後の値。必要な項目のみ         |

パスワード、認証トークン、機密パラメータなどは監査ログに含めません。

### 9.2 エラー・障害情報

エラー情報は、実行情報やイベント情報と関連付けて管理します。詳細なスタックトレースを別テーブルにするか、ログ基盤に出力するかは運用要件に応じて決定します。

- エラーコード
- エラー概要
- 詳細メッセージ
- 発生日時
- 発生元サーバー
- 対象実行 ID
- 相関 ID／トレース ID
- スタックトレース参照先
- 運用上の対応状況
- 調査担当者
- 調査結果・対応メモ

## 10. 主要テーブルの関係

次の図は、主要な管理情報の関連を示した概念モデルです。

\#chatgpt-mermaid-\_r_1lf\_{font-family:-apple-system,"system-ui","Segoe UI",sans-serif;font-size:
14px;fill:rgb (26, 28, 31);}@keyframes edge-animation-frame{from{stroke-dashoffset:0;}}@keyframes
dash{to{stroke-dashoffset:0;}}#chatgpt-mermaid-\_r_1lf\_ .edge-animation-slow{stroke-dasharray:9,5!
important;stroke-dashoffset:900;animation:dash 50s linear infinite;stroke-linecap:
round;}#chatgpt-mermaid-\_r_1lf\_ .edge-animation-fast{stroke-dasharray:9,5!
important;stroke-dashoffset:900;animation:dash 20s linear infinite;stroke-linecap:
round;}#chatgpt-mermaid-\_r_1lf\_ .error-icon{fill:rgba (255, 255, 255, 0.96)
;}#chatgpt-mermaid-\_r_1lf\_ .error-text{fill:rgb (26, 28, 31);stroke:rgb (26, 28, 31)
;}#chatgpt-mermaid-\_r_1lf\_ .edge-thickness-normal{stroke-width:1px;}#chatgpt-mermaid-\_r_1lf\_
.edge-thickness-thick{stroke-width:3.5px;}#chatgpt-mermaid-\_r_1lf\_
.edge-pattern-solid{stroke-dasharray:0;}#chatgpt-mermaid-\_r_1lf\_
.edge-thickness-invisible{stroke-width:0;fill:none;}#chatgpt-mermaid-\_r_1lf\_
.edge-pattern-dashed{stroke-dasharray:3;}#chatgpt-mermaid-\_r_1lf\_
.edge-pattern-dotted{stroke-dasharray:2;}#chatgpt-mermaid-\_r_1lf\_ .marker{fill:rgba (26, 28, 31,
0.494);stroke:rgba (26, 28, 31, 0.494);}#chatgpt-mermaid-\_r_1lf\_ .marker.cross{stroke:rgba (26,
28, 31, 0.494);}#chatgpt-mermaid-\_r_1lf\_ svg{font-family:-apple-system,"system-ui","Segoe
UI",sans-serif;font-size:14px;}#chatgpt-mermaid-\_r_1lf\_ p{margin:0;}#chatgpt-mermaid-\_r_1lf\_
.entityBox{fill:rgb (224, 237, 254);stroke:rgb (83, 154, 248);}#chatgpt-mermaid-\_r_1lf\_
.relationshipLabelBox{fill:rgba (255, 255, 255, 0.96);opacity:0.7;background-color:rgba (255, 255,
255, 0.96);}#chatgpt-mermaid-\_r_1lf\_ .relationshipLabelBox rect{opacity:
0.5;}#chatgpt-mermaid-\_r_1lf\_ .labelBkg{background-color:rgba (255, 255, 255, 0.5)
;}#chatgpt-mermaid-\_r_1lf\_ .edgeLabel{background-color:rgb (255, 255, 255)
;}#chatgpt-mermaid-\_r_1lf\_ .edgeLabel .label rect{fill:rgb (255, 255, 255)
;}#chatgpt-mermaid-\_r_1lf\_ .edgeLabel .label text{fill:rgb (26, 28, 31)
;}#chatgpt-mermaid-\_r_1lf\_ .edgeLabel .label{fill:rgb (83, 154, 248);font-size:
14px;}#chatgpt-mermaid-\_r_1lf\_ .label{font-family:-apple-system,"system-ui","Segoe
UI",sans-serif;color:rgb (26, 28, 31);}#chatgpt-mermaid-\_r_1lf\_
.edge-pattern-dashed{stroke-dasharray:8,8;}#chatgpt-mermaid-\_r_1lf\_ .node
rect,#chatgpt-mermaid-\_r_1lf\_ .node circle,#chatgpt-mermaid-\_r_1lf\_ .node
ellipse,#chatgpt-mermaid-\_r_1lf\_ .node polygon{fill:rgb (224, 237, 254);stroke:rgb (83, 154, 248)
;stroke-width:1px;}#chatgpt-mermaid-\_r_1lf\_ .relationshipLine{stroke:rgba (26, 28, 31, 0.494)
;stroke-width:1px;fill:none;}#chatgpt-mermaid-\_r_1lf\_ .marker{fill:none!important;stroke:rgba (26,
28, 31, 0.494)!important;stroke-width:1;}#chatgpt-mermaid-\_r_1lf\_ [data-look=neo]
.labelBkg{background-color:rgba (255, 255, 255, 0.5);}#chatgpt-mermaid-\_r_1lf\_ .node
.neo-node{stroke:rgb (83, 154, 248);}#chatgpt-mermaid-\_r_1lf\_ [data-look="neo"].node
rect,#chatgpt-mermaid-\_r_1lf\_ [data-look="neo"].cluster
rect,#chatgpt-mermaid-\_r_1lf\_ [data-look="neo"].node polygon{stroke:url
(#chatgpt-mermaid-\_r_1lf\_-gradient);filter:drop-shadow (1px 2px 2px rgba (185,185,185,1))
;}#chatgpt-mermaid-\_r_1lf\_ [data-look="neo"].swimlane.cluster rect{filter:
none;}#chatgpt-mermaid-\_r_1lf\_ [data-look="neo"].node path{stroke:url
(#chatgpt-mermaid-\_r_1lf\_-gradient);stroke-width:1px;}#chatgpt-mermaid-\_r_1lf\_ [data-look="neo"]
.node .outer-path{filter:drop-shadow (1px 2px 2px rgba (185,185,185,1))
;}#chatgpt-mermaid-\_r_1lf\_ [data-look="neo"].node .neo-line path{stroke:rgb (83, 154, 248);filter:
none;}#chatgpt-mermaid-\_r_1lf\_ [data-look="neo"].node circle{stroke:url
(#chatgpt-mermaid-\_r_1lf\_-gradient);filter:drop-shadow (1px 2px 2px rgba (185,185,185,1))
;}#chatgpt-mermaid-\_r_1lf\_ [data-look="neo"].node circle .state-start{fill:
#000000;}#chatgpt-mermaid-\_r_1lf\_ [data-look="neo"].icon-shape .icon{fill:url
(#chatgpt-mermaid-\_r_1lf\_-gradient);filter:drop-shadow (1px 2px 2px rgba (185,185,185,1))
;}#chatgpt-mermaid-\_r_1lf\_ [data-look="neo"].icon-shape .icon-neo path{stroke:url
(#chatgpt-mermaid-\_r_1lf\_-gradient);filter:drop-shadow (1px 2px 2px rgba (185,185,185,1))
;}#chatgpt-mermaid-\_r_1lf\_ :root{--mermaid-font-family:-apple-system,"system-ui","Segoe
UI",sans-serif;}JOB_DEFINITIONJOB_EXECUTIONJOB_DEPLOYMENTAPPLICATION_SERVERJOB_EXECUTION_EVENTJOB_EXECUTION_FILEUPLOADED_FILEJOB_SCHEDULESCHEDULE_FIRECOMPANYSTOREUSER_COMPANYUSER_STOREUSER_NOTIFICATIONAUDIT_LOG実行される配備される配備先イベントを持つ親子関係ファイルを参照関連付けスケジュールを持つ発火する起動する所属所属ユーザー所属ユーザー会社単位店舗単位通知操作記録

この図は論理的な関連を示すもので、実際の外部キー制約や任意項目の扱いは詳細設計で確定します。

## 11. 初期リリースで必要な管理情報

最初からすべてを実装するのではなく、ジョブ実行・監視・復旧に必要な情報を優先して整備することを推奨します。

| 優先度       | 管理情報                 | 対象テーブル案                                              |
|--------------|--------------------------|-------------------------------------------------------------|
| 必須         | ジョブ定義               | `job_definition`                                            |
| 必須         | ジョブ実行・履歴         | `job_execution`                                             |
| 必須         | 実行イベント             | `job_execution_event`                                       |
| 必須         | アプリケーションサーバー | `application_server`                                        |
| 必須         | ジョブ配備・バージョン   | `job_deployment`                                            |
| 必須         | 同時実行制御             | `job_execution_slot`                                        |
| 必須         | ユーザー・権限           | `app_user`, `role_definition`, `user_company`, `user_store` |
| 必須         | 会社・店舗               | `company`, `store`                                          |
| 必須         | 監査ログ                 | `audit_log`                                                 |
| 条件付き必須 | スケジュール             | `job_schedule`, `schedule_fire`                             |
| 条件付き必須 | ファイル参照             | `uploaded_file`, `job_execution_file`                       |
| 次段階       | 通知                     | `job_notification_setting`, `user_notification`             |

「条件付き必須」は、初期リリースでスケジュール起動やファイル入力を提供する場合には必須という意味です。

## 12. 詳細設計で確定すべきポイント

特に次の点は、項目一覧をデータベース設計へ進める前に確定するとよいです。

1. ID の形式： UUID を採用するか、連番を採用するか。
2. 実行パラメータの保存： JSONB で保存する項目と、検索条件として独立カラムにする項目の区分。
3. 実行履歴の保管期間： 履歴、イベント、ログ、監査ログの保持期間とアーカイブ方針。
4. 実行状態の整合性： Jobloom と業務サーバーの状態が食い違った場合の照合方法。
5. 復旧の安全性： 再実行・途中再開の可否を判定する条件、チェックポイントの管理主体。
6. 会社・店舗の適用範囲： 全社共通ジョブと店舗別ジョブのデータモデル上の区別。
7. 秘密情報の管理： API 認証情報や機密パラメータを DB・ログに残さない方式。

設計上のおすすめは、まず `job_definition`、`job_execution`、`job_execution_event`、`application_server`、
`job_deployment` を中心に詳細化することです。 ここが固まると、実行 API、管理画面、障害復旧、性能テストの設計を一貫して進められます。
