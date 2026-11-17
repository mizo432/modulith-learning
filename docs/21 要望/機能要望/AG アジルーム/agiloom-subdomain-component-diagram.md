# Agiloom：サブドメイン依存関係コンポーネント図

## 1. 図の目的

本図は、Agiloomのコア・支援・汎用サブドメインと外部サービスの関係を示す論理コンポーネント図です。矢印は主な業務データまたはサービス利用の依存を表します。実際の配置方式や通信プロトコルを固定するものではありません。

## 2. コンポーネント図

```plantuml
@startuml
title Agiloom サブドメイン依存関係
left to right direction
skinparam shadowing false
skinparam componentStyle rectangle
skinparam packageStyle rectangle
skinparam defaultTextAlignment center
skinparam ArrowColor #526581
skinparam ArrowThickness 1.2
skinparam package {
  BorderColor #64748B
  FontColor #0F172A
  BackgroundColor #FFFFFF
}

package "Agiloom コアサブドメイン" #EAF2FF {
  component "プロダクトディスカバリー\nと価値形成" as Discovery #D6E6FF
  component "要求・プロダクト\nバックログ管理" as Backlog #D6E6FF
  component "価値・要求の\n優先順位決定" as Prioritization #D6E6FF
  component "スプリント実行と\nインクリメント形成" as Sprint #D6E6FF
  component "品質・受入・\n性能検証" as Quality #D6E6FF
  component "成果計測・\nフィードバック適応" as Learning #D6E6FF
}

package "Agiloom 支援サブドメイン" #EFF8F1 {
  component "プロジェクト・\nチーム運営設定" as ProjectOps #DDF2E2
  component "ワークフロー・\nテンプレート" as Workflow #DDF2E2
  component "従業員参加・\nステークホルダー" as Participation #DDF2E2
  component "イベント運営・\n改善アクション" as Events #DDF2E2
  component "トラッキング・\nレポート" as Tracking #DDF2E2
  component "ナレッジ・\n意思決定共有" as Knowledge #DDF2E2
  component "横断リスク・\n依存関係調整" as Coordination #DDF2E2
  component "プロジェクト終了・\nアーカイブ" as Closure #DDF2E2
}

package "Agiloom 汎用サブドメイン" #FFF7E8 {
  component "認証・アカウント・\n権限基盤" as Identity #FFE9BF
  component "通知・メール" as Notification #FFE9BF
  component "ファイル・\n添付保管" as Storage #FFE9BF
  component "監査ログ" as Audit #FFE9BF
  component "検索" as Search #FFE9BF
  component "API・Webhook\n連携基盤" as Integration #FFE9BF
  component "バックアップ・\n復元・保持" as Backup #FFE9BF
  component "可観測性・\n運用監視" as Observability #FFE9BF
}

package "利用者・外部サービス" #F3F4F6 {
  actor "PO・SM・開発者" as TeamUsers
  actor "トラッキング担当\n従業員" as TrackUsers
  actor "従業員・\nステークホルダー" as Employees
  component "IdP／認証サービス" as IdP
  component "メール・通知配信" as MailService
  component "ソース管理・CI/CD・\nテスト／性能ツール" as DevTools
  component "監視・ログ基盤" as OpsTools
}

' Core value flow
Discovery --> Backlog : 課題・価値仮説
Backlog --> Prioritization : 要求・根拠
Prioritization --> Sprint : 順序・目標候補
Sprint --> Quality : インクリメント
Quality --> Learning : 検証結果
Learning --> Discovery : 利用成果・学び
Learning --> Backlog : フィードバック
Learning --> Prioritization : 成果・リスク情報

' Supporting subdomains support core workflows
ProjectOps ..> Sprint : チーム・Sprint設定
Workflow ..> Backlog : 項目・状態設定
Participation ..> Discovery : 課題・利用者の声
Participation ..> Quality : レビュー参加
Events ..> Sprint : イベント記録
Events ..> Learning : 振り返り・改善
Tracking ..> Learning : 進捗・成果の可視化
Tracking ..> Prioritization : リスク・判断材料
Knowledge ..> Discovery : 業務知識
Knowledge ..> Sprint : 判断・手順
Coordination ..> Sprint : 障害・依存解消
Coordination ..> Prioritization : 横断リスク
Closure ..> Learning : 成果・学びの保存

' Generic capabilities serve the application domains
Identity ..> ProjectOps : 利用者・所属・権限
Identity ..> Tracking : 閲覧範囲
Notification ..> Participation : 招待・依頼
Notification ..> Events : リマインド
Notification ..> Coordination : 障害・判断通知
Storage ..> Discovery : 調査資料
Storage ..> Quality : テスト証跡
Storage ..> Knowledge : 文書
Search ..> Backlog : 項目検索
Search ..> Knowledge : ナレッジ検索
Integration ..> Quality : 外部テスト結果
Integration ..> Sprint : Issue／Build連携
Audit ..> Identity : 認証・権限操作
Audit ..> ProjectOps : 管理変更
Backup ..> ProjectOps : 設定・業務データ
Observability ..> Integration : 接続・同期状態
Observability ..> ProjectOps : サービス状態

' User access
TeamUsers --> Discovery
TeamUsers --> Backlog
TeamUsers --> Sprint
TeamUsers --> Quality
TrackUsers --> Tracking
Employees --> Participation

' External service adapters
Identity --> IdP
Notification --> MailService
Integration --> DevTools
Observability --> OpsTools

legend bottom
  |= 色 |= 分類 |
  |<#D6E6FF>| コアサブドメイン |
  |<#DDF2E2>| 支援サブドメイン |
  |<#FFE9BF>| 汎用サブドメイン |
  |<#F3F4F6>| 利用者・外部サービス |
  実線矢印：主な業務フロー／利用関係
  破線矢印：支援機能・共通機能の依存
endlegend
@enduml
```

## 3. 依存関係の読み方

### コアの価値フロー

1. **プロダクトディスカバリー**がユーザー課題と価値仮説を明らかにする。
2. **バックログ管理**が課題を実行・検証可能な要求にする。
3. **優先順位決定**が価値、Kano分類、リスク、コストを考慮して順序を決める。
4. **スプリント実行**がチームの計画に基づきインクリメントを形成する。
5. **品質・受入・性能検証**が期待値と品質を確認する。
6. **成果計測・フィードバック適応**が結果を次のディスカバリー、バックログ、優先順位に戻す。

### 支援サブドメイン

プロジェクト設定、テンプレート、従業員参加、イベント記録、トラッキング、ナレッジ、横断調整、終了処理が、コアの業務フローを運用しやすくします。支援機能はコアの業務判断を所有せず、コア側の情報を利用して運用・共有・調整を助けます。

### 汎用サブドメイン

認証、通知、ファイル、監査、検索、API接続、バックアップ、監視は複数の業務領域が共通して利用します。可能な範囲で標準技術や既存サービスを活用し、Agiloom固有の役割・業務ルールはコアまたは支援ドメイン側に残します。

## 4. 設計上のルール

- コアサブドメイン間は、業務上の明確な情報やイベントを介して連携させる。
- 汎用サブドメインをコア業務の判断主体にしない。
- 支援サブドメインからバックログ順序やSprint Backlogを直接変更しない。変更は責任ロールと権限に従う。
- 外部サービス連携はアダプター境界を設け、外部サービス固有のデータ形式をコアモデルへ持ち込まない。
- アクセス制御、監査、バックアップは横断機能として提供し、業務領域ごとの機密性と責任を考慮する。
- この図は論理的な依存関係を表す。物理的なサービス分割は、業務境界と運用要件を検証してから決める。

## 5. 結論

Agiloomの依存関係は、コアの価値フローを中心に、支援サブドメインが運用・調整・可視化を補い、汎用サブドメインが共通基盤を提供する形です。外部サービスは汎用サブドメインの境界越しに接続します。

この構造により、Agiloom固有の価値判断やスクラム開発の流れを保ちながら、認証、通知、保存、監視などの共通機能を交換可能な形で利用できます。
