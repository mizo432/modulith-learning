# AGENTS.md — modulith-learning

> 入口：運用規約の正本は `_harness` リポジトリの `AGENTS.master.md`、開発手順と共通ガイドラインの正本は agent-guidelines リポジトリ（`.agents/agent-guidelines/`）。このファイルにはプロジェクト固有の事項のみを置く。

AI エージェントおよび開発者向けのプロジェクト案内。実物のコードと設定に基づく。推測で書かれた記述に従う前に、必ず対象ファイルで裏を取ること。

## プロジェクト概要

Spring Modulith と DDD の学習・実践用デモプロジェクト。事業領域ごとにモジュールを分離したモジュラーモノリスを志向する。要件定義には RDRA を適用中（後述）。

## 技術スタック（backend/build.gradle.kts で確認済み）

- Java 25（Gradle toolchain）、Gradle マルチプロジェクト（Kotlin DSL）
- Spring Boot 4.1.2 / Spring Modulith 2.1.0 / Spring Cloud 2025.1.2
- DB: PostgreSQL + Flyway 11.3.4（事業領域ごとにスキーマとマイグレーションを分離）
- JPA（spring-boot-starter-data-jpa）、jMolecules（オニオンアーキテクチャ）
- バリデーション: YAVI 0.16.0、ビルダー生成: Jilt、Lombok 使用
- テスト: JUnit 6.1.0、ArchUnit 1.5.1、Testcontainers（PostgreSQL）、AssertJ-DB、Pitest（ミューテーション）、JaCoCo
- その他: Spring Batch + Super CSV、Apache POI、Redis、Caffeine、Eureka Client、springdoc OpenAPI、SonarCloud 連携

## Gradle モジュール構成（settings.gradle）

| モジュール | 役割 |
|-----------|------|
| backend | 本体。事業領域のパッケージ群（ルートパッケージ `undecided`）を含む |
| api-gateway | API Gateway（認証・認可の一元管理） |
| authorization | OAuth2 認可サーバー |
| service-registry | Eureka サーバ |
| frontend | フロントエンド |
| mizo432-shared-common | 共通ライブラリ（パッケージ `com.github.mizo432.shared`） |

## backend の事業領域と DB スキーマ

Flyway は事業領域ごとにタスクとスキーマが分かれている（backend/build.gradle.kts）。

| 領域 | スキーマ | マイグレーション配置 | Flyway タスク |
|------|---------|--------------------|--------------|
| 関連付け（AS） | undecided_as | db/migration/as | flywayMigrateAs |
| 開発運用（DO） | undecided_do | db/migration/do | flywayMigrateDo |
| 電子帳票（EF） | undecided_ef | db/migration/ef | flywayMigrateEf |
| 購買事業（PC） | undecided_pc | db/migration/pc | flywayMigratePc |
| 承認申請（AR） | undecided_ar | db/migration/ar | flywayMigrateAr |
| 認可（AZ） | undecided_az | db/migration/az | flywayMigrateAz |
| 通知（NT） | undecided_nt | db/migration/nt | flywayMigrateNt |

このほか初期要望には Agiloom（AG、Scrum プロジェクト管理）、ジョブ管理（JM）、レジストリ群（CA/TA/AD/ER/FI）などの事業領域がある。新しい領域を足すときは既存の命名規則（スキーマ `undecided_<領域名小文字>`）に合わせる。

## ビルドとテスト

```bash
./gradlew :backend:build          # ビルド
./gradlew :backend:test           # 単体テスト（medium / large タグは除外、タイムアウト60秒）
./gradlew :backend:mediumTest     # medium タグのテスト（タイムアウト5分）
./gradlew :backend:largeTest      # large タグのテスト（タイムアウト1時間）
./gradlew :backend:pitest         # ミューテーションテスト（対象は com.github.mizo432.shared.*）
./gradlew :backend:flywayMigrateAs  # 領域ごとのマイグレーション（例：AS）
```

- テストはタグでサイズを分ける。新しいテストには適切なタグ付けを検討すること
- DB が必要なテストは Testcontainers（PostgreSQL）を使う。H2 はテスト用ランタイムにも入っているが、本番前提は PostgreSQL
- ローカル実行用の compose.yml と Dockerfile が backend/ にある

## アーキテクチャ規約

- モジュール境界は Spring Modulith で検証する。`ApplicationModules.verify()` を破る変更をしない
- モジュール（事業領域）跨ぎは ID 参照とイベント連携のみ。他領域の内部型やテーブルを直接参照しない
- 他事業領域のデータをクエリーサービスで使用する場合は、イベントを使用して連携し、各事業領域がサブセット（射影）を保持してそれを参照する（必要な場合に適用する）
- 状態遷移の履歴はロングタームイベントパターン（Spring Modulith のイベント発行ログに永続化されるイベント）で担保し、専用の監査ログは持たない
- DB の FK 制約もモジュール内に閉じる。スキーマ跨ぎの FK は張らない
- 認証・認可は API Gateway と authorization（OAuth2 Authorization Code + PKCE、JWT は RS256、アクセストークン15分以内、リフレッシュトークン7日以内）に寄せる
- **internal パッケージのクラス・インタフェイスは、できる限りパッケージプライベートにする**。public にするのは spi に置く公開型（エンティティ・イベント・区分値など）のみとし、実装クラスはサブパッケージ（command／query／presentation 等）に分けず単一の internal パッケージに置くことで、パッケージプライベート同士で連携できるようにする。他領域のエンティティへの関連は持たず、ID で参照する

## ID とデータの規約

- 主キーは **Snowflake ID**。新規に採番の仕組みを作らず、支援サブドメインの既存実装を使う：`undecided.supporting.snowflake` の `SnowflakeIdGenerator`（`nextIdValue()` で値オブジェクト `SnowflakeId` を取得）
- エポックは 2025-01-01 UTC（EPOCH_MILLIS = 1735689600000）、ノードID は `SnowflakeNodeLeaseManager` のリース方式。DB のシーケンス／IDENTITY は使わない
- 画面・API の JSON では ID を string として扱う（JavaScript の Number 精度対策）
- 排他制御は version 列による楽観ロックを前提とする
- 状態・区分の値は英語コードで保持する（例：DRAFT / PUBLISHED / IN_PROGRESS）。DB にはコード値のみをセットし、日本語ラベルは DB に持たずアプリ側（enum）で対応付ける。区分値は処理の分岐条件として使用するものであり、条件判定は enum 定数で行い、文字列リテラルや表示ラベルで判定しない（ADR-003）
- カテゴリー（更新頻度・出所・影響範囲・連絡方法など）は処理の分岐に使用せず、検索条件としてのみ使用する。値（コードと表示名）は DB に保持し、データとして追加・変更できる（ADR-004）
- 項目の制約（最大長・最小長・最小値・最大値・文字セット）は、値オブジェクトの定数として定義し単一の情報源とする。検証・JPA の `@Column(length = ...)`・API 定義・境界値テストはその定数を参照し、数値を各所に直書きしない（ADR-002）。項目ごとの定義は `2_RDRASpec/項目辞書.md`

## 要件・設計ドキュメント（RDRA）

リポジトリルートに要件定義の成果物がある。設計判断の前に該当箇所を確認すること。

- `初期要望.txt`：背景・アクター・外部システム・要求・制約・ビジネスポリシー
- `0_RDRAZeroOne/`、`1_RDRA/`：RDRA の前段成果物
- `2_RDRASpec/`：画面照会.json、論理データモデル.md、物理データモデル.md、ビジネスルール.md、項目辞書.md など。現在 Spec 化が進んでいるのは AG-01（プロダクトディスカバリー）の範囲
- `RDRA_Knowledge/`：RDRA の知識置き場
- 物理設計の未決定事項は GitHub Issues（mizo432/modulith-learning、#560〜#568、design ラベル）で管理し、2026-11-16 にすべて決定済み・クローズした
- アーキテクチャ上の決定は `docs/14 ADR/` に ADR として記録する（非機能要件の対応可否を決めた ADR-0001〜0031 と、アーキテクチャ決定の 002〜005 が同居する。例：002 項目制約の値オブジェクト定数化、003 区分値のコード値保持と分岐条件としての使用、004 カテゴリーの検索条件としての DB 保持、005 開発ワークフローへの takt フローの移植と Junie CLI 直接派遣）

## 作業上の注意

- 既存の命名・パッケージ構成（`undecided.<領域>`、`com.github.mizo432.shared.*`）に合わせ、独自の流儀を持ち込まない
- 依存追加は backend/build.gradle.kts の既存のバージョン管理（extra プロパティ）に倣う
- コミット前に `./gradlew :backend:test` が通ることを確認する
- 開発ワークフロー（製造層）は、takt の開発フロー（development-core）を移植した手順で回し、実行は Junie CLI の直接派遣とする（ADR-005）。ステップは plan → write_tests → implement → peer-review（必要に応じて replan）で、ステップ間の判定と統制は Muse（参謀兼オペレーター）が担い、Junie は各ステップのワーカーとして起動する。制御面は Orca に一本化する。手順の正本は共有リポジトリ agent-guidelines の workflow ガイドライン（`.agents/agent-guidelines/guidelines/workflow/junie-dispatch-development.guidelines.md`）に置く
- 共用ガイドラインは `.agents/agent-guidelines` の git submodule で取り込んでいる。clone 時は `--recurse-submodules` を付けるか、clone 後に `git submodule update --init` を実行すること（付け忘れると `.agents/agent-guidelines` が空になる）