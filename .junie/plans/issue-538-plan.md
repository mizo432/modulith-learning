# Issue #538 実装計画：フィーチャー追加機能の backend 側（残作業の完了）

## 1. 現状（2026-11-28 実測）

#538 のチェックリストの大半は、親 Issue #537 の実装（コミット eed1a06b7「test: #537」／b6f9f05fd「feat: #537 フィーチャーのサービス層とマイグレーションを実装」）で develop に着地済み。

着地済み:
- spi: Feature、FeatureBacklogItem、FeatureStatus（遷移ルール実装済み）
- internal: 作成・更新・削除・割当・解除のコマンドサービス、FeatureQueryService（進捗計算＝完了SP÷総SP）、FeatureRepository、FeatureBacklogItemRepository
- プレゼンテーション: FeatureCommandController／FeatureQueryController（CQRS分割）、FeatureExceptionHandler、FeatureResponse（進捗・SP合計を含む）
- Flyway: V20261119260000__add_features_table.sql、V20261119270000__add_feature_backlog_items_table.sql
- small テスト: サービス層・コントローラー・区分値・エンティティの各テスト
- 権限制御: チームメンバーのみ作成・編集・削除・割当可（ScrumTeamSubset で判定）
- 未決定事項は Design Issues #660〜#664 で決定済み（サブフィーチャー不可、削除は関連のみ、進捗はSP比、遡及割当可）

未実施:
- 統合テスト（medium テスト）が無い。Feature の medium テストは 0 件（実測）

## 2. 本 Issue の作業範囲

1. Feature の medium テスト（DB連携）を新規作成する
   - 配置: backend/src/test/java/undecided/agiloom/requirementBacklog/internal/FeatureMediumTest.java
   - 形式は同モジュールの既存 medium テスト（BacklogItemSprintAssignmentMediumTest）に倣う: @Tag("medium")、@SpringBootTest、Testcontainers、@Transactional、サービス層経由で実DBへ読み書きする
2. backend small 全体と medium シャード0/1 で回帰を確認する
3. PR を作成し Closes #538 とする

本体コードの変更は行わない（テストで不具合が見つかった場合のみ、その旨を Issue に記録してから最小修正する）。

## 3. テストリスト（medium）

1. フィーチャーを作成したとき、DBに保存され状態はPLANNEDとなる
2. バックログアイテムをフィーチャーに割り当てたとき、関連がDBに保存される
3. 別のフィーチャーに割り当て済みのバックログアイテムを割り当てたとき、例外となり関連は増えない
4. DONEのバックログアイテムのSPが総SPの半分のとき、進捗は50%（完了SP・総SPも一致）となる
5. フィーチャーを削除したとき、関連は削除されバックログアイテムは残る
6. PLANNEDからDONEへの直接遷移をしたとき、例外となり状態は変わらない
7. チームメンバーでないユーザーがフィーチャーを作成したとき、例外となり保存されない

## 4. 検証

- 対象 medium テスト単体実行で全件成功
- backend small 全体: 失敗0（ベースライン 2511件）
- backend medium シャード0/1: 失敗0（ベースライン 223件＋新規7件＝230件）
