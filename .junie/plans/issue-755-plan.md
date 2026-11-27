# #755 外部通信のログを通信時に記録する 計画

## 現状（実測、origin/develop 20bda3ba2）
- 認可サービス向け RestClient（accountMgmt、AccountManagementConfig）: ロギングなし
- BankcodeJP API RestClient（bankReg、BankImportInfrastructureConfig）: ロギングなし。x-api-key ヘッダーを使用
- S3／MinIO（documentMgmt、DocumentStorageServiceImpl）: ロギングなし
- メール送信（notification、EmailNotificationService）: 失敗時 WARN のみ、成功時の記録なし
- ClientHttpRequestInterceptor の既存実装なし。ListAppender を使うテストの前例なし
- logback.xml の root は WARN。undecided.supporting は INFO 指定あり。documentMgmt と notification には INFO 指定がないため、このままでは INFO ログが出力されない

## 方針
1. 共通インターセプタ LoggingClientHttpRequestInterceptor を undecided.supporting.http に新設する
   - 送信前: INFO（メソッド、URI）
   - 受信後: INFO（メソッド、URI、ステータス、所要時間ms）
   - 例外時: WARN（メソッド、URI、所要時間ms、例外）を記録し、そのまま再スローする
   - ヘッダーとボディは記録しない（x-api-key、Authorization の漏えい防止）
2. AccountManagementConfig と BankImportInfrastructureConfig の RestClient へ組み込む
3. DocumentStorageServiceImpl にロガーを追加する
   - upload: INFO（バケット、キー、サイズ）／download・delete: INFO（バケット、キー）
   - 失敗時: WARN（バケット、キー、例外）を記録し、そのまま再スローする（現行の伝播挙動を維持）
4. EmailNotificationService の成功時に INFO（宛先、件名）を追加する。失敗時 WARN は現行どおり
5. logback.xml に undecided.generic.documentMgmt と undecided.notification の INFO ロガーを追加する（インターセプタは undecided.supporting の INFO 指定でカバーされる）

## テストリスト
1. ロギングインターセプタ経由でリクエストを送ったとき、メソッド・URI・ステータス・所要時間を含むINFOログが記録される
2. リクエストに機密ヘッダー（x-api-key）が含まれるとき、その値がログに記録されない
3. 通信が例外で失敗したとき、WARNログが記録され、例外はそのまま伝播する
4. メール送信が成功したとき、宛先と件名を含むINFOログが記録される
5. メール送信が失敗したとき、WARNログが記録される（既存の振る舞い）
6. S3へアップロードしたとき、バケット・キー・サイズを含むINFOログが記録される
7. S3からダウンロード・削除したとき、バケットとキーを含むINFOログが記録される
8. S3操作が失敗したとき、WARNログが記録され、例外はそのまま伝播する

## 対象外
- ログのマスク処理以上の秘匿情報管理、監視ログ（MONITORING）への分離
- S3クライアントのリトライ戦略の変更
