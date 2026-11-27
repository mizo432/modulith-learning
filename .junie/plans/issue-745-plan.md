# Issue #745 計画：現在時刻の取得をClock経由に統一する

## 実測（2026-11-27）
- now() 直接呼び出しは main に 113 か所。うちエンティティ内部が約89か所、非エンティティが約24か所
- Clock Bean は既存（snowflake の TimeConfiguation、Clock.systemUTC()）
- 旧来の Date／Calendar／SimpleDateFormat は 0 件

## 方針
1. Clock Bean のゾーンを systemDefaultZone へ変更する（Instant の挙動は不変、LocalDateTime.now() の現行挙動も不変）
2. 非エンティティ（コマンドサービス、コントローラ、バッチリスナー／リーダー、API クラス）へ Clock をコンストラクタ注入し、now(clock) へ置き換える。Clock 引数はコンストラクタの最後に置く
3. エンティティ内部は #757（サブ Issue）で扱う

## テストリスト
- 固定 Clock を注入したとき、ジョブ実行の開始・完了時刻は固定時刻となる
- 固定 Clock を注入したとき、ジョブ手動起動で保存される実行レコードの作成時刻は固定時刻となる
- 固定 Clock を注入したとき、ジョブ定義の作成時刻は固定時刻となる
- 固定 Clock を注入したとき、スプリントキャンセルイベントの発生時刻は固定時刻となる
- 固定 Clock を注入したとき、帳票の論理削除時刻は固定時刻となる
- 固定 Clock を注入したとき、帳票アップロードの作成時刻は固定時刻となる
- 移行後も既存テスト全件が Green である
