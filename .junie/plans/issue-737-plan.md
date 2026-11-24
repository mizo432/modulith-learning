# Issue #737 計画: mediumテストの一括実行でバッチ系テストが干渉して失敗し、5分タイムアウトで全件実行できない

## 調査結果（2026-11-24 実測）

### 再現
- develop（406eb789f）で medium 全36クラスを1回の `mediumTest` 実行にまとめると再現する
  - 5分タイムアウトで打ち切られ、94件実行時点で12件失敗（BacklogItemReadyMediumTest、ScrumTeamSubsetMemberEmployeeIdsMediumTest など）
  - MinIO系を除く29クラスでは 181件すべて Green（1分24秒）
  - 添付＋Ready＋SubsetMember の3クラスでは全 Green
- #525 段階6（2026-11-24）では LargeRecordImportBatchConfigTest 8件・SampleItemImportBatchConfigTest 3件が、すべて「ジョブのステータスが COMPLETED でない」という AssertionFailedError で失敗した。単独実行では全 Green

### 原因の見立て
- `@DirtiesContext` を持つクラスは6つ（BacklogItemAttachmentsEndpointMediumTest と documentMgmt の5クラス）。いずれも MinIO 用の独自コンテキストを持つ
- 全 medium テストは `TestcontainersConfiguration` の静的 PostgreSQL／Redis コンテナを共有するが、コンテナは `@Bean @ServiceConnection` として各コンテキストに登録されるため、あるコンテキストが閉じられる（DirtiesContext、コンテキストキャッシュの追い出し）と、共有コンテナまで停止される
- 静的フィールドは停止済みコンテナを保持し続け、`start()` は null のときしか呼ばれないため、後続クラスは停止した DB に対して実行され、実行順や負荷によって失敗するクラスが変わる（干渉の正体）
- mediumTest のタイムアウト5分（backend/build.gradle.kts）も、全件実行には不足する

## 対策
1. `TestcontainersConfiguration` を Testcontainers のシングルトンコンテナ方式に変更する
   - コンテナは静的初期化で起動し、Spring のライフサイクル管理下に置かない（コンテキストが閉じても停止されない。終了は JVM 終了時の Ryuk に任せる）
   - 接続情報は `@DynamicPropertySource` で spring.datasource.* と spring.data.redis.* に供給する（`@Import(TestcontainersConfiguration.class)` という利用側の契約は変えない）
2. mediumTest のタイムアウトを5分から30分に引き上げる

## テストリスト（検証シナリオ）
- medium全36クラスを1回で実行したとき、全件Greenとなる
- @DirtiesContextクラスを含む一括実行のとき、後続クラスのテストが失敗しない
- mediumTestが5分を超える実行でもタイムアウトで打ち切られない
- 対策後の一括実行を2回続けて行ったとき、2回とも全件Greenとなる（安定性確認）

## 対象外
- 他モジュール（service-registry、api-gateway、authorization）の mediumTest 設定
- MinIO コンテナ自体の扱い（各クラスの静的コンテナのまま）
