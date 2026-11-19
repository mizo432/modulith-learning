---
sessionId: session-261118-142132-1m44
---

# 要件定義

## 概要・目標

GitHub Issue #679「コメント通知（メール＋アプリ内スナックバー）を実装する」の実装計画。

コメントの追加・削除時に、コメント参加者（操作した人を除く）へメールとアプリ内スナックバーで通知する機能を実装する。

### 前提決定（#633/#635）
- **宛先**: コメント参加者（そのバックログアイテムにコメントした人）、ただし操作した人は除く
- **チャネル**: メール ＋ アプリ内スナックバー
- **コメント削除時も通知する**（#635の決定）
- backendに通知モジュールは存在しないため**新設する**
- 他事業領域のデータは**イベント連携＋サブセット保持**の規約に従う

### スコープ
**In Scope:**
- 通知モジュール（NT）の新設（backend）
- コメント追加・削除時のイベント発行（requirementBacklog）
- イベント受信による通知レコード作成（NTモジュール）
- メール送信基盤の導入
- 通知取得用API（スナックバー用）
- フロントエンドのスナックバー表示
- Flywayマイグレーション
- テスト

**Out of Scope:**
- コメント編集時の通知（編集機能未実装のため）
- WebSocketによるリアルタイム通知
- 通知のパーソナライゼーション（ON/OFF設定）
- 他ドメイン（スプリント等）の通知

### ユーザーストーリー
1. As a チームメンバー, I want コメントが追加されたときに通知を受け取る, so that バックログアイテムの議論を見逃さない
2. As a チームメンバー, I want コメントが削除されたときに通知を受け取る, so that 議論の履歴の変化に気づく
3. As a 操作者, I want 自分がコメントしたときは通知を受けない, so that 不要な通知で煩わされない

### 非機能要件
- メール送信は非同期（同期待機せず）
- 通知レコードは永続化し、再ログイン後も確認可能
- 既存の#658トースト仕様に準拠


# 技術設計

## (1) メール送信基盤の調査結果

**既存コードにメール送信基盤は存在しない。**

- `backend/build.gradle.kts` に `spring-boot-starter-mail` の依存関係なし
- `JavaMailSender` の使用箇所なし
- `application.yml` にSMTP設定なし
- `EmailAddress` 値オブジェクトは存在する（`undecided.association.relationshipMgmt.spi.party.contact.EmailAddress`）が、送信用途ではなく連絡先保持用

**→ 新設が必要。** `spring-boot-starter-mail` を依存関係に追加し、SMTP設定を `application.yml` に追加する。

## (2) 未読管理・スナックバー表示の提案

### 未読管理
**必要と提案。** 理由:
- メールは送信後取り消せないため、アプリ内での既読/未読の追跡がユーザー体験に不可欠
- 既存の `Notification` エンティティ（`sprintExecution.spi`）に `isRead` フィールドと `markAsRead()` メソッドが既に存在
- 通知モジュール新設時にこの設計を引き継ぐ

### スナックバー表示の仕組み
**ポーリング取得を提案。** 理由:
- WebSocketはスコープ外（Out of Scope）
- 既存の `useAutoRefresh` hook（`frontend/src/hooks/useAutoRefresh.ts`）が自動リフレッシュの基盤を提供
- 通知取得APIを定期的にポーリングし、未読の通知をスナックバーで表示
- ポーリング間隔: 30秒（推奨）

## (3) スナックバー表示仕様と#658トースト仕様の関係

### #658トースト仕様（決定済み）
- 表示位置: 画面右下
- 自動消去: 6秒
- 同時表示: 最大3件
- アニメーション: フェードイン/フェードアウト
- モバイル: 画面下部に横幅いっぱい

### 提案: 通知スナックバーは#658トースト仕様を流用
- 既存の `SprintSettings.tsx` が MUI `Snackbar` を使用（`autoHideDuration={6000}`, `anchorOrigin={{vertical: 'bottom', horizontal: 'center'}}`）
- 通知スナックバーは同じMUI Snackbarコンポーネントを使用
- 位置は#658の「右下」に合わせ、`anchorOrigin={{vertical: 'bottom', horizontal: 'right'}}` とする
- 自動消去6秒、同時最大3件、フェードアニメーションは#658に準拠
- 通知スナックバーはクリックでバックログアイテム詳細へ遷移可能（トーストとの差別化）
- 共通の `NotificationSnackbar` コンポーネントを新設し、全画面で利用可能に

## アーキテクチャ

### モジュール境界
```
requirementBacklog (AG)  ──イベント──>  notification (NT)  ──メール送信──>
         │                                                       │
         └─────API─────>  frontend  <─────API─────┘
```

### データフロー
1. ユーザーがコメントを追加・削除
2. `requirementBacklog` が `BacklogItemCommentAdded` / `BacklogItemCommentDeleted` イベントを発行
3. `notification` モジュールがイベントを受信
4. コメント参加者（操作者除く）の従業員IDを取得
5. 各参加者に対して通知レコードを作成（NTスキーマ）
6. メール送信（非同期）
7. フロントエンドがポーリングで通知を取得し、スナックバー表示

### 通知モジュールの構造
```
undecided.notification/
  internal/
    CommentParticipantSubset.java          # コメント参加者のサブセット
    CommentParticipantSubsetRepository.java
    NotificationRepository.java            # 通知レコードの永続化
    NotificationEntity.java               # JPAエンティティ（NTスキーマ）
    NotificationType.java                 # 区分値（COMMENT_ADDED, COMMENT_DELETED）
    EmailNotificationService.java         # メール送信
    NotificationQueryService.java         # 通知取得
    presentation/
      NotificationQueryController.java    # 通知取得API
  spi/
    NotificationCreated.java              # 通知作成用SPI（内部用）
```

## 変更対象ファイル

### 新規作成
- `backend/build.gradle.kts` — `spring-boot-starter-mail` 依存追加
- `backend/src/main/resources/application.yml` — SMTP設定追加
- `backend/src/main/resources/db/migration/nt/V*__create_notifications_table.sql` — 通知テーブル
- `backend/src/main/java/undecided/notification/internal/NotificationEntity.java`
- `backend/src/main/java/undecided/notification/internal/NotificationRepository.java`
- `backend/src/main/java/undecided/notification/internal/NotificationType.java`
- `backend/src/main/java/undecided/notification/internal/EmailNotificationService.java`
- `backend/src/main/java/undecided/notification/internal/CommentParticipantSubset.java`
- `backend/src/main/java/undecided/notification/internal/CommentParticipantSubsetRepository.java`
- `backend/src/main/java/undecided/notification/internal/BacklogItemCommentEventListener.java`
- `backend/src/main/java/undecided/notification/internal/NotificationQueryService.java`
- `backend/src/main/java/undecided/notification/internal/presentation/NotificationQueryController.java`
- `backend/src/main/java/undecided/notification/internal/presentation/NotificationResponse.java`
- `backend/src/main/java/undecided/notification/package-info.java`
- `backend/src/main/java/undecided/agiloom/requirementBacklog/spi/BacklogItemCommentAdded.java`
- `backend/src/main/java/undecided/agiloom/requirementBacklog/spi/BacklogItemCommentDeleted.java`
- `backend/src/main/java/undecided/agiloom/requirementBacklog/internal/CommentParticipantSubset.java` — 参加者管理
- `backend/src/main/java/undecided/agiloom/requirementBacklog/internal/CommentParticipantSubsetRepository.java`
- `frontend/src/components/NotificationSnackbar.tsx`
- `frontend/src/services/notificationService.ts`
- `frontend/src/hooks/useNotifications.ts`
- `backend/src/test/java/undecided/notification/internal/BacklogItemCommentEventListenerTest.java`
- `backend/src/test/java/undecided/notification/internal/EmailNotificationServiceTest.java`
- `backend/src/test/java/undecided/notification/internal/NotificationQueryServiceTest.java`
- `backend/src/test/java/undecided/agiloom/requirementBacklog/internal/AddBacklogItemCommentCommandServiceTest.java`（イベント発行確認）

### 変更
- `backend/build.gradle.kts` — 依存追加 + Flyway NTロケーション追加
- `backend/src/main/resources/application.yml` — SMTP設定
- `backend/src/main/java/undecided/agiloom/requirementBacklog/internal/AddBacklogItemCommentCommandService.java` — イベント発行追加
- `backend/src/main/java/undecided/agiloom/requirementBacklog/internal/DeleteBacklogItemCommentCommandService.java` — イベント発行追加
- `frontend/src/App.tsx` — NotificationSnackbar統合
- `frontend/src/pages/products/BacklogItemDetail.tsx` — 通知連携

## テストリスト

### 単体テスト（backend）
1. コメントを追加したとき、BacklogItemCommentAddedイベントが発行される
2. コメントを削除したとき、BacklogItemCommentDeletedイベントが発行される
3. イベントを受信したとき、コメント参加者（操作者除く）に通知レコードが作成される
4. コメントが1件のみで操作者が投稿者だったとき、通知レコードは作成されない
5. 複数の参加者がいるとき、操作者以外の全参加者に通知レコードが作成される
6. 通知レコード作成時、isReadはfalseである
7. メール送信サービスが起動したとき、JavaMailSenderが呼び出される
8. メール送信で例外が発生したとき、通知レコード作成は失敗しない（ログ出力のみ）
9. 通知取得APIを呼び出したとき、指定ユーザーの未読通知が返る
10. 通知を既読にしたとき、isReadがtrueになる
11. 未読通知がないとき、空リストが返る
12. コメント追加イベントのとき、NotificationTypeはCOMMENT_ADDEDである
13. コメント削除イベントのとき、NotificationTypeはCOMMENT_DELETEDである

### 統合テスト（backend）
14. コメントを追加したとき、通知レコードがDBに保存される（Testcontainers）
15. コメントを削除したとき、通知レコードがDBに保存される（Testcontainers）
16. モジュール境界が保たれていること（Spring Modulith検証）

### フロントエンドテスト
17. 未読通知があるとき、スナックバーが表示される
18. スナックバーを閉じたとき、通知が既読になる
19. 6秒経過したとき、スナックバーが自動消去される
20. 3件以上の通知があるとき、最大3件まで表示される
21. スナックバーをクリックしたとき、バックログアイテム詳細へ遷移する

## 手順

1. メール送信基盤の導入（依存追加、SMTP設定）
2. 通知モジュール（NT）の新設（パッケージ、エンティティ、リポジトリ、マイグレーション）
3. コメントイベント（BacklogItemCommentAdded/Deleted）の新設
4. requirementBacklogのコメントコマンドサービスにイベント発行を追加
5. 通知モジュールのイベントリスナー実装
6. メール送信サービス実装
7. 通知取得API実装
8. フロントエンドの通知サービス・hook・スナックバーコンポーネント実装
9. テスト実装
10. モジュール境界検証

## リスク

1. **SMTP環境の準備**: 開発環境でSMTPサーバーが必要。TestcontainersのMailpit等を使用するか、localhostのSMTPを前提とする
2. **メール送信の失敗ハンドリング**: メール送信が失敗してもコメント操作自体はロールバックしないこと。`@Async` で分離し、失敗はログ出力に留める
3. **コメント参加者の特定**: 既存の `BacklogItemCommentRepository` は `findByBacklogItemIdOrderByCreatedAtAsc` のみ。参加者IDの重複排除（DISTINCT）が必要
4. **既存Notificationエンティティとの関係**: `sprintExecution.spi.Notification` はAGスキーマに属するが、新設する通知はNTスキーマ。両者は独立し、既存のNotificationはスプリント実行専用として残す
5. **ポーリング負荷**: 30秒間隔のポーリングは負荷が低い。ユーザー数増加時はServer-Sent Events(SSE)への移行を検討
6. **employee_idからメールアドレスの解決**: 通知モジュールはAS（関連付け）領域のEmployeeデータを直接参照できないため、イベントでメールアドレスを含むスナップショットを受け取る、またはコメントにauthor_nameのみを使用してメール本文に名前を記載する


# テスト計画

## テスト戦略

### バリデーションアプローチ
- 単体テスト: Mockitoによる依存モック、各サービスの振る舞い検証
- 統合テスト: Testcontainers（PostgreSQL）によるDB連携テスト
- モジュール検証: Spring Modulith `ApplicationModules.verify()` による境界確認
- フロントエンド: React Testing Libraryによるコンポーネントテスト

### キーシナリオ
1. **コメント追加通知フロー**: コメント追加 → イベント発行 → 通知レコード作成 → メール送信 → API取得 → スナックバー表示
2. **コメント削除通知フロー**: コメント削除 → イベント発行 → 通知レコード作成 → メール送信
3. **操作者除外**: 操作者がコメント参加者の場合、通知されない
4. **参加者なし**: コメントが1件のみで操作者が投稿者 → 通知なし
5. **既読処理**: 通知取得 → 既読更新 → 再取得で未読から除外

### エッジケース
- コメント参加者が0人の場合（通知レコードなし）
- 操作者が唯一の参加者（通知レコードなし）
- メール送信失敗（ログ出力、通知レコードは残る）
- 同一ユーザーが複数コメント（1回のみ通知）
- 大量の参加者（バッチ処理検討）

### テストタグ
- 単体テスト: `@Tag("small")`
- 統合テスト: `@Tag("medium")`
- モジュール検証: `@Tag("large")`


# Delivery Steps

###   Step 1: メール送信基盤と通知モジュールのインフラ構築
spring-boot-starter-mailの依存追加、SMTP設定、NTスキーマのFlywayマイグレーション、通知モジュールの基本パッケージ構造を構築する。
- `backend/build.gradle.kts` に `spring-boot-starter-mail` 依存を追加
- `application.yml` にSMTP設定（spring.mail.*）を追加
- `db/migration/nt/V*__create_notifications_table.sql` を作成（notificationsテーブル）
- `db/migration/nt/V*__create_comment_participants_table.sql` を作成（comment_participantsテーブル）
- `undecided.notification/internal/` パッケージに NotificationEntity, NotificationRepository, NotificationType を新規作成
- `undecided.notification/internal/CommentParticipantSubset` とリポジトリを新規作成
- FlywayのNTタスクがマイグレーションを実行できることを確認

###   Step 2: コメントイベントとrequirementBacklog側のイベント発行
BacklogItemCommentAdded/Deletedイベントを新設し、コメント追加・削除コマンドサービスでイベントを発行する。
- `undecided.agiloom.requirementBacklog/spi/BacklogItemCommentAdded.java` をrecordとして新規作成
- `undecided.agiloom.requirementBacklog/spi/BacklogItemCommentDeleted.java` をrecordとして新規作成
- `AddBacklogItemCommentCommandService.execute()` に `ApplicationEventPublisher.publishEvent()` を追加
- `DeleteBacklogItemCommentCommandService.execute()` に `ApplicationEventPublisher.publishEvent()` を追加
- コメント参加者（CommentParticipantSubset）の管理ロジックを追加・更新
- 既存テストの更新（イベント発行のモック検証）

###   Step 3: 通知モジュールのイベントリスナーとメール送信
通知モジュールがコメントイベントを受信して通知レコードを作成し、メールを送信する。
- `BacklogItemCommentEventListener` を新規作成（@ApplicationModuleListenerでイベント受信）
- イベント受信時にコメント参加者（操作者除く）を取得
- 各参加者に対してNotificationEntityを作成・保存
- `EmailNotificationService` を新規作成（@AsyncでJavaMailSenderを使用）
- 通知作成後にメール送信を非同期で呼び出し
- メール送信失敗はログ出力のみ（通知レコードは残す）
- `NotificationQueryService` と `NotificationQueryController` を新規作成（未読取得・既読更新API）

###   Step 4: フロントエンドの通知スナックバー実装
#658トースト仕様に準拠した通知スナックバーをフロントエンドに実装し、ポーリングで通知を取得・表示する。
- `frontend/src/services/notificationService.ts` を新規作成（API呼び出し）
- `frontend/src/hooks/useNotifications.ts` を新規作成（ポーリング・状態管理）
- `frontend/src/components/NotificationSnackbar.tsx` を新規作成（MUI Snackbar）
- 表示位置: 右下、自動消去: 6秒、同時最大: 3件、フェードアニメーション（#658準拠）
- 通知クリックでバックログアイテム詳細へ遷移
- `App.tsx` に NotificationSnackbar を統合
- useAutoRefresh hook を参照してポーリング間隔を30秒に設定

###   Step 5: テスト実装とモジュール境界検証
計画書のテストリストに従い、単体テスト・統合テスト・フロントエンドテストを実装し、モジュール境界を検証する。
- backend単体テスト: イベント発行、通知作成、メール送信、API取得（13項目）
- backend統合テスト: TestcontainersでDB連携、モジュール境界検証（3項目）
- フロントエンドテスト: スナックバー表示・消去・遷移（5項目）
- `ApplicationModules.verify()` が通ることを確認
- `./gradlew :backend:test` が全件通ることを確認